//
//  HomeTableViewController.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import UIKit
import CoreData

class HomeTableViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    @IBOutlet weak var tableView: UITableView!
    
    lazy var frc: NSFetchedResultsController<Folder> = self.buildFRC()
    
    var needToScrollTo: IndexPath?
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    var currentColor: Folder.Color?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let c = UserDefaults.standard.integer(forKey: "kColor")
        if c > 0 {
            currentColor = Folder.Color(rawValue: Int16(c))
        }
        
        navigationController?.isNavigationBarHidden = true
        
        setFilter()
        
        NotificationCenter.default.addObserver(self, selector: .colorChanged, name: .colorChanged, object: nil)
        
        navigationController?.interactivePopGestureRecognizer?.delegate = self
    }
    
    func colorChanged(noti: Notification) {
        if let obj = noti.object as? Int16 {
            currentColor = Folder.Color(rawValue: Int16(obj))!
        }else {
            currentColor = nil
        }
        
        tableView.tableHeaderView?.removeAllSubviews()
        setFilter()
        
        self.frc = buildFRC()
        tableView.reloadData()
        
        if frc.fetchedObjects != nil {
            needToScrollTo = IndexPath(row: 0, section: 0)
        }else {
            needToScrollTo = nil
        }
    }
    
    func setFilter(){
        let colors = Folder.existColors()
        let count = CGFloat(colors.count)
        if count == 0 {
            return
        }
        
        let headerView = tableView.tableHeaderView!
        let gap: CGFloat = 0
        let w: CGFloat = 40, y = (headerView.bounds.height-w)*0.5
        let left = (screenW-w*count-(count-1.0)*gap)*0.5
        for (index, colorConfig) in colors.enumerated() {
            
            let btn = UIButton { [unowned self] in
                $0.frame = CGRect(left+(w+gap)*CGFloat(index), y+5, w, w)
                
                let color = colorConfig.uiColor
                $0.setImage(color.image(withSize:CGSize(10, 10), borderColor: headerView.backgroundColor!, borderWidth: 2))
                $0.setImage(color.arcImage(withSize:CGSize(22, 10)), for: .selected)
                $0.tag = Int(colorConfig.name.rawValue)
                $0.addTarget(self, action: #selector(HomeTableViewController.filter(btn:)), for: .touchUpInside)
                $0.isSelected = self.currentColor == colorConfig.name
            }
            
            headerView.addSubview(btn)
        }
    }
    
    func filter(btn: UIButton) {
        let tag = btn.tag
        
        if !btn.isSelected {
            if currentColor != nil && tag != Int(currentColor!.rawValue) {
                let lastBtn = btn.superview!.subview(withTag: Int(currentColor!.rawValue)) as? UIButton
                lastBtn?.isSelected = false
            }
            
            currentColor = Folder.Color(rawValue: Int16(tag))!
            
            UserDefaults.standard.set(tag, forKey: "kColor")
        }else {
            currentColor = nil
            
            UserDefaults.standard.removeObject(forKey: "kColor")
        }
        
        self.frc = buildFRC()
        
        tableView.reloadData()
        
        btn.isSelected = !btn.isSelected
        
        UserDefaults.standard.synchronize()
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        if needToScrollTo != nil && needToScrollTo!.row < (frc.fetchedObjects?.count ?? 0) {
            tableView.scrollToRow(at: needToScrollTo!, at: .bottom, animated: false)
            needToScrollTo = nil
        }
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
    }

    // In a storyboard-based application, you will often want to do a little preparation before navigation
    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "folder_detail0" {
            
        }else if segue.identifier == "folder_detail" {
            let ip = tableView.indexPathForSelectedRow!
            let vc = segue.destination as! ItemsTableViewController
            let newIp = currentColor == nil ? IndexPath(row: ip.row-1, section: ip.section) : ip
            let f = self.frc.object(at: newIp)
            vc.folder = f
            
            let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")!
            ud.set(f.title, forKey:"Current_Folder_Name")
            ud.synchronize()
        }else if segue.identifier == "addFolder" {
            let vc = segue.destination as! EditFolderViewController
            if let c = currentColor {
                vc.currentColor = c.rawValue
            }
        }
    }
 
    override var prefersStatusBarHidden: Bool {
        return false
    }
}

extension Selector {
    static let colorChanged = #selector(HomeTableViewController.colorChanged(noti:))
}

extension Notification.Name {
    static let colorChanged = NSNotification.Name("colorChanged")
}

extension HomeTableViewController {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        let count = self.frc.fetchedObjects?.count ?? 0
        return count + (currentColor == nil ? 1 : 0)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if isAllIndexPath(indexPath) {
            let cell = tableView.dequeueReusableCell(withIdentifier: "Cell0", for: indexPath) as! FolderTableViewCell
            cell.titleLbl.text = "All"
            cell.colorView.backgroundColor = .white
            
            let count = try? cdStore.mainContext.request(Item.self).fetch().count
            cell.countLbl.text = "\(count!) items"
            return cell
        }else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! FolderTableViewCell
            let ip = currentColor == nil ? IndexPath(row: indexPath.row-1, section: indexPath.section) : indexPath
            cell.folder = self.frc.object(at: ip)
            return cell
        }
    }
    
    func isAllIndexPath(_ ip: IndexPath) -> Bool {
        return currentColor == nil && ip.row == 0
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if isAllIndexPath(indexPath) {
            return 90
        }
        
        let ip = currentColor == nil ? IndexPath(row: indexPath.row-1, section: indexPath.section) : indexPath
        
        let folder = self.frc.object(at: ip)
        let title = folder.title!
        
        let h = (title as NSString).boundingRect(with: CGSize(width:screenW-55, height:CGFloat.infinity), options: [.usesLineFragmentOrigin], attributes: [NSFontAttributeName : FolderTableViewCell.titleFont], context: nil).height
        
        return ceil(h) + 90 - 21
    }
}

extension HomeTableViewController {
    
    func buildFRC() -> NSFetchedResultsController<Folder> {
        let request:NSFetchRequest<Folder> = Folder.fetchRequest()
        request.sortDescriptors = [NSSortDescriptor(key: "updatedAt", ascending: false)]
        if let c = currentColor  {
            request.predicate = NSPredicate(format: "color == %d", c.rawValue)
        }
        let frc = NSFetchedResultsController(fetchRequest: request , managedObjectContext: cdStore.mainContext! as! NSManagedObjectContext, sectionNameKeyPath: nil, cacheName: nil)
        frc.delegate = self
        
        try? frc.performFetch()
        
        return frc
    }
}


extension HomeTableViewController : NSFetchedResultsControllerDelegate {
    
    func controllerWillChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.beginUpdates()
    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        
        let ip = (currentColor == nil && indexPath != nil) ? IndexPath(row: indexPath!.row+1, section: indexPath!.section) : indexPath
        let newIp = (currentColor == nil && newIndexPath != nil) ? IndexPath(row: newIndexPath!.row+1, section: newIndexPath!.section) : newIndexPath
        
        switch type {
        case .delete:
            tableView.deleteRows(at: [ip!], with: .fade)
        case .insert:
            tableView.insertRows(at: [newIp!], with: .fade)
            needToScrollTo = newIp
        case .update:
            tableView.reloadRows(at: [ip!], with: .fade)
        case .move:
            tableView.moveRow(at: ip!, to: newIp!)
        }
    }
    
    func controllerDidChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.endUpdates()
    }
}

extension HomeTableViewController {
    override func encodeRestorableState(with coder: NSCoder) {
        coder.encode(tableView.contentOffset, forKey: "contentOffset")
        
        super.encodeRestorableState(with: coder)
    }
    
    override func decodeRestorableState(with coder: NSCoder) {
        let offset = coder.decodeCGPoint(forKey: "contentOffset")
        self.tableView.contentOffset = offset
        
        super.decodeRestorableState(with: coder)
    }
}

extension HomeTableViewController: UIGestureRecognizerDelegate {
    
    func gestureRecognizerShouldBegin(_ gestureRecognizer: UIGestureRecognizer) -> Bool {
        return navigationController!.viewControllers.count > 1
    }
}


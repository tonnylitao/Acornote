//
//  ItemLargeTableViewController.swift
//  Acornote
//
//  Created by Tonny on 29/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import SafariServices
import AVFoundation

class ItemLargeTableViewController: UIViewController, UITableViewDelegate, UITableViewDataSource {

    var item: Item?
    
    @IBOutlet weak var tableView: UITableView!
    
    //
    @IBOutlet weak var audioBtn: UIButton!
    @IBOutlet weak var linkBtn: UIButton!
    @IBOutlet weak var tagBtn: UIButton!
    
    override func viewDidLoad() {
        super.viewDidLoad()

        updateBtns()
        
        NotificationCenter.default.addObserver(self, selector: .itemChanged, name: .itemChanged, object: nil)
    }
    
    func itemChanged(noti: Notification) {
        tableView.reloadData()
    }
    
    func updateBtns() {
        audioBtn.isHidden = item?.folder?.playable == false
        
        linkBtn.isHidden = item?.url == nil
        
        tagBtn.isSelected = item?.taged == true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    // MARK: - Table view data source

    
    
    @IBAction func sound(_ sender: AnyObject) {
        guard let item = self.item else {
            return
        }
        
        if speecher.isSpeaking {
            speecher.stopSpeaking(at: .immediate)
        }
        
        if item.fliped == false {
            let utterence = AVSpeechUtterance(string: item.title!.replacingOccurrences(of: "\n", with: ", "))
            utterence.voice = AVSpeechSynthesisVoice(language: item.title!.voiceLanguage)
            
            speecher.speak(utterence)
        }else {
            if let des = item.des {
                let utterence = AVSpeechUtterance(string: des.replacingOccurrences(of: "\n", with: ", "))
                utterence.voice = AVSpeechSynthesisVoice(language: des.voiceLanguage)
                
                speecher.speak(utterence)
            }
        }
    }
    
    @IBAction func link(_ sender: AnyObject) {
        guard let path = self.item?.url, let url = URL(string: path) else {
            return
        }
        
        let svc = storyboard!.instantiateViewController(withIdentifier:"WebViewController") as! WebViewController
        svc.url = url
        svc.folder = self.item?.folder
        navigationController?.pushViewController(svc, animated: true)
    }
    
    @IBAction func bing(_ sender: AnyObject) {
        guard let item = self.item, let title = item.title, let path = "https://www.bing.com/images/search?q=\(title)&FORM=HDRSC2".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed), let url = URL(string: path) else {
            return
        }
        
        let svc = storyboard!.instantiateViewController(withIdentifier:"WebViewController") as! WebViewController
        svc.url = url
        svc.item = item
        navigationController?.pushViewController(svc, animated: true)
    }
    
    @IBAction func edit(_ sender: AnyObject) {
        //        pageVC.
        
        let sb:UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let vc = sb.instantiateViewController(withIdentifier:"EditItemViewController") as! EditItemViewController
        
        vc.folder = self.item?.folder
        vc.item = self.item
        
        let nav = UIApplication.shared.keyWindow?.rootViewController as? UINavigationController
        nav?.present(vc, animated: true, completion: nil)
    }
    
    @IBAction func tag(_ sender: UIButton) {
        item?.update(update: { (obj) in
            let item = obj as! Item
            item.taged = !item.taged
        }, callback: nil)
        
        sender.isSelected = !sender.isSelected
    }
}

extension Selector {
    static let itemChanged = #selector(ItemLargeTableViewController.itemChanged(noti:))
}

extension Notification.Name {
    static let itemChanged = NSNotification.Name("itemChanged")
}

extension ItemLargeTableViewController {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return item == nil ? 0 : 1
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "Cell", for: indexPath) as! ItemLargeTableViewCell
        
        cell.item = item!
        
        return cell
    }
    
    static let titleFont = UIFont(name: "Arial-BoldMT", size: 40) ?? UIFont.systemFont(ofSize: 40)
    static let desFont = UIFont(name: "Arial", size: 20) ?? UIFont.systemFont(ofSize: 20)
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        return item?.largeCellHeight ?? 0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if item?.folder?.quizlet == true {
            let cell = tableView.cellForRow(at: indexPath) as! ItemLargeTableViewCell
            
            if cell.item.fliped == false {
                cell.titleTxtView.isHidden = true
                cell.desTxtView.superview?.isHidden = false
            }else {
                cell.titleTxtView.isHidden = false
                cell.desTxtView.superview?.isHidden = true
            }
            UIView.transition(with: cell, duration: 0.3, options: [.transitionFlipFromLeft, .overrideInheritedOptions], animations: {
                
            }, completion: { (_) in
                cell.item.fliped = !cell.item.fliped
            })
        }
    }
}

extension ItemLargeTableViewController {
    override func encodeRestorableState(with coder: NSCoder) {
        debugPrint("encode large")
        super.encodeRestorableState(with: coder)
        
        coder.encode(item!.folder!.title, forKey: "folderTitle")
        coder.encode(item!.title, forKey: "itemTitle")
        coder.encode(item!.des, forKey: "itemDes")
    }
    
    override func decodeRestorableState(with coder: NSCoder) {
        debugPrint("decode large")
        //important: viewDidLoad before than decodeRestorableState, so frc, folder should be optional
        guard let title = coder.decodeObject(forKey: "folderTitle") as? String,
            let itemTitle = coder.decodeObject(forKey: "itemTitle") as? String,
            let itemDes = coder.decodeObject(forKey: "itemDes") as? String else {
                return
        }
        
        let pre = NSPredicate(format: "title == %@", title)
        if let folder = try! cdStore.mainContext.request(Folder.self).filtered(with: pre).fetch().first {
            let pre1 = NSPredicate(format: "folder == %@ AND title == %@ AND des = %@", folder, itemTitle, itemDes)
            
            if let item = try! cdStore.mainContext.request(Item.self).filtered(with: pre1).fetch().first {
                self.item = item
                
                self.tableView.reloadData()
                updateBtns()
            }
        }
        
        super.decodeRestorableState(with: coder)
    }
    
}

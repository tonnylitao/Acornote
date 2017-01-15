//
//  ItemsTableViewController.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import CoreData
import SugarRecord
import SafariServices
import AVFoundation

class ItemsTableViewController: UIViewController, UIPageViewControllerDataSource {
    
    @IBOutlet weak var lineView: UIView!
    
    var folder: Folder?
    lazy var frc: NSFetchedResultsController<Item>? = self.setupFrc(taged: false)
    
    @IBOutlet weak var tableView: UITableView!
    
    var selectTag: Bool = false
    
    var needToScrollTo: IndexPath?
    
    var quizletMode: Bool = UserDefaults.standard.bool(forKey: "kQuizletAll")
    
    // pageVC
    var pageVC: UIPageViewController?
    var item: Item?
    
    
    @IBOutlet weak var playRightCons: NSLayoutConstraint!
    @IBOutlet weak var linkRightCons: NSLayoutConstraint!
    @IBOutlet weak var flipRightCons: NSLayoutConstraint!
    
    @IBOutlet weak var audioBtn: UIButton!
    @IBOutlet weak var linkBtn: UIButton!
    @IBOutlet weak var quizletBtn: UIButton!
    @IBOutlet weak var moreBtn: UIButton!
    
    @IBOutlet weak var addBtn: UIButton!
    
    @IBOutlet weak var tableBottomCons: NSLayoutConstraint!
    
    var speechingLbl: UITextView?
    var speechingText: String?
    
    
    var quizletBtnTintColor: UIColor {
        if folder == nil {
            return quizletMode ? .appGreen : .white
        }else {
            return quizletMode ? (Folder.ColorConfig.color(withId: folder!.color)?.uiColor ?? .appGreen) : .white
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        var moveRightBtnCount:CGFloat = 0
        var playRightBtnCount:CGFloat = 0
        var linkRightBtnCount:CGFloat = 0
        var flipRightBtnCount:CGFloat = 0
        
        if folder != nil {
            quizletMode = folder?.quizlet == true
            
            moveRightBtnCount = 1
            playRightBtnCount = 1
            linkRightBtnCount = 1
            flipRightBtnCount = 1
        }else {
            addBtn.removeFromSuperview()
            addBtn = nil
        }
        
        if self.folder?.flipable == true {
            let img = UIImage(named: "icon_flip_on")!.withRenderingMode(.alwaysTemplate)
            quizletBtn?.setImage(img, for: .normal)
            quizletBtn?.tintColor = quizletBtnTintColor
            
            moveRightBtnCount += 1
            playRightBtnCount += 1
            linkRightBtnCount += 1
        }else {
            quizletBtn?.removeFromSuperview()
            quizletBtn = nil
        }
        
        if self.folder?.url != nil {
            moveRightBtnCount += 1
            playRightBtnCount += 1
        }else {
            linkBtn.removeFromSuperview()
            linkBtn = nil
        }
        
        if folder?.playable == true {
            moveRightBtnCount += 1
        }else {
            audioBtn.removeFromSuperview()
            audioBtn = nil
        }
        
        playRightCons?.constant = (44.0+10)*CGFloat(playRightBtnCount)+5
        linkRightCons?.constant = (44.0+10)*CGFloat(linkRightBtnCount)+5
        flipRightCons?.constant = (44.0+10)*CGFloat(flipRightBtnCount)+5
        
        setupSegment()
        
        if quizletMode {
            var page = 0
//            let count = frc?.fetchedObjects?.count ?? 0
            if let f = folder, let l = f.lastVisited {
                let pre = NSPredicate(format: "folder == %@ AND title == %@", f, l)
                if let item = Item.findOne(cdStore.mainContext, predicate: pre) {
                    let i = frc?.fetchedObjects?.index(of: item)
                    if i != nil && i != NSNotFound {
                        page = i!
                    }
                }
            }
//            debugPrint("page", page)
            
//            if count > 0 {
//                lineViewWCons.constant = min(CGFloat(max(page, 1))/CGFloat(count)*screenW, screenW)
//            }else {
//                lineViewWCons.constant = screenW
//            }
            pageVC = ItemsTableViewController.createPageVC(vc: self, page)
            tableView.isHidden = true
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(ItemsTableViewController.setupFrcNotification(noti:)), name: .tagChanged, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(ItemsTableViewController.imgPathChanged(noti:)), name: Notification.Name(rawValue:"ImgPathChanged"), object: nil)
        
        if folder != nil {
            let refresh = UIRefreshControl(frame: CGRect(0, 0, 20, 20))
            refresh.addTarget(self, action: #selector(ItemsTableViewController.refresh(sender:)), for: .valueChanged)
            tableView.refreshControl = refresh
            
            if let id = folder?.color {
                lineView.backgroundColor = Folder.ColorConfig.color(withId: id)?.uiColor
            }
        }else {
            lineView.backgroundColor = .appNav
        }
        
    }
    
    func pasteboardChanged(noti: NSNotification) {
        if let id = folder?.objectID, let text = UIPasteboard.general.string?.trimmingCharacters(in: .whitespacesAndNewlines), !text.isEmpty {
            
            try? cdStore.operation({[weak self] (ctx, save) -> Void in
                guard let _ = self, let folder = (ctx as! NSManagedObjectContext).object(with: id) as? Folder else {
                    return
                }
                
                let pre = NSPredicate(format: "folder == %@ AND title == %@", folder, text)
                if Item.findOne(ctx, predicate: pre) != nil {
                    return
                }
                
                //TODO 排列组合 components 长的在前
                let pre1 = NSPredicate(format: "folder == %@ AND title in %@", folder, text.components(separatedBy: .whitespaces))
                if let item = Item.findOne(ctx, predicate: pre1) {
                    if let des = item.des {
                        item.des = des + "\n" + text
                    }else {
                        item.des = text
                    }
                }else {
                    let item: Item = try! ctx.create()
                    item.title = text
                    
                    item.createdAt = NSDate()
                    item.folder = folder
                    folder.updatedAt = NSDate()
                }
                
                save()
                
                DispatchQueue.main.async {
                    NotificationCenter.default.post(name: NSNotification.Name("hightlightChanged"), object: text)
                }
            })
        }
    }
    
    func refresh(sender: UIRefreshControl) {
        
        let vc = storyboard!.instantiateViewController(withIdentifier:"EditItemViewController") as! EditItemViewController
        vc.folder = folder
        present(vc, animated: true, completion: {
            sender.endRefreshing()
        })
    }
    
    @IBAction func dismiss(_ sender: AnyObject) {
        player?.stop()
        player = nil
        task?.cancel()
        task = nil
        
        NotificationCenter.default.removeObserver(self)
        
        _ = navigationController?.popViewController(animated: true)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        navigationController?.setNavigationBarHidden(true, animated: animated)
        
        if needToScrollTo != nil {
            tableView.scrollToRow(at: needToScrollTo!, at: .bottom, animated: false)
            needToScrollTo = nil
        }
        
        //to fix bug: copy cut in edit-item vc
        NotificationCenter.default.removeObserver(self, name: .UIPasteboardChanged, object: nil)
    }
    
    func createHeaderView() {
        let view: UIView = { [unowned self] in
            let view = UIView(frame: CGRect(x: 0, y: 0, width: screenW, height: 50))
            
            let left:CGFloat = 60
            let frame = CGRect(x: left, y: 10, width: screenW-left*2, height: 30)
            let c = self.folder?.highlightColor ?? UIColor.appGreen
            let segment = UISegmentedControl.segmentedControl(frame: frame,  color: c, titles: "Untaged", "Taged")
            segment.addTarget(self, action: #selector(ItemsTableViewController.chose(seg:)), for: .valueChanged)
            
            view.addSubview(segment)
            
            return view
        }()
        
        self.tableView.tableHeaderView = view
    }
    
    
    @IBAction func more(_ sender: Any) {
        let action = UIAlertController(title: nil, message: nil, preferredStyle: .actionSheet)
        action.addAction(UIAlertAction(title: "Move items", style: .default, handler: {[unowned self] (_) in
            self.startMove()
        }))
        //TODO: reorder
//        action.addAction(UIAlertAction(title: "Reorder items", style: .default, handler: { (_) in
//        }))
        action.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (_) in
            
        }))
        present(action, animated: true)
    }
    
    //TODO: bug: canot select last cell
    func startMove() {
        audioBtn?.isHidden = true
        linkBtn?.isHidden = true
        quizletBtn?.isHidden = true
        moreBtn.isHidden = true
        
        addBtn?.isHidden = true
        
        let cancelBtn = UIButton {
            $0.frame = CGRect(screenW-70, 20, 60, 44)
            $0.setTitle("Cancel", for: .normal)
            $0.addTarget(self, action: #selector(ItemsTableViewController.cancelMove(btn:)), for: .touchUpInside)
        }
        moreBtn.superview?.addSubview(cancelBtn)
        
        //
        tableView.allowsMultipleSelectionDuringEditing = true
        tableView.isEditing = true
        
        tableView.visibleCells.forEach { $0.selectionStyle = .default }
        
        let btn = UIButton { [unowned self] in
            $0.frame = CGRect(0, screenH-44, screenW, 44)
            $0.setTitle("Move", for: .normal)
            $0.tag = 100
            $0.backgroundColor = self.lineView.backgroundColor
            $0.setTitleColor(.white, for: .normal)
            $0.addTarget(self, action: #selector(ItemsTableViewController.moveItems), for: .touchUpInside)
        }
        
        tableView.contentInset = UIEdgeInsets(top: 0, left: 0, bottom: 44, right: 0)
        
        view.addSubview(btn)
    }
    
    func cancelMove(btn: UIButton) {
        tableView.visibleCells.forEach { $0.selectionStyle = .none }
        
        tableView.allowsMultipleSelectionDuringEditing = false
        tableView.isEditing = false
        
        view.subview(withTag: 100)?.removeFromSuperview()
        
        audioBtn?.isHidden = false
        linkBtn?.isHidden = false
        quizletBtn?.isHidden = false
        moreBtn.isHidden = false
        
        addBtn?.isHidden = false
        
        btn.removeFromSuperview()
        
        tableView.contentInset = .zero
    }
    
    func moveItems() {
        guard let arr = tableView.indexPathsForSelectedRows else {
            return
        }
        if arr.count == 0 {
            return
        }
        
        let objs = arr.map { frc!.object(at: $0) }
        let folders: [Folder]!
        if folder == nil {
            folders = try! cdStore.saveContext.request(Folder.self).fetch()
        }else {
            folders = try! cdStore.saveContext.request(Folder.self).filtered(with: NSPredicate(format: "SELF != %@", folder!)).fetch()
        }
    
        let action = UIAlertController(title: "Move into", message: nil, preferredStyle: .alert)
        folders.forEach { folder in
            action.addAction(UIAlertAction(title: folder.title, style: .default, handler: { (_) in
                
                try? cdStore.operation({ (ctx, save) -> Void in
                    guard let folder = try? ctx.request(Folder.self).filtered(with: NSPredicate(format:"SELF == %@", folder)).fetch().first else {
                        return
                    }
                    let items = try? ctx.request(Item.self).filtered(with: NSPredicate(format: "SELF IN %@", objs)).fetch()
                    
                    items?.forEach { $0.folder = folder }
                    
                    save()
                })
            }))
        }
        action.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (_) in
            
        }))
        present(action, animated: true)
        
    }
    
    var player: AVAudioPlayer?
    var task: URLSessionTask?
    
    @IBAction func play(_ sender: UIButton) {
        
        if let audioUrl = folder?.audioUrl{
            if speecher.isSpeaking {
                speecher.stopSpeaking(at: .immediate)
            }
            
            let title = self.folder!.title!
            
            if !sender.isSelected {
                if let p = player {
                    p.play()
                }else {
                    task?.cancel()
                    task = Folder.audio(title: title, url: audioUrl, completion: { [weak self] data in
                        guard let d = data, let _ = self else {
                            return
                        }
                        
                        self?.player = try? AVAudioPlayer(data: d)
                        try? AVAudioSession.sharedInstance().setCategory(AVAudioSessionCategoryPlayback)
                        self?.player?.prepareToPlay()
                        self?.player?.play()
                    })
                    task?.resume()
                }
            }else {
                player?.pause()
            }
            
            sender.isSelected = !sender.isSelected
            return
        }
        
        //siri
        
        if speecher.isSpeaking {
            sender.isSelected = false
            speecher.stopSpeaking(at: .immediate)
            return
        }
        
        sender.isSelected = true
        
        speecher.delegate = self
        
        var from = 0
        if !quizletMode {
            from = tableView.indexPathsForVisibleRows?.first?.row ?? 0
        }
        let arr = frc?.fetchedObjects
        for i in from..<(arr?.count ?? 0) {
            let item = arr![i]
            let utterence = AVSpeechUtterance(string: item.title!.replacingOccurrences(of: "\n", with: ", "))
            utterence.voice = AVSpeechSynthesisVoice(language: item.title!.voiceLanguage)
            
            speecher.speak(utterence)
            
            if let des = item.des {
                let utterence = AVSpeechUtterance(string: des.replacingOccurrences(of: "\n", with: ", "))
                utterence.voice = AVSpeechSynthesisVoice(language: des.voiceLanguage)
                
                speecher.speak(utterence)
            }
        }
    }
    
    
    @IBAction func showLink(_ sender: AnyObject) {
        if let url = folder?.url {
            if folder != nil {
                NotificationCenter.default.addObserver(self, selector: #selector(ItemsTableViewController.pasteboardChanged(noti:)), name: NSNotification.Name.UIPasteboardChanged, object: nil)
            }
            
            _ = WebViewController.show(withUrl: url, folder: folder)
        }
    }
    
    func chose(seg: UISegmentedControl) {
        self.frc = self.setupFrc(taged: seg.selectedSegmentIndex == 1)
        self.tableView.reloadData()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }

    override func prepare(for segue: UIStoryboardSegue, sender: Any?) {
        if segue.identifier == "addItem" {
            let vc = segue.destination as! EditItemViewController
            vc.folder = self.folder
        }
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    
}

extension ItemsTableViewController: AVSpeechSynthesizerDelegate {
    
    func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didStart utterance: AVSpeechUtterance) {
        let string = utterance.speechString
        
        self.speechingText = string
        
        var cell = tableView.visibleCells.first { (cell) -> Bool in
            return (cell as! ItemTableViewCell).titleTxtView.text?.replacingOccurrences(of: "\n", with: ", ") == string
        }
        
        if cell == nil {
            //TODO scroll to visible
            return
        }
        
        cell = tableView.visibleCells.first { (cell) -> Bool in
            return (cell as! ItemTableViewCell).titleTxtView.text?.replacingOccurrences(of: "\n", with: ", ") == string
        }
        
        self.speechingLbl?.textColor = ItemTableViewCell.titleColor
        self.speechingLbl = (cell as? ItemTableViewCell)?.titleTxtView
        self.speechingLbl?.textColor = lineView.backgroundColor
    }
    
    //TODO: 无法知道 总结束
    func speechSynthesizer(_ synthesizer: AVSpeechSynthesizer, didFinish utterance: AVSpeechUtterance) {
        let string = utterance.speechString
        
        self.speechingText = nil
        
        let cellWithTitle = tableView.visibleCells.first { (cell) -> Bool in
            return (cell as! ItemTableViewCell).titleTxtView.text?.replacingOccurrences(of: "\n", with: ", ") == string
        }
        
        if cellWithTitle != nil && (cellWithTitle as? ItemTableViewCell)?.desTxtView.text == nil {
            if cellWithTitle != nil {
                self.speechingLbl?.textColor = ItemTableViewCell.titleColor
                self.speechingLbl = nil
            }
            
            return
        }
        
        //
        let cellWithDes = tableView.visibleCells.first { (cell) -> Bool in
            let text = (cell as! ItemTableViewCell).desTxtView.attributedText?.string.replacingOccurrences(of: "\n", with: ", ")
            
            return text == string
        }
        
        if cellWithDes != nil {
            self.speechingLbl?.textColor = ItemTableViewCell.titleColor
            self.speechingLbl = nil
        }
    }
}

extension ItemsTableViewController {
    class func createPageVC(vc: ItemsTableViewController, _ index: Int) -> UIPageViewController {
        let pageVC = UIPageViewController(transitionStyle: .scroll, navigationOrientation: .horizontal, options: nil)
        
        let items = vc.frc?.fetchedObjects
        let item = items?[index]
        vc.item = item
        
        pageVC.dataSource = vc
        
        vc.addChildViewController(pageVC)
        let pageView = pageVC.view!
        
        pageView.backgroundColor = .appBackground
        pageView.translatesAutoresizingMaskIntoConstraints = false
        pageView.frame = CGRect(x: 0, y:69, width: screenW, height: screenH-69)
        vc.view.addSubview(pageView)
        
        pageView.clipsToBounds = false
        
        pageVC.didMove(toParentViewController: vc)
        
        if let items = items, let item = vc.item {
            let page = items.index(of: item)!
            if let firstController = vc.getItemController(page, items) {
                pageVC.setViewControllers([firstController], direction: .forward, animated: false, completion: nil)
            }
        }
        
        pageVC.restorationIdentifier = "UIPageViewController"
//        pageVC.restorationClass = ItemsTableViewController.self
        
        return pageVC
    }
    
    @IBAction func quizletOrNot(_ sender: UIButton) {
        let to = !quizletMode
        
        guard let index = tableView.indexPathsForVisibleRows?.first?.row else {
            return
        }
        
        quizletMode = to
        if to {
            pageVC = ItemsTableViewController.createPageVC(vc: self, index)
            
            tableView.isHidden = true
            
            sender.tintColor = quizletBtnTintColor
        }else {
            self.item = nil
            
            tableView.isHidden = false
            
            self.pageVC?.view.removeFromSuperview()
            self.pageVC?.removeFromParentViewController()
            self.pageVC = nil
            
            sender.tintColor = .white
        }
        
        folder?.update(update: {(obj) in
            let folder = obj as! Folder
            folder.quizlet = !folder.quizlet
        }, callback: nil)
        
        if folder == nil {
            UserDefaults.standard.set(quizletMode, forKey: "kQuizletAll")
            UserDefaults.standard.synchronize()
        }
    }
    
    func getItemController(_ page: Int, _ items: [Item]) -> UIViewController? {
        if page >= 0 && page < items.count {
            
            let pageItemController = self.storyboard!.instantiateViewController(withIdentifier: "ItemLargeTableViewController") as! ItemLargeTableViewController
            
            let item = items[page]
            pageItemController.item = item
            
            folder?.lastVisited = item.title
            
//            debugPrint("page 0", frc?.fetchedObjects?.index(of: item))
            
            pageItemController.view.frame = CGRect(x: 0, y:69, width: screenW, height: screenH-69)
            
            //
//            if let count = frc?.fetchedObjects?.count {
//                UIView.animate(withDuration: 0.3, animations: { 
//                    self.lineViewWCons.constant = min(CGFloat(max(page, 1))/CGFloat(count)*screenW, screenW)
//                })
//            }
            
            return pageItemController
        }
        
        return nil
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerBefore viewController: UIViewController) -> UIViewController?{
        guard let items = self.frc?.fetchedObjects else {
            return nil
        }
        let itemVC = viewController as! ItemLargeTableViewController
        guard let item = itemVC.item else {
            return nil
        }
        
        let index = items.index(of: item)!
        let vc = getItemController(index-1, items)
        
        return vc
    }
    
    func pageViewController(_ pageViewController: UIPageViewController, viewControllerAfter viewController: UIViewController) -> UIViewController? {
        guard let items = self.frc?.fetchedObjects else {
            return nil
        }
        
        let itemVC = viewController as! ItemLargeTableViewController
        guard let item = itemVC.item else {
            return nil
        }
        
        let index = items.index(of: item)!
        let vc = getItemController(index+1, items)
        return vc
    }
}

extension ItemsTableViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return self.frc?.fetchedObjects?.count ?? 0
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let iden = ItemTableViewCellIdentifier.identifierWith(folder: folder)
        let cell = tableView.dequeueReusableCell(withIdentifier: iden, for: indexPath) as! ItemTableViewCell
        
        let item = self.frc?.object(at: indexPath)
        cell.item = item
        
        //TODO : speechingText 没有起作用
        if self.speechingText != nil &&
            self.speechingText == item?.title!.replacingOccurrences(of: "\n", with: ", ") &&
            self.speechingText == item?.des?.replacingOccurrences(of: "\n", with: ", ") {
            cell.titleTxtView.textColor = lineView.backgroundColor
        }
        
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let item = self.frc?.object(at: indexPath)
        let h = item?.height ?? 0
        
        if folder == nil {
            return h + 20
        }
        
        return h
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        if tableView.isEditing {
            return
        }
        
        if let item = self.frc?.fetchedObjects?[indexPath.row], item.folder?.playable == true {
            
            if speecher.isSpeaking {
               speecher.stopSpeaking(at: .immediate)
            }
            
            speecher.delegate = self
            
            let title = item.title!.replacingOccurrences(of: "\n", with: ", ")
            let utterence = AVSpeechUtterance(string: title)
            utterence.voice = AVSpeechSynthesisVoice(language: item.title!.voiceLanguage)
            
            speecher.speak(utterence)
            
            if let des = item.des {
                let utterence = AVSpeechUtterance(string: des.replacingOccurrences(of: "\n", with: ", "))
                utterence.voice = AVSpeechSynthesisVoice(language: des.voiceLanguage)
                
                speecher.speak(utterence)
            }
        }
    }
    
    func tableView(_ tableView: UITableView, canMoveRowAt indexPath: IndexPath) -> Bool {
        return false
    }
    
    func tableView(_ tableView: UITableView, moveRowAt sourceIndexPath: IndexPath, to destinationIndexPath: IndexPath) {
//        let from = frc?.object(at: sourceIndexPath)
//        let to = frc?.object(at: destinationIndexPath)
        
    }
    
    func tableView(_ tableView: UITableView, editingStyleForRowAt indexPath: IndexPath) -> UITableViewCellEditingStyle {
        return .none
    }
    
}

extension ItemsTableViewController {
    
    func setupSegment() {
        var pre: NSPredicate?
        if let f = folder {
            pre = NSPredicate(format: "folder == %@ AND taged == true", f)
        }else if folder == nil {
            pre = NSPredicate(format: "taged == true")
        }
        
        guard let p = pre else {
            return
        }
        
        try? cdStore.operation { (context, save) throws -> Void in
            if Item.findOne(context, predicate: p) != nil {
                if self.tableView.tableHeaderView == nil {
                    self.createHeaderView()
                }
            }else {
                self.tableView.tableHeaderView = nil
            }
        }
    }
    
    func imgPathChanged(noti: NSNotification) {
        guard let _ = noti.object as? String else {
            return
        }
        
        tableView.reloadData()
    }
    
    func setupFrcNotification(noti: NSNotification?) {
        setupSegment()
    }
    
    func setupFrc(taged: Bool) -> NSFetchedResultsController<Item>? {
        if folder == nil {
            self.selectTag = taged
            
            let request:NSFetchRequest<Item> = Item.fetchRequest()
            request.predicate = NSPredicate(format: "taged == %@", taged as CVarArg)
            
            request.sortDescriptors = [NSSortDescriptor(key: "createdAt", ascending: false)] //降序
            let frc = NSFetchedResultsController(fetchRequest: request , managedObjectContext: cdStore.mainContext! as! NSManagedObjectContext, sectionNameKeyPath: nil, cacheName: nil)
            frc.delegate = self
            
            try? frc.performFetch()
            
            return frc
        }else {
            guard let folder = self.folder else {
                return nil
            }
            
            self.selectTag = taged
            
            let request:NSFetchRequest<Item> = Item.fetchRequest()
            request.predicate = NSPredicate(format: "folder == %@ AND taged == %@", folder, taged as CVarArg)
            
            request.sortDescriptors = [NSSortDescriptor(key: "createdAt", ascending: true)]
            let frc = NSFetchedResultsController(fetchRequest: request , managedObjectContext: cdStore.mainContext! as! NSManagedObjectContext, sectionNameKeyPath: nil, cacheName: nil)
            frc.delegate = self
            
            try? frc.performFetch()
            
            return frc
        }
    }
}

extension ItemsTableViewController: NSFetchedResultsControllerDelegate {
    func controllerWillChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.beginUpdates()
    }
    
    func controller(_ controller: NSFetchedResultsController<NSFetchRequestResult>, didChange anObject: Any, at indexPath: IndexPath?, for type: NSFetchedResultsChangeType, newIndexPath: IndexPath?) {
        
        switch type {
        case .delete:
            tableView.deleteRows(at: [indexPath!], with: .fade)
        case .insert:
            tableView.insertRows(at: [newIndexPath!], with: .fade)
            needToScrollTo = newIndexPath
        case .update:
            tableView.reloadRows(at: [indexPath!], with: .fade)
        case .move:
            tableView.moveRow(at: indexPath!, to: newIndexPath!)
        }
    }
    
    func controllerDidChangeContent(_ controller: NSFetchedResultsController<NSFetchRequestResult>) {
        tableView.endUpdates()
    }
}

extension NSNotification.Name {
    static let tagChanged = NSNotification.Name("tagChanged")
}

extension ItemsTableViewController {
    override func encodeRestorableState(with coder: NSCoder) {
        
        debugPrint("encode items")
        coder.encode(folder!.title, forKey: "folderTitle")
        coder.encode(selectTag, forKey: "selectTag")
        coder.encode(tableView.contentOffset, forKey: "contentOffset")
        
        super.encodeRestorableState(with: coder)
    }
    
    override func decodeRestorableState(with coder: NSCoder) {
        debugPrint("decode items")
        //important: viewDidLoad before than decodeRestorableState, so frc, folder should be optional
        if let title = coder.decodeObject(forKey: "folderTitle") as? String {
            self.selectTag = coder.decodeBool(forKey: "selectTag")
            
            let pre = NSPredicate(format: "title == %@", title)
            self.folder = try! cdStore.mainContext.request(Folder.self).filtered(with: pre).fetch().first
            self.frc = self.setupFrc(taged: self.selectTag)
            self.tableView.reloadData()
            
            let offset = coder.decodeCGPoint(forKey: "contentOffset")
            self.tableView.contentOffset = offset
        }
        super.decodeRestorableState(with: coder)
    }
    
    
    
}

//extension ItemPageViewController {
//    class func viewController(withRestorationIdentifierPath identifierComponents: [Any], coder: NSCoder) -> UIViewController? {
//        return createPageVC()
//    }
//    
//    override func encodeRestorableState(with coder: NSCoder) {
//        debugPrint("encode pages")
//        super.encodeRestorableState(with: coder)
//        
//        coder.encode(item!.folder!.title, forKey: "folderTitle")
//        coder.encode(item!.title, forKey: "itemTitle")
//        coder.encode(item!.des, forKey: "itemDes")
//    }
//    
//    override func decodeRestorableState(with coder: NSCoder) {
//        debugPrint("decode pages")
//        //important: viewDidLoad before than decodeRestorableState, so frc, folder should be optional
//        guard let title = coder.decodeObject(forKey: "folderTitle") as? String,
//            let itemTitle = coder.decodeObject(forKey: "itemTitle") as? String,
//            let itemDes = coder.decodeObject(forKey: "itemDes") as? String else {
//                return
//        }
//        
//        let pre = NSPredicate(format: "title == %@", title)
//        if let folder = try! cdStore.mainContext.request(Folder.self).filtered(with: pre).fetch().first {
//            let pre1 = NSPredicate(format: "folder == %@ AND title == %@ AND des = %@", folder, itemTitle, itemDes)
//            
//            if let item = try! cdStore.mainContext.request(Item.self).filtered(with: pre1).fetch().first {
//                self.item = item
//                
//                let pre2 = NSPredicate(format: "folder == %@ AND taged == %@", folder, item.taged as CVarArg)
//                self.items = try! cdStore.mainContext.request(Item.self).filtered(with: pre2).fetch()
//                
//                if let items = self.items, let item = self.item {
//                    let page = items.index(of: item)!
//                    if let firstController = getItemController(page, items) {
//                        pageVC.setViewControllers([firstController], direction: .forward, animated: false, completion: nil)
//                    }
//                }
//            }
//        }
//        
//        super.decodeRestorableState(with: coder)
//    }
//}
//

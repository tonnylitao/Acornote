//
//  SearchTableViewController.swift
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

class SearchTableViewController: UIViewController {
    
    var frc: NSFetchedResultsController<Item>?
    
    @IBOutlet weak var tableView: UITableView!
    
    //
    var speechingLbl: UITextView?
    var speechingText: String?
    
    @IBOutlet weak var textField: UITextField!
    
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        textField.becomeFirstResponder()
    }
    
    @IBAction func dismiss(_ sender: AnyObject) {
        player?.stop()
        player = nil
        task?.cancel()
        task = nil
        
        _ = navigationController?.popViewController(animated: false)
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        navigationController?.setNavigationBarHidden(true, animated: animated)
    }
    
    var player: AVAudioPlayer?
    var task: URLSessionTask?
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }

}

extension SearchTableViewController: UITextFieldDelegate {
    
    @IBAction func changed(_ sender: UITextField) {
        frc = setupFrcWith(target: sender.text)
        tableView.reloadData()
    }
    
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        
        textField.resignFirstResponder()
        
        frc = setupFrcWith(target: textField.text)
        tableView.reloadData()
        
        return true
    }
}

extension SearchTableViewController: AVSpeechSynthesizerDelegate {
    
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
        self.speechingLbl?.textColor = (cell as? ItemTableViewCell)?.item.folder?.highlightColor
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

extension SearchTableViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return self.frc?.fetchedObjects?.count ?? 0
    }
    
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let iden = ItemTableViewCellIdentifier.identifierWith(folder: nil)
        let cell = tableView.dequeueReusableCell(withIdentifier: iden, for: indexPath) as! ItemTableViewCell
        
        let item = self.frc?.object(at: indexPath)
        cell.item = item
        
        //TODO : speechingText 没有起作用
        if self.speechingText != nil &&
            self.speechingText == item?.title!.replacingOccurrences(of: "\n", with: ", ") &&
            self.speechingText == item?.des?.replacingOccurrences(of: "\n", with: ", ") {
            cell.titleTxtView.textColor = item?.folder?.highlightColor
        }
        
        return cell
    }
    
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        let item = self.frc?.object(at: indexPath)
        let h = item?.height ?? 0
        
        return h + 20
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

extension SearchTableViewController {
    
    func setupFrcWith(target: String?) -> NSFetchedResultsController<Item>? {
        if target == nil {
            return nil
        }
        
        let request:NSFetchRequest<Item> = Item.fetchRequest()
        request.predicate = NSPredicate(format: "title CONTAINS[cd] %@", target!)
        
        request.sortDescriptors = [NSSortDescriptor(key: "createdAt", ascending: false)] //降序
        let frc = NSFetchedResultsController(fetchRequest: request , managedObjectContext: cdStore.mainContext! as! NSManagedObjectContext, sectionNameKeyPath: nil, cacheName: nil)
        
        try? frc.performFetch()
        
        return frc
    }
}

extension SearchTableViewController {
    
    override func encodeRestorableState(with coder: NSCoder) {
//        
//        debugPrint("encode items")
//        coder.encode(folder!.title, forKey: "folderTitle")
//        coder.encode(selectTag, forKey: "selectTag")
//        coder.encode(tableView.contentOffset, forKey: "contentOffset")
//        
        super.encodeRestorableState(with: coder)
    }
    
    override func decodeRestorableState(with coder: NSCoder) {
        debugPrint("decode items")
//        //important: viewDidLoad before than decodeRestorableState, so frc, folder should be optional
//        if let title = coder.decodeObject(forKey: "folderTitle") as? String {
//            self.selectTag = coder.decodeBool(forKey: "selectTag")
//            
//            let pre = NSPredicate(format: "title == %@", title)
//            self.folder = try! cdStore.mainContext.request(Folder.self).filtered(with: pre).fetch().first
//            self.frc = self.setupFrc(taged: self.selectTag)
//            self.tableView.reloadData()
//            
//            let offset = coder.decodeCGPoint(forKey: "contentOffset")
//            self.tableView.contentOffset = offset
//        }
        super.decodeRestorableState(with: coder)
    }
}

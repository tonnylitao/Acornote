//
//  EditItemViewController.swift
//  Acornote
//
//  Created by Tonny on 28/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import CoreData
import SwiftyJSON
import SafariServices
import Cache

class EditItemViewController: UIViewController {
    
    @IBOutlet weak var titleLbl: UILabel!
    
    @IBOutlet weak var titleTxtView: UITextView!
    
    @IBOutlet weak var imgBtn: UIButton!
    
    @IBOutlet weak var desTxtView: UITextView!
    
    @IBOutlet weak var linkBtn: UIButton!
    
    @IBOutlet weak var cardHCons: NSLayoutConstraint!
    
    @IBOutlet weak var transBtn: UIButton!
    
    
    var imgTask: URLSessionDataTask?
    var task: URLSessionDataTask?
    
    var folder: Folder!
    var item: Item? {
        didSet {
            self.data["title"] = self.item?.title
            self.data["des"] = self.item?.des
            self.data["url"] = self.item?.url
            self.data["imgPath"] = self.item?.imgPath
            
            self.titleLbl?.text = self.item == nil ? "Add Item" : "Edit Item"
        }
    }
    var data = [String: String]()

    override func viewDidLoad() {
        super.viewDidLoad()

        setup()
        
        NotificationCenter.default.addObserver(self, selector: #selector(EditItemViewController.keyboardWillShow(noti:)), name: .UIKeyboardWillShow, object: nil)
        
        view.backgroundColor = Folder.ColorConfig.color(withId: folder.color)?.uiColor ?? .appGreen
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        titleTxtView.becomeFirstResponder()
    }
    
    func showNewItem(_ item: Item?) {
        data.removeAll()
        self.item = item
        
        transBtn.isSelected = false
        transBtn.isEnabled = true
        transBtn.hideIndicator(nil)
        transBtn.setImage(UIImage(named:"icon_translate")!)
        
        task?.cancel()
        
        setup()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func dismiss(_ sender: AnyObject) {
        titleTxtView.resignFirstResponder()
        desTxtView.resignFirstResponder()
        
        NotificationCenter.default.removeObserver(self)
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func sure(_ sender: AnyObject) {
        guard let title = data["title"], !title.isEmpty else {
            return
        }
        
        createOrModifyItem()
        dismiss(sender)
    }
    
    func createOrModifyItem() {
        let title = data["title"]!
        
        let id = self.folder.objectID
        let itemId = self.item?.objectID
        let dataTemp = self.data
        try? cdStore.operation { (ctx, save) -> Void in
            let folder = (ctx as! NSManagedObjectContext).object(with: id) as? Folder
            
            let lastItem: Item!
            if itemId == nil {
                let item: Item = try! ctx.create()
                item.createdAt = NSDate()
                
                lastItem = item
                
                folder?.updatedAt = NSDate()
            }else {
                lastItem = (ctx as! NSManagedObjectContext).object(with: itemId!) as? Item
            }
            lastItem?.title = title
            lastItem?.des = dataTemp["des"]
            lastItem?.url = dataTemp["url"]
            lastItem?.imgPath = dataTemp["imgPath"]
            
            lastItem?.folder = folder
            
            save()
            
            DispatchQueue.main.async {
                NotificationCenter.default.post(name: Notification.Name("itemChanged"), object: nil)
            }
        }
    }
    
    override var prefersStatusBarHidden: Bool {
        return true
    }
    
    func setup() {
        titleTxtView.text = data["title"]
        desTxtView.text = data["des"]
        
        titleTxtView.becomeFirstResponder()
        
        if let imgUrl = data["imgPath"] {
            cache?.async.object(ofType: ImageWrapper.self, forKey: imgUrl, completion: { [weak self] result in
                if case .value(let wrapper) = result {
                    DispatchQueue.main.async {
                        self?.imgBtn.setImage(wrapper.image)
                    }
                }
            })
        }else {
            imgBtn.setImage(UIImage(named: "icon_image"))
        }
        
        //TODO: imgBtn
    }
}

//MARK: Notification

extension EditItemViewController {
    @objc func keyboardWillShow(noti: NSNotification) {
        let rect = noti.userInfo?[UIKeyboardFrameEndUserInfoKey] as? CGRect
        cardHCons.constant = screenH-64-50-44-10-(rect?.height ?? 271)
    }
}

extension EditItemViewController : UITextViewDelegate {
    func textViewDidChange(_ textView: UITextView) {
        let text = textView.text.trimmingCharacters(in: .whitespacesAndNewlines)
        
        if textView == titleTxtView {
            data["title"] = text
        }else {
            data["des"] = text
        }
    }
    
    func textView(_ textView: UITextView, shouldChangeTextIn range: NSRange, replacementText text: String) -> Bool {
        if textView == titleTxtView && text == "\n" {
            desTxtView.becomeFirstResponder()
            return false
        }
        return true
    }
}

//MARK: Action

extension EditItemViewController {
    
    @IBAction func preview(_ sender: Any) {
        if let i = item {
            let index = folder.items!.index(of: i)
            if index < folder.items!.count-1 {
                let item = folder.items!.object(at: index+1) as? Item
                
                if let title = data["title"], !title.isEmpty {
                    createOrModifyItem()
                }
                showNewItem(item)
            }
        }else if let item = folder.items?.lastObject as? Item {
            if let title = data["title"], !title.isEmpty {
                createOrModifyItem()
            }
            showNewItem(item)
        }
    }
    
    @IBAction func addLink(_ sender: AnyObject) {
        EditFolderViewController.addLink(vc: self, data: self.data, audio: false) { [unowned self] (text, audio) in
            self.data["url"] = text
        }
    }
    
    @IBAction func chosePhoto(_ sender: AnyObject) {
        guard let title = self.titleTxtView.text?.trimmingCharacters(in: .whitespacesAndNewlines) else {
            return
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(EditItemViewController.imgPathChanged(noti:)), name: Notification.Name(rawValue:"ImgPathChanged"), object: nil)
            
        let path = "https://www.google.com/search?site=&tbm=isch&source=hp&q=\(title.replacingOccurrences(of: " ", with: "+"))" //&FORM=HDRSC2&qft=+filterui:aspect-square
        let vc = WebViewController.show(withUrl: path, folder:folder, vc:self)
        vc?.isForImage = true
        vc?.item = self.item
    }
    
    @objc func imgPathChanged(noti: Notification) {
        guard let url = noti.object as? String else {
            return
        }
            
        self.data["imgPath"] = url
        
        cache?.async.object(ofType: ImageWrapper.self, forKey: url, completion: { [weak self] result in
            if case .value(let wrapper) = result {
                DispatchQueue.main.async {
                    self?.imgBtn.setImage(wrapper.image)
                }
            }
        })
    }
    
    
    @IBAction func switchTitleAndDes(_ sender: AnyObject) {
        let title = self.data["title"]
        let des = self.data["des"]
        
        self.data["des"] = title
        self.data["title"] = des
        
        titleTxtView.text = des
        desTxtView.text = title
        
        transBtn.isSelected = false
        transBtn.isEnabled = true
        transBtn.hideIndicator(nil)
        transBtn.setImage(UIImage(named:"icon_translate")!)
        
        task?.cancel()
    }
    
    @IBAction func transOrClear(_ sender: UIButton) {
        if !sender.isSelected {
            let text = titleTxtView.text.trimmingCharacters(in: .whitespacesAndNewlines)
            
            sender.isEnabled = false
            sender.setImage(nil)
            sender.showIndicator()
            loadTrans(text) {[weak self] result in
                sender.isEnabled = true
                sender.setImage(UIImage(named:"icon_translate")!)
                sender.hideIndicator(nil)
                
                guard let strongSelf = self else {
                    return
                }
                
                sender.isSelected = true
                
                if result != nil {
                    strongSelf.desTxtView.text = result
                    strongSelf.data["des"] = result
                }
            }
        }else {
            desTxtView.text = nil
            data["des"] = nil
            
            sender.isSelected = false
            
            sender.setImage(UIImage(named:"icon_translate")!)
        }
    }
    
    private func loadTrans(_ text: String, completion: @escaping ((String?)->Void)) {
        task?.cancel()
        
        let request = URLRequest(url: URL(string: "https://glosbe.com/gapi/translate?from=en&dest=zh&format=json&phrase=\(text.lowercased())&pretty=true".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed)!)!)
        task = URLSession.shared.dataTask(with: request) { (data, response, error) -> Void in
            guard let d = data else {
                DispatchQueue.main.async {
                    completion(nil)
                }
                return
            }
            
            if let obj = try? JSON(data: d) {
                var result = obj["tuc"][0]["phrase"]["text"].string // ?? obj["tuc"][0]["meanings"][0]["text"].string {
                if let result1 = obj["tuc"][1]["phrase"]["text"].string {
                    result! += "，\(result1)"
                }
                if let result2 = obj["tuc"][2]["phrase"]["text"].string {
                    result! += "，\(result2)"
                }
                
                DispatchQueue.main.async {
                    completion(result)
                }
            }else {
                DispatchQueue.main.async {
                    completion("")
                }
            }
        }
        task?.resume()
    }
    
    @IBAction func next(_ sender: Any) {
        if let i = item {
            let index = folder.items!.index(of: i)
            if index > 0 {
                let item = folder.items!.object(at: index-1) as? Item
                
                if let title = data["title"], !title.isEmpty {
                    createOrModifyItem()
                }
                showNewItem(item)
            }
        }else if let item = folder.items?.firstObject as? Item {
            
            if let title = data["title"], !title.isEmpty {
                createOrModifyItem()
            }
            showNewItem(item)
        }
    }
    
    @IBAction func addAnother(_ sender: AnyObject) {
        guard let title = data["title"], !title.isEmpty else {
            return
        }
        
        createOrModifyItem()
        showNewItem(nil)
    }
}

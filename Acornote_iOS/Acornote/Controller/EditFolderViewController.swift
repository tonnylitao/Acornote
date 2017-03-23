//
//  EditFolderViewController.swift
//  Acornote
//
//  Created by Tonny on 28/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import CoreData

class EditFolderViewController: UIViewController {

    @IBOutlet weak var colorStackView: UIStackView!
    
    @IBOutlet weak var textField: UITextField!
    
    @IBOutlet weak var linkBtn: UIButton!
    @IBOutlet weak var audioBtn: UIButton!
    @IBOutlet weak var flipBtn: UIButton!
    @IBOutlet weak var tagBtn: UIButton!
    
    @IBOutlet weak var btnBtmCons: NSLayoutConstraint!
    
    var data:[String: Any] = ["playable": false, "flipable": false, "tagable": false, "color": Folder.ColorConfig.defalut]
    var folder: Folder? {
        didSet {
            if let dic = folder?.dic {
                for (key, value) in dic where value != nil {
                    data.updateValue(value!, forKey: key)
                }
            }
        }
    }
    
    var currentColor: Int16  = Folder.ColorConfig.defalut {
        didSet {
            data["color"] = currentColor
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        textField.becomeFirstResponder()
        textField.text = data["title"] as? String
        textField.attributedPlaceholder = NSAttributedString(string: "Folder Name", attributes: [NSForegroundColorAttributeName: UIColor.white])
        
        linkBtn.isSelected = data["url"] != nil
        audioBtn.isSelected = data["playable"] as? Bool ?? false
        flipBtn.isSelected = data["flipable"] as? Bool ?? false
        tagBtn.isSelected = data["tagable"] as? Bool ?? false
        

        NotificationCenter.default.addObserver(self, selector: #selector(EditFolderViewController.textChanged(noti:)), name: .UITextFieldTextDidChange, object: textField)
        
        //
        let w = 40
        let targets = Folder.ColorConfig.all()
    
        for (index, config) in targets.enumerated() {
            let btn = UIButton(frame: CGRect(0, 0, w, w))
            
            let color = UIColor.rgb(config.rgb.0, config.rgb.1, config.rgb.2)
            btn.setImage(color.image(withSize:CGSize(10, 10), borderColor: color, borderWidth: 0))
            btn.setImage(color.arcImage(withSize:CGSize(22, 10), borderColor: .white, borderWidth: 2), for: .selected)
            btn.tag = index+1
            btn.addTarget(self, action: #selector(EditFolderViewController.choseColor(btn:)), for: .touchUpInside)
            btn.isSelected = data["color"] as? Int16 == config.name.rawValue
            
            if btn.isSelected {
                currentColor = Int16(btn.tag)
            }
            
            colorStackView.addArrangedSubview(btn)
        }
        
        //
        NotificationCenter.default.addObserver(self, selector: #selector(EditFolderViewController.keyboardWillShow(noti:)), name: .UIKeyboardWillShow, object: nil)
        
        view.backgroundColor = Folder.ColorConfig.color(withId: currentColor)?.uiColor ?? .appGreen
    }
    
    override var prefersStatusBarHidden: Bool {
        return true
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
}

//MARK: UITextFieldDelegate 

extension EditFolderViewController: UITextFieldDelegate {
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        return true
    }
}


//MARK: Action

extension EditFolderViewController {
    
    func choseColor(btn: UIButton) {
        if btn.isSelected {
            return
        }
        
        btn.isSelected = !btn.isSelected
        
        let lastBtn = btn.superview!.subview(withTag: Int(currentColor)) as! UIButton
        lastBtn.isSelected = false
        
        currentColor = Int16(btn.tag)
        
        data["color"] = Int16(btn.tag)
        
        view.backgroundColor = Folder.ColorConfig.color(withId: Int16(btn.tag))?.uiColor ?? .appGreen
    }
    
    @IBAction func dismiss(_ sender: AnyObject) {
        textField.resignFirstResponder()
        
        NotificationCenter.default.removeObserver(self)
        dismiss(animated: true, completion: nil)
    }
    
    @IBAction func sure(_ sender: AnyObject) {
        guard let title = data["title"] as? String, !title.isEmpty else {
            return
        }
        
        let id = self.folder?.objectID
        let dataTemp = self.data
        try? cdStore.operation { (ctx, save) -> Void in
            let lastItem: Folder!
            if id == nil {
                let item: Folder = try! ctx.create()
                lastItem = item
                lastItem?.createdAt = NSDate()
                lastItem?.updatedAt = NSDate()
            }else {
                lastItem = (ctx as! NSManagedObjectContext).object(with: id!) as? Folder
            }
            lastItem?.title = title
            lastItem?.url = dataTemp["url"] as? String
            lastItem?.audioUrl = dataTemp["audioUrl"] as? String
            
            let newColor = dataTemp["color"] as? Int16 ?? Folder.ColorConfig.defalut
            
            let needUpdate = lastItem?.color != newColor
            lastItem?.color = newColor
            
            lastItem?.playable = dataTemp["playable"] as? Bool ?? false
            lastItem?.flipable = dataTemp["flipable"] as? Bool ?? false
            lastItem?.tagable = dataTemp["tagable"] as? Bool ?? false
            
            save()
            
            let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")!
            ud.set(lastItem.title, forKey:"Current_Folder_Name")
            ud.synchronize()
            
            if needUpdate {
                DispatchQueue.main.async {
                    NotificationCenter.default.post(name: .colorChanged, object: newColor)
                }
            }
        }
        
        self.dismiss(sender)
    }
    
    @IBAction func link(_ sender: AnyObject) {
        EditFolderViewController.addLink(vc: self, data: self.data) { [unowned self] (text, audio) in
            self.data["url"] = text
            self.data["audioUrl"] = audio
            
            self.linkBtn.isSelected = text != nil
        }
    }
    
    class func addLink(vc:UIViewController, data: [String: Any]?, audio: Bool = true, completion: ((String?, String?) -> Void)?){
        let action = UIAlertController(title: "Input url", message: nil, preferredStyle: .alert)
        action.addTextField {(field) in
            field.placeholder = "https://"
            field.keyboardType = .URL
        }
        if audio {
            action.addTextField {(field) in
                field.placeholder = "audio url"
                field.keyboardType = .URL
            }
        }
        action.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (_) in
            
        }))
        action.addAction(UIAlertAction(title: "Sure", style: .default, handler: {[unowned action] (actionC) in
            let urlField = action.textFields?.first
            var url = urlField?.text?.trimmingCharacters(in: .whitespacesAndNewlines)
            
            if url?.hasPrefix("https://") == false && url?.hasPrefix("http://") == false {
                url = nil
            }
            
            var audio = action.textFields?.last?.text?.trimmingCharacters(in: .whitespacesAndNewlines)
            if audio?.hasPrefix("https://") == false && url?.hasPrefix("http://") == false {
                audio = nil
            }
            
            completion?(url, audio)
        }))
        
        vc.present(action, animated: true, completion: {[unowned action] in
            //fix bug: setText in 'action.addTextField {(field)' did not work
            if let field = action.textFields?.first, data != nil {
                field.text = data?["url"] as? String
                
                field.clearsOnBeginEditing = true
                field.borderStyle = .none
            }
            
            if let field = action.textFields?.last, data != nil, action.textFields!.count > 1 {
                field.text = data?["audioUrl"] as? String
                
                field.clearsOnBeginEditing = true
                field.borderStyle = .none
            }
        })
    }
    
    @IBAction func audio(_ sender: UIButton) {
        let has = data["playable"] as? Bool ?? false
        data["playable"] = !has
        
        audioBtn.isSelected = !audioBtn.isSelected
    }
    
    @IBAction func flip(_ sender: UIButton) {
        let has = data["flipable"] as? Bool ?? false
        data["flipable"] = !has
        
        sender.isSelected = !sender.isSelected
    }
    
    @IBAction func tag(_ sender: UIButton) {
        let has = data["tagable"] as? Bool ?? false
        data["tagable"] = !has
        
        sender.isSelected = !sender.isSelected
    }
}

//MARK: Notification

extension EditFolderViewController {
    func keyboardWillShow(noti: NSNotification) {
        let rect = noti.userInfo?[UIKeyboardFrameEndUserInfoKey] as? CGRect
        btnBtmCons.constant = 30+(rect?.height ?? 271)
    }
    
    func textChanged(noti: NSNotification) {
        if noti.object as? UITextField == textField {
            data["title"] = textField.text?.trimmingCharacters(in: .whitespacesAndNewlines)
        }
    }
}

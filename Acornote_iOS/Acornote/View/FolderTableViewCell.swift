//
//  FolderTableViewCell.swift
//  Acornote
//
//  Created by Tonny on 30/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import MGSwipeTableCell

class FolderTableViewCell: MGSwipeTableCell {
    
    @IBOutlet weak var titleLbl: UILabel!
    
    @IBOutlet weak var countLbl: UILabel!
    
    @IBOutlet weak var linkBtn: UIButton!
    
    @IBOutlet weak var colorView: UIView!
    
    
    static let titleFont = UIFont(name: "Arial-BoldMT", size: 18) ?? UIFont.systemFont(ofSize: 18)
    
    var folder: Folder! {
        didSet {
            titleLbl.text = folder.title
            titleLbl.font = FolderTableViewCell.titleFont
            
            countLbl.text = "\(folder.items?.count ?? 0) items"
            
            if let c = Folder.ColorConfig.color(withId: folder!.color) {
                colorView.backgroundColor = c.uiColor
            }else {
                colorView.backgroundColor = .white
            }
            
            linkBtn.isHidden = folder?.url == nil
        }
    }

    override func awakeFromNib() {
        super.awakeFromNib()
        
        if self.reuseIdentifier == "Cell" { //not all
            let padding = 14
            let img = UIImage(named: "icon_add")!.withRenderingMode(.alwaysTemplate)
            let add = MGSwipeButton(title: "", icon: img, backgroundColor: .clear, padding: padding) {[unowned self] cell -> Bool in
                
                let sb:UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                let vc = sb.instantiateViewController(withIdentifier:"EditItemViewController") as! EditItemViewController
                
                vc.folder = self.folder
                
                let nav = UIApplication.shared.keyWindow?.rootViewController as? UINavigationController
                nav?.present(vc, animated: true, completion: nil)
                
                return true
            }
            add.tintColor = .rgb(192, 194, 191)
            
            let edit = MGSwipeButton(title: "", icon: UIImage(named:"icon_edit"), backgroundColor: .clear, padding: padding) {[unowned self] cell -> Bool in
                
                let sb:UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
                let vc = sb.instantiateViewController(withIdentifier:"EditFolderViewController") as! EditFolderViewController
                
                vc.folder = self.folder
                
                let nav = UIApplication.shared.keyWindow?.rootViewController as? UINavigationController
                nav?.present(vc, animated: true, completion: nil)
                
                return true
            }
            let remove = MGSwipeButton(title: "", icon: UIImage(named:"icon_delete"), backgroundColor: .clear, padding: padding) {[unowned self] cell -> Bool in
                
                let pre = NSPredicate(format: "color == %d", self.folder.color)
                let count = try! cdStore.mainContext.request(Folder.self).filtered(with: pre).fetch().count
                let needUpdate = count == 1
                
                //
                let action = UIAlertController(title: "Delete Folder?", message: nil, preferredStyle: .alert)
                action.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (_) in
                    
                }))
                action.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: {[unowned self] (actionC) in
                    self.folder.remove( {
                        if needUpdate {
                            NotificationCenter.default.post(name: NSNotification.Name("colorChanged"), object: nil)
                        }
                    })
                }))
                let nav = UIApplication.shared.keyWindow?.rootViewController as? UINavigationController
                nav?.present(action, animated: true, completion: nil)
                
                return true
            }
            
            rightButtons = [remove, edit, add]
            rightSwipeSettings.transition = .static
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
    @IBAction func showLink(_ sender: Any) {
        guard let f = folder else {
            return
        }
        
        _ = WebViewController.show(withUrl: f.url, folder: f)
    }
    
}

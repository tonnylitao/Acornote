//
//  ItemTableViewCell.swift
//  Acornote
//
//  Created by Tonny on 28/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import MGSwipeTableCell
import SafariServices
import Cache

class ItemTableViewCell: MGSwipeTableCell {
    
    @IBOutlet weak var titleTxtView: TextView!
    @IBOutlet weak var desTxtView: TextView!
    @IBOutlet weak var imgView: UIImageView!
    
    @IBOutlet weak var linkBtn: UIButton!
    
    //
    @IBOutlet weak var titleTopCons: NSLayoutConstraint!
    @IBOutlet weak var titleRightCons: NSLayoutConstraint!

    //
    let padding = 14
    var bingBtn: MGSwipeButton?
    var editBtn: MGSwipeButton?
    var removeBtn: MGSwipeButton?
    
    //for folder info
    @IBOutlet weak var folderColorView: UIView!
    @IBOutlet weak var folderNameLbl: TextView!
    
    
    lazy var tagBtn: MGSwipeButton? = {
        let tagBtn = MGSwipeButton(title: "", icon: UIImage(named:"icon_flag_off"), backgroundColor: .clear, padding: self.padding) {[unowned self] cell -> Bool in
            
            self.item?.update(update: { (obj) in
                let item = obj as? Item
                item?.taged = !(item?.taged ?? false)
            }, callback: {
                NotificationCenter.default.post(name: .tagChanged, object: nil, userInfo: nil)
            })
            
            return true
        }
        tagBtn.setImage(UIImage(named:"icon_flag_on"), for: .selected)
        return tagBtn
    }()

    
    //
    static let titleColor = UIColor.rgb(74, 74, 74)
    
    static let titleFont = UIFont(name: "Arial-BoldMT", size: 20) ?? UIFont.systemFont(ofSize: 20)
    static let desFont = UIFont(name: "Arial", size: 14) ?? UIFont.systemFont(ofSize: 14)
    override func awakeFromNib() {
        super.awakeFromNib()
        
        bingBtn = MGSwipeButton(title: "", icon: UIImage(named:"icon_bing"), backgroundColor: .clear, padding: padding) {[unowned self] cell -> Bool in
            
            //https://cn.bing.com/dict/search?q=\(title)&FORM=HDRSC2
            guard let title = self.item.title, let path = "https://www.bing.com/images/search?q=\(title)&FORM=HDRSC2".addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed), let url = URL(string: path) else {
                return false
            }
            
            let sb:UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
            let svc = sb.instantiateViewController(withIdentifier:"WebViewController") as! WebViewController
            svc.url = url
            svc.item = self.item
            let vc = UIApplication.shared.keyWindow?.rootViewController as? UINavigationController
            vc?.pushViewController(svc, animated: true)
            
            return true
        }
        
        editBtn = MGSwipeButton(title: "", icon: UIImage(named:"icon_edit"), backgroundColor: .clear, padding: padding) {[unowned self] cell -> Bool in
            
            let sb:UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
            let vc = sb.instantiateViewController(withIdentifier:"EditItemViewController") as! EditItemViewController
            
            vc.folder = self.item.folder
            vc.item = self.item
            
            let nav = UIApplication.shared.keyWindow?.rootViewController as? UINavigationController
            nav?.present(vc, animated: true, completion: nil)
            
            return true
        }
        removeBtn = MGSwipeButton(title: "", icon: UIImage(named:"icon_delete"), backgroundColor: .clear, padding: padding) {[unowned self] cell -> Bool in
            
            //
            let action = UIAlertController(title: "Delete Item?", message: nil, preferredStyle: .alert)
            action.addAction(UIAlertAction(title: "Cancel", style: .cancel, handler: { (_) in
                
            }))
            action.addAction(UIAlertAction(title: "Delete", style: .destructive, handler: {[unowned self] (actionC) in
                self.item?.remove(nil)
            }))
            let nav = UIApplication.shared.keyWindow?.rootViewController as? UINavigationController
            nav?.present(action, animated: true, completion: nil)

            return true
        }
        
        rightSwipeSettings.transition = .static
    }
    
    var imgTask: URLSessionDataTask?
    
    var item: Item! {
        didSet {
            titleTxtView.attributedText = item.titleAttibutedString
            
            if let _ = item.des {
                desTxtView.attributedText = item.desAttributedString
                desTxtView.isHidden = false
            }else{
                desTxtView.attributedText = nil
                desTxtView.isHidden = true
            }
            
            if let imgUrl = self.item.imgPath {
                imgView.isHidden = false
                imgView.image = nil
                
                let title = self.item.title
                cache.object(imgUrl, completion: {[weak self] (img:UIImage?) in
                    if let img = img {
                        DispatchQueue.main.async {
                            if self?.item?.title == title {
                                self?.imgView.image = img
                            }
                        }
                    }else if let url = URL(string: imgUrl) {
                        self?.imgTask?.cancel()
                        var request = URLRequest(url: url)
                        request.addValue("image/*", forHTTPHeaderField: "Accept")
                        self?.imgTask = URLSession.shared.dataTask(with: request) {[weak self] (data, response, error) -> Void in
                            if let d = data, let img = UIImage(data: d){
                                cache.add(imgUrl, object: d, completion: {
                                    if self?.item?.title == title {
                                        DispatchQueue.main.async {
                                            self?.imgView.image = img
                                        }
                                    }
                                })
                            }
                        }
                        
                        self?.imgTask?.resume()
                    }
                })
            }else {
                imgView.isHidden = true
            }
            
            linkBtn.isHidden = self.item?.url == nil
            
            //update constraint
            
            let titleCons = item!.cuculateTitleCons
            titleTopCons.constant = titleCons.top
            titleRightCons.constant = titleCons.right
            
            if self.item?.folder?.tagable == true {
                tagBtn?.isSelected = self.item?.taged ?? false
                
                rightButtons = [removeBtn!, editBtn!, tagBtn!, bingBtn!]
            }else {
                rightButtons = [removeBtn!, editBtn!, bingBtn!]
            }
            
            //
            folderColorView?.backgroundColor = item.folder?.highlightColor
            folderNameLbl?.text = item.folder?.title
            
//            titleTxtView.backgroundColor = .blue
//            desTxtView.backgroundColor = .red
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        titleTxtView.selectedRange = NSMakeRange(0,0)
        desTxtView.selectedRange = NSMakeRange(0,0)
    }

    @IBAction func showLink(_ sender: Any) {
        _ = WebViewController.show(withUrl: item.url, item:item)
    }
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        titleTxtView.textColor = ItemTableViewCell.titleColor
    }
}


extension NSAttributedString {
    
    func height(size: CGSize) -> CGFloat {
        return ceil(self.boundingRect(with: size, options: [.usesLineFragmentOrigin, .usesFontLeading], context: nil).height)
    }
}

extension Item {
    
    var titleAttibutedString: NSAttributedString {
        let style:NSMutableParagraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
        style.lineSpacing = 2
        let att = NSAttributedString(string: title!, attributes: [NSFontAttributeName : ItemTableViewCell.titleFont, NSParagraphStyleAttributeName:style, NSForegroundColorAttributeName: ItemTableViewCell.titleColor])
        
        return att
    }
    
    var desAttributedString: NSAttributedString? {
        guard let d = des else {
            return nil
        }
        
        let style:NSMutableParagraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
        style.lineSpacing = 2
        let att = NSMutableAttributedString(string: d, attributes: [NSFontAttributeName : ItemTableViewCell.desFont, NSForegroundColorAttributeName:UIColor.gray, NSParagraphStyleAttributeName:style])
        
        let ranges = (d.lowercased() as NSString).allRange(of: title!.lowercased())
        ranges.forEach {
            att.addAttributes([NSForegroundColorAttributeName:folder?.highlightColor ?? UIColor.appGreen], range: $0)
        }
        
        return att
    }
    
    //for caculate height
    var simpleDesAttributedString: NSAttributedString? {
        guard let d = des else {
            return nil
        }
        
        let style = NSMutableParagraphStyle()
        style.lineSpacing = 2
        let att = NSAttributedString(string: d, attributes: [NSFontAttributeName : ItemTableViewCell.desFont, NSParagraphStyleAttributeName:style])

        return att
    }
    
    var cuculateTitleCons: (top:CGFloat, right:CGFloat, height:CGFloat) {
        let hasImg = imgPath != nil
        
        let top: CGFloat = hasImg ? 25 : 20
        let right: CGFloat = hasImg ? 70 : 20
        
        //
        let w = screenW-20-right
        let size = CGSize(width:w, height:CGFloat.infinity)
        
        return (top, right, titleAttibutedString.height(size: size))
    }
    
    var smallCellHeight: CGFloat {
        
        let titleCons = self.cuculateTitleCons
        let tTop = titleCons.top
        var height: CGFloat = tTop
        
        height += cuculateTitleCons.height
        
        if let simple = self.simpleDesAttributedString {
            height += 5.0 //gap
            
            height += simple.height(size: CGSize(width:screenW-20-20, height:CGFloat.infinity))
        }
        
        height += tTop //card bottom
        
        
        return ceil(height)
    }
}

extension NSString {
    func allRange(of string: String) -> [NSRange] {
        return self.allRange(of: string, origin: self)
    }
    
    func allRange(of string: String, origin: NSString, index: Int = 0) -> [NSRange] {
        if self.length == 0 {
            return []
        }
        
        var first = self.range(of: string)
        if first.location == NSNotFound {
            return []
        }else {
            let newIndex = index+first.location+first.length
            let sufix = origin.substring(from: newIndex)
            
            first.location += index
            return [first] + sufix.allRange(of: string, origin: origin, index: newIndex)
        }
    }
}

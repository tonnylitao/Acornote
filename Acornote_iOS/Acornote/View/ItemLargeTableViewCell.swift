//
//  ItemLargeTableViewCell.swift
//  Acornote
//
//  Created by Tonny on 28/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import Cache

extension Item {
    
    var largeCellHeight: CGFloat {
        let hasImg = self.imgPath != nil
        
        var h: CGFloat = 0
        if hasImg {
            h += screenW
        }
        
        
        return max(ceil(h), screenH-69-44)
    }
}

class ItemLargeTableViewCell: UITableViewCell {
    
    @IBOutlet weak var imgView: UIImageView!

    @IBOutlet weak var titleTxtView: TextView!
    @IBOutlet weak var desTxtView: TextView!
    
    //
    @IBOutlet weak var imgHCons: NSLayoutConstraint!
    @IBOutlet weak var titleHCons: NSLayoutConstraint!
    @IBOutlet weak var desHCons: NSLayoutConstraint!
    
    //
    static let titleFont = UIFont(name: "Arial-BoldMT", size: 30) ?? UIFont.systemFont(ofSize: 30)
    static let desFont = UIFont(name: "Arial", size: 18) ?? UIFont.systemFont(ofSize: 18)
    override func awakeFromNib() {
        super.awakeFromNib()
        
        titleTxtView.textContainerInset = .zero
        desTxtView.textContainerInset = .zero
        titleTxtView.font = ItemLargeTableViewCell.titleFont
        desTxtView.font = ItemLargeTableViewCell.desFont
    }
    
    var item: Item! {
        didSet {
            titleTxtView.text = item.title
            let textW = screenW-40-40
            if let des = item.des {
                let style = NSMutableParagraphStyle()
                style.lineSpacing = 4
                let att = NSMutableAttributedString(string: des, attributes: [.font : ItemLargeTableViewCell.desFont, .foregroundColor:desTxtView.textColor!, .paragraphStyle:style])
                
                let range = (des.lowercased() as NSString).range(of: item.title!.lowercased())
                if range.location != NSNotFound {
                    att.addAttributes([.foregroundColor:item.folder?.highlightColor ?? UIColor.appGreen], range: range)
                }
                desTxtView.attributedText = att
            }else{
                desTxtView.attributedText = nil
                desHCons.constant = 0
            }
            
            if let des = desTxtView.attributedText {
                let height = des.boundingRect(with: CGSize(width:textW, height:CGFloat.infinity), options: .usesLineFragmentOrigin, context: nil).height
                desHCons.constant = min(ceil(height), (screenH-69-44-20-20))
            }else {
                desHCons.constant = 0
            }
            
            if let imgUrl = item.imgPath {
                imgView.isHidden = false
                
                cache?.async.object(forKey: imgUrl, completion: { [weak self] result in
                    if case .value(let wrapper) = result {
                        DispatchQueue.main.async {
                            self?.imgView.image = wrapper.image
                        }
                    }
                })
            }else {
                imgView.isHidden = true
                imgHCons.constant = 0
                
//                titleTopCons.constant = 100
            }
            
            //quizlet
            if item.fliped == false {
                titleTxtView.isHidden = false
                desTxtView.superview?.isHidden = true
            }else {
                titleTxtView.isHidden = true
                desTxtView.superview?.isHidden = false
            }
            
            let titleH = (item.title! as NSString).boundingRect(with: CGSize(width:textW, height:CGFloat.infinity), options: [.usesLineFragmentOrigin], attributes: [.font : ItemLargeTableViewController.titleFont], context: nil).height
            titleHCons.constant = min(ceil(titleH), (screenH-69-44-20-20))
        }
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        
        titleTxtView.selectedRange = NSMakeRange(0,0)
        desTxtView.selectedRange = NSMakeRange(0,0)
    }

}

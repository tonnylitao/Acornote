//
//  TextView.swift
//  Acornote
//
//  Created by Tonny on 09/11/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit

class TextView: UITextView {

    required init?(coder aDecoder: NSCoder) {
        super.init(coder: aDecoder)
        
        textContainerInset = .zero
        textContainer.lineFragmentPadding = 0
    }
    
    override var textColor: UIColor? {
        didSet {
            if let old = attributedText?.string, let color = textColor {
                let style:NSMutableParagraphStyle = NSParagraphStyle.default.mutableCopy() as! NSMutableParagraphStyle
                style.lineSpacing = 2
                
                let att = NSAttributedString(string: old, attributes: [.font : ItemTableViewCell.titleFont, .paragraphStyle:style, .foregroundColor: color])
                
                attributedText = att
            }
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        if touches.count == 1 && touches.first?.tapCount == 1 {
            superview?.touchesBegan(touches, with: event)
            return
        }
        super.touchesBegan(touches, with: event)
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        if touches.count == 1 && touches.first?.tapCount == 1 {
            superview?.touchesEnded(touches, with: event)
            return
        }
        super.touchesEnded(touches, with: event)
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        if touches.count == 1 && touches.first?.tapCount == 1 {
            superview?.touchesMoved(touches, with: event)
            return
        }
        super.touchesMoved(touches, with: event)
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        if touches.count == 1 && touches.first?.tapCount == 1 {
            superview?.touchesCancelled(touches, with: event)
            return
        }
        super.touchesCancelled(touches, with: event)
    }
}

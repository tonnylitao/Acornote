//
//  UIViewExtension.swift
//  BabyBoat
//
//  Created by Tonny on 8/9/16.
//  Copyright © 2016 babyboat. All rights reserved.
//

import UIKit

extension UIView {
    func subview(withTag tag: Int) -> UIView? {
        for v in subviews {
            if v.tag == tag {
                return v
            }
        }
        
        return nil
    }
    
    func removeAllSubviews() {
        subviews.forEach { $0.removeFromSuperview()}
    }
}

extension UIView {
    
    func showIndicator() {
        showIndicator(nil)
    }
    
    func showIndicator(_ block: ((UIView) -> Void)?) {
        showIndicator(withCenter: CGPoint(x:self.frame.size.width*0.5, y:self.frame.size.height*0.5), style: .white, block: block)
    }
    
    func showIndicator(style: UIActivityIndicatorViewStyle) {
        showIndicator(withCenter: CGPoint(x:self.frame.size.width*0.5, y:self.frame.size.height*0.5), style: style, block: nil)
    }
    
    func showIndicator(withCenter center: CGPoint, style: UIActivityIndicatorViewStyle, block: ((UIView) -> Void)?) {
        var view = self.viewWithTag(40404) as? UIActivityIndicatorView
        
        if view == nil {
            view = UIActivityIndicatorView(frame: CGRect(x:center.x-10, y:center.y-10, width:20, height:20))
            view?.activityIndicatorViewStyle = style
            view?.tag = 40404
            
            view?.translatesAutoresizingMaskIntoConstraints = false
            
            addSubview(view!)
            
            view?.widthAnchor.constraint(equalToConstant: 20)
            view?.heightAnchor.constraint(equalToConstant: 20)
            view?.centerXAnchor.constraint(equalTo: self.centerXAnchor).isActive = true
            view?.centerYAnchor.constraint(equalTo: self.centerYAnchor).isActive = true
        }
        
        view?.startAnimating()
        
        
        block?(self)
        
    }
    
    func hideIndicator(_ block: ((UIView) -> Void)?) {
        let view = self.viewWithTag(40404) as? UIActivityIndicatorView
        view?.stopAnimating()
        view?.removeFromSuperview()
        
        block?(self)
    }
}

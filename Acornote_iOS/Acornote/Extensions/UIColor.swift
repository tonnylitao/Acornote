//
//  UIColor.swift
//  ChangingWorld
//
//  Created by Tonny on 9/30/16.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit

extension UIColor {
    class func rgb(_ r: Int, _ g: Int, _ b: Int) -> UIColor {
        return UIColor(red: CGFloat(r)/255.0, green: CGFloat(g)/255.0, blue: CGFloat(b)/255.0, alpha: 1)
    }
    
    func image(_ size: CGSize = CGSize(width:10, height:10)) -> UIImage {
        UIGraphicsBeginImageContextWithOptions(size, false, UIScreen.main.scale)
        
        let context = UIGraphicsGetCurrentContext()!
        self.setFill()
        
        let rect = CGRect(x:0, y:0, width: size.width, height: size.height)
        context.addRect(rect)
        context.fill(rect)
        
        let img = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        
        return img
    }
    
    func image(withSize size: CGSize = CGSize(width:10, height:10), borderColor: UIColor, borderWidth: CGFloat) -> UIImage {
        let rect = CGRect(x:0, y:0, width: size.width+borderWidth*2, height: size.height+borderWidth*2)
        
        UIGraphicsBeginImageContextWithOptions(rect.size, false, UIScreen.main.scale)
        
        let innderRect = CGRect(x:0, y:0, width: size.width, height: size.height)
        
        let context = UIGraphicsGetCurrentContext()!
        context.setFillColor(borderColor.cgColor)
        context.addArc(center: CGPoint(x: rect.width*0.5, y: rect.height*0.5), radius: rect.width*0.5, startAngle: 0, endAngle: CGFloat.pi*2, clockwise: true)
        context.fillPath()
        
        context.setFillColor(self.cgColor)
        context.addArc(center: CGPoint(x: rect.width*0.5, y: rect.height*0.5), radius: innderRect.width*0.5, startAngle: 0, endAngle: CGFloat.pi*2, clockwise: true)
        context.fillPath()
        
        let img = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        
        return img
    }

    //TODO: animation
    func arcImage(withSize size: CGSize = CGSize(width:10, height:10), borderColor: UIColor? = nil, borderWidth: CGFloat = 0) -> UIImage {
        let rect = CGRect(x:0, y:0, width: size.width+borderWidth*2, height: size.height+borderWidth*2)
        
        UIGraphicsBeginImageContextWithOptions(rect.size, false, UIScreen.main.scale)
        if let c = borderColor {
            let d = min(rect.width, rect.height), r = d*0.5
            
            let context = UIGraphicsGetCurrentContext()!
            context.addLines(between: [CGPoint(x:r, y:r), CGPoint(x:borderWidth+size.width-r, y:r)])
            context.setLineCap(CGLineCap.round)
            context.setLineWidth(d)
            
            context.setStrokeColor(c.cgColor)
            context.strokePath()
        }
    
        let innerRect = CGRect(x:0, y:0, width: size.width, height: size.height)
        let d = min(innerRect.width, innerRect.height), r = d*0.5
        
        let context = UIGraphicsGetCurrentContext()!
        context.setLineWidth(d)
        context.addLines(between: [CGPoint(x:borderWidth+r, y:borderWidth+r), CGPoint(x:size.width-r, y:borderWidth+r)])
        context.setLineCap(CGLineCap.round)
        
        context.setStrokeColor(self.cgColor)
        context.strokePath()
        
        let img = UIGraphicsGetImageFromCurrentImageContext()!
        UIGraphicsEndImageContext()
        
        return img
    }

}

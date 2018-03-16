//
//  UISegmentControl.swift
//  Acornote
//
//  Created by Tonny on 01/11/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import UIKit

extension UISegmentedControl {
    class func segmentedControl(frame: CGRect, color: UIColor, titles: String ...) -> UISegmentedControl {
        let segment = UISegmentedControl(frame: frame)
        
        for (index, title) in titles.enumerated() {
            segment.insertSegment(withTitle: title, at: index, animated: false)
        }
        
        segment.backgroundColor = .white
        //selected bg color
        segment.tintColor = .rgb(245, 245, 245)
        
        //border color
        let borderColor = UIColor.rgb(220, 220, 220)
        segment.layer.borderColor = borderColor.cgColor
        segment.layer.borderWidth = 1
        segment.layer.cornerRadius = 4
        segment.clipsToBounds = true
        
        //devider color
        let img = borderColor.image(CGSize(1, 30))
        segment.setDividerImage(img, forLeftSegmentState: .selected, rightSegmentState: .normal, barMetrics: .default)
        
        //text color
        segment.setTitleTextAttributes([NSAttributedStringKey.foregroundColor: UIColor.lightGray], for: .normal)
        segment.setTitleTextAttributes([NSAttributedStringKey.foregroundColor: color], for: .selected)
        
        segment.selectedSegmentIndex =  0 //should be at last
        return segment
    }
    
}

//
//  UIButton_Short.swift
//  Acornote
//
//  Created by Tonny on 02/11/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import UIKit

extension UIButton {
    func setImage(_ img: UIImage?) {
        setImage(img, for: .normal)
    }
}

extension CGPoint {
    init(_ x: Int = 0, _ y: Int = 0) {
        self = CGPoint(x: x, y: y)
    }
    
    init(_ x: Double = 0, _ y: Double = 0) {
        self = CGPoint(x: x, y: y)
    }
}

extension CGSize {
    init(_ width: Int = 0, _ height: Int = 0) {
        self = CGSize(width: width, height: height)
    }
    
    init(_ width: Double = 0, _ height: Double = 0) {
        self = CGSize(width: width, height: height)
    }
}

extension CGRect {
    init(_ x: Int = 0, _ y: Int = 0, _ width: Int = 0, _ height: Int = 0) {
        self = CGRect(x: x, y: y, width: width, height: height)
    }
    
    init(_ x: Double = 0, _ y: Double = 0, _ width: Double = 0, _ height: Double = 0) {
        self = CGRect(x: x, y: y, width: width, height: height)
    }
    
    init(_ x: CGFloat = 0, _ y: CGFloat = 0, _ width: CGFloat = 0, _ height: CGFloat = 0) {
        self = CGRect(Double(x), Double(y), Double(width), Double(height))
    }
}

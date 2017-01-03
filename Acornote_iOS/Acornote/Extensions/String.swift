//
//  String.swift
//  Acornote
//
//  Created by Tonny on 30/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation

extension String {
    var containsChineseCharacters: Bool {
        return self.range(of: "\\p{Han}", options: .regularExpression, range: self.range(of: self), locale: nil) != nil
    }
    
    var voiceLanguage: String {
        return containsChineseCharacters ? "zh-CN" : "en-US"
    }
    
    var urlTitle: String? {
        
        if self.contains("youtube.com"){
            return "Youtube"
        }else if self.contains("dict.eudic.net"){
            return "每日英语听力"
        }else if self.contains("medium.com"){
            return "Medium"
        }
        
        return nil
    }
}

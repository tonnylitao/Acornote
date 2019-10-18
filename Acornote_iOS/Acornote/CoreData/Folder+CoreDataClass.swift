//
//  Folder+CoreDataClass.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import CoreData
import UIKit
import SugarRecord

enum FolderOrderBy : Int16 {
    case createdAscend, createdDescent
}

public class Folder: NSManagedObject {
}

//MARK: Color

extension Folder {
    //1 is folder.color Coredata default value
    enum Color: Int16, Comparable {
        case sky = 1, red, green, yellow, purple, blue
        
        public static func <(lhs: Folder.Color, rhs: Folder.Color) -> Bool {
            return lhs.rawValue < rhs.rawValue
        }
    }
    
    struct ColorConfig {
        static let `defalut`: Int16 = Color.sky.rawValue
        
        var name: Color
        var rgb: (Int, Int, Int)
        
        static func all() -> [ColorConfig] {
            return [
                ColorConfig(name: .sky, rgb: (25, 210, 185)),
                ColorConfig(name: .red, rgb: (233, 78, 78)),
                ColorConfig(name: .green, rgb: (141, 219, 55)),
                ColorConfig(name: .yellow, rgb: (245, 166, 35)),
                ColorConfig(name: .purple, rgb: (186, 108, 255)),
                ColorConfig(name: .blue, rgb: (74, 144, 226))]
        }
        
        var uiColor: UIColor {
            return UIColor.rgb(rgb.0, rgb.1, rgb.2)
        }
        
        static func color(withId id: Int16) -> ColorConfig? {
            return all().first { $0.name.rawValue == id }
        }
    }
    
    class func existColors() -> [ColorConfig] {
        //TODO by group
        let folders = try! cdStore.mainContext.request(Folder.self).fetch()
        var colors:Set = Set<Int16>()
        folders.forEach { (item) in
            let color = item.color as Int16
            if !colors.contains(color) {
                colors.insert(color)
            }
        }
        
        let all = ColorConfig.all()
        
        let maps = colors.map { (item) -> ColorConfig? in
            
            return all.first(where: { (c) -> Bool in
                return c.name.rawValue == item
            })
        }
        
        let arr = maps.compactMap { $0 }
        
        return arr.sorted { $0.name < $1.name }
    }
    
    var highlightColor: UIColor {
        return Folder.ColorConfig.color(withId: self.color)?.uiColor ?? UIColor.appGreen
    }
}

extension Folder {
    class func audio(title: String, url: String, completion: @escaping ((Data?)->Void)) -> URLSessionTask? {
        let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).first!
        let file = (path as NSString).appendingPathComponent("\(title)_audio")
        
        if let data = try? Data(contentsOf: URL(fileURLWithPath: file)) {
            completion(data)
            return nil
        }
        
        let url = URL(string:url)!
        let request = URLRequest(url: url)
        return URLSession.shared.dataTask(with: request) { (data, response, error) -> Void in
            if let d = data {
                
                try? d.write(to: URL(fileURLWithPath: file))
                DispatchQueue.main.async {
                    completion(d)
                }
            }else {
                DispatchQueue.main.async {
                    completion(nil)
                }
            }
        }
    }
}

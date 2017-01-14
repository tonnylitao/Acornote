//
//  Item+CoreDataProperties.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import CoreData

extension Item {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<Item> {
        return NSFetchRequest<Item>(entityName: "Item");
    }

    @NSManaged public var title: String?
    @NSManaged public var des: String?
    
    @NSManaged public var url: String?
    
    @NSManaged public var taged: Bool
    @NSManaged public var imgPath: String?
    @NSManaged public var createdAt: NSDate?
    @NSManaged public var folder: Folder?
    
    @NSManaged public var fliped: Bool
    
    var dic: [String: Any] {
        var d = [String: Any]()
        
        d["title"] = title
        d["des"] = des
        
        d["url"] = url
        
        d["taged"] = taged
        d["imgPath"] = imgPath
        
        d["createdAt"] = createdAt
        
        d["folder"] = folder?.title
        
        d["fliped"] = fliped
        
        return d
    }
}

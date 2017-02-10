//
//  Folder+CoreDataProperties.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import CoreData

extension Folder {

    @nonobjc public class func fetchRequest() -> NSFetchRequest<Folder> {
        return NSFetchRequest<Folder>(entityName: "Folder");
    }

    @NSManaged public var title: String?
    @NSManaged public var url: String?
    @NSManaged public var audioUrl: String?
    
    @NSManaged public var color: Int16
    
    @NSManaged public var playable: Bool
    @NSManaged public var flipable: Bool
    @NSManaged public var tagable: Bool
    @NSManaged public var quizlet: Bool
    
    @NSManaged public var createdAt: NSDate?
    @NSManaged public var updatedAt: NSDate?
    
    @NSManaged public var orderBy: Int16
    
    @NSManaged public var lastVisited: String?
    
    @NSManaged public var items: NSOrderedSet?
    
    var dic: [String: Any?] {
        var d = [String: Any?]()
        
        d["title"] = title
        d["url"] = url
        d["audioUrl"] = audioUrl
        
        d["color"] = color
        
        d["playable"] = playable
        d["flipable"] = flipable
        d["tagable"] = tagable
        
        d["orderBy"] = orderBy
        
        d["quizlet"] = quizlet
        d["createdAt"] = createdAt
        d["updatedAt"] = createdAt
        
        d["lastVisited"] = lastVisited
        
        return d
    }

}

// MARK: Generated accessors for items
extension Folder {

    @objc(insertObject:inItemsAtIndex:)
    @NSManaged public func insertIntoItems(_ value: Item, at idx: Int)

    @objc(removeObjectFromItemsAtIndex:)
    @NSManaged public func removeFromItems(at idx: Int)

    @objc(insertItems:atIndexes:)
    @NSManaged public func insertIntoItems(_ values: [Item], at indexes: NSIndexSet)

    @objc(removeItemsAtIndexes:)
    @NSManaged public func removeFromItems(at indexes: NSIndexSet)

    @objc(replaceObjectInItemsAtIndex:withObject:)
    @NSManaged public func replaceItems(at idx: Int, with value: Item)

    @objc(replaceItemsAtIndexes:withItems:)
    @NSManaged public func replaceItems(at indexes: NSIndexSet, with values: [Item])

    @objc(addItemsObject:)
    @NSManaged public func addToItems(_ value: Item)

    @objc(removeItemsObject:)
    @NSManaged public func removeFromItems(_ value: Item)

    @objc(addItems:)
    @NSManaged public func addToItems(_ values: NSOrderedSet)

    @objc(removeItems:)
    @NSManaged public func removeFromItems(_ values: NSOrderedSet)

}

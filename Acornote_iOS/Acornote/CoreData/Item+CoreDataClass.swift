//
//  Item+CoreDataClass.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import CoreData
import SugarRecord


public class Item: NSManagedObject {
    
}

extension Entity where Self: NSManagedObject {
    
    static func findOne(_ requestable: Requestable? = nil, predicate: NSPredicate) -> Self? {
        let ctx = requestable ?? cdStore.saveContext
        let sort = NSSortDescriptor(key: "createdAt", ascending: false)
        let requ = FetchRequest<Self>(ctx, sortDescriptor:sort, predicate: predicate, fetchLimit:1)
        let items = try! requ.fetch()
        
        return items.first
    }
}

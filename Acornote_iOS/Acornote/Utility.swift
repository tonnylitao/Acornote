//
//  Utility.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import Foundation
import CoreData
import SugarRecord
import AVFoundation
import Cache

let cdStore = coreDataStorage()
let speecher = AVSpeechSynthesizer();

let screenW = UIScreen.main.bounds.width
let screenH = UIScreen.main.bounds.height

private func coreDataStorage() -> CoreDataDefaultStorage {
    let store = CoreDataStore.named("db")
    let model = CoreDataObjectModel.merged([Bundle.main])
    let defaultStorage = try! CoreDataDefaultStorage(store: store, model: model)
    return defaultStorage
}

extension NSManagedObject {
    func remove(_ completion: (()->Void)?) {
        let id = self.objectID
        try? cdStore.operation({ (ctx, save) in
            let obj = (ctx as! NSManagedObjectContext).object(with: id)
            
            try? ctx.remove(obj)
            save()
            
            DispatchQueue.main.async {
                completion?()
            }
        })
    }
    
    func update(update: @escaping (NSManagedObject) -> Void, callback: (() -> Void)? = nil) {
        let id = self.objectID
        try? cdStore.operation({ (ctx, save) in
            let obj = (ctx as! NSManagedObjectContext).object(with: id)
            update(obj)
            save()
            
            if let closure = callback {
                DispatchQueue.main.async {
                    closure()
                }
            }
        })
    }
}

extension UIColor {
    static var appGreen: UIColor {
        return UIColor.rgb(25, 210, 185)
    }
    
    static var appBackground: UIColor {
        return UIColor.rgb(241, 240, 239)
    }
    
    static var appNav: UIColor {
        return UIColor.rgb(55, 59, 70)
    }
}

///
let diskConfig = DiskConfig(name: "FloAcornote_Cacheppy")
let memoryConfig = MemoryConfig()

let cache = try? Storage(diskConfig: diskConfig, memoryConfig: memoryConfig)


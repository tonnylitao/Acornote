//
//  AppDelegate.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import CoreData

/* TODO:
 order item；
 restore iOS state；
 Home swip page；
 items re-order
 youtube in webview
 */

/* Feature:
 share from other app
 player : play list
*/


@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        
//        #if DEBUG
//            importDB()
//        #endif
        
        UINavigationBar.appearance().isTranslucent = false
        UINavigationBar.appearance().barStyle = .black
        UINavigationBar.appearance().barTintColor = .rgb(56, 59, 69)
        UINavigationBar.appearance().tintColor = .white
        //clear navigationBar bottom line
        UINavigationBar.appearance().shadowImage = UIImage()
        UINavigationBar.appearance().setBackgroundImage(UIImage(), for: .default)
        
        UIReferenceLibraryViewController.dictionaryHasDefinition(forTerm: "")
        
        //
        
        createDemoDataIfNecessory();
        
        return true
    }
    
    func createDemoDataIfNecessory() {
        let projects = try! cdStore.saveContext.request(Folder.self).fetch()
        if projects.count == 0 {
            [("demo", "Everyday Essential Words")].forEach({ (file, name) in
                let model: Folder = try! cdStore.saveContext.create()
                model.createdAt = NSDate()
                model.playable = true
                model.title = name
                model.color = Folder.ColorConfig.defalut
                //                model.url = "https://dict.eudic.net/webting/desktopplay/8b472527-01ec-11e6-a7a6-000c29ffef9b"
                
                let texts:String = try! String(contentsOfFile: Bundle.main.path(forResource: file, ofType: "")!, encoding: .utf8)
                let arr = texts.components(separatedBy: .newlines)
                
                for i in 0..<arr.count/2 {
                    let item: Item = try! cdStore.saveContext.create()
                    item.title = arr[2*i].trimmingCharacters(in: .whitespacesAndNewlines)
                    let des = arr[2*i+1].trimmingCharacters(in: .whitespacesAndNewlines)
                    if des != "."{
                        item.des = des
                    }
                    item.createdAt = NSDate()
                    item.folder = model
                    model.updatedAt = NSDate()
                }
            })
            
            let ctx = (cdStore.saveContext as! NSManagedObjectContext)
            ctx.performAndWait({
                try! ctx.save()
            })
        }
    }
    
    func applicationWillResignActive(_ application: UIApplication) {
        clearDefaults()
    }

    private func clearDefaults() {
        let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")
        ud?.removeObject(forKey: "Items")
        ud?.removeObject(forKey: "Item_Folder_Map")
        ud?.synchronize()
    }

    func applicationDidEnterBackground(_ application: UIApplication) {
        
        try? cdStore.operation { (context, save) throws -> Void in
            
            let folders = try! context.request(Folder.self).fetch()
            
            let folderArr = folders.reduce([], { (results, folder) -> [[String: Any]] in
                var re = results
                re.append(folder.dic)
                
                return re
            })
            
            let items = try! context.request(Item.self).fetch()
            let itemArr = items.reduce([], { (results, item) -> [[String: Any]] in
                var re = results
                re.append(item.dic)
                
                return re
            })
            
            if let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).first {
                let dic = ["folders": folderArr, "items": itemArr]
                
                let c = Calendar.current
                let coms = c.dateComponents([.year, .month, .day], from: Date())
                
                let file = (path as NSString).appendingPathComponent("db_\(coms.year!)_\(coms.month!)_\(coms.day!)")
                
                let success = (dic as NSDictionary).write(toFile: file, atomically: true)
                debugPrint("save file ", success)
                
                //TODO remove old files
            }
            
            save()
        }
    }

    func applicationWillEnterForeground(_ application: UIApplication) {

    }

    func applicationDidBecomeActive(_ application: UIApplication) {
        let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")!
        if let arr = ud.object(forKey: "Items") as? [String], let map = ud.object(forKey: "Item_Folder_Map") as? [String: String] {
            try? cdStore.operation { (context, save) throws -> Void in
                //TODO 批量
                arr.forEach({ text in
                    guard let fName = map[text] else {
                        return
                    }
                    
                    let pre = NSPredicate(format: "title == %@", fName)
                    if let folder = Folder.findOne(context, predicate: pre) {
                        
                        if let _ = Item.findOne(context, predicate: NSPredicate(format:"folder=%@ AND title == %@", folder, text)) {
                            //repeat
                        }else {
                            //copy des firstly, copy title later.
                            if let item = Item.findOne(context, predicate: NSPredicate(format: "folder == %@ AND title CONTAINS[cd] %@", folder, text)) {
                                if let des = item.des {
                                    if !des.components(separatedBy: .newlines).contains(text) {
                                        item.des = des + "\n" + text
                                    }
                                }else {
                                    item.des = item.title
                                    item.title = text
                                }
                            }else {
                                let item: Item = try! context.create()
                                item.createdAt = NSDate()
                                item.title = text
                                item.folder = folder
                                folder.updatedAt = NSDate()
                            }
                        }
                    }
                })
                save()
                
                self.clearDefaults()
            }
        }
        
        //
        try? cdStore.operation { (context, save) throws -> Void in
            let folders = try! context.request(Folder.self).sorted(with: "updatedAt", ascending: false).fetch()
            let items = folders.map({ (f) -> [String: String] in
                let rgb = Folder.ColorConfig.color(withId: f.color)!.rgb
                return [f.title!: "\(rgb.0),\(rgb.1),\(rgb.2)"]
            })
            ud.set(items, forKey: "All_Folder_Names")
            ud.synchronize()
        }
    }

    func applicationWillTerminate(_ application: UIApplication) {
        
    }

    func application(_ application: UIApplication, shouldSaveApplicationState coder: NSCoder) -> Bool {
        return true
    }
    
    #if DEBUG
    func importDB() {
        let url = Bundle.main.url(forResource: "db", withExtension: nil)!
        let data = try! Data(contentsOf: url)
        
        let path = NSSearchPathForDirectoriesInDomains(.documentDirectory, .userDomainMask, true).first!
        let p = (path as NSString).appendingPathComponent("db")
        
        try? data.write(to: URL(fileURLWithPath: p))
    }
    #endif
}


extension String {
    // He is tall
    // ["He", "He is", "He is tall", "is", "is tall", "tall"]
    var components: [String] {
        if self.isEmpty {
            return []
        }
        
        var arr = self.components(separatedBy: .whitespaces)
        
        var result = [String]()
        for (index, _) in arr.enumerated() {
            
            var items = [String]()
            for i in index..<arr.count {
                let item = arr[index...i].joined(separator: " ")
                items.append(item.replacingOccurrences(of: ".", with: "").replacingOccurrences(of: ",", with: ""))
            }
            
            result.append(contentsOf: items)
        }
        
        return result
    }
}

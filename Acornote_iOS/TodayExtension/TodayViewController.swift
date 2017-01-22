//
//  TodayViewController.swift
//  Acornote
//
//  Created by Tonny on 27/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import NotificationCenter

class TodayViewController: UIViewController, NCWidgetProviding {
    
    @IBOutlet weak var lbl: UILabel!
        
    @IBOutlet weak var lineView: UIView!
    @IBOutlet weak var folderLbl: UILabel!
    
    var allFolders: [[String: String]]?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view from its nib.
        
        let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")
        
        self.allFolders = ud?.object(forKey: "All_Folder_Names") as? [[String: String]]
        
        let folderName = ud?.object(forKey: "Current_Folder_Name") as? String ?? allFolders?.first?.keys.first
        folderLbl.text = folderName
        
        if let text = UIPasteboard.general.string?.trimmingCharacters(in: .whitespacesAndNewlines), !text.isEmpty {
            lbl.text = text
            
            if let arr = ud?.object(forKey: "Items") as? [String] {
                if arr.contains(text) {
                    return
                }
                
                var temp = arr
                temp.append(text)
                ud?.setValue(temp, forKey: "Items")
            }else {
                ud?.setValue([text], forKey: "Items")
            }
            
            //
            var map = ud?.object(forKey: "Item_Folder_Map") as? [String: String] ?? [:]
            map[text] = folderName
            ud?.set(map, forKey: "Item_Folder_Map")
            
            ud?.synchronize()
        }else {
            lbl.text = nil
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    private func widgetPerformUpdate(completionHandler: ((NCUpdateResult) -> Void)) {
        // Perform any setup necessary in order to update the view.
        
        // If an error is encountered, use NCUpdateResult.Failed
        // If there's no update required, use NCUpdateResult.NoData
        // If there's an update, use NCUpdateResult.NewData
        
        completionHandler(NCUpdateResult.newData)
    }
    
    @IBAction func tapFolder(_ sender: Any) {
        guard let arr = allFolders, !arr.isEmpty else {
            return
        }
        
        let text = folderLbl.text!
        
        guard let index = arr.index(where: { (item) -> Bool in
            return item.keys.first == text
        }) else {
            return
        }
        
        let obj = arr[(index+1)%arr.count]
        let c = obj.values.first!.components(separatedBy: ",")
        
        let r = CGFloat((c[0] as NSString).floatValue)
        let g = CGFloat((c[1] as NSString).floatValue)
        let b = CGFloat((c[2] as NSString).floatValue)
        lineView.backgroundColor = UIColor(red: r/255.0, green: g/255.0, blue: b/255.0, alpha: 1)
        
        let name = obj.keys.first!
        folderLbl.text = name
        
        let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")
        ud?.set(name, forKey: "Current_Folder_Name")
        ud?.synchronize()
        
        if let text = lbl.text, !text.isEmpty {
            var map = ud?.object(forKey: "Item_Folder_Map") as? [String: String] ?? [:]
            
            map[text] = name
            
            ud?.set(map, forKey: "Item_Folder_Map")
            ud?.synchronize()
        }
    }
    
    @IBAction func cancel(_ sender: Any) {
        guard let text = folderLbl.text else {
            return
        }
        
        lbl.text = nil
        
        //
        let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")
        if let arr = ud?.object(forKey: "Items") as? [String]
            , let index = arr.index(of: text) {
            
            var temp = arr
            temp.remove(at: index)
            ud?.setValue(temp, forKey: "Items")
        }
        
        //
        if var map = ud?.object(forKey: "Item_Folder_Map") as? [String: String] {
            map.removeValue(forKey: text)
            ud?.set(map, forKey: "Item_Folder_Map")
        }
        
        ud?.synchronize()
    }
    
    
}

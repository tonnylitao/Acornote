//
//  ShareViewController.swift
//  ActivityProvoider
//
//  Created by Tonny on 05/11/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import Social
import MobileCoreServices

class ShareViewController: SLComposeServiceViewController {
    
    override func isContentValid() -> Bool {
        // Do validation of contentText and/or NSExtensionContext attachments here
        
        guard let currentMessage = contentText, !currentMessage.isEmpty else {
            return false
        }
        
        let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")
        ud?.set(currentMessage, forKey: "group.tonnysunm.acornote.share")
        ud?.synchronize()
        
        cancel()
        return true
    }

    override func didSelectPost() {
        // This is called after the user selects Post. Do the upload of contentText and/or NSExtensionContext attachments.

        for attachment in extensionContext?.inputItems as! [NSItemProvider] {
            debugPrint(attachment.registeredTypeIdentifiers)
            
            if attachment.hasItemConformingToTypeIdentifier(kUTTypeURL as String) {
                //TODO
                //folder: title url
                let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")
                ud?.set(contentText, forKey: "group.tonnysunm.acornote.url")
                ud?.synchronize()
            }else if attachment.hasItemConformingToTypeIdentifier(kUTTypeText as String) {
                //item: text
                let ud = UserDefaults(suiteName: "group.tonnysunm.acornote")
                ud?.set(contentText, forKey: "group.tonnysunm.acornote.share")
                ud?.synchronize()
            }else if attachment.hasItemConformingToTypeIdentifier(kUTTypeImage as String) {
                attachment.loadItem(forTypeIdentifier: kUTTypeURL as String, options: nil, completionHandler: { (coding:NSSecureCoding?, _) in
                    _ = coding as! String
                })
                //TODO
                //item: text img
            }
        }
        
        // Inform the host that we're done, so it un-blocks its UI. Note: Alternatively you could call super's -didSelectPost, which will similarly complete the extension context.
        self.extensionContext!.completeRequest(returningItems: [], completionHandler: nil)
    }

    override func configurationItems() -> [Any]! {
        // To add configuration options via table cells at the bottom of the sheet, return an array of SLComposeSheetConfigurationItem here.
        return []
    }
    
    override func presentationAnimationDidFinish() {
        textView.font = UIFont(name: "Arial-BoldMT", size: 20) ?? UIFont.systemFont(ofSize: 20)
        textView.textColor = .lightGray
        placeholder = "Enter Item Title"
    }

    func imageFromExtensionItem(extensionItem: NSExtensionItem, callback: (_ image: UIImage?)->Void) {
//        for attachment in extensionItem.attachments {
//            debugPrint(attachment.registeredTypeIdentifiers)
//        }
    }
}

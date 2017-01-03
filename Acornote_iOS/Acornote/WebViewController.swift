//
//  WebViewController.swift
//  Acornote
//
//  Created by Tonny on 29/10/2016.
//  Copyright © 2016 Tonny&Sunm. All rights reserved.
//

import UIKit
import SafariServices

class WebViewController: UIViewController, UIWebViewDelegate {
    
    @IBOutlet weak var lineView: UIView!
    
    @IBOutlet weak var webView: UIWebView!
    
    var url: URL!
    
    var folder: Folder?
    var item: Item?
    
    var isForImage: Bool = false

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let req = URLRequest(url: url, cachePolicy:.returnCacheDataElseLoad)
     
        webView.showIndicator(style: .gray)
        webView.loadRequest(req)
        
        if let colorId = (folder ?? item?.folder)?.color, let color = Folder.ColorConfig.color(withId: colorId)?.uiColor {
            lineView.backgroundColor = color
        }
        
        NotificationCenter.default.addObserver(self, selector: #selector(WebViewController.hightlightChanged(noti:)), name: NSNotification.Name("hightlightChanged"), object: nil)
    }
    
    func hightlightChanged(noti: NSNotification) {
        if let obj = noti.object as? String {
//            highlight?.texts.append(obj)
            
            //TODO: crash
//            setHighlight()
        }
    }
    
    var imgTask: URLSessionDataTask?
    @IBAction func doCopy(_ sender: Any) {
        if let url = webView.request?.url {
            //UIPasteboard.general.url = url
            
            let downloadImg: ()->Void = { [weak self] in
                self?.imgTask?.cancel()
                var request = URLRequest(url: url)
                request.addValue("image/*", forHTTPHeaderField: "Accept")
                self?.imgTask = URLSession.shared.dataTask(with: request) { (data, response, error) -> Void in
                    if let d = data {
                        cache.add(url.absoluteString, object: d, completion: {
                            DispatchQueue.main.async {
                                NotificationCenter.default.post(name: NSNotification.Name(rawValue:"ImgPathChanged"), object: url.absoluteString)
                            }
                        })
                    }
                }
                
                self?.imgTask?.resume()
            }
            
            if let i = item {
                i.update(update: { (obj) in
                    if let item = obj as? Item {
                        item.imgPath = url.absoluteString
                    }
                }, callback: downloadImg)
            }else {
                downloadImg()
            }
            
            back(nil)
        }
    }
    
    @IBAction func back(_ sender: AnyObject?) {
        webView?.delegate = nil
        
        NotificationCenter.default.removeObserver(self)
        
        let vc = navigationController?.popViewController(animated: true)
        
        if vc == nil {
            dismiss(animated: true, completion: nil)
        }
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    @IBAction func safari(_ sender: Any) {
        let svc = SFSafariViewController(url: url, entersReaderIfAvailable: true)
        present(svc, animated: true, completion: nil)
    }

    func webView(_ webView: UIWebView, shouldStartLoadWith request: URLRequest, navigationType: UIWebViewNavigationType) -> Bool {
        if request.url?.absoluteString == "about:blank" {
            return false
        }
        //debugPrint(request, request.url as Any)
        return true
    }
    
    //http://stackoverflow.com/questions/11815294/memory-leaks-with-uiwebview-and-javascript
    func webViewDidFinishLoad(_ webView: UIWebView) {
        webView.hideIndicator(nil)
        
        setHighlight()
    }
    
    func setHighlight() {
        if folder == nil && item == nil {
            return
        }
        
        if url?.absoluteString != item?.url && url?.absoluteString != folder?.url {
            return
        }
        
        if highlight == nil {
            highlight = buildHighlightJS()
        }
        
        if let body = webView.stringByEvaluatingJavaScript(from: "document.body.innerHTML"), !body.isEmpty {
            if url?.absoluteString.contains("dict.eudic.net/") == true {
                if let range = body.range(of: ".mp3"), let httpR = body.range(of: "http", options: .backwards, range: body.startIndex..<range.upperBound, locale: nil) {
                    let mp3 = body.substring(with: httpR.lowerBound..<range.upperBound)
                    
                    folder?.update(update: { (obj) in
                        if let folder = obj as? Folder {
                            folder.audioUrl = mp3
                        }
                    }, callback: nil)
                }
            }
            
            var newBody = body
            let color = highlight!.color
            highlight?.texts.forEach({ (text) in
                newBody = newBody.replacingOccurrences(of: text, with: "<strong style=\"color: "+color+"\">"+text+"</strong>")
            })
            
            newBody = newBody.replacingOccurrences(of: "'", with: "\\'") //newBody单引号冲突
            newBody = newBody.replacingOccurrences(of: "\n", with: "\\n") //js字符串行尾需要带反斜杠
            
            // \\\n 最后需要换行 防止最后一个单引号被注释掉
            let js = "(function(body){ document.body.innerHTML=body; return true})('\(newBody)\\\n')"
            
            let dom = webView.stringByEvaluatingJavaScript(from: js)
            debugPrint("js reuslt", dom as Any)
        }
    }
    
    typealias Highlight = (texts:[String], color:String)
    var highlight: Highlight?
    
    func buildHighlightJS() -> Highlight {
        var texts = [String]()
        if let f = folder {
            f.items?.forEach({ (obj) in
                let item = obj as! Item
                //des first, title later
                if let des = item.des, !des.isEmpty {
                    texts.append(des)
                }
                
                texts.append(item.title!)
            })
        }else {
            texts.append(item!.title!)
        }
        
        //
        let colorId = item?.folder?.color ?? Folder.ColorConfig.defalut
        let rgb = Folder.ColorConfig.color(withId: colorId)?.rgb ?? Folder.ColorConfig.color(withId: Folder.ColorConfig.defalut)!.rgb
        let color: String = "rgb(\(rgb.0),\(rgb.1),\(rgb.2))"
        
        return (texts, color)
    }
    
    /*
    func buildHighlightJS() -> Highlight {
        let colorId = item?.folder?.color ?? Folder.ColorConfig.defalut
        let rgb = Folder.ColorConfig.color(withId: colorId)?.rgb ?? Folder.ColorConfig.color(withId: Folder.ColorConfig.defalut)!.rgb
        let color: String = "rgb(\(rgb.0),\(rgb.1),\(rgb.2))"
        
        var js:String = try! String(contentsOfFile: Bundle.main.path(forResource: "highlight", ofType: "js")!, encoding: .utf8)
        
        var text = "["
        if let f = folder {
            f.items?.forEach({ (obj) in
                let item = obj as! Item
                //des first, title later
                if let des = item.des {
                    text += "'\(des.replacingOccurrences(of: "'", with: "\\'"))',"
                }
                
                text += "'\(item.title!.replacingOccurrences(of: "'", with: "\\'"))',"
            })
            text += "]"
        }else {
            text += item!.title! + "]"
        }
        
        text = text.replacingOccurrences(of: "\n", with: "\\n")
        
        js += "highlight(" + text + ",'" + color + "')"
        
        debugPrint(js)
        
        return js
    }
 */
    
    override var preferredStatusBarStyle: UIStatusBarStyle {
        return .lightContent
    }
    
    deinit {
        debugPrint("web vc deinit")
    }
}

extension WebViewController {
    
    class func show(withUrl: String?, folder: Folder? = nil, item: Item? = nil, vc: UIViewController? = nil) -> WebViewController? {
        guard let url = withUrl, let path = url.addingPercentEncoding(withAllowedCharacters: .urlQueryAllowed), let URL = URL(string: path) else {
            return nil
        }
        
        let sb = UIStoryboard(name: "Main", bundle: nil)
        let svc = sb.instantiateViewController(withIdentifier:"WebViewController") as! WebViewController
        svc.url = URL
        svc.folder = folder
        svc.item = item
        
        if vc != nil {
            vc?.present(svc, animated: true, completion: nil)
        }else {
            (UIApplication.shared.keyWindow?.rootViewController as? UINavigationController)?.pushViewController(svc, animated: true)
        }
        
        return svc
    }
}

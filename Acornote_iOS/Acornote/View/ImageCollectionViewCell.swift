//
//  ImageCollectionViewCell.swift
//  Acornote
//
//  Created by Tonny on 24/03/17.
//  Copyright © 2017 Tonny&Sunm. All rights reserved.
//

import UIKit
import Cache

class ImageCollectionViewCell: UICollectionViewCell {
    
    @IBOutlet weak var imgView: UIImageView!
    
    @IBOutlet weak var titleLbl: UILabel!
    
    @IBOutlet weak var desLbl: UILabel!
    
    //fix bug
    override func awakeFromNib() {
        super.awakeFromNib()
        
        imgView = self.viewWithTag(1) as! UIImageView!
        titleLbl = self.viewWithTag(2) as! UILabel
        desLbl = self.viewWithTag(3) as! UILabel
    }
    
    var imgTask: URLSessionDataTask?
    
    var item: Item? {
        didSet {
            
            if let imgUrl = item?.imgPath {
                imgView.isHidden = false
                imgView.image = nil
                
                titleLbl.text = item?.title
                desLbl.text = item?.des
                
                cache?.async.object(ofType: ImageWrapper.self, forKey: imgUrl, completion: { [weak self] result in
                    if case .value(let wrapper) = result {
                        DispatchQueue.main.async {
                            self?.imgView.image = wrapper.image
                        }
                    }else if let url = URL(string: imgUrl) {
                        self?.imgTask?.cancel()
                        var request = URLRequest(url: url)
                        request.addValue("image/*", forHTTPHeaderField: "Accept")
                        self?.imgTask = URLSession.shared.dataTask(with: request) {[weak self] (data, response, error) -> Void in
                            if let d = data, let img = UIImage(data: d){
                                cache?.async.setObject(ImageWrapper(image: img), forKey: imgUrl, completion: { _ in
                                    DispatchQueue.main.async {
                                        self?.imgView.image = img
                                    }
                                })
                            }
                        }
                        
                        self?.imgTask?.resume()
                    }
                })
            }

        }
    }
    
}

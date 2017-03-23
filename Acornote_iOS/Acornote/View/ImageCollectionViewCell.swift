//
//  ImageCollectionViewCell.swift
//  Acornote
//
//  Created by Tonny on 24/03/17.
//  Copyright © 2017 Tonny&Sunm. All rights reserved.
//

import UIKit

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
                
                cache.object(imgUrl, completion: {[weak self] (img:UIImage?) in
                    if let img = img {
                        DispatchQueue.main.async {
                            self?.imgView.image = img
                        }
                    }else if let url = URL(string: imgUrl) {
                        self?.imgTask?.cancel()
                        var request = URLRequest(url: url)
                        request.addValue("image/*", forHTTPHeaderField: "Accept")
                        self?.imgTask = URLSession.shared.dataTask(with: request) {[weak self] (data, response, error) -> Void in
                            if let d = data, let img = UIImage(data: d){
                                cache.add(imgUrl, object: d, completion: {
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

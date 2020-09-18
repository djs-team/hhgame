//
//  CXSelectedPhotoView.h
//  hairBall
//
//  Created by mahong yang on 2019/10/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXSelectedPhotoViewSelectedPhotoBlock)(UIImage *photo);

@interface CXSelectedPhotoView : UIView

+ (instancetype)shareInstance;

@property (nonatomic, copy) CXSelectedPhotoViewSelectedPhotoBlock selectedPhotoBlock;

@end

NS_ASSUME_NONNULL_END

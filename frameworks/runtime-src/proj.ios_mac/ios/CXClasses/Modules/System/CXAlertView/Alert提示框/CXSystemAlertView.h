//
//  CXSystemAlertView.h
//  hairBall
//
//  Created by mahong yang on 2020/6/15.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"


typedef void(^AlertViewCancelBlock)(void);
typedef void(^AlertViewSureBlock)(void);

@interface CXSystemAlertView : MMPopupView

+ (CXSystemAlertView *)loadNib;

- (void)showAlertTitle:(NSString *)title message:(NSString *)message cancel:(AlertViewCancelBlock)cancelBlock sure:(AlertViewSureBlock)sureBlock;

@end


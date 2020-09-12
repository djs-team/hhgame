//
//  CXTextFieldAlertView.h
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXTextFieldAlertViewSureActionBlcok)(NSString *content);

@interface CXTextFieldAlertView : UIView

@property (nonatomic, strong) UIViewController *parentVC;

@property (nonatomic, strong) NSString *contentStr;

@property (nonatomic, copy) CXTextFieldAlertViewSureActionBlcok sureActionBlock;

@end

NS_ASSUME_NONNULL_END

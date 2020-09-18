//
//  XYTableViewAlertView.h
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXTableViewAlertViewSureActionBlcok)(NSArray *contentArrays);

@interface XYTableViewAlertView : UIView

@property (nonatomic, copy) CXTableViewAlertViewSureActionBlcok sureActionBlock;

@property (nonatomic, strong) UIViewController *parentVC;

@property (nonatomic, copy) NSArray *dataSources;

@end

NS_ASSUME_NONNULL_END

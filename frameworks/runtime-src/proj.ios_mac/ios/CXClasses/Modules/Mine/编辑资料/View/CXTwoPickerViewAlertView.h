//
//  CXTowPickerViewAlertView.h
//  hairBall
//
//  Created by mahong yang on 2019/10/25.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXTwoPickerViewAlertViewSureActionBlcok)(NSString *content1, NSString *content2);

@interface CXTwoPickerViewAlertView : UIView

@property (nonatomic, copy) CXTwoPickerViewAlertViewSureActionBlcok sureActionBlock;

@property (nonatomic, strong) UIViewController *parentVC;

@property (nonatomic, copy) NSString *title;

@property (nonatomic, strong) NSArray *dataSources;


@end

NS_ASSUME_NONNULL_END

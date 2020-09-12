//
//  CXTableViewAlertView.h
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXPickerViewAlertViewSureActionBlcok)(NSString *content);

@interface CXPickerViewAlertView : UIView

@property (nonatomic, copy) CXPickerViewAlertViewSureActionBlcok sureActionBlock;

@property (nonatomic, strong) UIViewController *parentVC;

@property (nonatomic, copy) NSString *title;

@property (nonatomic, copy) NSArray *dataSources;

@property (nonatomic, copy) NSString *contenStr;



@end

NS_ASSUME_NONNULL_END

//
//  CXDateSelectedView.h
//  hairBall
//
//  Created by mahong yang on 2019/10/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXDateSelectedViewSelectedDateBlock)(NSDate *selectedDate);

@interface CXDateSelectedView : UIView

@property (nonatomic, strong) UIViewController *parentVC;

@property (weak, nonatomic) IBOutlet UIDatePicker *mainDatePicker;

@property (nonatomic, copy) CXDateSelectedViewSelectedDateBlock selectedDateBlock;

@end

NS_ASSUME_NONNULL_END

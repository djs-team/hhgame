//
//  CXWithdrawSelectedDateViewController.h
//  hairBall
//
//  Created by mahong yang on 2019/11/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXWithdrawSelectedDateViewController : CXBaseViewController

@property (nonatomic, copy) void (^selectedDateBlock)(NSDate *b_time, NSDate *e_time);

@end

NS_ASSUME_NONNULL_END

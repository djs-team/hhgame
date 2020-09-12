//
//  CXWithdrawIncomeListViewController.h
//  hairBall
//
//  Created by mahong yang on 2019/11/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

typedef enum : NSUInteger {
    income, // 收入明细
    tixian, // 体现明细
    exchange, // 兑换明细
    redpack_incom, // 红包-收入明细
    redpack_tixian, // 红包-体现明细
    redpack_exchange, // 红包-兑换明细
} CXWithdrawIncomeListType;

@interface CXWithdrawIncomeListViewController : CXBaseViewController

@property (nonatomic, assign) CXWithdrawIncomeListType incomeType;

@end

NS_ASSUME_NONNULL_END

//
//  CXLiveRoomRechargeView.h
//  hairBall
//
//  Created by mahong yang on 2020/6/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"
#import "CXRechargeModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomRechargeView : MMPopupView

@property (nonatomic, copy) void (^rechargeBlock)(CXRechargeModel *model, NSInteger payAction);
@property (nonatomic, copy) void (^gotoRechargeProtocol)(NSString *linkURL);

@end

NS_ASSUME_NONNULL_END

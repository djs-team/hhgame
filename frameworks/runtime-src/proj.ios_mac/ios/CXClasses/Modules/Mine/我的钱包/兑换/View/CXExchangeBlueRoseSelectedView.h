//
//  CXExchangeBlueRoseSelectedView.h
//  hairBall
//
//  Created by mahong yang on 2020/4/3.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"
#import "CXFriendInviteModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXExchangeBlueRoseSelectedView : MMPopupView

@property (nonatomic, strong) CXFriendGiftModel *giftModel;

@property (nonatomic, copy) void (^exchangeBlueRoseBlock)(NSString *count, UIButton *sender);

@end

NS_ASSUME_NONNULL_END

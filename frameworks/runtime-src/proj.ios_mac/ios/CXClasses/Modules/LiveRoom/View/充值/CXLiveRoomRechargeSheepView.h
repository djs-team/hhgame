//
//  CXLiveRoomRechargeSheepView.h
//  hairBall
//
//  Created by mahong yang on 2020/6/14.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomRechargeSheepView : MMPopupView

- (void)setupRoseNumber:(NSString *)roseNumber roseRMB:(NSString *)reoseRMB;

@property (nonatomic, copy) void(^rechargeSheetViewBlcok)(BOOL isRecharge, BOOL isCancel, NSString *payAction);

@end

NS_ASSUME_NONNULL_END

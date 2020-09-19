//
//  CXLiveRoomSeatFansView.h
//  hairBall
//
//  Created by mahong yang on 2020/6/3.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomSeatFansView : MMPopupView

@property (nonatomic, strong) LiveRoomMicroInfo *microInfo;

@property (nonatomic, copy) void (^didSelectedFansModel)(CXSocketMessageSeatsFansModel *model);

@end

NS_ASSUME_NONNULL_END

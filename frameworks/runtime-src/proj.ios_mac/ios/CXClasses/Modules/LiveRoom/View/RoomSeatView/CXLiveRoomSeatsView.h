//
//  CXLiveRoomSeatsView.h
//  hairBall
//
//  Created by mahong yang on 2020/3/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXLiveRoomModel.h"
#import "CXLiveRoomSeatView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomSeatsView : UIView

@property NSMutableDictionary<NSIndexPath*, CXLiveRoomSeatView*> *seats;
@property (nonatomic) CXLiveRoomModel *model;
@property (nonatomic, strong) NSMutableArray *isMuteArrays;

@property (nonatomic, copy) void (^seatViewActionBlock)(NSInteger tag, LiveRoomMicroInfo * microInfo);
@property (nonatomic, copy) void (^onSeatTouchUpInsideBlock)(CXLiveRoomSeatView *sender);
@property (nonatomic, copy) void (^recommendActionBlock)(void);

- (void)reloadFirends;

- (void)reloadSeatsLocation;

@end

NS_ASSUME_NONNULL_END

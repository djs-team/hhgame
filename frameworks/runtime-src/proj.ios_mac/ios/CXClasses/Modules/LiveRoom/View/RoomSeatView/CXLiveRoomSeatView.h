//
//  CXLiveRoomSeatView.h
//  hairBall
//
//  Created by mahong yang on 2020/3/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXLiveRoomModel.h"
#import "VideoSession.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomSeatView : UIControl

@property (weak, nonatomic) IBOutlet UIButton *apply_seatBtn;

@property (weak, nonatomic) IBOutlet UIButton *sitdownBtn;
@property (weak, nonatomic) IBOutlet UIButton *muteBtn;

@property (weak, nonatomic) IBOutlet UIButton *seat_addFriendBtn;

@property (weak, nonatomic) IBOutlet UIImageView *seat_rank_firstImage;
@property (weak, nonatomic) IBOutlet UIImageView *seat_rank_secondImage;
@property (weak, nonatomic) IBOutlet UIImageView *seat_rank_thirdImage;


@property (nonatomic, strong) LiveRoomMicroInfo * model;

@property (nonatomic, copy) void (^seatViewActionBlock)(NSInteger tag, LiveRoomMicroInfo * microInfo);

@property (strong , nonatomic) VideoSession * session;
- (void)deleteAgoraRtc;
- (void)addAgoraRtc:(NSNumber *)uid;

@end

NS_ASSUME_NONNULL_END

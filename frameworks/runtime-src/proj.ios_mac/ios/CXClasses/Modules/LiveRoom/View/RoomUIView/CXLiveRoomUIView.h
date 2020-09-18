//
//  CXLiveRoomUIView.h
//  hairBall
//
//  Created by mahong yang on 2020/3/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "GameMessageListView.h"
#import "CXLiveRoomSeatsView.h"
#import "CXLiveRoomSeatView.h"
#import "CXHomeRoomModel.h"
#import "CXRommMusicLRCShowView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomUIView : UIView

@property (weak, nonatomic) IBOutlet UIButton *bottom_shareBtn;
@property (weak, nonatomic) IBOutlet UIButton *roomRedpackBtn;

//@property (weak, nonatomic) IBOutlet UILabel *bottom_music_reverse_numberLable;
//@property (weak, nonatomic) IBOutlet UIButton *bottom_music_new_tipBtn;

@property (nonatomic, copy) void (^bottomBtnAction)(NSInteger tag);
@property (nonatomic, copy) void (^topBtnAction)(NSInteger tag);
// 消息点击用户信息
@property (nonatomic, copy) void(^messageClickUserInfoBlock)(NSString *userID);
// 消息长按手势
@property (nonatomic, copy) void(^messageLongPressUserInfoBlock)(NSString *userName);
// 麦位点击
@property (nonatomic, copy) void(^roomSeatsViewBlock)(NSInteger tag, LiveRoomMicroInfo * _Nonnull microInfo);
// 展示全部歌词
@property (nonatomic, copy) void(^musicLRCShowViewBlock)(BOOL isSendGift);

@property (nonatomic, strong) GameMessageListView *messageListView;
@property (nonatomic, strong) CXLiveRoomSeatsView *roomSeatsView;
@property (nonatomic, strong) CXRommMusicLRCShowView *musicLRCShowView;

@property (nonatomic) CXLiveRoomModel *model;

@property (nonatomic, assign) NSInteger HeatValue; // 热度值
@property (nonatomic, strong) NSArray *ranks; // 排行榜

// 是上麦还是下麦
//@property (nonatomic, assign) BOOL isUpOrDownMicro;
@property (assign , nonatomic) BOOL isChangeMir;

// CycleScrollView
@property (nonatomic, strong) NSArray<CXHomeRoomBannerModel*> * bannerList;


@property (nonatomic, copy) void (^didSelectedCycleItem)(CXHomeRoomBannerModel *cycleItem);

- (void)reloadLayoutSubViews;

@end

NS_ASSUME_NONNULL_END

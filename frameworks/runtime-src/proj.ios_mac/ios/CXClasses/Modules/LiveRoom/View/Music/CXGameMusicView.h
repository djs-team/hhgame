//
//  CXGameMusicView.h
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef enum : NSUInteger {
    origin,
    music,
    playing,
    list
} CXGameMusicViewBtnType;

typedef enum : NSUInteger {
    music_unplay, // 未播放
    music_playing, // 播放
    music_pause, // 暂停
    music_resume, // 恢复
    music_loading, // 加载中
    music_downing, // 下载中
    music_downfail // 下载失败
} CXGameMusicPlayStatus;

typedef enum : NSUInteger {
    music_action_play, // 播放
    music_action_pause, // 暂停
    music_action_resume, // 恢复
    music_action_replay, // 重新播放
    music_action_next, // 下一首
} CXGameMusicActionStatus;

@interface CXGameMusicView : UIView

@property (nonatomic, assign) CXGameMusicViewBtnType btnType;
@property (nonatomic, assign) CXGameMusicPlayStatus musicPlayStatus;

@property (nonatomic, copy) void (^musicViewActionBlock)(CXGameMusicActionStatus status);

@property (nonatomic, strong) CXLiveRoomModel *room;

@property (nonatomic, assign) NSInteger pro_volum;
@property (nonatomic, assign) NSInteger pro_currentTime;
@property (nonatomic, assign) NSInteger pro_endTime;

- (void)reloadMusicView;

@end

NS_ASSUME_NONNULL_END

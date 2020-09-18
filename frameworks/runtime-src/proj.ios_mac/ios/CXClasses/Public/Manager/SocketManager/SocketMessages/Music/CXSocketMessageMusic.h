//
//  CXSocketMessageMusic.h
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXSocketMessageMusicModel : NSObject
@property (nonatomic, copy) NSString *music_id; // 唯一标识
@property (nonatomic, copy) NSString *SongName; // 歌曲名字
@property (nonatomic, assign) NSInteger SongType;
@property (nonatomic, copy) NSString *SongPath; // 歌曲路径
@property (nonatomic, copy) NSString *LyricPath; // 歌词路径
@property (nonatomic, copy) NSString *OriginalSingerName;
@property (nonatomic, copy) NSString *SingerName; // 原唱歌手
@property (nonatomic, assign) NSInteger Coin;
@property (nonatomic, assign) NSInteger SongMode; // 1为原唱，2为伴奏
@property (nonatomic, assign) NSInteger CreateTime;

@property (nonatomic, copy) NSString *DemandUserName; // 点播用户
@property (nonatomic, assign) NSInteger DemandUserId; // 点播用户Id
@property (nonatomic, copy) NSString *ConsertUserName; // 演唱用户
@property (nonatomic, assign) NSInteger ConsertUserId; // 演唱用户Id


@end

// 歌曲信息
@interface SocketMessageMusicListResponse : SocketMessageResponse

@property (nonatomic, copy) NSArray <CXSocketMessageMusicModel *> * SongParams;
@property (nonatomic, copy) NSArray <CXSocketMessageMusicModel *> * SongInfo;

@end

// 获取歌曲列表
@interface CXSocketMessageMusicList : SocketMessageRequest

@property (nonatomic, strong) SocketMessageMusicListResponse * response;

@property (nonatomic, assign) NSInteger SongMode; // 1为原唱，2为伴唱
@property (nonatomic, assign) NSInteger Page;
@end

// 获取预约列表
@interface CXSocketMessageMusicGetReverseList : SocketMessageRequest

@property (nonatomic, strong) SocketMessageMusicListResponse * response;

@property (nonatomic, assign) NSInteger Page;

@end

// 获取正在播放信息
@interface CXSocketMessageMusicGetPlayingDetail : SocketMessageRequest

@end

// 搜索
@interface CXSocketMessageMusicSearchList : SocketMessageRequest

@property (nonatomic, strong) SocketMessageMusicListResponse * response;

@property (nonatomic, assign) NSInteger SongMode; // 1为原唱，2为伴唱
@property (nonatomic, copy) NSString *SearchName;
@end

// 预约
@interface CXSocketMessageMusicReverse : SocketMessageRequest

@property (nonatomic, assign) NSInteger SongId;
@property (nonatomic, assign) NSInteger Level;
@property (nonatomic, assign) NSInteger Number;

@end

// 置顶
@interface CXSocketMessageMusicReverseTop : SocketMessageRequest

@property (nonatomic, copy) NSString *Id;

@end

// 删除
@interface CXSocketMessageMusicReverseDelete : SocketMessageRequest

@property (nonatomic, copy) NSString *Id;

@end

// 播放歌曲
@interface CXSocketMessageMusicPlay : SocketMessageRequest

@end

// 暂停歌曲
@interface CXSocketMessageMusicPause : SocketMessageRequest
@property BOOL IsPause;
@end

// 切歌
@interface CXSocketMessageMusicNext : SocketMessageRequest

@end

// 重播
@interface CXSocketMessageMusicReplay : SocketMessageRequest

@end

// 开始下载歌曲
@interface CXSocketMessageMusicStartDownloadSong : SocketMessageRequest

@end

// 歌曲下载成功
@interface CXSocketMessageMusicDownloadSongSuccess : SocketMessageRequest

@end

// 调音量
@interface CXSocketMessageMusicChangeVolum : SocketMessageRequest
@property float Volume;
@end

// 获取播放界面状态
@interface SocketMessageMusicPlayingViewDetailResponse : SocketMessageResponse

@property NSInteger IsPause; // 0未播放，1暂停，2恢复
@property float Volume;

@end

@interface CXSocketMessageMusicPlayingViewDetail : SocketMessageRequest

@property (nonatomic, strong) SocketMessageMusicPlayingViewDetailResponse * response;

@end

// ====================== 收到服务器回调
// 预约歌曲同步
@interface CXSocketMessageMusicReverseSync : SocketMessageNotification

@property (nonatomic, strong) NSString *DemandSongUserName; // 点歌人名称

@end

// 收到获取预约列表
@interface CXSocketMessageMusicReceiveReverseList : SocketMessageNotification

@property (nonatomic, strong) NSArray<CXSocketMessageMusicModel*> * SongInfos;
@property NSInteger AllPage;
@property NSInteger SongCount; // 预约歌曲数量

@end

// 收到正在播放信息
@interface SocketMessageMusicReceivePlayingDetail : SocketMessageNotification

@property (nonatomic, strong) CXSocketMessageMusicModel * NextSongInfo;
@property (nonatomic, strong) CXSocketMessageMusicModel * SongInfo;

@end

// 收到播放状态
@interface SocketMessageMusicReceivePlayingStatus : SocketMessageNotification

@property NSInteger IsPause; // 0未播放，1暂停，2恢复
@property float Volume;
@property NSInteger ConsertUserId; // 演唱者ID
/*
 * 切歌：0为正常切歌（房主手动切的），1为下麦自动切歌，2为下载失败自动切歌，3为点歌者玫瑰不足自动切歌
 * 下载歌曲状态：0为下载中，1为下载失败，2为下载成功
 */
@property NSInteger State;

@end

// 收到唱歌排行玫瑰数更新
@interface SocketMessageMusicUpdateSongRank : SocketMessageNotification
// 玫瑰数
@property NSInteger Rank;

@end

//====================== 获取房间内歌神排行榜 =====================
@interface CXSocketMessageMusicRankModel : NSObject
@property NSInteger UserId;
@property (nonatomic, strong) NSString *UserName;
@property (nonatomic, strong) NSString *UserImage;
@property NSInteger SongId;
@property (nonatomic, strong) NSString *SongName;
@property (nonatomic, strong) NSString *SingerName;
@property NSInteger Score;
@end

@interface SocketMessageMusicRankResponse : SocketMessageResponse

@property (nonatomic, copy) NSArray <CXSocketMessageMusicRankModel *> * SongRankDatas;

@end

@interface CXSocketMessageMusicRank : SocketMessageRequest

@property NSInteger Page;

@property (nonatomic, strong) SocketMessageMusicRankResponse * response;

@end

//====================== 歌词同步 =====================
@interface CXSocketMessageMusicUpdateSongLyric : SocketMessageRequest

// 歌词信息
@property (nonatomic, strong) NSString *Lyric;

@end

@interface SocketMessageMusicNotifySongLyric : SocketMessageNotification

// 歌词信息
@property (nonatomic, strong) NSString *Lyric;

@end




NS_ASSUME_NONNULL_END


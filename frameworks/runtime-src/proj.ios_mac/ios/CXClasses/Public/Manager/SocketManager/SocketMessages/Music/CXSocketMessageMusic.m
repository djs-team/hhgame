//
//  CXSocketMessageMusic.m
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSocketMessageMusic.h"

@implementation CXSocketMessageMusicModel
+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"music_id" : @"Id",
             };
}

- (NSInteger)SongMode {
    if (_SongMode) {
        return _SongMode;
    } else if (self.ConsertUserId > 0) {
        return 2;
    } else {
        return 1;
    }
}

@end

@implementation SocketMessageMusicListResponse
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"SongParams" : CXSocketMessageMusicModel.class,
             @"SongInfo" : CXSocketMessageMusicModel.class,
             };
}
@end

@implementation CXSocketMessageMusicList
@dynamic response;

- (Class)responseClass {
    return SocketMessageMusicListResponse.class;
}
SocketMessageInitMethod(SocketMessageIDMusicList)

@end

@implementation CXSocketMessageMusicGetReverseList
@dynamic response;

- (Class)responseClass {
    return SocketMessageMusicListResponse.class;
}
SocketMessageInitMethod(SocketMessageIDMusicGetReserveList)

@end

@implementation CXSocketMessageMusicGetPlayingDetail

SocketMessageInitMethod(SocketMessageIDMusicGetPlaySongParam)

@end

@implementation CXSocketMessageMusicSearchList
@dynamic response;

- (Class)responseClass {
    return SocketMessageMusicListResponse.class;
}
SocketMessageInitMethod(SocketMessageIDMusicSearchList)

@end

@implementation CXSocketMessageMusicReverse
SocketMessageInitMethod(SocketMessageIDMusicDianbo)

@end

@implementation CXSocketMessageMusicReverseSync

@end


@implementation CXSocketMessageMusicReverseTop
SocketMessageInitMethod(SocketMessageIDMusicSticky)

@end

@implementation CXSocketMessageMusicReverseDelete
SocketMessageInitMethod(SocketMessageIDMusicDeleteReserve)

@end

@implementation CXSocketMessageMusicPlay
SocketMessageInitMethod(SocketMessageIDMusicStartPlaySong)

@end

@implementation CXSocketMessageMusicPause
SocketMessageInitMethod(SocketMessageIDMusicStartPause)

@end

@implementation CXSocketMessageMusicNext
SocketMessageInitMethod(SocketMessageIDMusicNext)

@end

@implementation CXSocketMessageMusicReplay
SocketMessageInitMethod(SocketMessageIDMusicStartRepeat)

@end

@implementation CXSocketMessageMusicStartDownloadSong
SocketMessageInitMethod(SocketMessageIDMusicStartDownloadSong)
@end

@implementation CXSocketMessageMusicDownloadSongSuccess
SocketMessageInitMethod(SocketMessageIDMusicDownloadSongSuccess)
@end

@implementation CXSocketMessageMusicChangeVolum
SocketMessageInitMethod(SocketMessageIDMusicStartChangeVolum)

@end


@implementation SocketMessageMusicPlayingViewDetailResponse

@end
@implementation CXSocketMessageMusicPlayingViewDetail
@dynamic response;

- (Class)responseClass {
    return SocketMessageMusicPlayingViewDetailResponse.class;
}
SocketMessageInitMethod(SocketMessageIDMusicStartPlayingViewDetail)

@end




// ====================== 收到服务器回调
@implementation CXSocketMessageMusicReceiveReverseList
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"SongInfos" : [CXSocketMessageMusicModel class],
             };
}
@end

@implementation SocketMessageMusicReceivePlayingDetail

@end

@implementation SocketMessageMusicReceivePlayingStatus

@end

@implementation SocketMessageMusicUpdateSongRank

@end


@implementation CXSocketMessageMusicRankModel

@end

@implementation SocketMessageMusicRankResponse
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"SongRankDatas" : CXSocketMessageMusicRankModel.class,
             };
}
@end

@implementation CXSocketMessageMusicRank
@dynamic response;

- (Class)responseClass {
    return SocketMessageMusicRankResponse.class;
}
SocketMessageInitMethod(SocketMessageIDMusicRank)

@end

@implementation CXSocketMessageMusicUpdateSongLyric

SocketMessageInitMethod(SocketMessageIDMusicUpdateSongLyric)

@end

@implementation SocketMessageMusicNotifySongLyric


@end


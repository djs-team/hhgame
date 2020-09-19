//
//  SocketMessageJoinRoom.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN


@interface SocketMessageJoinRoomResponse : SocketMessageResponse

@property (nonatomic, strong) NSString * HuanxinRoomId;
@property (nonatomic, strong) NSString * ShengwangRoomId;
@property (nonatomic, strong) NSString * BanDesc; // 封禁说明
@property NSInteger BanTime; // 封禁时长


@end


@interface SocketMessageJoinRoom : SocketMessageRequest

@property (nonatomic, strong) SocketMessageJoinRoomResponse * response;

@property (nonatomic, strong) NSString * RoomId;

@property (nonatomic, strong) NSString * RoomName;
@property NSInteger RoomMode; // 1为相亲房，2为交友房，3为七人房，4为8人房）
@property BOOL IsExclusiveRoom;
@property BOOL IsCloseCamera;
@property BOOL IsOpenRedPacket;
@property BOOL IsOpenBreakEgg;
@property BOOL IsOpenPickSong;
@property BOOL IsOpenMediaLibrary;
@property BOOL IsOpenVideoFrame;

@end

NS_ASSUME_NONNULL_END

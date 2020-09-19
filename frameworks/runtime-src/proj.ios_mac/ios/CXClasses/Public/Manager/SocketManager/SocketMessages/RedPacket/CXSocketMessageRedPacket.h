//
//  CXSocketMessageRedPacket.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/27.
//

#import "SocketMessage.h"
#import "CXLiveRoomRedPacketModel.h"

NS_ASSUME_NONNULL_BEGIN

// 抢红包
@interface CXSocketMessageRobRedPacket : SocketMessageRequest

@end

// 广播红包列表
@interface CXSocketMessageNotifyRedPacketResultToClient : SocketMessageNotification

@property (nonatomic, strong) NSArray <CXLiveRoomRedPacketModel *>* UserRedPackets;

@end

// 广播开始抢红包
@interface CXSocketMessageNotifyStartRobRedPacket : SocketMessageNotification

@property (nonatomic, strong) NSString *Msg;

@property (nonatomic, strong) NSString *Message;
 
@end

// 广播红包进度
@interface CXSocketMessageNotifyRedPacketProgress : SocketMessageNotification
@property (nonatomic, assign) BOOL IsVisible; // 是否显示红色
@property (nonatomic, assign) float Progress; // 红包进度
@end

// 红包玩法介绍
@interface CXSocketMessageGetRedPacketPlayDescResponse : SocketMessageResponse
@property (nonatomic, strong) NSString *PlayingDesc;
@end

@interface CXSocketMessageGetRedPacketPlayDesc : SocketMessageRequest

@property (nonatomic, strong) CXSocketMessageGetRedPacketPlayDescResponse * response;

@end


NS_ASSUME_NONNULL_END

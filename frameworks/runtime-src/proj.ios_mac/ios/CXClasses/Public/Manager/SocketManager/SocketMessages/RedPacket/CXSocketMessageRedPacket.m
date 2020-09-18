//
//  CXSocketMessageRedPacket.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/27.
//

#import "CXSocketMessageRedPacket.h"

// 抢红包
@implementation CXSocketMessageRobRedPacket

SocketMessageInitMethod(SocketMessageIDRobRedPacketMessage)

@end

// 广播红包列表
@implementation CXSocketMessageNotifyRedPacketResultToClient
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"UserRedPackets" : [CXLiveRoomRedPacketModel class],
             };
}
@end

// 广播开始抢红包
@implementation CXSocketMessageNotifyStartRobRedPacket

@end

// 广播红包进度
@implementation CXSocketMessageNotifyRedPacketProgress

@end

// 红包玩法介绍
@implementation CXSocketMessageGetRedPacketPlayDescResponse

@end

@implementation CXSocketMessageGetRedPacketPlayDesc
@dynamic response;

- (Class)responseClass {
    return CXSocketMessageGetRedPacketPlayDescResponse.class;
}

SocketMessageInitMethod(SocketMessageIDGetRedPacketPlayDescMessage)

@end

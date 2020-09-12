//
//  SocketManager.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

typedef void (^SocketManagerSendRequestCallback)(__kindof SocketMessageRequest * request);

@class SocketManager;

@protocol SocketManagerDelegate <NSObject>
@optional
/// 加入成功
- (void)socketManager:(SocketManager*)mgr joinRoomSuccess:(NSString*)roomId;
/// 重连成功
- (void)socketManager:(SocketManager*)mgr reconnectionSuccess:(BOOL)success;
/// 房间内出错
- (void)socketManager:(SocketManager*)mgr room:(NSString*)roomId error:(NSError*)error;
/// 离开房间
- (void)socketManager:(SocketManager*)mgr didLeaveRoom:(NSString*)roomId;
/// 收到消息
- (void)socketManager:(SocketManager *)mgr didReceiveMessage:(id)message;

@end


@interface SocketManager : NSObject

@property (nonatomic, nullable, weak) id<SocketManagerDelegate> delegate;

- (BOOL)joinRoom:(NSString*)roomId withToken:(NSString*)token atAddr:(NSString*)address;
- (void)leaveRoom;

- (nullable __kindof SocketMessageRequest *)sendRequest:(__kindof SocketMessageRequest *)request withCallback:(nullable SocketManagerSendRequestCallback)callback;

- (void)onJoinRoom:(SocketMessageJoinRoom*)joinRoom;

@end


NS_ASSUME_NONNULL_END

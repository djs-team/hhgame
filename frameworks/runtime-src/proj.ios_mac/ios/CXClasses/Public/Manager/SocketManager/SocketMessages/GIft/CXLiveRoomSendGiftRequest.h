//
//  CXLiveRoomSendGiftRequest.h
//  hairBall
//
//  Created by mahong yang on 2020/5/18.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomSendGiftRequest : SocketMessageRequest

@end

@interface CXLiveRoomSendBlueRoseRequest : SocketMessageRequest

@property (nonatomic, strong) NSArray *UserIds;
@property (nonatomic, assign) NSInteger BlueRose;

@end

NS_ASSUME_NONNULL_END

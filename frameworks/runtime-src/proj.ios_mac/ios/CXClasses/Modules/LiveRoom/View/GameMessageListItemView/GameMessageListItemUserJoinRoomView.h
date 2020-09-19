//
//  GameMessageListItemUserJoinRoomView.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView.h"
#import "SocketMessageUserJoinRoom.h"

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageListItemUserJoinRoomView : GameMessageListItemView

@property (nonatomic, nullable) SocketMessageUserJoinRoom * model;

@end

NS_ASSUME_NONNULL_END

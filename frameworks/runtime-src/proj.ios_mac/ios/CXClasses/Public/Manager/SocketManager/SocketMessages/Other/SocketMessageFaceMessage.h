//
//  SocketMessageFaceMessage.h
//  hairBall
//
//  Created by shiwei on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageFaceMessage : SocketMessage

@property (nonatomic, strong) SocketMessageUserJoinRoom * JoinRoomUser;
@property (nonatomic, copy) NSString *face_url;

@end

NS_ASSUME_NONNULL_END

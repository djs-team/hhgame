//
//  SocketMessageRoomNameUpdate.h
//  CSharp
//
//  Created by 肖迎军 on 2019/7/28.
//  Copyright © 2019 肖迎军. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageRoomNameUpdate : SocketMessageNotification

@property (nonatomic, strong) NSString * Name;

@end

NS_ASSUME_NONNULL_END

//
//  SOcketMessageSendEmoticonResult.h
//  hairBall
//
//  Created by 肖迎军 on 2019/8/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SOcketMessageSendEmoticonResult : SocketMessageNotification

@property (nonatomic, strong) NSString * SendUserId;
@property (nonatomic, strong) NSString * ResultUrl;
@property (nonatomic, strong) NSNumber * MicroLevel;
@property (nonatomic, strong) NSNumber * MicroNumber;
@property (nonatomic, strong) NSNumber * ConnectionId;

- (NSIndexPath*)index;

@end

NS_ASSUME_NONNULL_END

//
//  SOcketMessageSendEmoticonBegin.h
//  hairBall
//
//  Created by 肖迎军 on 2019/8/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SOcketMessageSendEmoticonBegin : SocketMessageNotification

@property (nonatomic, strong) NSString * EmoticonAnimationUrl;
@property (nonatomic, strong) NSNumber * MicroLevel;
@property (nonatomic, strong) NSNumber * MicroNumber;
@property (nonatomic, strong) NSString * ConnectionId;

- (NSIndexPath*)index;

@end

NS_ASSUME_NONNULL_END

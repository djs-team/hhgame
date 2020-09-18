//
//  SocketMessageUserDisableChatMessage.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/23.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageUserDisableChatMessage : SocketMessageNotification

@property (nonatomic, strong) NSNumber * IsDisableMsg;

@end

NS_ASSUME_NONNULL_END

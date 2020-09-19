//
//  SocketMessageSetTagId.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageSetTagId : SocketMessageRequest

@property (nonatomic, strong) NSNumber * TagId;
@property (nonatomic, strong) NSNumber * ModelId;

@end

NS_ASSUME_NONNULL_END

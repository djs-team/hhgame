//
//  SocketMessageSendGift.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"
#import "SocketMessageGroupGift.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageSendGift : SocketMessageRequest

@property (nonatomic, strong) SocketMessageGroupGiftResponse * response;

@property (nonatomic, strong) NSNumber * Id;
@property (nonatomic, strong) NSNumber * GiftId;
@property (nonatomic, strong) NSNumber * Count;
@property (nonatomic, assign) BOOL IsUseBag;

@end

NS_ASSUME_NONNULL_END

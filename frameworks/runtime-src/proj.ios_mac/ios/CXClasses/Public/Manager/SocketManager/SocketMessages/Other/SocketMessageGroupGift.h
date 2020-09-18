//
//  SocketMessageGroupGift.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN


@interface SocketMessageGroupGiftResponse : SocketMessageResponse

@property (nonatomic, strong) NSNumber * Balance;

@end


@interface SocketMessageGroupGiftSeat : NSObject

@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;

@end


@interface SocketMessageGroupGift : SocketMessageRequest

@property (nonatomic, strong) SocketMessageGroupGiftResponse * response;

@property (nonatomic, strong) NSNumber * IsWholeMicro;
@property (nonatomic, strong) NSNumber * GiftId;
@property (nonatomic, strong) NSNumber * Count;
@property (nonatomic, strong) NSArray<SocketMessageGroupGiftSeat*> * Micros;
@property (nonatomic, assign) BOOL IsUseBag;

@end

NS_ASSUME_NONNULL_END

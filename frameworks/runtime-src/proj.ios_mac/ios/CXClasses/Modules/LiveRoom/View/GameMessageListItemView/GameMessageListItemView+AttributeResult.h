//
//  GameMessageListItemView+AttributeResult.h
//  hairBall
//
//  Created by shiwei on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView.h"

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageListItemView (AttributeResult)

- (NSMutableAttributedString *)resultWithRoomGuardLevel:(NSString *)RoomGuardLevel DukeLevel:(NSNumber *)DukeLevel UserLevel:(NSNumber *)UserLevel UserIdentity:(NSNumber *)UserIdentity Font:(UIFont *)font UserID:(NSString *)UserID;

@end

NS_ASSUME_NONNULL_END

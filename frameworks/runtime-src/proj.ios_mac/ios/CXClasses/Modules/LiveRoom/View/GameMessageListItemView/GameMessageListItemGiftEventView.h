//
//  GameMessageListItemGiftEventView.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView.h"
#import "SocketMessageGiftEvent.h"

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageListItemGiftEventView : GameMessageListItemView

@property (nonatomic, nullable) SocketMessageGiftEvent * model;

@end

NS_ASSUME_NONNULL_END

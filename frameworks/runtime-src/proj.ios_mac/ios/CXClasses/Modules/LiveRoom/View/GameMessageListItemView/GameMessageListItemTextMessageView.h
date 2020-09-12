//
//  GameMessageListItemTextMessageView.h
//  hairBall
//
//  Created by shiwei on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView.h"
#import "SocketMessageTextMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageListItemTextMessageView : GameMessageListItemView

@property (nonatomic, nullable) SocketMessageTextMessage *model;

@end

NS_ASSUME_NONNULL_END

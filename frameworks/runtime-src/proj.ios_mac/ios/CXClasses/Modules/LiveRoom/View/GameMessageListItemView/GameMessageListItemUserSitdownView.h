//
//  GameMessageListItemUserSitdownView.h
//  hairBall
//
//  Created by shiwei on 2019/7/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView.h"
#import "SocketMessageUserSitdown.h"

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageListItemUserSitdownView : GameMessageListItemView

@property (nonatomic, nullable) SocketMessageUserSitdown * model;

@end

NS_ASSUME_NONNULL_END

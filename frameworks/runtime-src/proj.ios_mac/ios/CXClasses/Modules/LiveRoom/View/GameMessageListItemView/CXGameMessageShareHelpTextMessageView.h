//
//  CXGameMessageShareHelpTextMessageView.h
//  hairBall
//
//  Created by mahong yang on 2020/5/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView.h"
#import "CXSocketMessageSystemRequest.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMessageShareHelpTextMessageView : GameMessageListItemView

@property (nonatomic, nullable) CXSocketMessageSystemShareHelpNotification *model;

@end

NS_ASSUME_NONNULL_END


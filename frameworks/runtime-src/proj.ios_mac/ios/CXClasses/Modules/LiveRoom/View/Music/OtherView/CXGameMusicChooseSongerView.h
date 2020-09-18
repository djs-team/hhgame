//
//  CXGameMusicChooseSongerView.h
//  hairBall
//
//  Created by mahong yang on 2020/2/11.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicChooseSongerView : MMPopupView

@property (nonatomic, strong) CXLiveRoomModel *room;

@property (nonatomic, copy) void (^songerPlayBlock)(LiveRoomUser *user);

@end

NS_ASSUME_NONNULL_END

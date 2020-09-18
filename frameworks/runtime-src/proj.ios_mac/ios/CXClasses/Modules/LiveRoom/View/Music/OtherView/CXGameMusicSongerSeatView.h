//
//  CXGameMusicSongerSeatView.h
//  hairBall
//
//  Created by mahong yang on 2020/2/11.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicSongerSeatView : UIView

@property (nonatomic, copy) void (^songerSeatSureActionBlock)(LiveRoomUser *user);

@property (nonatomic, strong) LiveRoomUser *user;

@end

NS_ASSUME_NONNULL_END

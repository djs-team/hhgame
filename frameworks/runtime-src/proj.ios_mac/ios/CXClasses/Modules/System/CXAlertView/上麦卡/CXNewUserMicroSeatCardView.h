//
//  CXNewUserMicroSeatCardView.h
//  hairBall
//
//  Created by mahong yang on 2020/6/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXNewUserMicroSeatCardView : MMPopupView

@property (nonatomic, copy) void (^userMicroSeatCardJoinBlock)(void);

@end

NS_ASSUME_NONNULL_END

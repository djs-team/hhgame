//
//  CXMuaGiftSendBlueRoseView.h
//  hairBall
//
//  Created by mahong yang on 2020/5/16.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXMuaGiftSendBlueRoseView : MMPopupView

@property (nonatomic, copy) void (^sendBlueRoseBlock) (NSInteger count);

@end

NS_ASSUME_NONNULL_END

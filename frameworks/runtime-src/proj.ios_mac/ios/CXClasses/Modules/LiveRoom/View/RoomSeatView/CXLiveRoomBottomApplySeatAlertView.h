//
//  CXLiveRoomBottomApplySeatAlertView.h
//  hairBall
//
//  Created by mahong yang on 2020/6/11.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomBottomApplySeatAlertView : UIView

@property (nonatomic, strong) SocketMessageMicroOrder *microOrder;

@property (nonatomic, copy) void (^applySeatAlertViewBlock)(BOOL isSure, BOOL isCancel, SocketMessageMicroOrder *microOrder);

@end

NS_ASSUME_NONNULL_END

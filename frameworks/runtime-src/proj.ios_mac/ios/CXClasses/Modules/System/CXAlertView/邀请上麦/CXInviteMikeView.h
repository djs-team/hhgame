//
//  CXInviteMikeView.h
//  hairBall
//
//  Created by mahong yang on 2019/11/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXSytemModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXInviteMikeView : UIView

@property (nonatomic, strong) CXInviteMikeModel *mikeModel;

@property (nonatomic, copy) void (^cancelActionBlcok)(CXInviteMikeModel *model);

@property (nonatomic, copy) void (^agreeActionBlcok)(CXInviteMikeModel *model);

@end

NS_ASSUME_NONNULL_END

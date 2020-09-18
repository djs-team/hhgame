//
//  CXMySettingWechatBindView.h
//  hairBall
//
//  Created by mahong yang on 2020/4/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXMySettingWechatBindView : MMPopupView
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;

@property (nonatomic, copy) void (^bindViewBlock)(NSInteger tag);

@end

NS_ASSUME_NONNULL_END

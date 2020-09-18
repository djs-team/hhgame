//
//  MuaExchangeResultTipView.h
//  hairBall
//
//  Created by shiwei on 2019/7/25.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface MuaExchangeResultTipView : UIView

+ (instancetype)tipView;

@property (weak, nonatomic) IBOutlet UILabel *statusLabel;
@property (weak, nonatomic) IBOutlet UIButton *button1;
@property (weak, nonatomic) IBOutlet UIButton *button2;

@property (nonatomic, copy) void(^button1Click)(void);
@property (nonatomic, copy) void(^button2Click)(void);

@end

NS_ASSUME_NONNULL_END

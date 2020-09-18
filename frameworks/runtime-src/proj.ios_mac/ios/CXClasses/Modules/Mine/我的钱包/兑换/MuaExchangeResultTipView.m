//
//  MuaExchangeResultTipView.m
//  hairBall
//
//  Created by shiwei on 2019/7/25.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MuaExchangeResultTipView.h"

@interface MuaExchangeResultTipView ()

@end

@implementation MuaExchangeResultTipView

+ (instancetype)tipView {
    return [[NSBundle mainBundle] loadNibNamed:@"MuaExchangeResultTipView" owner:nil options:nil].lastObject;
}

- (IBAction)button1Click:(id)sender {
    if (self.button1Click) {
        self.button1Click();
    }
}

- (IBAction)button2Click:(id)sender {
    if (self.button2Click) {
        self.button2Click();
    }
}


@end

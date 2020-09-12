//
//  CXGameMusicLRCLabel.m
//  hairBall
//
//  Created by mahong yang on 2020/2/18.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicLRCLabel.h"

@implementation CXGameMusicLRCLabel

- (void)setProgress:(CGFloat)progress {
    _progress = progress;
    
    [self setNeedsDisplay];
}

- (void)drawRect:(CGRect)rect {
    [super drawRect:rect];
    
    // 填充颜色
    [[UIColor redColor] set];
    
    // 设置改变颜色的宽度
    CGRect setRect = CGRectMake(rect.origin.x, rect.origin.y, rect.size.width * _progress, rect.size.height);
    
    // label颜色混合模式填充
    UIRectFillUsingBlendMode(setRect, kCGBlendModeSourceIn);
}

@end

//
//  CXExchangeBlueRoseCell.m
//  hairBall
//
//  Created by mahong yang on 2020/4/3.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXExchangeBlueRoseCell.h"

@implementation CXExchangeBlueRoseCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _bgView.layer.masksToBounds = YES;
    _bgView.layer.cornerRadius = 5;
    _bgView.layer.borderColor = UIColorHex(0x7CC7DE).CGColor;
    _bgView.layer.borderWidth = 0.5;
}

@end

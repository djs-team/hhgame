//
//  CXGameMusicAdjustEffectsCell.m
//  hairBall
//
//  Created by mahong yang on 2020/3/20.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicAdjustEffectsCell.h"

@implementation CXGameMusicAdjustEffectsCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.itemNameLabel.layer.masksToBounds = true;
    self.itemNameLabel.layer.cornerRadius = 10;
}

@end

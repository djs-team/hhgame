//
//  CXLiveRoomSetupTypeHelpCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomSetupTypeHelpCell.h"

@implementation CXLiveRoomSetupTypeHelpCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    _room_typeLabel.layer.masksToBounds = YES;
    _room_typeLabel.layer.cornerRadius = 12;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

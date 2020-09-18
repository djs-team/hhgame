//
//  CXSystemMessageCell.m
//  hairBall
//
//  Created by mahong yang on 2019/11/1.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSystemMessageCell.h"

@implementation CXSystemMessageCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.contentBGView.layer.cornerRadius = 6;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

//
//  CXGameMusicOrginMusicCell.m
//  hairBall
//
//  Created by mahong yang on 2020/2/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicOrginMusicCell.h"

@implementation CXGameMusicOrginMusicCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.reserveBtn.layer.masksToBounds = YES;
    self.reserveBtn.layer.cornerRadius = 10;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)reserveAction:(id)sender {
    if (self.reserveActionBlock) {
        self.reserveActionBlock();
    }
}

@end

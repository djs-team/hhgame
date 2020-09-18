//
//  CXGameMusicReserveMusicCell.m
//  hairBall
//
//  Created by mahong yang on 2020/2/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicReserveMusicCell.h"

@implementation CXGameMusicReserveMusicCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)topAction:(id)sender {
    if ([CXClientModel instance].room.isHost == YES) {
        if (self.reserveMusicActionBlock) {
            self.reserveMusicActionBlock(YES, NO);
        }
    }
}

- (IBAction)deleteAction:(id)sender {
    if (self.isEnableDelete == YES) {
        if (self.reserveMusicActionBlock) {
            self.reserveMusicActionBlock(NO, YES);
        }
    }
    
}

@end

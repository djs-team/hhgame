//
//  CXLIveRoomSetupRoomAudioStatusCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLIveRoomSetupRoomAudioStatusCell.h"

@implementation CXLIveRoomSetupRoomAudioStatusCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setIsCloseCamera:(BOOL)IsCloseCamera {
    _IsCloseCamera = IsCloseCamera;
    if (_IsCloseCamera == YES) {
        [_audio_noBtn setImage:[UIImage imageNamed:@"room_setup_circle"] forState:UIControlStateNormal];
        [_audio_yesBtn setImage:[UIImage imageNamed:@"room_setup_circle_selected"] forState:UIControlStateNormal];
    } else {
        [_audio_noBtn setImage:[UIImage imageNamed:@"room_setup_circle_selected"] forState:UIControlStateNormal];
        [_audio_yesBtn setImage:[UIImage imageNamed:@"room_setup_circle"] forState:UIControlStateNormal];
    }
}

- (IBAction)exclusiveAction:(UIButton *)sender {
    if (sender.tag == 10) { // 否
        if (self.setupRoomTypeAudioActionBlock) {
            self.setupRoomTypeAudioActionBlock(NO);
        }
        [_audio_noBtn setImage:[UIImage imageNamed:@"room_setup_circle_selected"] forState:UIControlStateNormal];
        [_audio_yesBtn setImage:[UIImage imageNamed:@"room_setup_circle"] forState:UIControlStateNormal];
    } else { // 是
        if (self.setupRoomTypeAudioActionBlock) {
            self.setupRoomTypeAudioActionBlock(YES);
        }
        [_audio_noBtn setImage:[UIImage imageNamed:@"room_setup_circle"] forState:UIControlStateNormal];
        [_audio_yesBtn setImage:[UIImage imageNamed:@"room_setup_circle_selected"] forState:UIControlStateNormal];
    }
}

@end

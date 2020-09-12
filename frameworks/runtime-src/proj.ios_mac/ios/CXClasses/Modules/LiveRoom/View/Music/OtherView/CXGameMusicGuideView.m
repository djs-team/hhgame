//
//  CXGameMusicGuideView.m
//  hairBall
//
//  Created by mahong yang on 2020/5/27.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicGuideView.h"

@implementation CXGameMusicGuideView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(315, 555));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = NO;
    self.type = MMPopupTypeCustom;
    
}

- (IBAction)guideBtnAction:(UIButton *)sender {
//    if (sender.tag == 10) {
//        [sender setImage:[UIImage imageNamed:@"live_room_music_guide_1"] forState:UIControlStateNormal];
//        sender.tag = 11;
//    } else
    if (sender.tag == 10) {
        [sender setImage:[UIImage imageNamed:@"live_room_music_guide_2"] forState:UIControlStateNormal];
        sender.tag = 12;
    } else if (sender.tag == 12) {
        [sender setImage:[UIImage imageNamed:@"live_room_music_guide_3"] forState:UIControlStateNormal];
        sender.tag = 13;
    } else if (sender.tag == 13) {
        [sender setImage:[UIImage imageNamed:@"live_room_music_guide_4"] forState:UIControlStateNormal];
        sender.tag = 14;
    } else if (sender.tag == 14) {
        [sender setImage:[UIImage imageNamed:@"live_room_music_guide_5"] forState:UIControlStateNormal];
        sender.tag = 15;
    } else if (sender.tag == 15) {
        [[NSUserDefaults standardUserDefaults] setBool:YES forKey:@"CXLiveRoomCXGameMusicViewFirst"];
        
        [self hide];
        [MMPopupView hideAll];
    }
}

@end

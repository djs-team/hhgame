//
//  CXGameMusicSongerTipView.m
//  hairBall
//
//  Created by mahong yang on 2020/5/26.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicSongerTipView.h"

@implementation CXGameMusicSongerTipView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(285, 225));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
    
    self.sureButton.layer.masksToBounds = YES;
    self.sureButton.layer.cornerRadius = 20;
}

- (IBAction)sureAction:(id)sender {
    [self hide];
    [MMPopupView hideAll];
}

@end

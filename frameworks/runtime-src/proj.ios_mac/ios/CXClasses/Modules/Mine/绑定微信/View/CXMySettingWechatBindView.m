//
//  CXMySettingWechatBindView.m
//  hairBall
//
//  Created by mahong yang on 2020/4/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXMySettingWechatBindView.h"

@interface CXMySettingWechatBindView()

@end

@implementation CXMySettingWechatBindView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(280, 330));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
}

- (IBAction)clickBtnAction:(UIButton *)sender {
    if (self.bindViewBlock) {
        self.bindViewBlock(sender.tag);
    }
    
    [self hide];
}

@end

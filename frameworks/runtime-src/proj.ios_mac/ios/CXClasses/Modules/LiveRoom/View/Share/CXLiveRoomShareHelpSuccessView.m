//
//  CXLiveRoomShareHelpSuccessView.m
//  hairBall
//
//  Created by mahong yang on 2020/5/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomShareHelpSuccessView.h"

@implementation CXLiveRoomShareHelpSuccessView


- (void)awakeFromNib {
    [super awakeFromNib];

    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(250, 281));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
    
    _closeBtn.layer.masksToBounds = YES;
    _closeBtn.layer.cornerRadius = 22;
}

- (IBAction)closeAction:(id)sender {
    
    [self hide];
}
@end

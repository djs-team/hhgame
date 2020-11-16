//
//  CXNewUserMicroSeatCardView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXNewUserMicroSeatCardView.h"

@interface CXNewUserMicroSeatCardView()

@property (weak, nonatomic) IBOutlet UIButton *joinBtn;

@property (weak, nonatomic) IBOutlet UILabel *card_numLabel;

@end

@implementation CXNewUserMicroSeatCardView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(300, 315));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
    
//    self.joinBtn.layer.masksToBounds = YES;
//    self.joinBtn.layer.cornerRadius = 21;
//
//    [_joinBtn setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(185, 42) Color1:UIColorHex(0xDBB292) color2:UIColorHex(0xF7DFC4) endPoint:CGPointMake(1, 0)] forState:UIControlStateNormal];
//
    self.card_numLabel.text = [NSString stringWithFormat:@"系统赠送您上麦卡%@张", [CXClientModel instance].card_num.stringValue];
}

- (IBAction)joinAction:(id)sender {
    if (self.userMicroSeatCardJoinBlock) {
        self.userMicroSeatCardJoinBlock();
    }
    
    [self hide];
    [MMPopupView hideAll];
}

@end

//
//  CXLiveRoomRechargeSheepView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/14.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomRechargeSheepView.h"

@interface CXLiveRoomRechargeSheepView()
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *rose_numberLabel;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *reose_rmbLabel;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *receiveBtn;

@end

@implementation CXLiveRoomRechargeSheepView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(kScreenWidth, kScreenWidth));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeSheet;
    
    _rose_numberLabel.layer.masksToBounds = YES;
    _rose_numberLabel.layer.cornerRadius = 8;
    _reose_rmbLabel.layer.masksToBounds = YES;
    _reose_rmbLabel.layer.cornerRadius = 8;
    _receiveBtn.layer.masksToBounds = YES;
    _receiveBtn.layer.cornerRadius = 4;
    
    UIImage *gImage = [UIImage gradientImageWithSize:CGSizeMake(110, 60) Color1:UIColorHex(0xFCDD8D) color2:UIColorHex(0xFBD063) endPoint:CGPointMake(1, 0)];
    [_rose_numberLabel setBackgroundImage:gImage forState:UIControlStateNormal];
    [_reose_rmbLabel setBackgroundImage:gImage forState:UIControlStateNormal];
    [_receiveBtn setBackgroundImage:gImage forState:UIControlStateNormal];
}

- (void)setupRoseNumber:(NSString *)roseNumber roseRMB:(NSString *)reoseRMB {
    NSMutableAttributedString *numberAttr = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@朵玫瑰", roseNumber]];
    [numberAttr addAttribute:NSForegroundColorAttributeName value:UIColorHex(0xE53611) range:NSMakeRange(0, roseNumber.length+3)];
    [numberAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:17] range:NSMakeRange(0, roseNumber.length+3)];
    [numberAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:22] range:NSMakeRange(0, roseNumber.length)];
    [_rose_numberLabel setAttributedTitle:numberAttr forState:UIControlStateNormal];
    
    NSMutableAttributedString *rmbAttr = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@元", reoseRMB]];
    [rmbAttr addAttribute:NSForegroundColorAttributeName value:UIColorHex(0xFFFFFF) range:NSMakeRange(0, reoseRMB.length+1)];
    [rmbAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:17] range:NSMakeRange(0, reoseRMB.length+1)];
    [rmbAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:22] range:NSMakeRange(0, reoseRMB.length)];
    [_reose_rmbLabel setAttributedTitle:rmbAttr forState:UIControlStateNormal];
}

- (IBAction)closeAction:(id)sender {
    if (self.rechargeSheetViewBlcok) {
        self.rechargeSheetViewBlcok(NO, YES);
    }
    
    [self hide];
}

- (IBAction)rechargeAction:(id)sender {
    if (self.rechargeSheetViewBlcok) {
        self.rechargeSheetViewBlcok(YES, NO);
    }
    
    [self hide];
}
@end

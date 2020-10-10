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

@property (weak, nonatomic) IBOutlet UIView *thirdPayView;
@property (weak, nonatomic) IBOutlet UIButton *wechatBtn;
@property (weak, nonatomic) IBOutlet UIButton *zfbBtn;

@property (nonatomic, strong) NSString *action;

@end

@implementation CXLiveRoomRechargeSheepView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(kScreenWidth, 396*SCALE_W));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeSheet;
    
    _rose_numberLabel.layer.masksToBounds = YES;
    _rose_numberLabel.layer.cornerRadius = 10;
    _reose_rmbLabel.layer.masksToBounds = YES;
    _reose_rmbLabel.layer.cornerRadius = 10;
    _receiveBtn.layer.masksToBounds = YES;
    _receiveBtn.layer.cornerRadius = 22;
    
//    UIImage *gImage = [UIImage gradientImageWithSize:CGSizeMake(110, 60) Color1:UIColorHex(0xFCDD8D) color2:UIColorHex(0xFBD063) endPoint:CGPointMake(1, 0)];
//    [_rose_numberLabel setBackgroundImage:gImage forState:UIControlStateNormal];
//    [_reose_rmbLabel setBackgroundImage:gImage forState:UIControlStateNormal];
//    [_receiveBtn setBackgroundImage:gImage forState:UIControlStateNormal];
    
    if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) { // 苹果支付
        self.thirdPayView.hidden = YES;
    }
}

- (void)setupRoseNumber:(NSString *)roseNumber roseRMB:(NSString *)reoseRMB {
    NSMutableAttributedString *numberAttr = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@朵玫瑰", roseNumber]];
    [numberAttr addAttribute:NSForegroundColorAttributeName value:UIColorHex(0xE53611) range:NSMakeRange(0, roseNumber.length+3)];
    [numberAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:18] range:NSMakeRange(0, roseNumber.length+3)];
    [numberAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:24] range:NSMakeRange(0, roseNumber.length)];
    [_rose_numberLabel setAttributedTitle:numberAttr forState:UIControlStateNormal];
    
    NSMutableAttributedString *rmbAttr = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@元", reoseRMB]];
    [rmbAttr addAttribute:NSForegroundColorAttributeName value:UIColorHex(0xFFFFFF) range:NSMakeRange(0, reoseRMB.length+1)];
    [rmbAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:18] range:NSMakeRange(0, reoseRMB.length+1)];
    [rmbAttr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:24] range:NSMakeRange(0, reoseRMB.length)];
    [_reose_rmbLabel setAttributedTitle:rmbAttr forState:UIControlStateNormal];
}

- (IBAction)closeAction:(id)sender {
    if (self.rechargeSheetViewBlcok) {
        self.rechargeSheetViewBlcok(NO, YES, @"");
    }
    
    [self hide];
}

- (IBAction)wechatAction:(id)sender {
    _action = @"weixin";
    [_wechatBtn setImage:[UIImage imageNamed:@"selected_on"] forState:UIControlStateNormal];
    [_zfbBtn setImage:[UIImage imageNamed:@"selected_off"] forState:UIControlStateNormal];
}
- (IBAction)zfbAction:(id)sender {
    _action = @"alipay";
    [_wechatBtn setImage:[UIImage imageNamed:@"selected_off"] forState:UIControlStateNormal];
    [_zfbBtn setImage:[UIImage imageNamed:@"selected_on"] forState:UIControlStateNormal];
}

- (IBAction)rechargeAction:(id)sender {
    if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) { // 苹果支付
        if (self.rechargeSheetViewBlcok) {
            self.rechargeSheetViewBlcok(YES, NO, @"Apple");
        }
    } else {
        if (_action.length <= 0) {
            [self showAlertWithMessage:@"请选择支付方式"];
            return;
        }
        
        if (self.rechargeSheetViewBlcok) {
            self.rechargeSheetViewBlcok(YES, NO, _action);
        }
    }
    
    [self hide];
}
@end

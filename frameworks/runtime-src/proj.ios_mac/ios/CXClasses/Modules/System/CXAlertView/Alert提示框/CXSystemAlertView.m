//
//  CXSystemAlertView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/15.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSystemAlertView.h"

@interface CXSystemAlertView() {
    AlertViewCancelBlock _cancelBlock;
    AlertViewSureBlock _sureBlock;
}

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;
@property (weak, nonatomic) IBOutlet UIButton *sureBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *titleTopLayout;

@end

@implementation CXSystemAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 12;
    
    _sureBtn.layer.masksToBounds = YES;
    _sureBtn.layer.cornerRadius = 18;
    [_sureBtn setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(110, 36) Color1:UIColorHex(0xEC5BBA) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    
    _cancelBtn.layer.masksToBounds = YES;
    _cancelBtn.layer.cornerRadius = 18;
    _cancelBtn.layer.borderColor = UIColorHex(0x7B3EF3).CGColor;
    _cancelBtn.layer.borderWidth = 1;
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
         make.size.mas_equalTo(CGSizeMake(kScreenWidth - 40, 200));
    }];
     
//    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
}

+ (CXSystemAlertView *)loadNib {
    return [[NSBundle mainBundle] loadNibNamed:@"CXSystemAlertView" owner:nil options:nil].firstObject;
}

- (void)showAlertTitle:(NSString *)title message:(NSString *)message cancel:(AlertViewCancelBlock)cancelBlock sure:(AlertViewSureBlock)sureBlock {
    _titleLabel.text = title;
    _messageLabel.text = message;
    _cancelBlock = cancelBlock;
    _sureBlock = sureBlock;
    if (message.length == 0) {
        _titleTopLayout.constant = 60;
    }
}

- (IBAction)cancelAction:(id)sender {
    if (_cancelBlock) {
        _cancelBlock();
    }
    [self hide];
    [MMPopupView hideAll];
}

- (IBAction)sureAction:(id)sender {
    if (_sureBlock) {
        _sureBlock();
    }
    [self hide];
    [MMPopupView hideAll];
}

@end

//
//  CXMineGuardAutomaticView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "CXMineGuardAutomaticView.h"

@interface CXMineGuardAutomaticView()

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UIView *open_topItemView;
@property (weak, nonatomic) IBOutlet UIButton *firstBtn;
@property (weak, nonatomic) IBOutlet UIButton *secondBtn;
@property (weak, nonatomic) IBOutlet UIButton *thirdBtn;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;

@property (weak, nonatomic) IBOutlet UILabel *userLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *timeLabelBottomLayout;
@property (weak, nonatomic) IBOutlet UIButton *sureBtn;
@property (weak, nonatomic) IBOutlet UIButton *cancelBtn;

@end

@implementation CXMineGuardAutomaticView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(280, 255));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;

    _sureBtn.layer.masksToBounds = YES;
    _sureBtn.layer.cornerRadius = 18;
    [_sureBtn setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(113, 36) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    
    _cancelBtn.layer.masksToBounds = YES;
    _cancelBtn.layer.cornerRadius = 18;
    _cancelBtn.layer.borderWidth = 1;
    _cancelBtn.layer.borderColor = UIColorHex(0x7B3EF3).CGColor;
}

- (void)setUser:(CXUserModel *)user {
    _user = user;
    self.userLabel.text = [NSString stringWithFormat:@"%@(ID:%@)",user.nickname, user.user_id];
    self.timeLabel.text = [NSString stringWithFormat:@"守护到期时间：%@", user.end_time];
    if ([user.is_auto integerValue] == 1) { // 已经开通自动续费
        self.titleLabel.text = @"关闭守护自动续费";
        self.open_topItemView.hidden = YES;
        self.timeLabelBottomLayout.constant = 22;
        self.descLabel.text = @"请在守护到期前续费，否则到期后亲密值会清零，也会失去守护特权。";
    }
}

- (IBAction)sureAction:(id)sender {
}

- (IBAction)cancelAction:(id)sender {
}

@end

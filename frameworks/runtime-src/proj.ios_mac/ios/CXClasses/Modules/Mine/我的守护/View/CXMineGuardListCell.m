//
//  CXMineGuardListCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "CXMineGuardListCell.h"

@implementation CXMineGuardListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    UIView *view = [[UIView alloc] init];
    view.frame = CGRectMake(14,5,kScreenWidth - 28,130);
    view.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0];
    view.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.17].CGColor;
    view.layer.shadowOffset = CGSizeMake(0,0);
    view.layer.shadowOpacity = 1;
    view.layer.shadowRadius = 14;
    view.layer.cornerRadius = 10;
    [self.contentView insertSubview:view atIndex:0];
    
    self.avatar.layer.masksToBounds = YES;
    self.avatar.layer.cornerRadius = 28;
    self.avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    self.avatar.layer.borderWidth = 0.5;
    
    _avatar.userInteractionEnabled = YES;
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(avatarTapAction:)];
    //为图片添加手势
    [_avatar addGestureRecognizer:singleTap];
    
    self.renewBtn.layer.masksToBounds = YES;
    self.renewBtn.layer.cornerRadius = 12;
    self.renewBtn.layer.borderColor = UIColorHex(0x7B3EF3).CGColor;
    self.renewBtn.layer.borderWidth = 1;
    
    self.automaticBtn.layer.masksToBounds = YES;
    self.automaticBtn.layer.cornerRadius = 12;
    UIImage *image = [[UIImage gradientImageWithSize:CGSizeMake(75, 24) Color1:UIColorHex(0xEC5BBA) color2:UIColorHex(0x7D3EF1)] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    [self.automaticBtn setBackgroundImage:image forState:UIControlStateNormal];
    
    self.stateLabel.layer.masksToBounds = YES;
    self.stateLabel.layer.cornerRadius = 8;
}

- (void)avatarTapAction:(UITapGestureRecognizer *)gesture {
    if (self.avatarTapGestureBlock) {
        self.avatarTapGestureBlock();
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(CXUserModel *)model {
    _model = model;
    
    if ([_model.nickname length] > 0) {
        self.nicknameLabel.text = _model.nickname;
    } else{
        self.nicknameLabel.text = @"用户名称";
    }
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:_model.avatar]];
    if (model.room_id.length > 0) {
        _avatar.userInteractionEnabled = YES;
    } else {
        _avatar.userInteractionEnabled = NO;
    }
    
    [self.sexBtn setTitle:model.age forState:UIControlStateNormal];
    if ([model.sex integerValue] == 1) {
        [self.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    [self.locationBtn setTitle:model.city forState:UIControlStateNormal];

    _stateLabel.hidden = NO;
    if (model.state.integerValue == 2 || model.state.integerValue == 3) {
        _stateLabel.text = @"相亲中";
        _stateLabel.backgroundColor = UIColorHex(0x7F3EF0);
    } else if (model.state.integerValue == 4 || model.state.integerValue == 5) {
        _stateLabel.text = @"热聊中";
        _stateLabel.backgroundColor = UIColorHex(0xEF51B2);
    } else if (model.state.integerValue == 6) {
        _stateLabel.text = @"开播中";
        _stateLabel.backgroundColor = UIColorHex(0xFEBF00);
    } else {
        _stateLabel.hidden = YES;
    }
    
    self.descLabel.text = model.intro;
    
    self.timeLabel.text = [NSString stringWithFormat:@"守护到期时间：%@", model.end_time];
    self.expiredLabel.text = [NSString stringWithFormat:@"还有%@天到期", model.countdown_day.stringValue];
    self.intimacyLabel.text = [NSString stringWithFormat:@"亲密值：%@", model.intimacy];
    
    if (_isMineGuard) {
        self.renewBtn.hidden = NO;
        if ([model.is_auto integerValue] == 1) { // 是自动续费
            [self.automaticBtn setTitle:@"关闭自助续费" forState:UIControlStateNormal];
        } else {
            [self.automaticBtn setTitle:@"开通自助续费" forState:UIControlStateNormal];
        }
    } else {
        self.renewBtn.hidden = YES;
        self.automaticBtn.hidden = YES;
    }
}

- (IBAction)renewAction:(id)sender {
    if (self.renewActionBlock) {
        self.renewActionBlock();
    }
}
- (IBAction)automaticAction:(id)sender {
    if (self.automaticActionBlock) {
        self.automaticActionBlock();
    }
}
@end

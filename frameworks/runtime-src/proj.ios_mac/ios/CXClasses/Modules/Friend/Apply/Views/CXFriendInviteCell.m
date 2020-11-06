//
//  CXFriendInviteCell.m
//  hairBall
//
//  Created by mahong yang on 2019/10/28.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXFriendInviteCell.h"

@implementation CXFriendInviteCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.avatar.layer.masksToBounds = YES;
    self.avatar.layer.cornerRadius = 28;
    _avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    _avatar.layer.borderWidth = 0.5;
    
    self.agreeButton.layer.masksToBounds = YES;
    self.agreeButton.layer.cornerRadius = 12;
    [self.agreeButton setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(76, 24) Color1:UIColorHex(0xEC5BBA) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    
    self.rejestButton.layer.masksToBounds = YES;
    self.rejestButton.layer.cornerRadius = 12;
    self.rejestButton.layer.borderWidth = 1;
    self.rejestButton.layer.borderColor = UIColorHex(0x7B3EF3).CGColor;
    
    self.stateLabel.layer.masksToBounds = YES;
    self.stateLabel.layer.cornerRadius = 12;
    
    self.onlineStateLabel.layer.masksToBounds = YES;
    self.onlineStateLabel.layer.cornerRadius = 8;
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(avatarTapAction:)];
    //为图片添加手势
    [_avatar addGestureRecognizer:singleTap];
    _avatar.userInteractionEnabled = YES;
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

- (void)setInviteModel:(CXFriendInviteModel *)inviteModel {
    _inviteModel = inviteModel;
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:inviteModel.avatar]];
    self.user_nameLabel.text = inviteModel.nickname;
    [self.sexBtn setTitle:inviteModel.age forState:UIControlStateNormal];
    if ([inviteModel.sex isEqualToString:@"1"]) {
        [self.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    self.useridLabel.text = [NSString stringWithFormat:@"ID:%@", inviteModel.user_id];
    
    [self.locationBtn setTitle:[NSString stringWithFormat:@"%@%@",inviteModel.city, inviteModel.city_two] forState:UIControlStateNormal];
    self.descLabel.text = inviteModel.msg;
    
    double timeInterval = [inviteModel.ctime doubleValue];
    if(timeInterval > 140000000000) {
        timeInterval = timeInterval / 1000;
    }
    NSDateFormatter* formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"MM月dd日"];
    NSString *latestMessageTime = [formatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:timeInterval]];
    self.timeLabel.text = latestMessageTime;
    
    _onlineStateLabel.hidden = NO;
    if ([inviteModel.online_str isEqualToString:@"热聊中"]) {
        _onlineStateLabel.text = @"热聊中";
        _onlineStateLabel.backgroundColor = UIColorHex(0xEF51B2);
    } else if ([inviteModel.online_str isEqualToString:@"相亲中"]) {
        _onlineStateLabel.text = @"相亲中";
        _onlineStateLabel.backgroundColor = UIColorHex(0x7F3EF0);
    } else if ([inviteModel.online_str isEqualToString:@"开播中"]) {
        _onlineStateLabel.text = @"开播中";
        _onlineStateLabel.backgroundColor = UIColorHex(0xFEBF00);
    } else {
        _onlineStateLabel.hidden = YES;
    }
}

- (IBAction)agreeAction:(id)sender {
    if (self.agreeActionBlock) {
        self.agreeActionBlock();
    }
}
- (IBAction)rejectAction:(id)sender {
    if (self.rejestActionBlock) {
        self.rejestActionBlock();
    }
}

@end

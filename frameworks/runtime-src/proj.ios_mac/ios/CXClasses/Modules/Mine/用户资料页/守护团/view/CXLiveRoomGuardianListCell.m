//
//  CXLiveRoomGuardianListCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/19.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomGuardianListCell.h"

@implementation CXLiveRoomGuardianListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _avatar.layer.masksToBounds = YES;
    _avatar.layer.cornerRadius = 28;
    _avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    _avatar.layer.borderWidth = 0.5;
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(CXLiveRoomGuardItemModel *)model {
    _model = model;
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:model.UserInfo.HeadImageUrl]];
    _nameLabel.text = model.UserInfo.Name;
    [_ageBtn setTitle:model.UserInfo.Age.stringValue forState:UIControlStateNormal];
    [_locationBtn setTitle:[NSString stringWithFormat:@"%@",model.UserInfo.City] forState:UIControlStateNormal];
    if (model.UserInfo.Sex == 1) { // 男
        [_ageBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
        _ageBtn.backgroundColor = UIColorHex(0x3F99FF);
    } else {
        [_ageBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
        _ageBtn.backgroundColor = UIColorHex(0xEB76E4);
    }
    
    _numberLabel.text = [NSString stringWithFormat:@"亲密度：%@", model.Intimacy.stringValue];
    _expireLabel.text = [NSString stringWithFormat:@"还有%@天到期", model.Days.stringValue];
    
}

- (void)setUserModel:(CXUserModel *)userModel {
    _userModel = userModel;
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:userModel.avatar]];
    _nameLabel.text = userModel.nickname;
    [_ageBtn setTitle:userModel.age forState:UIControlStateNormal];
    [_locationBtn setTitle:[NSString stringWithFormat:@"%@,%@",userModel.city, userModel.city_two] forState:UIControlStateNormal];
    if (userModel.sex.intValue == 1) { // 男
        [_ageBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
        _ageBtn.backgroundColor = UIColorHex(0x3F99FF);
    } else {
        [_ageBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
        _ageBtn.backgroundColor = UIColorHex(0xEB76E4);
    }
    
    _numberLabel.text = [NSString stringWithFormat:@"亲密度：%@", userModel.intimacy];
    _expireLabel.text = [NSString stringWithFormat:@"还有%@天到期", userModel.countdown_day.stringValue];
}

@end

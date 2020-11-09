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
    } else {
        [_ageBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    _numberLabel.text = [NSString stringWithFormat:@"亲密度：%@", model.Intimacy.stringValue];
    
    if ([self.guardianUserId isEqualToString:[CXClientModel instance].userId]) {
        _expireLabel.hidden = NO;
        _expireLabel.text = [NSString stringWithFormat:@"还有%@天到期", model.Days.stringValue];
        _endTimeLabel.text = [NSString stringWithFormat:@"守护到期时间：%@", model.DeadlineTime];
    } else {
        if ([_model.UserInfo.PrettyId isEqualToString:[CXClientModel instance].userId]) {
            _expireLabel.hidden = NO;
            _expireLabel.text = [NSString stringWithFormat:@"还有%@天到期", model.Days.stringValue];
            _endTimeLabel.text = [NSString stringWithFormat:@"守护到期时间：%@", model.DeadlineTime];
        } else {
            _expireLabel.hidden = YES;
            _endTimeLabel.text = @"";
        }
    }
}

- (void)setUserModel:(CXUserModel *)userModel {
    _userModel = userModel;
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:userModel.avatar]];
    _nameLabel.text = userModel.nickname;
    [_ageBtn setTitle:userModel.age forState:UIControlStateNormal];
    [_locationBtn setTitle:[NSString stringWithFormat:@"%@,%@",userModel.city, userModel.city_two] forState:UIControlStateNormal];
    if (userModel.sex.intValue == 1) { // 男
        [_ageBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [_ageBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    _numberLabel.text = [NSString stringWithFormat:@"亲密度：%@", userModel.intimacy];
    
//    _endTimeLabel.text = [NSString stringWithFormat:@"守护到期时间：%@", userModel.end_time];
    _endTimeLabel.text = @"";
    
    if ([self.guardianUserId isEqualToString:[CXClientModel instance].userId]) {
        _expireLabel.hidden = NO;
        _expireLabel.text = [NSString stringWithFormat:@"还有%@天到期", userModel.countdown_day.stringValue];
    } else {
        if ([userModel.user_id isEqualToString:[CXClientModel instance].userId]) {
            _expireLabel.hidden = NO;
            _expireLabel.text = [NSString stringWithFormat:@"还有%@天到期", userModel.countdown_day.stringValue];
        } else {
            _expireLabel.hidden = YES;
        }
    }
}

@end

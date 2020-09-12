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

- (void)setModel:(CXUserModel *)model {
    _model = model;
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:model.avatar]];
    _nameLabel.text = model.nickname;
    [_ageBtn setTitle:model.age forState:UIControlStateNormal];
    [_locationBtn setTitle:[NSString stringWithFormat:@"%@%@",model.city,model.city_two] forState:UIControlStateNormal];
    if ([model.sex integerValue] == 1) { // 男
        [_ageBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
        _ageBtn.backgroundColor = UIColorHex(0x3F99FF);
    } else {
        [_ageBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
        _ageBtn.backgroundColor = UIColorHex(0xEB76E4);
    }
    
    _numberLabel.text = [NSString stringWithFormat:@"亲密度：%@", model.intimacy];
    _expireLabel.text = [NSString stringWithFormat:@"还有%@天到期", model.countdown_day.stringValue];
    
}

@end

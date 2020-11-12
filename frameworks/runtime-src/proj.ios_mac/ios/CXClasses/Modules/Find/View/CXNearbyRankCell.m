//
//  CXNearbyRankCell.m
//  hairBall
//
//  Created by mahong yang on 2020/4/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXNearbyRankCell.h"

@implementation CXNearbyRankCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _rank_logo.layer.cornerRadius = 24;
    _rank_logo.layer.masksToBounds = YES;
    
    _rank_ageBtn.layer.cornerRadius = 8;
    _rank_ageBtn.layer.masksToBounds = YES;
    _rank_cityBtn.layer.cornerRadius = 8;
    _rank_cityBtn.layer.masksToBounds = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(CXUserModel *)model {
    _model = model;
    
    [_rank_logo sd_setImageWithURL:[NSURL URLWithString:model.avatar]];
    _rank_name.text = model.nickname;
    [_rank_ageBtn setTitle:model.age forState:UIControlStateNormal];
    if (model.city.length > 0) {
        _rank_cityBtn.hidden = NO;
        [_rank_cityBtn setTitle:[NSString stringWithFormat:@" %@ ",model.city] forState:UIControlStateNormal];
    } else {
        _rank_cityBtn.hidden = YES;
    }
    
    if ([model.sex integerValue] == 1) { // 男
        [_rank_ageBtn setImage:[UIImage imageNamed:@"find_nan"] forState:UIControlStateNormal];
        _rank_ageBtn.backgroundColor = UIColorHex(0x3F99FF);
    } else {
        [_rank_ageBtn setImage:[UIImage imageNamed:@"find_nv"] forState:UIControlStateNormal];
        _rank_ageBtn.backgroundColor = UIColorHex(0xEB76E4);
    }
    
}

@end

//
//  CXLiveRoomSeatFansCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/3.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomSeatFansCell.h"

@implementation CXLiveRoomSeatFansCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _rank_logo.layer.masksToBounds = YES;
    _rank_logo.layer.cornerRadius = 20;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(CXSocketMessageSeatsFansModel *)model {
    _model = model;
    
    [_rank_logo sd_setImageWithURL:[NSURL URLWithString:model.UserImage]];
    _rank_name.text = model.UserName;
    
    self.rank_descLabel.text = [NSString stringWithFormat:@"%@岁｜%@cm｜%@", model.Age,model.Stature.length > 0 ? model.Stature : @"身高", model.City.length > 0 ? model.City : @"城市"];

    if ([model.Sex integerValue] == 1) { // 男
       [_rank_ageBtn setImage:[UIImage imageNamed:@"find_nan"] forState:UIControlStateNormal];
    } else {
       [_rank_ageBtn setImage:[UIImage imageNamed:@"find_nv"] forState:UIControlStateNormal];
    }
    
    _rank_roseLabel.text = [NSString stringWithFormat:@"X%ld", model.Score];
}

@end

//
//  CXLiveRoomRecommendCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomRecommendCell.h"

@implementation CXLiveRoomRecommendCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 4;
    
    self.g_bgImage.image = [UIImage gradientImageWithSize:CGSizeMake(100, 40) Color1:UIColorHex(0xFFFFFF) color2:UIColorHex(0x000000) endPoint:CGPointMake(0, 1)];
    self.g_bgImage.alpha = 0.3;
}

- (void)setModel:(CXLiveRoomRecommendModel *)model {
    _model = model;
    self.nameLabel.text = model.nickname;
    self.descLabel.text = [NSString stringWithFormat:@"%@岁|%@cm|%@",model.age,model.stature,[model.sex intValue] == 1? @"男" : @"女"];
    [self.logo sd_setImageWithURL:[NSURL URLWithString:model.avatar]];
    if ([model.room_type intValue] == 5) { // 相亲
        self.tagLabel.hidden = NO;
        if ([model.state intValue] == 1) {
            [self.tagLabel setTitle:@"相亲中" forState:UIControlStateNormal];
            [self.tagLabel setBackgroundImage:[UIImage imageNamed:@"live_room_recommend_state1"] forState:UIControlStateNormal];
        } else if ([model.state intValue] == 2) {
            [self.tagLabel setTitle:@"等待中" forState:UIControlStateNormal];
            [self.tagLabel setBackgroundImage:[UIImage imageNamed:@"live_room_recommend_state2"] forState:UIControlStateNormal];
        } else {
            self.tagLabel.hidden = YES;
        }
    } else {
        self.tagLabel.hidden = YES;
    }
    
}

@end

//
//  CXNearbyRankSongCell.m
//  hairBall
//
//  Created by mahong yang on 2020/4/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXNearbyRankSongCell.h"

@implementation CXNearbyRankSongCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _rank_logo.layer.cornerRadius = 24;
    _rank_logo.layer.masksToBounds = YES;
    
    _bgImage.image = [UIImage gradientImageWithSize:CGSizeMake(kScreenWidth-10, 48) Color1:UIColorHex(0xEC1FC8) color2:UIColorHex(0x8F09CD)];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(CXUserModel *)model {
    _model = model;
    
    [_rank_logo sd_setImageWithURL:[NSURL URLWithString:model.avatar]];
    _rank_name.text = model.nickname;
    _rank_desc.text = [NSString stringWithFormat:@"%@-%@",model.song_name, model.singer_name];
    _rank_roseLabel.text = model.coin;
    
}

@end

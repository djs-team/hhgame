//
//  CXGameMusicRankListCell.m
//  hairBall
//
//  Created by mahong yang on 2020/4/15.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicRankListCell.h"

@implementation CXGameMusicRankListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _avatar.layer.cornerRadius = 24;
    _avatar.layer.masksToBounds = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(CXSocketMessageMusicRankModel *)model {
    _model = model;
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:model.UserImage]];
    _nameLabel.text = model.UserName;
    _descLabel.text = [NSString stringWithFormat:@"%@-%@",model.SongName, model.SingerName];
    _roseLabel.text = [NSString stringWithFormat:@"%ld",model.Score];
    
}

@end

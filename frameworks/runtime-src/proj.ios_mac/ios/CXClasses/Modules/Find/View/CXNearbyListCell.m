//
//  CXNearbyListCell.m
//  hairBall
//
//  Created by mahong yang on 2019/11/5.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXNearbyListCell.h"
#import "UIImage+GIF.h"

@implementation CXNearbyListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.avatar.layer.cornerRadius = 30;
    self.followButton.layer.cornerRadius = 10;
    self.sexBtn.layer.cornerRadius = 8;
    self.cityBtn.layer.cornerRadius = 8;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (IBAction)followAction:(id)sender {
    if (self.followActionBlock) {
        self.followActionBlock();
    }
}

- (void)setModel:(CXUserModel *)model {
    _model = model;
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:model.avatar] placeholderImage:[UIImage imageNamed:@"avatar_default"]];
    self.nameLabel.text = model.nickname;
    
    [self.sexBtn setTitle:model.age forState:UIControlStateNormal];
    if ([model.sex integerValue] == 1) {
        [self.sexBtn setImage:[UIImage imageNamed:@"find_nan"]
                     forState:UIControlStateNormal];
        self.sexBtn.backgroundColor = UIColorHex(0x3F99FF);
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"find_nv"] forState:UIControlStateNormal];
        self.sexBtn.backgroundColor = UIColorHex(0xEB76E4);
    }
    
    if (model.city.length > 0) {
        self.cityBtn.hidden = NO;
        [self.cityBtn setTitle:[NSString stringWithFormat:@" %@.%@ ", model.city, model.city_two] forState:UIControlStateNormal];
    } else {
        self.cityBtn.hidden = YES;
    }
    
    
    self.messageLabel.text = model.intro;
    
    if ([model.attention integerValue] == 1) { //已关注
        [self.followButton setTitle:@"已关注" forState:UIControlStateNormal];
        self.followButton.backgroundColor = UIColorHex(0x7C7C7C);
    } else {
        [self.followButton setTitle:@"+ 关注" forState:UIControlStateNormal];
        self.followButton.backgroundColor = UIColorHex(0xDE1616);
    }
}

@end

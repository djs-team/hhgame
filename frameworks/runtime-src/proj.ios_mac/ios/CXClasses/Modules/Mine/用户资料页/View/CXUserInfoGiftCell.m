//
//  CXUserInfoGiftCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXUserInfoGiftCell.h"

@implementation CXUserInfoGiftCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.logo.layer.masksToBounds = YES;
    self.logo.layer.cornerRadius = 30;
    self.logo.layer.borderWidth = 0.5;
    self.logo.layer.borderColor = UIColorHex(0x9B9B9B).CGColor;
}

- (void)setModel:(CXFriendGiftModel *)model {
    _model = model;
    
    [self.logo sd_setImageWithURL:[NSURL URLWithString:model.gift_image]];
    self.nameLabel.text = model.gift_name;
    self.countLabel.text = [NSString stringWithFormat:@"x%@", model.num.stringValue];
}

@end

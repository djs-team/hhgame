//
//  CXNearbyRankHeaderView.m
//  hairBall
//
//  Created by mahong yang on 2020/4/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXNearbyRankHeaderView.h"

@interface CXNearbyRankHeaderView()
@property (weak, nonatomic) IBOutlet UIView *firstView;
@property (weak, nonatomic) IBOutlet UIImageView *first_avatar;
@property (weak, nonatomic) IBOutlet UILabel *first_name;
@property (weak, nonatomic) IBOutlet UILabel *first_num;
@property (weak, nonatomic) IBOutlet UIImageView *first_rose;

@property (weak, nonatomic) IBOutlet UIView *secondView;
@property (weak, nonatomic) IBOutlet UIImageView *second_avatar;
@property (weak, nonatomic) IBOutlet UILabel *second_name;
@property (weak, nonatomic) IBOutlet UILabel *second_num;
@property (weak, nonatomic) IBOutlet UIImageView *second_rose;

@property (weak, nonatomic) IBOutlet UIView *thirdView;
@property (weak, nonatomic) IBOutlet UIImageView *third_avatar;
@property (weak, nonatomic) IBOutlet UILabel *third_name;
@property (weak, nonatomic) IBOutlet UILabel *third_num;
@property (weak, nonatomic) IBOutlet UIImageView *third_rose;

@end

@implementation CXNearbyRankHeaderView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _first_avatar.layer.cornerRadius = 42;
    _first_avatar.layer.masksToBounds = YES;
    _first_avatar.layer.borderColor = UIColorHex(0x770BA7).CGColor;
    _first_avatar.layer.borderWidth = 1;
    
    _second_avatar.layer.cornerRadius = 35;
    _second_avatar.layer.masksToBounds = YES;
    _second_avatar.layer.borderColor = UIColorHex(0x770BA7).CGColor;
    _second_avatar.layer.borderWidth = 1;
    
    _third_avatar.layer.cornerRadius = 35;
    _third_avatar.layer.masksToBounds = YES;
    _third_avatar.layer.borderColor = UIColorHex(0x770BA7).CGColor;
    _third_avatar.layer.borderWidth = 1;
}

- (void)reloadFirstModel:(CXUserModel *)model1 secondModel:(CXUserModel *)model2 thirdModel:(CXUserModel *)model3 type:(NSInteger)type {
    _firstView.hidden = YES;
    _secondView.hidden = YES;
    _thirdView.hidden = YES;
    if (type == 1) {
        _first_rose.image = [UIImage imageNamed:@"find_rose"];
        _second_rose.image = [UIImage imageNamed:@"find_rose"];
        _third_rose.image = [UIImage imageNamed:@"find_rose"];
        
        if (model1.user_id.length > 0) {
            _firstView.hidden = NO;
            [_first_avatar sd_setImageWithURL:[NSURL URLWithString:model1.avatar]];
            _first_name.text = model1.nickname;
            _first_num.text = model1.num;
        }
        
        if (model2.user_id.length > 0) {
            _secondView.hidden = NO;
            [_second_avatar sd_setImageWithURL:[NSURL URLWithString:model2.avatar]];
            _second_name.text = model2.nickname;
            _second_num.text = model2.num;
        }
        
        if (model3.user_id.length > 0) {
            _thirdView.hidden = NO;
            [_third_avatar sd_setImageWithURL:[NSURL URLWithString:model3.avatar]];
            _third_name.text = model3.nickname;
            _third_num.text = model3.num;
        }
        
    } else {
        _first_rose.image = [UIImage imageNamed:@"find_blue_rose"];
        _second_rose.image = [UIImage imageNamed:@"find_blue_rose"];
        _third_rose.image = [UIImage imageNamed:@"find_blue_rose"];
        
        if (model1.user_id.length > 0) {
            _firstView.hidden = NO;
            [_first_avatar sd_setImageWithURL:[NSURL URLWithString:model1.avatar]];
            _first_name.text = model1.nickname;
            _first_num.text = model1.blue_rose;
        }
        
        if (model2.user_id.length > 0) {
            _secondView.hidden = NO;
            [_second_avatar sd_setImageWithURL:[NSURL URLWithString:model2.avatar]];
            _second_name.text = model2.nickname;
            _second_num.text = model2.blue_rose;
        }
        
        if (model3.user_id.length > 0) {
            _thirdView.hidden = NO;
            [_third_avatar sd_setImageWithURL:[NSURL URLWithString:model3.avatar]];
            _third_name.text = model3.nickname;
            _third_num.text = model3.blue_rose;
        }
    }
}

- (IBAction)clickAction:(UIButton *)sender {
    if (self.clickActionBlock) {
        self.clickActionBlock(sender.tag);
    }
}

@end

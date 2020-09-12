//
//  CXLiveRoomRechargeCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomRechargeCell.h"

@implementation CXLiveRoomRechargeCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 8;
    
    _bg_view.layer.masksToBounds = YES;
    _bg_view.layer.cornerRadius = 8;
    _bg_view.layer.borderColor = [UIColor whiteColor].CGColor;
    _bg_view.layer.borderWidth = 1;
}

- (void)setChargeListItem:(CXRechargeModel *)chargeListItem {
    _chargeListItem = chargeListItem;
    
    
    
    self.rose_numberLabel.text = [NSString stringWithFormat:@"%@", chargeListItem.diamond];
    self.rose_valueLabel.text = [NSString stringWithFormat:@"%@元", chargeListItem.rmb];
    
    if ([chargeListItem.present integerValue] > 0) {
        self.tag_btn.hidden = NO;
        [self.tag_btn setTitle:[NSString stringWithFormat:@"赠%@玫瑰", chargeListItem.present] forState:UIControlStateNormal];
    } else {
        self.tag_btn.hidden = YES;
    }
    if (self.isLiveHome == YES) {
        _bg_view.backgroundColor = UIColorHex(0x333541);
        if (chargeListItem.isSelected == YES) {
            _rose_numberLabel.textColor = UIColorHex(0xFE2754);
            _rose_valueLabel.textColor = UIColorHex(0xFE2754);
            _bg_view.layer.borderColor = UIColorHex(0xFE2754).CGColor;
        } else {
            _rose_numberLabel.textColor = UIColorHex(0xFFFFFF);
            _rose_valueLabel.textColor = UIColorHex(0x999999);
            _bg_view.layer.borderColor = UIColorHex(0x7D7D7D).CGColor;
        
        }
    } else {
        _bg_view.backgroundColor = UIColorHex(0xF9EFFE);
        if (chargeListItem.isSelected == YES) {
            _rose_numberLabel.textColor = UIColorHex(0xFE2754);
            _rose_valueLabel.textColor = UIColorHex(0xFE2754);
            _bg_view.layer.borderColor = UIColorHex(0xFE2754).CGColor;
        } else {
            _rose_numberLabel.textColor = UIColorHex(0x000000);
            _rose_valueLabel.textColor = UIColorHex(0x333333);
            _bg_view.layer.borderColor = [UIColor whiteColor].CGColor;
        
        }
    }
}

- (void)setIsShowFirstTag:(BOOL)isShowFirstTag {
    _isShowFirstTag = isShowFirstTag;
    
    if (isShowFirstTag == YES) {
            CAGradientLayer * gradientLayer = [CAGradientLayer layer];
        gradientLayer.frame = CGRectMake(0, 0, self.frame.size.width, 15);
        gradientLayer.colors = @[(__bridge id)UIColorHex(0xFCBD50).CGColor,(__bridge id)UIColorHex(0xFC6114).CGColor];
        gradientLayer.startPoint = CGPointMake(0, 0);
        gradientLayer.endPoint = CGPointMake(1, 0);
        gradientLayer.locations = @[@0,@1];
        [self.first_tagLabel.layer addSublayer:gradientLayer];
        self.first_tagLabel.hidden = NO;
        self.first_tagNameLabel.hidden = NO;
    } else {
        [self.first_tagLabel.layer removeAllSublayers];
        self.first_tagLabel.hidden = YES;
        self.first_tagNameLabel.hidden = YES;
    }
}

@end

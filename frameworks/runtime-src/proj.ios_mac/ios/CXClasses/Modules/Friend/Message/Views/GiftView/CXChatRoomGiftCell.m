//
//  CXChatRoomGiftCell.m
//  hairBall
//
//  Created by mahong yang on 2019/10/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXChatRoomGiftCell.h"

@implementation CXChatRoomGiftCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.bgView.layer.masksToBounds = YES;
    self.bgView.layer.cornerRadius = 22;
    self.bgView.layer.borderColor = [UIColorHex(0xFFB5AB) CGColor];
    self.bgView.layer.borderWidth = 1;
    self.bgView.backgroundColor = [UIColor whiteColor];
    
    self.numberLabel.layer.masksToBounds = YES;
    self.numberLabel.layer.cornerRadius = 8;
    self.numberLabel.layer.borderColor = [UIColorHex(0xFFB5AB) CGColor];
    self.numberLabel.layer.borderWidth = 1;
}

- (IBAction)didSelectedAction:(UIButton *)sender {
    if (self.didSeletedBlock) {
        self.didSeletedBlock();
    }
}

- (void)setModel:(CXLiveRoomGiftModel *)model {
    _model = model;
    
    [self.gift_logo sd_setImageWithURL:[NSURL URLWithString:model.gift_image] placeholderImage:[UIImage imageNamed:@"avatar_default"]];
    self.gift_nameLabel.text = model.gift_name;
    self.gift_valueLabel.text = [NSString stringWithFormat:@"%@玫瑰",model.gift_coin];
    
    if ([model.gift_number integerValue] > 0) {
        self.numberLabel.text = [NSString stringWithFormat:@"+%@",model.gift_number];
        self.numberLabel.textColor = UIColorHex(0xF8705F);
        self.numberLabel.layer.borderColor = [UIColor colorWithHexString:@"#F8705F"].CGColor;
    } else {
        self.numberLabel.text = [NSString stringWithFormat:@"-%@",model.gift_number];
        self.numberLabel.textColor = UIColorHex(0x36C492);
        self.numberLabel.layer.borderColor = [UIColor colorWithHexString:@"#36C492"].CGColor;
        
    }
}

- (void)setIsSelected:(BOOL)isSelected {
    if (isSelected == YES) {
        self.bgView.layer.masksToBounds = YES;
        self.bgView.layer.cornerRadius = 22;
        self.bgView.layer.borderColor = [UIColorHex(0xFFB5AB) CGColor];
        self.bgView.layer.borderWidth = 1;
        self.bgView.backgroundColor = [UIColor whiteColor];
    } else {
        self.bgView.layer.masksToBounds = YES;
        self.bgView.layer.cornerRadius = 22;
        self.bgView.layer.borderColor = [UIColorHex(0xEEEEEE) CGColor];
        self.bgView.layer.borderWidth = 1;
        self.bgView.backgroundColor = UIColorHex(0xEEEEEE);
    }
}

@end

//
//  CXMineGuardRenewItemCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "CXMineGuardRenewItemCell.h"

@implementation CXMineGuardRenewItemCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    self.layer.borderColor = UIColorHex(0x878787).CGColor;
    self.layer.borderWidth = 0.5;
}

- (void)setModel:(CXRechargeModel *)model {
    _model = model;
    
    if (model.isSelected == YES) {
        self.bgimage.image = [UIImage gradientImageWithSize:self.bounds.size Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7F3FF0) endPoint:CGPointMake(0, 1)];
        self.daylabel.textColor = UIColorHex(0xFFFFFF);
        self.priceLabel.textColor = UIColorHex(0xFFFFFF);
    } else {
        self.bgimage.image = [UIImage imageWithColor:[UIColor whiteColor]];
        self.daylabel.textColor = UIColorHex(0x333333);
        self.priceLabel.textColor = UIColorHex(0x333333);
    }
    NSInteger hour = [model.long_time integerValue];
    if (hour > 24) {
        self.daylabel.text = [NSString stringWithFormat:@"%ld天", hour/24];
    } else {
        self.daylabel.text = [NSString stringWithFormat:@"%ld小时", hour];
    }
    
    self.priceLabel.text = [NSString stringWithFormat:@"%@元", model.coin];
    self.originPriceLabel.text = [NSString stringWithFormat:@"原价%@元", model.ori_coin];
}

@end

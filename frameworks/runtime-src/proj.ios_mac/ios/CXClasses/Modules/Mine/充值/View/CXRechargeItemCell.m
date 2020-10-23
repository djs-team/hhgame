//
//  CXRechargeItemCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import "CXRechargeItemCell.h"

@implementation CXRechargeItemCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 5;
    self.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
    self.layer.borderWidth = 1;
    
    self.awardCountLabel.layer.masksToBounds = YES;
    self.awardCountLabel.layer.cornerRadius = 2;
}

- (void)setModel:(CXRechargeModel *)model {
    _model = model;
    
    self.roseCountLabel.text = model.diamond;
    self.rmbLabel.text = [NSString stringWithFormat:@"%@元", model.rmb];
    if ([model.present integerValue] > 0) {
        self.awardCountLabel.hidden = NO;
        self.awardCountLabel.text = [NSString stringWithFormat:@"赠%@玫瑰", model.present];
    } else {
        self.awardCountLabel.hidden = YES;
    }
    
    if (model.isSelected == YES) {
        self.layer.borderColor = UIColorHex(0x7F3EF0).CGColor;
    } else {
       self.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
    }
    
    if (model.isShowFirstTag == YES) {
        self.boonTagLabel.hidden = NO;
    } else {
        self.boonTagLabel.hidden = YES;
    }
}


@end

//
//  CXExchangeBlueRoseSelectedView.m
//  hairBall
//
//  Created by mahong yang on 2020/4/3.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXExchangeBlueRoseSelectedView.h"

@interface CXExchangeBlueRoseSelectedView()
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet UITextField *countTextField;
@property (weak, nonatomic) IBOutlet UILabel *bottomDescLabel;


@end

@implementation CXExchangeBlueRoseSelectedView

- (void)awakeFromNib {
    [super awakeFromNib];
    
//    [self mas_makeConstraints:^(MASConstraintMaker *make) {
//          make.size.mas_equalTo(CGSizeMake(302, 375));
//    }];
//
//    [MMPopupWindow sharedWindow].touchWildToHide = YES;
//    self.type = MMPopupTypeCustom;

}

- (void)setGiftModel:(CXFriendGiftModel *)giftModel {
    _giftModel = giftModel;
    
    [self.logo sd_setImageWithURL:[NSURL URLWithString:giftModel.gift_image]];
    NSString *desc = [NSString stringWithFormat:@"蓝：%@\n数量：%@", giftModel.gift_blue_coin, giftModel.pack_num];
    self.descLabel.text = desc;
    
    [self reloadBottomDesc];
}

- (IBAction)reduceAction:(id)sender {
    NSInteger count = [self.countTextField.text integerValue];
    if (count <= 0) {
        count = 0;
    } else {
        count--;
    }
    
    self.countTextField.text = [NSString stringWithFormat:@"%ld", (long)count];
    [self reloadBottomDesc];
}

- (IBAction)addAction:(id)sender {
    NSInteger count = [self.countTextField.text integerValue];
    if (count >= [_giftModel.pack_num integerValue]) {
        count = [_giftModel.pack_num integerValue];
    } else {
        count++;
    }
    
    self.countTextField.text = [NSString stringWithFormat:@"%ld", (long)count];
    [self reloadBottomDesc];
}

- (IBAction)exchangeAction:(UIButton *)sender {
    [self endEditing:YES];
    if (self.exchangeBlueRoseBlock) {
        self.exchangeBlueRoseBlock(self.countTextField.text, sender);
    }
}

- (void)reloadBottomDesc {
    NSInteger count = [self.countTextField.text integerValue];
    NSInteger value = [_giftModel.gift_blue_coin integerValue];
    self.bottomDescLabel.text = [NSString stringWithFormat:@"兑换成蓝玫瑰数量:%ld", count * value];
}

@end

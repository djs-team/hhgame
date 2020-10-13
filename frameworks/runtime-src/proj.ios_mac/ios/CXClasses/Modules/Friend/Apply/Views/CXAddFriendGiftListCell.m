//
//  CXAddFriendGiftListCell.m
//  hairBall
//
//  Created by mahong yang on 2019/10/29.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXAddFriendGiftListCell.h"

@implementation CXAddFriendGiftListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setGiftModel:(CXFriendGiftModel *)giftModel {
    _giftModel = giftModel;
    
    [self.giftLogo sd_setImageWithURL:[NSURL URLWithString:giftModel.gift_image]];
    if ([giftModel.pack_num integerValue] > 0) {
        self.giftNameLabel.text = [NSString stringWithFormat:@"%@X%@",giftModel.gift_name, giftModel.pack_num];
    } else {
        self.giftNameLabel.text = giftModel.gift_name;
    }
    self.giftCoinLabel.text = [NSString stringWithFormat:@"x%@",giftModel.gift_coin];
}

@end

//
//  GameMessageListItemGiftEventView.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemGiftEventView.h"
#import "GameMessageLevelAndTagView.h"
#import "GameMessageListItemView+AttributeResult.h"

@interface GameMessageListItemGiftEventView ()

@end


@implementation GameMessageListItemGiftEventView

@dynamic model;

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
    }
    return self;
}

- (void)prepareForReuse {
    [super prepareForReuse];
    [super setModel:nil];
    self.textLabel.text = nil;
}

- (void)setModel:(SocketMessageGiftEvent *)model {
    [super setModel:model];
    
    UIFont *font = [UIFont boldSystemFontOfSize:12];
    UIColor *color = UIColorHex(0xEF51B2);
    if (model.GiftGiver.Sex.integerValue == 1) {
        color = UIColorHex(0x6E6EFF);
    }
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:model.GiftGiver.HeadImageUrl]];
    self.guardValue = model.GuardState;
    
    NSMutableAttributedString *result = [self resultWithRoomGuardLevel:model.GuardSign DukeLevel:model.DukeLevel UserLevel:model.UserLevel UserIdentity:model.UserIdentity Font:font UserID:[NSString stringWithFormat:@"%@", model.GiftGiver.UserId]];
    
    // 昵称
    NSString *nameStr = [NSString stringWithFormat:@"%@（%@|%@岁）", model.GiftGiver.Name, model.GiftGiver.City.length > 0 ? model.GiftGiver.City : @"城市", model.GiftGiver.Age.length > 0 ? model.GiftGiver.Age : @"18"];
    NSMutableAttributedString *user = [[NSMutableAttributedString alloc] initWithString:nameStr];
    [user setColor:color];
    user.font = font;
    __weak typeof(self) weakSelf = self;
    [user setTextHighlightRange:NSMakeRange(0, model.GiftGiver.Name.length) color:color backgroundColor:[UIColor clearColor] tapAction:^(UIView * _Nonnull containerView, NSAttributedString * _Nonnull text, NSRange range, CGRect rect) {
        if (weakSelf.clickUserInfo) {
            weakSelf.clickUserInfo(model.GiftGiver.UserId.stringValue);
        }
    }];
    [result appendAttributedString:user];
    
    // 操作
    NSMutableAttributedString *option = [[NSMutableAttributedString alloc] initWithString:@"打赏 "];
//    [option setColor:color];
    [option setColor:[UIColor whiteColor]];
    option.font = font;
    [result appendAttributedString:option];
    
    
    [model.GiveGiftDatas enumerateObjectsUsingBlock:^(SocketMessageGiftEventGiveGiftData * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (idx != 0) {
            [result appendString:@","];
        }
        UIColor *color1 = UIColorHex(0xEF51B2);
        if (obj.Sex.integerValue == 1) {
            color1 = UIColorHex(0x6E6EFF);
        }
        // 昵称
        NSMutableAttributedString *user = [[NSMutableAttributedString alloc] initWithString:obj.TargetName];
        [user setColor:color1];
        user.font = font;
        __weak typeof(self) weakSelf = self;
        [user setTextHighlightRange:NSMakeRange(0, obj.TargetName.length) color:color1 backgroundColor:[UIColor clearColor] tapAction:^(UIView * _Nonnull containerView, NSAttributedString * _Nonnull text, NSRange range, CGRect rect) {
            if (weakSelf.clickUserInfo) {
//                NSString *userID = [[CXClientModel instance].room.seatUsers objectForKey:obj.Micro.indexPath];
                NSString *userID = obj.TargetId;
                weakSelf.clickUserInfo(userID);
            }
        }];
        [result appendAttributedString:user];
    }];
    
    // 添加礼物
    NSString *gift_imageStr;
//    GiftGetListResponseDataListItem *info = [[AppDelegate shared].gift objectForKey:model.GiftId];
//    if (info) {
//        gift_imageStr = info.gift_image;
//    } else {
        gift_imageStr = model.GiftData.Image;
//    }
    
    UIImageView *giftV = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 25, 25)];
    [giftV sd_setImageWithURL:[NSURL URLWithString:gift_imageStr]];
    NSAttributedString *face = [NSAttributedString attachmentStringWithContent:giftV contentMode:UIViewContentModeCenter attachmentSize:giftV.size alignToFont:font alignment:YYTextVerticalAlignmentCenter];
    [result appendAttributedString:face];
    
    NSMutableAttributedString *count = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@" x%@", model.Count]];
    [count setColor:[UIColor colorWithHexString:@"#FFEB93"]];
    count.font = font;
    [result appendAttributedString:count];
    
    result.lineSpacing = 4.f;
    self.textLabel.attributedText = result;
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 50, size.height);
    CGSize textSize = [self.textLabel sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 18);
}

@end

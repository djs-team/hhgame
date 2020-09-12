//
//  SocketMessageLuckyDrawResultView.m
//  hairBall
//
//  Created by shiwei on 2019/7/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageLuckyDrawResultView.h"
#import "GameMessageListItemView+AttributeResult.h"

@interface SocketMessageLuckyDrawResultView ()

@end

@implementation SocketMessageLuckyDrawResultView

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

- (void)setModel:(SocketMessageLuckyDrawResult *)model {
    [super setModel:model];
    
    UIFont *font = [UIFont boldSystemFontOfSize:12];
    UIColor *color = rgba(255, 77, 77, 1);
    
    NSMutableAttributedString *result = [self resultWithRoomGuardLevel:nil DukeLevel:model.DukeLevel UserLevel:model.UserLevel UserIdentity:model.UserIdentity Font:font UserID:model.UserId];
    
    // 恭喜
    NSString *tip = [model.EggType intValue] ==  0 ? @"砸金蛋得到 " : @"砸银蛋得到 ";
    NSMutableAttributedString *user = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"恭喜%@ %@", model.UserNickName, tip]];
    [user setColor:color];
    user.font = font;
    __weak typeof(self) weakSelf = self;
    [user setTextHighlightRange:NSMakeRange(0, model.UserNickName.length) color:color backgroundColor:[UIColor clearColor] tapAction:^(UIView * _Nonnull containerView, NSAttributedString * _Nonnull text, NSRange range, CGRect rect) {
        if (weakSelf.clickUserInfo) {
            weakSelf.clickUserInfo(model.UserId);
        }
    }];
    [result appendAttributedString:user];
    
    
//    for (SocketMessageLuckyDrawReponseGift *gift in model.Gifts) {
    SocketMessageLuckyDrawReponseGift *gift = model.Gift;
        
//        GiftGetListResponseDataListItem *info = [[AppDelegate shared].gift objectForKey:[NSNumber numberWithString:gift.Id]];
        
        UIImageView *giftV = [[UIImageView alloc] initWithFrame:CGRectMake(0, 0, 25, 25)];
        [giftV sd_setImageWithURL:[NSURL URLWithString:gift.GiftData.Image]];
        NSAttributedString *face = [NSAttributedString attachmentStringWithContent:giftV contentMode:UIViewContentModeCenter attachmentSize:giftV.size alignToFont:font alignment:YYTextVerticalAlignmentCenter];
        [result appendAttributedString:face];
        
        NSMutableAttributedString *giftAtt = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@" x%@ ", gift.Count]];
        
        [giftAtt setColor:color];
        giftAtt.font = font;
        
        [result appendAttributedString:giftAtt];
//    }
    
    
    result.lineSpacing = 4.f;
    self.textLabel.attributedText = result;
    
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 40, size.height);
    CGSize textSize = [self.textLabel sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 8);
}

@end

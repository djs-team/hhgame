//
//  GameMessageListItemTextMessageView.m
//  hairBall
//
//  Created by shiwei on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemTextMessageView.h"
#import "GameMessageListItemView+AttributeResult.h"

@interface GameMessageListItemTextMessageView ()

@end

@implementation GameMessageListItemTextMessageView

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

- (void)setModel:(SocketMessageTextMessage *)model {
    [super setModel:model];
    
    UIFont *font = [UIFont boldSystemFontOfSize:12];
    UIColor *color = UIColorHex(0xEF51B2);
    if (model.JoinRoomUser.Sex.integerValue == 1) {
        color = UIColorHex(0x6E6EFF);
    }
    
    SocketMessageUserJoinRoom *joinroom = model.JoinRoomUser;
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:model.JoinRoomUser.Avatar]];

    // 等级
    NSMutableAttributedString *result = [self resultWithRoomGuardLevel:joinroom.GuardSign DukeLevel:joinroom.DukeLevel UserLevel:joinroom.UserLevel UserIdentity:joinroom.UserIdentity Font:font UserID:model.JoinRoomUser.UserId];
    
    // 昵称
    NSMutableAttributedString *user = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@（%@|%@岁）", model.JoinRoomUser.Name, model.JoinRoomUser.City.length > 0 ? model.JoinRoomUser.City : @"城市", model.JoinRoomUser.Age.length > 0 ? model.JoinRoomUser.Age : @"18"]];
    [user setColor:color];
    user.font = font;
    __weak typeof(self) weakSelf = self;
    [user setTextHighlightRange:NSMakeRange(0, model.JoinRoomUser.Name.length) color:color backgroundColor:[UIColor clearColor] tapAction:^(UIView * _Nonnull containerView, NSAttributedString * _Nonnull text, NSRange range, CGRect rect) {
        if (weakSelf.clickUserInfo) {
            weakSelf.clickUserInfo(model.JoinRoomUser.UserId);
        }
    }];
    [result appendAttributedString:user];
    
    // 操作
    NSMutableAttributedString *option = [[NSMutableAttributedString alloc] initWithString:model.text];
    // [option setColor:color];
    [option setColor:[UIColor whiteColor]];
    option.font = font;
    [result appendAttributedString:option];
    
    // 间距
    result.lineSpacing = 4.f;
    weakSelf.textLabel.attributedText = result;
    
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 40, size.height);
    CGSize textSize = [self.textLabel sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 18);
}

@end

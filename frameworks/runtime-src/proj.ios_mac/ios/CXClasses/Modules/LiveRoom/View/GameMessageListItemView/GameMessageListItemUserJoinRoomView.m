//
//  GameMessageListItemUserJoinRoomView.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemUserJoinRoomView.h"
#import "GameMessageLevelAndTagView.h"
#import "GameMessageListItemView+AttributeResult.h"

@interface GameMessageListItemUserJoinRoomView ()

@end

@implementation GameMessageListItemUserJoinRoomView

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

- (void)setModel:(SocketMessageUserJoinRoom *)model {
    [super setModel:model];
    
    UIFont *font = [UIFont boldSystemFontOfSize:12];
    UIColor *color = UIColorHex(0xEF51B2);
    if (model.Sex.integerValue == 1) {
        color = UIColorHex(0x6E6EFF);
    }

    [self.avatar sd_setImageWithURL:[NSURL URLWithString:model.Avatar]];
    
    // 添加 登记 身份 爵位
    NSMutableAttributedString *result = [self resultWithRoomGuardLevel:model.GuardSign DukeLevel:model.DukeLevel UserLevel:model.UserLevel UserIdentity:model.UserIdentity Font:font  UserID:model.UserId];
    
    // 昵称
    NSString *nameStr = [NSString stringWithFormat:@"%@（%@|%@岁）", model.Name, model.City.length > 0 ? model.City : @"城市", model.Age.length > 0 ? model.Age : @"18"];
    NSMutableAttributedString *user = [[NSMutableAttributedString alloc] initWithString:nameStr];
    [user setColor:color];
    user.font = font;
    __weak typeof(self) weakSelf = self;
    [user setTextHighlightRange:NSMakeRange(0, nameStr.length) color:color backgroundColor:[UIColor clearColor] tapAction:^(UIView * _Nonnull containerView, NSAttributedString * _Nonnull text, NSRange range, CGRect rect) {
        if (weakSelf.clickUserInfo) {
            weakSelf.clickUserInfo(model.UserId);
        }
    }];
    [result appendAttributedString:user];
    
    // 操作
    NSMutableAttributedString *option = [[NSMutableAttributedString alloc] initWithString:@"加入房间"];
//    [option setColor:color];
    [option setColor:[UIColor whiteColor]];
    option.font = font;
    [result appendAttributedString:option];
    
    // 间距
    result.lineSpacing = 4.f;
    self.textLabel.attributedText = result;
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 50, size.height);
    CGSize textSize = [self.textLabel sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 18);
}

@end

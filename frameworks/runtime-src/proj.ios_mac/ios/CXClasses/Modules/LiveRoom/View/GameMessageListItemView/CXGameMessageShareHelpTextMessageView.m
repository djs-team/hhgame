//
//  CXGameMessageShareHelpTextMessageView.m
//  hairBall
//
//  Created by mahong yang on 2020/5/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMessageShareHelpTextMessageView.h"

@interface CXGameMessageShareHelpTextMessageView()

@end

@implementation CXGameMessageShareHelpTextMessageView

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

- (void)setModel:(CXSocketMessageSystemShareHelpNotification *)model {
    [super setModel:model];
    
    UIColor *color = UIColorHex(0xEF51B2);
    UIColor *color1 = UIColorHex(0x6E6EFF);
    
    NSMutableAttributedString *result = [[NSMutableAttributedString alloc] init];
    NSMutableAttributedString *user2 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@",model.TargetName]];
    if (model.TargetSex == 1) {
        [user2 setColor:color1];
    } else {
        [user2 setColor:color];
    }
    [result appendAttributedString:user2];
    
    
    NSMutableAttributedString *option = [[NSMutableAttributedString alloc] initWithString:@"邀请好友给"];
    [option setColor:[UIColor whiteColor]];
    [result appendAttributedString:option];
    
    NSMutableAttributedString *user1 = [[NSMutableAttributedString alloc] initWithString:[NSString stringWithFormat:@"%@",model.UserName]];
    if (model.UserSex == 1) {
        [user1 setColor:color1];
    } else {
        [user1 setColor:color];
    }
    [result appendAttributedString:user1];
    
    NSString *message = [NSString stringWithFormat:@"助力了%ld朵玫瑰",model.Coin];
    NSMutableAttributedString *option2 = [[NSMutableAttributedString alloc] initWithString:message];
    [option2 setColor:[UIColor whiteColor]];
    [result appendAttributedString:option2];
    [result setFont:[UIFont boldSystemFontOfSize:12]];
    self.textLabel.attributedText = result;
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 40, size.height);
    CGSize textSize = [self.textLabel sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 8);
}

@end

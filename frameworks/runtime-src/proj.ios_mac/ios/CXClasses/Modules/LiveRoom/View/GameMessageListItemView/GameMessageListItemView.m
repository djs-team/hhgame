//
//  GameMessageListItemView.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView.h"

@interface GameMessageListItemView ()

@end

@implementation GameMessageListItemView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        _avatar = [[UIImageView alloc] initWithFrame:CGRectZero];
        _avatar.contentMode = UIViewContentModeScaleAspectFill;
        [self.contentView addSubview:_avatar];
        [_avatar mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(22, 22));
            make.left.mas_offset(4);
            make.top.mas_offset(4);
        }];
        _avatar.layer.cornerRadius = 11;
        _avatar.layer.masksToBounds = true;
        
        _textLabel = [YYLabel new];
        _textLabel.numberOfLines = 0;
        _textLabel.textColor = UIColorHex(0xE5DDE8);
        [self.contentView addSubview:_textLabel];
        [_textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(self.contentView).with.insets(UIEdgeInsetsMake(8, 30, 8, 10));
        }];
        
        self.contentView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6];
        self.layer.cornerRadius = 15.f;
        self.layer.masksToBounds = true;
    }
    return self;
}

@end

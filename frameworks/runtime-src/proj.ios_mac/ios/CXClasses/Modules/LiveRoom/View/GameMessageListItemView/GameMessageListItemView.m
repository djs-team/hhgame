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
            make.size.mas_equalTo(CGSizeMake(24, 24));
            make.left.mas_offset(3);
            make.top.mas_offset(5);
        }];
        _avatar.layer.cornerRadius = 12;
        _avatar.layer.masksToBounds = true;
        
        _avatar_bgImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
        _avatar_bgImageView.contentMode = UIViewContentModeScaleAspectFit;
        [self.contentView addSubview:_avatar_bgImageView];
        [_avatar_bgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(24, 29));
            make.left.mas_offset(3);
            make.top.mas_offset(0);
        }];
        
        _guard_avatar = [[UIImageView alloc] initWithFrame:CGRectZero];
        _guard_avatar.contentMode = UIViewContentModeScaleAspectFit;
        [self.contentView addSubview:_guard_avatar];
        [_guard_avatar mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(10, 10));
            make.right.equalTo(_avatar_bgImageView.mas_right).offset(-1);
            make.bottom.equalTo(_avatar_bgImageView.mas_bottom).offset(2);
        }];
        _guard_avatar.layer.cornerRadius = 5;
        _guard_avatar.layer.masksToBounds = true;
        
        _guard_bgImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
        _guard_bgImageView.contentMode = UIViewContentModeScaleAspectFit;
        _guard_bgImageView.image = [UIImage imageNamed:@"home_game_guard_avatar_bg"];
        [self.contentView addSubview:_guard_bgImageView];
        [_guard_bgImageView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(12, 12));
            make.right.equalTo(_avatar_bgImageView.mas_right).offset(0);
            make.bottom.equalTo(_avatar_bgImageView.mas_bottom).offset(4);
        }];

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

- (void)setGuardValue:(NSNumber *)guardValue {
    _guardValue = guardValue;
    
    if (guardValue.integerValue == 2) {
        _avatar_bgImageView.hidden = NO;
        _avatar_bgImageView.image = [UIImage imageNamed:@"home_game_guard_bg_2"];
        
        [_guard_avatar setImageURL:[NSURL URLWithString:[CXClientModel instance].room.GuardHeadImage]];
        _guard_avatar.hidden = NO;
        _guard_bgImageView.hidden = NO;
    } else if (guardValue.integerValue == 1) {
        _avatar_bgImageView.hidden = NO;
        _avatar_bgImageView.image = [UIImage imageNamed:@"home_game_guard_bg_1"];
        
        _guard_avatar.hidden = YES;
        _guard_bgImageView.hidden = YES;
    } else {
        _avatar_bgImageView.hidden = YES;
        _guard_avatar.hidden = YES;
        _guard_bgImageView.hidden = YES;
    }
}

- (void)setModel:(__kindof id)model {
    _avatar_bgImageView.hidden = YES;
    _guard_avatar.hidden = YES;
    _guard_bgImageView.hidden = YES;
}

@end

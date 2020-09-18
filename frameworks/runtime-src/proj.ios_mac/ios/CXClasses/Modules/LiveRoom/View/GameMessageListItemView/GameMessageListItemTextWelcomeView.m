//
//  GameMessageListItemTextWelcomeView.m
//  hairBall
//
//  Created by shiwei on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemTextWelcomeView.h"

@interface GameMessageListItemTextWelcomeView ()

@property UILabel * modelView;

@end

@implementation GameMessageListItemTextWelcomeView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        _modelView = [UILabel new];
        _modelView.numberOfLines = 1000;
        _modelView.textColor = [UIColor whiteColor];
        _modelView.font = [UIFont boldSystemFontOfSize:12];
        [self.contentView addSubview:_modelView];
        
        [_modelView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(self.contentView).with.insets(UIEdgeInsetsMake(8, 14, 8, 10));
        }];
        
//        self.contentView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.6];
//        
//        self.layer.cornerRadius = 6.f;
//        self.layer.masksToBounds = true;
    }
    return self;
}

- (void)prepareForReuse {
    [super prepareForReuse];
    [super setModel:nil];
    _modelView.text = nil;
}

- (void)setModel:(GameMessageTextWelcomeModel *)model {
    [super setModel:model];
    _modelView.text = model.text;
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 24, size.height);
    CGSize textSize = [self.modelView sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 18);
}

@end

//
//  GameMessageListItemUndefineView.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemUndefineView.h"

@interface GameMessageListItemUndefineView ()

@property UILabel * modelView;

@end

@implementation GameMessageListItemUndefineView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        _modelView = [UILabel new];
        _modelView.numberOfLines = 1000;
        _modelView.font = [UIFont boldSystemFontOfSize:12];
        _modelView.textColor = rgba(37, 255, 251, 1);
        [self.contentView addSubview:_modelView];
        
        [_modelView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.mas_equalTo(self.contentView).with.insets(UIEdgeInsetsMake(8, 14, 8, 10));
        }];
        
        self.contentView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.18];
        
        self.layer.cornerRadius = 6.f;
        self.layer.masksToBounds = true;
    }
    return self;
}

- (void)prepareForReuse {
    [super prepareForReuse];
    [super setModel:nil];
    _modelView.text = nil;
}

- (void)setModel:(id)model {
    [super setModel:model];
    _modelView.text = [model description];
    self.textLabel.hidden = YES;
    self.avatar.hidden = YES;
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 40, size.height);
    CGSize textSize = [_modelView sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 8);
}

@end

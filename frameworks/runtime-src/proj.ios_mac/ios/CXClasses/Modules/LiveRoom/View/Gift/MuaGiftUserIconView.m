//
//  MuaGiftUserIconView.m
//  hairBall
//
//  Created by shiwei on 2019/7/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MuaGiftUserIconView.h"

@interface MuaGiftUserIconView ()

@property (nonatomic, strong) UIView *contentView;
@property (nonatomic, strong) UIImageView *iconView;
@property (nonatomic, strong) UIImageView *selectTag;

@end

@implementation MuaGiftUserIconView

- (void)setModel:(MuaGiftUserModel *)model {
    _model = model;
    if (model.isSelect) {
        self.selectTag.hidden = false;
        self.iconView.layer.borderColor = UIColorHex(0xEF50B4).CGColor;
    } else {
        self.selectTag.hidden = true;
        self.iconView.layer.borderColor = [UIColor clearColor].CGColor;
    }
    LiveRoomUser *gameUser = model.model;
    [self.iconView sd_setImageWithURL:[NSURL URLWithString:gameUser.HeadImageUrl]];
}

- (void)reloadData {
    [self setModel:self.model];
}

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setupUI];
    }
    return self;
}

- (void)setupUI {
    
    _contentView = [[UIView alloc] init];
    _contentView.backgroundColor = [UIColor clearColor];
    
    _iconView = [[UIImageView alloc] init];
    // _iconView.layer.borderColor = rgba(255, 128, 54, 1).CGColor;
    _iconView.layer.borderColor = [UIColor clearColor].CGColor;
    _iconView.layer.borderWidth = 1.f;
    _iconView.layer.cornerRadius = self.height * 0.5;
    _iconView.layer.masksToBounds = true;
    
    _selectTag = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"GiftUser_select"]];
    _selectTag.hidden = true;
    
    [self addSubview:_contentView];
    [_contentView addSubview:_iconView];
    [_contentView addSubview:_selectTag];
    
    [_contentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self);
    }];
    
    [_iconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.contentView);
    }];
    
    [_selectTag mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.right.equalTo(self.contentView);
    }];
}

@end

@implementation MuaGiftUserModel

@end

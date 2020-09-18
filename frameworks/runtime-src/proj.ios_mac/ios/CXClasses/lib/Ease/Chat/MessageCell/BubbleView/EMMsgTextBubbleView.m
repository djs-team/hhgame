//
//  EMMsgTextBubbleView.m
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/2/14.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import "EMMsgTextBubbleView.h"

@implementation EMMsgTextBubbleView

- (instancetype)initWithDirection:(EMMessageDirection)aDirection
                             type:(EMMessageType)aType
{
    self = [super initWithDirection:aDirection type:aType];
    if (self) {
        [self _setupSubviews];
    }
    
    return self;
}

#pragma mark - Subviews

- (void)_setupSubviews
{
    [self setupBubbleBackgroundImage];
    
    self.textLabel = [[UILabel alloc] init];
    self.textLabel.font = [UIFont systemFontOfSize:18];
    self.textLabel.numberOfLines = 0;
    [self addSubview:self.textLabel];
    [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.mas_top).offset(10);
        make.bottom.equalTo(self.mas_bottom).offset(-10);
    }];
    
//    if (self.direction == EMMessageDirectionSend) {
//        self.textLabel.textColor = [UIColor whiteColor];
//        [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.left.equalTo(self.mas_left).offset(10);
//            make.right.equalTo(self.mas_right).offset(-15);
//        }];
//    } else {
//        self.textLabel.textColor = [UIColor blackColor];
//        [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.left.equalTo(self.mas_left).offset(15);
//            make.right.equalTo(self.mas_right).offset(-10);
//        }];
//    }
    
    self.extImageView = [[UIImageView alloc] init];
    self.extImageView.contentMode = UIViewContentModeScaleAspectFit;
    [self addSubview:self.extImageView];
    [self.extImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.width.height.mas_offset(0);
        make.left.equalTo(self.textLabel.mas_right).offset(10);
    }];
    
    kWeakSelf
    UITapGestureRecognizer *extImageGesture = [[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        if (weakSelf.extImageViewTapBlock) {
            weakSelf.extImageViewTapBlock();
        };
    }];
    [self.extImageView addGestureRecognizer:extImageGesture];
    
}

#pragma mark - Setter

- (void)setModel:(EMMessageModel *)model
{
    EMTextMessageBody *body = (EMTextMessageBody *)model.emModel.body;
    self.textLabel.text = [EMEmojiHelper convertEmoji:body.text];
    if (model.emModel.ext && [[model.emModel.ext allKeys] containsObject:@"type"]) {
        self.extImageView.userInteractionEnabled = YES;
        [self.extImageView mas_updateConstraints:^(MASConstraintMaker *make) {
            make.width.height.mas_equalTo(40);
        }];
        
        if (self.direction == EMMessageDirectionSend) {
            [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(self.mas_left).offset(10);
                make.right.equalTo(self.mas_right).offset(-15-50);
            }];
        } else {
            [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(self.mas_left).offset(15);
                make.right.equalTo(self.mas_right).offset(-10-50);
            }];
        }
        
        [self.extImageView sd_setImageWithURL:[NSURL URLWithString:model.emModel.ext[@"gift_url"]]];
    } else {
        self.extImageView.userInteractionEnabled = NO;
        [self.extImageView mas_updateConstraints:^(MASConstraintMaker *make) {
            make.width.height.mas_equalTo(0);
        }];
        
        if (self.direction == EMMessageDirectionSend) {
            [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(self.mas_left).offset(10);
                make.right.equalTo(self.mas_right).offset(-15);
            }];
        } else {
            [self.textLabel mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(self.mas_left).offset(15);
                make.right.equalTo(self.mas_right).offset(-10);
            }];
        }
    }
}

@end

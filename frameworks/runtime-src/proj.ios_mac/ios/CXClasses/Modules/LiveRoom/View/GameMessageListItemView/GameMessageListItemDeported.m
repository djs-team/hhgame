//
//  GameMessageListItemUserSitdownView.m
//  hairBall
//
//  Created by shiwei on 2019/7/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemDeported.h"
#import "GameMessageLevelAndTagView.h"
#import "GameMessageListItemView+AttributeResult.h"

@interface GameMessageListItemDeported ()

@end

@implementation GameMessageListItemDeported

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

- (void)setModel:(SocketMessageDeported *)model {
    [super setModel:model];
    
    self.avatar.hidden = YES;
    self.textLabel.text = [NSString stringWithFormat:@"系统消息：用户“%@”已踢出房间。" , model.NickName];
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width - 40, size.height);
    CGSize textSize = [self.textLabel sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 8);
}

@end

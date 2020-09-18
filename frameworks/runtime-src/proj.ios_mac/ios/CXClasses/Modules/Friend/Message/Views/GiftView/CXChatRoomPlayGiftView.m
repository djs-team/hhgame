//
//  CXChatRoomPlayGiftView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXChatRoomPlayGiftView.h"

@implementation CXChatRoomPlayGiftView

- (void)awakeFromNib {
    [super awakeFromNib];

    self.animatedImageView.userInteractionEnabled = YES;
    UITapGestureRecognizer *playGiftGesture = [[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        [self removeFromSuperview];
    }];
    [self.animatedImageView addGestureRecognizer:playGiftGesture];
}

- (IBAction)closeAction:(id)sender {
    [self removeFromSuperview];
}

@end

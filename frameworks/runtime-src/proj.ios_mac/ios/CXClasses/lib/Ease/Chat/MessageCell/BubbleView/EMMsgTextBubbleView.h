//
//  EMMsgTextBubbleView.h
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/2/14.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import "EMMessageBubbleView.h"

NS_ASSUME_NONNULL_BEGIN

@interface EMMsgTextBubbleView : EMMessageBubbleView

@property (nonatomic, strong) UILabel *textLabel;

@property (nonatomic, strong) UIImageView *extImageView;

@property (nonatomic, copy) void (^extImageViewTapBlock)(void);

@end

NS_ASSUME_NONNULL_END

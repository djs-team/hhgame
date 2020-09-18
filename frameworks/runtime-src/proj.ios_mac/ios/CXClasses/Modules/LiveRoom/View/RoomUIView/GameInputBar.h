//
//  GameInputBar.h
//  hairBall
//
//  Created by shiwei on 2019/7/17.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
@class GameInputBar;

NS_ASSUME_NONNULL_BEGIN

@protocol GameInputBarDelegate <NSObject>
@optional
- (void)inputBar:(GameInputBar *)inputBar sendContent:(NSString *)sendContent;
@end

@interface GameInputBar : UIView

- (instancetype)initWithFrame:(CGRect)frame;
// 文本输入框
@property (nonatomic,strong)UITextView *textInput;
// 设置输入框最大行数
@property (nonatomic,assign)NSInteger textViewMaxLine;
// textView占位符
@property (nonatomic,strong)UILabel *placeholderLabel;
@property (nonatomic,weak) id<GameInputBarDelegate>delegate;
@property (nonatomic, copy) void (^keyIsVisiableBlock)(Boolean keyboardIsVisiable);
@property (nonatomic, copy) void(^sendContentBlock)(NSString *sendContent);

// 发送成功
-(void)sendSuccessEndEditing;

@end

NS_ASSUME_NONNULL_END

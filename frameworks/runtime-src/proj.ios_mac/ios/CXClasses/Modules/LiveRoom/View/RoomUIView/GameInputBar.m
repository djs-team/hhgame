//
//  GameInputBar.m
//  hairBall
//
//  Created by shiwei on 2019/7/17.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameInputBar.h"
#import "UIView+CXCategory.h"

@interface GameInputBar () <UITextViewDelegate>

// 文本输入框最高高度
@property (nonatomic, assign)NSInteger textInputMaxHeight;
@property (nonatomic, assign)CGFloat textInputHeight;
@property (nonatomic, assign)CGFloat keyboardHeight;
@property (nonatomic,assign)BOOL keyboardIsVisiable;
// 发送按钮
@property (nonatomic, strong) UIButton *sendBtn;
@property (nonatomic,assign) CGFloat origin_y;

@end

@implementation GameInputBar

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.origin_y = frame.origin.y;
        [self initView];
        [self setupSubviews];
        [self addEventListening];
    }
    return self;
}

-(void)initView
{
    self.backgroundColor = [UIColor whiteColor];
    if (!self.textViewMaxLine || self.textViewMaxLine == 0) {
        self.textViewMaxLine = 4;
    }
}
- (void)setTextViewMaxLine:(NSInteger)textViewMaxLine
{
    _textViewMaxLine = textViewMaxLine;
    _textInputMaxHeight = ceil(self.textInput.font.lineHeight * (textViewMaxLine - 1) +
                               self.textInput.textContainerInset.top + self.textInput.textContainerInset.bottom);
}

// 添加通知
-(void)addEventListening
{
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillShow:) name:UIKeyboardWillShowNotification object:nil];
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillHidden:) name:UIKeyboardWillHideNotification object:nil];
}

/**
 初始化UI
 */
-(void)setupSubviews {
    UIView *line = [[UIView alloc] initWithFrame:CGRectMake(0, 0, self.width, 0.5)];
    line.backgroundColor = rgba(227, 228, 232, 1);
    [self addSubview:line];
    
    self.textInput = [[UITextView alloc] initWithFrame:CGRectMake(5, (self.height - 37)/2, self.width - 80, 37)];;
    self.textInput.font = [UIFont systemFontOfSize:15];
    self.textInput.layer.cornerRadius = 5;
    //    self.textInput.layer.borderColor = [UIColor colorWithRed:210 green:210 blue:210 alpha:1].CGColor;
    self.textInput.layer.borderColor = UIColorHex(0xA0A0A0).CGColor;
    self.textInput.layer.borderWidth = 1;
    self.textInput.layer.masksToBounds = YES;
    self.textInput.returnKeyType = UIReturnKeySend;
    self.textInput.enablesReturnKeyAutomatically = YES;
    self.textInput.delegate = self;
    [self addSubview:self.textInput];
    
    self.placeholderLabel = [[UILabel alloc]initWithFrame:CGRectMake(10, (self.height - 37)/2, self.width - 80, 37)];
    self.placeholderLabel.textColor = UIColorHex(0x878686);
    self.placeholderLabel.font = self.textInput.font;
    if (!self.placeholderLabel.text.length) {
        self.placeholderLabel.text = @" ";
    }
    [self addSubview:self.placeholderLabel];
    
    self.sendBtn = [[UIButton alloc] initWithFrame:CGRectMake(self.width - 60 - 8, self.height - 30 - 10, 60, 30)];
    //边框圆角
    self.sendBtn.layer.cornerRadius = 15;
    self.sendBtn.layer.masksToBounds = YES;
    self.sendBtn.enabled = true;
    self.sendBtn.titleLabel.font = [UIFont systemFontOfSize:15];
    [self.sendBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    self.sendBtn.backgroundColor = UIColorHex(0xFF3883);
    [self.sendBtn setTitle:@"发送" forState:UIControlStateNormal];
    [self.sendBtn addTarget:self action:@selector(didClickSendBtn) forControlEvents:UIControlEventTouchUpInside];
    [self addSubview:self.sendBtn];
}
#pragma mark keyboardnotification
- (void)keyboardWillShow:(NSNotification *)notification
{
    CGRect keyboardFrame = [notification.userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    _keyboardHeight = keyboardFrame.size.height;
    CGFloat duration = [notification.userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
    [UIView beginAnimations:nil context:NULL];
    [UIView setAnimationBeginsFromCurrentState:YES];
    [UIView setAnimationDuration:duration];
    [UIView setAnimationCurve:7];
    self.y = keyboardFrame.origin.y - self.height;
    [UIView commitAnimations];
    self.keyboardIsVisiable = YES;
    if (self.keyIsVisiableBlock) {
        self.keyIsVisiableBlock(YES);
    }
}
- (void)keyboardWillHidden:(NSNotification *)notification
{
    
    CGFloat duration = [notification.userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
    [UIView animateWithDuration:duration animations:^{
        self.y = self.origin_y;
    }];
    self.keyboardIsVisiable = NO;
    if (self.keyIsVisiableBlock) {
        self.keyIsVisiableBlock(NO);
    }
}
#pragma mark UITextViewDelegate
- (void)textViewDidChange:(UITextView *)textView
{
    if (textView.text.length > 32) {
        textView.text = [textView.text substringWithRange:NSMakeRange(0, 32)];
    }
    
    self.placeholderLabel.hidden = textView.text.length;
//    if (textView.text.length) {
//        self.sendBtn.enabled = YES;
//        [self.sendBtn setTitleColor:rgba(0, 0, 0, 0.9) forState:UIControlStateNormal];
//    }else {
//        self.sendBtn.enabled = NO;
//        [self.sendBtn setTitleColor:rgba(0, 0, 0, 0.2) forState:UIControlStateNormal];
//    }
    _textInputHeight = ceilf([self.textInput sizeThatFits:CGSizeMake(self.textInput.width, MAXFLOAT)].height);
    self.textInput.scrollEnabled = _textInputHeight > _textInputMaxHeight && _textInputMaxHeight > 0;
    if (self.textInput.scrollEnabled) {
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationBeginsFromCurrentState:YES];
        [UIView setAnimationDuration:0.3];
        [UIView setAnimationCurve:7];
        self.textInput.height = 5 + _textInputMaxHeight;
        self.y = SCREEN_HEIGHT - _keyboardHeight - _textInputMaxHeight - 5 - 10;
        self.height = _textInputMaxHeight + 15;
        self.sendBtn.y = self.height - 30 - 10;
        [UIView commitAnimations];
    } else {
        [UIView beginAnimations:nil context:NULL];
        [UIView setAnimationBeginsFromCurrentState:YES];
        [UIView setAnimationDuration:0.3];
        [UIView setAnimationCurve:7];
        self.textInput.height = _textInputHeight;
        self.y = SCREEN_HEIGHT - _keyboardHeight - _textInputHeight - 5 - 8;
        self.height = _textInputHeight + 15;
        self.sendBtn.y = self.height - 30 - 10;
        [UIView commitAnimations];
    }
}
- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text
{
    // 点击return按钮
    if ([text isEqualToString:@"\n"]){
        if ([_delegate respondsToSelector:@selector(inputBar:sendContent:)]) {
            [_delegate inputBar:self sendContent:self.textInput.text];
        }
        if (self.sendContentBlock) {
            self.sendContentBlock(self.textInput.text);
        }
        return NO;
    }
    if (textView.text.length + text.length - range.length > 32) {
        if (range.location == textView.text.length) {
            textView.text = [[textView.text stringByAppendingString:text] substringWithRange:NSMakeRange(0, 32)];
            return NO;
        }
        else {
//            [self.window makeToast:@"内容超过32个字"];
        }
        return NO;
    }
    return YES;
}

// 发送按钮
-(void)didClickSendBtn {
    if ([_delegate respondsToSelector:@selector(inputBar:sendContent:)]) {
        [_delegate inputBar:self sendContent:self.textInput.text];
    }
    if (self.sendContentBlock) {
        self.sendContentBlock(self.textInput.text);
    }
//    self.placeholderLabel.hidden = NO;
//    self.textInput.text = nil;
//    [self endEditing:true];
}
// 发送成功 清空文字 更新输入框大小
-(void)sendSuccessEndEditing {
    self.textInput.text = nil;
    [self.textInput.delegate textViewDidChange:self.textInput];
    [self endEditing:YES];
}

- (void)dealloc
{
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

@end

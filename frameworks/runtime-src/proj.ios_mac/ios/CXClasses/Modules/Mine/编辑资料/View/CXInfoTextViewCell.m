//
//  CXInfoTextViewCell.m
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXInfoTextViewCell.h"

@interface CXInfoTextViewCell() <UITextViewDelegate>

@end

@implementation CXInfoTextViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.inputTextView.delegate = self;
    
    self.inputTextView.layer.masksToBounds = YES;
    self.inputTextView.layer.cornerRadius = 10;
    self.inputTextView.layer.borderWidth = 0.5;
    self.inputTextView.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
}

- (void)setContentStr:(NSString *)contentStr {
    _contentStr = contentStr;
    if (contentStr.length > 0) {
        self.placehoderLabel.hidden = YES;
        self.inputTextView.text = contentStr;
    }
}

- (void)textViewDidChange:(UITextView *)textView {
    if (textView.text.length == 0) {
        self.placehoderLabel.hidden = NO;
    } else {
        self.placehoderLabel.hidden = YES;
    }
    
    if (textView.text.length > 50) {
        textView.text = [textView.text substringToIndex:50];
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

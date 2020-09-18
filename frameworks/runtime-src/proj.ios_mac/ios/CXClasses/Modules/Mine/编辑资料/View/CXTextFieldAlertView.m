//
//  CXTextFieldAlertView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXTextFieldAlertView.h"

@interface CXTextFieldAlertView() <UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UIView *alertView;
@property (weak, nonatomic) IBOutlet UITextField *contentTextField;
@property (weak, nonatomic) IBOutlet UIButton *cancelButton;
@property (weak, nonatomic) IBOutlet UIButton *sureButton;

@end

@implementation CXTextFieldAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    _alertView.layer.cornerRadius = 8;
    _cancelButton.layer.cornerRadius = 25;
    _sureButton.layer.cornerRadius = 25;
    
    [self.contentTextField addTarget:self action:@selector(textDidChange:) forControlEvents:UIControlEventValueChanged];
    
    _contentTextField.delegate = self;
    
}

- (void)textDidChange:(UITextField *)textField {
    if (textField.text.length > 16) {
        textField.text = [textField.text substringToIndex:16];
    }
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    if (self.sureActionBlock) {
        self.sureActionBlock(self.contentTextField.text);
    }
    [textField resignFirstResponder];
    [self.parentVC lew_dismissPopupView];
    return YES;
}

- (void)setContentStr:(NSString *)contentStr {
    _contentStr = contentStr;
    self.contentTextField.text = contentStr;
}

- (IBAction)cancelAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
}

- (IBAction)sureAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
    if (self.sureActionBlock) {
        self.sureActionBlock(self.contentTextField.text);
    }
    
}


@end

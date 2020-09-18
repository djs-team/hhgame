//
//  CXLiveRoomSetupRoomNameCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomSetupRoomNameCell.h"

@implementation CXLiveRoomSetupRoomNameCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _nameTextField.layer.masksToBounds = YES;
    _nameTextField.layer.cornerRadius = 15;
    UIView *left = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 10, 30)];
    _nameTextField.leftView = left;
    _nameTextField.leftViewMode = UITextFieldViewModeAlways;
    
    [_nameTextField addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)textFieldDidChange:(UITextField *)textField {
    if (textField.text.length > 8) {
        textField.text = [textField.text substringToIndex:8];
    }
}

@end

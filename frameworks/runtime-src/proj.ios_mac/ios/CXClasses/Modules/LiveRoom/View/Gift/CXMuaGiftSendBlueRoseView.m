//
//  CXMuaGiftSendBlueRoseView.m
//  hairBall
//
//  Created by mahong yang on 2020/5/16.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXMuaGiftSendBlueRoseView.h"

@interface CXMuaGiftSendBlueRoseView()
@property (weak, nonatomic) IBOutlet UITextField *countTextField;

@end

@implementation CXMuaGiftSendBlueRoseView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(250, 250));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
}

- (IBAction)closeAction:(id)sender {
    [self hide];
    [MMPopupView hideAll];
}

- (IBAction)sendAction:(id)sender {
    [self hide];
    [MMPopupView hideAll];
    
    if (self.sendBlueRoseBlock) {
        self.sendBlueRoseBlock([self.countTextField.text integerValue]);
    }
}

@end

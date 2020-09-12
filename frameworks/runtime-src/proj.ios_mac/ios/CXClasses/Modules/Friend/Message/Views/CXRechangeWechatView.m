//
//  CXRechangeWechatView.m
//  AFNetworking
//
//  Created by mahong yang on 2019/10/30.
//

#import "CXRechangeWechatView.h"

@implementation CXRechangeWechatView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.contentView.layer.cornerRadius = 10;
    self.commitButton.layer.cornerRadius = 22;
}

- (IBAction)closeAction:(id)sender {
    [self removeFromSuperview];
}

- (IBAction)commitAction:(id)sender {
    if (self.contentTextField.text.length <=0 ) return;
    if (self.commitActionBlock) {
        self.commitActionBlock();
        [self removeFromSuperview];
    }
}

@end

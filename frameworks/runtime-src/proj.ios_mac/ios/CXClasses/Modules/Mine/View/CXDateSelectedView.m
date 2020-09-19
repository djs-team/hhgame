//
//  CXDateSelectedView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXDateSelectedView.h"

@implementation CXDateSelectedView


- (IBAction)cancelAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
}

- (IBAction)sureAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
    if (self.selectedDateBlock) {
        self.selectedDateBlock(self.mainDatePicker.date);
    }
}


@end

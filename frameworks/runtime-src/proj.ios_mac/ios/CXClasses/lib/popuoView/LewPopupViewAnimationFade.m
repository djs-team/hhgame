//
//  LewPopupViewAnimationFade.m
//  LewPopupViewController
//
//  Created by pljhonglu on 15/3/4.
//  Copyright (c) 2015年 pljhonglu. All rights reserved.
//

#import "LewPopupViewAnimationFade.h"

@implementation LewPopupViewAnimationFade

- (void)showView:(UIView *)popupView overlayView:(UIView *)overlayView{
    // Generating Start and Stop Positions
    // Set starting properties
    popupView.center = overlayView.center;
    popupView.alpha = 0.0f;
    
    [UIView animateWithDuration:0.5 animations:^{
        popupView.alpha = 1.0f;
    } completion:nil];

}
- (void)dismissView:(UIView *)popupView overlayView:(UIView *)overlayView completion:(void (^)(void))completion{
    [UIView animateWithDuration:0.25 animations:^{
        overlayView.alpha = 0.0f;
        popupView.alpha = 0.0f;
    } completion:^(BOOL finished) {
        if (finished) {
            completion();
        }
    }];
}

@end
// 版权属于原作者
// http://code4app.com (cn) http://code4app.net (en)
// 发布代码于最专业的源码分享网站: Code4App.com
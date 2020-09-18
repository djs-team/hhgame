//
//  UIViewController+CXCategory.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/27.
//

#import "UIViewController+CXCategory.h"

@implementation UIViewController (Alert)

//两个按钮
- (void)alertTitle:(nullable NSString*)title message:(nullable NSString*)message confirm:(NSString *)confirm cancel:(NSString *)cancel confirm:(nullable void(^)(void))confirmComplete cancel:(nullable void(^)(void))cancelComplete {
    UIAlertController * ac = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleAlert];
    [ac addAction:[UIAlertAction actionWithTitle:confirm style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        if (confirmComplete) confirmComplete();
    }]];
    [ac addAction:[UIAlertAction actionWithTitle:cancel style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        if (cancelComplete) cancelComplete();
    }]];
    [self presentViewController:ac animated:YES completion:nil];
}

//一个按钮
- (void)alertTitle:(nullable NSString*)title message:(nullable NSString*)message cancel:(NSString *)cancel action:(nullable void(^)(void))block {
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:title message:message preferredStyle:UIAlertControllerStyleAlert];
    [alertController addAction:[UIAlertAction actionWithTitle:cancel style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        if (block) block ();
    }]];
    [self presentViewController:alertController animated:YES completion:nil];
}

@end

@implementation UIViewController (CXCategory)

- (void)presentViewController:(UIViewController *)viewControllerToPresent modalStyle:(UIModalPresentationStyle)style animated: (BOOL)flag completion:(void (^ __nullable)(void))completion {
    viewControllerToPresent.modalPresentationStyle = style;
    [self presentViewController:viewControllerToPresent animated:flag completion:completion];
}

@end

@implementation UIViewController(Toast)

- (void)toast:(NSString*)message {
    [MBProgressHUD showTipMessageInView:message];
}

@end

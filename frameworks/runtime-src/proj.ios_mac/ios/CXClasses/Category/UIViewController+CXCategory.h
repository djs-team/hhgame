//
//  UIViewController+CXCategory.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/27.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIViewController (Alert)

//两个按钮
- (void)alertTitle:(nullable NSString*)title message:(nullable NSString*)message confirm:(NSString *)confirm cancel:(NSString *)cancel confirm:(nullable void(^)(void))confirmComplete cancel:(nullable void(^)(void))cancelComplete;

//一个按钮
- (void)alertTitle:(nullable NSString*)title message:(nullable NSString*)message cancel:(NSString *)cancel action:(nullable void(^)(void))block;

@end

@interface UIViewController (CXCategory)

- (void)presentViewController:(UIViewController *)viewControllerToPresent modalStyle:(UIModalPresentationStyle)style animated: (BOOL)flag completion:(void (^ __nullable)(void))completion;

@end

@interface UIViewController(Toast)

- (void)toast:(NSString*)message;

@end

NS_ASSUME_NONNULL_END

//
//  MBProgressHUD+CXCategory.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/29.
//

#import <MBProgressHUD/MBProgressHUD.h>

NS_ASSUME_NONNULL_BEGIN

@interface MBProgressHUD (CXCategory)

+ (void)showTipMessageInWindow:(NSString*)message;
+ (void)showTipMessageInView:(NSString*)message;
+ (void)showTipMessageInWindow:(NSString*)message timer:(int)aTimer;
+ (void)showTipMessageInView:(NSString*)message timer:(int)aTimer;


+ (void)showActivityMessageInWindow:(NSString*)message;
+ (void)showActivityMessageInView:(NSString*)message;
+ (void)showActivityMessageInWindow:(NSString*)message timer:(int)aTimer;
+ (void)showActivityMessageInView:(NSString*)message timer:(int)aTimer;


+ (void)showSuccessMessage:(NSString *)Message;
+ (void)showErrorMessage:(NSString *)Message;
+ (void)showInfoMessage:(NSString *)Message;
+ (void)showWarnMessage:(NSString *)Message;


+ (void)showCustomIconInWindow:(NSString *)iconName message:(NSString *)message;
+ (void)showCustomIconInView:(NSString *)iconName message:(NSString *)message;

+ (void)showHUD;
+ (void)hideHUD;

@end

NS_ASSUME_NONNULL_END

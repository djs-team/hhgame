//
//  MBProgressHUD+CXCategory.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/29.
//

#import "MBProgressHUD+CXCategory.h"

static const NSInteger hideTime = 2.0;
static const NSInteger titleFontSize = 15;

@implementation MBProgressHUD (CXCategory)

+ (MBProgressHUD*)createMBProgressHUDviewWithMessage:(NSString*)message isWindiw:(BOOL)isWindow
{
    UIView *view = isWindow? (UIView*)[UIApplication sharedApplication].delegate.window:[self currentViewController].view;
    MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:view animated:YES];
    hud.detailsLabel.text = message ? message:@"加载中.....";
    hud.detailsLabel.font = [UIFont systemFontOfSize:titleFontSize];
    hud.removeFromSuperViewOnHide = YES;
    hud.userInteractionEnabled = NO;
    return hud;
}
#pragma mark-------------------- show Tip----------------------------

+ (void)showTipMessageInWindow:(NSString*)message
{
    [self showTipMessage:message isWindow:true timer:hideTime];
}
+ (void)showTipMessageInView:(NSString*)message
{
    [self showTipMessage:message isWindow:false timer:hideTime];
}
+ (void)showTipMessageInWindow:(NSString*)message timer:(int)aTimer
{
    [self showTipMessage:message isWindow:true timer:aTimer];
}
+ (void)showTipMessageInView:(NSString*)message timer:(int)aTimer
{
    [self showTipMessage:message isWindow:false timer:aTimer];
}
+ (void)showTipMessage:(NSString*)message isWindow:(BOOL)isWindow timer:(int)aTimer
{
    MBProgressHUD *hud = [self createMBProgressHUDviewWithMessage:message isWindiw:isWindow];
    hud.mode = MBProgressHUDModeText;
    [hud hideAnimated:YES afterDelay:aTimer];
}
#pragma mark-------------------- show Activity----------------------------

+ (void)showActivityMessageInWindow:(NSString*)message
{
    [self showActivityMessage:message isWindow:true timer:0];
}
+ (void)showActivityMessageInView:(NSString*)message
{
    [self showActivityMessage:message isWindow:false timer:0];
}
+ (void)showActivityMessageInWindow:(NSString*)message timer:(int)aTimer
{
    [self showActivityMessage:message isWindow:true timer:aTimer];
}
+ (void)showActivityMessageInView:(NSString*)message timer:(int)aTimer
{
    [self showActivityMessage:message isWindow:false timer:aTimer];
}
+ (void)showActivityMessage:(NSString*)message isWindow:(BOOL)isWindow timer:(int)aTimer
{
    MBProgressHUD *hud = [self createMBProgressHUDviewWithMessage:message isWindiw:isWindow];
    hud.mode = MBProgressHUDModeIndeterminate;
    if (aTimer > 0) {
        [hud hideAnimated:YES afterDelay:aTimer];
    }
}
#pragma mark-------------------- show Image----------------------------

+ (void)showSuccessMessage:(NSString *)Message
{
    NSString *name =@"MBProgressHUD+JDragon.bundle/MBProgressHUD/MBHUD_Success";
    [self showCustomIconInWindow:name message:Message];
}
+ (void)showErrorMessage:(NSString *)Message
{
    NSString *name =@"MBProgressHUD+JDragon.bundle/MBProgressHUD/MBHUD_Error";
    [self showCustomIconInWindow:name message:Message];
}
+ (void)showInfoMessage:(NSString *)Message
{
    NSString *name =@"MBProgressHUD+JDragon.bundle/MBProgressHUD/MBHUD_Info";
    [self showCustomIconInWindow:name message:Message];
}
+ (void)showWarnMessage:(NSString *)Message
{
    NSString *name =@"MBProgressHUD+JDragon.bundle/MBProgressHUD/MBHUD_Warn";
    [self showCustomIconInWindow:name message:Message];
}
+ (void)showCustomIconInWindow:(NSString *)iconName message:(NSString *)message
{
    [self showCustomIcon:iconName message:message isWindow:true];
    
}
+ (void)showCustomIconInView:(NSString *)iconName message:(NSString *)message
{
    [self showCustomIcon:iconName message:message isWindow:false];
}
+ (void)showCustomIcon:(NSString *)iconName message:(NSString *)message isWindow:(BOOL)isWindow
{
    MBProgressHUD *hud  =  [self createMBProgressHUDviewWithMessage:message isWindiw:isWindow];
    hud.customView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:iconName]];
    hud.mode = MBProgressHUDModeCustomView;
    [hud hideAnimated:YES afterDelay:hideTime];
    
}

+ (void)showHUD {
    dispatch_async(dispatch_get_main_queue(), ^{
        UIView *winView =(UIView*)[UIApplication sharedApplication].delegate.window;
        [self showHUDAddedTo:winView animated:YES];
    });
}

+ (void)hideHUD
{
    dispatch_async(dispatch_get_main_queue(), ^{
        //主线程执行
        UIView  *winView =(UIView*)[UIApplication sharedApplication].delegate.window;
        [self hideHUDForView:winView animated:YES];
        [self hideHUDForView:[self currentViewController].view animated:YES];
    });
}
#pragma mark --- 获取当前Window视图---------
+ (UIViewController *)currentViewController {
    
    UIViewController* vc = [UIApplication sharedApplication].keyWindow.rootViewController;

    while (1) {
        
        if ([vc isKindOfClass:[UITabBarController class]]) {
            
            vc = ((UITabBarController*)vc).selectedViewController;
            
        }
        
        if ([vc isKindOfClass:[UINavigationController class]]) {
            
            vc = ((UINavigationController*)vc).visibleViewController;
            
        }
        
        if (vc.presentedViewController) {
            
            vc = vc.presentedViewController;
            
        } else {
            
            break;
            
        }
    }
    return vc;
}

@end

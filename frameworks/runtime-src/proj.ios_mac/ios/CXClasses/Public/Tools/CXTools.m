//
//  CXTools.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import "CXTools.h"

@implementation CXTools

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

+ (NSMutableArray*)splitArray:(NSArray*)array withSubSize:(int)subSize{
    unsigned long count = array.count%subSize == 0?(array.count/subSize):(array.count/subSize+1);
    NSMutableArray *arr = [[NSMutableArray alloc]init];
    for (int i=0; i<count; i++) {
        int index = i*subSize;
        NSMutableArray *arr1 = [[NSMutableArray alloc]init];
        [arr1 removeAllObjects];
        int j = index;
        while (j<subSize*(i+1)&&j<array.count) {
            [arr1 addObject:[array objectAtIndex:j]];
            j += 1;
        }
        [arr addObject:[arr1 copy]];
    }
    return arr;
}

#pragma mark - ======================= Privacy ========================
// 获取麦克风权限
+ (BOOL)getAudioAuthStatus {
    __block BOOL enable;
    AVAuthorizationStatus videoAuthStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    if (videoAuthStatus == AVAuthorizationStatusNotDetermined) {
        [AVCaptureDevice requestAccessForMediaType:AVMediaTypeAudio completionHandler:^(BOOL granted) {
            if (granted) {
                // Microphone enabled code
                enable = YES;
            } else {
                // Microphone disabled code
                enable = NO;
            }
        }];
    } else if(videoAuthStatus == AVAuthorizationStatusRestricted || videoAuthStatus == AVAuthorizationStatusDenied) {// 未授权
        enable = NO;
    } else {// 已授权
        enable = YES;
    }
    
    return enable;
}
// 获取相机权限
+ (BOOL)getVideoAuthStatus {
    __block BOOL enable;
    AVAuthorizationStatus videoAuthStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    if (videoAuthStatus == AVAuthorizationStatusNotDetermined) {
        [AVCaptureDevice requestAccessForMediaType:AVMediaTypeVideo completionHandler:^(BOOL granted) {
            if (granted) {
                // Microphone enabled code
                enable = YES;
            } else {
                // Microphone disabled code
                enable = NO;
            }
        }];
    } else if(videoAuthStatus == AVAuthorizationStatusRestricted || videoAuthStatus == AVAuthorizationStatusDenied) {// 未授权
        enable = NO;
    } else{// 已授权
        enable = YES;
    }
    
    return enable;
}

+ (void)showSettingAlertViewTitle:(NSString *)title content:(NSString *)content {
    [[CXTools currentViewController] alertTitle:title message:content confirm:@"设置" cancel:@"取消" confirm:^{
        //跳入当前App设置界面
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString] options:@{} completionHandler:nil];
    } cancel:nil];
}

@end

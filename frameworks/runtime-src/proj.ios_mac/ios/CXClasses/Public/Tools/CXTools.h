//
//  CXTools.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXTools : NSObject

+ (UIViewController *)currentViewController;


+ (NSMutableArray*)splitArray:(NSArray*)array withSubSize:(int)subSize;


#pragma mark - ======================= Privacy ========================
// 获取麦克风权限
+ (BOOL)getAudioAuthStatus;
// 获取相机权限
+ (BOOL)getVideoAuthStatus;
// 授权提示
+ (void)showSettingAlertViewTitle:(NSString *)title content:(NSString *)content;
@end

NS_ASSUME_NONNULL_END

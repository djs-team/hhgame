//
//  AppController+Share.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/19.
//

#import "AppController.h"
#import <WXApi.h>

NS_ASSUME_NONNULL_BEGIN

@interface AppController (Share)

 #pragma mark --- 微信分享
// 微信分享(网页分享)
+ (void)WXShareIOSforUrl:(NSString *)url Title:(NSString *)tit Desc:(NSString *)desc;
// (文本)
+ (void)WXShareIOSforDescription:(NSString *)des;
// (截图)
+ (void)WXShareIOSforImage:(NSString *)path;

@end

NS_ASSUME_NONNULL_END

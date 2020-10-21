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
/// 微信网页分享
/// @param url 网页链接
/// @param tit 标题
/// @param desc 描述
/// @param image 图片
/// @param platform 分享渠道：WEIXIN_CIRCLE/WEIXIN
+ (void)WXShareIOSforUrl:(NSString *)url Title:(NSString *)tit Desc:(NSString *)desc image:(NSString *)image platform:(NSString *)platform;

/// 微信分享文本
/// @param des 文本内容
/// @param isTimeLine 是否是朋友圈
/// @param platform 分享渠道：WEIXIN_CIRCLE/WEIXIN
+ (void)WXShareIOSforDescription:(NSString *)des platform:(NSString *)platform;

/// 微信分享图片
/// @param path 图片文件路径
/// @param platform 分享渠道：WEIXIN_CIRCLE/WEIXIN
+ (void)WXShareIOSforImage:(NSString *)path platform:(NSString *)platform;

@end

NS_ASSUME_NONNULL_END

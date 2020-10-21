//
//  AppController+Login.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import "AppController.h"
#import <WXApi.h>

NS_ASSUME_NONNULL_BEGIN

@interface AppController (Login)
/*
 * 微信登录
 * 返回微信授权的信息，如果没有信息，则登录失败
 */
+ (void)weChatLoginWithMethod:(NSString *)method;

- (void)weChatLoginSuccess:(SendAuthResp*)authResp;

/*
* 一键登录
* 返回授权后的token，如果没有信息，则登录失败
*/
+ (void)JPushLoginWithMethod:(NSString *)method showPhoneAlert:(NSString *)showAlert;

@end

NS_ASSUME_NONNULL_END

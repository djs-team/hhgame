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

+ (void)weChatLogin;

- (void)weChatLoginSuccess:(SendAuthResp*)authResp;

+ (void)JPushLogin;

@end

NS_ASSUME_NONNULL_END

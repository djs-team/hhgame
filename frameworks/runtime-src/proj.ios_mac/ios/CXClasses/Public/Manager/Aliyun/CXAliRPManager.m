//
//  CXAliRPManager.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/25.
//

#import "CXAliRPManager.h"
//#import <RPSDK/RPSDK.h>

@implementation CXAliRPManager

+ (instancetype)sharedInstance {
    static CXAliRPManager * sinstance = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        sinstance = [[CXAliRPManager alloc] initInternal];
        
//        [RPSDK setup];
    });
    return sinstance;
}

- (instancetype)initInternal {
    if (self = [super init]) {

    }
    return self;
}

#if TARGET_IPHONE_SIMULATOR
- (void)startWithSuccess:(void(^)(void))success failure:(void(^)(int code, NSString* reason))failure nav:(UINavigationController*)nav {
    
}

#else

- (void)startWithSuccess:(void(^)(void))success failure:(void(^)(int code, NSString* reason))failure nav:(UINavigationController*)nav {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Approvice/getVerifyToken" parameters:@{@"signature": signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
//           [RPSDK startWithVerifyToken:responseObject[@"data"][@"Token"] viewController:nav completion:^(RPResult * _Nonnull result) {
//               if (result.state == RPStatePass) {
//                   [weakSelf verifySuccessCreateapproveWithSuccess:success failure:failure];
//                   if (success) success ();
//               } else if (result.state == RPStateNotVerify) {
//                   if (failure) failure ([result.errorCode intValue], @"未认证");
//               } else if (result.state == RPStateFail) {
//                   if (failure) failure ([result.errorCode intValue], @"认证失败");
//               }
//           }];
       }
    }];
}

#endif

// 实名认证
- (void)verifySuccessCreateapproveWithSuccess:(void(^)(void))success failure:(void(^)(int code, NSString* reason))failure {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Approvice/createapprove" parameters:@{@"signature": signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           if (success) success ();
       } else {
           if (failure) failure (500, @"认证失败");
       }
    }];
}

@end

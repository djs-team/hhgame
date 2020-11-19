//
//  CXAppleLoginManager.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/11/18.
//

#import "CXAppleLoginManager.h"
#import <AuthenticationServices/AuthenticationServices.h>

typedef void(^AppleLoginBlock)(NSInteger state, NSString *msg, id data);

@interface CXAppleLoginManager() <ASAuthorizationControllerDelegate, ASAuthorizationControllerPresentationContextProviding>

@property(nonatomic,copy)AppleLoginBlock appleLoginBlock;

@end

@implementation CXAppleLoginManager

static id _manager;
+ (CXAppleLoginManager *)manager {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _manager = [[CXAppleLoginManager alloc] init];
    });
    
    return _manager;
}


- (void)appleLoginComplete:(void(^)(NSInteger state, NSString *msg, id data))complete {
    self.appleLoginBlock = complete;
    if (@available(iOS 13.0, *)) {
        ASAuthorizationAppleIDProvider *appleIDProvider = [[ASAuthorizationAppleIDProvider alloc] init];
        ASAuthorizationAppleIDRequest *appleIDRequest = [appleIDProvider createRequest];
        appleIDRequest.requestedScopes = @[ASAuthorizationScopeFullName, ASAuthorizationScopeEmail];
        ASAuthorizationController *authController = [[ASAuthorizationController alloc] initWithAuthorizationRequests:@[appleIDRequest]];
        authController.delegate = self;
        authController.presentationContextProvider = self;
        [authController performRequests];
    } else {
        // Fallback on earlier versions
        complete(AppleLoginTypeUnknown, @"系统太低", nil);
    }
}

#pragma mark ————————————— 成功回调 —————————————
- (void)authorizationController:(ASAuthorizationController *)controller didCompleteWithAuthorization:(ASAuthorization *)authorization API_AVAILABLE(ios(13.0)) {
    if ([authorization.credential isKindOfClass:[ASAuthorizationAppleIDCredential class]])
    {
        // 用户登录使用ASAuthorizationAppleIDCredential
        ASAuthorizationAppleIDCredential *appleIDCredential = authorization.credential;
        NSString *user = appleIDCredential.user;
        // 使用过授权的，可能获取不到以下三个参数
        NSString *familyName = appleIDCredential.fullName.familyName;
        NSString *givenName = appleIDCredential.fullName.givenName;
        NSString *nickname = appleIDCredential.fullName.nickname;
        NSString *email = appleIDCredential.email;
        NSString *state = appleIDCredential.state;
        NSData *identityToken = appleIDCredential.identityToken;
        NSData *authorizationCode = appleIDCredential.authorizationCode;
        ASUserDetectionStatus realUserStatus = appleIDCredential.realUserStatus;
        
        // 服务器验证需要使用的参数
        NSString *identityTokenStr = [[NSString alloc] initWithData:identityToken encoding:NSUTF8StringEncoding];
        NSString *authorizationCodeStr = [[NSString alloc] initWithData:authorizationCode encoding:NSUTF8StringEncoding];

        NSMutableDictionary *dic = [NSMutableDictionary dictionary];
        [dic setValue:state forKey:@"state"];
        [dic setValue:user forKey:@"user"];
        [dic setValue:email forKey:@"email"];
        [dic setValue:familyName forKey:@"familyName"];
        [dic setValue:givenName forKey:@"givenName"];
        [dic setValue:nickname forKey:@"nickname"];
        [dic setValue:authorizationCodeStr forKey:@"authorizationCode"];
        [dic setValue:identityTokenStr forKey:@"identityToken"];
        
        if (self.appleLoginBlock)
        {
            self.appleLoginBlock(AppleLoginTypeSuccessful, @"ok",dic);
        }
    } else if ([authorization.credential isKindOfClass:[ASPasswordCredential class]]) {
        ASPasswordCredential *passwordCredential = authorization.credential;
        NSString *user = passwordCredential.user;
        NSString *password = passwordCredential.password;
        NSMutableDictionary *dic = [NSMutableDictionary dictionary];
        [dic setValue:user forKey:@"user"];
        [dic setValue:password forKey:@"password"];
        if (self.appleLoginBlock)
        {
            self.appleLoginBlock(AppleLoginTypeSuccessful, @"ok",dic);
        }
    } else {
        NSString *errorMsg = @"授权信息不符";
        NSMutableDictionary *dic = [NSMutableDictionary dictionary];
        [dic setValue:errorMsg forKey:@"errorMsg"];
        if (self.appleLoginBlock)
        {
          self.appleLoginBlock(AppleLoginTypeFailure,errorMsg,dic);
        }
    }
}
 
#pragma mark ————————————— 失败回调 —————————————
- (void)authorizationController:(ASAuthorizationController *)controller didCompleteWithError:(NSError *)error API_AVAILABLE(ios(13.0))
{
    NSString *errorMsg = nil;
    switch (error.code) {
        case ASAuthorizationErrorCanceled:
            errorMsg = @"用户取消了授权请求";
            break;
        case ASAuthorizationErrorFailed:
            errorMsg = @"授权请求失败";
            break;
        case ASAuthorizationErrorInvalidResponse:
            errorMsg = @"授权请求响应无效";
            break;
        case ASAuthorizationErrorNotHandled:
            errorMsg = @"未能处理授权请求";
            break;
        case ASAuthorizationErrorUnknown:
            errorMsg = @"授权请求失败未知原因";
            break;
    }
    NSMutableDictionary *dic = [NSMutableDictionary dictionary];
    [dic setValue:errorMsg forKey:@"errorMsg"];
    [dic setValue:[NSNumber numberWithInteger:error.code] forKey:@"code"];
    if (self.appleLoginBlock) {
      self.appleLoginBlock(AppleLoginTypeFailure,errorMsg,dic);
    }
}
 
#pragma mark ————————————— 告诉代理应该在哪个window 展示内容给用户 —————————————
- (ASPresentationAnchor)presentationAnchorForAuthorizationController:(ASAuthorizationController *)controller API_AVAILABLE(ios(13.0))
{
    return [UIApplication sharedApplication].delegate.window;
}

@end

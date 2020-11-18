/****************************************************************************
 Copyright (c) 2010-2013 cocos2d-x.org
 Copyright (c) 2013-2016 Chukong Technologies Inc.
 Copyright (c) 2017-2018 Xiamen Yaji Software Co., Ltd.
 
 http://www.cocos2d-x.org
 
 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:
 
 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.
 
 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 ****************************************************************************/

#import "AppController.h"
#import "AppController+Login.h"

#import "cocos2d.h"
#include "scripting/js-bindings/manual/ScriptingCore.h"

#import "AppDelegate.h"
#import "RootViewController.h"

#import "CXLiveRoomViewController.h"
#import "CXUserInfoViewController.h"
#import "CXBaseNavigationController.h"

// Pay
#import <AlipaySDK/AlipaySDK.h>
#import <WXApi.h>
#import "CXIPAPurchaseManager.h"
#import "CXThirdPayManager.h"

// 引入 JPush 功能所需头文件
#import "JPUSHService.h"
#import "JVERIFICATIONService.h"

// iOS10 注册 APNs 所需头文件
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif

// Photo
#import "CXPhotoManager.h"
#import <Photos/Photos.h>

// QRCode
#import "WSLNativeScanTool.h"

// 广告
#import <BUAdSDK/BUAdSDKManager.h>
#import <BUAdSDK/BUSplashAdView.h>
#import "CXBUAdRewardViewController.h"
#import <AppTrackingTransparency/AppTrackingTransparency.h>
#import <AdSupport/ASIdentifierManager.h>

// 设备信息
#import "CXPhoneBasicTools.h"

// OpenInstall
#import "OpenInstallSDK.h"

// 直播
#import "CXBaseTabBarViewController.h"
#import "CXUserModel.h"

// Bugly
#import <Bugly/Bugly.h>

//#import <StoreKit/StoreKit.h>

@interface AppController() <JPUSHRegisterDelegate, WXApiDelegate, BUSplashAdDelegate, OpenInstallDelegate>

@end

@implementation AppController

@synthesize window;

#pragma mark -
#pragma mark Application lifecycle

// cocos2d application instance
static AppDelegate s_sharedApplication;

- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions {
    
    cocos2d::Application *app = cocos2d::Application::getInstance();
    
    // Initialize the GLView attributes
    app->initGLContextAttrs();
    cocos2d::GLViewImpl::convertAttrs();
    
    // Override point for customization after application launch.

    // Add the view controller's view to the window and display.
    window = [[UIWindow alloc] initWithFrame: [[UIScreen mainScreen] bounds]];

    // Use RootViewController to manage CCEAGLView
    _viewController = [[RootViewController alloc]init];
//    _viewController.wantsFullScreenLayout = YES;
    
    // Set RootViewController to window
    if ( [[UIDevice currentDevice].systemVersion floatValue] < 6.0)
    {
        // warning: addSubView doesn't work on iOS6
        [window addSubview: _viewController.view];
    }
    else
    {
        // use this method on ios6
        [window setRootViewController:_viewController];
    }

    [window makeKeyAndVisible];
    
    // 穿山甲
    [self setupBUAdSDK];
    
    // OpenInstall
    [OpenInstallSDK initWithDelegate:self];

//    [[UIApplication sharedApplication] setStatusBarHidden:true];
    
    // IMPORTANT: Setting the GLView should be done after creating the RootViewController
    cocos2d::GLView *glview = cocos2d::GLViewImpl::createWithEAGLView((__bridge void *)_viewController.view);
    cocos2d::Director::getInstance()->setOpenGLView(glview);
    
    //run the cocos2d-x game scene
    app->run();
    
    // 极光
    [self configureJPushOptions:launchOptions];
    
    // 微信注册
    [WXApi registerApp:WX_AppKey universalLink:WX_UniversalLinks];
    
    // Bugly
    [Bugly startWithAppId:@"b59315e4a7"];
    

    
    // 苹果内购监听
//    [[CXIPAPurchaseManager manager] startManager];

    [[UIApplication sharedApplication] setIdleTimerDisabled: YES];
    
    // 权限获取
    [CXTools getVideoAuthStatus];
    [CXTools getAudioAuthStatus];
    
    return YES;
}
//#pragma mark - SKPaymentTransactionObserver
//
//- (void)paymentQueue:(SKPaymentQueue *)queue updatedTransactions:(NSArray<SKPaymentTransaction *> *)transactions {
//    if (transactions) {
//
//    }
//}

#pragma mark - ======================== JPush =============================
#pragma mark - 注册极光tagID
/// 注册极光tagID
/// @param tagID
+ (void)registerJPUSHTagsId:(NSString*_Nonnull)tagID {
    NSSet *tags = [NSSet setWithObject:tagID];
    [JPUSHService setTags:tags completion:^(NSInteger iResCode, NSSet *iTags, NSInteger seq) {
        if (iResCode == 0) {
            NSLog(@"tags设置成功=======%@",tagID);
        }
    } seq:100];
}

- (void)configureJPushOptions:(NSDictionary *)launchOptions {
    JPUSHRegisterEntity * entity = [[JPUSHRegisterEntity alloc] init];
    if (@available(iOS 12.0, *)) {
        entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound|JPAuthorizationOptionProvidesAppNotificationSettings;
    } else {
        entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound;
    }
    [JPUSHService registerForRemoteNotificationConfig:entity delegate:self];
    
    __block NSString *advertisingId;
    if (@available(iOS 14, *)) {
        // iOS14及以上版本需要先请求权限
        [ATTrackingManager requestTrackingAuthorizationWithCompletionHandler:^(ATTrackingManagerAuthorizationStatus status) {
            // 获取到权限后，依然使用老方法获取idfa
            if (status == ATTrackingManagerAuthorizationStatusAuthorized) {
                advertisingId = [[ASIdentifierManager sharedManager].advertisingIdentifier UUIDString];
            }
        }];
    } else {
        // iOS14以下版本依然使用老方法
        // 判断在设置-隐私里用户是否打开了广告跟踪
        if ([[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled]) {
            advertisingId = [[ASIdentifierManager sharedManager].advertisingIdentifier UUIDString];
        }
    }
    
    [JPUSHService setupWithOption:launchOptions appKey:jPush_appKey
                          channel:@"App Store"
                 apsForProduction:NO
            advertisingIdentifier:advertisingId];
    [JPUSHService registrationIDCompletionHandler:^(int resCode, NSString *registrationID) {
        if(resCode == 0){
            NSLog(@"registrationID获取成功：%@",registrationID);
            [CXClientModel instance].registration_id = registrationID;
        } else {
            NSLog(@"registrationID获取失败，code：%d",resCode);
        }
    }];
    
    NSString *idfaStr = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    JVAuthConfig *config = [[JVAuthConfig alloc] init];
    config.appKey = jPush_appKey;
    config.advertisingId = idfaStr;
    config.timeout = 5000;
    [JVERIFICATIONService setupWithConfig:config];
    [JVERIFICATIONService setDebug:YES];
}

- (void)application:(UIApplication *)application didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
    /// Required - 注册 DeviceToken
    [JPUSHService registerDeviceToken:deviceToken];
}

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    if ([url.host isEqualToString:@"safepay"]) {
        // 支付跳转支付宝钱包进行支付，处理支付结果
        [[AlipaySDK defaultService] processOrderWithPaymentResult:url standbyCallback:^(NSDictionary *resultDic) {
            [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXRechargeViewController_alipay object:resultDic];
            if ([CXOCJSBrigeManager manager].paySuccessMethod.length > 0) {
                if ([resultDic.allKeys containsObject:@"resultStatus"] && [[resultDic objectForKey:@"resultStatus"] integerValue] == 9000) {
                    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:@"0"];
                } else {
                    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:@"-1"];
                }
            }
            
        }];
        
        // 授权跳转支付宝钱包进行支付，处理支付结果
        [[AlipaySDK defaultService] processAuth_V2Result:url standbyCallback:^(NSDictionary *resultDic) {
            [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXRechargeViewController_alipay object:resultDic];
            if ([CXOCJSBrigeManager manager].paySuccessMethod.length > 0) {
                if ([resultDic.allKeys containsObject:@"resultStatus"] && [[resultDic objectForKey:@"resultStatus"] integerValue] == 9000) {
                    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:@"0"];
                } else {
                    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:@"-1"];
                }
            }
        }];
    } else {
        [WXApi handleOpenURL:url delegate:self];
    }
    
    [OpenInstallSDK handLinkURL:url];
    
    return YES;
}

- (void)applicationWillResignActive:(UIApplication *)application {
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
    // We don't need to call this method any more. It will interrupt user defined game pause&resume logic
    /* cocos2d::Director::getInstance()->pause(); */
}

- (void)applicationDidBecomeActive:(UIApplication *)application {
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
    // We don't need to call this method any more. It will interrupt user defined game pause&resume logic
    /* cocos2d::Director::getInstance()->resume(); */
    
    [[UIApplication sharedApplication] setApplicationIconBadgeNumber:0];
    
    cocos2d::Application::getInstance()->applicationWillEnterForeground();
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later. 
     If your application supports background execution, called instead of applicationWillTerminate: when the user quits.
     */
    cocos2d::Application::getInstance()->applicationDidEnterBackground();
}

- (void)applicationWillEnterForeground:(UIApplication *)application {
    /*
     Called as part of  transition from the background to the inactive state: here you can undo many of the changes made on entering the background.
     */
//    cocos2d::Application::getInstance()->applicationWillEnterForeground();
}

- (void)applicationWillTerminate:(UIApplication *)application {
    /*
     Called when the application is about to terminate.
     See also applicationDidEnterBackground:.
     */
}

#pragma mark- ================ JPUSHRegisterDelegate ===================

// iOS 12 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center openSettingsForNotification:(UNNotification *)notification API_AVAILABLE(ios(10.0)){
    if (notification && [notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        //从通知界面直接进入应用
    } else {
        //从通知设置界面进入应用
    }
}

// iOS 10 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(NSInteger))completionHandler  API_AVAILABLE(ios(10.0)){
    NSDictionary * userInfo = notification.request.content.userInfo;
    
    UNNotificationRequest *request = notification.request; // 收到推送的请求
    UNNotificationContent *content = request.content; // 收到推送的消息内容
    
    NSNumber *badge = content.badge;  // 推送消息的角标
    NSString *body = content.body;    // 推送消息体
    UNNotificationSound *sound = content.sound;  // 推送消息的声音
    NSString *subtitle = content.subtitle;  // 推送消息的副标题
    NSString *title = content.title;  // 推送消息的标题
    
    if([notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        [JPUSHService handleRemoteNotification:userInfo];
        NSLog(@"iOS10 前台收到远程通知:%@", [self logDic:userInfo]);
    } else {
        // 判断为本地通知
        NSLog(@"iOS10 前台收到本地通知:{\nbody:%@，\ntitle:%@,\nsubtitle:%@,\nbadge：%@，\nsound：%@，\nuserInfo：%@\n}",body,title,subtitle,badge,sound,userInfo);
    }
    completionHandler(UNNotificationPresentationOptionBadge|UNNotificationPresentationOptionSound|UNNotificationPresentationOptionAlert); // 需要执行这个方法，选择是否提醒用户，有Badge、Sound、Alert三种类型可以设置
}

// iOS 10 Support
- (void)jpushNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler  API_AVAILABLE(ios(10.0)){
    NSDictionary * userInfo = response.notification.request.content.userInfo;
    UNNotificationRequest *request = response.notification.request; // 收到推送的请求
    UNNotificationContent *content = request.content; // 收到推送的消息内容
    
    NSNumber *badge = content.badge;  // 推送消息的角标
    NSString *body = content.body;    // 推送消息体
    UNNotificationSound *sound = content.sound;  // 推送消息的声音
    NSString *subtitle = content.subtitle;  // 推送消息的副标题
    NSString *title = content.title;  // 推送消息的标题
    
    if([response.notification.request.trigger isKindOfClass:[UNPushNotificationTrigger class]]) {
        [JPUSHService handleRemoteNotification:userInfo];
        NSLog(@"iOS10 收到远程通知:%@", [self logDic:userInfo]);
        [self showNotificationContent:content userInfo:userInfo];
    }
    else {
        // 判断为本地通知
        NSLog(@"iOS10 收到本地通知:{\nbody:%@，\ntitle:%@,\nsubtitle:%@,\nbadge：%@，\nsound：%@，\nuserInfo：%@\n}",body,title,subtitle,badge,sound,userInfo);
    }
    
    completionHandler();  // 系统要求执行这个方法
}

- (void)jpushNotificationAuthorization:(JPAuthorizationStatus)status withInfo:(NSDictionary *)info {
    
}


- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    
    // Required, iOS 7 Support
    [JPUSHService handleRemoteNotification:userInfo];
    [self notificationJumpVC:userInfo];
    completionHandler(UIBackgroundFetchResultNewData);
}

// log NSSet with UTF8
// if not ,log will be \Uxxx
- (NSString *)logDic:(NSDictionary *)dic {
    if (![dic count]) {
        return nil;
    }
    NSString *tempStr1 =
    [[dic description] stringByReplacingOccurrencesOfString:@"\\u"
                                                 withString:@"\\U"];
    NSString *tempStr2 =
    [tempStr1 stringByReplacingOccurrencesOfString:@"\"" withString:@"\\\""];
    NSString *tempStr3 =
    [[@"\"" stringByAppendingString:tempStr2] stringByAppendingString:@"\""];
    NSData *tempData = [tempStr3 dataUsingEncoding:NSUTF8StringEncoding];
    NSString *str =
    [NSPropertyListSerialization propertyListFromData:tempData
                                     mutabilityOption:NSPropertyListImmutable
                                               format:NULL
                                     errorDescription:NULL];
    
    
    return str;
}

- (void)showNotificationContent:(UNNotificationContent *)content userInfo:(NSDictionary *)userInfo  API_AVAILABLE(ios(10.0)){
    [self notificationJumpVC:userInfo];
}

- (void)notificationJumpVC:(NSDictionary *)userInfo {
//    UIViewController *rootVC = [MTool currentViewController];
//    if ([rootVC isKindOfClass:[UINavigationController class]]) {
//        [rootVC.navigationController popToRootViewControllerAnimated:true];
//    } else if (rootVC.presentedViewController) {
//        [rootVC dismissViewControllerAnimated:YES completion:nil];
//    }
//
//    NSString *deepLink = userInfo[@"DeepLink"];
//    NSString *deepLink_value = userInfo[@"value"];
//    if ([deepLink isEqualToString:@"profileId"]) { // 属性页面
//        XSUserInforViewController * userinfor=[XSUserInforViewController new];
//        userinfor.user_Id  =  deepLink_value;
//        [rootVC.navigationController pushViewController:userinfor animated:YES];
//    } else if ([deepLink isEqualToString:@"roomId"]) { // 视频房内
//        [self joinRoom:deepLink_value];
//    } else if ([deepLink isEqualToString:@"feedId"]) { // 动态明细
//         self.rootTabVC.selectedIndex = 2;
//    } else if ([deepLink isEqualToString:@"roomPage"]) { // 大厅
//        self.rootTabVC.selectedIndex = 0;
//    } else if ([deepLink isEqualToString:@"profilePage"]) { // 发现
//        self.rootTabVC.selectedIndex = 1;
//    } else if ([deepLink isEqualToString:@"feedPage"]) { // 动态明细
//        XYDynamicDetailViewController *vc = [[XYDynamicDetailViewController alloc] initWithForum_id:deepLink_value];
//        [rootVC.navigationController pushViewController:vc animated:YES];
//    } else if ([deepLink isEqualToString:@"walletPage"]) { // 钱包
//        CXMyWalletViewController *vc = [[CXMyWalletViewController alloc] init];
//        [rootVC.navigationController pushViewController:vc animated:YES];
//    } else if ([deepLink isEqualToString:@"followPage"]) { // 关注
//        focusAndFansVC *vc = [focusAndFansVC suspendTopPageVCWithIndex:0];
//        [rootVC.navigationController pushViewController:vc animated:YES];
//    } else if ([deepLink isEqualToString:@"followerPage"]) { // 粉丝
//        focusAndFansVC *vc = [focusAndFansVC suspendTopPageVCWithIndex:1];
//        [rootVC.navigationController pushViewController:vc animated:YES];
//    } else if ([deepLink isEqualToString:@"visitPage"]) { // 最近来访
//        nearViSitorVC *VC = [[nearViSitorVC alloc]init];
//        [rootVC.navigationController pushViewController:VC animated:YES];
//    } else if ([deepLink isEqualToString:@"invitePage"]) { // 我的邀请
//        CXMineInviteViewController *vc = [CXMineInviteViewController new];
//        [rootVC.navigationController pushViewController:vc animated:YES];
//    } else if ([deepLink isEqualToString:@"msgPage"]) {// 好友
//        self.rootTabVC.selectedIndex = 3;
//    }
    
}


#pragma mark - ================ WXApiDelegate ===================
- (void)onResp:(BaseResp *)resp {
    if ([resp isKindOfClass:[PayResp class]]){
        [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXRechargeViewController_weixin object:[resp modelToJSONObject]];
        if ([CXOCJSBrigeManager manager].paySuccessMethod.length > 0) {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:[NSString stringWithFormat:@"%d",resp.errCode]];
        }
    } else if ([resp isKindOfClass:[SendAuthResp class]]){
        SendAuthResp *resp2 = (SendAuthResp *)resp;
        [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXMySettingWechatBindViewController_weixin object:resp2];
        if ([CXOCJSBrigeManager manager].wxLoginMethod.length > 0) {
            [self weChatLoginSuccess:resp2];
        }
    } else if ([resp isKindOfClass:[SendMessageToWXResp class]]) {
        SendMessageToWXResp *resp2 = (SendMessageToWXResp *)resp;
        [[NSNotificationCenter defaultCenter] postNotificationName:@"CXLiveRoomViewControllerWXShare" object:resp2];
    }
}

#pragma mark -  ================ Pay ===================

/// 支付入口
/// @param payType 支付渠道类型：1:苹果内购 2:支付宝 3:微信
/// @param payParam 支付参数：
/// @param userID 当前支付用户ID
/// @param orderNo 己方订单号
/// @param paySuccessMethod 支付成功通知的方法名
+ (void)appPurchaseWithPayType:(NSString *_Nonnull)payType payParam:(NSString *)payParam userID:(NSString *)userID orderNo:(NSString *_Nonnull)orderNo paySuccessMethod:(NSString *)paySuccessMethod {
    [CXOCJSBrigeManager manager].paySuccessMethod = paySuccessMethod;
    if ([payType isEqualToString:@"ios"]) {
        [CXIPAPurchaseManager manager].userid = userID;
        [CXIPAPurchaseManager manager].purchaseType = MaJiang;
        [CXIPAPurchaseManager manager].order_sn = orderNo;
        [[CXIPAPurchaseManager manager] inAppPurchaseWithProductID:payParam iapResult:^(BOOL isSuccess, NSDictionary *param, NSString *errorMsg) {
            if (isSuccess == YES) {
                NSString *paramStr = [param jsonStringEncoded];
                if ([CXOCJSBrigeManager manager].paySuccessMethod.length > 0) {
                    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:paramStr];
                }
            }
        }];
    } else if ([payType isEqualToString:@"alipay"]) {
        [[CXThirdPayManager sharedApi] aliPayWithPayParam:payParam success:nil failure:^(PayCode code) {
            if ([CXOCJSBrigeManager manager].paySuccessMethod.length > 0) {
                [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:@"-1"];
            }
        }];
    } else if ([payType isEqualToString:@"wx"]) {
        [[CXThirdPayManager sharedApi] wxPayWithPayParam:payParam success:nil failure:^(PayCode code) {
            if ([CXOCJSBrigeManager manager].paySuccessMethod.length > 0) {
                [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].paySuccessMethod param:@"-1"];
            }
        }];
    }
}

#pragma mark -  ================ Photo ===================
+ (void)selectedOnePhotoWithPutParam:(NSString *)putParam method:(NSString *_Nonnull)method {
    if (putParam.length <= 0) {
        return;
    }
    NSDictionary *dict = [putParam jsonValueDecoded];
    if ([dict.allKeys containsObject:@"info"]) {
        NSDictionary *info = dict[@"info"];
        [[CXPhotoManager manager] showPickerWith:[CXTools currentViewController] putParam:[info jsonStringEncoded] allowEdit:YES completeBlock:^(NSString * _Nonnull imageUrl) {
            NSLog(@"imageUrl=%@", imageUrl);
            if (method.length > 0) {
                [AppController dispatchCustomEventWithMethod:method param:imageUrl];
            }
        }];
    }
}

#pragma mark - ================ QRCode ===================
/// 生成二维码
/// @param codeString 二维码字符串
+ (CIImage *)creatQRcodeWithUrlstring:(NSString *)urlString{
    
    // 1.实例化二维码滤镜
    CIFilter *filter = [CIFilter filterWithName:@"CIQRCodeGenerator"];
    // 2.恢复滤镜的默认属性 (因为滤镜有可能保存上一次的属性)
    [filter setDefaults];
    // 3.将字符串转换成NSdata
    NSData *data  = [urlString dataUsingEncoding:NSUTF8StringEncoding];
    // 4.通过KVO设置滤镜, 传入data, 将来滤镜就知道要通过传入的数据生成二维码
    [filter setValue:data forKey:@"inputMessage"];
    // 5.生成二维码
    CIImage *outputImage = [filter outputImage];
    return outputImage;
}
+ (UIImage *)createNonInterpolatedUIImageFormCIImage:(CIImage *)image withSize:(CGFloat) size
{
    CGRect extent = CGRectIntegral(image.extent);
    CGFloat scale = MIN(size/CGRectGetWidth(extent), size/CGRectGetHeight(extent));
    
    // 1.创建bitmap;
    size_t width = CGRectGetWidth(extent) * scale;
    size_t height = CGRectGetHeight(extent) * scale;
    CGColorSpaceRef cs = CGColorSpaceCreateDeviceGray();
    CGContextRef bitmapRef = CGBitmapContextCreate(nil, width, height, 8, 0, cs, (CGBitmapInfo)kCGImageAlphaNone);
    CIContext *context = [CIContext contextWithOptions:nil];
    CGImageRef bitmapImage = [context createCGImage:image fromRect:extent];
    CGContextSetInterpolationQuality(bitmapRef, kCGInterpolationNone);
    CGContextScaleCTM(bitmapRef, scale, scale);
    CGContextDrawImage(bitmapRef, extent, bitmapImage);
    
    // 2.保存bitmap到图片
    CGImageRef scaledImage = CGBitmapContextCreateImage(bitmapRef);
    CGContextRelease(bitmapRef);
    CGImageRelease(bitmapImage);
    return [UIImage imageWithCGImage:scaledImage];
}

+ (void)createQRCodeImageWithString:(nonnull NSString *)codeString method:(NSString *_Nonnull)method {
//    UIImage *tempImage = [WSLNativeScanTool createQRCodeImageWithString:codeString andSize:CGSizeMake(200, 200) andBackColor:[UIColor whiteColor] andFrontColor:[UIColor blackColor] andCenterImage:nil];
    UIImage *tempImage = [self createNonInterpolatedUIImageFormCIImage:[self creatQRcodeWithUrlstring:codeString] withSize:150];
    //图片保存路径
    //这里将图片放在沙盒的documents/image文件夹中
    NSString *documentsPath = [NSHomeDirectory() stringByAppendingPathComponent:@"Documents"];
    NSString *imgPath = [documentsPath stringByAppendingPathComponent:@"image"];

    //文件管理器
    NSFileManager *fileManager = [NSFileManager defaultManager];
    //生成唯一字符串
    NSString *uuid = [[NSUUID UUID] UUIDString];
    //生成文件名
    NSString *fileName = [NSString stringWithFormat:@"%@.png",uuid];

    //把刚刚由图片转成的data对象拷贝至沙盒中 并保存为xxxxx-xxxx-xxx...xxx.png
    /******保存之前最好先清空下，不然占用磁盘越来越大********/
    [fileManager removeItemAtPath:imgPath error:nil];
    /************************************************/

    [fileManager createDirectoryAtPath:imgPath withIntermediateDirectories:YES attributes:nil error:nil];
    NSData *data = UIImageJPEGRepresentation(tempImage, 0.5);
    [fileManager createFileAtPath:[imgPath stringByAppendingPathComponent:fileName] contents:data attributes:nil];

    //得到选择后沙盒中图片的完整路径
    NSString *filePath = [[NSString alloc]initWithFormat:@"%@",[imgPath stringByAppendingPathComponent:fileName]];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [AppController dispatchCustomEventWithMethod:method param:filePath];
    });
}

#pragma mark - ================ 广告 ===================
// 打开激励视频
+ (void)openBUAdRewardWithUserId:(NSString *_Nonnull)userId method:(NSString *_Nonnull)method {
    [CXOCJSBrigeManager manager].BUAdRewardMethod = method;
    //激励视频
    [[CXBUAdRewardViewController manager] openAdWithUserId:userId];
}

- (void)setupBUAdSDK {
    //optional
    //GDPR 0 close privacy protection, 1 open privacy protection
    [BUAdSDKManager setGDPR:0];
    //optional
    //Coppa 0 adult, 1 child
    [BUAdSDKManager setCoppa:0];
    //BUAdSDK requires iOS 9 and up
    [BUAdSDKManager setAppID:BUDAd_appKey];

    [BUAdSDKManager setIsPaidApp:NO];
    
    // 初始化激励视屏
    UIViewController *_csjAdReward = [[CXBUAdRewardViewController alloc] init];
    [_viewController.view addSubview:_csjAdReward.view];

    
    // splash AD demo
//    [self addSplashAD];
}

- (void)addSplashAD {
    CGRect frame = [UIScreen mainScreen].bounds;
    BUSplashAdView *splashAdView = [[BUSplashAdView alloc] initWithSlotID:BUDAd_SlotID frame:frame];
    // tolerateTimeout = CGFLOAT_MAX , The conversion time to milliseconds will be equal to 0
    splashAdView.tolerateTimeout = 10;
    splashAdView.delegate = self;

    UIWindow *keyWindow = self.window;
    [splashAdView loadAdData];
    [keyWindow.rootViewController.view addSubview:splashAdView];
    splashAdView.rootViewController = keyWindow.rootViewController;
}

- (void)splashAdDidLoad:(BUSplashAdView *)splashAd {
//    if (splashAd.zoomOutView) {
//        UIViewController *parentVC = [UIApplication sharedApplication].keyWindow.rootViewController;
//        [parentVC.view addSubview:splashAd.zoomOutView];
//        [parentVC.view bringSubviewToFront:splashAd];
//        //Add this view to your container
//        [parentVC.view insertSubview:splashAd.zoomOutView belowSubview:splashAd];
//        splashAd.zoomOutView.rootViewController = parentVC;
//    }
}

- (void)splashAdDidClose:(BUSplashAdView *)splashAd {
    [splashAd removeFromSuperview];
}

- (void)splashAdDidClick:(BUSplashAdView *)splashAd {
    [splashAd removeFromSuperview];
}

- (void)splashAdDidClickSkip:(BUSplashAdView *)splashAd {
    [splashAd removeFromSuperview];
}

- (void)splashAd:(BUSplashAdView *)splashAd didFailWithError:(NSError *)error {
    [splashAd removeFromSuperview];
}

#pragma mark -  ================ OpenInstall ===================
- (BOOL)application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray<id<UIUserActivityRestoring>> * _Nullable))restorationHandler {
    //判断是否通过OpenInstall Universal Link 唤起App
    if ([OpenInstallSDK continueUserActivity:userActivity]){//如果使用了Universal link ，此方法必写
        return YES;
    }
    //其他第三方回调；
    return YES;
}

- (void)getWakeUpParams:(OpeninstallData *)appData {
    if (appData.data) {
        [CXClientModel instance].OpeninstallData = appData.data;
    }
}

+ (void)getOpenInstallParamWithMethod:(NSString *_Nonnull)method {
    [CXOCJSBrigeManager manager].openInstallParamMethod = method;
    [[OpenInstallSDK defaultManager] getInstallParmsCompleted:^(OpeninstallData * _Nullable appData) {
        if (appData.data && [appData.data.allKeys containsObject:@"installPid"]) {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:appData.data[@"installPid"]];
        } else {
            if ([CXClientModel instance].OpeninstallData && [[CXClientModel instance].OpeninstallData.allKeys containsObject:@"installPid"]) {
                [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:[CXClientModel instance].OpeninstallData[@"installPid"]];
            }
        }
    }];
}

#pragma mark - 获取手机基本信息
+ (NSString *)getImei {
    NSString *imei = [NSString stringWithFormat:@"%@%@", [CXPhoneBasicTools getUUID], [CXPhoneBasicTools getIdentifierForAdvertising]];
    return imei;
}
/// 获取当前连接网络
+ (NSString *_Nullable)getNetWorkStates {
    NSString *netWorkStates = [CXPhoneBasicTools getNetWorkStates];
    return netWorkStates;
}
/// 获取网络信号
+ (NSString *_Nullable)getSignalStrength {
    NSString *signalStrength = [NSString stringWithFormat:@"%ld",[CXPhoneBasicTools getSignalStrength]];
    return signalStrength;
}
/// 获取电量
+ (NSString *_Nullable)getBatteryLevel {
    NSString *batteryLevel = [NSString stringWithFormat:@"%0.2f",[CXPhoneBasicTools getBatteryLevel]];
    return batteryLevel;
}

+ (NSString *_Nullable)getDevice {
    return [CXPhoneBasicTools deviceName];
}

/// 获取版本号
+ (NSString *_Nullable)getVersion {
    return [CXPhoneBasicTools getVersion];
}

#pragma mark - ================ 复制到剪贴板 ===================

/// 复制到剪贴板
/// @param copyStr 复制的字符串
+ (void)copyToPasteboard:(NSString *)copyStr {
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = copyStr;
//    [[CXTools currentViewController] toast:@"复制成功"];
}

#pragma mark - ================ OC调用JS ===================

/// 派发JS事件
/// @param method 方法名
/// @param param 参数
+ (void)dispatchCustomEventWithMethod:(NSString *)method param:(NSString *)param {
    if (!method || method.length <= 0) {
        return;
    }
    
    std::string strMethod = [method UTF8String];
    if (param.length > 0) {
        std::string strParam = [param UTF8String];
        std::string jsCallStr = cocos2d::StringUtils::format("cc.eventManager.dispatchCustomEvent(\"%s\",'%s');", strMethod.c_str(),strParam.c_str());
        ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
    } else {
        std::string jsCallStr = cocos2d::StringUtils::format("cc.eventManager.dispatchCustomEvent(\"%s\");", strMethod.c_str());
        ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
    }
    
    [AppController setOrientation:@""];
}

+ (void)JsCallBack:(NSString *)funcNameStr param:(NSString *)param {
    std::string funcName = [funcNameStr UTF8String];
    std::string paramStr  = [param UTF8String];
    std::string jsCallStr = cocos2d::StringUtils::format("%s(\"%s\");",funcName.c_str(), paramStr.c_str());
    ScriptingCore::getInstance()->evalString(jsCallStr.c_str());
}

#pragma mark - ================ 横竖屏 ===================
UIInterfaceOrientationMask oMask = UIInterfaceOrientationMaskLandscape;

- (UIInterfaceOrientationMask)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(UIWindow *)window{
    return oMask;
}

+ (void)setOrientation:(NSString*)dir {
    [[UIDevice currentDevice] setValue:[NSNumber numberWithInteger:UIDeviceOrientationUnknown] forKey:@"orientation"];
    
    if([dir isEqualToString:@"V"]){
        oMask = UIInterfaceOrientationMaskPortrait;
        [[UIDevice currentDevice] setValue:[NSNumber numberWithInteger:UIInterfaceOrientationPortrait] forKey:@"orientation"];
    } else {
        oMask = UIInterfaceOrientationMaskLandscape;
        [[UIDevice currentDevice] setValue:[NSNumber numberWithInteger:UIInterfaceOrientationLandscapeRight] forKey:@"orientation"];
    }
}

#pragma mark - ================ 直播相关 ===================
/// 从麻将进入视频
+ (void)enterLiveBroadcastWithToken:(NSString *)param {
    [[CXOCJSBrigeManager manager] clear];
    if (param.length <= 0 ) {
        return;
    }
    NSDictionary *dict = [param jsonValueDecoded];
    if ([dict.allKeys containsObject:@"applePayType"]) {
        [CXClientModel instance].applePayType = dict[@"applePayType"];
    }
    NSMutableDictionary *paramDict = [[NSMutableDictionary alloc] init];
    NSString *url = @"/index.php/Api/Member/login";
    if ([dict[@"platform"] integerValue] == 3) { // 微信登录
        [paramDict setValue:dict[@"username"] forKey:@"wx_id"];
        url = @"/index.php/Api/Member/wx_login";
    } else {
        [paramDict setValue:dict[@"username"] forKey:@"username"];
    }
    [paramDict setValue:dict[@"nickname"] forKey:@"nickname"];
    [paramDict setValue:dict[@"avatar"] forKey:@"avatar"];
    [paramDict setValue:[CXClientModel instance].registration_id forKey:@"registration_id"];
    
    [CXHTTPRequest POSTWithURL:url parameters:paramDict callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"info"]];
            [CXClientModel instance].token = user.token;
            [CXClientModel instance].userId = user.user_id;
            [CXClientModel instance].username = user.username;
            [CXClientModel instance].nickname = user.nickname;
            [CXClientModel instance].avatar = user.avatar;
            [CXClientModel instance].sex = user.sex;
            [CXClientModel instance].card_num = user.card_num;
            [CXClientModel instance].is_receive = user.is_receive;
            
            [[EMClient sharedClient] loginWithUsername:user.user_id password:user.user_id completion:^(NSString *aUsername, EMError *aError) {
                [CXOCJSBrigeManager manager].resumeAllMusicMethod = @"resumeAllMusic";
                [AppController setOrientation:@"V"];
                CXBaseTabBarViewController *tabbarVC = [CXBaseTabBarViewController new];
                tabbarVC.modalPresentationStyle = UIModalPresentationFullScreen;
                [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:tabbarVC animated:YES completion:nil];
//                if (aError) {
////                    if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:user:loginError:)]) {
////                        [wself.delegate easemob:wself user:aUsername loginError:EMErrorToNSErrro(aError)];
////                    }
//                } else {
////                    if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:didLoginWithUser:)]) {
////                        [wself.delegate easemob:wself didLoginWithUser:aUsername];
////                    }
//                }
            }];
            
//            [[CXClientModel instance].easemob login:user.user_id];
        } else {
            [CXTools showAlertWithMessage:responseObject[@"desc"]];
        }
    }];
}

+ (void)joinRoom:(NSString *)roomId {
    if (roomId.length <= 0) {
        return;
    }
    if ([CXTools getAudioAuthStatus] == NO) {
        [CXTools showSettingAlertViewTitle:@"麦克风权限未开启" content:@"麦克风权限未开启，请进入系统【设置】>【隐私】>【麦克风】中打开开关,开启麦克风功能"];
        return;
    }
    if ([CXTools getVideoAuthStatus] == NO) {
        [CXTools showSettingAlertViewTitle:@"相机权限未开启" content:@"相机权限未开启，请进入系统【设置】>【隐私】>【相机】中打开开关,开启相机功能"];
        return;
    }
    
    [MBProgressHUD showHUD];
    
    [[CXClientModel instance].easemob leaveRoom];
    [[CXClientModel instance].agoraEngineManager.engine leaveChannel:nil];
    [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleAudience];
    [[CXClientModel instance].agoraEngineManager.engine setVideoSource:nil];
    
    [[CXClientModel instance] joinRoom:roomId callback:^(NSString * _Nonnull roomId, BOOL success) {
        [MBProgressHUD hideHUD];
        if (success) {
            UIViewController *vc = [CXTools currentViewController];
            CXLiveRoomViewController *roomVC = [CXLiveRoomViewController new];
            CXBaseNavigationController *nav = [[CXBaseNavigationController alloc] initWithRootViewController:roomVC];
            [vc presentViewController:nav modalStyle:UIModalPresentationFullScreen animated:YES completion:nil];
        } else {
            [[CXTools currentViewController] toast:@"加入房间失败，请稍后重试"];
        }
    }];
}

+ (void)showUserProfile:(NSString *)userId {
    CXUserInfoViewController *vc = [CXUserInfoViewController new];
    vc.user_Id = userId;
    UIViewController *currentVC = [CXTools currentViewController];
    [currentVC.navigationController pushViewController:vc animated:YES];
    
}

+ (void)showUserProfile:(NSString *_Nonnull)userId target:(UIViewController *)target {
    CXUserInfoViewController *vc = [CXUserInfoViewController new];
    vc.user_Id = userId;
    [target.navigationController pushViewController:vc animated:YES];
}

+ (void)logout {
    NSLog(@"登录异常，请重新登录");
    [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXBaseTabBarViewController_leaveOut object:nil];
}

#pragma mark -
#pragma mark Memory management

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application {
    /*
     Free up as much memory as possible by purging cached data objects that can be recreated (or reloaded from disk) later.
     */
}

#if __has_feature(objc_arc)
#else
- (void)dealloc {
    [window release];
    [_viewController release];
    [super dealloc];
}
#endif


@end

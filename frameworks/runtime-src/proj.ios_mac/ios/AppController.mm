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
#import "CXConfigObject.h"

@interface AppController() <JPUSHRegisterDelegate, WXApiDelegate, BUSplashAdDelegate, OpenInstallDelegate>

@property (nonatomic, copy) NSString *registration_id;

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

//    [[UIApplication sharedApplication] setStatusBarHidden:true];
    
    // IMPORTANT: Setting the GLView should be done after creating the RootViewController
    cocos2d::GLView *glview = cocos2d::GLViewImpl::createWithEAGLView((__bridge void *)_viewController.view);
    cocos2d::Director::getInstance()->setOpenGLView(glview);
    
    //run the cocos2d-x game scene
    app->run();
        
    // 苹果内购监听
    [[CXIPAPurchaseManager manager] startManager];
    
    // 极光
    [self configureJPushOptions:launchOptions];
    
    // 微信注册
    [WXApi registerApp:WX_AppKey universalLink:WX_UniversalLinks];
    
    // 穿山甲
    [self setupBUAdSDK];
    
    // OpenInstall
    [OpenInstallSDK initWithDelegate:self];

    return YES;
}

#pragma mark - ======================== JPush =============================
- (void)configureJPushOptions:(NSDictionary *)launchOptions {
    JPUSHRegisterEntity * entity = [[JPUSHRegisterEntity alloc] init];
    if (@available(iOS 12.0, *)) {
        entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound|JPAuthorizationOptionProvidesAppNotificationSettings;
    } else {
        entity.types = JPAuthorizationOptionAlert|JPAuthorizationOptionBadge|JPAuthorizationOptionSound;
    }
    [JPUSHService registerForRemoteNotificationConfig:entity delegate:self];
    
    [JPUSHService setupWithOption:launchOptions appKey:jPush_appKey
                          channel:@"Publish channel"
                 apsForProduction:FALSE
            advertisingIdentifier:nil];
    [JPUSHService registrationIDCompletionHandler:^(int resCode, NSString *registrationID) {
        if(resCode == 0){
            NSLog(@"registrationID获取成功：%@",registrationID);
            self.registration_id = registrationID;
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

- (BOOL)application:(UIApplication *)app openURL:(NSURL *)url options:(NSDictionary<UIApplicationOpenURLOptionsKey,id> *)options {
    if ([url.host isEqualToString:@"safepay"]) {
        // 支付跳转支付宝钱包进行支付，处理支付结果
        [[AlipaySDK defaultService] processOrderWithPaymentResult:url standbyCallback:^(NSDictionary *resultDic) {
            [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXRechargeViewController_alipay object:resultDic];
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:[resultDic jsonStringEncoded]];
        }];
        
        // 授权跳转支付宝钱包进行支付，处理支付结果
        [[AlipaySDK defaultService] processAuth_V2Result:url standbyCallback:^(NSDictionary *resultDic) {
            [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXRechargeViewController_alipay object:resultDic];
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:[resultDic jsonStringEncoded]];
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
    cocos2d::Application::getInstance()->applicationWillEnterForeground();
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
        [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:[[resp modelToJSONObject] jsonStringEncoded]];
    } else if ([resp isKindOfClass:[SendAuthResp class]]){
        SendAuthResp *resp2 = (SendAuthResp *)resp;
//        [[NSNotificationCenter defaultCenter] postNotificationName:@"CXLoginLaunchControllerWXLogin" object:resp2];
        [self weChatLoginSuccess:resp2];
    } else if ([resp isKindOfClass:[SendMessageToWXResp class]]) {
        SendMessageToWXResp *resp2 = (SendMessageToWXResp *)resp;
//        [[NSNotificationCenter defaultCenter] postNotificationName:@"CXLiveRoomViewControllerWXShare" object:resp2];
    }
}

#pragma mark -  ================ Pay ===================

/// 支付入口
/// @param payType 支付渠道类型：1:苹果内购 2:支付宝 3:微信
/// @param payParam 支付参数：
/// @param userID 当前支付用户ID
/// @param paySuccessMethod 支付成功通知的方法名
+ (void)appPurchaseWithPayType:(NSString *_Nonnull)payType payParam:(NSString *)payParam userID:(NSString *)userID paySuccessMethod:(NSString *)paySuccessMethod {
    [CXOCJSBrigeManager manager].paySuccessMethod = paySuccessMethod;
    if ([payType isEqualToString:@"ios"]) {
        [CXIPAPurchaseManager manager].userid = userID;
        [CXIPAPurchaseManager manager].purchaseType = MaJiang;
        [[CXIPAPurchaseManager manager] inAppPurchaseWithProductID:payParam iapResult:^(BOOL isSuccess, NSDictionary *param, NSString *errorMsg) {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:[param jsonStringEncoded]];
        }];
    } else if ([payType isEqualToString:@"alipay"]) {
        [[CXThirdPayManager sharedApi] aliPayWithPayParam:payParam success:^(PayCode code) {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:@"success"];
        } failure:^(PayCode code) {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:@"failure"];
        }];
    } else if ([payType isEqualToString:@"wx"]) {
        [[CXThirdPayManager sharedApi] wxPayWithPayParam:payParam success:^(PayCode code) {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:@"success"];
        } failure:^(PayCode code) {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:@"failure"];
        }];
    }
}

#pragma mark -  ================ Photo ===================
+ (void)selectedOnePhoto {
    [[CXPhotoManager manager] showPickerWith:[CXTools currentViewController] allowEdit:YES completeBlock:^(NSString * _Nonnull imageUrl) {
        NSLog(@"imageUrl=%@", imageUrl);
    }];
}

#pragma mark - ================ QRCode ===================
/// 生成二维码
/// @param codeString 二维码字符串
/// @param centerImage 中心图片
+ (UIImage *_Nonnull)createQRCodeImageWithString:(nonnull NSString *)codeString {
    return [WSLNativeScanTool createQRCodeImageWithString:codeString andSize:CGSizeMake(200, 200) andBackColor:[UIColor whiteColor] andFrontColor:[UIColor orangeColor] andCenterImage:nil];
}

#pragma mark - ================ 广告 ===================
// 打开激励视频
+ (void)openBUAdRewardViewControllerWithMethod:(NSString *_Nonnull)method {
    [CXOCJSBrigeManager manager].BUAdRewardMethod = method;
    //激励视频
    [[CXBUAdRewardViewController manager] openAd];
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
    
    // splash AD demo
//    [self addSplashAD];
    
    if (@available(iOS 14, *)) {
        // iOS14及以上版本需要先请求权限
        [ATTrackingManager requestTrackingAuthorizationWithCompletionHandler:^(ATTrackingManagerAuthorizationStatus status) {
            // 获取到权限后，依然使用老方法获取idfa
            if (status == ATTrackingManagerAuthorizationStatusAuthorized) {
                NSString *idfa = [[ASIdentifierManager sharedManager].advertisingIdentifier UUIDString];
                NSLog(@"%@",idfa);
            } else {
//                [CXTools showAlertWithMessage:@"请在设置-隐私-Tracking 允许App请求跟踪"];
            }
        }];
    } else {
        // iOS14以下版本依然使用老方法
        // 判断在设置-隐私里用户是否打开了广告跟踪
        if ([[ASIdentifierManager sharedManager] isAdvertisingTrackingEnabled]) {
            NSString *idfa = [[ASIdentifierManager sharedManager].advertisingIdentifier UUIDString];
            NSLog(@"%@",idfa);
        } else {
//            [CXTools showAlertWithMessage:@"请在设置-隐私-广告 打开广告跟踪功能"];
        }
    }
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

+ (void)getOpenInstallParamWithMethod:(NSString *_Nonnull)method {
    [CXOCJSBrigeManager manager].openInstallParamMethod = method;
    [[OpenInstallSDK defaultManager] getInstallParmsCompleted:^(OpeninstallData * _Nullable appData) {
        [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].openInstallParamMethod param:[appData.data jsonStringEncoded]];
    }];
}

#pragma mark - 获取手机基本信息
+ (NSString *)getImei {
    NSString *imei = [NSString stringWithFormat:@"%@%@", [CXPhoneBasicTools getUUID], [CXPhoneBasicTools getIdentifierForAdvertising]];
    return imei;
}

+ (NSString *_Nullable)getDevice {
    return [CXPhoneBasicTools deviceName];
}

#pragma mark - ================ 复制到剪贴板 ===================

/// 复制到剪贴板
/// @param copyStr 复制的字符串
+ (void)copyToPasteboard:(NSString *)copyStr {
    UIPasteboard *pasteboard = [UIPasteboard generalPasteboard];
    pasteboard.string = copyStr;
}

#pragma mark - ================ OC调用JS ===================

/// 派发JS事件
/// @param method 方法名
/// @param param 参数
+ (void)dispatchCustomEventWithMethod:(NSString *)method param:(NSString *)param {
    std::string strParam = [param UTF8String];
    std::string strMethod = [method UTF8String];
    std::string jsCallStr = cocos2d::StringUtils::format("cc.eventManager.dispatchCustomEvent(\"%s\",'%s');", strMethod.c_str(),strParam.c_str());
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
    if (param.length <= 0 ) {
        return;
    }
    NSDictionary *dict = [param jsonValueDecoded];
    if ([dict.allKeys containsObject:@"token"]) {
        [CXClientModel instance].token = dict[@"token"];
    }
    if ([dict.allKeys containsObject:@"applePayType"]) {
        [CXClientModel instance].applePayType = dict[@"applePayType"];
    }
    
    [CXConfigObject enterOnline];
}

+ (void)joinRoom:(NSString *)roomId {
    if (roomId.length <= 0) {
        return;
    }
    [[CXClientModel instance] joinRoom:roomId callback:^(NSString * _Nonnull roomId, BOOL success) {
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

+ (void)logout {
    NSLog(@"登录异常，请重新登录");
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

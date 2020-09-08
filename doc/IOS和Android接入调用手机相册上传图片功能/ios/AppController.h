/****************************************************************************
 Copyright (c) 2010-2013 cocos2d-x.org
 Copyright (c) 2013-2014 Chukong Technologies Inc.

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

@class RootViewController;
#import "WXApi.h"
//引入钉钉
#import <DTShareKit/DTOpenKit.h>
//引入闲聊
#import "XianliaoApiManager.h"
#import "XianliaoApiObject.h"

// iOS10注册APNs所需头文件
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
#import <UserNotifications/UserNotifications.h>
#endif
// 如果需要使用idfa功能所需要引入的头文件（可选）
#import <AdSupport/AdSupport.h>

// 引入高德地图定位功能所需头文件
#import <AMapLocationKit/AMapLocationManager.h>
#import <AMapFoundationKit/AMapServices.h>
#import <AMapFoundationKit/AMapFoundationKit.h>

//引入聊呗
#import <LBSDK/LBSDK.h>
#import <LBSDK/LBSDKApi.h>
#import <LBSDK/LBSDKApiObject.h>
#import <LBSDK/LBSDKResultCode.h>
#import "XGPush.h"
#import "DLService.h"
#import <WebKit/WebKit.h>
#import <UIKit/UIKit.h>

#import "WebViewJavascriptBridge.h"
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_10_0
#import <UserNotifications/UserNotifications.h>
#endif
#pragma mark - 01.使用相机相册要导入头文件的
#import <AVFoundation/AVFoundation.h>
#import <Photos/Photos.h>
static NSString *appKey = @"339cff07d2c65012d6c7f7cc";
static NSString *channel = @"Publish channel";
static BOOL isProduction = FALSE;


@interface AppController : NSObject <UIApplicationDelegate,UIAlertViewDelegate,WXApiDelegate,LBSDKApiDelegate, AMapLocationManagerDelegate ,XGPushDelegate, DuoLiaoDelegate,WKUIDelegate,WKNavigationDelegate,
    UIImagePickerControllerDelegate,UINavigationControllerDelegate>
{
    UIWindow *window;
    NSString * access_token;
    NSString * refresh_token;
    NSString * openid;
    NSString * wxappid;
    RootViewController    *viewController;
    
    NSString * xl_access_token;
    NSString * xl_refresh_token;
    NSString * xl_openid;
   
   
}
@property (nonatomic, strong) NSString * tokenvalue;
@property (nonatomic, strong) NSString * uploadimgUrl;
@property(strong, nonatomic)UIImagePickerController *_imagePickerG;
@property(strong ,nonatomic)WKWebView  *wkWebView;
@property WebViewJavascriptBridge* bridge;
@property (nonatomic,assign) bool isAllowPortrait;
+(NSString*)getBuildVersion;
+(NSString*)getVersion;
+(NSString*)getFormattedAddress;

+(AppController*)getCurrentInstance;
@end


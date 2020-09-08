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


#import "cocos2d.h"
#include "scripting/js-bindings/manual/ScriptingCore.h"
#import "AppController.h"
#import "AppDelegate.h"
#import "RootViewController.h"
#import "platform/ios/CCEAGLView-ios.h"
#import "Foundation/Foundation.h"
#import "AudioToolbox/AudioToolbox.h"
#import <YunCeng/YunCeng.h>

#import "codeObfuscation.h"
#import <Foundation/Foundation.h>
#import "UserRecord.h"
#import "aliYunKey.h"

#import "UserRecord.h"

#import "netWorkUtil/OcReachability.h"
#import <CoreTelephony/CTTelephonyNetworkInfo.h>

#import <Bugly/Bugly.h>
#import "LBUtils.h"
#import <Contacts/Contacts.h>
#import <ContactsUI/ContactsUI.h>

#import "TDUtils.h"

#import "DLUtils.h"
#import "DLService.h"
#import "TalkingDataGA.h"
#import "AYUtils.h"


#import <Leto/Leto.h>
#define isiPhone5or5sor5c ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(640, 1136), [[UIScreen mainScreen] currentMode].size) : NO)
#define isiPhone6or6s ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(750, 1334), [[UIScreen mainScreen] currentMode].size) : NO)
#define isiPhone6plusor6splus ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(1242, 2208), [[UIScreen mainScreen] currentMode].size) : NO)
#define isiPhoneXMax ([UIScreen instancesRespondToSelector:@selector(currentMode)] ? CGSizeEqualToSize(CGSizeMake(1125, 2436), [[UIScreen mainScreen] currentMode].size) : NO)
//about xianLiao
NSString * XL_AppID = @"MYrpjOFNCBRmu1to";
NSString * XL_AppSecret = @"lUc7GK6AKyQPwPTn";
NSString * xl_access_token;
NSString * xl_refresh_token;
NSString * xl_openid;
BOOL isAPPNewStart = true;
std::string inviateMsgStr = "";

//@interface AppController ()<XGPushDelegate>
//
//@end


@implementation AppController


// 获取当前设备可用内存及所占内存的头文件
#import <sys/sysctl.h>
#import <mach/mach.h>

#pragma mark -
#pragma mark Application lifecycle

// cocos2d application instance
static AppDelegate s_sharedApplication;
static UserRecord*myRecord=NULL;
BOOL isRecord = false;


//微----信
static NSString* g_wxAppId = @"wx288d0045b3d71d93";
static NSString * g_wxSecret = @"a2a00bd9769dfb97fa5487a8cfe1481a";

//高德sdk
static NSString* g_GaoDeAppId = @"ba8205a1aef3e258cf277fa5f9702387";
//open URL
static NSString* g_handleOpenURL = @"xynmmj";
//Bugly
static NSString* g_BuglyAppId = @"3a2aa5c4b2";

static NSString* dl_handleOpenURL = @"dlxynmmj";

//信鸽id与key
static uint32_t g_XGappId = 2200340210 ;
static NSString* g_XGappKey = @"I6ZY43V3M1HE";
static AppController* appInstance = nil;
NSNotificationCenter *center = [NSNotificationCenter defaultCenter];


AMapLocationManager * locationManager;

//横竖屏的标志 true 竖, false 横
static BOOL g_bIsPortrait = false;

-(id)init{
    if(self = [super init]){
        appInstance = self;
    }

    
    // 键盘将出现事件监听
    [center addObserver:self selector:@selector(handleKeyboardWillShow:)
                   name:UIKeyboardWillShowNotification
                 object:nil];
    
    // 键盘将隐藏事件监听
    [center addObserver:self selector:@selector(handleKeyboardWillHide:)
                   name:UIKeyboardWillHideNotification
                 object:nil];
    return self;
}

+(void)imageViewIsSelector:(NSString* )token uploadimgUrl:(NSString*)uploadimgUrlP//打开相册选择图片
{
    appInstance.tokenvalue = token;
    appInstance.uploadimgUrl = uploadimgUrlP;
    [appInstance persentImagePicker];
}

+ (UIImage*)compressImage:(UIImage*)image scaledToMaxSideLength:(int)length {
    CGSize size;
    if (image.size.width >= image.size.height) {
        size.width = length;
        size.height = (int)(length / (double)image.size.width * image.size.height);
    } else {
        size.height = length;
        size.width = (int)(length / (double)image.size.height * image.size.width);
    }
    UIGraphicsBeginImageContext(size);
    [image drawInRect:CGRectMake(0, 0, size.width, size.height)];
    UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return newImage;
}


///调用本地相册
- (void)persentImagePicker{
    if (!self._imagePickerG) {
        ///初始化相机
        self._imagePickerG = [[UIImagePickerController alloc]init];
        ///代理
        self._imagePickerG.delegate = self;
    }
    ///相册
    self._imagePickerG.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    self._imagePickerG.allowsEditing = YES;
    [viewController presentViewController:self._imagePickerG animated:YES completion:nil];
}

-(UIImage *)convertImage:(UIImage *)origImage scope:(CGFloat)scope
{
    UIImage *image = nil;
    CGSize size = origImage.size;
    if (size.width < scope && size.height < scope) {
        // do nothing
        image = origImage;
    } else {
        CGFloat length = size.width;
        if (size.width < size.height) {
            length = size.height;
        }
        CGFloat f = scope/length;
        NSInteger nw = size.width * f;
        NSInteger nh = size.height * f;
        if (nw > scope) {
            nw = scope;
        }
        if (nh > scope) {
            nh = scope;
        }

        CGSize newSize = CGSizeMake(nw, nh);
//        CGSize newSize = CGSizeMake(size.width*f, size.height*f);
        
        //
        UIGraphicsBeginImageContext(newSize);
        //UIGraphicsBeginImageContextWithOptions(newSize, NO, 0.0f);
        // Tell the old image to draw in this new context, with the desired
        // new size
        [origImage drawInRect:CGRectMake(0, 0, newSize.width, newSize.height)];
        // Get the new image from the context
        image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
    }
    return image;
}

//压缩图片
- (NSData*) compressImage:(UIImage*)originImage PixelCompress:(BOOL)pc MaxPixel:(CGFloat)maxPixel JPEGCompress:(BOOL)jc MaxSize_KB: (CGFloat)maxKB
{
    /*
     压缩策略： 支持最多921600个像素点
        像素压缩：（调整像素点个数）
            当图片长宽比小于3:1 or 1:3时，图片长和宽最多为maxPixel像素；
            当图片长宽比在3:1 和 1:3之间时，会保证图片像素压缩到921600像素以内；
        JPEG压缩：（调整每个像素点的存储体积）
            默认压缩比0.99;
            如果压缩后的数据仍大于IMAGE_MAX_BYTES，那么将调整压缩比将图片压缩至IMAGE_MAX_BYTES以下。
     策略调整：
        不进行像素压缩，或者增大maxPixel，像素损失越小。
        使用无损压缩，或者增大IMAGE_MAX_BYTES.
     注意：
        jepg压缩比为0.99时，图像体积就能压缩到原来的0.40倍了。
     */
    UIImage * scopedImage = nil;
    NSData * data = nil;
    //CGFloat maxbytes = maxKB * 1024;

    if (originImage == nil) {
        return nil;
    }
    
    if ( pc == YES ) {    //像素压缩
        // 像素数最多为maxPixel*maxPixel个
        CGFloat photoRatio = originImage.size.height / originImage.size.width;
        if ( photoRatio < 0.3333f )
        {                           //解决宽长图的问题
            CGFloat FinalWidth = sqrt ( maxPixel*maxPixel/photoRatio );
            scopedImage = [self convertImage:originImage scope:MAX(FinalWidth, maxPixel)];
        }
        else if ( photoRatio <= 3.0f )
        {                           //解决高长图问题
            scopedImage = [self convertImage:originImage scope: maxPixel];
        }
        else
        {                           //一般图片
            CGFloat FinalHeight = sqrt ( maxPixel*maxPixel*photoRatio );
            scopedImage = [self convertImage:originImage scope:MAX(FinalHeight, maxPixel)];
        }
    }
    else {          //不进行像素压缩
        scopedImage = originImage;
    }
    
    [scopedImage retain];
    
    
//    NSData *imageData = UIImageJPEGRepresentation(image, compression);
//    while ([imageData length] > kb && compression > maxCompression) {
//        compression -= 0.1;
//        imageData = UIImageJPEGRepresentation(image, compression);
//    }
    
//    CGFloat compression = 0.6f;
    //CGFloat maxCompression = 0.1f;
    if ( jc == YES ) {     //JPEG压缩
        data = UIImageJPEGRepresentation(scopedImage, 0.6);
//        while ([data length] > maxKB && compression > maxCompression) {
//            compression -= 0.01;
//            data = UIImageJPEGRepresentation(scopedImage, compression);
//        }
        NSLog(@"data compress with ratio (0.6) : %d KB", data.length/1024);
    }
    else {
        data = UIImageJPEGRepresentation(scopedImage, 1.0);
        NSLog(@"data compress : %d KB", data.length/1024);
    }
    
    [scopedImage release];
    
    return data;
}
///选择图片完成（从相册或者拍照完成）
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary<NSString *,id> *)info{
    
 
    //获取修剪后的图片
    NSString *imageUp = [info objectForKey:UIImagePickerControllerImageURL];
    NSString *imageUpURL = [info objectForKey:@"PUPhotoPickerOriginalImagePath"];
    UIImage *imageR = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
     NSData *dataOld = [NSData dataWithContentsOfFile:imageUp];
    NSData *dataE = [appInstance compressImage:imageR PixelCompress:YES MaxPixel:921600 JPEGCompress:YES MaxSize_KB:1000];
    
    NSInteger  lenOld = dataOld.length/1024;
    NSInteger  len = dataE.length/1024;

    NSLog(@"原来图片的大小是%ldKB",lenOld);
    NSLog(@"压缩原来图片的大小是%ldKB",len);
                 
    if(len<1024){
        //--------上传逻辑开始
        NSString *filenameexestr = @"";
        NSString *filename = @"";
        NSString *extension = @"";
        // 从路径中获得完整的文件名（带后缀）
        filenameexestr = [imageUpURL lastPathComponent];
        // 获得文件名（不带后缀）
        filename = [imageUpURL stringByDeletingPathExtension];
          
        // 获得文件的后缀名（不带'.'）
        extension = [imageUpURL pathExtension];
        //设置mimeType
        NSString *mimeType = @"image/jpeg";
        NSString *TWITTERFON_FORM_BOUNDARY = @"AaB03x";
        //根据url初始化request
        NSString* URL = appInstance.uploadimgUrl;
        
         NSLog(@"上传URL: %@", URL);
    //    NSString* URL = @"http://10.1.6.94:8080/weixin/uploadimg";

        NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:URL]

                                                               cachePolicy:NSURLRequestReloadIgnoringLocalCacheData

                                                           timeoutInterval:10];
        
        [request setValue:appInstance.tokenvalue forHTTPHeaderField:@"authorization"];
       
        
           NSLog(@"%@", request.allHTTPHeaderFields);    //打印出header验证
        //分界线 --AaB03x
        NSString *MPboundary=[[NSString alloc]initWithFormat:@"--%@",TWITTERFON_FORM_BOUNDARY];

        //结束符 AaB03x--

        NSString *endMPboundary=[[NSString alloc]initWithFormat:@"%@--",MPboundary];
        //要上传的文件

        NSData *data = [NSData dataWithContentsOfFile:imageUp];
        //NSInteger  len1 = data.length/1024/1024;
        //NSLog(@"原来图片的大小是%ldMB",len1);
        //http body的字符串

        NSMutableString *body=[[NSMutableString alloc]init];

        ////添加分界线，换行

        [body appendFormat:@"%@\r\n",MPboundary];

        //声明文件字段，文件名

        [body appendFormat:@"Content-Disposition: form-data; name=\"cardimg\"; filename=\"%@\"\r\n",filenameexestr];

        //声明上传文件的格式
        
        [body appendFormat:@"Content-Type: %@\r\n\r\n",mimeType];
        //声明结束符：--AaB03x--

        NSString *end=[[NSString alloc]initWithFormat:@"\r\n%@",endMPboundary];

        //声明myRequestData，用来放入http body

        NSMutableData *myRequestData=[NSMutableData data];

        //将body字符串转化为UTF8格式的二进制

        [myRequestData appendData:[body dataUsingEncoding:NSUTF8StringEncoding]];

        //将file的data加入

        [myRequestData appendData:dataE];

        //加入结束符--AaB03x--

        [myRequestData appendData:[end dataUsingEncoding:NSUTF8StringEncoding]];
        //设置HTTPHeader中Content-Type的值

        NSString *content=[[NSString alloc]initWithFormat:@"multipart/form-data; boundary=%@",TWITTERFON_FORM_BOUNDARY];

        //设置HTTPHeader
        
        
        [request setValue:content forHTTPHeaderField:@"Content-Type"];

        [request setValue:[NSString stringWithFormat:@"%d", [myRequestData length]] forHTTPHeaderField:@"Content-Length"];

        //设置http body

        [request setHTTPBody:myRequestData];

        //http method

        [request setHTTPMethod:@"POST"];
        NSData *returnData = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];

        NSString *returnString = [[NSString alloc] initWithData:returnData encoding:NSUTF8StringEncoding];
        
        NSLog(@"上传状态返回值returnString: %@", returnString);
            std::string event = "GET_PHOTO_UPLOADPIC";
            std::string funName = "cc.eventManager.dispatchCustomEvent";
            std::string result_c_str= [returnString cStringUsingEncoding: NSUTF8StringEncoding];
        //{"errno":0,"payimg":"http://cardimg888.oss-cn-hangzhou.aliyuncs.com/xynmmj_686914_1577949909032.JPG"}
        //errno 值为0说明成功了
        
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        [picker dismissViewControllerAnimated:YES completion:nil];
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    }
    else
    {
        //图片超过1M 的处理
        NSString *result = @"{\"errno\":-1,\"payimg\":\"-1\"}";
        
        std::string result_c_str= [result cStringUsingEncoding: NSUTF8StringEncoding];
        std::string event ="PIC_SIZE_WARNING";
        std::string funName ="cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
         [picker dismissViewControllerAnimated:YES completion:nil];
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    }
    //---------------------------------上传逻辑结束
   
}
//取消选择图片（拍照）
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker{
    [picker dismissViewControllerAnimated:YES completion:nil];
}
//相册调用end
// 键盘出现

- (void)handleKeyboardWillShow:(NSNotification *)paramNotification {
    NSLog(@"handleKeyboardWillShow");
    // 获取键盘的高度UIKeyboardFrameEndUserInfoKey
    CGRect frame = [[[paramNotification userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    CGFloat height = frame.size.height;
    NSString *myLabel11 = [NSString stringWithFormat:@"%g",height];
    std::string result_c_str= [myLabel11 UTF8String];
    
    NSLog(@"handleKeyboardWillShow_height:%f",height);
    std::string event = "handleKeyboardWillShow";
    std::string funName = "cc.eventManager.dispatchCustomEvent";
    
    std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
    NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
    //NSLog(@"printStr:%@",printStr);
    
    ScriptingCore::getInstance()->evalString(rStr.c_str());
}

// 键盘消失

- (void)handleKeyboardWillHide:(NSNotification *)paramNotification {
    NSLog(@"handleKeyboardWillHide");
    //    CGRect frame = [[[paramNotification userInfo] objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    //    CGFloat height = frame.size.height;
    // NSLog(@"hhandleKeyboardWillHide_height:%f",height);
    std::string event = "handleKeyboardWillHide";
    std::string funName = "cc.eventManager.dispatchCustomEvent";
    std::string rStr = funName + "(\"" + event + "\")";
    
    NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
    NSLog(@"printStr:%@",printStr);
    
    ScriptingCore::getInstance()->evalString(rStr.c_str());
}
//获取 bundle version版本号
-(NSString*) getLocalAppVersion
{
    return [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleShortVersionString"];
}

//获取BundleID
-(NSString*) getBundleID
{
    return [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleIdentifier"];
}

//获取app的名字
-(NSString*) getAppName
{
    NSString *appName = [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleDisplayName"];
    NSMutableString *mutableAppName = [NSMutableString stringWithString:appName];
    return mutableAppName;
}



- (BOOL)application:(UIApplication *)application didFinishLaunchingWithOptions:(NSDictionary *)launchOptions{
    [DTOpenAPI registerApp:@"dingoafzpbllkgvdcmuen3"];
    BuglyConfig *bugConfig = [[BuglyConfig alloc] init];
    bugConfig.version = [self getLocalAppVersion];
    bugConfig.debugMode = false;
    [Bugly startWithAppId:g_BuglyAppId developmentDevice:false config:bugConfig];
    currentInstance = self;  //游戏中心添加
    std::string uidPath=cocos2d::FileUtils::getInstance()->getWritablePath()+"xynmmj_uid";

    if( cocos2d::FileUtils::getInstance()->isFileExist(uidPath)){
        auto  str1 = cocos2d::FileUtils::getInstance()->getStringFromFile(uidPath);
        [Bugly setUserIdentifier:[NSString stringWithFormat:@"%s" , str1.c_str()]];
    }
    
    
    [WXApi registerApp:g_wxAppId];
    
    cocos2d::Application *app = cocos2d::Application::getInstance();
    app->initGLContextAttrs();
    cocos2d::GLViewImpl::convertAttrs();

    
 /****************************bpush begin******************************************************/
    // iOS10 下需要使用新的 API
    if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 10.0) {
#ifdef NSFoundationVersionNumber_iOS_9_x_Max
        UNUserNotificationCenter* center = [UNUserNotificationCenter currentNotificationCenter];
        
        [center requestAuthorizationWithOptions:(UNAuthorizationOptionAlert + UNAuthorizationOptionSound + UNAuthorizationOptionBadge)
                              completionHandler:^(BOOL granted, NSError * _Nullable error) {
                                  // Enable or disable features based on authorization.
                                  if (granted) {
                                      [application registerForRemoteNotifications];
                                  }
                              }];
#endif
    }
    else if ([[[UIDevice currentDevice] systemVersion] floatValue] >= 8.0) {
        UIUserNotificationType myTypes = UIUserNotificationTypeBadge | UIUserNotificationTypeSound | UIUserNotificationTypeAlert;
        
        UIUserNotificationSettings *settings = [UIUserNotificationSettings settingsForTypes:myTypes categories:nil];
        [[UIApplication sharedApplication] registerUserNotificationSettings:settings];
    }else {
        UIRemoteNotificationType myTypes = UIRemoteNotificationTypeBadge|UIRemoteNotificationTypeAlert|UIRemoteNotificationTypeSound;
        [[UIApplication sharedApplication] registerForRemoteNotificationTypes:myTypes];
    }
    

    /****************************bpush end******************************************************/
    
    
    // Override point for customization after application launch.

    // Add the view controller's view to the window and display.
    window = [[UIWindow alloc] initWithFrame: [[UIScreen mainScreen] bounds]];
    
    
    
    CGSize size = [[UIScreen mainScreen] currentMode].size;
    CGFloat width = size.width;
    CGFloat height = size.height;
    NSLog(@"frame.size.height=width=:%f",width);
    NSLog(@"frame.size.height=height=:%f",height);
    
    if (isiPhone5or5sor5c) {
        NSLog(@"这是 iPhone5 或 5s 或 5c") ;
    } else if (isiPhone6or6s) {
        NSLog(@"这是 iPhone6 或 6s");
    } else if (isiPhone6plusor6splus) {
        NSLog(@"这是 iPhone6plus 或6splus");
    }
    else
    {
         NSLog(@"这bu是 iPhone6plus 或6splus");
    }
        
    
    CCEAGLView *eaglView = [CCEAGLView viewWithFrame: [window bounds]
                                     pixelFormat: (NSString*)cocos2d::GLViewImpl::_pixelFormat
                                     depthFormat: cocos2d::GLViewImpl::_depthFormat
                              preserveBackbuffer: NO
                                      sharegroup: nil
                                   multiSampling: NO
                                 numberOfSamples: 0 ];

    [eaglView setMultipleTouchEnabled:NO];
    
    // Use RootViewController manage CCEAGLView
    viewController = [[RootViewController alloc] initWithNibName:nil bundle:nil];
    viewController.wantsFullScreenLayout = YES;
    viewController.view = eaglView;

    // Set RootViewController to window
    if ( [[UIDevice currentDevice].systemVersion floatValue] < 6.0)
    {
        // warning: addSubView doesn't work on iOS6
        [window addSubview: viewController.view];
    }
    else
    {
        // use this method on ios6
        [window setRootViewController:viewController];
    }
    
    //调用相册功能start
    //调用相册功能end
    
    //添加配置
    std::string iamhenanmj("false");
    if(iamhenanmj.compare("true") == 0){ //河南麻将特有配置
        //add config file for xianLiao SDK.
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/xianliao.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/location.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/iosiap.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/jpush.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/wxshare.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/zogevoice.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/liaobei.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/copyRoomID.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/talkingData.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/btn_cnChat.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/newVersion20181025.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/countApp.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/baiduMap.txt");
        cocos2d::FileUtils::getInstance()->writeStringToFile("control",cocos2d::FileUtils::getInstance()->getWritablePath()+"/Cocos317Engine.txt");
    }


    [window makeKeyAndVisible];

    [[UIApplication sharedApplication] setStatusBarHidden: YES];
    [UIApplication sharedApplication].idleTimerDisabled=YES;//不自动锁屏
    // 避免多个按钮同时点击
    [[UIButton appearance] setExclusiveTouch:YES];
    

    // IMPORTANT: Setting the GLView should be done after creating the RootViewController
    cocos2d::GLView *glview = cocos2d::GLViewImpl::createWithEAGLView(eaglView);
    cocos2d::Director::getInstance()->setOpenGLView(glview);
    
    [self addNotBackUpiCloud];
    
    //如果是 iphone 4s 一下的机型 使用低配加载资源
    float  memory = [self availableMemory];
    if( memory < 412 )
    {
        cocos2d::Texture2D::setDefaultAlphaPixelFormat( cocos2d::Texture2D::PixelFormat::RGBA4444 );
    }
    
/**********************************************************************************/
    int ret = [YunCeng initWithAppKey:g_dsfyunKey];
    if( 0!= ret ){
        printf("init failed. \n");
        return YES;
    }
/**********************************************************************************/
    //GaoDe SDK
    NSString* canUseMap = [ [NSUserDefaults standardUserDefaults] objectForKey:@"canUseMap"];
    if ( canUseMap ) {
#if TARGET_IPHONE_SIMULATOR
        Byte dt[32] = {0xc6, 0x1e, 0x5a, 0x13, 0x2d, 0x04, 0x83, 0x82, 0x12, 0x4c, 0x26, 0xcd, 0x0c, 0x16, 0xf6, 0x7c, 0x74, 0x78, 0xb3, 0x5f, 0x6b, 0x37, 0x0a, 0x42, 0x4f, 0xe7, 0x97, 0xdc, 0x9f, 0x3a, 0x54, 0x10};
        [self application:application didRegisterForRemoteNotificationsWithDeviceToken:[NSData dataWithBytes:dt length:32]];
#endif
        
        
        [self configLocationManager];
        [self startSerialLocation];
    }

    /****************************闲聊******************************************************/
    if ( true ) {
        //注册闲聊
        [XianliaoApiManager registerApp:XL_AppID];
        //    [XianliaoApiManager showLog:true];
        
        
        //注册从闲聊跳转过来的游戏调用，注册了以后从Xianliao调起你的游戏APP时会以下的block，如果重复执行这个方法，会执行最后注册的block
        [XianliaoApiManager getAppFromXianliao:^(NSString *roomToken, NSString *roomId, NSNumber *openId) {
            NSString *gameString0 = [NSString stringWithFormat:@"roomToken:%@,roomId:%@,openId:%@", roomToken, roomId, openId];
            NSLog(@"%@", gameString0);
            NSString *gameString = [NSString stringWithFormat:@"{roomToken:%@,roomId:%@}", roomToken, roomId];
            NSLog(@"%@", gameString);
            if(![roomToken  isEqual: @""] && ![roomId  isEqual: @""]){
                if(gameString){
                    dispatch_async(dispatch_get_main_queue(), ^{
                        std::string result_c_str= [gameString cStringUsingEncoding: NSUTF8StringEncoding];
                        std::string event ="XL_InviteGameInfo";
                        std::string funName ="cc.eventManager.dispatchCustomEvent";
                        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
                        if(!isAPPNewStart){
                            
                            
                            NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
                            NSLog(@"printStr:%@",printStr);
                            
                            ScriptingCore::getInstance()->evalString(rStr.c_str());
                            NSLog(@"已发送闲聊邀请信息(已启动应用)。。。");
                        }
                        else{
                            inviateMsgStr = rStr;
                            NSLog(@"已保存闲聊邀请信息。。。");
                        }
                    });
                }
            }
            else{
                NSLog(@"这里是闲聊内约局(roomToken和roomId都是空)...");
            }
        }];
    }
        /************************闲聊end********************************************************/
    // 第一步：注册 ClientID，后面就可以 分享与授权。
    [LBSDKApi registerClientId:[LBUtils clientId]];
    
    [TDUtils initTD];
    [DLUtils initDL];

    /*******************************游戏中心*****************************************************/
    //注册=>监听游戏运行状态的回调
    MgcMiniGameRunStatusBlock runStatusBlock = ^(NSString *name,NSString *gameId,MgcMiniGameRunStatusType runType){
        NSString *runStatusStr = runType == MgcMiniGameRunStatusType_Start ? @"启动":@"退出";
        NSLog(@">>>>>>name = %@,gameId = %@,runStatus = %@",name,gameId,runStatusStr);
    };
    
    MgcGameCenterExitBlock exitBlock = ^(void)
    {
        NSLog(@"游戏中心退出");
        std::string event ="removeWKWebView";
        std::string funName ="cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\");";
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    };

    NSDictionary *dic = @{
                          mgc_app_id_key:@"364937",//364795 （测试的key）   364851是四方游戏中心-IOS  这里的值要和游戏中心提供的匹配
                          //如果要监控游戏的运行状态，需要进行下面的设置
                          mgc_register_run_status_key:runStatusBlock,
                          //设置日志等级
                          mgc_log_level_key:@(MGCSdkLogLevel_Debug),
                          //游戏中心退出
                          mgc_register_game_center_exit_key:exitBlock,
                          };
    [MGCMiniGameApi mgc_initSdkWithInfo:dic];
    /*******************************游戏中心end**************************************************/

    app->run();
    
    [self initXGPush:launchOptions];

//    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(imageViewIsSelector)];
//
//    [viewController.view addGestureRecognizer:tap];
//[self.wkWebView addGestureRecognizer:tap];

    return YES;
}

- (void)application:(UIApplication *)application didRegisterUserNotificationSettings:(UIUserNotificationSettings *)notificationSettings
{        
    [application registerForRemoteNotifications];
        
}


- (void)application:(UIApplication *)application
didRegisterForRemoteNotificationsWithDeviceToken:(NSData *)deviceToken {
}
    

- (void)application:(UIApplication *)application didReceiveLocalNotification:(UILocalNotification *)notification
{
    NSLog(@"接收本地通知啦！！！");
}


- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo fetchCompletionHandler:(void (^)(UIBackgroundFetchResult))completionHandler {
    // App 收到推送的通知

    // Required, iOS 7 Support
    completionHandler(UIBackgroundFetchResultNewData);
    
    NSLog(@"********** ios7.0之前 **********");
    // 应用在前台 或者后台开启状态下，不跳转页面，让用户选择。
    if (application.applicationState == UIApplicationStateActive || application.applicationState == UIApplicationStateBackground) {
        NSLog(@"acitve or background");
        UIAlertView *alertView =[[UIAlertView alloc]initWithTitle:@"收到一条消息" message:userInfo[@"aps"][@"alert"] delegate:self cancelButtonTitle:@"取消" otherButtonTitles:@"确定", nil];
        [alertView show];
    }
}

- (void)application:(UIApplication *)application didReceiveRemoteNotification:(NSDictionary *)userInfo {
    // App 收到推送的通知
}


    
// 当 DeviceToken 获取失败时，系统会回调此方法
- (void)application:(UIApplication *)application didFailToRegisterForRemoteNotificationsWithError:(NSError *)error
{
        NSLog(@"DeviceToken 获取失败，原因：%@",error);
}

    
    
- (void)networkDidReceiveMessage:(NSNotification *)notification {
    NSDictionary * userInfo = [notification userInfo];
    NSString *content = [userInfo valueForKey:@"content"];
    if(content){
        
    }
    NSDictionary *extras = [userInfo valueForKey:@"extras"];
    NSString *customizeField1 = [extras valueForKey:@"customizeField1"]; //服务端传递的Extras附加字段，key是自己定义的
    if(customizeField1){
        
    }
    
}




- (BOOL)application:(UIApplication *)application handleOpenURL:(NSURL *)url
{
    //用户从闲聊调用你的APP时，需要从这个方法获得闲聊传递的内容
    if ([XianliaoApiManager handleOpenURL:url]) {
        return YES;
    }
    //处理微----信通过URL启动App时传递的数据
    else if([WXApi handleOpenURL:url delegate:self]){
        return YES;
    }
    //处理聊呗返回的结果
    else if([LBSDKApi handleOpenURL:url delegate:self]){
        return YES;
    }
    else if([DLService handleOpenUrl:url.absoluteString delegate:self]){
        return YES;
    }
    else if([AYUtils handleOpenUrl:url]){
        return YES;
    }
    else
        return NO;
}

-(BOOL)application:(UIApplication *)application openURL:(NSURL *)url sourceApplication:(NSString *)sourceApplication annotation:(id)annotation
{
    if ([[url scheme] isEqualToString:g_handleOpenURL]) //
    {
        NSString *host = [url host];
        NSArray *dataArr = [host componentsSeparatedByString:@"&"];
        if([dataArr objectAtIndex:0]){
            NSString *roomData = [dataArr objectAtIndex:0];
            NSLog(@"--------------application------------------roomData: %@", roomData);
            
            NSArray *roomDataArr = [roomData componentsSeparatedByString:@"="];
            if([roomDataArr objectAtIndex:1]){
                
                NSString *roomData2 = [roomDataArr objectAtIndex:1];
                
                [[NSUserDefaults standardUserDefaults] setObject:roomData2 forKey:@"roomData"];
                [[NSUserDefaults standardUserDefaults] synchronize];
            }
            
        }
        return true;
        
    }else if ([XianliaoApiManager handleOpenURL:url]) {	//用户从闲聊调用你的APP时，需要从这个方法获得闲聊传递的内容
        return YES;
    }
    //处理聊呗返回的结果
    else if([LBSDKApi handleOpenURL:url delegate:self]){
        return YES;
    }
    else if([DLService handleOpenUrl:url.absoluteString delegate:self]){
        return YES;
    }
    else if([AYUtils handleOpenUrl:url]){
        return YES;
    }
    else {
        return [WXApi handleOpenURL:url delegate:self];
    }
}


- (void)getLoginInfo:(DLLoginInfo *)loginInfo {
    //loginInfo 为获取的信息
    NSLog(@"多聊登录结果...");
    
    NSString *nickName = [loginInfo.nickName stringByRemovingPercentEncoding];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        NSString*   result = [NSString stringWithFormat:@"{\"code\":\"%@\",\"openId\":\"%@\",\"gender\":\"%@\",\"nickName\":\"%@\",\"originalAvatar\":\"%@\"}",loginInfo.code,loginInfo.openId,loginInfo.gender,nickName,loginInfo.originalAvatar];
        
        std::string result_c_str= [result cStringUsingEncoding: NSUTF8StringEncoding];
        
        std::string event = "DL_USER_LOGIN";
        std::string funName = "cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        
        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
        NSLog(@"printStr:%@",printStr);
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    });
    NSLog(@"多聊登录结束...");
}

-(void)onReq:(BaseReq *)req
{
    
}




#pragma mark -
#pragma mark 重新注册微----信appID
+(void)WXRegisterAppId:(NSString* )a_appId{
    if([a_appId length] < 5){
        return ;
    }
    [WXApi registerApp:a_appId];
}



+(NSString *)isWxInstall{
    BOOL haveWx = [WXApi isWXAppInstalled];
    if(haveWx){
        return @"true";
    }
    else{
        return @"false";
    }
}

#pragma mark -
#pragma mark 微----信计费代码--- 与微----信登陆&计费&分享&回调

-(void)onResp:(BaseResp *)resp
{
    NSLog(@"weixinonResp--->");
    NSString * appid = g_wxAppId;
    NSString * secret = g_wxSecret;
    
    NSString *code;
    NSString * grant_type=@"authorization_code";
    if(grant_type){
        
    }
    SendAuthResp *aresp = (SendAuthResp *)resp;
    if([resp isKindOfClass:[SendMessageToWXResp class]]){ //微----信分享
        SendMessageToWXResp* response = (SendMessageToWXResp*)resp;
        NSString *result = NULL;
        switch( response.errCode){
            case WXSuccess:
                NSLog(@"微----信_分享成功"); //{"errCode":"0","type":"0"} ---朋友圈返回 0
                result = [NSString stringWithFormat:@"{\"errCode\":\"%d\",\"type\":\"%d\"}",response.errCode,response.type];
                break;
            case WXErrCodeCommon:
                NSLog(@"微----信_分享错误");
                result = [NSString stringWithFormat:@"{\"errCode\":\"%d\"}",response.errCode];
                break;
            case WXErrCodeUserCancel:
                result = [NSString stringWithFormat:@"{\"errCode\":\"%d\"}",response.errCode];
                NSLog(@"微----信_分享 用户取消 无需处理");
                break;
            default:
                result = [NSString stringWithFormat:@"{\"errCode\":\"%d\"}",-1];
                NSLog(@"支---付分享，retcode=%d",resp.errCode);
                break;
        }
        
        std::string result_c_str= [result cStringUsingEncoding: NSUTF8StringEncoding];
        std::string event ="WX_SHARE_RESULT";
        
        std::string funName ="cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
    
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    }
    else if (aresp.errCode == 0) { //微----信登陆返回值
        code = aresp.code;
        NSLog(@"weixincode=%@", code);
        NSString *url =[NSString stringWithFormat:@"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%@&secret=%@&code=%@&grant_type=authorization_code",appid,secret,code];
        dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,0),^{
        
        NSURL *zoneUrl=[NSURL URLWithString:url];
        
        NSString *zoneStr = [NSString stringWithContentsOfURL:zoneUrl encoding:NSUTF8StringEncoding error:nil];
        NSData *data = [zoneStr dataUsingEncoding:NSUTF8StringEncoding];
            dispatch_async(dispatch_get_main_queue(), ^{
                if(data){
                    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
                    self->access_token = [dic objectForKey:@"access_token"];
                    NSLog(@"access_token=%@" , self->access_token);
                    self->refresh_token = [dic objectForKey:@"refresh_token"];
                    NSLog(@"access_token=%@" ,self->refresh_token);
                  
                    self->openid = [dic objectForKey:@"openid"];
                    NSLog(@"openid= %@" ,self->openid);
                    [self getUserInfo];
                    [self sendToken];
            }
            });
        });
    }else
    {
        NSLog(@"false");
    }
}

-(BOOL) application:(UIApplication *)application continueUserActivity:(NSUserActivity *)userActivity restorationHandler:(void (^)(NSArray * _Nullable))restorationHandler{
    
    NSURL * url = userActivity.webpageURL;
    NSLog(@"url:%@",url );
    
    NSString* querry = [url query ];
    NSLog(@"querry:%@",querry );
    
    NSArray *dataArray = [querry componentsSeparatedByString:@"&"];
    if( [dataArray count] >0 ){
        NSString* roomData = [dataArray objectAtIndex:0];
        NSLog(@"-----application----- roomData=%@", roomData);
        
        NSArray* roomDataArray = [roomData componentsSeparatedByString:@"="];
        if( [roomDataArray count] >1 ){
            
            NSString* roomData2 = [roomDataArray objectAtIndex:1];
            
            [[NSUserDefaults standardUserDefaults] setObject:roomData2 forKey:@"rootData"];
            [[NSUserDefaults standardUserDefaults] synchronize];
        }
    }
}

-(void)sendToken
{
    NSString*   result = [NSString stringWithFormat:@"{\"access_token\":\"%@\",\"refresh_token\":\"%@\"}",self->access_token,self->refresh_token];
    
    std::string result_c_str= [result cStringUsingEncoding: NSUTF8StringEncoding];
    std::string event ="WX_USER_LOGIN_TOKEN";
    std::string funName ="cc.eventManager.dispatchCustomEvent";
    std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
    
    ScriptingCore::getInstance()->evalString(rStr.c_str());
}

#pragma mark -
#pragma mark 微----信登陆 获取用户信息
-(void)getUserInfo
{
    //https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code
    NSString*url=[NSString stringWithFormat:@"https://api.weixin.qq.com/sns/userinfo?access_token=%@&openid=%@",self->access_token,self->openid];
    NSLog(@"getUserInfo->url:%@", url);
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,0),^{
    NSURL *zoneUrl = [NSURL URLWithString:url];
    NSString *zoneStr = [NSString stringWithContentsOfURL:zoneUrl encoding:NSUTF8StringEncoding error:nil];
    NSData *data = [zoneStr dataUsingEncoding:NSUTF8StringEncoding];
    dispatch_async(dispatch_get_main_queue(), ^{
        if(data){
            NSString *result = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
			NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
            NSString*   nickname = [dic objectForKey:@"nickname"];
            cocos2d::FileUtils::getInstance()->writeStringToFile(nickname.UTF8String,
            cocos2d::FileUtils::getInstance()->getWritablePath()+"/nickname.txt");
			
            std::string result_c_str= [result cStringUsingEncoding: NSUTF8StringEncoding];
            std::string event ="WX_USER_LOGIN";
            std::string funName ="cc.eventManager.dispatchCustomEvent";
            std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
             

            ScriptingCore::getInstance()->evalString(rStr.c_str());
            
        }
    });
    });
}

+(void)sendAuthRequest
{
    [WXApi registerApp:g_wxAppId];
    SendAuthReq* req = [[[SendAuthReq alloc] init] autorelease];
    req.scope = @"snsapi_userinfo";
    req.state = @"123";
    [WXApi sendReq:req];
}

+(void)NativeBattery
{
    [[UIDevice currentDevice] setBatteryMonitoringEnabled:YES];  
    float battery =  [[UIDevice currentDevice] batteryLevel]; 
    int   batteryNum =  floor(battery*100);
    
    std::stringstream ss;
    std::string result_c_str;
    ss<<batteryNum;
    ss>>result_c_str;
    std::string event ="nativePower";
    std::string funName ="cc.eventManager.dispatchCustomEvent";

    std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
    ScriptingCore::getInstance()->evalString(rStr.c_str());
}

+(void)NativeVibrato{
    //NSLog(@"NativeVibrato");
    AudioServicesPlaySystemSound(kSystemSoundID_Vibrate); //12.01不支持
//    AudioServicesPlaySystemSound(1521);//连续三次短震
}

#pragma mark -
#pragma mark 添加 定位 提审
+(void) initGaoDeMap{
    [[ NSUserDefaults standardUserDefaults] setObject:@"true" forKey:@"canUseMap"];
    [[NSUserDefaults standardUserDefaults] synchronize ];
}

//添加提审
+(void) initGapDeMap{
    [[ NSUserDefaults standardUserDefaults] setObject:@"true" forKey:@"canUseMap"];
    [[NSUserDefaults standardUserDefaults] synchronize ];
}

#pragma mark -
#pragma mark 获取房间号
+(NSString*)getRoomData
{
    NSString *roomData_str = [[NSUserDefaults standardUserDefaults] objectForKey:@"roomData"];
    if(roomData_str)
    {
        [[NSUserDefaults standardUserDefaults] removeObjectForKey:@"roomData"];
        
        NSLog(@"getRoomData roomData: %@", roomData_str);
        return roomData_str;
    }else {
        return NULL;
    }
}


#pragma mark -
#pragma mark 分享图片到微----信好友

+(void)wxShareTexture:(NSString* )path AndTimeLine:(BOOL)timeLine
{
    NSLog(@"filepath11:%@",path);
    WXMediaMessage * message =[WXMediaMessage message];
    UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
    CGSize size= [image size];
    
    CGSize scaleSize = CGSizeMake(120, 120 / size.width *size.height);
    
    UIGraphicsBeginImageContext(scaleSize);
    CGRect rect = CGRectMake(0.0, 0.0, scaleSize.width, scaleSize.height);
    [image drawInRect:rect];
    UIImage * scaleimage = UIGraphicsGetImageFromCurrentImageContext();
    [message setThumbImage:scaleimage];
    UIGraphicsEndImageContext();
    
    
    
    WXImageObject *imgobject = [WXImageObject object];
    //获取图片
    imgobject.imageData = [NSData dataWithContentsOfFile:path];
    message.mediaObject = imgobject;
    SendMessageToWXReq * req =[[[SendMessageToWXReq alloc] init] autorelease];
    req.bText = NO;
    req.message = message;
    req.scene = timeLine ? WXSceneTimeline : WXSceneSession;
    [WXApi sendReq:req];
    NSLog(@"filepath12:%@",path);
}
/**
 * showType: 0是分享给好友, 1是分享给朋友圈
 */
+(void)wxShareTexture:(NSString* )path AndType:(NSString*)showType
{
    
    NSLog(@"filepath11:%@",path);
    WXMediaMessage * message =[WXMediaMessage message];
    UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
    CGSize size= [image size];
    
    CGSize scaleSize = CGSizeMake(120, 120 / size.width *size.height);
    
    UIGraphicsBeginImageContext(scaleSize);
    CGRect rect = CGRectMake(0.0, 0.0, scaleSize.width, scaleSize.height);
    [image drawInRect:rect];
    UIImage * scaleimage = UIGraphicsGetImageFromCurrentImageContext();
    [message setThumbImage:scaleimage];
    UIGraphicsEndImageContext();
    
    /**
     缩放截屏图片为512
     */
    CGSize scaleSize1 = CGSizeMake(1024,1024 / size.width * size.height);
    UIGraphicsBeginImageContext(scaleSize1);
    CGRect rect1 = CGRectMake(0.0, 0.0, scaleSize1.width, scaleSize1.height);
    [image drawInRect:rect1];
    UIImage * scaleimage1 = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    WXImageObject *imgobject = [WXImageObject object];
    //获取图片
    
    //UIImagePNGRepresentation(<#UIImage * _Nonnull image#>)
    //imgobject.imageData = [NSData dataWithContentsOfFile:path];
    imgobject.imageData =UIImageJPEGRepresentation(scaleimage1, 100);
    message.mediaObject = imgobject;
    SendMessageToWXReq * req =[[[SendMessageToWXReq alloc] init] autorelease];
    req.bText = NO;
    req.message = message;
    req.scene = ([showType isEqualToString: @"0"] ? WXSceneSession : WXSceneTimeline);
    [WXApi sendReq:req];
    NSLog(@"filepath12:%@",path);
}



+(void)wxShareTexture:(NSString* )path
{
    [self wxShareTexture:path AndTimeLine:NO];
}

+(void)StartURLImageWxSceneSession:(NSString* )path
{
    [self wxShareTexture:path AndTimeLine:NO];
}


//分享url图片
+(void)StartURLImageWxSceneSession:(NSString* )path AndTimeLine:(BOOL)timeLine
{
    //[UIImagePNGRepresentation(image) writeToFile:pngPath atomically:YES];
    NSLog(@"StartURLImageWxSceneSession path = :%@",path);
    UIImage *image = NULL;
    
    NSData* imageData = [[NSData alloc]initWithContentsOfURL:[NSURL URLWithString:path]];
    image = [[UIImage alloc] initWithData:imageData];
    
    if(image){
        WXMediaMessage * message =[WXMediaMessage message];
        //UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
        
        CGSize size= [image size];
        
        CGSize scaleSize = CGSizeMake(120, 120 / size.width *size.height);
        
        UIGraphicsBeginImageContext(scaleSize);
        CGRect rect = CGRectMake(0.0, 0.0, scaleSize.width, scaleSize.height);
        [image drawInRect:rect];
        UIImage * scaleimage = UIGraphicsGetImageFromCurrentImageContext();
        [message setThumbImage:scaleimage];
        UIGraphicsEndImageContext();
        
        
        WXImageObject *imgobject = [WXImageObject object];
        //获取图片
        //imgobject.imageData = [NSData dataWithContentsOfFile:path];
        //imgobject.imageUrl = path;
        //imgobject.imageData = UIImagePNGRepresentation(image);
        imgobject.imageData = imageData;
        message.mediaObject = imgobject;
        SendMessageToWXReq * req =[[[SendMessageToWXReq alloc] init] autorelease];
        req.bText = NO;
        req.message = message;
        req.scene = timeLine ? WXSceneTimeline : WXSceneSession;
        [WXApi sendReq:req];
        NSLog(@"StartURLImageWxSceneSession path = :%@",path);
    }
    
    
}
#pragma mark -
#pragma mark 分享图片到朋友圈
+(void)wxShareTextureWXSceneTimeline:(NSString* )path
{
    
    NSLog(@"filepath11:%@",path);
    WXMediaMessage * message =[WXMediaMessage message];
    UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
    CGSize size= [image size];
    
    //大图
    float scale_1 = 1.0;
    CGSize scaleSize1 = CGSizeMake(size.width/scale_1,size.height/scale_1);
    UIGraphicsBeginImageContext(scaleSize1);
    CGRect rect1 = CGRectMake(0.0, 0.0, scaleSize1.width, scaleSize1.height);
    [image drawInRect:rect1];
    UIImage * scaleimage1 = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    //缩略图
    CGSize scaleSize = CGSizeMake(120, 120 / size.width *size.height);
    UIGraphicsBeginImageContext(scaleSize);
    CGRect rect = CGRectMake(0.0, 0.0, scaleSize.width, scaleSize.height);
    [image drawInRect:rect];
    UIImage * scaleimage = UIGraphicsGetImageFromCurrentImageContext();
    [message setThumbImage:scaleimage];
    UIGraphicsEndImageContext();
    
    //创建分享对象
    WXImageObject *imgobject = [WXImageObject object];
    //压缩比例
    float ration_1 = 0.5;
    imgobject.imageData =UIImageJPEGRepresentation(scaleimage1, ration_1);
    message.mediaObject = imgobject;
    SendMessageToWXReq * req =[[[SendMessageToWXReq alloc] init] autorelease];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneTimeline;
    [WXApi sendReq:req];
    NSLog(@"filepath12:%@",path);
}

#pragma mark -
#pragma mark 微----信分享----朋友圈分享
+(void)wxShareUrlTimeline:(NSString*)title AndText:(NSString*)text AndUrl:(NSString*)url
{
    NSLog(@"wxShareUrlTimeline: title=%@ text=%@ url=%@", title,text , url );
    WXMediaMessage * message = [WXMediaMessage message];
    message.title = [title stringByAppendingString:text];
    message.description = text;
    [message setThumbImage:[UIImage imageNamed:@"Icon-100.png"]];
    WXWebpageObject * webpageObject = [WXWebpageObject object];
    webpageObject.webpageUrl = url;
    message.mediaObject = webpageObject;
    SendMessageToWXReq* req = [[SendMessageToWXReq alloc]init];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneTimeline;
    [WXApi sendReq:req];
}


+(void)wxShareUrl:(NSString*)title AndText:(NSString*)text AndUrl:(NSString*)url
{
    NSLog(@"weixinshareurl: title=%@ text=%@ url=%@", title,text , url );
    WXMediaMessage * message = [WXMediaMessage message];
    message.title = title;
    message.description = text;
    [message setThumbImage:[UIImage imageNamed:@"Icon-100.png"]];
    WXWebpageObject * webpageObject = [WXWebpageObject object];
    webpageObject.webpageUrl = url;
    message.mediaObject = webpageObject;
    SendMessageToWXReq* req = [[SendMessageToWXReq alloc]init];
    req.bText = NO;
    req.message = message;
    req.scene = WXSceneSession;
    [WXApi sendReq:req];
    
    //[self wxShareUrlTimeline:title AndText:text AndUrl:url];
}
//add by gxmj
+(void)wxShareUrl:(NSString*)title AndText:(NSString*)text AndUrl:(NSString*)url AndType:(NSString*)showType
{
    NSLog(@"weixinshareurl:%@",url);
    WXMediaMessage * message = [WXMediaMessage message];
    message.title = title;
    message.description = text;
    [message setThumbImage:[UIImage imageNamed:@"Icon-100.png"]];
    WXWebpageObject * webpageObject = [WXWebpageObject object];
    webpageObject.webpageUrl = url;
    message.mediaObject = webpageObject;
    SendMessageToWXReq* req = [[SendMessageToWXReq alloc]init];
    req.bText = NO;
    req.message = message;
    if([showType isEqualToString: @"0"])
    {
        req.scene = WXSceneSession;
    }
    else
    {
        message.title = text;
        req.scene = WXSceneTimeline;
    }
    
    [WXApi sendReq:req];
}


#pragma mark -
#pragma mark 微----信复制加入房间接口
+(void)writeToClipboard:(NSString*) content{
    UIPasteboard *pasteBoard = [UIPasteboard generalPasteboard];
    if(content){
        NSLog(@"writeToClipboard --->%@" , content);
    }
    [pasteBoard setString:content];
}

+(NSString*)getFromClipboard{
    UIPasteboard *pasteBoard = [UIPasteboard generalPasteboard];
    NSString * itemp = [pasteBoard string];
    
    if(itemp){
#ifdef COCOS2D_DEBUG
//        NSLog(@"getFromClipboard --->%@" , itemp);
#endif
    }
    return itemp;
}

/*
 打开浏览器
 */
+(void)openURLInBrowser:(NSString *)url{
    if (url && url.length > 0)
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:url]];
}

//分享文字
+(void)wxShareText:(NSString* )text  WithTimeLine:(BOOL)timeLine {
	SendMessageToWXReq* req = [[SendMessageToWXReq alloc]init];
	req.text = text;
	req.bText = YES;
	if(timeLine) {
		req.scene = WXSceneTimeline;
	}
	else {
		req.scene = WXSceneSession;
	}

	[WXApi sendReq:req];
}

/**
 * 初始化 录音
 */
+(id)initRecorder
{
    if(!myRecord)
    {
        NSLog(@"create new Instance");
        myRecord=[[UserRecord alloc]init];
    }
    return myRecord;
}
+(NSString*)startRecord:(NSString*)filePath lajioc:(NSString *)fileName
{
    if (isRecord) return @"";
    UserRecord*recoder=[self initRecorder];
    NSString* path = [recoder beginRecord:filePath fileName:fileName];
    NSString *homePath = NSHomeDirectory();
    if(homePath){}
    isRecord = true;
    return  path;   
}
+(void)endRecord:(NSString*)eventName
{
    if(!isRecord) return;
    UserRecord*recoder=[self initRecorder];
    [recoder stopRecord];
    NSURL *recordFile = [recoder getAudioFile];
    NSString * fileName = [recordFile absoluteString];
    isRecord = false;
    [self runJs:eventName lajioc:fileName];
}

+(void)runJs:(NSString *)eventName lajioc:(NSString *)para
{
    std::string event = [eventName cStringUsingEncoding: NSUTF8StringEncoding];
    std::string data = [para cStringUsingEncoding: NSUTF8StringEncoding];
    std::string funName ="cc.eventManager.dispatchCustomEvent";
    
    std::string rStr = funName + "(\"" + event + "\", \"" + data + "\");";
    NSLog(@"jsCallBack Str is: %s", rStr.c_str());
    
    ScriptingCore::getInstance()->evalString(rStr.c_str());
}

+(NSString*)getBuildVersion
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    // app build版本
    NSString *app_build = [infoDictionary objectForKey:@"CFBundleVersion"];
    return app_build;
}

+(NSString*)getVersion
{
    NSDictionary *infoDictionary = [[NSBundle mainBundle] infoDictionary];
    NSString *app_Version = [infoDictionary objectForKey:@"CFBundleShortVersionString"];
    return app_Version;
}



+(void)HelloOC:(NSString*)message
{
    NSLog(@"HelloOC: %@", message);
}

+(void)downloadFile:(NSString*)filePath fileName:(NSString*)fileName url:(NSString*)urlStr eventName:(NSString*)eventName
{
    NSLog(@"\nfilePath is: %@ \n fileName is: %@ \n urlStr is: %@\n eventName is: %@", filePath, fileName, urlStr, eventName);
    
    //    filePath = @"/Users/machao/Desktop/appData/scmj/";
    //    urlStr = @"http://192.168.1.123:3000/9.jpg";
    //    fileName = [urlStr lastPathComponent];
    
    NSString* zipFilePath = [filePath stringByAppendingString:fileName];
    NSURL    *url = [NSURL URLWithString:urlStr];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    NSOperationQueue *que = [[NSOperationQueue alloc] init];
    
    [NSURLConnection sendAsynchronousRequest:request queue:que completionHandler:^(NSURLResponse *response, NSData *data, NSError *connectionError) {
        if (connectionError) {
            NSLog(@"AsynchronousRequest1 get data is OK  on thread %@!!",[NSThread currentThread]);
        }
        else{
            NSLog(@" statusCode is %ld on thread %@",(long)[(NSHTTPURLResponse*)response  statusCode],[NSThread currentThread]);
            
            if (data != nil){
                NSLog(@"下载成功");
                if ([data writeToFile:zipFilePath atomically:YES]) {
                    NSLog(@"保存成功.");
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self runJs:eventName lajioc:zipFilePath];
                    });
                }
                else{
                    [self runJs:eventName lajioc:@"writeError"];
                    NSLog(@"保存失败.");
                }
            } else {
                [self runJs:eventName lajioc:@"downloadError"];
                NSLog(@"%@", connectionError);
            }
        }
    }];
    
}



+(void)uploadFile:(NSString *)filedic url:(NSString*)strUrl eventName:(NSString*)eventName
{
    NSLog(@"uploadFile %@", filedic);
    //分界线的标识符
    NSString *TWITTERFON_FORM_BOUNDARY = @"AaB03x";
    //根据url初始化request
    //        NSString* URL = [NSString stringWithFormat:@"http://%@%@",NSLocalizedString(@"MQTT_IP", @""),NSLocalizedString(@"im_uploadfileURL", @"")];
    NSString* URL = strUrl;
    
    NSString* fileName = [filedic lastPathComponent];
    
    NSMutableURLRequest* request = [NSMutableURLRequest requestWithURL:[NSURL URLWithString:URL]
                                                           cachePolicy:NSURLRequestReloadIgnoringLocalCacheData
                                                       timeoutInterval:10];
    //分界线 --AaB03x
    NSString *MPboundary=[[NSString alloc]initWithFormat:@"--%@",TWITTERFON_FORM_BOUNDARY];
    //结束符 AaB03x--
    NSString *endMPboundary=[[NSString alloc]initWithFormat:@"%@--",MPboundary];
    //要上传的文件
    //        NSData *data = [NSData dataWithContentsOfFile:[filedic objectForKey:@"filepath"]];
    NSData *data = [NSData dataWithContentsOfFile:filedic];
    
    
    //http body的字符串
    NSMutableString *body=[[NSMutableString alloc]init];
    //参数的集合普通的key－value参数
    
    body = [self setParamsKey:@"uptype" value:@"1" body:body];
    
    ////添加分界线，换行
    [body appendFormat:@"%@\r\n",MPboundary];
    //声明文件字段，文件名
    [body appendFormat:@"Content-Disposition: form-data; name=\"upfile\"; filename=\"%@\"\r\n", fileName];
    //声明上传文件的格式
    [body appendFormat:@"Content-Type: %@\r\n\r\n",[self GetContentType:fileName]];
    
    //声明结束符：--AaB03x--
    NSString *end=[[NSString alloc]initWithFormat:@"\r\n%@",endMPboundary];
    //声明myRequestData，用来放入http body
    NSMutableData *myRequestData=[NSMutableData data];
    //将body字符串转化为UTF8格式的二进制
    [myRequestData appendData:[body dataUsingEncoding:NSUTF8StringEncoding]];
    //将file的data加入
    [myRequestData appendData:data];
    //加入结束符--AaB03x--
    [myRequestData appendData:[end dataUsingEncoding:NSUTF8StringEncoding]];
    
    //设置HTTPHeader中Content-Type的值
    NSString *content=[[NSString alloc]initWithFormat:@"multipart/form-data; boundary=%@",TWITTERFON_FORM_BOUNDARY];
    //设置HTTPHeader
    [request setValue:content forHTTPHeaderField:@"Content-Type"];
    //设置Content-Length
    [request setValue:[NSString stringWithFormat:@"%lu", (unsigned long)[myRequestData length]] forHTTPHeaderField:@"Content-Length"];
    //设置http body
    [request setHTTPBody:myRequestData];
    //http method
    [request setHTTPMethod:@"POST"];
    
    //开线程下载
    dispatch_queue_t defaultQueue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    dispatch_async(defaultQueue, ^{
        // 另开线程
        NSData *returnData = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
        NSString *returnString = [[NSString alloc] initWithData:returnData encoding:NSUTF8StringEncoding];
        NSLog(@"上传状态返回值: %@", returnString);
        dispatch_async(dispatch_get_main_queue(), ^{
            [self runJs:eventName lajioc:filedic];
        });
    });
}

+(NSMutableString*)setParamsKey:(NSString*)key value:(NSString*)value body:(NSMutableString*)body{
    NSString *TWITTERFON_FORM_BOUNDARY = @"AaB03x";
    //分界线 --AaB03x
    NSString *MPboundary=[[NSString alloc]initWithFormat:@"--%@",TWITTERFON_FORM_BOUNDARY];
    //添加分界线，换行
    [body appendFormat:@"%@\r\n",MPboundary];
    //添加字段名称，换2行
    [body appendFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n",key];
    //添加字段的值
    [body appendFormat:@"%@\r\n",value];
    return body;
}

+(NSString*)GetContentType:(NSString*)filename{
    if ([filename hasSuffix:@".avi"]) {
        return @"video/avi";
    }
    else if([filename hasSuffix:@".bmp"])
    {
        return @"application/x-bmp";
    }
    else if([filename hasSuffix:@"jpeg"])
    {
        return @"image/jpeg";
    }
    else if([filename hasSuffix:@"jpg"])
    {
        return @"image/jpeg";
    }
    else if([filename hasSuffix:@"png"])
    {
        return @"image/x-png";
    }
    else if([filename hasSuffix:@"mp3"])
    {
        return @"audio/mp3";
    }
    else if([filename hasSuffix:@"mp4"])
    {
        return @"video/mpeg4";
    }
    else if([filename hasSuffix:@"rmvb"])
    {
        return @"application/vnd.rn-realmedia-vbr";
    }
    else if([filename hasSuffix:@"txt"])
    {
        return @"text/plain";
    }
    else if([filename hasSuffix:@"xsl"])
    {
        return @"application/x-xls";
    }
    else if([filename hasSuffix:@"xslx"])
    {
        return @"application/x-xls";
    }
    else if([filename hasSuffix:@"xwd"])
    {
        return @"application/x-xwd";
    }
    else if([filename hasSuffix:@"doc"])
    {
        return @"application/msword";
    }
    else if([filename hasSuffix:@"docx"])
    {
        return @"application/msword";
    }
    else if([filename hasSuffix:@"ppt"])
    {
        return @"application/x-ppt";
    }
    else if([filename hasSuffix:@"pdf"])
    {
        return @"application/pdf";
    }
    return nil;
}


//设置禁止云同步

-(void)addNotBackUpiCloud{
    NSArray *docPaths = NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES);
    NSArray *libPaths = NSSearchPathForDirectoriesInDomains(NSLibraryDirectory, NSUserDomainMask, YES);

    NSString *docPath = [docPaths objectAtIndex:0];
    NSString *libPath = [libPaths objectAtIndex:0];
    
    [self fileList:docPath];
    [self fileList:libPath];
}


- (void)fileList:(NSString*)directory{
    
    NSError *error = nil;
    
    NSFileManager * fileManager = [NSFileManager defaultManager];
    
    NSArray *fileList = [fileManager contentsOfDirectoryAtPath:directory error:&error];
    
    for (NSString* each in fileList) {
        
        NSMutableString* path = [[NSMutableString alloc]initWithString:directory];
        
        [path appendFormat:@"/%@",each];
        NSLog(@"path %@:", each);
        
        NSURL *filePath = [NSURL fileURLWithPath:path];
        
        [self addSkipBackupAttributeToItemAtURL:filePath];
        
        [self fileList:path];
    }
}

#pragma mark -
#pragma mark 通过阿里盾获取远程IP
+(NSString *)getRemoteIpByAliDun:(NSString *)groupname
{
    NSString * ipNstr;
    int ret = -1;
    
    std::string event ="GetRemoteIpByAliDun_Back";
    std::string funName ="cc.eventManager.dispatchCustomEvent";
    
    std::string errorCode="errorCode:";
    char ip[128]= {0};
    try {
    
        const char* groupnameEx =[groupname UTF8String];
        
        ret = [YunCeng getNextIPByGroupName:groupnameEx:ip :128];
        std::string rStr = funName + "(\"" + event + "\", \"" + ip + "\");";
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    
        
    } catch (YunCengException *ex) {
        NSLog(@"------------   alidun exception");
        std::string  str = [[NSString stringWithFormat:@"%ld", (long)ex.code] cStringUsingEncoding:NSUTF8StringEncoding];
        std::string rStr = funName + "(\"" + event + "\", \"" + errorCode+str + "\");";
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
//        ip = errorCode + "fail";
    }
    
    return [[NSString alloc] initWithUTF8String:ip];

}


//设置禁止云同步

-(BOOL)addSkipBackupAttributeToItemAtURL:(NSURL *)URL{
    
    double version = [[UIDevice currentDevice].systemVersion doubleValue];//判定系统版本。
    
    if(version >=5.1f){
        
        NSError *error = nil;
        
        BOOL success = [URL setResourceValue: [NSNumber numberWithBool: YES] forKey: NSURLIsExcludedFromBackupKey error: &error];
        
        if(!success){
            
            NSLog(@"Error excluding %@ from backup %@", [URL lastPathComponent], error);
        }else
        {
            NSLog(@"successful");
        }
        
        return success;
        
    }
    
    //    const char* filePath = [[URL path] fileSystemRepresentation];
    //        const char* attrName = "com.apple.MobileBackup";
    //
    //    u_int8_t attrValue = 1;
    //
    //    int result = setxattr(filePath, attrName, &attrValue, sizeof(attrValue), 0, 0);
    
    
    //    return result == 0;
    return false;
}


- (void)applicationWillResignActive:(UIApplication *)application {
    /*
     Sent when the application is about to move from active to inactive state. This can occur for certain types of temporary interruptions (such as an incoming phone call or SMS message) or when the user quits the application and it begins the transition to the background state.
     Use this method to pause ongoing tasks, disable timers, and throttle down OpenGL ES frame rates. Games should use this method to pause the game.
     */
    cocos2d::Director::getInstance()->pause();
}


- (void)applicationDidBecomeActive:(UIApplication *)application {
    /*
     Restart any tasks that were paused (or not yet started) while the application was inactive. If the application was previously in the background, optionally refresh the user interface.
     */
    cocos2d::Director::getInstance()->resume();
}

- (void)applicationDidEnterBackground:(UIApplication *)application {
    /*
     Use this method to release shared resources, save user data, invalidate timers, and store enough application state information to restore your application to its current state in case it is terminated later.
     If your application supports background execution, called instead of applicationWillTerminate: when the user quits.
     */
    cocos2d::Application::getInstance()->applicationDidEnterBackground();
    [UIApplication sharedApplication].idleTimerDisabled=YES;//自动锁屏
    isAPPNewStart = false;
    [[UIApplication sharedApplication] beginBackgroundTaskWithExpirationHandler:^{
    }];
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


#pragma mark -
#pragma mark Memory management

- (void)applicationDidReceiveMemoryWarning:(UIApplication *)application {
    /*
     Free up as much memory as possible by purging cached data objects that can be recreated (or reloaded from disk) later.
     */
     cocos2d::Director::getInstance()->purgeCachedData();
}


- (void)dealloc {
    [super dealloc];
}

// 获取当前任务所占用的内存（单位：MB）
- (double)usedMemory
{
    task_basic_info_data_t taskInfo;
    mach_msg_type_number_t infoCount = TASK_BASIC_INFO_COUNT;
    kern_return_t kernReturn = task_info(mach_task_self(),
                                         TASK_BASIC_INFO,
                                         (task_info_t)&taskInfo,
                                         &infoCount);
    
    if (kernReturn != KERN_SUCCESS
        ) {
        return NSNotFound;
    }
    
    return taskInfo.resident_size / 1024.0 / 1024.0;
}

- (double)availableMemory
{
    int mib[6];
    mib[0] = CTL_HW;
    mib[1] = HW_PAGESIZE;
    
    int pagesize;
    size_t length;
    length = sizeof (pagesize);
    if (sysctl (mib, 2, &pagesize, &length, NULL, 0) < 0)
    {
        fprintf (stderr, "getting page size");
    }
    
    mach_msg_type_number_t count = HOST_VM_INFO_COUNT;
    
    vm_statistics_data_t vmstat;
    if (host_statistics (mach_host_self (), HOST_VM_INFO, (host_info_t) &vmstat, &count) != KERN_SUCCESS)
    {
        fprintf (stderr, "Failed to get VM statistics.");
    }
    task_basic_info_64_data_t info;
    unsigned size = sizeof (info);
    task_info (mach_task_self (), TASK_BASIC_INFO_64, (task_info_t) &info, &size);
    
    double unit = 1024 * 1024;
    double total = (vmstat.wire_count + vmstat.active_count + vmstat.inactive_count + vmstat.free_count) * pagesize / unit;
    double wired = vmstat.wire_count * pagesize / unit;
    double active = vmstat.active_count * pagesize / unit;
    double inactive = vmstat.inactive_count * pagesize / unit;
    double free = vmstat.free_count * pagesize / unit;
    double resident = info.resident_size / unit;
    NSLog(@"===================================================");
    NSLog(@"Total:%.2lfMb", total);
    NSLog(@"Wired:%.2lfMb", wired);
    NSLog(@"Active:%.2lfMb", active);
    NSLog(@"Inactive:%.2lfMb", inactive);
    NSLog(@"Free:%.2lfMb", free);
    NSLog(@"Resident:%.2lfMb", resident);
    
    return total;
}

//-----------------------gaode ------------------------------------
#pragma mark -
#pragma mark 高德SDK

- (void)configLocationManager
{
    [AMapServices sharedServices].apiKey = g_GaoDeAppId;
    locationManager = [[AMapLocationManager alloc] init];
    
    [locationManager setDelegate:self];
    
    [locationManager setPausesLocationUpdatesAutomatically:NO];
    
    [locationManager setAllowsBackgroundLocationUpdates:NO];
}
//- (void)startSerialLocation
//{
//    //开始定位
//    [locationManager startUpdatingLocation];
//}

//add by gxmj

NSString *formattedAddress = @"";
    
+(NSString*)getFormattedAddress
{
    return formattedAddress;
}

//formattedAddress在startSerialLocation中赋值，startSerialLocation方法已存在

- (void)startSerialLocation
{
    //开始定位
//    [locationManager startUpdatingLocation];
    // 带逆地理（返回坐标和地址信息）。将下面代码中的YES改成NO，则不会返回地址信息。
    [locationManager requestLocationWithReGeocode:YES completionBlock:^(CLLocation *location, AMapLocationReGeocode *regeocode, NSError *error){
        
        if (error)
        {
            NSLog(@"locError:{%ld - %@};", (long)error.code, error.localizedDescription);
            
            if (error.code == AMapLocationErrorLocateFailed)
            {
                return;
            }
        }
        
        NSLog(@"location:%@", location);
        
        latitudePos = location.coordinate.latitude;
        longitudePos = location.coordinate.longitude;
        
        if (regeocode)
        {
            NSLog(@"reGeocode:%@", regeocode);
            
            formattedAddress = [regeocode.formattedAddress copy];
        }
    }];
}

- (void)stopSerialLocation
{
    //停止定位
    [locationManager stopUpdatingLocation];
}

- (void)amapLocationManager:(AMapLocationManager *)manager didFailWithError:(NSError *)error
{
    //定位错误
    NSLog(@"%s, 定位错误amapLocationManager = %@, error = %@", __func__, [manager class], error);
}

float latitudePos;
float longitudePos;
- (void)amapLocationManager:(AMapLocationManager *)manager didUpdateLocation:(CLLocation *)location
{
    //定位结果
    //NSLog(@"定位结果location:{lat:%f; lon:%f; accuracy:%f}", location.coordinate.latitude, location.coordinate.longitude, location.horizontalAccuracy);
    
    latitudePos = location.coordinate.latitude;
    longitudePos = location.coordinate.longitude;
    
    [self stopSerialLocation];
}


+(NSString*)getLatitudePos//wei du 纬度
{
    NSString* latPos = [NSString stringWithFormat:@"%6f",latitudePos];
    
    return latPos;
}

+(NSString*)getLongitudePos//jing du 经度
{
    NSString* lonPos = [NSString stringWithFormat:@"%6f",longitudePos];
    
    return lonPos;
}
/*
 计算距离
 */
+(NSString*)calculateDistance:(NSString*)latvar1 lon1:(NSString*)lonvar1 lat2:(NSString*)latvar2 lon2:(NSString*)lonvar2
{
    
    double lat1 = [latvar1 doubleValue];
    double lon1 = [lonvar1 doubleValue];
    double lat2 = [latvar2 doubleValue];
    double lon2 = [lonvar2 doubleValue];
    
    double R = 6371004; //chi dao ban jing
    double dd = M_PI/180;
    
    double x1=lat1*dd, x2=lat2*dd;
    double y1=lon1*dd, y2=lon2*dd;
    
    double distance = (2*R*asin(sqrt(2-2*cos(x1)*cos(x2)*cos(y1-y2) - 2*sin(x1)*sin(x2))/2));
    
    NSString* disStr = [NSString stringWithFormat:@"%6f",distance];
    
    return disStr;
}

//------------------------xianliao-------------------------------
//闲聊请求授权
+(void)sendXLRequest
{
    NSLog(@"闲聊请求code");
    if([self isXLInstall]){
        [XianliaoApiManager loginState:@"sugram_login_state" fininshBlock:^(XianliaoLoginCallBackType callBackType,NSString *code,NSString *state) {
            NSLog(@"callBackType:%lu, code:%@, state:%@", (unsigned long)callBackType, code, state);
            //获取成功
            if(callBackType == XianliaoLoginSuccessType){
                //根据获取的code去获取token
                NSLog(@"code:%@",code);
                [self getXLToken:code];
            }
            [self xlLoginMSG:callBackType];
        }];
    }
}

//根据code获取token
+(void)getXLToken:(NSString* )code
{
    NSLog(@"根据code获取token");
    NSString *url = [NSString stringWithFormat:@"https://ssgw.updrips.com/oauth2/accessToken?client_id=%@&client_secret=%@&grant_type=authorization_code&code=%@",XL_AppID,XL_AppSecret,code];
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,0),^{
        NSURL *zoneUrl=[NSURL URLWithString:url];
        NSString *zoneStr = [NSString stringWithContentsOfURL:zoneUrl encoding:NSUTF8StringEncoding error:nil];
        NSData *data = [zoneStr dataUsingEncoding:NSUTF8StringEncoding];
        dispatch_async(dispatch_get_main_queue(), ^{
            if(data){
                NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
                NSInteger xl_code = [[dic objectForKey:@"code"] intValue];
                if(xl_code == 0){
                    xl_access_token = [[dic objectForKey:@"data"] objectForKey:@"access_token"];
                    NSLog(@"xl_access_token:");
                    NSLog(@"xl_access_token:%@",xl_access_token);
                    
                    xl_refresh_token = [[dic objectForKey:@"data"] objectForKey:@"refresh_token"];
                    NSLog(@"xl_refresh_token:");
                    NSLog(@"xl_refresh_token:%@",xl_refresh_token);
                    //根据token获取userinfo
                    [self getXLUserinfoByToken];
                }
                else{
                    [self xlErrorAfterGetCode];
                }
            }
        });
    });
}

//根据token去获取userinfo(iOS 版没有写在token失效的情况下对token进行刷新的逻辑，在token失效的情况下可以看作登陆失败，重新进行登录授权即可)
+(void)getXLUserinfoByToken
{
    NSLog(@"根据token去获取userinfo");
    NSString *url = [NSString stringWithFormat:@"https://ssgw.updrips.com/resource/user/getUserInfo?access_token=%@",xl_access_token];
    
    dispatch_async(dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT,0),^{
        NSURL *zoneUrl=[NSURL URLWithString:url];
        NSString *zoneStr = [NSString stringWithContentsOfURL:zoneUrl encoding:NSUTF8StringEncoding error:nil];
        NSData *data = [zoneStr dataUsingEncoding:NSUTF8StringEncoding];
        dispatch_async(dispatch_get_main_queue(), ^{
            if(data){
                NSString *result = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
                NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:nil];
                NSInteger xl_code = [[dic objectForKey:@"code"] intValue];
                if(xl_code == 0){
                    xl_openid =[[dic objectForKey:@"data"] objectForKey:@"openId"];
                    
                    NSString*   nickname = [[dic objectForKey:@"data"] objectForKey:@"nickName"];
                    cocos2d::FileUtils::getInstance()->writeStringToFile(nickname.UTF8String,
                                                                         cocos2d::FileUtils::getInstance()->getWritablePath()+"/XLNickName.txt");
                    std::string result_c_str= [result cStringUsingEncoding: NSUTF8StringEncoding];
                    std::string event ="XL_USER_LOGIN";
                    std::string funName ="cc.eventManager.dispatchCustomEvent";
                    std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
                    
                    
                    NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
                    NSLog(@"printStr:%@",printStr);
                    
                    
                    ScriptingCore::getInstance()->evalString(rStr.c_str());
                }
                else{
                    [self xlErrorAfterGetCode];
                }
            }
        });
    });
}

//闲聊分享文本
+(void)xlShareText:(NSString*)title AndText:(NSString*)text
{
    NSLog(@"闲聊分享文本");
    if([self isXLInstall]){
        XianliaoShareTextObject *textObject = [[XianliaoShareTextObject alloc] init];
        textObject.text = text;
        [XianliaoApiManager share:textObject fininshBlock:^(XianliaoShareCallBackType callBackType) {
            NSLog(@"callBackType:%ld", (long)callBackType);
            [self xlShareMSG:callBackType];
        }];
    }
}
//闲聊分享图片
+(void)xlShareImage:(NSString*)path
{
    NSLog(@"闲聊分享图片");
    if([self isXLInstall]){
        UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
        CGSize size= [image size];
        
        float scale_1 = 1.0;
        CGSize scaleSize1 = CGSizeMake(size.width/scale_1,size.height/scale_1);
        UIGraphicsBeginImageContext(scaleSize1);
        CGRect rect1 = CGRectMake(0.0, 0.0, scaleSize1.width, scaleSize1.height);
        [image drawInRect:rect1];
        UIImage * scaleimage1 = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        
        float ration_1 = 0.5;
        XianliaoShareImageObject *imageObject = [[XianliaoShareImageObject alloc] init];
        imageObject.imageData =UIImageJPEGRepresentation(scaleimage1, ration_1);
        [XianliaoApiManager share:imageObject fininshBlock:^(XianliaoShareCallBackType callBackType) {
            NSLog(@"callBackType:%ld", (long)callBackType);
            [self xlShareMSG:callBackType];
        }];
    }
}
//闲聊分享游戏邀请
+(void)xlShareInvite:(NSString*)roomToken AndID:(NSString*)roomId AndTitle:(NSString*)title AndText:(NSString*)text
{
    NSLog(@"闲聊分享游戏邀请->roomToken:%@  roomId:%@  title:%@  text:%@",roomToken,roomId,title,text);
    if([self isXLInstall]){
        XianliaoShareAppObject * inviteObject = [[XianliaoShareAppObject alloc] init];
        inviteObject.roomToken = roomToken;
        inviteObject.roomId = roomId;
        inviteObject.title = title;
        inviteObject.text = text;
        inviteObject.imageData =UIImagePNGRepresentation([UIImage imageNamed:@"Icon-100.png"]);
        
        [XianliaoApiManager share:inviteObject fininshBlock:^(XianliaoShareCallBackType callBackType) {
            NSLog(@"callBackType:%ld", (long)callBackType);
            [self xlShareMSG:callBackType];
        }];
    }
}

+(void)xlShareUrl:(NSString*)title AndText:(NSString*)text AndUrl:(NSString*)url {
    if ([self isXLInstall]) {
        XianliaoShareLinkObject *object = [[XianliaoShareLinkObject alloc] init];
        object.title = title;
        object.linkDescription = text;
        object.imageData = UIImagePNGRepresentation([UIImage imageNamed:@"Icon-100.png"]);
        object.url = url;
        [XianliaoApiManager share:object fininshBlock:^(XianliaoShareCallBackType callBackType) {
            NSLog(@"callBackType:%ld", (long)callBackType);
            [self xlShareMSG:callBackType];
        }];
    }
}

//闲聊登录结果
+(void)xlLoginMSG:(XianliaoLoginCallBackType)callBackType{
    NSLog(@"闲聊登录结果...");
    std::string result_c_str= "";
    
    if (callBackType == XianliaoLoginSuccessType) {
        result_c_str = "{\"errMsg\":\"ERR_SUCCESS\"}";
    }
    else if (callBackType == XianliaoLoginCancelType) {
        result_c_str = "{\"errMsg\":\"ERR_CANCEL\"}";
    }
    else if(callBackType == XianliaoLoginErrorType){
        result_c_str = "{\"errMsg\":\"ERR_FAIL\"}";
    }
    else if(callBackType == XianliaoLoginUnkonwType){
        result_c_str = "{\"errMsg\":\"ERR_UNKNOW\"}";
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        std::string event = "XL_LOGIN_MSG";
        std::string funName = "cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        
        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
        NSLog(@"printStr:%@",printStr);
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    });
    NSLog(@"闲聊登录结果结束...");
}

//闲聊在获取code之后的其他操作失败
+(void)xlErrorAfterGetCode{
    NSLog(@"闲聊在获取code之后的其他操作失败...");
    std::string result_c_str = "{\"errMsg\":\"ERR_AFTERGETCODE\"}";
    dispatch_async(dispatch_get_main_queue(), ^{
        std::string event = "XL_LOGIN_MSG";
        std::string funName = "cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        
        
        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
        NSLog(@"printStr:%@",printStr);
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    });
}

//闲聊分享结果
+(void)xlShareMSG:(XianliaoShareCallBackType)callBackType{
    NSLog(@"闲聊分享结果...");
    std::string result_c_str= "";
    
    if (callBackType == XianliaoShareSuccesslType) {
        result_c_str = "{\"errMsg\":\"ERR_SUCCESS\"}";
    }
    else if(callBackType == XianliaoShareCancelType){
        result_c_str = "{\"errMsg\":\"ERR_CANCEL\"}";
    }
    else if(callBackType == XianliaoShareErrorType){
        result_c_str = "{\"errMsg\":\"ERR_FAIL\"}";
    }
    else if(callBackType == XianliaoShareUnkonwType){
        result_c_str = "{\"errMsg\":\"ERR_UNKNOW\"}";
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        std::string event = "XL_SHARE_MSG";
        std::string funName = "cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        
        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
        NSLog(@"printStr:%@",printStr);
        
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    });
    NSLog(@"闲聊分享结果结束...");
}

//查询闲聊查询游戏邀请信息
+(void)xlGetInviteGameInfo
{
    NSLog(@"查询闲聊查询游戏邀请信息...");
    NSString *inviateMsgStr1 = [NSString stringWithCString:inviateMsgStr.c_str() encoding:[NSString defaultCStringEncoding]];
    NSLog(@"inviateMsgStr:%@  len=%lu",inviateMsgStr1,inviateMsgStr.length());
    if(inviateMsgStr.length() > 0){
        dispatch_async(dispatch_get_main_queue(), ^{
            NSLog(@"准备发送闲聊邀请信息...");
            ScriptingCore::getInstance()->evalString(inviateMsgStr.c_str());
            NSLog(@"已发送闲聊邀请信息(新启动应用)。。。");
            
            inviateMsgStr = "";
            NSLog(@"已重置闲聊邀请信息。。。");
        });
    }
}

/*! @brief 检查微信是否已被用户安装
 *
 * @return 微信已安装返回YES，未安装返回NO。
 */
+(BOOL)isWXAppInstalledgame{
    BOOL haveWX = [WXApi isWXAppInstalled];
    if(haveWX){
//        NSLog(@"have haveWX app");
        return true;
    }
    else{
//        NSLog(@"no haveWX app");
        return false;
    }
}

//查询是否安装了闲聊
+(BOOL)isXLInstall{
    BOOL haveXL = [XianliaoApiManager isInstallXianliao];
    if(haveXL){
        NSLog(@"have xianliao app");
        return true;
    }
    else{
        NSLog(@"no xianliao app");
        [self doWithoutXLApp];
        return false;
    }
}
//没有安装闲聊的处理
+(void)doWithoutXLApp{
    NSLog(@"doWithoutXLApp...");
    NSURL *url = [ [ NSURL alloc ] initWithString: @"http://a.app.qq.com/o/simple.jsp?pkgname=org.xianliao"];
    [[UIApplication sharedApplication] openURL:url];
}

//------------xianliao--------end-----------------------------------


// --------------------------------聊呗函数 start---------------------------------------------------


// 分享的回调结果
- (void)onShareResp:(LBSDKShareResp *)resp {
    if (resp.resultCode == LBSDKResultCode_Success) {
        NSString *str = [NSString stringWithFormat:@"share message success for state: %@", resp.state];
        [self showResult:str];
        [self lbShareMSG:0];
    } else {
        NSString *str = [NSString stringWithFormat:@"share message faield for state: %@，code: %li, message: %@", resp.state, (long)resp.resultCode, resp.errStr];
        [self showResult:str];
        [self lbShareMSG:-1];
    }
}

// 授权的回调结果
- (void)onAuthResp:(LBSDKAuthResp *)resp {
    
    NSLog(@"聊呗授权回调用...");
    //    NSLog(@"get accessToken %@", resp.resultCode);
    if (resp.resultCode == LBSDKResultCode_Success) {
        [self lbLoginMSG:0];
        NSLog(@"聊呗授权回调用成功...");
        NSString *code = resp.code;
        if (code) {
            [LBUtils getAccessToken:code completionHandler:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
                if (error != nil) {
                    // 请求错误处理
                    NSLog(@"聊呗请求错误处理1...");
                    NSLog(@"错误信息  %@", result);
                    //                    NSLog(@"%@",error);
                    NSLog(@"聊呗请求错误处理111...");
                    
                    
                    [self lbErrorAfterGetCode];
                    NSLog(@"聊呗请求错误处理end...");
                    return;
                }
                // TODO: 对 result 的验证，以及 result 的取值，请自行验证，保证对象的可用性。
                NSLog(@"get accessToken %@", result);
                int responseCode = [result[@"result"] intValue];
                if (responseCode == 0) {
                    NSString *token = result[@"access_token"];
                    NSString *openId = result[@"openid"];
                    
                    dispatch_async(dispatch_get_main_queue(), ^{
                        NSString *result = [NSString stringWithFormat:@"{\"result\":\"SUCCESS\",\"access_token\":\"%@\",\"open_id\":\"%@\"}", token, openId];
                        std::string resultCStr = [result cStringUsingEncoding: NSUTF8StringEncoding];
                        std::string event = "MSG_LB_AUTH";
                        std::string funName = "cc.eventManager.dispatchCustomEvent";
                        std::string rStr = funName + "(\"" + event + "\"," + resultCStr + ");";
                        ScriptingCore::getInstance()->evalString(rStr.c_str());
                    });
                    
                    //请求用户信息
                    [LBUtils getUserInfo:token openId:openId completionHandler:^(NSDictionary * _Nullable result, NSError * _Nullable error) {
                        if (error != nil) {
                            // 请求错误处理
                            NSLog(@"聊呗请求错误处理2...");
                            [self lbErrorAfterGetCode];
                            return;
                        }
                        
                        int infoCode = [result[@"result"] intValue];
                        if (infoCode == 0)
                        {
                            
                            NSData *myJsonData = [NSJSONSerialization dataWithJSONObject:result options:NSJSONWritingPrettyPrinted error:&error];
                            
                            dispatch_async(dispatch_get_main_queue(), ^{
                                
                                NSLog(@"聊呗获取用户信息成功1...");
                                
                                NSString *nickname = result[@"nickname"];
                                cocos2d::FileUtils::getInstance()->writeStringToFile(nickname.UTF8String,
                                                                                     cocos2d::FileUtils::getInstance()->getWritablePath()+"/LBNickName.txt");
                                
                                
                                NSString * returnString = [[NSString alloc] initWithData:myJsonData encoding:NSUTF8StringEncoding];
                                std::string result_c_str = [returnString UTF8String];
                                std::string event ="LB_USER_LOGIN";
                                std::string funName ="cc.eventManager.dispatchCustomEvent";
                                std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
                                
                                
                                //                                [self lbLoginUserMSG:0];
                                
                                NSLog(@"聊呗获取用户信息成功2...");
                                NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
                                NSLog(@"printStr:%@",printStr);
                                
                                
                                //                            NSLog(@"printStr:%@",rStr);
                                
                                ScriptingCore::getInstance()->evalString(rStr.c_str());
                                NSLog(@"聊呗获取用户信息成功3...");
                            });
                            
                            
                        }else
                        {
                            NSLog(@"聊呗请求错误处理3...");
                            [self lbErrorAfterGetCode];
                        }
                        
                        
                        
                        
                        NSLog(@"get user Info %@", result);
                    }];
                } else {
                    NSLog(@"聊呗请求错误处理4...");
                    [self lbErrorAfterGetCode];
                    // 返回的 errcode 请自行参考文档 https://open.liaobe.cn/doc/appLoginApi
                }
            }];
        }
    }
    else {
        NSLog(@"聊呗授权回调用失败...");
        [self lbLoginMSG:-1];
        // error handle
    }
}

- (void)showResult:(NSString *)str {
    NSLog(@"%@", str);
    // TODO: alert result
}



//聊呗登录结果
-(void)lbLoginMSG:(int)code{
    NSLog(@"聊呗登录结果...");
    std::string result_c_str= "";
    
    if (code == 0) {
        result_c_str = "{\"errMsg\":\"ERR_SUCCESS\"}";
    }
    else {
        result_c_str = "{\"errMsg\":\"ERR_CANCEL\"}";
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        std::string event = "LB_LOGIN_MSG";
        std::string funName = "cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        
        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
        NSLog(@"printStr:%@",printStr);
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    });
    NSLog(@"聊呗登录结果结束...");
}

//-(void)lbLoginUserMSG:(int)code{
//    NSLog(@"聊呗用户信息 结果...");
//    std::string result_c_str= "";
//
//    if (code == 0) {
//        result_c_str = "{\"errMsg\":\"ERR_SUCCESS\"}";
//    }
//
//    dispatch_async(dispatch_get_main_queue(), ^{
//        std::string event = "LB_USER_LOGIN";
//        std::string funName = "cc.eventManager.dispatchCustomEvent";
//        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
//
//        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
//        NSLog(@"printStr:%@",printStr);
//
//        ScriptingCore::getInstance()->evalString(rStr.c_str());
//    });
//    NSLog(@"聊呗用户信息 结束...");
//}

//聊呗在获取code之后的其他操作失败
-(void)lbErrorAfterGetCode{
    NSLog(@"聊呗在获取code之后的其他操作失败...");
    std::string result_c_str = "{\"errMsg\":\"ERR_AFTERGETCODE\"}";
    dispatch_async(dispatch_get_main_queue(), ^{
        std::string event = "LB_LOGIN_MSG";
        std::string funName = "cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        
        
        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
        NSLog(@"printStr:%@",printStr);
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    });
}

//聊呗分享结果
-(void)lbShareMSG:(int)code{
    NSLog(@"聊呗分享结果...");
    std::string result_c_str= "";
    
    if (code == 0) {
        result_c_str = "{\"errMsg\":\"ERR_SUCCESS\"}";
    }
    else{
        result_c_str = "{\"errMsg\":\"ERR_CANCEL\"}";
    }
    
    
    dispatch_async(dispatch_get_main_queue(), ^{
        std::string event = "LB_SHARE_MSG";
        std::string funName = "cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
        
        NSString *printStr = [NSString stringWithCString:rStr.c_str() encoding:[NSString defaultCStringEncoding]];
        NSLog(@"printStr:%@",printStr);
        
        
        ScriptingCore::getInstance()->evalString(rStr.c_str());
    });
    NSLog(@"聊呗分享结果结束...");
}




- (NSString *)uuid {
    return [[NSUUID UUID] UUIDString];
}

/**
 关联群组之后的回调
 @param resp 关联群组回调结果
 */
- (void)onRelevanceGroupResp:(LBSDKRelevanceGroupResq *)resp {
    if (resp.resultCode == LBSDKResultCode_Success) {
        dispatch_async(dispatch_get_main_queue(), ^{
            std::string resultCStr = "{\"result\":\"SUCCESS\"}";
            std::string event = "MSG_LB_RELEVANCE_GROUP";
            std::string funName = "cc.eventManager.dispatchCustomEvent";
            std::string rStr = funName + "(\"" + event + "\"," + resultCStr + ");";
            ScriptingCore::getInstance()->evalString(rStr.c_str());
        });
    }
}

/**
 加入群组之后的回调
 @param resp 加入群组回调结果
 */
- (void)onJoinGroupResp:(LBSDKJoinGroupResq *)resp {
    if (resp.resultCode == LBSDKResultCode_Success) {
        dispatch_async(dispatch_get_main_queue(), ^{
            std::string resultCStr = "{\"result\":\"SUCCESS\"}";
            std::string event = "MSG_LB_JOIN_GROUP";
            std::string funName = "cc.eventManager.dispatchCustomEvent";
            std::string rStr = funName + "(\"" + event + "\"," + resultCStr + ");";
            ScriptingCore::getInstance()->evalString(rStr.c_str());
        });
    }
}

/**
 关联聊呗群
 */
+ (void)lbRelevanceGroup:(NSString*)groupId userId:(NSString*)userId userName:(NSString*)userName callbackUrl:(NSString*)callbackUrl{
    LBSDKRelevanceGroupReq *relevanceGroupReq = [LBSDKRelevanceGroupReq new];
    relevanceGroupReq.groupId = groupId;
    relevanceGroupReq.userId = userId;
    relevanceGroupReq.userName = userName;
    relevanceGroupReq.groupUrl = callbackUrl;
    [LBSDKApi sendRelevanceGroupReq:relevanceGroupReq];
}

/**
 加入聊呗群
 */
+ (void)lbJoinGroup:(NSString *)groupUrl{
    LBSDKJoinGroupReq *joinGroupReq = [LBSDKJoinGroupReq new];
    joinGroupReq.hashCodeUrlString = groupUrl;
    [LBSDKApi sendJoinGroupReq:joinGroupReq];
}

//查询是否安装了闲聊
+(BOOL)isLBInstall{
    BOOL haveLB = [LBSDKApi isLiaobeiAppInstalled];
    if(haveLB){
        NSLog(@"have liaobei app");
        return true;
    }
    else{
        NSLog(@"no liaobei app");
        [self doWithoutLBApp];
        return false;
    }
}
//没有安装闲聊的处理
+(void)doWithoutLBApp{
    NSLog(@"doWithoutLBApp...");
    //    NSURL *url = [ [ NSURL alloc ] initWithString: @"http://a.app.qq.com/o/simple.jsp?pkgname=org.xianliao"];
    NSURL *url = [ [ NSURL alloc ] initWithString: @"http://liaobe.cn"];
    [[UIApplication sharedApplication] openURL:url];
}


// 聊呗发送授权申请
+(void)sendLBRequest
{
    if([self isLBInstall]){
        
        NSLog(@"聊呗调用授权start...");
        LBSDKAuthReq *req = [LBSDKAuthReq new];
        //    req.state = [self uuid];
        BOOL success = [LBSDKApi sendAuthReq:req];
        if (success) {
            NSLog(@"send auth req for state，，，");
        }
    }
    
    NSLog(@"聊呗调用授权end...");
}

// 聊呗分享文字消息
+(void)lbShareText:(NSString*)text
{
    LBSDKTextMessage *message = [LBSDKTextMessage new];
    message.text = text;
    
    LBSDKShareReq *req = [LBSDKShareReq new];
    req.message = message;
    // state 这次 request 的唯一标识，用客户端传入，并在 resp 中返回，用于客户端验证请求的唯一性，
    // 客户端如果不用 可以为空的。
    //    req.state = [self uuid];
    BOOL success = [LBSDKApi sendShareReq:req];
    if (success) {
        NSLog(@"send share text req for state %@", req.state);
    }
}

// 聊呗分享图片消息
+(void)lbShareImage:(NSString*)path
{
    LBSDKImageMessage *message = [LBSDKImageMessage new];
    UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
    // message.image = [UIImage imageNamed:@"SharedImage"];
    message.image = image;
    message.caption = @""; //这个是一个可选字段
    
    LBSDKShareReq *req = [LBSDKShareReq new];
    req.message = message;
    //    req.state = [self uuid];
    BOOL success = [LBSDKApi sendShareReq:req];
    if (success) {
        NSLog(@"send share text req for state %@", req.state);
    }
}

// 聊呗分享链接
+(void)lbShareUrl:(NSString*)title AndText:(NSString*)text AndUrl:(NSString*)url
{
    NSLog(@"聊呗分享 URL text %@", text);
    NSLog(@"聊呗分享 URL title %@", title);
    NSLog(@"聊呗分享 URL url %@", url);
    LBSDKLinkMessage *message = [LBSDKLinkMessage new];
    message.url = url; // 聊呗分享的链接，不能为空
    message.title = title; // title 不能为空
    message.desc = text;
    //    message.icon =   // 这个是一个可选的 icon imagef l
    message.icon = [UIImage imageNamed:@"Icon-100.png"];
    
    LBSDKShareReq *req = [LBSDKShareReq new];
    req.message = message;
    //    req.state = [self uuid];
    BOOL success = [LBSDKApi sendShareReq:req];
    if (success) {
        NSLog(@"send share text req for state %@", req.state);
    }
    
    //    [UIView animateWithDuration:02 animations:nil completion:nil];
}

// 聊呗分享链接带Icon
- (IBAction)sendUrlWithImageShare:(id)sender {
    // TODO: add more test
}







// --------------------------------聊呗函数 end---------------------------------------------------
#pragma mark -
#pragma mark 横竖屏转换
- (UIInterfaceOrientationMask)application:(UIApplication *)application supportedInterfaceOrientationsForWindow:(nullable UIWindow *)window{
    if (g_bIsPortrait) {
        return UIInterfaceOrientationMaskPortrait;//竖屏
    }else{
        return UIInterfaceOrientationMaskLandscape;//横屏
    }
}

+(bool)getOrientation{
    return g_bIsPortrait;
}

/**
 bIsPortrait true 竖, false 横
 返回值: 1 横, 2竖, -1错
 */
+(int)setOrientation:(bool)bIsPortrait{
    int aRet = -1;
    if(g_bIsPortrait == bIsPortrait){
        return aRet;
    }
    
    g_bIsPortrait = bIsPortrait;
    UIInterfaceOrientation interfaceOrientation = UIInterfaceOrientationUnknown;
    if(bIsPortrait){
        interfaceOrientation =UIInterfaceOrientationPortrait;
        aRet = 2;
    }
    else{
        interfaceOrientation =UIInterfaceOrientationLandscapeRight;
        aRet = 1;
    }
    
    NSLog(@"%d,setOrientation",bIsPortrait);
    NSNumber *resetOrientationTarget = [NSNumber numberWithInt:UIInterfaceOrientationUnknown];
    [[UIDevice currentDevice] setValue:resetOrientationTarget forKey:@"orientation"];
    
    NSNumber *orientationTarget = [NSNumber numberWithInt:interfaceOrientation];
    [[UIDevice currentDevice] setValue:orientationTarget forKey:@"orientation"];
    
    return aRet;
    
}


// --------------------------------钉钉 begin----------------------------------------------------
//钉钉分享文本
+(void)StartShareTxtToDD:(NSString* )sayTxt {
    DTSendMessageToDingTalkReq *sendMessageReq = [[DTSendMessageToDingTalkReq alloc] init];
    
    DTMediaMessage *mediaMessage = [[DTMediaMessage alloc] init];
    
    DTMediaTextObject *textObject = [[DTMediaTextObject alloc] init];
    textObject.text = sayTxt;
    
    mediaMessage.mediaObject = textObject;
    sendMessageReq.message = mediaMessage;
    
    BOOL result = [DTOpenAPI sendReq:sendMessageReq];
    if (result)
    {
        NSLog(@"Text 发送成功.");
    }
    else
    {
        NSLog(@"Text 发送失败.");
    }
}

//钉钉分享图片
+(void)StartShareTextureToDD:(NSString* )path {
    if([self isDDInstall]){
        //UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
        UIImage *image  = [[UIImage alloc]initWithContentsOfFile:path];
        CGSize size= [image size];
        
        float scale_1 = 1.0;
        CGSize scaleSize1 = CGSizeMake(size.width/scale_1,size.height/scale_1);
        UIGraphicsBeginImageContext(scaleSize1);
        CGRect rect1 = CGRectMake(0.0, 0.0, scaleSize1.width, scaleSize1.height);
        [image drawInRect:rect1];
        UIImage * scaleimage1 = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        
        float ration_1 = 0.5;
        
        DTSendMessageToDingTalkReq *sendMessageReq = [[DTSendMessageToDingTalkReq alloc] init];
        
        DTMediaMessage *mediaMessage = [[DTMediaMessage alloc] init];
        
        DTMediaImageObject *imageObject = [[DTMediaImageObject alloc] init];
        imageObject.imageData = UIImageJPEGRepresentation(scaleimage1, ration_1);
        //imageObject.imageURL = @"https://img.alicdn.com/tps/TB1dagdIpXXXXc5XVXXXXXXXXXX.jpg";
        
        mediaMessage.mediaObject = imageObject;
        sendMessageReq.message = mediaMessage;
        
        BOOL result = [DTOpenAPI sendReq:sendMessageReq];
        if (result)
        {
            NSLog(@"Image 发送成功.");
        }
        else
        {
            NSLog(@"Image 发送失败.");
        }
    }
    
}

//钉钉分享链接
+(void)StartShareUrlToDD:(NSString*)title AndText:(NSString*)text AndUrl:(NSString*)url {
    if([self isDDInstall]){
        
        NSLog(@"isDingTalkSupportOpenAPI: %@", @([DTOpenAPI isDingTalkSupportOpenAPI]));
        
        DTSendMessageToDingTalkReq *sendMessageReq = [[DTSendMessageToDingTalkReq alloc] init];
        
        DTMediaMessage *mediaMessage = [[DTMediaMessage alloc] init];
        DTMediaWebObject *webObject = [[DTMediaWebObject alloc] init];
        webObject.pageURL = url;
        
        mediaMessage.title = title;
        
        //钉钉图标
//        mediaMessage.thumbURL = @"https://static.dingtalk.com/media/lALOGp__Tnh4_120_120.png";
        
        // Or Set a image data which less than 32K.
        mediaMessage.thumbData = UIImagePNGRepresentation([UIImage imageNamed:@"Icon-100.png"]);
        
        mediaMessage.messageDescription = text;
        mediaMessage.mediaObject = webObject;
        sendMessageReq.message = mediaMessage;
        
        BOOL result = [DTOpenAPI sendReq:sendMessageReq];
        if (result)
        {
            NSLog(@"钉钉Link 发送成功.");
        }
        else
        {
            NSLog(@"钉钉Link 发送失败.");
        }
    }
    
    
}
//查询是否安装了钉钉
+(BOOL)isDDInstall {
    BOOL haveDD = [DTOpenAPI isDingTalkInstalled];
    if(haveDD){
        return true;
    }
    else{
        [self doWithoutDDApp];
        return false;
    }
}

//没有安装钉钉的处理
+(void)doWithoutDDApp {
    NSURL *url = [[NSURL alloc]initWithString: [DTOpenAPI appStoreURLOfDingTalk]  ];
    [[UIApplication sharedApplication] openURL:url];
}
// --------------------------------钉钉 end　----------------------------------------------------

//查询是否安装了吹牛
+(BOOL)isCNInstall {
    return false;
}

//没有安装吹牛的处理
+(void)doWithoutCNApp {
}

//吹牛分享链接
+(void)StartShareUrlToCN:(NSString*)title AndText:(NSString*)text AndUrl:(NSString*)url {
}

//吹牛分享图片
+(void)StartShareTextureToCN:(NSString* )path {
}

//--------------------------------------新语音SDK----------------------------
#pragma mark -
#pragma mark 新语音SDK
+(void)initGameVoiceRoom
{
}
+(void)JoinGameVoiceRoom
{
}
+(void)setVoiceRoomID:(NSString*)roomId
{
}
+(void)setVoiceUserName:(NSString*)userName
{
}
+(void)setVoiceUserId:(NSString*)userId
{
}
+(void)voiceStart
{
}
+(void)voiceStop
{
}
+(void)leaveRoom
{
}
+(void)returnRoom
{
}


+(void)gameExit
{
    cocos2d::Director::getInstance()->end();
//    exit(0);
}

- (void)xyljb_11start{
      
}

- (void)xyljb_start{
    
}



#pragma mark - XGPushDelegate& 信鸽推送相关

-(void) initXGPush:(NSDictionary *)launchOptions {
    //终止信鸽推送服务--终止信鸽推送服务器, 将无法通过信鸽推送服务向设备推送消息
    //[[XGPush defaultManager] stopXGNotification];
    
    [[XGPush defaultManager] setEnableDebug:NO];
    
    //BOOL debugEnabled = [[XGPush defaultManager] isEnableDebug]; //返回true 就是debug 模式
    XGNotificationAction *action1 = [XGNotificationAction actionWithIdentifier:@"xgaction001" title:@"xgAction1" options:XGNotificationActionOptionNone];
    XGNotificationAction *action2 = [XGNotificationAction actionWithIdentifier:@"xgaction002" title:@"xgAction2" options:XGNotificationActionOptionDestructive];
    if (action1 && action2) {
        XGNotificationCategory *category = [XGNotificationCategory categoryWithIdentifier:@"xgCategory" actions:@[action1, action2] intentIdentifiers:@[] options:XGNotificationCategoryOptionNone];
        
        XGNotificationConfigure *configure = [XGNotificationConfigure configureNotificationWithCategories:[NSSet setWithObject:category] types:XGUserNotificationTypeAlert|XGUserNotificationTypeBadge|XGUserNotificationTypeSound];
        if (configure) {
            [[XGPush defaultManager] setNotificationConfigure:configure];
        }
    }
    //
    [[XGPush defaultManager] startXGWithAppID:g_XGappId appKey:g_XGappKey delegate:self];
    [[XGPush defaultManager] setXgApplicationBadgeNumber:0];
    //统计推送效果
    [[XGPush defaultManager] reportXGNotificationInfo:launchOptions];
}

- (void)xgPushDidFinishStart:(BOOL)isSuccess error:(NSError *)error {
    NSLog(@"%s, result %@, error %@", __FUNCTION__, isSuccess?@"OK":@"NO", error);
    UIViewController *ctr = viewController;//[self.window rootViewController];
    if ([ctr isKindOfClass:[UINavigationController class]]) {
//        ViewController *viewCtr = (ViewController *)[(UINavigationController *)ctr topViewController];
//        [viewCtr updateNotification:[NSString stringWithFormat:@"%@%@", @"启动信鸽服务", (isSuccess?@"成功":@"失败")]];
        NSLog(@"%@%@", @"启动信鸽服务", (isSuccess?@"成功":@"失败"));
    }
}

- (void)xgPushDidFinishStop:(BOOL)isSuccess error:(NSError *)error {
    UIViewController *ctr = viewController;//[self.window rootViewController];
    if ([ctr isKindOfClass:[UINavigationController class]]) {
////        ViewController *viewCtr = (ViewController *)[(UINavigationController *)ctr topViewController];
////        [viewCtr updateNotification:[NSString stringWithFormat:@"%@%@", @"注销信鸽服务", (isSuccess?@"成功":@"失败")]];
        
        NSLog(@"%@%@", @"注销信鸽服务", (isSuccess?@"成功":@"失败"));
    }

}

- (void)xgPushDidRegisteredDeviceToken:(NSString *)deviceToken error:(NSError *)error {
    NSLog(@"%s, result %@, error %@", __FUNCTION__, error?@"NO":@"OK", error);
}

// iOS 10 新增 API
// iOS 10 会走新 API, iOS 10 以前会走到老 API
#if __IPHONE_OS_VERSION_MAX_ALLOWED >= __IPHONE_10_0
// App 用户点击通知
// App 用户选择通知中的行为
// App 用户在通知中心清除消息
// 无论本地推送还是远程推送都会走这个回调
- (void)xgPushUserNotificationCenter:(UNUserNotificationCenter *)center didReceiveNotificationResponse:(UNNotificationResponse *)response withCompletionHandler:(void (^)(void))completionHandler {
    NSLog(@"[XGDemo] click notification");
    if ([response.actionIdentifier isEqualToString:@"xgaction001"]) {
        NSLog(@"click from Action1");
    } else if ([response.actionIdentifier isEqualToString:@"xgaction002"]) {
        NSLog(@"click from Action2");
    }
    
    [[XGPush defaultManager] reportXGNotificationResponse:response];
    
    completionHandler();
}

// App 在前台弹通知需要调用这个接口
//- (void)xgPushUserNotificationCenter:(UNUserNotificationCenter *)center willPresentNotification:(UNNotification *)notification withCompletionHandler:(void (^)(UNNotificationPresentationOptions))completionHandler {
//    [[XGPush defaultManager] reportXGNotificationInfo:notification.request.content.userInfo];
//    completionHandler(UNNotificationPresentationOptionBadge | UNNotificationPresentationOptionSound | UNNotificationPresentationOptionAlert);
//}
#endif

- (void)xgPushDidReceiveRemoteNotification:(id)notification withCompletionHandler:(void (^)(NSUInteger))completionHandler {
    if ([notification isKindOfClass:[NSDictionary class]]) {
        [[XGPush defaultManager] reportXGNotificationInfo:(NSDictionary *)notification];
        completionHandler(UIBackgroundFetchResultNewData);
    } else if ([notification isKindOfClass:[UNNotification class]]) {
        [[XGPush defaultManager] reportXGNotificationInfo:((UNNotification *)notification).request.content.userInfo];
        completionHandler(UNNotificationPresentationOptionBadge | UNNotificationPresentationOptionSound | UNNotificationPresentationOptionAlert);
    }
}

- (void)xgPushDidSetBadge:(BOOL)isSuccess error:(NSError *)error {
    NSLog(@"%s, result %@, error %@", __FUNCTION__, error?@"NO":@"OK", error);
}


/**
 @brief 监控信鸽服务上报推送消息的情况
 
 @param isSuccess 上报是否成功
 @param error 上报失败的信息
 */
- (void)xgPushDidReportNotification:(BOOL)isSuccess error:(nullable NSError *)error{
    
}

+(NSString *)internetStatus {
    Reachability *reachability   = [Reachability reachabilityWithHostName:@"www.baidu.com"];
    NetworkStatus internetStatus = [reachability currentReachabilityStatus];
    NSString *net = @"2|4G"; //蜂窝网络
    switch (internetStatus) {
        case ReachableViaWiFi:
            net = @"1|wifi"; //wifi
            break;
            
        case ReachableViaWWAN:
            net = [@"2|" stringByAppendingString:[self getNetType]];//蜂窝网络
            break;
            
        case NotReachable:
            net = @"0|当前无网络连接"; //当前无网络连接
            
        default:
            break;
    }
    
    return net;
}

+ (NSString *)getNetType{
    CTTelephonyNetworkInfo *info = [[CTTelephonyNetworkInfo alloc] init];
    NSString *currentStatus = info.currentRadioAccessTechnology;
    NSString *netconnType = @"4G";
    
    if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyGPRS"]) {
        netconnType = @"GPRS";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyEdge"]) {
        netconnType = @"2.75G EDGE";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyWCDMA"]){
        netconnType = @"3G";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyHSDPA"]){
        netconnType = @"3.5G HSDPA";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyHSUPA"]){
        netconnType = @"3.5G HSUPA";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMA1x"]){
        netconnType = @"2G";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMAEVDORev0"]){
        netconnType = @"3G";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMAEVDORevA"]){
        netconnType = @"3G";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyCDMAEVDORevB"]){
        netconnType = @"3G";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyeHRPD"]){
        netconnType = @"HRPD";
    }else if ([currentStatus isEqualToString:@"CTRadioAccessTechnologyLTE"]){
        netconnType = @"4G";
    }
    return netconnType;
}


//获取手机通讯录列表
+(void)getPhoneList{
    [self requestContactAuthorAfterSystemVersion9];
}

#pragma mark 请求通讯录权限
+ (void)requestContactAuthorAfterSystemVersion9{
    CNAuthorizationStatus status = [CNContactStore authorizationStatusForEntityType:CNEntityTypeContacts];
    if (status == CNAuthorizationStatusNotDetermined) {
        CNContactStore *store = [[CNContactStore alloc] init];
        [store requestAccessForEntityType:CNEntityTypeContacts completionHandler:^(BOOL granted, NSError*  _Nullable error) {
            if (error) {
                NSLog(@"授权失败");
            }else {
                NSLog(@"成功授权");
                [self openContact];
            }
        }];
    }
    else if(status == CNAuthorizationStatusRestricted)
    {
        NSLog(@"用户拒绝");
        [self showAlertViewAboutNotAuthorAccessContact];
    }
    else if (status == CNAuthorizationStatusDenied)
    {
        NSLog(@"用户拒绝");
        [self showAlertViewAboutNotAuthorAccessContact];
    }
    else if (status == CNAuthorizationStatusAuthorized)//已经授权
    {
        //有通讯录权限-- 进行下一步操作
        [self openContact];
    }
}

//有通讯录权限-- 进行下一步操作
+ (void)openContact{
    // 获取指定的字段,并不是要获取所有字段，需要指定具体的字段
    NSArray *keysToFetch = @[CNContactGivenNameKey, CNContactFamilyNameKey, CNContactPhoneNumbersKey];
    CNContactFetchRequest *fetchRequest = [[CNContactFetchRequest alloc] initWithKeysToFetch:keysToFetch];
    CNContactStore *contactStore = [[CNContactStore alloc] init];
    
    NSMutableArray *arrM = [NSMutableArray array];
    NSString *platform = [[UIDevice currentDevice] model];
//    NSMutableArray *phoneItem = [NSMutableArray array];
    NSArray * phoneTypeArray = @[platform,@"phoneType"];
    [arrM addObject:phoneTypeArray];
    [contactStore enumerateContactsWithFetchRequest:fetchRequest error:nil usingBlock:^(CNContact * _Nonnull contact, BOOL * _Nonnull stop) {
        
        NSString *givenName = contact.givenName;
        NSString *familyName = contact.familyName;
        // NSLog(@"givenName=%@, familyName=%@", givenName, familyName);
        //拼接姓名
        NSString *nameStr = [NSString stringWithFormat:@"%@%@",contact.familyName,contact.givenName];
        
        NSArray *phoneNumbers = contact.phoneNumbers;
        
        for (CNLabeledValue *labelValue in phoneNumbers) {
            //遍历一个人名下的多个电话号码
            NSString *label = labelValue.label;
            //   NSString *    phoneNumber = labelValue.value;
            CNPhoneNumber *phoneNumber = labelValue.value;
            
            NSString * stringNumber = phoneNumber.stringValue ;
            
            //去掉电话中的特殊字符
            stringNumber = [stringNumber stringByReplacingOccurrencesOfString:@"+86" withString:@""];
            stringNumber = [stringNumber stringByReplacingOccurrencesOfString:@"-" withString:@""];
            stringNumber = [stringNumber stringByReplacingOccurrencesOfString:@"(" withString:@""];
            stringNumber = [stringNumber stringByReplacingOccurrencesOfString:@")" withString:@""];
            stringNumber = [stringNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
            stringNumber = [stringNumber stringByReplacingOccurrencesOfString:@" " withString:@""];
            
            //NSLog(@"姓名=%@, 电话号码是=%@", nameStr, stringNumber);
            NSMutableArray *phoneItem = [NSMutableArray array];
            NSArray * array4 = @[nameStr,stringNumber];
            
            [arrM addObject:array4];
        }
        
    }];
    
    NSError *error = nil;
    NSData *result = [NSJSONSerialization dataWithJSONObject:arrM
                                                     options:kNilOptions
                                                       error:&error];
    NSString *resultEnd = nil;
    if(result) {
        resultEnd = [[NSString alloc]initWithData:result encoding:NSUTF8StringEncoding];
    }
    
    NSLog(@"%@",resultEnd);
    
    std::string result_c_str= [resultEnd cStringUsingEncoding: NSUTF8StringEncoding];
    std::string event ="phoneList";
    std::string funName ="cc.eventManager.dispatchCustomEvent";
    std::string rStr = funName + "(\"" + event + "\"," + result_c_str + ");";
    ScriptingCore::getInstance()->evalString(rStr.c_str());
}

//提示没有通讯录权限
- (void)showAlertViewAboutNotAuthorAccessContact{
    
    UIAlertController *alertController = [UIAlertController
                                          alertControllerWithTitle:@"请授权通讯录权限"
                                          message:@"请在iPhone的\"设置-隐私-通讯录\"选项中,允许花解解访问你的通讯录"
                                          preferredStyle: UIAlertControllerStyleAlert];
    
    UIAlertAction *OKAction = [UIAlertAction actionWithTitle:@"好的" style:UIAlertActionStyleDefault handler:nil];
    [alertController addAction:OKAction];
    [self presentViewController:alertController animated:YES completion:nil];
}

#pragma mark - 获取网络状态
+(NSString *)getNetStauts {
    Reachability *reachability   = [Reachability reachabilityWithHostName:@"www.apple.com"];
    NetworkStatus internetStatus = [reachability currentReachabilityStatus];
    NSString *net = @"wifi";
    switch (internetStatus) {
        case ReachableViaWiFi:
            net = @"wifi";
            NSLog(@"wifi");
            break;            
        case ReachableViaWWAN:
            net = @"mobile";
            NSLog(@"mobile");
            break;           
        case NotReachable:
            net = @"notReachable";
            NSLog(@"notReachable");
            break;
        default:
            net = @"wifi";
            NSLog(@"1234");
            break;
    }
    return net;
}

//直接打开微信
+(bool)openWXappOnly{
    return  [WXApi openWXApp];

}

//需要动态初始化的三方
+ (void)init3rdSDK{

    if(appInstance){
        NSLog(@"init3rdSDK");
        //初始化高德地图
        [appInstance configLocationManager];
        [appInstance startSerialLocation];
    }
}

//发送自定义事件
+(void)talkingDataOnClick:(NSString*)eventName ParaKey:(NSString*)key ParaValue:(NSString*)value{
    NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:value, key, nil ];
    [TalkingDataGA onEvent:eventName eventData:dic];
}

//talkingData函数
//绑定uid
+(void)talkingDataSetAccount:(NSString*)uid{
    TDGAAccount *account = [TDGAAccount setAccount:uid];
    [account setAccountType:(TDGAAccountType)kAccountRegistered];
}

+(void)letoMoregames{
    NSLog(@"更多游戏");
    [MGCMiniGameApi mgc_moreGameWithVc:[RootViewController getCurrentInstance]];
    NSLog(@"更多游戏1");
}

//退出游戏
+(void)exitGame
{
    cocos2d::Director::getInstance()->end();
    exit(0);
}

+(void)LaunchWx
{
    // BOOL haveWx = [WXApi isWXAppInstalled];
    // if(haveWx){
        NSLog(@"have wx app");
        [WXApi registerApp:@"wx9b0b68b4192624fa"];
        [WXApi openWXApp];
        // return true;
    // }
    // else{
    //     NSLog(@"no wx app");
        
    //     return false;
    // }
    
}
//talkingDataOnClick
//发送自定义事件
+(void)openH5Game:(NSString*)url
{
    NSLog(@"openH5Game111==");
    NSLog(@"url:%@", url);
    
    [[AppController getCurrentInstance] initWKWebViews:url];
//    [self initWKWebViews:url];
    
}
-(void)initWKWebViews:(NSString*)url
{
    if(self.wkWebView==nil){
    //NSLog(@"openH5Game111=6=");
    WKWebViewConfiguration *configuration = [[WKWebViewConfiguration alloc] init];
        
    WKUserContentController *userContentController = [[WKUserContentController alloc] init];
    NSString *jSString = @"var meta = document.createElement('meta'); meta.setAttribute('name', 'viewport'); meta.setAttribute('content', 'width=device-width, initial-scale=1.0, viewport-fit=cover'); document.getElementsByTagName('head')[0].appendChild(meta);";
    WKUserScript *wkUserScript = [[WKUserScript alloc] initWithSource:jSString injectionTime:WKUserScriptInjectionTimeAtDocumentEnd forMainFrameOnly:YES];
     // 添加自适应屏幕宽度js调用的方法
    [userContentController addUserScript:wkUserScript];
    configuration.userContentController = userContentController;
        
    WKPreferences *preferences = [WKPreferences new];
    preferences.javaScriptCanOpenWindowsAutomatically = YES;
       //设定最小字体
//    preferences.minimumFontSize = 50.0;
        
    configuration.preferences = preferences;
        
        
        
    //configuration.preferences.minimumFontSize = 10;
    //configuration.preferences.javaScriptEnabled = true;
    //configuration.preferences.javaScriptCanOpenWindowsAutomatically = true;
        CGRect s = CGRectMake(0,0,viewController.view.frame.size.width + viewController.view.safeAreaInsets.left + viewController.view.safeAreaInsets.right,viewController.view.frame.size.height + viewController.view.safeAreaInsets.bottom);
        
        
    
    self.wkWebView = [[WKWebView alloc] initWithFrame:s  configuration:configuration];
    
    //[configuration.userContentController addScriptMessageHandler:this name:@"ShowMessageFromWKWebView"];//注册ShowMessageFromWKWebView被js调用的方法
    
    self.wkWebView.UIDelegate = self;
    self.wkWebView.navigationDelegate = self;
    self.wkWebView.allowsBackForwardNavigationGestures = YES;
    self.wkWebView.opaque = NO;
    self.wkWebView.backgroundColor = [UIColor clearColor];
        
    
    
    self.wkWebView.scrollView.scrollEnabled = NO;
    self.wkWebView.multipleTouchEnabled = NO;
    //
    self.wkWebView.scrollView.showsVerticalScrollIndicator = YES;
    self.wkWebView.scrollView.maximumZoomScale = 1.0;
    self.wkWebView.scrollView.minimumZoomScale = 1.0;
    
    [self setupWebViewJavaScriptBridge:self.wkWebView];
    [viewController.view addSubview:self.wkWebView];
        
    NSString *pStr = [NSString stringWithFormat:url, @([[NSDate date] timeIntervalSinceReferenceDate])];
    pStr = [pStr stringByAddingPercentEscapesUsingEncoding:NSUTF8StringEncoding];
    [self.wkWebView loadRequest:[NSURLRequest requestWithURL:[NSURL URLWithString:pStr] cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:30]];
    }
}
-(void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler
{
    
    // WKWebView默认拦截scheme 需在下面方法手动打开
    // 打开外部应用 Safari等操作
    if ([navigationAction.request.URL.absoluteString hasPrefix:@"opennmmj"]) { // 对应的scheme
        NSLog(@"opennmmj111111111111删除wkwebview");
        [self.wkWebView removeFromSuperview];
        self.wkWebView = nil;
        
       
        std::string event ="removeWKWebView";
        std::string funName ="cc.eventManager.dispatchCustomEvent";
        std::string rStr = funName + "(\"" + event + "\");";
        ScriptingCore::getInstance()->evalString(rStr.c_str());
//      [[UIApplication sharedApplication] openURL:navigationAction.request.URL];
    }
    WKNavigationActionPolicy actionPolicy = WKNavigationActionPolicyAllow;
    NSString*urlString = navigationAction.request.URL.absoluteString;
    
    urlString = [urlString stringByRemovingPercentEncoding];
    NSLog(@"urlString11111111111111111111111111111:%@", urlString);
    if ([urlString containsString:@"weixin:"]) {
        actionPolicy =WKNavigationActionPolicyCancel;
        //解决wkwebview weixin://无法打开微信客户端的处理
        NSURL*url = [NSURL URLWithString:urlString];
        if ([[UIApplication sharedApplication]respondsToSelector:@selector(openURL:options:completionHandler:)]) {
            [[UIApplication sharedApplication] openURL:url options:@{UIApplicationOpenURLOptionUniversalLinksOnly: @NO} completionHandler:^(BOOL success) {
            }];
        } else {
            [[UIApplication sharedApplication]openURL:webView.URL];
        }
    }
    if(navigationAction.targetFrame == nil)
    {
        [webView loadRequest:navigationAction.request];
    }
    decisionHandler(WKNavigationActionPolicyAllow);
}
- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message {
    
    NSLog(@"body:%@", message.body);
    
    if ([message.name isEqualToString:@"ShowMessageFromWKWebView"]) {
        [self showMessageWithParams:message.body];
    }
}
//注册被js调用的方法
- (void)showMessageWithParams:(NSDictionary *)dict {
    
    if (![dict isKindOfClass:[NSDictionary class]]) {
        return;
    }
    
    NSString *messageStr = [dict objectForKey:@"message"];
    NSString *titleStr = [dict objectForKey:@"title"];
    NSLog(@"title:%@", titleStr);
    NSLog(@"messageStr:%@", messageStr);
    
    // do it
    
    // 将结果返回给js  js端方法showMessageFromWKWebViewResult
    NSString *returnJSStr = [NSString stringWithFormat:@"showMessageFromWKWebViewResult('%@')", @"message传到OC成功，message传到OC成功，message传到OC成功，message传到OC成功，message传到OC成功，message传到OC成功"];
    [self.wkWebView evaluateJavaScript:returnJSStr completionHandler:^(id _Nullable result, NSError * _Nullable error) {
        
        NSLog(@"%@,%@", result, error);
    }];
}

-(void) setupWebViewJavaScriptBridge:(WKWebView*)webView
{
    
    //[WebViewJavascriptBridge enableLogging];
    //
    _bridge = [WebViewJavascriptBridge bridgeForWebView:webView];
    //
    [_bridge setWebViewDelegate:self];
    
    //
    [_bridge registerHandler:@"testObjcCallback" handler:^(id data, WVJBResponseCallback responseCallback) {
        NSLog(@"testObjcCallback called: %@", data);
        responseCallback(@"Response from testObjcCallback");
    }];
    
    [_bridge callHandler:@"testJavascriptHandler" data:@{ @"foo":@"before ready" }];
    
    //
    id data = @{ @"greetingFromObjC": @"Hi there, JS!" };
    [_bridge callHandler:@"testJavascriptHandler" data:data responseCallback:^(id response) {
        NSLog(@"testJavascriptHandler responded: %@", response);
    }];
    
    //console.log
    [_bridge registerHandler:@"NsLog" handler:^(id data, WVJBResponseCallback responseCallback) {
        NSLog(@"console.log: %@", data);
        //responseCallback(@"Response from NsLog");
    }];
    //console.warn
    [_bridge registerHandler:@"NsWarn" handler:^(id data, WVJBResponseCallback responseCallback) {
        NSLog(@"console.warn: %@", data);
        //responseCallback(@"Response from NsWarn");
    }];
    //console.error
    [_bridge registerHandler:@"NsError" handler:^(id data, WVJBResponseCallback responseCallback) {
        NSLog(@"console.error: %@", data);
        //responseCallback(@"Response from NsError");
    }];
    //
    
    //
    
    
    
    //
    
}
-(NSString *)URLDecodedString:(NSString *)str
{
    NSString *decodedString=(__bridge_transfer NSString *)CFURLCreateStringByReplacingPercentEscapesUsingEncoding(NULL, (__bridge CFStringRef)str, CFSTR(""), CFStringConvertNSStringEncodingToEncoding(NSUTF8StringEncoding));
    
    return decodedString;
}

-(BOOL)webView:(UIWebView *)webView shouldStartLoadWithRequest:(NSURLRequest *)request navigationType:(UIWebViewNavigationType)navigationType
{
    return [self shouldStartLoadWithRequest:request];
}
- (void)webView:(WKWebView *)webView didFinishNavigation:(null_unspecified WKNavigation *)navigation {
   
    /*
    CGRect frame = [[UIScreen mainScreen] bounds] ;
    CGSize sizeL = [[UIScreen mainScreen] currentMode].size;
//    CGSize size = frame.size;
    CGFloat width = sizeL.width;
    CGFloat height = sizeL.height;
    frame.size = sizeL;
    NSLog(@"frame.size.height=width=:%f",width);
    NSLog(@"frame.size.height=height=:%f",height);
    self.wkWebView.frame = frame;
    [self.wkWebView sizeToFit];
     */
   /*
    //  加载完成
    CGRect frame = [[UIScreen mainScreen] bounds] ;
    
    
    CGRect rect = viewController.view.frame;
    CGSize size = rect.size;
    CGFloat width = size.width;
    CGFloat height = size.height;
    NSLog(@"frame.size.height=width=:%f",width);
    NSLog(@"frame.size.height=height=:%f",height);
    
    
    frame.size.height =  1242;//self.wkWebView.scrollView.contentSize.height;
    frame.size.width = 2688;//self.wkWebView.scrollView.contentSize.width;
    NSLog(@"frame.size.height==:%f",frame.size.height);
    NSLog(@"frame.size.width==:%f",frame.size.width);
    self.wkWebView.frame = frame;
    [self.wkWebView sizeToFit];
    */
    
}
-(BOOL) shouldStartLoadWithRequest:(NSURLRequest *)request
{
    //BOOL resultBOOL = YES;
    BOOL bret = YES;
    NSURL* url = [request URL];
    NSString* scheme = [url scheme];
    //NSString* baseurl = ;
    NSString* query = [url query];//?time=22223434
    NSString* nsabstr = [url absoluteString];
    NSString* absoluteString = [self URLDecodedString: nsabstr];
    NSString* relativeString = [url relativeString];
    //
    NSLog(@"start request:%@", absoluteString);
    
    std::string urlstr = [absoluteString UTF8String];
    std::string schemestr = [scheme UTF8String];
    //std::string::size_type idx = urlstr.find(schemestr);
    std::string msg = urlstr.substr(schemestr.length()+3);//http + ://
    //
    
    //
    {
        NSDictionary *headers = [request allHTTPHeaderFields];
        
        NSString* referer = [headers objectForKey:@"Referer"];
        
        if (referer != nil) {
            if (![referer isEqualToString:@"qmby.feefoxes.com"]) {
                
                NSMutableURLRequest* newRequest = [NSMutableURLRequest requestWithURL:url cachePolicy:NSURLRequestUseProtocolCachePolicy timeoutInterval:10.0];
                
                [newRequest setHTTPMethod:@"GET"];
                
                [newRequest setValue:@"qmby.feefoxes.com" forHTTPHeaderField: @"Referer"];
                return NO;
            }
            
            //return YES;
            
        } else {
            
           
        }
        //
        {
            std::string::size_type redirectIdx = urlstr.find("/redirect/");
            if (redirectIdx != std::string::npos) {
                //
                NSString* queryString = [self URLDecodedString: query];//?time=22223434
                std::string querystr = queryString != nil ? [queryString UTF8String] : "";
                redirectIdx = querystr.find("/redirect/");
                if (redirectIdx == std::string::npos) {
                    //最好是从前面加载URL时解析出这个redirect_url具体匹配
                    
                    //
                    return NO;
                }
                //
            }
        }
        //
    }
    
    //test
    //scheme = @"viewurl";
    //msg = "http://www.baidu.com";
    
    //加载Safari打开页面的两种模式，提供特殊接口
    if ([scheme isEqualToString:@"openurl"]) {
        
        NSString* urlstr = [NSString stringWithUTF8String:msg.c_str()];
        NSURL* trueurl = [NSURL URLWithString:urlstr];
        return ![[UIApplication sharedApplication] openURL:trueurl];
        //return NO;
    }
    else if ([scheme isEqualToString:@"viewurl"]) {
        
        NSString* urlstr = [NSString stringWithUTF8String:msg.c_str()];
        NSURL* trueurl = [NSURL URLWithString:urlstr];
        
        //SFSafariViewController * safari =
        //[[SFSafariViewController alloc]initWithURL:trueurl];
        //[self presentViewController:safari animated:YES completion:nil];
        
        return NO;
        //
    }
    else if ([scheme isEqualToString:@"weixin"]) {
        
        //[request setValue:@"http://www.xxx.com" forHTTPHeaderField: @"Referer"];
        
        //[[UIApplication sharedApplication] openURL:url];
        
        //SFSafariViewController * safari =
        //    [[SFSafariViewController alloc]initWithURL:url];
        //[self presentViewController:safari animated:YES completion:nil];
        
        if ([[UIApplication sharedApplication] respondsToSelector:@selector(openURL:options:completionHandler:)]) {
            
            [[UIApplication sharedApplication] openURL:url options:@{UIApplicationOpenURLOptionUniversalLinksOnly: @NO}
                                     completionHandler:^(BOOL success) {
                                         
                                     }];
        } else {
            [[UIApplication sharedApplication]openURL:url];
        }
        
        return NO;
    }
    return bret;
}


static AppController* currentInstance = nil;//游戏中心添加
+ (AppController*)getCurrentInstance{//游戏中心添加
    return currentInstance;//游戏中心添加
}//游戏中心添加


@end


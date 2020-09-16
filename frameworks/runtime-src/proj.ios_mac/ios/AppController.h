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

#import <UIKit/UIKit.h>

@class RootViewController;

@interface AppController : NSObject <UIApplicationDelegate> {

}

@property(nonatomic, readonly) RootViewController* viewController;

#pragma mark - 直播相关
/// 进入直播间
/// @param roomId 直播间ID
+ (void)joinRoom:(NSString *)roomId;

/// 进入用户详情资料页
/// @param userId 用户ID
+ (void)showUserProfile:(NSString *)userId;

/// 退出登录
+ (void)logout;

#pragma mark - 屏幕横竖屏
/// 改变屏幕旋转
/// @param dir横竖屏标志  V：竖屏  其他：横屏
+ (void)setOrientation:(NSString*)dir;

#pragma mark - Pay

/// 支付入口
/// @param payType 支付渠道类型：1:苹果内购 2:支付宝 3:微信
/// @param payParam 支付参数：
/// @param userID 当前支付用户ID
+ (void)appPurchaseWithPayType:(NSInteger)payType payParam:(NSString *)payParam userID:(NSString *)userID;

#pragma mark - Photo
+ (void)selectedOnePhoto;

#pragma mark - QRCode
/// 生成二维码
/// @param codeString 二维码字符串
/// @param centerImage 中心图片
+ (UIImage *_Nonnull)createQRCodeImageWithString:(nonnull NSString *)codeString andCenterImage:(nullable UIImage *)centerImage;

#pragma mark - 广告
/// 打开激励视频
+ (void)openBUAdRewardViewController;

#pragma mark - 获取手机基本信息
+ (NSString *_Nullable)getImei;

#pragma mark - OC调用JS

/// 派发JS事件
/// @param method 方法名
/// @param param 参数
+ (void)dispatchCustomEventWithMethod:(NSString *)method param:(NSString *)param;

@end


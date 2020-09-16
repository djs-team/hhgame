//
//  CXPhoneBasicTools.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXPhoneBasicTools : NSObject
// 获取设备uuid
+ (NSString *)getUUID;

// 获取设备广告标示符
+ (NSString *)getIdentifierForAdvertising;

// 获取版本号
+ (NSString *)getVersion;

// 获取机型信息
+ (NSString *)deviceName;

// 获取当前连接网络
+ (NSString *)getNetWorkStates;
// 获取网络信号
+ (NSInteger)getSignalStrength;
// 获取电量
+ (double)getBatteryLevel;

@end

NS_ASSUME_NONNULL_END

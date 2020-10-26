//
//  CXPhoneBasicTools.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import "CXPhoneBasicTools.h"
#import <sys/utsname.h>
#import <AdSupport/AdSupport.h>

#import "Reachability.h"
#import <CoreTelephony/CTCarrier.h>
#import <CoreTelephony/CTTelephonyNetworkInfo.h>

@implementation CXPhoneBasicTools

// 获取设备uuid
+ (NSString *)getUUID {
//    CFUUIDRef puuid = CFUUIDCreate( nil );
//    CFStringRef uuidString = CFUUIDCreateString( nil, puuid );
//    NSString * result = (NSString *)CFBridgingRelease(CFStringCreateCopy( NULL, uuidString));
//    return result;
    
    NSString *identifierForVendor = [[UIDevice currentDevice].identifierForVendor UUIDString];
    return identifierForVendor;
}

// 获取设备广告标示符
+ (NSString *)getIdentifierForAdvertising {
    NSString *identifierForAdvertising = [[[ASIdentifierManager sharedManager] advertisingIdentifier] UUIDString];
    return identifierForAdvertising;
}

// 获取版本号
+ (NSString *)getVersion {
    NSString *string = [[NSBundle mainBundle] pathForResource:@"Info" ofType:@"plist"];
    NSDictionary *dic = [[NSDictionary alloc] initWithContentsOfFile:string];
    NSString *version = [dic objectForKey:@"CFBundleVersion"];
    return version;
}

// 获取机型信息
+ (NSString *)deviceName {
    struct utsname systemInfo;
    uname(&systemInfo);
    NSString *platform = [NSString stringWithCString:systemInfo.machine encoding:NSASCIIStringEncoding];
    
//    if ([platform isEqualToString:@"iPhone1,1"])    return @"iPhone 1G";
//    if ([platform isEqualToString:@"iPhone1,2"])    return @"iPhone 3G";
//    if ([platform isEqualToString:@"iPhone2,1"])    return @"iPhone 3GS";
//    if ([platform isEqualToString:@"iPhone3,1"])    return @"iPhone 4 (GSM)";
//    if ([platform isEqualToString:@"iPhone3,3"])    return @"iPhone 4 (CDMA)";
//    if ([platform isEqualToString:@"iPhone4,1"])    return @"iPhone 4S";
//    if ([platform isEqualToString:@"iPhone5,1"])    return @"iPhone 5 (GSM)";
//    if ([platform isEqualToString:@"iPhone5,2"])    return @"iPhone 5 (CDMA)";
//    if ([platform isEqualToString:@"iPhone5,3"])    return @"iPhone 5C";
//    if ([platform isEqualToString:@"iPhone5,4"])    return @"iPhone 5C";
//    if ([platform isEqualToString:@"iPhone6,1"])    return @"iPhone 5S";
//    if ([platform isEqualToString:@"iPhone6,2"])    return @"iPhone 5S";
//    if ([platform isEqualToString:@"iPhone7,1"])    return @"iPhone 6 Plus";
//    if ([platform isEqualToString:@"iPhone7,2"])    return @"iPhone 6";
//    if ([platform isEqualToString:@"iPhone8,1"])    return @"iPhone 6s";
//    if ([platform isEqualToString:@"iPhone8,2"])    return @"iPhone 6s Plus";
//    if ([platform isEqualToString:@"iPhone8,4"])    return @"iPhone SE";
//    // 日行两款手机型号均为日本独占，可能使用索尼FeliCa支付方案而不是苹果支付
//    if ([platform isEqualToString:@"iPhone9,1"])    return @"iPhone 7";
//    if ([platform isEqualToString:@"iPhone9,2"])    return @"iPhone 7 Plus";
//    if ([platform isEqualToString:@"iPhone9,3"])    return @"iPhone 7";
//    if ([platform isEqualToString:@"iPhone9,4"])    return @"iPhone 7 Plus";
//    if ([platform isEqualToString:@"iPhone10,1"])   return @"iPhone_8";
//    if ([platform isEqualToString:@"iPhone10,4"])   return @"iPhone_8";
//    if ([platform isEqualToString:@"iPhone10,2"])   return @"iPhone_8_Plus";
//    if ([platform isEqualToString:@"iPhone10,5"])   return @"iPhone_8_Plus";
//    if ([platform isEqualToString:@"iPhone10,3"])   return @"iPhone X";
//    if ([platform isEqualToString:@"iPhone10,6"])   return @"iPhone X";
//    if ([platform isEqualToString:@"iPhone11,8"])   return @"iPhone XR";
//    if ([platform isEqualToString:@"iPhone11,2"])   return @"iPhone XS";
//    if ([platform isEqualToString:@"iPhone11,6"])   return @"iPhone XS Max";
//    if ([platform isEqualToString:@"iPhone11,4"])   return @"iPhone XS Max";
//    if ([platform isEqualToString:@"iPhone12,1"])   return @"iPhone 11";
//    if ([platform isEqualToString:@"iPhone12,3"])   return @"iPhone 11 Pro";
//    if ([platform isEqualToString:@"iPhone12,5"])   return @"iPhone 11 Pro Max";
//    if ([platform isEqualToString:@"iPhone12,8"])   return @"iPhone SE2";
//
//    if ([platform isEqualToString:@"iPod1,1"])      return @"iPod Touch 1G";
//    if ([platform isEqualToString:@"iPod2,1"])      return @"iPod Touch 2G";
//    if ([platform isEqualToString:@"iPod3,1"])      return @"iPod Touch 3G";
//    if ([platform isEqualToString:@"iPod4,1"])      return @"iPod Touch 4G";
//    if ([platform isEqualToString:@"iPod5,1"])      return @"iPod Touch (5 Gen)";
//    if ([platform isEqualToString:@"iPad1,1"])      return @"iPad";
//    if ([platform isEqualToString:@"iPad1,2"])      return @"iPad 3G";
//    if ([platform isEqualToString:@"iPad2,1"])      return @"iPad 2 (WiFi)";
//    if ([platform isEqualToString:@"iPad2,2"])      return @"iPad 2";
//    if ([platform isEqualToString:@"iPad2,3"])      return @"iPad 2 (CDMA)";
//    if ([platform isEqualToString:@"iPad2,4"])      return @"iPad 2";
//    if ([platform isEqualToString:@"iPad2,5"])      return @"iPad Mini (WiFi)";
//    if ([platform isEqualToString:@"iPad2,6"])      return @"iPad Mini";
//    if ([platform isEqualToString:@"iPad2,7"])      return @"iPad Mini (GSM+CDMA)";
//    if ([platform isEqualToString:@"iPad3,1"])      return @"iPad 3 (WiFi)";
//    if ([platform isEqualToString:@"iPad3,2"])      return @"iPad 3 (GSM+CDMA)";
//    if ([platform isEqualToString:@"iPad3,3"])      return @"iPad 3";
//    if ([platform isEqualToString:@"iPad3,4"])      return @"iPad 4 (WiFi)";
//    if ([platform isEqualToString:@"iPad3,5"])      return @"iPad 4";
//    if ([platform isEqualToString:@"iPad3,6"])      return @"iPad 4 (GSM+CDMA)";
//    if ([platform isEqualToString:@"iPad4,1"])      return @"iPad Air (WiFi)";
//    if ([platform isEqualToString:@"iPad4,2"])      return @"iPad Air (Cellular)";
//    if ([platform isEqualToString:@"iPad4,4"])      return @"iPad Mini 2 (WiFi)";
//    if ([platform isEqualToString:@"iPad4,5"])      return @"iPad Mini 2 (Cellular)";
//    if ([platform isEqualToString:@"iPad4,6"])      return @"iPad Mini 2";
//    if ([platform isEqualToString:@"iPad4,7"])      return @"iPad Mini 3";
//    if ([platform isEqualToString:@"iPad4,8"])      return @"iPad Mini 3";
//    if ([platform isEqualToString:@"iPad4,9"])      return @"iPad Mini 3";
//    if ([platform isEqualToString:@"iPad5,1"])      return @"iPad Mini 4 (WiFi)";
//    if ([platform isEqualToString:@"iPad5,2"])      return @"iPad Mini 4 (LTE)";
//    if ([platform isEqualToString:@"iPad5,3"])      return @"iPad Air 2";
//    if ([platform isEqualToString:@"iPad5,4"])      return @"iPad Air 2";
//    if ([platform isEqualToString:@"iPad6,3"])      return @"iPad Pro 9.7";
//    if ([platform isEqualToString:@"iPad6,4"])      return @"iPad Pro 9.7";
//    if ([platform isEqualToString:@"iPad6,7"])      return @"iPad Pro 12.9";
//    if ([platform isEqualToString:@"iPad6,8"])      return @"iPad Pro 12.9";
//
//    if ([platform isEqualToString:@"AppleTV2,1"])      return @"Apple TV 2";
//    if ([platform isEqualToString:@"AppleTV3,1"])      return @"Apple TV 3";
//    if ([platform isEqualToString:@"AppleTV3,2"])      return @"Apple TV 3";
//    if ([platform isEqualToString:@"AppleTV5,3"])      return @"Apple TV 4";
//
//    if ([platform isEqualToString:@"i386"])         return @"Simulator";
//    if ([platform isEqualToString:@"x86_64"])       return @"Simulator";

    return platform;
}

// 获取当前连接网络
+ (NSString *)getNetWorkStates {
    NSString *network = @"";
    switch ([[Reachability reachabilityForInternetConnection] currentReachabilityStatus]) {
        case NotReachable:
            network = @"NONE";
            break;
        case ReachableViaWiFi:
            network = @"WIFI";
            break;
        case ReachableViaWWAN:
            network = @"WWAN";
            break;
        default:
            break;
    }
    if ([network isEqualToString:@""]) {
        network = @"NO DISPLAY";
    }
    return network;
}

+ (NSInteger)getSignalStrength{
    
    int signalStrength = 0;
    if (@available(iOS 13.0, *)) {
        UIStatusBarManager *statusBarManager = [UIApplication sharedApplication].keyWindow.windowScene.statusBarManager;
         
        id statusBar = nil;
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wundeclared-selector"
        if ([statusBarManager respondsToSelector:@selector(createLocalStatusBar)]) {
            UIView *localStatusBar = [statusBarManager performSelector:@selector(createLocalStatusBar)];
            if ([localStatusBar respondsToSelector:@selector(statusBar)]) {
                statusBar = [localStatusBar performSelector:@selector(statusBar)];
            }
        }
#pragma clang diagnostic pop
        if (statusBar) {
            if ([[self getNetWorkStates]isEqualToString:@"WIFI"]) {
                id currentData = [[statusBar valueForKeyPath:@"_statusBar"] valueForKeyPath:@"currentData"];
                id wifiEntry = [currentData valueForKeyPath:@"wifiEntry"];
                if ([wifiEntry isKindOfClass:NSClassFromString(@"_UIStatusBarDataIntegerEntry")]) {
                    signalStrength = [[wifiEntry valueForKey:@"displayValue"] intValue];
                }
            } else {
                id currentData = [[statusBar valueForKeyPath:@"_statusBar"] valueForKeyPath:@"currentData"];
                id cellularEntry = [currentData valueForKeyPath:@"cellularEntry"];
                if ([cellularEntry isKindOfClass:NSClassFromString(@"_UIStatusBarDataCellularEntry")]) {
                    signalStrength = [[cellularEntry valueForKey:@"displayValue"] intValue];
                }
            }
        }
    } else {
        if (IS_IPHONE_X) {
            id statusBar = [[UIApplication sharedApplication] valueForKeyPath:@"statusBar"];
            id statusBarView = [statusBar valueForKeyPath:@"statusBar"];
            UIView *foregroundView = [statusBarView valueForKeyPath:@"foregroundView"];
            int signalStrength = 0;
            
            NSArray *subviews = [[foregroundView subviews][2] subviews];
            
            for (id subview in subviews) {
                if ([subview isKindOfClass:NSClassFromString(@"_UIStatusBarWifiSignalView")]) {
                    signalStrength = [[subview valueForKey:@"numberOfActiveBars"] intValue];
                    break;
                } else if ([subview isKindOfClass:NSClassFromString(@"_UIStatusBarPersistentAnimationView")]) { // _UIStatusBarStringView
                    signalStrength = [[subview valueForKey:@"numberOfActiveBars"] intValue];
                    break;
                }
            }
        } else {
            
            UIApplication *app = [UIApplication sharedApplication];
            NSArray *subviews = [[[app valueForKey:@"statusBar"] valueForKey:@"foregroundView"] subviews];
            NSString *dataNetworkItemView = nil;
            int signalStrength = 0;
            
            for (id subview in subviews) {
                
                if([subview isKindOfClass:[NSClassFromString(@"UIStatusBarDataNetworkItemView") class]] && [[self getNetWorkStates] isEqualToString:@"WIFI"] && ![[self getNetWorkStates] isEqualToString:@"NONE"]) {
                    dataNetworkItemView = subview;
                    signalStrength = [[dataNetworkItemView valueForKey:@"_wifiStrengthBars"] intValue];
                    break;
                } else if ([subview isKindOfClass:[NSClassFromString(@"UIStatusBarSignalStrengthItemView") class]] && ![[self getNetWorkStates] isEqualToString:@"WIFI"] && ![[self getNetWorkStates] isEqualToString:@"NONE"]) {
                    dataNetworkItemView = subview;
                    signalStrength = [[dataNetworkItemView valueForKey:@"_signalStrengthRaw"] intValue];
                    break;
                }
            }
        }
    }
    return signalStrength;
}

// 获取电量
+ (double)getBatteryLevel {
    [UIDevice currentDevice].batteryMonitoringEnabled = YES;
    double deviceLevel = [UIDevice currentDevice].batteryLevel;
    return deviceLevel;
}


@end

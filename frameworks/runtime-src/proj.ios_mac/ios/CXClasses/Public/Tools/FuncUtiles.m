//
//  FuncUtiles.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/5.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "FuncUtiles.h"
#import <sys/utsname.h>


NSString * BundleID ()
{
    static NSString * ret = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        ret = [[NSBundle mainBundle] bundleIdentifier];
    });
    return ret;
}


NSString * AppVersion()
{
    static NSString * ret = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        ret = [NSString stringWithFormat:@"%@,%@", [[[NSBundle mainBundle]infoDictionary] objectForKey:@"CFBundleShortVersionString"], [[[NSBundle mainBundle]infoDictionary] objectForKey:@"CFBundleVersion"]];
    });
    return ret;
}


NSString * Platform()
{
    return [NSString stringWithFormat:@"iOS,%@", [[UIDevice currentDevice] systemVersion]];
}


NSString * ChannelID()
{
//#ifdef APP_STORE
    return @"appStore";
//#else
//    return @"web";
//#endif
}


NSString * Device()
{
    struct utsname systemInfo;
    uname(&systemInfo);
    return [NSString stringWithFormat:@"%@", [NSString stringWithCString:systemInfo.machine encoding:NSASCIIStringEncoding]];
}

NSString * ShortVersionString()
{
    return [[[NSBundle mainBundle]infoDictionary] objectForKey:@"CFBundleShortVersionString"];
}

NSString * Version()
{
    return [NSString stringWithFormat:@"%@", [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]];
}

NSString * Identifier()
{
    return [NSString stringWithFormat:@"%@", [[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleIdentifier"]];
}


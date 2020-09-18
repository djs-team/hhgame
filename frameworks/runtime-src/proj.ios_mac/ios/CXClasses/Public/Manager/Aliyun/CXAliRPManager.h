//
//  CXAliRPManager.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/25.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@class UINavigationController;

@interface CXAliRPManager : NSObject

+ (instancetype)sharedInstance;
+ (instancetype)new NS_UNAVAILABLE;
- (instancetype)init NS_UNAVAILABLE;

- (void)startWithSuccess:(void(^)(void))success failure:(void(^)(int code, NSString* reason))failure nav:(UINavigationController*)nav;

@end

NS_ASSUME_NONNULL_END

//
//  CXAppleLoginManager.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/11/18.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSUInteger, AppleLoginType) {
    AppleLoginTypeUnknown = 0,
    AppleLoginTypeSuccessful,
    AppleLoginTypeUserSuccessful,
    AppleLoginTypeFailure
};

@interface CXAppleLoginManager : NSObject

+ (CXAppleLoginManager *)manager;

- (void)appleLoginComplete:(void(^)(NSInteger state, NSString *msg, id data))complete;

@end

NS_ASSUME_NONNULL_END

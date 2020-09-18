//
//  EasemobManager.h
//  CSharp
//
//  Created by 肖迎军 on 2019/7/31.
//  Copyright © 2019 肖迎军. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "EasemobRoomMessage.h"

NS_ASSUME_NONNULL_BEGIN

@class EasemobManager;


extern NSString * const EasemobManagerErrorDomain;


typedef NS_ENUM(NSUInteger, EasemobManagerErrorCode) {
    EasemobManagerErrorCodeUnknow,
    EasemobManagerErrorCodeEasemobBegin,
    EasemobManagerErrorCodeEasemobEnd = 1000,
};


@protocol EasemobManagerDelegate <NSObject>

@optional

- (void)easemob:(EasemobManager*)manager didLoginWithUser:(NSString*)uid;
- (void)easemob:(EasemobManager*)manager user:(NSString*)uid loginError:(NSError*)error;

- (void)easemob:(EasemobManager*)manager didLogoutWithUser:(NSString*)uid;
- (void)easemob:(EasemobManager*)manager user:(NSString*)uid logoutError:(NSError*)error;

- (void)easemob:(EasemobManager *)manager didJoinRoom:(NSString *)roomId;
- (void)easemob:(EasemobManager *)manager joinRoom:(NSString *)roomId error:(NSError*)error;

- (void)easemob:(EasemobManager *)manager didLeaveRoom:(NSString *)roomId;
- (void)easemob:(EasemobManager *)manager leaveRoom:(NSString *)roomId error:(NSError*)error;

- (void)easemob:(EasemobManager *)manager didReceiveRoom:(NSString*)roomId messages:(NSArray<EasemobRoomMessage*>*)messages;

@end


@interface EasemobManager : NSObject

+ (instancetype)new NS_UNAVAILABLE;
+ (instancetype)init NS_UNAVAILABLE;
+ (instancetype)instanceWithAppKey:(NSString*)key;

@property (nonatomic, nullable) id<EasemobManagerDelegate> delegate;

//@property (nonatomic, readonly, nullable) NSString * uid;
//@property (nonatomic, readonly, nullable) NSString * loginUid;
//@property (nonatomic, readonly, nullable) NSString * logoutUid;

//@property (nonatomic, readonly, nullable) NSString * roomId;
//@property (nonatomic, readonly, nullable) NSString * joinRoomId;
//@property (nonatomic, readonly, nullable) NSString * leaveRoomId;

- (BOOL)login:(NSString*)uid;
- (BOOL)logout;

- (BOOL)joinRoom:(NSString*)roomId;
- (BOOL)leaveRoom;
//
//- (BOOL)isLogin;
//- (BOOL)isInRoom;
//
//- (BOOL)isBusy;

- (void)sendMessage:(EasemobRoomMessage*)msg callback:(void(^)(EasemobRoomMessage*msg))callback;

@end

NS_ASSUME_NONNULL_END

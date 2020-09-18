//
//  SocketMessageGetOnlineUserList.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN


@interface SocketMessageGetOnlineUserListResponseOnlineUserUser : NSObject

@property (nonatomic, strong) NSString * HeadImageUrl;
@property (nonatomic, strong) NSString * Name;
@property (nonatomic, strong) NSString * UserId;
@property (nonatomic, strong) NSString * City;
@property (nonatomic, strong) NSString * Age;
@property (nonatomic, strong) NSNumber * Stature;


@end


@interface SocketMessageGetOnlineUserListResponseOnlineUser : NSObject

@property (nonatomic, strong) SocketMessageGetOnlineUserListResponseOnlineUserUser * User;
@property (nonatomic, strong) NSNumber * Sex;
@property (nonatomic, strong) NSNumber * UserLevel;
@property (nonatomic, strong) NSNumber * VipLevel;

@end


@interface SocketMessageGetOnlineUserListResponse : SocketMessageResponse

@property (nonatomic, strong) NSArray<SocketMessageGetOnlineUserListResponseOnlineUser*> * OnlineUsers;

@end


@interface SocketMessageGetOnlineUserList : SocketMessageRequest

@property (nonatomic, strong) SocketMessageGetOnlineUserListResponse * response;

@property (nonatomic, strong) NSNumber * Index;

@property (nonatomic, strong) NSNumber * Sex;

@end

NS_ASSUME_NONNULL_END

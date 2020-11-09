//
//  CXLiveRoomUserProfileView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/24.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomUserProfileView : MMPopupView

@property (nonatomic, strong) SocketMessageGetUserInfoResponse *userInfo;

@property (nonatomic, copy) void (^userProfileActionBlock)(NSInteger tag);

@property (nonatomic, copy) void (^userProfileAvatarActionBlock)(void);

@end

NS_ASSUME_NONNULL_END

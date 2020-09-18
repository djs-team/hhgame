//
//  CXLiveRoomUserProfileJinyanView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/24.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomUserProfileJinyanView : MMPopupView

@property (nonatomic, strong) SocketMessageGetUserInfoResponse *userInfo;

@property (nonatomic, copy) void (^userProfileJinyanActionBlock)(NSInteger tag);

@end

NS_ASSUME_NONNULL_END

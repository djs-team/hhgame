//
//  CXLiveRoomGuardGroupView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/20.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomGuardGroupView : MMPopupView

@property (nonatomic, strong) NSString *userId;

@property (nonatomic, copy) void (^guardGroupViewBlcok)(BOOL isClose);

@end

NS_ASSUME_NONNULL_END

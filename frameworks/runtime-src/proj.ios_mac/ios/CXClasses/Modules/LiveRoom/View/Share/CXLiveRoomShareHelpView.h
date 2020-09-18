//
//  CXLiveRoomShareHelpView.h
//  hairBall
//
//  Created by mahong yang on 2020/5/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MMPopupView.h"

NS_ASSUME_NONNULL_BEGIN

typedef enum : NSUInteger {
    ShareHelp = 0,
    MusicReserve,
} CXLiveRoomShareHelpViewType;

@interface CXLiveRoomShareHelpView : MMPopupView

@property (nonatomic, assign) CXLiveRoomShareHelpViewType shareHelpType;

@property (nonatomic, copy) void (^didSelectedUser)(LiveRoomUser *user);

@end

NS_ASSUME_NONNULL_END

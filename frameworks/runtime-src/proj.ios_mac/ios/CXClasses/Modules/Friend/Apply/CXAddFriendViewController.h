//
//  CXAddFriendViewController.h
//  hairBall
//
//  Created by mahong yang on 2019/10/17.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXAddFriendViewController : CXBaseViewController

@property (nonatomic, copy) NSString *nickname;
@property (nonatomic, copy) NSString *user_id;
@property (nonatomic, copy) NSString *user_avatar;

@property (nonatomic, assign) BOOL isHost; // 是否是红娘

@property (nonatomic, strong) NSString *is_room; // 1:房间内送礼加好友 2:房间外送礼加好友

@end

NS_ASSUME_NONNULL_END

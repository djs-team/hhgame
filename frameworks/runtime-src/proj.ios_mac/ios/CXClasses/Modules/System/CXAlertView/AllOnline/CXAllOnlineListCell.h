//
//  CXAllOnlineListCell.h
//  hairBall
//
//  Created by mahong yang on 2019/11/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXAllOnlineListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UIButton *applyButton;
@property (weak, nonatomic) IBOutlet UIView *alphView;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;

@property (nonatomic, strong) LiveRoomMicroOrder *orderModel;
@property (nonatomic, strong) SocketMessageGetOnlineUserListResponseOnlineUser *onlineUser;

@property (nonatomic, strong) CXUserModel *model;
@property (nonatomic, strong) CXFriendInviteModel *friendModel;

@property (nonatomic, copy) void (^applyActionBlock)(void);

@property (nonatomic, copy) void (^checkUserProfileBlock)(void);

@end

NS_ASSUME_NONNULL_END

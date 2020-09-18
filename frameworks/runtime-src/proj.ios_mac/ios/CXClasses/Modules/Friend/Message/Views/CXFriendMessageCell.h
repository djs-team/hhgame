//
//  CXFriendMessageCell.h
//  hairBall
//
//  Created by mahong yang on 2019/10/28.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXFriendMessageCell : UITableViewCell

@property (nonatomic, copy) void(^avatarTapGestureBlock)(void);

@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *usernameLabel;
@property (weak, nonatomic) IBOutlet UIButton *userSexBtn;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;
@property (weak, nonatomic) IBOutlet UILabel *userMessageLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *unReadCount;
@property (weak, nonatomic) IBOutlet UILabel *stateLabel;
@property (weak, nonatomic) IBOutlet UILabel *onlineLabel;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *stateLabel_widthLayout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *stateLabel_heightLayout;

@property (nonatomic, assign) BOOL isConversation;
@property (strong, nonatomic) CXFriendInviteModel *model;

@end

NS_ASSUME_NONNULL_END

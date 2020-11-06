//
//  CXFriendInviteCell.h
//  hairBall
//
//  Created by mahong yang on 2019/10/28.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXFriendInviteCellAgreeActionBlock)(void);
typedef void(^CXFriendInviteCellRejestActionBlock)(void);

@interface CXFriendInviteCell : UITableViewCell
@property (nonatomic, copy) CXFriendInviteCellAgreeActionBlock agreeActionBlock;
@property (nonatomic, copy) CXFriendInviteCellRejestActionBlock rejestActionBlock;

@property (nonatomic, copy) void(^avatarTapGestureBlock)(void);

@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *user_nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet UILabel *useridLabel;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;
@property (weak, nonatomic) IBOutlet UIButton *agreeButton;
@property (weak, nonatomic) IBOutlet UIButton *rejestButton;
@property (weak, nonatomic) IBOutlet UIButton *fromTipLabel;
@property (weak, nonatomic) IBOutlet UILabel *stateLabel;
@property (weak, nonatomic) IBOutlet UILabel *onlineStateLabel;

@property (nonatomic, strong) CXFriendInviteModel *inviteModel;

@end

NS_ASSUME_NONNULL_END

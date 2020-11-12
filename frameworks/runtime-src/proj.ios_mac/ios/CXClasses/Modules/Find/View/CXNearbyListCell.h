//
//  CXNearbyListCell.h
//  hairBall
//
//  Created by mahong yang on 2019/11/5.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXNearbyListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *stateLabel;
@property (weak, nonatomic) IBOutlet UILabel *messageLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UIButton *followButton;
@property (weak, nonatomic) IBOutlet UIImageView *onlineImage;
@property (weak, nonatomic) IBOutlet UIButton *cityBtn;

@property (nonatomic, copy) void (^followActionBlock)(void);

@property (nonatomic, strong) CXUserModel *model;

@end

NS_ASSUME_NONNULL_END

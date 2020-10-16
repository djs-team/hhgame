//
//  CXLiveRoomGuardianListCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/19.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomGuardianListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *ageBtn;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;
@property (weak, nonatomic) IBOutlet UILabel *numberLabel;
@property (weak, nonatomic) IBOutlet UILabel *expireLabel;
@property (weak, nonatomic) IBOutlet UILabel *endTimeLabel;

@property (nonatomic, strong) CXLiveRoomGuardItemModel *model;
@property (nonatomic, strong) CXUserModel *userModel;


@end

NS_ASSUME_NONNULL_END

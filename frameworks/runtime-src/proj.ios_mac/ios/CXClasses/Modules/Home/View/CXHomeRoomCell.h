//
//  CXHomeRoomCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXHomeRoomModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXHomeRoomCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *room_image;
@property (weak, nonatomic) IBOutlet UILabel *room_nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *room_descLabel;
@property (weak, nonatomic) IBOutlet UIButton *room_stateBtn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *room_stateBtn_widthLayout;
@property (weak, nonatomic) IBOutlet UIImageView *right_cornImage;

@property (weak, nonatomic) IBOutlet UIView *room_lockView;

@property (weak, nonatomic) IBOutlet UIImageView *user_avatarImage;
@property (weak, nonatomic) IBOutlet UILabel *user_nicknameLabel;

@property (nonatomic, nullable) CXHomeRoomModel * model;

@end

NS_ASSUME_NONNULL_END

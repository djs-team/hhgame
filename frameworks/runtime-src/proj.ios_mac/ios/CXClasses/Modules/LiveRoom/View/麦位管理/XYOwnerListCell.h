//
//  XYOwnerListCell.h
//  hairBall
//
//  Created by zyy on 2019/10/31.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
@class LiveRoomUser;
@class LiveRoomMicroOrder;

NS_ASSUME_NONNULL_BEGIN

@interface XYOwnerListCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UILabel *indexLabel;

@property (weak, nonatomic) IBOutlet UIImageView *headerImageV;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;

@property (strong , nonatomic) LiveRoomUser * userModel;

@property (assign , nonatomic) NSInteger indexP;

@property (strong , nonatomic) LiveRoomMicroOrder * order;

@end

NS_ASSUME_NONNULL_END

//
//  CXLiveRoomSeatFansCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/3.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomSeatFansCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIButton *rank_numBtn;
@property (weak, nonatomic) IBOutlet UIImageView *rank_logo;
@property (weak, nonatomic) IBOutlet UILabel *rank_name;
@property (weak, nonatomic) IBOutlet UIButton *rank_ageBtn;
@property (weak, nonatomic) IBOutlet UILabel *rank_descLabel;
@property (weak, nonatomic) IBOutlet UILabel *rank_roseLabel;

@property (nonatomic, strong) CXSocketMessageSeatsFansModel *model;

@end

NS_ASSUME_NONNULL_END

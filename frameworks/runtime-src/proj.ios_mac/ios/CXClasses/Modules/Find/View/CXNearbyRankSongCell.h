//
//  CXNearbyRankSongCell.h
//  hairBall
//
//  Created by mahong yang on 2020/4/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXNearbyRankSongCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *bgImage;
@property (weak, nonatomic) IBOutlet UIButton *rank_numBtn;
@property (weak, nonatomic) IBOutlet UIImageView *rank_logo;
@property (weak, nonatomic) IBOutlet UILabel *rank_name;
@property (weak, nonatomic) IBOutlet UILabel *rank_desc;
@property (weak, nonatomic) IBOutlet UILabel *rank_roseLabel;
@property (nonatomic, strong) CXUserModel *model;

@end

NS_ASSUME_NONNULL_END

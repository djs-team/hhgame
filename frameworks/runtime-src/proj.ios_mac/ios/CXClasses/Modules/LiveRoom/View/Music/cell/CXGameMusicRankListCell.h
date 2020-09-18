//
//  CXGameMusicRankListCell.h
//  hairBall
//
//  Created by mahong yang on 2020/4/15.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXSocketMessageMusic.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicRankListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIButton *numBtn;
@property (weak, nonatomic) IBOutlet UIImageView *tagImage;
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet UILabel *roseLabel;

@property (nonatomic, strong) CXSocketMessageMusicRankModel *model;

@end

NS_ASSUME_NONNULL_END

//
//  CXGameMusicReserveMusicCell.h
//  hairBall
//
//  Created by mahong yang on 2020/2/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicReserveMusicCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *rankLabel;
@property (weak, nonatomic) IBOutlet UILabel *music_titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *music_dianboLabel;
@property (weak, nonatomic) IBOutlet UILabel *music_guistLabe;

@property (nonatomic, copy) void (^reserveMusicActionBlock)(BOOL isTop, BOOL isDelete);

@property (nonatomic, assign) BOOL isEnableDelete;

@end

NS_ASSUME_NONNULL_END

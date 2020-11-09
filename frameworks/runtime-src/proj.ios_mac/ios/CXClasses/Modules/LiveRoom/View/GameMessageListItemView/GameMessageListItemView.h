//
//  GameMessageListItemView.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageListItemView : UICollectionViewCell

@property (nonatomic, strong) YYLabel *textLabel;

@property (nonatomic, strong) UIImageView *avatar;
@property (nonatomic, strong) UIImageView *avatar_bgImageView;

@property (nonatomic, strong) UIImageView *guard_avatar;
@property (nonatomic, strong) UIImageView *guard_bgImageView;

@property (nonatomic, strong) NSNumber *guardValue; // 0 不是守护 1: 普通守护 2:榜一守护

@property (nullable, nonatomic) id __kindof model;
@property (nonatomic, copy) void(^clickUserInfo)(NSString *userID);

@end

NS_ASSUME_NONNULL_END

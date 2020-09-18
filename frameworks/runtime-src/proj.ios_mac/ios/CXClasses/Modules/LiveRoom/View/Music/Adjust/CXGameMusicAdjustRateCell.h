//
//  CXGameMusicAdjustRateCell.h
//  hairBall
//
//  Created by mahong yang on 2020/2/19.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicAdjustRateCell : UICollectionViewCell

@property (nonatomic, strong) NSDictionary *dict;

@property (nonatomic, assign) double currentValue;

@property (nonatomic, copy) void (^adjustRateBlock)(float value);

@end

NS_ASSUME_NONNULL_END

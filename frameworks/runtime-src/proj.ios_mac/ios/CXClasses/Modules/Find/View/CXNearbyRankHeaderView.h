//
//  CXNearbyRankHeaderView.h
//  hairBall
//
//  Created by mahong yang on 2020/4/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXNearbyRankHeaderView : UIView

@property (nonatomic, copy) void (^clickActionBlock)(NSInteger tag);

- (void)reloadFirstModel:(CXUserModel *)model1 secondModel:(CXUserModel *)model2 thirdModel:(CXUserModel *)model3 type:(NSInteger)type;

@end

NS_ASSUME_NONNULL_END

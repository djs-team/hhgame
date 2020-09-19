//
//  GameMessageListView.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageListView : UICollectionView

- (void)clearModel;
- (void)addModel:(id)model;

// 点击用户信息
@property (nonatomic, copy) void(^clickUserInfoBlock)(NSString *userID);
// 长按手势
@property (nonatomic, copy) void(^longPressUserInfoBlock)(NSString *userName);

@end

NS_ASSUME_NONNULL_END

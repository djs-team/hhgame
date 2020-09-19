//
//  MuaGiftListView.h
//  hairBall
//
//  Created by shiwei on 2019/7/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXLiveRoomGiftModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface MuaGiftListView : UIView

@property (nonatomic, strong) NSArray<CXLiveRoomGiftModel *> *gift_info;
@property (nonatomic, strong) NSArray<CXLiveRoomGiftModel *> *knapsack_gift;
@property (nonatomic, strong) NSArray *users; // 打赏目标用户

@property (nonatomic, copy) void(^sendGiftBlock)(CXLiveRoomGiftModel *info, NSArray *selectUsers, NSString *count, BOOL IsUseBug);
@property (nonatomic, copy) void(^rechargeBlock)(void);
- (void)setDataWith:(NSInteger)type; // type 1为背包

@end

NS_ASSUME_NONNULL_END

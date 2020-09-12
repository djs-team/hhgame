//
//  MuaTipManager.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import <Foundation/Foundation.h>
#import "MuaGiftListView.h"
#import "CXLiveRoomGiftModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface MuaTipManager : NSObject


/// 送礼弹框
+ (MuaGiftListView*)showGiftViewWithListArray:(NSArray *)gift_list knapsackList:(NSArray *)knapsackList users:(NSArray *)users isAddFriend:(BOOL)isAddFriend rechargeBlock:(void (^)(void))rechargeBlock sendGiftBlock:(void (^)(CXLiveRoomGiftModel *info, NSArray *selelctUsers, NSString *count, BOOL IsUseBag))sendGiftBlock;

@end

NS_ASSUME_NONNULL_END

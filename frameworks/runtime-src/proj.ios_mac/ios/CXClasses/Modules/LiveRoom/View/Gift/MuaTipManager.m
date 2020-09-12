//
//  MuaTipManager.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import "MuaTipManager.h"

@implementation MuaTipManager

+ (MuaGiftListView*)showGiftViewWithListArray:(NSArray *)gift_list knapsackList:(NSArray *)knapsackList users:(NSArray *)users isAddFriend:(BOOL)isAddFriend rechargeBlock:(void (^)(void))rechargeBlock sendGiftBlock:(void (^)(CXLiveRoomGiftModel *info, NSArray *selelctUsers, NSString *count, BOOL IsUseBag))sendGiftBlock {
    
    CGFloat margin = kBottomArea;
    MuaGiftListView *gift = [[MuaGiftListView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 420)];
    gift.gift_info = gift_list;
    gift.knapsack_gift = knapsackList;
    [gift setDataWith:0];
    gift.users = users;
//    if (isAddFriend == YES) {
//        gift.titleLabel.hidden = NO;
//        gift.titleLabel.text = @"相亲房内送礼物，无需确认，自动加好友";
//    } else {
//        gift.titleLabel.hidden = YES;
//    }
    
    gift.rechargeBlock = ^{
        if (rechargeBlock) {
            rechargeBlock();
        }
        [LEEAlert closeWithCompletionBlock:nil];
    };
    gift.sendGiftBlock = ^(CXLiveRoomGiftModel * _Nonnull info, NSArray * _Nonnull selectUsers, NSString * _Nonnull count, BOOL IsUseBug) {
        
        if (sendGiftBlock) {
            sendGiftBlock(info, selectUsers, count, IsUseBug);
        }
//        [LEEAlert closeWithCompletionBlock:nil];
    };
    
    
    [LEEAlert actionsheet].config
    .LeeAddCustomView(^(LEECustomView *custom) {
        
        custom.view = gift;
        
        custom.isAutoWidth = YES;
    })
    .LeeConfigMaxWidth(^CGFloat(LEEScreenOrientationType type) {
        // 这是最大宽度为屏幕宽度 (横屏和竖屏)
        return CGRectGetWidth([[UIScreen mainScreen] bounds]);
    })
    .LeeActionSheetBottomMargin(-kBottomArea)
    .LeeCornerRadius(0.0f)
    .LeeMaxHeight(gift.height)
    .LeeHeaderColor([UIColor clearColor])
    .LeeBackgroundStyleTranslucent(0.5)
    .LeeItemInsets(UIEdgeInsetsMake(0, 0, 0, 0))
    .LeeHeaderInsets(UIEdgeInsetsMake(0, 0, 0, 0))
    .LeeStatusBarStyle(UIStatusBarStyleLightContent)
    .LeeClickBackgroundClose(true)
    .LeeShow();
    return gift;
}


@end

//
//  CXBUAdRewardViewController.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/12.
//

#import "CXBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXBUAdRewardViewController : CXBaseViewController

+ (CXBUAdRewardViewController *)manager;

@property (nonatomic, assign) BOOL isPlaySuccess;

- (void)openAdWithUserId:(NSString *)userId;

@end

NS_ASSUME_NONNULL_END

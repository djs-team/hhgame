//
//  CXMineGuardRenewView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXMineGuardRenewView : MMPopupView

@property (nonatomic, strong) NSString *userId;

@property (nonatomic, copy) void (^guardRenewViewBlock)(void);

@end

NS_ASSUME_NONNULL_END

//
//  CXChangeAgeAlertView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/24.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXChangeAgeAlertView : MMPopupView

@property (nonatomic, copy) void(^backActionBlock)(void);

@end

NS_ASSUME_NONNULL_END

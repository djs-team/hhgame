//
//  CXMineBlockActionView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/28.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXMineBlockActionView : MMPopupView

@property (nonatomic, copy) void (^blockActionBlock)(NSInteger tag);

@end

NS_ASSUME_NONNULL_END

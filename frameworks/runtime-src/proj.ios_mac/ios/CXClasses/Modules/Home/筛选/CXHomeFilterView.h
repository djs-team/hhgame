//
//  CXHomeFilterView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXHomeFilterView : MMPopupView

@property (nonatomic, copy) void(^filterBlock)(NSString *age, NSString *city, NSString *city_two, NSString *city_three);

@end

NS_ASSUME_NONNULL_END

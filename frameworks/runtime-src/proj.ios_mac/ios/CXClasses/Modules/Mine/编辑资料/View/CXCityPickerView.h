//
//  CXCityPickerView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/10/13.
//

#import <MMPopupView/MMPopupView.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXCityPickerView : MMPopupView

@property (nonatomic, copy) void (^cityPickerSureBlock)(NSString *city);

@end

NS_ASSUME_NONNULL_END

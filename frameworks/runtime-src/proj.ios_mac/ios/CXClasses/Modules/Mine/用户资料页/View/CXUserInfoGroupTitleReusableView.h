//
//  CXUserInfoGroupTitleReusableView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXUserInfoGroupTitleReusableView : UICollectionReusableView

@property (nonatomic, strong) UILabel * groupTitle;
@property (nonatomic, strong) UIButton * MoreBtn;

@property (nonatomic, copy) void(^MoreBlock) (void);

@end

NS_ASSUME_NONNULL_END

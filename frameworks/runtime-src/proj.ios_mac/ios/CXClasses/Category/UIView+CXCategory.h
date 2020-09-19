//
//  UIView+CXCategory.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/19.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIView (CXCategory)

@property (nonatomic, assign) CGFloat x;
@property (nonatomic, assign) CGFloat y;
@property (nonatomic,assign) CGFloat left;
@property (nonatomic,assign) CGFloat top;
@property (nonatomic,assign) CGFloat height;
@property (nonatomic,assign) CGFloat right;
@property (nonatomic,assign) CGFloat bottom;
@property (nonatomic,assign) CGFloat width;
@property (nonatomic,assign) CGPoint origin;
@property (nonatomic,assign) CGSize size;
@property (nonatomic,assign) CGFloat centerX;
@property (nonatomic,assign) CGFloat centerY;

@end

@interface UIView (BlocksKit)

- (void)sw_whenTouches:(NSUInteger)numberOfTouches tapped:(NSUInteger)numberOfTaps handler:(void (^)(void))block;


- (void)sw_whenTapped:(void (^)(void))block;
- (void)sw_whenTappedView:(void (^)(UIView *view))block;

- (void)sw_whenDoubleTapped:(void (^)(void))block;


- (void)sw_eachSubview:(void (^)(UIView *subview))block;

@end

NS_ASSUME_NONNULL_END

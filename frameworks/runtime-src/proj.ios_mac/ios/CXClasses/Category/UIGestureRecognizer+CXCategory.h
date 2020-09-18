//
//  UIGestureRecognizer+CXCategory.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/19.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIGestureRecognizer (CXCategory)

+ (id)sw_recognizerWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block delay:(NSTimeInterval)delay;

- (id)sw_initWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block delay:(NSTimeInterval)delay NS_REPLACES_RECEIVER;


+ (id)sw_recognizerWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block;


- (id)sw_initWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block NS_REPLACES_RECEIVER;

@property (nonatomic, copy, setter = sw_setHandler:) void (^sw_handler)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location);

@property (nonatomic, setter = sw_setHandlerDelay:) NSTimeInterval sw_handlerDelay;

- (void)sw_cancel;

@end

NS_ASSUME_NONNULL_END

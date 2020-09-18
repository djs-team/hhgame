//
//  UIGestureRecognizer+CXCategory.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/19.
//

#import <objc/runtime.h>
#import "UIGestureRecognizer+CXCategory.h"

static const void *SWGestureRecognizerBlockKey = &SWGestureRecognizerBlockKey;
static const void *SWGestureRecognizerDelayKey = &SWGestureRecognizerDelayKey;
static const void *SWGestureRecognizerShouldHandleActionKey = &SWGestureRecognizerShouldHandleActionKey;

@interface UIGestureRecognizer (CXCategory)

@property (nonatomic, setter = sw_setShouldHandleAction:) BOOL sw_shouldHandleAction;

- (void)sw_handleAction:(UIGestureRecognizer *)recognizer;

@end

@implementation UIGestureRecognizer (CXCategory)

+ (id)sw_recognizerWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block delay:(NSTimeInterval)delay
{
    return [[[self class] alloc] sw_initWithHandler:block delay:delay];
}

- (id)sw_initWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block delay:(NSTimeInterval)delay
{
    self = [self initWithTarget:self action:@selector(sw_handleAction:)];
    if (!self) return nil;
    
    self.sw_handler = block;
    self.sw_handlerDelay = delay;
    
    return self;
}

+ (id)sw_recognizerWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block
{
    return [self sw_recognizerWithHandler:block delay:0.0];
}

- (id)sw_initWithHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))block
{
    return (self = [self sw_initWithHandler:block delay:0.0]);
}

- (void)sw_handleAction:(UIGestureRecognizer *)recognizer
{
    void (^handler)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location) = recognizer.sw_handler;
    if (!handler) return;
    
    NSTimeInterval delay = self.sw_handlerDelay;
    CGPoint location = [self locationInView:self.view];
    void (^block)(void) = ^{
        if (!self.sw_shouldHandleAction) return;
        handler(self, self.state, location);
    };
    
    self.sw_shouldHandleAction = YES;
    
    if (!delay) {
        block();
        return;
    }
    
    dispatch_time_t popTime = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(delay * NSEC_PER_SEC));
    dispatch_after(popTime, dispatch_get_main_queue(), block);
}

- (void)sw_setHandler:(void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))handler
{
    objc_setAssociatedObject(self, SWGestureRecognizerBlockKey, handler, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (void (^)(UIGestureRecognizer *sender, UIGestureRecognizerState state, CGPoint location))sw_handler
{
    return objc_getAssociatedObject(self, SWGestureRecognizerBlockKey);
}

- (void)sw_setHandlerDelay:(NSTimeInterval)delay
{
    NSNumber *delayValue = delay ? @(delay) : nil;
    objc_setAssociatedObject(self, SWGestureRecognizerDelayKey, delayValue, OBJC_ASSOCIATION_RETAIN_NONATOMIC);
}

- (NSTimeInterval)sw_handlerDelay
{
    return [objc_getAssociatedObject(self, SWGestureRecognizerDelayKey) doubleValue];
}

- (void)sw_setShouldHandleAction:(BOOL)flag
{
    objc_setAssociatedObject(self, SWGestureRecognizerShouldHandleActionKey, @(flag), OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (BOOL)sw_shouldHandleAction
{
    return [objc_getAssociatedObject(self, SWGestureRecognizerShouldHandleActionKey) boolValue];
}

- (void)sw_cancel
{
    self.sw_shouldHandleAction = NO;
}


@end

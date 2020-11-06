//
//  CXOCJSBrigeManager.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/17.
//

#import "CXOCJSBrigeManager.h"

@implementation CXOCJSBrigeManager
static id _manager;
+ (CXOCJSBrigeManager *)manager {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _manager = [[CXOCJSBrigeManager alloc] init];
    });
    
    return _manager;
}

- (void)clear {
    self.wxLoginMethod = nil;
    self.jpushLoginMethod = nil;
    self.openInstallParamMethod = nil;
    self.paySuccessMethod = nil;
    self.BUAdRewardMethod = nil;
}

@end

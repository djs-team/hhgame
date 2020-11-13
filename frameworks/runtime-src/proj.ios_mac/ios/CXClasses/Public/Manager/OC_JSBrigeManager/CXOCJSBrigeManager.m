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
    _wxLoginMethod = nil;
    _jpushLoginMethod = nil;
    _openInstallParamMethod = nil;
    _paySuccessMethod = nil;
    _BUAdRewardMethod = nil;
    _resumeAllMusicMethod = nil;
}

- (void)setWxLoginMethod:(NSString *)wxLoginMethod {
    [self clear];
    _wxLoginMethod = wxLoginMethod;
}
- (void)setJpushLoginMethod:(NSString *)jpushLoginMethod {
    [self clear];
    _jpushLoginMethod = jpushLoginMethod;
}
- (void)setOpenInstallParamMethod:(NSString *)openInstallParamMethod {
    [self clear];
    _openInstallParamMethod = openInstallParamMethod;
}
- (void)setPaySuccessMethod:(NSString *)paySuccessMethod {
    [self clear];
    _paySuccessMethod = paySuccessMethod;
}
- (void)setBUAdRewardMethod:(NSString *)BUAdRewardMethod {
    [self clear];
    _BUAdRewardMethod = BUAdRewardMethod;
}
- (void)setResumeAllMusicMethod:(NSString *)resumeAllMusicMethod {
    [self clear];
    _resumeAllMusicMethod = resumeAllMusicMethod;
}

@end

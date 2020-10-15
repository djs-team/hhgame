//
//  SVGAManager.m
//  hairBall
//
//  Created by 肖迎军 on 2019/8/13.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SVGAManager.h"

@implementation SVGALoadOperator

- (void)dealloc {
    
}

@end

@interface SVGAManager ()

@property SVGAParser * preLoadParser;
@property SVGAParser * loadParser;
@property NSMutableSet<NSString*> * requetURLs;
@property NSMutableSet<NSString*> * completionURLs;

@end

@implementation SVGAManager

+ (instancetype)shared {
    static SVGAManager * _shared = nil;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _shared = [SVGAManager new];
    });
    return _shared;
}

- (instancetype)init {
    if (self = [super init]) {
        _preLoadParser = [SVGAParser new];
        _preLoadParser.enabledMemoryCache = NO;
        _loadParser = [SVGAParser new];
        _loadParser.enabledMemoryCache = YES;
        _requetURLs = [NSMutableSet new];
        _completionURLs = [NSMutableSet new];
    }
    return self;
}

- (void)cache:(NSString*)urlString completionBlock:(void ( ^ _Nonnull )(BOOL isSuccess))completionBlock{
    if (!urlString.length) return;
    NSURL * url = [NSURL URLWithString:urlString];
    if (!url) return;
    if ([_requetURLs containsObject:urlString]) return;
    if ([_completionURLs containsObject:urlString]) return;
    
    __weak typeof (self) wself = self;
    [_requetURLs addObject:urlString];
    [_preLoadParser parseWithURL:url completionBlock:^(SVGAVideoEntity * _Nullable videoItem) {
        [wself.requetURLs removeObject:urlString];
        [wself.completionURLs addObject:urlString];
        completionBlock(YES);
    } failureBlock:^(NSError * _Nullable error) {
        [wself.requetURLs removeObject:urlString];
        completionBlock(NO);
    }];
}

- (BOOL)isCached:(NSString*)urlString {
    return [_completionURLs containsObject:urlString];
}

- (SVGALoadOperator*)load:(NSString*)urlString completionBlock:(void ( ^ _Nonnull )(SVGAVideoEntity * _Nullable videoItem))completionBlock failureBlock:(void ( ^ _Nullable)(NSError * _Nullable error))failureBlock {    
    __block SVGALoadOperator * operator = [SVGALoadOperator new];
    operator.string = urlString;
    operator.canceld = NO;
    __weak typeof (self) wself = self;
    [_loadParser parseWithURL:[NSURL URLWithString:urlString] completionBlock:^(SVGAVideoEntity * _Nullable videoItem) {
        [wself.completionURLs addObject:urlString];
        if (operator && !operator.canceld && completionBlock) {
            completionBlock(videoItem);
        }
        operator = nil;
    } failureBlock:^(NSError * _Nullable error) {
        if (operator && !operator.canceld && failureBlock) {
            failureBlock(error);
        }
        operator = nil;
    }];
    return operator;
}

@end

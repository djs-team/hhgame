//
//  SVGAManager.h
//  hairBall
//
//  Created by 肖迎军 on 2019/8/13.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <SVGAPlayer/SVGA.h>

NS_ASSUME_NONNULL_BEGIN

@interface SVGALoadOperator : NSObject

@property NSString * string;
@property void ( ^ completionBlock )(SVGAVideoEntity * _Nullable videoItem);
@property void ( ^ failureBlock )(NSError * _Nullable error);
@property BOOL canceld;

@end


@interface SVGAManager : NSObject

+ (instancetype)shared;

- (void)cache:(NSString*)urlString completionBlock:(void ( ^ _Nonnull )(BOOL isSuccess))completionBlock;
- (BOOL)isCached:(NSString*)urlString;

- (SVGALoadOperator*)load:(NSString*)urlString completionBlock:(void ( ^ _Nonnull )(SVGAVideoEntity * _Nullable videoItem))completionBlock failureBlock:(void ( ^ _Nullable)(NSError * _Nullable error))failureBlock;

@end

NS_ASSUME_NONNULL_END

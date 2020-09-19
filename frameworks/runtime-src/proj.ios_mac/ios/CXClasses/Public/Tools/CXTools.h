//
//  CXTools.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXTools : NSObject

+ (UIViewController *)currentViewController;


+ (NSMutableArray*)splitArray:(NSArray*)array withSubSize:(int)subSize;

@end

NS_ASSUME_NONNULL_END

//
//  NSDate+CXCategory.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "NSDate+CXCategory.h"

@implementation NSDate (CXCategory)
+ (NSString *)currentTime {
    NSDateFormatter *fmt = [[NSDateFormatter alloc]init];
    fmt.dateFormat = @"yyyyMMddHHmmss";
    NSString *str = [fmt stringFromDate:[NSDate date]];
    return str;
}


@end

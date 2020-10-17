//
//  NSDate+CXCategory.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "NSDate+CXCategory.h"

@implementation NSDate (CXCategory)
+ (NSString *)currentTime {
    return [self currentTimeWithDateFormatter:@"yyyyMMddHHmmss"];
}

+ (NSString *)currentTimeWithDateFormatter:(NSString *)formatter {
    NSDate *currentDate = [NSDate date];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:formatter];
    NSString *dateString = [dateFormatter stringFromDate:currentDate];
    return dateString;
}

+ (NSInteger)currentTimeInterval {
    NSDate *date = [NSDate dateWithTimeIntervalSinceNow:0];
    NSTimeInterval time = [date timeIntervalSince1970]*1000;
    return time;
}

+ (NSString *)getDateStringWithTimestamp:(NSInteger)timestamp formatter:(NSString *)formatter {
    NSTimeInterval time = timestamp/1000;
    NSDate *detailDate = [NSDate dateWithTimeIntervalSince1970:time];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:formatter];
    NSString *currentDateStr = [dateFormatter stringFromDate:detailDate];
    return currentDateStr;
}

+ (NSInteger)getTimestampWithDate:(NSString *)data formatter:(NSString *)formatter {
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:formatter];
    NSDate *tempDate = [dateFormatter dateFromString:data];
    NSTimeInterval time = [tempDate timeIntervalSince1970]*1000;
    return time;
}
@end

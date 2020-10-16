//
//  NSDate+CXCategory.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSDate (CXCategory)
/// 获取当前时间
+ (NSString *)currentTime;
/// 获取当前时间
/// @param formatter 格式
+ (NSString *)currentTimeWithDateFormatter:(NSString *)formatter;
/// 获取当前时间戳
+ (NSInteger)currentTimeInterval;
/// 时间戳转时间
/// @param timestamp 时间戳
/// @param formatter 格式
+ (NSString *)getDateStringWithTimestamp:(NSInteger)timestamp formatter:(NSString *)formatter;
/// 时间转时间戳
/// @param data 时间
/// @param formatter 格式
+ (NSInteger)getTimestampWithDate:(NSString *)data formatter:(NSString *)formatter;

@end

NS_ASSUME_NONNULL_END

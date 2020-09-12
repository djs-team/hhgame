//
//  CXMineTaskModel.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/6.
//

#import "CXMineTaskModel.h"

@implementation CXMineTaskModel
+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"task_name" : @"name",
             @"task_desc": @"desc",
             };
}
@end

@implementation CXMineTaskOnlineModel

@end

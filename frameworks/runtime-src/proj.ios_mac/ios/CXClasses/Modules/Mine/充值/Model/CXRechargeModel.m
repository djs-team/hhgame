//
//  CXRechargeModel.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import "CXRechargeModel.h"

@implementation CXRechargeModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"guard_id" : @"id",
             @"charge_id" : @[@"charge_id", @"chargeid"],
             };
}

@end

@implementation CXConsumeRecordModel

@end


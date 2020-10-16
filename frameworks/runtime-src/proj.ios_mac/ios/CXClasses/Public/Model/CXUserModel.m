//
//  CXUserModel.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import "CXUserModel.h"
@implementation CXUserCurrentRoomModel
@end

@implementation CXUserModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{@"user_id": @[@"userid", @"user_id", @"userId", @"id"],
             @"is_vip" : @[@"is_vip", @"isvip", @"isVip", @"vip_lv"],
             @"room_id" : @[@"room_id", @"roomid"],
             @"isBlocks" : @[@"is_blocks",@"isBlocks"],
             };
}

- (UIImage *)sexImage {
    if (_sex.integerValue == 1) {
        return [UIImage imageNamed:@"nan2"];
    } else {
        return [UIImage imageNamed:@"nv2"];
    }
}
@end

@implementation CXUserInfoMenusModel

@end

@implementation CXUserInfoMenusDataMenusModel : NSObject

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"menus" : CXUserInfoMenusModel.class,
             };
}

@end

@implementation CXUserInfoMenusDataModel : NSObject

@end

//
//  CXLiveRoomGiftModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/7.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, GiftType) {
    GiftTypeNormal = 1,
    GiftTypeAnimate = 2,
    GiftTypeFree = 3,
};

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomGiftModel : NSObject

@property(nonatomic, strong) NSString * animation;
@property(nonatomic, strong) NSNumber * broadcast;
@property(nonatomic, strong) NSString * gift_animation;
@property(nonatomic, strong) NSNumber * gift_coin;
@property(nonatomic, strong) NSNumber * gift_id;
@property(nonatomic, strong) NSString * gift_image;
@property(nonatomic, strong) NSString * gift_name;
@property(nonatomic, strong) NSNumber * gift_number;
@property(nonatomic, strong) NSString * pack_num;
@property GiftType gift_type;
@property (nonatomic, assign) Boolean isSelect;

@end

NS_ASSUME_NONNULL_END

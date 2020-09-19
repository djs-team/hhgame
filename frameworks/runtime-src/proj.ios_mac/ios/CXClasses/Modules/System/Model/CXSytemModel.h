//
//  CXSytemModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/7.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXSytemModel : NSObject

@end

@interface CXInviteMikeModel : NSObject

@property (nonatomic, copy) NSString * roomId;
@property (nonatomic, copy) NSString * hongId;
@property (nonatomic, copy) NSString * hongName;
@property (nonatomic, copy) NSString * guestId;
@property (nonatomic, copy) NSString * guestName;
@property (nonatomic, copy) NSString * free; // 1是免费 2不免费
@property (nonatomic, copy) NSString * hongAvatar;
@property (nonatomic, copy) NSString * guestAvatar;
@property (nonatomic, copy) NSString * sex; // 1男 2女 3保密
@property (nonatomic, copy) NSString * city;
@property (nonatomic, copy) NSString * stature;
@property (nonatomic, copy) NSString * age;
@property (nonatomic, copy) NSString * invite_id;

@property (nonatomic, copy) NSString * micro_level;
@property (nonatomic, copy) NSString * micro_cost;

@end

@interface CXUploadImageTokenModel : NSObject

@property (nonatomic, copy) NSString *AccessKeyId;
@property (nonatomic, copy) NSString *AccessKeySecret;
@property (nonatomic, copy) NSString *Expiration;
@property (nonatomic, copy) NSString *SecurityToken;
@property (nonatomic, copy) NSString *Endpoint;
@property (nonatomic, copy) NSString *BucketName;

@end

NS_ASSUME_NONNULL_END

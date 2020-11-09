//
//  CXSytemModel.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/7.
//

#import "CXSytemModel.h"

@implementation CXSytemModel

@end

@implementation CXInviteMikeModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"invite_id" : @"id",
             };
}
@end

@implementation CXUploadImageTokenModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"AccessKeyId" : @[@"AccessKeyId", @"accessKeyIdOss"],
             @"AccessKeySecret" : @[@"AccessKeySecret", @"accessKeySecretOss"],
             @"Expiration" : @[@"Expiration", @"expiration"],
             @"SecurityToken" : @[@"SecurityToken", @"token"],
             @"Endpoint" : @[@"Expiration", @"endpoint"],
             @"BucketName" : @[@"BucketName", @"bucketName"],
             };
}

- (NSString *)Expiration {
    return self.Endpoint;
}

@end



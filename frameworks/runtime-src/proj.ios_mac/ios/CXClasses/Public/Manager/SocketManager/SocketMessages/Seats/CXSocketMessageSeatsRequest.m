//
//  CXSocketMessageSeatsRequest.m
//  hairBall
//
//  Created by mahong yang on 2020/6/4.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSocketMessageSeatsRequest.h"

@implementation CXSocketMessageSeatsRequest

@end

@implementation CXSocketMessageSeatsFansModel

@end

@implementation CXSocketMessageSeatsFansListResponse

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"MicroRankDatas" : CXSocketMessageSeatsFansModel.class,
             };
}

@end

@implementation CXSocketMessageSeatsFansListRequest

@dynamic response;

- (Class)responseClass {
    return CXSocketMessageSeatsFansListResponse.class;
}
SocketMessageInitMethod(SocketMessageIDSeatFansList)

@end


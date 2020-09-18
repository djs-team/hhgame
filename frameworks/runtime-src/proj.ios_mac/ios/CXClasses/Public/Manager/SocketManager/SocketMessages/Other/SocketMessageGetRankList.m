//
//  SocketMessageGetRankList.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageGetRankList.h"


@implementation SocketMessageGetRankListReponseRankData

@end


@implementation SocketMessageGetRankListReponse

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"UserRankDatas" : [SocketMessageGetRankListReponseRankData class]
             };
}

@end


@implementation SocketMessageGetRankList

@dynamic response;

- (Class)responseClass {
    return SocketMessageGetRankListReponse.class;
}

SocketMessageInitMethod(SocketMessageIDGetRankList)

@end

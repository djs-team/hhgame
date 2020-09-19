//
//  SocketMessageLuckyDraw.m
//  hairBall
//
//  Created by shiwei on 2019/7/30.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageLuckyDraw.h"


@implementation SocketMessageLuckyDrawReponseGift

@end


@implementation SocketMessageLuckyDrawReponse

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"Gifts" : SocketMessageLuckyDrawReponseGift.class
             };
}

@end


@implementation SocketMessageLuckyDraw

@dynamic response;

- (Class)responseClass {
    return SocketMessageLuckyDrawReponse.class;
}

SocketMessageInitMethod(SocketMessageIDLuckyDraw)

@end

@implementation SocketMessageLuckyDrawRecordModel

@end


@implementation SocketMessageLuckyDrawRecordReponse

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"BreakEggRecords" : SocketMessageLuckyDrawRecordModel.class
             };
}

@end


@implementation SocketMessageLuckyDrawRecordRequest

@dynamic response;

- (Class)responseClass {
    return SocketMessageLuckyDrawRecordReponse.class;
}

SocketMessageInitMethod(SocketMessageIDLuckyDrawRecord)

@end

@implementation SocketMessageLuckyDrawAllRecordRequest

@dynamic response;

- (Class)responseClass {
    return SocketMessageLuckyDrawRecordReponse.class;
}

SocketMessageInitMethod(SocketMessageIDLuckyDrawAllRecord)

@end


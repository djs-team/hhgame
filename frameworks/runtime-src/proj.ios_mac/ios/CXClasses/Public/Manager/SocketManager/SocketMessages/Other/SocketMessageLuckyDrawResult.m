//
//  SocketMessageLuckyDrawResultReponse.m
//  hairBall
//
//  Created by shiwei on 2019/7/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageLuckyDrawResult.h"

@implementation SocketMessageLuckyDrawResultGift

@end

@implementation SocketMessageLuckyDrawResult



+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
//             @"Gifts" : SocketMessageLuckyDrawReponseGift.class
            @"Gift" : SocketMessageLuckyDrawReponseGift.class
             };
}

@end

@implementation SocketMessageLuckyDrawResultArgs
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
//             @"Gifts" : SocketMessageLuckyDrawReponseGift.class
            @"Args" : SocketMessageLuckyDrawResult.class
             };
}

@end


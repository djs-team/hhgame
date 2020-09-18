//
//  SocketMessageGiftEvent.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageGiftEvent.h"

@implementation SocketMessageGiftEventGiveGiftDataMicro

- (NSIndexPath*)indexPath {
    return [NSIndexPath indexPathForRow:_Number.integerValue inSection:_Level.integerValue];
}

@end


@implementation SocketMessageGiftEventGiveGiftData

@end


@implementation SocketMessageGiftEventGiftGiver

@end

@implementation SocketMessageGiftData

@end


@implementation SocketMessageGiftEvent

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"GiveGiftDatas" : SocketMessageGiftEventGiveGiftData.class
             };
}

@end

@implementation SocketMessageGiftEventArgs

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"Args" : SocketMessageGiftEvent.class
             };
}

@end

@implementation SocketMessageSendGiftAddFriendResponse

@end

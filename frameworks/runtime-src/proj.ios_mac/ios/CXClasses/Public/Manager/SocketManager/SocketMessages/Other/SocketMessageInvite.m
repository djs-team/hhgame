//
//  SocketMessageInvite.m
//  hairBall
//
//  Created by zyy on 2019/11/4.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageInvite.h"

@implementation SocketMessageInvite

SocketMessageInitMethod(SocketMessageIDInviteUpMirco)

@end

@implementation SocketMessageCost

-(Class)responseClass{
    return SocketMessageUpMricoCost.class;
}

SocketMessageInitMethod(SocketMessageIDReceiveInviteCost)

@end

@implementation SocketMessageReplyInvite

SocketMessageInitMethod(SocketMessageIDReplyInviteUpMirco)

@end

@implementation SocketMessageInviteResponse

@end

@implementation SocketMessageUpMricoCost

@end

//

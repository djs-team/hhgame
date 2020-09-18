//
//  CXSocketMessageSystemRequest.m
//  hairBall
//
//  Created by mahong yang on 2019/12/10.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSocketMessageSystemRequest.h"

@implementation CXSocketMessageSystemRequest

@end

@implementation CXSocketMessageSystemNotification

@end


@implementation CXSocketMessageSystemUpdateRequest

SocketMessageInitMethod(SocketMessageIDUpdate)

@end

@implementation CXSocketMessageSystemShareParamResponse

@end

@implementation CXSocketMessageSystemShareParamRequest
@dynamic response;

- (Class)responseClass {
    return CXSocketMessageSystemShareParamResponse.class;
}

SocketMessageInitMethod(SocketMessageIDSystemShareParam)

@end

@implementation CXSocketMessageSystemShareHelpNotification

@end

@implementation CXSocketMessageSystemSendMessageRequest

SocketMessageInitMethod(SocketMessageIDLiveRoomSendMessage)

@end


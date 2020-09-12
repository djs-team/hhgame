//
//  SocketMessageGetUserInfo.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessageGetUserInfo.h"
@implementation SocketMessageGetDisableMsgTemplatesDisableMsgData

@end

@implementation SocketMessageGetUserInfoResponseUser

- (NSString*)displayId {
    NSString * display = self.PrettyId;
    if (!display) {
        display = self.UserId;
    }
    return display;
}

@end


@implementation SocketMessageGetUserInfoResponse

@end


@implementation SocketMessageGetUserInfo

@dynamic response;

- (Class)responseClass {
    return SocketMessageGetUserInfoResponse.class;
}

SocketMessageInitMethod(SocketMessageIDGetUserPersonalData)

@end

@implementation SocketMessageSetUserIsDisableMsg

SocketMessageInitMethod(SocketMessageIDSetUserIsDisableMsg)

@end

@implementation SocketMessageGetDisableMsgTemplatesResponse

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"DisableMsgs" : [SocketMessageGetDisableMsgTemplatesDisableMsgData class],
             };
}

@end
@implementation SocketMessageGetDisableMsgTemplates
@dynamic response;
- (Class)responseClass {
    return SocketMessageGetDisableMsgTemplatesResponse.class;
}
SocketMessageInitMethod(SocketMessageIDGetDisableMsgTemplates)

@end


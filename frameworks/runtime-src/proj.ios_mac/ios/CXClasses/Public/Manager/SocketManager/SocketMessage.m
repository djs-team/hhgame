//
//  SocketMessage.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

static NSString * SocketMessageErrorCode(NSInteger msgId, NSInteger code);


@implementation SocketMessage

- (NSString*)toString {
    return [self modelToJSONString];
}

@end

@implementation SocketMessageResponse

- (BOOL)isSuccess {
    if (_Success && _Success.integerValue == SocketMessageStatusCodeSuccess) {
        return YES;
    }
    return NO;
}

- (NSString*)desc {
    return SocketMessageErrorCode(self.MsgId, self.Success.integerValue);
}

@end


@implementation SocketMessageRequest

- (Class)responseClass {
    return SocketMessageResponse.class;
}

- (NSString*)toRequestJson {
    return [self modelToJSONString];
}

- (BOOL)noError {
    return _error == nil;
}

@end


@implementation SocketMessageNotification

@end


static NSString * SocketMessageErrorCode(NSInteger msgId, NSInteger code)
{
    switch (msgId) {
        case SocketMessageIDJoinRoom: {     //2
            switch (code) {
                case 3: {
                    return @"房间已被锁";
                }
                    break;
                case 6: {
                    return @"服务器繁忙";
                }
                    break;
                case 7: {
                    return @"房间已关闭";
                }
                    break;
                default:
                    break;
            }
        }
            break;
        case SocketMessageIDJoinSeat: { // 6
            switch (code) {
                case 6:
                    return @"您当前不在麦上"; // 非房主管理员身份不能上麦
                    break;
                case 66: {
                    return @"没有权限";
                }
                    break;
                case 21: {
                    return @"余额不足";
                }
                    break;
                case 12:{
                    return @"已经在队列中了";
                }
                    break;
                case 20:{
                    return @"申请成功";
                }
                    break;
                default:
                    break;
            }
            
        }
            break;
        case SocketMessageIDGroupGift: { // 29
            switch (code) {
                case 20:
                    return @"青少年模式限制";
                    break;
                case 6:
                    return @"余额不足";
                    break;
                default:
                    break;
            }
        }
            
            break;
        case SocketMessageIDLeaveSeat: { // 8
            switch (code) {
                case 2:
                    return @"您当前不在麦上";
                    break;
                    
                default:
                    break;
            }
        }
            break;
        case SocketMessageIDSetUserIsManager: { // 31
            switch (code) {
                case 5:
                    return @"您不是房主";
                    break;
                    
                default:
                    break;
            }
        }
            break;
        case SocketMessageIDEnableMic: {    //11
            switch (code) {
                case 100:
                    return @"没有被禁麦";
                    break;
                    
                default:
                    break;
            }
        }
            break;
        default:
            break;
    }
    return @"请求失败";
}

// 下麦失败 3 权限不足  报下麦

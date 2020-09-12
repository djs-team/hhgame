//
//  EasemobManager.m
//  CSharp
//
//  Created by 肖迎军 on 2019/7/31.
//  Copyright © 2019 肖迎军. All rights reserved.
//

#import "EasemobManager.h"
#import <HyphenateLite/HyphenateLite.h>

NSString * const EasemobManagerErrorDomain = @"com.hehexq.im";


NSError * _Nullable  EMErrorToNSErrro(EMError * error) {
    return [NSError errorWithDomain:EasemobManagerErrorDomain code:error.code userInfo:@{NSLocalizedDescriptionKey:[NSString stringWithFormat:@"%@", error.description]}];
}


@interface EasemobManager() <EMChatManagerDelegate, EMClientDelegate>

@property (nonatomic, nullable) NSString * uid;

@property (nonatomic, nullable) NSString * roomId;

@end


@implementation EasemobManager

static EasemobManager * _instance;

+ (instancetype)instanceWithAppKey:(NSString*)key {
    if (!_instance) {
        _instance = [[EasemobManager alloc] initWithAppKey:key];
    }
    return _instance;
}

- (instancetype)initWithAppKey:(NSString*)key {
    if (self = [super init]) {
        EMOptions *options = [EMOptions optionsWithAppkey:key];
        options.apnsCertName = nil;
        [[EMClient sharedClient] initializeSDKWithOptions:options];
        [[EMClient sharedClient].chatManager addDelegate:self delegateQueue:dispatch_get_main_queue()];
    }
    return self;
}

- (BOOL)login:(NSString*)uid {
    __weak typeof (self) wself = self;
    _uid = uid;
    [[EMClient sharedClient] loginWithUsername:uid password:uid completion:^(NSString *aUsername, EMError *aError) {
        if (aError) {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:user:loginError:)]) {
                [wself.delegate easemob:wself user:aUsername loginError:EMErrorToNSErrro(aError)];
            }
        } else {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:didLoginWithUser:)]) {
                [wself.delegate easemob:wself didLoginWithUser:aUsername];
            }
        }
    }];
    return YES;
}

- (BOOL)logout {
    __weak typeof (self) wself = self;
    [[EMClient sharedClient] logout:YES completion:^(EMError *aError) {
        if (aError) {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:user:logoutError:)]) {
                [wself.delegate easemob:self user:wself.uid logoutError:EMErrorToNSErrro(aError)];
            }
        }
        else {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:didLogoutWithUser:)]) {
                [wself.delegate easemob:self didLogoutWithUser:wself.uid];
            }
        }
    }];
    return YES;
}

- (BOOL)joinRoom:(NSString*)roomId {
    _roomId = roomId;
    __weak typeof (self) wself = self;
    [[EMClient sharedClient].roomManager joinChatroom:roomId completion:^(EMChatroom *aChatroom, EMError *aError) {
        if (aError) {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:joinRoom:error:)]) {
                [wself.delegate easemob:wself joinRoom:roomId error:EMErrorToNSErrro(aError)];
            }
        } else {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:didJoinRoom:)]) {
                [wself.delegate easemob:wself didJoinRoom:roomId];
            }
        }
    }];
    return YES;
}

- (BOOL)leaveRoom {
    __weak typeof (self) wself = self;
    [[EMClient sharedClient].roomManager leaveChatroom:self.roomId completion:^(EMError *aError) {
        if (aError) {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:leaveRoom:error:)]) {
                [wself.delegate easemob:wself leaveRoom:wself.roomId error:EMErrorToNSErrro(aError)];
            }
        }
        else {
            if (wself.delegate && [wself.delegate respondsToSelector:@selector(easemob:didLeaveRoom:)]) {
                [wself.delegate easemob:wself didLeaveRoom:wself.roomId];
            }
        }
    }];
    return YES;
}

- (void)sendMessage:(EasemobRoomMessage*)msg callback:(void(^)(EasemobRoomMessage*msg))callback {
    EMTextMessageBody *body = [[EMTextMessageBody alloc] initWithText:msg.text];
    EMMessage * message = [[EMMessage alloc] initWithConversationID:msg.roomId from:[[EMClient sharedClient] currentUsername] to:msg.roomId body:body ext:[msg.ext modelToJSONObject]];
    message.chatType = EMChatTypeChatRoom;
    
    [[EMClient sharedClient].chatManager sendMessage:message progress:^(int progress) {
        
    } completion:^(EMMessage *message, EMError *err) {
        if (err) {
            msg.error = EMErrorToNSErrro(err);
        } else {
            msg.messageId = message.messageId;
        }
        
        if (callback) callback (msg);
    }];
}

- (void)messagesDidReceive:(NSArray<EMMessage*> *)aMessages {
    NSMutableArray<EasemobRoomMessage*> * array = [NSMutableArray new];
    [aMessages enumerateObjectsUsingBlock:^(EMMessage * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj.conversationId isEqualToString:self.roomId]) {
            switch (obj.chatType) {
                case EMChatTypeChat:
                    break;
                case EMChatTypeGroupChat: {
                    
                }
                    break;
                case EMChatTypeChatRoom: {
                    switch (obj.body.type) {
                        case EMMessageBodyTypeText: {
                            EMTextMessageBody * body = (EMTextMessageBody*)obj.body;
                            
                            EasemobRoomMessage * erm = [EasemobRoomMessage new];
                            erm.messageId = obj.messageId;
                            erm.roomId = obj.conversationId;
                            erm.ext = [EasemobRoomMessageExt new];
                            erm.text = body.text;
                            erm.ext.XYType = [obj.ext objectForKey:@"XYType"];
                            erm.ext.XYEmoji = [EmoticonGetListResponseDataEmoticonListItem modelWithJSON:[obj.ext objectForKey:@"XYEmoji"]];
                            erm.ext.XYUser = [SocketMessageUserJoinRoom modelWithJSON:[obj.ext objectForKey:@"XYUser"]];
            
                            [array addObject:erm];
                            break;
                        }
                        case EMMessageBodyTypeImage: {
                            
                            break;
                        }
                        case EMMessageBodyTypeVideo: {
                            
                            break;
                        }
                        case EMMessageBodyTypeLocation: {
                            
                            break;
                        }
                        case EMMessageBodyTypeVoice: {
                            
                            break;
                        }
                        case EMMessageBodyTypeFile: {
                            
                            break;
                        }
                        case EMMessageBodyTypeCmd: {
                            
                            break;
                        }
                        case EMMessageBodyTypeCustom: {
                            
                            break;
                        }
                    }
                }
                    break;
            }
        }
    }];
    
    if (array.count && self.delegate && [self.delegate respondsToSelector:@selector(easemob:didReceiveRoom:messages:)]) {
        [self.delegate easemob:self didReceiveRoom:self.roomId messages:array];
    }
}


@end

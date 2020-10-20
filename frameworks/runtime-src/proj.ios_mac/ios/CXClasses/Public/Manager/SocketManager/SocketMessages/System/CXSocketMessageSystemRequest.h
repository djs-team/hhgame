//
//  CXSocketMessageSystemRequest.h
//  hairBall
//
//  Created by mahong yang on 2019/12/10.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXSocketMessageSystemRequest : SocketMessageRequest

@end

@interface CXSocketMessageSystemNotification : SocketMessageNotification

@property (nonatomic, strong) NSNumber *Numbers; // 同步用户上麦卡数量

@property (nonatomic, strong) NSNumber *UserId; // 同步用户上麦卡数量

@end

// socket心跳
@interface CXSocketMessageSystemUpdateRequest : SocketMessageRequest

@end
// 心跳回应
@interface CXSocketMessageSystemKeepaLiveRequest : SocketMessageRequest

@end

// 分享参数
@interface CXSocketMessageSystemShareParamResponse : SocketMessageResponse

@property (nonatomic, strong) NSString * WebPageUrl;
@property (nonatomic, strong) NSString * UserName;
@property (nonatomic, strong) NSString * Path;
@property (nonatomic, strong) NSString * Title;
@property (nonatomic, strong) NSString * ImageUrl;

@end

@interface CXSocketMessageSystemShareParamRequest : SocketMessageRequest

@property (nonatomic, strong) CXSocketMessageSystemShareParamResponse * response;

@end

// 广播助力消息
@interface CXSocketMessageSystemShareHelpNotification : SocketMessageNotification

@property NSInteger UserId;
@property (nonatomic, strong) NSString *UserName;
@property NSInteger UserSex;
@property NSInteger TargetId;
@property (nonatomic, strong) NSString *TargetName;
@property NSInteger TargetSex;
@property NSInteger Coin;

@end

// 打字发言
@interface CXSocketMessageSystemSendMessageRequest : SocketMessageRequest

@property (nonatomic, strong) NSString * Msg;

@end


NS_ASSUME_NONNULL_END

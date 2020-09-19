//
//  CXSocketMessageSeatsRequest.h
//  hairBall
//
//  Created by mahong yang on 2020/6/4.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXSocketMessageSeatsRequest : SocketMessageRequest

@end

// 获取麦位粉丝榜
@interface CXSocketMessageSeatsFansModel : NSObject
@property NSInteger UserId;
@property (nonatomic, strong) NSString *UserName;
@property (nonatomic, strong) NSString *UserImage;
@property (nonatomic, strong) NSNumber *Sex;
@property (nonatomic, strong) NSString *Age;
@property (nonatomic, strong) NSString *City;
@property NSInteger Score;
@property (nonatomic, strong) NSString * Stature;
@end

@interface CXSocketMessageSeatsFansListResponse : SocketMessageResponse

@property NSInteger AllPage;
@property NSInteger AllRolse;
@property (nonatomic, copy) NSArray <CXSocketMessageSeatsFansModel *> * MicroRankDatas;

@end

@interface CXSocketMessageSeatsFansListRequest : SocketMessageRequest

@property NSInteger Page;
@property NSInteger Level;
@property NSInteger Number;

@property (nonatomic, strong) CXSocketMessageSeatsFansListResponse * response;

@end


NS_ASSUME_NONNULL_END

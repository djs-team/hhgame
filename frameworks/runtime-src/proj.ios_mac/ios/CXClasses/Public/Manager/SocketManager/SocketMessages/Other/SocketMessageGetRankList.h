//
//  SocketMessageGetRankList.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageGetRankListReponseRankData : NSObject

@property (nonatomic, copy) NSString *Rank;
@property (nonatomic, copy) NSString *UserId;
@property (nonatomic, copy) NSString *HeadImageUrl;
@property (nonatomic, copy) NSString *NickName;
@property (nonatomic, copy) NSString *Sex;
@property (nonatomic, copy) NSString *UserLevel;
@property (nonatomic, copy) NSString *VipLevel;
@property (nonatomic, copy) NSString *DukeLevel;
@property (nonatomic, copy) NSString *Value;

@end


@interface SocketMessageGetRankListReponse : SocketMessageResponse

@property (nonatomic, strong) NSArray<SocketMessageGetRankListReponseRankData*> *UserRankDatas;

@property (nonatomic, assign) NSInteger AllPage;

@end


@interface SocketMessageGetRankList : SocketMessageRequest

@property (nonatomic, strong) SocketMessageGetRankListReponse * response;

/**1，2 财富，心动*/
@property (nonatomic, strong) NSNumber * Type;
/**1，2，3 日周月*/
@property (nonatomic, strong) NSNumber * State;

@property NSInteger Page;

@end

NS_ASSUME_NONNULL_END

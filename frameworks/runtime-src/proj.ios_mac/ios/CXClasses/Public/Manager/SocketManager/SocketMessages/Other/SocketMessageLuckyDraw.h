//
//  SocketMessageLuckyDraw.h
//  hairBall
//
//  Created by shiwei on 2019/7/30.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "SocketMessage.h"
@class SocketMessageGiftData;

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageLuckyDrawReponseGift : NSObject

@property (nonatomic, copy) NSString *Id;
@property (nonatomic, copy) NSString *Count;
@property (nonatomic, copy) NSString *TotalCount;
// 砸蛋获得的玫瑰价值
@property (nonatomic, copy) NSString *TotalCoin;

@property (nonatomic , strong) SocketMessageGiftData * GiftData;

@end


@interface SocketMessageLuckyDrawReponse : SocketMessageResponse
// 锤子当前数量
@property (nonatomic, copy) NSString *Harmmers;
// 砸蛋获得的礼物列表
@property (nonatomic, strong) NSArray<SocketMessageLuckyDrawReponseGift *> *Gifts;

@property (nonatomic, strong) SocketMessageLuckyDrawReponseGift *Gift;

@end


@interface SocketMessageLuckyDraw : SocketMessageRequest

@property (nonatomic, strong) SocketMessageLuckyDrawReponse * response;

@property (nonatomic, strong) NSNumber * Count; // 只能为1或2或5
@property (nonatomic, strong) NSNumber * EggType; // 0为金蛋，1为银蛋

@end

// ================= 砸蛋记录 =================
@interface SocketMessageLuckyDrawRecordModel : NSObject

@property (nonatomic, assign) NSInteger EggType; // 0金蛋 1银蛋
@property (nonatomic, assign) NSInteger Count;
@property (nonatomic, assign) NSInteger Time;
@property (nonatomic, copy) NSString *GiftImage;
@property (nonatomic, copy) NSString *GiftName;

@end


@interface SocketMessageLuckyDrawRecordReponse : SocketMessageResponse

// 砸蛋获得的礼物列表
@property (nonatomic, strong) NSArray<SocketMessageLuckyDrawRecordModel *> *BreakEggRecords;

@end

// 前十条砸蛋记录
@interface SocketMessageLuckyDrawRecordRequest : SocketMessageRequest

@property (nonatomic, strong) SocketMessageLuckyDrawRecordReponse * response;

@end

// 所有砸蛋记录
@interface SocketMessageLuckyDrawAllRecordRequest : SocketMessageRequest

@property (nonatomic, strong) SocketMessageLuckyDrawRecordReponse * response;

@property (nonatomic, assign) NSInteger Page;

@end

NS_ASSUME_NONNULL_END

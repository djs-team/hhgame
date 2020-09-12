//
//  CXSocketMessageTask.h
//  hairBall
//
//  Created by mahong yang on 2019/12/6.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXSocketMessageTaskAwardModel : NSObject
@property int AwardType; // 0为钻石，1为玫瑰，2为道具
@property NSInteger AwardCount;
@property (nonatomic, strong) NSNumber * ItemId;

@end

@interface CXSocketMessageTaskModel : NSObject

@property (nonatomic, strong) NSNumber * TaskId; // 任务Id
@property NSString * TaskName; // 任务名称
@property NSString * TaskDesc; // 任务简介

@property NSInteger PreTaskId; // 条件ID

@property NSInteger TaskType; // 任务类型：0 直接领取
/*
 case 2: //开播（次）
 case 3: //开播（时长）
 case 4: //女嘉宾上麦（时长）
 case 5: //相亲（次）
 case 6: //在线人数
 case 7: //房间里玫瑰花费
 */
@property NSInteger ConditionType; // 条件类型
@property NSInteger Step; // 任务当前进度
@property NSInteger FinishStep; // 任务完成所需进度
@property NSInteger State; // 任务状态（1为进行中，2为已完成，3为失效)

@property BOOL IsTimer; // 是否定时
@property BOOL IsStartTimer; // 是否开始定时



@property (nonatomic, strong) NSString * StepStr;
@property (nonatomic, strong) NSString * FinishStepStr;
@property (nonatomic, strong) NSString * Logo;

@property (nonatomic, strong) NSArray<CXSocketMessageTaskAwardModel*> * Awards;

@end

@interface CXSocketMessageTaskInfo : SocketMessageNotification

@property (nonatomic, strong) NSArray<CXSocketMessageTaskModel*> * TaskItems;

@end

@interface CXSocketMessageTaskUpdateInfo : SocketMessageNotification

@property (nonatomic, strong) NSArray<CXSocketMessageTaskModel*> * TaskInfos;

@end

// 同步任务状态：计时任务，如果完成，调接口去检查完成状态
@interface CXSocketMessageTaskCheckRequest : SocketMessageRequest
@property (nonatomic, strong) NSNumber * TaskId;
@end

// 领取任务奖励
@interface SocketMessageTaskGetAwardResponse : SocketMessageResponse

@property (nonatomic, strong) NSArray<CXSocketMessageTaskModel*> * FinishedTasks;
@property (nonatomic, strong) NSString * Code;

@end

@interface CXSocketMessageTaskGetAwardRequest : SocketMessageRequest

@property (nonatomic, strong) SocketMessageTaskGetAwardResponse * response;

//@property NSArray * TaskIds;
@end

// 直接获取某个任务的奖励
@interface CXSocketMessageTaskGetOneAwardRequest : SocketMessageRequest
@property (nonatomic, strong) SocketMessageTaskGetAwardResponse * response;
@property (nonatomic, strong) NSNumber * TaskId;
@end

// 同步任务完成
@interface CXSocketMessageTaskUpdateFinishTaskInfo : SocketMessageNotification

@property (nonatomic, strong) NSString * TaskName;
@property (nonatomic, strong) NSNumber * AwardType; // 奖励类型  1:玫瑰   2:上麦卡
@property (nonatomic, strong) NSNumber * AwardNum;

@end

NS_ASSUME_NONNULL_END

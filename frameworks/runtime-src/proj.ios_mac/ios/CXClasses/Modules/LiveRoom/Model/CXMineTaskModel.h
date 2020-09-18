//
//  CXMineTaskModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/6.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXMineTaskModel : NSObject
@property (nonatomic, copy) NSString *task_id;
@property (nonatomic, copy) NSString *task_name;
@property (nonatomic, copy) NSString *task_desc;
@property (nonatomic, copy) NSString *step; //当前任务金额
@property (nonatomic, copy) NSString *finishstep; //当前任务需要满足的金额
@property (nonatomic, copy) NSString *state; //任务状态（1为进行中，2为完成，3为失效, 4未领取奖励）
@property (nonatomic, strong) NSNumber *reward; //任务赠送的玫瑰数，0表示没有
@property (nonatomic, copy) NSString *type; //跳转类型
@property (nonatomic, copy) NSString *receive_type; //1:玫瑰 2:上麦卡
@property (nonatomic, copy) NSString *create_time;
@property (nonatomic, copy) NSString *lose_efficacy_time;

@property (nonatomic, copy) NSString *task_type; // 任务类型： 6:新手任务 7:每日任务


@end

@interface CXMineTaskOnlineModel : NSObject

@property (nonatomic, copy) NSString *blind_date_time; // 互动时长，分钟
@property (nonatomic, copy) NSString *up_time; // 互动要求时长，小时
@property (nonatomic, copy) NSString *on_micro_time; //上麦时长，分钟

@end

NS_ASSUME_NONNULL_END

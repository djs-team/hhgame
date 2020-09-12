//
//  CXRechargeModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXRechargeModel : NSObject

@property (nonatomic, strong) NSString * charge_id;//充值ID
@property (nonatomic, strong) NSString * rmb; // 金额
@property (nonatomic, strong) NSString * diamond; // 软件内货币
@property (nonatomic, strong) NSString * present; // 赠送的虚拟币
@property (nonatomic, strong)NSString * coinimg; // 图片
@property (nonatomic, strong) NSString * iosflag; // 标识

@property (nonatomic, assign) BOOL isSelected; // 是否是选择项
@property (nonatomic, assign) BOOL isShowFirstTag; // 是否展示首冲奖励

// 守护
@property (nonatomic, strong) NSString * long_time;// 时长（小时）
@property (nonatomic, strong) NSString * coin; //实际价格
@property (nonatomic, strong) NSString * ori_coin; // 原价
@property (nonatomic, strong) NSString * paytype; // 商品所属充值类型[1]支付宝官方 [2]微信支付
@property (nonatomic, strong) NSString * guard_id; // 对应的守护配置id


@end

@interface CXConsumeRecordModel : NSObject

@property (nonatomic,copy)NSString *userid;
@property (nonatomic,copy)NSString *action;
@property (nonatomic,copy)NSString *addtime;
@property (nonatomic,copy)NSString *coin;
@property (nonatomic,copy)NSString *content;
@property (nonatomic,copy)NSString *type;

@end


NS_ASSUME_NONNULL_END

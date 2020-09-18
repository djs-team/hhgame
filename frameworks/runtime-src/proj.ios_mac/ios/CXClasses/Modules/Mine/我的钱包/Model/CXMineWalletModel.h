//
//  CXMineWalletModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXMineWalletModel : NSObject

@property (nonatomic, strong) NSNumber *scale; // 兑换比例

@property (nonatomic, strong) NSNumber * diamond; //余额
@property (nonatomic, strong) NSNumber * coin; //玫瑰数量
@property (nonatomic, strong) NSNumber * check; // 正在审核的提现
@property (nonatomic, strong) NSNumber * list_today_price;
@property (nonatomic, strong) NSNumber * list_yes_price;
@property (nonatomic, strong) NSNumber * list_week_price;
@property (nonatomic, strong) NSNumber * list_month_price;

@property (nonatomic, strong) NSNumber * type; //是否显示1:显示 2:不显示
@property (nonatomic, strong) NSNumber * is_cash; //是否显示提现1:显示 2:不显示
@property (nonatomic, strong) NSNumber * exchange_switch; // 是否显示兑换1:显示 2:不显示

@property (nonatomic, strong) NSNumber * balance; //当前余额
@property (nonatomic, strong) NSString * status; //0 已经绑定 1未绑定 2 审核中
@property (nonatomic, strong) NSString * zfb;    //支付宝账号
@property (nonatomic, strong) NSNumber * cash; //最低提现余额
@property (nonatomic, strong) NSNumber * cash_price; //提现手续费
@property (nonatomic, strong) NSNumber * cash_tax; //提现税费

@property (nonatomic, strong) NSNumber * redpack_cash; //最低提现余额
@property (nonatomic, strong) NSNumber * redpack_cash_price; //提现手续费
@property (nonatomic, strong) NSNumber * redpack_num; //提现次数

@property (nonatomic, strong) NSNumber * blue_coin; //蓝玫瑰数量

@end

@interface CXMineWalletCashModel : NSObject

@property (nonatomic, strong) NSString *cashid; // 提现id
@property (nonatomic, strong) NSString *cash; // 提现金额
@property (nonatomic, strong) NSString *status; // 提现状态 0待审核 1审核通过 2审核驳回
@property (nonatomic, strong) NSString *cashtime; // 提现时间
@property (nonatomic, strong) NSString *type; //提现： 1支付宝 2微信 /收入：1:邀请人送礼 2:邀请人收礼 3:红娘收益 4:收到礼物
@property (nonatomic, strong) NSString *apliuserid; // 提现账号
@property (nonatomic, strong) NSString *cash_price; // 该笔提现手续费
@property (nonatomic, strong) NSString *cash_tax; // 该笔提现税费
@property (nonatomic, strong) NSString *real_cash; // 到账金额
@property (nonatomic, strong) NSString *desc; // 驳回备注

@property (nonatomic, strong) NSString *time; // 时间
@property (nonatomic, strong) NSString *initiatorId; // 支付人
@property (nonatomic, strong) NSString *recipientId; // 接收人
@property (nonatomic, strong) NSString *divide; // 收到分成
@property (nonatomic, strong) NSString *coin; // 消耗玫瑰

@property (nonatomic, strong) NSString *value; // 红包金额

@end

@interface CXMineWalletExchangeModel : NSObject

@property (nonatomic, strong) NSString *exchangeid; // 兑换id
@property (nonatomic, strong) NSString *before_diamond; // 兑换前余额
@property (nonatomic, strong) NSString *diamond; // 兑换金额
@property (nonatomic, strong) NSString *after_diamond; // 兑换后余额
@property (nonatomic, strong) NSString *before_coin; // 兑换前玫瑰数
@property (nonatomic, strong) NSString *coin; // 兑换玫瑰数
@property (nonatomic, strong) NSString *after_coin; // 兑换后玫瑰数
@property (nonatomic, strong) NSString *time; // 时间

@end

NS_ASSUME_NONNULL_END

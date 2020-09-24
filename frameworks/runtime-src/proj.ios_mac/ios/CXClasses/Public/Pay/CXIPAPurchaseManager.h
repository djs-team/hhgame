//
//  CXIPAPurchaseManager.h
//  iOS_Purchase
//
//  Created by zhanfeng on 2017/6/6.
//  Copyright © 2017年 zhanfeng. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum : NSUInteger {
    LiveBroadcast = 0, // 直播
    MaJiang, // 麻将
} IPAPurchaseType;

/**
 block

 @param isSuccess 是否支付成功
 @param certificate 支付成功得到的凭证（用于在自己服务器验证）
 @param errorMsg 错误信息
 */
typedef void(^InAppPurchaseResult)(BOOL isSuccess, NSDictionary *param,NSString *errorMsg);

@interface CXIPAPurchaseManager : NSObject
@property (nonatomic, copy)InAppPurchaseResult iapResultBlock;

@property (nonatomic, assign) IPAPurchaseType purchaseType;

//内购注册相关
@property (nonatomic,copy)NSString * order_sn ;//平台订单号
@property (nonatomic,copy)NSString * userid;//游戏用户ID

+ (instancetype)manager;

/**
 启动内购工具
 */
-(void)startManager;

/**
停止内购工具
*/
-(void)stopManager;
/**
 内购支付
 @param productID 内购商品ID
 @param iapResult 结果
 */
-(void)inAppPurchaseWithProductID:(NSString *)productID iapResult:(InAppPurchaseResult)iapResult;


@end

//
//  CXOCJSBrigeManager.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/17.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXOCJSBrigeManager : NSObject

+ (CXOCJSBrigeManager *)manager;

#pragma mark - *JS调用OC，OC要传给JS的监听方法值
@property (nonatomic, strong) NSString *wxLoginMethod; // 微信登录
@property (nonatomic, strong) NSString *jpushLoginMethod; // 极光一键登录

@property (nonatomic, strong) NSString *openInstallParamMethod; // OpenInstall参数

@property (nonatomic, strong) NSString *paySuccessMethod; // 支付成功回调

@property (nonatomic, strong) NSString *BUAdRewardMethod; // 广告看完回调


@end

NS_ASSUME_NONNULL_END

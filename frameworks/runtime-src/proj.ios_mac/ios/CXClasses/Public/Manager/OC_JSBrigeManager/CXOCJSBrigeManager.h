//
//  CXOCJSBrigeManager.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/17.
//

#import <Foundation/Foundation.h>

@interface CXOCJSBrigeManager : NSObject

+ (CXOCJSBrigeManager *)manager;

#pragma mark - *JS调用OC，OC要传给JS的监听方法值
@property (nonatomic, strong) NSString *wxLoginMethod; // 微信登录
@property (nonatomic, strong) NSString *jpushLoginMethod; // 极光一键登录

@property (nonatomic, strong) NSString *openInstallParamMethod; // OpenInstall参数

@property (nonatomic, strong) NSString *paySuccessMethod; // 支付成功回调

@property (nonatomic, strong) NSString *BUAdRewardMethod; // 广告看完回调

@property (nonatomic, strong) NSString *resumeAllMusicMethod; // 恢复麻将音乐播放

- (void)clear;

@end


//
//  CXThirdPayManager.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import "CXThirdPayManager.h"
#import "WXApi.h"
#import <AlipaySDK/AlipaySDK.h>

@interface CXThirdPayManager() <WXApiDelegate>

@property (nonatomic, copy) void(^PaySuccess)(PayCode code);
@property (nonatomic, copy) void(^PayError)(PayCode code);

@end

@implementation CXThirdPayManager

static id _instance;
+ (instancetype)sharedApi {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _instance = [[CXThirdPayManager alloc] init];
    });
    
    return _instance;
}

///微信支付
- (void)wxPayWithPayParam:(NSString *)pay_param
                  success:(void (^)(PayCode code))successBlock
                  failure:(void (^)(PayCode code))failBlock {
    self.PaySuccess = successBlock;
    self.PayError = failBlock;
    
    //解析结果
    NSData * data = [pay_param dataUsingEncoding:NSUTF8StringEncoding];
    NSError *error;
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:data options:NSJSONReadingMutableContainers error:&error];
    
    if(error != nil) {
        failBlock(WXERROR_PARAM);
        return ;
    }
    
    NSString *appid = dic[@"appId"];
    NSString *partnerid = dic[@"mchId"];
    NSString *prepayid = dic[@"prepayId"];
    NSString *package = @"Sign=WXPay";
    NSString *noncestr = dic[@"nonceStr"];
    NSString *timestamp = dic[@"timeStamp"];
    NSString *sign = dic[@"paySign"];
        
    if(![WXApi isWXAppInstalled]) {
        failBlock(WXERROR_NOTINSTALL);
        return ;
    }
    if (![WXApi isWXAppSupportApi]) {
        failBlock(WXERROR_UNSUPPORT);
        return ;
    }
    
    //发起微信支付
    PayReq* req   = [[PayReq alloc] init];
    //微信分配的商户号
    req.partnerId = partnerid;
    //微信返回的支付交易会话ID
    req.prepayId  = prepayid;
    // 随机字符串，不长于32位
    req.nonceStr  = noncestr;
    // 时间戳
    req.timeStamp = timestamp.intValue;
    //暂填写固定值Sign=WXPay
    req.package   = package;
    //签名
    req.sign      = sign;
    [WXApi sendReq:req completion:nil];
    
    //日志输出
    NSLog(@"appid=%@\npartid=%@\nprepayid=%@\nnoncestr=%@\ntimestamp=%ld\npackage=%@\nsign=%@",appid,req.partnerId,req.prepayId,req.nonceStr,(long)req.timeStamp,req.package,req.sign );
}

#pragma mark - 微信回调
// 微信终端返回给第三方的关于支付结果的结构体
- (void)onResp:(BaseResp *)resp
{
    if ([resp isKindOfClass:[PayResp class]])
    {
        switch (resp.errCode) {
            case WXSuccess:
                self.PaySuccess(WXSUCESS);
                break;
                
            case WXErrCodeUserCancel:   //用户点击取消并返回
                self.PayError(WXSCANCEL);
                break;
                
            default:        //剩余都是支付失败
                self.PayError(WXERROR);
                break;
        }
    }
}

#pragma mark 支付宝支付
- (void)aliPayWithPayParam:(NSString *)pay_param
                   success:(void (^)(PayCode code))successBlock
                   failure:(void (^)(PayCode code))failBlock
{
    self.PaySuccess = successBlock;
    self.PayError = failBlock;
    NSString * appScheme =  @"APP Scheme";
    
    [[AlipaySDK defaultService] payOrder:pay_param fromScheme:appScheme callback:^(NSDictionary *resultDic) {
        NSInteger resultCode = [resultDic[@"resultStatus"] integerValue];
        switch (resultCode) {
            case 9000:     //支付成功
                successBlock(ALIPAYSUCESS);
                break;
                
            case 6001:     //支付取消
                failBlock(ALIPAYCANCEL);
                break;
                
            default:        //支付失败
                failBlock(ALIPAYERROR);
                break;
        }
    }];
}

@end

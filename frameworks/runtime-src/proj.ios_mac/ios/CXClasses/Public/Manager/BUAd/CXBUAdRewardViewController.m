//
//  CXBUAdRewardViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/12.
//

#import "CXBUAdRewardViewController.h"

#import <BUAdSDK/BUAdSDK.h>
#import <BUAdSDK/BUAdSDKManager.h>

@interface CXBUAdRewardViewController () <BUNativeExpressRewardedVideoAdDelegate>

@property (nonatomic, strong) BUNativeExpressRewardedVideoAd *rewardedAd;

@end

@implementation CXBUAdRewardViewController

static id _manager;
+ (CXBUAdRewardViewController *)manager {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _manager = [[CXBUAdRewardViewController alloc] init];
    });
    
    return _manager;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

//打开激励视频的方法
- (void)openAdWithUserId:(NSString *)userId; {
    BURewardedVideoModel *model = [[BURewardedVideoModel alloc] init];
    model.userId = userId;
    self.rewardedAd = [[BUNativeExpressRewardedVideoAd alloc] initWithSlotID:BUDAd_SlotID rewardedVideoModel:model];
    self.rewardedAd.delegate = self;
    [self.rewardedAd loadAdData];
}

#pragma mark - BUNativeExpressRewardedVideoAdDelegate

// 视频下载完成
- (void)nativeExpressRewardedVideoAdDidDownLoadVideo:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    [self pbud_logWithSEL:_cmd msg:@""];
    [self.rewardedAd showAdFromRootViewController:[CXTools currentViewController]];
}

// 视频结束的回调
- (void)nativeExpressRewardedVideoAdDidClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"success"];
    [self pbud_logWithSEL:_cmd msg:@""];
}
//
//- (void)nativeExpressRewardedVideoAdDidPlayFinish:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
//    [self dismissViewControllerAnimated:YES completion:nil];
//    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"success"];
//}
//
//- (void)nativeExpressRewardedVideoAd:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *)error {
//    NSLog(@"fail");
//}


//- (void)nativeExpressRewardedVideoAdDidLoad:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAd:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
//    [self pbud_logWithSEL:_cmd msg:[NSString stringWithFormat:@"%@", error]];
//}
//
//- (void)nativeExpressRewardedVideoAdCallback:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd withType:(BUNativeExpressRewardedVideoAdType)nativeExpressVideoType{
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdViewRenderSuccess:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdViewRenderFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd error:(NSError *_Nullable)error {
//    [self pbud_logWithSEL:_cmd msg:[NSString stringWithFormat:@"%@", error]];
//}
//
//- (void)nativeExpressRewardedVideoAdWillVisible:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdDidVisible:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdWillClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
////- (void)nativeExpressRewardedVideoAdDidClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
////    [self pbud_logWithSEL:_cmd msg:@""];
////}
//
//- (void)nativeExpressRewardedVideoAdDidClick:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdDidClickSkip:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdDidPlayFinish:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
//    [self pbud_logWithSEL:_cmd msg:[NSString stringWithFormat:@"%@", error]];
//}
//
//- (void)nativeExpressRewardedVideoAdServerRewardDidSucceed:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd verify:(BOOL)verify {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdServerRewardDidFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd error:(NSError * _Nullable)error {
//    [self pbud_logWithSEL:_cmd msg:[NSString stringWithFormat:@"%@", error]];
//}
//
//- (void)nativeExpressRewardedVideoAdDidCloseOtherController:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd interactionType:(BUInteractionType)interactionType {
//    NSString *str = nil;
//    if (interactionType == BUInteractionTypePage) {
//        str = @"ladingpage";
//    } else if (interactionType == BUInteractionTypeVideoAdDetail) {
//        str = @"videoDetail";
//    } else {
//        str = @"appstoreInApp";
//    }
//    [self pbud_logWithSEL:_cmd msg:str];
//}

#pragma mark - Log
- (void)pbud_logWithSEL:(SEL)sel msg:(NSString *)msg {
    MJRefreshLog(@"SDKDemoDelegate BUNativeExpressRewardedVideoAd In VC (%@) extraMsg:%@", NSStringFromSelector(sel), msg);
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end

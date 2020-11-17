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

//static id _manager;
static CXBUAdRewardViewController *instance;
+ (CXBUAdRewardViewController *)manager {
//    static dispatch_once_t onceToken;
//    dispatch_once(&onceToken, ^{
//        _manager = [[CXBUAdRewardViewController alloc] init];
//    });
//
//    return _manager;
    
    return instance;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    instance = self;
    
    self.view.backgroundColor = [UIColor clearColor];
    
    BURewardedVideoModel *model = [[BURewardedVideoModel alloc] init];
    model.userId = @"11111";
    self.rewardedAd = [[BUNativeExpressRewardedVideoAd alloc] initWithSlotID:BUDAd_SlotID rewardedVideoModel:model];
    self.rewardedAd.delegate = self;
    [self.rewardedAd loadAdData];
    
    self.view.hidden = YES;
}

//打开激励视频的方法
- (void)openAdWithUserId:(NSString *)userId; {
//    if(self.rewardedAd.isAdValid){
        self.view.hidden = NO;
       [self.rewardedAd showAdFromRootViewController:self];
//    }
//    [self.rewardedAd showAdFromRootViewController:self];
}

#pragma mark - BUNativeExpressRewardedVideoAdDelegate

// 视频下载完成
//- (void)nativeExpressRewardedVideoAdDidDownLoadVideo:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//    [self.rewardedAd showAdFromRootViewController:self];
//}

// 视频结束的回调
- (void)nativeExpressRewardedVideoAdDidClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    self.view.hidden = YES;
    if (_isPlaySuccess == YES) {
        [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"0"];
    } else {
        [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"-1"];
    }
    [self.rewardedAd loadAdData];
}

//- (void)nativeExpressRewardedVideoAdDidPlayFinish:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
//    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"-1"];
//}

- (void)nativeExpressRewardedVideoAd:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error; {
    self.view.hidden = YES;
    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"-1"];
}

//- (void)nativeExpressRewardedVideoAdServerRewardDidFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    self.view.hidden = YES;
//    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"-1"];
//}
- (void)nativeExpressRewardedVideoAdViewRenderFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd error:(NSError *_Nullable)error {
    self.view.hidden = YES;
    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"-1"];
}

//- (void)nativeExpressRewardedVideoAdDidLoad:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//- (void)nativeExpressRewardedVideoAdCallback:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd withType:(BUNativeExpressRewardedVideoAdType)nativeExpressVideoType{
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdViewRenderSuccess:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
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
//- (void)nativeExpressRewardedVideoAdDidClick:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}

- (void)nativeExpressRewardedVideoAdDidClickSkip:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd {
    [self pbud_logWithSEL:_cmd msg:@""];
    self.view.hidden = YES;
    [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].BUAdRewardMethod param:@"-1"];
}

- (void)nativeExpressRewardedVideoAdDidPlayFinish:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd didFailWithError:(NSError *_Nullable)error {
    if (error == nil) {
        self.isPlaySuccess = YES;
    } else {
        self.isPlaySuccess = NO;
    }
}
//
//- (void)nativeExpressRewardedVideoAdServerRewardDidSucceed:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd verify:(BOOL)verify {
//    [self pbud_logWithSEL:_cmd msg:@""];
//}
//
//- (void)nativeExpressRewardedVideoAdServerRewardDidFail:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd error:(NSError * _Nullable)error {
//    [self pbud_logWithSEL:_cmd msg:[NSString stringWithFormat:@"%@", error]];
//}

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

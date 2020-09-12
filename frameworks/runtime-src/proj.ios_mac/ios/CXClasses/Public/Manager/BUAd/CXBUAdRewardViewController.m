//
//  CXBUAdRewardViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/12.
//

#import "CXBUAdRewardViewController.h"

#import <BUAdSDK/BURewardedVideoModel.h>
#import <BUAdSDK/BUNativeExpressRewardedVideoAd.h>
#import <BUAdSDK/BUAdSDKManager.h>

@interface CXBUAdRewardViewController () <BUNativeExpressRewardedVideoAdDelegate>

@property (nonatomic,strong) BUNativeExpressRewardedVideoAd *rewardedVideoAd;

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
    
    BURewardedVideoModel *model = [[BURewardedVideoModel alloc] init];
    model.userId = [CXClientModel instance].userId;
    self.rewardedVideoAd=[[BUNativeExpressRewardedVideoAd alloc] initWithSlotID:BUDAd_SlotID rewardedVideoModel:model];
    self.rewardedVideoAd.delegate=self;
    [self.rewardedVideoAd loadAdData];
}

//打开激励视频的方法
- (void)openAd{
   if(self.rewardedVideoAd.isAdValid){
       [self.rewardedVideoAd showAdFromRootViewController:self];
    }
}

//视频结束的回调
- (void)nativeExpressRewardedVideoAdDidClose:(BUNativeExpressRewardedVideoAd *)rewardedVideoAd{
    [self.rewardedVideoAd loadAdData];        //视频结束后，再加载一次广告数据，保证广告的不重复
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

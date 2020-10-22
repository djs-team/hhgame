//
//  CXLiveRoomViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/27.
//

#import "CXLiveRoomViewController.h"
#import "CXBaseWebViewController.h"
#import "CXFriendViewController.h"
#import "CXLiveRoomSetupViewController.h"

#import <AZCategory/UIView+AZGradient.h>

#import "UIView+CXCategory.h"

#import <WXApi.h>
#import "CXAliRPManager.h"

// UIView
#import "CXLiveRoomUIView.h"
#import "GameInputBar.h"
#import "CXLiveRoomShareHelpView.h"
#import "GameSVGAPlayView.h"

// 个人信息
#import "CXLiveRoomUserProfileView.h"
#import "CXLiveRoomUserProfileJinyanView.h"
#import "CXLiveRoomUserProfileReportView.h"

// 守护榜
#import "CXLiveRoomGuardGroupView.h"
#import "CXMineGuardRenewView.h"

// 守护飘窗
#import "CXLiveRoomGuardianAlertView.h"
#import "CXLiveRoomGuardianAnimationView.h"

// 美颜
#import <AGMBase/AGMBase.h>
#import <AGMCapturer/AGMCapturer.h>
#import "FUManager.h"

// Music
#import "CXGameMusicView.h"
#import "CXMusicDownloader.h"
#import "DDAudioLRCParser.h"
#import "CXGameMusicLRCLabel.h"
#import "CXGameMusicLRCFullView.h"
#import "CXGameMusicSongerTipView.h"
#import "CXGameMusicGuideView.h"

// 推荐
#import "CXLiveRoomRecommendView.h"

// 申请上麦
#import "CXLiveRoomBottomApplySeatAlertView.h"

// 麦位管理
#import "CXAllOnlineListViewController.h"
#import "XYRoomAlreadApplyListView.h"
#import "XYOwnerSortListView.h"
#import "CXLiveRoomSeatFansView.h"

// 充值
#import "CXLiveRoomRechargeView.h"
#import "CXLiveRoomRechargeSheepView.h"

// 送礼
#import "MuaTipManager.h"
#import "CXMuaGiftSendBlueRoseView.h"

// 红包
#import "CXRedPacketProgressView.h"
#import "CXRedPacketRegularView.h"
#import "CXRedPacketAwardRecordView.h"
#import "CXRedPacketStartView.h"
#import "GameMessageTextWelcomeModel.h"

// 好友
#import "EMChatViewController.h"
#import "CXAddFriendViewController.h"

@interface CXLiveRoomViewController () <CXClientModelEventListener, AgoraVideoSourceProtocol>

@property (nonatomic, strong) CXLiveRoomUIView *roomUIView;
@property (nonatomic, strong) UIView * coverView;
@property (nonatomic, strong) GameInputBar *inputBar;

@property (nonatomic, strong) GameSVGAPlayView * svgaPlayView;

// 美颜
@property (nonatomic, strong) AGMCameraCapturer *cameraCapturer;
@property (nonatomic, strong) AGMCapturerVideoConfig *videoConfig;
@property (nonatomic, strong) AGMVideoAdapterFilter *videoAdapterFilter;

// 静音麦位userid列表
@property (nonatomic, strong) NSMutableArray *muteArrays;

// 心跳
@property (nonatomic, strong) dispatch_source_t timer;

// Music
@property (nonatomic, assign) NSInteger totalLRCProgress;
@property (nonatomic, strong) CXGameMusicView *musicView;
@property (nonatomic, strong) CXGameMusicLRCFullView *musicLRCFullView;
@property (nonatomic, strong) dispatch_source_t music_timer;
@property (nonatomic, assign) BOOL isShowMusicLRCFullView; // 是否打开了全部歌词

// 申请上麦提示
@property (nonatomic, strong) CXLiveRoomBottomApplySeatAlertView *applySeatAlertView;
@property (nonatomic, strong) NSMutableArray <SocketMessageMicroOrder *> *applySeatArrays; // 申请上麦的人
@property (nonatomic, assign) BOOL applySeatAlertViewIsShow;

@property (nonatomic, assign) BOOL isCloseMineMicro; // 是否关闭了自己的麦克风

// 送礼
@property (nonatomic, strong) NSArray <CXLiveRoomGiftModel *> *giftArrays; // 送礼礼物列表
@property (nonatomic, strong) NSArray <CXLiveRoomGiftModel *> *guardGiftArrays; // 送礼守护礼物列表
@property (nonatomic, strong) NSArray <CXLiveRoomGiftModel *> *firendGiftArrays; // 送礼加好友礼物列表

// 推荐
@property (nonatomic, strong) CXLiveRoomRecommendView *recommendView;

// 红包
@property (nonatomic, strong) CXRedPacketStartView *redpacketStartView;
@property (nonatomic, strong) CXRedPacketProgressView *redpacketProgressView;

@end

@implementation CXLiveRoomViewController
@synthesize consumer;

- (void)leaveRoom {
    kWeakSelf
    [[CXClientModel instance] leaveRoomCallBack:^(NSString * _Nonnull roomId, BOOL success) {
        if (success == YES) {
            [weakSelf deallocRoom];
        } else {
            [weakSelf toast:@"离开房间失败，请重试"];
        }
    }];
}

- (void)deallocRoom {
    
    [CXClientModel instance].isJoinedRoom = NO;
    
    [[CXClientModel instance].listener removeObject:self];
    
    if (_timer) {
        dispatch_source_cancel(_timer);
    }
    if (_music_timer) {
        dispatch_source_cancel(_music_timer);
    }

    [_musicView removeFromSuperview];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self];
    
    [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleAudience];
    [[CXClientModel instance].agoraEngineManager.engine setVideoSource:nil];
    
    [self music_playStop];
    
    [self dismissViewControllerAnimated:YES completion:nil];
    
    [[UIApplication sharedApplication] endReceivingRemoteControlEvents];
        
    [[CXClientModel instance].room.roomMessages removeAllObjects];
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.muteArrays = [NSMutableArray array];
    self.applySeatArrays = [NSMutableArray array];
    
    [self setupSubViews];
    
    [self.roomUIView.messageListView clearModel];
    
    [[CXClientModel instance].listener addObject:self];
    
    [self undoSocketRoomMessage];
    
    // 点击同意进入页面的
    if ([CXClientModel instance].isAgreeInviteJoinRoom == YES) {
        [CXClientModel instance].isAgreeInviteJoinRoom = NO;
        
        [self applyJoinChannel:false level:[CXClientModel instance].currentAgreeInviteMikeModel.micro_level.integerValue];
    }
    
    if ([[[NSUserDefaults standardUserDefaults] valueForKey:@"UesrDefault_faceUnityOpen"] boolValue] == NO) {
        [[FUManager shareManager] loadFilterLandmarksType:FUAITYPE_FACELANDMARKS75];
        [self initCapturer];
        [[CXClientModel instance].agoraEngineManager.engine setVideoSource:self];
    } else {
        [[CXClientModel instance].agoraEngineManager.engine setVideoSource:nil];
        //美颜
        AgoraBeautyOptions *options = nil;
        options = [[AgoraBeautyOptions alloc] init];
        // 亮度明暗对比度
        options.lighteningContrastLevel = [FUManager shareManager].blurShape;
        // 美白
        options.lighteningLevel = [FUManager shareManager].whiteLevel;
        // 平滑度:磨皮
        options.smoothnessLevel = [FUManager shareManager].blurLevel;
        // 红润
        options.rednessLevel = [FUManager shareManager].redLevel;

        [[CXClientModel instance].agoraEngineManager.engine setBeautyEffectOptions:YES options:options];
    }
    
    [self getAudioAuthStatus];
    [self getVideoAuthStatus];
    
    // socket 心跳
    [self socketSystemUpdate];
    
    // 苹果支付
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(actionApplePayFinished) name:@"ApplePayGetReceiptDataRequest" object:nil];
    
    // 监听支付宝支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(aliPayCallBack:) name:kNSNotificationCenter_CXRechargeViewController_alipay object:nil];

    // 监听微信支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(weChatCallBack:) name:kNSNotificationCenter_CXRechargeViewController_weixin object:nil];
    
    [self getGiftListData];
    [self getGuardGiftListData];
    [self getFriendListData];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBarHidden = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBarHidden = NO;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    // 获取好友列表
    [self getFriendListData];
}

- (void)viewDidLayoutSubviews {
    [super viewDidLayoutSubviews];
    
    UIInterfaceOrientation orientation = [UIApplication sharedApplication].statusBarOrientation;
    #define DEGREES_TO_RADIANS(x) (x * M_PI/180.0)
    CGAffineTransform rotation = CGAffineTransformMakeRotation( DEGREES_TO_RADIANS(90));
    switch (orientation) {
        case UIInterfaceOrientationPortrait:
            rotation = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(90));
            break;
        case UIInterfaceOrientationPortraitUpsideDown:
            break;
        case UIInterfaceOrientationLandscapeLeft:
            rotation = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(180));
            break;
        case UIInterfaceOrientationLandscapeRight:
            rotation = CGAffineTransformMakeRotation(DEGREES_TO_RADIANS(0));
            break;

        default:
            break;
    }
    self.videoAdapterFilter.affineTransform = rotation;
}

- (void)setupSubViews {
    
//    [self.view az_setGradientBackgroundWithColors:@[UIColorHex(0xCF429F),UIColorHex(0x381C5D),UIColorHex(0x141261)] locations:@[@0,@0.5,@1] startPoint:CGPointMake(0, 0) endPoint:CGPointMake(0, 1)];
    
    [self.view addSubview:self.roomUIView];
    [_roomUIView mas_remakeConstraints:^(MASConstraintMaker *make) {
           make.edges.mas_offset(0);
       }];
    
    [self setTextViewToolbar];
    
    self.svgaPlayView = [GameSVGAPlayView new];
    [self.view addSubview:self.svgaPlayView];
    [_svgaPlayView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.view);
    }];
    
    [CXClientModel instance].isJoinedRoom = YES;
}

- (void)undoSocketRoomMessage {
    kWeakSelf
    [[CXClientModel instance].room.roomMessages enumerateObjectsUsingBlock:^(SocketMessageNotification *  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [weakSelf modelClient:[CXClientModel instance] didReceiveNotification:obj];
    }];
    
    [[CXClientModel instance].room.roomMessages removeAllObjects];
}

#pragma mark - ==================== 美颜 ========================
- (void)initCapturer {
    // Capturer
    self.videoConfig = [AGMCapturerVideoConfig defaultConfig];
    self.videoConfig.videoSize = CGSizeMake(480, 640);
    self.videoConfig.sessionPreset = AGMCaptureSessionPreset480x640;
    self.videoConfig.fps = 15;
    self.cameraCapturer = [[AGMCameraCapturer alloc] initWithConfig:self.videoConfig];
    
    // Filter
    self.videoAdapterFilter = [[AGMVideoAdapterFilter alloc] init];
    self.videoAdapterFilter.ignoreAspectRatio = YES;
    self.videoAdapterFilter.isMirror = NO;
    // push pixelBuffer
    __weak typeof(self) weakSelf = self;
    [self.cameraCapturer addVideoSink:self.videoAdapterFilter];
//    [[FUManager shareManager] setBeautyDefaultParameters];
    [self.videoAdapterFilter setFrameProcessingCompletionBlock:^(AGMVideoSource * _Nonnull videoSource, CMTime time) {
        CVPixelBufferRef pixelBuffer = videoSource.framebufferForOutput.pixelBuffer;
        [[FUManager shareManager] renderItemsToPixelBuffer:pixelBuffer];
//        if (weakSelf.model.type == FULiveModelTypeMusicFilter) {
//            [[FUManager shareManager] musicFilterSetMusicTime];
//        }
        [weakSelf.consumer consumePixelBuffer:pixelBuffer withTimestamp:time rotation:AgoraVideoRotationNone];
    }];
}

- (BOOL)shouldInitialize {
    return YES;
}

- (void)shouldStart {
    [self.cameraCapturer start];
}

- (void)shouldStop {
    [self.cameraCapturer stop];
}

- (void)shouldDispose {
    
}

- (AgoraVideoBufferType)bufferType {
    return AgoraVideoBufferTypePixelBuffer;
}

#pragma mark - ====================== 心跳 ===========================
- (void)socketSystemUpdate {
    return;
    
    //每隔一分钟执行一次打印
    // GCD定时器
    //设置时间间隔
    NSTimeInterval period = 60.f;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), period * NSEC_PER_SEC, 0);
    // 事件回调
    dispatch_source_set_event_handler(_timer, ^{
        dispatch_async(dispatch_get_main_queue(), ^{
            //网络请求 doSomeThing...
            CXSocketMessageSystemUpdateRequest *request = [CXSocketMessageSystemUpdateRequest new];
            [[CXClientModel instance] sendSocketRequest:request withCallback:nil];
        });
    });
    
    // 开启定时器
    dispatch_resume(_timer);
}

#pragma mark - =========================== Https ================================
//  获取好友列表
- (void)getFriendListData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Friend/getFriendList" parameters:@{@"signature": signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
        
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXFriendInviteModel class] json:responseObject[@"data"][@"list"]];
            NSMutableArray *tempArray = [NSMutableArray array];
            [array enumerateObjectsUsingBlock:^(CXFriendInviteModel *  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                
                [tempArray addObject:obj.user_id];
            }];
            [CXClientModel instance].firendIdArrays = [NSArray arrayWithArray:tempArray];
            [CXClientModel instance].firendArrays = [NSArray arrayWithArray:array];
            [weakSelf.roomUIView.roomSeatsView reloadFirends];
        }
    }];
}

// 获取房间内广告
- (void)getCycleScrollDataListIsFirstCharge:(BOOL)isFirstCharge {
    __weak typeof (self) wself = self;
    NSDictionary *param = @{@"type" : @"2"};
    
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/bannerlist" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (responseObject) {
            NSArray *array = [NSArray modelArrayWithClass:[CXHomeRoomBannerModel class] json:responseObject[@"data"][@"bannerList"]];
            NSMutableArray *tempArray = [NSMutableArray arrayWithArray:array];
            if (isFirstCharge == YES) {
                CXHomeRoomBannerModel *firstItem = [CXHomeRoomBannerModel new];
                firstItem.image = @"live_room_first_charge_banner";
                firstItem.link_type = @"3";
                firstItem.ui_type = @"1001";
                [tempArray insertObject:firstItem atIndex:0];
            }

            wself.roomUIView.bannerList = tempArray;
        }
    }];
}

// 获取礼物列表
- (void)getGiftListData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature" : signature,
        @"type" : @"1",
        @"status" : @"0",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Gift/getList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        
        if (!error) {
            weakSelf.giftArrays = [NSArray modelArrayWithClass:[CXLiveRoomGiftModel class] json:responseObject[@"data"][@"gift_info"]];
        }
    }];
}
// 获取守护礼物列表
- (void)getGuardGiftListData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature" : signature,
        @"type" : @"3",
        @"status" : @"0",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Gift/getList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {

        if (!error) {
            weakSelf.guardGiftArrays = [NSArray modelArrayWithClass:[CXLiveRoomGiftModel class] json:responseObject[@"data"][@"gift_info"]];
        }
    }];
}
// 获取加好友礼物列表
- (void)getFirendGiftListData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature" : signature,
        @"type" : @"1",
        @"status" : @"1",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Gift/getList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {

        if (!error) {
            weakSelf.firendGiftArrays = [NSArray modelArrayWithClass:[CXLiveRoomGiftModel class] json:responseObject[@"data"][@"gift_info"]];
        }
    }];
}

#pragma mark - ==================== CXClientModelEventListener ========================
- (void)modelClient:(CXClientModel *)client didReceiveNotification:(__kindof SocketMessageNotification *)notification {
    if (notification) {
        switch (notification.MsgId) {
            case SocketMessageIDRoomInit:// 初始化
            {
                SocketMessageRoomInit * roomInit = notification;
                
                if ([CXClientModel instance].room.isConsertModel == YES) {
                    if ([CXClientModel instance].room.isSonger) {
                        [self music_playResume];
                    }
                }
                                
                NSIndexPath *seatIndex = [[CXClientModel instance].room.userSeats objectForKey:[CXClientModel instance].userId];
                if (!seatIndex) {
                    [CXClientModel instance].agoraEngineManager.offMic = YES;
                    [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleAudience];
                } else {
                    [CXClientModel instance].agoraEngineManager.offMic = NO;
                    [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleBroadcaster];
                }
                
                [[CXClientModel instance].agoraEngineManager joinRoom:roomInit.ShengwangRoomId withUID:[CXClientModel instance].userId.unsignedIntegerValue success:nil];
                [[CXClientModel instance].easemob joinRoom:roomInit.HuanxinRoomId];
                
                self.roomUIView.roomSeatsView.isMuteArrays = [NSMutableArray arrayWithArray:self.muteArrays];
                self.roomUIView.model = [CXClientModel instance].room;
                self.roomUIView.isChangeMir = YES;
                
                // 获取当前播放歌曲信息
                CXSocketMessageMusicGetPlayingDetail *request = [CXSocketMessageMusicGetPlayingDetail new];
                [[CXClientModel instance] sendSocketRequest:request withCallback:nil];
                                
//                if ([CXClientModel instance].room.IsHelpShare == YES) { // 助力分享
//                    [self.roomUIView.bottom_shareBtn setImage:[UIImage imageNamed:@"room_bottom_share_help"] forState:UIControlStateNormal];
//                } else {
//                    // false为普通分
//                    [self.roomUIView.bottom_shareBtn setImage:[UIImage imageNamed:@"room_bottom_share"] forState:UIControlStateNormal];
//                }
                
                // 是首充
                if (roomInit.IsFirstCharge == YES) {
                    [self getFirstRechargeData];
                }
                // 获取轮播数据
                [self getCycleScrollDataListIsFirstCharge:roomInit.IsFirstCharge];
                
            }
                break;
            case SocketMessageIDUserJoinRoom: { // 加入房间
                if ([CXClientModel instance].isJoinedRoom) {
                    SocketMessageUserJoinRoomArgs *args = notification;
                    [args.Args enumerateObjectsUsingBlock:^(SocketMessageUserJoinRoom * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                        SocketMessageUserJoinRoom *data = obj;
                        [self.roomUIView.messageListView addModel:data];
                        
                        if (data.IsRoomGuard == YES) {
                            __block CXLiveRoomGuardianAlertView *alertView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomGuardianAlertView" owner:self options:nil].firstObject;
                            alertView.model = data;
                            [[UIApplication sharedApplication].keyWindow addSubview:alertView];
                            [alertView mas_makeConstraints:^(MASConstraintMaker *make) {
                                make.left.right.mas_equalTo(0);
                                make.top.mas_equalTo([CXClientModel instance].room.RoomData.seatsSizeHeight+kNavHeight - 40);
                                make.height.mas_equalTo(41);
                            }];
                            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                                [alertView removeFromSuperview];
                                [MMPopupView hideAll];
                            });
                        }
                    }];
                }
                
            }
                break;
            case SocketMessageIDUserSitdown: // 上麦
            {
                SocketMessageUserSitdownArgs *args = notification;
                [args.Args enumerateObjectsUsingBlock:^(SocketMessageUserSitdown * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                    SocketMessageUserSitdown *userSitdown = obj;
                    NSIndexPath *indexPath = [userSitdown.MicroInfo indexPath];
                    
                    NSIndexPath * selfSeatIndex = [[CXClientModel instance].room.userSeats objectForKey:[CXClientModel instance].userId];
                    if (selfSeatIndex && [selfSeatIndex isEqual:indexPath]) {
                        [CXClientModel instance].agoraEngineManager.offMic = NO;
                        self.roomUIView.isChangeMir = YES;
                        [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleBroadcaster];
                    }
                
                    LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:indexPath];
                    CXLiveRoomSeatView *seatView = [self.roomUIView.roomSeatsView.seats objectForKey:indexPath];
                    seatView.model = seat;
                    [seatView addAgoraRtc:[userSitdown.MicroInfo.User.UserId numberValue]];
                    
                    [self.roomUIView.messageListView addModel:userSitdown];
                }];
            }
                break;
            case SocketMessageIDUserStandup: // 下麦
            {
                SocketMessageUserStandupArgs *args = notification;
                [args.Args enumerateObjectsUsingBlock:^(SocketMessageUserStandup * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                    SocketMessageUserStandup *userStandup = obj;
                    NSIndexPath *indexPath = [userStandup indexPath];
                    
                    //获取麦位
                    LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:indexPath];
                    
                    CXLiveRoomSeatView *seatView = [self.roomUIView.roomSeatsView.seats objectForKey:indexPath];
                    
                    if (seatView.session.uid == [[CXClientModel instance].userId integerValue]) {
                        [CXClientModel instance].agoraEngineManager.offMic = YES;
                        self.roomUIView.isChangeMir = NO;
                        [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleAudience];
                    }
                    
                    [self.muteArrays removeObject:[NSString stringWithFormat:@"%ld", seatView.session.uid]];
                    if ([[CXClientModel instance].agoraEngineManager.engine muteRemoteAudioStream:seatView.session.uid mute:NO] == 0) {
                        seat.isMute = NO;
                    }
                    
                    [seatView deleteAgoraRtc];
                    
                    if (seatView.model.Type == LiveRoomMicroInfoTypeHost) {
                        [self toast:@"主持已关闭此房间，去其他房间看看吧！"];
                        [self leaveRoom];
                    }
                    
                    LiveRoomMicroInfo *microInfo = [LiveRoomMicroInfo new];
                    microInfo.Type = userStandup.Level.integerValue;
                    microInfo.Number = userStandup.Number.integerValue;
                    seatView.model = microInfo;
                }];
                
            }
                break;
            case SocketMessageIDCloseRoomRequest: // 自己不在房间了
            {
                SocketMessageDeported *message= notification;
                if (message.LeaveRoomMsg.length > 0) {
                    [[CXTools currentViewController] toast:message.LeaveRoomMsg];
                }
                
                if (message.LeaveRoomCode.integerValue != 5) {
                    [self leaveRoom];
                }
            }
                break;
            case SocketMessageIDDeported: {//被踢出去了，但是得判断是否是自己被踢出去了
                SocketMessageDeported * dep = notification;
                [self.roomUIView.messageListView addModel:dep];
                if ([dep.UserId isEqualToNumber:[[CXClientModel instance].userId numberValue]]) {
                    [self toast:dep.Code];
                    [self leaveRoom];
                }
            }
                break;
            case SocketMessageIDRoomLockUpdate: { // 群主锁房间了
                if ([[CXClientModel instance].room.RoomData.RoomLock isEqual: @(1)]) {
                    NSIndexPath * index = [[CXClientModel instance].room.userSeats objectForKey:[CXClientModel instance].userId];//这里已经取不到了了
                    if (!index) {
                        // 不在麦上
                        [self leaveRoom];
                    }
                }
            }
                break;
            case SocketMessageIDMicroCancelOrder:{ // 移除麦序
                self.roomUIView.isChangeMir = true;
                
                SocketMessageMicroCancelOrder * cancel = notification;
                
                if ([cancel.Id.stringValue isEqualToString:self.applySeatAlertView.microOrder.MicroOrderData.User.UserId]) {
                    [self.applySeatAlertView removeFromSuperview];
                    self.applySeatAlertViewIsShow = NO;
                } else {
                    [self.applySeatArrays enumerateObjectsUsingBlock:^(SocketMessageMicroOrder * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                        if ([obj.MicroOrderData.User.UserId isEqualToString:cancel.Id.stringValue]) {
                            [self.applySeatArrays removeObjectAtIndex:idx];
                            *stop = YES;
                        }
                    }];
                }
            }
                break;
            case SocketMessageIDRoomNameUpdate: {
                self.roomUIView.top_roomNameWidthLayout.constant = [[CXClientModel instance].room.RoomData.RoomName sizeWithFont:[UIFont systemFontOfSize:16]].width + 44;
                self.roomUIView.top_roomNameLabel.text = [CXClientModel instance].room.RoomData.RoomName;
            }
                break;
            case SocketMessageIDMicroSeatNumber: { // 同步用户上麦卡数量
                CXSocketMessageSystemNotification *message =  notification;
                [CXClientModel instance].room.WheatCardCount = message.Numbers;
                
                self.roomUIView.isChangeMir = true;
            }
                break;
            case SocketMessageIDSystemToastMessage: { // 通知客户端提示语
                SocketMessageDeported *message = notification;
                [CXTools showAlertWithMessage:message.Msg];
            }
                break;
            case SocketMessageIDSeatRoseValueUpdate: { // 更新麦位玫瑰数
                SocketMessageSeatHeartValueUpdateArgs *args = notification;
                [args.Args enumerateObjectsUsingBlock:^(SocketMessageSeatHeartValueUpdate * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                    SocketMessageSeatHeartValueUpdate * update = obj;
                    NSIndexPath * index = [update indexPath];
                    LiveRoomMicroInfo * seat = [[CXClientModel instance].room.seats objectForKey:index];
                    seat.Rolse = update.Rose;
                    CXLiveRoomSeatView *seatView = [self.roomUIView.roomSeatsView.seats objectForKey:index];
                    seatView.seat_rank_firstImage.hidden = YES;
                    seatView.seat_rank_secondImage.hidden = YES;
                    seatView.seat_rank_thirdImage.hidden = YES;
                    for (int i = 0; i < update.RoseRanks.count; i++) {
                        if (i == 0) {
                            seatView.seat_rank_firstImage.hidden = NO;
                            [seatView.seat_rank_firstImage sd_setImageWithURL:[NSURL URLWithString:update.RoseRanks[i]]];
                        } else if (i == 1) {
                            seatView.seat_rank_secondImage.hidden = NO;
                            [seatView.seat_rank_secondImage sd_setImageWithURL:[NSURL URLWithString:update.RoseRanks[i]]];
                        } else {
                            seatView.seat_rank_thirdImage.hidden = NO;
                            [seatView.seat_rank_thirdImage sd_setImageWithURL:[NSURL URLWithString:update.RoseRanks[i]]];
                        }
                    }
                }];
            }
                break;
            case SocketMessageIDGiftEvent: { //接收到礼物后的信息
                SocketMessageGiftEventArgs *args = notification;
                [args.Args enumerateObjectsUsingBlock:^(SocketMessageGiftEvent * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                    SocketMessageGiftEvent * giftEvent = obj;
                    
                    [self.roomUIView.messageListView addModel:giftEvent];
                    
                    if (giftEvent.GiftData.GiftAnimation.length) {
                        [self.svgaPlayView pushSVGAURLString:giftEvent.GiftData];
                    }
                }];
            }
                break;
            case SocketMessageIDMicroOrder: { // 申请上麦
                self.roomUIView.isChangeMir = true;
                
                NSString * loginUserId = [CXClientModel instance].userId;
                LiveRoomMicroInfo * microInfo = [[CXClientModel instance].room microInfoForUser:loginUserId];
                if (microInfo && microInfo.Type == LiveRoomMicroInfoTypeHost) {//红娘
                    SocketMessageMicroOrder * order = notification;
                    if (order.MicroOrderData.User) {
                        LiveRoomUser * userInfo = order.MicroOrderData.User;
                        SocketMessageUserJoinRoom *user = [SocketMessageUserJoinRoom new];
                        user.Name = userInfo.Name;
                        user.Avatar = userInfo.HeadImageUrl;
                        user.UserLevel = userInfo.VipLevel;
                        user.Age = userInfo.Age.stringValue;
                        user.City = userInfo.City;
                        user.Sex = @(userInfo.Sex);
                        [self listViewAddTextModel:@"申请上麦" user:user];
                        
                        [self.applySeatArrays addObject:order];
                        [self showApplySeatAlertView];
                    }
                }
            }
                break;
            case SocketMessageIDReceiveInviteUpMirco:{//接收到邀请，
                SocketMessageInviteResponse * response = notification;
                
                NSString *title = @"主持邀请您上麦，是否同意上麦";
                if (response.Free.integerValue != 1) {//这里是需要成本的
                    title = [NSString stringWithFormat:@"上麦需要花费%@朵玫瑰", response.Cost.stringValue];
                }
                
                CXSystemAlertView *alertView = [CXSystemAlertView loadNib];
                kWeakSelf
                [alertView showAlertTitle:title message:nil cancel:^{
                    [weakSelf replyInvite:false isAgree:@(0)];
                } sure:^{
                    [weakSelf replyInvite:false isAgree:@(1)];
                }];
                [alertView show];
            }
                break;
            case SocketMessageIDSendGiftAddFriendSuccess: {// 送礼添加好友成功回调
                [self getFriendListData];
                SocketMessageSendGiftAddFriendResponse *response = notification;
                if (response.UserId) {
                    NSString *messageStr = [NSString stringWithFormat:@"Hi~ %@, 我已成为你的好友，开聊吧", response.NickName];
                    EMTextMessageBody *body = [[EMTextMessageBody alloc] initWithText:messageStr];
                    NSString *from = [[EMClient sharedClient] currentUsername];
                    EMMessage *message = [[EMMessage alloc] initWithConversationID:[response.UserId stringValue] from:from to:[response.UserId stringValue] body:body ext:nil];
                    message.chatType = EMChatTypeChat;
                    [[EMClient sharedClient].chatManager sendMessage:message progress:nil completion:nil];
                    
                    [self toast:@"添加好友成功"];
                }
            }
                break;
            case SocketMessageIDOnlineMember_apply: {
                CXSocketMessageOnlineMemberNumber *number = notification;
                [CXClientModel instance].room.applyNumber_man = number.LeftNumber;
                [CXClientModel instance].room.applyNumber_woman = number.RightNumber;
            }
                break;
            case SocketMessageIDOnlineMember_online: {
                CXSocketMessageOnlineMemberNumber *number = notification;
                [CXClientModel instance].room.onlineNumber_man = number.LeftNumber;
                [CXClientModel instance].room.onlineNumber_woman = number.RightNumber;
                
            }
                break;
            case SocketMessageIDMusicReserveList: { // 预约列表
//               CXSocketMessageMusicReceiveReverseList *list = notification;
//            self.roomUIView.bottom_music_reverse_numberLable.text = [NSString stringWithFormat:@"预约:%ld", list.SongCount];
               
            }
                break;
            case SocketMessageIDMusicStartPlaySongSyncGist: {// 收到播放歌曲消息
                SocketMessageMusicReceivePlayingDetail *detail = notification;
                self.musicView.musicPlayStatus = music_loading;
                [self music_playStart];
            }
                break;
            case SocketMessageIDMusicStartSyncLastSongerStop: { // 红娘切歌了
                SocketMessageMusicReceivePlayingStatus *status = notification;
                if (status.State == 1) {
                    [self musicShowErrorMessage:@"演唱者走神啦，自动播放下一首歌"];
                } else if (status.State == 2) {
                    [self musicShowErrorMessage:@"网络走神啦，稍后下载，自动播放下一首歌"];
                }
                [self music_playStop];
                
                if (status.ConsertUserId == [[CXClientModel instance].userId integerValue]) {
                    CXGameMusicSongerTipView *tipView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerTipView" owner:self options:nil].firstObject;
                    [tipView show];
                }
                
            }
                break;
            case SocketMessageIDMusicReserveSync: {// 有人点歌了（红娘收到）
                CXSocketMessageMusicReverseSync *message = notification;
                CXGameMusicSongerTipView *tipView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerTipView" owner:self options:nil].firstObject;
                tipView.bgImage.image = [UIImage imageNamed:@"home_game_music_reverse_sync_bg"];
                tipView.descLabel.text = [NSString stringWithFormat:@"%@预约了歌曲，点击底部“KTV”图标，进入“正在播放”开始播放点歌", message.DemandSongUserName];
                [tipView show];
            }
            case SocketMessageIDMusicStartSyncPause: {// 收到暂停同步状态
                NSIndexPath *seatIndex = [[CXClientModel instance].room.userSeats objectForKey:[CXClientModel instance].userId];
                if (seatIndex) {
                    if (_isCloseMineMicro == YES) {
                        [CXClientModel instance].agoraEngineManager.offMic = YES;
                    } else {
                        [CXClientModel instance].agoraEngineManager.offMic = NO;
                    }
                } else {
                    [CXClientModel instance].agoraEngineManager.offMic = YES;
                }
                [[CXClientModel instance].agoraEngineManager.engine setLocalVoiceReverbPreset:AgoraAudioReverbPresetOff];
                SocketMessageMusicReceivePlayingStatus *status = notification;
                if ([CXClientModel instance].room.isConsertModel == YES) {
                    if (status.ConsertUserId > 0 && status.ConsertUserId == [[CXClientModel instance].userId integerValue]) { // 自己是演唱者
                        // 不闭麦
                        [CXClientModel instance].agoraEngineManager.offMic = NO;
                        if (status.IsPause == 0) { // 未播放
                            self.musicView.musicPlayStatus = music_unplay;
                            [self music_playStop];
                        } else if (status.IsPause == 1) {//暂停
                            self.musicView.musicPlayStatus = music_pause;
                            [self music_playPause];
                        } else if (status.IsPause == 2) {//恢复
                            [CXClientModel instance].agoraEngineManager.offMic = NO;
                            [[CXClientModel instance].agoraEngineManager.engine setLocalVoiceReverbPreset:AgoraAudioReverbPresetKTV];
                            if (self.musicView.musicPlayStatus != music_unplay) {
                                [self music_playResume];
                            }
                        }
                    } else {
                        if (status.IsPause == 2) {
                            [CXClientModel instance].agoraEngineManager.offMic = YES;
                        }
                    }
                }
                
                [_musicView reloadMusicView];
            }
                break;
            case SocketMessageIDMusicPlayingDetail: {// 获取到正在播放歌曲信息
                SocketMessageMusicReceivePlayingDetail *detail = notification;
                [self.roomUIView reloadLayoutSubViews];
                [_roomUIView setNeedsLayout];
                
                if (detail.SongInfo.SongMode == 2) { // 伴唱
                    self.roomUIView.musicLRCShowView.hidden = NO;
                    self.roomUIView.musicLRCShowView.lrc_music_lrcLabel.text = @"";
                    self.roomUIView.musicLRCShowView.lrc_music_nameLabel.text = detail.SongInfo.SongName;
                    self.roomUIView.musicLRCShowView.lrc_music_songerLabel.text = [NSString stringWithFormat:@"演唱者：%@",detail.SongInfo.SingerName];
                } else {
                    self.roomUIView.musicLRCShowView.hidden = YES;
                }
                if ([CXClientModel instance].isSocketManagerReconnect == YES && [CXClientModel instance].currentMusicPlayingSongPath) {
                    [self music_playReconnectPlay];
                }
                
                [_musicView reloadMusicView];
            }
                break;
            case SocketMessageIDMusicStartSyncVolum: {// 音量同步
                SocketMessageMusicReceivePlayingStatus *detail = notification;
                self.musicView.pro_volum = detail.Volume;
            }
                break;
            case SocketMessageIDMusicDownloadSongState: {// 歌曲下载状态
                SocketMessageMusicReceivePlayingStatus *detail = notification;
                // 1为下载失败，2为下载成功
                if (detail.State == 1) {
                    self.musicView.musicPlayStatus = music_downfail;
                } else if (detail.State == 2) {
                    self.musicView.musicPlayStatus = music_playing;
                }
            }
                break;
            case SocketMessageIDMusicNotifySongLyric: {// 收到广播歌词
                SocketMessageMusicNotifySongLyric *response = notification;
                if (response.Lyric.length > 0) {
                    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:[response.Lyric dataValue]
                                                                            options:NSJSONReadingMutableContainers
                                                                              error:nil];
                    if (dic) {
                        self.roomUIView.musicLRCShowView.lrc_music_lrcLabel.text = dic[@"content"];
                        NSInteger currentTime = [dic[@"currentTime"] integerValue];
                //        NSInteger startTime = [dic[@"startTime"] integerValue];
                //        NSInteger endTime = [dic[@"endTime"] integerValue];
                        NSInteger allTime = [dic[@"songEndTime"] integerValue];
                        self.musicView.pro_endTime = allTime * 0.001;
                        self.musicView.pro_currentTime = currentTime * 0.001;
                    }
                }
            }
                break;
            case SocketMessageIDMusicStartSyncPepeat: {// 重唱
                [self music_playReplay];
            }
                break;
            case SocketMessageIDMusicUpdateSongRank: {
                SocketMessageMusicUpdateSongRank *message = notification;
                if (message.Rank > 9999) {
                    [self.roomUIView.musicLRCShowView.lrc_music_roseNumBtn setTitle:[NSString stringWithFormat:@"%.1f万", message.Rank / 10000.0] forState:UIControlStateNormal];
                } else {
                    [self.roomUIView.musicLRCShowView.lrc_music_roseNumBtn setTitle:[NSString stringWithFormat:@"%ld", (long)message.Rank] forState:UIControlStateNormal];
                }
                
            }
                break;
                
            case SocketMessageIDShareHelpMessage: {//分享助力消息
                CXSocketMessageSystemShareHelpNotification *message = notification;
                [self.roomUIView.messageListView addModel:message];
            }
                break;
            case SocketMessageIDUpdateGuardianMessage: { // 同步用户守护牌
                SocketMessageUserJoinRoom *message = notification;
                
                [CXClientModel instance].room.mineInfoInRoom.GuardSign = message.GuardSign;
            }
                break;
            case SocketMessageIDShowGuardianAnimationMessage: { // 显示守护动画
                SocketMessageUserJoinRoom *message = notification;
                
                __block CXLiveRoomGuardianAnimationView *view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomGuardianAnimationView" owner:self options:nil].firstObject;
                view.message = message;
                [view show];
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [view hide];
                    [MMPopupView hideAll];
                });
                
            }
                break;
                
                //=========== 红包 =============
            case SocketMessageIDNotifyStartRobRedPacketMessage: { // 开始抢红包
                CXSocketMessageNotifyStartRobRedPacket *msg = notification;
                if (msg.Msg) {
                    NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithData:[msg.Msg dataUsingEncoding:NSUnicodeStringEncoding] options:@{ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType } documentAttributes:nil error:nil];
                    UIFont *boldFont = [UIFont boldSystemFontOfSize:16];
                    [attributedString addAttribute:NSFontAttributeName value:boldFont range:NSMakeRange(0, attributedString.length)];
                    self.redpacketStartView.msgLabel.attributedText =  attributedString;
                    
                    GameMessageTextWelcomeModel *textModel = [GameMessageTextWelcomeModel new];
                    textModel.text = attributedString.string;
                    [self.roomUIView.messageListView addModel:textModel];
                } else {
                    self.redpacketStartView.msgLabel.text = @"";
                }
                
                [self.redpacketStartView show];
            }
                break;
            case SocketMessageIDNotifyRedPacketResultToClientMessage: { // 广播红包列表
                CXSocketMessageNotifyRedPacketResultToClient *list = notification;
                [self showRedPacketResult:list.UserRedPackets];
            }
                break;
            case SocketMessageIDNotifyRedPacketProgressMessage: {// 广播红包进度
                [self showRedpacketProgressView];
                CXSocketMessageNotifyRedPacketProgress *progress = notification;
                if (progress.IsVisible) {
                    self.redpacketProgressView.redpacketProgressView.progress = 1;
                } else {
                    self.redpacketProgressView.redpacketProgressView.progress = 0;
                }
                int proVlaue = floorf(progress.Progress*100);
                self.redpacketProgressView.redpacketProgressLabel.text = [NSString stringWithFormat:@"%d%%", MIN(proVlaue, 100)];
            }
                break;
            case SocketMessageIDNotifyLetterEffectMessage: {
                CXSocketMessageNotifyStartRobRedPacket *msg = notification;
                if (msg.Message.length > 0) {
                    [self toast:msg.Message];
                    
                    GameMessageTextWelcomeModel *textModel = [GameMessageTextWelcomeModel new];
                    textModel.text = msg.Message;
                    [self.roomUIView.messageListView addModel:textModel];
                }
            }
                break;
                
                //=========== 心跳 =============
            case SocketMessageIDKeepaLiveNotification: {
                CXSocketMessageSystemKeepaLiveRequest *request = [CXSocketMessageSystemKeepaLiveRequest new];
                [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {

                }];
            }
                break;
            default:
                break;
        }
    }
}

//- (void)modelClient:(CXClientModel *)client reconnectRoomSuccess:(BOOL)success {
//
//    NSIndexPath *seatIndex = [[CXClientModel instance].room.userSeats objectForKey:[CXClientModel instance].userId];
//    if (!seatIndex) {
//        [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleAudience];
//    } else {
//        [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleBroadcaster];
//        CXLiveRoomSeatView *seatView = [self.roomUIView.roomSeatsView.seats objectForKey:seatIndex];
//        [seatView addAgoraRtc:[[CXClientModel instance].userId numberValue]];
//    }
//
//    if ([CXClientModel instance].room.isConsertModel == YES) {
//        if ([CXClientModel instance].room.isSonger) {
//            [self music_playResume];
//        }
//    }
    
    
//    else {
//        if ([CXClientModel instance].room.isHost == YES) {
//            [self music_playResume];
//        }
//    }
//}

- (void)modelClient:(CXClientModel *)client room:(NSString *)roomId error:(NSError *)error {
    kWeakSelf
    if ([CXClientModel instance].room.RoomData.RoomId.length <= 0) {
        [self leaveRoom];
        return;
    }
    [self alertTitle:@"房间连接失败" message:@"是否重新连接" confirm:@"确定" cancel:@"取消" confirm:^{
        [weakSelf reconectRoom];
    } cancel:^{
        [weakSelf leaveRoom];
    }];
    
    [self music_playPause];
}

- (void)reconectRoom {
    kWeakSelf
    if ([CXHTTPRequest isNetwork] == YES) {
        [[CXClientModel instance] reconnectRoom:[CXClientModel instance].room.RoomData.RoomId];
    } else {
        [self alertTitle:@"无网络，房间重接失败" message:@"是否重新连接" confirm:@"确定" cancel:@"取消" confirm:^{
            [weakSelf reconectRoom];
        } cancel:^{
            [weakSelf leaveRoom];
        }];
    }
}

- (void)modelClient:(CXClientModel*)client didReceiveRoomMessage:(NSArray<EasemobRoomMessage*>*)msgs {
    kWeakSelf
    [msgs enumerateObjectsUsingBlock:^(EasemobRoomMessage * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj.ext.XYType isEqualToString:EasemobRoomMessageExtTypeEmoji]) {
//            [weakSelf listViewAddFaceModel:obj.ext.XYEmoji.face_image user:obj.ext.XYUser];
//            [weakSelf addEmoji:obj.ext.XYEmoji ToUser:obj.ext.XYUser.UserId];
        } else {
            [weakSelf listViewAddTextModel:obj.text user:obj.ext.XYUser];
        }
    }];
}

#pragma mark - ============================ 用户信息 ================================
- (void)getUserInfoWith:(NSNumber *)UserID {
    __weak typeof(self) wself = self;
    SocketMessageGetUserInfo * getInfo = [SocketMessageGetUserInfo new];
    getInfo.UserId = UserID;
    [[CXClientModel instance] sendSocketRequest:getInfo withCallback:^(SocketMessageGetUserInfo * _Nonnull request) {
        if (request.noError && request.response.isSuccess) {
            [wself showUserInfoViewWithUserInfo:request.response];
        }
    }];
}

// 显示用户信息
- (void)showUserInfoViewWithUserInfo:(SocketMessageGetUserInfoResponse *)userInfo {
    CXLiveRoomUserProfileView *profileView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomUserProfileView" owner:self options:nil].firstObject;
    LiveRoomUser *user = userInfo.User;
    profileView.userInfo = userInfo;
    kWeakSelf
    profileView.userProfileAvatarActionBlock = ^{
        [AppController showUserProfile:userInfo.User.UserId target:weakSelf];
    };
    profileView.userProfileActionBlock = ^(NSInteger tag) {
        switch (tag) {
            case 10: // 禁言
                [weakSelf jinyanUser:userInfo];
                break;
            case 11: // 闭麦
                [weakSelf muteOrCloseMicroWithUser:user];
                break;
            case 12: // 拉黑
                [weakSelf blockUser:userInfo];
                break;
            case 13: // 移除房间
                [weakSelf yichuUser:user];
                break;
            case 14: // 举报用户
                [weakSelf reportUser:user];
                break;
            case 20: // 守护榜
                [weakSelf gotoGuardRankWithUserId:user.UserId];
                break;
            case 21: // 免费邀请
                [weakSelf inviteUser:user free:YES];
                break;
            case 22: // 收费邀请
                [weakSelf inviteUser:user free:NO];
                break;
            case 30: // 私聊好友
            {
                if ([[CXClientModel instance].firendIdArrays containsObject:user.UserId]) { //已经是好友了
                    EMConversationModel *model = [EMConversationHelper modelFromContact:user.UserId];
                    if (model) {
                        EMChatViewController *controller = [[EMChatViewController alloc] initWithCoversationModel:model];
                        CXFriendInviteModel *friend = [CXFriendInviteModel modelWithJSON:[user modelToJSONObject]];
                        controller.friendModel = friend;
                        [self.navigationController pushViewController:controller animated:YES];
                    }
                } else {
                    CXAddFriendViewController *vc = [[CXAddFriendViewController alloc] init];
                    vc.nickname = user.Name;
                    vc.user_id = user.UserId;
                    vc.user_avatar = user.HeadImageUrl;
                    vc.is_room = @"1";
                    [self.navigationController pushViewController:vc animated:YES];
                }
            }
                break;
            case 31: // @TA
            {
                [weakSelf.inputBar.textInput becomeFirstResponder];
                weakSelf.inputBar.placeholderLabel.hidden = YES;
                weakSelf.inputBar.textInput.text = [NSString stringWithFormat:@"@%@ ",user.Name];
                [weakSelf.view addSubview:weakSelf.coverView];
            }
                break;
            case 32: // 送TA礼物
            {
                NSIndexPath *index = [[CXClientModel instance].room.userSeats objectForKey:user.UserId];
                if (index) {
                    user.modelSeat = [[LiveRoomMicroInfo alloc] init];
                    user.modelSeat.Number = index.row;
                    user.modelSeat.Type = index.section;
                }
                                
                if ([CXClientModel instance].room.IsFirstCharge == YES) {
                   [weakSelf roomRechargeRechargeType:2 seatUser:user];
                } else {
                   NSIndexPath *index = [[CXClientModel instance].room.userSeats objectForKey:user.UserId];
                   if (index) {
                       [weakSelf sendGiftWithUser:user status:@"0" isSeat:YES];
                   } else {
                       [weakSelf sendGiftWithUser:user status:@"0" isSeat:NO];
                   }
                }
            }
                break;
            default:
                break;
        }
    };
    [profileView show];
}

// 拉黑某用户
- (void)blockUser:(SocketMessageGetUserInfoResponse *)userInfo {
    if (userInfo.IsBlock == YES) { // 取消拉黑
        [self blockUserWithUserId:userInfo.User.UserId isBlock:NO];
    } else { // 拉黑
        CXSystemAlertView *view = [CXSystemAlertView loadNib];
        kWeakSelf
        [view showAlertTitle:@"拉黑对方" message:@"拉黑对方后，将无法再接收到Ta的任何消息。" cancel:nil sure:^{
            [weakSelf blockUserWithUserId:userInfo.User.UserId isBlock:YES];
        }];
        [view show];
    }
}
- (void)blockUserWithUserId:(NSString *)userId isBlock:(BOOL)isBlcok {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"user_id" : userId
    };
    NSString *url = @"/index.php/Api/Member/blackout";
    if (isBlcok) {
        url = @"/index.php/Api/Member/defriend";
    }
    kWeakSelf
    [CXHTTPRequest POSTWithURL:url parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           [weakSelf toast:isBlcok == YES ? @"拉黑成功" : @"取消拉黑成功"];
       }
    }];
}

// 禁言某用户
- (void)jinyanUser:(SocketMessageGetUserInfoResponse *)userInfo {
    if (userInfo.IsDisableMsg == YES) { // 取消禁言
        [self setUserIsDisableMsg: NO tag:0];
    } else { //禁言
        CXLiveRoomUserProfileJinyanView *view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomUserProfileJinyanView" owner:self options:nil].firstObject;
        view.userInfo = userInfo;
        kWeakSelf
        view.userProfileJinyanActionBlock = ^(NSInteger tag) {
            [weakSelf setUserIsDisableMsg: YES tag:tag];
        };
        [view show];
    }
}
- (void)setUserIsDisableMsg:(BOOL)IsDisableMsg tag:(NSInteger)tag {
    SocketMessageSetUserIsDisableMsg *request = [SocketMessageSetUserIsDisableMsg new];
    request.Id = [NSNumber numberWithInteger:tag];
    request.IsDisableMsg = IsDisableMsg;
    [[CXClientModel instance] sendSocketRequest:request withCallback:nil];
}
// 静音/闭麦
- (void)muteOrCloseMicroWithUser:(LiveRoomUser *)user {
    NSIndexPath *seatIndex = [[CXClientModel instance].room.userSeats objectForKey:user.UserId];
    CXLiveRoomSeatView *seatView = [self.roomUIView.roomSeatsView.seats objectForKey:seatIndex];
    if ([user.UserId isEqualToString:[CXClientModel instance].userId]) {
        if ([CXClientModel instance].agoraEngineManager.offMic == YES) { // 闭麦了，打开
            self.isCloseMineMicro = NO;
            [CXClientModel instance].agoraEngineManager.offMic = NO;
            [seatView.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
        } else {
            self.isCloseMineMicro = YES;
            [CXClientModel instance].agoraEngineManager.offMic = YES;
            [seatView.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_off"] forState:UIControlStateNormal];
        }
    } else {
        LiveRoomMicroInfo * seat = [[CXClientModel instance].room.seats objectForKey:seatIndex];
        if (seat.isMute == YES) {//静音了此麦克风，打开
            if ([[CXClientModel instance].agoraEngineManager.engine muteRemoteAudioStream:[user.UserId integerValue] mute:NO] == 0) {
                seat.isMute = NO;
                [self.muteArrays removeObject:user.UserId];
                [seatView.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
            }
        } else {
            if ([[CXClientModel instance].agoraEngineManager.engine muteRemoteAudioStream:[user.UserId integerValue] mute:YES] == 0) {
                seat.isMute = YES;
                [self.muteArrays addObject:user.UserId];
                [seatView.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_off"] forState:UIControlStateNormal];
            }
        }
    }
}
// 移除房间
- (void)yichuUser:(LiveRoomUser *)user {
    SocketMessageKickOut * request = [[SocketMessageKickOut alloc] init];
    request.UserId = @(user.UserId.integerValue);
    kWeakSelf
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
        if (request.response.Success.integerValue == -49) { // 不在房间
            [weakSelf toast:@"该用户已不在房间"];
        }
    }];
}
// 举报用户
- (void)reportUser:(LiveRoomUser *)user {
    CXLiveRoomUserProfileReportView *view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomUserProfileReportView" owner:self options:nil].firstObject;
    view.userId = user.UserId;
    [view show];
}
#pragma mark - ============================ 下麦/邀请上麦 ================================
// 主持踢人下麦
- (void)closeMicroWithMicroInfo:(LiveRoomMicroInfo *)microInfo {
    SocketMessageLeaveSeat * leave = [[SocketMessageLeaveSeat alloc] init];
    leave.Level = @(microInfo.Type);
    leave.Number = @(microInfo.Number);
    kWeakSelf
    [[CXClientModel instance] sendSocketRequest:leave withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
        if (request.response.Success.integerValue == 6) {
            [weakSelf music_next];
        }
    }];
}
// 主持邀请房间内用户上麦：isFree是否免费
- (void)inviteUser:(LiveRoomUser *)user free:(BOOL)isFree {
    SocketMessageInvite * invite = [SocketMessageInvite new];
    invite.TargetUserIds = [NSArray arrayWithObjects:@{@"Id" : user.UserId}, nil];
    invite.Free = isFree == YES ? @(1) : @(2);
    [[CXClientModel instance] sendSocketRequest:invite withCallback:nil];
}
// 房间内用户回复主持的邀请：isForce 是否跳过 agree: 1同意  0不同意
- (void)replyInvite:(BOOL)isForce isAgree:(NSNumber*)agree {
    SocketMessageReplyInvite * reply = [SocketMessageReplyInvite new];
    reply.Agree = agree;
    reply.Force = isForce;
    kWeakSelf
    [[CXClientModel instance] sendSocketRequest:reply withCallback:^(SocketMessageReplyInvite * _Nonnull request) {
        NSLog(@"SocketMessageReplyInvite  %@" , request);
        if (request.response.Success.integerValue == 21) {
            // 余额不足
            [weakSelf showRechargeErrorView];
        } else if (request.response.Success.integerValue == 8) {
            // 未实名认证
            [weakSelf showVerifiedMessage:true level:0];
        }
    }];
}
#pragma mark - ===================== 申请上麦 =====================
- (void)applyJoinChannel:(BOOL)isForce {
    [self applyJoinChannel:isForce level:0];
}
- (void)applyJoinChannel:(BOOL)isForce level:(NSInteger)level {
    kWeakSelf
    // 1. 是否在麦位
    NSIndexPath * ownerIndex = [[CXClientModel instance].room.userSeats objectForKey:[CXClientModel instance].userId];
    if (ownerIndex) {//已经在麦位了
        return;
    }
    
    NSMutableDictionary<NSString*, LiveRoomMicroOrder*> * orders = nil;
    if ([CXClientModel instance].sex.integerValue == 1) {//需要判断自己是男是女
        orders = [CXClientModel instance].room.leftOrders;
    } else{
        orders = [CXClientModel instance].room.rightOrders;
    }
    // 2. 麦序位数是否大于20个
    if (orders.allKeys.count >= 20) {
        [self toast:@"麦序已满，请稍等再试"];
        
        return;
    }
    // 3. 是否在麦序中
    LiveRoomMicroOrder * order = [orders objectForKey:[CXClientModel instance].userId];
    if (order) {
        [self toast:@"您已经在麦序中"];
        return;
    }
    // 4. 是否收费
    if ([CXClientModel instance].sex.integerValue == 1) { // 男
        SocketMessageCost *cost = [SocketMessageCost new];
        [[CXClientModel instance] sendSocketRequest:cost withCallback:^(SocketMessageCost * _Nonnull request) {
            if (request.response.Cost.integerValue == 0) {
                SocketMessageJoinSeat *join = [SocketMessageJoinSeat new];
                join.Number = @(100);
                join.Force = isForce;
                join.Level = @(level);
                [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                    [weakSelf showJoinSeatSuccessfulMessage:request level:level];
                }];
            } else {
                if ([CXClientModel instance].room.WheatCardCount.integerValue > 0) {
                    // 有上麦卡
                    CXSystemAlertView *alertView = [CXSystemAlertView loadNib];
                    [alertView showAlertTitle:@"免费上麦" message:@"您将消耗一张上麦卡，免费上麦" cancel:nil sure:^{
                        SocketMessageJoinSeat * join = [SocketMessageJoinSeat new];
                        join.Number = @(100);
                        join.Force = isForce;
                        join.Level = @(level);
                        [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                            [weakSelf showJoinSeatSuccessfulMessage:request level:level];
                        }];
                    }];
                    [alertView show];
                } else {
                    CXSystemAlertView *alertView = [CXSystemAlertView loadNib];
                    NSString *title = [NSString stringWithFormat:@"上麦需要花费%@朵玫瑰",request.response.Cost.stringValue];
                    [alertView showAlertTitle:title message:nil cancel:nil sure:^{
                        SocketMessageJoinSeat * join = [SocketMessageJoinSeat new];
                        join.Number = @(100);
                        join.Force = isForce;
                        join.Level = @(level);
                        [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                            [weakSelf showJoinSeatSuccessfulMessage:request level:level];
                        }];
                    }];
                    [alertView show];
                }
            }
        }];
    } else { // 其他性别
        SocketMessageJoinSeat * join = [SocketMessageJoinSeat new];
        join.Number = @(100);
        join.Force = isForce;
        join.Level = @(level);
        [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
            [weakSelf showJoinSeatSuccessfulMessage:request level:level];
        }];
    }
}


// 申请上麦错误处理
- (void)showJoinSeatSuccessfulMessage:(SocketMessageRequest *)request level:(NSInteger)level {
    if (request.response.Success.integerValue == 20) {
        [CXTools showAlertWithMessage:@"申请上麦成功"];
    } else if (request.response.Success.integerValue == 21) {
        // 余额不足
        [self showRechargeErrorView];
    } else if (request.response.Success.integerValue == 8) {
        // 实名认证
        [self showVerifiedMessage: false level:level];
    }
}

// 余额不足请充值
- (void)showRechargeErrorView {
    kWeakSelf
    [LEEAlert alert].config
    .LeeTitle(@"")
    .LeeContent(@"账户余额不足, 请充值")
    .LeeCancelAction(@"取消", ^{
    })
    .LeeAction(@"充值", ^{
        [weakSelf roomRechargeRechargeType:0 seatUser:nil];
    })
    .LeeShow();
}

// 实名认证: isInvite: 是否是邀请上麦，默认申请上麦
- (void)showVerifiedMessage:(BOOL)isInvite level:(NSInteger)level {
    NSString *msg = @"实名认证信息受到用户隐私条款保护，不会向第三方透露。";
    if ([CXClientModel instance].sex.integerValue == 2) {//需要判断自己是男是女
        msg = @"实名认证信息受到用户隐私条款保护，不会向第三方透露。";
    }
    kWeakSelf
    [LEEAlert alert].config
    .LeeTitle(@"为保障合合社区的真实和严肃性，所有上麦用户需要实名认证")
    .LeeContent(msg)
    .LeeCancelAction(@"跳过", ^ {
        if (isInvite == true) {
            [weakSelf replyInvite:true isAgree:@(1)];
        } else {
            [weakSelf applyJoinChannel:true level:level];
        }
    })
    .LeeAction(@"认证", ^{
        [[CXAliRPManager sharedInstance] startWithSuccess:^{
            [weakSelf toast:@"认证成功"];
            [weakSelf applyJoinChannel:false];
        } failure:^(int code, NSString * _Nonnull reason) {
            [weakSelf toast:reason];
        } nav:weakSelf.navigationController];
    })
    .LeeShow();
}

#pragma mark - ===================== 充值 =====================
// 是否是首充
- (void)getFirstRechargeData {
    if ([CXClientModel instance].room.IsFirstCharge == YES) {
        kWeakSelf
        NSString *url = @"/index.php/Api/Order/room_charge";
        if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) {
            url = @"/index.php/Api/ApplePay/room_charge";
        }
        [CXHTTPRequest POSTWithURL:url parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                // 是否首冲 1:是 2:不是
                NSNumber *is_first = responseObject[@"data"][@"is_first"];
                if (is_first.integerValue == 1) {
                    NSString *rmb = responseObject[@"data"][@"rmb"];
                    NSString *diamond = responseObject[@"data"][@"diamond"];
                    NSString *iosflag = responseObject[@"data"][@"iosflag"];
                    NSString *chargeid = responseObject[@"data"][@"chargeid"];
                    [CXClientModel instance].room.rmb = rmb;
                    [CXClientModel instance].room.diamond = diamond;
                    [CXClientModel instance].room.iosflag = iosflag;
                    [CXClientModel instance].room.chargeId = chargeid;
                    [CXClientModel instance].room.IsFirstCharge = YES;
                } else {
                    [CXClientModel instance].room.IsFirstCharge = NO;
                }
                
                [weakSelf getCycleScrollDataListIsFirstCharge:[CXClientModel instance].room.IsFirstCharge];
            } else {
                [CXClientModel instance].room.IsFirstCharge = NO;
            }
        }];
    }
}

// rechargeType: 0: 充值 1:送礼 2:麦位送礼
- (void)roomRechargeRechargeType:(NSInteger)rechargeType seatUser:(LiveRoomUser *)seatUser {
    kWeakSelf
    NSString *url = @"/index.php/Api/Order/room_charge";
    if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) {
        url = @"/index.php/Api/ApplePay/room_charge";
    }
    [CXHTTPRequest POSTWithURL:url parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            // 是否首冲 1:是 2:不是
            NSNumber *is_first = responseObject[@"data"][@"is_first"];
            if (is_first.integerValue == 1) {
                NSString *rmb = responseObject[@"data"][@"rmb"];
                NSString *diamond = responseObject[@"data"][@"diamond"];
                NSString *iosflag = responseObject[@"data"][@"iosflag"];
                NSString *chargeid = responseObject[@"data"][@"chargeid"];
                [weakSelf showRechargeSheetViewRMB:rmb diamond:diamond iosflag:iosflag chargeid:chargeid rechargeType:rechargeType seatUser:seatUser];
            } else {
                [weakSelf showRechargeView];
            }
        } else {
            [weakSelf showRechargeView];
        }
    }];
}

// rechargeType: 0: 充值 1:送礼 2:麦位送礼
- (void)showRechargeSheetViewRMB:(NSString *)rmb diamond:(NSString *)diamond iosflag:(NSString *)iosflag chargeid:(NSString *)chargeid rechargeType:(NSInteger)rechargeType seatUser:(LiveRoomUser *)seatUser {
    CXLiveRoomRechargeSheepView *sheetView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomRechargeSheepView" owner:self options:nil].firstObject;
    [sheetView setupRoseNumber:diamond roseRMB:rmb];
    kWeakSelf
    sheetView.rechargeSheetViewBlcok = ^(BOOL isRecharge, BOOL isCancel, NSString *payAction) {
        if (isCancel) {
            if (rechargeType == 1) { // 送礼
                [weakSelf sendGiftWithUser:nil status:@"0" isSeat:YES];
            } else if (rechargeType == 2) {
                NSIndexPath *index = [[CXClientModel instance].room.userSeats objectForKey:seatUser.UserId];
                if (index) {
                    [self sendGiftWithUser:seatUser status:@"1" isSeat:YES];
                } else {
                    [self sendGiftWithUser:seatUser status:@"1" isSeat:NO];
                }
            } else { // 充值
                [weakSelf showRechargeView];
            }
            
        } else {
            if ([payAction isEqualToString:@"weixin"]) {
                NSDictionary *param = @{
                    @"rmb":rmb,
                    @"action" : @"weixin",
                    @"uid" : [CXClientModel instance].userId,
                    @"type" : @"1",
                    @"is_active" : @"0",
                    @"chargeid": chargeid,
                };
                [CXHTTPRequest POSTWithURL:@"/index.php/Api/Order/pay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
                    if (!error) {
                        NSString *str = [responseObject[@"data"] jsonStringEncoded];
                        [[CXThirdPayManager sharedApi] wxPayWithPayParam:str success:nil failure:nil];
                    }
                }];
            } else if ([payAction isEqualToString:@"alipay"]) {
                NSDictionary *param = @{
                    @"rmb":rmb,
                    @"action" : @"alipay",
                    @"uid" : [CXClientModel instance].userId,
                    @"type" : @"1",
                    @"is_active" : @"0",
                    @"chargeid": chargeid,
                };
                [CXHTTPRequest POSTWithURL:@"/index.php/Api/Order/pay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
                    if (!error) {
                        [[CXThirdPayManager sharedApi] aliPayWithPayParam:responseObject[@"data"] success:nil failure:nil];
                    }
                }];
            } else {
                [weakSelf rechargeWithProductId:iosflag];
            }
        }
    };
    [sheetView show];
}

- (void)rechargeWithProductId:(NSString *)productId {
    kWeakSelf
    [CXIPAPurchaseManager manager].userid = [CXClientModel instance].userId;
    [CXIPAPurchaseManager manager].purchaseType = LiveBroadcast;
    [[CXIPAPurchaseManager manager] inAppPurchaseWithProductID:productId iapResult:^(BOOL isSuccess,  NSDictionary *param, NSString *errorMsg) {
        if (isSuccess) {
            [weakSelf toast:@"购买成功"];
            [weakSelf getFirstRechargeData];
        } else {
            [weakSelf toast:errorMsg];
        }
    }];
}

- (void)showRechargeView {
    CXLiveRoomRechargeView *rechargeView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomRechargeView" owner:self options:nil].firstObject;
    kWeakSelf
    rechargeView.gotoRechargeProtocol = ^(NSString * _Nonnull linkURL) {
        NSURL *url = [NSURL URLWithString:linkURL];
        CXBaseWebViewController *webVC = [[CXBaseWebViewController alloc] initWithURL:url];
        webVC.title = @"合合有约充值协议";
        [weakSelf.navigationController pushViewController:webVC animated:YES];
    };
    rechargeView.rechargeBlock = ^(CXRechargeModel * _Nonnull model, NSInteger payAction) {
        if (payAction == 1) {
            NSDictionary *param = @{
                @"rmb":model.rmb,
                @"action" : @"weixin",
                @"uid" : [CXClientModel instance].userId,
                @"type" : @"1",
                @"is_active" : @"0",
                @"chargeid": model.charge_id,
            };
            [CXHTTPRequest POSTWithURL:@"/index.php/Api/Order/pay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
                if (!error) {
                    NSString *str = [responseObject[@"data"] jsonStringEncoded];
                    [[CXThirdPayManager sharedApi] wxPayWithPayParam:str success:nil failure:nil];
                }
            }];
        } else if (payAction == 2) {
            NSDictionary *param = @{
                @"rmb":model.rmb,
                @"action" : @"alipay",
                @"uid" : [CXClientModel instance].userId,
                @"type" : @"1",
                @"is_active" : @"0",
                @"chargeid": model.charge_id,
            };
            [CXHTTPRequest POSTWithURL:@"/index.php/Api/Order/pay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
                if (!error) {
                    [[CXThirdPayManager sharedApi] aliPayWithPayParam:responseObject[@"data"] success:nil failure:nil];
                }
            }];
        } else {
            [weakSelf rechargeWithProductId:model.iosflag];
        }
    };
    [rechargeView show];
}

- (void)actionApplePayFinished {
    [self toast:@"充值成功"];
}

- (void)aliPayCallBack:(NSNotification *)info {

    if ([[info.object objectForKey:@"resultStatus"] integerValue] == 9000) {
        [self toast:@"充值成功"];
        [self getFirstRechargeData];
    } else {
        [self toast:@"支付失败"];
    }
}

- (void)weChatCallBack:(NSNotification *)info {
    NSLog(@"%@", info);

    // WXSuccess           = 0,    /**< 成功    */
    // WXErrCodeCommon     = -1,   /**< 普通错误类型    */
    // WXErrCodeUserCancel = -2,   /**< 用户点击取消并返回    */
    // WXErrCodeSentFail   = -3,   /**< 发送失败    */
    // WXErrCodeAuthDeny   = -4,   /**< 授权失败    */
    // WXErrCodeUnsupport  = -5,   /**< 微信不支持    */
    NSDictionary *obj = info.object;
    switch ([[obj objectForKey:@"errCode"] integerValue]) {
        case WXSuccess:
            [self toast:@"充值成功"];
            [self getFirstRechargeData];
            break;
        case WXErrCodeUserCancel:
            [self toast:@"取消支付"];
            break;
        default:
            [self toast:@"支付失败"];
            break;
    }
}

#pragma mark - ===================== 礼物 =====================
/**
 *  status
 *  0: 普通礼物 1: 加好友送礼，2: 演唱嘉宾送礼
 */
- (void)sendGiftWithUser:(LiveRoomUser *)user status:(NSString *)stataus isSeat:(BOOL)isSeat {
    
    if ([stataus isEqualToString:@"1"]) { // 加好友礼物
        [self showGiftView:self.firendGiftArrays knapsackList:self.guardGiftArrays user:user isAddFriend:YES isSeat:isSeat];
    } else {
        [self showGiftView:self.giftArrays knapsackList:self.guardGiftArrays user:user isAddFriend:NO isSeat:isSeat];
    }
}

- (void)showGiftView:(NSArray *)giftList knapsackList:(NSArray *)knapsackList user:(LiveRoomUser *)user isAddFriend:(BOOL)isAddFriend isSeat:(BOOL)isSeat {
    
    NSMutableArray<LiveRoomUser*> *temp = [NSMutableArray array];
    if (user) { // 针对发送
        [temp addObject:user];
    } else {
        for (LiveRoomUser *user in [CXClientModel instance].room.users.allValues) {
            if (![user.UserId isEqualToString:[CXClientModel instance].userId]) {
                
                LiveRoomMicroInfo * microInfo = [[CXClientModel instance].room microInfoForUser:user.UserId];
                if (microInfo && microInfo.Type == LiveRoomMicroInfoTypeHost && [CXClientModel instance].room.CanSendGiftToEmcee == NO) {//红娘
                    NSLog(@"此红娘不能送礼物");
                } else {
                    [temp addObject:user];
                }
            }
        }
    }
    
    NSArray *users = [temp sortedArrayUsingComparator:^NSComparisonResult(LiveRoomUser *  _Nonnull obj1, LiveRoomUser *  _Nonnull obj2) {
        if (obj1.modelSeat.indexPath.section != obj2.modelSeat.indexPath.section) {
            if (obj1.modelSeat.indexPath.section == LiveRoomMicroInfoTypeMan) {
                return NSOrderedDescending;
            }
            if (obj2.modelSeat.indexPath.section == LiveRoomMicroInfoTypeMan) {
                return NSOrderedAscending;
            }
            return obj1.modelSeat.indexPath.section > obj2.modelSeat.indexPath.section ? NSOrderedDescending : NSOrderedAscending;
        }
        else {
            return (obj1.modelSeat.indexPath.row > obj2.modelSeat.indexPath.row) ? NSOrderedDescending : NSOrderedAscending;
        }
    }];
    
    __weak typeof (self) wself = self;
    MuaGiftListView * giftListView = [MuaTipManager showGiftViewWithListArray:giftList knapsackList:knapsackList users:users isAddFriend:isAddFriend rechargeBlock:^{
        [wself roomRechargeRechargeType:0 seatUser:nil];
    } sendGiftBlock:^(CXLiveRoomGiftModel * _Nonnull info, NSArray * _Nonnull selelctUsers, NSString * _Nonnull count, BOOL IsUseBug) {
        if ([info.gift_id integerValue] == 0) { //赠送蓝玫瑰
            NSMutableArray *userids = [NSMutableArray array];
            if (selelctUsers.count > 0) {
                [selelctUsers enumerateObjectsUsingBlock:^(LiveRoomUser *  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                    [userids addObject:obj.UserId];
                }];
            }
            CXMuaGiftSendBlueRoseView *sendView = [[NSBundle mainBundle] loadNibNamed:@"CXMuaGiftSendBlueRoseView" owner:self options:nil].firstObject;
            sendView.sendBlueRoseBlock = ^(NSInteger count) {
                //  赠送蓝玫瑰
                CXLiveRoomSendBlueRoseRequest *request = [CXLiveRoomSendBlueRoseRequest new];
                request.UserIds = userids;
                request.BlueRose = count;
                [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                    if (request.response.isSuccess) {
                        [wself toast:@"赠送成功"];
                    }
                }];
            };
            
            [sendView show];
            
            return;
        }
        if (isSeat == NO) { // 不在麦位上的人送礼
            LiveRoomUser *model = selelctUsers[0];
            SocketMessageSendGift * request = [SocketMessageSendGift new];
            request.GiftId = info.gift_id;
            request.Count = [count numberValue];
            request.Id = [model.UserId numberValue];
            request.IsUseBag = IsUseBug;
            [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageGroupGift * _Nonnull request) {
                if (request.noError && request.response.isSuccess) {
                    [CXClientModel instance].balance = request.response.Balance;
                } else if (request.response.Success.integerValue == 4) {
                    [LEEAlert alert].config
                    .LeeTitle(@"")
                    .LeeContent(@"账户余额不足, 请充值")
                    .LeeCancelAction(@"取消", ^{
                    })
                    .LeeAction(@"充值", ^{
                        [wself roomRechargeRechargeType:0 seatUser:nil];
                    })
                    .LeeShow();
                }
            }];
        } else { // 给在麦位上的人送礼
            NSMutableArray<SocketMessageGroupGiftSeat*> * seats = [NSMutableArray new];
            [selelctUsers enumerateObjectsUsingBlock:^(LiveRoomUser * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                if (obj.modelSeat) {
                    SocketMessageGroupGiftSeat * seat = [SocketMessageGroupGiftSeat new];
                    seat.Level = @(obj.modelSeat.Type);
                    seat.Number = @(obj.modelSeat.Number);
                    [seats addObject:seat];
                }
            }];
            
            SocketMessageGroupGift * request = [SocketMessageGroupGift new];
            request.IsWholeMicro = @(NO);
            request.GiftId = info.gift_id;
            request.Count = @([count integerValue]);
            request.Micros = seats;
            request.IsUseBag = IsUseBug;
            [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageGroupGift * _Nonnull request) {
                if (request.noError && request.response.isSuccess) {
                    [CXClientModel instance].balance = request.response.Balance;
                } else if (request.response.Success.integerValue == 6) {
                    [LEEAlert alert].config
                    .LeeTitle(@"")
                    .LeeContent(@"账户余额不足, 请充值")
                    .LeeCancelAction(@"取消", ^{
                    })
                    .LeeAction(@"充值", ^{
                        [wself roomRechargeRechargeType:0 seatUser:nil];
                    })
                    .LeeShow();
                }
            }];
        }
    }];
}

// 赠送单枝玫瑰
- (void)sendGiftOneRoseWithUserId:(NSString *)userId {
    SocketMessageSendGift * request = [SocketMessageSendGift new];
    request.GiftId = @1;
    request.Count = @1;
    request.Id = [NSNumber numberWithString:userId];
    request.IsUseBag = NO;
    kWeakSelf
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageGroupGift * _Nonnull request) {
        if (request.noError && request.response.isSuccess) {
            [CXClientModel instance].balance = request.response.Balance;
        } else if (request.response.Success.integerValue == 4) {
            [LEEAlert alert].config
            .LeeTitle(@"")
            .LeeContent(@"账户余额不足, 请充值")
            .LeeCancelAction(@"取消", ^{
            })
            .LeeAction(@"充值", ^{
                [weakSelf roomRechargeRechargeType:0 seatUser:nil];
            })
            .LeeShow();
        }
    }];
}

#pragma mark - ============================ 守护榜 ================================
- (void)gotoGuardRankWithUserId:(NSString *)userId {
    CXLiveRoomGuardGroupView *guardRankView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomGuardGroupView" owner:self options:nil].firstObject;
    guardRankView.userId = userId;
    kWeakSelf
    guardRankView.guardGroupViewBlcok = ^(void) {
        [weakSelf guardRenew:userId];
    };
    [guardRankView show];
}

- (void)guardRenew:(NSString *)userId {
    CXMineGuardRenewView *guardRankView = [[NSBundle mainBundle] loadNibNamed:@"CXMineGuardRenewView" owner:self options:nil].firstObject;
    guardRankView.userId = userId;
    [guardRankView show];
    
}

#pragma mark - ===================== 麦位管理 =====================
// 点击某个麦位
- (void)clickMicroInfo:(LiveRoomMicroInfo *)microInfo {
    if (microInfo.modelUser.UserId.length > 0) {
        NSIndexPath * indexPath = [microInfo indexPath];
        LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:indexPath];
        if (seat) {
            if (seat.modelUser) {
                // 查看用户
                [self getUserInfoWith:@(seat.modelUser.UserId.integerValue)];
            } else {
                // 申请上麦
                [self applyJoinChannel:false level:microInfo.Type];
            }
        }
    } else {
        if ([CXClientModel instance].room.isHost == YES) { // 红娘
            if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) {
                if (microInfo.Type == LiveRoomMicroInfoTypeMan) {
                    CXAllOnlineListViewController *vc = [[CXAllOnlineListViewController alloc] init];
                    vc.isMan = YES;
                    vc.view.frame = self.view.bounds;
                    [self addChildViewController:vc];
                    [self.view addSubview:vc.view];
                } else {
                    CXAllOnlineListViewController *vc = [[CXAllOnlineListViewController alloc] init];
                    vc.view.frame = self.view.bounds;
                    vc.isMan = NO;
                    [self addChildViewController:vc];
                    [self.view addSubview:vc.view];
                }
            } else {
                CXAllOnlineListViewController *vc = [[CXAllOnlineListViewController alloc] init];
                vc.view.frame = self.view.bounds;
                vc.isMan = NO;
                [self addChildViewController:vc];
                [self.view addSubview:vc.view];
            }
        } else {
            // 申请上麦
            [self applyJoinChannel:false level:microInfo.Type];
        }
        
    }
}
// 点击麦位管理按钮
- (void)clickMicroManagerAction {
    NSString * loginUserId = [CXClientModel instance].userId;

    LiveRoomMicroInfo * microInfo = [[CXClientModel instance].room microInfoForUser:loginUserId];
    
    if ( microInfo && microInfo.Type == LiveRoomMicroInfoTypeHost) {//红娘
        
        XYRoomAlreadApplyListView * view = [[NSBundle mainBundle] loadNibNamed:@"XYRoomAlreadApplyListView" owner:self options:nil].firstObject;
        
        [view show];
        
        return;
    }
    
    //在麦上执行下麦
    if (microInfo) {
        CXSystemAlertView *alertView = [CXSystemAlertView loadNib];
        kWeakSelf
        [alertView showAlertTitle:@"是否下麦" message:nil cancel:nil sure:^{
            //现在这里是直接下麦，这里按UI设计图，是
            SocketMessageLeaveSeat * leave = [SocketMessageLeaveSeat new];
            leave.Level = @(microInfo.Type);
            leave.Number = @(100);
            [[CXClientModel instance] sendSocketRequest:leave withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                if (request.noError && request.response.isSuccess) {
                    weakSelf.roomUIView.isChangeMir = false;
                } else if (request.response.Success.integerValue == 6) {
                    [weakSelf music_next];
                }
            }];
        }];
        [alertView show];
        
        return;
    } else {
        NSMutableDictionary<NSString*, LiveRoomMicroOrder*> * orders = nil;
        if ([CXClientModel instance].sex.integerValue == 1) {//需要判断自己是男是女
            orders = [CXClientModel instance].room.leftOrders;
        } else {
            orders = [CXClientModel instance].room.rightOrders;
        }

        LiveRoomMicroOrder * order = [orders objectForKey:loginUserId];
        if (order) {//这个时候，是没有加入到相亲聊天中，但是已经申请加入相亲，在队列中...
            //            [self toast:@"打开排麦队列未完成"];
            XYOwnerSortListView * view = [[NSBundle mainBundle] loadNibNamed:@"XYOwnerSortListView" owner:self options:nil].firstObject;
            [view show];
        }
        //不在队列加入队列
        else {//加入队列

            if (orders.allKeys.count >= 20) {
                [self toast:@"排队人数已满，请稍后。"];
                return;
            }

            //申请上麦
            [self applyJoinChannel:false];
        }
    }
}

#pragma mark - ============================ share ================================
- (void)share {
    if ([CXClientModel instance].room.IsHelpShare == YES) { // 助力分享
        CXLiveRoomShareHelpView *view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomShareHelpView" owner:self options:nil].firstObject;
        kWeakSelf
        view.didSelectedUser = ^(LiveRoomUser * _Nonnull user) {
            [weakSelf shareUser:user];
        };
        [view show];
    } else {
        // false为普通分享
        [self shareUser:nil];
    }
}

- (void)shareUser:(LiveRoomUser *)user {
    CXSocketMessageSystemShareParamRequest *join = [CXSocketMessageSystemShareParamRequest new];
    [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof CXSocketMessageSystemShareParamRequest * _Nonnull request) {
        if ([request.response.Success integerValue] == 1) {
            WXMiniProgramObject *object = [WXMiniProgramObject object];
            object.webpageUrl = request.response.WebPageUrl;
            object.userName = request.response.UserName;
            if (user) {
                object.path = [NSString stringWithFormat:@"%@&referrerId=%@&toGetId=%@&referrerName=%@&toGetIdName=%@",request.response.Path, user.UserId, [CXClientModel instance].userId, [CXClientModel instance].nickname, user.Name];
            } else {
                object.path = request.response.Path;
            }
            
            object.miniProgramType = WXMiniProgramTypeRelease;
            WXMediaMessage *message = [WXMediaMessage message];
            message.title = request.response.Title;
            //            message.thumbData = nil;  //兼容旧版本节点的图片，小于32KB，新版本优先
            [[SDWebImageManager sharedManager] loadImageWithURL:[NSURL URLWithString:request.response.ImageUrl] options:0 progress:nil completed:^(UIImage * _Nullable image, NSData * _Nullable data, NSError * _Nullable error, SDImageCacheType cacheType, BOOL finished, NSURL * _Nullable imageURL) {
                if (image) {
                    NSData *imageData = [self compressWithOriginalImage:image maxLength:30*1024];
                    UIImage *shareImage = [UIImage imageWithData:imageData];
                    [message setThumbImage:shareImage];
                }
                
                //使用WXMiniProgramObject的hdImageData属性
                message.mediaObject = object;
                SendMessageToWXReq *req = [[SendMessageToWXReq alloc] init];
                req.bText = NO;
                req.message = message;
                req.scene = WXSceneSession;  //目前只支持会话
                [WXApi sendReq:req completion:nil];
            }];
        }
    }];
}

- (NSData *)compressWithOriginalImage:(UIImage *)image maxLength:(NSUInteger)maxLength{
    CGFloat compression = 1;
    NSData *data = UIImageJPEGRepresentation(image, compression);
    if (data.length <= maxLength) {
        return data;
    }
    CGFloat max = 1;
    CGFloat min = 0;
    for (int i = 0; i < 6; ++i) {
        
        UIImage *resultImage = [UIImage imageWithData:data];
        CGFloat ratio = (CGFloat)maxLength / data.length;
        CGSize size = CGSizeMake((NSUInteger)(resultImage.size.width * sqrtf(ratio)), (NSUInteger)(resultImage.size.height * sqrtf(ratio)));
        UIGraphicsBeginImageContext(size);
        [resultImage drawInRect:CGRectMake(0, 0, size.width, size.height)];
        resultImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();

        data = UIImageJPEGRepresentation(resultImage, 1);
        compression = (max + min) / 2;

        if (data.length <= maxLength) {
            return data;
        }
        if (data.length < maxLength) {
            min = compression;
        } else if (data.length > maxLength * 0.9) {
            max = compression;
        } else {
            break;
        }

        data = UIImageJPEGRepresentation(resultImage, compression);
        if (data.length <= maxLength) {
            return data;
        }
    }
    return data;
}

#pragma mark - ===================== 红包 =====================

- (CXRedPacketStartView *)redpacketStartView {
    if (!_redpacketStartView) {
        _redpacketStartView = [[NSBundle mainBundle] loadNibNamed:@"CXRedPacketStartView" owner:self options:nil].firstObject;
    }
    
    return _redpacketStartView;
}

- (CXRedPacketProgressView *)redpacketProgressView {
    if (!_redpacketProgressView) {
        CXRedPacketProgressView *view = [[NSBundle mainBundle] loadNibNamed:@"CXRedPacketProgressView" owner:self options:nil].firstObject;
        kWeakSelf
        view.redpacketProgressRegularBlock = ^{
            // 红包规则
            CXRedPacketRegularView *view = [[NSBundle mainBundle] loadNibNamed:@"CXRedPacketRegularView" owner:self options:nil].firstObject;
            [view show];
        };
        view.redpacketHideBlock = ^{
            // 隐藏
            weakSelf.roomUIView.roomRedpackBtn.hidden = NO;
            [weakSelf hideRedpacketProgressView];
        };
        _redpacketProgressView = view;
        
        [self.view addSubview:_redpacketProgressView];
        [_redpacketProgressView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_offset(kNavHeight + 60);
            make.left.mas_offset(0);
            make.width.mas_offset(90);
            make.height.mas_offset(120);
        }];
    }
    
    return _redpacketProgressView;
}

- (void)showRedpacketProgressView {
    self.roomUIView.roomRedpackBtn.hidden = YES;
    self.redpacketProgressView.hidden = NO;
}

- (void)hideRedpacketProgressView {
    self.roomUIView.roomRedpackBtn.hidden = NO;
    self.redpacketProgressView.hidden = YES;
}

- (void)showRedPacketResult:(NSArray *)array {
    CXRedPacketAwardRecordView *recordView = [[NSBundle mainBundle] loadNibNamed:@"CXRedPacketAwardRecordView" owner:self options:nil].firstObject;
    recordView.recordArrays = [NSArray arrayWithArray:array];
    [recordView show];
}

#pragma mark - ===================== KTV =====================
- (void)musicLRCUpdate {
    //每隔一分钟执行一次打印
    // GCD定时器
    //设置时间间隔
    
    if (_music_timer) {
        dispatch_source_cancel(_music_timer);
    }
    NSMutableArray *lrcModels = [NSMutableArray arrayWithArray:[CXClientModel instance].currentMusicPlayingLRCModel.units];
    if (lrcModels.count == 0) {
        return;
    }
    kWeakSelf
//    NSTimeInterval period = 0.001f;
    NSTimeInterval period = 1;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    _music_timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(_music_timer, dispatch_walltime(NULL, 0), period * NSEC_PER_SEC, 0);
    // 事件回调
    dispatch_source_set_event_handler(_music_timer, ^{
        dispatch_async(dispatch_get_main_queue(), ^{
            NSInteger msec = [[CXClientModel instance].agoraEngineManager.engine getAudioMixingCurrentPosition];
            if (msec > 1000) {
                [CXClientModel instance].currentMusicPlayingProgress = msec;
            }
            weakSelf.musicView.pro_currentTime = msec * 0.001;
            if (weakSelf.isShowMusicLRCFullView == YES) {
                weakSelf.musicLRCFullView.pro_endTime = weakSelf.musicView.pro_endTime;
                weakSelf.musicLRCFullView.pro_currentTime = msec * 0.001;
            }
            if (msec > 0 && ceil(msec * 0.001) >= weakSelf.totalLRCProgress) {
                weakSelf.roomUIView.musicLRCShowView.lrc_music_lrcLabel.text = @"";
                [weakSelf music_next];
            }
            
            for (int i = 0; i < lrcModels.count; i ++) {
                DDAudioLRCUnit *obj = lrcModels[i];
                if (msec > obj.startTime && msec < obj.endTime) {
                    NSInteger start = obj.startTime;
                    NSInteger end = obj.endTime;
                    NSDictionary *dict = @{
                                           @"content": [NSString stringWithFormat:@"%@\n%@", obj.lrc, obj.next_lrc],
                                           @"currentTime" : [NSString stringWithFormat:@"%ld", (long)msec],
                                           @"startTime" : [NSString stringWithFormat:@"%ld", (long)start],
                                           @"endTime" : [NSString stringWithFormat:@"%ld", (long)end],
                                           @"songEndTime" : [NSString stringWithFormat:@"%ld", weakSelf.totalLRCProgress*1000],
                                           @"startTimeString" : obj.secString,
                                           };
                    NSString *jsonStr = [dict jsonStringEncoded];
//                    NSData *musicData = [jsonStr dataUsingEncoding:NSUTF8StringEncoding];
                    
                    CXSocketMessageMusicUpdateSongLyric *request = [CXSocketMessageMusicUpdateSongLyric new];
                    request.Lyric = jsonStr;
                    [[CXClientModel instance] sendSocketRequest:request withCallback:nil];
                    
//                    [[CXClientModel instance].agoraEngineManager.packetProcessing pushAudioExternalData:musicData];
                    weakSelf.roomUIView.musicLRCShowView.lrc_music_lrcLabel.text = [NSString stringWithFormat:@"%@\n%@", obj.lrc, obj.next_lrc];
                    if (weakSelf.isShowMusicLRCFullView == YES) {
                        weakSelf.musicLRCFullView.currentShowRow = obj.lrc_row;
                    }
                } else if (msec > obj.endTime) {
                    [lrcModels removeObject:obj];
                    break;
                }
            }
        });
    });
    
    dispatch_resume(_music_timer);
}

- (void)music_playStart {
    [self music_playStop];
    kWeakSelf
    [[CXClientModel instance].agoraEngineManager.engine enableInEarMonitoring:NO];
    CXSocketMessageMusicStartDownloadSong *request = [CXSocketMessageMusicStartDownloadSong new];
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
        if (request.response.isSuccess) {
//            [LEEAlert alert].config
//            .LeeTitle(@"歌曲下载中...")
//            .LeeShow();
            weakSelf.musicView.musicPlayStatus = music_downing;
            CXSocketMessageMusicModel *music = [CXClientModel instance].room.playing_SongInfo;
            [CXMusicDownloader downloadURL:music.LyricPath progress:^(NSProgress * _Nonnull downloadProgress) {
//                NSString *progress = [NSString stringWithFormat:@"下载:%f%%",100.0 * downloadProgress.completedUnitCount/downloadProgress.totalUnitCount];
            } success:^(NSURL * _Nonnull targetPath) {
                NSString *cur_lrcPath = [CXClientModel instance].room.playing_SongInfo.LyricPath;
                if (![[cur_lrcPath lastPathComponent] isEqualToString: targetPath.lastPathComponent]) {
                    weakSelf.musicView.musicPlayStatus = music_unplay;
                    return ;
                }
                NSData *data = [NSData dataWithContentsOfURL:targetPath];
                NSString *lrcText = [[NSString alloc] initWithData:data encoding:CFStringConvertEncodingToNSStringEncoding(kCFStringEncodingGB_18030_2000)];
                DDAudioLRC *lrc = [DDAudioLRCParser parserLRCText:lrcText];
                [CXClientModel instance].currentMusicPlayingLRCModel = lrc;
                [CXMusicDownloader downloadURL:music.SongPath progress:^(NSProgress * _Nonnull downloadProgress) {
//                    NSString *progress = [NSString stringWithFormat:@"下载:%f%%",100.0 * downloadProgress.completedUnitCount/downloadProgress.totalUnitCount];
                } success:^(NSURL * _Nonnull targetPath) {
                    CXSocketMessageMusicDownloadSongSuccess *request = [CXSocketMessageMusicDownloadSongSuccess new];
                    [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
                        if (request.response.isSuccess) {
                            // 当前播放的歌曲
                            NSString *cur_songPath = [CXClientModel instance].room.playing_SongInfo.SongPath;
                            if (![[cur_songPath lastPathComponent] isEqualToString: targetPath.lastPathComponent]) {
                                weakSelf.musicView.musicPlayStatus = music_unplay;
                                return ;
                            }
//                                [LEEAlert closeWithCompletionBlock:nil];
                            [CXClientModel instance].currentMusicPlayingSongPath = targetPath;
                            [[CXClientModel instance].agoraEngineManager.engine stopAudioMixing];
                            if ([[CXClientModel instance].agoraEngineManager.engine startAudioMixing:targetPath.absoluteString loopback:NO replace:NO cycle:1] == 0) {
                                if ([CXClientModel instance].isSocketManagerReconnect == YES) {
                                    [CXClientModel instance].isSocketManagerReconnect = NO;
                                    
                                    [[CXClientModel instance].agoraEngineManager.engine setAudioMixingPosition:[CXClientModel instance].currentMusicPlayingProgress];
                                }
                                
                                [[CXClientModel instance].agoraEngineManager.engine adjustAudioMixingVolume:MAX([CXClientModel instance].room.music_Volume, 30)];
                                
                                if ([CXClientModel instance].room.isConsertModel == YES) {
                                    // 耳返
                                    [[CXClientModel instance].agoraEngineManager.engine enableInEarMonitoring:YES];
                                    [[CXClientModel instance].agoraEngineManager.engine setInEarMonitoringVolume:100];
                                } else {
                                    [[CXClientModel instance].agoraEngineManager.engine enableInEarMonitoring:NO];
                                }
                                
                                AVURLAsset *audioAsset = [AVURLAsset URLAssetWithURL:targetPath options:nil];
                                weakSelf.musicView.pro_endTime = CMTimeGetSeconds(audioAsset.duration);
                                weakSelf.totalLRCProgress = CMTimeGetSeconds(audioAsset.duration);
                                weakSelf.musicView.pro_volum = MIN([CXClientModel instance].room.music_Volume, 30);
                                
                                [weakSelf musicLRCUpdate];
                                
                                weakSelf.musicView.musicPlayStatus = music_playing;
                            }
                            
                        } else {
                            if ([request.response.Success integerValue] == 2) {
                                [weakSelf musicShowErrorMessage:@"找不到演唱歌曲"];
                            } else if ([request.response.Success integerValue] == 3) {
                                [weakSelf musicShowErrorMessage:@"点歌人玫瑰不足，歌曲被取消"];
                            } else if ([request.response.Success integerValue] == 4) {
                                [weakSelf musicShowErrorMessage:@"已经切歌"];
                            }
                        }
                    }];
                }  failure:^(NSError * _Nonnull error) {
                    [weakSelf musicShowErrorMessage:@"下载失败"];
                }];
                
            } failure:^(NSError * _Nonnull error) {
                [weakSelf musicShowErrorMessage:@"下载失败"];
            }];
        }
    }];
}

- (void)music_playPause {
    if ([[CXClientModel instance].agoraEngineManager.engine pauseAudioMixing] == 0) {
        [[CXClientModel instance].agoraEngineManager.engine enableInEarMonitoring:NO];
        NSLog(@"暂停成功");
    }
}

- (void)music_playResume {
    if ([CXClientModel instance].currentMusicPlayingSongPath && [[CXClientModel instance].agoraEngineManager.engine resumeAudioMixing] == 0) {
        [[CXClientModel instance].agoraEngineManager.engine adjustAudioMixingVolume:MAX([CXClientModel instance].room.music_Volume, 30)];
        if ([CXClientModel instance].room.isConsertModel == YES) {
            // 耳返
            [[CXClientModel instance].agoraEngineManager.engine enableInEarMonitoring:YES];
            [[CXClientModel instance].agoraEngineManager.engine setInEarMonitoringVolume:100];
        }
    } else {
        [self music_playStart];
    }
    
}

- (void)music_playReplay {
    if ([CXClientModel instance].room.playing_SongInfo &&  [CXClientModel instance].currentMusicPlayingSongPath) {
        if ([CXClientModel instance].currentMusicPlayingSongPath) {
            self.musicView.musicPlayStatus = music_playing;
            [[CXClientModel instance].agoraEngineManager.engine stopAudioMixing];
            [[CXClientModel instance].agoraEngineManager.engine startAudioMixing:[CXClientModel instance].currentMusicPlayingSongPath.absoluteString loopback:NO replace:NO cycle:1];
            [[CXClientModel instance].agoraEngineManager.engine adjustAudioMixingVolume:MAX([CXClientModel instance].room.music_Volume, 30)];
            
            if ([CXClientModel instance].currentMusicPlayingLRCModel) {
                if ([CXClientModel instance].room.isConsertModel == YES) {
                    // 耳返
                    [[CXClientModel instance].agoraEngineManager.engine enableInEarMonitoring:YES];
                    [[CXClientModel instance].agoraEngineManager.engine setInEarMonitoringVolume:100];
                }
                
                [self musicLRCUpdate];
            }
            
            AVURLAsset *audioAsset = [AVURLAsset URLAssetWithURL: [CXClientModel instance].currentMusicPlayingSongPath options:nil];
            self.musicView.pro_endTime = CMTimeGetSeconds(audioAsset.duration);
            self.totalLRCProgress = CMTimeGetSeconds(audioAsset.duration);
            self.musicView.pro_volum = MIN([CXClientModel instance].room.music_Volume, 30);
            
            return;
        }
    }
    
    [self music_playStart];
}

- (void)music_playReconnectPlay {
    if ([CXClientModel instance].room.playing_SongInfo && [CXClientModel instance].currentMusicPlayingSongPath) {
        if ([CXClientModel instance].currentMusicPlayingSongPath) {
            [[CXClientModel instance].agoraEngineManager.engine stopAudioMixing];
            [[CXClientModel instance].agoraEngineManager.engine startAudioMixing:[CXClientModel instance].currentMusicPlayingSongPath.absoluteString loopback:NO replace:NO cycle:1];
            [[CXClientModel instance].agoraEngineManager.engine adjustAudioMixingVolume:MAX([CXClientModel instance].room.music_Volume, 30)];
            
            if ([CXClientModel instance].currentMusicPlayingLRCModel) {
                
                
                [self musicLRCUpdate];
            }
            
            AVURLAsset *audioAsset = [AVURLAsset URLAssetWithURL:[CXClientModel instance].currentMusicPlayingSongPath options:nil];
            self.musicView.pro_endTime = CMTimeGetSeconds(audioAsset.duration);
            self.totalLRCProgress = CMTimeGetSeconds(audioAsset.duration);
            self.musicView.pro_volum = MIN([CXClientModel instance].room.music_Volume, 30);
        }
    }
}

- (void)music_playStop {
    [CXClientModel instance].currentMusicPlayingSongPath = nil ;
    [CXClientModel instance].currentMusicPlayingProgress = 0;
    self.musicView.musicPlayStatus = music_unplay;
    if (_music_timer) {
        dispatch_source_cancel(_music_timer);
    }
    if ([[CXClientModel instance].agoraEngineManager.engine stopAudioMixing] == 0) {
        //        [CXClientModel instance].room.music_isPause = YES;
    }
    [[CXClientModel instance].agoraEngineManager.engine enableInEarMonitoring:NO];
}

// 切歌
- (void)music_next {
    [self music_playStop];
    kWeakSelf
    CXSocketMessageMusicNext *request = [CXSocketMessageMusicNext new];
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicNext * _Nonnull request) {
        if (request.response.Success.integerValue == 1) {
            CXSocketMessageMusicPlay *request = [CXSocketMessageMusicPlay new];
            [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicPlay * _Nonnull request) {
                [weakSelf.musicView reloadMusicView];
                if (request.response.isSuccess) {
                    if ([CXClientModel instance].room.playing_SongInfo.SongMode == 1) { // 原唱
                        [weakSelf music_playStart];
                    }
                } else {
                    if ([request.response.Success integerValue] == 3) {
                        [weakSelf.musicView removeFromSuperview];
                    }
                }

            }];

        } else {
            [weakSelf.musicView removeFromSuperview];
        }
    }];
}

- (void)musicShowErrorMessage:(NSString *)message {
    self.musicView.musicPlayStatus = music_unplay;
    
    [LEEAlert alert].config
    .LeeTitle(message)
    .LeeShow();
    kWeakSelf
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [LEEAlert closeWithCompletionBlock:nil];
    });
}

#pragma mark - ===================== 发送 easemob 消息 =====================
// 添加文本消息至listView
- (void)listViewAddTextModel:(NSString *)text user:(SocketMessageUserJoinRoom *)user {
    
    SocketMessageTextMessage *textModel = [SocketMessageTextMessage new];
    textModel.JoinRoomUser = user; //[ModelClient instance].room.mineInfoInRoom;
    textModel.text = text;
    
    [self.roomUIView.messageListView addModel:textModel];
}

#pragma mark - ========================= 系统权限 ============================
// 相册权限
- (BOOL)getVideoAuthStatus {
    __block BOOL enable;
    AVAuthorizationStatus videoAuthStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeVideo];
    if (videoAuthStatus == AVAuthorizationStatusNotDetermined) {// 未询问用户是否授权
        //第一次询问用户是否进行授权
        [[AVAudioSession sharedInstance] requestRecordPermission:^(BOOL granted) {
            // CALL YOUR METHOD HERE - as this assumes being called only once from user interacting with permission alert!
            if (granted) {
                // Microphone enabled code
                enable = YES;
            }
            else {
                // Microphone disabled code
                enable = NO;
            }
        }];
    }
    else if(videoAuthStatus == AVAuthorizationStatusRestricted || videoAuthStatus == AVAuthorizationStatusDenied) {// 未授权
        enable = NO;
        [self showSetAlertView:@"相机权限未开启" content:@"相机权限未开启，请进入系统【设置】>【隐私】>【相机】中打开开关,开启相机功能"];
    }
    else{// 已授权
        NSLog(@"已经授权");
        enable = YES;
    }
    
    return enable;
}
// 麦克风权限
- (BOOL)getAudioAuthStatus {
    __block BOOL enable;
    AVAuthorizationStatus videoAuthStatus = [AVCaptureDevice authorizationStatusForMediaType:AVMediaTypeAudio];
    if (videoAuthStatus == AVAuthorizationStatusNotDetermined) {// 未询问用户是否授权
        //第一次询问用户是否进行授权
        [[AVAudioSession sharedInstance] requestRecordPermission:^(BOOL granted) {
            // CALL YOUR METHOD HERE - as this assumes being called only once from user interacting with permission alert!
            if (granted) {
                // Microphone enabled code
                enable = YES;
            }
            else {
                // Microphone disabled code
                enable = NO;
            }
        }];
    }
    else if(videoAuthStatus == AVAuthorizationStatusRestricted || videoAuthStatus == AVAuthorizationStatusDenied) {// 未授权
        enable = NO;
        [self showSetAlertView:@"麦克风权限未开启" content:@"麦克风权限未开启，请进入系统【设置】>【隐私】>【麦克风】中打开开关,开启麦克风功能"];
    }
    else{// 已授权
        NSLog(@"已经授权");
        enable = YES;
    }
    
    return enable;
}

//提示用户进行麦克风使用授权
- (void)showSetAlertView:(NSString *)title content:(NSString *)content {
    kWeakSelf
    [LEEAlert alert].config
    .LeeTitle(title)
    .LeeContent(content)
    .LeeCancelAction(@"取消", ^{
        NSString * loginUserId = [CXClientModel instance].userId;
        
        LiveRoomMicroInfo * microInfo = [[CXClientModel instance].room microInfoForUser:loginUserId];
        
        if (microInfo && microInfo.Type == LiveRoomMicroInfoTypeHost) {//红娘
            [weakSelf leaveRoom];
        } else {
            NSIndexPath * seatIndex = [[CXClientModel instance].room.userSeats objectForKey:[CXClientModel instance].userId];
            if (seatIndex) {//下麦
                NSString * uid = [CXClientModel instance].userId;
                NSIndexPath * index = [[CXClientModel instance].room.userSeats objectForKey:uid];
                LiveRoomMicroInfo * seat = [[CXClientModel instance].room.seats objectForKey:index];
                SocketMessageLeaveSeat * leave = [SocketMessageLeaveSeat new];
                leave.Level = @(seat.Type);
                leave.Number = @(seat.Number);
                [[CXClientModel instance] sendSocketRequest:leave withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                    if (request.response.Success.integerValue == 6) {
                        [weakSelf music_next];
                    }
                    
                }];
            }
        }
    })
    .LeeAction(@"设置", ^{
        //跳入当前App设置界面
        [[UIApplication sharedApplication] openURL:[NSURL URLWithString:UIApplicationOpenSettingsURLString] options:@{} completionHandler:nil];
    })
    .LeeShow();
}


#pragma mark - ===================== UI Action =====================
- (void)roomUIView_topAndbottomBtnAction:(NSInteger)tag {
    kWeakSelf
    switch (tag) {
        case 20: // 退出
        {
            [self alertTitle:@"确定离开房间？" message:@"" confirm:@"确定" cancel:@"取消" confirm:^{
                [weakSelf leaveRoom];
            } cancel:nil];
        }
            break;
        case 21: // 房间设置
        {
            if ([CXClientModel instance].room.UserIdentity != GameUserIdentityNormal) {
                [self showRoomInfoViewWithRoomInfo];
            } else {
                [self getUserInfoWith:[CXClientModel instance].room.RoomData.OwnerUserId];
            }
            
        }
            break;
        case 23: // 守护团
            [self gotoGuardRankWithUserId:[CXClientModel instance].room.RoomData.OwnerUserId.stringValue];
            break;
        case 10: // 聊天
        {
            [self.inputBar.textInput becomeFirstResponder];
            [self.view addSubview:self.coverView];
        }
            break;
        case 11: // 分享
        {
            [self share];
        }
            break;
        case 12: // 好友
        {
            CXFriendViewController * messageVC = [CXFriendViewController new];
            messageVC.isShowBackBtn = YES;
            [self.navigationController pushViewController:messageVC animated:YES];
        }
            break;
        case 13: // KTV
        {
            [self addSubViewWithMusicView];

            if ([[NSUserDefaults standardUserDefaults] boolForKey:@"CXLiveRoomCXGameMusicViewFirst"] == NO) {
                CXGameMusicGuideView *guideView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicGuideView" owner:self options:nil].firstObject;
                [guideView show];
            }
        }
            break;
        case 14: // 送礼
        {
            if ([CXClientModel instance].room.IsFirstCharge == YES) {
                [self roomRechargeRechargeType:1 seatUser:nil];
            } else {
                [self sendGiftWithUser:nil status:@"0" isSeat:YES];
            }
        }
            break;
        case 30: // 上麦管理
        {
            [self clickMicroManagerAction];
        }
            break;
        case 31: // 推荐
        {
            self.recommendView.dataSources = [NSMutableArray array];

            [self.recommendView show];
        }
            break;
        case 32: // 红包
        {
            [self showRedpacketProgressView];
        }
            break;
        default:
            break;
    }
}

- (void)roomUIView_seatsBtnAction:(NSInteger)tag microInfo:(LiveRoomMicroInfo *)microInfo {
    switch (tag) {
        case 10: //点击麦位
            [self clickMicroInfo:microInfo];
            break;
        case 11: //粉丝弹框
        {
            CXLiveRoomSeatFansView *view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomSeatFansView" owner:self options:nil].firstObject;
            view.microInfo = microInfo;
            view.didSelectedFansModel = ^(CXSocketMessageSeatsFansModel * _Nonnull model) {
                [AppController showUserProfile:[NSString stringWithFormat:@"%ld", (long)model.UserId]];
            };
            [view show];
        }
            break;
        case 12: // 送单枝玫瑰
            if (microInfo.modelUser) {
                [self sendGiftOneRoseWithUserId:microInfo.modelUser.UserId];
            }
            break;
        case 13: // 送礼
        {
            LiveRoomUser * user = [LiveRoomUser new];
            user.UserId = microInfo.modelUser.UserId;
            user.Name = microInfo.modelUser.Name;
            user.HeadImageUrl = microInfo.modelUser.HeadImageUrl;
            user.modelSeat = [[LiveRoomMicroInfo alloc] init];
            user.modelSeat.Number = microInfo.Number;
            user.modelSeat.Type = microInfo.Type;
            
            if ([CXClientModel instance].room.IsFirstCharge == YES) {
                [self roomRechargeRechargeType:2 seatUser:user];
            } else {
                [self sendGiftWithUser:user status:@"0" isSeat:YES];
            }
        }
            break;
        case 14: // 踢下麦
        {
            NSIndexPath * seatIndex = [[CXClientModel instance].room.userSeats objectForKey:microInfo.modelUser.UserId];
            if (seatIndex) {//下麦
                [self closeMicroWithMicroInfo: microInfo];
            }
        }
            break;
        case 15: // 静音\闭麦
        {
            LiveRoomUser *user = microInfo.modelUser;
            [self muteOrCloseMicroWithUser:user];
        }
            break;
        case 16: // 加好友
        {
            CXAddFriendViewController *vc = [[CXAddFriendViewController alloc] init];
            vc.nickname = microInfo.modelUser.Name;
            vc.user_id = microInfo.modelUser.UserId;
            vc.user_avatar = microInfo.modelUser.HeadImageUrl;
            vc.is_room = @"1";
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        default:
            break;
    }
}

// 管理员 房间信息弹框
- (void)showRoomInfoViewWithRoomInfo {
    __weak typeof(self) wself = self;
    UIAlertController *sheet = [UIAlertController alertControllerWithTitle:@"房间信息" message:@"" preferredStyle:UIAlertControllerStyleActionSheet];
//    NSString *lockRoom = [[CXClientModel instance].room.RoomData.RoomLock boolValue] == YES ? @"解锁房间" : @"锁定房间";
//    [sheet addAction:[UIAlertAction actionWithTitle:lockRoom style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//        SocketMessageSetRoomLock * lock = [SocketMessageSetRoomLock new];
//        lock.IsLock = @(![[CXClientModel instance].room.RoomData.RoomLock boolValue]);
//        [[CXClientModel instance] sendSocketRequest:lock withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
//            if (request.response.isSuccess) {
//                [wself toast:@"设置成功"];
//            } else {
//                [wself toast:@"设置失败"];
//            }
//        }];
//    }]];
    [sheet addAction:[UIAlertAction actionWithTitle:@"房间设置" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        CXLiveRoomSetupViewController *vc = [[CXLiveRoomSetupViewController alloc] init];
        [wself.navigationController pushViewController:vc animated:YES];
    }]];
    [sheet addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    [self presentViewController:sheet animated:YES completion:nil];
}


#pragma mark - Setter/Getter

- (CXLiveRoomUIView *)roomUIView {
    if (!_roomUIView) {
        _roomUIView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomUIView" owner:self options:nil].lastObject;
        kWeakSelf
        _roomUIView.bottomBtnAction = ^(NSInteger tag) {
            [weakSelf roomUIView_topAndbottomBtnAction: tag];
        };
        _roomUIView.topBtnAction = ^(NSInteger tag) {
            [weakSelf roomUIView_topAndbottomBtnAction: tag];
        };
        
        _roomUIView.messageClickUserInfoBlock = ^(NSString * _Nonnull userID) {
            [weakSelf getUserInfoWith:[NSNumber numberWithString:userID]];
        };
        
        _roomUIView.roomSeatsViewBlock = ^(NSInteger tag, LiveRoomMicroInfo * _Nonnull microInfo) {
            [weakSelf roomUIView_seatsBtnAction:tag microInfo:microInfo];
        };
        _roomUIView.musicLRCShowViewBlock = ^(BOOL isSendGift) {
            if (isSendGift == YES) {
                if ([CXClientModel instance].room.playing_SongInfo.ConsertUserId <= 0 || [CXClientModel instance].room.playing_SongInfo.ConsertUserId == [CXClientModel instance].userId.integerValue ) {
                    return;
                }
                // 送礼
                [weakSelf sendGiftOneRoseWithUserId:[NSString stringWithFormat:@"%ld", [CXClientModel instance].room.playing_SongInfo.ConsertUserId]];
            } else {
                // 查看全部歌词
                if ([CXClientModel instance].currentMusicPlayingLRCModel.units.count > 0) {
                    [weakSelf showMusicLRCFullView];
                }
            }
        };
        
        _roomUIView.messageLongPressUserInfoBlock = ^(NSString * _Nonnull userName) {
            if (userName.length > 0) {
                [weakSelf.inputBar.textInput becomeFirstResponder];
                weakSelf.inputBar.placeholderLabel.hidden = YES;
                weakSelf.inputBar.textInput.text = [NSString stringWithFormat:@"@%@ ",userName];
                [weakSelf.view addSubview:weakSelf.coverView];
            }
        };
        
        // 轮播
        _roomUIView.didSelectedCycleItem = ^(CXHomeRoomBannerModel * _Nonnull cycleItem) {
            if ([cycleItem.link_type integerValue] == 3) { // 房间内UI类型
                if ([cycleItem.ui_type integerValue] == 1) { // 砸蛋
//                    CXLiveRoomLuckDrawViewController *vc = [CXLiveRoomLuckDrawViewController new];
//                    [weakSelf.navigationController pushViewController:vc animated:YES];
                } else if ([cycleItem.ui_type integerValue] == 1001) { // 首充
                    [weakSelf showRechargeSheetViewRMB:[CXClientModel instance].room.rmb diamond:[CXClientModel instance].room.diamond iosflag:[CXClientModel instance].room.iosflag chargeid:[CXClientModel instance].room.chargeId rechargeType:0 seatUser:nil];
                }
            } else {
                NSString * linkurl = [cycleItem.linkurl copy];
                if (linkurl.length) {
                    NSRange range = [linkurl rangeOfString:@"?"];
                    if (range.location < linkurl.length) {
                        linkurl = [linkurl stringByAppendingFormat:@"&uid=%@&token=%@", [CXClientModel instance].userId, [CXClientModel instance].token];
                    } else {
                        linkurl = [linkurl stringByAppendingFormat:@"?uid=%@&token=%@", [CXClientModel instance].userId, [CXClientModel instance].token];
                    }
                    
                    CXBaseWebViewController *webVC = [[CXBaseWebViewController alloc] initWithURL:[NSURL URLWithString:linkurl]];
                    webVC.title = cycleItem.title;
                    [weakSelf.navigationController pushViewController:webVC animated:YES];
                    
                }
            }
        };
    }
    
    return _roomUIView;
}

- (void)setTextViewToolbar {
    _inputBar = [[GameInputBar alloc] initWithFrame:CGRectMake(0,kScreenHeight, kScreenWidth, 50)];
    _inputBar.textViewMaxLine = 5;
    _inputBar.placeholderLabel.text = @"来聊几句吧...";
    __weak typeof (self) wself = self;
    _inputBar.sendContentBlock = ^(NSString * _Nonnull sendContent) {
        if ([CXClientModel instance].room.IsDisableMsg.boolValue == YES) {
            [CXTools showAlertWithMessage:@"你已经被禁言，禁止发送消息"];
            return;
        }
        [[CXClientModel instance] sendRoomText:sendContent success:^(EasemobRoomMessage * _Nonnull msg) {
            if (msg.error) {
                [[CXClientModel instance].easemob joinRoom:[CXClientModel instance].room.HuanxinRoomId];
            } else {
                SocketMessageTextMessage *textModel = [SocketMessageTextMessage new];
                
                textModel.JoinRoomUser = [CXClientModel instance].room.mineInfoInRoom;
                textModel.text = sendContent;
                
                [wself.roomUIView.messageListView addModel:textModel];
                
                CXSocketMessageSystemSendMessageRequest *request = [CXSocketMessageSystemSendMessageRequest new];
                request.Msg = sendContent;
                [[CXClientModel instance] sendSocketRequest:request withCallback:nil];
            }
        }];
        [wself.inputBar sendSuccessEndEditing];
        [wself.coverView removeFromSuperview];
    };
    _coverView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight)];
    [_coverView sw_whenTapped:^{
        [wself.view endEditing:true];
        [wself.coverView removeFromSuperview];
    }];
    [_coverView addSubview:_inputBar];
}

- (void)addSubViewWithMusicView {
    [self.musicView removeFromSuperview];
    
    [self.view addSubview:self.musicView];
    self.musicView.room = [CXClientModel instance].room;
}

- (CXGameMusicView *)musicView {
    if (!_musicView) {
        _musicView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicView" owner:self options:nil].lastObject;
        _musicView.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
        kWeakSelf
        _musicView.musicViewActionBlock = ^(CXGameMusicActionStatus status) {
            switch (status) {
                case music_action_play: // 播放
                    [weakSelf music_playStart];
                    break;
                case music_action_pause: // 暂停
                    [weakSelf music_playPause];
                    break;
                case music_action_resume: // 恢复
                    [weakSelf music_playResume];
                    break;
                case music_action_replay: // 重新播放
                    [weakSelf music_playReplay];
                    break;
                case music_action_next: // 下一曲
                    [weakSelf music_next];
                    break;
                default:
                    break;
            }
        };
    }
    
    return _musicView;
}

- (void)showMusicLRCFullView {
    self.isShowMusicLRCFullView = YES;
    [self.musicLRCFullView removeFromSuperview];
    [self.view addSubview:self.musicLRCFullView];
    self.musicLRCFullView.lrcModel = [CXClientModel instance].currentMusicPlayingLRCModel;
}

- (CXGameMusicLRCFullView *)musicLRCFullView {
    if (!_musicLRCFullView) {
        _musicLRCFullView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicLRCFullView" owner:self options:nil].firstObject;
        _musicLRCFullView.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
        kWeakSelf
        _musicLRCFullView.musicLRCFullViewCloseBlock = ^{
            weakSelf.isShowMusicLRCFullView = NO;
        };
    }
    
    return _musicLRCFullView;
}


- (CXLiveRoomRecommendView *)recommendView {
    if (!_recommendView) {
        _recommendView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomRecommendView" owner:self options:nil].firstObject;
        _recommendView.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
        kWeakSelf
        _recommendView.joinRecommendRoom = ^(NSString * _Nonnull roomId) {
            [weakSelf.roomUIView.messageListView clearModel];
//            [weakSelf.app leaveRoom];
//            [weakSelf.app joinRoom:roomId];
            
            [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleAudience];
//            [[CXClientModel instance].agoraEngineManager.engine setVideoSource:nil];
            [[CXClientModel instance].easemob leaveRoom];
            [weakSelf music_playStop];
            
//            self.app.isJoinedRoom = NO;
            
//            if (_timer) {
//                dispatch_source_cancel(_timer);
//            }
            if (weakSelf.music_timer) {
                dispatch_source_cancel(weakSelf.music_timer);
            }

            [weakSelf.musicView removeFromSuperview];

//            [self.app.client.listener removeAllObjects];

//            [[NSNotificationCenter defaultCenter] removeObserver:self];
            
            // 关闭跑马灯
//            [_horizontalMarquee marqueeOfSettingWithState:MarqueeShutDown_H];
            
        //    [self.app.gameVCStack dismissViewControllerAnimated:YES completion:nil];
//            [self dismissViewControllerAnimated:YES completion:nil];
               
//            [[UIApplication sharedApplication] endReceivingRemoteControlEvents];
            
            SocketMessageJoinRoom * joinRoom = [SocketMessageJoinRoom new];
            joinRoom.RoomId = roomId;
            [[CXClientModel instance] sendSocketRequest:joinRoom withCallback:nil];
        };
    }
    
    return _recommendView;
}

- (void)showApplySeatAlertView {
    if (self.applySeatArrays.count <= 0) {
        return;
    }
    
    if (self.applySeatAlertViewIsShow == NO) {
        self.applySeatAlertViewIsShow = YES;
        [self.view addSubview:self.applySeatAlertView];
        
        SocketMessageMicroOrder *order = self.applySeatArrays.firstObject;
        self.applySeatAlertView.microOrder = order;
        
        CGFloat width = [[NSString stringWithFormat:@"%@申请上麦", order.MicroOrderData.User.Name] sizeForFont:[UIFont systemFontOfSize:12] size:CGSizeMake(kScreenWidth, 15) mode:0].width;
        [self.applySeatAlertView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.mas_equalTo(36);
            make.height.mas_equalTo(40);
            make.width.mas_equalTo(MIN(width + 130, kScreenWidth - 120));
            make.bottom.mas_offset(-(48 + kBottomArea));
        }];
        
        kWeakSelf
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [weakSelf.applySeatAlertView removeFromSuperview];
            weakSelf.applySeatAlertViewIsShow = NO;
            [weakSelf showApplySeatAlertView];
        });
        
        [self.applySeatArrays removeFirstObject];
    }
}

- (CXLiveRoomBottomApplySeatAlertView *)applySeatAlertView {
    if (!_applySeatAlertView) {
        _applySeatAlertView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomBottomApplySeatAlertView" owner:self options:nil].firstObject;
        kWeakSelf
        _applySeatAlertView.applySeatAlertViewBlock = ^(BOOL isSure, BOOL isCancel, SocketMessageMicroOrder *microOrder) {
            if (isCancel == YES) {
                [weakSelf.applySeatAlertView removeFromSuperview];
                weakSelf.applySeatAlertViewIsShow = NO;
                [weakSelf showApplySeatAlertView];
            } else if (isSure) {
                __block BOOL isJoinSeat = NO; // 能否直接上麦
                
                if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) { // 相亲
                    NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:1];
                    if (microOrder.MicroOrderData.Sex.integerValue == 2) {
                        index = [NSIndexPath indexPathForRow:0 inSection:2];
                    }
                    LiveRoomMicroInfo *microInfo = [[CXClientModel instance].room.seats objectForKey:index];
                    if (microInfo && microInfo.modelUser) {
                        isJoinSeat = NO;
                    } else {
                        isJoinSeat = YES;
                    }
                } else {
                    [[CXClientModel instance].room.seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, LiveRoomMicroInfo * _Nonnull obj, BOOL * _Nonnull stop) {
                        if (!obj.modelUser || obj.modelUser.UserId.length <= 0) {
                            isJoinSeat = YES;
                            *stop = YES;
                        }
                    }];
                }
                
                if (isJoinSeat == YES) {
                    // 上麦
                    SocketMessageJoinSeat *join = [SocketMessageJoinSeat new];
                    join.TargetUserId = [microOrder.MicroOrderData.User.UserId numberValue];
                    [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                        if (request.response.isSuccess) {
                            [weakSelf.applySeatAlertView removeFromSuperview];
                            weakSelf.applySeatAlertViewIsShow = NO;
                            [weakSelf showApplySeatAlertView];
                        }
                    }];
                } else {
                    // 进入麦序
                    XYRoomAlreadApplyListView * view = [[NSBundle mainBundle] loadNibNamed:@"XYRoomAlreadApplyListView" owner:weakSelf options:nil].firstObject;
                    if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) {
                        view.listType = microOrder.MicroOrderData.Sex.integerValue;
                    } else {
                        view.listType = 2;
                    }
                    
                    [view show];
                }
                
            }
        };
    }
    
    return _applySeatAlertView;
}

@end

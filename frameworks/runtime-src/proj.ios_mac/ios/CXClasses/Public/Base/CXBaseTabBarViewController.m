//
//  CXBaseTabBarViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/22.
//

#import "CXBaseTabBarViewController.h"

#import "CXBaseNavigationController.h"

#import "CXHomeViewController.h"
#import "CXFriendViewController.h"
#import "CXMineViewController.h"

#import "CXInviteMikeView.h"

#import "AppController.h"

@interface CXBaseTabBarViewController () <EMChatManagerDelegate, EMNotificationsDelegate>

@property (nonatomic) BOOL isViewAppear;

@property (nonatomic, strong) CXHomeViewController *homeController;
@property (nonatomic, strong) CXFriendViewController *friendController;
@property (nonatomic, strong) CXMineViewController *mineController;

@property (nonatomic, strong) NSMutableArray *inviteMikeArrays; // 红娘邀请上麦列表
@property (nonatomic, strong) CXInviteMikeView *mikeView;

@end

@implementation CXBaseTabBarViewController

- (void)dealloc
{
    [[EMClient sharedClient].chatManager removeDelegate:self];
    [[EMNotificationHelper shared] removeDelegate:self];
    
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(back) name:kNSNotificationCenter_CXBaseTabBarViewController_leaveOut object:nil];
    
    [self setupChildController];
    
    //监听消息接收，主要更新会话tabbaritem的badge
    [[EMClient sharedClient].chatManager addDelegate:self delegateQueue:nil];
    
    [self checkUserDataUpdate];
    
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBarHidden = YES;
    self.isViewAppear = YES;
    [self _loadTabBarItemsBadge];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    
    self.isViewAppear = NO;
}

- (void)login {
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Member/autologin" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (responseObject) {
            CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"info"]];
            [CXClientModel instance].userId = user.user_id;
        }
    }];
}

- (void)back {
    [AppController setOrientation:@""];
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (void)setupChildController {
    
    self.homeController = [CXHomeViewController new];
    [self setChildViewController:self.homeController title:@"首页" imageName:@"home_tabbar"];
    self.friendController = [CXFriendViewController new];
    [self setChildViewController:self.friendController title:@"好友" imageName:@"friend_tabbar"];
    self.mineController = [CXMineViewController new];
    [self setChildViewController:self.mineController title:@"我的" imageName:@"mine_tabbar"];
}

- (void)setChildViewController:(UIViewController *)childController title:(NSString *)title imageName:(NSString *)imageName {
    childController.tabBarItem.image = [[UIImage imageNamed:imageName] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    childController.tabBarItem.selectedImage = [[UIImage imageNamed:[NSString stringWithFormat:@"%@_selected", imageName]] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    [childController.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName : [UIColor colorWithHexString:@"#333333"], NSFontAttributeName : [UIFont systemFontOfSize:10]} forState:UIControlStateSelected];
    [childController.tabBarItem setTitleTextAttributes:@{NSForegroundColorAttributeName : [UIColor colorWithHexString:@"#333333"], NSFontAttributeName : [UIFont systemFontOfSize:10]} forState:UIControlStateNormal];
    childController.title = title;
    CXBaseNavigationController *nav = [[CXBaseNavigationController alloc] initWithRootViewController:childController];
    [self addChildViewController:nav];
}

#pragma mark - EMChatManagerDelegate

- (void)messagesDidReceive:(NSArray *)aMessages
{
    if (self.isViewAppear) {
        [self _loadConversationTabBarItemBadge];
    }
}

- (void)conversationListDidUpdate:(NSArray *)aConversationList
{
    [self _loadConversationTabBarItemBadge];
}

#pragma mark - EMNotificationsDelegate

- (void)didNotificationsUnreadCountUpdate:(NSInteger)aUnreadCount
{
    if (aUnreadCount > 0) {
        self.friendController.tabBarItem.badgeValue = @(aUnreadCount).stringValue;
    } else {
        self.friendController.tabBarItem.badgeValue = nil;
    }
}

#pragma mark - Private

- (void)_loadConversationTabBarItemBadge
{
    NSArray *conversations = [[EMClient sharedClient].chatManager getAllConversations];
    NSInteger unreadCount = 0;
    for (EMConversation *conversation in conversations) {
        unreadCount += conversation.unreadMessagesCount;
    }
    
    NSString *unreadCountStr = unreadCount > 0 ? @(unreadCount).stringValue : nil;
    self.friendController.tabBarItem.badgeValue = unreadCountStr;
//    [EMRemindManager updateApplicationIconBadgeNumber:unreadCount];
}

- (void)_loadTabBarItemsBadge
{
    [self _loadConversationTabBarItemBadge];
    
    [self didNotificationsUnreadCountUpdate:[EMNotificationHelper shared].unreadCount];
}

#pragma mark - ===================== 心跳处理 ========================
- (void)checkUserDataUpdate {
    return ;
    
    self.inviteMikeArrays = [NSMutableArray array];
    
    // GCD定时器
    static dispatch_source_t _timer;
    
    //设置时间间隔
    NSTimeInterval period = 3.f;
    dispatch_queue_t queue = dispatch_get_global_queue(DISPATCH_QUEUE_PRIORITY_DEFAULT, 0);
    _timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    dispatch_source_set_timer(_timer, dispatch_walltime(NULL, 0), period * NSEC_PER_SEC, 0);
    // 事件回调
    dispatch_source_set_event_handler(_timer, ^{
        dispatch_async(dispatch_get_main_queue(), ^{
            //网络请求 doSomeThing...
            [CXHTTPRequest POSTWithURL:@"/index.php/Api/MemberOnline/addMember" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
                if (!error) {
                    NSArray *inviteList = [NSArray modelArrayWithClass:[CXInviteMikeModel class] json:responseObject[@"data"][@"list"]];
                    if (inviteList.count > 0) {
                        [self.inviteMikeArrays addObjectsFromArray:inviteList];
                        if (self.inviteMikeArrays.count > 0) {
                            [self showInviteMikeView];
                        }
                    }
                }
            }];
        });
    });
    
    dispatch_resume(_timer);
}

#pragma mark - 展示红娘邀请弹框
- (void)showInviteMikeView {
    if (self.mikeView) {
        [self.mikeView removeFromSuperview];
    }
    
    [[UIApplication sharedApplication].keyWindow addSubview:self.mikeView];
    [CXClientModel instance].isAgreeInviteJoinRoom = NO;
    self.mikeView.mikeModel = self.inviteMikeArrays[0];
    kWeakSelf
    self.mikeView.cancelActionBlcok = ^(CXInviteMikeModel * _Nonnull model) {
        NSDictionary *param = @{
            @"inviterId":model.hongId,
            @"inviteeId":[CXClientModel instance].userId,
            @"invite_id":model.invite_id,
            @"status":@"2",
            @"roomId":model.roomId,
        };
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Keng/inviteHandle" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                [weakSelf.inviteMikeArrays removeObject:model];
                if (weakSelf.inviteMikeArrays.count > 0) {
                    weakSelf.mikeView.mikeModel = weakSelf.inviteMikeArrays[0];
                } else {
                    [weakSelf.mikeView removeFromSuperview];
                }
            }
        }];
    };
    self.mikeView.agreeActionBlcok = ^(CXInviteMikeModel * _Nonnull model) {
        NSDictionary *param = @{
            @"inviterId":model.hongId,
            @"inviteeId":[CXClientModel instance].userId,
            @"invite_id":model.invite_id,
            @"status":@"1",
            @"roomId":model.roomId,
        };
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Keng/inviteHandle" parameters:param callback:nil];
        
        [CXClientModel instance].currentAgreeInviteMikeModel = model;
        
        [weakSelf.inviteMikeArrays removeAllObjects];
        [weakSelf.mikeView removeFromSuperview];
        
        if ([CXClientModel instance].room.isJoinedRoom == YES) {
            [[CXClientModel instance] leaveRoomCallBack:^(NSString * _Nonnull roomId, BOOL success) {
                if (success) {
                    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                        [CXClientModel instance].isAgreeInviteJoinRoom = YES;
                        [AppController joinRoom:model.roomId];
                    });
                }
            }];
        } else {
            [CXClientModel instance].isAgreeInviteJoinRoom = YES;
            [AppController joinRoom:model.roomId];
        }
    };
}

- (CXInviteMikeView *)mikeView {
    if (!_mikeView) {
        _mikeView = [[[NSBundle mainBundle] loadNibNamed:@"CXInviteMikeView" owner:self options:nil] lastObject];
        _mikeView.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    }
    return _mikeView;
}


@end

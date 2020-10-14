//
//  CXFriendViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/27.
//

#import "CXFriendViewController.h"
#import "CXFriendMessageViewController.h"
#import "CXSystemMessageViewController.h"
#import "CXFriendApplyViewController.h"

@interface CXFriendViewController () <CXPageViewControllerDataSource>

@property (nonatomic, strong) CXFriendMessageViewController *conversationVC;
@property (nonatomic, strong) UIButton *applyBtn;
@property (nonatomic, strong) UIButton *clearBtn;
@property (nonatomic, strong) UILabel *applyCountLabel;

@property (nonatomic, strong) NSMutableArray *titleArray;

@property (nonatomic, assign) NSInteger messageCount;
@property (nonatomic, assign) NSInteger systemCount;

@end

@implementation CXFriendViewController

- (void)viewDidLoad {
    
    // Do any additional setup after loading the view.
    
    CXFriendMessageViewController *friendVC = [CXFriendMessageViewController new];
    friendVC.isConversation = YES;
    _conversationVC = friendVC;
    
    CXSystemMessageViewController *systemVC = [CXSystemMessageViewController new];
    
    self.viewControllerClasses = [NSArray arrayWithObjects:friendVC, systemVC, nil];
    
    NSDictionary *friendItem = @{@"title":@"好友", @"count":@"0"};
    NSDictionary *systemItem = @{@"title":@"系统", @"count":@"0"};
    _titleArray = [NSMutableArray arrayWithObjects:friendItem, systemItem, nil];
    self.titles = [NSArray arrayWithArray:_titleArray];
    self.itemWidth = 60;
    
    [super viewDidLoad];
    
    [self setupSubViews];
    
    [self setupNavBack];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBarHidden = YES;
    
    [self getUnReadCountData];
    
    [self _loadConversationTabBarItemBadge];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBarHidden = NO;
}

- (void)_loadConversationTabBarItemBadge
{
    NSArray *conversations = [[EMClient sharedClient].chatManager getAllConversations];
    NSInteger unreadCount = 0;
    for (EMConversation *conversation in conversations) {
        unreadCount += conversation.unreadMessagesCount;
    }
    
    NSDictionary *friendItem = @{@"title":@"好友", @"count":unreadCount > 0 ? @(unreadCount).stringValue : @"0"};
    [self.titleArray replaceObjectAtIndex:0 withObject:friendItem];
    self.titles = [NSArray arrayWithArray:self.titleArray];
    [self reloadData];
    
    _messageCount = unreadCount;
    
    [self reloadTabBarBadge];
}

- (void)getUnReadCountData {
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Friend/messageNum" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSString *system_num = responseObject[@"data"][@"system_num"];
            NSDictionary *systemItem = @{@"title":@"系统", @"count":system_num.integerValue > 0 ? system_num : @"0"};
            [weakSelf.titleArray replaceObjectAtIndex:1 withObject:systemItem];
            weakSelf.titles = [NSArray arrayWithArray:weakSelf.titleArray];
            [weakSelf reloadData];
            
            NSString *my_apply = responseObject[@"data"][@"my_apply"];
            NSString *apply_my = responseObject[@"data"][@"apply_my"];
            
            weakSelf.systemCount = [system_num integerValue] + [my_apply integerValue] + [apply_my integerValue];
            
            [weakSelf reloadTabBarBadge];
            
            NSInteger applyCount = [my_apply integerValue] + [apply_my integerValue];
            if (applyCount > 0) {
                weakSelf.applyCountLabel.hidden = NO;
                weakSelf.applyCountLabel.text = [NSString stringWithFormat:@"%ld", applyCount];
            } else {
                weakSelf.applyCountLabel.hidden = YES;
            }
        }
    }];
}

- (void)reloadTabBarBadge {
    NSInteger unreadCount = _messageCount + _systemCount;
    NSString *unreadCountStr = unreadCount > 0 ? @(MIN(unreadCount, 99)).stringValue : nil;
    self.tabBarItem.badgeValue = unreadCountStr;
}

- (void)setupNavBack {
    if (_isShowBackBtn) {
        UIButton *back = [[UIButton alloc] initWithFrame:CGRectMake(0, kStatusHeight, 44, 44)];
        [back setImage:[UIImage imageNamed:@"back_gray"] forState:UIControlStateNormal];
        [back addTarget:self action:@selector(back) forControlEvents:UIControlEventTouchUpInside];
        [self.view addSubview:back];
    }
}

- (void)back {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - CXPageViewControllerDataSource
- (CGRect)pageControllerMenuViewRect {
    return CGRectMake((SCREEN_WIDTH - (60 * 2 + 30))/2, kStatusHeight, 60 * 2 + 30, 44);
}

- (CGRect)pageControllerContentViewRect {
    return CGRectMake(0, kStatusHeight+44, SCREEN_WIDTH, self.view.bounds.size.height - (kStatusHeight+44));
}

#pragma mark - Action
- (void)applyAction {
    CXFriendApplyViewController *vc = [CXFriendApplyViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)clearAction {
    [_conversationVC clearConversationList];
}

- (void)setupSubViews {
    UIButton *apply = [[UIButton alloc] initWithFrame:CGRectMake(SCREEN_WIDTH - 20 - 15, kStatusHeight + 12, 20, 20)];
    [apply setImage:[UIImage imageNamed:@"friend_applyBtn"] forState:UIControlStateNormal];
    [apply addTarget:self action:@selector(applyAction) forControlEvents:UIControlEventTouchUpInside];
    _applyBtn = apply;
    [self.view addSubview:apply];
    
    UIButton *clear = [[UIButton alloc] initWithFrame:CGRectMake(SCREEN_WIDTH - 20 - 15 - 17 - 20, kStatusHeight + 12, 20, 20)];
    [clear setImage:[UIImage imageNamed:@"friend_clearBtn"] forState:UIControlStateNormal];
    [clear addTarget:self action:@selector(clearAction) forControlEvents:UIControlEventTouchUpInside];
    _clearBtn = clear;
    [self.view addSubview:clear];
    
    UILabel *label = [[UILabel alloc] initWithFrame:CGRectMake(SCREEN_WIDTH - 20 - 15 + 15, kStatusHeight + 12 - 5, 14, 14)];
    label.font = [UIFont systemFontOfSize:8];
    label.textAlignment = NSTextAlignmentCenter;
    label.textColor = [UIColor whiteColor];
    label.backgroundColor = UIColorHex(0xF70505);
    label.layer.masksToBounds = YES;
    label.layer.cornerRadius = 7;
    _applyCountLabel = label;
    [self.view addSubview:label];
    _applyCountLabel.hidden = YES;
}
@end

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

@property (nonatomic) BOOL isViewAppear;

@end

@implementation CXFriendViewController

- (void)dealloc
{
    
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewDidLoad {
    
    // Do any additional setup after loading the view.
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(reloadFriendMessage:) name:kNSNotificationCenter_CXFriendViewController_unreadCount object:nil];
    
    CXFriendMessageViewController *friendVC = [CXFriendMessageViewController new];
    friendVC.isConversation = YES;
    _conversationVC = friendVC;
    
//    CXSystemMessageViewController *systemVC = [CXSystemMessageViewController new];
    
    self.viewControllerClasses = [NSArray arrayWithObjects:friendVC, nil];
    
    NSDictionary *friendItem = @{@"title":@"好友", @"count":@"0"};
//    NSDictionary *systemItem = @{@"title":@"系统", @"count":@"0"};
    _titleArray = [NSMutableArray arrayWithObjects:friendItem, nil];
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
    
    _isViewAppear = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBarHidden = NO;
    
    _isViewAppear = NO;
}

- (void)reloadFriendMessage:(NSNotification *)object {
    if (_isViewAppear == YES) {
        NSString *unread = object.userInfo[@"unreadCount"];
        NSDictionary *systemItem = @{@"title":@"好友", @"count":unread.integerValue > 0 ? unread : @""};
        [self.titleArray replaceObjectAtIndex:0 withObject:systemItem];
        self.titles = [NSArray arrayWithArray:self.titleArray];
        [self reloadData];
    }
}

- (void)getUnReadCountData {
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Friend/messageNum" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            
            NSString *my_apply = responseObject[@"data"][@"my_apply"];
            NSString *apply_my = responseObject[@"data"][@"apply_my"];
            
            weakSelf.systemCount = [my_apply integerValue] + [apply_my integerValue];
                        
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
    [self alertTitle:@"是否要删除所有消息" message:@"删除后不可恢复" confirm:@"确定" cancel:@"取消" confirm:^{
        [_conversationVC clearConversationList];
    } cancel:nil];
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

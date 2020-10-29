//
//  CXMineGuardListViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "CXMineGuardListViewController.h"
#import "CXMineGuardListCell.h"
#import "CXMineGuardRenewView.h"
#import "CXMineGuardAutomaticView.h"

@interface CXMineGuardListViewController () <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, assign) NSInteger page;

@end

@implementation CXMineGuardListViewController

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXMineGuardListCell" bundle:nil] forCellReuseIdentifier:@"CXMineGuardListCellID"];
    
    _dataSources = [NSMutableArray array];
    _page = 1;
    
    kWeakSelf
    self.mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        weakSelf.page = 1;
        [weakSelf getGuardListData];
    }];
    self.mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        weakSelf.page++;
        [weakSelf getGuardListData];
    }];
    
    [self getGuardListData];
    
    // 监听支付宝支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(aliPayCallBack:) name:kNSNotificationCenter_CXRechargeViewController_alipay object:nil];

    // 监听微信支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(weChatCallBack:) name:kNSNotificationCenter_CXRechargeViewController_weixin object:nil];
}

- (void)aliPayCallBack:(NSNotification *)info {
    if ([[info.object objectForKey:@"resultStatus"] integerValue] == 9000) {
        [self toast:@"支付成功"];
        [self getGuardListData];
    } else {
        [self toast:@"支付失败"];
    }
}
- (void)weChatCallBack:(NSNotification *)info {
    NSDictionary *obj = info.object;
    switch ([[obj objectForKey:@"errCode"] integerValue]) {
        case WXSuccess:
            [self toast:@"支付成功"];
            [self getGuardListData];
            break;
        case WXErrCodeUserCancel:
            [self toast:@"取消支付"];
            break;
        default:
            [self toast:@"支付失败"];
            break;
    }
}

- (void)getGuardListData {
    NSDictionary *param = @{
        @"type": _isMineGuard == YES ? @"1" : @"2",
        @"page": [NSString stringWithFormat:@"%ld", _page],
    };
    
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Guard/getList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXUserModel class] json:responseObject[@"data"][@"guard_memberlist"]];
            if (weakSelf.page == 1) {
                [weakSelf.dataSources removeAllObjects];
            }
            
            [weakSelf.dataSources addObjectsFromArray:array];
            
            if (array.count < [responseObject[@"data"][@"pageInfo"][@"pageNum"] integerValue]) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
            [weakSelf.mainTableView reloadData];
        }
    }];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXMineGuardListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXMineGuardListCellID"];
    CXUserModel *user = self.dataSources[indexPath.row];
    cell.isMineGuard = self.isMineGuard;
    cell.model = user;
    
    kWeakSelf
    cell.avatarTapGestureBlock = ^{
        if ([user.room_id integerValue] > 0) {
            [AppController joinRoom:user.room_id];
        } else {
            [AppController showUserProfile:user.user_id target:weakSelf];
        }
    };
    cell.automaticActionBlock = ^{
        [weakSelf automaticAction:user];
    };
    cell.renewActionBlock = ^{
        [weakSelf renewAction:user];
    };
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXUserModel *user = self.dataSources[indexPath.row];
    [AppController showUserProfile:user.user_id];
}

- (void)renewAction:(CXUserModel *)user {
    CXMineGuardRenewView *renewView = [[NSBundle mainBundle] loadNibNamed:@"CXMineGuardRenewView" owner:self options:nil].firstObject;
    renewView.userId = user.user_id;
    [renewView show];
}

- (void)automaticAction:(CXUserModel *)user {
    CXMineGuardAutomaticView *automaticView = [[NSBundle mainBundle] loadNibNamed:@"CXMineGuardAutomaticView" owner:self options:nil].firstObject;
    automaticView.user = user;
    [automaticView show];
}

@end

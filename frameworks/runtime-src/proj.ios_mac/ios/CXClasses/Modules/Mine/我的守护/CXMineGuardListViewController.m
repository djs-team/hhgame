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
        [AppController joinRoom:user.room_id];
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
    renewView.userId= user.user_id;
    [renewView show];
}

- (void)automaticAction:(CXUserModel *)user {
    CXMineGuardAutomaticView *automaticView = [[NSBundle mainBundle] loadNibNamed:@"CXMineGuardAutomaticView" owner:self options:nil].firstObject;
    automaticView.user = user;
    [automaticView show];
}

@end

//
//  CXNearbyViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/5.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXNearbyViewController.h"
#import "CXNearbyListCell.h"

@interface CXNearbyViewController () <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger totalPage;

@end

@implementation CXNearbyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self setupSubViews];
    
    _dataSources = [NSMutableArray array];
    
    _page = 1;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self getNearbyListData];
}

- (void)getNearbyListData {
    NSDictionary *param = @{
        @"page" : [NSString stringWithFormat:@"%ld", (long)_page],
        @"pagenum" : @"20",
        @"type" : [NSString stringWithFormat:@"%ld", (long)self.vcType],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"index.php/Api/MemberOnline/findOnlineList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
        if (error) {
            weakSelf.totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXUserModel class] json:responseObject[@"data"][@"list"]];
            if (weakSelf.page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            
            [weakSelf.mainTableView reloadData];
            
            if (weakSelf.totalPage <= weakSelf.page) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
        }
    }];
}

#pragma mark - UITableViewDataSource/Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXNearbyListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXNearbyListCellID"];
    CXUserModel *model = self.dataSources[indexPath.row];
    cell.model = model;
    kWeakSelf
    cell.followActionBlock = ^{
        NSDictionary *param = @{
            @"type" : [model.attention integerValue] == 1 ? @"2" : @"1",
            @"userided" : model.user_id,
        };
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Attention/attention_member" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                for (CXUserModel *tempModel in self.dataSources) {
                    if ([model.user_id isEqualToString:tempModel.user_id]) {
                        if ([tempModel.attention integerValue] == 1) {
                            tempModel.attention = @"2";
                        } else{
                            tempModel.attention = @"1";
                        }
                    }
                }
                [weakSelf.mainTableView reloadData];
            }
        }];
    };
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXUserModel *model = self.dataSources[indexPath.row];
    [AppController showUserProfile:model.user_id target:self];
}

#pragma mark - MJRefresh
- (void)headerRefresh {
    _page = 1;
    [self getNearbyListData];
}

- (void)footerRefresh {
    if (_page == _totalPage) {
        return;
    }
    _page++;
    [self getNearbyListData];
}


- (void)setupSubViews {
    self.title = @"发现";
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXNearbyListCell" bundle:nil] forCellReuseIdentifier:@"CXNearbyListCellID"];
    self.mainTableView.tableFooterView = [UIView new];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    
}

@end

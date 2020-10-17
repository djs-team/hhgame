//
//  CXExchangeBlueRoseRecordViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/4/17.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXExchangeBlueRoseRecordViewController.h"
#import "CXExchangeBlueRoseRecordCell.h"

@interface CXExchangeBlueRoseRecordViewController () <UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) NSMutableArray *dataSources;
@property (nonatomic, assign) NSInteger page;

@end

@implementation CXExchangeBlueRoseRecordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    _bgView.layer.cornerRadius = 10;
    
    _mainTableView.dataSource = self;
    
    _dataSources = [NSMutableArray array];
    
    _page = 1;
    
    self.title = @"兑换记录（24小时内）";
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXExchangeBlueRoseRecordCell" bundle:nil] forCellReuseIdentifier:@"CXExchangeBlueRoseRecordCellID"];
    self.mainTableView.tableFooterView = [UIView new];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    
    [self getRecordListData];
}

- (void)headerRefresh {
    _page = 1;
    [self getRecordListData];
}

- (void)footerRefresh {
    _page++;
    [self getRecordListData];
}
- (void)getRecordListData {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/blueExchangeInfo" parameters:@{@"page": [NSString stringWithFormat:@"%ld", (long)_page]} callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXFriendGiftModel class] json:responseObject[@"data"][@"list"]];
            if (self->_page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }

            [weakSelf.mainTableView reloadData];

            if (array.count < [responseObject[@"data"][@"pageNum"] integerValue]) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
        }
    }];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXExchangeBlueRoseRecordCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXExchangeBlueRoseRecordCellID"];
    CXFriendGiftModel *model = self.dataSources[indexPath.row];
    cell.timeLabel.text = [model.time componentsSeparatedByString:@" "].lastObject;
    cell.giftNameLabel.text = model.gift_name;
    cell.giftNumLabel.text = [NSString stringWithFormat:@"%@个",model.gift_num];
    cell.roseNumLabel.text = [NSString stringWithFormat:@"%0.0f枝", [model.value floatValue]];
    return cell;
}

@end

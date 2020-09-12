//
//  CXConsumeRecordViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import "CXConsumeRecordViewController.h"
#import "CXRechargeModel.h"
#import "CXConsumeRecordCell.h"

@interface CXConsumeRecordViewController () <UITableViewDelegate,UITableViewDataSource>

@property (nonatomic,strong)UITableView *tableView;
@property (nonatomic,strong)NSMutableArray<CXConsumeRecordModel *> *dataArray;
@property (nonatomic,assign)NSInteger currentPage;

@end

@implementation CXConsumeRecordViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = @"消费记录";
    
    [self setUI];
    
    [self loadRecordList];
}

- (void)loadRecordList {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/order/mbdetails" parameters:@{@"page": [NSString stringWithFormat:@"%ld",(long)_currentPage]} callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.tableView.mj_header endRefreshing];
        [weakSelf.tableView.mj_footer endRefreshing];
        if (!error) {
            if (weakSelf.currentPage == 1) {
                [weakSelf.dataArray removeAllObjects];
            }
            NSArray *array = [NSArray modelArrayWithClass:[CXConsumeRecordModel class] json:responseObject[@"data"][@"money_record"]];
            [weakSelf.dataArray addObjectsFromArray:array];
            
            [weakSelf.tableView reloadData];
            
            if (array.count <= 0 ) {
                [weakSelf.tableView.mj_footer endRefreshingWithNoMoreData];
            }
        }
    }];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}

- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    CXConsumeRecordCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CELL"];
    if (!cell) {
        cell = [[CXConsumeRecordCell alloc]init];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    CXConsumeRecordModel *model = self.dataArray[indexPath.row];
    cell.model = model;
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 65*SCALE_W;
}

- (void)refreshData{
    self.currentPage = 1;
    [self loadRecordList];
}
- (void)loadMore{
    self.currentPage +=1;
    [self loadRecordList];
}

- (void)setUI{
    [self.view addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view.mas_left).offset(0);
        make.right.equalTo(self.view.mas_right).offset(0);
        make.top.equalTo(self.view.mas_top).offset(0);
        make.bottom.equalTo(self.view.mas_bottom).offset(0);
    }];
}

#pragma mark -setter
- (UITableView*)tableView{
    if (!_tableView) {
        _tableView = [[UITableView alloc]init];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.tableFooterView = [UIView new];
        _tableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(refreshData)];
        _tableView.mj_footer = [MJRefreshBackNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(loadMore)];
    }
    return _tableView;
}

- (NSMutableArray*)dataArray{
    if (!_dataArray) {
        _dataArray = [[NSMutableArray alloc]init];
    }
    return _dataArray;
}

@end

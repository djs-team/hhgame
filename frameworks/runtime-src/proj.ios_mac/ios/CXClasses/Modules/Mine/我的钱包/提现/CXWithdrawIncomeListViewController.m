//
//  CXWithdrawIncomeListViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXWithdrawIncomeListViewController.h"
#import "CXWithdrawIncomeListCell.h"
#import "CXWithdrawSelectedDateViewController.h"
#import "CXMineWalletModel.h"

@interface CXWithdrawIncomeListViewController () <UITableViewDelegate, UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIImageView *bgImage;
@property (weak, nonatomic) IBOutlet UILabel *numberLabel;
@property (weak, nonatomic) IBOutlet UITableView *mainTableView;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger totalPage;

@property (nonatomic, strong) NSDate *b_time;
@property (nonatomic, strong) NSDate *e_time;

@end

@implementation CXWithdrawIncomeListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    [self setupSubViews];
}

- (void)setIncomeType:(CXWithdrawIncomeListType)incomeType {
    _page = 1;
    _b_time = [NSDate date];
    _e_time = [NSDate date];
    
    _incomeType = incomeType;
    
    if (incomeType == income) {
        self.title = @"收入明细";
        [self getIncomelistDate];
    } else if (incomeType == tixian) {
        self.title = @"提现明细";
        [self getCashlistDate];
    } else if (incomeType == exchange) {
        self.title = @"兑换明细";
        [self getExchangeDate];
    } else if (incomeType == redpack_incom) {
        self.title = @"红包收入明细";
        [self redpack_getIncomelistDate];
    } else if (incomeType == redpack_tixian) {
        self.title = @"红包提现明细";
        [self redpack_getCashlistDate];
    } else if (incomeType == redpack_exchange) {
        self.title = @"红包兑换明细";
        [self redpack_getExchangeDate];
    }
}

- (void)getIncomelistDate {
    NSDictionary *param = @{
        @"page": [NSString stringWithFormat:@"%ld", (long)_page],
        @"size": @"10",
        @"stime": [_b_time stringWithFormat:@"yyyy-MM-dd"],
        @"etime": [_e_time stringWithFormat:@"yyyy-MM-dd"],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/incomelist" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.numberLabel.text = [NSString stringWithFormat:@"¥%.2f",[responseObject[@"data"][@"total_price"] doubleValue]];
            weakSelf.totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXMineWalletCashModel class] json:responseObject[@"data"][@"list"]];
            if (weakSelf.page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            
            [self.mainTableView reloadData];
            
            [weakSelf.mainTableView.mj_header endRefreshing];
            [weakSelf.mainTableView.mj_footer endRefreshing];
            if (array.count < 10) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
        }
    }];
}

- (void)redpack_getIncomelistDate {
    NSDictionary *param = @{
        @"page": [NSString stringWithFormat:@"%ld", (long)_page],
        @"size": @"10",
        @"stime": [_b_time stringWithFormat:@"yyyy-MM-dd"],
        @"etime": [_e_time stringWithFormat:@"yyyy-MM-dd"],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/redpacket_incomelist" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.numberLabel.text = [NSString stringWithFormat:@"¥%.2f",[responseObject[@"data"][@"total_price"] doubleValue]];
            weakSelf.totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXMineWalletCashModel class] json:responseObject[@"data"][@"list"]];
            if (weakSelf.page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            
            [self.mainTableView reloadData];
            
            [weakSelf.mainTableView.mj_header endRefreshing];
            [weakSelf.mainTableView.mj_footer endRefreshing];
            if (array.count < 10) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
        }
    }];
}

- (void)getCashlistDate {
    NSDictionary *param = @{
        @"page": [NSString stringWithFormat:@"%ld", (long)_page],
        @"size": @"10",
        @"stime": [_b_time stringWithFormat:@"yyyy-MM-dd"],
        @"etime": [_e_time stringWithFormat:@"yyyy-MM-dd"],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/cashlist" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.numberLabel.text = [NSString stringWithFormat:@"¥%.2f",[responseObject[@"data"][@"total_price"] doubleValue]];
            weakSelf.totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXMineWalletCashModel class] json:responseObject[@"data"][@"cash_list"]];
            if (weakSelf.page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            
            [self.mainTableView reloadData];
            
            [weakSelf.mainTableView.mj_header endRefreshing];
            [weakSelf.mainTableView.mj_footer endRefreshing];
            if (self->_totalPage <= self->_page) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
        }
    }];
}
- (void)redpack_getCashlistDate {
    NSDictionary *param = @{
        @"page": [NSString stringWithFormat:@"%ld", (long)_page],
        @"size": @"10",
        @"stime": [_b_time stringWithFormat:@"yyyy-MM-dd"],
        @"etime": [_e_time stringWithFormat:@"yyyy-MM-dd"],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/redpacket_cashlist" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.numberLabel.text = [NSString stringWithFormat:@"¥%.2f",[responseObject[@"data"][@"total_price"] doubleValue]];
            weakSelf.totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXMineWalletCashModel class] json:responseObject[@"data"][@"cash_list"]];
            if (weakSelf.page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            
            [self.mainTableView reloadData];
            
            [weakSelf.mainTableView.mj_header endRefreshing];
            [weakSelf.mainTableView.mj_footer endRefreshing];
            if (self->_totalPage <= self->_page) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
        }
    }];
}

- (void)getExchangeDate {
    NSDictionary *param = @{
        @"page": [NSString stringWithFormat:@"%ld", (long)_page],
        @"size": @"10",
        @"stime": [_b_time stringWithFormat:@"yyyy-MM-dd"],
        @"etime": [_e_time stringWithFormat:@"yyyy-MM-dd"],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/exchangeInfo" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.numberLabel.text = [NSString stringWithFormat:@"¥%.2f",[responseObject[@"data"][@"total_price"] doubleValue]];
            weakSelf.totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXMineWalletExchangeModel class] json:responseObject[@"data"][@"cash_list"]];
            if (weakSelf.page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            
            [self.mainTableView reloadData];
            
            [weakSelf.mainTableView.mj_header endRefreshing];
            [weakSelf.mainTableView.mj_footer endRefreshing];
            if (self->_totalPage <= self->_page) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
        }
    }];
}

- (void)redpack_getExchangeDate {
    NSDictionary *param = @{
        @"page": [NSString stringWithFormat:@"%ld", (long)_page],
        @"size": @"10",
        @"stime": [_b_time stringWithFormat:@"yyyy-MM-dd"],
        @"etime": [_e_time stringWithFormat:@"yyyy-MM-dd"],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/redpacket_exchangeInfo" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.numberLabel.text = [NSString stringWithFormat:@"¥%.2f",[responseObject[@"data"][@"total_price"] doubleValue]];
            weakSelf.totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXMineWalletExchangeModel class] json:responseObject[@"data"][@"cash_list"]];
            if (weakSelf.page == 1) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            
            [self.mainTableView reloadData];
            
            [weakSelf.mainTableView.mj_header endRefreshing];
            [weakSelf.mainTableView.mj_footer endRefreshing];
            if (self->_totalPage <= self->_page) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
        }
    }];
}

#pragma mark - <UITableViewDelegate, UITableViewDataSource>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXWithdrawIncomeListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXWithdrawIncomeListCellID"];
    
    if (self.incomeType == income) {
        CXMineWalletCashModel *model = self.dataSources[indexPath.row];
        NSString *leftStr = [NSString stringWithFormat:@"%@\n支付人：%@\n消耗玫瑰：%@支",model.time, model.initiatorId, model.coin];
        cell.leftLabel.text = leftStr;
        NSString *type = model.type;
        NSString *rightStr = [NSString stringWithFormat:@"%@\n接收人：%@\n收到分成：¥%@", type, model.recipientId, model.divide];
        cell.rightLabel.text = rightStr;
    } else if (self.incomeType == tixian) {
        CXMineWalletCashModel *model = self.dataSources[indexPath.row];
        NSString *type = @"支付宝";
        if ([model.type integerValue] == 2) {
            type = @"微信";
        }
        NSString *leftStr = [NSString stringWithFormat:@"%@\n提现金额： ¥%@\n%@：%@\n手续费：$%@ \n税费：$%@",model.cashtime, model.cash, type,model.apliuserid, model.cash_price, model.cash_tax];
        cell.leftLabel.text = leftStr;
        NSString *status = @"待审核";
        if ([model.status integerValue] == 1) {
            status = @"审核通过";
        } else if ([model.status integerValue] == 2) {
            status = @"审核驳回";
        }
        NSString *rightStr = [NSString stringWithFormat:@"状态：%@\n到账金额：$%@",status, model.real_cash];
        cell.rightLabel.text = rightStr;
        
    } else if (self.incomeType == redpack_incom) {
        CXMineWalletCashModel *model = self.dataSources[indexPath.row];
        NSString *leftStr = [NSString stringWithFormat:@"%@\n来自:%@",model.type, model.room_name];;
        cell.leftLabel.text = leftStr;
        NSString *rightStr = [NSString stringWithFormat:@"%@\n%@",model.value, model.time];
        cell.rightLabel.text = rightStr;
       
    } else if (self.incomeType == redpack_tixian) {
        CXMineWalletCashModel *model = self.dataSources[indexPath.row];
        NSString *type = @"支付宝";
        if ([model.type integerValue] == 2) {
            type = @"微信";
        }
        NSString *leftStr = [NSString stringWithFormat:@"%@\n提现金额： ¥%@\n%@：%@\n手续费：$%@ \n税费：$%@",model.cashtime, model.cash, type,model.apliuserid, model.cash_price, model.cash_tax];
        cell.leftLabel.text = leftStr;
        NSString *status = @"待审核";
        if ([model.status integerValue] == 1) {
            status = @"审核通过";
        } else if ([model.status integerValue] == 2) {
            status = @"审核驳回";
        }
        NSString *rightStr = [NSString stringWithFormat:@"状态：%@\n到账金额：$%@",status, model.real_cash];
        cell.rightLabel.text = rightStr;
          
    }
    else if (self.incomeType == redpack_exchange) {
        CXMineWalletExchangeModel *model = self.dataSources[indexPath.row];
        NSString *leftStr = [NSString stringWithFormat:@"%@\n兑换金额： ¥%@\n余额：¥%@",model.time, model.diamond,model.after_diamond];
        cell.leftLabel.text = leftStr;
        
        NSString *rightStr = [NSString stringWithFormat:@"    \n兑换玫瑰数：%@\n玫瑰总数：%@",model.coin, model.after_coin];
        cell.rightLabel.text = rightStr;

    }
    else {
        CXMineWalletExchangeModel *model = self.dataSources[indexPath.row];
        NSString *leftStr = [NSString stringWithFormat:@"%@\n兑换金额： ¥%@\n余额：¥%@",model.time, model.diamond,model.after_diamond];
        cell.leftLabel.text = leftStr;
        
        NSString *rightStr = [NSString stringWithFormat:@"    \n兑换玫瑰数：%@\n玫瑰总数：%@",model.coin, model.after_coin];
        cell.rightLabel.text = rightStr;
    }
   
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.incomeType == income) {
        return 74;
    } else if (self.incomeType == tixian) {
        return 116;
    } else {
        return 74;
    }
}

#pragma mark - Action
- (void)rightClick {
    CXWithdrawSelectedDateViewController *vc = [[CXWithdrawSelectedDateViewController alloc] init];
    kWeakSelf
    vc.selectedDateBlock = ^(NSDate * _Nonnull b_time, NSDate * _Nonnull e_time) {
        NSString *b_timeStr = [b_time stringWithFormat:@"yyyy年MM月dd日"];
        NSString *e_timeStr = [e_time stringWithFormat:@"yyyy年MM月dd日"];
        weakSelf.timeLabel.text = [NSString stringWithFormat:@"%@-%@",b_timeStr, e_timeStr];
        
        weakSelf.page = 1;
        weakSelf.b_time = b_time;
        weakSelf.e_time = e_time;
        
        if (self.incomeType == income) {
            [self getIncomelistDate];
        } else if (self.incomeType == tixian) {
            [self getCashlistDate];
        } else if (self.incomeType == exchange) {
            [self getExchangeDate];
        }
    };
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - MJRefresh
- (void)headerRefresh {
    _page = 1;
    
    if (_incomeType == income) {
        [self getIncomelistDate];
    } else if (_incomeType == tixian) {
        [self getCashlistDate];
    } else if (_incomeType == exchange) {
        [self getExchangeDate];
    } else if (_incomeType == redpack_incom) {
        [self redpack_getIncomelistDate];
    } else if (_incomeType == redpack_tixian) {
        [self redpack_getCashlistDate];
    } else if (_incomeType == redpack_exchange) {
        [self redpack_getExchangeDate];
    }
}

- (void)footerRefresh {
    if (_page == _totalPage) {
        return;
    }
    _page++;
    
    if (_incomeType == income) {
        [self getIncomelistDate];
    } else if (_incomeType == tixian) {
        [self getCashlistDate];
    } else if (_incomeType == exchange) {
        [self getExchangeDate];
    } else if (_incomeType == redpack_incom) {
        [self redpack_getIncomelistDate];
    } else if (_incomeType == redpack_tixian) {
        [self redpack_getCashlistDate];
    } else if (_incomeType == redpack_exchange) {
        [self redpack_getExchangeDate];
    }
}


#pragma mark - subViews
- (void)setupSubViews {
    UIButton *rightBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 80, 44)];
    [rightBtn addTarget:self action:@selector(rightClick) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
    [rightBtn setTitle:@"日期查询" forState:UIControlStateNormal];
    [rightBtn setTitleColor:UIColorHex(0x333333) forState:UIControlStateNormal];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:14.0f];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
        
    self.mainTableView.estimatedRowHeight = 90;
    self.mainTableView.rowHeight = UITableViewAutomaticDimension;
    
    self.mainTableView.layer.cornerRadius = 12;
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXWithdrawIncomeListCell" bundle:nil] forCellReuseIdentifier:@"CXWithdrawIncomeListCellID"];

    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    self.mainTableView.tableFooterView = [UIView new];
    
    NSString *b_time = [[NSDate date] stringWithFormat:@"yyyy年MM月dd日"];
    self.timeLabel.text = [NSString stringWithFormat:@"%@-%@",b_time, b_time];
    
    self.bgImage.layer.masksToBounds = YES;
    self.bgImage.layer.cornerRadius = 10;
    self.bgImage.image = [UIImage x_gradientImageWithSize:CGSizeMake(SCREEN_WIDTH - 40, 160) Color1:UIColorHex(0x95397B) color2:UIColorHex(0x391B5B)];
}

@end

//
//  CXNearbyRankViewController.m
//  
//
//  Created by mahong yang on 2020/4/9.
//

#import "CXNearbyRankViewController.h"
#import "CXNearbyRankCell.h"
#import "CXNearbyRankSongCell.h"
#import "CXNearbyRankHeaderView.h"

@interface CXNearbyRankViewController () <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UIView *segmentedView;
@property (weak, nonatomic) IBOutlet UIButton *charmBtn;
@property (weak, nonatomic) IBOutlet UIButton *songBtn;
@property (weak, nonatomic) IBOutlet UIButton *blueRoseBtn;

@property (unsafe_unretained, nonatomic) IBOutlet UIButton *dayBtn;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *weekBtn;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *monthBtn;
@property (unsafe_unretained, nonatomic) IBOutlet NSLayoutConstraint *indicatorLeadingLayout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tableViewTopLayout;

@property (nonatomic, assign) NSInteger segmentedType;
@property (nonatomic, assign) NSInteger buttonType;

@property (nonatomic, strong) NSMutableArray *dataSources;
@property (nonatomic, assign) NSInteger page;

@end

@implementation CXNearbyRankViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    _segmentedType = 1;
    _buttonType = 1;
    
    self.view.backgroundColor = [UIColor whiteColor];

    _mainTableView.dataSource = self;
    _mainTableView.delegate = self;
    
    _page = 1;
    _dataSources = [NSMutableArray array];
    
    [self setupSubViews];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self headerRefresh];
}

#pragma mark - MJRefresh
- (void)headerRefresh {
    _page = 1;
    [self getRankData];
}

- (void)footerRefresh {
    _page++;
    [self getRankData];
}
#pragma mark - Http
- (void)getRankData {
//    CXNearbyRankListRequest *request = [CXNearbyRankListRequest new];
//    request.page = [NSString stringWithFormat:@"%ld", (long)_page];
//    request.pagenum = @"20";
//    request.type = [NSNumber numberWithInteger:_segmentedType];
//    request.date = [NSNumber numberWithInteger:_buttonType];
//    kWeakSelf
//    [self sendRequest:request withCallback:^(__kindof CXNearbyRankListRequest * _Nonnull request) {
//        if (!request.error && request.phpResponse.isSuccess) {
//            NSArray *array = [NSArray modelArrayWithClass:[CXNearbyModel class] json:request.phpResponse.data[@"list"]];
//            if (self->_page == 1) {
//                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
//            } else {
//                [weakSelf.dataSources addObjectsFromArray:array];
//            }
//            [weakSelf reloadHeaderView];
//            [weakSelf.mainTableView reloadData];
//
//            [weakSelf.mainTableView.mj_header endRefreshing];
//            [weakSelf.mainTableView.mj_footer endRefreshing];
//            if (array.count < 20) {
//                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
//            }
//        }
//    }];
}

- (void)reloadHeaderView {
//    kWeakSelf
//    if (self.dataSources.count == 0) {
//        self.mainTableView.tableHeaderView = [UIView new];
//    } else if (_segmentedType == 3) {
//        CXNearbyRankHeaderView *headerView = [[NSBundle mainBundle] loadNibNamed:@"CXNearbyRankHeaderView" owner:self options:nil].lastObject;
//        headerView.frame = CGRectMake(0, 0, kScreenWidth, 190);
//        [headerView reloadFirstModel:[self getModel:0] secondModel:[self getModel:1] thirdModel:[self getModel:2] type:3];
//        headerView.clickActionBlock = ^(NSInteger tag) {
//            CXNearbyModel *model = [weakSelf getModel:tag-1];
//            XSUserInforViewController * userinfor=[XSUserInforViewController new];
//            userinfor.user_Id = model.user_id;
//            [weakSelf.navigationController pushViewController:userinfor animated:YES];
//        };
//        self.mainTableView.tableHeaderView = headerView;
//    } else if (_segmentedType == 2) {
//       self.mainTableView.tableHeaderView = [UIView new];
//    } else if (_segmentedType == 1) {
//        if (_buttonType == 1) {
//            self.mainTableView.tableHeaderView = [UIView new];
//        } else {
//            CXNearbyRankHeaderView *headerView = [[NSBundle mainBundle] loadNibNamed:@"CXNearbyRankHeaderView" owner:self options:nil].lastObject;
//            headerView.frame = CGRectMake(0, 0, kScreenWidth, 190);
//            [headerView reloadFirstModel:[self getModel:0] secondModel:[self getModel:1] thirdModel:[self getModel:2] type:1];
//            headerView.clickActionBlock = ^(NSInteger tag) {
//                CXNearbyModel *model = [weakSelf getModel:tag-1];
//                XSUserInforViewController * userinfor=[XSUserInforViewController new];
//                userinfor.user_Id = model.user_id;
//                [weakSelf.navigationController pushViewController:userinfor animated:YES];
//            };
//            self.mainTableView.tableHeaderView = headerView;
//        }
//    }
}

- (CXUserModel *)getModel:(NSInteger)index {
    if (self.dataSources.count > index) {
        return self.dataSources[index];
    } else {
        return [CXUserModel new];
    }
}

#pragma mark - UITableViewDataSource/Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (_segmentedType == 3) {
        return MAX(self.dataSources.count - 3, 0);
    } else if (_segmentedType == 2) {
        return self.dataSources.count;
    } else {
        if (_buttonType == 1) {
            return self.dataSources.count;
        } else {
            return MAX(self.dataSources.count - 3, 0);
        }
    }
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (_segmentedType == 2) {
        CXNearbyRankSongCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXNearbyRankSongCellID"];
        CXUserModel *model = self.dataSources[indexPath.row];
           cell.model = model;
        [cell.rank_numBtn setTitle:@"" forState:UIControlStateNormal];
        [cell.rank_numBtn setImage:[UIImage new] forState:UIControlStateNormal];
        cell.bgImage.hidden = YES;
        if (indexPath.row == 0) {
            [cell.rank_numBtn setImage:[UIImage imageNamed:@"find_rank_1"] forState:UIControlStateNormal];
            if (_buttonType == 1) {
                cell.bgImage.hidden = YES;
            } else {
                cell.bgImage.hidden = NO;
            }
        } else if (indexPath.row == 1) {
            [cell.rank_numBtn setImage:[UIImage imageNamed:@"find_rank_2"] forState:UIControlStateNormal];
            if (_buttonType == 1) {
                cell.bgImage.hidden = YES;
            } else {
                cell.bgImage.hidden = NO;
            }
        } else if (indexPath.row == 2) {
            [cell.rank_numBtn setImage:[UIImage imageNamed:@"find_rank_3"] forState:UIControlStateNormal];
            if (_buttonType == 1) {
                cell.bgImage.hidden = YES;
            } else {
                cell.bgImage.hidden = NO;
            }
        } else {
            [cell.rank_numBtn setTitle:[NSString stringWithFormat:@"%ld", indexPath.row + 1] forState:UIControlStateNormal];
            cell.bgImage.hidden = YES;
        }
        
        return cell;
    } else {
        CXNearbyRankCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXNearbyRankCellID"];
        if (_segmentedType == 1) {
            if (_buttonType == 1) {
                CXUserModel *model = self.dataSources[indexPath.row];
                cell.model = model;
                cell.rank_roseLabel.text = model.num;
                cell.rang_roseImage.image = [UIImage imageNamed:@"find_rose"];
                [cell.rank_numBtn setTitle:@"" forState:UIControlStateNormal];
                [cell.rank_numBtn setImage:[UIImage new] forState:UIControlStateNormal];
                if (indexPath.row == 0) {
                    [cell.rank_numBtn setImage:[UIImage imageNamed:@"find_rank_1"] forState:UIControlStateNormal];
                } else if (indexPath.row == 1) {
                    [cell.rank_numBtn setImage:[UIImage imageNamed:@"find_rank_2"] forState:UIControlStateNormal];
                } else if (indexPath.row == 2) {
                    [cell.rank_numBtn setImage:[UIImage imageNamed:@"find_rank_3"] forState:UIControlStateNormal];
                } else {
                    [cell.rank_numBtn setTitle:[NSString stringWithFormat:@"%ld", indexPath.row + 1] forState:UIControlStateNormal];
                }
            } else {
                if (indexPath.row+3 < self.dataSources.count) {
                    CXUserModel *model = self.dataSources[indexPath.row+3];
                    cell.model = model;
                    cell.rank_roseLabel.text = model.num;
                    cell.rang_roseImage.image = [UIImage imageNamed:@"find_rose"];
                    [cell.rank_numBtn setImage:[UIImage new] forState:UIControlStateNormal];
                    [cell.rank_numBtn setTitle:[NSString stringWithFormat:@"%ld", indexPath.row + 4] forState:UIControlStateNormal];
                }
            }
        } else {
            if (indexPath.row+3 < self.dataSources.count) {
                CXUserModel *model = self.dataSources[indexPath.row+3];
                cell.model = model;
                cell.rank_roseLabel.text = model.blue_rose;
                cell.rang_roseImage.image = [UIImage imageNamed:@"find_blue_rose"];
                [cell.rank_numBtn setImage:[UIImage new] forState:UIControlStateNormal];
                [cell.rank_numBtn setTitle:[NSString stringWithFormat:@"%ld", indexPath.row + 4] forState:UIControlStateNormal];
            }
            
        }
        return cell;
    }
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXUserModel *model = [CXUserModel new];
    if (_segmentedType == 2) {
        CXUserModel *tempModel = self.dataSources[indexPath.row];
        model = tempModel;
    } else {
        if (_segmentedType == 1) {
            if (_buttonType == 1) {
                CXUserModel *tempModel = self.dataSources[indexPath.row];
                model = tempModel;
            } else {
                CXUserModel *tempModel = self.dataSources[indexPath.row+3];
                model = tempModel;
            }
        } else {
            CXUserModel *tempModel = self.dataSources[indexPath.row+3];
            model = tempModel;
        }
    }
//    XSUserInforViewController * userinfor=[XSUserInforViewController new];
//    userinfor.user_Id = model.user_id;
//    [self.navigationController pushViewController:userinfor animated:YES];
}

#pragma mark - Action
- (IBAction)segmentedAction:(UIButton *)sender {
    _segmentedType = sender.tag;
    if (sender.tag == 3) {
        _tableViewTopLayout.constant = 8;
    } else {
        _tableViewTopLayout.constant = 54;
    }
    [self reloadRankSegmentedStatus:sender.tag];
    [self headerRefresh];
}

- (IBAction)rankAction:(UIButton *)sender {
    _buttonType = sender.tag - 9;
    [self reloadRankBtnStatus:sender.tag];
    
    [self headerRefresh];
}

- (void)reloadRankSegmentedStatus:(NSInteger)status {
    UIImage *wImage = [UIImage imageWithColor:[UIColor whiteColor]];
    UIImage *gImage1 = [UIImage gradientImageWithSize:CGSizeMake(85, 30) Color1:UIColorHex(0xF20E3E) color2:UIColorHex(0xEC1FC8)];
    UIImage *gImage2 = [UIImage gradientImageWithSize:CGSizeMake(85, 30) Color1:UIColorHex(0xEC1FC8) color2:UIColorHex(0x8F09CD)];
    UIImage *gImage3 = [UIImage gradientImageWithSize:CGSizeMake(85, 30) Color1:UIColorHex(0x8F09CD) color2:UIColorHex(0x280DEE)];
    [_charmBtn setBackgroundImage:status == 1 ? gImage1 : wImage forState:UIControlStateNormal];
    [_songBtn setBackgroundImage:status == 2 ? gImage2 : wImage forState:UIControlStateNormal];
    [_blueRoseBtn setBackgroundImage:status == 3 ? gImage3 : wImage forState:UIControlStateNormal];
    
    [_charmBtn setTitleColor:status == 1 ? [UIColor whiteColor] : UIColorHex(0x282828) forState:UIControlStateNormal];
    [_songBtn setTitleColor:status == 2 ? [UIColor whiteColor] : UIColorHex(0x282828) forState:UIControlStateNormal];
    [_blueRoseBtn setTitleColor:status == 3 ? [UIColor whiteColor] : UIColorHex(0x282828) forState:UIControlStateNormal];
}

- (void)reloadRankBtnStatus:(NSInteger)status {
    [_dayBtn setTitleColor:status == 10 ? UIColorHex(0x770BA7) : UIColorHex(0x282828) forState:UIControlStateNormal];
    [_weekBtn setTitleColor:status == 11 ? UIColorHex(0x770BA7) : UIColorHex(0x282828) forState:UIControlStateNormal];
    [_monthBtn setTitleColor:status == 12 ? UIColorHex(0x770BA7) : UIColorHex(0x282828) forState:UIControlStateNormal];
    _indicatorLeadingLayout.constant = (kScreenWidth/3 - 28)/2 + (status - 10)*kScreenWidth/3;
}

- (void)setupSubViews {
    _segmentedView.layer.cornerRadius = 15;
    _segmentedView.layer.masksToBounds = YES;
    _segmentedView.layer.borderColor = UIColorHex(0x00000).CGColor;
    _segmentedView.layer.borderWidth = 0.5;
    _charmBtn.layer.cornerRadius = 15;
    _charmBtn.layer.masksToBounds = YES;
    _songBtn.layer.cornerRadius = 15;
    _songBtn.layer.masksToBounds = YES;
    _blueRoseBtn.layer.cornerRadius = 15;
    _blueRoseBtn.layer.masksToBounds = YES;
    
    [self reloadRankSegmentedStatus:1];
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXNearbyRankCell" bundle:nil] forCellReuseIdentifier:@"CXNearbyRankCellID"];
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXNearbyRankSongCell" bundle:nil] forCellReuseIdentifier:@"CXNearbyRankSongCellID"];
    
    self.mainTableView.tableFooterView = [UIView new];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
}

@end

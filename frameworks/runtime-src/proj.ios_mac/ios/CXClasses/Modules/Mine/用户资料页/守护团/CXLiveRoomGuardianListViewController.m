//
//  CXLiveRoomGuardianListViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/6/19.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomGuardianListViewController.h"
#import "CXLiveRoomGuardianListCell.h"
#import "CXLiveRoomGuardianPrivilegeView.h"
#import "CXMineGuardRenewView.h"

@interface CXLiveRoomGuardianListViewController () <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, assign) NSInteger page;

@property (weak, nonatomic) IBOutlet UIView *first_bgView;
@property (weak, nonatomic) IBOutlet UIImageView *first_avatar;
@property (weak, nonatomic) IBOutlet UILabel *first_name;
@property (weak, nonatomic) IBOutlet UILabel *first_number;

@property (weak, nonatomic) IBOutlet UIView *second_bgView;
@property (weak, nonatomic) IBOutlet UIImageView *second_avatar;
@property (weak, nonatomic) IBOutlet UILabel *second_name;
@property (weak, nonatomic) IBOutlet UILabel *second_number;

@property (weak, nonatomic) IBOutlet UIView *third_bgView;
@property (weak, nonatomic) IBOutlet UIImageView *third_avatar;
@property (weak, nonatomic) IBOutlet UILabel *third_name;
@property (weak, nonatomic) IBOutlet UILabel *third_number;

@property (weak, nonatomic) IBOutlet UIView *top_radiusView;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;
@property (weak, nonatomic) IBOutlet UIButton *bottomBtn;

@property (nonatomic, strong) CXUserModel *firstModel;
@property (nonatomic, strong) CXUserModel *secondModel;
@property (nonatomic, strong) CXUserModel *thirdModel;

@end

@implementation CXLiveRoomGuardianListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    _first_avatar.layer.masksToBounds = YES;
    _first_avatar.layer.cornerRadius = 30;
    _first_avatar.layer.borderWidth = 0.5;
    _first_avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    
    _second_avatar.layer.masksToBounds = YES;
    _second_avatar.layer.cornerRadius = 30;
    _second_avatar.layer.borderWidth = 0.5;
    _second_avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    
    _third_avatar.layer.masksToBounds = YES;
    _third_avatar.layer.cornerRadius =30;
    _third_avatar.layer.borderWidth = 0.5;
    _third_avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    
    self.bottomBtn.layer.masksToBounds = YES;
    self.bottomBtn.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [self.bottomBtn setBackgroundImage:image forState:UIControlStateNormal];
    
    if ([_userId isEqualToString:[CXClientModel instance].userId]) {
        self.bottomBtn.hidden = YES;
    }
    
    _mainTableView.dataSource = self;
    _mainTableView.delegate = self;
    
    _page = 1;
    _dataSources = [NSMutableArray array];
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomGuardianListCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomGuardianListCellID"];
    
    self.mainTableView.tableFooterView = [UIView new];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    
    [self headerRefresh];
    
    // 监听支付宝支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(aliPayCallBack:) name:kNSNotificationCenter_CXRechargeViewController_alipay object:nil];

    // 监听微信支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(weChatCallBack:) name:kNSNotificationCenter_CXRechargeViewController_weixin object:nil];
}

- (void)aliPayCallBack:(NSNotification *)info {

    if ([[info.object objectForKey:@"resultStatus"] integerValue] == 9000) {
        [self toast:@"守护成功"];
        [self headerRefresh];
    } else {
        [self toast:@"支付失败"];
    }
}

- (void)weChatCallBack:(NSNotification *)info {
    NSDictionary *obj = info.object;
    switch ([[obj objectForKey:@"errCode"] integerValue]) {
        case WXSuccess:
            [self toast:@"守护成功"];
            [self headerRefresh];
            break;
        case WXErrCodeUserCancel:
            [self toast:@"取消支付"];
            break;
        default:
            [self toast:@"支付失败"];
            break;
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBar.hidden = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBar.hidden = NO;
}

- (void)headerRefresh {
    _page = 1;
    [self getRankData];
}

- (void)footerRefresh {
    _page++;
    [self getRankData];
}

- (void)getRankData {
    NSDictionary *param = @{
        @"page":[NSString stringWithFormat:@"%ld", (long)_page],
        @"userId" : _userId,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Guard/getUserList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXUserModel class] json:responseObject[@"data"][@"guard_memberlist"]];
            if (self->_page == 1) {
                [weakSelf reloadHeaderView:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
                
                [weakSelf.mainTableView reloadData];
            }
            if (weakSelf.page >= [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue]) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
            BOOL is_guard = [[responseObject[@"data"][@"is_guard"] stringValue] isEqualToString:@"1"];
            if (is_guard) {
                [weakSelf.bottomBtn setTitle:@"续费" forState:UIControlStateNormal];
            } else {
                [weakSelf.bottomBtn setTitle:@"开通守护" forState:UIControlStateNormal];
            }
            
        }
    }];
}

- (void)reloadHeaderView:(NSArray *)array {
    self.firstModel = [CXUserModel new];
    self.secondModel = [CXUserModel new];
    self.thirdModel = [CXUserModel new];
    self.dataSources = [NSMutableArray arrayWithArray:array];
    if (array.count > 0) {
        self.first_bgView.hidden = NO;
        CXUserModel *firstModel = array.firstObject;
        if (firstModel.type.intValue == 2) {
            [self.mainTableView reloadData];
            return;
        } else {
            [self.dataSources removeObject:firstModel];
            [self.first_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.avatar]];
            self.first_name.text = [firstModel.nickname substringToIndex:MIN(5, firstModel.nickname.length)];
            self.first_number.text = [NSString stringWithFormat:@"亲密度：%@", firstModel.intimacy];
            self.firstModel = firstModel;
        }
    } else {
        self.first_bgView.hidden = YES;
    }
    
    if (array.count > 1) {
        self.second_bgView.hidden = NO;
        CXUserModel *firstModel = array[1];
        if (firstModel.type.intValue == 2) {
            [self.mainTableView reloadData];
            return;
        } else {
            [self.dataSources removeObject:firstModel];
            [self.second_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.avatar]];
            self.second_name.text = [firstModel.nickname substringToIndex:MIN(5, firstModel.nickname.length)];
            self.second_number.text = [NSString stringWithFormat:@"亲密度：%@", firstModel.intimacy];
            self.secondModel = firstModel;
        }
        
    } else {
        self.second_bgView.hidden = YES;
    }
    
    if (array.count > 2) {
        self.third_bgView.hidden = NO;
        CXUserModel *firstModel = array[2];
        if (firstModel.type.integerValue == 2) {
            [self.mainTableView reloadData];
            return;
        } else {
            [self.dataSources removeObject:firstModel];
            [self.third_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.avatar]];
            self.third_name.text = [firstModel.nickname substringToIndex:MIN(5, firstModel.nickname.length)];
            self.third_number.text = [NSString stringWithFormat:@"亲密度：%@", firstModel.intimacy];
            self.thirdModel = firstModel;
        }
    } else {
        self.third_bgView.hidden = YES;
    }
    
    if (self.dataSources.count > 0) {
        self.mainTableView.hidden = NO;
        self.top_radiusView.hidden = NO;
        [self.mainTableView reloadData];
    } else {
        self.mainTableView.hidden = YES;
        self.top_radiusView.hidden = YES;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomGuardianListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLiveRoomGuardianListCellID"];
    CXUserModel *model = self.dataSources[indexPath.row];
    cell.userModel = model;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXUserModel *model = self.dataSources[indexPath.row];
    [AppController showUserProfile:model.user_id];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (IBAction)backAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)guardianAction:(UIButton *)sender {
    if (sender.tag == 10 && self.firstModel && self.firstModel.user_id.length > 0) {
        [AppController showUserProfile:_firstModel.user_id];
    } else if (sender.tag == 11 && self.secondModel && self.secondModel.user_id.length > 0) {
        [AppController showUserProfile:_secondModel.user_id];
    } else if (sender.tag == 12 && self.thirdModel && self.thirdModel.user_id.length > 0) {
        [AppController showUserProfile:_thirdModel.user_id];
    }
    
}

- (IBAction)ruleAction:(id)sender {
    CXLiveRoomGuardianPrivilegeView *privilegeView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomGuardianPrivilegeView" owner:self options:nil].firstObject;
    [privilegeView show];
}

- (IBAction)bottomAction:(id)sender {
    CXMineGuardRenewView *guardRankView = [[NSBundle mainBundle] loadNibNamed:@"CXMineGuardRenewView" owner:self options:nil].firstObject;
    guardRankView.userId = _userId;
    [guardRankView show];
}

@end

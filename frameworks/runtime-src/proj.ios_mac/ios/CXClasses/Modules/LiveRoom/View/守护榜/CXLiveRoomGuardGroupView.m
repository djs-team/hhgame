//
//  CXLiveRoomGuardGroupView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/20.
//

#import "CXLiveRoomGuardGroupView.h"
#import "CXLiveRoomGuardianListCell.h"

@interface CXLiveRoomGuardGroupView() <UITableViewDelegate, UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *numberLabel;

@property (nonatomic, assign) BOOL is_guard;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;
@property (weak, nonatomic) IBOutlet UIView *open_guard_view;
@property (weak, nonatomic) IBOutlet UIButton *open_guard_btn;
@property (weak, nonatomic) IBOutlet UIButton *renew_guard_btn;

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, assign) NSInteger page;

@end

@implementation CXLiveRoomGuardGroupView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 400));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeSheet;
    
    _avatar.layer.masksToBounds = YES;
    _avatar.layer.cornerRadius = 28;
    _avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    _avatar.layer.borderWidth = 0.5;
    
    self.open_guard_btn.layer.masksToBounds = YES;
    self.open_guard_btn.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [self.open_guard_btn setBackgroundImage:image forState:UIControlStateNormal];
    
    self.renew_guard_btn.layer.masksToBounds = YES;
    self.renew_guard_btn.layer.cornerRadius = 19;
    [self.renew_guard_btn setBackgroundImage:image forState:UIControlStateNormal];
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomGuardianListCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomGuardianListCellID"];
    
    _mainTableView.dataSource = self;
    _mainTableView.delegate = self;
    
    _page = 0;
    _dataSources = [NSMutableArray array];
    
    self.mainTableView.tableFooterView = [UIView new];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
}

- (void)setUserId:(NSString *)userId {
    _userId = userId;
    [self getRankData];
}

- (void)headerRefresh {
    _page = 0;
    [self getRankData];
}

- (void)footerRefresh {
    _page++;
    [self getRankData];
}

- (void)getRankData {
    kWeakSelf
    SocketMessageGetGuardItemListRequest * request = [SocketMessageGetGuardItemListRequest new];
    request.UserId = [NSNumber numberWithString:_userId];
    request.Page = _page;
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(SocketMessageGetGuardItemListRequest * _Nonnull request) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
        if (request.noError && request.response.isSuccess) {
            NSArray *array = [NSArray arrayWithArray:request.response.GuardItems];
            if (self->_page == 0) {
                [weakSelf.dataSources removeAllObjects];
            }
            
            [weakSelf.dataSources addObjectsFromArray:array];

            [weakSelf.mainTableView reloadData];
            
            if (_page >= request.response.AllPage.intValue) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
            [weakSelf.avatar setImageURL:[NSURL URLWithString:request.response.GuardHead]];
            weakSelf.is_guard = request.response.IsGuard;
            
            if (weakSelf.is_guard == YES) {
                NSString *str1 = [NSString stringWithFormat:@"%@的守护团 ",request.response.GuardName];
                NSString *str2 = [NSString stringWithFormat:@"%@(%@人)", str1,request.response.UserCount.stringValue];
                NSMutableAttributedString *attri = [[NSMutableAttributedString alloc] initWithString:str2];
                [attri addAttribute:NSForegroundColorAttributeName value:UIColorHex(0x763CE5) range:NSMakeRange(0, str2.length)];
                [attri addAttribute:NSForegroundColorAttributeName value:UIColorHex(0x343434) range:NSMakeRange(0, str1.length)];
                weakSelf.numberLabel.attributedText = attri;
            } else {
                NSString *str1 = [NSString stringWithFormat:@"%@的守护团 ",request.response.GuardName];
                weakSelf.numberLabel.text = str1;
            }
            
        }
    }];
}

- (void)setIs_guard:(BOOL)is_guard {
    _is_guard = is_guard;
    if (_is_guard == YES) {
        self.open_guard_view.hidden = YES;
        if ([_userId isEqualToString:[CXClientModel instance].userId]) {
            self.renew_guard_btn.hidden = YES;
        } else {
            self.renew_guard_btn.hidden = NO;
        }
        
    } else {
        self.open_guard_view.hidden = NO;
        self.renew_guard_btn.hidden = YES;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomGuardianListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLiveRoomGuardianListCellID"];
    CXLiveRoomGuardItemModel *model = self.dataSources[indexPath.row];
    cell.guardianUserId = self.userId;
    cell.model = model;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomGuardItemModel *model = self.dataSources[indexPath.row];
    [AppController showUserProfile:model.UserInfo.UserId];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (IBAction)renew_guard_btnAction:(id)sender {
    [self hide];
    
    // 续费
    if (self.guardGroupViewBlcok) {
        self.guardGroupViewBlcok(NO);
    }
}

- (IBAction)open_guard_btnAction:(id)sender {
    [self hide];
    
    // 成为守护
    if (self.guardGroupViewBlcok) {
        self.guardGroupViewBlcok(NO);
    }
}

- (void)hide {
    [super hide];
    
    if (self.guardGroupViewBlcok) {
        self.guardGroupViewBlcok(YES);
    }
}

@end

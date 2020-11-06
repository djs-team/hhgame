//
//  CXLiveRoomGuardRankView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/21.
//

#import "CXLiveRoomGuardRankView.h"
#import "CXLiveRoomGuardianListCell.h"

@interface CXLiveRoomGuardRankView()  <UITableViewDelegate, UITableViewDataSource>

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, assign) NSInteger page;

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

@property (weak, nonatomic) IBOutlet UIImageView *first_avatar;
@property (weak, nonatomic) IBOutlet UILabel *first_name;
@property (weak, nonatomic) IBOutlet UILabel *first_number;

@property (weak, nonatomic) IBOutlet UIImageView *second_avatar;
@property (weak, nonatomic) IBOutlet UILabel *second_name;
@property (weak, nonatomic) IBOutlet UILabel *second_number;

@property (weak, nonatomic) IBOutlet UIImageView *third_avatar;
@property (weak, nonatomic) IBOutlet UILabel *third_name;
@property (weak, nonatomic) IBOutlet UILabel *third_number;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) CXLiveRoomGuardItemModel *firstModel;
@property (nonatomic, strong) CXLiveRoomGuardItemModel *secondModel;
@property (nonatomic, strong) CXLiveRoomGuardItemModel *thirdModel;

@end

@implementation CXLiveRoomGuardRankView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 420));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeSheet;
    
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
    
    _mainTableView.dataSource = self;
    _mainTableView.delegate = self;
    
    _page = 0;
    _dataSources = [NSMutableArray array];
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomGuardianListCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomGuardianListCellID"];
    
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
            if (self->_page == 1) {
                [weakSelf reloadHeaderView:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];

                [weakSelf.mainTableView reloadData];
            }
            if (array.count < 20) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
            weakSelf.titleLabel.text = [NSString stringWithFormat:@"%@的守护团", request.response.GuardName];
        }
    }];
}

- (void)reloadHeaderView:(NSArray *)array {
    self.firstModel = [CXLiveRoomGuardItemModel new];
    self.secondModel = [CXLiveRoomGuardItemModel new];
    self.thirdModel = [CXLiveRoomGuardItemModel new];
    self.dataSources = [NSMutableArray arrayWithArray:array];
    if (array.count > 0) {
        CXLiveRoomGuardItemModel *firstModel = array.firstObject;
        [self.dataSources removeObject:firstModel];
        [self.first_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.UserInfo.HeadImageUrl]];
        self.first_name.text = [firstModel.UserInfo.Name substringToIndex:MIN(5, firstModel.UserInfo.Name.length)];
        self.first_number.text = firstModel.Intimacy.stringValue;
        self.firstModel = firstModel;
    } else {
        self.first_avatar.image = [UIImage new];
        self.first_name.text = @"";
        self.first_number.text = @"";
    }
    
    if (array.count > 1) {
        CXLiveRoomGuardItemModel *firstModel = array[1];
        [self.dataSources removeObject:firstModel];
        [self.first_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.UserInfo.HeadImageUrl]];
        self.first_name.text = [firstModel.UserInfo.Name substringToIndex:MIN(5, firstModel.UserInfo.Name.length)];
        self.first_number.text = firstModel.Intimacy.stringValue;
        self.firstModel = firstModel;
        
    } else {
        self.second_avatar.image = [UIImage new];
        self.second_name.text = @"";
        self.second_number.text = @"";
    }
    
    if (array.count > 2) {
        CXLiveRoomGuardItemModel *firstModel = array[2];
        [self.dataSources removeObject:firstModel];
        [self.first_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.UserInfo.HeadImageUrl]];
        self.first_name.text = [firstModel.UserInfo.Name substringToIndex:MIN(5, firstModel.UserInfo.Name.length)];
        self.first_number.text = firstModel.Intimacy.stringValue;
        self.firstModel = firstModel;
    } else {
        self.third_avatar.image = [UIImage new];
        self.third_name.text = @"";
        self.third_number.text = @"";
    }
    
    [self.mainTableView reloadData];
    
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
    CXUserModel *model = self.dataSources[indexPath.row];
    [AppController showUserProfile:model.user_id];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 80;
}

- (IBAction)guardianAction:(UIButton *)sender {
    if (sender.tag == 10 && self.firstModel && self.firstModel.UserInfo.UserId.length > 0) {
        [AppController showUserProfile:_firstModel.UserInfo.UserId];
    } else if (sender.tag == 11 && self.secondModel && self.secondModel.UserInfo.UserId.length > 0) {
        [AppController showUserProfile:_secondModel.UserInfo.UserId];
    } else if (sender.tag == 12 && self.thirdModel && self.thirdModel.UserInfo.UserId.length > 0) {
        [AppController showUserProfile:_thirdModel.UserInfo.UserId];
    }
    
}

@end

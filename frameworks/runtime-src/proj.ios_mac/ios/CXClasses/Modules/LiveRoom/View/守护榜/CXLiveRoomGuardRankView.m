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

@property (nonatomic, strong) CXUserModel *firstModel;
@property (nonatomic, strong) CXUserModel *secondModel;
@property (nonatomic, strong) CXUserModel *thirdModel;

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
    
    _page = 1;
    _dataSources = [NSMutableArray array];
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomGuardianListCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomGuardianListCellID"];
    
    self.mainTableView.tableFooterView = [UIView new];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
}

- (void)setUserId:(NSString *)userId {
    _userId = userId;
    
    __weak typeof(self) wself = self;
    SocketMessageGetUserInfo * getInfo = [SocketMessageGetUserInfo new];
    getInfo.UserId = [NSNumber numberWithString:userId];
    [[CXClientModel instance] sendSocketRequest:getInfo withCallback:^(SocketMessageGetUserInfo * _Nonnull request) {
        if (request.noError && request.response.isSuccess) {
            LiveRoomUser *user = [LiveRoomUser modelWithJSON:[request.response.User modelToJSONObject]];
            wself.titleLabel.text = [NSString stringWithFormat:@"%@的守护团", user.Name];
            [wself getRankData];
        }
    }];
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
        @"pagenum" : @"20",
        @"userid" : _userId,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/MemberOnline/rankingList_shouhu" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXUserModel class] json:responseObject[@"data"][@"list"]];
            if (self->_page == 1) {
                [weakSelf reloadHeaderView:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
                
                [weakSelf.mainTableView reloadData];
            }
            if (array.count < 20) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
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
        CXUserModel *firstModel = array.firstObject;
        if (firstModel.type.intValue == 2) {
            [self.mainTableView reloadData];
            return;
        } else {
            [self.dataSources removeObject:firstModel];
            [self.first_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.avatar]];
            self.first_name.text = [firstModel.nickname substringToIndex:MIN(5, firstModel.nickname.length)];
            self.first_number.text = firstModel.num;
            self.firstModel = firstModel;
        }
    } else {
        self.first_avatar.image = [UIImage new];
        self.first_name.text = @"";
        self.first_number.text = @"";
    }
    
    if (array.count > 1) {
        CXUserModel *firstModel = array[1];
        if (firstModel.type.intValue == 2) {
            [self.mainTableView reloadData];
            return;
        } else {
            [self.dataSources removeObject:firstModel];
            [self.second_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.avatar]];
            self.second_name.text = [firstModel.nickname substringToIndex:MIN(5, firstModel.nickname.length)];
            self.second_number.text = firstModel.num;
            self.secondModel = firstModel;
        }
        
    } else {
        self.second_avatar.image = [UIImage new];
        self.second_name.text = @"";
        self.second_number.text = @"";
    }
    
    if (array.count > 2) {
        CXUserModel *firstModel = array[2];
        if (firstModel.type.integerValue == 2) {
            [self.mainTableView reloadData];
            return;
        } else {
            [self.dataSources removeObject:firstModel];
            [self.third_avatar sd_setImageWithURL:[NSURL URLWithString:firstModel.avatar]];
            self.third_name.text = [firstModel.nickname substringToIndex:MIN(5, firstModel.nickname.length)];
            self.third_number.text = firstModel.num;
            self.thirdModel = firstModel;
        }
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
    CXUserModel *model = self.dataSources[indexPath.row];
    cell.model = model;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXUserModel *model = self.dataSources[indexPath.row];
    [AppController showUserProfile:model.user_id];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 60;
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

@end

//
//  CXAllOnlineListViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXAllOnlineListViewController.h"
#import "CXAllOnlineListCell.h"
#import "XYRoomAlreadListCell.h"
#import "SocketMessageJoinSeat.h"
#import "SocketMessage23.h"
#import "SocketMessage22.h"

@interface CXAllOnlineListViewController () <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

@property (nonatomic, assign) NSInteger type_level; // 4 沙发座，3 普通座
@property (weak, nonatomic) IBOutlet UIButton *sofa_btn;
@property (weak, nonatomic) IBOutlet UIButton *normal_btn;
@property (weak, nonatomic) IBOutlet UIView *indictor_view;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *indictor_leadingLayout;

@property (weak, nonatomic) IBOutlet UIView *selected_bgView;
@property (nonatomic, assign) NSInteger selected_type;  // 0 申请, 1 房间内, 2 最新活跃, 3 好友
@property (weak, nonatomic) IBOutlet UIButton *apply_btn;
@property (weak, nonatomic) IBOutlet UIButton *room_btn;
@property (weak, nonatomic) IBOutlet UIButton *now_btn;
@property (weak, nonatomic) IBOutlet UIButton *friend_btn;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *selected_bgView_topLayout;


@property (weak, nonatomic) IBOutlet UIView *contentBGView;
@property (weak, nonatomic) IBOutlet UITableView *mianTableView;

@property (weak, nonatomic) IBOutlet UIButton *charge_applyButton;
@property (weak, nonatomic) IBOutlet UIButton *applyButton;

@property (strong, nonatomic) NSMutableArray * dataArray;

@property (strong, nonatomic) NSMutableArray * selectedArray;

@property (strong, nonatomic) NSMutableArray * roomOnlistJoinedArrays;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger totalPage;

@property (nonatomic, strong) UIImage *selectedBtnBGImage;

@property (nonatomic, assign) BOOL isLoadingData;

@end

@implementation CXAllOnlineListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    self.contentBGView.layer.cornerRadius = 18;
    [self.mianTableView registerNib:[UINib nibWithNibName:@"CXAllOnlineListCell" bundle:nil] forCellReuseIdentifier:@"CXAllOnlineListCellID"];
    [self.mianTableView registerNib:[UINib nibWithNibName:@"XYRoomAlreadListCell" bundle:nil] forCellReuseIdentifier:@"XYRoomAlreadListCellID"];
    self.mianTableView.tableFooterView = [UIView new];
    
    _apply_btn.layer.masksToBounds = true;
    _apply_btn.layer.cornerRadius = 15;
    _room_btn.layer.masksToBounds = true;
    _room_btn.layer.cornerRadius = 15;
    _now_btn.layer.masksToBounds = true;
    _now_btn.layer.cornerRadius = 15;
    _friend_btn.layer.masksToBounds = true;
    _friend_btn.layer.cornerRadius = 15;
    
    _selected_bgView.layer.masksToBounds = true;
    _selected_bgView.layer.cornerRadius = 15;
    _selected_bgView.layer.borderColor = [UIColor blackColor].CGColor;
    _selected_bgView.layer.borderWidth = 0.5;

    _selectedBtnBGImage = [UIImage gradientImageWithSize:CGSizeMake(300/4, 30) Color1:UIColorHex(0xEC1FC8) color2:UIColorHex(0x8F09CD) endPoint:CGPointMake(1, 0)];
    
    self.dataArray = [NSMutableArray array];
    self.selectedArray = [NSMutableArray array];
    
    self.applyButton.hidden = YES;
    self.applyButton.layer.masksToBounds = YES;
    self.applyButton.layer.cornerRadius = 18;
    [self.applyButton setBackgroundImage:[UIImage x_gradientImageWithSize:CGSizeMake(120, 36) Color1:UIColorHex(0xEC5BBA) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    self.charge_applyButton.hidden = YES;
    self.charge_applyButton.layer.masksToBounds = YES;
    self.charge_applyButton.layer.cornerRadius = 18;
    self.charge_applyButton.layer.borderColor = UIColorHex(0x7B3EF3).CGColor;
    self.charge_applyButton.layer.borderWidth = 1;
    
    if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) { // 相亲房
        if (_isMan == YES) {
            self.titleLabel.text = @"男嘉宾麦位管理";
        } else {
            self.titleLabel.text = @"女嘉宾麦位管理";
        }
    } else {
        self.titleLabel.text = @"麦位管理";
    }
    
    self.charge_applyButton.titleLabel.numberOfLines = 0;
    self.charge_applyButton.titleLabel.textAlignment = NSTextAlignmentCenter;
    NSString *str = @"";
    if ([CXClientModel instance].room.RoomData.IsExclusiveRoom == YES) { // 专属房
//        [_charge_applyButton setTitle:[NSString stringWithFormat:@"收费邀请\n(%@朵玫瑰/分钟)", [ModelClient instance].room.OnMicroCost.stringValue] forState:UIControlStateNormal];
        
        str = [NSString stringWithFormat:@"收费邀请\n(%@朵玫瑰/分钟)", [CXClientModel instance].room.OnMicroCost.stringValue];
    } else {
//        [_charge_applyButton setTitle:[NSString stringWithFormat:@"收费邀请\n(%@朵玫瑰)", [ModelClient instance].room.OnMicroCost.stringValue] forState:UIControlStateNormal];
        str = [NSString stringWithFormat:@"收费邀请\n(%@朵玫瑰)", [CXClientModel instance].room.OnMicroCost.stringValue];
    }
    
    NSMutableAttributedString *attr = [[NSMutableAttributedString alloc] initWithString:str];
    [attr addAttribute:NSFontAttributeName value:[UIFont systemFontOfSize:8] range:NSMakeRange(5, str.length - 5)];
    [_charge_applyButton setAttributedTitle:attr forState:UIControlStateNormal];
    
    [self segmentAction:_apply_btn];
    [self typeBtnAction:_sofa_btn];
    
    if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 8) { // 8人房
        self.selected_bgView_topLayout.constant = 90;
        self.sofa_btn.hidden = NO;
        self.normal_btn.hidden = NO;
        self.indictor_view.hidden = NO;
        if (_isMan == NO) {
            [self typeBtnAction:_normal_btn];
        }
    } else {
        self.sofa_btn.hidden = YES;
        self.normal_btn.hidden = YES;
        self.indictor_view.hidden = YES;
        self.selected_bgView_topLayout.constant = 50;
    }
    
    self.isLoadingData = NO;
}

#pragma mark - MJRefresh
- (void)headerRefresh {
    _page = 1;
    [self requstAllOnlineList];
}

- (void)footerRefresh {
    if (_page == _totalPage) {
        return;
    }
    _page++;
    [self requstAllOnlineList];
}

- (NSMutableArray *)roomOnlistJoinedArrays {
    if (!_roomOnlistJoinedArrays) {
        _roomOnlistJoinedArrays = [NSMutableArray array];
        kWeakSelf
        [[CXClientModel instance].room.leftOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
            
            [weakSelf.roomOnlistJoinedArrays addObject:obj.modelUser.UserId];
            
        }];
        
        [[CXClientModel instance].room.rightOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
            
            [weakSelf.roomOnlistJoinedArrays addObject:obj.modelUser.UserId];
            
        }];
        
        [[CXClientModel instance].room.seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, LiveRoomMicroInfo * _Nonnull obj, BOOL * _Nonnull stop) {
            NSString *userID = [[CXClientModel instance].room.seatUsers objectForKey:key];
            if (userID) {
                [weakSelf.roomOnlistJoinedArrays addObject:userID];
            }
        }];
    }
    
    return _roomOnlistJoinedArrays;
}

- (IBAction)typeBtnAction:(UIButton *)sender {
    if (sender.tag == 1) { //沙发座
        _type_level = 4;
    } else { // 普通座
        _type_level = 3;
    }
    
    [_sofa_btn setTitleColor:sender.tag == 1 ? UIColorHex(0xEC1FC8): UIColorHex(0x282828) forState:UIControlStateNormal];
    [_normal_btn setTitleColor:sender.tag == 2 ? UIColorHex(0xEC1FC8): UIColorHex(0x282828) forState:UIControlStateNormal];
    _sofa_btn.titleLabel.font = [UIFont systemFontOfSize:sender.tag == 1 ? 16 : 14];
    _normal_btn.titleLabel.font = [UIFont systemFontOfSize:sender.tag == 2 ? 16 : 14];
    
    _indictor_leadingLayout.constant = 10 + (150 - 20)/2 + (sender.tag - 1)*150;
    
    [self segmentAction:_apply_btn];
}

- (IBAction)segmentAction:(UIButton *)sender {
    if (self.isLoadingData == YES) {
        return;
    }
    
    _isLoadingData = YES;
    
    _selected_type = sender.tag - 20; // 0, 1, 2, 3
    
    [self reloadSelectedBtnStatus];
      
    self.page = 1;
    [self.dataArray removeAllObjects];
    [_selectedArray removeAllObjects];
    
    [_roomOnlistJoinedArrays removeAllObjects];
    _roomOnlistJoinedArrays = nil;
    
    _mianTableView.mj_header = nil;
    _mianTableView.mj_footer = nil;
    
    kWeakSelf
    if (self.selected_type == 0) { // 已申请
        if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) { // 相亲房
            if (_isMan == YES) {
                BOOL isManMicor = YES;
                NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:1];
                LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:index];
                if (seat.modelUser) { // 男麦有人
                    isManMicor = NO;
                }
                self.dataArray = [NSMutableArray arrayWithCapacity:[CXClientModel instance].room.leftOrders.allKeys.count];
                
                [[CXClientModel instance].room.leftOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
                    obj.isCanUpMicro = isManMicor;
                    [weakSelf.dataArray addObject:obj];
                }];
                
                _isLoadingData = NO;
                [self.mianTableView reloadData];
            } else {
                BOOL isManMicor = YES;
                NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:2];
                LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:index];
                if (seat.modelUser) { // 男麦有人
                    isManMicor = NO;
                }
                
                self.dataArray = [NSMutableArray arrayWithCapacity:[CXClientModel instance].room.rightOrders.allKeys.count];
                
                [[CXClientModel instance].room.rightOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
                    obj.isCanUpMicro = isManMicor;
                    [weakSelf.dataArray addObject:obj];
                    
                }];
                
                _isLoadingData = NO;
                [self.mianTableView reloadData];
            }
        } else if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 8) { // 八人房
            if (_type_level == 4) { // 沙发座
                BOOL isManMicor = YES;
                NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:1];
                LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:index];
                if (seat.modelUser) { // 男麦有人
                    isManMicor = NO;
                }
                self.dataArray = [NSMutableArray arrayWithCapacity:[CXClientModel instance].room.leftOrders.allKeys.count];
                
                [[CXClientModel instance].room.leftOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
                    obj.isCanUpMicro = isManMicor;
                    [weakSelf.dataArray addObject:obj];
                    
                }];
                
                _isLoadingData = NO;
                [self.mianTableView reloadData];
            } else {
                BOOL isManMicor = YES;
                NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:2];
                LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:index];
                if (seat.modelUser) { // 男麦有人
                    isManMicor = NO;
                }
                
                self.dataArray = [NSMutableArray arrayWithCapacity:[CXClientModel instance].room.rightOrders.allKeys.count];
                
                [[CXClientModel instance].room.rightOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
                    obj.isCanUpMicro = isManMicor;
                    [weakSelf.dataArray addObject:obj];
                    
                }];
                
                _isLoadingData = NO;
                [self.mianTableView reloadData];
            }
            
        } else {
            BOOL isManMicor = YES;
            NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:2];
            LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:index];
            if (seat.modelUser) { // 男麦有人
                isManMicor = NO;
            }
            
            self.dataArray = [NSMutableArray arrayWithCapacity:[CXClientModel instance].room.rightOrders.allKeys.count];
            
            [[CXClientModel instance].room.rightOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
                obj.isCanUpMicro = isManMicor;
                [weakSelf.dataArray addObject:obj];
                
            }];
            
            _isLoadingData = NO;
            [self.mianTableView reloadData];
        }
        
        self.applyButton.hidden = YES;
        self.charge_applyButton.hidden = YES;
    } else if (self.selected_type == 1) { // 房间内
        self.applyButton.hidden = NO;
        self.charge_applyButton.hidden = NO;
        [self requstRoomOnlineList];
    } else if (self.selected_type == 2) { // 最近活跃
        self.applyButton.hidden = NO;
        self.charge_applyButton.hidden = NO;
        
        _mianTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
        _mianTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
        
        [self requstAllOnlineList];
    } else { // 好友
        self.applyButton.hidden = NO;
        self.charge_applyButton.hidden = NO;
        
        _isLoadingData = NO;
        self.dataArray = [NSMutableArray arrayWithArray:[CXClientModel instance].firendArrays];
        [self.mianTableView reloadData];
    }
}

- (void)requstRoomOnlineList {
    SocketMessageGetOnlineUserList *list = SocketMessageGetOnlineUserList.new;
    list.Index = @(0);
    if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) {
        if (_isMan) {
            list.Sex = @(1);
        } else {
            list.Sex = @(2);
        }
    } else {
        list.Sex = @(0);
    }
    
    MJWeakSelf;
    self.isLoadingData = YES;
    [[CXClientModel instance] sendSocketRequest:list withCallback:^(SocketMessageGetOnlineUserList * _Nonnull request) {
        weakSelf.isLoadingData = NO;
        if (request.noError && request.response.isSuccess) {
            if (!request.response.OnlineUsers || request.response.OnlineUsers.count == 0) {
//                [weakSelf toast:@"在线列表为空"];
                [weakSelf.mianTableView reloadData];
                return;
            }
            [weakSelf.dataArray addObjectsFromArray:request.response.OnlineUsers];
        }
        
        [weakSelf.mianTableView reloadData];
    }];
}

- (void)requstAllOnlineList {
    NSDictionary *param = @{
        @"page":[NSString stringWithFormat:@"%ld", (long)_page],
        @"pagenum":@"10",
        @"is_online":@"1",
        @"state":@"0,1",
        @"sex":self.isMan ? @"1" : @"2",
    };
    NSDictionary *param1 = @{
        @"page":[NSString stringWithFormat:@"%ld", (long)_page],
        @"pagenum":@"10",
        @"is_online":@"1",
        @"state":@"0,1",
        @"sex":@"",
    };
    kWeakSelf
    self.isLoadingData = YES;
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/MemberOnline/OnlineList" parameters:[CXClientModel instance].room.RoomData.RoomType.integerValue == 5 ? param:param1 callback:^(id responseObject, BOOL isCache, NSError *error) {
        weakSelf.isLoadingData = NO;
        if (!error) {
            self->_totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            NSArray *array = [NSArray modelArrayWithClass:[CXUserModel class] json:responseObject[@"data"][@"list"]];
            if (self->_page == 1) {
                weakSelf.dataArray = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataArray addObjectsFromArray:array];
            }

            [weakSelf.mianTableView.mj_header endRefreshing];
            [weakSelf.mianTableView.mj_footer endRefreshing];
            if (self->_totalPage <= self->_page) {
                [weakSelf.mianTableView.mj_footer endRefreshingWithNoMoreData];
            }
            
            [weakSelf.mianTableView reloadData];
        }
    }];
}

#pragma mark - UITableViewDataSource/Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.selected_type == 0) { // 已申请
        XYRoomAlreadListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"XYRoomAlreadListCellID"];
        LiveRoomMicroOrder *model = self.dataArray[indexPath.row];
        cell.order = model;
        cell.checkUserProfileBlock = ^{
            [AppController showUserProfile:model.modelUser.UserId];
        };
        MJWeakSelf;
        cell.alreadListOpreationBlock = ^(AlreadButOpreationType opreationType , LiveRoomMicroOrder * order) {
            
            if (opreationType == joinOpreationType) {
                SocketMessageJoinSeat * join = [SocketMessageJoinSeat new];
                join.TargetUserId = [order.modelUser.UserId numberValue];
                [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                    
                    if (request.noError) {//成功了  删除在队列中的成员 , 这里自动就减少了，厉害，我都还没有完全搞清楚
                        
                    }
                    [weakSelf hide];
                }];
            }
            else if (opreationType == deleteOpreationType){
                SocketMessage23 * message = [SocketMessage23 new];
                message.Id = [order.modelUser.UserId numberValue];
                [[CXClientModel instance] sendSocketRequest:message withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                    
                    [weakSelf hide];
                    
                }];
                
            }
            else if (opreationType == upOpreationType){
                SocketMessage22 * message = [SocketMessage22 new];
                message.Id = [order.modelUser.UserId numberValue];
                [[CXClientModel instance] sendSocketRequest:message withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                    
                    [weakSelf hide];
                }];
                
            }
            
        };
        return cell;
    } else if (self.selected_type == 1) {
        CXAllOnlineListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXAllOnlineListCellID"];
        [cell.applyButton setTitle:nil forState:UIControlStateNormal];
        SocketMessageGetOnlineUserListResponseOnlineUser *model = self.dataArray[indexPath.row];
        cell.onlineUser = model;
        cell.checkUserProfileBlock = ^{
            [AppController showUserProfile:model.User.UserId];
        };
        cell.alphView.hidden = YES;
        if ([self.roomOnlistJoinedArrays containsObject:model.User.UserId]) {
            [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
            cell.alphView.hidden = NO;
        } else {
            if ([self.selectedArray containsObject:model.User.UserId]) {
                [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
            } else {
                [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
            }
        }
        
        kWeakSelf
        cell.applyActionBlock = ^{
            if (weakSelf.selectedArray.count >= 10 - [[CXClientModel instance].room.applyNumber_man integerValue]) {
                [weakSelf toast:@"麦序已满，不能添加"];
                return ;
            }
            if (![self.roomOnlistJoinedArrays containsObject:model.User.UserId]) {
                if ([weakSelf.selectedArray containsObject:model.User.UserId]) {
                    [weakSelf.selectedArray removeObject:model.User.UserId];
                } else {
                    [weakSelf.selectedArray addObject:model.User.UserId];
                }
                [weakSelf.mianTableView reloadData];
            }
        };
        
        return cell;
    } else if (_selected_type == 2) {
        CXAllOnlineListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXAllOnlineListCellID"];
        [cell.applyButton setTitle:nil forState:UIControlStateNormal];
        CXUserModel *model = self.dataArray[indexPath.row];
        cell.model = model;
        cell.checkUserProfileBlock = ^{
            [AppController showUserProfile:model.user_id];
        };
        cell.alphView.hidden = YES;
        if ([self.selectedArray containsObject:model.user_id]) {
            [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
        } else {
            [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
        }
        kWeakSelf
        cell.applyActionBlock = ^{
            if (weakSelf.selectedArray.count >= 10 - [[CXClientModel instance].room.applyNumber_man integerValue]) {
                [weakSelf toast:@"麦序已满，不能添加"];
                return ;
            }
            if ([weakSelf.selectedArray containsObject:model.user_id]) {
                [weakSelf.selectedArray removeObject:model.user_id];
            } else {
                [weakSelf.selectedArray addObject:model.user_id];
            }
            [weakSelf.mianTableView reloadData];
        };
        
        return cell;
    } else {
        CXAllOnlineListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXAllOnlineListCellID"];
        [cell.applyButton setTitle:nil forState:UIControlStateNormal];
        CXFriendInviteModel *model = self.dataArray[indexPath.row];
        cell.friendModel = model;
        cell.checkUserProfileBlock = ^{
            [AppController showUserProfile:model.user_id];
        };
        cell.alphView.hidden = YES;
        if ([self.roomOnlistJoinedArrays containsObject:model.user_id]) {
            [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
            cell.alphView.hidden = NO;
        } else {
            if ([self.selectedArray containsObject:model.user_id]) {
                [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
            } else {
                [cell.applyButton setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
            }
        }
        kWeakSelf
        cell.applyActionBlock = ^{
            if (weakSelf.selectedArray.count >= 10 - [[CXClientModel instance].room.applyNumber_man integerValue]) {
                [weakSelf toast:@"麦序已满，不能添加"];
                return ;
            }
            if ([weakSelf.selectedArray containsObject:model.user_id]) {
                [weakSelf.selectedArray removeObject:model.user_id];
            } else {
                [weakSelf.selectedArray addObject:model.user_id];
            }
            [weakSelf.mianTableView reloadData];
        };
        
        return cell;
    }
}
//
//- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
//    if (_selected_type == 0) {
//        return;
//    }
//    
//    if (self.selected_type == 1) {
//        SocketMessageGetOnlineUserListResponseOnlineUser *model = self.dataArray[indexPath.row];
//        if (self.selectedArray.count >= 10 - [[CXClientModel instance].room.applyNumber_man integerValue]) {
//            [self toast:@"麦序已满，不能添加"];
//            return ;
//        }
//        if (![self.roomOnlistJoinedArrays containsObject:model.User.UserId]) {
//            if ([self.selectedArray containsObject:model.User.UserId]) {
//                [self.selectedArray removeObject:model.User.UserId];
//            } else {
//                [self.selectedArray addObject:model.User.UserId];
//            }
//            [self.mianTableView reloadData];
//        }
//    } else if (self.selected_type == 2) {
//        CXUserModel *model = self.dataArray[indexPath.row];
//        if (self.selectedArray.count >= 10 - [[CXClientModel instance].room.applyNumber_man integerValue]) {
//            [self toast:@"麦序已满，不能添加"];
//            return ;
//        }
//        if ([self.selectedArray containsObject:model.user_id]) {
//            [self.selectedArray removeObject:model.user_id];
//        } else {
//            [self.selectedArray addObject:model.user_id];
//        }
//        [self.mianTableView reloadData];
//    } else {
//        CXFriendInviteModel *model = self.dataArray[indexPath.row];
//        if (self.selectedArray.count >= 10 - [[CXClientModel instance].room.applyNumber_man integerValue]) {
//            [self toast:@"麦序已满，不能添加"];
//            return ;
//        }
//        if ([self.selectedArray containsObject:model.user_id]) {
//            [self.selectedArray removeObject:model.user_id];
//        } else {
//            [self.selectedArray addObject:model.user_id];
//        }
//        [self.mianTableView reloadData];
//    }
//}

#pragma mark - Action
- (IBAction)applyAction:(UIButton *)sender {
    if (_selectedArray.count == 0) {
        return;
    }
    
    kWeakSelf
    BOOL isFree = YES;
    if (sender.tag == 30) { // 收费
        isFree = NO;
    }
    
    if (self.selected_type == 1) { // 房间内
        NSMutableArray *tempArrays = [NSMutableArray array];
        for (int i = 0; i < _selectedArray.count; i++) {
            NSDictionary *dict = @{@"Id": _selectedArray[i]};
            [tempArrays addObject:dict];
        }
        SocketMessageInvite * invite = [SocketMessageInvite new];
        invite.TargetUserIds = tempArrays;
        invite.Free = isFree == YES ? @(1) : @(2);
        invite.Level = @(_type_level);
        [[CXClientModel instance] sendSocketRequest:invite withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
            if (request.noError && request.response.isSuccess) {
                [weakSelf hide];
            }
        }];
    } else {
        NSMutableDictionary *param = [NSMutableDictionary dictionary];
        [param setValue:[CXClientModel instance].userId forKey:@"inviterId"];
        [param setValue:[_selectedArray componentsJoinedByString:@","] forKey:@"inviteeId"];
        [param setValue:[CXClientModel instance].room.RoomData.RoomId forKey:@"roomId"];
        [param setValue:isFree == YES ? @"1" : @"2" forKey:@"free"];
        if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) {
            if (_isMan) {
                [param setValue:@"1" forKey:@"micro_level"];
            } else {
                [param setValue:@"2" forKey:@"micro_level"];
            }
        } else if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 8) {
            if (_type_level == 4) { // 沙发
                [param setValue:@"4" forKey:@"micro_level"];;
            } else {
                [param setValue:@"3" forKey:@"micro_level"];
            }
        } else {
            [param setValue:@"3" forKey:@"micro_level"];
        }
        if (isFree == YES) {
            [param setValue:@"0" forKey:@"micro_cost"];
        } else {
            [param setValue:[CXClientModel instance].room.OnMicroCost.stringValue forKey:@"micro_cost"];
        }
        kWeakSelf
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Keng/inviteUp" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                [weakSelf hide];
            } else {
                [weakSelf toast:@"邀请失败，请重试"];
            }
        }];
    }
}

- (IBAction)closeAction:(id)sender {
    [self hide];
}

- (void)hide {
    [self.view removeFromSuperview];
    [self removeFromParentViewController];
}

- (void)reloadSelectedBtnStatus {
    [_apply_btn setTitleColor:_selected_type == 0 ? [UIColor whiteColor] : [UIColor blackColor] forState:UIControlStateNormal];
    [_apply_btn setBackgroundImage:_selected_type == 0 ? _selectedBtnBGImage : [UIImage new] forState:UIControlStateNormal];
    
    [_room_btn setTitleColor:_selected_type == 1 ? [UIColor whiteColor] : [UIColor blackColor] forState:UIControlStateNormal];
    [_room_btn setBackgroundImage:_selected_type == 1 ? _selectedBtnBGImage : [UIImage new] forState:UIControlStateNormal];
    
    [_now_btn setTitleColor:_selected_type == 2 ? [UIColor whiteColor] : [UIColor blackColor] forState:UIControlStateNormal];
    [_now_btn setBackgroundImage:_selected_type == 2 ? _selectedBtnBGImage : [UIImage new] forState:UIControlStateNormal];
    
    [_friend_btn setTitleColor:_selected_type == 3 ? [UIColor whiteColor] : [UIColor blackColor] forState:UIControlStateNormal];
    [_friend_btn setBackgroundImage:_selected_type == 3 ? _selectedBtnBGImage : [UIImage new] forState:UIControlStateNormal];
}

@end

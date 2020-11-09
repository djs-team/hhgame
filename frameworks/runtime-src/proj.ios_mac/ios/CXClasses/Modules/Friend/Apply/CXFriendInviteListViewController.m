//
//  CXFriendInviteListViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/10/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXFriendInviteListViewController.h"
#import "CXFriendInviteCell.h"

@interface CXFriendInviteListViewController () <UITableViewDataSource, UITableViewDelegate>
{
    NSInteger _page;
    NSInteger _totalPage;
}

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@end

@implementation CXFriendInviteListViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.

    [self setupSubViews];
    
    _dataSources = [NSMutableArray array];
    
    _page = 1;
    
    [self getInviteListData];
}

- (void)getInviteListData {
    kWeakSelf
    
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"page": [NSString stringWithFormat:@"%ld", _page],
    };
    NSString *url = @"/index.php/Api/Friend/applyFriendList";
    if (self.friendApply == NO) {
        url = @"/index.php/Api/Friend/myFriendList";
    } 
    [CXHTTPRequest GETWithURL:url parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
       [weakSelf.mainTableView.mj_header endRefreshing];
       [weakSelf.mainTableView.mj_footer endRefreshing];
        
       if (!error) {
           NSArray *array = [NSArray modelArrayWithClass:[CXFriendInviteModel class] json:responseObject[@"data"][@"list"]];
           if (self->_page == 1) {
               
               [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXBaseTabBarViewController_reloadSystemUnreadCount object:nil];
               
               weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
           } else {
               [weakSelf.dataSources addObjectsFromArray:array];
           }
           
           [weakSelf.mainTableView reloadData];

           self->_totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
           if (self->_totalPage <= self->_page) {
               [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
           }
       }
    }];
}

- (void)agreeInviteWithInviteModel:(CXFriendInviteModel *)model {
    if ([model.is_friend intValue] == 1) {

        NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
        NSDictionary *param = @{
            @"signature": signature,
            @"touid": model.user_id,
            @"gift_id": model.gift_id,
            @"apply_id": model.apply_id
        };
        kWeakSelf
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Friend/agreeFriendly" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
           if (!error) {
               [weakSelf getInviteListData];

               EMTextMessageBody *body = [[EMTextMessageBody alloc] initWithText:@"我接受你了你的好友请求，开聊吧。"];
               NSString *from = [[EMClient sharedClient] currentUsername];
               EMMessage *message = [[EMMessage alloc] initWithConversationID:model.user_id from:from to:model.user_id body:body ext:nil];
               message.chatType = EMChatTypeChat;
               [[EMClient sharedClient].chatManager sendMessage:message progress:nil completion:nil];
           }
        }];
    } else {
        NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
        NSDictionary *param = @{
            @"signature": signature,
            @"touid": model.user_id,
            @"gift_id": model.gift_id,
            @"apply_id": model.apply_id
        };
        kWeakSelf
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Friend/passFriendly" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
           if (!error) {
               [weakSelf getInviteListData];

               EMTextMessageBody *body = [[EMTextMessageBody alloc] initWithText:@"我接受你了你的好友请求，开聊吧。"];
               NSString *from = [[EMClient sharedClient] currentUsername];
               EMMessage *message = [[EMMessage alloc] initWithConversationID:model.user_id from:from to:model.user_id body:body ext:nil];
               message.chatType = EMChatTypeChat;
               [[EMClient sharedClient].chatManager sendMessage:message progress:nil completion:nil];
           }
        }];
    }
    
}

- (void)rejestInviteWithInviteModel:(CXFriendInviteModel *)model {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"touid": model.user_id,
        @"gift_id": model.gift_id,
        @"apply_id": model.apply_id
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Friend/delFriendly" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           [weakSelf getInviteListData];
       }
    }];
}

#pragma makr - UITableViewDelegate/Datasource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXFriendInviteCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXFriendInviteCellID"];
    CXFriendInviteModel *model = _dataSources[indexPath.row];
    cell.inviteModel = model;
    kWeakSelf
    cell.agreeActionBlock = ^{
        [weakSelf agreeInviteWithInviteModel:model];
    };
    cell.rejestActionBlock = ^{
        [weakSelf rejestInviteWithInviteModel:model];
    };
    
    cell.avatarTapGestureBlock = ^{
        if ([model.room_id integerValue] > 0) {
            if ([CXClientModel instance].isJoinedRoom) {
                for (UIViewController *controller in self.navigationController.viewControllers) {
                    if ([controller isKindOfClass:[CXLiveRoomViewController class]]) {
                        [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXLiveRoomViewController_joinNewRoom object:nil userInfo:@{@"roomId" : model.room_id}];
                        
                        CXLiveRoomViewController *vc = (CXLiveRoomViewController *)controller;
                        [self.navigationController popToViewController:vc animated:YES];
                    }
                }
            } else {
                [AppController joinRoom:model.room_id];
            }
        } else {
            [AppController showUserProfile:model.user_id target:weakSelf];
        }
    };
    
    if (self.friendApply == YES) {
        [cell.fromTipLabel setTitle:@"请求和你成为好友" forState:UIControlStateNormal];
        //1:同意 2:拒绝 3:申请中 4:已过期
        if ([model.mstatus integerValue] == 1) {
            cell.rejestButton.hidden = YES;
            cell.agreeButton.hidden = YES;
            cell.stateLabel.hidden = NO;
            cell.stateLabel.text = @"    已添加    ";
        } else if ([model.mstatus integerValue] == 2) {
            cell.rejestButton.hidden = YES;
            cell.agreeButton.hidden = YES;
            cell.stateLabel.hidden = NO;
            cell.stateLabel.text = @"    已拒绝    ";
        } else if ([model.mstatus integerValue] == 4) {
            cell.rejestButton.hidden = YES;
            cell.agreeButton.hidden = YES;
            cell.stateLabel.hidden = NO;
            cell.stateLabel.text = @"    已过期    ";
        } else {
            cell.rejestButton.hidden = NO;
            cell.agreeButton.hidden = NO;
            cell.stateLabel.hidden = YES;
        }
    } else {
        [cell.fromTipLabel setTitle:@"申请成为Ta的好友" forState:UIControlStateNormal];
        cell.rejestButton.hidden = YES;
        cell.agreeButton.hidden = YES;
        cell.stateLabel.hidden = NO;
        //1:同意 2:拒绝 3:申请中 4:已过期
        if ([model.mstatus integerValue] == 2) {
            cell.stateLabel.text = @" 对方已拒绝礼物已退回 ";
        } else if ([model.mstatus integerValue] == 1) {
            cell.stateLabel.text = @"  已成为好友  ";
        } else if ([model.mstatus integerValue] == 4) {
            cell.stateLabel.text = @"  已过期  ";
        } else {
            cell.stateLabel.text = @"  等待对方通过  ";
        }
    }
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXFriendInviteModel *model = _dataSources[indexPath.row];
    if ([model.gift_id integerValue] > 0 ) {
        return 148;
    }
    return 80;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXFriendInviteModel *model = _dataSources[indexPath.row];
    [AppController showUserProfile:model.user_id];
}


- (void)headerRefresh {
    _page = 1;
    [self getInviteListData];
}

- (void)footerRefresh {
    if (_page == _totalPage) {
        return;
    }
    _page++;
    [self getInviteListData];
}

#pragma mark - SubViews
- (void)setupSubViews {
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXFriendInviteCell" bundle:nil] forCellReuseIdentifier:@"CXFriendInviteCellID"];
    self.mainTableView.tableFooterView = [UIView new];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];

}

@end

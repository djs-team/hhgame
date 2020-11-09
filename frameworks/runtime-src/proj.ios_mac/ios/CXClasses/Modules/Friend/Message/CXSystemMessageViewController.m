//
//  CXSystemMessageViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/1.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSystemMessageViewController.h"
#import "CXSystemMessageCell.h"

@interface CXSystemMessageViewController () <UITableViewDataSource, UITableViewDelegate>
{
    NSInteger _page;
}

@property (nonatomic, copy) NSMutableArray *dataSource;

@end

@implementation CXSystemMessageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    _dataSource = [NSMutableArray array];
    
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXSystemMessageCell" bundle:nil] forCellReuseIdentifier:@"CXSystemMessageCellID"];
    
    self.mainTableView.estimatedRowHeight = 100;
    self.mainTableView.rowHeight = UITableViewAutomaticDimension;
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    
    UILongPressGestureRecognizer *longpress = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(pressAction:)];
    [self.mainTableView addGestureRecognizer:longpress];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    _page = 1;
    
    [self getMessageList];
}

- (void)headerRefresh {
    _page = 1;
    [self getMessageList];
}

- (void)footerRefresh {
    _page++;
    [self getMessageList];
}

- (void)getMessageList {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    NSDictionary *param = @{
        @"page" : [NSString stringWithFormat:@"%ld",_page],
        @"signature": signature
    };
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Pushmessage/systemList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
       if (!error) {
           NSArray *array = [NSArray modelArrayWithClass:[CXSystemMessageModel class] json:responseObject[@"data"][@"list"]];
           
           if (self->_page == 1) {
               [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXFriendViewController_reloadUnReadCount object:nil];
               [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXBaseTabBarViewController_reloadSystemUnreadCount object:nil];
               
               weakSelf.dataSource = [NSMutableArray arrayWithArray:array];
           } else {
               [weakSelf.dataSource addObjectsFromArray:array];
           }
           
           [weakSelf.mainTableView reloadData];
           
           NSInteger totalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
           
           if (totalPage <= self->_page) {
               [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
           }
       }
    }];
}

#pragma mark - UITabelViewDataSource/Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _dataSource.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXSystemMessageCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXSystemMessageCellID"];
    
    CXSystemMessageModel *model = _dataSource[indexPath.row];
    cell.timeLabel.text = model.time;
    cell.contentLabel.text = model.content;
    
    return cell;
}

- (void)pressAction:(UILongPressGestureRecognizer *)longPressGesture {
    if (longPressGesture.state == UIGestureRecognizerStateBegan) {//手势开始
        CGPoint point = [longPressGesture locationInView:self.mainTableView];
        NSIndexPath *currentIndexPath = [self.mainTableView indexPathForRowAtPoint:point]; // 可以获取我们在哪个cell上长按
        CXSystemMessageModel *model = [self.dataSource objectAtIndex:currentIndexPath.row];
        kWeakSelf
        [self alertTitle:@"是否要删除该消息" message:@"删除后不可恢复!" confirm:@"确定" cancel:@"取消" confirm:^{
            [weakSelf deleteSystemMessageWithMessageId:model.messageId];
        } cancel:nil];
    }
}

- (void)deleteSystemMessageWithMessageId:(NSString *)messageId {
    NSDictionary *param = @{
        @"id" : messageId,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Pushmessage/systemDel" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [weakSelf toast:@"删除成功"];
            self->_page = 1;
            [weakSelf getMessageList];
        }
    }];
}

@end

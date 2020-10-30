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
@property (nonatomic, copy) NSArray *dataSource;

@end

@implementation CXSystemMessageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXSystemMessageCell" bundle:nil] forCellReuseIdentifier:@"CXSystemMessageCellID"];
    
    self.mainTableView.estimatedRowHeight = 100;
    self.mainTableView.rowHeight = UITableViewAutomaticDimension;
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self getMessageList];
}

- (void)getMessageList {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Pushmessage/systemList" parameters:@{@"signature": signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           NSArray *array = [NSArray modelArrayWithClass:[CXSystemMessageModel class] json:responseObject[@"data"][@"list"]];
           weakSelf.dataSource = [NSArray arrayWithArray:array];
           [weakSelf.mainTableView reloadData];
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

@end

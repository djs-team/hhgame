//
//  CXLiveRoomSetupTypeHelpViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/6/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomSetupTypeHelpViewController.h"
#import "CXLiveRoomSetupTypeHelpCell.h"

@interface CXLiveRoomSetupTypeHelpViewController () <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) NSArray *dataSources;

@end

@implementation CXLiveRoomSetupTypeHelpViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"房间类型";
    self.view.backgroundColor = UIColorHex(0xF8F8F8);
    self.mainTableView.backgroundColor = UIColorHex(0xF8F8F8);
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    
    self.mainTableView.estimatedRowHeight = 60;
    self.mainTableView.rowHeight = UITableViewAutomaticDimension;
    
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomSetupTypeHelpCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomSetupTypeHelpCellID"];
    
    [self getHelpList];
}

- (void)getHelpList {
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Languageroom/room_type_help" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.dataSources = [NSArray arrayWithArray:responseObject[@"data"][@"list"]];
            [weakSelf.mainTableView reloadData];
        }
    }];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomSetupTypeHelpCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLiveRoomSetupTypeHelpCellID"];
    NSDictionary *dict = _dataSources[indexPath.row];
    cell.room_typeLabel.text = dict[@"tag"];
    cell.room_typeDescLabel.text = dict[@"value"];
    
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0.01;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    return 0.01;
}

@end

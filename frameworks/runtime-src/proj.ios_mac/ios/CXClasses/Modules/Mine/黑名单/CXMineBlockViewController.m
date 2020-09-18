//
//  CXMineBlockViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/28.
//

#import "CXMineBlockViewController.h"
#import "CXMineBlockCell.h"
#import "CXMineBlockActionView.h"
#import "CXLiveRoomUserProfileReportView.h"

@interface CXMineBlockViewController () <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) NSArray *dataArrays;

@end

@implementation CXMineBlockViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"黑名单";
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXMineBlockCell" bundle:nil] forCellReuseIdentifier:@"CXMineBlockCellID"];
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    
    self.mainTableView.tableFooterView = [UIView new];
    
    [self getBlockData];
}

- (void)getBlockData {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/listBlack" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.dataArrays = [NSArray modelArrayWithClass:[CXUserModel class] json:responseObject[@"data"]];
            [weakSelf.mainTableView reloadData];
        }
    }];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _dataArrays.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXMineBlockCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXMineBlockCellID"];
    CXUserModel *user = _dataArrays[indexPath.row];
    [cell.avatar sd_setImageWithURL:[NSURL URLWithString:user.avatar]];
    cell.nameLabel.text = user.nickname;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXUserModel *user = _dataArrays[indexPath.row];
    CXMineBlockActionView *view = [[NSBundle mainBundle] loadNibNamed:@"CXMineBlockActionView" owner:self options:nil].firstObject;
    kWeakSelf
    view.blockActionBlock = ^(NSInteger tag) {
        if (tag == 10) { // 查看详情
            [AppController showUserProfile:user.user_id];
        } else if (tag == 20) { // 取消拉黑
            CXSystemAlertView *alertView = [CXSystemAlertView loadNib];
            [alertView showAlertTitle:@"是否取消拉黑该用户？" message:nil cancel:nil sure:^{
               [weakSelf cancelBlockWithUser:user];
            }];
            [alertView show];
        } else {// 举报
            [weakSelf reportWithUser:user];
        }
    };
    [view show];
}

- (void)cancelBlockWithUser:(CXUserModel *)user {
   NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
   NSDictionary *param = @{
       @"signature": signature,
       @"user_id" : user.user_id
   };
   NSString *url = @"/index.php/Api/Member/blackout";
   kWeakSelf
   [CXHTTPRequest POSTWithURL:url parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
      if (!error) {
          [weakSelf toast:@"取消拉黑成功"];
          [weakSelf getBlockData];
      }
   }];
}

- (void)reportWithUser:(CXUserModel *)user {
    CXLiveRoomUserProfileReportView *view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomUserProfileReportView" owner:self options:nil].firstObject;
    view.userId = user.user_id;
    [view show];
}


@end

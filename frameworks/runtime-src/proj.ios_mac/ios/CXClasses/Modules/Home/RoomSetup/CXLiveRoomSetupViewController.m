//
//  CXLiveRoomSetupViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/10/21.
//

#import "CXLiveRoomSetupViewController.h"
#import "CXLiveRoomSetupRoomNameCell.h"
#import "SocketMessageSetName.h"

@interface CXLiveRoomSetupViewController () <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (strong, nonatomic) UITextField *nameTextField;
@property (nonatomic, copy) NSString *room_name;

@end

@implementation CXLiveRoomSetupViewController

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"房间设置";
    
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithTitle:@"保存" style:UIBarButtonItemStylePlain target:self action:@selector(saveProfile)];
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomSetupRoomNameCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomSetupRoomNameCellID"];
    self.mainTableView.tableFooterView = [UIView new];
    
    _room_name = [CXClientModel instance].room.RoomData.RoomName;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomSetupRoomNameCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLiveRoomSetupRoomNameCellID"];
    cell.nameTextField.text = _room_name;
    self.nameTextField = cell.nameTextField;
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 40;
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    [self.view endEditing:YES];
}

- (void)saveProfile {
    _room_name = self.nameTextField.text;
    if (_room_name.length < 0) {
        [self toast:@"房间名不能为空"];
        return;
    }
    kWeakSelf
    SocketMessageSetName *request = [SocketMessageSetName new];
    request.Name = _room_name;
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
        if (request.response.Success.integerValue == 1) {
            [weakSelf toast:@"修改成功"];
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [weakSelf.navigationController popViewControllerAnimated:YES];
            });
        } else {
            [weakSelf toast:request.response.Code];
        }
    }];
}

@end

//
//  CXHomeSearchViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import "CXHomeSearchViewController.h"
#import "CXHomeSearchCell.h"
#import "CXUserModel.h"
#import "CXHomeRoomModel.h"

@interface CXHomeSearchViewController () <UITextFieldDelegate, UITableViewDataSource, UITableViewDelegate>

@property (strong, nonatomic) IBOutlet UITableView *mainTableView;
@property (nonatomic, weak) UITextField * searchTextField;

@property (nonatomic, strong) NSArray *peopleDataSources;
//@property (nonatomic, strong) NSArray *roomDataSources;

@end

@implementation CXHomeSearchViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self setupSubViews];
    
    _peopleDataSources = [NSArray array];
//    _roomDataSources = [NSArray array];
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    self.mainTableView.tableFooterView = [UIView new];
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXHomeSearchCell" bundle:nil] forCellReuseIdentifier:@"CXHomeSearchCellID"];
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.peopleDataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXHomeSearchCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXHomeSearchCellID"];
    CXUserModel *user = _peopleDataSources[indexPath.row];
    cell.user = user;
    cell.avatarTapGestureBlock = ^{
        [AppController joinRoom:user.room_id];
    };
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXUserModel *user = _peopleDataSources[indexPath.row];
    [AppController showUserProfile:user.user_id];
}

#pragma mark - Private
- (void)setupSubViews {
    
    [self.navigationItem setLeftBarButtonItem:[UIBarButtonItem new]];
    
    UIButton * cancleBtn = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 50, 44)];
    [cancleBtn setTitle:@"取消" forState:UIControlStateNormal];
    [cancleBtn setTitleColor:[UIColor blackColor] forState:UIControlStateNormal];
    [cancleBtn addTarget:self action:@selector(searchCancleClick) forControlEvents:UIControlEventTouchUpInside];
    cancleBtn.titleLabel.font = [UIFont systemFontOfSize:13.0f];
    UIBarButtonItem * cancel = [[UIBarButtonItem alloc] initWithCustomView:cancleBtn];
    self.navigationItem.rightBarButtonItem = cancel;
    
    UIView *titleView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH-100, 30)];
    titleView.backgroundColor = UIColorHex(0xF3F3F3);
    titleView.clipsToBounds = YES;
    titleView.layer.cornerRadius = 15;
    
    UITextField * searchTextField = [[UITextField alloc]initWithFrame:CGRectMake(40, 0, SCREEN_WIDTH-100-40, 30)];
    [titleView addSubview:searchTextField];
    searchTextField.placeholder = @"搜索昵称、ID";
    searchTextField.font = [UIFont systemFontOfSize:13.0f];
    searchTextField.textColor = UIColorHex(0x333333);
    [searchTextField setReturnKeyType:UIReturnKeySearch];
    UIImageView *imageViewPwd = [[UIImageView alloc] initWithFrame:CGRectMake(10, 6, 18, 18)];
    imageViewPwd.image = [UIImage imageNamed:@"search_gray"];
    [titleView addSubview:imageViewPwd];
    self.navigationItem.titleView = titleView;
    self.searchTextField = searchTextField;
    self.searchTextField.delegate = self;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField{
    [self.searchTextField resignFirstResponder];
    
    NSDictionary *params = @{
        @"search": textField.text,
        @"type": @"1",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Search/searchmsg" parameters:params callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (responseObject) {
            weakSelf.peopleDataSources = [NSArray modelArrayWithClass:[CXUserModel class] json:responseObject[@"data"][@"member_list"]];
//            weakSelf.roomDataSources = [NSArray modelArrayWithClass:[CXHomeRoomModel class] json:responseObject[@"room_list"]];
            
            [weakSelf.mainTableView reloadData];
        }
    }];
    
    return YES;
}

- (void)searchCancleClick{
    [self.searchTextField resignFirstResponder];
    [self.navigationController popViewControllerAnimated:YES];
}

@end

//
//  CXFriendApplyViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/12.
//

#import "CXFriendApplyViewController.h"
#import "CXFriendMessageViewController.h"
#import "CXFriendInviteListViewController.h"

@interface CXFriendApplyViewController () <CXPageViewControllerDataSource>

@end

@implementation CXFriendApplyViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = @"我的好友";
    
    CXFriendMessageViewController *friendVC = [CXFriendMessageViewController new];
    
    CXFriendInviteListViewController *friendApply = [CXFriendInviteListViewController new];
    friendApply.friendApply = YES;
    
    CXFriendInviteListViewController *mineApply = [CXFriendInviteListViewController new];
    mineApply.friendApply = NO;
    
    self.viewControllerClasses = [NSArray arrayWithObjects:friendVC, friendApply, mineApply, nil];
    
    NSDictionary *friendItem = @{@"title":@"我的好友", @"count":@"0"};
    NSDictionary *friendapplyItem = @{@"title":@"好友申请", @"count":@"0"};
    NSDictionary *mineItem = @{@"title":@"我的申请", @"count":@"0"};
    self.titles = [NSArray arrayWithObjects:friendItem, friendapplyItem, mineItem, nil];
    self.itemWidth = 80;
    
    [super viewDidLoad];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];

    [self getUnReadCountData];
}

- (void)getUnReadCountData {
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Friend/messageNum" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSString *my_apply = responseObject[@"data"][@"my_apply"];
            NSString *apply_my = responseObject[@"data"][@"apply_my"];
//            NSString *system_num = responseObject[@"data"][@"system_num"];
            NSDictionary *friendItem = @{@"title":@"我的好友", @"count":@"0"};
            NSDictionary *friendapplyItem = @{@"title":@"好友申请", @"count":my_apply.integerValue > 0 ? my_apply : @"0"};
            NSDictionary *mineItem = @{@"title":@"我的申请", @"count":apply_my.integerValue > 0 ? apply_my : @"0"};
            weakSelf.titles = [NSArray arrayWithObjects:friendItem, friendapplyItem, mineItem, nil];
            [weakSelf reloadData];
        }
    }];
}

#pragma mark - CXPageViewControllerDataSource
- (CGRect)pageControllerMenuViewRect {
    return CGRectMake((SCREEN_WIDTH - (80 * 3 + 30*2))/2, 0, 80 * 3 + 30*2, 44);
}

- (CGRect)pageControllerContentViewRect {
    return CGRectMake(0, 44, SCREEN_WIDTH, self.view.bounds.size.height - 44);
}


@end

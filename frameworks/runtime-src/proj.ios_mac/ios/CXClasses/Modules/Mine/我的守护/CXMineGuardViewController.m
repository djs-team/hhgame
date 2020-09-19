//
//  CXMineGuardViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "CXMineGuardViewController.h"
#import "CXMineGuardListViewController.h"

@interface CXMineGuardViewController () <CXPageViewControllerDataSource>

@end

@implementation CXMineGuardViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title = @"我的守护";
    
    CXMineGuardListViewController *friendApply = [CXMineGuardListViewController new];
    friendApply.isMineGuard = YES;
    
    CXMineGuardListViewController *mineApply = [CXMineGuardListViewController new];
    mineApply.isMineGuard = NO;
    self.viewControllerClasses = [NSArray arrayWithObjects:friendApply, mineApply, nil];
    
    NSDictionary *friendItem = @{@"title":@"我守护的人", @"count":@"0"};
    NSDictionary *friendapplyItem = @{@"title":@"守护我的人", @"count":@"0"};
    
    self.titles = [NSArray arrayWithObjects:friendItem, friendapplyItem, nil];
    self.itemWidth = 80;
    
    [super viewDidLoad];
}

#pragma mark - CXPageViewControllerDataSource
- (CGRect)pageControllerMenuViewRect {
    return CGRectMake((SCREEN_WIDTH - (80 * 2 + 30*1))/2, 0, 80 * 2 + 30*1, 44);
}

- (CGRect)pageControllerContentViewRect {
    return CGRectMake(0, 44, SCREEN_WIDTH, self.view.bounds.size.height - 44);
}

@end

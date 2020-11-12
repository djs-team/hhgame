//
//  CXFindViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/1/14.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXFindViewController.h"
#import "CXNearbyViewController.h"
#import "CXNearbyRankViewController.h"

@interface CXFindViewController ()

@end

@implementation CXFindViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.titleSizeNormal = 16;
    self.titleSizeSelected = 18;
    self.titleColorSelected = UIColorHex(0x282828);
    self.titleColorNormal = UIColorHex(0x656565);
    self.automaticallyCalculatesItemWidths = YES;
    self.menuViewStyle = WMMenuViewStyleLine;
    self.progressColor = UIColorHex(0x282828);
    
//    CXFindViewController *vc = [CXFindViewController pageViewControllerWithControllers:[self getArrayVCs]
//                                                                                  titles:[self getArrayTitles]
//                                                                                  config:configration];
    self.titles = [self getArrayTitles];
    self.viewControllerClasses = [self getArrayVCs];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
//    if (self.app.isHomeCycleScrollToFind_rank == YES) {
//        self.app.isHomeCycleScrollToFind_rank = NO;
//        [self setSelectedPageIndex:3];
//    }
}

#pragma mark - Public Function

- (NSArray *)getArrayVCs {
    CXNearbyViewController *firstVC = [[CXNearbyViewController alloc] init];
    firstVC.vcType = 1;
    
    CXNearbyViewController *secondVC = [[CXNearbyViewController alloc] init];
    secondVC.vcType = 2;
    
//    CXNearbyViewController *thirdVC = [[CXNearbyViewController alloc] init];
//    thirdVC.vcType = 3;
    
//    CXNearbyRankViewController *fourVC = [[CXNearbyRankViewController alloc] init];
    
    return @[firstVC, secondVC];
}

- (NSArray *)getArrayTitles {
    return @[@"推荐", @"附近的人"];
}

- (CGRect)pageController:(WMPageController *)pageController preferredFrameForMenuView:(WMMenuView *)menuView{
    
    return CGRectMake(0, kStatusHeight, SCREEN_WIDTH, 44);
}
- (CGRect)pageController:(WMPageController *)pageController preferredFrameForContentView:(WMScrollView *)contentView{
    return  CGRectMake(0, kNavHeight, SCREEN_WIDTH, SCREEN_HEIGHT-kNavHeight-kBottomArea-49);
}
- (NSInteger)numbersOfChildControllersInPageController:(WMPageController *)pageController{
    return self.titles.count;
}

- (__kindof UIViewController *)pageController:(WMPageController *)pageController viewControllerAtIndex:(NSInteger)index{
    
    return (UIViewController *)self.viewControllerClasses[index];
}
- (NSString *)pageController:(WMPageController *)pageController titleAtIndex:(NSInteger)index{
    
    return self.titles[index];
}

@end

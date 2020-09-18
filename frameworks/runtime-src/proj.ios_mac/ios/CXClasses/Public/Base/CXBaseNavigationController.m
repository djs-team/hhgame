//
//  CXBaseNavigationController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/22.
//

#import "CXBaseNavigationController.h"

@interface CXBaseNavigationController ()

@end

@implementation CXBaseNavigationController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    NSDictionary *dict = @{NSForegroundColorAttributeName : UIColorHex(0x3B3B3B), NSFontAttributeName : [UIFont boldSystemFontOfSize:21]};
    self.navigationBar.titleTextAttributes = dict;
    [[UINavigationBar appearance] setShadowImage:[UIImage imageWithColor:[UIColor colorWithHexString:@"#FFFFFF"]]];
    [UINavigationBar appearance].translucent = NO;
    
    self.navigationBar.barTintColor = [UIColor whiteColor];
}

#pragma mark - UIGestureRecognizerDelegate
- (void)pushViewController:(UIViewController *)viewController animated:(BOOL)animated {
    
    if (viewController == nil) {
        return;
    }
    // 如果导航控制器里面的视图>0个
    if(self.childViewControllers.count > 0){
        // 隐藏标签控制器
        viewController.hidesBottomBarWhenPushed = YES;
        viewController.navigationItem.leftBarButtonItem = [[UIBarButtonItem alloc] initWithImage:[[UIImage imageNamed:@"back_gray"] imageWithRenderingMode:(UIImageRenderingModeAlwaysOriginal)] style:UIBarButtonItemStylePlain target:self action:@selector(backClick:)];
    }
    
    [super pushViewController:viewController animated:animated];
}

- (void)backClick:(UIBarButtonItem *)item {
    
    [self popViewControllerAnimated:true];
}

@end

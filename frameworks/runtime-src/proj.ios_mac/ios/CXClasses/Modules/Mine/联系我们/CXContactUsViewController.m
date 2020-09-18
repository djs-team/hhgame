//
//  CXContactUsViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/20.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXContactUsViewController.h"

@interface CXContactUsViewController ()

@property (nonatomic, strong) CAGradientLayer *gradientLayer;

@property (weak, nonatomic) IBOutlet UIView *bgView;

@end

@implementation CXContactUsViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"联系我们";
    
    [self.bgView.layer addSublayer:self.gradientLayer];
    self.gradientLayer.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    
    
}

- (CAGradientLayer *)gradientLayer {
    if (!_gradientLayer) {
        _gradientLayer = [[CAGradientLayer alloc] init];
        _gradientLayer.colors = @[(id)[UIColor colorWithHexString:@"#FFDC44"].CGColor, (id)[UIColor colorWithHexString:@"#FFC031"].CGColor];
        _gradientLayer.startPoint = CGPointMake(0, 0.5);
        _gradientLayer.endPoint = CGPointMake(0, 1);
//        _gradientLayer.cornerRadius = 30;
        _gradientLayer.masksToBounds = YES;
    }
    return _gradientLayer;
}

@end

//
//  CXWithdrawSelectedDateViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXWithdrawSelectedDateViewController.h"
#import "CXDateSelectedView.h"

@interface CXWithdrawSelectedDateViewController ()
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIButton *checkButton;
@property (weak, nonatomic) IBOutlet UIButton *b_timeButton;
@property (weak, nonatomic) IBOutlet UIButton *e_timeButton;

@property (nonatomic, strong) NSDate *b_time;
@property (nonatomic, strong) NSDate *e_time;

@property (nonatomic, strong) CXDateSelectedView *dateView;

@end

@implementation CXWithdrawSelectedDateViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"按日期查询";

    self.bgView.layer.masksToBounds = YES;
    self.bgView.layer.cornerRadius = 10;
    self.checkButton.layer.masksToBounds = YES;
    self.checkButton.layer.cornerRadius = 13;
    [self.checkButton setBackgroundImage:[UIImage x_gradientImageWithSize:CGSizeMake(113, 26) Color1:UIColorHex(0x97397D) color2:UIColorHex(0x3D1D5D)] forState:UIControlStateNormal];
    
    NSString *b_timeStr = [[NSDate date] stringWithFormat:@"yyyy年MM月dd日"];
    _b_time = [NSDate date];
    _e_time = [NSDate date];
    [self.b_timeButton setTitle:b_timeStr forState:UIControlStateNormal];
    [self.e_timeButton setTitle:b_timeStr forState:UIControlStateNormal];
}

- (IBAction)b_timeAction:(UIButton *)sender {
    [self lew_presentPopupView:self.dateView animation:nil];
    kWeakSelf
    self.dateView.selectedDateBlock = ^(NSDate * _Nonnull selectedDate) {
        NSString *timeStr = [selectedDate stringWithFormat:@"yyyy年MM月dd日"];
        [weakSelf.b_timeButton setTitle:timeStr forState:UIControlStateNormal];
        weakSelf.b_time = selectedDate;
    };
}
- (IBAction)e_timeAction:(UIButton *)sender {
    [self lew_presentPopupView:self.dateView animation:nil];
    kWeakSelf
    self.dateView.selectedDateBlock = ^(NSDate * _Nonnull selectedDate) {
        NSString *timeStr = [selectedDate stringWithFormat:@"yyyy年MM月dd日"];
        [weakSelf.e_timeButton setTitle:timeStr forState:UIControlStateNormal];
        weakSelf.e_time = selectedDate;
    };
}

- (IBAction)checkAction:(id)sender {
    if (self.selectedDateBlock) {
        self.selectedDateBlock(_b_time, _e_time);
    }
    [self.navigationController popViewControllerAnimated:YES];
}

- (CXDateSelectedView *)dateView {
    if (!_dateView) {
        _dateView = [[[NSBundle mainBundle] loadNibNamed:@"CXDateSelectedView" owner:nil options:nil] lastObject];
        _dateView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        _dateView.parentVC = self;
    }
    
    return _dateView;
}

@end

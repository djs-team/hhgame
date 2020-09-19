//
//  CXMyWalletViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXMyWalletViewController.h"
#import "MuaExchangeVC.h"
#import "CXWithdrawAccountViewController.h"
#import "CXWithdrawInfoViewController.h"
#import "CXWithdrawIncomeListViewController.h"
#import "CXExchangeBlueRoseViewController.h"

@interface CXMyWalletViewController ()
@property (weak, nonatomic) IBOutlet UIView *bgView;

@property (weak, nonatomic) IBOutlet UILabel *roseNumLabel;
@property (weak, nonatomic) IBOutlet UIButton *rechargeButton;

@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;
@property (weak, nonatomic) IBOutlet UIButton *exchangeButton;
@property (weak, nonatomic) IBOutlet UILabel *yesterdayLabel;
@property (weak, nonatomic) IBOutlet UILabel *todayLabel;
@property (weak, nonatomic) IBOutlet UILabel *weekLabel;
@property (weak, nonatomic) IBOutlet UILabel *monthLabel;
@property (weak, nonatomic) IBOutlet UIButton *shouruButton;
@property (weak, nonatomic) IBOutlet UIButton *tixianmingxiButton;
@property (weak, nonatomic) IBOutlet UIButton *tixianButton;
@property (weak, nonatomic) IBOutlet UIButton *shoukuanButton;

@property (weak, nonatomic) IBOutlet UILabel *redpack_balanceLabel;
@property (weak, nonatomic) IBOutlet UIButton *redpack_exchangeButton;
@property (weak, nonatomic) IBOutlet UILabel *redpack_yesterdayLabel;
@property (weak, nonatomic) IBOutlet UILabel *redpack_todayLabel;
@property (weak, nonatomic) IBOutlet UILabel *redpack_weekLabel;
@property (weak, nonatomic) IBOutlet UILabel *redpack_monthLabel;
@property (weak, nonatomic) IBOutlet UIButton *redpack_redpack_recordButton;
@property (weak, nonatomic) IBOutlet UIButton *redpack_tixianmingxiButton;
@property (weak, nonatomic) IBOutlet UIButton *redpack_tixianButton;
@property (weak, nonatomic) IBOutlet UIButton *redpack_shoukuanButton;

@end

@implementation CXMyWalletViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"我的钱包";
    
    [self setupSubViews];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBarHidden = YES;
    
    [self loadBalances];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
     self.navigationController.navigationBarHidden = NO;
}

- (void)loadBalances {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/initmymoney" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.roseNumLabel.text = [responseObject[@"data"][@"coin"] stringValue];
            weakSelf.balanceLabel.text = [NSString stringWithFormat:@"%.2f", [responseObject[@"data"][@"diamond"] floatValue]];
            weakSelf.todayLabel.text = [NSString stringWithFormat:@"今日：¥%.2f", [responseObject[@"data"][@"list_today_price"] floatValue]];
            weakSelf.yesterdayLabel.text = [NSString stringWithFormat:@"昨日：¥%.2f", [responseObject[@"data"][@"list_yes_price"] floatValue]];
            weakSelf.weekLabel.text = [NSString stringWithFormat:@"本周：¥%.2f", [responseObject[@"data"][@"list_week_price"] floatValue]];
            weakSelf.monthLabel.text = [NSString stringWithFormat:@"本月：¥%.2f", [responseObject[@"data"][@"list_month_price"] floatValue]];
            
            weakSelf.redpack_balanceLabel.text = [NSString stringWithFormat:@"%.2f", [responseObject[@"data"][@"redpacket_coin"] floatValue]];
            weakSelf.redpack_todayLabel.text = [NSString stringWithFormat:@"今日：¥%.2f", [responseObject[@"data"][@"red_list_today_price"] floatValue]];
            weakSelf.redpack_yesterdayLabel.text = [NSString stringWithFormat:@"昨日：¥%.2f", [responseObject[@"data"][@"red_list_yes_price"] floatValue]];
            weakSelf.redpack_weekLabel.text = [NSString stringWithFormat:@"本周：¥%.2f", [responseObject[@"data"][@"red_list_week_price"] floatValue]];
            weakSelf.redpack_monthLabel.text = [NSString stringWithFormat:@"本月：¥%.2f", [responseObject[@"data"][@"red_list_month_price"] floatValue]];
            
            // 是否显示提现
            if ([responseObject[@"data"][@"is_cash"] integerValue] == 1) {
                weakSelf.tixianmingxiButton.hidden = NO;
                weakSelf.tixianButton.hidden = NO;
                weakSelf.shoukuanButton.hidden = NO;
                weakSelf.redpack_tixianButton.hidden = NO;
                weakSelf.redpack_tixianmingxiButton.hidden = NO;
                weakSelf.redpack_shoukuanButton.hidden = NO;
            } else {
                weakSelf.tixianmingxiButton.hidden = YES;
                weakSelf.tixianButton.hidden = YES;
                weakSelf.shoukuanButton.hidden = YES;
                weakSelf.redpack_tixianButton.hidden = YES;
                weakSelf.redpack_tixianmingxiButton.hidden = YES;
                weakSelf.redpack_shoukuanButton.hidden = YES;
            }
            // 是否显示兑换
            if ([responseObject[@"data"][@"exchange_switch"] integerValue] == 1) {
                weakSelf.exchangeButton.hidden = NO;
                weakSelf.redpack_exchangeButton.hidden = NO;
            } else {
                weakSelf.exchangeButton.hidden = YES;
                weakSelf.redpack_exchangeButton.hidden = YES;
            }
        }
    }];
}

#pragma mark - Actions
- (IBAction)rechargeAction:(id)sender {
    CXRechargeViewController *VC = [CXRechargeViewController new];
    [self.navigationController pushViewController:VC animated:YES];
}

- (IBAction)balanceAction:(UIButton *)sender {
    switch (sender.tag) {
        case 10: // 兑换
        {
            MuaExchangeVC *vc = [MuaExchangeVC new];
            [self.navigationController pushViewController:vc animated:true];
        }
            break;
        case 11: // 收入明细
        {
            CXWithdrawIncomeListViewController *vc = [[CXWithdrawIncomeListViewController alloc] init];
            vc.incomeType = income;
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 12: // 提现明细
        {
            CXWithdrawIncomeListViewController *vc = [[CXWithdrawIncomeListViewController alloc] init];
            vc.incomeType = tixian;
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 13: // 提现
        {
            CXWithdrawInfoViewController *vc = [[CXWithdrawInfoViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
         case 14: // 收款账号
        {
            CXWithdrawAccountViewController *vc = [[CXWithdrawAccountViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        default:
            break;
    }
}

- (IBAction)redPack_balanceAction:(UIButton *)sender {
    switch (sender.tag) {
        case 20: // 兑换
        {
            MuaExchangeVC *vc = [MuaExchangeVC new];
            vc.isRedPacket = YES;
            [self.navigationController pushViewController:vc animated:true];
        }
            break;
        case 21: // 中奖记录
        {
            CXWithdrawIncomeListViewController *vc = [[CXWithdrawIncomeListViewController alloc] init];
            vc.incomeType = redpack_incom;
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 22: // 提现
        {
            CXWithdrawInfoViewController *vc = [[CXWithdrawInfoViewController alloc] init];
            vc.isRedPacket = YES;
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 23: // 提现明细
        {
            CXWithdrawIncomeListViewController *vc = [[CXWithdrawIncomeListViewController alloc] init];
            vc.incomeType = redpack_tixian;
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        case 24: // 收款账号
        {
            CXWithdrawAccountViewController *vc = [[CXWithdrawAccountViewController alloc] init];
            [self.navigationController pushViewController:vc animated:YES];
        }
            break;
        default:
            break;
    }
}

- (IBAction)backAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

#pragma mark - SubViews
- (void)setupSubViews {
    UIImage *gImage = [UIImage gradientImageWithSize:CGSizeMake(kScreenWidth, 149*SCALE_W) Color1:UIColorHex(0xEB5BBA) color2:UIColorHex(0x793EF2)];
    UIImageView *gImageView = [[UIImageView alloc] initWithImage:gImage];
    gImageView.frame = CGRectMake(0, 0, kScreenWidth, 149*SCALE_W);
    [self.view insertSubview:gImageView atIndex:0];
    
    UIBezierPath *path = [[UIBezierPath alloc] init];
    [path moveToPoint:CGPointMake(0, 0)];
    [path addLineToPoint:CGPointMake(0, 130*SCALE_W)];
    [path addQuadCurveToPoint:CGPointMake(SCREEN_WIDTH, 130*SCALE_W) controlPoint:CGPointMake(SCREEN_WIDTH/2, 149*SCALE_W)];
    [path addLineToPoint:CGPointMake(SCREEN_WIDTH, 0)];
    CAShapeLayer *layer = [[CAShapeLayer alloc] init];
    layer.path = path.CGPath;
    layer.frame = gImageView.bounds;
    gImageView.layer.mask = layer;
    
    UIView *view = [[UIView alloc] init];
    view.frame = CGRectMake(14,44+10+(SCREEN_WIDTH - 16)*129/359+2,SCREEN_WIDTH - 30, 248);
    view.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0];
    view.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.17].CGColor;
    view.layer.shadowOffset = CGSizeMake(0,2);
    view.layer.shadowOpacity = 1;
    view.layer.shadowRadius = 11;
    view.layer.cornerRadius = 10;
    [self.bgView insertSubview:view atIndex:0];
    
    UIView *view2 = [[UIView alloc] init];
    view2.frame = CGRectMake(14,44+10+(SCREEN_WIDTH - 16)*129/359+2+248+12,SCREEN_WIDTH - 30, 248);
    view2.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0];
    view2.layer.shadowColor = [UIColor colorWithRed:0/255.0 green:0/255.0 blue:0/255.0 alpha:0.17].CGColor;
    view2.layer.shadowOffset = CGSizeMake(0,2);
    view2.layer.shadowOpacity = 1;
    view2.layer.shadowRadius = 11;
    view2.layer.cornerRadius = 10;
    [self.bgView insertSubview:view2 atIndex:0];
    
    self.rechargeButton.layer.masksToBounds = YES;
    self.rechargeButton.layer.cornerRadius = 12;
    [self.rechargeButton setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(80, 24) Color1:UIColorHex(0xEC5BBA) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    
    [self setButtonLayer:self.exchangeButton];
    [self setButtonLayer:self.shouruButton];
    [self setButtonLayer:self.tixianmingxiButton];
    [self setButtonLayer:self.tixianButton];
    [self setButtonLayer:self.shoukuanButton];
    
    [self setButtonLayer:self.redpack_exchangeButton];
    [self setButtonLayer:self.redpack_redpack_recordButton];
    [self setButtonLayer:self.redpack_tixianmingxiButton];
    [self setButtonLayer:self.redpack_tixianButton];
    [self setButtonLayer:self.redpack_shoukuanButton];
    
    self.exchangeButton.hidden = YES;
    self.tixianmingxiButton.hidden = YES;
    self.tixianButton.hidden = YES;
    self.shoukuanButton.hidden = YES;
    self.redpack_exchangeButton.hidden = YES;
    self.redpack_tixianButton.hidden = YES;
    self.redpack_tixianmingxiButton.hidden = YES;
    self.redpack_shoukuanButton.hidden = YES;
}

- (void)setButtonLayer:(UIButton *)btn {
    btn.layer.masksToBounds = YES;
    btn.layer.cornerRadius = 12;
    btn.layer.borderColor = UIColorHex(0x7B3EF3).CGColor;
    btn.layer.borderWidth = 0.5;
}

@end

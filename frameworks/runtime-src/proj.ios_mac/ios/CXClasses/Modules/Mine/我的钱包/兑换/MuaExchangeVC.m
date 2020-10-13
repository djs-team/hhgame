//
//  MuaExchangeVC.m
//  hairBall
//
//  Created by shiwei on 2019/7/25.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MuaExchangeVC.h"
#import "MuaExchangeResultTipView.h"
#import "CXWithdrawIncomeListViewController.h"
#import "CXMineWalletModel.h"

@interface MuaExchangeVC () <UIScrollViewDelegate>

@property (weak, nonatomic) IBOutlet UITextField *diamondNumber;
@property (weak, nonatomic) IBOutlet UITextField *MCoinNumber;
@property (weak, nonatomic) IBOutlet UILabel *diamondCount;
@property (weak, nonatomic) IBOutlet UIButton *exchangeButton;

@property (nonatomic, strong) CXMineWalletModel *walletModel;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;

@property (weak, nonatomic) IBOutlet UILabel *tip;

@end

@implementation MuaExchangeVC

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:true];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"兑换";
    
    UIButton *rightBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 80, 44)];
    [rightBtn addTarget:self action:@selector(rightClick) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
    [rightBtn setTitle:@"兑换记录" forState:UIControlStateNormal];
    [rightBtn setTitleColor:UIColorHex(0x333333) forState:UIControlStateNormal];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:14.0f];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    
    [self requestData];
    
    self.scrollView.delegate = self;
    
    self.exchangeButton.layer.cornerRadius = 19;
    self.exchangeButton.layer.masksToBounds = YES;
    [self.exchangeButton setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xEC5BBA) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    
    [self.diamondNumber addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
}

- (void)textFieldDidChange:(UITextField *)textField {
    if (textField == _diamondNumber) {
        if (textField.text.length > 10) {
            self.diamondNumber.text = [textField.text substringToIndex:10];
        }
        
        NSUInteger scale = [self.walletModel.scale integerValue];
        NSUInteger dianum = [textField.text integerValue];
        if ([self.diamondNumber.text integerValue] > [self.walletModel.diamond integerValue]) {
            [self toast:@"余额不足"];
            self.diamondNumber.text = [textField.text substringToIndex:textField.text.length - 1];
            return;
        }
        if (scale) {
            self.MCoinNumber.text = [NSString stringWithFormat:@"%lu", dianum * scale];
        }
    }
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    [self.view endEditing:true];
}

- (void)rightClick {
    CXWithdrawIncomeListViewController *vc = [[CXWithdrawIncomeListViewController alloc] init];
    vc.incomeType = _isRedPacket == YES ? redpack_exchange : exchange;
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)requestData {
    kWeakSelf
    NSString *url = @"/index.php/Api/AliPay/initExchange";
    if (_isRedPacket) {
        url = @"/index.php/Api/AliPay/redpacket_initExchange";
    }
    [CXHTTPRequest POSTWithURL:url parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.walletModel = [CXMineWalletModel modelWithJSON:responseObject[@"data"]];
            weakSelf.diamondCount.text = [NSString stringWithFormat:@"%0.2f", [weakSelf.walletModel.diamond floatValue]];
            weakSelf.tip.text = [NSString stringWithFormat:@"* 1¥兑换%@玫瑰", weakSelf.walletModel.scale];
        }
    }];
}

- (IBAction)exchangeClick:(id)sender {
    
    if ([self.diamondNumber.text integerValue] <= 0) {
        [self toast:@"数量输入有误"];
        return;
    }
    
    if ([self.diamondNumber.text integerValue] > [self.walletModel.diamond integerValue]) {
        [self toast:@"余额不足"];
        return;
    }
    
    kWeakSelf
    NSString *url = @"/index.php/Api/AliPay/exchangeRose";
    if (_isRedPacket) {
        url = @"/index.php/Api/AliPay/redpacket_exchangeRose";
    }
    [CXHTTPRequest POSTWithURL:url parameters:@{@"coin": self.diamondNumber.text} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           [weakSelf showAlertWith:true];
           [weakSelf requestData];
           weakSelf.diamondNumber.text = @"";
           weakSelf.MCoinNumber.text = @"0";
       } else {
           [weakSelf showAlertWith:false];
       }
    }];
}

- (void)showAlertWith:(Boolean)success {
    
    MuaExchangeResultTipView *tip = [MuaExchangeResultTipView tipView];
    tip.statusLabel.text = success ? @"兑换成功" : @"兑换失败";
    tip.button1Click = ^{
        [LEEAlert closeWithCompletionBlock:nil];
    };
    tip.button2Click = ^{
        [LEEAlert closeWithCompletionBlock:nil];
        [self.navigationController popToRootViewControllerAnimated:YES];
//        [[NSNotificationCenter defaultCenter] postNotificationName:@"MuaChangeSuccessBackHomeVC" object:nil];
    };
    
    UIView* backView = [[UIView alloc] initWithFrame: CGRectMake(0, 0, 248, 267)];
    [tip setFrame:backView.bounds];
    [backView addSubview:tip];
    
    [LEEAlert alert].config
    .LeeAddCustomView(^(LEECustomView *custom) {
        custom.view = backView;
    })
    .LeeHeaderColor([UIColor clearColor])
    .LeeBackgroundStyleBlur(UIBlurEffectStyleDark)
    .LeeBackgroundStyleTranslucent(0.4)
    .LeeClickBackgroundClose(false)
    .LeeMaxWidth(backView.width)
    .LeeCornerRadius(4.f)
    .LeeShow();
}

@end

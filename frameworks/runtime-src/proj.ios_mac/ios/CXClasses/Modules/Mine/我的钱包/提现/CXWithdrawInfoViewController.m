//
//  CXWithdrawInfoViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXWithdrawInfoViewController.h"
#import "CXMineWalletModel.h"

@interface CXWithdrawInfoViewController ()
@property (weak, nonatomic) IBOutlet UIImageView *bgImage;
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;
@property (weak, nonatomic) IBOutlet UIView *bg1View;
@property (weak, nonatomic) IBOutlet UIView *bg2View;

@property (weak, nonatomic) IBOutlet UITextField *inputTextField;

@property (weak, nonatomic) IBOutlet UITextField *receiveTextField;

@property (weak, nonatomic) IBOutlet UILabel *accountLabel;
@property (weak, nonatomic) IBOutlet UILabel *tipLabel;
@property (weak, nonatomic) IBOutlet UIButton *commitButton;

@property (nonatomic, strong) CXMineWalletModel *currentModel;
@end

@implementation CXWithdrawInfoViewController
- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"提现";
    self.view.backgroundColor = [UIColor whiteColor];
    
    self.bgImage.image = [UIImage x_gradientImageWithSize:CGSizeMake(SCREEN_WIDTH, 160) Color1:UIColorHex(0x923483) color2:UIColorHex(0x2C185D)];
    
    self.bg1View.layer.cornerRadius = 12;
    self.bg2View.layer.cornerRadius = 12;
    
    _commitButton.layer.masksToBounds = YES;
    _commitButton.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [self.commitButton setBackgroundImage:image forState:UIControlStateNormal];
    
    [self getAccountDetail];
    
    [self.inputTextField addTarget:self action:@selector(textFieldChange:) forControlEvents:UIControlEventEditingChanged];
}

- (void)getAccountDetail {
    NSString *url = @"/index.php/Api/AliPay/cashinfo";
    if (_isRedPacket) {
        url = @"/index.php/Api/AliPay/redpacket_cashinfo";
    }
    [CXHTTPRequest POSTWithURL:url parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            self.currentModel = [CXMineWalletModel modelWithJSON:responseObject[@"data"]];
            [self reloadData];
        }
    }];
}

- (void)textFieldChange:(UITextField *)textField {
    float inputValue = [textField.text floatValue];
    if (inputValue > [_currentModel.balance floatValue]) {
        [self toast:@"可提现余额不足"];
        return;
    }
    float value = inputValue - [_currentModel.cash_price floatValue] - (inputValue - [_currentModel.cash_price floatValue]) * [_currentModel.cash_tax floatValue];
    self.receiveTextField.text = [NSString stringWithFormat:@"%.2f", value];
}

- (void)reloadData {
    self.balanceLabel.text = [NSString stringWithFormat:@"%.2f", [_currentModel.balance floatValue]];
    self.accountLabel.text = [NSString stringWithFormat:@"支付宝账号：%@", _currentModel.zfb];
    float tax = [_currentModel.cash_tax floatValue] * 100;
    self.tipLabel.text = [NSString stringWithFormat:@"• 单比最低¥%@可以提现\n• 提款金额需要首先扣除固定¥%@手续费\n• 扣除手续费的余额需要在扣除%.1f%%的税费", !_isRedPacket ? _currentModel.cash.stringValue : _currentModel.redpack_cash.stringValue, !_isRedPacket ? [_currentModel.cash_price stringValue] : [_currentModel.redpack_cash_price stringValue], tax];
}

- (IBAction)commitAction:(id)sender {
    if ([self.inputTextField.text floatValue] < (!_isRedPacket ? _currentModel.cash.floatValue : _currentModel.redpack_cash.floatValue)) {
        [self toast:[NSString stringWithFormat:@"单比最低¥%@可以提现", !_isRedPacket ? _currentModel.cash.stringValue : _currentModel.redpack_cash.stringValue]];
    } else {
        NSDictionary *param = @{
            @"apliuserid" : self.currentModel.zfb,
            @"cash" : self.inputTextField.text,
            @"totalcash" : self.receiveTextField.text,
            @"type" : @"1",
        };
        NSString *url = @"/index.php/Api/AliPay/cash";
        if (_isRedPacket) {
            url=@"/index.php/Api/AliPay/redpacket_cash";
            
        }
        [CXHTTPRequest POSTWithURL:url parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                [self toast:@"提现成功"];
                [self getAccountDetail];
            } else {
               [self toast:responseObject[@"desc"]];
            }
        }];
    }
    
}


@end

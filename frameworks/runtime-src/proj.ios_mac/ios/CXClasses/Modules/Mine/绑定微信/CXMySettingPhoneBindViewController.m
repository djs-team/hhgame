//
//  CXMySettingPhoneBindViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/7.
//

#import "CXMySettingPhoneBindViewController.h"

@interface CXMySettingPhoneBindViewController ()
@property (weak, nonatomic) IBOutlet UITextField *phoneTextField;
@property (weak, nonatomic) IBOutlet UITextField *codeTextField;
@property (weak, nonatomic) IBOutlet UIButton *codeBtn;
@property (weak, nonatomic) IBOutlet UIButton *completeBtn;

//定时器
@property (nonatomic, weak) NSTimer *timer;
//时间
@property (nonatomic, assign) NSInteger timerNumber;

@end

@implementation CXMySettingPhoneBindViewController

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"手机认证";
    
    _codeBtn.layer.masksToBounds = YES;
    _codeBtn.layer.cornerRadius = 3;
    [_codeBtn setBackgroundImage:[UIImage x_gradientImageWithSize:CGSizeMake(86, 30) Color1:UIColorHex(0x3D1D63) color2:UIColorHex(0x852F7D)] forState:UIControlStateNormal];
    _completeBtn.layer.masksToBounds = YES;
    _completeBtn.layer.cornerRadius = 5;
    [_completeBtn setBackgroundImage:[UIImage x_gradientImageWithSize:CGSizeMake(SCREEN_WIDTH - 60, 40) Color1:UIColorHex(0x3D1D63) color2:UIColorHex(0x852F7D)] forState:UIControlStateNormal];
    
    [self.phoneTextField addTarget:self action:@selector(textDidChanged:) forControlEvents:UIControlEventEditingChanged];
    [self.codeTextField addTarget:self action:@selector(textDidChanged:) forControlEvents:UIControlEventEditingChanged];
}

- (void)textDidChanged: (UITextField *)textField {
    if (textField == self.phoneTextField) {
        if (self.phoneTextField.text.length > 11) {
            self.phoneTextField.text = [textField.text substringToIndex:11];
        }
    }
    if (textField == self.codeTextField) {
        if (self.codeTextField.text.length > 6) {
            self.codeTextField.text = [textField.text substringToIndex:6];
        }
    }
}

- (IBAction)codeAction:(id)sender {
    if (self.phoneTextField.text.length == 0) {
        return;
    }
    
    NSDictionary *param = @{
        @"phone":self.phoneTextField.text,
        @"type":@"1",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Sms/sendSMS" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [weakSelf toast:@"验证码发送成功"];
            [weakSelf.codeTextField becomeFirstResponder];
            weakSelf.codeBtn.userInteractionEnabled = NO;
            weakSelf.timerNumber = 60;
            weakSelf.timer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:weakSelf selector:@selector(setButtontitle:) userInfo:nil repeats:YES];
            weakSelf.codeBtn.userInteractionEnabled = NO;
        }
    }];
}

- (void)setButtontitle:(NSTimer *)timer{
    if(self.timerNumber == 0){
        self.timerNumber = 60;
        [self.timer  invalidate];
        self.timer = nil;
        [self.codeBtn setTitle:@"重新获取" forState:0];
        self.codeBtn.userInteractionEnabled = YES;
    } else {
        self.timerNumber --;
        [self.codeBtn setTitle:[NSString stringWithFormat:@"%@S",@(self.timerNumber)] forState:0];
    }
}

- (IBAction)completeAction:(id)sender {
    
    if (self.phoneTextField.text.length == 0 || self.codeTextField.text.length == 0) {
        return;
    }
    
    NSDictionary *param = @{
        @"phone":self.phoneTextField.text,
        @"vertify":self.codeTextField.text,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/member/bind_phone" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [weakSelf toast:@"手机号绑定成功！"];
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                [weakSelf.navigationController popToRootViewControllerAnimated:YES];
            });
        } else {
            [weakSelf toast:responseObject[@"desc"]];
        }
    }];
}


@end

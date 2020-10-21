//
//  CXChangeAgeAlertView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/24.
//

#import "CXChangeAgeAlertView.h"

@interface CXChangeAgeAlertView()
@property (weak, nonatomic) IBOutlet UIButton *nanBtn;
@property (weak, nonatomic) IBOutlet UIButton *nvBtn;
@property (weak, nonatomic) IBOutlet UIButton *sureBtn;
@property (weak, nonatomic) IBOutlet UIButton *backBtn;

@property (nonatomic, assign) NSInteger sex;

@end

@implementation CXChangeAgeAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(200, 240));
    }];
    
    [MMPopupWindow sharedWindow].touchWildToHide = NO;
    self.type = MMPopupTypeCustom;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    self.sureBtn.layer.masksToBounds = YES;
    self.sureBtn.layer.cornerRadius = 15;
    [self.sureBtn setBackgroundImage:[UIImage x_gradientImageWithSize:CGSizeMake(95, 16) Color1:UIColorHex(0xE05AC0) color2:UIColorHex(0x8C42E9)] forState:UIControlStateNormal];
    
    _sex = 1;
    
    self.backBtn.layer.masksToBounds = YES;
    self.backBtn.layer.cornerRadius = 15;
    self.backBtn.layer.borderWidth = 1;
    self.backBtn.layer.borderColor = UIColorHex(0x8C42E9).CGColor;
}
- (IBAction)sexAction:(UIButton *)sender {
    [self.nanBtn setImage:sender.tag == 1 ? [UIImage imageNamed:@"selected_on"] : [UIImage imageNamed:@"selected_off"] forState:UIControlStateNormal];
    [self.nvBtn setImage:sender.tag == 2 ? [UIImage imageNamed:@"selected_on"] : [UIImage imageNamed:@"selected_off"] forState:UIControlStateNormal];
    
    _sex = sender.tag;
}

- (IBAction)sureAction:(id)sender {
    NSDictionary *param = @{
        @"sex" : [NSString stringWithFormat:@"%ld", _sex],
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/setSex" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [CXClientModel instance].sex = @(_sex);
            [weakSelf hide];
        }
    }];
}

- (IBAction)backAction:(id)sender {
    if (self.backActionBlock) {
        self.backActionBlock();
    }
    
    [self hide];
}

@end

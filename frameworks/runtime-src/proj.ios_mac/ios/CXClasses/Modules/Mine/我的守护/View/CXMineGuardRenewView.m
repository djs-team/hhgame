//
//  CXMineGuardRenewView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import "CXMineGuardRenewView.h"
#import "CXMineGuardRenewItemCell.h"

@interface CXMineGuardRenewView() <UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout>

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UICollectionView *mainTableView;
@property (weak, nonatomic) IBOutlet UIButton *automaticBtn;
@property (weak, nonatomic) IBOutlet UILabel *guardUserLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UIButton *rechargeBtn;
@property (weak, nonatomic) IBOutlet UIView *thirdPayView;
@property (weak, nonatomic) IBOutlet UIButton *wechatBtn;
@property (weak, nonatomic) IBOutlet UIButton *zfbBtn;

@property (nonatomic, strong) NSArray <CXRechargeModel*>*itemArrays;
@property (nonatomic, strong) CXRechargeModel *currentModel;

@property (nonatomic, assign) BOOL is_shouhu; // 是否是该用户的守护1:是 2:不是

@property (nonatomic, strong) NSString *action;

@end

@implementation CXMineGuardRenewView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(320, 370));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    _rechargeBtn.layer.masksToBounds = YES;
    _rechargeBtn.layer.cornerRadius = 19;
    [_rechargeBtn setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXMineGuardRenewItemCell" bundle:nil] forCellWithReuseIdentifier:@"CXMineGuardRenewItemCellID"];
}

- (void)setUserId:(NSString *)userId {
    _userId = userId;
    
    [self getGuardData];
    
    if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) { // 苹果支付
        self.thirdPayView.hidden = YES;
    }
}

- (void)getGuardData {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Guard/initGuard" parameters:@{@"target_id":_userId} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.itemArrays = [NSArray modelArrayWithClass:[CXRechargeModel class] json:responseObject[@"data"][@"data"]];
            weakSelf.is_shouhu = [[responseObject[@"data"][@"is_shouhu"] stringValue] isEqualToString:@"1"];
            if ([[responseObject[@"data"][@"is_auto"] stringValue] isEqualToString:@"1"]) {
                weakSelf.automaticBtn.hidden = NO;
            } else {
                weakSelf.automaticBtn.hidden = YES;
            }
            weakSelf.guardUserLabel.text = [NSString stringWithFormat:@"%@(ID:%@)",responseObject[@"data"][@"nickname"], weakSelf.userId];
            // self.timeLabel.text = [NSString stringWithFormat:@"守护到期时间：%@", user.end_time];
            [weakSelf.mainTableView reloadData];
        }
    }];
}

- (void)setIs_shouhu:(BOOL)is_shouhu {
    _is_shouhu = is_shouhu;
    if (_is_shouhu) {
        self.titleLabel.text = @"守护续费";
        self.timeLabel.hidden = NO;
    } else {
        self.titleLabel.text = @"开通守护";
        self.timeLabel.hidden = YES;
    }
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return _itemArrays.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXMineGuardRenewItemCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXMineGuardRenewItemCellID" forIndexPath:indexPath];
    CXRechargeModel *model = _itemArrays[indexPath.row];
    if ([model.charge_id isEqualToString:_currentModel.charge_id]) {
        model.isSelected = YES;
    } else {
        model.isSelected = NO;
    }
    cell.model = model;
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((320-16*3)/3, 116);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    _currentModel = self.itemArrays[indexPath.row];
    [self.mainTableView reloadData];
}

- (IBAction)automaticAction:(UIButton *)sender {
    if (sender.tag == 0) {
        [sender setImage:[UIImage imageNamed:@"selected_off"] forState:UIControlStateNormal];
        sender.tag = 10;
    } else {
        [sender setImage:[UIImage imageNamed:@"selected_on"] forState:UIControlStateNormal];
        sender.tag = 0;
    }
}

- (IBAction)wechatAction:(id)sender {
    _action = @"weixin";
    [_wechatBtn setImage:[UIImage imageNamed:@"selected_on"] forState:UIControlStateNormal];
    [_zfbBtn setImage:[UIImage imageNamed:@"selected_off"] forState:UIControlStateNormal];
}
- (IBAction)zfbAction:(id)sender {
    _action = @"alipay";
    [_wechatBtn setImage:[UIImage imageNamed:@"selected_off"] forState:UIControlStateNormal];
    [_zfbBtn setImage:[UIImage imageNamed:@"selected_on"] forState:UIControlStateNormal];
}

- (IBAction)rechargeAction:(id)sender {
    if (_currentModel.charge_id.length <= 0) {
        [self showAlertWithMessage:@"请选择守护时间"];
        return;
    }
    
    if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) { // 苹果支付
        [CXIPAPurchaseManager manager].purchaseType = LiveBroadcast;
        [[CXIPAPurchaseManager manager] inAppPurchaseWithProductID:_currentModel.iosflag iapResult:^(BOOL isSuccess, NSDictionary *param, NSString *errorMsg) {
            NSLog(@"");
        }];
    } else {
        if (_action.length <= 0) {
            [self showAlertWithMessage:@"请选择支付方式"];
            return;
        }

        NSDictionary *param = @{
            @"rmb":_currentModel.coin,
            @"action":_action,
            @"uid":[CXClientModel instance].userId,
            @"type":@"2",
            @"is_active": _is_shouhu == YES ? @"1" : @"2",
            @"chargeid":_currentModel.charge_id,
            @"target_id":_userId,
            @"guard_id":_currentModel.guard_id,
            @"long_day":_currentModel.long_time,
        };
        kWeakSelf
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Order/pay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                if ([_action isEqualToString:@"weixin"]) {
                    NSString *str = [responseObject[@"data"] jsonStringEncoded];
                    [[CXThirdPayManager sharedApi] wxPayWithPayParam:str success:nil failure:nil];
                } else {
                    [[CXThirdPayManager sharedApi] aliPayWithPayParam:responseObject[@"data"] success:nil failure:nil];
                }
                
                [weakSelf hide];
            }
        }];
    }
    
}

@end

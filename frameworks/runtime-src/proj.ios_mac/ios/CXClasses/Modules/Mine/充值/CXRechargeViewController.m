//
//  CXRechargeViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import "CXRechargeViewController.h"
#import "CXRechargeModel.h"
#import "CXRechargeItemCell.h"
#import "CXBaseWebViewController.h"
#import "CXConsumeRecordViewController.h"

#import "WXApi.h"


@interface CXRechargeViewController () <UICollectionViewDataSource, UICollectionViewDelegate>

@property (nonatomic, assign) NSInteger payMethod; // 1: 微信 2: 支付宝 3: 苹果

@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;
@property (weak, nonatomic) IBOutlet UIView *thirdPayView;
@property (weak, nonatomic) IBOutlet UIButton *wechatPayBtn;
@property (weak, nonatomic) IBOutlet UIButton *zfbPayBtn;
@property (weak, nonatomic) IBOutlet UIButton *rechargeBtn;

@property (nonatomic, assign) BOOL isFirstCharge;
@property (nonatomic, strong) NSMutableArray <CXRechargeModel *>*rechargeItems;
@property (nonatomic, strong) CXRechargeModel *selectedItem;

@end

@implementation CXRechargeViewController

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"余额";
    
    [self loadBalances];
    [self loadRechargeItems];
    [self setupSubViews];
    
    // 监听支付宝支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(aliPayCallBack:) name:kNSNotificationCenter_CXRechargeViewController_alipay object:nil];

    // 监听微信支付回调
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(weChatCallBack:) name:kNSNotificationCenter_CXRechargeViewController_weixin object:nil];
}

- (void)loadBalances {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/initmymoney" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.balanceLabel.text = [NSString stringWithFormat:@"%@", responseObject[@"data"][@"coin"]];
        }
    }];
}

- (void)loadRechargeItems {
    kWeakSelf
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/ApplePay/chargelist" parameters:@{@"signature":signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXRechargeModel class] json:responseObject[@"data"][@"charge_list"]];
            weakSelf.rechargeItems = [NSMutableArray arrayWithArray:array];
            
            if ([responseObject[@"data"][@"is_first"] integerValue] == 1) {
               weakSelf.isFirstCharge = YES;
            } else {
               weakSelf.isFirstCharge = NO;
            }
            
            [weakSelf.mainCollectionView reloadData];
        }
    }];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return _rechargeItems.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXRechargeItemCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXRechargeItemCellID" forIndexPath:indexPath];
    CXRechargeModel *item = self.rechargeItems[indexPath.row];
    if (_selectedItem && [item.charge_id isEqualToString:_selectedItem.charge_id]) {
        item.isSelected = YES;
    } else {
        item.isSelected = NO;
    }
    item.isShowFirstTag = NO;
    if (self.isFirstCharge == YES) {
        if (indexPath.row == 0) {
            item.isShowFirstTag = YES;
        }
    }
    cell.model = item;
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    CXRechargeModel *item = self.rechargeItems[indexPath.row];
    _selectedItem = item;
    [self.mainCollectionView reloadData];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat width = (kScreenWidth - 12 * 4)/3;
    return CGSizeMake(width, 64);
}


#pragma mark - Action
- (IBAction)wechatPayAction:(id)sender {
    self.payMethod = 1;
    self.wechatPayBtn.layer.borderColor = UIColorHex(0x7F3EF0).CGColor;
    self.zfbPayBtn.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
}
- (IBAction)zfbPayAction:(id)sender {
    self.payMethod = 2;
    self.zfbPayBtn.layer.borderColor = UIColorHex(0x7F3EF0).CGColor;
    self.wechatPayBtn.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
}

- (IBAction)rechargeAction:(id)sender {
    if (self.payMethod <= 0) {
        [self toast:@"请选择支付方式"];
        return;
    }
    
    if (self.selectedItem.charge_id.length <= 0) {
        [self toast:@"请选择支付金额"];
        return;
    }
    
    if (_payMethod == 1) { // 微信
        NSDictionary *param = @{
            @"rmb":_selectedItem.rmb,
            @"action" : @"weixin",
            @"uid" : [CXClientModel instance].userId,
            @"type" : @"1",
            @"is_active" : @"0",
            @"chargeid": _selectedItem.charge_id,
        };
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Order/pay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                NSString *str = [responseObject jsonStringEncoded];
                [[CXThirdPayManager sharedApi] wxPayWithPayParam:str success:nil failure:nil];
            }
        }];
    } else if (_payMethod == 2) { // 支付宝
        NSDictionary *param = @{
            @"rmb":_selectedItem.rmb,
            @"action" : @"alipay",
            @"uid" : [CXClientModel instance].userId,
            @"type" : @"1",
            @"is_active" : @"0",
            @"chargeid": _selectedItem.charge_id,
        };
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Order/pay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                [[CXThirdPayManager sharedApi] aliPayWithPayParam:responseObject[@"data"] success:nil failure:nil];
            }
        }];
    } else { // 内购
        kWeakSelf
        [CXIPAPurchaseManager manager].userid = [CXClientModel instance].userId;
        [CXIPAPurchaseManager manager].purchaseType = LiveBroadcast;
        [[CXIPAPurchaseManager manager] inAppPurchaseWithProductID:_selectedItem.iosflag iapResult:^(BOOL isSuccess, NSString *certificate, NSString *errorMsg) {
            if (isSuccess) {
                [weakSelf toast:@"购买成功"];
                [weakSelf loadBalances];
            } else {
                [weakSelf toast:errorMsg];
            }
        }];
    }
}

- (IBAction)payProtocolAction:(id)sender {
    NSURL *url = [NSURL URLWithString:@"https://lin01.hehe555.com:85/Public/Download/charge.html"];
    CXBaseWebViewController *webVC = [[CXBaseWebViewController alloc] initWithURL:url];
    webVC.title = @"合合有约充值协议";
    [self.navigationController pushViewController:webVC animated:YES];
}

- (void)rightBtnAction {
    CXConsumeRecordViewController *vc = [CXConsumeRecordViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)aliPayCallBack:(NSNotification *)info {

    if ([[info.object objectForKey:@"resultStatus"] integerValue] == 9000) {
        [self toast:@"支付成功"];
        [self loadBalances];
    } else {
        [self toast:@"支付失败"];
    }
}

- (void)weChatCallBack:(NSNotification *)info {
    NSLog(@"%@", info);

    // WXSuccess           = 0,    /**< 成功    */
    // WXErrCodeCommon     = -1,   /**< 普通错误类型    */
    // WXErrCodeUserCancel = -2,   /**< 用户点击取消并返回    */
    // WXErrCodeSentFail   = -3,   /**< 发送失败    */
    // WXErrCodeAuthDeny   = -4,   /**< 授权失败    */
    // WXErrCodeUnsupport  = -5,   /**< 微信不支持    */
    NSDictionary *obj = info.object;
    switch ([[obj objectForKey:@"errCode"] integerValue]) {
        case WXSuccess:
            [self toast:@"支付成功"];
            [self loadBalances];
            break;
        case WXErrCodeUserCancel:
            [self toast:@"取消支付"];
            break;
        default:
            [self toast:@"支付失败"];
            break;
    }
}

- (void)setupSubViews {
    self.wechatPayBtn.layer.masksToBounds = YES;
    self.wechatPayBtn.layer.cornerRadius = 5;
    self.wechatPayBtn.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
    self.wechatPayBtn.layer.borderWidth = 0.5;
    self.zfbPayBtn.layer.masksToBounds = YES;
    self.zfbPayBtn.layer.cornerRadius = 5;
    self.zfbPayBtn.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
    self.zfbPayBtn.layer.borderWidth = 0.5;
    
    self.rechargeBtn.layer.masksToBounds = YES;
    self.rechargeBtn.layer.cornerRadius = 19;
    [self.rechargeBtn setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(266, 39) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    
    UIButton *rightBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 80, 44)];
    [rightBtn setTitle:@"消费记录" forState:UIControlStateNormal];
    [rightBtn setTitleColor:UIColorHex(0x818181) forState:UIControlStateNormal];
    rightBtn.titleLabel.font = [UIFont boldSystemFontOfSize:14.0f];
    [rightBtn addTarget:self action:@selector(rightBtnAction) forControlEvents:UIControlEventTouchUpInside];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    
    
    self.mainCollectionView.dataSource = self;
    self.mainCollectionView.delegate = self;
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXRechargeItemCell" bundle:nil] forCellWithReuseIdentifier:@"CXRechargeItemCellID"];
}


@end

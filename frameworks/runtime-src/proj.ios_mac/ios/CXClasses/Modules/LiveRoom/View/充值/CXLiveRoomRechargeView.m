//
//  CXLiveRoomRechargeView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomRechargeView.h"
#import "CXRechargeItemCell.h"

@interface CXLiveRoomRechargeView() <UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout>
@property (weak, nonatomic) IBOutlet UIView *first_rechargeView;
@property (weak, nonatomic) IBOutlet UILabel *first_rechargeTagLabel;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *rechargeItem_topLayout;
@property (weak, nonatomic) IBOutlet UIButton *sureRechargeBtn;
@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;
@property (weak, nonatomic) IBOutlet UILabel *balanceLabel;

@property (nonatomic, assign) BOOL isFirstCharge;

@property (nonatomic, strong) CXRechargeModel *selectedItem;

//展示列表
@property (nonatomic,nullable) NSArray<CXRechargeModel*> *products;


@property (nonatomic, assign) NSInteger payMethod; // 1: 微信 2: 支付宝 其他: 苹果
@property (weak, nonatomic) IBOutlet UIView *thirdPayView;
@property (weak, nonatomic) IBOutlet UIButton *wechatPayBtn;
@property (weak, nonatomic) IBOutlet UIButton *zfbPayBtn;

@end

@implementation CXLiveRoomRechargeView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.wechatPayBtn.layer.masksToBounds = YES;
    self.wechatPayBtn.layer.cornerRadius = 5;
    self.wechatPayBtn.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
    self.wechatPayBtn.layer.borderWidth = 1;
    self.zfbPayBtn.layer.masksToBounds = YES;
    self.zfbPayBtn.layer.cornerRadius = 5;
    self.zfbPayBtn.layer.borderColor = UIColorHex(0xBBBBBB).CGColor;
    self.zfbPayBtn.layer.borderWidth = 1;
    
    _sureRechargeBtn.layer.masksToBounds = YES;
    _sureRechargeBtn.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [self.sureRechargeBtn setBackgroundImage:image forState:UIControlStateNormal];
    
    _first_rechargeTagLabel.layer.masksToBounds = YES;
    _first_rechargeTagLabel.layer.cornerRadius = 5;
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(kScreenWidth, 512));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeSheet;
    
    _mainCollectionView.dataSource = self;
    _mainCollectionView.delegate = self;
    [_mainCollectionView registerNib:[UINib nibWithNibName:@"CXRechargeItemCell" bundle:nil] forCellWithReuseIdentifier:@"CXRechargeItemCellID"];
    
    _payMethod = 0;
        
    [self sendLoadProductsRequest];
    [self loadMoney];
    
    if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) { // 苹果支付
        self.thirdPayView.hidden = YES;
    }
}

- (void)loadMoney {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/initmymoney" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           weakSelf.balanceLabel.text = [NSString stringWithFormat:@"%@",responseObject[@"data"][@"coin"]];
           [CXClientModel instance].balance = responseObject[@"data"][@"coin"];
       }
    }];
}

- (void)sendLoadProductsRequest {
    kWeakSelf
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSString *url = @"/index.php/Api/Order/chargelist";
    if ([[CXClientModel instance].applePayType isEqualToString:@"Apple"]) {
        url = @"/index.php/Api/ApplePay/chargelist";
    }
    [CXHTTPRequest POSTWithURL:url parameters:@{@"signature":signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXRechargeModel class] json:responseObject[@"data"][@"charge_list"]];
            weakSelf.products = [NSArray arrayWithArray:array];
            
            if ([responseObject[@"data"][@"is_first"] integerValue] == 1) {
                weakSelf.first_rechargeView.hidden = NO;
                weakSelf.rechargeItem_topLayout.constant = 68;
                weakSelf.isFirstCharge = YES;
            } else {
                weakSelf.first_rechargeView.hidden = YES;
                weakSelf.isFirstCharge = NO;
                weakSelf.rechargeItem_topLayout.constant = 20;
            }

            dispatch_async(dispatch_get_main_queue(), ^{
               [weakSelf.mainCollectionView reloadData];
            });
        }
    }];
}

#pragma mark - UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.products.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXRechargeItemCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXRechargeItemCellID" forIndexPath:indexPath];
    CXRechargeModel *item = self.products[indexPath.row];
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

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat width = (kScreenWidth - 12 * 4)/3;
    return CGSizeMake(width, 64);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    CXRechargeModel *product = self.products[indexPath.row];
    _selectedItem = product;
    [self.mainCollectionView reloadData];
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

- (IBAction)sureRechargeAction:(id)sender {
    if (self.selectedItem) {
        if (self.rechargeBlock) {
            self.rechargeBlock(_selectedItem, _payMethod);
        }
    }
    
    [self hide];
}

- (IBAction)rechargeAgreementAction:(id)sender {
    if (self.gotoRechargeProtocol) {
        self.gotoRechargeProtocol(@"https://lin01.hehe555.com:85/Public/Download/charge.html");
    }
    
    [self hide];
}


@end

//
//  CXExchangeBlueRoseViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/4/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXExchangeBlueRoseViewController.h"
#import "CXExchangeBlueRoseCell.h"
#import "CXExchangeBlueRoseSelectedView.h"
#import "CXExchangeBlueRoseRecordViewController.h"
#import "TPKeyboardAvoidingScrollView.h"

@interface CXExchangeBlueRoseViewController () <UICollectionViewDataSource, UICollectionViewDelegateFlowLayout, UICollectionViewDelegate>
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;

@property (nonatomic, strong) NSArray *dataSources;

@property (nonatomic, strong)TPKeyboardAvoidingScrollView *exchange_backgroundSCorllView;

@property (nonatomic, strong) CXFriendGiftModel *currentGiftModel;

@end

@implementation CXExchangeBlueRoseViewController

- (TPKeyboardAvoidingScrollView*)exchange_backgroundSCorllView{
    if (!_exchange_backgroundSCorllView) {
        _exchange_backgroundSCorllView = [[TPKeyboardAvoidingScrollView alloc]initWithFrame:CGRectMake(0, 0, kScreenWidth, kScreenHeight)];
        _exchange_backgroundSCorllView.backgroundColor = rgba(1, 1, 1, 0.5);
        
        kWeakSelf
        __weak CXExchangeBlueRoseSelectedView *exchangeView = [[NSBundle mainBundle] loadNibNamed:@"CXExchangeBlueRoseSelectedView" owner:self options:nil].lastObject;
        exchangeView.giftModel = _currentGiftModel;
        exchangeView.exchangeBlueRoseBlock = ^(NSString * _Nonnull count, UIButton * _Nonnull sender) {
            if (sender.tag == 20) {
                [weakSelf.exchange_backgroundSCorllView removeFromSuperview];
                weakSelf.exchange_backgroundSCorllView = nil;
            } else {
                [weakSelf.exchange_backgroundSCorllView removeFromSuperview];
                weakSelf.exchange_backgroundSCorllView = nil;
                [weakSelf exchangeBlueNum:count gift_id:weakSelf.currentGiftModel.gift_id];
            }
        };
        [_exchange_backgroundSCorllView addSubview:exchangeView];
        [exchangeView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.center.mas_offset(0);
            make.size.mas_equalTo(CGSizeMake(302, 375));
        }];
    }
    return _exchange_backgroundSCorllView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    _titleLabel.layer.masksToBounds = YES;
    _titleLabel.layer.cornerRadius = 14;
    
    self.title = @"兑换蓝玫瑰";
    
    UIButton *rightBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 80, 44)];
    [rightBtn addTarget:self action:@selector(rightClick) forControlEvents:UIControlEventTouchUpInside];
    rightBtn.contentHorizontalAlignment = UIControlContentHorizontalAlignmentRight;
    [rightBtn setTitle:@"兑换记录" forState:UIControlStateNormal];
    [rightBtn setTitleColor:UIColorHex(0x333333) forState:UIControlStateNormal];
    rightBtn.titleLabel.font = [UIFont systemFontOfSize:14.0f];
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc]initWithCustomView:rightBtn];
    
    
    self.mainCollectionView.dataSource = self;
    self.mainCollectionView.delegate = self;
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXExchangeBlueRoseCell" bundle:nil] forCellWithReuseIdentifier:@"CXExchangeBlueRoseCellID"];
    
    [self initBlueData];
}

- (void)rightClick{
    CXExchangeBlueRoseRecordViewController *vc = [CXExchangeBlueRoseRecordViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - Https
- (void)initBlueData {
    kWeakSelf
//    AliPayInitBlueExchangeRequest *request = [AliPayInitBlueExchangeRequest new];
//    [self sendRequest:request withCallback:^(__kindof AliPayInitBlueExchangeRequest * _Nonnull request) {
//        if (!request.error && request.phpResponse.isSuccess) {
//            weakSelf.dataSources = [NSMutableArray arrayWithArray:request.phpResponse.data];
//            [weakSelf.mainCollectionView reloadData];
//        }
//        else {
//            [weakSelf toastError:request.error];
//        }
//    }];
}

- (void)exchangeBlueNum:(NSString *)num gift_id:(NSString *)gift_id {
    kWeakSelf
//    AliPayExchangeBlueRequest *request = [AliPayExchangeBlueRequest new];
//    request.num = [num numberValue];
//    request.gift_id = [gift_id numberValue];
//    [self sendRequest:request withCallback:^(__kindof PHPRequest * _Nonnull request) {
//        if (!request.error && request.phpResponse.isSuccess) {
//            [weakSelf initBlueData];
//            [weakSelf toast:@"兑换成功"];
//        }
//        else {
//            [weakSelf toast:request.phpResponse.desc];
//        }
//    }];
}

#pragma mark - UICollectionViewDataSource, UICollectionViewDelegateFlowLayout
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.dataSources.count;
}
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXExchangeBlueRoseCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXExchangeBlueRoseCellID" forIndexPath:indexPath];
    
    CXFriendGiftModel *model = _dataSources[indexPath.row];
    [cell.logo sd_setImageWithURL:[NSURL URLWithString:model.gift_image]];
    NSString *desc = [NSString stringWithFormat:@"蓝玫瑰：%@\n礼物数量：%@", model.gift_blue_coin, model.pack_num];
    cell.descLabel.text = desc;
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    CGFloat width = (kScreenWidth - 17*4)/3;
    return CGSizeMake(width, width);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    CXFriendGiftModel *model = _dataSources[indexPath.row];
    _currentGiftModel = model;
//    [self.view addSubview:self.exchange_backgroundSCorllView];
    [[UIApplication sharedApplication].keyWindow addSubview:self.exchange_backgroundSCorllView];
}

@end

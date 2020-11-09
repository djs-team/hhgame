//
//  CXAddFriendViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/10/17.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXAddFriendViewController.h"
#import "CXAddFriendGiftListCell.h"
#import <MBProgressHUD.h>

@interface CXAddFriendViewController () <UITextFieldDelegate, UICollectionViewDelegate, UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>

@property (nonatomic, strong) NSMutableArray *giftListArray;
@property (nonatomic, strong) NSMutableArray *backpageListArray;
@property (nonatomic, strong) NSMutableArray *dataSources;
@property (weak, nonatomic) IBOutlet UIImageView *user_avatarImage;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;

@property (nonatomic, weak) IBOutlet UICollectionView *giftCollectionView;
@property (weak, nonatomic) IBOutlet UIPageControl *currentPageControl;

@property (nonatomic, strong) CXFriendGiftModel *currentGiftModel;

@property (strong, nonatomic) IBOutlet UIView *sendView;
@property (weak, nonatomic) IBOutlet UIView *sendContentView;
@property (weak, nonatomic) IBOutlet UILabel *giftTitleLabel;
@property (weak, nonatomic) IBOutlet UITextField *giftTextField;
@property (weak, nonatomic) IBOutlet UIImageView *giftLogoImageView;
@property (weak, nonatomic) IBOutlet UIButton *giftSendBtn;

@property (weak, nonatomic) IBOutlet UIButton *loveBtn;
@property (weak, nonatomic) IBOutlet UIButton *backpageBtn;;

@property (weak, nonatomic) IBOutlet UIButton *bottomBtn;
@property (weak, nonatomic) IBOutlet UIButton *buyMoreBtn;

@property (weak, nonatomic) IBOutlet UILabel *meiguiCoinLabel;

@property (nonatomic, assign) BOOL isBackpageGift;

@property (strong, nonatomic) IBOutlet UIView *no_sendView;
@property (weak, nonatomic) IBOutlet UIView *no_sendContentView;
@property (weak, nonatomic) IBOutlet UITextField *no_giftTextField;
@property (weak, nonatomic) IBOutlet UIButton *no_giftSendBtn;


@end

@implementation CXAddFriendViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    
    [self setupSubViews];
    
    [self getGiftListData];
    
    [self getMeguiCoin];
    
}

#pragma mark - Http
- (void)getGiftListData {
    if (self.giftListArray.count > 0) {
        self.dataSources = [NSMutableArray arrayWithArray:self.giftListArray];
        [self.giftCollectionView reloadData];
        return;
    }
    
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature" : signature,
        @"is_room" : _is_room ?: @"2"
    };
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Friend/friendGiftList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {

        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXFriendGiftModel class] json:responseObject[@"data"][@"list"]];
            weakSelf.giftListArray = [NSMutableArray arrayWithArray:array];
            weakSelf.dataSources = [NSMutableArray arrayWithArray:weakSelf.giftListArray];
            [weakSelf.giftCollectionView reloadData];
        }
    }];
}

- (void)requestBackpackData {
//    if (self.backpageListArray.count > 0) {
//        self.dataSources = [NSMutableArray arrayWithArray:self.backpageListArray];
//        [self.giftCollectionView reloadData];
//        return;
//    }
    
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature" : signature,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/Mepack_list" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {

        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXFriendGiftModel class] json:responseObject[@"data"][@"list"]];
            weakSelf.backpageListArray = [NSMutableArray arrayWithArray:array];
            weakSelf.dataSources = [NSMutableArray arrayWithArray:weakSelf.giftListArray];
            [weakSelf.giftCollectionView reloadData];
        }
    }];
}

- (void)getMeguiCoin {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/initmymoney" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           weakSelf.meiguiCoinLabel.text = [NSString stringWithFormat:@"X%@朵",responseObject[@"data"][@"coin"]];
           [CXClientModel instance].balance = responseObject[@"data"][@"coin"];
       }
    }];
}

- (void)sendFriendInviteWithParam:(BOOL)isGift {
    [self.sendView removeFromSuperview];
    NSDictionary *param1 = @{
        @"touid" : self.user_id,
        @"friendmsg": self.giftTextField.text,
        @"gift_id" : self.currentGiftModel.gift_id,
        @"type" : _isBackpageGift == YES ? @"2" : @"1",
    };
    
    NSDictionary *param2 = @{
        @"type" : @"",
        @"friendmsg" : self.no_giftTextField.text,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Friend/addFriendly" parameters:isGift == YES ? param1: param2 callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            if (isGift) {
                [weakSelf getMeguiCoin];
            }

            [weakSelf toast:responseObject[@"desc"]];
        } else {
            [weakSelf toast:@"添加失败，请重试"];
        }
    }];
}

#pragma mark - UICollectionViewDelegate, UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXAddFriendGiftListCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXAddFriendGiftListCellID" forIndexPath:indexPath];
    cell.giftModel = self.dataSources[indexPath.row];
    
    return cell;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    CXFriendGiftModel *model = self.dataSources[indexPath.row];
    self.currentGiftModel = model;
    self.sendView.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    [[UIApplication sharedApplication].keyWindow addSubview:self.sendView];
    self.giftTextField.text = @"";

    [self.giftLogoImageView sd_setImageWithURL:[NSURL URLWithString:model.gift_image]];
    self.giftTitleLabel.text = [NSString stringWithFormat:@"送给%@%@",self.nickname, model.gift_name];
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((SCREEN_WIDTH - 24)/4, (SCREEN_WIDTH - 24)/4 + 25);
}

//- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
//    self.currentPageControl.currentPage = ceil(scrollView.contentOffset.x/SCREEN_WIDTH);
//}

#pragma mark - Action
- (IBAction)sendViewCloseAction:(id)sender {
    
    [self.sendView removeFromSuperview];
    [self.no_sendView removeFromSuperview];
}

- (IBAction)sendGiftAction:(id)sender {
    
    [self.sendView removeFromSuperview];
    [self sendFriendInviteWithParam:YES];
}

- (IBAction)showNoGiftView:(id)sender {
    [[UIApplication sharedApplication].keyWindow addSubview:self.no_sendView];
    self.no_giftTextField.text = @"";
}

- (IBAction)noGiftAction:(id)sender {
    [self.no_sendView removeFromSuperview];
    [self sendFriendInviteWithParam:NO];
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    [self.giftTextField resignFirstResponder];
    return YES;
}

- (IBAction)loveAction:(id)sender {
    [self.dataSources removeAllObjects];
    self.backpageBtn.backgroundColor = [UIColor clearColor];
    [self.backpageBtn setTitleColor:UIColorHex(0xFFFFFF) forState:UIControlStateNormal];
    
    self.loveBtn.backgroundColor = [UIColor whiteColor];
    [self.loveBtn setTitleColor:UIColorHex(0x773BE7) forState:UIControlStateNormal];
    self.isBackpageGift = NO;
    [self getGiftListData];
}

- (IBAction)myPackAction:(UIButton *)sender {
    [self.dataSources removeAllObjects];
    self.isBackpageGift = YES;
    self.loveBtn.backgroundColor = [UIColor clearColor];
    [self.loveBtn setTitleColor:UIColorHex(0xFFFFFF) forState:UIControlStateNormal];
    
    self.backpageBtn.backgroundColor = [UIColor whiteColor];
    [self.backpageBtn setTitleColor:UIColorHex(0x773BE7) forState:UIControlStateNormal];
    [self requestBackpackData];
}

- (void)setSelectedButton:(UIButton *)sender {
    sender.backgroundColor = [UIColor whiteColor];
    [sender setTitleColor:UIColorHex(0xFF8686) forState:UIControlStateNormal];
}

- (void)setUnSelectedButton:(UIButton *)sender {
    sender.backgroundColor = [UIColor clearColor];
    [sender setTitleColor:UIColorHex(0x333333) forState:UIControlStateNormal];
}


- (IBAction)buyMoreActoin:(id)sender {
    CXRechargeViewController *vc = [CXRechargeViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

#pragma mark - Private
- (void)setupSubViews {
    self.title = @"送礼物";
    [self.user_avatarImage sd_setImageWithURL:[NSURL URLWithString:self.user_avatar] placeholderImage:[UIImage imageNamed:@"avatar_default"]];
    self.user_avatarImage.layer.cornerRadius = 22;
    self.titleLabel.text = [NSString stringWithFormat:@"给%@送个礼，留个言\n都不好意思回绝你的热情～",self.nickname];
    [self.giftCollectionView registerNib:[UINib nibWithNibName:@"CXAddFriendGiftListCell" bundle:nil] forCellWithReuseIdentifier:@"CXAddFriendGiftListCellID"];
//    self.giftCollectionView.pagingEnabled = YES;
//    self.giftCollectionView.scrollEnabled = YES;
    
    self.currentPageControl.hidden = YES;
    
    _loveBtn.layer.masksToBounds = YES;
    _backpageBtn.layer.masksToBounds = YES;
    _loveBtn.layer.cornerRadius = 4;
    _backpageBtn.layer.cornerRadius = 4;
//    _luxuryBtn.layer.cornerRadius = 10;
    
    _bottomBtn.layer.cornerRadius = 4;
    _buyMoreBtn.layer.cornerRadius = 4;
    _giftSendBtn.layer.cornerRadius = 22;
    _sendContentView.layer.cornerRadius = 3;
    
    _no_giftSendBtn.layer.cornerRadius = 22;
    _no_sendContentView.layer.cornerRadius = 3;
    
    if (self.isHost) {
        self.bottomBtn.hidden = false;
    } else {
        self.bottomBtn.hidden = YES;
    }
}



@end

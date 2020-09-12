//
//  CXMineViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/7.
//

#import "CXMineViewController.h"
#import "CXMineCell.h"
#import "CXRechargeViewController.h"
#import "CXMyWalletViewController.h"
#import "CXMineGuardViewController.h"
#import "CXMineEditProfileViewController.h"
#import "CXContactUsViewController.h"
#import "CXMySettingWechatBindViewController.h"
#import "CXSelectedPhotoView.h"
#import <AliyunOSSiOS/AliyunOSSiOS.h>
#import "CXMineBlockViewController.h"

@interface CXMineViewController () <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (nonatomic, copy) NSString *avatarImageUrl;
@property (nonatomic, strong) UIImage *avatarImage;
@property (nonatomic, strong) CXUploadImageTokenModel *uploadImageModel;

@property (weak, nonatomic) IBOutlet UILabel *nickNameLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;
@property (weak, nonatomic) IBOutlet UILabel *userIdLabel;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mainTableViewHeightLayout;

@property (nonatomic, strong) NSMutableArray *itemArrays;

@property (nonatomic, strong) CXUserModel *currentUser;
@property (nonatomic, assign) BOOL isWechatBind;
@property (nonatomic, strong) NSNumber *bind_type;

@end

@implementation CXMineViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setupSubViews];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBarHidden = YES;
    
    [self getUserData];
    [self loadBalances];
//    [self getWechatBindStatus];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBarHidden = NO;
}

- (void)getUserData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"user_id": [CXClientModel instance].userId,
        @"device": @"iOS",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/user_info" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"user_info"]];
            weakSelf.currentUser = user;
            [weakSelf.avatar sd_setImageWithURL:[NSURL URLWithString:user.avatar]];
            weakSelf.nickNameLabel.text = user.nickname;
            [weakSelf.sexBtn setTitle:user.age forState:UIControlStateNormal];
            if (user.sex.intValue == 1) {
                [weakSelf.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
            } else {
                [weakSelf.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
            }
            [weakSelf.locationBtn setTitle:[NSString stringWithFormat:@"%@%@",user.city.length > 0 ? user.city : @"",user.city_two.length > 0 ? user.city_two : @""] forState:UIControlStateNormal];
            
            weakSelf.userIdLabel.text = [NSString stringWithFormat:@"ID:%@", user.user_id];
            
            weakSelf.bind_type = responseObject[@"data"][@"bind_type"];
            
            if (weakSelf.bind_type.intValue != 1 || weakSelf.bind_type.intValue != 2) {
                NSDictionary *item = weakSelf.itemArrays[4];
                NSString *title = item[@"title"];
                if ([title isEqualToString:@"绑定"]) {
                    [weakSelf.itemArrays removeObjectAtIndex:4];
                    weakSelf.mainTableViewHeightLayout.constant = 45*weakSelf.itemArrays.count;
                }
            }
            
            [weakSelf.mainTableView reloadData];
            
        }
    }];
}

- (void)getWechatBindStatus {
//    kWeakSelf
//    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/is_band_wx" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
//        if (!error) {
//            if ([responseObject[@"data"][@"is_band"] integerValue] == 1) {
//                NSDictionary *item = @{
//                    @"logo": @"mine_bangdingshouji",
//                    @"title": @"绑定",
//                    @"desc": @"已绑定",
//                };
//                weakSelf.isWechatBind = YES;
//                [weakSelf.itemArrays replaceObjectAtIndex:4 withObject:item];
//            } else {
//                NSDictionary *item = @{
//                    @"logo": @"mine_bangdingshouji",
//                    @"title": @"绑定微信",
//                    @"desc": @"未绑定",
//                };
//                weakSelf.isWechatBind = NO;
//                [weakSelf.itemArrays replaceObjectAtIndex:4 withObject:item];
//            }
//            [weakSelf.mainTableView reloadRow:4 inSection:0 withRowAnimation:UITableViewRowAnimationAutomatic];
//        }
//    }];
    
}

- (void)loadBalances {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/initmymoney" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSDictionary *item = @{
                @"logo": @"mine_yue",
                @"title": @"余额",
                @"desc": [NSString stringWithFormat:@"%@玫瑰", responseObject[@"data"][@"coin"]],
            };
            
            [weakSelf.itemArrays replaceObjectAtIndex:0 withObject:item];
            
            [weakSelf.mainTableView reloadRow:0 inSection:0 withRowAnimation:UITableViewRowAnimationAutomatic];
        }
    }];
}

#pragma mark - UITableViewDataSource/Delegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.itemArrays.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXMineCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXMineCellID"];
    NSDictionary *item = self.itemArrays[indexPath.row];
    cell.logo.image = [UIImage imageNamed:item[@"logo"]];
    cell.itemTitleLabel.text = item[@"title"];
    cell.itemDescLabel.text = item[@"desc"];
    if (indexPath.row >= self.itemArrays.count - 1) {
        cell.line.hidden = YES;
    } else {
        cell.line.hidden = NO;
    }
    
    NSString *title = item[@"title"];
    if ([title isEqualToString:@"余额"]) {
        cell.itemDescLabel.textColor = UIColorHex(0xE85ABC);
    } else if ([title isEqualToString:@"实名认证"]) {
        if (_currentUser.attestation == YES) {
            cell.itemDescLabel.text = @"已认证";
            cell.itemDescLabel.textColor = UIColorHex(0xF3BB49);
        } else {
            cell.itemDescLabel.text = @"认证得玫瑰";
            cell.itemDescLabel.textColor = UIColorHex(0x818181);
        }
    } else if ([title isEqualToString:@"绑定"]) {
        if (self.bind_type.integerValue == 1) {
            cell.itemTitleLabel.text = @"绑定微信";
//            cell.itemDescLabel.textColor = UIColorHex(0xF3BB49);
        } else {
            cell.itemTitleLabel.text = @"绑定手机号";
//            cell.itemDescLabel.textColor = UIColorHex(0x818181);
        }
    }

    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *item = self.itemArrays[indexPath.row];
    NSString *title = item[@"title"];
    if ([title isEqualToString:@"余额"]) {
        CXRechargeViewController *vc = [CXRechargeViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([title isEqualToString:@"我的钱包"]) {
        CXMyWalletViewController *vc = [CXMyWalletViewController new];
        vc.is_matchmaker = _currentUser.is_matchmaker;
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([title isEqualToString:@"我的守护"]) {
        CXMineGuardViewController *vc = [CXMineGuardViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([title isEqualToString:@"编辑资料"]) {
        CXMineEditProfileViewController *vc = [CXMineEditProfileViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([title isEqualToString:@"联系我们"]) {
        CXContactUsViewController *vc = [[CXContactUsViewController alloc] init];
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([title isEqualToString:@"绑定"]) {
        CXMySettingWechatBindViewController *vc = [[CXMySettingWechatBindViewController alloc] init];
        vc.bind_type = self.bind_type;
        [self.navigationController pushViewController:vc animated:YES];
    } else if ([title isEqualToString:@"黑名单"]) {
        CXMineBlockViewController *vc = [CXMineBlockViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    }
}

#pragma mark - Action
// 查看个人资料
- (IBAction)mineProfileAction:(id)sender {
    [AppController showUserProfile:[CXClientModel instance].userId];
}

- (IBAction)userAvatarAction:(id)sender {
    CXSelectedPhotoView *photoView = [CXSelectedPhotoView shareInstance];
    MJWeakSelf
    photoView.selectedPhotoBlock = ^(UIImage * _Nonnull photo) {
        weakSelf.avatarImage = photo;
        [weakSelf getUploadToken];
    };
    [[CXTools currentViewController] lew_presentPopupView:photoView animation:nil];
}

- (void)getUploadToken {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/api/alists/getststoken" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           weakSelf.uploadImageModel = [CXUploadImageTokenModel modelWithJSON:responseObject[@"data"]];
           [weakSelf uploadImageRequest];
       }
    }];
}

- (void)uploadImageRequest {
    id<OSSCredentialProvider> credential = [[OSSStsTokenCredentialProvider alloc] initWithAccessKeyId:self.uploadImageModel.AccessKeyId secretKeyId:self.uploadImageModel.AccessKeySecret securityToken:self.uploadImageModel.SecurityToken];
    OSSClient *client = [[OSSClient alloc] initWithEndpoint:self.uploadImageModel.Expiration credentialProvider:credential];
    OSSPutObjectRequest *put = [[OSSPutObjectRequest alloc] init];
    put.contentType = @"image/jpg";
    put.bucketName = self.uploadImageModel.BucketName;
    //memberpid/用户id/身份证图片
    put.objectKey = [NSString stringWithFormat:@"memberpid/%@/%@.jpg", [CXClientModel instance].userId, [NSDate currentTime]];
    NSData *imageData = UIImageJPEGRepresentation(_avatarImage, 0.5);
    put.uploadingData = imageData;
    
    OSSTask *putTask = [client putObject:put];
    __weak typeof(self) weakSelf = self;
    [putTask continueWithBlock:^id _Nullable(OSSTask * _Nonnull task) {
        if (!task.error) {
            dispatch_async(dispatch_get_main_queue(), ^{
               
                weakSelf.avatarImageUrl = put.objectKey;
                [weakSelf reMarkUserAvatar];
            });
        }
        return nil;
    }];
    [putTask waitUntilFinished];
}

- (void)reMarkUserAvatar {
    kWeakSelf
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"avatar":self.avatarImageUrl,
    };
    [CXHTTPRequest POSTWithURL:@"/index.php/api/user/setavatar" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           weakSelf.avatar.image = weakSelf.avatarImage;
       }
    }];
}

- (NSMutableArray *)itemArrays {
    if (!_itemArrays) {
        _itemArrays = [NSMutableArray array];
        
        NSDictionary *dict0 = @{
            @"logo": @"mine_yue",
            @"title": @"余额",
            @"desc": @"0玫瑰",
        };
        [_itemArrays addObject:dict0];
        
        NSDictionary *dict1 = @{
            @"logo": @"mine_qianbao",
            @"title": @"我的钱包",
            @"desc": @"",
        };
        [_itemArrays addObject:dict1];
        
        NSDictionary *dict2 = @{
            @"logo": @"mine_shouhu",
            @"title": @"我的守护",
            @"desc": @"",
        };
        [_itemArrays addObject:dict2];
        
        NSDictionary *dict3 = @{
            @"logo": @"mine_shimingrenzheng",
            @"title": @"实名认证",
            @"desc": @"认证得玫瑰",
        };
        [_itemArrays addObject:dict3];
        
        NSDictionary *dict4 = @{
            @"logo": @"mine_bangdingshouji",
            @"title": @"绑定",
            @"desc": @"",
        };
        [_itemArrays addObject:dict4];
        
        NSDictionary *dict6 = @{
            @"logo": @"mine_block",
            @"title": @"黑名单",
            @"desc": @"",
        };
        [_itemArrays addObject:dict6];
        
        NSDictionary *dict7 = @{
            @"logo": @"mine_editProfile",
            @"title": @"编辑资料",
            @"desc": @"",
        };
        [_itemArrays addObject:dict7];
        
        NSDictionary *dict5 = @{
            @"logo": @"mine_lianxiwomen",
            @"title": @"联系我们",
            @"desc": @"",
        };
        [_itemArrays addObject:dict5];
    }
    
    return _itemArrays;
}

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
    view.frame = CGRectMake(14,kStatusHeight+25,SCREEN_WIDTH - 28, 116);
    view.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0];
    view.layer.shadowColor = [UIColor colorWithRed:4/255.0 green:0/255.0 blue:0/255.0 alpha:0.15].CGColor;
    view.layer.shadowOffset = CGSizeMake(0,3);
    view.layer.shadowOpacity = 1;
    view.layer.shadowRadius = 8;
    view.layer.cornerRadius = 15;
    
    [self.view insertSubview:view aboveSubview:gImageView];
    
    self.avatar.layer.masksToBounds = YES;
    self.avatar.layer.cornerRadius = 36;
    self.avatar.layer.borderWidth = 0.5;
    self.avatar.layer.borderColor = UIColorHex(0x7B3EF1).CGColor;
    
    self.mainTableView.tableFooterView = [UIView new];
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    self.mainTableView.scrollEnabled = NO;
    self.mainTableView.layer.masksToBounds = YES;
    self.mainTableView.layer.cornerRadius = 12.5;
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXMineCell" bundle:nil] forCellReuseIdentifier:@"CXMineCellID"];
    self.mainTableViewHeightLayout.constant = 45*self.itemArrays.count;
    
    UIView *view2 = [[UIView alloc] init];
    view2.frame = CGRectMake(14,kStatusHeight+25+116+10,SCREEN_WIDTH - 28,45*self.itemArrays.count);
    view2.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:1.0];
    view2.layer.shadowColor = [UIColor colorWithRed:4/255.0 green:0/255.0 blue:0/255.0 alpha:0.23].CGColor;
    view2.layer.shadowOffset = CGSizeMake(0,0);
    view2.layer.shadowOpacity = 1;
    view2.layer.shadowRadius = 12;
    view2.layer.cornerRadius = 12.5;
    [self.view insertSubview:view2 atIndex:0];
}

@end

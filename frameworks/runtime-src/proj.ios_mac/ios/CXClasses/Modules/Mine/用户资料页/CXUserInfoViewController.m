//
//  CXUserInfoViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXUserInfoViewController.h"
#import "CXUserInfoHeaderReusableView.h"
#import "CXUserInfoGroupTitleReusableView.h"
#import "CXUserInfoMainInfoCell.h"
#import "CXUserInfoIntrosCell.h"
#import "CXUserInfoGiftCell.h"
#import "CXLiveRoomGuardianListViewController.h"
#import "CXUserInfoTotalGiftViewController.h"
#import "EMChatViewController.h"
#import "CXAddFriendViewController.h"
#import "CXLiveRoomUserProfileReportView.h"

@interface CXUserInfoViewController () <UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>
{
    NSInteger _mainInfoSectionIndex;
    NSInteger _introsectionIndex;
    NSInteger _profileSectionIndex;
    NSInteger _giftSectionIndex;
}

@property (weak, nonatomic) IBOutlet UIButton *moreButton;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mainCollectionView_bottomLayout;
@property (weak, nonatomic) IBOutlet UIButton *bottomBtn;

@property (nonatomic, copy) NSMutableArray *profileArrays;
@property (nonatomic, copy) NSArray *giftArrays;

@property (nonatomic, strong) CXUserModel *currentUser;
@property (nonatomic, strong) NSString *gift_num;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *hiddenLabel_topLayout;
@property (weak, nonatomic) IBOutlet UILabel *hiddenLabel;

@end

@implementation CXUserInfoViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    [self setupSubViews];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBarHidden = YES;
    
    [self getUserInfoData];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBarHidden = NO;
}

- (void)getUserInfoData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"user_id": _user_Id,
        @"device": @"iOS",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/user_info" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"user_info"]];
            weakSelf.currentUser = user;
            
            weakSelf.giftArrays = [NSArray modelArrayWithClass:[CXFriendGiftModel class] json:responseObject[@"data"][@"rank_gift"]];
            
            weakSelf.titleLabel.text = user.nickname;
            if ([weakSelf.user_Id isEqualToString:[CXClientModel instance].userId]) {
                weakSelf.bottomBtn.hidden = YES;
                weakSelf.moreButton.hidden = YES;
                weakSelf.mainCollectionView_bottomLayout.constant = 0;
            } else if ([weakSelf.currentUser.is_friend integerValue] == 1) {
                [weakSelf.bottomBtn setTitle:@"私聊好友" forState:UIControlStateNormal];
            } else {
                [weakSelf.bottomBtn setTitle:@"送礼物加好友（20玫瑰）" forState:UIControlStateNormal];
            }
            
            weakSelf.gift_num = responseObject[@"data"][@"gift_num"];
            
            [weakSelf.mainCollectionView reloadData];
            
            if ([user.isBlocks intValue] == 2) { // 已拉黑
                weakSelf.hiddenLabel.hidden = NO;
                weakSelf.hiddenLabel_topLayout.constant = 300*SCALE_W;
                weakSelf.mainCollectionView.scrollEnabled = NO;
            } else {
                weakSelf.hiddenLabel.hidden = YES;
                weakSelf.mainCollectionView.scrollEnabled = YES;
            }
        }
    }];
}

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    _mainInfoSectionIndex = 0;
    _introsectionIndex = _mainInfoSectionIndex+1;
    _profileSectionIndex = _introsectionIndex+1;
    _giftSectionIndex = _profileSectionIndex+1;
    
    return _giftSectionIndex+1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    if (section == _profileSectionIndex) {
        return self.profileArrays.count;
    } else if (section == _giftSectionIndex) {
        return self.giftArrays.count;
    } else {
        return 1;
    }
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == _mainInfoSectionIndex) {
        CXUserInfoMainInfoCell *infoCell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXUserInfoMainInfoCellID" forIndexPath:indexPath];
        infoCell.model = _currentUser;
        kWeakSelf
        infoCell.guardActionBlock = ^{
            // 守护团
            CXLiveRoomGuardianListViewController *vc = [CXLiveRoomGuardianListViewController new];
            vc.userId = weakSelf.currentUser.user_id;
            [weakSelf.navigationController pushViewController:vc animated:YES];
        };
        return infoCell;
    } else if (indexPath.section == _giftSectionIndex) {
        CXUserInfoGiftCell *infoCell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXUserInfoGiftCellID" forIndexPath:indexPath];
        CXFriendGiftModel *model = _giftArrays[indexPath.row];
        infoCell.model = model;
        return infoCell;
    } else {
        CXUserInfoIntrosCell *infoCell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXUserInfoIntrosCellID" forIndexPath:indexPath];
        if (indexPath.section == _introsectionIndex) {
            infoCell.itemLabel.text = _currentUser.intro;
        } else {
            NSDictionary *dict = _profileArrays[indexPath.row];
            infoCell.itemLabel.text = [NSString stringWithFormat:@"%@:  %@",dict[@"key"],dict[@"value"]];
        }
        return infoCell;
    }
}

#pragma mark headView _ footView
- (UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath{
    UICollectionReusableView * reusableview=nil;

    if ([kind isEqualToString:UICollectionElementKindSectionHeader]) {
        if (indexPath.section == _mainInfoSectionIndex) {
            CXUserInfoHeaderReusableView *headerView = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"CXUserInfoHeaderReusableView" forIndexPath:indexPath];
            [headerView.avatar sd_setImageWithURL:[NSURL URLWithString:_currentUser.avatar]];
            reusableview = headerView;
        } else if (indexPath.section==_profileSectionIndex){
            CXUserInfoGroupTitleReusableView *headerView = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"CXUserInfoGroupTitleReusableView" forIndexPath:indexPath];
            headerView.groupTitle.text=@"详细资料";
            headerView.MoreBtn.hidden=YES;
            reusableview = headerView;
        } else if (indexPath.section== _introsectionIndex){
            CXUserInfoGroupTitleReusableView *headerView = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"CXUserInfoGroupTitleReusableView" forIndexPath:indexPath];
            headerView.groupTitle.text=@"交友心声";
            headerView.MoreBtn.hidden=YES;
            reusableview = headerView;
        } else {
            CXUserInfoGroupTitleReusableView *headerView = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"CXUserInfoGroupTitleReusableView" forIndexPath:indexPath];
            NSString *numStr = [NSString stringWithFormat:@"累计获得礼物 (%@)", self.gift_num];
            NSMutableAttributedString *attri = [[NSMutableAttributedString alloc] initWithString:numStr];
            [attri addAttribute:NSForegroundColorAttributeName value:UIColorHex(0xEF51B2) range:NSMakeRange(6, numStr.length - 6)];
            headerView.groupTitle.attributedText = attri;
            kWeakSelf
            headerView.MoreBlock = ^{
                CXUserInfoTotalGiftViewController *vc = [CXUserInfoTotalGiftViewController new];
                vc.user_Id = self.user_Id;
                [weakSelf.navigationController pushViewController:vc animated:YES];
            };
            headerView.MoreBtn.hidden = self.giftArrays.count==0?YES:NO;
            reusableview=headerView;
        }

    } else{

    }
    return reusableview;
}
#pragma mark  Header_CGSize
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section{
    if (section == _mainInfoSectionIndex) {
       return CGSizeMake(SCREEN_WIDTH,300*SCALE_W);
    }
    return CGSizeMake(SCREEN_WIDTH, 40);
}
#pragma mark  Item_CGSize
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == _mainInfoSectionIndex) {
        return CGSizeMake(SCREEN_WIDTH, 90);
    } else if (indexPath.section==_profileSectionIndex) {
        return CGSizeMake(SCREEN_WIDTH, 30);
    } else if (indexPath.section == _giftSectionIndex) {
        return CGSizeMake(60, 100);
    } else if (indexPath.section == _introsectionIndex) {
        return CGSizeMake(SCREEN_WIDTH, [_currentUser.intro sizeForFont:[UIFont systemFontOfSize:15] size:CGSizeMake(kScreenWidth - 30, 100) mode:0].height+10);
    } else {
        return CGSizeMake(SCREEN_WIDTH, 60);
    }
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    if (section == _giftSectionIndex) {
         return UIEdgeInsetsMake(0, 15, 0, 2);
    }
      return UIEdgeInsetsMake(0, 0, 0, 0);
}

- (IBAction)backAction:(id)sender {
    [self.navigationController popViewControllerAnimated:YES];
}

- (IBAction)moreAction:(id)sender {
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"" message:@"" preferredStyle:UIAlertControllerStyleActionSheet];
    kWeakSelf
    UIAlertAction *blockAction = [UIAlertAction actionWithTitle:[_currentUser.isBlocks isEqualToString:@"1"] ? @"拉黑" : @"取消拉黑" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf blockUser];
    }];
    UIAlertAction *reportAction = [UIAlertAction actionWithTitle:@"举报" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf reportUser];
    }];
    
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil];
    
    [alert addAction:blockAction];
    [alert addAction:reportAction];
    [alert addAction:cancel];
    
    [self presentViewController:alert animated:YES completion:nil];
}

- (void)blockUser {
    if ([_currentUser.isBlocks isEqualToString:@"2"]) { // 取消拉黑
        [self blockUserWithUserId:_currentUser.user_id isBlock:NO];
    } else { // 拉黑
        CXSystemAlertView *view = [CXSystemAlertView loadNib];
        kWeakSelf
        [view showAlertTitle:@"拉黑对方" message:@"拉黑对方后，将无法再接收到Ta的任何消息。" cancel:nil sure:^{
            [weakSelf blockUserWithUserId:_currentUser.user_id isBlock:YES];
        }];
        [view show];
    }
}
- (void)blockUserWithUserId:(NSString *)userId isBlock:(BOOL)isBlcok {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"user_id" : userId
    };
    NSString *url = @"/index.php/Api/Member/blackout";
    if (isBlcok) {
        url = @"/index.php/Api/Member/defriend";
    }
    kWeakSelf
    [CXHTTPRequest POSTWithURL:url parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           [weakSelf toast:isBlcok == YES ? @"拉黑成功" : @"取消拉黑成功"];
           [weakSelf getUserInfoData];
       }
    }];
}

- (void)reportUser {
    CXLiveRoomUserProfileReportView *view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomUserProfileReportView" owner:self options:nil].firstObject;
    view.userId = _currentUser.user_id;
    [view show];
}

- (IBAction)bottomAction:(id)sender {
    if ([self.currentUser.is_friend integerValue] == 1) { // 私聊好友
        EMConversationModel *model = [EMConversationHelper modelFromContact:self.currentUser.user_id];
        if (model) {
            EMChatViewController *controller = [[EMChatViewController alloc] initWithCoversationModel:model];
            CXFriendInviteModel *friend = [CXFriendInviteModel modelWithJSON:[self.currentUser modelToJSONObject]];
            controller.friendModel = friend;
            [self.navigationController pushViewController:controller animated:YES];
        }
    } else { // 送礼物加好友（20玫瑰）
        CXAddFriendViewController *vc = [[CXAddFriendViewController alloc] init];
        vc.nickname = self.currentUser.nickname;
        vc.user_id = self.user_Id;
        vc.user_avatar = self.currentUser.avatar;
        [self.navigationController pushViewController:vc animated:YES];
    }
}

- (void)setupSubViews {
    self.bottomBtn.layer.masksToBounds = YES;
    self.bottomBtn.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [self.bottomBtn setBackgroundImage:image forState:UIControlStateNormal];

    [self.mainCollectionView registerClass:[CXUserInfoHeaderReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"CXUserInfoHeaderReusableView"];
    [_mainCollectionView registerClass:[CXUserInfoGroupTitleReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionHeader withReuseIdentifier:@"CXUserInfoGroupTitleReusableView"];
    [_mainCollectionView registerNib:[UINib nibWithNibName:@"CXUserInfoMainInfoCell" bundle:nil] forCellWithReuseIdentifier:@"CXUserInfoMainInfoCellID"];
    [_mainCollectionView registerNib:[UINib nibWithNibName:@"CXUserInfoIntrosCell" bundle:nil] forCellWithReuseIdentifier:@"CXUserInfoIntrosCellID"];
    [_mainCollectionView registerNib:[UINib nibWithNibName:@"CXUserInfoGiftCell" bundle:nil] forCellWithReuseIdentifier:@"CXUserInfoGiftCellID"];
    
    _mainCollectionView.delegate = self;
    _mainCollectionView.dataSource = self;
    _mainCollectionView.showsVerticalScrollIndicator = NO;
    _mainCollectionView.showsHorizontalScrollIndicator = NO;
}

- (NSMutableArray *)profileArrays {

    _profileArrays = [NSMutableArray array];
    if (self.currentUser.birthday.length > 0) {
        NSString *birth = self.currentUser.birthday;
        NSDateFormatter *dateFormatter = [[NSDateFormatter alloc]init];
        [dateFormatter setDateFormat:@"yyyy-MM-dd hh:mm:ss"];
        NSDate *birthDay = [dateFormatter dateFromString:birth];
        NSString *ageStr = [NSString stringWithFormat:@"%ld", (long)[self ageWithDateOfBirth:birthDay]];
        [_profileArrays addObject:@{@"key": @"年龄", @"value": ageStr}];

    }
    if (self.currentUser.education.length > 0) {
//        NSString *educationStr = [self.profileJson[@"education"] objectForKey:self.userInfo.user_info.education];
         [_profileArrays addObject:@{@"key": @"学历", @"value": self.currentUser.education}];
    }
    if (self.currentUser.marital_status.length > 0) {
        [_profileArrays addObject:@{@"key":@"婚姻", @"value": self.currentUser.marital_status}];
    }
    if (self.currentUser.stature.length > 0 && [self.currentUser.stature integerValue] > 0) {
        [_profileArrays addObject:@{@"key": @"身高", @"value": self.currentUser.stature}];
    }
    if (self.currentUser.pay.length > 0) {
//        NSString *valueStr = [self.profileJson[@"pay"] objectForKey:self.userInfo.user_info.pay];;
        [_profileArrays addObject:@{@"key": @"月收入", @"value": self.currentUser.pay}];
    }
    if (self.currentUser.profession.length > 0) {
        [_profileArrays addObject:@{@"key": @"职业", @"value": self.currentUser.profession}];
    }
    if (self.currentUser.housing_status.length > 0) {
//        NSString *valueStr = [self.profileJson[@"housing_status"] objectForKey:self.userInfo.user_info.housing_status];
        [_profileArrays addObject:@{@"key": @"住房情况", @"value": self.currentUser.housing_status}];
    }
    if (self.currentUser.charm_part.length > 0) {
//        NSArray *charmArray = [self.userInfo.user_info.charm_part componentsSeparatedByString:@","];
//        NSMutableArray *valueArrays = [NSMutableArray array];
//        for (int i = 0; i < charmArray.count; i++) {
//            NSString *tempStr = [self itemValueWithKey:charmArray[i] withType:@"charm_part"];
//            [valueArrays addObject:tempStr];
//        }
        [_profileArrays addObject:@{@"key": @"魅力部位", @"value": self.currentUser.charm_part}];
    }
    if (self.currentUser.blood_type.length > 0) {
//        NSString *blood_typeStr = [self.profileJson[@"blood_type"] objectForKey:self.userInfo.user_info.blood_type];
        [_profileArrays addObject:@{@"key": @"血型", @"value": self.currentUser.blood_type}];
    }
    if (self.currentUser.together.length > 0) {
//        NSString *togetherStr = [self.profileJson[@"together"] objectForKey:self.userInfo.user_info.together];
        [_profileArrays addObject:@{@"key": @"婚后与父母同居", @"value": self.currentUser.together}];
    }

    if (self.currentUser.live_together_marrge.length > 0) {
//        NSString *live_together_marrgeStr = [self.profileJson[@"live_together_marrge"] objectForKey:self.userInfo.user_info.live_together_marrge];
        [_profileArrays addObject:@{@"key":@"婚前同居", @"value": self.currentUser.live_together_marrge}];
    }

    return _profileArrays;
}

- (NSInteger)ageWithDateOfBirth:(NSDate *)date {
    // 出生日期转换 年月日
    NSDateComponents *components1 = [[NSCalendar currentCalendar] components:NSCalendarUnitDay | NSCalendarUnitMonth | NSCalendarUnitYear fromDate:date];
    NSInteger brithDateYear  = [components1 year];
    NSInteger brithDateDay   = [components1 day];
    NSInteger brithDateMonth = [components1 month];
    
    // 获取系统当前 年月日
    NSDateComponents *components2 = [[NSCalendar currentCalendar] components:NSCalendarUnitDay | NSCalendarUnitMonth | NSCalendarUnitYear fromDate:[NSDate date]];
    NSInteger currentDateYear  = [components2 year];
    NSInteger currentDateDay   = [components2 day];
    NSInteger currentDateMonth = [components2 month];
    
    // 计算年龄
    NSInteger iAge = currentDateYear - brithDateYear - 1;
    if ((currentDateMonth > brithDateMonth) || (currentDateMonth == brithDateMonth && currentDateDay >= brithDateDay)) {
        iAge++;
    }
    
    return iAge;
}

@end

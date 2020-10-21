//
//  CXHomeViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/22.
//

#import "CXHomeViewController.h"
#import "CXHomeChildViewController.h"
#import "CXHomeSearchViewController.h"
#import "CXLiveRoomCreateRoomViewController.h"
#import "CXUserModel.h"
#import "CXHomeFilterView.h"
#import "CXHomeRoomModel.h"
#import "CXChangeAgeAlertView.h"

@interface CXHomeViewController ()

@property (nonatomic, strong) UIButton *createRoomBtn;

@end

@implementation CXHomeViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self setupSubViews];

    self.titleSizeNormal = 16;
    self.titleSizeSelected = 23;
    self.titleColorSelected = UIColorHex(0xFFFFFF);
    self.titleColorNormal = UIColorHex(0xFFFFFF);
    self.automaticallyCalculatesItemWidths = YES;
    self.menuViewStyle = WMMenuViewStyleLine;
    self.progressColor = UIColorHex(0xFFFFFF);
    
    [self getRoomModeList];
    
    if ([CXClientModel instance].sex.integerValue < 1) { // 未设置性别
        CXChangeAgeAlertView *ageView = [[NSBundle mainBundle] loadNibNamed:@"CXChangeAgeAlertView" owner:nil options:nil].lastObject;
        kWeakSelf
        ageView.backActionBlock = ^{
            [weakSelf back];
        };
        [ageView show];
    }
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navigationController.navigationBarHidden = YES;
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    self.navigationController.navigationBarHidden = NO;
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    [self checkHostStatus];
}

- (void)getRoomModeList {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/RoomMode/getList" parameters:@{@"signature": signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSArray <CXHomeRoomModeModel *>*array = [NSArray modelArrayWithClass:[CXHomeRoomModeModel class] json:responseObject[@"data"][@"room_mode"]];
            NSMutableArray *titleArray = [NSMutableArray array];
            NSMutableArray *vcArray = [NSMutableArray array];
            [array enumerateObjectsUsingBlock:^(CXHomeRoomModeModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
                CXHomeChildViewController *vc = [CXHomeChildViewController new];
                vc.mode = obj;
                [vcArray addObject:vc];
                [titleArray addObject:obj.room_mode];
            }];
            
            weakSelf.titles = titleArray;
            weakSelf.viewControllerClasses = vcArray;
            
            [weakSelf reloadData];
            
            [weakSelf.scrollView setBackgroundColor:[UIColor clearColor]];
        }
    }];
    
}

// 自己是不是主播
- (void)checkHostStatus {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/iscreateroom" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            /*
             is_show 入口是否显示1:显示 2:不显示
             type 打开的类型1:主持我的房间 2:申请主持页
             */
            if ([responseObject[@"data"][@"is_show"] integerValue] == 1) {
                
                if ([responseObject[@"data"][@"type"] integerValue] == 1) {
                    [CXClientModel instance].user_isAnchor = YES;
                    [weakSelf.createRoomBtn setImage:[UIImage imageNamed:@"home_create"] forState:UIControlStateNormal];
                } else {
                    [CXClientModel instance].user_isAnchor = NO;
                    [weakSelf.createRoomBtn setImage:[UIImage imageNamed:@"home_shaixuan"] forState:UIControlStateNormal];
                }
            } else {
                [CXClientModel instance].user_isAnchor = NO;
                [weakSelf.createRoomBtn setImage:[UIImage imageNamed:@"home_shaixuan"] forState:UIControlStateNormal];
            }
        } else {
            [CXClientModel instance].user_isAnchor = NO;
            [weakSelf.createRoomBtn setImage:[UIImage imageNamed:@"home_shaixuan"] forState:UIControlStateNormal];
        }
    }];
}

- (CGRect)pageController:(WMPageController *)pageController preferredFrameForMenuView:(WMMenuView *)menuView{
    
    return CGRectMake(40, kStatusHeight, SCREEN_WIDTH-110, 44);
}
- (CGRect)pageController:(WMPageController *)pageController preferredFrameForContentView:(WMScrollView *)contentView{
    return  CGRectMake(0, kNavHeight, SCREEN_WIDTH, SCREEN_HEIGHT-kNavHeight-kBottomArea-49);
}
- (NSInteger)numbersOfChildControllersInPageController:(WMPageController *)pageController{
    return self.titles.count;
}

- (__kindof UIViewController *)pageController:(WMPageController *)pageController viewControllerAtIndex:(NSInteger)index{
    
    return (UIViewController *)self.viewControllerClasses[index];
}
- (NSString *)pageController:(WMPageController *)pageController titleAtIndex:(NSInteger)index{
    
    return self.titles[index];
}

- (void)setupSubViews {
    UIImage *gImage = [UIImage gradientImageWithSize:CGSizeMake(kScreenWidth, 190*SCALE_W) Color1:UIColorHex(0xEB5BBA) color2:UIColorHex(0x793EF2)];
    UIImageView *gImageView = [[UIImageView alloc] initWithImage:gImage];
    gImageView.frame = CGRectMake(0, 0, kScreenWidth, 190*SCALE_W);
    [self.view addSubview:gImageView];
    
    UIBezierPath *path = [[UIBezierPath alloc] init];
    [path moveToPoint:CGPointMake(0, 0)];
    [path addLineToPoint:CGPointMake(0, 150*SCALE_W)];
    [path addQuadCurveToPoint:CGPointMake(SCREEN_WIDTH, 150*SCALE_W) controlPoint:CGPointMake(SCREEN_WIDTH/2, 190*SCALE_W)];
    [path addLineToPoint:CGPointMake(SCREEN_WIDTH, 0)];
    CAShapeLayer *layer = [[CAShapeLayer alloc] init];
    layer.path = path.CGPath;
    layer.frame = gImageView.bounds;
    gImageView.layer.mask = layer;
    
    UIButton *back = [[UIButton alloc] initWithFrame:CGRectMake(0, kStatusHeight, 40, 44)];
    [back setImage:[UIImage imageNamed:@"back_white"] forState:UIControlStateNormal];
    [back addTarget:self action:@selector(back) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:back];
    
    UIButton *search = [[UIButton alloc] initWithFrame:CGRectMake(SCREEN_WIDTH - 70, kStatusHeight, 35, 44)];
    [search setImage:[UIImage imageNamed:@"search_white"] forState:UIControlStateNormal];
    [search addTarget:self action:@selector(search) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:search];
    
    UIButton *shanxuan = [[UIButton alloc] initWithFrame:CGRectMake(SCREEN_WIDTH - 35, kStatusHeight, 35, 44)];
    [shanxuan setImage:[UIImage imageNamed:@"home_shaixuan"] forState:UIControlStateNormal];
    [shanxuan addTarget:self action:@selector(shaixuan) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:shanxuan];
    _createRoomBtn = shanxuan;
}

- (void)back {

    [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXBaseTabBarViewController_leaveOut object:nil];
}

- (void)search {
    CXHomeSearchViewController *vc = [CXHomeSearchViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)shaixuan {
    if ([CXClientModel instance].user_isAnchor == YES) {
        kWeakSelf
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/create" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                CXHomeRoomCreateRoomInfoModel *roomInfo = [CXHomeRoomCreateRoomInfoModel modelWithJSON:responseObject[@"data"][@"room_info"]];
                if (roomInfo) {
                   CXLiveRoomCreateRoomViewController *vc = [CXLiveRoomCreateRoomViewController new];
                   vc.roomInfo = roomInfo;
                   [weakSelf.navigationController pushViewController:vc animated:YES];
                }
            }
        }];
    } else {
        CXHomeFilterView *filter = [[NSBundle mainBundle] loadNibNamed:@"CXHomeFilterView" owner:self options:nil].firstObject;
        filter.filterBlock = ^(NSString * _Nonnull age, NSString * _Nonnull city, NSString * _Nonnull city_two, NSString * _Nonnull city_three) {
            NSDictionary *dict = @{
                @"age":age,
                @"city":city,
                @"city_two":city_two,
                @"city_three":city_three,
            };
            [[NSNotificationCenter defaultCenter] postNotificationName:kNSNotificationCenter_CXHomeChildViewController_Filter object:dict];
        };
        [filter show];
    }
}

- (void)createRoomAction {
    CXLiveRoomCreateRoomViewController *vc = [CXLiveRoomCreateRoomViewController new];
    [self.navigationController pushViewController:vc animated:YES];
}

@end

//
//  CXHomeChildViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import "CXHomeChildViewController.h"
#import "CXHomeRoomCell.h"
#import "CXHomeRoomCrycleCell.h"
#import "CXHomeRoomRecommendCell.h"
#import "CXBaseWebViewController.h"

@interface CXHomeChildViewController () <UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout>
@property (retain, nonatomic) IBOutlet UICollectionView *mainCollectionView;

@property (nonatomic, assign) NSInteger page;

@property (nonatomic, strong) NSArray <CXHomeRoomBannerModel *>* bannerData;
@property (nonatomic, strong) NSMutableArray <CXHomeRoomModel *>* roomList;

@property (nonatomic, strong) NSString *age;
@property (nonatomic, strong) NSString *city;
@property (nonatomic, strong) NSString *city_two;
@property (nonatomic, strong) NSString *city_three;

@end

@implementation CXHomeChildViewController

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.view.backgroundColor = [UIColor clearColor];
    
    self.roomList = [NSMutableArray array];
    
    [self setupSubViews];
    
    [self getCycleData];
    
    self.age = @"";
    self.city = @"";
    self.city_two = @"";
    self.city_three = @"";
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(filter:) name:kNSNotificationCenter_CXHomeChildViewController_Filter object:nil];
}

- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
    [self headerRefresh];
}

- (void)filter:(NSNotification *)notification {
    NSDictionary *dict = notification.userInfo;
    self.age = dict[@"age"];
    self.city = dict[@"city"];
    self.city_two = dict[@"city_two"];
    self.city_three = dict[@"city_three"];
    [self headerRefresh];
}
- (void)getCycleData {
    if (!self.bannerData) {
        __weak typeof (self) wself = self;
        NSDictionary *param = @{@"type" : @"1"};
        
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/bannerlist" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (responseObject) {
                wself.bannerData = [NSArray modelArrayWithClass:[CXHomeRoomBannerModel class] json:responseObject[@"data"][@"bannerList"]];
                [wself.mainCollectionView reloadSections:[NSIndexSet indexSetWithIndex:0]];
            }
        }];
    }
}

#pragma mark - MJRefresh
- (void)headerRefresh {
    _page = 1;
    [self getRoomListData];
}

- (void)footerRefresh {
    _page++;
    [self getRoomListData];
}

- (void)getRoomListData {
    kWeakSelf
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"room_type": self.mode.mode_id,
        @"page":[NSString stringWithFormat:@"%ld", (long)_page],
        @"signature": signature,
        @"age" : self.age? self.age: @"",
        @"city" : self.city? self.city: @"",
        @"city_two" : self.city_two? self.city_two: @"",
        @"city_three" : self.city_three? self.city_three: @"",
    };
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/getRoomList" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        [weakSelf.mainCollectionView.mj_header endRefreshing];
        [weakSelf.mainCollectionView.mj_footer endRefreshing];
        if (!error) {
            NSArray *roomList = [NSArray modelArrayWithClass:[CXHomeRoomModel class] json:responseObject[@"data"][@"room_list"]];
            if (weakSelf.page == 1) {
                [weakSelf.roomList removeAllObjects];
            }
            [weakSelf.roomList addObjectsFromArray:roomList];
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [weakSelf.mainCollectionView reloadData];
            });
            
            NSInteger totoalPage = [responseObject[@"data"][@"pageInfo"][@"totalPage"] integerValue];
            if (totoalPage <= weakSelf.page) {
                [weakSelf.mainCollectionView.mj_footer endRefreshingWithNoMoreData];
            }
        }
    }];
}

#pragma mark <UICollectionViewDataSource>

- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 4;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    if (section == 0) {
        return _roomList.count > 0 ? 1 : 0;
    } else if (section == 1) {
        return _roomList.count > 1 ? MIN(_roomList.count, 2) : 0;
    } else if (section == 2) {
        return 1;
    } else {
        return _roomList.count > 3 ? _roomList.count - 3: 0;
    }
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 2) {
        CXHomeRoomCrycleCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXHomeRoomCrycleCellID" forIndexPath:indexPath];
        cell.bannerList = self.bannerData;
        kWeakSelf
        cell.didSelectedCycleUrl = ^(CXHomeRoomBannerModel * _Nonnull item) {
            [weakSelf cycleScrollDidSecected:item];
        };
        return cell;
    } else if (indexPath.section == 0) {
        CXHomeRoomRecommendCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXHomeRoomRecommendCellID" forIndexPath:indexPath];
        cell.model = _roomList[0];
        return cell;
    } else if (indexPath.section == 1) {
        CXHomeRoomCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXHomeRoomCellID" forIndexPath:indexPath];
        
        cell.model = _roomList[indexPath.row+1];
        
        return cell;
    } else {
        CXHomeRoomCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXHomeRoomCellID" forIndexPath:indexPath];
        
        cell.model = _roomList[indexPath.row+3];
        
        return cell;
    }
}

- (void)cycleScrollDidSecected:(CXHomeRoomBannerModel *)item {
    if ([item.link_type integerValue] == 3) {
        if ([item.ui_type integerValue] == 2) { // 排行榜
//            self.app.isHomeCycleScrollToFind_rank = YES;
//            self.tabBarController.selectedIndex = 1;
        }
    } else {
        NSString * linkurl = [item.linkurl copy];
        if (linkurl.length) {
            NSRange range = [linkurl rangeOfString:@"?"];
            if (range.location < linkurl.length) {
                linkurl = [linkurl stringByAppendingFormat:@"&uid=%@&token=%@", [CXClientModel instance].userId, [CXClientModel instance].token];
            } else {
                linkurl = [linkurl stringByAppendingFormat:@"?uid=%@&token=%@", [CXClientModel instance].userId, [CXClientModel instance].token];
            }

            CXBaseWebViewController *webVC = [[CXBaseWebViewController alloc] initWithURL:[NSURL URLWithString:linkurl]];
            webVC.title = item.title;
            [self.navigationController pushViewController:webVC animated:YES];
        }
    }
}

#pragma mark <UICollectionViewDelegate>

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section != 2) {
        if (indexPath.section == 0) {
            CXHomeRoomModel *room = _roomList.firstObject;
            if ([room.room_lock integerValue] == 1) {
                [self toast:@"房间已锁"];
            } else {
                [AppController joinRoom:room.room_id];
            }
        } else if (indexPath.section == 1) {
            CXHomeRoomModel *room = _roomList[indexPath.row+1];
            if ([room.room_lock integerValue] == 1) {
                [self toast:@"房间已锁"];
            } else {
                [AppController joinRoom:room.room_id];
            }
        } else {
            CXHomeRoomModel *room = _roomList[indexPath.row+3];
            if ([room.room_lock integerValue] == 1) {
                [self toast:@"房间已锁"];
            } else {
                [AppController joinRoom:room.room_id];
            }
        }
    }
}

#pragma mark <UICollectionViewDelegateFlowLayout>
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0 || indexPath.section == 2) {
        return CGSizeMake(SCREEN_WIDTH - 28, 118*SCALE_W);
    } else {
        CGFloat w =(collectionView.bounds.size.width - 28 - 10) / 2;

        return CGSizeMake(w, w*150/167+26);
    }
}


- (void)setupSubViews {
    self.mainCollectionView.dataSource = self;
    self.mainCollectionView.delegate = self;
    
    self.mainCollectionView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];

    self.mainCollectionView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXHomeRoomCell" bundle:nil] forCellWithReuseIdentifier:@"CXHomeRoomCellID"];
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXHomeRoomCrycleCell" bundle:nil] forCellWithReuseIdentifier:@"CXHomeRoomCrycleCellID"];
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXHomeRoomRecommendCell" bundle:nil] forCellWithReuseIdentifier:@"CXHomeRoomRecommendCellID"];
    
    
}


@end

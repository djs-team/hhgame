//
//  MuaGiftListView.m
//  hairBall
//
//  Created by shiwei on 2019/7/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MuaGiftListView.h"
#import "giftCell.h"
#import "voiceGiftModel.h"
#import "MuaGiftUserIconView.h"
#import <MBProgressHUD.h>
#import "MuaGiftListSelectCountCell.h"
#import "UIView+CXCategory.h"

@interface MuaGiftListView () <UICollectionViewDelegate, UICollectionViewDataSource, UITableViewDataSource, UITableViewDelegate>

@property (nonatomic, strong) UIButton *aimPeopleBtn;
@property (nonatomic, strong) UIScrollView *topScrollView;

@property (nonatomic, strong) UIButton *giftButton; // 礼物
@property (nonatomic, strong) UIView *giftLine;
@property (nonatomic, strong) UIButton *knapsackButton; // 背包
@property (nonatomic, strong) UIView *knapsackLine;

@property (nonatomic, strong) UICollectionView *giftCollectionView;
@property (nonatomic, strong) UIPageControl *currentPageControl;

@property (nonatomic,strong)UIButton *addMoneyBtn;

@property (nonatomic,strong)NSMutableArray *usersView;
@property (nonatomic,strong)NSMutableArray *giftListArray;
@property (nonatomic,strong)NSMutableArray *selectUsersArray;

@property (nonatomic,strong,nullable)voiceGiftModel *currentModel;



@property (nonatomic, strong) UILabel *numberLabel;

@property (nonatomic, strong) UIButton *selectCountBg;
@property (nonatomic, strong) UITableView *selectCountTable;
@property (nonatomic, strong) NSArray *selectCountArray;

@end

@implementation MuaGiftListView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setupUI];
    }
    return self;
}

- (void)setupUI {
    
    self.backgroundColor = [UIColor blackColor];
    
    UIView *topBg = [UIView new];
    topBg.backgroundColor = [UIColor clearColor];
    [self addSubview:topBg];
    [topBg mas_makeConstraints:^(MASConstraintMaker *make) {
       make.left.top.right.offset(0);
       make.height.mas_equalTo(74);
    }];

    UILabel *send = [UILabel new];
    send.text = @"送给";
    send.textColor = [UIColor whiteColor];
    send.font = [UIFont boldSystemFontOfSize:13];
    [topBg addSubview:send];
    [send mas_makeConstraints:^(MASConstraintMaker *make) {
       make.left.offset(15);
       make.centerY.mas_equalTo(topBg);
    }];
    
    _aimPeopleBtn = [[UIButton alloc]init];
    [_aimPeopleBtn setTitle:@"全麦" forState:UIControlStateNormal];
    [_aimPeopleBtn setBackgroundImage:[UIImage imageWithColor:[UIColor colorWithHexString:@"#2E364E"]] forState:UIControlStateNormal];
    [_aimPeopleBtn setBackgroundImage:[UIImage imageWithColor:[UIColor whiteColor]] forState:UIControlStateSelected];
    [_aimPeopleBtn setTitleColor:[UIColor colorWithHexString:@"#F052B2"] forState:UIControlStateSelected];
    [_aimPeopleBtn setTitleColor:[UIColor colorWithHexString:@"#626B86"] forState:UIControlStateNormal];
    [_aimPeopleBtn addTarget:self action:@selector(aimPeopleClick) forControlEvents:UIControlEventTouchUpInside];
    _aimPeopleBtn.clipsToBounds = YES;
    _aimPeopleBtn.layer.cornerRadius = 19;
    _aimPeopleBtn.titleLabel.font = [UIFont boldSystemFontOfSize:15.0f];
    [topBg addSubview:_aimPeopleBtn];
    [_aimPeopleBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(send.mas_right).offset(10);
        make.centerY.equalTo(topBg);
        make.size.mas_equalTo(CGSizeMake(38, 38));
    }];
    
    _topScrollView = [[UIScrollView alloc]init];
    _topScrollView.alwaysBounceHorizontal = true;
    _topScrollView.backgroundColor = [UIColor clearColor];
    [topBg addSubview:_topScrollView];
    [_topScrollView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.aimPeopleBtn.mas_right).offset(10);
        make.right.offset(0);
        make.centerY.height.equalTo(_aimPeopleBtn);
//        make.height.mas_equalTo(38);
    }];
    
    UIView *line = [[UIView alloc]init];
    line.backgroundColor = [UIColor whiteColor];
    line.alpha = 0.32;
    [self addSubview:line];
    
    _giftButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_giftButton setTitle:@"普通礼物" forState:UIControlStateNormal];
    [_giftButton setTitleColor:UIColorHex(0x828282) forState:UIControlStateNormal];
    [_giftButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    _giftButton.titleLabel.font = [UIFont systemFontOfSize:15];
    [_giftButton addTarget:self action:@selector(giftBtnAction) forControlEvents:UIControlEventTouchUpInside];
    _giftButton.selected = true;
    [self addSubview:_giftButton];
    [_giftButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(15);
        make.top.mas_equalTo(topBg.mas_bottom).offset(0);
        make.size.mas_equalTo(CGSizeMake(70, 46));
    }];
    _giftLine = [UIView new];
    _giftLine.backgroundColor = [UIColor whiteColor];
    [self addSubview:_giftLine];
    [_giftLine mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.giftButton.mas_bottom).offset(2);
        make.centerX.equalTo(self.giftButton);
        make.size.mas_equalTo(CGSizeMake(10, 2));
    }];
    
    _knapsackButton = [UIButton buttonWithType:UIButtonTypeCustom];
    [_knapsackButton setTitle:@"守护礼物" forState:UIControlStateNormal];
    [_knapsackButton setTitleColor:UIColorHex(0x828282) forState:UIControlStateNormal];
    [_knapsackButton setTitleColor:[UIColor whiteColor] forState:UIControlStateSelected];
    [_knapsackButton addTarget:self action:@selector(knapsackBtnAction) forControlEvents:UIControlEventTouchUpInside];
    _knapsackButton.titleLabel.font = [UIFont systemFontOfSize:15];
    [self addSubview:_knapsackButton];
    [_knapsackButton mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.giftButton.mas_right).offset(30);
        make.size.centerY.equalTo(self.giftButton);
    }];
    _knapsackLine = [UIView new];
    _knapsackLine.backgroundColor = [UIColor whiteColor];
    _knapsackLine.hidden = true;
    [self addSubview:_knapsackLine];
    [_knapsackLine mas_makeConstraints:^(MASConstraintMaker *make) {
       make.top.mas_equalTo(self.knapsackButton.mas_bottom).offset(2);
       make.centerX.equalTo(self.knapsackButton);
        make.size.mas_equalTo(CGSizeMake(10, 2));
    }];
    
    [line mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.offset(0);
        make.centerY.equalTo(self.giftLine);
        make.height.mas_equalTo(0.5);
    }];
    
    UICollectionViewFlowLayout *waterLayout = [[UICollectionViewFlowLayout alloc]init];
    waterLayout.itemSize = CGSizeMake(SCREEN_WIDTH, 230);
    waterLayout.minimumLineSpacing = 0;
    waterLayout.minimumInteritemSpacing = 0;
    waterLayout.scrollDirection = UICollectionViewScrollDirectionHorizontal;
    waterLayout.minimumInteritemSpacing = 0;
    waterLayout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
    
    self.giftCollectionView = [[UICollectionView alloc]initWithFrame:CGRectMake(0, 0, 0, 0) collectionViewLayout:waterLayout];
    self.giftCollectionView.delegate = self;
    self.giftCollectionView.dataSource = self;
    self.giftCollectionView.bounces = NO;
    self.giftCollectionView.showsVerticalScrollIndicator = NO;
    self.giftCollectionView.showsHorizontalScrollIndicator = NO;
    self.giftCollectionView.allowsMultipleSelection= NO;
    self.giftCollectionView.pagingEnabled = YES;
    self.giftCollectionView.scrollEnabled = YES;
    self.giftCollectionView.alwaysBounceHorizontal = YES;
    self.giftCollectionView.backgroundColor = [UIColor clearColor];
    [self.giftCollectionView registerClass:[giftCell class] forCellWithReuseIdentifier:@"ZPGiftViewCell"];
    [self addSubview:self.giftCollectionView];
    [self.giftCollectionView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(0);
        make.right.equalTo(self.mas_right).offset(0);
        make.top.mas_equalTo(line.mas_bottom).offset(10);
        make.height.mas_equalTo(230);
    }];
    
    _currentPageControl = [[UIPageControl alloc]init];
    _currentPageControl.currentPageIndicatorTintColor = [UIColor whiteColor];
    _currentPageControl.pageIndicatorTintColor = UIColorHex(0x646167);
    [_currentPageControl addTarget:self action:@selector(valueChanged:) forControlEvents:(UIControlEventValueChanged)];
    [self addSubview:self.currentPageControl];
    [self.currentPageControl mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(200, 22));
        make.left.equalTo(self.mas_left).offset((SCREEN_WIDTH-200)/2);
        make.top.equalTo(self.giftCollectionView.mas_bottom).offset(-20);
    }];
    
    UIImageView *roseImg = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"liveroom_rose"]];
    [self addSubview:roseImg];
    [roseImg mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(15);
        make.bottom.mas_equalTo(-20);
        make.size.mas_equalTo(CGSizeMake(16, 17));
    }];
    
    _moneyLabel = [[UILabel alloc]init];
    _moneyLabel.textColor = [UIColor whiteColor];
    _moneyLabel.font = [UIFont boldSystemFontOfSize:12.0f];
    self.moneyLabel.text = [[CXClientModel instance].balance stringValue];
    [self addSubview:_moneyLabel];
    [self.moneyLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(roseImg.mas_right).offset(10);
        make.centerY.equalTo(roseImg);
    }];
    
    _addMoneyBtn = [[UIButton alloc]init];
    [_addMoneyBtn setTitle:@"充值" forState:UIControlStateNormal];
    [_addMoneyBtn setTitleColor:UIColorHex(0xF052B2) forState:UIControlStateNormal];
    _addMoneyBtn.titleLabel.font = [UIFont boldSystemFontOfSize:16.0f];
    _addMoneyBtn.layer.masksToBounds = YES;
    _addMoneyBtn.layer.cornerRadius = 15;
    _addMoneyBtn.layer.borderWidth = 1;
    _addMoneyBtn.layer.borderColor = UIColorHex(0xFF08F1).CGColor;
    [self addSubview:_addMoneyBtn];
    [_addMoneyBtn addTarget:self action:@selector(addMoneyBtnTouchUpInside) forControlEvents:UIControlEventTouchUpInside];
    [self.addMoneyBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self.moneyLabel);
        make.left.equalTo(self.moneyLabel.mas_right).offset(10);
        make.size.mas_equalTo(CGSizeMake(75, 30));
    }];
}

- (void)giftBtnAction {
    _giftButton.selected = true;
    self.knapsackButton.selected = false;
    self.giftLine.hidden = false;
    self.knapsackLine.hidden = true;
    [self setDataWith:0];
}

- (void)knapsackBtnAction {
    _knapsackButton.selected = YES;
    self.giftButton.selected = NO;
    self.giftLine.hidden = YES;
    self.knapsackLine.hidden = NO;
    [self setDataWith:1];
}

- (void)setUsers:(NSArray *)users {
    _users = users;
    
    if (!users.count) {
        return;
    }
    
    NSMutableArray *itemUsers = [NSMutableArray array];
    for (LiveRoomUser *user in users) {
        MuaGiftUserModel *giftUser = [MuaGiftUserModel new];
        giftUser.isSelect = false;
        giftUser.model = user;
        [itemUsers addObject:giftUser];
    }
    
    self.selectUsersArray = [NSMutableArray array];
    self.usersView = [NSMutableArray array];
    [self.topScrollView removeAllSubviews];
    
    for (int i=0; i<itemUsers.count; i++) {
        MuaGiftUserIconView *user = [[MuaGiftUserIconView alloc]initWithFrame:CGRectMake((38+10)*i, 0, 38, 38)];
        user.backgroundColor = [UIColor clearColor];
        user.model = itemUsers[i];
        [user sw_whenTapped:^{
            
            if (user.model.isSelect) {
                user.model.isSelect = false;
                [self.selectUsersArray removeObject:user.model.model];
            } else {
                user.model.isSelect = true;
                [self.selectUsersArray addObject:user.model.model];
            }
            
            [user reloadData];
        }];
        [self.usersView addObject:user];
        [self.topScrollView addSubview:user];
        self.topScrollView.contentSize = CGSizeMake(users.count * (38+10), 0);
    }
    
    if (users.count == 1) {
        MuaGiftUserIconView *user = self.usersView.lastObject;
        user.model.isSelect = true;
        self.aimPeopleBtn.selected = true;
        [user reloadData];
        [self.selectUsersArray addObject:user.model.model];
    }
}

- (void)setGift_info:(NSArray *)gift_info {
    _gift_info = gift_info;
}

- (void)setKnapsack_gift:(NSArray<CXLiveRoomGiftModel *> *)knapsack_gift {
    _knapsack_gift = knapsack_gift;
    
}

/// type 1 背包
- (void)setDataWith:(NSInteger)type {
    
    NSArray *arr;
    NSMutableArray *temp = [NSMutableArray array];
    
    if (type == 1) {

        arr = [NSArray arrayWithArray:self.knapsack_gift];
        
        for (CXLiveRoomGiftModel *info in arr) {
            voiceGiftModel *model = [voiceGiftModel modelWithJSON:[info modelToJSONObject]];
            model.isSelect = false;
            model.IsUseBeg = true;
            [temp addObject:model];
        }
        
    } else {
        
        arr = [NSArray arrayWithArray:self.gift_info];
        
        for (CXLiveRoomGiftModel *info in arr) {
            voiceGiftModel *model = [voiceGiftModel modelWithJSON:[info modelToJSONObject]];
            model.isSelect = false;
            [temp addObject:model];
        }
    }
    
    self.giftListArray = [NSMutableArray array];
    
    self.giftListArray = [CXTools splitArray:temp withSubSize:8];
    
    self.currentPageControl.numberOfPages = self.giftListArray.count;
    self.currentPageControl.currentPage = 0;
    [self.giftCollectionView setContentOffset:CGPointZero];
    [self.giftCollectionView reloadData];
}

- (void)aimPeopleClick {
    
    if (self.aimPeopleBtn.selected) {
        
        self.selectUsersArray = [NSMutableArray array];
        for (MuaGiftUserIconView *user in self.usersView) {
            user.model.isSelect = false;
            [user reloadData];
        }
        self.aimPeopleBtn.selected = false;
    } else {
        
        self.selectUsersArray = [NSMutableArray array];
        for (MuaGiftUserIconView *user in self.usersView) {
            user.model.isSelect = true;
            [user reloadData];
            [self.selectUsersArray addObject:user.model.model];
        }
        self.aimPeopleBtn.selected = true;
    }
}

- (void)giftSendClick {
    if (!self.currentModel) {
        [MBProgressHUD showTipMessageInView:@"未选中礼物"];
        
        return;
    }
    if (!self.selectUsersArray.count) {
        [MBProgressHUD showTipMessageInView:@"未选中用户"];
        return;
    }
    if (self.sendGiftBlock) {
        CXLiveRoomGiftModel *info = [CXLiveRoomGiftModel modelWithJSON:[self.currentModel modelToJSONObject]];
        self.sendGiftBlock(info, self.selectUsersArray, [self.numberLabel.text componentsSeparatedByString:@"x"].lastObject, self.knapsackButton.selected);
    }
}

- (void)valueChanged:(id)sender {
    
}

- (void)addMoneyBtnTouchUpInside {
    if (self.rechargeBlock) {
        self.rechargeBlock();
    }
}

#pragma mark- UICollectionViewDelegate
- (void)scrollViewDidEndDecelerating:(UIScrollView *)scrollView {
    self.currentPageControl.currentPage = ceil(scrollView.contentOffset.x/self.frame.size.width);
}
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.giftListArray.count;
}
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    giftCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"ZPGiftViewCell" forIndexPath:indexPath];
    [cell setGiftCell:self.giftListArray[indexPath.row]];
    __weak typeof(self) weakSelf = self;
    cell.clickBlock = ^(voiceGiftModel * _Nonnull model) {
        __strong typeof(self) self = weakSelf;
        [self clickSendGiftBtn:model];
    };
    return cell;
}

-(void)clickSendGiftBtn:(voiceGiftModel *)model{
    self.currentModel.isSelect = false;
    model.isSelect = true;
    self.currentModel = model;
    [self.giftCollectionView reloadData];
    
    if (!self.currentModel) {
       [MBProgressHUD showTipMessageInView:@"未选中礼物"];
       
       return;
   }
    
   if (!self.selectUsersArray.count) {
       [MBProgressHUD showTipMessageInView:@"未选中用户"];
       return;
   }
    
   if (self.sendGiftBlock) {
       CXLiveRoomGiftModel *info = [CXLiveRoomGiftModel modelWithJSON:[self.currentModel modelToJSONObject]];
       self.sendGiftBlock(info, self.selectUsersArray, @"1", self.knapsackButton.selected);
   }
    
}

- (void)dealloc {
    NSLog(@"MuaGiftListView dealloc");
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.selectCountArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {

    MuaGiftListSelectCountCell *cell = [MuaGiftListSelectCountCell cellWithTableView:tableView identifier:@"MuaGiftListSelectCountCell"];
    cell.countStr = self.selectCountArray[indexPath.row];
    return cell;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 35;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    // [tableView deselectRowAtIndexPath:indexPath animated:true];
    NSString *str = self.selectCountArray[indexPath.row];
    self.numberLabel.text = [NSString stringWithFormat:@"x%@", [str componentsSeparatedByString:@" "].firstObject];
    self.selectCountBg.hidden = true;
}

- (NSArray *)selectCountArray
{
    if (_selectCountArray == nil) {
        _selectCountArray = @[
            @"1 一心一意",
            @"5 五福临门",
            @"10 十全十美",
            @"21 爱你爱你",
            @"30 想你",
            @"66 诸事顺利",
            @"188 要抱抱",
            @"520 我爱你",
            @"999 天长地久",
            @"1314 一生一世"
        ];
    }
    return _selectCountArray;
}

@end

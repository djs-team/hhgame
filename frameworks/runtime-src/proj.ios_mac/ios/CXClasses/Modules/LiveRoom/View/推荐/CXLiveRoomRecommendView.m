//
//  CXLiveRoomRecommendView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomRecommendView.h"
#import "CXLiveRoomRecommendCell.h"

@interface CXLiveRoomRecommendView() <UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout>

@property (weak, nonatomic) IBOutlet UIButton *clickBtn;
@property (weak, nonatomic) IBOutlet UIView *listBGView;
@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *recommendViewTrailingLayout;

@property (nonatomic, assign) NSInteger page;

@end

@implementation CXLiveRoomRecommendView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.mainCollectionView.delegate = self;
    self.mainCollectionView.dataSource = self;
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXLiveRoomRecommendCell" bundle:nil] forCellWithReuseIdentifier:@"CXLiveRoomRecommendCellID"];
    
    kWeakSelf
    self.mainCollectionView.mj_header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        [weakSelf headerfresh];
    }];
    self.mainCollectionView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        [weakSelf footerFresh];
    }];
    
    _listBGView.layer.masksToBounds = YES;
    _listBGView.layer.cornerRadius = 4;
    
    _page = 1;
    
}

- (void)headerfresh {
    _page = 1;
    
    [self getRecommendListData];
}

- (void)footerFresh {
    _page++;
    
    [self getRecommendListData];
}

// 获取推荐列表
- (void)getRecommendListData {
    NSDictionary *param = @{
        @"page":[NSString stringWithFormat:@"%ld", _page],
        @"now_roomid":[CXClientModel instance].room.RoomData.RoomId,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/recommend_room" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSArray *array = [NSArray modelArrayWithClass:[CXLiveRoomRecommendModel class] json:responseObject[@"data"][@"list"]];
            if (weakSelf.page == 1) {
                [weakSelf.dataSources removeAllObjects];
                weakSelf.dataSources = [NSMutableArray arrayWithArray:array];
            } else {
                [weakSelf.dataSources addObjectsFromArray:array];
            }
            [weakSelf.mainCollectionView reloadData];
            
            [weakSelf.mainCollectionView.mj_header endRefreshing];
            [weakSelf.mainCollectionView.mj_footer endRefreshing];
            
            if (array.count < 10) {
                [weakSelf.mainCollectionView.mj_footer endRefreshingWithNoMoreData];
            }
        }
    }];
}

- (void)show {
    [[UIApplication sharedApplication].keyWindow addSubview:self];
    self.recommendViewTrailingLayout.constant = 150;
    [UIView animateWithDuration:5 animations:^{
        self.recommendViewTrailingLayout.constant = 0;
    }];
    
    [self headerfresh];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomRecommendCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXLiveRoomRecommendCellID" forIndexPath:indexPath];
    CXLiveRoomRecommendModel *model = _dataSources[indexPath.row];
    cell.model = model;
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(100, 100);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomRecommendModel *model = _dataSources[indexPath.row];
    if (self.joinRecommendRoom) {
        self.joinRecommendRoom(model.room_id);
    }
    
    [self hideAction:nil];
}

- (IBAction)hideAction:(id)sender {
    
    [self removeFromSuperview];
}
@end

//
//  CXGameMusicRankListCollectionCell.m
//  hairBall
//
//  Created by mahong yang on 2020/4/14.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicRankListCollectionCell.h"
#import "CXGameMusicRankListCell.h"

@interface CXGameMusicRankListCollectionCell() <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, assign) NSInteger page;

@end

@implementation CXGameMusicRankListCollectionCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _page = 0;
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    
    _dataSources = [NSMutableArray array];
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    _mainTableView.mj_footer.automaticallyChangeAlpha = YES;
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXGameMusicRankListCell" bundle:nil] forCellReuseIdentifier:@"CXGameMusicRankListCellID"];
}

- (void)reloadRankList {
//    kWeakSelf
//    CXSocketMessageMusicRank *request = [CXSocketMessageMusicRank new];
//    request.Page = self.page;
//    [AppDelegate.shared.client sendSocketRequest:request withCallback:^(CXSocketMessageMusicRank * _Nonnull request) {
//        if (request.response.isSuccess) {
//            // 获取成功
//            if (weakSelf.page == 0) {
//                weakSelf.dataSources = [NSMutableArray arrayWithArray:request.response.SongRankDatas];
//            } else {
//                [weakSelf.dataSources addObjectsFromArray:request.response.SongRankDatas];
//            }
//            
//            [weakSelf.mainTableView reloadData];
//            
//            [weakSelf.mainTableView.mj_header endRefreshing];
//            [weakSelf.mainTableView.mj_footer endRefreshing];
//            
//            if (request.response.SongRankDatas.count < 10) {
//                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
//            }
//        }
//    }];
}

- (void)headerRefresh {
    self.page = 0;
    [self reloadRankList];
}

- (void)footerRefresh {
    self.page++;
    [self reloadRankList];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXGameMusicRankListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXGameMusicRankListCellID"];
    CXSocketMessageMusicRankModel *model = self.dataSources[indexPath.row];
    cell.model = model;
    [cell.numBtn setTitle:[NSString stringWithFormat:@"%ld", indexPath.row + 1] forState:UIControlStateNormal];
    if (indexPath.row == 0) {
        cell.tagImage.image = [UIImage imageNamed:@"home_game_music_rank_tag_1"];
    } else if (indexPath.row == 1) {
        cell.tagImage.image = [UIImage imageNamed:@"home_game_music_rank_tag_2"];
    } else if (indexPath.row == 2) {
        cell.tagImage.image = [UIImage imageNamed:@"home_game_music_rank_tag_3"];
    } else {
        
        cell.tagImage.image = [UIImage new];
    }
    return cell;
}

@end

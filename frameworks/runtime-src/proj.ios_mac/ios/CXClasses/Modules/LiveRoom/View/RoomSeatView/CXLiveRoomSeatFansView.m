//
//  CXLiveRoomSeatFansView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/3.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomSeatFansView.h"
#import "CXLiveRoomSeatFansCell.h"

@interface CXLiveRoomSeatFansView() <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *nameBGImae;
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *usernameLabel;
@property (weak, nonatomic) IBOutlet UILabel *roseCountLable;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) NSMutableArray *dataSources;
@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger level;
@property (nonatomic, assign) NSInteger number;

@end

@implementation CXLiveRoomSeatFansView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.type = MMPopupTypeSheet;
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(kScreenWidth, 360 * SCALE_W));
    }];
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    
    _nameBGImae.layer.masksToBounds = YES;
    _nameBGImae.layer.cornerRadius = 19;
    _nameBGImae.image = [UIImage gradientImageWithSize:CGSizeMake(312, 38) Color1:UIColorHex(0xC72EA9) color2:UIColorHex(0x3F1D5F)];
    
    self.logo.layer.masksToBounds = YES;
    self.logo.layer.cornerRadius = 20;
    
    _dataSources = [NSMutableArray array];
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    _mainTableView.mj_footer.automaticallyChangeAlpha = YES;
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomSeatFansCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomSeatFansCellID"];
    
    _page = 0;
}

- (void)setMicroInfo:(LiveRoomMicroInfo *)microInfo {
    _microInfo = microInfo;
    
    [_logo sd_setImageWithURL:[NSURL URLWithString:microInfo.modelUser.HeadImageUrl]];
    _usernameLabel.text = [NSString stringWithFormat:@"%@的粉丝榜", microInfo.modelUser.Name];
    
    _level = microInfo.Type;
    _number = microInfo.Number;
    
    [self reloadRankList];
}


- (void)reloadRankList {
    kWeakSelf
    CXSocketMessageSeatsFansListRequest *request = [CXSocketMessageSeatsFansListRequest new];
    request.Page = self.page;
    request.Level = self.level;
    request.Number = self.number;
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageSeatsFansListRequest * _Nonnull request) {
        [weakSelf.mainTableView.mj_header endRefreshing];
        [weakSelf.mainTableView.mj_footer endRefreshing];
        if (request.response.isSuccess) {
            // 获取成功
            if (weakSelf.page == 0) {
                weakSelf.dataSources = [NSMutableArray arrayWithArray:request.response.MicroRankDatas];
            } else {
                [weakSelf.dataSources addObjectsFromArray:request.response.MicroRankDatas];
            }
            
            [weakSelf.mainTableView reloadData];
            NSString *str = [NSString stringWithFormat:@"本场共计玫瑰：%ld",request.response.AllRolse];
            NSMutableAttributedString *attri = [[NSMutableAttributedString alloc] initWithString:str];
            [attri addAttribute:NSForegroundColorAttributeName value:UIColorHex(0x333333) range:NSMakeRange(0, 4)];
            weakSelf.roseCountLable.attributedText = attri;
            
            if (weakSelf.page >= request.response.AllPage - 1) {
                [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
            }
        }
    }];
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
    CXLiveRoomSeatFansCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLiveRoomSeatFansCellID"];
    CXSocketMessageSeatsFansModel *model = self.dataSources[indexPath.row];
    cell.model = model;
    if (indexPath.row == 0) {
        [cell.rank_numBtn setTitle:@"" forState:UIControlStateNormal];
        [cell.rank_numBtn setImage:[[UIImage imageNamed:@"liveroom_seat_fans_first"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] forState:UIControlStateNormal];
    } else if (indexPath.row == 1) {
        [cell.rank_numBtn setTitle:@"" forState:UIControlStateNormal];
        [cell.rank_numBtn setImage:[[UIImage imageNamed:@"liveroom_seat_fans_second"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] forState:UIControlStateNormal];
    } else if (indexPath.row == 2) {
        [cell.rank_numBtn setTitle:@"" forState:UIControlStateNormal];
        [cell.rank_numBtn setImage:[[UIImage imageNamed:@"liveroom_seat_fans_third"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal] forState:UIControlStateNormal];
    } else {
        [cell.rank_numBtn setTitle:[NSString stringWithFormat:@"%02ld", (long)indexPath.row+1] forState:UIControlStateNormal];
        [cell.rank_numBtn setImage:nil forState:UIControlStateNormal];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    CXSocketMessageSeatsFansModel *model = self.dataSources[indexPath.row];
    if (self.didSelectedFansModel) {
        self.didSelectedFansModel(model);
    }
    
    [self hide];
}

@end

//
//  CXGameMusicReserveListCollectionCell.m
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicReserveListCollectionCell.h"
#import "CXGameMusicOrginMusicCell.h"
#import "CXGameMusicReserveMusicCell.h"
//#import "CXGameMusicChooseSongerView.h"
#import "CXLiveRoomShareHelpView.h"

@interface CXGameMusicReserveListCollectionCell() <UITableViewDataSource, UITableViewDelegate, UITextFieldDelegate>

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (weak, nonatomic) IBOutlet UIView *searchBgView;
@property (weak, nonatomic) IBOutlet UITextField *searchTextField;

@property (weak, nonatomic) IBOutlet UIButton *recommendBtn;
@property (weak, nonatomic) IBOutlet UIButton *onlineBtn;
@property (weak, nonatomic) IBOutlet UIButton *hotBtn;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *tableviewTopLayout;

@property (nonatomic, strong) NSMutableArray *dataSource;

@property (nonatomic, assign) NSInteger page;

@property (nonatomic, assign) NSInteger selectedTag;

//@property (nonatomic, strong) CXGameMusicChooseSongerView *chooseSongerView;

@end

@implementation CXGameMusicReserveListCollectionCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _searchBgView.layer.masksToBounds = YES;
    _searchBgView.layer.cornerRadius = 18;
    NSMutableAttributedString *attri = [[NSMutableAttributedString alloc] initWithString:@"搜索歌曲/歌手"];
    [attri addAttribute:NSForegroundColorAttributeName value:UIColorHex(0x999999) range:NSMakeRange(0, 7)];
    _searchTextField.attributedPlaceholder = attri;
    
    self.mainTableView.dataSource = self;
    self.mainTableView.delegate = self;
    
    self.mainTableView.estimatedRowHeight = 44;
    self.mainTableView.rowHeight = UITableViewAutomaticDimension;
    
    _mainTableView.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerRefresh)];
    _mainTableView.mj_footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerRefresh)];
    _mainTableView.mj_footer.automaticallyChangeAlpha = YES;
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXGameMusicOrginMusicCell" bundle:nil] forCellReuseIdentifier:@"CXGameMusicOrginMusicCellID"];
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXGameMusicReserveMusicCell" bundle:nil] forCellReuseIdentifier:@"CXGameMusicReserveMusicCellID"];
    
    [self getMusicListData];
    
    self.searchTextField.delegate = self;
//    self.searchTextField.layer.cornerRadius = 17;
    
    _recommendBtn.layer.masksToBounds = YES;
    _recommendBtn.layer.cornerRadius = 10;
    _recommendBtn.layer.borderWidth = 0.5;
    _recommendBtn.layer.borderColor = UIColorHex(0xEF51B1).CGColor;
    _onlineBtn.layer.masksToBounds = YES;
    _onlineBtn.layer.cornerRadius = 10;
    _onlineBtn.layer.borderWidth = 0.5;
    _onlineBtn.layer.borderColor = UIColorHex(0xEF51B1).CGColor;
    _hotBtn.layer.masksToBounds = YES;
    _hotBtn.layer.cornerRadius = 10;
    _hotBtn.layer.borderWidth = 0.5;
    _hotBtn.layer.borderColor = UIColorHex(0xEF51B1).CGColor;
}
- (IBAction)topBtnAction:(UIButton *)sender {
    _recommendBtn.backgroundColor = sender.tag == 10 ? UIColorHex(0xEF51B1) : UIColorHex(0x000000);
    _onlineBtn.backgroundColor = sender.tag == 20 ? UIColorHex(0xEF51B1) : UIColorHex(0x000000);
    _hotBtn.backgroundColor = sender.tag == 30 ? UIColorHex(0xEF51B1) : UIColorHex(0x000000);
    _selectedTag = sender.tag;
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    kWeakSelf
    if (_listType == Orgin) {
        CXSocketMessageMusicSearchList *request = [CXSocketMessageMusicSearchList new];
        request.SongMode = 1; // 1为原唱，2为伴揍
        request.SearchName = textField.text;
        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicSearchList * _Nonnull request) {
            [textField endEditing:YES];
            if (request.response.isSuccess) {
                weakSelf.dataSource = [NSMutableArray arrayWithArray:request.response.SongParams];
                [weakSelf.mainTableView reloadData];
            }
        }];
    } else if (_listType == Music) {
        CXSocketMessageMusicSearchList *request = [CXSocketMessageMusicSearchList new];
        request.SongMode = 2; // 1为原唱，2为伴揍
        request.SearchName = textField.text;
        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicSearchList * _Nonnull request) {
            [textField endEditing:YES];
            if (request.response.isSuccess) {
                weakSelf.dataSource = [NSMutableArray arrayWithArray:request.response.SongParams];
                [weakSelf.mainTableView reloadData];
            }
        }];
    }
    
    return YES;
}

- (void)setListType:(CXGameMusicReserveListType)listType {
    _listType = listType;
    _page = 0;
    _selectedTag = 10;
    self.searchTextField.text = @"";
    self.dataSource = [NSMutableArray array];
    [self.mainTableView reloadData];
    [self getMusicListData];
    if (_listType == Reserve) {
        self.tableviewTopLayout.constant = 0;
    } else {
        self.tableviewTopLayout.constant = 50;
    }
}

- (void)headerRefresh {
    self.page = 0;
    [self getMusicListData];
}

- (void)footerRefresh {
    self.page++;
    [self getMusicListData];
}

// 获取歌曲列表
- (void)getMusicListData {
    kWeakSelf
    if (_listType == Orgin) {
        CXSocketMessageMusicList *request = [CXSocketMessageMusicList new];
        request.SongMode = 1; // 1为原唱，2为伴揍
        request.Page = self.page;
        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicList * _Nonnull request) {
            if (request.response.isSuccess) {
                // 获取成功
                if (weakSelf.page == 0) {
                    weakSelf.dataSource = [NSMutableArray arrayWithArray:request.response.SongParams];
                } else {
                    [weakSelf.dataSource addObjectsFromArray:request.response.SongParams];
                }
                
                CXSocketMessageMusicModel *music = weakSelf.dataSource.firstObject;
                if (music) {
                    weakSelf.coinLabel.text = [NSString stringWithFormat:@"PS: 消耗玫瑰%ld朵/首", (long)music.Coin];
                }
                
                [weakSelf.mainTableView reloadData];
                
                [weakSelf.mainTableView.mj_header endRefreshing];
                [weakSelf.mainTableView.mj_footer endRefreshing];
                
                if (request.response.SongParams.count < 10) {
                    [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
                }
            }
        }];
    } else if (_listType == Music) {
        CXSocketMessageMusicList *request2 = [CXSocketMessageMusicList new];
        request2.SongMode = 2; // 1为原唱，2为伴奏
        request2.Page = self.page;
        [[CXClientModel instance] sendSocketRequest:request2 withCallback:^(CXSocketMessageMusicList * _Nonnull request) {
            if (request.response.isSuccess) {
                // 获取成功
                if (weakSelf.page == 0) {
                    weakSelf.dataSource = [NSMutableArray arrayWithArray:request.response.SongParams];
                } else {
                    [weakSelf.dataSource addObjectsFromArray:request.response.SongParams];
                }
                
                CXSocketMessageMusicModel *music = weakSelf.dataSource.firstObject;
                if (music) {
                    weakSelf.coinLabel.text = [NSString stringWithFormat:@"PS: 消耗玫瑰%ld朵/首", (long)music.Coin];
                }
                
                [weakSelf.mainTableView reloadData];
                
                [weakSelf.mainTableView.mj_header endRefreshing];
                [weakSelf.mainTableView.mj_footer endRefreshing];
                
                if (request.response.SongParams.count < 10) {
                    [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
                }
            }
        }];
    } else if (_listType == Reserve) {
        CXSocketMessageMusicGetReverseList *request = [CXSocketMessageMusicGetReverseList new];
        request.Page = self.page;
        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetReverseList * _Nonnull request) {
            if (request.response.isSuccess) {
                // 获取成功
                [CXClientModel instance].room.music_reverse_page = weakSelf.page;
                weakSelf.dataSource = [NSMutableArray arrayWithArray:[CXClientModel instance].room.music_reverseArrays];
                [weakSelf.mainTableView reloadData];
                
                [weakSelf.mainTableView.mj_header endRefreshing];
                [weakSelf.mainTableView.mj_footer endRefreshing];
                
                if (weakSelf.page >= [CXClientModel instance].room.music_reverse_allPage-1) {
                    [weakSelf.mainTableView.mj_footer endRefreshingWithNoMoreData];
                }
            }
        }];
    }
}

#pragma mark - <UITableViewDataSource, UITableViewDelegate>

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSource.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    kWeakSelf
    if (self.listType == Reserve) {
        CXGameMusicReserveMusicCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXGameMusicReserveMusicCellID"];
        CXSocketMessageMusicModel *music = self.dataSource[indexPath.row];
        cell.music_titleLabel.text = [NSString stringWithFormat:@"%ld. %@ - %@", indexPath.row+1, music.SongName, music.SingerName.length > 0 ? music.SingerName : @"佚名"];
        cell.music_dianboLabel.text = [NSString stringWithFormat:@"点播：%@", music.DemandUserName];
        if (music.ConsertUserName.length > 0) {
            cell.music_guistLabe.text = [NSString stringWithFormat:@"演唱嘉宾：%@", music.ConsertUserName];
        } else {
            cell.music_guistLabe.text = @"";
        }
        if ([CXClientModel instance].room.isHost == YES || [[CXClientModel instance].userId integerValue] == music.DemandUserId) {
            cell.isEnableDelete = YES;
        } else {
            cell.isEnableDelete = NO;
        }
        cell.rankLabel.text = [NSString stringWithFormat:@"%02ld", (long)indexPath.row+1];
        cell.reserveMusicActionBlock = ^(BOOL isTop, BOOL isDelete) {
            if (isTop) {
                CXSocketMessageMusicReverseTop *request = [CXSocketMessageMusicReverseTop new];
                request.Id = music.music_id;
                [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetReverseList * _Nonnull request) {
                    if (request.response.isSuccess) {
                        [weakSelf headerRefresh];
                    }
                }];
            }
            
            if (isDelete) {
                CXSocketMessageMusicReverseDelete *request = [CXSocketMessageMusicReverseDelete new];
                request.Id = music.music_id;
                [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetReverseList * _Nonnull request) {
                    if (request.response.isSuccess) {
                        [weakSelf getMusicListData];
                    }
                }];
            }
            
        };
        
        return cell;
    } else {
        CXGameMusicOrginMusicCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXGameMusicOrginMusicCellID"];
        CXSocketMessageMusicModel *music = self.dataSource[indexPath.row];
        cell.music_nameLabel.text = music.SongName;
        cell.music_songerLabel.text = music.SingerName.length > 0 ? music.SingerName : @"佚名";
        cell.reserveActionBlock = ^{

            if (weakSelf.listType == Music) {
                CXLiveRoomShareHelpView * view = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomShareHelpView" owner:self options:nil].firstObject;
                view.shareHelpType = MusicReserve;
                view.didSelectedUser = ^(LiveRoomUser * _Nonnull user) {
                    if (weakSelf.gameMusicReserveListBlock) {
                        weakSelf.gameMusicReserveListBlock(music, weakSelf.listType, user);
                    }
                };
                [view show];
            } else {
                if (weakSelf.gameMusicReserveListBlock) {
                    weakSelf.gameMusicReserveListBlock(music, weakSelf.listType, [LiveRoomUser new]);
                }
            }
        };
        return cell;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.listType == Reserve) {
        return 80;
    } else {
        return 60;
    }
}

@end

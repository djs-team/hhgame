//
//  CXGameMusicView.m
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicView.h"
#import "CXGameMusicPlayingCollectionCell.h"
#import "CXGameMusicReserveListCollectionCell.h"
#import "CXGameMusicAdjustView.h"
#import "LEEAlert.h"
#import "UIImage+GIF.h"

//#import "UIButton+CXTouch.h"
#import "CXGameMusicRankListCollectionCell.h"


@interface CXGameMusicView() <UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>
@property (weak, nonatomic) IBOutlet UIView *topBGView;

@property (weak, nonatomic) IBOutlet UIButton *hiddenButton;

@property (weak, nonatomic) IBOutlet UIButton *originBtn;
@property (weak, nonatomic) IBOutlet UIButton *musicBtn;
@property (weak, nonatomic) IBOutlet UIButton *playingBtn;
@property (weak, nonatomic) IBOutlet UIButton *listBtn;

@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;

@property (weak, nonatomic) IBOutlet UIView *emptyView;
@property (weak, nonatomic) IBOutlet UILabel *empty_title;
@property (weak, nonatomic) IBOutlet UIButton *empty_sureBtn;

@property (weak, nonatomic) IBOutlet UIView *no_musicView;

@property (strong, nonatomic) CXSocketMessageMusicModel * playing_NextSongInfo;
@property (strong, nonatomic) CXSocketMessageMusicModel * playing_SongInfo;

@property (nonatomic, strong) CXGameMusicAdjustView *adjustView;

// 能否点击按钮
@property (nonatomic, assign) BOOL isEnableClickBtn;

@end

@implementation CXGameMusicView

- (CXGameMusicAdjustView *)adjustView {
    if (!_adjustView) {
        _adjustView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicAdjustView" owner:self options:nil].firstObject;
        _adjustView.frame = CGRectMake(0, 0, kScreenWidth, kScreenHeight);
    }
    
    return _adjustView;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
//    _hidenButton.layer.masksToBounds = YES;
//    _hidenButton.layer.cornerRadius = 10;
//    _hidenButton.layer.borderColor = [UIColor whiteColor].CGColor;
//    _hidenButton.layer.borderWidth = 1;
    
    self.mainCollectionView.dataSource = self;
    self.mainCollectionView.delegate = self;
    self.mainCollectionView.scrollEnabled = false;
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXGameMusicPlayingCollectionCell" bundle:nil] forCellWithReuseIdentifier:@"CXGameMusicPlayingCollectionCellID"];
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXGameMusicReserveListCollectionCell" bundle:nil] forCellWithReuseIdentifier:@"CXGameMusicReserveListCollectionCellID"];
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXGameMusicRankListCollectionCell" bundle:nil] forCellWithReuseIdentifier:@"CXGameMusicRankListCollectionCellID"];
    
    self.emptyView.hidden = YES;
    self.no_musicView.hidden = YES;
    _empty_sureBtn.layer.cornerRadius = 20;
    _empty_sureBtn.layer.masksToBounds = YES;
    _isEnableClickBtn = YES;
    
    _hiddenButton.layer.masksToBounds = YES;
    _hiddenButton.layer.cornerRadius = 12;
    _hiddenButton.layer.borderColor = UIColorHex(0xEF51B1).CGColor;
    _hiddenButton.layer.borderWidth = 1;

}

- (void)layoutSubviews {
    [super layoutSubviews];
    
//    [self setupGradientLayer];
}

//- (void)setupGradientLayer {
//
//    [self.topBGView az_setGradientBackgroundWithColors:@[UIColorHex(0x9351FE),UIColorHex(0x4B7CFF)] locations:@[@0,@1] startPoint:CGPointMake(0, 0) endPoint:CGPointMake(1, 0)];
//    self.topBGView.layer.cornerRadius = 10;
//}

- (void)reloadMusicView {
    kWeakSelf
    if ([CXClientModel instance].room.isHost) {
      
        CXSocketMessageMusicPlayingViewDetail *request2 = [CXSocketMessageMusicPlayingViewDetail new];
        [[CXClientModel instance] sendSocketRequest:request2 withCallback:^(CXSocketMessageMusicPlayingViewDetail * _Nonnull request) {
            if (request.response.isSuccess) {
                
                [CXClientModel instance].room.music_Volume = request.response.Volume;
                if (request.response.IsPause == 0) { // 未播放
                    weakSelf.musicPlayStatus = music_unplay;
                } else if (request.response.IsPause == 1) { // 暂停
                    weakSelf.musicPlayStatus = music_pause;
                } else if (request.response.IsPause == 2) { // 恢复
                    if (weakSelf.musicPlayStatus == music_resume) {
                        weakSelf.musicPlayStatus = music_playing;
                    } else if (weakSelf.musicPlayStatus == music_playing) {
                        weakSelf.musicPlayStatus = music_playing;
                    } else {
                        weakSelf.musicPlayStatus = music_unplay;
                    }
                }
            }
        }];
    }
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:2 inSection:0];
    [self.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
}

- (void)getPlayingData {
    kWeakSelf
    CXSocketMessageMusicGetPlayingDetail *request = [CXSocketMessageMusicGetPlayingDetail new];
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
        if (request.response.isSuccess) {
            NSIndexPath *indexPath = [NSIndexPath indexPathForRow:2 inSection:0];
            [weakSelf.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
        }
    }];
           
    if ([CXClientModel instance].room.isHost) {
      
        CXSocketMessageMusicPlayingViewDetail *request2 = [CXSocketMessageMusicPlayingViewDetail new];
        [[CXClientModel instance] sendSocketRequest:request2 withCallback:^(CXSocketMessageMusicPlayingViewDetail * _Nonnull request) {
            if (request.response.isSuccess) {
                
                [CXClientModel instance].room.music_Volume = request.response.Volume;
                if (request.response.IsPause == 0) { // 未播放
                    weakSelf.musicPlayStatus = music_unplay;
                } else if (request.response.IsPause == 1) { // 暂停
                    weakSelf.musicPlayStatus = music_pause;
                } else if (request.response.IsPause == 2) { // 恢复
                    if (weakSelf.musicPlayStatus == music_resume) {
                        weakSelf.musicPlayStatus = music_playing;
                    } else if (weakSelf.musicPlayStatus == music_playing) {
                        weakSelf.musicPlayStatus = music_playing;
                    } else {
                        weakSelf.musicPlayStatus = music_unplay;
                    }
                    
                }
            }
        }];
    }
        
}

- (void)setRoom:(CXLiveRoomModel *)room {
    _room = room;
    
    [self getPlayingData];
}

- (void)setMusicPlayStatus:(CXGameMusicPlayStatus)musicPlayStatus {
    if ([CXClientModel instance].room.isHost == NO) {
        return;
    }
    _musicPlayStatus = musicPlayStatus;
    
    CXGameMusicPlayingCollectionCell *cell = (CXGameMusicPlayingCollectionCell *)[_mainCollectionView cellForItemAtIndexPath:[NSIndexPath indexPathForRow:2 inSection:0]];
    switch (musicPlayStatus) {
        case music_unplay:
            [cell.playBtn setImage:[UIImage imageNamed:@"home_game_music_play"] forState:UIControlStateNormal];
            break;
         case music_playing:
            [cell.playBtn setImage:[UIImage imageNamed:@"home_game_music_pause"] forState:UIControlStateNormal];
            break;
        case music_pause:
            [cell.playBtn setImage:[UIImage imageNamed:@"home_game_music_play"] forState:UIControlStateNormal];
            break;
        case music_resume:
            [cell.playBtn setImage:[UIImage imageNamed:@"home_game_music_pause"] forState:UIControlStateNormal];
            break;
        case music_loading: {
//            NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"home_game_music_loading" ofType:@"gif"];
//            NSData *imageData = [NSData dataWithContentsOfFile:imagePath];
//            UIImage *image = [UIImage sd_imageWithGIFData:imageData];
//            [cell.playBtn setImage:image forState:UIControlStateNormal];
        }
            break;
        case music_downing: {
//           NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"home_game_music_download" ofType:@"gif"];
//           NSData *imageData = [NSData dataWithContentsOfFile:imagePath];
//           UIImage *image = [UIImage sd_imageWithGIFData:imageData];
//           [cell.playBtn setImage:image forState:UIControlStateNormal];
       }
           break;
        case music_downfail: {
//           NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"home_game_music_downfail" ofType:@"gif"];
//           NSData *imageData = [NSData dataWithContentsOfFile:imagePath];
//           UIImage *image = [UIImage sd_imageWithGIFData:imageData];
//           [cell.playBtn setImage:image forState:UIControlStateNormal];
       }
           break;
        default:
            break;
    }
    
}

- (void)setPro_volum:(NSInteger)pro_volum {
    _pro_volum = pro_volum;
    
    CXGameMusicPlayingCollectionCell *cell = (CXGameMusicPlayingCollectionCell *)[_mainCollectionView cellForItemAtIndexPath:[NSIndexPath indexPathForRow:2 inSection:0]];
    cell.pro_volume.value = _pro_volum;
    cell.pro_volumLabel.text = [NSString stringWithFormat:@"%ld", (long)_pro_volum];
}

- (void)setPro_currentTime:(NSInteger)pro_currentTime {
    _pro_currentTime = pro_currentTime;
    CXGameMusicPlayingCollectionCell *cell = (CXGameMusicPlayingCollectionCell *)[_mainCollectionView cellForItemAtIndexPath:[NSIndexPath indexPathForRow:2 inSection:0]];
    cell.pro_curTimeLabel.text = [self getMMSSFromSS:_pro_currentTime];
    cell.pro_progressView.progress = (_pro_currentTime*0.1) / (_pro_endTime * 0.1);
    cell.pro_allTimeLabel.text = [self getMMSSFromSS:_pro_endTime];
}

#pragma mark - UICollectionViewDataSource, UICollectionViewDelegateFlowLayout
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return 5;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    kWeakSelf
    self.emptyView.hidden = YES;
    self.no_musicView.hidden = YES;
    if (indexPath.row == 2) {
        CXGameMusicPlayingCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXGameMusicPlayingCollectionCellID" forIndexPath:indexPath];

        if ([CXClientModel instance].room.playing_SongInfo.SongName.length > 0) {
            cell.playing_NextSongInfo = [CXClientModel instance].room.playing_NextSongInfo;
            cell.playing_SongInfo = [CXClientModel instance].room.playing_SongInfo;
        } else {
//            [self showEmptyMessage:@"暂无播放歌曲"];
            self.no_musicView.hidden = NO;
        }
        cell.playingACtionBlock = ^(BOOL isReplay, BOOL isPlay, BOOL isNext) {
            if (isPlay) {
                switch (weakSelf.musicPlayStatus) {
                    case music_unplay: { // 未播放-播放
                        weakSelf.musicPlayStatus = music_loading;
                        CXSocketMessageMusicPlay *request = [CXSocketMessageMusicPlay new];
                        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicPlay * _Nonnull request) {
                            if (request.response.isSuccess) {
                                if ([CXClientModel instance].room.playing_SongInfo.SongMode == 1) {
                                    if (weakSelf.musicViewActionBlock) {
                                        weakSelf.musicViewActionBlock(music_action_play);
                                    }
                                }
                            } else {
                                [weakSelf playError:[request.response.Success integerValue]];
                            }
                        }];
                    }
                        break;
                    case music_playing: { // 播放-暂停
                        CXSocketMessageMusicPause *request = [CXSocketMessageMusicPause new];
                        request.IsPause = YES;
                        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
                            if (request.response.isSuccess) {
                                weakSelf.musicPlayStatus = music_pause;
                                if ([CXClientModel instance].room.playing_SongInfo.SongMode == 1) {
                                    if (weakSelf.musicViewActionBlock) {
                                        weakSelf.musicViewActionBlock(music_action_pause);
                                    }
                                }
                            }
                        }];
                    }
                        break;
                    case music_resume: { // 恢复-暂停
                        CXSocketMessageMusicPause *request = [CXSocketMessageMusicPause new];
                        request.IsPause = YES;
                        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
                            if (request.response.isSuccess) {
                                weakSelf.musicPlayStatus = music_pause;
                                if ([CXClientModel instance].room.playing_SongInfo.SongMode == 1) {
                                    if (weakSelf.musicViewActionBlock) {
                                        weakSelf.musicViewActionBlock(music_action_pause);
                                    }
                                }
                            }
                        }];
                    }
                        break;
                    case music_pause: { // 暂停-恢复播放
                        CXSocketMessageMusicPause *request = [CXSocketMessageMusicPause new];
                        request.IsPause = NO;
                        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
                            if (request.response.isSuccess) {
                                weakSelf.musicPlayStatus = music_resume;
                                if ([CXClientModel instance].room.playing_SongInfo.SongMode == 1) {
                                    if (weakSelf.musicViewActionBlock) {
                                        weakSelf.musicViewActionBlock(music_action_resume);
                                    }
                                }
                            }
                        }];
                    }
                        break;
                    case music_downfail: { // 下载失败-重新下载
                        weakSelf.musicPlayStatus = music_loading;
                        CXSocketMessageMusicPlay *request = [CXSocketMessageMusicPlay new];
                        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicPlay * _Nonnull request) {
                            if (request.response.isSuccess) {
                                if ([CXClientModel instance].room.playing_SongInfo.SongMode == 1) {
                                    if (weakSelf.musicViewActionBlock) {
                                        weakSelf.musicViewActionBlock(music_action_play);
                                    }
                                }
                            } else {
                                [weakSelf playError:[request.response.Success integerValue]];
                            }
                        }];
                    }
                        break;
                    default: {
                        weakSelf.musicPlayStatus = music_loading;
                        CXSocketMessageMusicPlay *request = [CXSocketMessageMusicPlay new];
                        [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicPlay * _Nonnull request) {
                            if (request.response.isSuccess) {
                                if ([CXClientModel instance].room.playing_SongInfo.SongMode == 1) {
                                    if (weakSelf.musicViewActionBlock) {
                                        weakSelf.musicViewActionBlock(music_action_play);
                                    }
                                }
                            } else {
                                [weakSelf playError:[request.response.Success integerValue]];
                            }
                        }];
                    }
                        break;
                }
            }
            
            if (isNext) {
                if (weakSelf.musicViewActionBlock) {
                    weakSelf.musicViewActionBlock(music_action_next);
                }
//                CXSocketMessageMusicNext *request = [CXSocketMessageMusicNext new];
//                [AppDelegate.shared.client sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
//                    if (request.response.isSuccess) {
//                        weakSelf.musicPlayStatus = music_loading;
//                        CXSocketMessageMusicPlay *request = [CXSocketMessageMusicPlay new];
//                        [AppDelegate.shared.client sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
//                            if (request.response.isSuccess) {
//                                if ([ModelClient instance].room.playing_SongInfo.SongMode == 1) {
//                                    if (weakSelf.musicViewActionBlock) {
//                                        weakSelf.musicViewActionBlock(music_action_play);
//                                    }
//                                }
//                            } else {
//                                [weakSelf playError:[request.response.Success integerValue]];
//                                [weakSelf.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
//                            }
//                        }];
//                    } else {
//
//                        [weakSelf playError:[request.response.Success integerValue]];
//                    }
//
//                    [weakSelf.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
//                }];
            }
            
            if (isReplay) {
                CXSocketMessageMusicReplay *request = [CXSocketMessageMusicReplay new];
                [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
                    if (request.response.isSuccess) {
                        weakSelf.musicPlayStatus = music_unplay;
                        if (weakSelf.musicViewActionBlock) {
                            weakSelf.musicViewActionBlock(music_action_replay);
                        }
                    } else {
                        
                        [weakSelf playError:[request.response.Success integerValue]];
                    }
                }];
            }
        };
        
        cell.playingChangeVolumBlock = ^(float volum) {
            [[CXClientModel instance].agoraEngineManager.engine adjustAudioMixingVolume:volum];
            [CXClientModel instance].room.music_Volume = volum;
            weakSelf.pro_volum = volum;
            
//            CXSocketMessageMusicChangeVolum *request = [CXSocketMessageMusicChangeVolum new];
//            request.Volume = volum;
//            [AppDelegate.shared.client sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetPlayingDetail * _Nonnull request) {
//                if (request.response.isSuccess) {
////                    [[ModelClient instance].agoraEngineManager.engine adjustAudioMixingVolume:volum];
////                    [ModelClient instance].room.music_Volume = volum;
////                    weakSelf.pro_volum = volum;
//                } else {
//                    [weakSelf playError:[request.response.Success integerValue]];
//                }
//
//               [weakSelf.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
//            }];
        };
        
        cell.adjustBlock = ^{
            [[UIApplication sharedApplication].keyWindow addSubview:weakSelf.adjustView];
        };
        return cell;
    } else if (indexPath.row == 4) { // 排行榜
        CXGameMusicRankListCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXGameMusicRankListCollectionCellID" forIndexPath:indexPath];
        [cell reloadRankList];
        return cell;
        
    } else {
        CXGameMusicReserveListCollectionCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXGameMusicReserveListCollectionCellID" forIndexPath:indexPath];
        
        cell.room = self.room;
        
        if (indexPath.row == 0) {
            cell.listType = Orgin;
        } else if (indexPath.row == 1) {
            cell.listType = Music;
        } else {
            cell.listType = Reserve;
        }
        cell.gameMusicReserveListBlock = ^(CXSocketMessageMusicModel * _Nonnull model, CXGameMusicReserveListType listType, LiveRoomUser * _Nonnull user) {
            if (listType == Music) {
                CXSocketMessageMusicReverse *request = [CXSocketMessageMusicReverse new];
                request.SongId = [model.music_id integerValue];
                NSIndexPath *indexPath = [[CXClientModel instance].room.userSeats objectForKey:user.UserId];
                request.Level = indexPath.section;
                request.Number = indexPath.row;
                [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetReverseList * _Nonnull request) {
                    if (request.response.isSuccess) {
                        [weakSelf showEmptyMessage:@"歌曲预约成功"];
                    } else {
                        [weakSelf playError:[request.response.Success integerValue]];
                    }
                }];
            } else {
                CXSocketMessageMusicReverse *request = [CXSocketMessageMusicReverse new];
                request.SongId = [model.music_id integerValue];
                [[CXClientModel instance] sendSocketRequest:request withCallback:^(CXSocketMessageMusicGetReverseList * _Nonnull request) {
                    if (request.response.isSuccess) {
                        [weakSelf showEmptyMessage:@"歌曲预约成功"];
                    } else {
                        [weakSelf playError:[request.response.Success integerValue]];
                    }
                }];
            }
        };

        return cell;
    }
}

- (void)playError:(NSInteger)code {
    // 5余额不足是 4为找不到歌曲信息，2为找不到麦位信息，3为麦上没有用户
    self.musicPlayStatus = music_unplay;
    switch (code) {
        case 5:
            [self showEmptyMessage:@"点歌人玫瑰不足，歌曲被取消"];
            break;
        case 4:
            [self showEmptyMessage:@"歌曲信息缺失，歌曲被取消"];
            break;
        case 2:
            [self showEmptyMessage:@"指定嘉宾已经不在麦上，歌曲被取消"];
            break;
        case 3:
            [self showEmptyMessage:@"当前没有可播放的歌"];
            break;
        case 6:
            [self showEmptyMessage:@"预约列表有重复歌曲"];
            break;
        case 7:
            [self showEmptyMessage:@"超过最大预约歌曲"];
            break;
        default:
            break;
    }
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(kScreenWidth, 360);
}

- (IBAction)closeAction:(id)sender {
    [self removeFromSuperview];
}

- (IBAction)orginMusicAction:(UIButton *)sender {
//    if (_isEnableClickBtn == NO) {
//        return;
//    }
    [_originBtn setTitleColor:UIColorHex(0xFFFFFF) forState:UIControlStateNormal];
    [_musicBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_playingBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_listBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
//    [_rankButton setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:0 inSection:0];
    [self.mainCollectionView scrollToItemAtIndexPath:indexPath atScrollPosition:UICollectionViewScrollPositionNone animated:false];
    [self.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
    
    [self endEditing:YES];
}

- (IBAction)panzouMusicAction:(UIButton *)sender {
//    if (_isEnableClickBtn == NO) {
//        return;
//    }
    [_originBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_musicBtn setTitleColor:UIColorHex(0xFFFFFF) forState:UIControlStateNormal];
    [_playingBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_listBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
//    [_rankButton setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:1 inSection:0];
    [self.mainCollectionView scrollToItemAtIndexPath:indexPath atScrollPosition:UICollectionViewScrollPositionNone animated:false];
    [self.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
    
    [self endEditing:YES];
}

- (IBAction)playingAction:(UIButton *)sender {
//    if (_isEnableClickBtn == NO) {
//        return;
//    }
    [_originBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_musicBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_playingBtn setTitleColor:UIColorHex(0xFFFFFF) forState:UIControlStateNormal];
    [_listBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
//    [_rankButton setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:2 inSection:0];
    [self.mainCollectionView scrollToItemAtIndexPath:indexPath atScrollPosition:UICollectionViewScrollPositionNone animated:false];
    
    [self endEditing:YES];
}

- (IBAction)listAction:(UIButton *)sender {
//    if (_isEnableClickBtn == NO) {
//        return;
//    }
    [_originBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_musicBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_playingBtn setTitleColor:UIColorHex(0x999999) forState:UIControlStateNormal];
    [_listBtn setTitleColor:UIColorHex(0xFFFFFF) forState:UIControlStateNormal];
//    [_rankButton setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
    
    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:3 inSection:0];
    [self.mainCollectionView scrollToItemAtIndexPath:indexPath atScrollPosition:UICollectionViewScrollPositionNone animated:false];
    [self.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
    
    [self endEditing:YES];
}

- (IBAction)rankAction:(UIButton *)sender {
//    [_originBtn setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
//    [_musicBtn setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
//    [_playingBtn setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
//    [_listBtn setTitleColor:UIColorHex(0xB2CAFE) forState:UIControlStateNormal];
//    [_rankButton setTitleColor:UIColorHex(0xFFFFFF) forState:UIControlStateNormal];
//    
//    NSIndexPath *indexPath = [NSIndexPath indexPathForRow:4 inSection:0];
//    [self.mainCollectionView scrollToItemAtIndexPath:indexPath atScrollPosition:UICollectionViewScrollPositionNone animated:false];
//    [self.mainCollectionView reloadItemsAtIndexPaths:@[indexPath]];
//    
//    [self endEditing:YES];
}

#pragma mark - Empty
- (void)showEmptyMessage:(NSString *)message {
    self.emptyView.hidden = false;
    
    self.empty_title.text = message;
}
- (IBAction)empty_sureAction:(id)sender {
    if ([CXClientModel instance].room.playing_SongInfo.SongName.length > 0) {
        self.emptyView.hidden = true;
    } else {
        self.emptyView.hidden = true;
//        [self closeAction:nil];
        [self orginMusicAction:self.originBtn];
    }
    
}

- (NSString *)getMMSSFromSS:(NSInteger)totalTime{
    
    //format of minute
    NSString *str_minute = [NSString stringWithFormat:@"%02ld",totalTime/60];
    //format of second
    NSString *str_second = [NSString stringWithFormat:@"%02ld",totalTime%60];
    //format of time
    NSString *format_time = [NSString stringWithFormat:@"%@:%@",str_minute,str_second];
    
    return format_time;
    
}

@end

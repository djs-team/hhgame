//
//  CXGameMusicPlayingCollectionCell.h
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicPlayingCollectionCell : UICollectionViewCell

@property (strong, nonatomic) CXSocketMessageMusicModel * playing_NextSongInfo;
@property (strong, nonatomic) CXSocketMessageMusicModel * playing_SongInfo;

@property (weak, nonatomic) IBOutlet UISlider *pro_volume;
@property (weak, nonatomic) IBOutlet UILabel *pro_volumLabel;
@property (weak, nonatomic) IBOutlet UIProgressView *pro_progressView;
@property (weak, nonatomic) IBOutlet UILabel *pro_curTimeLabel;
@property (weak, nonatomic) IBOutlet UILabel *pro_allTimeLabel;

@property (weak, nonatomic) IBOutlet UIButton *playBtn;
//@property (weak, nonatomic) IBOutlet UIButton *adjustBtn;

@property (nonatomic, copy) void (^playingACtionBlock)(BOOL isPlay, BOOL isNext, BOOL isReplay);
@property (nonatomic, copy) void (^playingChangeVolumBlock)(float volum);
@property (nonatomic, copy) void (^adjustBlock)(void);

@end

NS_ASSUME_NONNULL_END

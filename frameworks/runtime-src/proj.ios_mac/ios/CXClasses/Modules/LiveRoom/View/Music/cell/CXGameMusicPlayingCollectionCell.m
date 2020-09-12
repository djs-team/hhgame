//
//  CXGameMusicPlayingCollectionCell.m
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicPlayingCollectionCell.h"

@interface CXGameMusicPlayingCollectionCell()
@property (weak, nonatomic) IBOutlet UILabel *cur_nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *cur_originNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *cur_changNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *cur_dianNameLabel;

@property (weak, nonatomic) IBOutlet UIView *action_bgView;

//@property (weak, nonatomic) IBOutlet UIView *next_bgView;
//@property (weak, nonatomic) IBOutlet UILabel *next_nameLabel;
//@property (weak, nonatomic) IBOutlet UILabel *next_dianNameLabel;
//@property (weak, nonatomic) IBOutlet UILabel *next_changeNameLabel;


@end

@implementation CXGameMusicPlayingCollectionCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
//    _pro_bgView.layer.masksToBounds = YES;
//    _pro_bgView.layer.cornerRadius = 4;
//    _pro_bgView.layer.borderColor = UIColorHex(0xE3E3FF).CGColor;
//    _pro_bgView.layer.borderWidth = 1;
    
    [self.pro_volume setThumbImage:[UIImage imageNamed:@"home_game_music_track"] forState:UIControlStateNormal];
    [_pro_volume addTarget:self action:@selector(sliderValueChange:) forControlEvents:UIControlEventTouchUpInside];
}

//- (void)layoutSubviews {
//    [super layoutSubviews];
//
//    _next_bgView.layer.cornerRadius = 4;
//    _next_bgView.layer.shadowColor = [UIColor colorWithRed:4/255.0 green:0/255.0 blue:0/255.0 alpha:0.08].CGColor;
//    _next_bgView.layer.shadowOffset = CGSizeMake(0,1);
//    _next_bgView.layer.shadowOpacity = 1;
//    _next_bgView.layer.shadowRadius = 9;
//
//    _pro_bgView.layer.cornerRadius = 4;
//    _pro_bgView.layer.shadowColor = [UIColor colorWithRed:4/255.0 green:0/255.0 blue:0/255.0 alpha:0.08].CGColor;
//    _pro_bgView.layer.shadowOffset = CGSizeMake(0,1);
//    _pro_bgView.layer.shadowOpacity = 1;
//    _pro_bgView.layer.shadowRadius = 9;
//
//}

- (void)sliderValueChange:(UISlider *)sender {
    if ([CXClientModel instance].room.isSonger == YES || [CXClientModel instance].room.isHost == YES) {
        if (self.playingChangeVolumBlock) {
            self.playingChangeVolumBlock(sender.value);
        }
    }
}

- (void)setPlaying_SongInfo:(CXSocketMessageMusicModel *)playing_SongInfo {
    _playing_SongInfo = playing_SongInfo;
    
    self.cur_nameLabel.text = _playing_SongInfo.SongName;
    self.cur_originNameLabel.text = _playing_SongInfo.OriginalSingerName;
    self.cur_dianNameLabel.text = [NSString stringWithFormat:@"点播：%@", _playing_SongInfo.DemandUserName];
    if (_playing_SongInfo.ConsertUserId > 0) {
        self.cur_changNameLabel.text = [NSString stringWithFormat:@"演唱上麦嘉宾：%@", _playing_SongInfo.ConsertUserName];
    } else {
        self.cur_changNameLabel.text = @"";
    }
    
    if ([CXClientModel instance].room.isConsertModel == YES) { // 伴唱模式
        if ([CXClientModel instance].room.isHost == YES) {
            if ([CXClientModel instance].room.isSonger == YES) {
                self.action_bgView.hidden = NO;
            } else {
                self.action_bgView.hidden = NO;
            }
            
        } else {
            if ([CXClientModel instance].room.isSonger == YES) {
                self.action_bgView.hidden = YES;
            } else {
                self.action_bgView.hidden = YES;
            }
        }
    } else {
        if ([CXClientModel instance].room.isHost == YES) {
            self.action_bgView.hidden = NO;
        } else {
            self.action_bgView.hidden = YES;
        }
    }
}

- (void)setPlaying_NextSongInfo:(CXSocketMessageMusicModel *)playing_NextSongInfo {
//    if (!playing_NextSongInfo) {
//        self.next_bgView.hidden = YES;
//        return;
//    }
//    self.next_bgView.hidden = NO;
//    _playing_NextSongInfo = playing_NextSongInfo;
//
//    self.next_nameLabel.text = playing_NextSongInfo.SongName;
//    if (playing_NextSongInfo.DemandUserName.length > 0) {
//        self.next_dianNameLabel.text = [NSString stringWithFormat:@"点播：%@", playing_NextSongInfo.DemandUserName];
//    } else {
//        self.next_dianNameLabel.text = @"";
//    }
//
//    if (playing_NextSongInfo.ConsertUserName.length > 0) {
//        self.next_changeNameLabel.text = [NSString stringWithFormat:@"演唱上麦嘉宾：%@", playing_NextSongInfo.ConsertUserName];
//    } else {
//        self.next_changeNameLabel.text = @"";
//    }
}

- (IBAction)replayAction:(id)sender {
    if ([CXClientModel instance].room.isHost == YES) {
        if (self.playingACtionBlock) {
            self.playingACtionBlock(YES, NO, NO);
        }
    }
}

- (IBAction)playAction:(id)sender {
    if ([CXClientModel instance].room.isHost == YES) {
        if (self.playingACtionBlock) {
            self.playingACtionBlock(NO, YES, NO);
        }
    }
}

- (IBAction)nextAction:(id)sender {
    if ([CXClientModel instance].room.isHost == YES) {
        if (self.playingACtionBlock) {
            self.playingACtionBlock(NO, NO, YES);
        }
    }
}

- (IBAction)adjustAction:(id)sender {
    if (self.adjustBlock) {
        self.adjustBlock();
    }
}

@end

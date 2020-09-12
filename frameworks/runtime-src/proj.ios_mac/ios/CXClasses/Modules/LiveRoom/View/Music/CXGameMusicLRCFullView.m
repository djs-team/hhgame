//
//  CXGameMusicLRCFullView.m
//  hairBall
//
//  Created by mahong yang on 2020/5/18.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicLRCFullView.h"

@interface CXGameMusicLRCFullView() <UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel *music_titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *music_descLabel;
@property (weak, nonatomic) IBOutlet UILabel *music_minLabel;
@property (weak, nonatomic) IBOutlet UILabel *music_maxLabel;
@property (weak, nonatomic) IBOutlet UIProgressView *music_progress;
@property (weak, nonatomic) IBOutlet UITableView *mainTableView;
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIImageView *bgImage;


@end

@implementation CXGameMusicLRCFullView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.mainTableView.dataSource = self;
    self.bgView.layer.masksToBounds = YES;
    self.bgView.layer.cornerRadius = 10;
    
    self.bgImage.image = [UIImage y_gradientImageWithSize:CGSizeMake(SCREEN_WIDTH, 350) Color1:UIColorHex(0x923483) color2:UIColorHex(0x2C185D)];
}

- (void)setLrcModel:(DDAudioLRC *)lrcModel {
    _lrcModel = lrcModel;
    
    self.music_titleLabel.text = [CXClientModel instance].room.playing_SongInfo.SongName;
    self.music_descLabel.text = [CXClientModel instance].room.playing_SongInfo.SingerName;
    
    _currentShowRow = -1;
    [self.mainTableView reloadData];
}

- (void)setPro_currentTime:(NSInteger)pro_currentTime {
    _pro_currentTime = pro_currentTime;
    self.music_minLabel.text = [self getMMSSFromSS:_pro_currentTime];
    self.music_progress.progress = (_pro_currentTime*0.1) / (_pro_endTime * 0.1);
    self.music_maxLabel.text = [self getMMSSFromSS:_pro_endTime];
}

- (void)setCurrentShowRow:(NSInteger)currentShowRow {
    if (_currentShowRow == currentShowRow) {
        return;
    }
    _currentShowRow = currentShowRow;
    [self.mainTableView reloadData];
    [self.mainTableView scrollToRowAtIndexPath:[NSIndexPath indexPathForRow:currentShowRow inSection:0] atScrollPosition:UITableViewScrollPositionMiddle animated:YES];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.lrcModel.units.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"cellID"];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"cellID"];
        cell.selectionStyle = UITableViewCellSelectionStyleNone;
        cell.backgroundColor = [UIColor clearColor];
    }
    
    DDAudioLRCUnit *unit = _lrcModel.units[indexPath.row];
    cell.textLabel.text = unit.lrc;
    if(indexPath.row==_currentShowRow) {
        cell.textLabel.textColor =  UIColorHex(0xFFE403);
        cell.textLabel.font = [UIFont boldSystemFontOfSize:12];
    } else {
        cell.textLabel.textColor =  UIColorHex(0xFFFFFF);
        cell.textLabel.font = [UIFont boldSystemFontOfSize:12];
    }
    cell.textLabel.textAlignment = NSTextAlignmentCenter;
    
    return cell;
}

- (IBAction)closeAction:(id)sender {
    if (self.musicLRCFullViewCloseBlock) {
        self.musicLRCFullViewCloseBlock();
    }
    [self removeFromSuperview];
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

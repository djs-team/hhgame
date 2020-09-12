//
//  CXGameMusicAdjustRateCell.m
//  hairBall
//
//  Created by mahong yang on 2020/2/19.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicAdjustRateCell.h"

@interface CXGameMusicAdjustRateCell()

@property (weak, nonatomic) IBOutlet UILabel *item_title;
@property (nonatomic, strong) UISlider *toneSlider;

@end


@implementation CXGameMusicAdjustRateCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _toneSlider = [[UISlider alloc] initWithFrame: CGRectZero];
    [self addSubview:_toneSlider];
    [_toneSlider mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerX.mas_equalTo(0);
        make.centerY.mas_equalTo(30);
        make.width.mas_equalTo(230);
        make.height.mas_equalTo(30);
    }];
    _toneSlider.transform = CGAffineTransformMakeRotation(M_PI_2);
    _toneSlider.minimumValue = -15;
    _toneSlider.maximumValue = 15;
    _toneSlider.value = 0;
    _toneSlider.tintColor = UIColorHex(0xA933CA);
    [_toneSlider setThumbImage:[UIImage imageNamed:@"home_game_music_track_blue"] forState:UIControlStateNormal];
    [_toneSlider addTarget:self action:@selector(sliderValueChange:) forControlEvents:UIControlEventTouchUpInside];
}

- (void)setDict:(NSDictionary *)dict {
    _dict = dict;
    self.item_title.text = dict[@"title"];
    self.toneSlider.minimumValue = [dict[@"min"] doubleValue];
    self.toneSlider.maximumValue = [dict[@"max"] doubleValue];
}

- (void)setCurrentValue:(double)currentValue {
    _currentValue = currentValue;
    _toneSlider.value = currentValue;
}

- (void)sliderValueChange:(UISlider *)sender {
    if (self.adjustRateBlock) {
        self.adjustRateBlock(sender.value);
    }
}


@end

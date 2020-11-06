//
//  CXRommMusicLRCShowView.m
//  hairBall
//
//  Created by mahong yang on 2020/3/18.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXRommMusicLRCShowView.h"
#import "UIButton+CXCategory.h"

@interface CXRommMusicLRCShowView()
@property (weak, nonatomic) IBOutlet UIView *bgView;

@end

@implementation CXRommMusicLRCShowView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _bgView.layer.masksToBounds = true;
    _bgView.layer.cornerRadius = 27;
    
    _lrc_music_roseNumBtn.layer.masksToBounds = true;
    _lrc_music_roseNumBtn.layer.cornerRadius = 17;
}

- (IBAction)showFullLRCAction:(id)sender {
    if (self.showMusicLRCFullBlock) {
        self.showMusicLRCFullBlock();
    }
}

- (IBAction)sendGiftRedRoseAction:(id)sender {
    if (self.sendGiftRedRoseBlock) {
        self.sendGiftRedRoseBlock();
    }
}

@end


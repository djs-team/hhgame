//
//  CXRommMusicLRCShowView.h
//  hairBall
//
//  Created by mahong yang on 2020/3/18.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXRommMusicLRCShowView : UIView
@property (weak, nonatomic) IBOutlet UILabel *lrc_music_nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *lrc_music_songerLabel;
@property (weak, nonatomic) IBOutlet UILabel *lrc_music_lrcLabel;
@property (weak, nonatomic) IBOutlet UIButton *lrc_music_roseNumBtn;

@property (nonatomic, copy) void (^showMusicLRCFullBlock)(void);
@property (nonatomic, copy) void (^sendGiftRedRoseBlock)(void);


@end

NS_ASSUME_NONNULL_END

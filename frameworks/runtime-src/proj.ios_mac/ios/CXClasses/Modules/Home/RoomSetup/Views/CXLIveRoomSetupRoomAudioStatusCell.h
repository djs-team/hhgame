//
//  CXLIveRoomSetupRoomAudioStatusCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLIveRoomSetupRoomAudioStatusCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIButton *audio_noBtn;
@property (weak, nonatomic) IBOutlet UIButton *audio_yesBtn;

@property (nonatomic, copy) void (^setupRoomTypeAudioActionBlock)(BOOL isAudio);

@property (nonatomic, assign) BOOL IsCloseCamera; // 是否允许音频上麦

@end

NS_ASSUME_NONNULL_END

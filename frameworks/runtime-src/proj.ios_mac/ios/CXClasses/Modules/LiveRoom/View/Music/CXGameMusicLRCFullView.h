//
//  CXGameMusicLRCFullView.h
//  hairBall
//
//  Created by mahong yang on 2020/5/18.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "DDAudioLRCParser.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicLRCFullView : UIView

@property (nonatomic, strong) DDAudioLRC *lrcModel;

@property (nonatomic, copy) void (^musicLRCFullViewCloseBlock)(void);

@property (nonatomic, assign) NSInteger pro_currentTime;
@property (nonatomic, assign) NSInteger pro_endTime;

@property (nonatomic, assign) NSInteger currentShowRow;

@end

NS_ASSUME_NONNULL_END

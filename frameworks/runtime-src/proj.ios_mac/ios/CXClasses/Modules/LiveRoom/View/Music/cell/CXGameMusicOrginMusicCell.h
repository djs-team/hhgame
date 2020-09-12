//
//  CXGameMusicOrginMusicCell.h
//  hairBall
//
//  Created by mahong yang on 2020/2/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXGameMusicOrginMusicCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *music_nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *music_songerLabel;
@property (weak, nonatomic) IBOutlet UIButton *reserveBtn;
@property (nonatomic, copy) void (^reserveActionBlock)(void);
@end

NS_ASSUME_NONNULL_END

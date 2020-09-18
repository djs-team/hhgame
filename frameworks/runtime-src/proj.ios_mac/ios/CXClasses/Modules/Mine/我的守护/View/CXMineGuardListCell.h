//
//  CXMineGuardListCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXMineGuardListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nicknameLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;
@property (weak, nonatomic) IBOutlet UILabel *stateLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *expiredLabel;
@property (weak, nonatomic) IBOutlet UILabel *intimacyLabel;

@property (weak, nonatomic) IBOutlet UIButton *renewBtn;
@property (weak, nonatomic) IBOutlet UIButton *automaticBtn;

@property (nonatomic, copy) void(^renewActionBlock)(void);
@property (nonatomic, copy) void(^automaticActionBlock)(void);
@property (nonatomic, copy) void(^avatarTapGestureBlock)(void);

@property (nonatomic, assign) BOOL isMineGuard;
@property (nonatomic, strong) CXUserModel *model;

@end

NS_ASSUME_NONNULL_END

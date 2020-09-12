//
//  CXGameMusicSongerSeatView.m
//  hairBall
//
//  Created by mahong yang on 2020/2/11.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicSongerSeatView.h"

@interface CXGameMusicSongerSeatView()
@property (weak, nonatomic) IBOutlet UIImageView *avatar_bg;
@property (weak, nonatomic) IBOutlet UIView *bottom_bgView;
@property (weak, nonatomic) IBOutlet UIImageView *avatar_bottom;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;

@property (weak, nonatomic) IBOutlet UIView *sureView;
@property (weak, nonatomic) IBOutlet UILabel *sureLabel;
@property (weak, nonatomic) IBOutlet UIButton *sureBtn;
@property (weak, nonatomic) IBOutlet UIButton *microBtn;
@end

@implementation CXGameMusicSongerSeatView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    self.bottom_bgView.layer.masksToBounds = YES;
    self.bottom_bgView.layer.cornerRadius = 12;
    
    self.avatar_bottom.layer.masksToBounds = YES;
    self.avatar_bottom.layer.cornerRadius = 8;
    
    self.sureBtn.layer.masksToBounds = YES;
    self.sureBtn.layer.cornerRadius = 13;
    
    self.microBtn.layer.masksToBounds = YES;
    self.microBtn.layer.cornerRadius = 12;
}

- (void)setUser:(LiveRoomUser *)user {
    _user = user;
    
    [self.avatar_bg sd_setImageWithURL:[NSURL URLWithString:user.HeadImageUrl]];
    [self.avatar_bottom sd_setImageWithURL:[NSURL URLWithString:user.HeadImageUrl]];
    
    self.nameLabel.text = user.Name;
    self.descLabel.text = [NSString stringWithFormat:@"%@|%@|%@",user.Age,user.Stature,user.Sex == 1 ? @"男" : @"女"];
    
    NSIndexPath *indexPath = [[CXClientModel instance].room.userSeats objectForKey:user.UserId];
    if (indexPath.row == 0 && indexPath.section == 0) {
        self.sureLabel.text = @"房主将演唱你的点歌";
        
        self.descLabel.hidden = YES;
        self.nameLabel.font = [UIFont systemFontOfSize:16];
        self.nameLabel.text = @"主播";
    } else {
        if (indexPath.row == 0 && indexPath.section == 1) {
            self.sureLabel.text = @"男嘉宾将演唱你的点歌";
        }
        self.descLabel.hidden = NO;
        self.nameLabel.font = [UIFont systemFontOfSize:10];
        self.nameLabel.text = user.Sex == 1 ? @"男嘉宾" : @"女嘉宾";
    }
    
}

- (IBAction)playAction:(id)sender {
    self.sureView.hidden = NO;
}

- (IBAction)sureCancelAction:(id)sender {
    self.sureView.hidden = YES;
}

- (IBAction)sureAction:(id)sender {
    if (self.songerSeatSureActionBlock) {
        self.songerSeatSureActionBlock(_user);
    }
}

@end

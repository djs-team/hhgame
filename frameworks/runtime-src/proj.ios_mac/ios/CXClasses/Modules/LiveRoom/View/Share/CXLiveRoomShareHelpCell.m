//
//  CXLiveRoomShareHelpCell.m
//  hairBall
//
//  Created by mahong yang on 2020/5/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomShareHelpCell.h"

@interface CXLiveRoomShareHelpCell()
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UIView *infoViewBG;
@property (weak, nonatomic) IBOutlet UILabel *userNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;

@end

@implementation CXLiveRoomShareHelpCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _infoViewBG.layer.masksToBounds = YES;
    _infoViewBG.layer.cornerRadius = 15;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 4;
    
}

- (void)setUser:(LiveRoomUser *)user {
    _user = user;
    
    [_logo sd_setImageWithURL:[NSURL URLWithString:user.HeadImageUrl]];
    _userNameLabel.text = user.Name;
    _descLabel.text = [NSString stringWithFormat:@"%@岁｜%@cm｜%@",user.Age.stringValue,user.Stature.stringValue,user.City.length > 0 ? user.City : @"城市"];
}

@end

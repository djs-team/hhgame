//
//  CXAllOnlineListCell.m
//  hairBall
//
//  Created by mahong yang on 2019/11/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXAllOnlineListCell.h"

@implementation CXAllOnlineListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.avatar.layer.masksToBounds = YES;
    self.avatar.layer.cornerRadius = 15;
    
    UITapGestureRecognizer *singleTap =
    [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(whenClickImage:)];
    self.avatar.userInteractionEnabled = YES;
    [self.avatar addGestureRecognizer:singleTap];
}

- (void)whenClickImage:(UIGestureRecognizerState *)gesture {
    if (self.checkUserProfileBlock) {
        self.checkUserProfileBlock();
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setOrderModel:(LiveRoomMicroOrder *)orderModel {
    _orderModel = orderModel;
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:orderModel.modelUser.HeadImageUrl]];
    self.nameLabel.text = orderModel.modelUser.Name;
    [self.sexBtn setTitle:orderModel.Age.stringValue forState:UIControlStateNormal];
    if ([orderModel.Sex isEqual: @1]) {
        [self.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    [self.locationBtn setTitle:orderModel.City.length > 0 ? orderModel.City: @"" forState:UIControlStateNormal];
}

- (void)setOnlineUser:(SocketMessageGetOnlineUserListResponseOnlineUser *)onlineUser {
    _onlineUser = onlineUser;
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:onlineUser.User.HeadImageUrl]];
    self.nameLabel.text = onlineUser.User.Name;
    [self.sexBtn setTitle:onlineUser.User.Age forState:UIControlStateNormal];
    if ([onlineUser.Sex isEqual: @1]) {
        [self.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    [self.locationBtn setTitle:onlineUser.User.City.length > 0 ? onlineUser.User.City: @"" forState:UIControlStateNormal];
}

- (void)setModel:(CXUserModel *)model {
    _model = model;
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:model.avatar]];
    self.nameLabel.text = model.nickname;
    [self.sexBtn setTitle:model.age forState:UIControlStateNormal];
    if ([model.sex isEqual: @1]) {
        [self.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    [self.locationBtn setTitle:[NSString stringWithFormat:@"%@%@",model.city.length > 0 ? model.city:@"",model.city_two.length > 0 ? model.city_two : @""] forState:UIControlStateNormal];
}

- (void)setFriendModel:(CXFriendInviteModel *)friendModel {
    _friendModel = friendModel;
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:friendModel.avatar]];
    self.nameLabel.text = friendModel.nickname;
    [self.sexBtn setTitle:friendModel.age forState:UIControlStateNormal];
    if ([friendModel.sex isEqualToString:@"1"]) {
        [self.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    [self.locationBtn setTitle:[NSString stringWithFormat:@"%@%@",friendModel.city.length > 0 ? friendModel.city:@"", friendModel.city_two.length > 0 ? friendModel.city_two:@""] forState:UIControlStateNormal];
}

- (IBAction)applyAction:(id)sender {
    if (self.applyActionBlock) {
        self.applyActionBlock();
    }
}

@end

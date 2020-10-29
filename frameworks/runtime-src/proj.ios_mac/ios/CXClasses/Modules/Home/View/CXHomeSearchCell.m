//
//  CXHomeSearchCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import "CXHomeSearchCell.h"

@implementation CXHomeSearchCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _avatar.layer.masksToBounds = YES;
    _avatar.layer.cornerRadius = 28;
    _avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    _avatar.layer.borderWidth = 0.5;
    
    _stateLabel.layer.masksToBounds = YES;
    _stateLabel.layer.cornerRadius = 8;
    
    UITapGestureRecognizer *singleTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(avatarTapAction:)];
    //为图片添加手势
    [_avatar addGestureRecognizer:singleTap];
    
    _avatar.userInteractionEnabled = YES;
}

- (void)avatarTapAction:(UITapGestureRecognizer *)gesture {
    if (self.avatarTapGestureBlock) {
        self.avatarTapGestureBlock();
    }
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setUser:(CXUserModel *)user {
    _user = user;
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:user.avatar]];
    _nameLabel.text = user.nickname;
    _user_idLabel.text = [NSString stringWithFormat:@"ID:%@", user.user_id];
    [_sexBtn setImage:user.sexImage forState:UIControlStateNormal];
    [_sexBtn setTitle:user.age forState:UIControlStateNormal];
    if (user.city.length > 0) {
        _locationLabel.hidden = NO;
        if (user.city_two.length > 0) {
            [_locationLabel setTitle:[NSString stringWithFormat:@"%@%@", user.city,user.city_two] forState:UIControlStateNormal];
        } else {
            [_locationLabel setTitle:user.city forState:UIControlStateNormal];
        }
    } else {
        _locationLabel.hidden = YES;
    }
    
    _onlineStateLabel.text = user.online_str;
    _stateLabel.hidden = YES;
    _stateLabel.text = @"";
    if ([user.online_str isEqualToString:@"在线"]) {
        _onlineStateLabel.textColor = UIColorHex(0x0FE770);
    } else if ([user.online_str isEqualToString:@"刚刚在线"]) {
        _onlineStateLabel.textColor = UIColorHex(0xFBD711);
    } else if ([user.online_str isEqualToString:@"热聊中"]) {
        _stateLabel.hidden = NO;
        _stateLabel.text = @"热聊中";
        _stateLabel.backgroundColor = UIColorHex(0xEF51B2);
        
        _onlineStateLabel.textColor = UIColorHex(0xEF51B2);
    } else if ([user.online_str isEqualToString:@"相亲中"]) {
        _stateLabel.hidden = NO;
        _stateLabel.text = @"相亲中";
        _stateLabel.backgroundColor = UIColorHex(0x7F3EF0);
        
        _onlineStateLabel.textColor = UIColorHex(0x7F3EF0);
    } else if ([user.online_str isEqualToString:@"开播中"]) {
        _stateLabel.hidden = NO;
        _stateLabel.text = @"开播中";
        _stateLabel.backgroundColor = UIColorHex(0xFEBF00);
        
        _onlineStateLabel.textColor = UIColorHex(0xFEBF00);
    } else {
        _onlineStateLabel.textColor = UIColorHex(0x818181);
    }
    
    if (user.room_id.length > 0) {
        _avatar.userInteractionEnabled = YES;
    } else {
        _avatar.userInteractionEnabled = NO;
    }
}

- (void)setRoom:(CXHomeRoomModel *)room {
    _room = room;
}

@end

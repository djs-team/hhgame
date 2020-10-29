//
//  CXFriendMessageCell.m
//  hairBall
//
//  Created by mahong yang on 2019/10/28.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXFriendMessageCell.h"

@implementation CXFriendMessageCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _avatar.layer.masksToBounds = YES;
    _avatar.layer.cornerRadius = 28;
    _avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    _avatar.layer.borderWidth = 0.5;
    
    _unReadCount.layer.masksToBounds = YES;
    _unReadCount.layer.cornerRadius = 8;
    
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

- (void)setModel:(CXFriendInviteModel *)model {
    _model = model;
    
    if ([_model.nickname length] > 0) {
        self.usernameLabel.text = _model.nickname;
    } else{
        self.usernameLabel.text = @"用户名称";
    }
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:_model.avatar]];
    
    [self.userSexBtn setTitle:model.age forState:UIControlStateNormal];
    if ([model.sex isEqualToString:@"1"]) {
        [self.userSexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.userSexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    [self.locationBtn setTitle:model.city forState:UIControlStateNormal];

    if (_isConversation == YES) {
        _onlineLabel.hidden = YES;
        if ([model.online_str isEqualToString:@"在线"]) {
            _stateLabel_widthLayout.constant = 16;
            _stateLabel.text = @"";
            _stateLabel.backgroundColor = UIColorHex(0x0FE770);
        } else if ([model.online_str isEqualToString:@"刚刚在线"]) {
            _stateLabel_widthLayout.constant = 16;
            _stateLabel.text = @"";
            _stateLabel.backgroundColor = UIColorHex(0xFBD711);
        } else if ([model.online_str isEqualToString:@"热聊中"]) {
            _stateLabel_widthLayout.constant = 40;
            _stateLabel.text = @"热聊中";
            _stateLabel.backgroundColor = UIColorHex(0xEF51B2);
        } else if ([model.online_str isEqualToString:@"相亲中"]) {
            _stateLabel_widthLayout.constant = 40;
            _stateLabel.text = @"相亲中";
            _stateLabel.backgroundColor = UIColorHex(0x7F3EF0);
        } else if ([model.online_str isEqualToString:@"开播中"]) {
            _stateLabel_widthLayout.constant = 40;
            _stateLabel.text = @"开播中";
            _stateLabel.backgroundColor = UIColorHex(0xFEBF00);
        } else {
            _stateLabel_widthLayout.constant = 16;
            _stateLabel.text = @"";
            _stateLabel.backgroundColor = UIColorHex(0x818181);
        }
        
        if (model.conversation.unreadMessagesCount > 99) {
            self.unReadCount.text = @"99+";
        } else if (model.conversation.unreadMessagesCount > 0) {
            self.unReadCount.text = [NSString stringWithFormat:@"%d",model.conversation.unreadMessagesCount];
        } else {
            self.unReadCount.hidden = YES;
        }
    } else {
        _timeLabel.hidden = YES;
        _unReadCount.hidden = YES;
        
        _onlineLabel.text = model.online_str;
        _stateLabel.hidden = YES;
        _stateLabel.text = @"";
        _stateLabel_widthLayout.constant = 16;
        if ([model.online_str isEqualToString:@"在线"]) {
            _onlineLabel.textColor = UIColorHex(0x0FE770);
        } else if ([model.online_str isEqualToString:@"刚刚在线"]) {
            _onlineLabel.textColor = UIColorHex(0xFBD711);
        } else if ([model.online_str isEqualToString:@"热聊中"]) {
            _stateLabel.hidden = NO;
            _stateLabel_widthLayout.constant = 40;
            _stateLabel.text = @"热聊中";
            _stateLabel.backgroundColor = UIColorHex(0xEF51B2);
            
            _onlineLabel.textColor = UIColorHex(0xEF51B2);
        } else if ([model.online_str isEqualToString:@"相亲中"]) {
            _stateLabel.hidden = NO;
            _stateLabel_widthLayout.constant = 40;
            _stateLabel.text = @"相亲中";
            _stateLabel.backgroundColor = UIColorHex(0x7F3EF0);
            
            _onlineLabel.textColor = UIColorHex(0x7F3EF0);
        } else if ([model.online_str isEqualToString:@"开播中"]) {
            _stateLabel.hidden = NO;
            _stateLabel_widthLayout.constant = 40;
            _stateLabel.text = @"开播中";
            _stateLabel.backgroundColor = UIColorHex(0xFEBF00);
            
            _onlineLabel.textColor = UIColorHex(0xFEBF00);
        } else {
            _onlineLabel.textColor = UIColorHex(0x818181);
        }
    }
}

@end

//
//  CXUserInfoMainInfoCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXUserInfoMainInfoCell.h"

@implementation CXUserInfoMainInfoCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _stateLabel.layer.masksToBounds = YES;
    _stateLabel.layer.cornerRadius = 8;
    
    _guardBtn.layer.masksToBounds = YES;
    _guardBtn.layer.cornerRadius = 12;
}
- (IBAction)guardAction:(id)sender {
    if (self.guardActionBlock) {
        self.guardActionBlock();
    }
}

- (void)setModel:(CXUserModel *)model {
    _model = model;
    
    self.nicknameLabel.text = _model.nickname;
    self.useridLabel.text = [NSString stringWithFormat:@"ID:%@", model.user_id];
    [self.sexBtn setTitle:model.age forState:UIControlStateNormal];
    if ([model.sex integerValue] == 1) {
        [self.sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [self.sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    if (model.online.integerValue == 3 || model.online.integerValue == 2) {
        _stateLabel.text = @"相亲中";
        _stateLabel.backgroundColor = UIColorHex(0x7F3EF0);
    } else if (model.online.integerValue == 5 || model.online.integerValue == 4) {
        _stateLabel.text = @"热聊中";
        _stateLabel.backgroundColor = UIColorHex(0xEF51B2);
    } else if (model.online.integerValue == 6) {
        _stateLabel.text = @"开播中";
        _stateLabel.backgroundColor = UIColorHex(0xFEBF00);
    } else if (model.online.integerValue == 0) {
        _stateLabel.text = @"离线";
        _stateLabel.backgroundColor = UIColorHex(0x818181);
    } else {
        _stateLabel.text = @"在线";
        _stateLabel.backgroundColor = UIColorHex(0x10E770);
    }
}

@end

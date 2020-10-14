//
//  CXHomeRoomCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXHomeRoomCell.h"
#import "UIImage+GIF.h"

@interface CXHomeRoomCell()

@property (nonatomic, strong) UIImage *onlineStateImage;

@end

@implementation CXHomeRoomCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    _user_avatarImage.layer.masksToBounds = YES;
    _user_avatarImage.layer.cornerRadius = 9;
    _user_avatarImage.layer.borderWidth = 0.5;
    _user_avatarImage.layer.borderColor = UIColorHex(0x686E7A).CGColor;
    
    
    _room_image.layer.masksToBounds = YES;
    _room_image.layer.cornerRadius = 10;
    
    _room_stateBtn.layer.masksToBounds = YES;
    _room_stateBtn.layer.cornerRadius = 12;
    
    _room_lockView.layer.masksToBounds = YES;
    _room_lockView.layer.cornerRadius = 10;
    
}

- (void)setModel:(CXHomeRoomModel *)model {
    _model = model;
    [_room_image sd_setImageWithURL:[NSURL URLWithString:model.fm_room_image]];
    _room_descLabel.text = [NSString stringWithFormat:@"%@岁 · %@",model.fm_age, model.fm_city];
    _room_nameLabel.text = model.room_name;
    
    [_user_avatarImage sd_setImageWithURL:[NSURL URLWithString:model.maker_avatar]];
    _room_lockView.hidden = [model.room_lock integerValue] != 1;
    _user_nicknameLabel.text = model.fm_nickname;

    if ([model.room_type integerValue] == 5) {
        // 相亲房房间状态 1:无嘉宾 2:等待中 3:相亲中
        if ([model.xq_type intValue] == 3) {
            [self.room_stateBtn setBackgroundImage:[UIImage imageWithColor:UIColorHex(0xCE58FA)] forState:UIControlStateNormal];
            [self.room_stateBtn setTitle:@"相亲中" forState:UIControlStateNormal];
//            [self.room_stateBtn setImage:self.onlineStateImage forState:UIControlStateNormal];
        } else if ([model.xq_type intValue] == 2) {
            [self.room_stateBtn setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(58, 16) Color1:UIColorHex(0x0BF7AE) color2:UIColorHex(0x009EFD)] forState:UIControlStateNormal];
            [self.room_stateBtn setTitle:@"等待中" forState:UIControlStateNormal];
//            [self.room_stateBtn setImage:self.onlineStateImage forState:UIControlStateNormal];
            self.room_stateBtn_widthLayout.constant = 58;
        } else {
            [self.room_stateBtn setBackgroundImage:[UIImage imageWithColor:[UIColor clearColor]] forState:UIControlStateNormal];
            [self.room_stateBtn setTitle:@"" forState:UIControlStateNormal];
//            [self.room_stateBtn setImage:self.onlineStateImage forState:UIControlStateNormal];
        }
    } else {
        [self.room_stateBtn setBackgroundImage:[UIImage imageWithColor:[UIColor clearColor]] forState:UIControlStateNormal];
        self.room_stateBtn.backgroundColor = [UIColor clearColor];
        [self.room_stateBtn setTitle:@"" forState:UIControlStateNormal];
//        [self.room_stateBtn setImage:self.onlineStateImage forState:UIControlStateNormal];
    }
    
    if ([model.right_corn integerValue] == 1) {
        self.right_cornImage.hidden = NO;
    } else {
        self.right_cornImage.hidden = YES;
    }
}

- (UIImage *)onlineStateImage {
//    if (_onlineStateImage) {
//        NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"live_room_online" ofType:@"gif"];
//        NSData *imageData = [NSData dataWithContentsOfFile:imagePath];
//    _onlineStateImage = [UIImage sd_imageWithGIFData:imageData];
//        _onlineStateImage = [UIImage sd_animatedGIFWithData:imageData];
//    }
    return _onlineStateImage;
}

@end

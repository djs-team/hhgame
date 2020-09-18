//
//  CXLiveRoomGuardianAnimationView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/23.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomGuardianAnimationView.h"
#import "UIImage+GIF.h"

@interface CXLiveRoomGuardianAnimationView()

@property (weak, nonatomic) IBOutlet UIImageView *animation_bgImage;

@property (weak, nonatomic) IBOutlet UIImageView *bgImage;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *contentView_topLayout;
@property (weak, nonatomic) IBOutlet UIImageView *user_avatar;
@property (weak, nonatomic) IBOutlet UIImageView *guardian_avatar;

@property (weak, nonatomic) IBOutlet UIButton *messageBtn;

@end

@implementation CXLiveRoomGuardianAnimationView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(kScreenWidth, kScreenHeight));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = NO;
    self.type = MMPopupTypeCustom;
    
//    NSString *imagePath = [[NSBundle mainBundle] pathForResource:@"live_room_guardian_animation" ofType:@"gif"];
//    NSData *imageData = [NSData dataWithContentsOfFile:imagePath];
//    _bgImage.image = [UIImage sd_animatedGIFWithData:imageData];
//    _animation_bgImage.image = [UIImage sd_animatedGIFWithData:imageData];
    
    _contentView_topLayout.constant = 105*SCALE_W + kNavHeight;
    
    _user_avatar.layer.masksToBounds = YES;
    _user_avatar.layer.cornerRadius = 43;

    UIBezierPath *path = [UIBezierPath bezierPath];
    [path moveToPoint:CGPointMake(30, 10)];
    [path addQuadCurveToPoint:CGPointMake(54, 26) controlPoint:CGPointMake(54, 10)];
    [path addQuadCurveToPoint:CGPointMake(15, 52) controlPoint:CGPointMake(34, 52)];
    [path addQuadCurveToPoint:CGPointMake(12, 0) controlPoint:CGPointMake(-12, 0)];
    [path addQuadCurveToPoint:CGPointMake(30, 10) controlPoint:CGPointMake(24, 0)];

    CAShapeLayer *maskLayer = [[CAShapeLayer alloc]init];
    //设置大小
    maskLayer.frame = _guardian_avatar.bounds;
    //设置图形样子
    maskLayer.path = path.CGPath;
    _guardian_avatar.layer.mask = maskLayer;
    
    self.messageBtn.titleLabel.numberOfLines = 2;
    self.messageBtn.titleLabel.textAlignment = NSTextAlignmentCenter;
}

- (void)setMessage:(SocketMessageUserJoinRoom *)message {
    _message = message;
    
    [self.user_avatar sd_setImageWithURL:[NSURL URLWithString:message.TargetImage] placeholderImage:[UIImage imageNamed:@"mine_guardian_placeholder"]];
    [self.guardian_avatar sd_setImageWithURL:[NSURL URLWithString:message.UserImage] placeholderImage:[UIImage imageNamed:@"mine_guardian_placeholder"]];
    
    [self.messageBtn setTitle:[NSString stringWithFormat:@"恭喜“%@”成为“%@”的守护", message.UserName, message.TargetName] forState:UIControlStateNormal];
}


@end

//
//  XYRoomAlreadListCell.m
//  hairBall
//
//  Created by zyy on 2019/11/1.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "XYRoomAlreadListCell.h"

@interface XYRoomAlreadListCell ()

@property (weak, nonatomic) IBOutlet UILabel *indexLabel;
@property (weak, nonatomic) IBOutlet UILabel *levelLabel;

@property (weak, nonatomic) IBOutlet UILabel *userNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *userInfoLabel;
@property (weak, nonatomic) IBOutlet UIImageView *headerImageV;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageV;

@property (weak, nonatomic) IBOutlet UIButton *upMicroBtn;

@end

@implementation XYRoomAlreadListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    UITapGestureRecognizer *singleTap =
    [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(whenClickImage:)];
    self.headerImageV.userInteractionEnabled = YES;
    [self.headerImageV addGestureRecognizer:singleTap];
    
    self.levelLabel.layer.masksToBounds = YES;
    self.levelLabel.layer.cornerRadius = 8;
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

- (void)setIndexP:(NSInteger)indexP {
    self.indexLabel.text = indexP > 8 ? [NSString stringWithFormat:@"%ld" , indexP + 1] : [NSString stringWithFormat:@"%ld" , indexP + 1];
}

- (void)setOrder:(LiveRoomMicroOrder *)order{
    _order = order;
    
    self.userNameLabel.text = order.modelUser.Name;
    self.levelLabel.text = [NSString stringWithFormat:@" LV.%@ ",order.modelUser.VipLevel];
    self.userInfoLabel.text = [NSString stringWithFormat:@"%@岁| %@ |%@" ,order.modelUser.Age ? order.modelUser.Age: @"年龄" , order.modelUser.City ? order.modelUser.City :@"" , order.modelUser.Stature ? order.modelUser.Stature : @"身高"];
    
    [self.headerImageV sd_setImageWithURL:[NSURL URLWithString:order.modelUser.HeadImageUrl] placeholderImage:[UIImage imageNamed:@"avatar_default"]];
    
    self.sexImageV.image = order.Sex.integerValue == 1 ? [UIImage imageNamed:@"nan"] : [UIImage imageNamed:@"nv"];
    
    if (order.isCanUpMicro == NO) {
        
//        [self.upMicroBtn setBackgroundImage:[UIImage imageNamed:@"AlreadApplylistGray"] forState:UIControlStateNormal];
        self.upMicroBtn.hidden = YES;
    } else {
//        [self.upMicroBtn setBackgroundImage:[UIImage imageNamed:@"AlreadApplylistyellow"] forState:UIControlStateNormal];
        self.upMicroBtn.hidden = NO;
    }
    
}

- (IBAction)alreadListButClick:(UIButton *)sender {
    if (sender.tag == 3 && _order.isCanUpMicro == NO) {
        return;
    }
    self.alreadListOpreationBlock(sender.tag , self.order);
    
}


@end

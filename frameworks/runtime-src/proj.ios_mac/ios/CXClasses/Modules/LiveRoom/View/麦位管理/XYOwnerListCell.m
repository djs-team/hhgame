//
//  XYOwnerListCell.m
//  hairBall
//
//  Created by zyy on 2019/10/31.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "XYOwnerListCell.h"
#import <SDImageCache.h>

@implementation XYOwnerListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.headerImageV.layer.masksToBounds = YES;
    
    self.headerImageV.layer.cornerRadius = 19;
    
}

-(void)setIndexP:(NSInteger)indexP{
    
    self.indexLabel.text = indexP > 9 ?  [NSString stringWithFormat:@"%ld" , indexP + 1] : [NSString stringWithFormat:@"0%ld" , indexP + 1];
}

-(void)setUserModel:(LiveRoomUser *)userModel{
    self.nameLabel.text = userModel.Name;
    [self.headerImageV sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@" , userModel.HeadImageUrl]]];
}

-(void)setOrder:(LiveRoomMicroOrder *)order{
    self.nameLabel.text = order.modelUser.Name;
    
    [self.headerImageV sd_setImageWithURL:[NSURL URLWithString:[NSString stringWithFormat:@"%@" , order.modelUser.HeadImageUrl]]];
    
//    self.indexLabel.text = order.Number.integerValue > 8 ?  [NSString stringWithFormat:@"%ld" , order.Number.integerValue + 1] : [NSString stringWithFormat:@"0%ld" , order.Number.integerValue + 1];
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

}

@end

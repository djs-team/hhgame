//
//  CXHomeRoomRecommendCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import "CXHomeRoomRecommendCell.h"

@implementation CXHomeRoomRecommendCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    self.descLabel.layer.masksToBounds = YES;
    self.descLabel.layer.cornerRadius = 14;

}

- (void)setModel:(CXHomeRoomModel *)model {
    _model = model;
    [_roomImage sd_setImageWithURL:[NSURL URLWithString:model.fm_room_image] placeholderImage:[UIImage imageNamed:@"applogo"]];
//    _descLabel.text = [NSString stringWithFormat:@"%@岁 · %@",model.fm_age, model.fm_city];
    _roomnameLabel.text = [NSString stringWithFormat:@"欢迎来到%@的直播间",model.fm_nickname];
    
    if ([model.right_corn integerValue] == 1) {
        self.roomActivityImage.hidden = NO;
    } else {
        self.roomActivityImage.hidden = YES;
    }
}

@end

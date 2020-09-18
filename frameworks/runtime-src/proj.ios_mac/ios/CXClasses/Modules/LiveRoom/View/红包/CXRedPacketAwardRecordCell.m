//
//  CXRedPacketAwardRecordCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import "CXRedPacketAwardRecordCell.h"

@implementation CXRedPacketAwardRecordCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.avatar.layer.masksToBounds = YES;
    self.avatar.layer.cornerRadius = 18;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

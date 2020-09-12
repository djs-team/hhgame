//
//  MuaGiftListSelectCountCell.m
//  hairBall
//
//  Created by shiwei on 2019/8/1.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "MuaGiftListSelectCountCell.h"

@interface MuaGiftListSelectCountCell ()

@property (weak, nonatomic) IBOutlet UILabel *number;
@property (weak, nonatomic) IBOutlet UILabel *desc;

@end

@implementation MuaGiftListSelectCountCell

- (void)setCountStr:(NSString *)countStr {
    _countStr = countStr;
    self.number.text = [countStr componentsSeparatedByString:@" "].firstObject;
    self.desc.text = [countStr componentsSeparatedByString:@" "].lastObject;
}

+ (instancetype)cellWithTableView:(UITableView *)tableView identifier:(NSString *)identifier {
    
    MuaGiftListSelectCountCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if(cell == nil){
        cell = [[[NSBundle mainBundle] loadNibNamed:NSStringFromClass([self class]) owner:nil options:nil] lastObject];
    }
    // 设置数据
    return cell;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end

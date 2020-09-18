//
//  CXMineEditProfileItemCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXMineEditProfileItemCell.h"

@implementation CXMineEditProfileItemCell

- (instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
    }
    return self;
}
-(void)setUI{
    [self addSubview:self.leftLabel];
    [self addSubview:self.rightLabel];
//    UIView *line = [[UIView alloc]init];
//    line.backgroundColor = [UIColor colorWithHexString:@"FFFFFF"];
//    [self addSubview:line];
    
    [self.leftLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(15);
        make.top.equalTo(self.mas_top).offset(0);
        make.bottom.equalTo(self.mas_bottom).offset(0);
//        make.width.mas_equalTo(100);
    }];
    [self.rightLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.mas_right).offset(-40);
        make.top.equalTo(self.mas_top).offset(0);
        make.bottom.equalTo(self.mas_bottom).offset(0);
        make.left.equalTo(self.leftLabel.mas_right).offset(20);
    }];
//    [line mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.right.equalTo(self.mas_right).offset(0);
//        make.height.mas_equalTo(1);
//        make.bottom.equalTo(self.mas_bottom).offset(0);
//        make.left.equalTo(self.mas_left).offset(0);
//    }];
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
#pragma mark -setter
-(UILabel*)leftLabel{
    if (!_leftLabel) {
        _leftLabel = [[UILabel alloc]init];
        _leftLabel.textColor = [UIColor colorWithHexString:@"333333"];
        _leftLabel.font = [UIFont boldSystemFontOfSize:16.0];
    }
    return _leftLabel;
}
-(UILabel*)rightLabel{
    if (!_rightLabel) {
        _rightLabel = [[UILabel alloc]init];
        _rightLabel.textColor = [UIColor colorWithHexString:@"FF0D0D"];
        _rightLabel.textAlignment = NSTextAlignmentRight;
        _rightLabel.font = [UIFont boldSystemFontOfSize:16.0];
    }
    return _rightLabel;
}

@end

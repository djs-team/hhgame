//
//  CXConsumeRecordCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import "CXConsumeRecordCell.h"

@interface CXConsumeRecordCell()
@property (nonatomic,strong)UILabel *contentLabel;
@property (nonatomic,strong)UILabel *timeLabel;
@property (nonatomic,strong)UILabel *rightLabel;
@end

@implementation CXConsumeRecordCell

- (instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
    }
    return self;
}

- (void)setUI{
    [self addSubview:self.contentLabel];
    [self addSubview:self.timeLabel];
    [self addSubview:self.rightLabel];
    [self.contentLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(20);
        make.top.equalTo(self.mas_top).offset(15);
    }];
    [self.timeLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(20);
        make.bottom.equalTo(self.mas_bottom).offset(-12);
    }];
    [self.rightLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.mas_right).offset(-25);
        make.centerY.equalTo(self);
    }];
}
-(void)setModel:(CXConsumeRecordModel *)model {
    self.contentLabel.text = model.content;
    self.timeLabel.text = model.addtime;
    if ([model.type isEqualToString:@"expend"]) {
        self.rightLabel.text = [@"-" stringByAppendingString:model.coin];
        self.rightLabel.textColor = [UIColor colorWithHexString:@"#9B9B9B"];
    }else{
        self.rightLabel.text = [@"+" stringByAppendingString:model.coin];
        self.rightLabel.textColor = [UIColor colorWithHexString:@"#FF8282"];

    }
}
- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}
-(UILabel*)timeLabel{
    if (!_timeLabel) {
        _timeLabel = [[UILabel alloc]init];
        _timeLabel.textColor = [UIColor colorWithHexString:@"#BBBBBB"];
        _timeLabel.font = [UIFont systemFontOfSize:13.0f];
    }
    return _timeLabel;
}
-(UILabel*)contentLabel{
    if (!_contentLabel) {
        _contentLabel = [[UILabel alloc]init];
        _contentLabel.textColor = UIColorHex(0x333333);
        _contentLabel.font = [UIFont systemFontOfSize:16.0f];
    }
    return _contentLabel;
}
-(UILabel*)rightLabel{
    if (!_rightLabel) {
        _rightLabel = [[UILabel alloc]init];
        _rightLabel.font = [UIFont systemFontOfSize:16.0f];
    }
    return _rightLabel;
}

@end

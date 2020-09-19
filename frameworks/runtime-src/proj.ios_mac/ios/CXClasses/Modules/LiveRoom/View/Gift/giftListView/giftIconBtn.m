//
//  giftIconBtn.m
//  hairBall
//
//  Created by ashuan on 2019/4/27.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "giftIconBtn.h"
@interface giftIconBtn()
@property (nonatomic,strong)UIView *borderView;

@end
@implementation giftIconBtn
-(instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
    }
    return self;
}
-(void)setUI{
    self.clipsToBounds = YES;
    self.layer.borderColor = [UIColor clearColor].CGColor;
    self.layer.borderWidth = 1;
    self.layer.cornerRadius = 4.f;
    [self addSubview:self.giftXLabel];
    [self addSubview:self.giftNameLabel];
    [self addSubview:self.giftCountLabel];
    [self addSubview:self.gifIconView];
    [self addSubview:self.borderView];
    [self.giftXLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.mas_equalTo(16.0f);
        make.top.equalTo(self.mas_top).offset(5);
        make.right.equalTo(self.mas_right).offset(-5);
    }];
    [self.borderView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(self.giftXLabel);
        make.top.equalTo(self.giftXLabel.mas_top).offset(0);
        make.left.equalTo(self.giftXLabel.mas_left).offset(0);
    }];
    [self.giftCountLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.mas_bottom).offset(-4);
        make.centerX.equalTo(self);
    }];
    [self.giftNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.equalTo(self.giftCountLabel.mas_top).offset(-4);
        make.centerX.equalTo(self);
    }];
    [self.gifIconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(55, 55));
        make.top.equalTo(self.mas_top).offset(10);
        make.centerX.equalTo(self);
    }];
}


-(void)setGiftIconBtnModel:(voiceGiftModel*)model{
    if ([model.gift_id integerValue] == 0) { // 蓝玫瑰
        self.gifIconView.image = [UIImage imageNamed:model.gift_image];
        self.giftNameLabel.text = model.gift_name;
        self.giftCountLabel.hidden = YES;
        self.giftXLabel.hidden = YES;
        self.borderView.hidden = YES;
    } else {
        self.giftCountLabel.hidden = NO;
        self.giftXLabel.hidden = NO;
        self.borderView.hidden = NO;
        
        [self.gifIconView sd_setImageWithURL:[NSURL URLWithString:model.gift_image] placeholderImage:[UIImage imageNamed:@"gift_logo_placeholder"]];
        self.giftNameLabel.text = model.gift_name;
//        if (model.IsUseBeg) {
//            self.giftNameLabel.text = [NSString stringWithFormat:@"%@x%@", model.gift_name, model.pack_num];
//        } else {
//            self.giftNameLabel.text = model.gift_name;
//        }
        
        self.giftCountLabel.text = [NSString stringWithFormat:@"%@玫瑰",model.gift_coin];
        if ([model.gift_number integerValue]>0) {
            [self.giftXLabel setTitle:[NSString stringWithFormat:@"+%@",model.gift_number] forState:UIControlStateNormal];
            
            self.borderView.layer.borderColor = [UIColor colorWithHexString:@"#F8705F"].CGColor;
        }else{
            [self.giftXLabel setTitle:[NSString stringWithFormat:@"%@",model.gift_number] forState:UIControlStateNormal];
            self.borderView.layer.borderColor = [UIColor colorWithHexString:@"#00D386"].CGColor;
            
        }
    }
    
    if (model.isSelect) {
        self.layer.borderColor = [UIColor colorWithHexString:@"#FE4EC0"].CGColor;
    }else{
        self.layer.borderColor = [UIColor clearColor].CGColor;

    }
}
-(UIButton*)giftXLabel{
    if (!_giftXLabel) {
        _giftXLabel = [UIButton buttonWithType:UIButtonTypeCustom];
        _giftXLabel.contentEdgeInsets = UIEdgeInsetsMake(0, 6, 0, 6);
        _giftXLabel.titleLabel.font = [UIFont systemFontOfSize:9.0f];
    }
    return _giftXLabel;
}
-(UILabel*)giftCountLabel{
    if (!_giftCountLabel) {
        _giftCountLabel = [[UILabel alloc]init];
        _giftCountLabel.textColor = UIColorHex(0x646167);
        _giftCountLabel.font = [UIFont systemFontOfSize:11.0f];
    }
    return _giftCountLabel;
}
-(UILabel*)giftNameLabel{
    if (!_giftNameLabel) {
        _giftNameLabel = [[UILabel alloc]init];
        _giftNameLabel.textColor = [UIColor whiteColor];
        _giftNameLabel.font = [UIFont systemFontOfSize:12.0f];
    }
    return _giftNameLabel;
}
-(UIImageView*)gifIconView{
    if (!_gifIconView) {
        _gifIconView = [[UIImageView alloc]init];
    }
    return _gifIconView;
}
-(UIView*)borderView{
    if (!_borderView) {
        _borderView = [[UIView alloc]init];
        _borderView.clipsToBounds = YES;
        _borderView.layer.cornerRadius = 8.0f;
        _borderView.layer.borderWidth = 1;
    }
    return _borderView;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

@end

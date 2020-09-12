//
//  CXLiveRoomRegularView.m
//  hairBall
//
//  Created by mahong yang on 2020/3/18.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomRegularView.h"

@interface CXLiveRoomRegularView()
@property (weak, nonatomic) IBOutlet UIView *regularBGView;

@property (weak, nonatomic) IBOutlet UILabel *regularLabel;
@property (weak, nonatomic) IBOutlet UIButton *upOrDownBtn;
@property (weak, nonatomic) IBOutlet UILabel *guizeLabel;

@end

@implementation CXLiveRoomRegularView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _guizeLabel.layer.masksToBounds = YES;
    _guizeLabel.layer.cornerRadius = 8;
    
    _regularBGView.layer.masksToBounds = YES;
    _regularBGView.layer.cornerRadius = 12;
    
    self.regular_height = 24;
}

- (void)setRegular:(NSString *)regular {
    _regular = regular;
    
    NSMutableAttributedString *attri = [[NSMutableAttributedString alloc] initWithString:regular];
    [attri addAttribute:NSForegroundColorAttributeName value:[UIColor clearColor] range:NSMakeRange(0, 2)];
    self.regularLabel.attributedText = attri;
    self.regularLabel.numberOfLines = 1;
    self.upOrDownBtn.tag = 10;
    [self.upOrDownBtn setImage:[UIImage imageNamed:@"liveroom_add"] forState:UIControlStateNormal];
}

- (IBAction)upOrDownAction:(UIButton *)sender {
    if (sender.tag == 10) {
        self.regularLabel.numberOfLines = 0;
        self.upOrDownBtn.tag = 11;
        [self.upOrDownBtn setImage:[UIImage imageNamed:@"liveroom_add"] forState:UIControlStateNormal];
        CGFloat height = [_regular sizeForFont:[UIFont systemFontOfSize:12] size:CGSizeMake(kScreenWidth - 80, 100) mode:0].height;
        self.regular_height = height + 8;
        [self mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(height);
        }];
        
    } else {
        self.regularLabel.numberOfLines = 1;
        self.upOrDownBtn.tag = 10;
        [self.upOrDownBtn setImage:[UIImage imageNamed:@"liveroom_add"] forState:UIControlStateNormal];
        self.regular_height = 24;
        [self mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.height.mas_equalTo(20);
        }];
    }
    
}


@end

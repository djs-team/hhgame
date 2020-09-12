//
//  CXInviteMikeView.m
//  hairBall
//
//  Created by mahong yang on 2019/11/2.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXInviteMikeView.h"

@interface CXInviteMikeView()
@property (weak, nonatomic) IBOutlet UIView *contentBGView;
@property (weak, nonatomic) IBOutlet UIView *oneView;
@property (weak, nonatomic) IBOutlet UIView *twoView;
@property (weak, nonatomic) IBOutlet UIView *oneBGView;
@property (weak, nonatomic) IBOutlet UIView *twoBGView;

@property (weak, nonatomic) IBOutlet UIImageView *matchmaker_avatar;
@property (weak, nonatomic) IBOutlet UILabel *matchmaker_nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *matchmaker_round_avatar;

@property (weak, nonatomic) IBOutlet UIView *guestView;
@property (weak, nonatomic) IBOutlet UIImageView *guest_avatar;
@property (weak, nonatomic) IBOutlet UIImageView *guest_round_avatar;
@property (weak, nonatomic) IBOutlet UILabel *guest_nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *guest_descLabel;

@property (weak, nonatomic) IBOutlet UIButton *agreeButton;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *matchmakerViewWidthLayout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *matchmakerViewCenterLayout;

@end

@implementation CXInviteMikeView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.contentBGView.layer.cornerRadius = 18;
    self.oneView.layer.cornerRadius = 11;
    self.twoView.layer.cornerRadius = 11;
    self.oneBGView.layer.cornerRadius = 13;
    self.twoBGView.layer.cornerRadius = 13;
}

- (void)setMikeModel:(CXInviteMikeModel *)mikeModel {
    _mikeModel = mikeModel;
    
    [self.matchmaker_avatar sd_setImageWithURL:[NSURL URLWithString:mikeModel.hongAvatar] placeholderImage:[UIImage imageNamed:@"applogo"]];
    [self.matchmaker_round_avatar sd_setImageWithURL:[NSURL URLWithString:mikeModel.hongAvatar] placeholderImage:[UIImage imageNamed:@"applogo"]];
    
    NSString *nameStr = [NSString stringWithFormat:@"主持%@", mikeModel.hongName];
    self.matchmaker_nameLabel.text = nameStr;
    
    CGFloat width = [nameStr sizeWithFont:[UIFont systemFontOfSize:10]].width;
    self.matchmakerViewWidthLayout.constant = MIN(32 + width + 12, 124);
    
    if (mikeModel.guestId && mikeModel.guestId.length > 0) {
        _guestView.hidden = NO;
        _matchmakerViewCenterLayout.constant = -69;
        [self.guest_avatar sd_setImageWithURL:[NSURL URLWithString:mikeModel.guestAvatar] placeholderImage:[UIImage imageNamed:@"applogo"]];
        [self.guest_round_avatar sd_setImageWithURL:[NSURL URLWithString:mikeModel.guestAvatar] placeholderImage:[UIImage imageNamed:@"applogo"]];
        
        self.guest_nameLabel.text = mikeModel.guestName;
        if (mikeModel.city && mikeModel.city.length > 0) {
            self.guest_descLabel.text = [NSString stringWithFormat:@"%@ | %@ | %@",mikeModel.age, mikeModel.city, mikeModel.stature];
        } else {
            self.guest_descLabel.text = [NSString stringWithFormat:@"%@ | %@",mikeModel.age, mikeModel.stature];
        }
        
    } else {
        _guestView.hidden = YES;
        _matchmakerViewCenterLayout.constant = 0;
    }
    
//    [self.agreeButton setTitle:@"同意" forState:UIControlStateNormal];
    
    if ([mikeModel.free integerValue] == 1) { // 免费
        [self.agreeButton setTitle:@"同意" forState:UIControlStateNormal];
    } else {
        NSString *str = [NSString stringWithFormat:@"同意(%@玫瑰)", mikeModel.micro_cost];
        [self.agreeButton setTitle:str forState:UIControlStateNormal];
    }

}

- (IBAction)closeAction:(id)sender {
    [self removeFromSuperview];
}

- (IBAction)cancelAction:(id)sender {
    if (self.cancelActionBlcok) {
        self.cancelActionBlcok(self.mikeModel);
    }
}

- (IBAction)agreeAction:(id)sender {
    if (self.agreeActionBlcok) {
        self.agreeActionBlcok(self.mikeModel);
    }
}
@end

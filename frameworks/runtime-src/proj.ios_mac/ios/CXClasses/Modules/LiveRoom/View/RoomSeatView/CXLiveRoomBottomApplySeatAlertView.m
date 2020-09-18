//
//  CXLiveRoomBottomApplySeatAlertView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/11.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomBottomApplySeatAlertView.h"

@interface CXLiveRoomBottomApplySeatAlertView()

@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *alertMessageLabel;
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIButton *sureBtn;

@end

@implementation CXLiveRoomBottomApplySeatAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _bgView.layer.masksToBounds = YES;
    _bgView.layer.cornerRadius = 20;
    
    _logo.layer.masksToBounds = YES;
    _logo.layer.cornerRadius = 15;
    _logo.layer.borderWidth = 0.5;
    _logo.layer.borderColor = UIColorHex(0xEF51B1).CGColor;
    
    _sureBtn.layer.masksToBounds = YES;
    _sureBtn.layer.cornerRadius = 9;
}

- (void)setMicroOrder:(SocketMessageMicroOrder *)microOrder {
    _microOrder = microOrder;
    [self.logo sd_setImageWithURL:[NSURL URLWithString:microOrder.MicroOrderData.User.HeadImageUrl]];
    self.alertMessageLabel.text = [NSString stringWithFormat:@"%@申请上麦", microOrder.MicroOrderData.User.Name];
}

- (IBAction)sureAction:(id)sender {
    if (self.applySeatAlertViewBlock) {
        self.applySeatAlertViewBlock(YES, NO, _microOrder);
    }
}

- (IBAction)closeAction:(id)sender {
    if (self.applySeatAlertViewBlock) {
        self.applySeatAlertViewBlock(NO, YES, _microOrder);
    }
}
@end

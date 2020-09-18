//
//  CXLiveRoomGuardianRuleViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/6/23.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomGuardianRuleViewController.h"

@interface CXLiveRoomGuardianRuleViewController ()
@property (weak, nonatomic) IBOutlet UILabel *ruleLabel;
@property (weak, nonatomic) IBOutlet UILabel *privilegeLabel;

@end

@implementation CXLiveRoomGuardianRuleViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"守护榜规则";
    
    self.view.backgroundColor = [UIColor whiteColor];
    
    self.ruleLabel.text = @"1.七日内为该用户赠送礼物价值达到520支玫瑰，且在守护榜排名前三的为该用户守护。\n2.每个用户最多可以有3个守护。\n3.一个用户可以成为多人的守护。";
    self.privilegeLabel.text = @"1.直播间进场特效\n2.守护专属头像框";
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end

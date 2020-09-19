//
//  CXMineBlockActionView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/28.
//

#import "CXMineBlockActionView.h"

@implementation CXMineBlockActionView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(190, 153));
    }];
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
}

- (IBAction)buttonActoin:(UIButton *)sender {
    if (self.blockActionBlock) {
        self.blockActionBlock(sender.tag);
    }
    [self hide];
}

@end

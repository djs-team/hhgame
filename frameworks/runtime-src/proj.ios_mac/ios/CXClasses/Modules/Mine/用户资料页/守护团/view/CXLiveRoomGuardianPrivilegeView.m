//
//  CXLiveRoomGuardianPrivilegeView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/1.
//

#import "CXLiveRoomGuardianPrivilegeView.h"

@implementation CXLiveRoomGuardianPrivilegeView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(282, 200));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeAlert;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
}

@end

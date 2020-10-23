//
//  CXUserInfoHeaderReusableView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXUserInfoHeaderReusableView.h"

@implementation CXUserInfoHeaderReusableView

- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor =[UIColor colorWithHexString:@"F8F8F8"];
        
        self.clipsToBounds = YES;
      
        [self setUpUI];
    }
    return self;
}
- (void)setUpUI{
    
    _avatar =[[UIImageView alloc] init];
    _avatar.contentMode = UIViewContentModeScaleAspectFill;
    [self addSubview:_avatar];
    
    [_avatar mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.bottom.left.right.equalTo(self);
//        make.top.mas_offset(-kStatusHeight);
    }];
}

@end

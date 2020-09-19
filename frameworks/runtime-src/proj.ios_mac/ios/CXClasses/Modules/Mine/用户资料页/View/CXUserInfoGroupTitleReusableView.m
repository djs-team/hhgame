//
//  CXUserInfoGroupTitleReusableView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXUserInfoGroupTitleReusableView.h"

@implementation CXUserInfoGroupTitleReusableView

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
       self.backgroundColor =[UIColor colorWithHexString:@"F8F8F8"];
        _groupTitle =[[UILabel alloc] init];
        _groupTitle.font=[UIFont systemFontOfSize:16 weight:UIFontWeightMedium];
        _groupTitle.textColor = [UIColor colorWithHexString:@"333333"];
        
        _MoreBtn =[[UIButton alloc] init];
        [_MoreBtn setTitle:@"更多礼物 >" forState:0];
        _MoreBtn.titleLabel.font =[UIFont systemFontOfSize:15];
        [_MoreBtn setTitleColor:[UIColor colorWithHexString:@"818181"] forState:0];
        [_MoreBtn addTarget:self action:@selector(moreBlockAction) forControlEvents:UIControlEventTouchUpInside];
        
        [self addSubview:_groupTitle];
        [self addSubview:_MoreBtn];
        
        
        [_groupTitle mas_makeConstraints:^(MASConstraintMaker *make) {
            make.left.equalTo(self).offset(15);
            make.top.bottom.right.equalTo(self);
        }];
        
        [_MoreBtn mas_makeConstraints:^(MASConstraintMaker *make) {
            make.right.equalTo(self).offset(-15);
            make.top.bottom.equalTo(self);
        }];
        
    }
    return self;
}

-(void)moreBlockAction{
    
    if (self.MoreBlock) {
        self.MoreBlock();
    }
    
}

@end

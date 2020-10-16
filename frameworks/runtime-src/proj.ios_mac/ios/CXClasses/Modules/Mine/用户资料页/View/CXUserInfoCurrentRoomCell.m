//
//  CXUserInfoCurrentRoomCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/10/16.
//

#import "CXUserInfoCurrentRoomCell.h"

@implementation CXUserInfoCurrentRoomCell
- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        self.backgroundColor =[UIColor colorWithHexString:@"F8F8F8"];
        [self setUpUI];
    }
    return self;
}
-(void)setUpUI{
 
    _bgView =[[UIImageView alloc] init];
    _bgView.layer.cornerRadius=70/2;
    _bgView.backgroundColor =[UIColor colorWithHexString:@"FF95A0"];
    
    _room_Icon =[[UIImageView alloc] init];
    _room_Icon.layer.cornerRadius= 50/2;
    _room_Icon.layer.borderColor=[UIColor whiteColor].CGColor;
    _room_Icon.contentMode = UIViewContentModeScaleAspectFill;
    _room_Icon.layer.borderWidth=1.5f;
    [_room_Icon.layer setMasksToBounds:YES];
    
    _top_label=[[UILabel alloc] init];
    _top_label.text=@"当前正在:";
    _top_label.textColor =[UIColor colorWithHexString:@"FFFFFF"];
    _top_label.font =[UIFont systemFontOfSize:16];
    
    _room_name =[[UILabel alloc] init];
    _room_name.text =@"Mr红颜";
    _room_name.textColor =[UIColor colorWithHexString:@"FFFFFF"];
    
    _arrow =[[UIImageView alloc] init];
    _arrow.backgroundColor =[UIColor redColor];
    
    _into_Room =[[UILabel alloc] init];
    _into_Room.text =@"进入房间";
    _into_Room.textColor =[UIColor colorWithHexString:@"FFFFFF"];
    
    [self addSubview:_bgView];
    [self addSubview:_room_Icon];
    [self addSubview:_top_label];
    [self addSubview:_room_name];
    [self addSubview:_arrow];
    [self addSubview:_into_Room];
    
    [_bgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self).offset(10);
        make.bottom.equalTo(self).offset(-10);
        make.left.equalTo(self).offset(14);
        make.right.equalTo(self).offset(-14);
    }];

    [_room_Icon mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self->_bgView).offset(5);
        make.top.equalTo(self->_bgView).offset(10);
        make.bottom.equalTo(self->_bgView).offset(-10);
        make.width.mas_equalTo(self->_room_Icon.mas_height);
    }];

    [_top_label mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self->_room_Icon.mas_right).offset(12);
        make.top.equalTo(self->_bgView).offset(10);
    }];

    [_room_name mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self->_room_Icon.mas_right).offset(12);
        make.bottom.equalTo(self->_bgView).offset(-10);
        make.right.equalTo(self).offset(-80);
    }];

    [_arrow mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self).offset(-34);
        make.centerY.equalTo(self.mas_centerY);
    }];

    [_into_Room mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self->_arrow.mas_left).offset(-6);
        make.centerY.equalTo(self->_arrow.mas_centerY);
    }];

}
@end

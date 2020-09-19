//
//  VideoSession.m
//  OpenLive
//
//  Created by GongYuhua on 2016/9/12.
//  Copyright © 2016年 Agora. All rights reserved.
//

#import "VideoSession.h"

@interface VideoSession ()

@property (strong , nonatomic) UIView * infoView;

@property (strong , nonatomic) UIView * roundView;

@property (strong , nonatomic) UILabel * redMotherNameLabel;

@property (strong , nonatomic) UILabel * nameLabel;

@property (strong , nonatomic) UILabel * infoLabel;

@end

@implementation VideoSession
- (instancetype)initWithUid:(NSUInteger)uid {
    if (self = [super init]) {
        self.uid = uid;
        
        self.hostingView = [[UIView alloc] init];
        self.hostingView.translatesAutoresizingMaskIntoConstraints = NO;
        self.hostingView.userInteractionEnabled = NO;
        self.canvas = [[AgoraRtcVideoCanvas alloc] init];
        self.canvas.uid = uid;
        self.canvas.view = self.hostingView;
        self.canvas.renderMode = AgoraVideoRenderModeHidden;
        
//        _infoView = [UIView new];
//        _infoView.userInteractionEnabled = NO;
//        [self.hostingView addSubview:_infoView];
//        _infoView.layer.masksToBounds = YES;
//        _infoView.layer.cornerRadius = 17;
//        _infoView.backgroundColor = [UIColor colorWithRed:102/255.0 green:102/255.0 blue:102/255.0 alpha:0.7];
//
//        _nameLabel = [UILabel new];
//        _nameLabel.text = @"名字";
//        _nameLabel.textColor = [UIColor whiteColor];
//        _nameLabel.font = [UIFont systemFontOfSize:10];
//        _nameLabel.textAlignment = NSTextAlignmentCenter;
//        [_infoView addSubview:_nameLabel];
//
//        _roundView = [UIView new];
//        _roundView.userInteractionEnabled = NO;
//        [_infoView addSubview:_roundView];
//        _roundView.layer.masksToBounds = YES;
//        _roundView.layer.cornerRadius = 14;
//        _roundView.backgroundColor = [UIColor colorWithHexString:@"d8d8d8"];
//
//        _infoLabel = [UILabel new];
//        _infoLabel.text = @"25岁|北京|180";
//        _infoLabel.textColor = [UIColor whiteColor];
//        _infoLabel.font = [UIFont systemFontOfSize:10];
//        _infoLabel.textAlignment = NSTextAlignmentCenter;
//        [_infoView addSubview:_infoLabel];
//
//        _redMotherNameLabel = [UILabel new];
//        _redMotherNameLabel.text = @"红娘";
//        _redMotherNameLabel.textColor = [UIColor whiteColor];
//        _redMotherNameLabel.font = [UIFont systemFontOfSize:10];
//        _redMotherNameLabel.textAlignment = NSTextAlignmentCenter;
//        [_infoView addSubview:_redMotherNameLabel];
//
//
//        [self.hostingView addSubview:_infoView];
//
//        [_infoView mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(self).offset(10);
//            make.bottom.mas_equalTo(self).offset(-2);
//            make.height.mas_equalTo(34);
//            make.width.mas_equalTo(110);
//        }];
//        [_roundView mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.left.mas_equalTo(self->_infoView.mas_left).offset(3);
//            make.top.mas_equalTo(self->_infoView.mas_top).offset(3);
//            make.bottom.mas_equalTo(self->_infoView.mas_bottom).offset(-3);
//            make.width.mas_equalTo(self->_roundView.mas_height).multipliedBy(1);
//        }];
//
//        [_redMotherNameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.centerY.mas_equalTo(self->_infoView.mas_centerY);
//            make.left.mas_equalTo(self->_roundView.mas_right).offset(8);
//        }];
//
//        [_nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.top.equalTo(self->_infoView.mas_top).offset(3);
//            make.left.mas_equalTo(self->_roundView.mas_left).offset(6);
//            make.right.mas_greaterThanOrEqualTo(self->_infoView.mas_right).offset(-5);
//        }];
//
//        [_infoLabel mas_makeConstraints:^(MASConstraintMaker *make) {
//            make.bottom.equalTo(self->_infoView.mas_bottom).offset(-3);
//            make.left.mas_equalTo(self->_roundView.mas_left).offset(6);
//            make.right.mas_greaterThanOrEqualTo(self->_infoView.mas_right).offset(-5);
//        }];
        
    }
    return self;
}

-(void)setModel:(LiveRoomMicroInfo *)model{
    
//    if (model.Type == ModelGameMicroInfoTypeHost) {
//        _redMotherNameLabel.hidden = NO;
//        _infoLabel.hidden = YES;
//        _nameLabel.hidden = YES;
//    }
//    else{
//        _infoLabel.hidden = NO;
//        _nameLabel.hidden = NO;
//        _redMotherNameLabel.hidden = YES;
//    }
}

+ (instancetype)localSession {
    return [[VideoSession alloc] initWithUid:0];
}
@end

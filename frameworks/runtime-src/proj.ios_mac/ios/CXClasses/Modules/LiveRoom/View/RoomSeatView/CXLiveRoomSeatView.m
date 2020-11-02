//
//  CXLiveRoomSeatView.m
//  hairBall
//
//  Created by mahong yang on 2020/3/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomSeatView.h"
#import "UIButton+CXCategory.h"

@interface CXLiveRoomSeatView()

@property (weak, nonatomic) IBOutlet UIImageView *seat_bgImageView;

@property (weak, nonatomic) IBOutlet UIButton *rose_Btn;
@property (weak, nonatomic) IBOutlet UIButton *seat_giftBtn;

@property (weak, nonatomic) IBOutlet UIView *seat_bottomView;
@property (weak, nonatomic) IBOutlet UIImageView *seat_bottomImage;
@property (weak, nonatomic) IBOutlet UILabel *seat_bottomName;
@property (weak, nonatomic) IBOutlet UILabel *seat_bottomDesc;

@end

@implementation CXLiveRoomSeatView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self setClipsToBounds:YES];
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 4;
    
    self.seat_rank_firstImage.layer.masksToBounds = YES;
    self.seat_rank_firstImage.layer.cornerRadius = 12;
    self.seat_rank_firstImage.layer.borderColor = UIColorHex(0xEF51B2).CGColor;
    self.seat_rank_firstImage.layer.borderWidth = 0.5;
    self.seat_rank_secondImage.layer.masksToBounds = YES;
    self.seat_rank_secondImage.layer.cornerRadius = 12;
    self.seat_rank_secondImage.layer.borderColor = UIColorHex(0xEF51B2).CGColor;
    self.seat_rank_secondImage.layer.borderWidth = 0.5;
    self.seat_rank_thirdImage.layer.masksToBounds = YES;
    self.seat_rank_thirdImage.layer.cornerRadius = 12;
    self.seat_rank_thirdImage.layer.borderColor = UIColorHex(0xEF51B2).CGColor;
    self.seat_rank_thirdImage.layer.borderWidth = 0.5;
    
    [self setupSubViews];
    
    _apply_seatBtn.titleLabel.numberOfLines = 0;
}

- (void)setModel:(LiveRoomMicroInfo *)model {
    _model = model;
    
    self.seat_addFriendBtn.hidden = YES;
    [_apply_seatBtn setImage:nil forState:UIControlStateNormal];
    [_apply_seatBtn setTitle:nil forState:UIControlStateNormal];
    
    self.rose_Btn.hidden = YES;
    self.seat_giftBtn.hidden = YES;
    
    self.seat_rank_thirdImage.hidden = YES;
    self.seat_rank_secondImage.hidden = YES;
    self.seat_rank_firstImage.hidden = YES;
    
    self.sitdownBtn.hidden = YES;
    self.muteBtn.hidden = YES;
    
    if (model.modelUser) {
        self.muteBtn.hidden = NO;
        _seat_bottomView.hidden = false;
        
        _seat_addFriendBtn.hidden = NO;
        _seat_giftBtn.hidden = NO;
        _rose_Btn.hidden = NO;
        
        [_seat_bgImageView sd_setImageWithURL:[NSURL URLWithString:model.modelUser.HeadImageUrl]];
        [_seat_bottomImage sd_setImageWithURL:[NSURL URLWithString:model.modelUser.HeadImageUrl]];
        _seat_bottomName.text = model.modelUser.Name;
        _seat_bottomDesc.text = [NSString stringWithFormat:@"%@ | %@ |%@", model.modelUser.Age ? [NSString stringWithFormat:@"%@岁" , model.modelUser.Age] : @"年龄" , model.modelUser.Stature ? model.modelUser.Stature : @"身高", model.modelUser.Sex == 1 ? @"男" : @"女"];
        
        if ([model.modelUser.UserId isEqualToString:[CXClientModel instance].userId]) {
            _seat_addFriendBtn.hidden = YES;
            _seat_giftBtn.hidden = YES;
            _rose_Btn.hidden = YES;
            if ([CXClientModel instance].agoraEngineManager.offMic == YES) { // 闭麦了
                [self.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_off"] forState:UIControlStateNormal];
            } else {
                [self.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
            }
        } else {
            if (model.isMute == YES) {
                [self.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_off"] forState:UIControlStateNormal];
            } else {
                [self.muteBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
            }
            
            if ([[CXClientModel instance].firendIdArrays containsObject:model.modelUser.UserId]) {
                self.seat_addFriendBtn.hidden = YES;
            }
            
            if ([CXClientModel instance].room.isHost) {
                self.sitdownBtn.hidden = NO;
            }
        }
        
        for (int i = 0; i < model.RoseRanks.count; i++) {
            if (i == 0) {
                self.seat_rank_secondImage.hidden = NO;
                self.seat_rank_secondImage.hidden = NO;
                [self.seat_rank_secondImage sd_setImageWithURL:[NSURL URLWithString:model.RoseRanks[i]]];
            } else if (i == 1) {
                self.seat_rank_firstImage.hidden = NO;
                [self.seat_rank_firstImage sd_setImageWithURL:[NSURL URLWithString:model.RoseRanks[i]]];
            } else {
                self.seat_rank_thirdImage.hidden = NO;
                [self.seat_rank_thirdImage sd_setImageWithURL:[NSURL URLWithString:model.RoseRanks[i]]];
            }
        }
    } else {
        _seat_bottomView.hidden = true;
        _seat_bgImageView.image = nil;
//        if ([CXClientModel instance].room.isConsertModel == NO) {
            
            if ([CXClientModel instance].room.isHost == YES) {
                [_apply_seatBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
                [_apply_seatBtn setTitle:@"邀请嘉宾上麦" forState:UIControlStateNormal];
            } else {
                if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) {
                    if (model.Type == LiveRoomMicroInfoTypeMan) {
                        if ([CXClientModel instance].room.WheatCardCount.integerValue > 0) {
                            [_apply_seatBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
                            [_apply_seatBtn setTitle:[NSString stringWithFormat:@"申请上麦\n上麦卡*%@", [CXClientModel instance].room.WheatCardCount.stringValue] forState:UIControlStateNormal];
                        } else {
                            [_apply_seatBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
                            [_apply_seatBtn setTitle:[NSString stringWithFormat:@"申请上麦\n%@朵玫瑰", [CXClientModel instance].room.OnMicroCost.stringValue] forState:UIControlStateNormal];
                        }
                    } else {
                        [_apply_seatBtn setImage:[UIImage imageNamed:@"shafa"] forState:UIControlStateNormal];
                        [_apply_seatBtn setTitle:@"女嘉宾专属位" forState:UIControlStateNormal];
                    }
                } else {
                    if ([CXClientModel instance].sex.integerValue == 2) { // 女的
                        [_apply_seatBtn setTitle:@"申请上麦" forState:UIControlStateNormal];
                    } else {
                        if ([CXClientModel instance].room.WheatCardCount.integerValue > 0) {
                            [_apply_seatBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
                            [_apply_seatBtn setTitle:[NSString stringWithFormat:@"申请上麦\n上麦卡*%@", [CXClientModel instance].room.WheatCardCount.stringValue] forState:UIControlStateNormal];
                        } else {
                            [_apply_seatBtn setImage:[UIImage imageNamed:@"liveroom_seat_micro_on"] forState:UIControlStateNormal];
                            [_apply_seatBtn setTitle:[NSString stringWithFormat:@"申请上麦\n%@朵玫瑰", [CXClientModel instance].room.OnMicroCost.stringValue] forState:UIControlStateNormal];
                        }
                    }
                }
            }
            

            [_apply_seatBtn setIconInTopWithSpacing:8];
//        }
    }
}

- (void)addAgoraRtc:(NSNumber *)uid {
    
    self.session = [[VideoSession alloc] initWithUid:uid.integerValue];
        
    [self insertSubview:self.session.hostingView aboveSubview:self.seat_bgImageView];

    if ([uid isEqualToNumber:[[CXClientModel instance].userId numberValue]] ) {
        int success = [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleBroadcaster];
        [[CXClientModel instance].agoraEngineManager.engine setupLocalVideo:self.session.canvas];
        [[CXClientModel instance].agoraEngineManager.engine startPreview];
    } else {
        [[CXClientModel instance].agoraEngineManager.engine setClientRole:AgoraClientRoleAudience];
        [[CXClientModel instance].agoraEngineManager.engine setupRemoteVideo:self.session.canvas];
    }
    
    [self.session.hostingView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.center.width.height.equalTo(self.seat_bgImageView);
    }];
}

- (void)deleteAgoraRtc{
    if (self.session.canvas.uid == [[CXClientModel instance].userId integerValue]) {
        [[CXClientModel instance].agoraEngineManager.engine setupLocalVideo:nil];
        
        [[CXClientModel instance].agoraEngineManager.engine stopPreview];
                
        [CXClientModel instance].agoraEngineManager.offMic = YES;
    }
    
    [self.session.hostingView removeFromSuperview];
    
    self.session.hostingView = nil;
    
    [self.session.canvas.view removeFromSuperview];
    self.session.canvas.view = nil;
}


- (void)setupSubViews {
    self.seat_bottomView.layer.masksToBounds = true;
    self.seat_bottomView.layer.cornerRadius = 14;
    
    self.seat_bottomImage.layer.masksToBounds = true;
    self.seat_bottomImage.layer.cornerRadius = 12.5;
    
    self.seat_addFriendBtn.layer.masksToBounds = true;
    self.seat_addFriendBtn.layer.cornerRadius = 9;
    
    self.sitdownBtn.layer.masksToBounds = true;
    self.sitdownBtn.layer.cornerRadius = 10;
}

- (IBAction)seatViewAction:(UIButton *)sender {
    if (self.seatViewActionBlock) {
        self.seatViewActionBlock(sender.tag, self.model);
    }
}


@end

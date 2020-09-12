//
//  CXGameMusicChooseSongerView.m
//  hairBall
//
//  Created by mahong yang on 2020/2/11.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicChooseSongerView.h"
#import "CXGameMusicSongerSeatView.h"

@implementation CXGameMusicChooseSongerView

- (void)awakeFromNib{
    
    [super awakeFromNib];
    self.attachedView.mm_dimBackgroundView.backgroundColor = MMHexColor(0x0000004F);
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    
    self.type = MMPopupTypeCustom;
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(330, 380));
    }];

    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
}

- (void)setRoom:(CXLiveRoomModel *)room {
    _room = room;
    kWeakSelf
    if ([room.users allKeys].count == 1) {
        CXGameMusicSongerSeatView *seatView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerSeatView" owner:nil options:nil].lastObject;
        [self addSubview:seatView];
        seatView.user = room.users.allValues[0];
        [seatView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.mas_equalTo(CGSizeMake(120, 130));
            make.center.mas_equalTo(0);
        }];
        
        seatView.songerSeatSureActionBlock = ^(LiveRoomUser * _Nonnull user) {
            if ([weakSelf songerPlayBlock]) {
                weakSelf.songerPlayBlock(user);
                [weakSelf hide];
                [MMPopupView hideAll];
            }
        };
        
    } else if ([room.users allKeys].count == 2) {
        for (NSString *userId in [room.users allKeys]) {
            NSIndexPath *indexPath = [[CXClientModel instance].room.userSeats objectForKey:userId];
            if (indexPath.row == 0 && indexPath.section == 0) {
                CXGameMusicSongerSeatView *seatView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerSeatView" owner:nil options:nil].lastObject;
                [self addSubview:seatView];
                seatView.user = [room.users valueForKey:userId];
                [seatView mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.size.mas_equalTo(CGSizeMake(120, 144));
                    make.centerY.mas_equalTo(0);
                    make.left.mas_equalTo(35);
                }];
                
                seatView.songerSeatSureActionBlock = ^(LiveRoomUser * _Nonnull user) {
                    if ([weakSelf songerPlayBlock]) {
                        weakSelf.songerPlayBlock(user);
                        [weakSelf hide];
                        [MMPopupView hideAll];
                    }
                };
            } else {
                CXGameMusicSongerSeatView *seatView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerSeatView" owner:nil options:nil].lastObject;
                [self addSubview:seatView];
                seatView.user =  [room.users valueForKey:userId];
                [seatView mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.size.mas_equalTo(CGSizeMake(120, 144));
                    make.centerY.mas_equalTo(0);
                    make.right.mas_equalTo(-35);
                }];
                
                seatView.songerSeatSureActionBlock = ^(LiveRoomUser * _Nonnull user) {
                    if ([weakSelf songerPlayBlock]) {
                        weakSelf.songerPlayBlock(user);
                        [weakSelf hide];
                        [MMPopupView hideAll];
                    }
                };
            }
        }
        
    } else {
        for (NSString *userId in [room.users allKeys]) {
            NSIndexPath *indexPath = [[CXClientModel instance].room.userSeats objectForKey:userId];
            if (indexPath.row == 0 && indexPath.section == 0) {
                CXGameMusicSongerSeatView *seatView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerSeatView" owner:nil options:nil].lastObject;
                [self addSubview:seatView];
                seatView.user = [room.users valueForKey:userId];
                [seatView mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.size.mas_equalTo(CGSizeMake(120, 130));
                    make.centerX.mas_equalTo(0);
                    make.top.mas_equalTo(70);
                }];
                
                seatView.songerSeatSureActionBlock = ^(LiveRoomUser * _Nonnull user) {
                    if ([weakSelf songerPlayBlock]) {
                        weakSelf.songerPlayBlock(user);
                        [weakSelf hide];
                        [MMPopupView hideAll];
                    }
                };
            } else if (indexPath.row == 0 && indexPath.section == 1) {
                CXGameMusicSongerSeatView *seatView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerSeatView" owner:nil options:nil].lastObject;
                [self addSubview:seatView];
                seatView.user = [room.users valueForKey:userId];
                [seatView mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.size.mas_equalTo(CGSizeMake(120, 144));
                    make.top.mas_equalTo(211);
                    make.left.mas_equalTo(35);
                }];
                
                seatView.songerSeatSureActionBlock = ^(LiveRoomUser * _Nonnull user) {
                    if ([weakSelf songerPlayBlock]) {
                        weakSelf.songerPlayBlock(user);
                        [weakSelf hide];
                        [MMPopupView hideAll];
                    }
                };
            } else {
                CXGameMusicSongerSeatView *seatView = [[NSBundle mainBundle] loadNibNamed:@"CXGameMusicSongerSeatView" owner:nil options:nil].lastObject;
                [self addSubview:seatView];
                seatView.user = [room.users valueForKey:userId];
                [seatView mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.size.mas_equalTo(CGSizeMake(120, 144));
                    make.top.mas_equalTo(211);
                    make.right.mas_equalTo(-35);
                }];
                
                seatView.songerSeatSureActionBlock = ^(LiveRoomUser * _Nonnull user) {
                    if ([weakSelf songerPlayBlock]) {
                        weakSelf.songerPlayBlock(user);
                        [weakSelf hide];
                        [MMPopupView hideAll];
                    }
                };
            }
        }
    }
}

@end

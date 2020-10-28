//
//  CXLiveRoomSeatsView.m
//  hairBall
//
//  Created by mahong yang on 2020/3/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomSeatsView.h"

static const int space = 8;
static const int leading = 12;

@interface CXLiveRoomSeatsView()

@property (nonatomic, strong) NSIndexPath *songerIndexPath;

@end

@implementation CXLiveRoomSeatsView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.seats = [[NSMutableDictionary alloc] initWithCapacity:[CXClientModel instance].room.seats.count];
    
    _songerIndexPath = [NSIndexPath indexPathForRow:-1 inSection:-1];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    if ([CXClientModel instance].room.isConsertModel == NO) { // 普通模式
        
        [_seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, CXLiveRoomSeatView * _Nonnull obj, BOOL * _Nonnull stop) {
            switch (key.section) {
                case LiveRoomMicroInfoTypeHost: {
                    [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                        make.size.mas_equalTo([CXClientModel instance].room.RoomData.hostSize);
                        make.top.mas_offset(0);
                        if ([CXClientModel instance].room.seats.count < 3) {
                            make.left.mas_equalTo(0);
                        } else {
                            make.left.mas_equalTo((kScreenWidth - [CXClientModel instance].room.RoomData.hostSize.width)/2);
                        }
                    }];
                                        
                }
                    break;
                case LiveRoomMicroInfoTypeMan: {
                    [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                        make.size.mas_equalTo([CXClientModel instance].room.RoomData.manSize);
                        if ([CXClientModel instance].room.seats.count < 3) {
                            make.top.mas_equalTo(0);
                            make.right.mas_equalTo(0);
                        } else {
                            make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+space);
                            make.left.mas_equalTo(leading);
                        }
                        
                    }];
                }
                    break;
                case LiveRoomMicroInfoTypeWomen: {
                    [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                        make.size.mas_equalTo([CXClientModel instance].room.RoomData.womanSize);
                        if ([CXClientModel instance].room.seats.count < 3) {
                            make.top.mas_equalTo(0);
                            make.right.mas_equalTo(0);
                        } else {
                            make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+space);
                            make.right.mas_equalTo(-leading);
                        }
                    }];
                }
                    break;
                case LiveRoomMicroInfoTypeSofa: {
                    [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                        make.size.mas_equalTo([CXClientModel instance].room.RoomData.sofaSize);
                        make.top.mas_offset(0);
                        make.left.mas_equalTo(kScreenWidth/2+space/2);
                    }];
                }
                    break;
                case LiveRoomMicroInfoTypeNormal: {
                    switch (key.row) {
                        case 0:{
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.normalSize);
                                if ([CXClientModel instance].room.seats.count < 3) {
                                    make.top.mas_equalTo(0);
                                    make.right.mas_equalTo(0);
                                } else {
                                    make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+space);
                                    make.left.mas_equalTo(leading);
                                }
                                
                            }];
                            
                        }
                            break;
                        case 1: {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.normalSize);
                                if ([CXClientModel instance].room.seats.count < 3) {
                                    make.top.mas_equalTo(0);
                                    make.right.mas_equalTo(0);
                                } else {
                                    make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+space);
                                    make.right.mas_equalTo(-leading);
                                }
                            }];
                            
                        }
                            break;
                        case 2: {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.normalSize);
                                make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+1);
                                make.right.mas_equalTo(-(kScreenWidth - [CXClientModel instance].room.RoomData.hostSize.width*2-space)/2);
                            }];
                        }
                            break;
                        case 3: {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.normalSize);
                                make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+1+[CXClientModel instance].room.RoomData.normalSize.height+1);
                                make.left.mas_equalTo((kScreenWidth - [CXClientModel instance].room.RoomData.hostSize.width*2-space)/2);
                            }];
                        }
                            break;
                        case 4: {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.normalSize);
                                make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+1+[CXClientModel instance].room.RoomData.normalSize.height+1);
                                make.left.mas_equalTo([CXClientModel instance].room.RoomData.normalSize.width+space/2);
                            }];
                        }
                            break;
                        case 5: {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.normalSize);
                                make.top.mas_offset([CXClientModel instance].room.RoomData.hostSize.height+space+[CXClientModel instance].room.RoomData.normalSize.height+space);
                                make.right.mas_equalTo(0);
                            }];
                        }
                            break;
                        default:
                            break;
                    }
                }
                    break;
            }
        }];
    } else { // 伴唱模式
        [_seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, CXLiveRoomSeatView * _Nonnull obj, BOOL * _Nonnull stop) {
            switch (key.section) {
                case LiveRoomMicroInfoTypeHost: {
                    if (_songerIndexPath == key) {
                        [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_songerSize);
                            make.top.mas_equalTo(0);
                            make.right.mas_equalTo(0);
                        }];
                    } else {
                        [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                            make.top.mas_equalTo(0);
                            make.left.mas_equalTo(0);
                        }];
                    }
                }
                    break;
                case LiveRoomMicroInfoTypeMan: {
                    if (_songerIndexPath == key) {
                        [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_songerSize);
                            make.top.mas_equalTo(0);
                            make.right.mas_equalTo(0);
                        }];
                    } else {
                        if ([CXClientModel instance].room.seats.count < 3) {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                make.top.mas_equalTo(0);
                                make.left.mas_equalTo(0);
                            }];
                        } else {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                make.top.mas_offset([CXClientModel instance].room.RoomData.music_normalSize.height+space);
                                make.left.mas_offset(0);
                            }];
                        }
                    }
                    
                }
                    break;
                case LiveRoomMicroInfoTypeWomen: {
                    if (_songerIndexPath == key) {
                        [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_songerSize);
                            make.top.mas_equalTo(0);
                            make.right.mas_equalTo(0);
                        }];
                    } else {
                        if ([CXClientModel instance].room.seats.count < 3) {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                make.top.mas_equalTo(0);
                                make.right.mas_equalTo(0);
                            }];
                        } else {
                            if (_songerIndexPath.section == LiveRoomMicroInfoTypeHost) {
                                [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                    make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                    make.top.mas_offset(0);
                                    make.left.mas_offset(0);
                                }];
                            } else {
                                [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                    make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                    make.top.mas_offset([CXClientModel instance].room.RoomData.music_normalSize.height+space);
                                    make.left.mas_offset(0);
                                }];
                            }
                        }
                    }
                    
                }
                    break;
                case LiveRoomMicroInfoTypeSofa: {
                    if (_songerIndexPath == key) {
                        [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_songerSize);
                            make.top.mas_equalTo(0);
                            make.right.mas_equalTo(0);
                        }];
                    } else if (_songerIndexPath.section == LiveRoomMicroInfoTypeHost) {
                        [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                            make.top.mas_equalTo(0);
                            make.left.mas_equalTo(0);
                        }];
                    } else {
                        [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                            make.size.mas_equalTo(CGSizeMake([CXClientModel instance].room.RoomData.music_normalSize.width, [CXClientModel instance].room.RoomData.music_normalSize.height - space/2));
                            make.top.mas_offset([CXClientModel instance].room.RoomData.music_normalSize.height+space);
                            make.left.mas_offset(0);
                        }];
                    }
                }
                    break;
                case LiveRoomMicroInfoTypeNormal: {
                    if (key.row == 0) { // 1号位
                        if (_songerIndexPath == key) {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_songerSize);
                                make.top.mas_equalTo(0);
                                make.right.mas_equalTo(0);
                            }];
                        } else {
                            if ([CXClientModel instance].room.seats.count < 3) {
                                [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                    make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                    make.top.mas_equalTo(0);
                                    make.left.mas_equalTo(0);
                                }];
                            } else {
                                [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                    make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                    make.top.mas_offset([CXClientModel instance].room.RoomData.music_normalSize.height+space);
                                    make.left.mas_offset(0);
                                }];
                            }
                        }
                    } else {
                        if (_songerIndexPath == key) {
                            [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_songerSize);
                                make.top.mas_equalTo(0);
                                make.right.mas_equalTo(0);
                            }];
                        } else {
                            if ([CXClientModel instance].room.seats.count < 3) {
                                [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                    make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                    make.top.mas_equalTo(0);
                                    make.right.mas_equalTo(0);
                                }];
                            } else {
                                if (_songerIndexPath.section == LiveRoomMicroInfoTypeHost) {
                                    [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                        make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                        make.top.mas_offset(0);
                                        make.left.mas_offset(0);
                                    }];
                                } else {
                                    [obj mas_remakeConstraints:^(MASConstraintMaker *make) {
                                        make.size.mas_equalTo([CXClientModel instance].room.RoomData.music_normalSize);
                                        make.top.mas_offset([CXClientModel instance].room.RoomData.music_normalSize.height+space);
                                        make.left.mas_offset(0);
                                    }];
                                }
                            }
                        }
                    }
                    
                }
                    break;
            }
        }];
    }
}

- (void)reloadRoomSeat {
    [_seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, CXLiveRoomSeatView * _Nonnull obj, BOOL * _Nonnull stop) {
        obj.model = obj.model;
    }];
}

- (void)reloadSeatsLocation {
    if ([CXClientModel instance].room.isConsertModel == YES) { // 伴唱模式
        [_model.seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, LiveRoomMicroInfo * _Nonnull obj, BOOL * _Nonnull stop) {
            if (obj.modelUser && [obj.modelUser.UserId integerValue] == [CXClientModel instance].room.playing_SongInfo.ConsertUserId) {
                _songerIndexPath = key;
                *stop = YES;
                [self setNeedsLayout];
            }
        }];
    } else {
        [self setNeedsLayout];
    }
    
    [self reloadRoomSeat];
}

- (void)reloadFirends {
    if (_seats) {
        [_seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, CXLiveRoomSeatView * _Nonnull obj, BOOL * _Nonnull stop) {
            if (obj.model.modelUser.UserId.length > 0) {
                if ([obj.model.modelUser.UserId isEqualToString:[CXClientModel instance].userId]) {
                    obj.seat_addFriendBtn.hidden = YES;
                } else if ([[CXClientModel instance].firendIdArrays containsObject:obj.model.modelUser.UserId]) {
                    obj.seat_addFriendBtn.hidden = YES;
                } else {
                    obj.seat_addFriendBtn.hidden = NO;
                }
            } else {
                obj.seat_addFriendBtn.hidden = YES;
            }
            
        }];
    }
}

- (void)setModel:(CXLiveRoomModel *)model {
    _model = model;
    
    [_seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, CXLiveRoomSeatView * _Nonnull obj, BOOL * _Nonnull stop) {
        [obj removeFromSuperview];
    }];
    
    _seats = [NSMutableDictionary new];
    [model.seats enumerateKeysAndObjectsUsingBlock:^(NSIndexPath * _Nonnull key, LiveRoomMicroInfo * _Nonnull obj, BOOL * _Nonnull stop) {
        CXLiveRoomSeatView * seatView = [self createSeatView:obj];
        
        [self addSubview:seatView];
        
        if (seatView.model.modelUser && [seatView.model.modelUser.UserId integerValue] == [CXClientModel instance].room.playing_SongInfo.ConsertUserId) {
            _songerIndexPath = key;
        }
        // 如果已经静音了此人，重连静音
        if ([self.isMuteArrays containsObject:obj.modelUser.UserId]) {
            if ([[CXClientModel instance].agoraEngineManager.engine muteRemoteAudioStream:[obj.modelUser.UserId integerValue] mute:YES] == 0) {
                obj.isMute = YES;
            }
        }
        if (obj.modelUser) {
            [seatView addAgoraRtc:[obj.modelUser.UserId numberValue]];
        }
    }];
    
    [self setNeedsLayout];
    
}

- (CXLiveRoomSeatView*)createSeatView:(LiveRoomMicroInfo*)seat {
    CXLiveRoomSeatView * seatView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomSeatView" owner:self options:nil].lastObject;
    seatView.model = seat;
    NSIndexPath *index = [NSIndexPath indexPathForRow:seat.Number inSection:seat.Type];
    [self.seats setObject:seatView forKey:index];
    seatView.seatViewActionBlock = ^(NSInteger tag, LiveRoomMicroInfo * _Nonnull microInfo) {
        if (self.seatViewActionBlock) {
            self.seatViewActionBlock(tag, microInfo);
        }
    };
    
    return seatView;
}

- (IBAction)liveRoomSeatsBtnAction:(UIButton *)sender {
    if (self.seatViewActionBlock) {
        self.seatViewActionBlock(sender.tag, [LiveRoomMicroInfo new]);
    }
}

- (IBAction)recommendAction:(id)sender {
    if (self.recommendActionBlock) {
        self.recommendActionBlock();
    }
}


@end

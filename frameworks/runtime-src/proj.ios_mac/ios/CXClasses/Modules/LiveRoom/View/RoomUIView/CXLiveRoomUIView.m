//
//  CXLiveRoomUIView.m
//  hairBall
//
//  Created by mahong yang on 2020/3/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomUIView.h"
#import "CXLiveRoomRegularView.h"
#import "UIImage+GIF.h"
#import "SDCycleScrollView.h"
#import "UIButton+CXCategory.h"

@interface CXLiveRoomUIView() <SDCycleScrollViewDelegate>

// Top
@property (weak, nonatomic) IBOutlet UIView *top_roomBgView;
@property (weak, nonatomic) IBOutlet UIImageView *top_roomAvatar;
@property (weak, nonatomic) IBOutlet UIButton *top_roomHotBtn;

@property (weak, nonatomic) IBOutlet UIView *top_shouhuBgView;
@property (weak, nonatomic) IBOutlet UILabel *top_shouhuLabel;
@property (weak, nonatomic) IBOutlet UIImageView *top_oneImage;
@property (weak, nonatomic) IBOutlet UIImageView *top_twoImage;
@property (weak, nonatomic) IBOutlet UIImageView *top_threeImage;

// Bottom
@property (weak, nonatomic) IBOutlet UIButton *bottom_messageBtn;
@property (weak, nonatomic) IBOutlet UIButton *bottom_friendBtn;
@property (weak, nonatomic) IBOutlet UIButton *bottom_musicBtn;

// middle
@property (weak, nonatomic) IBOutlet UIButton *applySeatBtn;
@property (weak, nonatomic) IBOutlet UIButton *pkSeatBtn;
@property (weak, nonatomic) IBOutlet UIButton *recommendBtn;

@property (weak, nonatomic) IBOutlet SDCycleScrollView *cycleScrollView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *cycleScrollViewBottomLayout;

@property (nonatomic, strong) CXLiveRoomRegularView *regularView;

@end

@implementation CXLiveRoomUIView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _top_roomBgView.layer.masksToBounds = YES;
    _top_roomBgView.layer.cornerRadius = 15;
    _top_roomAvatar.layer.masksToBounds = YES;
    _top_roomAvatar.layer.cornerRadius = 13;
    
    _top_shouhuBgView.layer.masksToBounds = YES;
    _top_shouhuBgView.layer.cornerRadius = 15;
    _top_shouhuLabel.layer.masksToBounds = YES;
    _top_shouhuLabel.layer.cornerRadius = 12;
    _top_oneImage.layer.masksToBounds = YES;
    _top_oneImage.layer.cornerRadius = 10;
    _top_twoImage.layer.masksToBounds = YES;
    _top_twoImage.layer.cornerRadius = 10;
    _top_threeImage.layer.masksToBounds = YES;
    _top_threeImage.layer.cornerRadius = 10;
    
    _applySeatBtn.layer.masksToBounds = YES;
    _applySeatBtn.layer.cornerRadius = 18;
    _pkSeatBtn.layer.masksToBounds = YES;
    _pkSeatBtn.layer.cornerRadius = 18;
    
    _recommendBtn.layer.masksToBounds = YES;
    _recommendBtn.layer.cornerRadius = 10;
    [_recommendBtn setTitle:@"为\n你\n推\n荐" forState:UIControlStateNormal];
    _recommendBtn.titleLabel.numberOfLines = 4;
    _recommendBtn.imageView.size = CGSizeMake(6, 10);
    
    _roomRedpackBtn.layer.masksToBounds = YES;
    _roomRedpackBtn.layer.cornerRadius = 10;
    [_roomRedpackBtn setTitle:@"红\n包" forState:UIControlStateNormal];
    _roomRedpackBtn.titleLabel.numberOfLines = 2;
    _roomRedpackBtn.imageView.size = CGSizeMake(6, 10);
    [_roomRedpackBtn setIconInRightWithSpacing:0];
    
    _bottom_messageBtn.layer.cornerRadius = 19;
    _bottom_messageBtn.layer.masksToBounds = YES;
    
    // CycleScrollView
    self.cycleScrollView.hidden = YES;
    self.cycleScrollView.layer.masksToBounds = YES;
    self.cycleScrollView.layer.cornerRadius = 10;
    self.cycleScrollView.bannerImageViewContentMode = UIViewContentModeScaleAspectFit;
    self.cycleScrollView.delegate = self;
    self.cycleScrollView.pageControlDotSize = CGSizeMake(4, 4);
    self.cycleScrollView.pageControlBottomOffset = -6;
    self.cycleScrollView.backgroundColor = rgba(255, 255, 255, 0.6);
    
    [self addSubview:self.regularView];
    [_regularView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.top.height.right.mas_equalTo(0);
    }];
    
    _messageListView = [GameMessageListView new];
    _messageListView.frame = CGRectZero;
    [self addSubview:_messageListView];
    kWeakSelf
    self.messageListView.clickUserInfoBlock = ^(NSString * _Nonnull userID) {
        if (weakSelf.messageClickUserInfoBlock) {
            weakSelf.messageClickUserInfoBlock(userID);
        }
    };
    
    self.messageListView.longPressUserInfoBlock = ^(NSString * _Nonnull userName) {
        if (userName.length > 0) {
            if (weakSelf.messageLongPressUserInfoBlock) {
                weakSelf.messageLongPressUserInfoBlock(userName);
            }
        }
    };
    
    [self insertSubview:self.roomSeatsView atIndex:1];
    self.roomSeatsView.backgroundColor = [UIColor clearColor];
    [self.roomSeatsView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.mas_equalTo(0);
        make.height.mas_equalTo([CXClientModel instance].room.RoomData.seatsSizeHeight);
        make.top.mas_equalTo(kNavHeight+[CXClientModel instance].room.horseLampHeight);
    }];
    
    [self addSubview:self.musicLRCShowView];
    [self.musicLRCShowView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.height.right.mas_equalTo(0);
        make.bottom.equalTo(self.regularView.mas_top).offset(-5);
    }];
    
    self.top_roomHotBtn.hidden = YES;
    
}

- (void)reloadLayoutSubViews {
    if ([CXClientModel instance].room.isConsertModel == NO) {
        [self.regularView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo([CXClientModel instance].room.RoomData.regular_top);
            make.left.mas_equalTo(0);
            make.right.mas_equalTo(0);
            make.height.mas_equalTo(MAX(self.regularView.regular_height, 24));
        }];
        
        switch ([CXClientModel instance].room.seats.count) {
            case 1:
            {
                [self.roomSeatsView mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.left.right.top.mas_equalTo(0);
                    make.height.mas_equalTo([CXClientModel instance].room.RoomData.seatsSizeHeight);
                }];
            }
                break;
            default:
            {
                [self.roomSeatsView mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.left.right.mas_equalTo(0);
                    make.top.mas_equalTo(kNavHeight+[CXClientModel instance].room.horseLampHeight);
                    make.height.mas_equalTo([CXClientModel instance].room.RoomData.seatsSizeHeight);
                }];
            }
                break;
        }
        
        [self.musicLRCShowView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.bottom.height.right.mas_equalTo(0);
        }];
        
    } else {
        [self.regularView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.top.mas_equalTo([CXClientModel instance].room.RoomData.regular_top+52);
            make.left.mas_equalTo(0);
            make.right.mas_equalTo(-90);
            make.height.mas_equalTo(MAX(self.regularView.regular_height, 24));
        }];
        
        switch ([CXClientModel instance].room.seats.count) {
            case 1:
            {
                [self.roomSeatsView mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.left.right.top.mas_equalTo(0);
                    make.height.mas_equalTo([CXClientModel instance].room.RoomData.music_seatsSizeHeight);
                }];
            }
                break;
            default:
            {
                [self.roomSeatsView mas_remakeConstraints:^(MASConstraintMaker *make) {
                    make.left.right.mas_equalTo(0);
                    make.top.mas_equalTo(kNavHeight+[CXClientModel instance].room.horseLampHeight);
                    make.height.mas_equalTo([CXClientModel instance].room.RoomData.music_seatsSizeHeight);
                }];
            }
                break;
        }
        
        [self.musicLRCShowView mas_remakeConstraints:^(MASConstraintMaker *make) {
            make.left.right.mas_equalTo(0);
            make.height.mas_equalTo(42);
            make.bottom.equalTo(self.regularView.mas_top).offset(-5);
        }];
    }
    
    [_messageListView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.regularView.mas_bottom).offset(8);
        make.left.mas_equalTo(15);
        make.bottom.mas_equalTo(-49-kBottomArea);
        make.right.mas_equalTo(-100);
    }];
    
    [self.roomSeatsView reloadSeatsLocation];
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    [self reloadLayoutSubViews];
}

- (void)setModel:(CXLiveRoomModel *)model {
    _model = model;
    
    _top_roomNameLabel.text = model.RoomData.RoomName;
    [_top_roomAvatar sd_setImageWithURL:[NSURL URLWithString:model.RoomOwnerHeadUrl]];
    _top_roomNameWidthLayout.constant = [model.RoomData.RoomName sizeWithFont:[UIFont systemFontOfSize:16]].width + 44;
    
    NSString *regular = [NSString stringWithFormat:@"规则规则%@", [CXClientModel instance].room.Tips];
    self.regularView.regular = regular;
    
    self.ranks = model.Ranks;
    
    NSInteger heatValue = [model.RoomData.VisitorNum integerValue] + [model.RoomData.ExternVisitorNumbers integerValue];
    if (heatValue < 100000) {
        [_top_roomHotBtn setTitle:[NSString stringWithFormat:@" %ld", heatValue] forState:UIControlStateNormal];
    } else {
        [_top_roomHotBtn setTitle:[NSString stringWithFormat:@" %ld万", heatValue / 10000] forState:UIControlStateNormal];
    }
    
    self.roomSeatsView.model = model;
    
    [self setNeedsLayout];
}

- (void)setIsChangeMir:(BOOL)isChangeMir{
    _isChangeMir = isChangeMir;
    
    NSString * loginUserId = [CXClientModel instance].userId;
    LiveRoomMicroInfo * microInfo = [[CXClientModel instance].room microInfoForUser:loginUserId];

    NSString *applySeatBtn_title = @"";
    NSString *applySeatBtn_count = @"";
    NSString *applySeatBtn_desc = @"";
    if (microInfo &&  microInfo.Type == LiveRoomMicroInfoTypeHost) {//是房主
        applySeatBtn_title = @"上麦管理";
        NSInteger count = [[CXClientModel instance].room.rightOrders allKeys].count + [[CXClientModel instance].room.leftOrders allKeys].count;
        applySeatBtn_count = [NSString stringWithFormat:@"%ld人申请中", count];
//        applySeatBtn_desc = @"人申请中";
        
    } else if (microInfo) { // 在相亲了
        applySeatBtn_title = @"下麦";
    } else {
        if ([CXClientModel instance].room.WheatCardCount.integerValue > 0) {
            applySeatBtn_count = [NSString stringWithFormat:@"x%@上麦卡",[CXClientModel instance].room.WheatCardCount.stringValue];
            applySeatBtn_title = @"免费上麦";
        } else if ([CXClientModel instance].room.OnMicroCost.integerValue > 0) {
            applySeatBtn_count = [NSString stringWithFormat:@"%@朵玫瑰",[CXClientModel instance].room.OnMicroCost.stringValue];
            applySeatBtn_title = @"申请上麦";
        } else {
            applySeatBtn_title = @"申请上麦";
        }
    }
    
    if (applySeatBtn_desc.length > 0) {
        NSString *tempStr = [NSString stringWithFormat:@"%@\n%@", applySeatBtn_title,applySeatBtn_count];
        NSMutableAttributedString *applySeatBtnAttri = [[NSMutableAttributedString alloc] initWithString:tempStr];
        [applySeatBtnAttri addAttributes:@{NSFontAttributeName:[UIFont boldSystemFontOfSize:12], NSForegroundColorAttributeName:UIColorHex(0xFFE42A)} range: NSMakeRange(0, applySeatBtn_title.length)];
        [applySeatBtnAttri addAttributes:@{NSFontAttributeName:[UIFont boldSystemFontOfSize:10], NSForegroundColorAttributeName:UIColorHex(0xEF51B2)} range: NSMakeRange(applySeatBtn_title.length, applySeatBtn_count.length)];
        
        _applySeatBtn.titleLabel.lineBreakMode = NSLineBreakByWordWrapping;
        _applySeatBtn.titleLabel.numberOfLines = 0;
        [_applySeatBtn setAttributedTitle:applySeatBtnAttri forState:UIControlStateNormal];
    } else {
        [_applySeatBtn setTitle:applySeatBtn_title forState:UIControlStateNormal];
    }
}

- (void)setRanks:(NSArray *)ranks {
    if (ranks.count == 0) {
        return;
    } else if (ranks.count == 1) {
        [self.top_oneImage sd_setImageWithURL:[NSURL URLWithString:ranks[0]]];
    } else if (ranks.count == 2) {
        [self.top_oneImage sd_setImageWithURL:[NSURL URLWithString:ranks[0]]];
        [self.top_twoImage sd_setImageWithURL:[NSURL URLWithString:ranks[1]]];
    } else if (ranks.count == 3) {
        [self.top_oneImage sd_setImageWithURL:[NSURL URLWithString:ranks[0]]];
        [self.top_twoImage sd_setImageWithURL:[NSURL URLWithString:ranks[1]]];
        [self.top_threeImage sd_setImageWithURL:[NSURL URLWithString:ranks[2]]];
    }
}

- (void)setHeatValue:(NSInteger)HeatValue {
    _HeatValue = HeatValue;
    if (HeatValue < 100000) {
        [_top_roomHotBtn setTitle:[NSString stringWithFormat:@" %ld", HeatValue] forState:UIControlStateNormal];
    } else {
        [_top_roomHotBtn setTitle:[NSString stringWithFormat:@" %ld万", HeatValue / 10000] forState:UIControlStateNormal];
    }
}

- (IBAction)topBtnAction:(UIButton *)sender {
    if (self.topBtnAction) {
        self.topBtnAction(sender.tag);
    }
}


- (IBAction)bottomBtnAction:(UIButton *)sender {
    if (self.bottomBtnAction) {
        self.bottomBtnAction(sender.tag);
    }
}

- (CXLiveRoomRegularView *)regularView {
    if (!_regularView) {
        _regularView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomRegularView" owner:self options:nil].lastObject;
        _regularView.backgroundColor = [UIColor clearColor];
        _regularView.layer.masksToBounds = YES;
        _regularView.layer.cornerRadius = 8;
    }
    
    return _regularView;
}

#pragma mark - CycleScrollView
- (void)setBannerList:(NSArray<CXHomeRoomBannerModel *> *)bannerList {
    _bannerList = bannerList;
    
    if (bannerList.count > 0) {
        self.cycleScrollView.hidden = NO;
        NSMutableArray * imgURLs = [NSMutableArray new];
        NSMutableArray * titlesGroup = [NSMutableArray new];
        [_bannerList enumerateObjectsUsingBlock:^(CXHomeRoomBannerModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [imgURLs addObject:[NSString stringWithFormat:@"%@", obj.image]];
            [titlesGroup addObject:[NSString stringWithFormat:@"%@", obj.title]];
        }];
        self.cycleScrollView.imageURLStringsGroup = imgURLs;
    } else {
        self.cycleScrollView.hidden = YES;
    }
}

#pragma mark - <SDCycleScrollViewDelegate>
- (void)cycleScrollView:(SDCycleScrollView *)cycleScrollView didSelectItemAtIndex:(NSInteger)index {
    if (index < self.bannerList.count) {
        CXHomeRoomBannerModel * banner = self.bannerList[index];
        if (self.didSelectedCycleItem) {
            self.didSelectedCycleItem(banner);
        }
    }
}


#pragma mark - roomSeatsView
- (CXLiveRoomSeatsView *)roomSeatsView {
    if (!_roomSeatsView) {
        _roomSeatsView = [[NSBundle mainBundle] loadNibNamed:@"CXLiveRoomSeatsView" owner:self options:nil].lastObject;
        kWeakSelf
        _roomSeatsView.seatViewActionBlock = ^(NSInteger tag, LiveRoomMicroInfo * _Nonnull microInfo) {
            if (weakSelf.roomSeatsViewBlock) {
                weakSelf.roomSeatsViewBlock(tag, microInfo);
            }
        };
    }
    
    return _roomSeatsView;
}

- (CXRommMusicLRCShowView *)musicLRCShowView {
    if (!_musicLRCShowView) {
        _musicLRCShowView = [[NSBundle mainBundle] loadNibNamed:@"CXRommMusicLRCShowView" owner:self options:nil].lastObject;
        kWeakSelf
        _musicLRCShowView.showMusicLRCFullBlock = ^{
            if (weakSelf.musicLRCShowViewBlock) {
                weakSelf.musicLRCShowViewBlock(NO);
            }
        };
        
        _musicLRCShowView.sendGiftRedRoseBlock = ^{
            if (weakSelf.musicLRCShowViewBlock) {
                weakSelf.musicLRCShowViewBlock(YES);
            }
        };
    }
    
    return _musicLRCShowView;
}

@end

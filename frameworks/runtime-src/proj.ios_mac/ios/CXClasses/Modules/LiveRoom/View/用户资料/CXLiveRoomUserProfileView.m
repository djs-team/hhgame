//
//  CXLiveRoomUserProfileView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/24.
//

#import "CXLiveRoomUserProfileView.h"

@interface CXLiveRoomUserProfileView ()
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UILabel *heightLabel;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;

@property (weak, nonatomic) IBOutlet UIButton *guardBtn;
@property (weak, nonatomic) IBOutlet UIButton *chatBtn;
@property (weak, nonatomic) IBOutlet UIButton *atBtn;
@property (weak, nonatomic) IBOutlet UIButton *giftBtn;
@property (weak, nonatomic) IBOutlet UIView *bottomView;

@property (weak, nonatomic) IBOutlet UIButton *free_inviteBtn;
@property (weak, nonatomic) IBOutlet UIButton *buy_inviteBtn;

@property (weak, nonatomic) IBOutlet UIButton *moreBtn;
@property (weak, nonatomic) IBOutlet UIButton *jinyanBtn;
@property (weak, nonatomic) IBOutlet UIButton *bimaiBtn;
@property (weak, nonatomic) IBOutlet UIButton *laheiBtn;
@property (weak, nonatomic) IBOutlet UIButton *yichuBtn;
@property (weak, nonatomic) IBOutlet UIButton *jubaoBtn;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bimaiBtn_topLayout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *laheiBtn_topLayout;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *jubaoBtn_topLayout;
@end

@implementation CXLiveRoomUserProfileView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 300));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeSheet;
    
    _avatar.layer.masksToBounds = YES;
    _avatar.layer.cornerRadius = 28;
    _avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    _avatar.layer.borderWidth = 0.5;
    
    _guardBtn.layer.masksToBounds = YES;
    _guardBtn.layer.cornerRadius = 14;
    
    _buy_inviteBtn.layer.masksToBounds = YES;
    _buy_inviteBtn.layer.cornerRadius = 18;
    _buy_inviteBtn.titleLabel.numberOfLines = 0;
    [_buy_inviteBtn setTitle:@"免费邀请\n上麦" forState:UIControlStateNormal];
    
    _free_inviteBtn.layer.masksToBounds = YES;
    _free_inviteBtn.layer.cornerRadius = 18;
    _free_inviteBtn.titleLabel.numberOfLines = 0;
    [_free_inviteBtn setTitle:@"收费邀请\n上麦" forState:UIControlStateNormal];
}

- (void)setUserInfo:(SocketMessageGetUserInfoResponse *)userInfo {
    
    if (userInfo.IsDisableMsg == YES) {
        [_jinyanBtn setTitle:@"取消禁言" forState:UIControlStateNormal];
    } else {
        [_jinyanBtn setTitle:@"禁言" forState:UIControlStateNormal];
    }
    
    if (userInfo.IsBlock == YES) {
        [_laheiBtn setTitle:@"取消拉黑" forState:UIControlStateNormal];
    } else {
        [_laheiBtn setTitle:@"拉黑" forState:UIControlStateNormal];
    }
    
    LiveRoomUser *user = userInfo.User;
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:user.HeadImageUrl]];
    _nameLabel.text = user.Name;
    [_sexBtn setTitle:user.Age.stringValue forState:UIControlStateNormal];
    if (user.Sex == 1) {
        [_sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [_sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    [_locationBtn setTitle:user.City forState:UIControlStateNormal];
    
    if ([[CXClientModel instance].firendIdArrays containsObject:user.UserId]) { //已经是好友了
        [_chatBtn setTitle:@"私聊好友" forState:UIControlStateNormal];
    } else {
        [_chatBtn setTitle:@"添加好友" forState:UIControlStateNormal];
    }
    
    NSIndexPath * seatIndex = [[CXClientModel instance].room.userSeats objectForKey:user.UserId];
    
    //查看是自己
    if ([user.UserId isEqualToString:[CXClientModel instance].userId]) {
        self.jinyanBtn.hidden = YES;
        self.laheiBtn.hidden = YES;
        self.yichuBtn.hidden = YES;
        self.jubaoBtn.hidden = YES;
        self.free_inviteBtn.hidden = YES;
        self.buy_inviteBtn.hidden = YES;
        self.chatBtn.hidden = YES;
        self.bottomView.hidden = NO;
        
        //在麦上
        if (seatIndex) {
            // 闭麦
            if ([CXClientModel instance].agoraEngineManager.offMic == YES) {//闭麦了，打开
                [self.bimaiBtn setTitle:@"取消闭麦" forState:UIControlStateNormal];
            } else {
                [self.bimaiBtn setTitle:@"闭麦" forState:UIControlStateNormal];
            }
            
            _bimaiBtn.hidden = NO;
            _bimaiBtn_topLayout.constant = 0;
        } else {
            // 不在麦上
            _moreBtn.hidden = YES;
            _bimaiBtn.hidden = YES;
        }
    } else {
        self.bottomView.hidden = YES;
        GameUserIdentity selfIdentity = [CXClientModel instance].room.UserIdentity.integerValue;
        if (seatIndex) { //在麦上
            LiveRoomMicroInfo * seat = [[CXClientModel instance].room.seats objectForKey:seatIndex];
            if (seat.isMute == YES) {//静音了此人，打开
                [self.bimaiBtn setTitle:@"取消静音" forState:UIControlStateNormal];
            } else {
                [self.bimaiBtn setTitle:@"静音" forState:UIControlStateNormal];
            }
            
            if (selfIdentity == GameUserIdentityOwner || selfIdentity == GameUserIdentityManager) {//是红娘
                _jinyanBtn.hidden = NO;
                _bimaiBtn.hidden = NO;
                _laheiBtn.hidden = NO;
                _yichuBtn.hidden = NO;
                _jubaoBtn.hidden = NO;
                _bimaiBtn_topLayout.constant = 27;
                _laheiBtn_topLayout.constant = 27*2;
                _jubaoBtn_topLayout.constant = 27;
            } else {//不是红娘
                _jinyanBtn.hidden = YES;
                _bimaiBtn.hidden = NO;
                _laheiBtn.hidden = NO;
                _yichuBtn.hidden = YES;
                _jubaoBtn.hidden = NO;
                _bimaiBtn_topLayout.constant = 0;
                _laheiBtn_topLayout.constant = 27;
                _jubaoBtn_topLayout.constant = 0;
            }
            
            self.free_inviteBtn.hidden = YES;
            self.buy_inviteBtn.hidden = YES;
        } else {
            if (selfIdentity == GameUserIdentityOwner || selfIdentity == GameUserIdentityManager) {
                _jinyanBtn.hidden = NO;
                _bimaiBtn.hidden = YES;
                _laheiBtn.hidden = NO;
                _yichuBtn.hidden = NO;
                _jubaoBtn.hidden = NO;
                _bimaiBtn_topLayout.constant = 27;
                _laheiBtn_topLayout.constant = 27;
                _jubaoBtn_topLayout.constant = 27;
            } else {
                _jinyanBtn.hidden = YES;
                _bimaiBtn.hidden = YES;
                _laheiBtn.hidden = NO;
                _yichuBtn.hidden = YES;
                _jubaoBtn.hidden = NO;
                _bimaiBtn_topLayout.constant = 0;
                _laheiBtn_topLayout.constant = 0;
                _jubaoBtn_topLayout.constant = 0;
            }
        }
        
    }
}

- (IBAction)userProfileBtnAction:(UIButton *)sender {
    if (self.userProfileActionBlock) {
        self.userProfileActionBlock(sender.tag);
    }
    
    [self hide];
}


@end

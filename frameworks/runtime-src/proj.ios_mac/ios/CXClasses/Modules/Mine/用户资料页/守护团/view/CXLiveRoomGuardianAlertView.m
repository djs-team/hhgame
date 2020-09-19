//
//  CXLiveRoomGuardianAlertView.m
//  hairBall
//
//  Created by mahong yang on 2020/6/22.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomGuardianAlertView.h"
#import "GameMessageListItemView+AttributeResult.h"

@interface CXLiveRoomGuardianAlertView()
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (strong, nonatomic) YYLabel *textLabel;

@end

@implementation CXLiveRoomGuardianAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    _avatar.layer.masksToBounds = YES;
    _avatar.layer.cornerRadius = 18;
    
    _textLabel = [YYLabel new];
    _textLabel.numberOfLines = 0;
    _textLabel.textColor = rgba(138, 255, 228, 1);
    [self addSubview:_textLabel];
    [_textLabel mas_makeConstraints:^(MASConstraintMaker *make) { make.edges.mas_equalTo(self).with.insets(UIEdgeInsetsMake(8, 42, 8, 10));
    }];
}

- (void)setModel:(SocketMessageUserJoinRoom *)model {
    _model = model;
    
    UIFont *font = [UIFont systemFontOfSize:14];
    
    UIColor *color = UIColorHex(0xFF2B7C);
    if (model.Sex.integerValue == 1) {
        color = UIColorHex(0x6E6EFF);
    }
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:model.Avatar]];
    
    // 添加 登记 身份 爵位
    NSMutableAttributedString *result = [self resultWithRoomGuardLevel:model.GuardSign DukeLevel:model.DukeLevel UserLevel:model.UserLevel UserIdentity:model.UserIdentity Font:font  UserID:model.UserId];
    
    // 昵称
    NSMutableAttributedString *user = [[NSMutableAttributedString alloc] initWithString:model.Name];
    [user setColor:color];
    user.font = font;
//    __weak typeof(self) weakSelf = self;
    [user setTextHighlightRange:NSMakeRange(0, model.Name.length) color:color backgroundColor:[UIColor clearColor] tapAction:^(UIView * _Nonnull containerView, NSAttributedString * _Nonnull text, NSRange range, CGRect rect) {
//        if (weakSelf.clickUserInfo) {
//            weakSelf.clickUserInfo(model.UserId);
//        }
    }];
    [result appendAttributedString:user];

    // 操作
    NSMutableAttributedString *option = [[NSMutableAttributedString alloc] initWithString:@"加入房间"];
    //    [option setColor:color];
    [option setColor:[UIColor whiteColor]];
    option.font = font;
    [result appendAttributedString:option];

    // 间距
    result.lineSpacing = 4.f;
    _textLabel.attributedText = result;
}

- (CGSize)sizeThatFits:(CGSize)size {
    CGSize tempSize = CGSizeMake(size.width, size.height);
    CGSize textSize = [self.textLabel sizeThatFits:tempSize];
    return CGSizeMake(size.width, textSize.height + 18);
}

- (NSMutableAttributedString *)resultWithRoomGuardLevel:(NSString *)RoomGuardLevel DukeLevel:(NSNumber *)DukeLevel UserLevel:(NSNumber *)UserLevel UserIdentity:(NSNumber *)UserIdentity Font:(UIFont *)font UserID:(NSString *)UserID {
    font = [UIFont systemFontOfSize:10];
    NSMutableAttributedString *result = [[NSMutableAttributedString alloc] init];
    NSMutableAttributedString *margin = [[NSMutableAttributedString alloc] initWithString:@" "];
//    // 等级
//    NSInteger userLevel = [UserLevel integerValue];
//    if (userLevel != 0) {
//        UIImage *level = [AppDelegate.shared.dynamicResoueceManager chatIcon:@(userLevel).stringValue];
//        NSAttributedString *att = [NSAttributedString attachmentStringWithContent:level contentMode:UIViewContentModeCenter attachmentSize:level.size alignToFont:font alignment:YYTextVerticalAlignmentCenter];
//        [result appendAttributedString:att];
//        [result appendAttributedString:margin];
//        [result appendAttributedString:margin];
//    }
//    // 身份 0普通 1管理员 2主持
//    switch ([UserIdentity integerValue]) {
//        case 1: {
//            UIImage *level = [AppDelegate.shared.dynamicResoueceManager chatIcon:@"管理"];
//            NSAttributedString *att = [NSAttributedString attachmentStringWithContent:level contentMode:UIViewContentModeCenter attachmentSize:level.size alignToFont:font alignment:YYTextVerticalAlignmentCenter];
//            [result appendAttributedString:att];
//
//            NSMutableAttributedString *margin = [[NSMutableAttributedString alloc] initWithString:@" "];
//            [result appendAttributedString:margin];
//            [result appendAttributedString:margin];
//        }
//            break;
//        default:
//            break;
//    }
    
//    if (RoomGuardLevel.length > 0) {
//        UIImage *guardlevel = [AppDelegate.shared.dynamicResoueceManager chatGuardIcon:RoomGuardLevel andLevel:userLevel];
//        NSAttributedString *att = [NSAttributedString attachmentStringWithContent:guardlevel contentMode:UIViewContentModeCenter attachmentSize:guardlevel.size alignToFont:font alignment:YYTextVerticalAlignmentCenter];
//        [result appendAttributedString:att];
//        [result appendAttributedString:margin];
//        [result appendAttributedString:margin];
//    }
    
    return result;
}

@end

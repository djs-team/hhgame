//
//  GameMessageListItemView+AttributeResult.m
//  hairBall
//
//  Created by shiwei on 2019/7/22.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListItemView+AttributeResult.h"
#import "GameMessageLevelAndTagView.h"

@implementation GameMessageListItemView (AttributeResult)

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
//    
    if (RoomGuardLevel.length > 0) {
        UIImage *guardlevel = [UIImage imageNamed:@"liveroom_chat_guard_bg"];
        NSAttributedString *att = [NSAttributedString attachmentStringWithContent:guardlevel contentMode:UIViewContentModeCenter attachmentSize:guardlevel.size alignToFont:font alignment:YYTextVerticalAlignmentCenter];
        [result appendAttributedString:att];
        [result appendAttributedString:margin];
        [result appendAttributedString:margin];
    }
    
    return result;
}

@end

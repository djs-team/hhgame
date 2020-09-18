//
//  EMMessageBubbleView.m
//  ChatDemo-UI3.0
//
//  Created by XieYajie on 2019/1/25.
//  Copyright © 2019 XieYajie. All rights reserved.
//

#import "EMMessageBubbleView.h"

@implementation EMMessageBubbleView

- (instancetype)initWithDirection:(EMMessageDirection)aDirection
                             type:(EMMessageType)aType
{
    self = [super init];
    if (self) {
        _direction = aDirection;
        _type = aType;
    }
    
    return self;
}

- (void)setupBubbleBackgroundImage
{
    if (self.direction == EMMessageDirectionSend) {
        self.image = [[UIImage imageNamed:@"friend_message_sender_bg"] stretchableImageWithLeftCapWidth:16 topCapHeight:26];
    } else {
        self.image = [[UIImage imageNamed:@"friend_message_recivier_bg"] stretchableImageWithLeftCapWidth:16 topCapHeight:26];
    }
}



@end

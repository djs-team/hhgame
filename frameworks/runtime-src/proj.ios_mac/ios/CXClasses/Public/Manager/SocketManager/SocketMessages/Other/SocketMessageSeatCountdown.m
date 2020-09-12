//
//  SocketMessageSeatCountdown.m
//  CSharp
//
//  Created by 肖迎军 on 2019/7/27.
//  Copyright © 2019 肖迎军. All rights reserved.
//

#import "SocketMessageSeatCountdown.h"

@implementation SocketMessageSeatCountdown

SocketMessageInitMethod(SocketMessageIDSeatCountdown)

- (NSIndexPath*)indexPath {
    return [NSIndexPath indexPathForRow:_Number.integerValue inSection:_Level.integerValue];
}

@end

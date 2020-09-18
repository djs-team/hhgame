//
//  SocketMessageSeatCountdown.h
//  CSharp
//
//  Created by 肖迎军 on 2019/7/27.
//  Copyright © 2019 肖迎军. All rights reserved.
//

#import "SocketMessage.h"

NS_ASSUME_NONNULL_BEGIN

@interface SocketMessageSeatCountdown : SocketMessageNotification

@property (nonatomic, strong) NSNumber * Level;
@property (nonatomic, strong) NSNumber * Number;
@property (nonatomic, strong) NSString * SpeechTime;
@property (nonatomic, strong) NSNumber * Duration;

@property (weak, readonly, nonatomic) NSIndexPath * indexPath;

@end

NS_ASSUME_NONNULL_END

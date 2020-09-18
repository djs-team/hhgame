//
//  CXSocketMessageOnlineMemberNumber.h
//  hairBall
//
//  Created by mahong yang on 2019/11/9.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXSocketMessageOnlineMemberNumber : SocketMessage

@property  (nonatomic, strong) NSString * LeftNumber;
@property  (nonatomic, strong) NSString * RightNumber;

@end

NS_ASSUME_NONNULL_END

//
//  CXFriendMessageViewController.h
//  hairBall
//
//  Created by mahong yang on 2019/10/17.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXFriendMessageViewController : CXBaseViewController

@property (nonatomic, assign) BOOL isConversation;

- (void)clearConversationList;

@end

NS_ASSUME_NONNULL_END

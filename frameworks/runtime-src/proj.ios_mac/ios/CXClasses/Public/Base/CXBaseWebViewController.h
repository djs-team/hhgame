//
//  CXBaseWebViewController.h
//  hairBall
//
//  Created by mahong yang on 2020/3/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXBaseWebViewController : CXBaseViewController

- (instancetype)initWithURL:(NSURL*)pageURL;

@property (nonatomic, copy) NSString *webUrl;

@end

NS_ASSUME_NONNULL_END

//
//  CXNearbyViewController.h
//  hairBall
//
//  Created by mahong yang on 2019/11/5.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXBaseViewController.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXNearbyViewController : CXBaseViewController

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, assign) NSInteger vcType; // 1: 推荐， 2: 同城

@end

NS_ASSUME_NONNULL_END

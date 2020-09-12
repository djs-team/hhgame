//
//  CXProfessionSelectedView.h
//  hairBall
//
//  Created by mahong yang on 2019/12/12.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN


@interface CXProfessionSelectedView : UIView

@property (nonatomic,strong)NSString *selectFirstIndex;
@property (nonatomic,strong)NSString *selectSecondIndex;
@property (nonatomic,strong)NSDictionary *dataDict;
@property (nonatomic,strong)UIViewController *parentVC;
@property (nonatomic,copy) void(^sureActionBlock)(NSString *content1, NSString *content2);

@end

NS_ASSUME_NONNULL_END

//
//  CXLiveRoomRecommendView.h
//  hairBall
//
//  Created by mahong yang on 2020/6/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomRecommendView : UIView

@property (nonatomic, strong) NSMutableArray *dataSources;

@property (nonatomic, copy) void (^joinRecommendRoom)(NSString *roomId);

- (void)show;

@end

NS_ASSUME_NONNULL_END

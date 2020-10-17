//
//  CXRefreshNormalTimeHeader.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/10/16.
//

#import "CXRefreshNormalTimeHeader.h"

@implementation CXRefreshNormalTimeHeader

- (void)prepare
{
    [super prepare];
   
    //隐藏时间
    self.lastUpdatedTimeLabel.hidden = YES;
    // 隐藏状态
//    self.stateLabel.hidden = YES;
    self.stateLabel.font = [UIFont systemFontOfSize:12];
}

@end

@implementation CXRefreshNormalFooter

- (void)prepare
{
    [super prepare];
   
    self.stateLabel.font = [UIFont systemFontOfSize:12];
}

@end


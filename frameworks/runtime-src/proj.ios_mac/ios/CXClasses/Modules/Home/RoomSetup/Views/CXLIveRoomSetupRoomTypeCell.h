//
//  CXLIveRoomSetupRoomTypeCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLIveRoomSetupRoomTypeCell : UITableViewCell

@property (nonatomic, assign) NSInteger sectionIndex;

@property (nonatomic, copy) void (^setupRoomTypeHelpActionBlock)(void);
@property (nonatomic, copy) void (^setupRoomTypeExclusiveActionBlock)(BOOL isExclusive);

@property (nonatomic, copy) void (^setupRoomTypeSelectedArrayBlock)(NSArray *array);

@property (nonatomic, strong) NSArray *dataSources;
@property (nonatomic, strong) NSMutableArray *selectedArrays;

@property (nonatomic, assign) BOOL IsExclusiveRoom; // 是否是专属房

@end

NS_ASSUME_NONNULL_END

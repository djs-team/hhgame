//
//  XYRoomAlreadListCell.h
//  hairBall
//
//  Created by zyy on 2019/11/1.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
@class LiveRoomMicroOrder;

NS_ASSUME_NONNULL_BEGIN

typedef enum : NSInteger{
    deleteOpreationType = 1,
    upOpreationType = 2,
    joinOpreationType = 3,
}AlreadButOpreationType;

@interface XYRoomAlreadListCell : UITableViewCell

@property (copy , nonatomic) void(^alreadListOpreationBlock)(AlreadButOpreationType opreationType , LiveRoomMicroOrder * order);

@property (nonatomic, copy) void (^checkUserProfileBlock)(void);

@property (strong , nonatomic) LiveRoomMicroOrder * order;

@property (assign , nonatomic) NSInteger indexP;

@end

NS_ASSUME_NONNULL_END

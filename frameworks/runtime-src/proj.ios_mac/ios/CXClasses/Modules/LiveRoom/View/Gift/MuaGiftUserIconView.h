//
//  MuaGiftUserIconView.h
//  hairBall
//
//  Created by shiwei on 2019/7/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface MuaGiftUserModel : NSObject

@property (nonatomic, strong) LiveRoomUser *model;
@property (nonatomic, assign) Boolean isSelect;

@end

@interface MuaGiftUserIconView : UIView

@property (nonatomic, strong) MuaGiftUserModel *model;
- (void)reloadData;

@end

NS_ASSUME_NONNULL_END

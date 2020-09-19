//
//  CXChatRoomGIftView.h
//  hairBall
//
//  Created by mahong yang on 2019/10/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXLiveRoomGiftModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXChatRoomGIftView : UIView

@property (weak, nonatomic) IBOutlet UILabel *giftValueLabel;

@property (nonatomic, readonly) CGFloat viewHeight;

- (void)reloadGiftData:(NSArray *)dataSource;

@property (nonatomic, copy) void (^sendGiftAction)(CXLiveRoomGiftModel *model);
@property (nonatomic, copy) void (^rechargeAction)(void);

@end

NS_ASSUME_NONNULL_END

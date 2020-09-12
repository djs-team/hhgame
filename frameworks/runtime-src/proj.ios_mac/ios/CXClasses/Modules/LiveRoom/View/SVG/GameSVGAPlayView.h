//
//  GameSVGAPlayView.h
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSUInteger, GiftInfoClassType) {
    GiftInfoClassTypeBig = 1,
    GiftInfoClassTypeSmall = 2,
};

@class SocketMessageGiftData;

NS_ASSUME_NONNULL_BEGIN


@interface GameSVGAPlayView : UIView

- (void)pushSVGAURLString:(SocketMessageGiftData*)giftData;

@end

NS_ASSUME_NONNULL_END

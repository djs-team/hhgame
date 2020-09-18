//
//  giftCell.h
//  hairBall
//
//  Created by ashuan on 2019/4/29.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "giftIconBtn.h"
NS_ASSUME_NONNULL_BEGIN
@protocol giftCellDelegate <NSObject>
-(void)clickSendGiftBtn:(voiceGiftModel*)model;
@end

@interface giftCell : UICollectionViewCell
-(void)setGiftCell:(NSMutableArray*)dataArray;
@property (nonatomic,weak)id<giftCellDelegate>deleagte;

@property (nonatomic, copy) void(^clickBlock)(voiceGiftModel *model);
@end

NS_ASSUME_NONNULL_END

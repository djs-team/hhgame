//
//  CXAddFriendGiftListCell.h
//  hairBall
//
//  Created by mahong yang on 2019/10/29.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXAddFriendGiftListCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *giftLogo;
@property (weak, nonatomic) IBOutlet UILabel *giftNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *giftCoinLabel;

@property (nonatomic, strong) CXFriendGiftModel *giftModel;

@end

NS_ASSUME_NONNULL_END

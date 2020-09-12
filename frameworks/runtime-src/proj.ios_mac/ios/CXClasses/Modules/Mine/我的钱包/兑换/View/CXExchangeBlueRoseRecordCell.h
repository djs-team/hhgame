//
//  CXExchangeBlueRoseRecordCell.h
//  hairBall
//
//  Created by mahong yang on 2020/4/17.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXExchangeBlueRoseRecordCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *giftNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *giftNumLabel;
@property (weak, nonatomic) IBOutlet UILabel *roseNumLabel;

@end

NS_ASSUME_NONNULL_END

//
//  CXLiveRoomRechargeCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/10.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXRechargeModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomRechargeCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIButton *tag_btn;
@property (weak, nonatomic) IBOutlet UIView *bg_view;
@property (weak, nonatomic) IBOutlet UILabel *rose_numberLabel;
@property (weak, nonatomic) IBOutlet UILabel *rose_valueLabel;
@property (weak, nonatomic) IBOutlet UILabel *first_tagLabel;
@property (weak, nonatomic) IBOutlet UILabel *first_tagNameLabel;


@property (nonatomic, assign) BOOL isLiveHome; // 是否是房间内充值
@property (nonatomic, strong) CXRechargeModel *chargeListItem;

@property (nonatomic, assign) BOOL isShowFirstTag;

@end

NS_ASSUME_NONNULL_END

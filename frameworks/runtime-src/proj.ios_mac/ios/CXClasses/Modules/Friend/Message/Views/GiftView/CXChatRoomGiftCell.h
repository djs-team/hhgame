//
//  CXChatRoomGiftCell.h
//  hairBall
//
//  Created by mahong yang on 2019/10/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXLiveRoomGiftModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXChatRoomGiftCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UILabel *numberLabel;
@property (weak, nonatomic) IBOutlet UIImageView *gift_logo;
@property (weak, nonatomic) IBOutlet UILabel *gift_nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *gift_valueLabel;

@property (weak, nonatomic) IBOutlet UIButton *selectedButton;

@property (nonatomic, copy) void (^didSeletedBlock)(void);

@property (nonatomic, strong) CXLiveRoomGiftModel *model;

@property (nonatomic, assign) BOOL isSelected;

@end

NS_ASSUME_NONNULL_END

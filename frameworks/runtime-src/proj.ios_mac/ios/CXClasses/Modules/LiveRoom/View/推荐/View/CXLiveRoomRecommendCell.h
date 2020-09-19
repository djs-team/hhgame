//
//  CXLiveRoomRecommendCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomRecommendCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIButton *tagLabel;
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UIImageView *g_bgImage;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *descLabel;

@property (nonatomic, strong) CXLiveRoomRecommendModel *model;

@end

NS_ASSUME_NONNULL_END

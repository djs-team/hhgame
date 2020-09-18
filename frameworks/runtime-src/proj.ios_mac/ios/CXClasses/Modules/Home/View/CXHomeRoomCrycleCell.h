//
//  CXHomeRoomCrycleCell.h
//  hairBall
//
//  Created by mahong yang on 2020/6/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "CXHomeRoomModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXHomeRoomCrycleCell : UICollectionViewCell

@property (nonatomic, strong) NSArray<CXHomeRoomBannerModel*> * bannerList;

@property (nonatomic, copy) void (^didSelectedCycleUrl)(CXHomeRoomBannerModel *item);

@end

NS_ASSUME_NONNULL_END

//
//  CXHomeRoomRecommendCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import <UIKit/UIKit.h>
#import "CXHomeRoomModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXHomeRoomRecommendCell : UICollectionViewCell
@property (retain, nonatomic) IBOutlet UIImageView *roomImage;
@property (retain, nonatomic) IBOutlet UILabel *descLabel;
@property (retain, nonatomic) IBOutlet UILabel *roomnameLabel;
@property (retain, nonatomic) IBOutlet UIImageView *roomActivityImage;

@property (nonatomic, nullable) CXHomeRoomModel * model;

@end

NS_ASSUME_NONNULL_END

//
//  CXMineGuardRenewItemCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/15.
//

#import <UIKit/UIKit.h>
#import "CXRechargeModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXMineGuardRenewItemCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *bgimage;
@property (weak, nonatomic) IBOutlet UILabel *tehuiTagLabel;
@property (weak, nonatomic) IBOutlet UILabel *daylabel;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;
@property (weak, nonatomic) IBOutlet UILabel *originPriceLabel;

@property (nonatomic, strong) CXRechargeModel *model;

@end

NS_ASSUME_NONNULL_END

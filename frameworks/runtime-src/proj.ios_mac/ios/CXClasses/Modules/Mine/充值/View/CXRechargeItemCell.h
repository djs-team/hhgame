//
//  CXRechargeItemCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import <UIKit/UIKit.h>
#import "CXRechargeModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXRechargeItemCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UILabel *roseCountLabel;
@property (weak, nonatomic) IBOutlet UILabel *rmbLabel;
@property (weak, nonatomic) IBOutlet UILabel *awardCountLabel;
@property (weak, nonatomic) IBOutlet UILabel *boonTagLabel;

@property (nonatomic, strong) CXRechargeModel *model;

@end

NS_ASSUME_NONNULL_END

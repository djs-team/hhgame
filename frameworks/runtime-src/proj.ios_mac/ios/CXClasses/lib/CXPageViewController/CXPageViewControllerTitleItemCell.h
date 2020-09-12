//
//  CXPageViewControllerTitleItemCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/11.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXPageViewControllerTitleItemCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UILabel *countLabel;
@property (weak, nonatomic) IBOutlet UIView *indicatorView;

@end

NS_ASSUME_NONNULL_END

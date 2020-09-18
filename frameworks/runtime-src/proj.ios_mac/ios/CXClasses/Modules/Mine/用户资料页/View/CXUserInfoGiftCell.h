//
//  CXUserInfoGiftCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXUserInfoGiftCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *countLabel;

@property (nonatomic, strong) CXFriendGiftModel *model;

@end

NS_ASSUME_NONNULL_END

//
//  CXMineCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/14.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXMineCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *itemTitleLabel;
@property (weak, nonatomic) IBOutlet UILabel *itemDescLabel;
@property (weak, nonatomic) IBOutlet UIView *line;

@end

NS_ASSUME_NONNULL_END

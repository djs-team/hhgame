//
//  CXRedPacketAwardRecordCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXRedPacketAwardRecordCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UIImageView *rankImg;

@end

NS_ASSUME_NONNULL_END

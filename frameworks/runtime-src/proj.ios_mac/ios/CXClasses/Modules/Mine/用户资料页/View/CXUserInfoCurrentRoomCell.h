//
//  CXUserInfoCurrentRoomCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/10/16.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXUserInfoCurrentRoomCell : UICollectionViewCell

@property (strong, nonatomic) UIImageView * bgView;
@property (strong, nonatomic) UIImageView * room_Icon;
@property (strong, nonatomic) UILabel * top_label;
@property (strong, nonatomic) UILabel     * room_name;
@property (strong, nonatomic) UIImageView * arrow;
@property (strong, nonatomic) UILabel * into_Room;

@end

NS_ASSUME_NONNULL_END

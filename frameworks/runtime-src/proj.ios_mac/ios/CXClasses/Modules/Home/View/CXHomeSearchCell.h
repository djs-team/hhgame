//
//  CXHomeSearchCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import <UIKit/UIKit.h>
#import "CXUserModel.h"
#import "CXHomeRoomModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXHomeSearchCell : UITableViewCell

@property (nonatomic, copy) void(^avatarTapGestureBlock)(void);

@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UILabel *user_idLabel;
@property (weak, nonatomic) IBOutlet UIButton *locationLabel;
@property (weak, nonatomic) IBOutlet UILabel *onlineStateLabel;
@property (weak, nonatomic) IBOutlet UILabel *stateLabel;

@property (nonatomic, strong) CXUserModel *user;
@property (nonatomic, strong) CXHomeRoomModel *room;

@end

NS_ASSUME_NONNULL_END

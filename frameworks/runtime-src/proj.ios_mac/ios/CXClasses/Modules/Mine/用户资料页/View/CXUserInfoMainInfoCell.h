//
//  CXUserInfoMainInfoCell.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXUserInfoMainInfoCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UILabel *nicknameLabel;
@property (weak, nonatomic) IBOutlet UILabel *stateLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UILabel *useridLabel;
@property (weak, nonatomic) IBOutlet UIButton *guardBtn;

@property (nonatomic, copy) void (^guardActionBlock)(void);

@property (nonatomic, strong) CXUserModel *model;

@end

NS_ASSUME_NONNULL_END

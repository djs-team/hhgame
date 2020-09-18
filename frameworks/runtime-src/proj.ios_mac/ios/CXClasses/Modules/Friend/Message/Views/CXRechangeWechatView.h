//
//  CXRechangeWechatView.h
//  AFNetworking
//
//  Created by mahong yang on 2019/10/30.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^CXRechangeWechatViewCommitActionBlcok)(void);

@interface CXRechangeWechatView : UIView

@property (nonatomic, copy) CXRechangeWechatViewCommitActionBlcok commitActionBlock;

@property (unsafe_unretained, nonatomic) IBOutlet UIView *contentView;
@property (unsafe_unretained, nonatomic) IBOutlet UITextField *contentTextField;
@property (unsafe_unretained, nonatomic) IBOutlet UIButton *commitButton;

@end

NS_ASSUME_NONNULL_END

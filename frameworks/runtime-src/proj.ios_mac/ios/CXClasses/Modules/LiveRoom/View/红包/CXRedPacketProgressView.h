//
//  CXRedPacketProgressView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXRedPacketProgressView : UIView
@property (weak, nonatomic) IBOutlet UILabel *redpacketProgressLabel;
@property (weak, nonatomic) IBOutlet UIProgressView *redpacketProgressView;

@property (nonatomic, copy) void (^redpacketProgressRegularBlock)(void);

@property (nonatomic, copy) void (^redpacketHideBlock)(void);

@end

NS_ASSUME_NONNULL_END

//
//  GameMessageLevelAndTagView.h
//  hairBall
//
//  Created by shiwei on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface GameMessageLevelAndTagView : UIView


+ (instancetype)viewWithBackImage:(NSString *)backImage text:(NSString *)text isLevel:(Boolean)isLevel;

@end

NS_ASSUME_NONNULL_END

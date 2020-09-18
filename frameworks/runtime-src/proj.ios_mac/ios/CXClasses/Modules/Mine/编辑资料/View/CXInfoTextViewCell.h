//
//  CXInfoTextViewCell.h
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXInfoTextViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *placehoderLabel;
@property (weak, nonatomic) IBOutlet UITextView *inputTextView;

@property (nonatomic, copy) NSString *contentStr;

@end

NS_ASSUME_NONNULL_END

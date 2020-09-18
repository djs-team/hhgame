//
//  MuaGiftListSelectCountCell.h
//  hairBall
//
//  Created by shiwei on 2019/8/1.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface MuaGiftListSelectCountCell : UITableViewCell

@property (nonatomic, copy) NSString *countStr;
+ (instancetype)cellWithTableView:(UITableView *)tableView identifier:(NSString *)identifier;

@end

NS_ASSUME_NONNULL_END

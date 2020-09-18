//
//  CXPageViewControllerTitleItemCell.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/11.
//

#import "CXPageViewControllerTitleItemCell.h"

@implementation CXPageViewControllerTitleItemCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    _countLabel.layer.masksToBounds = YES;
    _countLabel.layer.cornerRadius = 7;
}

@end

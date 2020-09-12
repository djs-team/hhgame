//
//  GameMessageLevelAndTagView.m
//  hairBall
//
//  Created by shiwei on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageLevelAndTagView.h"

@interface GameMessageLevelAndTagView ()

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *margin;
@property (weak, nonatomic) IBOutlet UIImageView *backImage;
@property (weak, nonatomic) IBOutlet UILabel *textLabel;

@end

@implementation GameMessageLevelAndTagView

+ (instancetype)viewWithBackImage:(NSString *)backImage text:(NSString *)text isLevel:(Boolean)isLevel {
    GameMessageLevelAndTagView *view = [[NSBundle mainBundle] loadNibNamed:@"GameMessageLevelAndTagView" owner:nil options:nil].lastObject;
    if (isLevel) {
        [view.margin setValue:@"1.4" forKeyPath:@"multiplier"];
    } else {
        [view.margin setValue:@"1.0" forKeyPath:@"multiplier"];
    }
    view.backImage.image = [UIImage imageNamed:backImage];
    view.textLabel.text = text;
    return view;
}

- (void)awakeFromNib {
    [super awakeFromNib];
}


@end

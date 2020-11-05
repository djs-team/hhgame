//
//  giftIconBtn.h
//  hairBall
//
//  Created by ashuan on 2019/4/27.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "voiceGiftModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface giftIconBtn : UIButton
/**价值豆label*/
@property (nonatomic,strong)UILabel *giftCountLabel;
/**礼物名字*/
@property (nonatomic,strong)UILabel *giftNameLabel;
/**礼物图标*/
@property (nonatomic,strong)UIImageView *gifIconView;
/**心动值label*/
@property (nonatomic,strong)UIButton *giftXLabel;
/**守护礼物标签*/
@property (nonatomic,strong)UILabel *giftTagLabel;

-(void)setGiftIconBtnModel:(voiceGiftModel*)model;
@end

NS_ASSUME_NONNULL_END

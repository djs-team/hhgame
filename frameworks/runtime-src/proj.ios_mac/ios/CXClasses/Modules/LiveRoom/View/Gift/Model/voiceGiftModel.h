//
//  voiceGiftModel.h
//  hairBall
//
//  Created by ashuan on 2019/3/29.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface voiceGiftModel : NSObject
/**礼物id*/
@property (nonatomic,copy) NSString  *gift_id;
/**礼物名称*/
@property (nonatomic,copy) NSString  *gift_name;
/**礼物财富值*/
@property (nonatomic,copy) NSString *gift_number;
/**礼物币*/
@property (nonatomic,copy) NSString *gift_coin;
/**礼物图标*/
@property (nonatomic,copy) NSString *gift_image;
/**礼物类型 1普通礼物 2动画礼物 3免费礼物*/
@property (nonatomic,copy) NSString *gift_type;
/**礼物svga动画地址*/
@property (nonatomic,copy) NSString *gift_animation;
/**礼物gif动画地址*/
@property (nonatomic,copy) NSString *animation;
/**礼物大小*/
@property (nonatomic,assign) NSInteger class_type;
@property (nonatomic,assign) NSInteger sendCount;

@property (nonatomic,assign) BOOL isSelect;

@property (nonatomic, assign) BOOL IsUseBeg;
@property (nonatomic, copy) NSString *pack_num;
@end

NS_ASSUME_NONNULL_END

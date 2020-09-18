//
//  CXGameMusicReserveListCollectionCell.h
//  hairBall
//
//  Created by mahong yang on 2020/2/7.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef enum : NSUInteger {
    Orgin = 0,
    Music,
    Reserve,
} CXGameMusicReserveListType;

@interface CXGameMusicReserveListCollectionCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UILabel *coinLabel;

@property (nonatomic, strong) CXLiveRoomModel *room;

@property (nonatomic, assign) CXGameMusicReserveListType listType;

@property (nonatomic, copy) void (^gameMusicReserveListBlock)(CXSocketMessageMusicModel *model, CXGameMusicReserveListType listType, LiveRoomUser *user);

@end

NS_ASSUME_NONNULL_END

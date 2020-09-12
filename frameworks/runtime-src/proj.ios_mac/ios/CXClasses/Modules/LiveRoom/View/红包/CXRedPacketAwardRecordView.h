//
//  CXRedPacketAwardRecordView.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import <MMPopupView/MMPopupView.h>
#import "CXLiveRoomRedPacketModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface CXRedPacketAwardRecordView : MMPopupView

@property (nonatomic, strong) NSArray <CXLiveRoomRedPacketModel *>*recordArrays;

@end

NS_ASSUME_NONNULL_END

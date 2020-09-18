//
//  CXLiveRoomRedPacketModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXLiveRoomRedPacketModel : NSObject

@property (nonatomic, assign) NSInteger UserId;
@property (nonatomic, strong) NSString *HeadImageUrl;
@property (nonatomic, strong) NSString *NickName;
@property (nonatomic, assign) float Value; // 红包金额

@end

NS_ASSUME_NONNULL_END

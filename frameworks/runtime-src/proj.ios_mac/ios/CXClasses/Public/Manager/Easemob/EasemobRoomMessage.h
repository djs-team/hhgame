//
//  EasemobRoomMessage.h
//  hairBall
//
//  Created by 肖迎军 on 2019/8/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

extern NSString * const EasemobRoomMessageExtTypeEmoji;
extern NSString * const EasemobRoomMessageExtTypeText;

@interface EmoticonGetListResponseDataEmoticonListItem : NSObject

@property (nonatomic, strong) NSString * face_id;
@property (nonatomic, strong) NSString * face_name;
@property (nonatomic, strong) NSString * face_image;
@property (nonatomic, strong) NSString * is_lock;
@property (nonatomic, strong) NSString * animation;
@property (nonatomic, strong) NSString * type;
@property (nonatomic, strong) NSArray<NSString*> * game_images;

@end

@interface EasemobRoomMessageExt : NSObject

@property (nonatomic, strong) NSString * XYType;
@property (nonatomic, strong) SocketMessageUserJoinRoom * XYUser;
@property (nonatomic, strong) EmoticonGetListResponseDataEmoticonListItem * XYEmoji;

@end


@interface EasemobRoomMessage : NSObject

@property (nonatomic, strong) NSError * error;

@property (nonatomic, strong) NSString * messageId;
@property (nonatomic, strong) NSString * roomId;
@property (nonatomic, strong) NSString * text;
@property (nonatomic, strong) EasemobRoomMessageExt * ext;

@end

NS_ASSUME_NONNULL_END

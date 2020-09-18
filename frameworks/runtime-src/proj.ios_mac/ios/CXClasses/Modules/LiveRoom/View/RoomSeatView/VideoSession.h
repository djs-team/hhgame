//
//  VideoSession.h
//  OpenLive
//
//  Created by GongYuhua on 2016/9/12.
//  Copyright © 2016年 Agora. All rights reserved.
//

#import <UIKit/UIKit.h>

@class LiveRoomMicroInfo;

@interface VideoSession : NSObject
@property (assign, nonatomic) NSUInteger uid;
@property (strong, nonatomic) UIView *hostingView;
@property (strong, nonatomic) AgoraRtcVideoCanvas *canvas;

@property (nonatomic) LiveRoomMicroInfo * model;

- (instancetype)initWithUid:(NSUInteger)uid;
@end

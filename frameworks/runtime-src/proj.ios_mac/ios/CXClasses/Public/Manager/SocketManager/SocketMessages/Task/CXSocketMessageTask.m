//
//  CXSocketMessageTask.m
//  hairBall
//
//  Created by mahong yang on 2019/12/6.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSocketMessageTask.h"

@implementation CXSocketMessageTaskAwardModel

@end

@implementation CXSocketMessageTaskModel

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"Awards" : [CXSocketMessageTaskAwardModel class],
             };
}

@end

@implementation CXSocketMessageTaskUpdateInfo
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"TaskInfos" : [CXSocketMessageTaskModel class],
             };
}

@end

@implementation CXSocketMessageTaskInfo
+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"TaskItems" : [CXSocketMessageTaskModel class],
             };
}

@end

@implementation CXSocketMessageTaskCheckRequest

SocketMessageInitMethod(SocketMessageIDTaskCheck)

@end

@implementation SocketMessageTaskGetAwardResponse

+ (NSDictionary *)modelContainerPropertyGenericClass {
    return @{
             @"FinishedTasks" : [CXSocketMessageTaskModel class],
             };
}
@end

@implementation CXSocketMessageTaskGetAwardRequest

@dynamic response;

- (Class)responseClass {
    return SocketMessageTaskGetAwardResponse.class;
}

SocketMessageInitMethod(SocketMessageIDTaskGetAward)

@end

@implementation CXSocketMessageTaskGetOneAwardRequest

@dynamic response;

- (Class)responseClass {
    return SocketMessageTaskGetAwardResponse.class;
}

SocketMessageInitMethod(SocketMessageIDTaskGetOneAward)

@end

@implementation CXSocketMessageTaskUpdateFinishTaskInfo
@end

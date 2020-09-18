//
//  CXMusicDownloader.h
//  hairBall
//
//  Created by mahong yang on 2020/2/14.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXMusicDownloader : NSObject

/**
 *  下载文件
 *
 *  @param downloadURL  下载链接
 *  @param success 请求结果
 *  @param faliure 错误信息
 */
+ (void)downloadURL:(NSString *) downloadURL progress:(void (^)(NSProgress *downloadProgress))progress success:(void (^)(NSURL *targetPath))success failure:(void(^)(NSError *error))faliure;

@end

NS_ASSUME_NONNULL_END

//
//  CXMusicDownloader.m
//  hairBall
//
//  Created by mahong yang on 2020/2/14.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXMusicDownloader.h"
#import <AFNetworking/AFNetworking.h>

@implementation CXMusicDownloader

/**
 *  下载文件
 *
 *  @param downloadURL  下载链接
 *  @param success 请求结果
 *  @param faliure 错误信息
 */
+ (void)downloadURL:(NSString *)downloadURL progress:(void (^)(NSProgress *downloadProgress))progress success:(void (^)(NSURL *targetPath))success failure:(void(^)(NSError *error))faliure {
    
//    YYCache *cache = [YYCache cacheWithName:@"LiveRoomMusicDownCache"];
//    if ([cache containsObjectForKey:downloadURL]) {
//        id filePath = [cache objectForKey:downloadURL];
//        NSURL *filePathURL = filePath;
//        if ([MTool fileSizeAtPath:filePathURL.absoluteString] > 0) {
//            success(filePath);
//            return;
//        }
//    }
    
    //1.创建管理者
    AFHTTPSessionManager *manage  = [AFHTTPSessionManager manager];
    
    //2.下载文件
    /*
     第一个参数：请求对象
     第二个参数：下载进度
     第三个参数：block回调，需要返回一个url地址，用来告诉AFN下载文件的目标地址
     targetPath：AFN内部下载文件存储的地址，tmp文件夹下
     response：请求的响应头
     返回值：文件应该剪切到什么地方
     第四个参数：block回调，当文件下载完成之后调用
     response：响应头
     filePath：文件存储在沙盒的地址 == 第三个参数中block的返回值
     error：错误信息
     */
    
    //2.1 创建请求对象
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString: downloadURL]];
    
    NSURLSessionDownloadTask *downloadTask = [manage downloadTaskWithRequest:request progress:^(NSProgress * _Nonnull downloadProgress) {//进度
        
        if (downloadProgress) {
            progress(downloadProgress);
        }
        
    } destination:^NSURL * _Nonnull(NSURL * _Nonnull targetPath, NSURLResponse * _Nonnull response) {
        
        NSString *caches = [NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, YES) lastObject];
        //拼接文件全路径
        NSString *fullpath = [caches stringByAppendingPathComponent:response.suggestedFilename];
        NSURL *filePathUrl = [NSURL fileURLWithPath:fullpath];
        
        return filePathUrl;
        
    } completionHandler:^(NSURLResponse * _Nonnull response, NSURL * _Nonnull filePath, NSError * _Nonnull error) {
        if (error) {
            faliure(error);
        }
        if(filePath){
//            [cache setObject:filePath forKey:downloadURL];
            success(filePath);
        }
    }];
    
    //3.启动任务
    [downloadTask resume];
}

@end

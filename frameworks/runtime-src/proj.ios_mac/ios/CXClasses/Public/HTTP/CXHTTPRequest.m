//
//  CXHTTPRequest.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/28.
//

#import "CXHTTPRequest.h"
#import "FuncUtiles.h"

#define DevelopSever 0
#define TestSever    1
#define ProductSever 0

#if DevelopSever
/** 接口前缀-开发服务器*/
NSString *const kApiPrefix = @"https://test-lin.hehe555.com:85";
NSString *const kCSharpApiPrefix = @"http://10.66.6.207:444";
#elif TestSever
/** 接口前缀-测试服务器*/
NSString *const kApiPrefix = @"https://vpser.harmonygames.cn:39911";
NSString *const kCSharpApiPrefix = @"https://vfhser.harmonygames.cn:39921";
#elif ProductSever
/** 接口前缀-生产服务器*/
NSString *const kApiPrefix = @"https://lin01.hehe555.com:85";
NSString *const kCSharpApiPrefix = @"https://win02-win.hehe555.com:444";
#endif

@interface CXHTTPRequest()


@end

@implementation CXHTTPRequest

+ (void)initialize {
    [super initialize];
    
    [self setRequestSerializer:HJRequestSerializerHTTP];
}

+ (NSDictionary *)getHTTPHeaders {
    NSMutableDictionary * mdict = [NSMutableDictionary new];
    [mdict setObject:Platform() forKey:@"xy-platform"];
    [mdict setObject:ChannelID() forKey:@"xy-channel"];
    [mdict setObject:Device() forKey:@"xy-device"];
    [mdict setObject:BundleID() forKey:@"xy-id"];
    [mdict setObject:AppVersion() forKey:@"xy-version"];
    
    return mdict;
}

+ (void)GETWithURL:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback {
    [self httpWithMethod:HJRequestMethodGET url:url parameters:parameters callback:callback];
}

+ (void)POSTWithURL:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback {
    [self httpWithMethod:HJRequestMethodPOST url:url parameters:parameters callback:callback];
}

+ (void)httpWithMethod:(HJRequestMethod)method url:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback {
    NSString *urlStr = [NSString stringWithFormat:@"%@/%@", kApiPrefix, url];
//    [MBProgressHUD showActivityMessageInView:@""];
    NSMutableDictionary * mutableBaseParameters = [NSMutableDictionary dictionaryWithDictionary:parameters];
    if ([CXClientModel instance].token.length > 0) {
        [mutableBaseParameters addEntriesFromDictionary:@{@"token":[CXClientModel instance].token}];
        parameters = [mutableBaseParameters copy];
    }
//    [self setResponseSerializer:HJResponseSerializerHTTP];
    [self HTTPWithMethod:method url:urlStr parameters:parameters headers:[self getHTTPHeaders] cachePolicy:HJCachePolicyIgnoreCache callback:^(id responseObject, BOOL isCache, NSError *error) {
//        [MBProgressHUD hideHUD];
        if (responseObject) {
            NSInteger code = [responseObject[@"code"] integerValue];
            if (code == PHPResponseCodeSuccess) {
                callback ? callback(responseObject, NO, nil) : nil;
            } else if (code == PHPResponseCode5000 || code == PHPResponseCode5001 || code == PHPResponseCode5002) {
                // 登录异常，重新登录
                [AppController logout];
                NSError *error = [NSError errorWithDomain:@"com.hehexq.http" code:code userInfo:@{NSLocalizedDescriptionKey: responseObject[@"desc"]}];
                callback ? callback(responseObject, NO, error) : nil;
            } else {
//                [[CXTools currentViewController] toast:responseObject[@"desc"]];
                NSError *error = [NSError errorWithDomain:@"com.hehexq.http" code:code userInfo:@{NSLocalizedDescriptionKey: responseObject[@"desc"]}];
                callback ? callback(responseObject, NO, error) : nil;
            }
        } else {
//            [[CXTools currentViewController] toast:@"请求异常，请重试"];
            NSError *error = [NSError errorWithDomain:@"com.hehexq.http" code:-1 userInfo:@{NSLocalizedDescriptionKey: @"请求异常，请重试"}];
            callback ? callback(responseObject, NO, error) : nil;
        }
        
    }];
}

+ (void)csharp_httpWithMethod:(HJRequestMethod)method url:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback {
    NSString *urlStr = [NSString stringWithFormat:@"%@%@", kCSharpApiPrefix, url];
//    NSMutableDictionary * mutableBaseParameters = [NSMutableDictionary dictionaryWithDictionary:parameters];
//    if ([CXClientModel instance].token.length > 0) {
//        [mutableBaseParameters addEntriesFromDictionary:@{@"token":[CXClientModel instance].token}];
//        parameters = [mutableBaseParameters copy];
//    }
    NSString *tempURL = [NSString stringWithFormat:@"%@?token=%@",urlStr,[CXClientModel instance].token];
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:tempURL] cachePolicy:NSURLRequestReloadIgnoringLocalCacheData timeoutInterval:30];
    NSURLSession *sharedSession = [NSURLSession sharedSession];
    NSURLSessionDataTask *dataTask = [sharedSession dataTaskWithRequest:request completionHandler:^(NSData * _Nullable data, NSURLResponse * _Nullable response, NSError * _Nullable error) {
        if (data && (error == nil)) {
            // 网络访问成功
            callback ? callback(data, NO, nil) : nil;
        } else {
            // 网络访问失败
            NSLog(@"error=%@",error);
        }
    }];
    
    [dataTask resume];
    
//    [self setResponseSerializer:HJResponseSerializerHTTP];
//    [self HTTPWithMethod:method url:urlStr parameters:parameters headers:[self getHTTPHeaders] cachePolicy:HJCachePolicyIgnoreCache callback:^(id responseObject, BOOL isCache, NSError *error) {
//        callback ? callback(responseObject, NO, nil) : nil;
//    }];
}

@end

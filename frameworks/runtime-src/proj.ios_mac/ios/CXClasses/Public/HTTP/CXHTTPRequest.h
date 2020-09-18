//
//  CXHTTPRequest.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/28.
//

#import "HJNetwork.h"

typedef NS_ENUM(NSUInteger, PHPResponseCode) {
    PHPResponseCodeSuccess = 200,
    PHPResponseCodeUserNotFound = 2000,
    PHPResponseCodeClientVersionTooOld = 3000,
    PHPResponseCode5000 = 5000,
    PHPResponseCode5001 = 5001,
    PHPResponseCode5002 = 5002,
};

@interface CXHTTPRequest : HJNetwork

+ (void)GETWithURL:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback;

+ (void)POSTWithURL:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback;

+ (void)httpWithMethod:(HJRequestMethod)method url:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback;

+ (void)csharp_httpWithMethod:(HJRequestMethod)method url:(NSString *)url parameters:(NSDictionary *)parameters callback:(HJHttpRequest)callback;

@end


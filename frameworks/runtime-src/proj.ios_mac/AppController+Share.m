//
//  AppController+Share.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/19.
//

#import "AppController+Share.h"

@implementation AppController (Share)

// 微信分享(网页分享)
+ (void)WXShareIOSforUrl:(NSString *)url Title:(NSString *)tit Desc:(NSString *)desc image:(NSString *)image platform:(NSString *)platform
{
    if ([WXApi isWXAppInstalled]) {
        WXMediaMessage * webmsg = [WXMediaMessage message];
        webmsg.title = tit;
        webmsg.description = desc;
        // 缩略图
        UIImage * img = [UIImage imageWithContentsOfFile:image];
        if (img) {
            NSData *imageData = [self compressWithOriginalImage:img maxLength:30*1024];
            UIImage *shareImage = [UIImage imageWithData:imageData];
            [webmsg setThumbImage:shareImage];
        }
        WXWebpageObject * webobj = [WXWebpageObject object];
        webobj.webpageUrl = url;
        webmsg.mediaObject = webobj;
        
        SendMessageToWXReq * req  = [[SendMessageToWXReq alloc]init];
        req.bText = NO;
        req.message = webmsg;
        if ([platform isEqualToString:@"WEIXIN_CIRCLE"]) {
            req.scene = WXSceneTimeline; // 朋友圈
        } else {
            req.scene = WXSceneSession;
        }
        
        [WXApi sendReq:req completion:nil];
        
        NSLog(@"已经发送req");
    }
}

// (文本)
+ (void)WXShareIOSforDescription:(NSString *)des platform:(NSString *)platform
{
    SendMessageToWXReq * req= [[SendMessageToWXReq alloc]init];
    req.text = des;
    req.bText = YES;
    if ([platform isEqualToString:@"WEIXIN_CIRCLE"]) {
        req.scene = WXSceneTimeline; // 朋友圈
    } else {
        req.scene = WXSceneSession; 
    }
    
    [WXApi sendReq:req completion:nil];
}

// (截图)
+ (void)WXShareIOSforImage:(NSString *)path platform:(NSString *)platform
{
    if (path.length <= 0) {
        return;
    }
    
    NSData *imageData = [NSData dataWithContentsOfFile: path];
    
    WXMediaMessage * msg = [WXMediaMessage message];
    
    WXImageObject * imgObj = [WXImageObject object];
    imgObj.imageData = imageData ;
    msg.mediaObject = imgObj;
    
    SendMessageToWXReq * req= [[SendMessageToWXReq alloc]init];
    req.bText = NO;
    req.message = msg;
    req.scene = [platform isEqualToString:@"WEIXIN"] ? WXSceneSession : WXSceneTimeline;
    
    [WXApi sendReq:req completion:nil];
}

// 等比压缩分辨率
+ (NSData *)imageWithImage:(UIImage*)image scaledToSize:(CGSize)newSize;
{
    UIGraphicsBeginImageContext(newSize);
    [image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
    UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return UIImageJPEGRepresentation(newImage, 1.0);
}

// 压缩字节大小
+ (UIImage *)zipImgData:(NSData *)data
{
    UIImage * img = [UIImage imageWithData:data];
    while (data.length>30000){
        data = UIImageJPEGRepresentation(img, 0.1);
        img = [UIImage imageWithData:data];
    }
    return img;
}

+ (NSData *)compressWithOriginalImage:(UIImage *)image maxLength:(NSUInteger)maxLength{
    CGFloat compression = 1;
    NSData *data = UIImageJPEGRepresentation(image, compression);
    if (data.length <= maxLength) {
        return data;
    }
    CGFloat max = 1;
    CGFloat min = 0;
    for (int i = 0; i < 6; ++i) {
        
        UIImage *resultImage = [UIImage imageWithData:data];
        CGFloat ratio = (CGFloat)maxLength / data.length;
        CGSize size = CGSizeMake((NSUInteger)(resultImage.size.width * sqrtf(ratio)), (NSUInteger)(resultImage.size.height * sqrtf(ratio)));
        UIGraphicsBeginImageContext(size);
        [resultImage drawInRect:CGRectMake(0, 0, size.width, size.height)];
        resultImage = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();

        data = UIImageJPEGRepresentation(resultImage, 1);
        compression = (max + min) / 2;

        if (data.length <= maxLength) {
            return data;
        }
        if (data.length < maxLength) {
            min = compression;
        } else if (data.length > maxLength * 0.9) {
            max = compression;
        } else {
            break;
        }

        data = UIImageJPEGRepresentation(resultImage, compression);
        if (data.length <= maxLength) {
            return data;
        }
    }
    return data;
}

@end

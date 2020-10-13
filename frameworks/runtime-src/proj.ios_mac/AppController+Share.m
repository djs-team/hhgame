//
//  AppController+Share.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/19.
//

#import "AppController+Share.h"

@implementation AppController (Share)

// 微信分享(网页分享)
+ (void)WXShareIOSforUrl:(NSString *)url Title:(NSString *)tit Desc:(NSString *)desc image:(NSString *)image
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
        req.scene = WXSceneSession; // 默认发送到聊天界面
        
        [WXApi sendReq:req completion:nil];
        
        NSLog(@"已经发送req");
    }
}

// (文本)
+ (void)WXShareIOSforDescription:(NSString *)des isTimeLine:(BOOL)isTimeLine
{
    SendMessageToWXReq * req= [[SendMessageToWXReq alloc]init];
    req.text = des;
    req.bText = YES;
    if (isTimeLine == YES) {
        req.scene = WXSceneTimeline; // 朋友圈
    } else {
        req.scene = WXSceneSession; 
    }
    
    [WXApi sendReq:req completion:nil];
}

// (截图)
+ (void)WXShareIOSforImage:(NSString *)path
{
    if (path.length <= 0) {
        return;
    }
    NSLog(@"从cocos发过来的图片地址----> %@",path);
    UIImage * img = [UIImage imageWithContentsOfFile:path];
    if (!img) {
        return;
    }
    // 根据原图压缩一半分辨率
    float h = img.size.height;
    float w = img.size.width;
    NSData * newdata = [self imageWithImage:img scaledToSize:CGSizeMake(w/2, h/2)]; // 调整图片分辨率
    UIImage * newimg =[self zipImgData:newdata]; // 压缩图片字节
    
    
    WXMediaMessage * msg = [WXMediaMessage message];
    [msg setThumbImage:newimg];
    
    WXImageObject * imgObj = [WXImageObject object];
    imgObj.imageData = newdata ;
    msg.mediaObject = imgObj;
    
    SendMessageToWXReq * req= [[SendMessageToWXReq alloc]init];
    req.bText = NO;
    req.message = msg;
    req.scene = WXSceneTimeline;
    
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

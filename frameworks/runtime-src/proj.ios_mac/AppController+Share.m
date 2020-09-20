//
//  AppController+Share.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/19.
//

#import "AppController+Share.h"

@implementation AppController (Share)

// 微信分享(网页分享)
+ (void)WXShareIOSforUrl:(NSString *)url Title:(NSString *)tit Desc:(NSString *)desc
{
    if ([WXApi isWXAppInstalled]) {
        WXMediaMessage * webmsg = [WXMediaMessage message];
        webmsg.title = tit;
        webmsg.description = desc;
        // 缩略图
        UIImage * img = [UIImage imageNamed:@"掼蛋"];// 本地icon，你可以由服务器发一个
        NSData  * imgData = UIImageJPEGRepresentation(img, 1.0);
        UIImage * wimg = [self zipImgData:imgData]; // 压缩图片字节
        [webmsg setThumbImage:wimg];
        
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
+ (void)WXShareIOSforDescription:(NSString *)des
{
    SendMessageToWXReq * req= [[SendMessageToWXReq alloc]init];
    req.text = des;
    req.bText = YES;
    req.scene = WXSceneTimeline; // 朋友圈
    
    [WXApi sendReq:req completion:nil];
}

// (截图)
+ (void)WXShareIOSforImage:(NSString *)path
{
    NSLog(@"从cocos发过来的图片地址----> %@",path);
    UIImage * img = [UIImage imageWithContentsOfFile:path];
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

@end

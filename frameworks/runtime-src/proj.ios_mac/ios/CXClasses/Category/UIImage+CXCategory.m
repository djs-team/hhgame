//
//  UIImage+CXCategory.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import "UIImage+CXCategory.h"

@implementation UIImage (CXCategory)

+ (UIImage *)imageWithColor:(UIColor *)color
{
    return [UIImage imageWithColor:color size:CGSizeMake(1, 1)];
}

+ (UIImage *)imageWithColor:(UIColor *)color size:(CGSize)size
{
    if (color == nil) {
        return nil;
    }
    
    CGRect rect=CGRectMake(0.0f, 0.0f, size.width, size.height);
    UIGraphicsBeginImageContext(rect.size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGContextSetFillColorWithColor(context, [color CGColor]);
    CGContextFillRect(context, rect);
    UIImage *theImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    return theImage;
}

+ (instancetype)gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2 {
    
    UIGraphicsBeginImageContext(size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGColorSpaceRef rgb = CGColorSpaceCreateDeviceRGB();
    CGContextScaleCTM(context, size.width, size.height);
    
    NSString *colorStr1 = [NSString stringWithFormat:@"%@", color1];
    NSArray *RGBArr1 = [colorStr1 componentsSeparatedByString:@" "];
    CGFloat r1 = [[RGBArr1 objectAtIndex:1] floatValue];
    CGFloat g1 = [[RGBArr1 objectAtIndex:2] floatValue];
    CGFloat b1 = [[RGBArr1 objectAtIndex:3] floatValue];
    
    NSString *colorStr2 = [NSString stringWithFormat:@"%@", color2];
    NSArray *RGBArr2 = [colorStr2 componentsSeparatedByString:@" "];
    CGFloat r2 = [[RGBArr2 objectAtIndex:1] floatValue];
    CGFloat g2 = [[RGBArr2 objectAtIndex:2] floatValue];
    CGFloat b2 = [[RGBArr2 objectAtIndex:3] floatValue];
    
    
    CGFloat colors[] = {
        
        r1, g1, b1, 1.0,
        
        r2, g2, b2, 1.0,
        
    };
    CGGradientRef backGradient = CGGradientCreateWithColorComponents(rgb, colors, NULL, sizeof(colors)/(sizeof(colors[0])*4));
    CGColorSpaceRelease(rgb);
    
    //设置颜色渐变的方向，范围在(0,0)与(1.0,1.0)之间，如(0,0)(1.0,0)代表水平方向渐变,(0,0)(0,1.0)代表竖直方向渐变
    
    CGContextDrawLinearGradient(context, backGradient, CGPointMake(0, 0), CGPointMake(1.0, 0), kCGGradientDrawsBeforeStartLocation);
    
    UIImage * image = UIGraphicsGetImageFromCurrentImageContext();
    
    CGGradientRelease(backGradient);
    CGContextRelease(context);
    
    return image;
}

+ (instancetype)gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2 endPoint: (CGPoint)endPoint {
    UIGraphicsBeginImageContext(size);
    CGContextRef context = UIGraphicsGetCurrentContext();
    CGColorSpaceRef rgb = CGColorSpaceCreateDeviceRGB();
    CGContextScaleCTM(context, size.width, size.height);
    
    NSString *colorStr1 = [NSString stringWithFormat:@"%@", color1];
    NSArray *RGBArr1 = [colorStr1 componentsSeparatedByString:@" "];
    CGFloat r1 = [[RGBArr1 objectAtIndex:1] floatValue];
    CGFloat g1 = [[RGBArr1 objectAtIndex:2] floatValue];
    CGFloat b1 = [[RGBArr1 objectAtIndex:3] floatValue];
    
    NSString *colorStr2 = [NSString stringWithFormat:@"%@", color2];
    NSArray *RGBArr2 = [colorStr2 componentsSeparatedByString:@" "];
    CGFloat r2 = [[RGBArr2 objectAtIndex:1] floatValue];
    CGFloat g2 = [[RGBArr2 objectAtIndex:2] floatValue];
    CGFloat b2 = [[RGBArr2 objectAtIndex:3] floatValue];
    
    
    CGFloat colors[] = {
        
        r1, g1, b1, 1.0,
        
        r2, g2, b2, 1.0,
        
    };
    CGGradientRef backGradient = CGGradientCreateWithColorComponents(rgb, colors, NULL, sizeof(colors)/(sizeof(colors[0])*4));
    CGColorSpaceRelease(rgb);
    
    //设置颜色渐变的方向，范围在(0,0)与(1.0,1.0)之间，如(0,0)(1.0,0)代表水平方向渐变,(0,0)(0,1.0)代表竖直方向渐变
    
    CGContextDrawLinearGradient(context, backGradient, CGPointMake(0, 0), endPoint, kCGGradientDrawsBeforeStartLocation);
    
    UIImage * image = UIGraphicsGetImageFromCurrentImageContext();
    
    CGGradientRelease(backGradient);
    CGContextRelease(context);
    
    return image;
}


+ (instancetype)x_gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2 {
    return [self gradientImageWithSize:size Color1:color1 color2:color2];
}
+ (instancetype)y_gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2 {
    return [self gradientImageWithSize:size Color1:color1 color2:color2 endPoint:CGPointMake(0, 1)];
}

@end

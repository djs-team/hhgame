//
//  UIImage+CXCategory.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface UIImage (CXCategory)

+ (UIImage *)imageWithColor:(UIColor *)color;
+ (UIImage *)imageWithColor:(UIColor *)color size:(CGSize)size;

+ (instancetype)x_gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2;
+ (instancetype)y_gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2;

+ (instancetype)gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2;

+ (instancetype)gradientImageWithSize:(CGSize)size Color1:(UIColor *)color1 color2:(UIColor *)color2 endPoint:(CGPoint)endPoint;



@end

NS_ASSUME_NONNULL_END

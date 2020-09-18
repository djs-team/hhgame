//
//  NSString+CXCategory.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/7.
//

#import "NSString+CXCategory.h"

@implementation NSString (CXCategory)

@end

@implementation NSString (Size)

- (CGSize)sizeWithFont:(UIFont *)font maxW:(CGFloat)maxW
{
    NSMutableDictionary *attrs = [NSMutableDictionary dictionary];
    attrs[NSFontAttributeName] = font;
    CGSize maxSize = CGSizeMake(maxW, MAXFLOAT);
    
    return [self boundingRectWithSize:maxSize options:NSStringDrawingUsesLineFragmentOrigin attributes:attrs context:nil].size;
}

- (CGSize)sizeWithFont:(UIFont *)font
{
    if (self && self.length) {
        return [self sizeWithFont:font maxW:MAXFLOAT];
    } else {
        return CGSizeZero;
    }
}

@end

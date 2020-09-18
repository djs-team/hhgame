//
//  NSString+CXCategory.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/7.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NSString (CXCategory)

@end

@interface NSString (Size)

- (CGSize)sizeWithFont:(UIFont *)font maxW:(CGFloat)maxW;
- (CGSize)sizeWithFont:(UIFont *)font;

@end


NS_ASSUME_NONNULL_END

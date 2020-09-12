//
//  CXTools.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import "CXTools.h"

@implementation CXTools

+ (UIViewController *)currentViewController {
    
    UIViewController* vc = [UIApplication sharedApplication].keyWindow.rootViewController;

    while (1) {
        
        if ([vc isKindOfClass:[UITabBarController class]]) {
            
            vc = ((UITabBarController*)vc).selectedViewController;
            
        }
        
        if ([vc isKindOfClass:[UINavigationController class]]) {
            
            vc = ((UINavigationController*)vc).visibleViewController;
            
        }
        
        if (vc.presentedViewController) {
            
            vc = vc.presentedViewController;
            
        } else {
            
            break;
            
        }
    }
    return vc;
}

+ (NSMutableArray*)splitArray:(NSArray*)array withSubSize:(int)subSize{
    unsigned long count = array.count%subSize == 0?(array.count/subSize):(array.count/subSize+1);
    NSMutableArray *arr = [[NSMutableArray alloc]init];
    for (int i=0; i<count; i++) {
        int index = i*subSize;
        NSMutableArray *arr1 = [[NSMutableArray alloc]init];
        [arr1 removeAllObjects];
        int j = index;
        while (j<subSize*(i+1)&&j<array.count) {
            [arr1 addObject:[array objectAtIndex:j]];
            j += 1;
        }
        [arr addObject:[arr1 copy]];
    }
    return arr;
}


@end

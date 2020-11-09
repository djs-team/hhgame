//
//  CXPhotoManager.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXPhotoManager : NSObject <UIImagePickerControllerDelegate, UINavigationControllerDelegate>

+ (CXPhotoManager *)manager;

- (void)showPickerWith:(UIViewController *)viewController
              putParam:(NSString *)putParam
             allowEdit:(BOOL)allowEdit
         completeBlock:(void (^)(NSString *imageUrl))complete;


@end

NS_ASSUME_NONNULL_END

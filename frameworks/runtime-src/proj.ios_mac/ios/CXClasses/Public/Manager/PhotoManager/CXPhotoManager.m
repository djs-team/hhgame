//
//  CXPhotoManager.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import "CXPhotoManager.h"
#import <AliyunOSSiOS/AliyunOSSiOS.h>

@interface CXPhotoManager()
{
    void (^ImageBlock)(NSString *imageUrl);
}
@end

@implementation CXPhotoManager

static id _manager;
+ (CXPhotoManager *)manager {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        _manager = [[CXPhotoManager alloc] init];
    });
    
    return _manager;
}

- (void)showPickerWith:(UIViewController *)viewController
             allowEdit:(BOOL)allowEdit
         completeBlock:(void (^)(NSString *imageUrl))complete {
    ImageBlock = complete;
    
    UIAlertController *sheet = [UIAlertController alertControllerWithTitle:@"选择照片" message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    [sheet addAction:[UIAlertAction actionWithTitle:@"相册" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
        imagePickerController.delegate = self;
        imagePickerController.allowsEditing = allowEdit;
        imagePickerController.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        
        [viewController presentViewController:imagePickerController animated:YES completion:nil];
    }]];
    [sheet addAction:[UIAlertAction actionWithTitle:@"拍摄" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
        imagePickerController.delegate = self;
        imagePickerController.allowsEditing = allowEdit;
        imagePickerController.sourceType = UIImagePickerControllerSourceTypeCamera;
        
        [viewController presentViewController:imagePickerController animated:YES completion:nil];
    }]];
    [sheet addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:nil]];
    
    [viewController presentViewController:sheet animated:YES completion:nil];
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:nil];
    UIImage *image = [info objectForKey:UIImagePickerControllerEditedImage];

    [self getUploadTokenWithImage:image];
}

- (void)getUploadTokenWithImage:(UIImage *)image {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/api/alists/getststoken" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           CXUploadImageTokenModel *uploadImageModel = [CXUploadImageTokenModel modelWithJSON:responseObject[@"data"]];
           [weakSelf uploadImageRequestWithImage:image uploadImageModel:uploadImageModel];
       }
    }];
}

- (void)uploadImageRequestWithImage:(UIImage *)image uploadImageModel:(CXUploadImageTokenModel *)uploadImageModel {
    id<OSSCredentialProvider> credential = [[OSSStsTokenCredentialProvider alloc] initWithAccessKeyId:uploadImageModel.AccessKeyId secretKeyId:uploadImageModel.AccessKeySecret securityToken:uploadImageModel.SecurityToken];
    OSSClient *client = [[OSSClient alloc] initWithEndpoint:uploadImageModel.Expiration credentialProvider:credential];
    OSSPutObjectRequest *put = [[OSSPutObjectRequest alloc] init];
    put.contentType = @"image/jpg";
    put.bucketName = uploadImageModel.BucketName;
    //memberpid/用户id/身份证图片
    put.objectKey = [NSString stringWithFormat:@"memberpid/user/%@.jpg", [NSDate currentTime]];
    NSData *imageData = UIImageJPEGRepresentation(image, 0.5);
    put.uploadingData = imageData;
    
    OSSTask *putTask = [client putObject:put];
    [putTask continueWithBlock:^id _Nullable(OSSTask * _Nonnull task) {
        if (!task.error) {
            if (ImageBlock) {
                ImageBlock(put.objectKey);
            }
        }
        return nil;
    }];
    [putTask waitUntilFinished];
}


@end

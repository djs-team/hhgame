//
//  CXWithdrawAccountViewController.m
//  hairBall
//
//  Created by mahong yang on 2019/11/15.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXWithdrawAccountViewController.h"
#import "CXSelectedPhotoView.h"
#import "CXMineWalletModel.h"
#import <AliyunOSSiOS/AliyunOSSiOS.h>

@interface CXWithdrawAccountViewController ()
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIButton *commitButton;
@property (weak, nonatomic) IBOutlet UITextField *accountTextField;

@property (weak, nonatomic) IBOutlet UIButton *zheng_btn;
@property (weak, nonatomic) IBOutlet UIButton *fan_btn;

@property (nonatomic, strong) CXMineWalletModel *currentModel;

@property (nonatomic, strong) CXUploadImageTokenModel *uploadImageModel;
@property (nonatomic, strong) UIImage *zhengImage;
@property (nonatomic, copy) NSString *zhengImageUrl;
@property (nonatomic, strong) UIImage *fanImage;
@property (nonatomic, copy) NSString *fanImageUrl;
@property (nonatomic, assign) BOOL isZhengAction;


@end

@implementation CXWithdrawAccountViewController

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"收款账号设置";
    self.view.backgroundColor = [UIColor whiteColor];
    
    _bgView.layer.cornerRadius = 12;
    _commitButton.layer.cornerRadius = 25;
    
    self.commitButton.userInteractionEnabled = NO;
    
    self.zheng_btn.hidden = YES;
    self.fan_btn.hidden = YES;
    
    [self getUploadToken];
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self getAccountDetail];
}

- (void)getAccountDetail {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/cashinfo" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.currentModel = [CXMineWalletModel modelWithJSON:responseObject[@"data"]];
            [weakSelf reloadData];
        }
    }];
}

- (void)reloadData {
    self.accountTextField.text = _currentModel.zfb;
    if ([_currentModel.status integerValue] == 0) {
        [self.commitButton setTitle:@"修改" forState:UIControlStateNormal];
        self.commitButton.userInteractionEnabled = YES;
        
        self.zheng_btn.hidden = NO;
        self.fan_btn.hidden = NO;
    } else if ([_currentModel.status integerValue] == 1) {
        [self.commitButton setTitle:@"提交" forState:UIControlStateNormal];
        self.commitButton.userInteractionEnabled = YES;
        self.zheng_btn.hidden = NO;
        self.fan_btn.hidden = NO;
    } else {
        [self.commitButton setTitle:@"审核中" forState:UIControlStateNormal];
        self.commitButton.userInteractionEnabled = NO;
    }
}

- (IBAction)commitAction:(id)sender {
    if (self.zhengImageUrl.length == 0 || self.fanImageUrl.length == 0) {
        [self toast:@"请选择身份证照片"];
        return;
    }
    if ([_currentModel.status integerValue] == 0 || [_currentModel.status integerValue] == 1) {
        NSDictionary *param = @{
            @"apliuserid":self.accountTextField.text,
            @"justpic":_zhengImageUrl,
            @"backpic":_fanImageUrl,
            @"type":@"1",
        };
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/AliPay/bindaplipay" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                [self toast:@"绑定成功"];
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [self.navigationController popViewControllerAnimated:YES];
                });
            } else {
                [self toast: responseObject[@"desc"]];
            }
        }];
    }
}

#pragma mark - Iamge

- (IBAction)zhegnBtnAction:(id)sender {
    _isZhengAction = YES;
    CXSelectedPhotoView *photoView = [CXSelectedPhotoView shareInstance];
    MJWeakSelf
    photoView.selectedPhotoBlock = ^(UIImage * _Nonnull photo) {
        weakSelf.zhengImage = photo;
        [weakSelf uploadImageRequest];
    };
    [[CXTools currentViewController] lew_presentPopupView:photoView animation:nil];
}

- (IBAction)fanBtnAction:(id)sender {
    _isZhengAction = NO;
    CXSelectedPhotoView *photoView = [CXSelectedPhotoView shareInstance];
    MJWeakSelf
    photoView.selectedPhotoBlock = ^(UIImage * _Nonnull photo) {
        weakSelf.fanImage = photo;
        [weakSelf uploadImageRequest];
    };
    [[CXTools currentViewController] lew_presentPopupView:photoView animation:nil];
}

- (void)getUploadToken {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/api/alists/getststoken" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           weakSelf.uploadImageModel = [CXUploadImageTokenModel modelWithJSON:responseObject[@"data"]];
       }
    }];
}

- (void)uploadImageRequest {
    id<OSSCredentialProvider> credential = [[OSSStsTokenCredentialProvider alloc] initWithAccessKeyId:self.uploadImageModel.AccessKeyId secretKeyId:self.uploadImageModel.AccessKeySecret securityToken:self.uploadImageModel.SecurityToken];
    OSSClient *client = [[OSSClient alloc] initWithEndpoint:self.uploadImageModel.Expiration credentialProvider:credential];
    OSSPutObjectRequest *put = [[OSSPutObjectRequest alloc] init];
    put.contentType = @"image/jpg";
    put.bucketName = self.uploadImageModel.BucketName;
    //memberpid/用户id/身份证图片
    put.objectKey = [NSString stringWithFormat:@"memberpid/%@/%@.jpg", [CXClientModel instance].userId, [NSDate currentTime]];
    NSData *imageData;
    if (_isZhengAction) {
        imageData = UIImageJPEGRepresentation(_zhengImage, 1.0);
    } else {
        imageData = UIImageJPEGRepresentation(_fanImage, 1.0);
    }
    put.uploadingData = imageData;
    
    OSSTask *putTask = [client putObject:put];
    __weak typeof(self) weakSelf = self;
    [putTask continueWithBlock:^id _Nullable(OSSTask * _Nonnull task) {
        if (!task.error) {
            dispatch_async(dispatch_get_main_queue(), ^{
                if (self->_isZhengAction) {
                    [weakSelf.zheng_btn setBackgroundImage:weakSelf.zhengImage forState:UIControlStateNormal];
                    weakSelf.zhengImageUrl = put.objectKey;
                } else {
                    [weakSelf.fan_btn setBackgroundImage:weakSelf.fanImage forState:UIControlStateNormal];
                    weakSelf.fanImageUrl = put.objectKey;
                }
                
            });
        }
        return nil;
    }];
    [putTask waitUntilFinished];
}

@end

//
//  CXSelectedPhotoView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/19.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXSelectedPhotoView.h"

static CXSelectedPhotoView *_selectPhoto = nil;

@interface CXSelectedPhotoView() <UIGestureRecognizerDelegate,UIImagePickerControllerDelegate,UIActionSheetDelegate,UINavigationControllerDelegate>

@property (nonatomic,strong)UIButton *takePhotoBtn;
@property (nonatomic,strong)UIButton *photoBtn;
@property (nonatomic,strong)UIButton *cancleBtn;
@property (nonatomic,strong)UIView *backView;

@end

@implementation CXSelectedPhotoView

+ (instancetype)shareInstance {
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        if (_selectPhoto == nil) {
            _selectPhoto = [[self alloc] init];
            _selectPhoto.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            _selectPhoto.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
            [_selectPhoto setupSubViews];
        }
    });
    
    return _selectPhoto;
}

#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:nil];
    UIImage *image = [info objectForKey:UIImagePickerControllerEditedImage];
    if (self.selectedPhotoBlock) {
        self.selectedPhotoBlock(image);
    }
}

#pragma mark -UIGestureRecognizerDelegate
- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch{
    if ([touch.view isDescendantOfView:self.backView]) {
        return NO;
    }
    return YES;
}

#pragma mark - Action
- (void)cancleClick{
    [self removeFromSuperview];
}

- (void)takephotoClick{
    if ([CXTools getVideoAuthStatus] == NO) {
        [CXTools showSettingAlertViewTitle:@"相机权限未开启" content:@"相机权限未开启，请进入系统【设置】>【隐私】>【相机】中打开开关,开启相机功能"];
        return;
    }
    [self removeFromSuperview];
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
    imagePickerController.delegate = self;
    imagePickerController.allowsEditing = YES;
    imagePickerController.sourceType = UIImagePickerControllerSourceTypeCamera;
    
    [[CXTools currentViewController] presentViewController:imagePickerController animated:YES completion:nil];
}

- (void)photoClick{
    if ([CXTools getVideoAuthStatus] == NO) {
        [CXTools showSettingAlertViewTitle:@"相机权限未开启" content:@"相机权限未开启，请进入系统【设置】>【隐私】>【相机】中打开开关,开启相机功能"];
        return;
    }
    [self removeFromSuperview];
    UIImagePickerController *imagePickerController = [[UIImagePickerController alloc] init];
    imagePickerController.delegate = self;
    imagePickerController.allowsEditing = YES;
    imagePickerController.sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
    
    [[CXTools currentViewController] presentViewController:imagePickerController animated:YES completion:nil];
}

#pragma mark - Subviews
-(void)setupSubViews{
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(cancleClick)];
    tap.delegate = self;
    [self addGestureRecognizer:tap];
    
    [self addSubview:self.backView];
    [self addSubview:self.takePhotoBtn];
    [self addSubview:self.photoBtn];
    [self addSubview:self.cancleBtn];
    
    [self.cancleBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 50*SCALE_W));
        make.left.equalTo(self.mas_left).offset(0);
        if (kStatusHeight>20) {
            make.bottom.equalTo(self.mas_bottom).offset(-49);
        }else{
            make.bottom.equalTo(self.mas_bottom).offset(0);
        }
    }];
    [self.photoBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 50*SCALE_W));
        make.left.equalTo(self.mas_left).offset(0);
        make.bottom.equalTo(self.cancleBtn.mas_top).offset(-5);
    }];
    [self.takePhotoBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 50*SCALE_W));
        make.left.equalTo(self.mas_left).offset(0);
        make.bottom.equalTo(self.photoBtn.mas_top).offset(-1);
    }];
    [self.backView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(0);
        make.right.equalTo(self.mas_right).offset(0);
        make.bottom.equalTo(self.mas_bottom).offset(0);
        make.top.equalTo(self.takePhotoBtn.mas_top).offset(0);
    }];
    
    UIView *line = [[UIView alloc]init];
    line.backgroundColor = UIColorHex(0xBBBBBB);
    [self addSubview:line];
    [line mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(0);
        make.right.equalTo(self.mas_right).offset(0);
        make.height.mas_equalTo(1);
        make.top.equalTo(self.takePhotoBtn.mas_bottom).offset(0);
    }];
    
    UIView *line1 = [[UIView alloc]init];
    line1.backgroundColor = UIColorHex(0xBBBBBB);
    [self addSubview:line1];
    [line1 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(0);
        make.right.equalTo(self.mas_right).offset(0);
        make.height.mas_equalTo(5);
        make.top.equalTo(self.photoBtn.mas_bottom).offset(0);
    }];
}

#pragma mark - Setter
-(UIView*)backView{
    if (!_backView) {
        _backView = [[UIView alloc]init];
        _backView.backgroundColor = [UIColor whiteColor];
    }
    return _backView;
}
-(UIButton*)takePhotoBtn{
    if (!_takePhotoBtn) {
        _takePhotoBtn = [[UIButton alloc]init];
        [_takePhotoBtn setTitle:@"拍摄" forState:UIControlStateNormal];
        [_takePhotoBtn setTitleColor:UIColorHex(333333) forState:UIControlStateNormal];
        _takePhotoBtn.titleLabel.font = [UIFont systemFontOfSize:16.0f];
        [_takePhotoBtn addTarget:self action:@selector(takephotoClick) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _takePhotoBtn;
}
-(UIButton*)photoBtn{
    if (!_photoBtn) {
        _photoBtn = [[UIButton alloc]init];
        [_photoBtn setTitle:@"上传照片" forState:UIControlStateNormal];
        [_photoBtn setTitleColor:UIColorHex(333333) forState:UIControlStateNormal];
        _photoBtn.titleLabel.font = [UIFont systemFontOfSize:16.0f];
        [_photoBtn addTarget:self action:@selector(photoClick) forControlEvents:UIControlEventTouchUpInside];
        
    }
    return _photoBtn;
}
-(UIButton*)cancleBtn{
    if (!_cancleBtn) {
        _cancleBtn = [[UIButton alloc]init];
        [_cancleBtn setTitle:@"取消" forState:UIControlStateNormal];
        [_cancleBtn setTitleColor:UIColorHex(333333) forState:UIControlStateNormal];
        _cancleBtn.titleLabel.font = [UIFont systemFontOfSize:16.0f];
        [_cancleBtn addTarget:self action:@selector(cancleClick) forControlEvents:UIControlEventTouchUpInside];
    }
    return _cancleBtn;
}

@end

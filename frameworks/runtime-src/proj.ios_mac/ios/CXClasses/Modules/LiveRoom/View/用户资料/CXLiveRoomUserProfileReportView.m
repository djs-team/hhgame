//
//  CXLiveRoomUserProfileReportView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/24.
//

#import "CXLiveRoomUserProfileReportView.h"
#import "CXLiveRoomUserProfileReportCell.h"
#import "CXSelectedPhotoView.h"
#import <AliyunOSSiOS/AliyunOSSiOS.h>

@interface CXLiveRoomUserProfileReportView () <UITableViewDataSource, UITableViewDelegate, UITextViewDelegate>

@property (weak, nonatomic) IBOutlet UIButton *commitbtn;
@property (weak, nonatomic) IBOutlet UIButton *blockBtn;

@property (weak, nonatomic) IBOutlet UITextView *reasonTextView;
@property (weak, nonatomic) IBOutlet UILabel *textViewLengthLabel;

@property (weak, nonatomic) IBOutlet UIButton *image1Btn;
@property (weak, nonatomic) IBOutlet UIButton *image2Btn;
@property (weak, nonatomic) IBOutlet UIButton *image3Btn;
@property (weak, nonatomic) IBOutlet UILabel *imageLengthLabel;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@property (nonatomic, strong) NSMutableArray *imageArrays;
@property (nonatomic, strong) NSMutableArray *itemArrays;
@property (nonatomic, strong) NSMutableArray *selectedItemArrays;
@property (nonatomic, strong) NSMutableArray *selectedItemContentArrays;
@property (nonatomic, assign) BOOL isBlock;

@property (nonatomic, strong) CXUploadImageTokenModel *uploadImageModel;

@end

@implementation CXLiveRoomUserProfileReportView
- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(318, 550));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeAlert;
        
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    self.commitbtn.layer.masksToBounds = YES;
    self.commitbtn.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [self.commitbtn setBackgroundImage:image forState:UIControlStateNormal];
    
    self.reasonTextView.delegate = self;
    
    _imageArrays = [NSMutableArray array];
    _image1Btn.layer.borderColor = UIColorHex(0x888888).CGColor;
    _image1Btn.layer.borderWidth = 0.5;
    _image2Btn.layer.borderColor = UIColorHex(0x888888).CGColor;
    _image2Btn.layer.borderWidth = 0.5;
    _image3Btn.layer.borderColor = UIColorHex(0x888888).CGColor;
    _image3Btn.layer.borderWidth = 0.5;
    [self reloadImageBtn];
    
    _isBlock = YES;
    
    _itemArrays = [NSMutableArray array];
    _selectedItemArrays = [NSMutableArray array];
    _selectedItemContentArrays = [NSMutableArray array];
        
    _mainTableView.dataSource = self;
    _mainTableView.delegate = self;
    _mainTableView.rowHeight = UITableViewAutomaticDimension;
    _mainTableView.estimatedRowHeight = 24;
    [_mainTableView registerNib:[UINib nibWithNibName:@"CXLiveRoomUserProfileReportCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomUserProfileReportCellID"];
    
    [self getReportData];
    [self getUploadToken];
}

- (void)getUploadToken {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/api/alists/getststoken" parameters:@{} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           weakSelf.uploadImageModel = [CXUploadImageTokenModel modelWithJSON:responseObject[@"data"]];
       }
    }];
}

- (void)getReportData {
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/api/report/option" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.itemArrays = [NSMutableArray arrayWithArray:responseObject[@"data"]];
            [weakSelf.mainTableView reloadData];
        }
    }];
}

#pragma mark - UITableViewDataSource, UITableViewDelegate
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _itemArrays.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomUserProfileReportCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLiveRoomUserProfileReportCellID"];
    NSDictionary *dict = _itemArrays[indexPath.row];
    cell.itemLabel.text = dict[@"content"];
    if ([self.selectedItemArrays containsObject:dict[@"id"]]) {
        [cell.selectedLogo setImage:[UIImage imageNamed:@"home_selected_on"]];
    } else {
        [cell.selectedLogo setImage:[UIImage imageNamed:@"home_selected_off"]];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSDictionary *dict = _itemArrays[indexPath.row];
    if ([self.selectedItemArrays containsObject:dict[@"id"]]) {
        [self.selectedItemArrays removeObject:dict[@"id"]];
        [self.selectedItemContentArrays removeObject:dict[@"content"]];
        
    } else {
        [self.selectedItemArrays addObject:dict[@"id"]];
        [self.selectedItemContentArrays addObject:dict[@"content"]];
    }
    
    [tableView reloadData];
}

#pragma mark - UITextViewDelegate
- (void)textViewDidChange:(UITextView *)textView {
    if (textView.text.length > 100) {
        textView.text = [textView.text substringToIndex:100];
    }
    _textViewLengthLabel.text = [NSString stringWithFormat:@"%ld/100字", textView.text.length];
}

- (void)reloadImageBtn {
    if (_imageArrays.count == 0) {
        _image2Btn.hidden = YES;
        _image3Btn.hidden = YES;
        [_image1Btn setImage:[UIImage imageNamed:@"add"] forState:UIControlStateNormal];
    } else if (_imageArrays.count == 1) {
        _image2Btn.hidden = NO;
        _image3Btn.hidden = YES;
        [_image2Btn setImage:[UIImage imageNamed:@"add"] forState:UIControlStateNormal];
    } else {
        _image2Btn.hidden = NO;
        _image3Btn.hidden = NO;
        [_image3Btn setImage:[UIImage imageNamed:@"add"] forState:UIControlStateNormal];
    }
    
    _imageLengthLabel.text = [NSString stringWithFormat:@"%ld/3张", _imageArrays.count];
}

- (IBAction)imageAction:(UIButton *)sender {
    CXSelectedPhotoView *photoView = [CXSelectedPhotoView shareInstance];
    MJWeakSelf
    photoView.selectedPhotoBlock = ^(UIImage * _Nonnull photo) {
        [weakSelf uploadImageRequest:photo btn:sender];
    };
    [[CXTools currentViewController] lew_presentPopupView:photoView animation:nil];
}

- (void)uploadImageRequest:(UIImage *)photo btn:(UIButton *)sender {
    id<OSSCredentialProvider> credential = [[OSSStsTokenCredentialProvider alloc] initWithAccessKeyId:self.uploadImageModel.AccessKeyId secretKeyId:self.uploadImageModel.AccessKeySecret securityToken:self.uploadImageModel.SecurityToken];
    OSSClient *client = [[OSSClient alloc] initWithEndpoint:self.uploadImageModel.Expiration credentialProvider:credential];
    OSSPutObjectRequest *put = [[OSSPutObjectRequest alloc] init];
    put.contentType = @"image/jpg";
    put.bucketName = self.uploadImageModel.BucketName;
    //memberpid/用户id/身份证图片
    put.objectKey = [NSString stringWithFormat:@"memberpid/%@/%@.jpg", [CXClientModel instance].userId, [NSDate currentTime]];
    NSData *imageData = UIImageJPEGRepresentation(photo, 0.5);
    put.uploadingData = imageData;
    
    OSSTask *putTask = [client putObject:put];
    __weak typeof(self) weakSelf = self;
    [putTask continueWithBlock:^id _Nullable(OSSTask * _Nonnull task) {
        if (!task.error) {
            dispatch_async(dispatch_get_main_queue(), ^{
                [weakSelf.imageArrays insertObject:put.objectKey atIndex:sender.tag];
                [sender setImage:photo forState:UIControlStateNormal];
                [weakSelf reloadImageBtn];
            });
        }
        return nil;
    }];
    [putTask waitUntilFinished];
}

- (IBAction)blockAction:(UIButton *)sender {
    if (sender.tag == 0) {
        _isBlock = NO;
        sender.tag = 1;
        [sender setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
    } else {
        _isBlock = YES;
        sender.tag = 0;
        [sender setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
    }
}

- (IBAction)commitAction:(id)sender {
    NSDictionary *param = @{
        @"user_id" : _userId,
        @"option_id": [_selectedItemArrays componentsJoinedByString:@","],
        @"contents": _reasonTextView.text,
        @"option_content":[_selectedItemContentArrays componentsJoinedByString:@","],
        @"img1":_imageArrays.count >= 1 ? _imageArrays[0] : @"",
        @"img2":_imageArrays.count >= 2 ? _imageArrays[1] : @"",
        @"img3":_imageArrays.count >= 3 ? _imageArrays[2] : @"",
        @"is_black":_isBlock == YES ? @"1" : @"2",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Report/addReport" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [CXTools showAlertWithMessage:@"举报成功"];
            [weakSelf hide];
        }
    }];
}

@end

//
//  CXMySettingWechatBindViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/4/13.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXMySettingWechatBindViewController.h"
#import "CXMySettingWechatBindView.h"
#import "CXMySettingPhoneBindViewController.h"
#import <WXApi.h>
#import <AFNetworking/AFNetworking.h>

@interface CXMySettingWechatBindViewController ()
@property (weak, nonatomic) IBOutlet UILabel *currentLoginLabel;
@property (weak, nonatomic) IBOutlet UILabel *currentPhoneLabel;
@property (weak, nonatomic) IBOutlet UIButton *bind_weixinBtn;
@property (weak, nonatomic) IBOutlet UIButton *bind_phoneBtn;

@property (weak, nonatomic) IBOutlet UIView *bind_weixinBGView;

@property (nonatomic, assign) BOOL isWechatBind;

@property (nonatomic, strong) NSString *wx_id;
@property (nonatomic, strong) NSString *origin_bind_id; //原绑定微信的用户id

@end

@implementation CXMySettingWechatBindViewController

- (void)dealloc {
    [[NSNotificationCenter defaultCenter] removeObserver:self];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.view.backgroundColor = [UIColor whiteColor];
    self.title = @"绑定";
    
    self.bind_weixinBtn.layer.cornerRadius = 12;
    self.bind_weixinBtn.layer.masksToBounds = YES;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(80, 24) Color1:UIColorHex(0xEC5BBA) color2:UIColorHex(0x7D3EF1)];
    [self.bind_weixinBtn setBackgroundImage:image forState:UIControlStateNormal];
    self.bind_phoneBtn.layer.cornerRadius = 12;
    self.bind_phoneBtn.layer.masksToBounds = YES;
    [self.bind_phoneBtn setBackgroundImage:image forState:UIControlStateNormal];

    if (self.bind_type.integerValue == 1) { // 绑定微信
        self.bind_phoneBtn.hidden = YES;
        NSString *phone = [CXClientModel instance].username;
        if (phone.length > 0) {
            self.currentPhoneLabel.text = [NSString stringWithFormat:@"%@****%@", [phone substringToIndex:3], [phone substringWithRange:NSMakeRange(phone.length - 4, 4)]];
        }
        
        [self getWechatBindStatus];
        
        [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(wxLogin:) name:kNSNotificationCenter_CXMySettingWechatBindViewController_weixin object:nil];
        
        self.bind_weixinBGView.hidden = NO;
    } else {
        self.currentLoginLabel.hidden = YES;
        self.currentPhoneLabel.hidden = YES;
        self.bind_weixinBGView.hidden = YES;
    }

}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    [self getWechatBindStatus];
}

#pragma mark - Https
- (void)getWechatBindStatus {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/is_band_wx" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            if ([responseObject[@"data"][@"is_band"] integerValue] == 1) {
                weakSelf.isWechatBind = YES;
            } else {
                weakSelf.isWechatBind = NO;
            }

            if (weakSelf.isWechatBind == YES) {
                NSString *wx_id = responseObject[@"data"][@"wx_id"];
                weakSelf.wx_id = wx_id;
                [weakSelf.bind_weixinBtn setTitle:@"解绑" forState:UIControlStateNormal];
            } else {
                [weakSelf.bind_weixinBtn setTitle:@"绑定" forState:UIControlStateNormal];
            }
        }
    }];
}

- (void)bindWechat {
    SendAuthReq *req = [[SendAuthReq alloc] init];
    req.scope = @"snsapi_userinfo";
    req.state = @"123";
    [WXApi sendReq:req completion:nil];
}

- (void)wxLogin:(NSNotification*)noti{
    //获取到code
    SendAuthResp *resp = noti.object;

    AFHTTPSessionManager *manager = [AFHTTPSessionManager manager];
    NSString *url = [NSString stringWithFormat:@"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%@&secret=%@&code=%@&grant_type=%@",WX_AppKey,WX_AppSecret,resp.code,@"authorization_code"];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    [manager.requestSerializer setValue:@"text/html; charset=utf-8" forHTTPHeaderField:@"Content-Type"];

    NSMutableSet *mgrSet = [NSMutableSet set];
    mgrSet.set = manager.responseSerializer.acceptableContentTypes;
    [mgrSet addObject:@"text/html"];
    //因为微信返回的参数是text/plain 必须加上 会进入fail方法
    [mgrSet addObject:@"text/plain"];
    [mgrSet addObject:@"application/json"];
    manager.responseSerializer.acceptableContentTypes = mgrSet;
    [manager GET:url parameters:nil headers:nil progress:nil success:^(NSURLSessionDataTask * _Nonnull task, id  _Nullable responseObject) {
        NSDictionary *resp = (NSDictionary*)responseObject;
        NSString *openid = resp[@"openid"];
        [self bindWechatWithOpenId:openid];
    } failure:nil];
}

- (void)bindWechatWithOpenId:(NSString *)openId {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/band_wx" parameters:@{@"wx_id":openId} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSString *bindId = responseObject[@"data"][@"id"];
            if (![bindId isEqualToString:[CXClientModel instance].userId]) {
                weakSelf.origin_bind_id = bindId;
                NSString *wx_id = responseObject[@"data"][@"wx_id"];
                weakSelf.wx_id = wx_id;
                [weakSelf showBindErrorWithOriginPhone:responseObject[@"data"][@"username"]];
            } else {
//                [weakSelf showBindSuccess];
                [weakSelf toast:@"绑定成功"];
                [weakSelf.navigationController popToRootViewControllerAnimated:YES];
            }
        }
    }];
}

- (void)unBindWechat {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/out_set_band" parameters:@{@"wx_id":_wx_id} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [weakSelf showUnBindSuccess];
        }
    }];
}

- (void)bindPhoneWithPhoneNumber:(NSString *)phoneNumber {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/band_phone" parameters:@{@"phone":phoneNumber} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSString *bindId = responseObject[@"data"][@"id"];
            if (![bindId isEqualToString:[CXClientModel instance].userId]) {
                weakSelf.origin_bind_id = bindId;
                NSString *wx_id = responseObject[@"data"][@"wx_id"];
                weakSelf.wx_id = wx_id;
                [weakSelf showBindErrorWithOriginPhone:responseObject[@"data"][@"username"]];
            } else {
                [weakSelf showBindSuccess];
            }
        }
    }];
}

- (IBAction)bindAction:(id)sender {
    if (self.bind_type.integerValue == 1) { //绑定微信
        if (self.isWechatBind == YES) {
            [self unBindWechat];
        } else {
            [self bindWechat];
        }
    } else { // 绑定手机号
        CXMySettingPhoneBindViewController *vc = [CXMySettingPhoneBindViewController new];
        [self.navigationController pushViewController:vc animated:YES];
    }
}

- (void)showBindSuccess {
    
    CXMySettingWechatBindView *bindView = [[NSBundle mainBundle] loadNibNamed:@"CXMySettingWechatBindView" owner:self options:nil].lastObject;
    bindView.cancelBtn.hidden = YES;
    bindView.titleLabel.text = @"绑定成功";
    bindView.descLabel.text = @"";

    [bindView show];
    
    [self getWechatBindStatus];
}

- (void)showBindErrorWithOriginPhone:(NSString *)originPhone {
    [self toast:[NSString stringWithFormat:@"该微信已绑定其他手机号（%@****%@),无法进行绑定。", [originPhone substringToIndex:3],[originPhone substringWithRange:NSMakeRange(originPhone.length - 4, 4)]]];
//    kWeakSelf
//    CXMySettingWechatBindView *bindView = [[NSBundle mainBundle] loadNibNamed:@"CXMySettingWechatBindView" owner:self options:nil].lastObject;
//    bindView.cancelBtn.hidden = YES;
//    bindView.titleLabel.text = @"绑定微信";
//    bindView.descLabel.text = [NSString stringWithFormat:@"该微信已绑定其他手机号（%@****%@),无法进行绑定，是否需要解绑原手机", [originPhone substringToIndex:3],[originPhone substringWithRange:NSMakeRange(originPhone.length - 4, 4)]];
//    bindView.bindViewBlock = ^(NSInteger tag) {
//        if (tag == 10) {
//            [weakSelf unBindWechat];
//        }
//    };
//
//    [bindView show];
}

- (void)showUnBindSuccess {
    kWeakSelf
    CXMySettingWechatBindView *bindView = [[NSBundle mainBundle] loadNibNamed:@"CXMySettingWechatBindView" owner:self options:nil].lastObject;
    bindView.cancelBtn.hidden = YES;
    bindView.titleLabel.text = @"解绑成功";
    bindView.descLabel.text = @"绑定当前手机号";
    bindView.bindViewBlock = ^(NSInteger tag) {
        if (tag == 10) {
            [weakSelf bindWechatWithOpenId:weakSelf.wx_id];
        }
    };

    [bindView show];
    
    [self getWechatBindStatus];
}

@end

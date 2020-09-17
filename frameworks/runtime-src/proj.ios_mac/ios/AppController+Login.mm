//
//  AppController+Login.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/9/11.
//

#import "AppController+Login.h"
#import "JVERIFICATIONService.h"
#import "HJNetwork.h"
#import "cocos2d.h"
#include "scripting/js-bindings/manual/ScriptingCore.h"

@implementation AppController (Login)

+ (void)weChatLoginWithMethod:(NSString *)method {
    [CXOCJSBrigeManager manager].wxLoginMethod = method;
    
    SendAuthReq *req = [[SendAuthReq alloc] init];
    req.scope = @"snsapi_userinfo";
    req.state = @"123";
    [WXApi sendReq:req completion:nil];
}

- (void)weChatLoginSuccess:(SendAuthResp*)authResp {
    //获取到code
    SendAuthResp *resp = authResp;
    
    NSString *url = [NSString stringWithFormat:@"https://api.weixin.qq.com/sns/oauth2/access_token?appid=%@&secret=%@&code=%@&grant_type=%@",WX_AppKey,WX_AppSecret,resp.code,@"authorization_code"];
    [HJNetwork GETWithURL:url parameters:nil cachePolicy:HJCachePolicyIgnoreCache callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSDictionary *resp = (NSDictionary*)responseObject;
            NSString *respStr = [resp jsonStringEncoded];
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].wxLoginMethod param:respStr];
        } else {
            [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].wxLoginMethod param:@""];
        }
    }];
}

+ (void)JPushLoginWithMethod:(NSString *)method {
    [CXOCJSBrigeManager manager].jpushLoginMethod = method;
    [JVERIFICATIONService getAuthorizationWithController:[CXTools currentViewController] completion:^(NSDictionary *result) {
        [AppController dispatchCustomEventWithMethod:[CXOCJSBrigeManager manager].jpushLoginMethod param:[result jsonStringEncoded]];
        [JVERIFICATIONService dismissLoginControllerAnimated:YES completion:nil];
    }];
}

//- (void)customUI {
//    JVUIConfig *config = [[JVUIConfig alloc] init];
//    config.navReturnImg = [UIImage imageNamed:@"back_white"];
//    config.agreementNavReturnImage = [UIImage imageNamed:@"close"];
//    config.autoLayout = YES;
//    config.navText = [[NSAttributedString alloc] initWithString:@"登录&注册" attributes:@{NSForegroundColorAttributeName:[UIColor whiteColor], NSFontAttributeName:[UIFont systemFontOfSize:18]}];
//    config.navDividingLineHidden = YES;
//    config.prefersStatusBarHidden = NO;
//    config.navTransparent = YES;
//    
//    config.authPageBackgroundImage = [UIImage imageNamed:@"login_bg"];
//    
////    _config = config;
////    _config.navControl = [[UIBarButtonItem alloc] initWithCustomView:self.rightPhoneBtn];
//    //  设置后，隐私协议栏文本修改为 文本1 + 运营商默认协议名称 + 文本2 + 开发者协议名称1 + 文本3 + 开发者协议文本2 + 文本4
////    config.privacyComponents = @[@"登录即同意",@"《用户协议》",@"《合合隐私协议》",@""];
//
//    config.navColor = [UIColor whiteColor];
//    config.sloganTextColor = [UIColor colorWithRed:187/255.0 green:188/255.0 blue:197/255.0 alpha:1/1.0];
//   
//    //logo
//    config.logoImg = [UIImage imageNamed:@"login_phone_top"];
//    CGFloat logoWidth = 154;
//    CGFloat logoHeight = 53;
//    CGFloat windowW = SCREEN_WIDTH;
//    JVLayoutConstraint *logoConstraintX = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
//    JVLayoutConstraint *logoConstraintY = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeTop multiplier:1 constant:kNavHeight+70];
//    JVLayoutConstraint *logoConstraintW = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeWidth multiplier:1 constant:logoWidth];
//    JVLayoutConstraint *logoConstraintH = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeHeight multiplier:1 constant:logoHeight];
//    config.logoConstraints = @[logoConstraintX,logoConstraintY,logoConstraintW,logoConstraintH];
//    
//    //横屏
//    JVLayoutConstraint *logoConstraintY1 = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeTop multiplier:1 constant:20];
//
//    config.logoHorizontalConstraints = @[logoConstraintX,logoConstraintY1,logoConstraintH,logoConstraintW];
//    
//    
//    //号码栏
//    JVLayoutConstraint *numberConstraintX = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
//    JVLayoutConstraint *numberConstraintY = [JVLayoutConstraint constraintWithAttribute: NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemLogo attribute:NSLayoutAttributeBottom multiplier:1 constant:116];
//    JVLayoutConstraint *numberConstraintW = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeWidth multiplier:1 constant:190];
//    JVLayoutConstraint *numberConstraintH = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeHeight multiplier:1 constant:30];
//
//    config.numberConstraints = @[numberConstraintX,numberConstraintY,numberConstraintW,numberConstraintH];
//    config.numberColor = [UIColor whiteColor];
//    config.numberSize = 28;
//    
//    
//    //slogan展示
//    JVLayoutConstraint *sloganConstraintX = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
//    JVLayoutConstraint *sloganConstraintY = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNumber attribute:NSLayoutAttributeBottom   multiplier:1 constant:8];
//    JVLayoutConstraint *sloganConstraintW = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeWidth multiplier:1 constant:130];
//    JVLayoutConstraint *sloganConstraintH = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeHeight multiplier:1 constant:20];
//    
//    config.sloganConstraints = @[sloganConstraintX,sloganConstraintY,sloganConstraintW,sloganConstraintH];
//    
//    
//    //登录按钮
//    UIImage *login_nor_image = [UIImage imageNamed:@"login_quickly_login_btn"];
//    config.logBtnImgs = @[login_nor_image,login_nor_image,login_nor_image];
//    config.logBtnText = @"";
//    CGFloat loginButtonWidth = 300;
//    CGFloat loginButtonHeight = 40;
//    JVLayoutConstraint *loginConstraintX = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
//    JVLayoutConstraint *loginConstraintY = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeTop relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSlogan attribute:NSLayoutAttributeBottom multiplier:1 constant:40];
//    JVLayoutConstraint *loginConstraintW = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeWidth multiplier:1 constant:loginButtonWidth];
//    JVLayoutConstraint *loginConstraintH = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeHeight multiplier:1 constant:loginButtonHeight];
//    config.logBtnConstraints = @[loginConstraintX,loginConstraintY,loginConstraintW,loginConstraintH];
//    
////    //勾选框
////    UIImage * uncheckedImg = [UIImage imageNamed:@"checkBox_unSelected"];
////    UIImage * checkedImg = [UIImage imageNamed:@"checkBox_selected"];
////    CGFloat checkViewWidth = 11;
////    CGFloat checkViewHeight = 11;
////    CGFloat spacing = (windowW - 300)/2;
////
////    config.uncheckedImg = uncheckedImg;
////    config.checkedImg = checkedImg;
////    JVLayoutConstraint *checkViewConstraintX = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeLeft relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeRight multiplier:1 constant:spacing];
////    JVLayoutConstraint *checkViewConstraintY = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemPrivacy attribute:NSLayoutAttributeBottom multiplier:1 constant:-20];
////
////    JVLayoutConstraint *checkViewConstraintW = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeWidth multiplier:1 constant:checkViewWidth];
////    JVLayoutConstraint *checkViewConstraintH = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeHeight multiplier:1 constant:checkViewHeight];
////    config.checkViewConstraints = @[checkViewConstraintX,checkViewConstraintY,checkViewConstraintW,checkViewConstraintH];
//    //隐私
//    config.privacyState = YES;
//    config.privacyTextFontSize = 11;
//    config.privacyTextAlignment = NSTextAlignmentCenter;
//    config.appPrivacyColor = @[[UIColor colorWithRed:187/255.0 green:188/255.0 blue:197/255.0 alpha:1/1.0],[UIColor colorWithRed:137/255.0 green:152/255.0 blue:255/255.0 alpha:1/1.0]];
//    
//    JVLayoutConstraint *privacyConstraintX = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
//    JVLayoutConstraint *privacyConstraintW = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationGreaterThanOrEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeWidth multiplier:1 constant:400];
//
//    JVLayoutConstraint *privacyConstraintY = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeBottom multiplier:1 constant:-25];
//    JVLayoutConstraint *privacyConstraintH = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeHeight multiplier:1 constant:14];
//    
//    
//    config.privacyConstraints = @[privacyConstraintX,privacyConstraintW,privacyConstraintY,privacyConstraintH];
//    
//    JVLayoutConstraint *privacyConstraintY1 = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeBottom relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeBottom multiplier:1 constant:-16];
//
//    config.privacyHorizontalConstraints = @[privacyConstraintX,privacyConstraintW,privacyConstraintH,privacyConstraintY1];
//    
//    //loading
//    JVLayoutConstraint *loadingConstraintX = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeCenterX relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeCenterX multiplier:1 constant:0];
//    JVLayoutConstraint *loadingConstraintY = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeCenterY relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemSuper attribute:NSLayoutAttributeCenterY multiplier:1 constant:0];
//    JVLayoutConstraint *loadingConstraintW = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeWidth relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeWidth multiplier:1 constant:kScreenWidth];
//    JVLayoutConstraint *loadingConstraintH = [JVLayoutConstraint constraintWithAttribute:NSLayoutAttributeHeight relatedBy:NSLayoutRelationEqual toItem:JVLayoutItemNone attribute:NSLayoutAttributeHeight multiplier:1 constant:kScreenHeight];
//    config.loadingConstraints = @[loadingConstraintX,loadingConstraintY,loadingConstraintW,loadingConstraintH];
//    [JVERIFICATIONService customUIWithConfig:config customViews:^(UIView *customAreaView) {
//        [customAreaView addSubview:self.topTitleLabel];
//        [customAreaView addSubview:self.phoneBtn];
//        [customAreaView addSubview:self.wxchatLoginBtn];
//        [customAreaView addSubview:self.otherLoginLabel];
//    }];
//}


@end

//
//  CXConfigObject.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/7/7.
//  

#import "CXConfigObject.h"
#import "AppController.h"
#import "CXBaseTabBarViewController.h"
#import "CXUserModel.h"

@implementation CXConfigObject

+ (void)enterOnline {
    NSDictionary *parame = @{
        @"username": [CXClientModel instance].username,
        @"nickname": [CXClientModel instance].nickname,
        @"avatar": [CXClientModel instance].avatar,
    };
    NSString *url = @"//index.php/Api/Member/login";
    [CXHTTPRequest POSTWithURL:url parameters:parame callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"info"]];
            [CXClientModel instance].userId = user.user_id;
            [CXClientModel instance].username = user.username;
            [CXClientModel instance].nickname = user.nickname;
            [CXClientModel instance].avatar = user.avatar;
            [CXClientModel instance].sex = user.sex;
            
            [[CXClientModel instance].easemob login:user.user_id];
            
            [AppController setOrientation:@"V"];
            CXBaseTabBarViewController *tabbarVC = [CXBaseTabBarViewController new];
            tabbarVC.modalPresentationStyle = UIModalPresentationFullScreen;
            [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:tabbarVC animated:YES completion:nil];
        } else {
            [CXTools showAlertWithMessage:responseObject[@"desc"]];
        }
    }];
    
//    NSString *token = [[NSUserDefaults standardUserDefaults] objectForKey:@"userDefaults_token"];
//    if (token.length > 0) {
//        [self loginWithPhone:@""];
//        return;
//    }
//    UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"请输入手机号" message:@"" preferredStyle:UIAlertControllerStyleAlert];
//
//    [alert addAction:[UIAlertAction actionWithTitle:@"确定" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
//        //获取第1个输入框；
//        UITextField *userNameTextField = alert.textFields.firstObject;
//        [self loginWithPhone:userNameTextField.text];
//    }]];
//    //定义第一个输入框；
//    [alert addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
//      textField.placeholder = @"请输入手机号";
//    }];
//    [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:alert animated:YES completion:nil];

}

+ (void)loginWithPhone:(NSString *)phone {
    NSString *token = [[NSUserDefaults standardUserDefaults] objectForKey:@"userDefaults_token"];
    if (token.length <= 0) {
        NSDictionary *parame = @{
            @"username": phone,
            @"vertify": @"111111",
            @"registration_id": @"1111111",
        };
        [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/login" parameters:parame callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"info"]];
                [CXClientModel instance].userId = user.user_id;
                [CXClientModel instance].token = user.token;
                [CXClientModel instance].username = user.username;
                [CXClientModel instance].nickname = user.nickname;
                [CXClientModel instance].avatar = user.avatar;
                
                [[CXClientModel instance].easemob login:user.user_id];
                
                [[NSUserDefaults standardUserDefaults] setValue:user.token forKey:@"userDefaults_token"];
                
                [AppController setOrientation:@"V"];
                CXBaseTabBarViewController *tabbarVC = [CXBaseTabBarViewController new];
                tabbarVC.modalPresentationStyle = UIModalPresentationFullScreen;
                [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:tabbarVC animated:YES completion:nil];
            } else {
                [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"userDefaults_token"];
                [self enterOnline];
            }
        }];
    } else {
        [CXHTTPRequest GETWithURL:@"/index.php/Api/Member/autologin" parameters:@{@"token": token} callback:^(id responseObject, BOOL isCache, NSError *error) {
            if (!error) {
                CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"info"]];
                [CXClientModel instance].userId = user.user_id;
                [CXClientModel instance].token = token;
                [CXClientModel instance].username = user.username;
                [CXClientModel instance].nickname = user.nickname;
                [CXClientModel instance].avatar = user.avatar;
                
                [[NSUserDefaults standardUserDefaults] setValue:token forKey:@"userDefaults_token"];

                [[CXClientModel instance].easemob login:user.user_id];
                
                [AppController setOrientation:@"V"];
                CXBaseTabBarViewController *tabbarVC = [CXBaseTabBarViewController new];
                tabbarVC.modalPresentationStyle = UIModalPresentationFullScreen;
                [[UIApplication sharedApplication].keyWindow.rootViewController presentViewController:tabbarVC animated:YES completion:nil];
            } else {
                [[NSUserDefaults standardUserDefaults] setValue:@"" forKey:@"userDefaults_token"];
                
                [self enterOnline];
            }
        }];
    }
}

@end

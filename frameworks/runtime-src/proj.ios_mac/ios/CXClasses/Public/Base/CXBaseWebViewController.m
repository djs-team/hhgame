//
//  CXBaseWebViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/3/9.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXBaseWebViewController.h"
#import <WebKit/WebKit.h>
//#import <JavaScriptCore/JavaScriptCore.h>

@interface CXBaseWebViewController ()<WKNavigationDelegate,UINavigationControllerDelegate>

@property(nonatomic, strong) WKWebView *webView;

@end

@implementation CXBaseWebViewController

- (instancetype)initWithURL:(NSURL*)pageURL {
    
    if (self = [super init]) {
        
        self.webUrl = pageURL.absoluteString;
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    UIImage *selectImage = [[UIImage imageNamed:@"back_gray"] imageWithRenderingMode:UIImageRenderingModeAlwaysOriginal];
    
    UIBarButtonItem *back = [[UIBarButtonItem alloc] initWithImage:selectImage style:UIBarButtonItemStylePlain target:self action:@selector(back)];
    
    self.navigationItem.leftBarButtonItem = back;
}

- (void)back {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)setWebUrl:(NSString *)webUrl {
    _webUrl = webUrl;
    
    NSURLRequest *request = [NSURLRequest requestWithURL:[NSURL URLWithString:_webUrl]];
    [self.webView loadRequest:request];
}

- (WKWebView *)webView {
    if (!_webView) {
        WKWebViewConfiguration *config = [[WKWebViewConfiguration alloc] init];
        
        UIEdgeInsets safeAreaInsets = UIEdgeInsetsZero;
        if (@available(iOS 11.0, *)) {
            safeAreaInsets = self.view.safeAreaInsets;
        } else {
            safeAreaInsets.top = UIApplication.sharedApplication.statusBarFrame.size.height;
        }
        
        _webView = [[WKWebView alloc] initWithFrame:CGRectZero configuration:config];
        [self.view addSubview:_webView];
        [_webView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.edges.equalTo(self.view).insets(safeAreaInsets);
        }];
    }
    
    return _webView;
}

@end

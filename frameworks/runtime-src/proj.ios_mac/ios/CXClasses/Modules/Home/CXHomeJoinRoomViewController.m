//
//  CXHomeJoinRoomViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/11/18.
//

#import "CXHomeJoinRoomViewController.h"

@interface CXHomeJoinRoomViewController ()
@property (weak, nonatomic) IBOutlet UIButton *btn_1;
@property (weak, nonatomic) IBOutlet UIButton *btn_2;
@property (weak, nonatomic) IBOutlet UIButton *btn_3;
@property (weak, nonatomic) IBOutlet UIButton *applyBtn;

@end

@implementation CXHomeJoinRoomViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    _applyBtn.layer.masksToBounds = YES;
    _applyBtn.layer.cornerRadius = 15;
    
    [self getInitData];
}

- (void)getInitData {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/init_apply" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            NSDictionary *info = responseObject[@"data"];
            if ([info[@"status"] intValue] == 0) {
                [weakSelf.applyBtn setTitle:@"申请中" forState:UIControlStateNormal];
                weakSelf.applyBtn.enabled = NO;
            } else if ([info[@"status"] intValue] == 1) {
                [weakSelf.applyBtn setTitle:@"已通过" forState:UIControlStateNormal];
                weakSelf.applyBtn.enabled = NO;
            } else {
                [weakSelf.applyBtn setTitle:@"申请成为红娘" forState:UIControlStateNormal];
                weakSelf.applyBtn.enabled = YES;
            }
            
            [weakSelf.btn_1 setTitle:info[@"result"][@"one"] forState:UIControlStateNormal];
            [weakSelf.btn_2 setTitle:info[@"result"][@"two"] forState:UIControlStateNormal];
            [weakSelf.btn_3 setTitle:info[@"result"][@"three"] forState:UIControlStateNormal];
        }
    }];
}

- (IBAction)closeAction:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}

- (IBAction)applyAction:(id)sender {
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Languageroom/apply_info" parameters:nil callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [weakSelf toast:@"申请成功"];
            [weakSelf getInitData];
        } else {
            [weakSelf toast:responseObject[@"desc"]];
        }
    }];
}

@end

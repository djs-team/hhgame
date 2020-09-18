//
//  CXMineEditProfileViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXMineEditProfileViewController.h"
#import "CXMineEditProfileItemCell.h"
#import "CXInfoTextViewCell.h"
#import "CXTextFieldAlertView.h"
#import "CXPickerViewAlertView.h"
#import "XYTableViewAlertView.h"
#import "CXTwoPickerViewAlertView.h"
#import "CXProfessionSelectedView.h"

@interface CXMineEditProfileViewController ()<UITableViewDataSource,UITableViewDelegate, UITextViewDelegate>
{
    NSString *_nickNameStr;
    NSString *_ageStr;
    NSString *_cityStr;
    NSString *_educationStr;
    NSString *_marital_statusStr;
    NSString *_statureStr;
    NSString *_payStr;
    NSString *_professionStr;
    NSString *_housing_statusStr;
    NSArray *_charm_partArray;
    NSString *_blood_typeStr;
    NSString *_togetherStr;
    NSString *_live_together_marrgeStr;
    
    //征友条件
    NSString *_friend_cityStr;
    NSString *_friend_ageStr;
    NSString *_friend_statureStr;
    NSString *_friend_educationStr;
    NSString *_friend_payStr;
}

@property (nonatomic,strong) UITableView *tableView;
@property (nonatomic,strong) NSMutableArray *dataSources;

@property (nonatomic, strong) NSDictionary *profileJson;
@property (nonatomic,strong) NSMutableArray *areaArray;

@property (nonatomic, strong) NSMutableArray *ageArrays;
@property (nonatomic, strong) NSMutableArray *statureArrays;
@property (nonatomic, strong) NSMutableArray *provinceArrays;

@property (nonatomic, strong) UITextView *introTextView;

@property (nonatomic, strong) NSMutableArray *ageNameArrays;
@property (nonatomic, strong) NSMutableArray *statureNameArrays;

@property (nonatomic, strong) CXUserModel *currentUser;
@property (nonatomic, strong) CXUserInfoMenusDataModel *menusData;

@end

@implementation CXMineEditProfileViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.view.backgroundColor = [UIColor whiteColor];
    self.navigationItem.title = @"编辑资料";
    [self setUI];
    // Do any additional setup after loading the view.
    
    if (_isFriendCondition) {
        [self requestData];
    } else {
        [self getMenuData];
    }
}

- (void)textViewDidChange:(UITextView *)textView {
    if (textView.text.length > 140) {
        textView.text = [textView.text substringToIndex:140];
        [self toast:@"最多输入140"];
    }
}

- (void)getMenuData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/getMenusList" parameters:@{@"signature": signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            
            weakSelf.menusData = [CXUserInfoMenusDataModel modelWithJSON:responseObject[@"data"]];
            
            self.dataSources = [NSMutableArray array];
            [self.dataSources addObject: @"jyxs"];
            [self.dataSources addObject: self.menusData.jbxx];
            [self.dataSources addObject: self.menusData.grzl];
            
            [self.tableView reloadData];
        }
    }];
}

- (void)requestData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"user_id": [CXClientModel instance].userId,
        @"device": @"iOS",
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/user_info" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            CXUserModel *user = [CXUserModel modelWithJSON:responseObject[@"data"][@"user_info"]];
            weakSelf.currentUser = user;
            [weakSelf reloadFiendConditons];
        }
    }];
}

- (void)saveProfileAction {
    if (_isFriendCondition) {
        [self saveConditionProfile];
    } else {
        [self saveMineProfile];
    }
    
}
// 保存个人信息
- (void)saveMineProfile {
    NSMutableDictionary *profileDict = [NSMutableDictionary dictionary];
    [profileDict setValue:self.introTextView.text forKey:@"intro"];
    
    CXUserInfoMenusDataMenusModel *jbxx_menu = _menusData.jbxx;
    [jbxx_menu.menus enumerateObjectsUsingBlock:^(CXUserInfoMenusModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [profileDict setValue:obj.name forKey:obj.column];
    }];
    CXUserInfoMenusDataMenusModel *grzl_menu = _menusData.grzl;
    [grzl_menu.menus enumerateObjectsUsingBlock:^(CXUserInfoMenusModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [profileDict setValue:obj.name forKey:obj.column];
    }];
    
    NSString *profileStr = [profileDict modelToJSONString];
    
    NSString *signature = [CocoaSecurity md5:[NSString stringWithFormat:@"%@+%@",[CXClientModel instance].token,[CXClientModel instance].userId]].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"user_id": [CXClientModel instance].userId,
        @"profile": profileStr,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/edit" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }
    }];
}

// 保存征婚条件
- (void)saveConditionProfile {
    NSMutableDictionary *profileDict = [NSMutableDictionary dictionary];

    if (_friend_cityStr.length > 0) {
        [profileDict setValue:_friend_cityStr forKey:@"friend_city"];
    }
    if (_friend_ageStr.length > 0) {
        [profileDict setValue:_friend_ageStr forKey:@"friend_age"];
    }
    if (_friend_statureStr.length > 0) {
        [profileDict setValue:_friend_statureStr forKey:@"friend_stature"];
    }
    if (_friend_educationStr.length > 0) {
        [profileDict setValue:_friend_educationStr forKey:@"friend_education"];
    }
    if (_friend_payStr.length > 0) {
        [profileDict setValue:_friend_payStr forKey:@"friend_pay"];
    }

    NSString *profileStr = [profileDict modelToJSONString];
    
    NSString *signature = [CocoaSecurity md5:[NSString stringWithFormat:@"%@+%@",[CXClientModel instance].token,[CXClientModel instance].userId]].hexLower;
    NSDictionary *param = @{
        @"signature": signature,
        @"user_id": [CXClientModel instance].userId,
        @"profile": profileStr,
    };
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Member/edit" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            [weakSelf.navigationController popViewControllerAnimated:YES];
        }
    }];
}

// 刷新征友资料
- (void)reloadFiendConditons {
    self.dataSources = [NSMutableArray array];
    NSMutableArray *array0 = [NSMutableArray array];
    
    if (self.currentUser.friend_city.length > 0) {
        _friend_cityStr = self.currentUser.friend_city;
        [array0 addObject:@{@"key":@"所在地", @"value": _friend_cityStr}];
    } else {
        [array0 addObject:@{@"key":@"所在地", @"value": @"请选择"}];
    }
    
    NSString *ageStr = @"请选择";
    if (self.currentUser.friend_age.length > 0) {
        _friend_ageStr = self.currentUser.friend_age;
        NSArray *array = [self.currentUser.friend_age componentsSeparatedByString:@","];
        ageStr = [NSString stringWithFormat:@"%@-%@",array[0],array[1]];
    }
    [array0 addObject:@{@"key":@"年龄", @"value": ageStr}];
    
    NSString *statureStr = @"请选择";
    if (self.currentUser.friend_stature.length > 0) {
        _friend_statureStr = self.currentUser.friend_stature;
        NSArray *array = [self.currentUser.friend_stature componentsSeparatedByString:@","];
        statureStr = [NSString stringWithFormat:@"%@-%@",array[0],array[1]];
    }
    [array0 addObject:@{@"key":@"身高", @"value": statureStr}];
    
    if (self.currentUser.friend_education.length > 0) {
        _friend_educationStr = self.currentUser.friend_education;
        [array0 addObject:@{@"key":@"最低学历", @"value": _friend_educationStr}];
    } else {
        [array0 addObject:@{@"key":@"最低学历", @"value": @"请选择"}];
    }
    
    if (self.currentUser.friend_pay.length > 0) {
        _friend_payStr = self.currentUser.friend_pay;
        [array0 addObject:@{@"key":@"最低收入", @"value": _friend_payStr}];
    } else {
        [array0 addObject:@{@"key":@"最低收入", @"value": @"请选择"}];
    }
    
    [_dataSources addObject:array0];
    [self.tableView reloadData];
}

#pragma mark - UITableViewDataSource,UITableViewDelegate
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return self.dataSources.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (section == 0) {
        if (self.isFriendCondition == YES) {
            return [self.dataSources[section] count];
        }
        return 1;
    } else {
        CXUserInfoMenusDataMenusModel *menu = _dataSources[section];
        return menu.menus.count;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 0) {
        if (self.isFriendCondition == YES) {
            return 50;
        }
        return 100;
    } else {
        return 50;
    }
}
- (UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    if (indexPath.section == 0) {
        if (self.isFriendCondition == YES) {
            CXMineEditProfileItemCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CELL"];
            if (!cell) {
                cell = [[CXMineEditProfileItemCell alloc]init];
                cell.selectionStyle = UITableViewCellSelectionStyleNone;
            }
            NSDictionary *dict = self.dataSources[indexPath.section][indexPath.row];
            cell.leftLabel.text = dict[@"key"];
            cell.rightLabel.text = dict[@"value"];
            if ([cell.rightLabel.text isEqualToString:@"请选择"]) {
                cell.rightLabel.textColor = UIColorHex(0xFF0D0D);
            } else {
                cell.rightLabel.textColor = UIColorHex(0x333333);
            }
            cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
            return cell;
        } else {
            CXInfoTextViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXInfoTextViewCellID"];
            cell.contentStr = self.menusData.jyxs;
//            cell.inputTextView.delegate = self;
            self.introTextView = cell.inputTextView;
            return cell;
        }
        
    } else {
        CXMineEditProfileItemCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CELL"];
        if (!cell) {
            cell = [[CXMineEditProfileItemCell alloc]init];
            cell.selectionStyle = UITableViewCellSelectionStyleNone;
        }
        CXUserInfoMenusDataMenusModel *menu = _dataSources[indexPath.section];
        CXUserInfoMenusModel *model = menu.menus[indexPath.row];
        cell.leftLabel.text = model.type_name;
        cell.rightLabel.text = model.name.length > 0 ? model.name : @"请选择";
        if ([cell.rightLabel.text isEqualToString:@"请选择"]) {
            cell.rightLabel.textColor = UIColorHex(0xFF0D0D);
        } else {
            cell.rightLabel.textColor = UIColorHex(0x333333);
        }
        cell.accessoryType = UITableViewCellAccessoryDisclosureIndicator;
        return cell;
    }
    
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 50;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH, 50)];
    headerView.backgroundColor = UIColorHex(0xFFFFFF);
    UILabel *titleLabel = [[UILabel alloc] initWithFrame:CGRectMake(45, 0, SCREEN_WIDTH, 50)];
    CXUserInfoMenusDataMenusModel *menu = _dataSources[section];
    titleLabel.text = section == 0 ? (_isFriendCondition == YES ? @"征友条件" : @"交友心声") : menu.name;
    titleLabel.textColor = UIColorHex(0x333333);
    titleLabel.font = [UIFont systemFontOfSize:19 weight:UIFontWeightBold];
    [headerView addSubview:titleLabel];
    UIImageView *logo = [[UIImageView alloc] initWithFrame:CGRectMake(15, 15, 20, 20)];
    if (section == 0) {
        logo.image = [UIImage imageNamed:@"mine_editprofile_one"];
    } else {
        logo.image = [UIImage imageNamed:@"mine_editprofile_two"];
    }
    [headerView addSubview:logo];
    return headerView;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    [tableView deselectRowAtIndexPath:indexPath animated:YES];
    
    MJWeakSelf
    CXMineEditProfileItemCell *cell = [tableView cellForRowAtIndexPath:indexPath];
    
    if (_isFriendCondition) {
        if (indexPath.row == 0) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改所在地";
            alertView.dataSources = self.provinceArrays;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                self->_friend_cityStr = content;
                if (content.length > 0) {
                    cell.rightLabel.text = content;
                }
            };
            [self lew_presentPopupView:alertView animation:nil];

        } else if (indexPath.row == 1) {
            
            CXTwoPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXTwoPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改年龄";
            alertView.dataSources = self.ageNameArrays;
            alertView.sureActionBlock = ^(NSString * _Nonnull content1, NSString * _Nonnull content2) {
                self->_friend_ageStr = [NSString stringWithFormat:@"%@,%@", content1,content2];
                if (content2.length > 0) {
                    cell.rightLabel.text = [NSString stringWithFormat:@"%@-%@", content1,content2];;
                }
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if (indexPath.row == 2) {
            
            CXTwoPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXTwoPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改身高";
            alertView.dataSources = self.statureNameArrays;
            alertView.sureActionBlock = ^(NSString * _Nonnull content1, NSString * _Nonnull content2) {
                self->_friend_statureStr = [NSString stringWithFormat:@"%@,%@", content1,content2];
                if (content2.length > 0) {
                    cell.rightLabel.text = [NSString stringWithFormat:@"%@-%@", content1,content2];;
                }
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if (indexPath.row == 3) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改学历";
            alertView.dataSources = [self itemValueWithKey:@"education"];
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                self->_friend_educationStr = content;
                if (content.length > 0) {
                    cell.rightLabel.text = content;
                }
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if (indexPath.row == 4) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改月收入";
            alertView.dataSources = [self itemValueWithKey:@"pay"];
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                self->_friend_payStr = content;
                if (content.length > 0) {
                    cell.rightLabel.text = content;
                }
            };
            [self lew_presentPopupView:alertView animation:nil];
        }
    } else {
        CXUserInfoMenusDataMenusModel *menu = _dataSources[indexPath.section];
        CXUserInfoMenusModel *model = menu.menus[indexPath.row];
        if ([model.column isEqualToString:@"nickname"]) {
            //昵称
            CXTextFieldAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXTextFieldAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"birthday"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改年龄";
            alertView.dataSources = self.ageArrays;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"city"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改城市";
            alertView.dataSources = self.provinceArrays;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];

        } else if ([model.column isEqualToString:@"education"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改学历";
            alertView.dataSources = model.list;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"marital_status"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改婚姻状况";
            alertView.dataSources = model.list;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"stature"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改身高";
            alertView.dataSources = self.statureArrays;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"pay"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改月收入";
            alertView.dataSources = model.list;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"profession"]) {
            CXProfessionSelectedView *v = [[CXProfessionSelectedView alloc]init];
            v.dataDict = model.option;
            v.selectFirstIndex = model.option.allKeys.firstObject;
            v.selectSecondIndex = model.option.allValues.firstObject[0];
            v.parentVC = self;
            v.sureActionBlock = ^(NSString * _Nonnull content1, NSString * _Nonnull content2) {
               model.name = content2;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:v animation:nil];
        } else if ([model.column isEqualToString:@"housing_status"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改住房情况";
            alertView.dataSources = model.list;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"charm_part"]) {
            XYTableViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"XYTableViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.dataSources = model.list;
            alertView.sureActionBlock = ^(NSArray * _Nonnull contentArrays) {
                model.name = [contentArrays componentsJoinedByString:@","];
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };

            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"blood_type"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改血型";
            alertView.dataSources = model.list;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"together"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改婚后与父母同居";
            alertView.dataSources = model.list;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        } else if ([model.column isEqualToString:@"live_together_marrge"]) {
            CXPickerViewAlertView *alertView = [[[NSBundle mainBundle] loadNibNamed:@"CXPickerViewAlertView" owner:self options:nil] lastObject];
            alertView.parentVC = self;
            alertView.title = @"修改婚前同居";
            alertView.dataSources = model.list;
            alertView.contenStr = cell.rightLabel.text;
            alertView.sureActionBlock = ^(NSString * _Nonnull content) {
                model.name = content;
                [weakSelf.tableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
            };
            [self lew_presentPopupView:alertView animation:nil];
        }
    }
}

- (void)ageSelectSucess{
    [self.tableView reloadData];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    [self.view endEditing:YES];
}

#pragma mark - Private
- (NSInteger)ageWithDateOfBirth:(NSDate *)date {
    // 出生日期转换 年月日
    NSDateComponents *components1 = [[NSCalendar currentCalendar] components:NSCalendarUnitDay | NSCalendarUnitMonth | NSCalendarUnitYear fromDate:date];
    NSInteger brithDateYear  = [components1 year];
    NSInteger brithDateDay   = [components1 day];
    NSInteger brithDateMonth = [components1 month];
    
    // 获取系统当前 年月日
    NSDateComponents *components2 = [[NSCalendar currentCalendar] components:NSCalendarUnitDay | NSCalendarUnitMonth | NSCalendarUnitYear fromDate:[NSDate date]];
    NSInteger currentDateYear  = [components2 year];
    NSInteger currentDateDay   = [components2 day];
    NSInteger currentDateMonth = [components2 month];
    
    // 计算年龄
    NSInteger iAge = currentDateYear - brithDateYear - 1;
    if ((currentDateMonth > brithDateMonth) || (currentDateMonth == brithDateMonth && currentDateDay >= brithDateDay)) {
        iAge++;
    }
    
    return iAge;
}

- (NSArray *)itemValueWithKey:(NSString *)key {
    NSMutableArray *valueArray = [NSMutableArray array];
    NSDictionary *itemDict = [self.profileJson objectForKey:key];
    NSArray *keys = [itemDict allKeys];
    for (NSInteger i = 1; i <= keys.count; i++) {
        NSString *key = [NSString stringWithFormat:@"%ld",(long)i];
        NSString *value = [itemDict objectForKey:key];
        [valueArray addObject:value];
    }

    return valueArray;
}

- (NSString *)itemKeyWithValue:(NSString *)value withType:(NSString *)type {
    NSDictionary *itemDict = [self.profileJson objectForKey:type];
    NSArray *key = [itemDict allKeysForObject:value];
    return key[0];
}

- (NSString *)itemValueWithKey:(NSString *)key withType:(NSString *)type {
    NSDictionary *itemDict = [self.profileJson objectForKey:type];
    NSString *value = [itemDict objectForKey:key];
    return value;
}

#pragma mark - SubViews
- (void)setUI {
    [self.view addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.view.mas_left).offset(0);
        make.right.equalTo(self.view.mas_right).offset(0);
        make.top.equalTo(self.view.mas_top).offset(0);
        make.bottom.equalTo(self.view.mas_bottom).offset(0);
    }];
    
    _tableView.estimatedRowHeight = 0;
    
    _tableView.estimatedSectionHeaderHeight = 0;
    
    _tableView.estimatedSectionFooterHeight = 0;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"CXInfoTextViewCell" bundle:nil] forCellReuseIdentifier:@"CXInfoTextViewCellID"];
    
    UIButton *rightBut = [[UIButton alloc] initWithFrame:CGRectMake(0, 0, 44, 44)];
    [rightBut setTitle:@"保存" forState:UIControlStateNormal];
    [rightBut setTitleColor:UIColorHex(0x818181) forState:UIControlStateNormal];
    rightBut.titleLabel.font = [UIFont boldSystemFontOfSize:16];
    [rightBut addTarget:self action:@selector(saveProfileAction) forControlEvents:UIControlEventTouchUpInside];
    UIBarButtonItem *rightItem = [[UIBarButtonItem alloc] initWithCustomView:rightBut];
    self.navigationItem.rightBarButtonItem = rightItem;
    
    
    UIBarButtonItem *backItem = [[UIBarButtonItem alloc] initWithImage:[UIImage imageNamed:@"back_gray"] style:UIBarButtonItemStylePlain target:self action:@selector(backAction)];
    backItem.tintColor = UIColorHex(0x333333);
    self.navigationItem.leftBarButtonItem = backItem;
}

- (void)backAction {
    kWeakSelf
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:nil message:@"是否保存当前信息" preferredStyle:UIAlertControllerStyleAlert];
    [alert addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf.navigationController popViewControllerAnimated:YES];
    }]];
    [alert addAction:[UIAlertAction actionWithTitle:@"保存" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        [weakSelf saveProfileAction];
    }]];
    
    [self presentViewController:alert animated:YES completion:nil];
}
#pragma mark - Setter
- (UITableView*)tableView{
    if (!_tableView) {
        _tableView = [[UITableView alloc]init];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        if (self.isFriendCondition) {
            _tableView.scrollEnabled = NO;
        }
    }
    return _tableView;
}

- (NSMutableArray *)ageArrays {
    if (!_ageArrays) {
        _ageArrays = [NSMutableArray array];
        int age = 18;
        do {
            [_ageArrays addObject:[NSString stringWithFormat:@"%d", age]];
            age++;
        } while (age <= 60);
        
    }
    
    return _ageArrays;
}

- (NSMutableArray *)statureArrays {
    if (!_statureArrays) {
        _statureArrays = [NSMutableArray array];
        int stature = 150;
        do {
            [_statureArrays addObject:[NSString stringWithFormat:@"%d", stature]];
            stature++;
        } while (stature <= 200);
        
    }
    
    return _statureArrays;
}


- (NSMutableArray *)ageNameArrays {
    if (!_ageNameArrays) {
        _ageNameArrays = [NSMutableArray array];
        int age = 18;
        do {
            [_ageNameArrays addObject:[NSString stringWithFormat:@"%d", age]];
            age++;
        } while (age <= 60);
        
    }
    
    return _ageNameArrays;
}
- (NSMutableArray *)statureNameArrays {
    if (!_statureNameArrays) {
        _statureNameArrays = [NSMutableArray array];
        int stature = 150;
        do {
            [_statureNameArrays addObject:[NSString stringWithFormat:@"%d", stature]];
            stature++;
        } while (stature <= 200);
        
    }
    
    return _statureNameArrays;
}
#pragma mark - Setter-Json
- (NSDictionary *)profileJson {
    if (!_profileJson) {
        NSString *path = [[NSBundle mainBundle] pathForResource:@"userProfile.json" ofType:nil];
        NSData *data = [NSData dataWithContentsOfFile:path];
        NSDictionary *dict = [NSDictionary dictionary];
        dict = [NSJSONSerialization JSONObjectWithData:data
                                               options:kNilOptions error:nil];
        _profileJson = [NSDictionary dictionaryWithDictionary:dict];
    }

    return _profileJson;
}

- (NSMutableArray *)provinceArrays {
    if (!_provinceArrays) {
        NSString *path = [[NSBundle mainBundle] pathForResource:@"city.json" ofType:nil];
        NSData *data = [NSData dataWithContentsOfFile:path];
        NSArray *arr = [NSJSONSerialization JSONObjectWithData:data
                                                       options:NSJSONReadingMutableContainers error:nil];
        
        NSMutableArray *arrays = [NSMutableArray array];
        [arr enumerateObjectsUsingBlock:^(NSDictionary * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [arrays addObject:obj[@"name"]];
        }];
        
        _provinceArrays = [NSMutableArray arrayWithArray:arrays];
    }
    
    return _provinceArrays;
}



@end


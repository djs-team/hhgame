//
//  CXHomeFilterView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import "CXHomeFilterView.h"

@interface CXHomeFilterView() <UIPickerViewDataSource, UIPickerViewDelegate>

@property (nonatomic, assign) NSInteger selectedType;

@property (retain, nonatomic) IBOutlet UITextField *ageTextField;
@property (retain, nonatomic) IBOutlet UITextField *provinceTextField;
@property (retain, nonatomic) IBOutlet UITextField *cityTextField;
@property (retain, nonatomic) IBOutlet UITextField *districtTextField;
@property (retain, nonatomic) IBOutlet UIButton *searchBtn;

@property (retain, nonatomic) IBOutlet UIView *selectedBGView;
@property (retain, nonatomic) IBOutlet NSLayoutConstraint *selectedBGViewTopLayout;
@property (retain, nonatomic) IBOutlet UILabel *selectedTitle;
@property (retain, nonatomic) IBOutlet UIPickerView *mainPickerView;

@property (nonatomic, strong) NSMutableArray *ageNameArrays;
@property (nonatomic, strong) NSMutableArray *dataSources;
@property (nonatomic, copy) NSString *firstStr;
@property (nonatomic, copy) NSString *secondStr;
@property (nonatomic, strong) NSNumber *provinceCode;
@property (nonatomic, strong) NSString *provinceName;
@property (nonatomic, strong) NSString *cityName;
@property (nonatomic, strong) NSNumber *cityCode;
@property (nonatomic, strong) NSString *districtName;
@property (nonatomic, strong) NSNumber *districtCode;

@end

@implementation CXHomeFilterView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 450));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeSheet;
    
    _mainPickerView.dataSource = self;
    _mainPickerView.delegate = self;
    
    _firstStr = @"18";
    _secondStr = @"60";
    
    [self setupSubViews];
}

- (void)getAreaDataLevel:(NSNumber *)level pcode:(NSNumber *)pcode {
    NSDictionary *param = @{
        @"level":level,
        @"pcode":pcode,
    };
    kWeakSelf
    [CXHTTPRequest GETWithURL:@"/index.php/Api/Member/getArea" parameters:param callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            weakSelf.dataSources = [NSMutableArray arrayWithArray:responseObject[@"data"]];
            NSDictionary *dict = weakSelf.dataSources.firstObject;
            if (weakSelf.selectedType == 2) {
                weakSelf.provinceCode = dict[@"code"];
                weakSelf.provinceName = dict[@"name"];
            } else if (weakSelf.selectedType == 3) {
                weakSelf.cityCode = dict[@"code"];
                weakSelf.cityName = dict[@"name"];
            } else {
                weakSelf.districtCode = dict[@"code"];
                weakSelf.districtName = dict[@"name"];
            }
            [weakSelf.mainPickerView reloadAllComponents];
        }
    }];

}

#pragma mark - <UIPickerViewDataSource, UIPickerViewDelegate>
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    if (_selectedType == 1) {
        return 2;
    } else {
        return 1;
    }
}
//行中有几列
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return self.dataSources.count;
}
//列显示的数据
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger) row forComponent:(NSInteger)component {
    if (_selectedType == 1) {
        return self.dataSources[row];
    } else {
        NSDictionary *dict = self.dataSources[row];
        return dict[@"name"];
    }
}

- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view {
    UILabel* pickerLabel = (UILabel*)view;
    if (!pickerLabel){
        pickerLabel = [[UILabel alloc] init];
        pickerLabel.adjustsFontSizeToFitWidth = YES;
        pickerLabel.textAlignment = NSTextAlignmentCenter;
//        [pickerLabel setBackgroundColor:COLOR_HEX(@"FFF7DA")];
        [pickerLabel setFont:[UIFont systemFontOfSize:20]];
        pickerLabel.textColor = UIColorHex(0x7D3EF1);
    }
    // Fill the label text here
    pickerLabel.text = [self pickerView:pickerView titleForRow:row forComponent:component];
    return pickerLabel;
}

#pragma mark - delegate
// 选中某一组的某一行
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if (_selectedType == 1) {
        NSInteger selectProIndex = [pickerView selectedRowInComponent:0];
        if (component == 0) {
            [pickerView reloadComponent:1];
            
            [pickerView selectRow:selectProIndex inComponent:1 animated:YES];
        }
        NSInteger selectCityIndex = [pickerView selectedRowInComponent:1];
        
        _firstStr = self.dataSources[selectProIndex];
        _secondStr = self.dataSources[selectCityIndex];
    } else {
        NSDictionary *dict = self.dataSources[row];
        if (_selectedType == 2) {
            _provinceCode = dict[@"code"];
            _provinceName = dict[@"name"];
        } else if (_selectedType == 3) {
            _cityCode = dict[@"code"];
            _cityName = dict[@"name"];
        } else {
            _districtCode = dict[@"code"];
            _districtName = dict[@"name"];
        }
    }
}

#pragma mark - Action
- (IBAction)ageAction:(id)sender {
    _selectedType = 1;
    _selectedBGView.hidden = NO;
    _selectedBGViewTopLayout.constant = 70+20+2;
    _selectedTitle.text = @"请选择年龄";
    [self.dataSources removeAllObjects];
    self.dataSources = [NSMutableArray arrayWithArray:self.ageNameArrays];
    [self.mainPickerView reloadAllComponents];
}
- (IBAction)provinceAction:(id)sender {
    _selectedType = 2;
    [self.dataSources removeAllObjects];
    _selectedBGView.hidden = NO;
    _selectedBGViewTopLayout.constant = 70+20*2+50+2;
    _selectedTitle.text = @"请选择省份";
    _cityTextField.text = @"";
    _districtTextField.text = @"";
    _cityCode = @0;
    _districtCode = @0;
    
    [self getAreaDataLevel:@0 pcode:@0];
}
- (IBAction)cityAction:(id)sender {
    _selectedType = 3;

    if (_provinceCode.integerValue > 0) {
        [self.dataSources removeAllObjects];
        _selectedBGView.hidden = NO;
        _selectedBGViewTopLayout.constant = 70+20*3+20+50+2;
        _selectedTitle.text = @"请选择城市";
        _districtTextField.text = @"";
        _districtCode = @0;
        
        
        [self getAreaDataLevel:@1 pcode:_provinceCode];
    }
    
}
- (IBAction)districtAction:(id)sender {
    _selectedType = 4;
    if (_cityCode.integerValue > 0) {
        [self.dataSources removeAllObjects];
        _selectedBGView.hidden = NO;
        _selectedBGViewTopLayout.constant = 70+20*4+20*2+50+2;
        _selectedTitle.text = @"请选择县城";
        [self getAreaDataLevel:@2 pcode:_cityCode];
    }
}

- (IBAction)sureAction:(id)sender {
    _selectedBGView.hidden = YES;
    switch (_selectedType) {
        case 1:
            _ageTextField.text = [NSString stringWithFormat:@"%@-%@", _firstStr, _secondStr];
            break;
        case 2:
            _provinceTextField.text = _provinceName.length > 0 ? _provinceName: @"";
            break;
        case 3:
            _cityTextField.text = _cityName.length > 0 ? _cityName: @"";
            break;
        case 4:
            _districtTextField.text = _districtName.length > 0 ? _districtName: @"";
            break;
        default:
            break;
    }
}

- (IBAction)searchAction:(id)sender {
    if (self.filterBlock) {
        self.filterBlock(_ageTextField.text.length > 0 ? _ageTextField.text : @"", _provinceTextField.text.length > 0 ? _provinceTextField.text: @"", _cityTextField.text.length > 0 ? _cityTextField.text: @"", _districtTextField.text.length > 0 ? _districtTextField.text: @"");
    }
    [self hide];
}

- (NSMutableArray *)ageNameArrays {
//    if (!_ageNameArrays) {
        _ageNameArrays = [NSMutableArray array];
        int age = 18;
        do {
            [_ageNameArrays addObject:[NSString stringWithFormat:@"%d", age]];
            age++;
        } while (age <= 60);
        
//    }
    
    return _ageNameArrays;
}

- (void)setupSubViews {
    [_searchBtn setBackgroundImage:[UIImage gradientImageWithSize:CGSizeMake(SCREEN_WIDTH - 50*2, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)] forState:UIControlStateNormal];
    _searchBtn.layer.masksToBounds = YES;
    _searchBtn.layer.cornerRadius = 19;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    UIView *leftView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 20)];
    _ageTextField.leftView = leftView;
    _ageTextField.leftViewMode = UITextFieldViewModeAlways;
    _ageTextField.layer.masksToBounds = YES;
    _ageTextField.layer.cornerRadius = 4;
    
    UIView *leftView1 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 20)];
    _provinceTextField.leftView = leftView1;
    _provinceTextField.leftViewMode = UITextFieldViewModeAlways;
    _provinceTextField.layer.masksToBounds = YES;
    _provinceTextField.layer.cornerRadius = 4;

    UIView *leftView2 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 20)];
    _cityTextField.leftView = leftView2;
    _cityTextField.leftViewMode = UITextFieldViewModeAlways;
    _cityTextField.layer.masksToBounds = YES;
    _cityTextField.layer.cornerRadius = 4;

    UIView *leftView3 = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 15, 20)];
    _districtTextField.leftView = leftView3;
    _districtTextField.leftViewMode = UITextFieldViewModeAlways;
    _districtTextField.layer.masksToBounds = YES;
    _districtTextField.layer.cornerRadius = 4;

    _selectedBGView.hidden = YES;
    _selectedBGView.layer.masksToBounds = YES;
    _selectedBGView.layer.cornerRadius = 5;
    _selectedBGView.layer.borderColor = UIColorHex(0xA9A9A9).CGColor;
    _selectedBGView.layer.borderWidth = 0.5;

    _selectedType = 0;
    self.dataSources = [NSMutableArray array];
}

@end

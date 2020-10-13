//
//  CXCityPickerView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/10/13.
//

#import "CXCityPickerView.h"

@interface CXCityPickerView() <UIPickerViewDataSource, UIPickerViewDelegate>
@property (weak, nonatomic) IBOutlet UIPickerView *mainPickerView;
@property (weak, nonatomic) IBOutlet UIButton *sureButton;

@property (strong, nonatomic) NSDictionary *pickerDic;
@property (strong, nonatomic) NSArray *selectedArray;
@property (strong, nonatomic) NSArray *provinceArray;
@property (strong, nonatomic) NSArray *cityArray;

@end

@implementation CXCityPickerView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(kScreenWidth - 40, 450));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeAlert;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 8;
    
    self.sureButton.layer.masksToBounds = YES;
    self.sureButton.layer.cornerRadius = 25;
    
    _mainPickerView.dataSource = self;
    _mainPickerView.delegate = self;
    
    [self getAddressInformation];
}

- (void)getAddressInformation {
    NSString *path = [[NSBundle mainBundle] pathForResource:@"Address" ofType:@"plist"];
    self.pickerDic = [[NSDictionary alloc] initWithContentsOfFile:path];
    self.provinceArray = [self.pickerDic allKeys];
    self.selectedArray = [self.pickerDic objectForKey:[[self.pickerDic allKeys] objectAtIndex:0]];
    if (self.selectedArray.count > 0) {
        self.cityArray = [[self.selectedArray objectAtIndex:0] allKeys];
    }
    
    [self.mainPickerView reloadAllComponents];
}

#pragma mark - <UIPickerViewDataSource, UIPickerViewDelegate>

- (NSInteger)numberOfComponentsInPickerView:(nonnull UIPickerView *)pickerView {
    return 2;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (component == 0) {
        return self.provinceArray.count;
    } else {
        return self.cityArray.count;
    }
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    if (component == 0) {
        return [self.provinceArray objectAtIndex:row];
    } else {
        return [self.cityArray objectAtIndex:row];
    }
}

- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view {
    UILabel* pickerLabel = (UILabel*)view;
    if (!pickerLabel){
        pickerLabel = [[UILabel alloc] init];
        pickerLabel.adjustsFontSizeToFitWidth = YES;
        pickerLabel.textAlignment = NSTextAlignmentCenter;
        [pickerLabel setBackgroundColor:UIColorHex(0xFFF7DA)];
        [pickerLabel setFont:[UIFont systemFontOfSize:21]];
    }
    // Fill the label text here
    pickerLabel.text = [self pickerView:pickerView titleForRow:row forComponent:component];
    return pickerLabel;
}

- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if (component == 0) {
        self.selectedArray = [self.pickerDic objectForKey:[self.provinceArray objectAtIndex:row]];
        if (self.selectedArray.count > 0) {
            self.cityArray = [[self.selectedArray objectAtIndex:0] allKeys];
        } else {
            self.cityArray = @[];
        }

        [pickerView reloadComponent:1];
        [pickerView selectedRowInComponent:1];
    }
    if (component == 1) {
        self.selectedArray = [self.pickerDic objectForKey:[self.provinceArray objectAtIndex:[self.mainPickerView selectedRowInComponent:0]]];
    }
}

- (IBAction)sureAction:(id)sender {
    NSString *province = [self.provinceArray objectAtIndex:[self.mainPickerView selectedRowInComponent:0]];
    NSString *city  = [self.cityArray objectAtIndex:[self.mainPickerView selectedRowInComponent:1]];
    
    NSString *cityStr = [NSString stringWithFormat:@"%@,%@", province, city];
    if (self.cityPickerSureBlock) {
        self.cityPickerSureBlock(cityStr);
    }
    
    [self hide];
}

@end

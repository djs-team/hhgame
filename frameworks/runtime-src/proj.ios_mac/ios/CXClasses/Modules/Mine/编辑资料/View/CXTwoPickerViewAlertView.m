//
//  CXTowPickerViewAlertView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/25.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXTwoPickerViewAlertView.h"

@interface CXTwoPickerViewAlertView() <UIPickerViewDataSource, UIPickerViewDelegate>

@property (nonatomic, assign) NSInteger firstSelectedIndex;
@property (nonatomic, copy) NSString *firstStr;
@property (nonatomic, copy) NSString *secondStr;

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (weak, nonatomic) IBOutlet UIPickerView *mainPickerView;
@property (weak, nonatomic) IBOutlet UIButton *sureButton;

@property (nonatomic, strong) NSMutableArray *secondArrays;

@end

@implementation CXTwoPickerViewAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    _contentView.layer.cornerRadius = 8;
    _sureButton.layer.cornerRadius = 25;
    
    _mainPickerView.dataSource = self;
    _mainPickerView.delegate = self;
    
    _firstSelectedIndex = 0;
}

- (void)setTitle:(NSString *)title {
    _title = title;
    self.titleLabel.text = title;
}

- (void)setDataSources:(NSArray *)dataSources {
    _dataSources = dataSources;
    _firstStr = self.dataSources[0];
    _secondStr = self.dataSources[5];
    [self.mainPickerView reloadAllComponents];
}

#pragma mark - <UIPickerViewDataSource, UIPickerViewDelegate>
//有几行
- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 2;
}
//行中有几列
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (component == 0) {
        return self.dataSources.count;
    } else {
        _firstSelectedIndex = [pickerView selectedRowInComponent:0];
        return self.secondArrays.count;
    }
}
//列显示的数据
- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger) row forComponent:(NSInteger)component {
    if (component == 0) {
        return self.dataSources[row];
    } else {
        return self.secondArrays[row];
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

#pragma mark - delegate
// 选中某一组的某一行
- (void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    if (component == 0) {
        
        [pickerView reloadComponent:1];
        
        [pickerView selectRow:0 inComponent:1 animated:YES];
    }
    
    NSInteger selectProIndex =  [pickerView selectedRowInComponent:0];
    _firstSelectedIndex = selectProIndex;
    NSInteger selectCityIndex = [pickerView selectedRowInComponent:1];
    
    _firstStr = self.dataSources[selectProIndex];
    
    _secondStr = self.secondArrays[selectCityIndex];
}

- (NSMutableArray *)secondArrays {
    NSMutableArray *tempArray = [NSMutableArray arrayWithArray:self.dataSources];
     NSArray *secArray = [tempArray subarrayWithRange:NSMakeRange(self.firstSelectedIndex, tempArray.count - self.firstSelectedIndex)];
    _secondArrays = [NSMutableArray arrayWithArray:secArray];
    return _secondArrays;
    
}

- (IBAction)cancelAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
}

- (IBAction)sureAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
    
    if (self.sureActionBlock) {
        self.sureActionBlock(_firstStr, _secondStr);
    }
}



@end

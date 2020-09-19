//
//  CXTableViewAlertView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXPickerViewAlertView.h"

@interface CXPickerViewAlertView() <UIPickerViewDataSource, UIPickerViewDelegate>

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (weak, nonatomic) IBOutlet UIPickerView *mainPickerView;
@property (weak, nonatomic) IBOutlet UIButton *sureButton;

@end

@implementation CXPickerViewAlertView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    _contentView.layer.cornerRadius = 8;
    _sureButton.layer.cornerRadius = 25;
    
    _mainPickerView.dataSource = self;
    _mainPickerView.delegate = self;
}

- (void)setContenStr:(NSString *)contenStr {
    _contenStr = contenStr;
    
    if ([_dataSources containsObject:_contenStr]) {
        NSInteger index = [_dataSources indexOfObject:_contenStr];
        
        [self.mainPickerView selectRow:index inComponent:0 animated:YES];
    } else {
        _contenStr = _dataSources[0];
    }
}

- (void)setTitle:(NSString *)title {
    _title = title;
    self.titleLabel.text = title;
}

- (void)setDataSources:(NSArray *)dataSources {
    _dataSources = dataSources;
    
    [self.mainPickerView reloadAllComponents];
}

#pragma mark - <UIPickerViewDataSource, UIPickerViewDelegate>

- (NSInteger)numberOfComponentsInPickerView:(nonnull UIPickerView *)pickerView {
    return 1;
}

- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return _dataSources.count;
}

- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component {
    return _dataSources[row];
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
    _contenStr = _dataSources[row];
}

- (IBAction)cancelAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
}

- (IBAction)sureAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
    
    if (self.sureActionBlock) {
        self.sureActionBlock(_contenStr);
    }
}

@end

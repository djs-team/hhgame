//
//  CXProfessionSelectedView.m
//  hairBall
//
//  Created by mahong yang on 2019/12/12.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXProfessionSelectedView.h"

@interface CXProfessionSelectedView()<UIPickerViewDelegate,UIPickerViewDataSource,UIGestureRecognizerDelegate>

@property (strong, nonatomic) UIPickerView *pickerView;
@property (strong,nonatomic)UIView *backView;
@property (strong,nonatomic)UIButton *cancleBtn;
@property (strong,nonatomic)UIButton *sureBtn;
@property (strong,nonatomic)NSString *currntProvince;
@property (strong,nonatomic)NSString *currntCity;

@end
@implementation CXProfessionSelectedView

- (instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        self.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        self.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.5];
        [self setUI];
    }
    return self;
}
-(NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 2;
}

-(NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    if (component == 0) {
        return self.dataDict.allKeys.count;
    } else {
        NSArray *array = [self.dataDict objectForKey:self.selectFirstIndex];
        return array.count;
    }
}
- (CGFloat)pickerView:(UIPickerView *)pickerView rowHeightForComponent:(NSInteger)component{
    return 40;
}
- (CGFloat) pickerView:(UIPickerView *)pickerView widthForComponent:(NSInteger)component{
    if (component==0) {//iOS6边框占10+10
        return  SCREEN_WIDTH/2;
    }
    return  SCREEN_WIDTH/2;
    
}
- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view {
    if (!view){
        view = [[UIView alloc]init];
    }
    UILabel *text = [[UILabel alloc]initWithFrame:CGRectMake(0, 0, SCREEN_WIDTH/2, 40)];
    text.font = [UIFont systemFontOfSize:16.0f];
    text.textAlignment = NSTextAlignmentCenter;
    if (component==0) {
        text.text = [self.dataDict.allKeys objectAtIndex:row];
    } else {
        NSArray *array = [self.dataDict objectForKey:self.selectFirstIndex];
        text.text = array[row];
    }
    
    [view addSubview:text];
    
    //隐藏上下直线
    
    [self.pickerView.subviews objectAtIndex:1].backgroundColor = [UIColor whiteColor];
    
    [self.pickerView.subviews objectAtIndex:2].backgroundColor = [UIColor whiteColor];
    
    return view;
    
}
//- (NSString *)pickerView:(UIPickerView *)pickerView titleForRow:(NSInteger)row forComponent:(NSInteger)component{
//
//    NSString *str = [self.dataArray objectAtIndex:row];
//
//    return str;
//
//}
//被选择的行

-(void)pickerView:(UIPickerView *)pickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component{
    if (component==0) {
        self.selectFirstIndex = [self.dataDict.allKeys objectAtIndex:row];
        [self.pickerView reloadComponent:1];
        [pickerView selectedRowInComponent:1];
    }
}
-(void)sureClick{
    _selectFirstIndex = [self.dataDict.allKeys objectAtIndex:[self.pickerView selectedRowInComponent:0]];
    NSArray *array = [self.dataDict objectForKey:self.selectFirstIndex];
    _selectSecondIndex  = [array objectAtIndex:[self.pickerView selectedRowInComponent:1]];
    if (self.sureActionBlock) {
        self.sureActionBlock(_selectFirstIndex, _selectSecondIndex);
    }
    
    [self.parentVC lew_dismissPopupView];
}
-(void)cancleClick{
    [self.parentVC lew_dismissPopupView];
}
#pragma mark -UIGestureRecognizerDelegate
-(BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldReceiveTouch:(UITouch *)touch{
    if ([touch.view isDescendantOfView:self.backView]) {
        return NO;
    }
    return YES;
}
-(UIView*)backView{
    if (!_backView) {
        _backView = [[UIView alloc]init];
        _backView.backgroundColor = [UIColor whiteColor];
    }
    return _backView;
}
-(UIButton*)cancleBtn{
    if (!_cancleBtn) {
        _cancleBtn = [[UIButton alloc]init];
        _cancleBtn.titleLabel.font = [UIFont systemFontOfSize:16.0f];
        [_cancleBtn setTitle:@"取消" forState:UIControlStateNormal];
        [_cancleBtn setTitleColor:[UIColor colorWithHexString:@"#FF8B8B"] forState:UIControlStateNormal];
        [_cancleBtn setTitleColor:UIColorHex(0x333333) forState:UIControlStateHighlighted];
        [_cancleBtn addTarget:self action:@selector(cancleClick) forControlEvents:UIControlEventTouchUpInside];
    }
    return _cancleBtn;
}
-(UIButton*)sureBtn{
    if (!_sureBtn) {
        _sureBtn = [[UIButton alloc]init];
        _sureBtn.titleLabel.font = [UIFont systemFontOfSize:16.0f];
        [_sureBtn setTitle:@"确定" forState:UIControlStateNormal];
        [_sureBtn addTarget:self action:@selector(sureClick) forControlEvents:UIControlEventTouchUpInside];
        
        [_sureBtn setTitleColor:[UIColor colorWithHexString:@"#FF8B8B"] forState:UIControlStateNormal];
        [_sureBtn setTitleColor:UIColorHex(0x333333) forState:UIControlStateHighlighted];
    }
    return _sureBtn;
}

-(void)setUI{
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(cancleClick)];
    tap.delegate = self;
    [self addGestureRecognizer:tap];
    
    [self addSubview:self.backView];
    [self.backView addSubview:self.cancleBtn];
    [self.backView addSubview:self.sureBtn];
    [self.backView addSubview:self.pickerView];
    [self.cancleBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(0);
        make.size.mas_equalTo(CGSizeMake(60, 40));
        make.top.equalTo(self.backView.mas_top).offset(0);
    }];
    [self.sureBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.equalTo(self.mas_right).offset(0);
        make.size.mas_equalTo(CGSizeMake(60, 40));
        make.top.equalTo(self.backView.mas_top).offset(0);
    }];
    [self.backView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(0);
        make.right.equalTo(self.mas_right).offset(0);
        make.height.mas_equalTo(325);
        make.bottom.equalTo(self.mas_bottom).offset(0);
        
    }];
    [self.pickerView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.mas_left).offset(0);
        make.right.equalTo(self.mas_right).offset(0);
        if (kStatusHeight>20) {
            make.bottom.equalTo(self.backView.mas_bottom).offset(-49);
        }else{
            make.bottom.equalTo(self.backView.mas_bottom).offset(0);
        }
        make.top.equalTo(self.backView.mas_top).offset(40);
    }];
}

-(UIPickerView*)pickerView{
    if (!_pickerView) {
        _pickerView = [[UIPickerView alloc]init];
        _pickerView.delegate = self;
        _pickerView.dataSource = self;
        _pickerView.backgroundColor = [UIColor whiteColor];
    }
    return _pickerView;
}


@end

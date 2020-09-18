//
//  XYTableViewAlertView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/24.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "XYTableViewAlertView.h"
#import "XYTableViewAlertCell.h"

@interface XYTableViewAlertView() <UITableViewDataSource, UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UIButton *sureButton;
@property (weak, nonatomic) IBOutlet UIView *contentView;

@property (nonatomic, strong) NSMutableArray *selectedArrays;

@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@end

@implementation XYTableViewAlertView


- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.frame = CGRectMake(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
    
    _contentView.layer.cornerRadius = 8;
    _sureButton.layer.cornerRadius = 25;
    
    _mainTableView.dataSource = self;
    _mainTableView.delegate = self;
    
    _selectedArrays = [NSMutableArray array];
    
    [_mainTableView registerNib:[UINib nibWithNibName:@"XYTableViewAlertCell" bundle:nil] forCellReuseIdentifier:@"XYTableViewAlertCellID"];
    
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    XYTableViewAlertCell *cell = [tableView dequeueReusableCellWithIdentifier:@"XYTableViewAlertCellID"];
    cell.itemContentLabel.text = self.dataSources[indexPath.row];
    if ([self.selectedArrays containsObject:self.dataSources[indexPath.row]]) {
        cell.logoImageView.image = [UIImage imageNamed:@"mine_selected"];
    } else {
        cell.logoImageView.image = [UIImage imageNamed:@"mine_selected_no"];
    }
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    NSString *content = self.dataSources[indexPath.row];
    if ([_selectedArrays containsObject:content]) {
        [_selectedArrays removeObject:content];
    } else {
        [_selectedArrays addObject:content];
    }
    
    [self.mainTableView reloadRowAtIndexPath:indexPath withRowAnimation:UITableViewRowAnimationNone];
}

- (IBAction)cancelAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
}

- (IBAction)sureAction:(id)sender {
    [self.parentVC lew_dismissPopupView];
    
    if (self.sureActionBlock) {
        self.sureActionBlock(_selectedArrays);
    }
}

@end

//
//  XYOwnerSortListView.m
//  hairBall
//
//  Created by zyy on 2019/10/31.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "XYOwnerSortListView.h"
#import "XYOwnerListCell.h"
#import "SocketMessage53.h"
#import <SDImageCache.h>

@interface XYOwnerSortListView ()<UITableViewDelegate , UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UIView *ownerView;
@property (weak, nonatomic) IBOutlet UILabel *ownerIndexLabel;
@property (weak, nonatomic) IBOutlet UIImageView *ownerHeaderImageV;
@property (weak, nonatomic) IBOutlet UILabel *ownerNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *frontUserMemberlabel;

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet UIButton *cancelBut;

@property (strong , nonatomic) NSMutableArray * dataArray;


@property (assign , nonatomic) NSInteger ownerIndex;

@end

@implementation XYOwnerSortListView

static NSString * cellID = @"XYOwnerListCell";

-(void)awakeFromNib{
    [super awakeFromNib];
    
    self.type = MMPopupTypeSheet;
    self.attachedView.mm_dimBackgroundView.backgroundColor = MMHexColor(0x0000004F);
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(SCREEN_WIDTH, 413 * SCALE_W));
    }];

    self.cancelBut.layer.masksToBounds = YES;
    self.cancelBut.layer.cornerRadius = 21;
    
    self.ownerView.layer.cornerRadius = 12;
    self.ownerView.layer.masksToBounds = YES;
    
    self.ownerHeaderImageV.layer.masksToBounds = YES;
    self.ownerHeaderImageV.layer.cornerRadius = 27;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"XYOwnerListCell" bundle:[NSBundle mainBundle]] forCellReuseIdentifier:cellID];
    
//    [self.tableView addObserver:self forKeyPath:@"contentOffset" options:NSKeyValueObservingOptionNew context:nil];

    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    
    [self setData];
}

-(void)setData{
    
    NSMutableDictionary<NSString*, LiveRoomMicroOrder*> * orders = nil;
    if ([CXClientModel instance].sex.integerValue == 1) {//需要判断自己是男是女
        orders = [CXClientModel instance].room.leftOrders;
    }
    else{
        orders = [CXClientModel instance].room.rightOrders;
    }
    
    MJWeakSelf;
    
    self.dataArray = [NSMutableArray array];
    
//    for (int i = 0 ; i<[ModelClient instance].room.WomenOrders.allKeys.count; i++) {
//
//        ModelGameMicroOrder * order = [[ModelClient instance].room.WomenOrders objectForKey:[ModelClient instance].room.WomenOrders.allKeys[i]];
//
//        [self.dataArray addObject:order];
//
//    }
    
    [orders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
//        self.dataArray[obj.Number.integerValue] = obj;
        [weakSelf.dataArray addObject:obj];
        if([obj.modelUser.UserId isEqualToString:[CXClientModel instance].userId]){
            weakSelf.ownerIndex = weakSelf.dataArray.count-1;
            weakSelf.ownerNameLabel.text = obj.modelUser.Name;
            [weakSelf.ownerHeaderImageV sd_setImageWithURL:[NSURL URLWithString:obj.modelUser.HeadImageUrl] placeholderImage:[UIImage imageNamed:@"avatar_default"]];
            weakSelf.ownerIndexLabel.text = self.dataArray.count >9 ? [NSString stringWithFormat:@"%lu" , self.dataArray.count] : [NSString stringWithFormat:@"0%lu" , self.dataArray.count];
            weakSelf.frontUserMemberlabel.text = [NSString stringWithFormat:@"前面还有%lu人" , (unsigned long)self.dataArray.count];
        }
    }];
//    [[ModelClient instance].room.users enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, ModelGameUser * _Nonnull obj, BOOL * _Nonnull stop) {
//        [weakSelf.dataArray addObject:obj];
//
//
//    }];
    
    
    
//    [self.dataArray enumerateObjectsUsingBlock:^(ModelGameUser * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
//        if([obj.UserId isEqualToString:[ModelClient instance].uid]){//表明是自己
//            weakSelf.ownerNameLabel.text = obj.Name;
//            weakSelf.frontUserMemberlabel.text = [NSString stringWithFormat:@"前面还有%lu人" , (unsigned long)idx];
//            weakSelf.ownerIndexLabel.text = idx>8 ? [NSString stringWithFormat:@"%lu" , idx + 1] : [NSString stringWithFormat:@"0%lu" , idx + 1];
//            [weakSelf.ownerHeaderImageV sd_setImageWithURL:[NSURL URLWithString:obj.HeadImageUrl] placeholderImage:[UIImage imageNamed:@"avatar_default"]];
//
//        }
//    }];
    
    [self.tableView reloadData];
}

-(void)dealloc{
//    [self.tableView removeObserver:self forKeyPath:@"contentOffset"];
}

- (IBAction)cancelSortClick:(UIButton *)sender {
    
    SocketMessage53 * join = [SocketMessage53 new];
    kWeakSelf
    [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
        if (request.response.isSuccess) {
            [weakSelf hide];
        }
    }];
    
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    XYOwnerListCell * cell = [tableView dequeueReusableCellWithIdentifier:cellID];
    if (!cell) {
        cell = [[NSBundle mainBundle] loadNibNamed:cellID owner:self options:nil].firstObject;
    }
    cell.indexP = indexPath.row;
    cell.order = self.dataArray[indexPath.row];
    return cell;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 54.0;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
}

//-(void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSKeyValueChangeKey,id> *)change context:(void *)context{
//    if ([keyPath isEqualToString:@"contentOffset"]) {
//        NSLog(@"%@" , change);
//        
//        CGPoint point = [[change objectForKey:@"new"] CGPointValue];
//        
//        NSLog(@"point.y %f" , point.y);
//        
//        if (point.y>54.0 * (self.ownerIndex+1) || point.y < 54.0 * (self.ownerIndex - 6)) {
//            self.ownerView.hidden = NO;
//        }
//        else{
//            self.ownerView.hidden = YES;
//        }
//        
//    }
//}

//-(NSMutableArray *)dataArray{
//    if (!_dataArray) {
//        _dataArray = [NSMutableArray array];
//    }
//    return _dataArray;
//}


@end

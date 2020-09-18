//
//  XYRoomAlreadApplyListView.m
//  hairBall
//
//  Created by zyy on 2019/10/31.
//  Copyright © 2019年 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "XYRoomAlreadApplyListView.h"
#import "XYRoomAlreadListCell.h"
#import "SocketMessageJoinSeat.h"
#import "SocketMessage23.h"
#import "SocketMessage22.h"

@interface XYRoomAlreadApplyListView ()<UITableViewDataSource , UITableViewDelegate>

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (weak, nonatomic) IBOutlet UIButton *womanBut;
@property (weak, nonatomic) IBOutlet UIButton *manBut;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *butWidth;

@property (strong , nonatomic) NSMutableArray * dataArray;

@end

@implementation XYRoomAlreadApplyListView

static NSString * cellID = @"XYRoomAlreadListCell";

- (void)awakeFromNib{
    [super awakeFromNib];
    
    if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5 || [CXClientModel instance].room.RoomData.RoomType.integerValue == 8) {
        self.butWidth.constant = (375/2) * SCALE_W;
    } else {
        self.butWidth.constant = 0;
    }
    
    self.womanBut.layer.cornerRadius = 10;
    self.womanBut.layer.masksToBounds = YES;
    self.manBut.layer.cornerRadius = 10;
    self.manBut.layer.masksToBounds = YES;
    
    if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) {
        [self.manBut setTitle:@"男嘉宾" forState:UIControlStateNormal];
        [self.womanBut setTitle:@"女嘉宾" forState:UIControlStateNormal];
        [self manClick:nil];
    } else if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 8) {
        [self.manBut setTitle:@"沙发座" forState:UIControlStateNormal];
        [self.womanBut setTitle:@"普通座" forState:UIControlStateNormal];
        [self manClick:nil];
    } else {
        [self womanClick:nil];
        [self.womanBut setTitle:@"普通座" forState:UIControlStateNormal];
    }
    
    self.type = MMPopupTypeSheet;
    self.attachedView.mm_dimBackgroundView.backgroundColor = MMHexColor(0x0000004F);
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(375 * SCALE_W, 447 * SCALE_W));
    }];
    
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    
//    [self.tableView registerNib:[UINib nibWithNibName:cellID bundle:[NSBundle mainBundle]] forCellReuseIdentifier:cellID];
//
//    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect: CGRectMake(0, 0, kScreenWidth - _butWidth.constant, 49) byRoundingCorners:UIRectCornerTopLeft | UIRectCornerTopRight cornerRadii:CGSizeMake(10,10)];
//    //创建 layer
//    CAShapeLayer *maskLayer = [[CAShapeLayer alloc] init];
//    maskLayer.frame = CGRectMake(0, 0, kScreenWidth - _butWidth.constant, 49);
//    //赋值
//    maskLayer.path = maskPath.CGPath;
//    self.manBut.layer.mask = maskLayer;
//
//    self.manBut.layer.backgroundColor = [UIColor whiteColor].CGColor;
//
//
//    UIBezierPath *maskPath2 = [UIBezierPath bezierPathWithRoundedRect: CGRectMake(0, 0, kScreenWidth - _butWidth.constant, 49) byRoundingCorners:UIRectCornerTopLeft | UIRectCornerTopRight cornerRadii:CGSizeMake(10,10)];
//    //创建 layer
//    CAShapeLayer *maskLayer2 = [[CAShapeLayer alloc] init];
//    maskLayer2.frame = CGRectMake(0, 0, kScreenWidth - _butWidth.constant, 49);
//    //赋值
//    maskLayer2.path = maskPath2.CGPath;
//    self.womanBut.layer.mask = maskLayer2;
//
//    self.womanBut.layer.backgroundColor = [UIColor whiteColor].CGColor;

    
}

- (void)setListType:(NSInteger)listType {
    _listType = listType;
    
    if (listType == 1) {
        [self manClick:_manBut];
    } else {
        [self womanClick:_womanBut];
    }
    
}

- (IBAction)womanClick:(UIButton *)sender {
    MJWeakSelf;
    if (self.womanBut.selected == YES) {
        return;
    }
    
    self.womanBut.selected = YES;
    self.manBut.selected = NO;
    
    self.dataArray = nil;
    
    self.dataArray = [NSMutableArray arrayWithCapacity:[CXClientModel instance].room.rightOrders.allKeys.count];
    
    BOOL isManMicor = YES;
    NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:2];
    LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:index];
    if (seat.modelUser) { // 男麦有人
        isManMicor = NO;
    }
    
    [[CXClientModel instance].room.rightOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
        obj.isCanUpMicro = isManMicor;
        [weakSelf.dataArray addObject:obj];
        
    }];
    
    [self.tableView reloadData];
    
    self.manBut.backgroundColor = [UIColor whiteColor];
    self.womanBut.backgroundColor = UIColorHex(0xe1e1e1);
    
//    for (int i = 0; i< self.womanBut.layer.sublayers.count; i++ ) {
//        CALayer * layer = self.womanBut.layer.sublayers[i];
//        if ([layer isKindOfClass:[CAGradientLayer class]]) {
//            [layer removeFromSuperlayer];
//        }
//
//    }
//
//    CAGradientLayer * gradientLayer = [[CAGradientLayer alloc] init];
//    gradientLayer.colors = @[(__bridge id)[UIColor colorWithHexString:@"#fcfcfc"].CGColor , (__bridge id)[UIColor colorWithHexString:@"#e1e1e1"].CGColor];
//    gradientLayer.locations = @[@0.0,@1.0];
//    gradientLayer.startPoint = CGPointMake(0, 0);
//    gradientLayer.endPoint = CGPointMake(0, 1.0);
//    gradientLayer.frame = CGRectMake(0, 0, sender.width, 49);
//    [self.manBut.layer insertSublayer:gradientLayer atIndex:0];

}

- (IBAction)manClick:(UIButton *)sender {
    
    if (self.manBut.selected == YES) {
        return;
    }
    
    self.manBut.selected = YES;
    self.womanBut.selected = NO;
    
    self.dataArray = nil;
    
    self.dataArray = [NSMutableArray arrayWithCapacity:[CXClientModel instance].room.leftOrders.allKeys.count];
    
    BOOL isManMicor = YES;
    NSIndexPath *index = [NSIndexPath indexPathForRow:0 inSection:1];
    LiveRoomMicroInfo *seat = [[CXClientModel instance].room.seats objectForKey:index];
    if (seat.modelUser) { // 男麦有人
        isManMicor = NO;
    }
    
    MJWeakSelf;
    [[CXClientModel instance].room.leftOrders enumerateKeysAndObjectsUsingBlock:^(NSString * _Nonnull key, LiveRoomMicroOrder * _Nonnull obj, BOOL * _Nonnull stop) {
        
        obj.isCanUpMicro = isManMicor;
        [weakSelf.dataArray addObject:obj];
        
    }];
    
    [self.tableView reloadData];
    
    self.womanBut.backgroundColor = [UIColor whiteColor];
    self.manBut.backgroundColor = UIColorHex(0xe1e1e1);
    
//    for (int i = 0; i< self.manBut.layer.sublayers.count; i++ ) {
//        CALayer * layer = self.manBut.layer.sublayers[i];
//        if ([layer isKindOfClass:[CAGradientLayer class]]) {
//            [layer removeFromSuperlayer];
//        }
//    }
//
//    CAGradientLayer * gradientLayer = [[CAGradientLayer alloc] init];
//    gradientLayer.colors = @[(__bridge id)[UIColor colorWithHexString:@"#fcfcfc"].CGColor , (__bridge id)[UIColor colorWithHexString:@"#e1e1e1"].CGColor];
//    gradientLayer.locations = @[@0.0,@1.0];
//    gradientLayer.startPoint = CGPointMake(0, 0);
//    gradientLayer.endPoint = CGPointMake(0, 1.0);
//    gradientLayer.frame = CGRectMake(0, 0, sender.width, 49);
//    [self.womanBut.layer insertSublayer:gradientLayer atIndex:0];
    
}

-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.dataArray.count;
}
-(NSInteger)numberOfSectionsInTableView:(UITableView *)tableView{
    return 1;
}

-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    
    XYRoomAlreadListCell * cell = [tableView dequeueReusableCellWithIdentifier:cellID];
    if (!cell) {
        cell = [[NSBundle mainBundle] loadNibNamed:cellID owner:self options:nil].firstObject;
    }
    
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    cell.order = self.dataArray[indexPath.row];
    
    MJWeakSelf;
    cell.alreadListOpreationBlock = ^(AlreadButOpreationType opreationType , LiveRoomMicroOrder * order) {
        
        if (opreationType == joinOpreationType) {
            SocketMessageJoinSeat * join = [SocketMessageJoinSeat new];
            join.TargetUserId = [order.modelUser.UserId numberValue];
            [[CXClientModel instance] sendSocketRequest:join withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                
                if (request.noError) {//成功了  删除在队列中的成员 , 这里自动就减少了，厉害，我都还没有完全搞清楚
                }
                
//                [weakSelf toastSocketRequestErrorAndFailure:request];
                [weakSelf hide];
            }];
        }
        else if (opreationType == deleteOpreationType){
            SocketMessage23 * message = [SocketMessage23 new];
            message.Id = [order.modelUser.UserId numberValue];
            [[CXClientModel instance] sendSocketRequest:message withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                
                [weakSelf hide];
                
            }];
            
        }
        else if (opreationType == upOpreationType){
            SocketMessage22 * message = [SocketMessage22 new];
            message.Id = [order.modelUser.UserId numberValue];
            [[CXClientModel instance] sendSocketRequest:message withCallback:^(__kindof SocketMessageRequest * _Nonnull request) {
                
                [weakSelf hide];
            }];
            
        }
        
    };
    
    return cell;
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 66.0;
}




@end

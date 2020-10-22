//
//  CXRedPacketAwardRecordView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import "CXRedPacketAwardRecordView.h"
#import "CXRedPacketAwardRecordCell.h"

@interface CXRedPacketAwardRecordView () <UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (weak, nonatomic) IBOutlet UITableView *mainTableView;

@end

@implementation CXRedPacketAwardRecordView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(312, 451));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = NO;
    self.type = MMPopupTypeCustom;
    
    [self.mainTableView registerNib:[UINib nibWithNibName:@"CXRedPacketAwardRecordCell" bundle:nil] forCellReuseIdentifier:@"CXRedPacketAwardRecordCellID"];
    self.mainTableView.dataSource = self;
}
- (IBAction)closeAction:(id)sender {
    [self hide];
    [MMPopupView hideAll];
}

- (void)setRecordArrays:(NSArray<CXLiveRoomRedPacketModel *> *)recordArrays {
    _recordArrays = recordArrays;
    
    [self.mainTableView reloadData];
    
    __block CXLiveRoomRedPacketModel *currentUserRedPacket = [CXLiveRoomRedPacketModel new];
    [_recordArrays enumerateObjectsUsingBlock:^(CXLiveRoomRedPacketModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (obj.UserId == [[CXClientModel instance].userId integerValue]) {
            currentUserRedPacket = obj;
            *stop = YES;
        }
    }];
    
    if (currentUserRedPacket.UserId > 0) {
        self.titleLabel.text = [NSString stringWithFormat:@"恭喜你中奖了，获得红包\n%0.2f元",currentUserRedPacket.Value];
    } else {
        self.titleLabel.text = @"很遗憾你没有抢到红包";
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return _recordArrays.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    CXRedPacketAwardRecordCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXRedPacketAwardRecordCellID"];
    CXLiveRoomRedPacketModel *model = _recordArrays[indexPath.row];
    [cell.avatar sd_setImageWithURL:[NSURL URLWithString:model.HeadImageUrl]];
    cell.nameLabel.text = model.NickName;
    cell.moneyLabel.text = [NSString stringWithFormat:@"%0.2f元",model.Value];
    cell.rankImg.hidden = YES;
    if (indexPath.row == 0) {
        cell.rankImg.hidden = NO;
        cell.rankImg.image = [UIImage imageNamed:@"liveroom_redpacket_first"];
    } else if (indexPath.row == 1) {
        cell.rankImg.hidden = NO;
        cell.rankImg.image = [UIImage imageNamed:@"liveroom_redpacket_second"];
    } else if (indexPath.row == 2) {
        cell.rankImg.hidden = NO;
        cell.rankImg.image = [UIImage imageNamed:@"liveroom_redpacket_third"];
    }
    return cell;
}

@end

//
//  CXLiveRoomCreateRoomViewController.m
//  hairBall
//
//  Created by mahong yang on 2020/6/1.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomCreateRoomViewController.h"
#import "CXLiveRoomSetupRoomNameCell.h"
#import "CXLIveRoomSetupRoomTypeCell.h"
#import "CXLIveRoomSetupRoomAudioStatusCell.h"
#import "CXLiveRoomSetupTypeHelpViewController.h"

@interface CXLiveRoomCreateRoomViewController () <UITableViewDataSource, UITableViewDelegate>
@property (weak, nonatomic) IBOutlet UITableView *mainTableVIew;
@property (retain, nonatomic) IBOutlet UIButton *openBtn;

@property (nonatomic, strong) NSArray <CXHomeRoomModeModel*> *tagsArrays;

@property (strong, nonatomic) UITextField *nameTextField;
@property (nonatomic, copy) NSString *room_name;
@property (nonatomic, strong) NSMutableArray *selectedTypeArrays;
@property (nonatomic, strong) NSMutableArray *selectedFunctionArrays;
@property (nonatomic, assign) BOOL IsExclusiveRoom; // 是否是专属房
@property (nonatomic, assign) BOOL IsCloseCamera; // 是否允许音频上麦


@end

@implementation CXLiveRoomCreateRoomViewController

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.view endEditing:YES];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view from its nib.
    
    self.title = @"房间设置";
    self.view.backgroundColor = [UIColor whiteColor];
    
    _openBtn.layer.masksToBounds = YES;
    _openBtn.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(250, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [_openBtn setBackgroundImage:image forState:UIControlStateNormal];
    
    self.mainTableVIew.dataSource = self;
    self.mainTableVIew.delegate = self;
    [self.mainTableVIew registerNib:[UINib nibWithNibName:@"CXLiveRoomSetupRoomNameCell" bundle:nil] forCellReuseIdentifier:@"CXLiveRoomSetupRoomNameCellID"];
    [self.mainTableVIew registerNib:[UINib nibWithNibName:@"CXLIveRoomSetupRoomTypeCell" bundle:nil] forCellReuseIdentifier:@"CXLIveRoomSetupRoomTypeCellID"];
    [self.mainTableVIew registerNib:[UINib nibWithNibName:@"CXLIveRoomSetupRoomAudioStatusCell" bundle:nil] forCellReuseIdentifier:@"CXLIveRoomSetupRoomAudioStatusCellID"];
    
    _room_name = _roomInfo.room_name;
    _IsExclusiveRoom = [_roomInfo.is_exclusive_room intValue] == 1;
    _IsCloseCamera = [_roomInfo.is_close_camera intValue] == 1;
    
    _selectedFunctionArrays = [NSMutableArray array];
    if ([_roomInfo.is_open_red_packet intValue] == 1) {
        [_selectedFunctionArrays addObject:@"红包"];
    }
    if ([_roomInfo.is_open_break_egg intValue] == 1) {
        [_selectedFunctionArrays addObject:@"砸蛋"];
    }
    if ([_roomInfo.is_open_pick_song intValue] == 1) {
        [_selectedFunctionArrays addObject:@"点歌"];
    }
    if ([_roomInfo.is_open_media_library intValue] == 1) {
        [_selectedFunctionArrays addObject:@"媒体库"];
    }
    if ([_roomInfo.is_open_video_frame intValue] == 1) {
        [_selectedFunctionArrays addObject:@"视频框"];
    }
    
    _selectedTypeArrays = [NSMutableArray arrayWithObjects:_roomInfo.room_type, nil];
    
    [self getTagsList];
}

- (void)getTagsList {
    __weak typeof (self) wself = self;
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    [CXHTTPRequest POSTWithURL:@"/index.php/Api/Tags/getTagsList" parameters:@{@"signature" : signature} callback:^(id responseObject, BOOL isCache, NSError *error) {
        if (!error) {
            wself.tagsArrays = [NSArray modelArrayWithClass:[CXHomeRoomModeModel class] json:responseObject[@"data"][@"mode_list"]];
            [wself.mainTableVIew reloadSection:1 withRowAnimation:UITableViewRowAnimationAutomatic];
        }
    }];
}

- (IBAction)openRoomAction:(id)sender {
    self.room_name = self.nameTextField.text;
    if (self.room_name.length <= 0) {
        [self toast:@"房间名不能为空"];
        return;
    }
    CXLiveRoomDataModel *roomData = [CXLiveRoomDataModel new];
    roomData.RoomName = self.room_name;
    roomData.RoomType = [NSNumber numberWithString:self.selectedTypeArrays[0]];
    [CXClientModel instance].room.RoomData = roomData;
    if ([CXClientModel instance].room.RoomData.RoomType.integerValue == 5) {
        [CXClientModel instance].room.RoomData.IsExclusiveRoom = self.IsExclusiveRoom;
    }
//    [ModelClient instance].IsCloseCamera = self.IsCloseCamera;
//    // @"红包", @"砸蛋", @"点歌", @"媒体库", @"视频框"
//    if ([self.selectedFunctionArrays containsObject:@"红包"]) {
//        [ModelClient instance].IsOpenRedPacket = YES;
//    }
//    if ([self.selectedFunctionArrays containsObject:@"砸蛋"]) {
//        [ModelClient instance].IsOpenBreakEgg = YES;
//    }
//    if ([self.selectedFunctionArrays containsObject:@"点歌"]) {
//        [ModelClient instance].IsOpenPickSong = YES;
//    }
//    if ([self.selectedFunctionArrays containsObject:@"媒体库"]) {
//        [ModelClient instance].IsOpenMediaLibrary = YES;
//    }
//    if ([self.selectedFunctionArrays containsObject:@"视频框"]) {
//        [ModelClient instance].IsOpenVideoFrame = YES;
//    }
    [AppController joinRoom:_roomInfo.room_id];
}

#pragma mark - <UITableViewDataSource, UITableViewDelegate>
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 2;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 1;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    kWeakSelf
    if (indexPath.section == 0) {
        CXLiveRoomSetupRoomNameCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLiveRoomSetupRoomNameCellID"];
        cell.nameTextField.text = _room_name;
        
        self.nameTextField = cell.nameTextField;
        return cell;
    } else if (indexPath.section == 1) {
        CXLIveRoomSetupRoomTypeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLIveRoomSetupRoomTypeCellID"];

        cell.selectedArrays = self.selectedTypeArrays;
        cell.sectionIndex = indexPath.section;
        cell.dataSources = self.tagsArrays;
        cell.IsExclusiveRoom = self.IsExclusiveRoom;
        cell.setupRoomTypeExclusiveActionBlock = ^(BOOL isExclusive) {
            weakSelf.IsExclusiveRoom = isExclusive;
            [weakSelf.mainTableVIew reloadSection:1 withRowAnimation:UITableViewRowAnimationAutomatic];
        };
        cell.setupRoomTypeSelectedArrayBlock = ^(NSArray * _Nonnull array) {
            weakSelf.selectedTypeArrays = [NSMutableArray arrayWithArray:array];
            [weakSelf.mainTableVIew reloadSection:1 withRowAnimation:UITableViewRowAnimationAutomatic];
        };
        
        cell.setupRoomTypeHelpActionBlock = ^{
            CXLiveRoomSetupTypeHelpViewController *vc = [CXLiveRoomSetupTypeHelpViewController new];
            [weakSelf.navigationController pushViewController:vc animated:YES];
        };
        return cell;
    } else if (indexPath.section == 2) {
        CXLIveRoomSetupRoomAudioStatusCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLIveRoomSetupRoomAudioStatusCellID"];
        cell.IsCloseCamera = _IsCloseCamera;
        cell.setupRoomTypeAudioActionBlock = ^(BOOL isAudio) {
            weakSelf.IsCloseCamera = isAudio;
            [weakSelf.mainTableVIew reloadSection:2 withRowAnimation:UITableViewRowAnimationAutomatic];
        };
        
        return cell;
    } else {
        CXLIveRoomSetupRoomTypeCell *cell = [tableView dequeueReusableCellWithIdentifier:@"CXLIveRoomSetupRoomTypeCellID"];
        cell.selectedArrays = [NSMutableArray arrayWithArray:_selectedFunctionArrays];
        cell.sectionIndex = indexPath.section;
        cell.dataSources = @[@"红包", @"砸蛋", @"点歌", @"媒体库", @"视频框"];
        cell.setupRoomTypeSelectedArrayBlock = ^(NSArray * _Nonnull array) {
            weakSelf.selectedFunctionArrays = [NSMutableArray arrayWithArray:array];
        };
        return cell;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        return 40;
    } else if (indexPath.section == 1) {
        NSInteger row = self.tagsArrays.count/3;
        CGFloat height = 0;
        if (self.tagsArrays.count % 3 == 0) {
            height += row*40;
        } else {
            height += (row + 1)*40;
        }
        
//        if ([self.selectedTypeArrays containsObject:@"5"]) {
//            height += 50;
//        }
        
        return height;
    } else if (indexPath.section == 2) {
        return 40;
    } else {
        return 150;
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section {
    return 0.01;;;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section {
    return 0.01;;
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    [self.view endEditing:YES];
}

//- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section {
//    UIView *headerView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 6)];
//    headerView.backgroundColor = UIColorHex(0xF3F3F3);
//    return headerView;
//}


- (void)dealloc {
    
}
@end

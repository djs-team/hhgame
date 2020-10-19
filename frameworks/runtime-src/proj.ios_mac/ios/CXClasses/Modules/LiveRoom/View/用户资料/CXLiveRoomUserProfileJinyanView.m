//
//  CXLiveRoomUserProfileJinyanView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/24.
//

#import "CXLiveRoomUserProfileJinyanView.h"
#import "CXLiveRoomUserProfileJinyanDateCell.h"

@interface CXLiveRoomUserProfileJinyanView () <UICollectionViewDataSource, UICollectionViewDelegate, UICollectionViewDelegateFlowLayout>
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIButton *sexBtn;
@property (weak, nonatomic) IBOutlet UIButton *locationBtn;

@property (weak, nonatomic) IBOutlet UICollectionView *dateCollectionView;

@property (weak, nonatomic) IBOutlet UIButton *jinyanBtn;

@property (nonatomic, strong) SocketMessageGetDisableMsgTemplatesDisableMsgData *selectedDate;

@property (nonatomic, strong) NSArray *dateArrays;

@end

@implementation CXLiveRoomUserProfileJinyanView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(320, 300));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeAlert;
        
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    _avatar.layer.masksToBounds = YES;
    _avatar.layer.cornerRadius = 21;
    _avatar.layer.borderColor = UIColorHex(0x9645E0).CGColor;
    _avatar.layer.borderWidth = 0.5;
    
    self.jinyanBtn.layer.masksToBounds = YES;
    self.jinyanBtn.layer.cornerRadius = 19;
    UIImage *image = [UIImage gradientImageWithSize:CGSizeMake(266, 38) Color1:UIColorHex(0xE85ABC) color2:UIColorHex(0x7D3EF1)];
    [self.jinyanBtn setBackgroundImage:image forState:UIControlStateNormal];
    
    _dateCollectionView.dataSource = self;
    _dateCollectionView.delegate = self;
    [_dateCollectionView registerNib:[UINib nibWithNibName:@"CXLiveRoomUserProfileJinyanDateCell" bundle:nil] forCellWithReuseIdentifier:@"CXLiveRoomUserProfileJinyanDateCellID"];
    
    [self getDisableMsgTemplates];
}

- (void)getDisableMsgTemplates {
    SocketMessageGetDisableMsgTemplates *request = [SocketMessageGetDisableMsgTemplates new];
    kWeakSelf
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof SocketMessageGetDisableMsgTemplates * _Nonnull request) {
        if (request.response.isSuccess) {
            weakSelf.dateArrays = request.response.DisableMsgs;
            [weakSelf.dateCollectionView reloadData];
        }
    }];
}

- (void)setUserInfo:(SocketMessageGetUserInfoResponse *)userInfo {
    LiveRoomUser *user = [LiveRoomUser modelWithJSON:[userInfo.User modelToJSONObject]];
    
    [_avatar sd_setImageWithURL:[NSURL URLWithString:user.HeadImageUrl]];
    _nameLabel.text = user.Name;
    [_sexBtn setTitle:user.Age.stringValue forState:UIControlStateNormal];
    if (user.Sex == 1) {
        [_sexBtn setImage:[UIImage imageNamed:@"nan2"] forState:UIControlStateNormal];
    } else {
        [_sexBtn setImage:[UIImage imageNamed:@"nv2"] forState:UIControlStateNormal];
    }
    
    [_locationBtn setTitle:user.City forState:UIControlStateNormal];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return _dateArrays.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomUserProfileJinyanDateCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXLiveRoomUserProfileJinyanDateCellID" forIndexPath:indexPath];
    SocketMessageGetDisableMsgTemplatesDisableMsgData *date = _dateArrays[indexPath.row];
    if (date.Time > 60*24) {
        [cell.dateBtn setTitle:[NSString stringWithFormat:@"%ld天", date.Time/(60*24)] forState:UIControlStateNormal];
    } else if (date.Time > 60) {
        [cell.dateBtn setTitle:[NSString stringWithFormat:@"%ld小时", date.Time/60] forState:UIControlStateNormal];
    } else {
        [cell.dateBtn setTitle:[NSString stringWithFormat:@"%ld分钟", date.Time] forState:UIControlStateNormal];
    }
    
    if (date.Id == _selectedDate.Id) {
        [cell.dateBtn setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
    } else {
        [cell.dateBtn setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
    }
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((320 - 32 - 10)/2, 30);
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    _selectedDate = _dateArrays[indexPath.row];
    [collectionView reloadData];
}

- (IBAction)jinyanAction:(id)sender {
    if (_selectedDate.Id <= 0) {
        return;
    }
    
    if (self.userProfileJinyanActionBlock) {
        self.userProfileJinyanActionBlock(_selectedDate.Id);
    }
    
}

@end

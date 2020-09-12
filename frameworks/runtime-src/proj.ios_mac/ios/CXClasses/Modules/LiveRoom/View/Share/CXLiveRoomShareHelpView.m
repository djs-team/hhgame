//
//  CXLiveRoomShareHelpView.m
//  hairBall
//
//  Created by mahong yang on 2020/5/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLiveRoomShareHelpView.h"
#import "CXLiveRoomShareHelpCell.h"
#import "JQCollectionViewAlignLayout.h"

@interface CXLiveRoomShareHelpView() <UICollectionViewDataSource, UICollectionViewDelegate, JQCollectionViewAlignLayoutDelegate>
@property (weak, nonatomic) IBOutlet UILabel *title_label;
@property (weak, nonatomic) IBOutlet UILabel *desc_label;

@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;

@end

@implementation CXLiveRoomShareHelpView

- (void)awakeFromNib {
    [super awakeFromNib];

    [self mas_makeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(320, 400));
    }];
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 15;
    
    self.mainCollectionView.delegate = self;
    self.mainCollectionView.dataSource = self;
      
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeCustom;

    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXLiveRoomShareHelpCell" bundle:nil] forCellWithReuseIdentifier:@"CXLiveRoomShareHelpCellID"];
}
- (void)setShareHelpType:(CXLiveRoomShareHelpViewType)shareHelpType {
    _shareHelpType = shareHelpType;
    if (shareHelpType == MusicReserve) {
        _title_label.text = @"请选择你的演唱嘉宾";
        _desc_label.text = @"只有麦上嘉宾可以演唱";
    }
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return [CXClientModel instance].room.users.allValues.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomShareHelpCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXLiveRoomShareHelpCellID" forIndexPath:indexPath];
    
    LiveRoomUser *user = [CXClientModel instance].room.users.allValues[indexPath.row];
    cell.user = user;
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(96, 110);
}

- (JQCollectionViewItemsHorizontalAlignment)collectionView:(UICollectionView *)collectionView layout:(JQCollectionViewAlignLayout *)layout itemsHorizontalAlignmentInSection:(NSInteger)section {
    return JQCollectionViewItemsHorizontalAlignmentLeft;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    
    LiveRoomUser *user = [CXClientModel instance].room.users.allValues[indexPath.row];
    if (self.didSelectedUser) {
        self.didSelectedUser(user);
    }
    
    [self hide];
    
}

@end

//
//  CXLIveRoomSetupRoomTypeCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/2.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXLIveRoomSetupRoomTypeCell.h"
#import "JQCollectionViewAlignLayout.h"
#import "CXLiveRoomSetupRoomTypeItemCell.h"
#import "CXHomeRoomModel.h"

@interface CXLIveRoomSetupRoomTypeCell() <UICollectionViewDataSource, UICollectionViewDelegate, JQCollectionViewAlignLayoutDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *cell_logo;
@property (weak, nonatomic) IBOutlet UILabel *cell_titleLabel;
@property (weak, nonatomic) IBOutlet UIButton *cell_helpButton;

@property (weak, nonatomic) IBOutlet UICollectionView *mainCollectionView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *collectionHeightLayout;

@property (weak, nonatomic) IBOutlet UIButton *exclusive_noBtn;
@property (weak, nonatomic) IBOutlet UIButton *exclusive_yesBtn;


@end

@implementation CXLIveRoomSetupRoomTypeCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.mainCollectionView.dataSource = self;
    self.mainCollectionView.delegate = self;
    [self.mainCollectionView registerNib:[UINib nibWithNibName:@"CXLiveRoomSetupRoomTypeItemCell" bundle:nil] forCellWithReuseIdentifier:@"CXLiveRoomSetupRoomTypeItemCellID"];
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setSectionIndex:(NSInteger)sectionIndex {
    _sectionIndex = sectionIndex;
    if (sectionIndex == 1) {
        _cell_logo.image = [UIImage imageNamed:@"home_leibie"];
        _cell_titleLabel.text = @"房间类型:";
        _cell_helpButton.hidden = NO;
        
    } else {
        _cell_logo.image = [UIImage imageNamed:@"home_leibie"];
        _cell_titleLabel.text = @"房间功能:";
        _cell_helpButton.hidden = YES;
        
    }
}

- (void)setDataSources:(NSArray *)dataSources {
    _dataSources = dataSources;
    NSInteger row = dataSources.count/3;
    if (dataSources.count % 3 == 0) {
        _collectionHeightLayout.constant = row*40;
    } else {
        _collectionHeightLayout.constant = (row + 1)*40;
    }
    
    [self.mainCollectionView reloadData];
}

- (void)setIsExclusiveRoom:(BOOL)IsExclusiveRoom {
    _IsExclusiveRoom = IsExclusiveRoom;
    
    if (_IsExclusiveRoom == YES) {
        [_exclusive_noBtn setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
        [_exclusive_yesBtn setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
    } else {
        [_exclusive_noBtn setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
        [_exclusive_yesBtn setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
    }

}

#pragma mark - <UICollectionViewDataSource, UICollectionViewDelegateFlowLayout, UICollectionViewDelegate>
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return _dataSources.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXLiveRoomSetupRoomTypeItemCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXLiveRoomSetupRoomTypeItemCellID" forIndexPath:indexPath];
    if (_sectionIndex == 1) {
        CXHomeRoomModeModel *tag = _dataSources[indexPath.row];
        cell.item_nameLabel.text = tag.room_mode;
        if ([_selectedArrays containsObject:tag.mode_id]) {
            cell.item_logo.image = [UIImage imageNamed:@"home_selected_on"];
        } else {
            cell.item_logo.image = [UIImage imageNamed:@"home_selected_off"];
        }    } else {
        NSString *item = _dataSources[indexPath.row];
        cell.item_nameLabel.text = item;
        if ([_selectedArrays containsObject:item]) {
            cell.item_logo.image = [UIImage imageNamed:@"home_selected_on"];
        } else {
            cell.item_logo.image = [UIImage imageNamed:@"home_selected_off"];
        }
    }
    
    return cell;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake((kScreenWidth - 116)/3 - 8, 30);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    return UIEdgeInsetsMake(0, 0, 0, 0);
}

- (JQCollectionViewItemsHorizontalAlignment)collectionView:(UICollectionView *)collectionView layout:(JQCollectionViewAlignLayout *)layout itemsHorizontalAlignmentInSection:(NSInteger)section {
    return JQCollectionViewItemsHorizontalAlignmentLeft;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (_sectionIndex == 1) {
        [_selectedArrays removeAllObjects];
        CXHomeRoomModeModel *tag = _dataSources[indexPath.row];
        [_selectedArrays addObject:tag.mode_id];
    } else {
        NSString *item = _dataSources[indexPath.row];
        if ([_selectedArrays containsObject:item]) {
            [_selectedArrays removeObject:item];
        } else {
            [_selectedArrays addObject:item];
        }
    }
    
    [self.mainCollectionView reloadData];
    if (self.setupRoomTypeSelectedArrayBlock) {
        self.setupRoomTypeSelectedArrayBlock(_selectedArrays);
    }
}


- (IBAction)exclusiveAction:(UIButton *)sender {
    if (sender.tag == 10) { // 否
        if (self.setupRoomTypeExclusiveActionBlock) {
            self.setupRoomTypeExclusiveActionBlock(NO);
        }
        [_exclusive_noBtn setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
        [_exclusive_yesBtn setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
    } else { // 是
        if (self.setupRoomTypeExclusiveActionBlock) {
            self.setupRoomTypeExclusiveActionBlock(YES);
        }
        [_exclusive_noBtn setImage:[UIImage imageNamed:@"home_selected_off"] forState:UIControlStateNormal];
        [_exclusive_yesBtn setImage:[UIImage imageNamed:@"home_selected_on"] forState:UIControlStateNormal];
    }
}

- (IBAction)helpAction:(id)sender {
    if (self.setupRoomTypeHelpActionBlock) {
        self.setupRoomTypeHelpActionBlock();
    }
}

@end

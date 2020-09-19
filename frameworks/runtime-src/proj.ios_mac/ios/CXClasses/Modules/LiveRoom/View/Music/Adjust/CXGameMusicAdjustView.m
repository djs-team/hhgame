//
//  CXGameMusicAdjustView.m
//  hairBall
//
//  Created by mahong yang on 2020/2/19.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXGameMusicAdjustView.h"
#import "CXGameMusicAdjustEffectsCell.h"
#import "CXGameMusicAdjustRateCell.h"

@interface CXGameMusicAdjustView() <UICollectionViewDataSource, UICollectionViewDelegateFlowLayout>
@property (nonatomic, strong) NSArray *reverberationArray;
@property (nonatomic, strong) NSArray *reverberationValueArray;
@property (nonatomic, strong) NSArray *effectsArray;
@property (nonatomic, strong) NSArray *reverbOfTypeArray;
@property (weak, nonatomic) IBOutlet UIView *bgView;

@property (weak, nonatomic) IBOutlet UICollectionView *rateCollectionView;
@property (weak, nonatomic) IBOutlet UICollectionView *effectsCollectionsView;

@property (nonatomic, strong) NSString *selectedEffects;
@property (nonatomic, assign) NSInteger selectedIndex;

@end

@implementation CXGameMusicAdjustView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.bgView.layer.masksToBounds = YES;
    self.bgView.layer.cornerRadius = 10;
    
    [_rateCollectionView registerNib:[UINib nibWithNibName:@"CXGameMusicAdjustRateCell" bundle:nil] forCellWithReuseIdentifier:@"CXGameMusicAdjustRateCellID"];
    [_effectsCollectionsView registerNib:[UINib nibWithNibName:@"CXGameMusicAdjustEffectsCell" bundle:nil] forCellWithReuseIdentifier:@"CXGameMusicAdjustEffectsCellID"];
    
    _rateCollectionView.dataSource = self;
    _rateCollectionView.delegate = self;
    
    _effectsCollectionsView.dataSource = self;
    _effectsCollectionsView.delegate = self;
    
    self.selectedEffects = @"KTV";
    self.selectedIndex = 6;
}

- (IBAction)closeAction:(id)sender {
    [self removeFromSuperview];
}

#pragma mark - UICollectionViewDataSource, UICollectionViewDelegateFlowLayout
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    if (collectionView == _effectsCollectionsView) {
        return self.effectsArray.count;
    } else {
        return self.reverberationArray.count;
    }
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (collectionView == _effectsCollectionsView) {
        CXGameMusicAdjustEffectsCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXGameMusicAdjustEffectsCellID" forIndexPath:indexPath];
        NSString *itemName = self.effectsArray[indexPath.row];
        cell.itemNameLabel.text = itemName;
        if ([itemName isEqualToString:self.selectedEffects]) {
            cell.itemNameLabel.backgroundColor = UIColorHex(0xCFCACA);
        } else {
            cell.itemNameLabel.backgroundColor = UIColorHex(0xA933CA);
        }
        return cell;
    } else {
        CXGameMusicAdjustRateCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXGameMusicAdjustRateCellID" forIndexPath:indexPath];
        NSDictionary *dict = self.reverberationArray[indexPath.row];
        cell.dict = dict;
        double value = [self.reverberationValueArray[_selectedIndex][indexPath.row] doubleValue];
        cell.currentValue = value;
        cell.adjustRateBlock = ^(float value) {
            NSInteger index = [self.reverbOfTypeArray[indexPath.row] integerValue];
            [[CXClientModel instance].agoraEngineManager.engine setLocalVoiceReverbOfType:index withValue:lroundf(value)];
        };
        return cell;
    }
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    if (collectionView == _effectsCollectionsView) {
        return CGSizeMake(kScreenWidth/7, 50);
    } else {
        return CGSizeMake(kScreenWidth/5, 280);
    }
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    if (collectionView == _effectsCollectionsView) {
        self.selectedIndex = indexPath.row;
        self.selectedEffects = _effectsArray[indexPath.row];
        [[CXClientModel instance].agoraEngineManager.engine setLocalVoiceReverbPreset:indexPath.row + 1];
        
        [self.rateCollectionView reloadData];
        [self.effectsCollectionsView reloadData];
    }
}

- (NSArray *)reverberationArray {
    if (!_reverberationArray) {
        NSDictionary *dict0 = @{@"title": @"ROOM\nSIZE",@"min":@"0",@"max":@"100",@"current":@"0"};
        NSDictionary *dict1 = @{@"title": @"WET\nDELAY",@"min":@"0",@"max":@"200",@"current":@"0"};
        NSDictionary *dict2 = @{@"title": @"STRENG\nTHEN",@"min":@"0",@"max":@"100",@"current":@"50"};
        NSDictionary *dict3 = @{@"title": @"WET\nLEVEL",@"min":@"-20",@"max":@"10",@"current":@"50"};
        NSDictionary *dict4 = @{@"title": @"DRY\nLEVEL",@"min":@"-20",@"max":@"10",@"current":@"50"};
        _reverberationArray = [NSArray arrayWithObjects:dict0, dict1, dict2, dict3, dict4, nil];
    }
    
    return _reverberationArray;
}

- (NSArray *)reverbOfTypeArray {
    if (!_reverbOfTypeArray) {
        _reverbOfTypeArray = @[@2, @3, @4, @1, @0];
    }
    
    return _reverbOfTypeArray;
}

- (NSArray *)reverberationValueArray {
    if (!_reverberationValueArray) {
        _reverberationValueArray = @[@[@80, @76, @64, @1, @-1], // Popular
                                     @[@80, @76, @44, @1, @-1], // R&B
                                     @[@95, @170, @58, @-7, @-7], // Rock
                                     @[@96, @24, @70, @-4, @-1], // HipHop
                                     @[@100, @26, @82, @3, @-1], // Ethereal
                                     @[@48, @57, @68, @2, @1], // VocalConcert
                                     @[@62, @76, @69, @-1, @-1], // KTV
                                     @[@26, @0, @50, @0, @4], // Studio
                                    ];
    }
    return _reverberationValueArray;
}

- (NSArray *)effectsArray {
    if (!_effectsArray) {
        _effectsArray = [NSArray arrayWithObjects:@"流行", @"R&B",@"摇滚",@"嘻哈" ,@"演唱会",@"KTV",@"录音棚", nil];
    }
    
    return _effectsArray;
}

@end

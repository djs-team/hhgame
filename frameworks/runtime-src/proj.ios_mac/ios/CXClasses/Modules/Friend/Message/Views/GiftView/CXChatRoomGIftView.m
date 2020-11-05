//
//  CXChatRoomGIftView.m
//  hairBall
//
//  Created by mahong yang on 2019/10/31.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXChatRoomGIftView.h"
#import "HLHorizontalPageLayout.h"
#import "CXChatRoomGiftCell.h"

@interface CXChatRoomGIftView() <UICollectionViewDelegate,UICollectionViewDataSource>

@property (strong, nonatomic) UICollectionView  *collectionView;
@property (weak, nonatomic) IBOutlet UIButton *sendButton;

@property (nonatomic, strong) NSArray *dataSources;
@property (nonatomic, strong) CXLiveRoomGiftModel *giftModel;

@end

@implementation CXChatRoomGIftView

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
    _sendButton.layer.cornerRadius = 15;
    
    [self addSubview:self.collectionView];
    
//    _bottomView.frame = CGRectMake(0, 0, SCREEN_WIDTH, 50);
//    [self addSubview:_bottomView];
    
    _viewHeight = 274;
}

- (void)reloadGiftData:(NSArray *)dataSource {
    self.dataSources = dataSource;
    [self.collectionView reloadData];
}

#pragma mark - UICollectionViewDataSource
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.dataSources.count;
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXChatRoomGiftCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXChatRoomGiftCellID" forIndexPath:indexPath];
    kWeakSelf
    
    CXLiveRoomGiftModel *model = self.dataSources[indexPath.row];
    cell.model = model;
    if ([model.gift_id integerValue] == [self.giftModel.gift_id integerValue]) {
        cell.isSelected = YES;
    } else {
        cell.isSelected = NO;
    }
    
    cell.didSeletedBlock = ^(void) {
        weakSelf.giftModel = model;
        [weakSelf.collectionView reloadData];
//        [weakSelf sendGiftAtItem:indexPath.row];
    };
    return cell;
}

#pragma makr - Action

- (IBAction)rechargeAction:(id)sender {
    if (self.rechargeAction) {
        self.rechargeAction();
    }
}

- (IBAction)sendAction:(id)sender {
    if (self.giftModel && self.sendGiftAction) {
        self.sendGiftAction(self.giftModel);
    }
}

#pragma mark - Getter
- (UICollectionView *)collectionView {
    if (_collectionView == nil) {
        
        CGFloat width = SCREEN_WIDTH;
        NSInteger col = 4; // 列数
        
        HLHorizontalPageLayout *layout = [[HLHorizontalPageLayout alloc] init];
        layout.sectionInset = UIEdgeInsetsMake(0, 0, 0, 0);
        layout.minimumInteritemSpacing = 0;
        layout.minimumLineSpacing = 0;
        // item宽
        CGFloat itemWidth = width / col;
        layout.itemSize = CGSizeMake( itemWidth, itemWidth);
        
        _collectionView = [[UICollectionView alloc] initWithFrame:CGRectMake(0, 10, width, itemWidth * 2 + 10 ) collectionViewLayout:layout];
        _collectionView.dataSource = self;
        _collectionView.delegate = self;
        _collectionView.pagingEnabled = YES;
        _collectionView.showsVerticalScrollIndicator = NO;
        _collectionView.showsHorizontalScrollIndicator = NO;
        [_collectionView registerNib:[UINib nibWithNibName:@"CXChatRoomGiftCell" bundle:nil] forCellWithReuseIdentifier:@"CXChatRoomGiftCellID"];
        _collectionView.backgroundColor = UIColorHex(0xFFFFFF);
    }
    return _collectionView;
}

@end

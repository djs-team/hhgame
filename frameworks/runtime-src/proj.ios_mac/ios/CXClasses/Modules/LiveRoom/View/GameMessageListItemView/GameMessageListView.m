//
//  GameMessageListView.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/14.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameMessageListView.h"
#import "GameMessageListItemUndefineView.h"
#import "GameMessageListItemUserJoinRoomView.h"
#import "GameMessageListItemGiftEventView.h"
#import "GameMessageListItemUserSitdownView.h"
#import "GameMessageListItemTextMessageView.h" // 文本消息
#import "GameMessageListItemTextWelcomeView.h" // 欢迎语
#import "SocketMessageLuckyDrawResult.h"
#import "SocketMessageLuckyDrawResultView.h"
#import "GameMessageListItemDeported.h"
#import "CXGameMessageShareHelpTextMessageView.h"

@interface GameMessageListView () <UICollectionViewDelegate, UICollectionViewDataSource>

@property NSMutableArray * data;
@property NSMutableArray * dataSize;
@property NSMutableDictionary<NSString*, GameMessageListItemView*> * sizeTestCell;
@property (nonatomic) YYTimer * updateTimer;
@property NSMutableArray * waittingAppend;
@property BOOL userStopAtBottom;
@property BOOL isUserDragging;

@end

@implementation GameMessageListView

- (void)dealloc {
    [self.updateTimer invalidate];
    self.updateTimer = nil;
}

- (instancetype)init {
    CGRect frame = CGRectMake(0,0,0,0);
    UICollectionViewFlowLayout * layout = [UICollectionViewFlowLayout new];
    return [self initWithFrame:frame collectionViewLayout:layout];
}

- (instancetype)initWithFrame:(CGRect)frame collectionViewLayout:(UICollectionViewLayout *)layout {
    if (self = [super initWithFrame:frame collectionViewLayout:layout]) {
        _data = [NSMutableArray new];
        _dataSize = [NSMutableArray new];
        _waittingAppend = [NSMutableArray new];
        _sizeTestCell = [NSMutableDictionary new];
        _userStopAtBottom = YES;
        _isUserDragging = NO;
        self.alwaysBounceVertical = YES;
        self.showsVerticalScrollIndicator = false;
        self.backgroundColor = [UIColor clearColor];
        [self registerClass:GameMessageListItemUndefineView.class forCellWithReuseIdentifier:GameMessageListItemUndefineView.className];
        [self registerClass:GameMessageListItemUserJoinRoomView.class forCellWithReuseIdentifier:SocketMessageUserJoinRoom.className];
        [self registerClass:GameMessageListItemGiftEventView.class forCellWithReuseIdentifier:SocketMessageGiftEvent.className];
        [self registerClass:GameMessageListItemUserSitdownView.class forCellWithReuseIdentifier:SocketMessageUserSitdown.className];
        [self registerClass:GameMessageListItemTextMessageView.class forCellWithReuseIdentifier:SocketMessageTextMessage.className];
        [self registerClass:GameMessageListItemTextWelcomeView.class forCellWithReuseIdentifier:GameMessageTextWelcomeModel.className];
        [self registerClass:SocketMessageLuckyDrawResultView.class forCellWithReuseIdentifier:SocketMessageLuckyDrawResult.className];
        [self registerClass:CXGameMessageShareHelpTextMessageView.class forCellWithReuseIdentifier:CXSocketMessageSystemShareHelpNotification.className];
        
        [self registerClass:GameMessageListItemDeported.class forCellWithReuseIdentifier:SocketMessageDeported.className];
        
        self.delegate = self;
        self.dataSource = self;
        
        [self.updateTimer fire];
    }
    return self;
}

- (YYTimer *)updateTimer {
    if (!_updateTimer) {
        _updateTimer = [[YYTimer alloc] initWithFireTime:0 interval:1 target:self selector:@selector(actionUpdate) repeats:YES];
    }
    
    return _updateTimer;
}
//
//- (void)didMoveToWindow {
//    [super didMoveToWindow];
//    if (self.window) {
//        [self.updateTimer fire];
//    } else {
//        [self.updateTimer invalidate];
//        self.updateTimer = nil;
//    }
//}

- (void)actionUpdate {
    if (_waittingAppend.count) {
        NSArray * insertDatas = [_waittingAppend copy];
        NSMutableArray * insertDataSizes = [NSMutableArray new];
        [_waittingAppend removeAllObjects];
        
        NSMutableArray * insertIndexPaths = [NSMutableArray new];
        [insertDatas enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            [insertIndexPaths addObject:[NSIndexPath indexPathForRow:self.data.count+idx inSection:0]];
            [insertDataSizes addObject:[NSNull null]];
        }];
        [self.data addObjectsFromArray:insertDatas];
        [self.dataSize addObjectsFromArray:insertDataSizes];
        
        if (insertDataSizes.count == 0 ) //要插入数据的数组
        {
            [self reloadData];
        } else {
            [self performBatchUpdates:^{
                [self insertItemsAtIndexPaths:insertIndexPaths];
            } completion:NULL];
        }
        
        if (self.data.count > 2000) {
            NSRange removeRange = NSMakeRange(0, self.data.count - 500);
            [self.data removeObjectsInRange:removeRange];
            [self.dataSize removeObjectsInRange:removeRange];
            [self reloadSections:[NSIndexSet indexSetWithIndex:0]];
        }

        if (!self.isUserDragging && self.userStopAtBottom) {
            [self scrollToItemAtIndexPath:[NSIndexPath indexPathForRow:self.data.count-1 inSection:0] atScrollPosition:UICollectionViewScrollPositionBottom animated:YES];
        }
    }
}

- (void)registerClass:(Class)cellClass forCellWithReuseIdentifier:(NSString *)identifier {
    [super registerClass:cellClass forCellWithReuseIdentifier:identifier];
    [_sizeTestCell setObject:[cellClass new] forKey:identifier];
}

- (void)clearModel {
    _data = [NSMutableArray new];
    _dataSize = [NSMutableArray new];
    [self reloadSections:[NSIndexSet indexSetWithIndex:0]];
}

- (void)addModel:(id)model {
    [self.waittingAppend addObject:model];
}

#pragma mark - UICollectionViewDataSource

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return _data.count;
}

- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    
    GameMessageListItemView * cell;
    
    id model = _data[indexPath.row];
    if ([model isMemberOfClass:SocketMessageUserJoinRoom.class]) {
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:SocketMessageUserJoinRoom.className forIndexPath:indexPath];
    }
//    else if ([model isMemberOfClass:GameMessageTextAmmouncementModel.class]) {
//        
//        cell = [collectionView dequeueReusableCellWithReuseIdentifier:GameMessageTextAmmouncementModel.className forIndexPath:indexPath];
//    }
    else if ([model isMemberOfClass:SocketMessageGiftEvent.class]) {
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:SocketMessageGiftEvent.className forIndexPath:indexPath];
    }
    else if ([model isMemberOfClass:SocketMessageUserSitdown.class]) {
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:SocketMessageUserSitdown.className forIndexPath:indexPath];
    }
    else if ([model isMemberOfClass:SocketMessageTextMessage.class]) {
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:SocketMessageTextMessage.className forIndexPath:indexPath];
    }
//    else if ([model isMemberOfClass:SocketMessageFaceMessage.class]) {
//        cell = [collectionView dequeueReusableCellWithReuseIdentifier:SocketMessageFaceMessage.className forIndexPath:indexPath];
//    }
    else if ([model isMemberOfClass:GameMessageTextWelcomeModel.class]) {
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:GameMessageTextWelcomeModel.className forIndexPath:indexPath];
    }
    else if ([model isMemberOfClass:SocketMessageLuckyDrawResult.class]) {
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:SocketMessageLuckyDrawResult.className forIndexPath:indexPath];
    }
    else if ([model isMemberOfClass:SocketMessageDeported.class]){
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:SocketMessageDeported.className forIndexPath:indexPath];
    }
    else if ([model isMemberOfClass:CXSocketMessageSystemShareHelpNotification.class]){
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:CXSocketMessageSystemShareHelpNotification.className forIndexPath:indexPath];
    }
    else {
        cell = [collectionView dequeueReusableCellWithReuseIdentifier:GameMessageListItemUndefineView.className forIndexPath:indexPath];
    }
    cell.model = model;
    
    __weak typeof(self) weakSelf = self;
    cell.clickUserInfo = ^(NSString * _Nonnull userID) {
        if (weakSelf.clickUserInfoBlock && userID && userID.length) {
            weakSelf.clickUserInfoBlock(userID);
        }
    };
    
    //添加长按手势
    UILongPressGestureRecognizer *longPressGesture =[[UILongPressGestureRecognizer alloc]initWithTarget:self action:@selector(cellLongPress:)];
//    longPressGesture.minimumPressDuration=1.5f;//设置长按 时间
    [cell addGestureRecognizer:longPressGesture];

    return cell;
}

//#pragma mark  实现成为第一响应者方法
//- (BOOL)canBecomeFirstResponder{
//    return YES;
//}

- (void)cellLongPress:(UILongPressGestureRecognizer *)longRecognizer{
    if (longRecognizer.state == UIGestureRecognizerStateBegan) {
        //成为第一响应者，需重写该方法
//        [self becomeFirstResponder];
        
        CGPoint location = [longRecognizer locationInView:self];
        NSIndexPath *indexPath = [self indexPathForItemAtPoint:location];
        id model = _data[indexPath.row];
        if (self.longPressUserInfoBlock) {
            if ([model isMemberOfClass:SocketMessageUserJoinRoom.class]) {
                SocketMessageUserJoinRoom *message = model;
                if (![message.UserId isEqualToString:[CXClientModel instance].userId]) {
                    self.longPressUserInfoBlock(message.Name);
                }
                
            }
//            else if ([model isMemberOfClass:GameMessageTextAmmouncementModel.class]) {
//                // 公告
//                NSLog(@"");
//            }
            else if ([model isMemberOfClass:SocketMessageGiftEvent.class]) {
                SocketMessageGiftEvent *message = model;
                if (![[message.GiftGiver.UserId stringValue] isEqualToString:[CXClientModel instance].userId]) {
                    self.longPressUserInfoBlock(message.GiftGiver.Name);
                }
            }
            else if ([model isMemberOfClass:SocketMessageUserSitdown.class]) {
                SocketMessageUserSitdown *message = model;
                if (![message.MicroInfo.User.UserId isEqualToString:[CXClientModel instance].userId]) {
                    self.longPressUserInfoBlock(message.MicroInfo.User.Name);
                }
            }
            else if ([model isMemberOfClass:SocketMessageTextMessage.class]) {
                SocketMessageTextMessage *message = model;
                if (![message.JoinRoomUser.UserId isEqualToString:[CXClientModel instance].userId]) {
                    self.longPressUserInfoBlock(message.JoinRoomUser.Name);
                }
            }
//            else if ([model isMemberOfClass:SocketMessageFaceMessage.class]) {
//                SocketMessageTextMessage *message = model;
//                if (![message.JoinRoomUser.UserId isEqualToString:[CXClientModel instance].userId]) {
//                    self.longPressUserInfoBlock(message.JoinRoomUser.Name);
//                }
//            }
            else if ([model isMemberOfClass:GameMessageTextWelcomeModel.class]) {
                NSLog(@"");
            }
            else if ([model isMemberOfClass:SocketMessageLuckyDrawResult.class]) {
                SocketMessageLuckyDrawResult *message = model;
                if (![message.UserId isEqualToString:[CXClientModel instance].userId]) {
                    self.longPressUserInfoBlock(message.UserNickName);
                }
            }
            else if ([model isMemberOfClass:SocketMessageDeported.class]){
                SocketMessageDeported *message = model;
                if (![[message.UserId stringValue] isEqualToString:[CXClientModel instance].userId]) {
                    self.longPressUserInfoBlock(message.NickName);
                }
            }
            
        }
    }
}

#pragma mark - UIScrollViewDelegate

- (void)scrollViewWillBeginDragging:(UIScrollView *)scrollView {
    _isUserDragging = YES;
}

- (void)scrollViewWillEndDragging:(UIScrollView *)scrollView withVelocity:(CGPoint)velocity targetContentOffset:(inout CGPoint *)targetContentOffset {
    if (scrollView.contentSize.height < scrollView.bounds.size.height
        || targetContentOffset->y + 15 + scrollView.bounds.size.height > scrollView.contentSize.height) {
        _userStopAtBottom = YES;
    }
    else {
        _userStopAtBottom = NO;
    }
}

- (void)scrollViewDidEndDragging:(UIScrollView *)scrollView willDecelerate:(BOOL)decelerate {
    _isUserDragging = NO;
}

- (BOOL)scrollViewShouldScrollToTop:(UIScrollView *)scrollView {
    return NO;
}

#pragma mark - UICollectionViewDelegate

- (BOOL)collectionView:(UICollectionView *)collectionView shouldHighlightItemAtIndexPath:(NSIndexPath *)indexPath {
    return NO;
}

#pragma mark - UICollectionViewDelegateFlowLayout

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    NSValue * value = _dataSize[indexPath.row];
    if ([value isKindOfClass:NSValue.class]) {
        return value.CGSizeValue;
    }
    CGSize ret = CGSizeMake(collectionView.bounds.size.width, 0);
    id model = _data[indexPath.row];
    GameMessageListItemView * testCell = [_sizeTestCell objectForKey:NSStringFromClass([model class])];
    if (testCell) {
        testCell.model = model;
        ret = [testCell sizeThatFits:CGSizeMake(collectionView.bounds.size.width, CGFLOAT_MAX)];
    } else {
        GameMessageListItemView * testCell = [_sizeTestCell objectForKey:GameMessageListItemUndefineView.className];
        testCell.model = model;
        CGSize contentSize = [testCell sizeThatFits:CGSizeMake(collectionView.bounds.size.width, CGFLOAT_MAX)];
        ret = CGSizeMake(contentSize.width, contentSize.height);
    }
    _dataSize[indexPath.row] = [NSValue valueWithCGSize:ret];
    return ret;
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout insetForSectionAtIndex:(NSInteger)section {
    return UIEdgeInsetsZero;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section {
    return 5;
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section {
    return 0;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout referenceSizeForHeaderInSection:(NSInteger)section {
    return CGSizeZero;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout referenceSizeForFooterInSection:(NSInteger)section {
    return CGSizeZero;
}

@end

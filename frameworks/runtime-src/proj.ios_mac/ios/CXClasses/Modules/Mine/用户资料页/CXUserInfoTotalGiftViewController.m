//
//  CXUserInfoTotalGiftViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/17.
//

#import "CXUserInfoTotalGiftViewController.h"
#import "CXUserInfoGiftCell.h"

@interface CXUserInfoTotalGiftViewController () <UICollectionViewDelegate,UICollectionViewDataSource>

@property (nonatomic, strong) UICollectionView * giftDetailCollection;

@property (nonatomic, strong) NSArray *giftArrays;
@end

@implementation CXUserInfoTotalGiftViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.title =@"累计收到礼物";
    
    UICollectionViewFlowLayout * layout = [UICollectionViewFlowLayout new];
    _giftDetailCollection =[[UICollectionView alloc] initWithFrame:CGRectZero collectionViewLayout:layout];
    [_giftDetailCollection registerNib:[UINib nibWithNibName:@"CXUserInfoGiftCell" bundle:nil] forCellWithReuseIdentifier:@"CXUserInfoGiftCellID"];
    _giftDetailCollection.delegate=self;
    _giftDetailCollection.dataSource=self;
    [self.view addSubview:_giftDetailCollection];
    
    [_giftDetailCollection mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.bottom.right.equalTo(self.view);
    }];
    
    [self requestData];
}

- (void)requestData {
    NSString *signature = [CocoaSecurity md5:[CXClientModel instance].token].hexLower;
    kWeakSelf
    [CXHTTPRequest POSTWithURL:@"/index.php/api/gift/giftranklist" parameters:@{@"signature": signature, @"touid":_user_Id} callback:^(id responseObject, BOOL isCache, NSError *error) {
       if (!error) {
           weakSelf.giftArrays = [NSArray modelArrayWithClass:[CXFriendGiftModel class] json:responseObject[@"data"]];
           [weakSelf.giftDetailCollection reloadData];
       }
    }];
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return _giftArrays.count;
}
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{

    CXUserInfoGiftCell * gifCell =[collectionView dequeueReusableCellWithReuseIdentifier:@"CXUserInfoGiftCellID" forIndexPath:indexPath];
    CXFriendGiftModel * model=_giftArrays[indexPath.item];
    gifCell.model = model;
    return gifCell;
}
#pragma mark  UIEdgeInsetsMake
- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    
  return UIEdgeInsetsMake(10, 10, 10, 10);

}
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{

    return CGSizeMake((SCREEN_WIDTH/4)-20, 100);

}

@end

//
//  CXPageViewController.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/11.
//

#import "CXPageViewController.h"
#import "CXPageViewControllerTitleItemCell.h"

@interface CXPageViewController ()<UIPageViewControllerDelegate,UIPageViewControllerDataSource,UICollectionViewDelegate,UICollectionViewDataSource>
{
    NSInteger _currentIndex;
}

@property (nonatomic, strong) UIPageViewController *pageViewController;
@property (nonatomic, strong) UICollectionView *titleCollectionView;
@end

@implementation CXPageViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.view.backgroundColor = [UIColor whiteColor];
    _currentIndex = 0;
    
    self.dataSource = self;
    
    [self createPageViewController];
    [self createCollectionView];
}

- (void)reloadData {
    [self.titleCollectionView reloadData];
}

#pragma mark - UIPageViewControllerDelegate,UIPageViewControllerDataSourc
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    return self.titles.count;
}
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    CXPageViewControllerTitleItemCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"CXPageViewControllerTitleItemCellID" forIndexPath:indexPath];
    NSDictionary *item = self.titles[indexPath.row];
    cell.titleLabel.text = item[@"title"];
    if (indexPath.row == _currentIndex) {
        cell.titleLabel.textColor = UIColorHex(0x773BE7);
        cell.indicatorView.hidden = NO;
    } else {
        cell.titleLabel.textColor = UIColorHex(0x3B3B3B);
        cell.indicatorView.hidden = YES;
    }
    if ([item[@"count"] integerValue] <= 0) {
        cell.countLabel.hidden = YES;
    } else {
        cell.countLabel.hidden = NO;
        cell.countLabel.text = item[@"count"];
    }
    return cell;
}
- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    UIViewController *vc = [self.viewControllerClasses objectAtIndex:indexPath.row];
    if (indexPath.row > _currentIndex) {
        [self.pageViewController setViewControllers:@[vc] direction:UIPageViewControllerNavigationDirectionForward animated:YES completion:^(BOOL finished) {
        }];
    } else {
        [self.pageViewController setViewControllers:@[vc] direction:UIPageViewControllerNavigationDirectionReverse animated:YES completion:^(BOOL finished) {
        }];
    }
    _currentIndex = indexPath.row;
    NSIndexPath *path = [NSIndexPath indexPathForRow:_currentIndex inSection:0];
    [self.titleCollectionView scrollToItemAtIndexPath:path atScrollPosition:UICollectionViewScrollPositionCenteredHorizontally animated:YES];
    [self.titleCollectionView reloadData];
}

/// 创建标题collectionview
- (void)createCollectionView{
    UICollectionViewFlowLayout *lay = [[UICollectionViewFlowLayout alloc] init];
    lay.itemSize = CGSizeMake(_itemWidth, 44);
    lay.minimumLineSpacing = 30;
    lay.minimumInteritemSpacing = 0;
    lay.scrollDirection = UICollectionViewScrollDirectionHorizontal;
//    self.titleCollectionView = [[UICollectionView alloc] initWithFrame:CGRectMake((SCREEN_WIDTH - (_itemWidth * _titles.count + (_titles.count - 1)*30))/2, kStatusHeight, _itemWidth * _titles.count + (_titles.count - 1)*30, 44) collectionViewLayout:lay];
    self.titleCollectionView = [[UICollectionView alloc] initWithFrame:[self.dataSource pageControllerMenuViewRect] collectionViewLayout:lay];
    self.titleCollectionView.showsHorizontalScrollIndicator = NO;
    self.titleCollectionView.backgroundColor = [UIColor whiteColor];
    self.titleCollectionView.delegate = self;
    self.titleCollectionView.dataSource = self;
    [self.titleCollectionView registerNib:[UINib nibWithNibName:@"CXPageViewControllerTitleItemCell" bundle:nil] forCellWithReuseIdentifier:@"CXPageViewControllerTitleItemCellID"];
    [self.view addSubview:self.titleCollectionView];
    
    UIView *line = [[UIView alloc] initWithFrame:CGRectZero];
    line.backgroundColor = UIColorHex(0xBBBBBB);
    [self.view addSubview:line];
    [line mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.mas_equalTo(0);
        make.height.mas_equalTo(1);
        make.top.equalTo(self.titleCollectionView.mas_bottom).offset(-1);
    }];
}
/// 创建pageViewController
- (void)createPageViewController {
    NSDictionary *option = [NSDictionary dictionaryWithObject:[NSNumber numberWithInteger:0] forKey:UIPageViewControllerOptionInterPageSpacingKey];
    _pageViewController = [[UIPageViewController alloc]initWithTransitionStyle:UIPageViewControllerTransitionStyleScroll navigationOrientation:UIPageViewControllerNavigationOrientationHorizontal options:option];
    _pageViewController.delegate = self;
    _pageViewController.dataSource = self;
    [self addChildViewController:_pageViewController];
    [self.view addSubview:_pageViewController.view];
    _pageViewController.view.frame = [self.dataSource pageControllerContentViewRect];
}
#pragma mark - UICollectionViewDelegate,UICollectionViewDataSource
/// 展示上一页
- (nullable UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerBeforeViewController:(UIViewController *)viewController {
    NSInteger index = [self.viewControllerClasses indexOfObject:viewController];
    if (index == 0 || (index == NSNotFound)) {
        return nil;
    }
    index--;
    return [self.viewControllerClasses objectAtIndex:index];
}
/// 展示下一页
- (nullable UIViewController *)pageViewController:(UIPageViewController *)pageViewController viewControllerAfterViewController:(UIViewController *)viewController {
    NSInteger index = [self.viewControllerClasses indexOfObject:viewController];
    if (index == self.viewControllerClasses.count - 1 || (index == NSNotFound)) {
        return nil;
    }
    index++;
    return [self.viewControllerClasses objectAtIndex:index];
}
/// 将要滑动切换的时候
- (void)pageViewController:(UIPageViewController *)pageViewController willTransitionToViewControllers:(NSArray<UIViewController *> *)pendingViewControllers {
    UIViewController *nextVC = [pendingViewControllers firstObject];
    NSInteger index = [self.viewControllerClasses indexOfObject:nextVC];
    _currentIndex = index;
}
/// 滑动结束后
- (void)pageViewController:(UIPageViewController *)pageViewController didFinishAnimating:(BOOL)finished previousViewControllers:(NSArray<UIViewController *> *)previousViewControllers transitionCompleted:(BOOL)completed {
    if (completed) {
        NSIndexPath *path = [NSIndexPath indexPathForRow:_currentIndex inSection:0];
        [self.titleCollectionView scrollToItemAtIndexPath:path atScrollPosition:UICollectionViewScrollPositionCenteredHorizontally animated:YES];
        [self.titleCollectionView reloadData];
    }
}

- (void)viewWillLayoutSubviews {
    [super viewWillLayoutSubviews];
    
    NSAssert(self.viewControllerClasses.count > 0, @"Must have one childViewCpntroller at least");
    NSAssert(self.titles.count == self.viewControllerClasses.count, @"The childViewController's count doesn't equal to the count of titleArray");
    
    UIViewController *vc = [self.viewControllerClasses objectAtIndex:_currentIndex];
    [self.pageViewController setViewControllers:@[vc] direction:UIPageViewControllerNavigationDirectionReverse animated:YES completion:nil];
}

#pragma mark - CXPageViewControllerDataSource
- (CGRect)pageControllerMenuViewRect {
    return CGRectZero;
}
- (CGRect)pageControllerContentViewRect {
    return CGRectZero;
}

@end

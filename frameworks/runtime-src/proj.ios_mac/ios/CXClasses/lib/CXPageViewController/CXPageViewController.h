//
//  CXPageViewController.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/11.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class CXPageViewController;

@protocol CXPageViewControllerDataSource <NSObject>

@required
- (CGRect)pageControllerMenuViewRect;
- (CGRect)pageControllerContentViewRect;
@end

@interface CXPageViewController : UIViewController

@property (nonatomic, weak) id<CXPageViewControllerDataSource> dataSource;

/**
 *  各个控制器的 class, 例如:[UITableViewController class]
 *  Each controller's class, example:[UITableViewController class]
 */
@property (nonatomic, nullable, copy) NSArray<UIViewController *> *viewControllerClasses;

/**
 *  各个控制器标题
 *  Titles of view controllers in page controller.
 */
@property (nonatomic, nullable, copy) NSArray<NSDictionary *> *titles;

/**
 *  每个item的宽度（平均宽度）
 *  Titles of view controllers in page controller.
*/
@property (nonatomic, assign) CGFloat itemWidth;

- (void)reloadData;

@end

NS_ASSUME_NONNULL_END

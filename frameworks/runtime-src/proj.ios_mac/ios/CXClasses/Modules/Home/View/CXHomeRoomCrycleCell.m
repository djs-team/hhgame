//
//  CXHomeRoomCrycleCell.m
//  hairBall
//
//  Created by mahong yang on 2020/6/12.
//  Copyright © 2020 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "CXHomeRoomCrycleCell.h"
#import <SDCycleScrollView/SDCycleScrollView.h>

@interface CXHomeRoomCrycleCell() <SDCycleScrollViewDelegate>

@property (nonatomic, strong) SDCycleScrollView * bannerView;
@end


@implementation CXHomeRoomCrycleCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.bannerView = [SDCycleScrollView cycleScrollViewWithFrame:CGRectMake(0, 0, SCREEN_WIDTH - 28, 118*SCALE_W) delegate:self placeholderImage:nil];
    self.bannerView.bannerImageViewContentMode = UIViewContentModeScaleAspectFill;
    self.bannerView.delegate = self;
    [self addSubview:self.bannerView];
    
    self.bannerView.clipsToBounds = YES;
//    self.bannerView.layer.masksToBounds = YES;
//    self.bannerView.layer.cornerRadius = 8;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
}

- (void)setBannerList:(NSArray<CXHomeRoomBannerModel *> *)bannerList {
    _bannerList = bannerList;
    
    NSMutableArray * imgURLs = [NSMutableArray new];
    NSMutableArray * titlesGroup = [NSMutableArray new];
    [_bannerList enumerateObjectsUsingBlock:^(CXHomeRoomBannerModel * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        [imgURLs addObject:[NSString stringWithFormat:@"%@", obj.image]];
        [titlesGroup addObject:[NSString stringWithFormat:@"%@", obj.title]];
    }];
    self.bannerView.imageURLStringsGroup = imgURLs;
}

#pragma mark <SDCycleScrollViewDelegate>

- (void)cycleScrollView:(SDCycleScrollView *)cycleScrollView didSelectItemAtIndex:(NSInteger)index {
    if (index < self.bannerList.count) {
        CXHomeRoomBannerModel * banner = self.bannerList[index];
        if (self.didSelectedCycleUrl) {
            self.didSelectedCycleUrl(banner);
        }
    }
}


@end

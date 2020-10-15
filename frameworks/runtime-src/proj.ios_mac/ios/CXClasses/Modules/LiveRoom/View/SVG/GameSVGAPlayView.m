//
//  GameSVGAPlayView.m
//  hairBall
//
//  Created by 肖迎军 on 2019/7/18.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "GameSVGAPlayView.h"
#import <SVGAPlayer/SVGA.h>
#import "SVGAManager.h"

@interface GameSVGAPlayView () <SVGAPlayerDelegate>

@property SVGAPlayer * player;
@property (nonatomic) BOOL isPlaying;
@property NSURL * parserUrl;
@property NSMutableArray<SocketMessageGiftData*> * urlQueue;

@end


@implementation GameSVGAPlayView

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        _player = [[SVGAPlayer alloc] initWithFrame:self.bounds];
        _player.delegate = self;
        [self addSubview:_player];
        _urlQueue = [NSMutableArray new];
        self.userInteractionEnabled = NO;
    }
    return self;
}

- (void)pushSVGAURLString:(SocketMessageGiftData*)giftData {
    if (giftData) {
        
        NSString * url = [giftData.GiftAnimation containsString:@"svga"] ? giftData.GiftAnimation : giftData.Animation;
        
        NSURL * svgaURL = [NSURL URLWithString:url];
        if (svgaURL) {
            //全局缓存
            [self.urlQueue addObject:giftData];
            
            [self tryPlay];
        }
    }
}

static CGSize fillSize(CGSize frameSize, CGSize contentSize)
{
    if (frameSize.width == 0 || frameSize.height == 0 || contentSize.width == 0 || contentSize.height == 0)
        return CGSizeMake(0, 0);
    CGFloat sx = contentSize.width / frameSize.width;
    CGFloat sy = contentSize.height / frameSize.height;
    CGFloat msy = MIN(sx, sy);
    
    return CGSizeMake(contentSize.width / msy, contentSize.height / msy);
}

static CGSize fitSize(CGSize frameSize, CGSize contentSize)
{
    if (frameSize.width == 0 || frameSize.height == 0 || contentSize.width == 0 || contentSize.height == 0)
        return CGSizeMake(0, 0);
    CGFloat sx = contentSize.width / frameSize.width;
    CGFloat sy = contentSize.height / frameSize.height;
    CGFloat msy = MAX(sx, sy);
    
    return CGSizeMake(contentSize.width / msy, contentSize.height / msy);
}

- (void)play:(SVGAVideoEntity*)videoItem withType:(GiftInfoClassType)class_type {
    if (videoItem.frames) {
        self.player.videoItem = videoItem;
        self.player.loops = 1;
        switch (class_type) {
            case GiftInfoClassTypeBig: {
                CGSize size = fillSize(self.bounds.size, videoItem.videoSize);
                self.player.bounds = CGRectMake(0, 0, size.width, size.height);
            }
                break;
            case GiftInfoClassTypeSmall:
            default: {
                CGSize size = fitSize(self.bounds.size, videoItem.videoSize);
                self.player.bounds = CGRectMake(0, 0, size.width, size.height);
            }
                break;
        }
        self.player.center = CGPointMake(self.bounds.size.width/2, self.bounds.size.height/2);
        [self.player startAnimation];
    }
    else {
        self.isPlaying = NO;
        [self tryPlay];
    }
}

- (void)tryPlay {
    while (self.urlQueue.count && !self.isPlaying) {
        //取出首个任务
        
        SocketMessageGiftData * giftInfo = self.urlQueue.firstObject;
        GiftInfoClassType class_type = giftInfo.ClassType.integerValue;
        [self.urlQueue removeFirstObject];
        NSString * key = giftInfo.GiftAnimation;
        if ([[SVGAManager shared] isCached:key]) {
            __weak typeof (self) wself = self;
            self.isPlaying = YES;
            [[SVGAManager shared] load:key completionBlock:^(SVGAVideoEntity * _Nullable videoItem) {
                [wself play:videoItem withType:class_type];
            } failureBlock:^(NSError * _Nullable error) {
                self.isPlaying = NO;
                [wself tryPlay];
            }];
        } else {
            __weak typeof (self) wself = self;
            [[SVGAManager shared] cache:key completionBlock:^(BOOL isSuccess) {
                if (isSuccess) {
                    wself.isPlaying = YES;
                    [[SVGAManager shared] load:key completionBlock:^(SVGAVideoEntity * _Nullable videoItem) {
                        [wself play:videoItem withType:class_type];
                    } failureBlock:^(NSError * _Nullable error) {
                        wself.isPlaying = NO;
                        [wself tryPlay];
                    }];
                }
            }];
        }
    }
}

#pragma mark - SVGAPlayerDelegate

- (void)svgaPlayerDidFinishedAnimation:(SVGAPlayer *)player {
    self.isPlaying = NO;
    self.player.videoItem = nil;
    [self tryPlay];
    
}

@end

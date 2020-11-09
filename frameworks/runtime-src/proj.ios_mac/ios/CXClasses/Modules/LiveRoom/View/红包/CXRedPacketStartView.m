//
//  CXRedPacketStartView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/29.
//

#import "CXRedPacketStartView.h"

@interface CXRedPacketStartView ()

@property (nonatomic, strong) NSTimer *timer;

@property (nonatomic, strong) CALayer *moveLayer;

@property (nonatomic, assign) BOOL isClick;

@end

@implementation CXRedPacketStartView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(kScreenWidth, kScreenHeight));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = NO;
    self.type = MMPopupTypeAlert;
}

- (void)show {
    [super show];
    
    [self.timer invalidate];
    
    self.timer = [NSTimer scheduledTimerWithTimeInterval:0.5 target:self selector:@selector(showRain) userInfo:nil repeats:YES];
    
    self.isClick = NO;
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        [self hide];
    });
}

- (void)showRain {
    UIImageView *imageV = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"liveroom_redpacket"]];
    imageV.frame = CGRectMake(0, 0, 65, 70);
    
    self.moveLayer = [[CALayer alloc] init];
    self.moveLayer.bounds = imageV.frame;
    self.moveLayer.anchorPoint = CGPointMake(0, 0);
    self.moveLayer.position = CGPointMake(0, -70);
    self.moveLayer.contents = (__bridge id _Nullable)(imageV.image.CGImage);
    
    [self.layer addSublayer:self.moveLayer];
    
    [self addAnimation];
}

- (void)addAnimation {
    CAKeyframeAnimation *moveAnimation = [[CAKeyframeAnimation alloc] init];
    moveAnimation.keyPath = @"position";
    moveAnimation.values = @[[NSValue valueWithCGPoint:CGPointMake(arc4random()%320, 10)],
    [NSValue valueWithCGPoint:CGPointMake(arc4random()%320, kScreenHeight - 200)]];
    moveAnimation.duration = 5;
    moveAnimation.repeatCount = 1;
    [self.moveLayer addAnimation:moveAnimation forKey:@"move"];
}

- (void)hide {
    [super hide];
    
    [self.timer invalidate];
    
    for (CALayer *item in self.layer.sublayers) {
        [item removeAllAnimations];
    }
    
    [MMPopupView hideAll];
}

- (IBAction)withdrawAction:(id)sender {
    if (self.isClick) {
        return;
    }
    self.isClick = YES;
    CXSocketMessageRobRedPacket *request = [CXSocketMessageRobRedPacket new];
    [[CXClientModel instance] sendSocketRequest:request withCallback:nil];
}

@end

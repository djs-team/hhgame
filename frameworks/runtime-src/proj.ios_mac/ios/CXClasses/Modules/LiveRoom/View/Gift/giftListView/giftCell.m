//
//  giftCell.m
//  hairBall
//
//  Created by ashuan on 2019/4/29.
//  Copyright © 2019 Hunan Nano Entertainment Network Technology Co., Ltd. All rights reserved.
//

#import "giftCell.h"
@interface giftCell()
@property (nonatomic,strong)NSMutableArray *btnsArray;
@property (nonatomic,strong)NSMutableArray *currentDataArray;
@end
@implementation giftCell

-(instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
    }
    return self;
}
-(void)btnClick:(UIButton*)btn{
    voiceGiftModel *m = self.currentDataArray[btn.tag];
    // [self.deleagte clickSendGiftBtn:m];
    if (self.clickBlock) {
        self.clickBlock(m);
    }
}
-(void)setGiftCell:(NSMutableArray*)dataArray{
    self.currentDataArray = dataArray;
    for (int i=0; i<self.btnsArray.count; i++) {
        giftIconBtn *btn = self.btnsArray[i];
        btn.hidden = YES;
    }
    for (int i=0; i<dataArray.count; i++) {
        giftIconBtn *btn = self.btnsArray[i];
        btn.hidden = NO;
        voiceGiftModel *m = dataArray[i];
        [btn setGiftIconBtnModel:m];
    }
}
-(void)setUI{
    [self.btnsArray removeAllObjects];
    for (int i=0; i<8; i++) {
        giftIconBtn *btn = [[giftIconBtn alloc]initWithFrame:CGRectMake(i%4*(SCREEN_WIDTH/4), i/4*106, SCREEN_WIDTH/4, 106)];
        btn.tag = i;
        btn.hidden = YES;
        [btn addTarget:self action:@selector(btnClick:) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:btn];
        [self.btnsArray addObject:btn];
    }
}
-(NSMutableArray*)btnsArray{
    if (!_btnsArray) {
        _btnsArray = [[NSMutableArray alloc]init];
    }
    return _btnsArray;
}
@end

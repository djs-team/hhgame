//
//  CXRedPacketProgressView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import "CXRedPacketProgressView.h"

@implementation CXRedPacketProgressView

- (IBAction)redpacketRegularAction:(id)sender {
    if (self.redpacketProgressRegularBlock) {
        self.redpacketProgressRegularBlock();
    }
}

- (IBAction)hideAction:(id)sender {
    if (self.redpacketHideBlock) {
        self.redpacketHideBlock();
    }
}

@end

//
//  CXRedPacketRegularView.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/26.
//

#import "CXRedPacketRegularView.h"

@interface CXRedPacketRegularView ()
@property (nonatomic, strong) NSString *regularStr;
@property (weak, nonatomic) IBOutlet UITextView *regularTextView;
@end

@implementation CXRedPacketRegularView

- (void)awakeFromNib {
    [super awakeFromNib];
    
    [self mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(318, 551));
    }];
         
    [MMPopupWindow sharedWindow].touchWildToHide = YES;
    self.type = MMPopupTypeAlert;
    
    self.layer.masksToBounds = YES;
    self.layer.cornerRadius = 10;
    
    [self getRegular];
}

- (IBAction)closeAction:(id)sender {
    
    [self hide];
}

- (void)getRegular {
    CXSocketMessageGetRedPacketPlayDesc *request = [CXSocketMessageGetRedPacketPlayDesc new];
    kWeakSelf
    [[CXClientModel instance] sendSocketRequest:request withCallback:^(__kindof CXSocketMessageGetRedPacketPlayDesc * _Nonnull request) {
        if (request.response.isSuccess) {
            NSMutableAttributedString *attributedString = [[NSMutableAttributedString alloc] initWithData:[request.response.PlayingDesc dataUsingEncoding:NSUnicodeStringEncoding] options:@{ NSDocumentTypeDocumentAttribute: NSHTMLTextDocumentType } documentAttributes:nil error:nil];
            UIFont *boldFont = [UIFont boldSystemFontOfSize:16];
            [attributedString addAttribute:NSFontAttributeName value:boldFont range:NSMakeRange(0, attributedString.length)];
            weakSelf.regularTextView.attributedText = attributedString;
        }
    }];
}


@end

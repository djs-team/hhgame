//
//  CXLiveRoomModel.m
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/1.
//

#import "CXLiveRoomModel.h"

static const int space = 8;
static const int leading = 12;

@implementation CXLiveRoomRecommendModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"room_id" : @"id",
             };
}

@end

@implementation CXLiveRoomDataModel

+ (NSDictionary *)modelCustomPropertyMapper {
    return @{
             @"VisitorNumbers" : @[@"VisitorNumbers", @"VisitorNum"],
             };
}

- (CGFloat)padding {
    return 8;
}

- (CGFloat)seatsSizeHeight {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return SCREEN_HEIGHT;
            break;
        case 2:
            return 343*SCALE_W;
            break;
        case 3:
            return ((SCREEN_WIDTH-space-leading*2)/2)*2+space;
            break;
        default:
            return 343*SCALE_W;
            break;
    }
}

- (CGFloat)music_seatsSizeHeight {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return SCREEN_HEIGHT;
            break;
        default:
            return 343*SCALE_W;
            break;
    }
}

- (CGSize)hostSize {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
            break;
        case 2:
            return CGSizeMake((SCREEN_WIDTH-space)/2, 343*SCALE_W);
            break;
        case 3:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
        default:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
    }
}

- (CGSize)manSize {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
            break;
        case 2:
            return CGSizeMake((SCREEN_WIDTH-space)/2, 343*SCALE_W);
            break;
        case 3:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
        default:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
    }
}

- (CGSize)womanSize {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
            break;
        case 2:
            return CGSizeMake((SCREEN_WIDTH-space)/2, 343*SCALE_W);
            break;
        case 3:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
        default:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
    }
}

- (CGSize)normalSize {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
            break;
        case 2:
            return CGSizeMake((SCREEN_WIDTH-space)/2, 343*SCALE_W);
            break;
        case 3:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
        default:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
    }
}

- (CGSize)sofaSize {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
            break;
        case 2:
            return CGSizeMake((SCREEN_WIDTH-space)/2, 343*SCALE_W);
            break;
        case 3:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
        default:
            return CGSizeMake((SCREEN_WIDTH-space-leading*2)/2, (SCREEN_WIDTH-space-leading*2)/2);
            break;
    }
}

- (CGSize)music_songerSize {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
            break;
        default:
            return CGSizeMake((SCREEN_WIDTH-space)/2, 343*SCALE_W);
            break;
    }
}

- (CGSize)music_normalSize {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return CGSizeMake(SCREEN_WIDTH, SCREEN_HEIGHT);
            break;
        case 2:
            return CGSizeMake((SCREEN_WIDTH-space)/2, 343*SCALE_W);
            break;
        case 3:
            return CGSizeMake((SCREEN_WIDTH-space)/2, (343*SCALE_W - space)/2);
            break;
        default:
            return CGSizeMake((SCREEN_WIDTH-space)/2, (343*SCALE_W - space)/2);
            break;
    }
}

- (CGFloat)regular_top {
    switch ([CXClientModel instance].room.seats.count) {
        case 1:
            return kNavHeight + [CXClientModel instance].room.horseLampHeight+343*SCALE_W+space;
            break;
        case 2:
            return kNavHeight + [CXClientModel instance].room.horseLampHeight+343*SCALE_W+space;
            break;
        case 3:
            return kNavHeight + [CXClientModel instance].room.horseLampHeight+((SCREEN_WIDTH-space-leading*2)/2)*2+space+6;
            break;
        default:
            return kNavHeight + [CXClientModel instance].room.horseLampHeight+343*SCALE_W+space;
            break;
    }
}

@end

@implementation LiveRoomMicroInfo

- (NSIndexPath*)indexPath {
    return [NSIndexPath indexPathForRow:_Number inSection:_Type];
}

+ (NSArray *)modelPropertyBlacklist {
    return @[@"modelUser"];
}

@end

@implementation LiveRoomUser

+ (NSArray *)modelPropertyBlacklist {
    return @[@"modelSeat"];
}

- (NSString*)displayId {
    NSString * display = self.PrettyId;
    if (!display) {
        display = self.UserId;
    }
    return display;
}

@end

@implementation LiveRoomMicroOrder

+ (NSArray *)modelPropertyBlacklist {
    return @[@"modelUser"];
}

@end

@implementation CXLiveRoomModel

// 初始化
- (instancetype)initWithMessage:(SocketMessageRoomInit*)message {
    if (self = [super init]) {
        _seats = [NSMutableDictionary new];
        _users = [NSMutableDictionary new];
        _leftOrders = [NSMutableDictionary new];
        _rightOrders = [NSMutableDictionary new];
        _userSeats = [NSMutableDictionary new];
        _seatUsers = [NSMutableDictionary new];
        _roomMessages = [NSMutableArray new];
        
        _music_reverseArrays = [NSMutableArray new];
        _music_reverse_page = 0;
        _music_reverse_allPage = 1;
        
        _horseLampHeight = 0;
        
        _IsHelpShare = message.IsHelpShare;
        
        _UserIdentity = message.UserIdentity;

        NSDateFormatter * dateFormatter = [NSDateFormatter new];
        dateFormatter.dateFormat = @"yyyy-MM-dd'T'HH:mm:ss.SSSSSSSzzz";
        
        [self modelSetWithDictionary:[message modelToJSONObject]];
        [message.MicroInfos enumerateObjectsUsingBlock:^(SocketMessageRoomInitMicroInfo * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            LiveRoomMicroInfo * seat = [LiveRoomMicroInfo modelWithJSON:[obj modelToJSONObject]];
            NSDate * daojishi = [dateFormatter dateFromString:seat.DaojishiShijiandian];
            if (daojishi) {
                seat.DaojishiEndDate = [NSDate dateWithTimeInterval:seat.DaojishiShichang.doubleValue sinceDate:daojishi];
            }
            NSIndexPath * indexPath = [seat indexPath];
            [self.seats setObject:seat forKey:indexPath];
            if (obj.User) {
                LiveRoomUser * user = [LiveRoomUser modelWithJSON:[obj.User modelToJSONObject]];
                [self.users setObject:user forKey:user.UserId];
                user.modelSeat = seat;
                seat.modelUser = user;
                [self.userSeats setObject:indexPath forKey:user.UserId];
                [self.seatUsers setObject:user.UserId forKey:indexPath];
            }
        }];
        
        [message.LeftMicroOrders enumerateObjectsUsingBlock:^(SocketMessageRoomInitMicroOrder * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            LiveRoomMicroOrder * order = [LiveRoomMicroOrder modelWithJSON:[obj modelToJSONObject]];

            if (obj.User) {
                LiveRoomUser * userInfo = [LiveRoomUser modelWithJSON:[obj.User modelToJSONObject]];
                order.modelUser = userInfo;
                
                [self.leftOrders setObject:order forKey:userInfo.UserId];
            }
        }];
        
        [message.RightMicroOrders enumerateObjectsUsingBlock:^(SocketMessageRoomInitMicroOrder * _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
            LiveRoomMicroOrder * order = [LiveRoomMicroOrder modelWithJSON:[obj modelToJSONObject]];
            if (obj.User) {
                LiveRoomUser * userInfo = [LiveRoomUser modelWithJSON:[obj.User modelToJSONObject]];
                order.modelUser = userInfo;
                
                [self.rightOrders setObject:order forKey:userInfo.UserId];
                
            }
        }];
        
        self.applyNumber_man = [message.WaitMicroLeftNumber stringValue];
        self.applyNumber_woman = [message.WaitMicroRightNumber stringValue];
        self.onlineNumber_man = [message.OnlineLeftNumber stringValue];
        self.onlineNumber_woman = [message.OnlineRightNumber stringValue];
        
        self.HeatValue = @(self.RoomData.VisitorNumbers.integerValue + self.RoomData.ExternVisitorNumbers.integerValue);
    }
    return self;
}

- (nullable LiveRoomMicroInfo*)microInfoForUser:(NSString*)userId {
    return [_users objectForKey:userId].modelSeat;
}

- (nullable LiveRoomMicroInfo*)microOrderForUser:(NSString*)userId {
    return nil;
}

- (BOOL)isSonger {
    if (_music_songerId && [_music_songerId isEqualToString:[CXClientModel instance].userId]) {
        return YES;
    }
    return NO;
}

- (BOOL)isConsertModel {
    if (_playing_SongInfo && _playing_SongInfo.SongMode == 2) {
        return YES;
    }
    return NO;
}

- (BOOL)isHost {
//    LiveRoomMicroInfo *microInfo = [self microInfoForUser:[CXClientModel instance].userId];
//    if (microInfo && microInfo.Type == LiveRoomMicroInfoTypeHost) { // 红娘
//        return YES;
//    }
//
//    return NO;
    if (self.UserIdentity.integerValue == 2) {
        return YES;
    } else {
        return NO;
    }
}

@end

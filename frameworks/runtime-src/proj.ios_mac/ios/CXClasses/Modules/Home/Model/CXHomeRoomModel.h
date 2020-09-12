//
//  CXHomeRoomModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXHomeRoomModel : NSObject

@property (nonatomic, strong) NSString * room_id;
@property (nonatomic, strong) NSString * room_name;
@property (nonatomic, strong) NSString * fm_room_image;
@property (nonatomic, strong) NSString * room_type;
@property (nonatomic, strong) NSString * visitor_number;
@property (nonatomic, strong) NSString * room_tags;
@property (nonatomic, strong) NSString * maker_nickname;
@property (nonatomic, strong) NSString * maker_avatar;
@property (nonatomic, strong) NSString * room_lock;
@property (nonatomic, strong) NSString * fm_city;
@property (nonatomic, strong) NSString * fm_sex;
@property (nonatomic, strong) NSString * fm_nickname;
@property (nonatomic, strong) NSString * fm_age;
@property (nonatomic, strong) NSString * xq_type; // 相亲房房间状态 1:无嘉宾 2:等待中 3:相亲中
@property (nonatomic, strong) NSNumber * right_corn; // 右上角图标：0:不显示 1:红包

@end

@interface CXHomeRoomModeModel : NSObject
@property (nonatomic, strong) NSString * mode_id; // 模式id
@property (nonatomic, strong) NSString * room_mode; // 模式名称
@property (nonatomic, strong) NSString * room_type; // 模式分类 1 普通用户 2公会用户
@property (nonatomic, strong) NSString * status; // 状态：用于首页 1首页 2未首页
@property (nonatomic, assign) NSInteger selected;
@property (nonatomic, copy) NSArray *mode_tags;

@end

@interface CXHomeRoomBannerModel : NSObject
@property (nonatomic, strong) NSString * banner_id;
@property (nonatomic, strong) NSString * image;
@property (nonatomic, strong) NSString * linkurl;
@property (nonatomic, strong) NSString * title;
@property (nonatomic, strong) NSString * link_type;// 链接类型1:为图片链接类型 2:为web链接类型 3:为界面ui类型
@property (nonatomic, strong) NSString * ui_type;// 房间内功能类型1：砸蛋类型，2:排行榜 1001:首充
@end

@interface CXHomeRoomCreateRoomInfoModel : NSObject

@property (nonatomic, strong) NSString * room_id;
@property (nonatomic, strong) NSString * user_id;
@property (nonatomic, strong) NSString * room_name;
@property (nonatomic, strong) NSString * room_desc;
@property (nonatomic, strong) NSString * room_welcomes;
@property (nonatomic, strong) NSString * room_type;
@property (nonatomic, strong) NSString * room_tags;
@property (nonatomic, strong) NSString * room_lock; // 0未锁定 1锁定
@property (nonatomic, strong) NSString * room_images;
@property (nonatomic, strong) NSString * is_exclusive_room;
@property (nonatomic, strong) NSString * is_close_camera;
@property (nonatomic, strong) NSString * is_open_red_packet;
@property (nonatomic, strong) NSString * is_open_break_egg;
@property (nonatomic, strong) NSString * is_open_pick_song;
@property (nonatomic, strong) NSString * is_open_media_library;
@property (nonatomic, strong) NSString * is_open_video_frame;

@end

NS_ASSUME_NONNULL_END

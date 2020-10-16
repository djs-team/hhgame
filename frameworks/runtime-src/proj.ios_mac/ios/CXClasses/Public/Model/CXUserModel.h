//
//  CXUserModel.h
//  hhgame-mobile
//
//  Created by mahong yang on 2020/8/3.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface CXUserCurrentRoomModel : NSObject
@property (nonatomic, strong) NSString *avatar;
@property (nonatomic, strong) NSString *room_id;
@property (nonatomic, strong) NSString *room_name;
@end

@interface CXUserModel : NSObject

@property (nonatomic, strong) NSString *user_id;
@property (nonatomic, strong) NSString *token;
@property (nonatomic, strong) NSString *username;
@property (nonatomic, strong) NSString *nickname;
@property (nonatomic, strong) NSString *intro; //简介
@property (nonatomic, strong) NSString *avatar;
@property (nonatomic, strong) NSNumber *sex; // 1:男 2:女
@property (nonatomic, strong) UIImage *sexImage;
@property (nonatomic, strong) NSString *age;
@property (nonatomic, strong) NSString * birthday;
@property (nonatomic, strong) NSNumber *is_vip; //是否为 vip
@property (nonatomic, strong) NSNumber *lv_dengji; //等级
@property (nonatomic, assign) BOOL attestation;// 是否认证
@property (nonatomic, strong) NSString * isBlocks; // 1未拉黑 2已拉黑
@property (nonatomic, strong) NSString *belongId;
@property (nonatomic, strong) NSString *is_matchmaker; // 是否是红娘 0:不是 1:是
@property (nonatomic, strong) NSString * is_friend; // 1是好友2不是

@property (nonatomic, strong) NSString *online; // 用户状态 0离线 3相亲中 5热聊中 6开播中

@property (nonatomic, strong) CXUserCurrentRoomModel *room_info; // 当前所在房间信息

// 搜索
@property (nonatomic, strong) NSString *room_id; // 房间id，如果为空就不在房间
@property (nonatomic, strong) NSString *online_str; // 在线文字描述：在线、离线、刚刚在线
@property (nonatomic, strong) NSString *state; // 2/3相亲中 4/5热聊中 6开播中 其他没有
@property (nonatomic, strong) NSString *city_two; // 市
@property (nonatomic, strong) NSString *city; // 省

@property (nonatomic, strong) NSNumber * type; // 1:是守护 2:不是守护

// 排行榜
@property (nonatomic, strong) NSString * num; // 魅力榜数值
@property (nonatomic, strong) NSString * coin; // 神曲榜玫瑰数
@property (nonatomic, strong) NSString * blue_rose; // 蓝玫瑰数量
@property (nonatomic, strong) NSString * song_name; // 歌名
@property (nonatomic, strong) NSString * singer_name; // 歌手名

// 守护榜
@property (nonatomic, strong) NSNumber * countdown_day; // 守护还有多少天到期
@property (nonatomic, strong) NSString * is_auto; // 是否自动续费 1:是 2：否
@property (nonatomic, strong) NSString * end_time; // 到期时间
@property (nonatomic, strong) NSString * intimacy; // 亲密值

// 详细资料
@property (nonatomic, strong) NSString * stature;// 身高
@property (nonatomic, strong) NSString * pay;// 月收入
@property (nonatomic, strong) NSString * profession;// 职业
@property (nonatomic, strong) NSString * education;// 学历
@property (nonatomic, strong) NSString * housing_status;// 住房情况
@property (nonatomic, strong) NSString * marital_status;// 婚姻
@property (nonatomic, strong) NSString * charm_part;// 魅力部位
@property (nonatomic, strong) NSString * blood_type;// 血型
@property (nonatomic, strong) NSString * together;// 婚后与父母同居
@property (nonatomic, strong) NSString * live_together_marrge;// 婚前同居

// 征友条件
@property (nonatomic, strong) NSString * friend_city;// 征友所在地
@property (nonatomic, strong) NSString * friend_age;// 征友年龄
@property (nonatomic, strong) NSString * friend_stature;// 征友身高
@property (nonatomic, strong) NSString * friend_education;// 征友学历
@property (nonatomic, strong) NSString * friend_pay;// 征友收入

// 黑名单
@property (nonatomic, strong) NSString * createtime;// 拉黑时间
@end

// 编辑资料
@interface CXUserInfoMenusModel : NSObject
@property (nonatomic, strong) NSString *mold;
@property (nonatomic, strong) NSString *default_name;
@property (nonatomic, strong) NSString *type_name;
@property (nonatomic, strong) NSString *column;
@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSArray <NSString *> *list;
@property (nonatomic, strong) NSDictionary *option;

@end

@interface CXUserInfoMenusDataMenusModel : NSObject
@property (nonatomic, strong) NSString *name;
@property (nonatomic, strong) NSArray <CXUserInfoMenusModel *> *menus;

@end

@interface CXUserInfoMenusDataModel : NSObject
@property (nonatomic, strong) CXUserInfoMenusDataMenusModel *jbxx;
@property (nonatomic, strong) CXUserInfoMenusDataMenusModel *grzl;
@property (nonatomic, strong) NSString *jyxs;
@end


NS_ASSUME_NONNULL_END

package cn.haoduogame.banker.core.message;

/**
 * 状态码
 * 
 * @author lulongji
 * @version 1.0
 */
public interface StatusCode {
	
	/**
	 * 为测试 message debug  leo
	 */
	boolean IS_DEBUG = false;
	
	/**
	 * 成功 注册成功
	 */
	short SUCCESS = 0;
	/**
	 * 失败
	 */
	short FAILURE = 1;

	/**
	 * 注册失败
	 */
	short REGISTER_FILURE = 2;

	/**
	 * token异常
	 */
	short TOKEN_ERROR = 3;

	/**
	 * 账号不存在
	 */
	short ACCOUNT_NONE_EXISTS = 4;

	/**
	 * 验证码不存在
	 */
	short SIMECODE_ERROR = 5;

	/**
	 * 账号已存在
	 */
	short ACCOUNT_HAVE_EXISTS = 6;

	/**
	 * 账号已存在 登陆用
	 */
	short SUCCESS_LOGIN = 7;

	/**
	 * 用户名已存在（昵称已存在）
	 */
	short NICKNAME_HAVE = 8;

	/**
	 * 昵称敏感词
	 */
	short NICKNAME_KEYWORD = 9;

	/**
	 * （用户清除数据后，通过IMEI查找账号表）存在手机账号
	 */
	short PHONE_ACCOUNT_EXISTS = 10;

	/**
	 * 登陆墙
	 */
	short LOGIN_WALL = 11;

	/**
	 * 重复登录，互踢
	 */
	short REPEATE_LOGIN = 12;

	/**
	 * 服务器维护，踢出玩家
	 */
	short KICK_PLAYER = 13;

	/**
	 * 用户在线Key验证失败
	 */
	short PLAYER_KEY_ERROR = 14;

	/**
	 * 未知游戏
	 */
	short UNKNOWN_GAME = 15;

	/**
	 * 未知平台
	 */
	short UNKNOWN_PLATFORM = 16;

	/**
	 * 手机号格式错误
	 */
	short PHONE_FORMAT_ERROR = 17;
	/**
	 * 密码错误
	 */
	short PASSWORD_ERROR = 18;

	/**
	 * 设备唯一码imei为空（或设备信息为空device）
	 */
	short IMIE_NULL = 19;

	/**
	 * 密码格式错误
	 */
	short PASSWORD_FORMAT_ERROR = 20;

	/**
	 * 手机已注册
	 */
	short PHONE_ALREADY_REGISTER = 21;

	/**
	 * 玩家不存在
	 */
	short PLAYER_NONE_EXIST = 22;

	/**
	 * 账号已绑定
	 */
	short ACCOUNT_ALREADY_BINDING = 23;

	/**
	 * 账号未绑定
	 */
	short ACCOUNT_UNBUNDLING = 24;

	/***
	 * 验证码格式错误
	 */
	short VERIFICATION_CODE_FORMAT_ERROR = 25;

	/**
	 * 手机账号不存在
	 */
	short PHONE_ACCOUNT_INEXISTENCE = 26;

	/**
	 * 登录Session错误
	 */
	short LOGIN_SESSION_ERROR = 27;

	/**
	 * 登录状态异常
	 */
	short LOGIN_STATUS_ERROR = 28;

	/**
	 * 日签重复
	 */
	short DAILY_ATTENDANCE_REPEAT = 29;

	/**
	 * 未知渠道
	 */
	short UNKNOWN_CHANNELS = 30;
	/**
	 * 未配置数据
	 */
	short UNKNOWN_DATAS = 31;

	/**
	 * 赠送奖励为空
	 */
	short UNKNOWN_REWARD = 32;
	/**
	 * 未完成
	 */
	short JUDGELUCK_UNFINISHED = 33;
	/**
	 * 当天已领取（已签到）
	 */
	short JUDGELUCK_HAVE_TO_RECEIVE = 34;
	/**
	 * 不能领取破产赠金
	 */
	short BANKRUPTCY_NOT_RECEIVE = 35;
	/**
	 * 破产补助次数已用完
	 */
	short BANKRUPTCY_HAVE_TO_RECEIVE = 36;
	/**
	 * 数据库错误
	 */
	short BANKRUPTCY_NOT_DATAS = 37;
	/**
	 * 未知参数（参数有误）（请求参数错误）
	 */
	short BANKRUPTCY_UNKNOWN_DATAS = 38;
	/**
	 * 已使用中
	 */
	short PROPS_USE = 39;
	/**
	 * 新手礼包，未到使用期
	 */
	short NEW_PLAYER_GAVE_AND_NOTUSE = 40;
	/**
	 * 没有摇奖次数也没有1000金币
	 */
	short NOTHING_ALL = 41;
	/**
	 * 初始位置不正确
	 */
	short INCORRECT_POSITIONING = 42;
	/**
	 * 错误的参数类型
	 */
	short FAILE_TYPE = 43;
	/**
	 * 兑换码错误
	 */
	short EXCHANGE_CODE = 44;
	/**
	 * 未知房间（房间号为空）
	 */
	short UNKNOWN_ROOM = 45;
	/**
	 * 抽奖机会已用完（转盘）
	 */
	short ELOTTERYDRAW_NOCHANCE = 46;
	/**
	 * 超出限制
	 */
	short ESUBMITSTATUS_REACHEDMAX = 47;
	/**
	 * 入库失败
	 */
	short ESUBMITSTATUS_WRITEFAILED = 48;
	/**
	 * 内容为空
	 */
	short ESUBMITSTATUS_SUBMITEMPTY = 49;
	/**
	 * 包含敏感词
	 */
	short CONTAIN_SENSITIVE_WORD = 50;
	/**
	 * 未知订单
	 */
	short UNKNOWN_ORDERINFO = 51;
	/**
	 * 未知道具id
	 */
	short UNKNOWN_PROP_ID = 52;
	/**
	 * 未知地址
	 */
	short UNKNOWN_HOST = 53;
	/**
	 * 已经在牌桌中
	 */
	short IN_ROOM = 54;

	/**
	 * 任务已过期
	 */
	short TASK_PAST_DUE = 55;

	/**
	 * 昵称长度不对
	 */
	short NICK_NAME_LEN_ERROR = 56;
	/**
	 * 返回登录界面
	 */
	short LOGIN_INTEGERFACE = 57;
	/**
	 * 已有账户，直接登录
	 */
	short LOGIN_REGISTER_USER = 58;
	/**
	 * 已关注过
	 */
	short IS_ATTENTION = 59;

	/**
	 * 用户游客，请登录
	 */
	short IS_TOURIST = 60;

	/**
	 * ip限制
	 */
	short IP_LIMIT = 61;
	
	/**
	 * 金币不足
	 */
	short COIN_NOT_ENOUGH = 62;
	
	/**
	 * 系统繁忙
	 */
	short SYSTEM_BUSY = 63;
	
	/**
	 * 系统维护
	 */
	short SYSTEM_MAINTENANCE = 64;
	/**
	 * 订单验证失败
	 */
	short ORDERINFO_CHECK_FAIL = 65;
	/***
	 * 验证码错误
	 */
	short VERIFICATION_CODE_ERROR = 66;
	/**
	 * 手机号格式錯誤
	 */
	short PHONE_TYPE_ERROR = 67;
	/**
	 * 一分钟后才能再次获取验证码
	 */
	short PHONE_VERIFICATION_TIME_ERROR = 68;
	/**
	 * 手机号格式和账号都有误
	 */
	short PHONE_AND_ACCOUNT_ERROR = 69;
	/**
	 * 此账号已经绑定其他玩家
	 */
	short USER_ACCOUNT_EXISTS_ERROR = 70;
	/**
	 * 账号或密码格式错误
	 */
	short ACT_OR_PWD_TYPE_ERROR = 71;
	/**
	 * 角色购买信息错误
	 */
	short BUY_ROLE_ERROE = 72;
	/**
	 * 道具不足
	 */
	short PROP_INSUFFICIENT_ERROR = 73;
	/**
	 * 角色不能购买
	 */
	short ROLE_NO_COIN_BUY_ERROR = 74;
	/**
	 * 道具扣除失败
	 */
	short PROP_DEDUCT_ERROR = 75;
	/**
	 * 任务未完成
	 */
	short TASK_UNFINISHED_ERROR = 76;
	/**
	 * 任务已领取
	 */
	short TASK_RECEIVED_ERROR = 77;
	/**
	 * 没有任务奖励可领取
	 */
	short NO_REWARD_ERROR = 78;
	/**
	 * 任务类型错误
	 */
	short TASK_TYPE_ERROR = 79;
	/**
	 * 获取任务列表失败
	 */
	short GET_TASK_ERROR = 80;
	/**
	 * 没有此挑战任务阶段
	 */
	short NO_HAVE_TASK_STAGE_ERROR = 81;
	/**
	 * 玩家没有此任务
	 */
	short PLAYER_NO_HAVE_TASK_ERROR = 82;
	/**
	 * 玩家刷新任务失败
	 */
	short REFRESH_TASK_ERROR = 83;
	/**
	 * 玩家角色不存在
	 */
	short NO_CAN_USER_ROLE_ERROR = 84;
	/**
	 * 没有此VIP
	 */
	short NO_HAVE_VIP_ERROR = 85;
	/**
	 * vip订单存储失败
	 */
	short VIP_ORDER_SAVE_ERROR = 86;
	/**
	 * 支付类型错误
	 */
	short PAY_TYPE_ERROR = 87;
	/**
	 * 签到未到此天
	 */
	short CHECKIN_BEFORE_THIS_DAY_ERROR = 88;
	/**
	 * 已经签到过
	 */
	short CHECKED_IN_ERROR = 89;
	/**
	 * 意见反馈失败
	 */
	short FEEDBACK_ERROR = 90;
	/**
	 * 摇钱树次数已用完
	 */
	short CASH_COW_NUM_NO_HAVE_ERROR = 91;
	/**
	 * 未知道具大类
	 */
	short  UNKNOWN_PROP_TYPE_ERROR = 92;
	/**
	 * 未知商品
	 */
	short  UNKNOWN_GOODS_ERROR = 93;
	/**
	 * 钻石不足
	 */
	short DIAMONDS_NOT_ENOUGH = 94;
	/**
	 * 看视频次数已用完
	 */
	short WATCH_VIDEO_NUM_NO_HAVE_ERROR = 95;
	/**
	 * 商品小类未知
	 */
	short  UNKNOWN_GOODS_TYPE_ERROR = 96;
	/**
	 *没有免费修改昵称次数
	 */
	short  NO_HAVA_UPDATE_NICKNAME_ERROR = 97;
	/**
	 * 昵称违规
	 */
	short NICKNAME_VIOLATIONS_ERROR = 98;

	
}

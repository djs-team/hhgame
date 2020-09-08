package cn.haoduogame.banker.mj.common;

/**
 * 房间 常量
 * @author leo
 */
public interface MJConstant {
	/**
	 * 成功
	 */
	byte RESULT_SUCCESS = 0;
	/**
	 * 失败
	 */
	byte RESULT_FAILURE = 1;
	
	/**
	 * 4人私房
	 */
	int EMATCHTYPE_4_PRIVATE = 1;
	/**
	 * 2人私房
	 */
	int EMATCHTYPE_2_PRIVATE = 2;
	/**
	 * 4人免费自由
	 */
	int EMATCHTYPE_4_FREE = 3;
	/**
	 * 2人免费自由
	 */
	int EMATCHTYPE_2_FREE = 4;
	/**
	 * 4人桌 比赛
	 */
	int EMATCHTYPE_4_MATCH = 5;
	/**
	 * 2人桌 比赛
	 */
	int EMATCHTYPE_2_MATCH = 6;
	/**
	 * 3人私房
	 */
	int EMATCHTYPE_3_PRIVATE = 8;
	/**
	 * 5人私房
	 */
	int EMATCHTYPE_5_PRIVATE = 9;
	/**
	 * 6人私房
	 */
	int EMATCHTYPE_6_PRIVATE = 10;
	/**
	 * 游戏已经开始
	 */
	int ERROR_TABLE_BEGIN = 10;
	/**
	 * 牌桌人数已经够了
	 */
	int ERROR_TABLE_PLAYERS_ENOUGH = 11;
	
	
	/**
	 * 玩家正在游戏队列中
	 */
	int ERROR_ROOM_PLAYING = 12;
	
	
	
	
	/**
	 * 玩家不在游戏队列中
	 */
	int ERROR_ROOM_NOT_WAITING = 13;
	

	/**
	 * 没有玩家信息
	 */
	int ERROR_ROOM_NO_PLAYER = 14;
	

	/**
	 * 没有  房卡或者金币不足
	 */
	int ERROR_ROOM_NO_FANGKA = 15;
	


	/**
	 * 房间不存在
	 */
	int ERROR_ROOM_NO_HAVA = 16;
	
	/**
	 * 登录墙  服务器维护
	 */
	int ERROR_ROOM_ServerMaintenance= 17;
	

	/**
	 * 创建房间 gps数据异常
	 */
	int ERROR_ROOM_GPSDATA_CREATROOM= 18;
	

	/**
	 * 进入房间 gps数据异常
	 */
	int ERROR_ROOM_GPSDATA_ENTERROOM= 19;
	

	/**
	 * 玩家创建房间key异常
	 */
	int ERROR_ROOM_TABLE_KEY_ERROR= 20;
	/**
	 * 所有比赛房间已满员
	 */
	int ERROR_ROOM_HAVE_NO_READY = 21;
	/**
	 * 现在没有比赛房间
	 */
	int ERROR_ROOM_HAVE_NO_MATCHROOM = 22;
	/**
	 * 现在没有比赛开启
	 */
	int ERROR_MATCH_NO_OPEN = 23;
	/**
	 * 尚未到比赛时间
	 */
	int ERROR_MATCH_BEFORE_STARTTIMT = 24;
	
	/**
	 * 代开房间成功
	 */
	int AGENT_CREATETABLE_SUCCESS = 25;
	/**
	 * 进入 代开房间 成功
	 */
	int AGENT_ENTERTABLE_SUCCESS = 26;
	
	/**
	 * 代开房间失败
	 */
	int AGENT_CREATETABLE_FAILURE = 27;
	/**
	 * 进入 待开房失败 被踢后5分钟内不可再次进入
	 */
	int AGENT_ENTERTABLE_FAILURE_FIXMINUTES = 28;
	/**
	 * 代开房失败  获取该玩家代开房信息失败
	 */
	int AGENT_ENTERTABLE_FAILURE_ROOMS = 29;
	/**
	 * 代开房失败 该玩家代开房数量已达最大值
	 */
	int AGENT_ENTERTABLE_FAILURE_MAX = 30;
	/**
	 * 玩家没有进入俱乐部
	 */
	int CLUB_PLAYER_NOTENTER = 31;
	/**
	 * 玩家在俱乐部没没有权限开房
	 */
	int CLUB_PLAYER_NOROLE = 32;
	/**
	 * 俱乐部不存在
	 */
	int CLUB_NOTEXIT = 33;
	/**
	 * 俱乐部非启用状态
	 */
	int CLUB_NOTAVAILABLE = 34;
	/**
	 * 玩家不存在
	 */
	int CLUB_NOPLAYER = 35;
	
	/**
	 * 参数为空
	 */
	int PARAMETER_ISNULL = 36;
	/**
	 * 房卡不足
	 */
	int CLUB_NOFANGKA = 37;
	/**
	 * 今日参加局数未达到参加比赛场的资格
	 */
	int TODAYPLAYNUM_ERR = 38;
	/**
	 * 比赛场入场券不足
	 */
	int ERROR_ROOM_NO_RUCHANGQUAN = 39;
}

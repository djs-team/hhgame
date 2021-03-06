
/**
 * 游戏HTTP消息配置
 */
load('game/config/HttpEvent', function () {
    const HTTP_HALL_BASE = 0x00130000
    let HttpEvent = {
        MJ_HALL_MESSAGE_LOGIN: HTTP_HALL_BASE + 1,
        MJ_HALL_MESSAGE_USERDATA: HTTP_HALL_BASE + 6,
        MJ_HALL_MESSAGE_UPDATEUSERNAME: HTTP_HALL_BASE + 7,

        MJ_HALL_CHOOSE_CITY: HTTP_HALL_BASE + 59,

        MJ_HALL_PLAYER_ROLE_GET: HTTP_HALL_BASE + 60,
        MJ_HALL_PLAYER_ROLE_BUY: HTTP_HALL_BASE + 61,
        MJ_HALL_PLAYER_TASK_DAILY : HTTP_HALL_BASE + 62,
        MJ_HALL_PLAYER_TASK_DAILY_RECEIVE : HTTP_HALL_BASE + 63,
        MJ_HALL_PLAYER_TASK_CHALLENGE  : HTTP_HALL_BASE + 64,
        MJ_HALL_PLAYER_TASK_CHALLENGE_RECEIVE  : HTTP_HALL_BASE + 65,
        MJ_HALL_PLAYER_TASK_CHALLENGE_REFRESH  : HTTP_HALL_BASE + 66,

        MJ_HALL_CASH_COW  : HTTP_HALL_BASE + 70,
        MJ_HALL_CASH_COW_NUM : HTTP_HALL_BASE + 71,
        MJ_HALL_TURNTABLE : HTTP_HALL_BASE + 72,
        MJ_HALL_PLAYER_TURNTABLE_RECEIVE : HTTP_HALL_BASE + 73,
        MJ_HALL_TURNPOINT : HTTP_HALL_BASE + 74,
        MJ_HALL_PLAYER_TURNTABLE_LOG : HTTP_HALL_BASE + 75,
        MJ_HALL_COIN_STORE : HTTP_HALL_BASE + 76,
        MJ_HALL_PLAYER_CHECK_HALL_FLAG: HTTP_HALL_BASE + 77,
        MJ_HALL_PLAYER_COIN_BUY: HTTP_HALL_BASE + 78,
        MJ_HALL_PLAYER_WATCH_VIDEO_GET_DIAMONDS: HTTP_HALL_BASE + 79,

        MJ_HALL_REAL_NAME: HTTP_HALL_BASE + 83,
        MJ_HALL_PLAYER_GET_DELIVERY_ADDRESS: HTTP_HALL_BASE + 84,
        MJ_HALL_PLAYER_UPDATE_DELIVERY_ADDRESS: HTTP_HALL_BASE + 85,
        MJ_HALL_CASH_COW_LOG  : HTTP_HALL_BASE + 88,
        MJ_HALL_LUCKY_PRIZE : HTTP_HALL_BASE + 89,


        MJ_HALL_PLAYER_TASK_INVITE  : HTTP_HALL_BASE + 91, //玩家邀请任务信息
        MJ_HALL_PLAYER_TASK_INVITE_RECEIVE  : HTTP_HALL_BASE + 92, //玩家邀请任务奖励领取
        MJ_HALL_PLAYER_INVITE_LOG  : HTTP_HALL_BASE + 93, //玩家邀请记录
        MJ_HALL_VIP_INFO : HTTP_HALL_BASE + 95,
        MJ_HALL_VIP_DAILY_GIFT_RECEIVE : HTTP_HALL_BASE + 96,



        MJ_HALL_CHECK_IN_INFO  : HTTP_HALL_BASE + 104,
        MJ_HALL_CHECK_IN  : HTTP_HALL_BASE + 105,


        MJ_HALL_PLAYER_ROLE_SELECT  : HTTP_HALL_BASE + 111,
    }
    return HttpEvent
})
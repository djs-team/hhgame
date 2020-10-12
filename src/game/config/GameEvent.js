
/**
 * 游戏内业务层的事件定义
 */
load('game/config/GameEvent', function () {
    let GameEvent = {
        'remoteConfigFinish_local': 'remoteConfigFinish_local',
        'nativePower': 'nativePower',
        'UPDATE_INFO': 'updateInfo',
        'pingPong': 'pingPong',
        'connectSucess_local': 'connectSucess',
        'WX_USER_LOGIN': 'WX_USER_LOGIN',
        'LB_USER_LOGIN': 'LB_USER_LOGIN',
        'XL_USER_LOGIN': 'XL_USER_LOGIN',
        'DL_USER_LOGIN': 'DL_USER_LOGIN',
        'MW_USER_LOGIN': 'MW_USER_LOGIN',
        'PHONE_USER_LOGIN': 'PHONE_USER_LOGIN',
        'WX_USER_LOGIN_TOKEN': 'WX_USER_LOGIN_TOKEN',
        'DIALOG_HIDE_ONE': 'DIALOG_HIDE_ONE',
        'DIALOG_HIDE_ALL': 'DIALOG_HIDE_ALL',
        'TURNTABLE_INIT': 'TURNTABLE_INIT',
        'USERDATA': 'USERDATA',
        'UPDATE_USERNAME': 'UPDATE_USERNAME',
        'AUTHENICATION': 'AUTHENICATION',
        'GET_CASHCOWNUM': 'GET_CASHCOWNUM',
        'UPDATE_CASHCOWNUM': 'UPDATE_CASHCOWNUM',
        'UPDATE_PROPSYNC': 'UPDATE_PROPSYNC',
        'GET_CASHCOWRECORD': 'GET_CASHCOWRECORD',
        'TURNTABLE_POINT': 'TURNTABLE_POINT',
        'TURNTABLE_RECEIVE': 'TURNTABLE_RECEIVE',
        'TURNTABLE_LUCKY_PRIZE': 'TURNTABLE_LUCKY_PRIZE',
        'TURNTABLE_PLAYERLOG': 'TURNTABLE_PLAYERLOG',
        'TASK_DAILY': 'TASK_DAILY',
        'TASK_DAILY_RECEIVEREWARDS': 'TASK_DAILY_RECEIVEREWARDS',
        'TASK_CHALLENGE': 'TASK_CHALLENGE',
        'TASK_CHALLENGE_RECEIVE': 'TASK_CHALLENGE_RECEIVE',
        'TASK_CHALLENGE_REFRESH': 'TASK_CHALLENGE_REFRESH',
        'SIGN_IN_DATA': 'SIGN_IN_DATA',
        'SIGN_IN_DATA_CHECK': 'SIGN_IN_DATA_CHECK',
        'MY_INVITE': 'MY_INVITE',
        'MY_INVITE_RECEIVE': 'MY_INVITE_RECEIVE',
        'SHARE_INVITE': 'SHARE_INVITE',
        'VIP_INFO_GET': 'VIP_INFO_GET',
        'VIP_INFO_RECEIVE': 'VIP_INFO_RECEIVE',
        'ROLES_GET': 'ROLES_GET',
        'ROLES_BUY': 'ROLES_BUY',
        'ROLES_SELECT': 'ROLES_SELECT',
        'COINSHOP_GET': 'COINSHOP_GET',
        'COINSHOP_BUY': 'COINSHOP_BUY',
        'ADRESS_GET': 'ADRESS_GET',
        'ADRESS_UPDATE': 'ADRESS_UPDATE',
        'VIDEO_WATCH_DIAMONDS': 'VIDEO_WATCH_DIAMONDS',
        'PLAYER_BUY_VIP_ORDER': 'PLAYER_BUY_VIP_ORDER',
        'PLAYER_BUY_VIP_ORDER_APPLE_CHECK': 'PLAYER_BUY_VIP_ORDER_APPLE_CHECK',
        'GETROLLIMGLIST': 'GETROLLIMGLIST',
        'MENULIST': 'MENULIST',
        'GOODSLIST': 'GOODSLIST',
        'FUKA_BUGGOODS': 'FUKA_BUGGOODS',
        'FUKA_ROBLIST': 'FUKA_ROBLIST',
        'FUKA_CARDLIST': 'FUKA_CARDLIST',
        'FUKA_ROB': 'FUKA_ROB',
        'FUKA_ROBLOG': 'FUKA_ROBLOG',
        'FUKA_MATERIAL_LIST': 'FUKA_MATERIAL_LIST',
        'FUKA_MATERIA_LOG': 'FUKA_MATERIA_LOG',
        'FUKA_MATERIA_EXCHANGE': 'FUKA_MATERIA_EXCHANGE',
        'UPDATE_USERPHOTO': 'UPDATE_USERPHOTO',
        'GET_ARENAMESSAGE': 'GET_ARENAMESSAGE',
        'MatchJinjiGaming': 'MatchJinjiGaming',
    }

    return GameEvent
})

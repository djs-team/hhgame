
/**
 * 游戏内业务层的事件定义
 */
load('game/public/PublicEvent', function () {
    let PublicEvent = {
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
        'WX_USER_LOGIN_TOKEN': 'WX_USER_LOGIN_TOKEN'
    }

    return PublicEvent
})

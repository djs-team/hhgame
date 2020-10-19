
/**
 * 游戏本地数据记录Key
 */
load('game/public/LocalSave', function () {
    let LocalSave = {
        'LocalImei': 'LocalImei', //本地imei缓存
        'CoinGamePeopleNum': 'CoinGamePeopleNum',// 金币场缓存上场人数
        'LocalLanguage' : 'LocalLanguage', //本地语言类型
        'SignDate': 'SignDate', //弹出签到界面的时间
        'MemberVipDate': 'MemberVipDate', //弹出VIP界面的时间
    }

    return LocalSave
})
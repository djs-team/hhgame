
/**
 * 游戏本地数据记录Key
 */
load('game/public/LocalSave', function () {
    let LocalSave = {
        'LocalImei': 'LocalImei', //本地imei缓存
        'CoinGamePeopleNum': 'CoinGamePeopleNum',// 金币场缓存上场人数
        'LocalLanguage' : 'LocalLanguage', //本地语言类型
    }

    return LocalSave
})

load('game/data/GameData', function () {
    let GameData = cc.Class.extend({
        gameInfo: {}, // app内各玩法的配置项
        vipTable: 0, // 当前所在的牌桌id，如果不在牌桌内则为0
        roleType: 0, // 用户类型(0:未加入游戏, 1:游戏玩家 , 2:游客)
        time: 0, // 登录成功的时间
        httpKey: 'abc',//HTTP消息key
        ctor: function () {},
        setData: function (data) {
            this.gameInfo = data.gameInfo
            this.vipTable = data.vipTable
            this.roleType = data.roleType
            this.httpKey = data.key
            this.time = data.time
        },
        resetData: function () {
            this.gameInfo = {}
            this.vipTable = 0
            this.roleType = 0
            this.time = 0
        },
        saveMsg: function (msg, key) {
            if (typeof key === 'string') {
                this[key] = msg[key]
            } else {
                for (let i = 0; i < key.length; ++i) {
                    this[key[i]] = msg[key[i]]
                }
            }
        },
    })
    return GameData
})

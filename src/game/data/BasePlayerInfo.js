
/*
 * 牌桌内 一位玩家的数据域基础类
 *
 */
load('game/data/BasePlayerInfo', function () {
    let BasePlayerInfo = cc.Class.extend({
        ctor: function () {},

        setData: function (data) {
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

        setDataMsg: function (msg) {
            for (let key in msg) {
                this[key] = msg[key]
            }
        }
    })

    return BasePlayerInfo
})

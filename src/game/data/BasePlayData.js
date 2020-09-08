
/*
 * 所有玩法基础数据类
 * 相当于原来的data.sdata
 */
load('game/data/BasePlayData', function () {
    let BasePlayData = cc.Class.extend({
        ctor: function () {},

        setData: function (data) {
            this.players = {}
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

    return BasePlayData
})


/*
 * 玩法的基础数据结构
 * tData
 */
load('game/data/BaseTableData', function () {
    let BaseTableData = cc.Class.extend({
        ctor: function () {},

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
        },

        saveData: function (msg,saveKey) {
            if ( saveKey ) {
                for(let i = 0; i < saveKey.length; ++i) {
                    this[saveKey[i]] = msg[saveKey[i]]
                }
            } else {
                for (let key in msg) {
                    this[key] = msg[key]
                }
            }
        },
    })

    return BaseTableData
})

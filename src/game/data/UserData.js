
load('game/data/UserData', function () {
    let GameConfig = include('game/config/GameConfig')
    let UserData = cc.Class.extend({
        platform: -1, //平台
        account: -1, // 账号
        token: -1, // 临时令牌
        key: -1, // 登陆成功后，服务端存的session KEY
        timetamp: -1,// 登陆时间戳
        pname: -1,
        photo: null,// 默认头像
        sdkphotourl: null,
        sex: -1,// 性别
        exp: -1,// 经验
        propAttr: [],// 道具列表
        isbindphone: 0,
        phone:-1,// 手机号
        lastChannel: null,//玩家上次登录的城市
        fistLogin: 0,//0是第一次1代表不是第一次
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

        getMjChannel: function () {
            if (!this.channel) {
                cc.log('error when getMjChannel ')
                return'error'
            }
            return GameConfig.channel[this.channel]
        },
        resetData: function () {

        }
    })

    return UserData
})

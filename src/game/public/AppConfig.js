/**
 * 游戏配置
 */
load('game/public/AppConfig', function () {
    let Appconfig = {}

    Appconfig.httpUrl = 'https://mjser.harmonygames.cn:39981/mj-hall-server/msgReceive/httpDecoder.do?' //外网正式服
    //Appconfig.httpUrl = 'http://10.66.66.111:8080/mj-hall-server/msgReceive/httpDecoder.do?'//峰哥本地内外IP
    //   Appconfig.httpUrl = 'http://47.105.94.107:38080/mj-hall-server/msgReceive/httpDecoder.do?' //外网测试服
    // Appconfig.httpUrl = 'http://hf.test.hehemj.cn:8080/mj-hall-server/msgReceive/httpDecoder.do?' // 峰哥外网接口

    Appconfig.webUrl = 'http://hot.harmonygames.cn/chot/web/'

    /******* 苹果通用配置 *********/
    Appconfig.applePayType = 'Other' // 苹果支付方式控制: Apple:苹果内购 Other: 支付宝微信
    Appconfig.loginShowPhoneAlert = 'Other' // 一键登录是否需要展示输入手机号弹框: Show:展示 Other:不展示
    Appconfig.appleIsForceCheck = true // 苹果版本是否检测热更
    Appconfig.isShowAppleLogin = false // 是否展示苹果登录

    Appconfig.event = {
        'TCP_CLOSE': 'TCP_CLOSE',
        'RECONNECT_OVER_TIMES': 'RECONNECT_OVER_TIMES',
    }

    return Appconfig
})

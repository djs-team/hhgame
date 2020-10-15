
/**
 * 游戏配置
 */
load('game/public/AppConfig', function () {
    let Appconfig = {}

    // Appconfig.httpUrl = 'http://mjser.harmonygames.cn:39982/mj-hall-server/msgReceive/httpDecoder.do?' //外网正式服
    //Appconfig.httpUrl = 'http://10.66.66.111:8080/mj-hall-server/msgReceive/httpDecoder.do?'//峰哥本地内外IP
    Appconfig.httpUrl = 'http://47.105.94.107:38080/mj-hall-server/msgReceive/httpDecoder.do?' //外网测试服
    // Appconfig.httpUrl = 'http://hf.test.hehemj.cn:8080/mj-hall-server/msgReceive/httpDecoder.do?' // 峰哥外网接口
    // 苹果支付方式控制: Apple:苹果内购 其他: 支付宝微信
    Appconfig.applePayType = 'Other'
    
    return Appconfig
})

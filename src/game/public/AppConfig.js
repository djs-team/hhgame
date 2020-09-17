
/**
 * 游戏配置
 */
load('game/public/AppConfig', function () {
    let Appconfig = {}
    // Appconfig.httpUrl = 'http://10.66.66.105:8080/mj-hall-server/msgReceive/httpDecoder.do?'
    Appconfig.httpUrl = 'http://47.105.94.107:38080/mj-hall-server/msgReceive/httpDecoder.do?' //外网测试服
    // Appconfig.httpUrl = 'http://hf.test.hehemj.cn:8080/mj-hall-server/msgReceive/httpDecoder.do?' // 峰哥外网接口
    return Appconfig
})
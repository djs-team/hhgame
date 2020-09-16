
load('game/config/ResConfig', function () {
    let ResConfig = {}
    /**
     *  调用的图片配置
     */
    ResConfig.Png = {

    }

    /**
     * 子模块依赖的界面配置
     */
    ResConfig.Module = {

    }

    /**
     *  图集资源配置
     */
    ResConfig.Atlas = {
        "Common": [{ plist: 'res/bsCommon/bsCommon.plist', png: 'res/bsCommon/bsCommon.png', retain: true }]
    }

    ResConfig.PlayerPlay = {
        'stand': 'stand',
        'hit': 'hit',
        'lose': 'lose',
        'win': 'win',
    }

    /**
     *  角色骨骼动画资源
     */
    ResConfig.AniPlayer = {
        "1": { json: 'res/animation/player/caishen/caishen.json', atlas: 'res/animation/player/caishen/caishen.atlas', png: 'res/animation/player/caishen/caishen.png' },
        "4": { json: 'res/animation/player/fage/fage.json', atlas: 'res/animation/player/fage/fage.atlas', png: 'res/animation/player/fage/fage.png' },
        "6": { json: 'res/animation/player/huamulan/huamulan.json', atlas: 'res/animation/player/huamulan/huamulan.atlas', png: 'res/animation/player/huamulan/huamulan.png' },
        "5": { json: 'res/animation/player/huangdi/huangdi.json', atlas: 'res/animation/player/huangdi/huangdi.atlas', png: 'res/animation/player/huangdi/huangdi.png' },
        "7": { json: 'res/animation/player/huxian/huxian.json', atlas: 'res/animation/player/huxian/huxian.atlas', png: 'res/animation/player/huxian/huxian.png' },
        "2": { json: 'res/animation/player/nanchushi/nanchushi.json', atlas: 'res/animation/player/nanchushi/nanchushi.atlas', png: 'res/animation/player/nanchushi/nanchushi.png' },
        "3": { json: 'res/animation/player/nvchushi/nvchushi.json', atlas: 'res/animation/player/nvchushi/nvchushi.atlas', png: 'res/animation/player/nvchushi/nvchushi.png' },
        "8": { json: 'res/animation/player/zhangzuolin/zhangzuolin.json', atlas: 'res/animation/player/zhangzuolin/zhangzuolin.atlas', png: 'res/animation/player/zhangzuolin/zhangzuolin.png' },
        "9": { json: 'res/animation/player/zhizunbao/zhizunbao.json', atlas: 'res/animation/player/zhizunbao/zhizunbao.atlas', png: 'res/animation/player/zhizunbao/zhizunbao.png' },
        "10": { json: 'res/animation/player/zixia/zixia.json', atlas: 'res/animation/player/zixia/zixia.atlas', png: 'res/animation/player/zixia/zixia.png' },
    }

    /**
     *  骨骼动画资源
     */
    ResConfig.AniHall = {
        "DatingJinbichang": { json: 'res/animation/hall/dating_jinbichang/dating_jinbichang.json', atlas: 'res/animation/hall/dating_jinbichang/dating_jinbichang.atlas', png: 'res/animation/hall/dating_jinbichang/dating_jinbichang.png' },
        "Yaojingshu": { json: 'res/animation/hall/yaojinshu/yaojinshu.json', atlas: 'res/animation/hall/yaojinshu/yaojinshu.atlas', png: 'res/animation/hall/yaojinshu/yaojinshu.png' },

    }

    /**
     *  资源界面配置
     */
    ResConfig.Ui = {
        'SystemTips': 'game/ui/public/SystemTips',
        'DialogLayer': 'game/ui/public/DialogLayer',
        'ReceivePropsLayer': 'game/ui/public/ReceivePropsLayer',
        'ChooseCityLayer': 'game/ui/layer/choosecity/ChooseCityLayer',
        'TurnTableLayer': 'game/ui/layer/turntable/TurnTableLayer',
        'PersonalLayer': 'game/ui/layer/personal/PersonalLayer',
        'Authentication': 'game/ui/layer/authentication/AuthenticationLayer',
        'CashCowLayer': 'game/ui/layer/cashcow/CashCowLayer',
        'TaskLayer': 'game/ui/layer/task/TaskLayer',
        'InvitationLayer': 'game/ui/layer/invitation/InvitationLayer',
        'ArenaLayer': 'game/ui/layer/match/ArenaLayer',
        'CoinGameLayer': 'game/ui/layer/coingame/CoinGameLayer',
        'CoinShopLayer': 'game/ui/layer/coinshop/CoinShopLayer',
        'ShopLayer': 'game/ui/layer/shop/ShopLayer',
        'SignLayer': 'game/ui/layer/sign/SignLayer',
        'EmailLayer': 'game/ui/layer/email/EmailLayer',
        'MemberLayer': 'game/ui/layer/member/MemberLayer',
        'RoleLayer': 'game/ui/layer/role/RoleLayer',
        'FukaShopLayer': 'game/ui/layer/fukashop/FukaShopLayer',

    }

    /**
     *  csb资源配置
     */
    ResConfig.View = {
        'LaunchScene': 'res/LaunchScene.json',
        'UpdateScene': 'res/UpdateScene.json',
        'LoginScene': 'res/LoginScene.json',
        'HallScene': 'res/HallScene.json',
        'SystemTipsLayer': 'res/SystemTipsLayer.json',
        'DialogLayer': 'res/DialogLayer.json',
        'ReceivePropsLayer': 'res/ReceivePropsLayer.json',
        'ChooseCityLayer': 'res/ChooseCityLayer.json',
        'SettingLayer': 'res/SettingLayer.json',
        'EmailLayer': 'res/EmailLayer.json',
        'EosBlock': 'res/EosBlock.json',
        'CreateRoom': 'res/CreateRoom.json',
        'JoinRoom': 'res/JoinRoom.json',
        'Record': 'res/Record.json',
        'RecordDetail': 'res/RecordDetail.json',
        'RecordOthers': 'res/RecordOthers.json',
        'PersonalInfo': 'res/PersonalInfo.json',
        'Email': 'res/Email.json',
        'CoinShopLayer': 'res/CoinShopLayer.json',
        'InvitationLayer': 'res/InvitationLayer.json',
        'TurnTableLayer': 'res/TurnTableLayer.json',
        'CashCowLayer': 'res/CashCowLayer.json',
        'CoinGameLayer': 'res/CoinGameLayer.json',
        'ConfLayer': 'res/ConfLayer.json',
        'FeedbackLayer': 'res/FeedbackLayer.json',
        'AuthenticationLayer': 'res/AuthenticationLayer.json',
        'CustomerServiceLayer': 'res/CustomerServiceLayer.json',
        'PersonalLayer': 'res/PersonalLayer.json',
        'TaskLayer': 'res/TaskLayer.json',
        'SignLayer': 'res/SignLayer.json',
        'ArenaLayer': 'res/ArenaLayer.json',
        'MemberLayer': 'res/MemberLayer.json',
        'RoleLayer': 'res/RoleLayer.json',
        'FukaShopLayer': 'res/FukaShopLayer.json',

    }

    ResConfig.prefixPath = { }
    ResConfig.prefixPath.CommonUI = ''
    ResConfig.prefixPath.DeleteRoom = ''
    ResConfig.prefixPath.personInfo = ''

    return ResConfig
})


/**
 *  EffectLayerMdt Mediator
 *
 */
load('module/mahjong/ui/EffectLayerMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let TableEvent = TableConfig.Event
    let ResConfig = include('module/mahjong/common/ResConfig')
    let GameResConfig = include('game/config/ResConfig')
    let Sound = ResConfig.Sound

    let DeskMdt = Mediator.extend({
        mediatorName: 'EffectLayerMdt',
        _resultDelayTime : TableConfig.resultDelayTime,
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                TableEvent.InitCardProto,
                TableEvent.MatchResultProto,
                TableEvent.MatchEnterTableProto,
                TableEvent.DrawCardProto,
                TableEvent.PlayerSelectProto,
                TableEvent.GameResultProto,
                TableEvent.GameBaoProto,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case TableEvent.InitCardProto:
                    this.InitCardProto(body)
                    break
                case TableEvent.PlayerSelectProto:
                    this.PlayerSelectProto(body)
                    break
                case TableEvent.MatchResultProto:
                    appInstance.gameAgent().delayCall(this._resultDelayTime, this.MatchResultProto, body, this)
                    // this.MatchResultProto(body)
                    break
                case TableEvent.MatchEnterTableProto:
                    this.MatchEnterTableProto(body)
                    break
                case TableEvent.TableHostingProto:
                    break
                case TableEvent.GameResultProto:
                    appInstance.gameAgent().delayCall(this._resultDelayTime, this.GameResultProto, body, this)
                    // this.GameResultProto(body)
                    break
                case TableEvent.GameBaoProto:
                    this.HuanBaoProto()
                    break
            }
        },

        HuanBaoProto: function () {
            appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.huanbao)
        },

        MatchEnterTableProto: function (msg) {
            if (msg.mcState === 1) {
                appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.match_jinji)
            }
        },

        MatchResultProto: function (msg) {
            if (msg.mcState === 0) {
                let soundArray = Sound.play['match_win_' + msg.ranking]
                if (soundArray) {
                    appInstance.gameAgent().mjUtil().playGamingSound(soundArray)
                }
            } else if (msg.mcState === 1) {
            //    淘汰
                appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.match_taotai)
            }
        },

        InitCardProto: function (msg) {
            let pData = appInstance.dataManager().getPlayData()
            if (pData.isMatch()) {
                let tData = pData.tableData
                if (tData.pCurRound === 1) {
                    appInstance.audioManager().playEffect(GameResConfig.Sound.begin)
                    setTimeout(function(){
                        appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.match_begin)
                    }, 1000)
                }
            } else {
                appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.begin)
            }
        },

        GameResultProto: function(msg) {
            let pData = appInstance.dataManager().getPlayData()
            let selfInfo = pData.getSelfInfo()
            let mySeatId = selfInfo.pSeatID
            let winSeat = msg.pWinSeatID

            if(msg.pIsLiuJu == 0){
                if (mySeatId === winSeat) {
                    let huType = msg.pHuType
                    let huArray = Sound.play.hu
                    let huTypeArray = [];
                    for (let i = 0; i < huType.length; ++i) {
                        huTypeArray.push(huType[i].pHu)
                    }
                    if (huTypeArray.indexOf(83) != -1) {
                        huArray = Sound.play.hu_baozhongbao
                    } else if (huTypeArray.indexOf(35) != -1) {
                        huArray = Sound.play.hu_loubao
                    } else if (huTypeArray.indexOf(4) != -1) {
                        huArray = Sound.play.hu_zimo
                    }
                    // 漏宝胡 ID 配置服务器没给
                    appInstance.gameAgent().mjUtil().playGamingSound(huArray)
                } else {
                    appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.lose)
                }
            }else{
               // appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.match_win_2)
            }

        },
        PlayerSelectProto: function (msg) {
            let actionId = msg._msg.pActionID
            switch (actionId) {
                case 110:
                    appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.peng)
                    break
                case 50:
                case 40:
                case 70:
                    appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.ting)
                    break
                case 120:
                    appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.chi)
                    break
                case 80:
                case 90:
                case 100:
                    appInstance.gameAgent().mjUtil().playGamingSound(Sound.play.gang)
                    break
            }
        },

        onRegister: function () {

        },

        onRemove: function () {
        }

    })

    return DeskMdt
})
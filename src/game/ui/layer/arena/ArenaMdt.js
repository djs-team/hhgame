
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/arena/ArenaMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'ArenaMdt',
        _matchIconArray: [
            { prop: '1', res: 'res/arena/ssc_32.png'},
            { prop: '2', res: 'res/arena/ssc_33.png'},
            { prop: '3', res: 'res/arena/ssc_34.png'},
            { prop: '4', res: 'res/arena/ssc_31.png'},
        ],
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [
                GameEvent.GET_ARENAMESSAGE,
            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.GET_ARENAMESSAGE:
                    this.onInitMatchList(body)
                    break
                default:
                    break
            }
        },
        onRegister: function () {
            this.initView()
        },

        onRemove: function () {

        },

        initView: function () {

        },

        initData: function () {

        },

        onInitMatchList: function (body) {

            let channel = appInstance.dataManager().getUserData().getMjChannel()
            let areaName = GameConfig.areaName[channel]
            let data = []
            for(let i = 0; i < body.matchList.length; i++){
                let match = body.matchList[i]
                let matchData = {}
                matchData.areaName = areaName
                matchData.matchId = match.matchId
                matchData.matchtitle = match.matchtitle
                matchData.format = match.format
                matchData.consumptionType = match.consumptionType
                matchData.matchfee = match.matchfee
                matchData.state = match.state
                matchData.matchplayersnum = match.matchplayersnum
                matchData.matchMode = match.matchMode

                if(matchData.state == 0){//不可报名
                    if(matchData.consumptionType == 1)//金币
                        matchData.consumptionRes = 'res/arena/ssc_9.png'
                    else
                        matchData.consumptionRes = 'res/arena/ssc_8.png'
                }else{
                    if(matchData.consumptionType == 1)
                        matchData.consumptionRes = 'res/arena/ssc_9_1.png'
                    else
                        matchData.consumptionRes = 'res/arena/ssc_8_1.png'
                }

                this.onFormatRankingList(match,matchData)
                this.onFormatTimes(match.starttime,matchData)
                data.push(matchData)
            }

            this.view.onInitMatchList(data)
        },

        onFormatRankingList: function (matchInfo,data) {



            let maxPropType = 1
            data.rankingList = []
            for(let i = 0; i < matchInfo.rangkingList.length; i++){
                let rankingInfo = matchInfo.rangkingList[i]
                let rankingData = {}
                let startRanking = rankingInfo.startRanking
                let endRanking = rankingInfo.endRanking
                let rewardInfoList = rankingInfo.rewardInfoList
                let currentpropType = 0

                if(startRanking <= 4){
                    rankingData.ranking = startRanking
                    rankingData.isImg = true

                    switch (startRanking) {
                        case 1:
                            rankingData.rankingImg = 'res/arena/ssc_25.png'
                            break
                        case 2:
                            rankingData.rankingImg = 'res/arena/ssc_26.png'
                            break
                        case 3:
                            rankingData.rankingImg = 'res/arena/ssc_27.png'
                            break
                        case 4:
                            rankingData.rankingImg = 'res/arena/ssc_28.png'
                            break
                        default:
                            break

                    }

                }else{
                    rankingData.ranking = startRanking + '-' + endRanking
                    rankingData.isImg = false
                }


                currentpropType = this.onFormatRankingData(rewardInfoList,rankingData)

                if(currentpropType > maxPropType)
                    maxPropType = currentpropType

                data.rankingList.push(rankingData)
            }

            data.propRes = this._matchIconArray[maxPropType - 1].res

        },

        onFormatRankingData: function (rewardInfoList,rankingData) {

            let maxProp = 0
            let propContext = ''
            for(let i = 0; i < rewardInfoList.length; i++){

                let info = rewardInfoList[0]
                let propType = info.propType
                let propCode = info.propCode
                let propNum = info.propNum
                let prop = 0

                if(propType != GameConfig.propType_currency){
                    prop = 4
                }else{
                    prop = propCode
                }

                if(prop > maxProp)
                    maxProp = prop

                propContext = GameConfig.propsRes[propType]['propCode'][propCode]['name'] + 'x' + propNum + '  '

            }
            rankingData.propContext = propContext

            return maxProp
        },

        onFormatTimes: function (timestamp,data) {

            let date = new Date(parseInt(timestamp))
            data.time = (date.getMonth() + 1) + '-' + (this.padLeftZero(date.getDate())) + '  ' + (this.padLeftZero(date.getHours())) + ":" + (this.padLeftZero(date.getMinutes()))

        },

        padLeftZero: function (str) {

            return ('00' + str).substr(str.toString().length);
        }


    })
    return mdt
})
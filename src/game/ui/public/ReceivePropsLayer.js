
/**
 * 共用弹出提示窗
 */
load('game/ui/public/ReceivePropsLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let ReceivePropsLayer = BaseLayer.extend({
        _className: 'ReceivePropsLayer',

        RES_BINDING: function () {
            return {
                'pnl': { onClicked : this.onClseClick },
                'pnl/rewardOnePnl': {  },
                'pnl/rewardTwoPnl': {  },
                'pnl/rewardThreePnl': {  },
                'pnl/bgAniNd': {  },
                'pnl/boomAniNd': {  },
            }
        },
        ctor: function (data) {
            this._super(ResConfig.View.ReceivePropsLayer)

            this.initView()
            this.initData(data)
        },

        initData: function (data) {

            let dataArray = []
            if(global.isArray(data))
                dataArray = data
            else
                dataArray.push(data)

            let lengtn = dataArray.length
            switch (lengtn) {
                case 1:
                    this._pnl = this.rewardOnePnl
                    break
                case 2:
                    this._pnl = this.rewardTwoPnl
                    break
                case 3:
                    this._pnl = this.rewardThreePnl
                    break
                default:
                    break

            }

            for(let i = 1; i <= lengtn; i++){

                let cell = dataArray[i-1]
                this._pnl.getChildByName('acceptedBg' + i).getChildByName('acceptedTypePg').loadTexture(cell.res)
                this._pnl.getChildByName('acceptedBg' + i).getChildByName('awardsVal').setString('x' + cell.propNum)
            }

            let scaleValue = lengtn
            if(scaleValue == 1)
                scaleValue = 1.5


            this._backAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingGongXiHuoDe)
            this.bgAniNd.addChild(this._backAni)
            this._backAni.setScale(scaleValue)
            this._backAni.setAnimation(0, 'animation2', true)



            this._boomAni = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.DatingGongXiHuoDe)
            this.boomAniNd.addChild(this._boomAni)
            this._boomAni.setScale(scaleValue)
            this._boomAni.setAnimation(0, 'animation', false)

            let deleAction = cc.DelayTime(0.4)
            let callBack = function () {
                this._pnl.setVisible(true)
            }.bind(this)
            //this.cointreeNd.runAction(cc.sequence(deleAction,cc.CallFunc(callBack)))
            this.boomAniNd.runAction(cc.sequence(deleAction,cc.CallFunc(callBack)))


        },

        initView: function () {
            this.rewardOnePnl.setVisible(false)
            this.rewardTwoPnl.setVisible(false)
            this.rewardThreePnl.setVisible(false)

        },

        onCreate: function () {
            this._super()
        },
        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },
        onClseClick: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return ReceivePropsLayer
})


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

            }
        },
        ctor: function (data) {
            cc.log('=========ReceivePropsLayer====data=======' + JSON.stringify(data))
            this._super(ResConfig.View.ReceivePropsLayer)

            this.initView()
            this.initData(data)
        },

        initData: function (data) {

            let lengtn = data.length
            let _pnl
            switch (lengtn) {
                case 1:
                    _pnl = this.rewardOnePnl
                    break
                case 2:
                    _pnl = this.rewardTwoPnl
                    break
                case 3:
                    _pnl = this.rewardThreePnl
                    break
                default:
                    break

            }

            for(let i = 1; i <= lengtn; i++){

                let cell = data[i-1]
                _pnl.getChildByName('acceptedBg' + i).getChildByName('acceptedTypePg').loadTexture(cell.res)
                _pnl.getChildByName('acceptedBg' + i).getChildByName('awardsVal').setString('x' + cell.propNum)
            }

            _pnl.setVisible(true)

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

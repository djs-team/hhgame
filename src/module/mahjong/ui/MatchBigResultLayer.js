
/**
 * DeskHeadLayer
 */
load('module/mahjong/ui/MatchBigResultLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let Layer = BaseLayer.extend({
        _className: 'MatchBigResultLayer',
        _playerPos: [
            cc.p(-360, 0),
            cc.p(-120, 0),
            cc.p(120, 0),
            cc.p(360, 0)
        ],
        _itemInfo: [
            {name: '坐庄次数', key: 'pZuoZhuang'},
            {name: '胡牌次数', key: 'pHuTimes'},
            {name: '点炮次数', key: 'pDianPao'},
            {name: '摸宝次数', key: 'pMoBao'},
            {name: '宝中宝', key: 'pBaozhongBao'},
        ],
        RES_BINDING: function () {
            return {
                'topPnl/BackBtn': { onClicked: this.onBackBtnClick },
                'midPnl/midNd': {  },
                'midPnl/midNd/PlayerCell': {  },
            }
        },

        onBackBtnClick: function () {
            let HallScene = include('game/ui/scene/HallScene')
            appInstance.sceneManager().replaceScene(new HallScene())
        },

        ctor: function () {
            this._super(ResConfig.View.MatchBigResultLayer)
            this.initView()
            cc.log('==================MatchBigResultLayer==========')
        },

        updateView: function (msg) {
            let players = msg.pPlayer
            for (let i = 0; i < players.length; ++i) {
                this.updatePlayerInfo(players[i], i)
            }
        },

        updatePlayerInfo: function (pinfo, index) {
            let pCell = this.PlayerCell.clone()
            pCell.setVisible(true)
            this.midNd.addChild(pCell)
            pCell.setPosition(this._playerPos[index])

            pCell.getChildByName('name').setString(pinfo.pid)

            // 这个晋级与否 需要跟服务器核对一下
            pCell.getChildByName('Win').setVisible(pinfo.pOffsetCoins > 0)

            pCell.getChildByName('Head').setVisible(true)
            pCell.getChildByName('winNum').setString('总积分  ' + pinfo.pCoins)
            let max = 0
            for (let i = 0; i < 5; ++i) {
                max = max < pinfo[this._itemInfo[i].key] ? pinfo[this._itemInfo[i].key] : max
            }

            for (let i = 0; i < 5; ++i) {
                pCell.getChildByName('Item' + i).getChildByName('Name').setString(this._itemInfo[i].name)
                let num = pinfo[this._itemInfo[i].key]
                pCell.getChildByName('Item' + i).getChildByName('Num').setString(num)
                let percentNum = max
                if (max !== 0) {
                    percentNum = Math.floor(num / max * 100)
                }
                pCell.getChildByName('Item' + i).getChildByName('LoadingBar').setPercent(percentNum)
            }
        },

        initView: function () {
            this.PlayerCell.setVisible(false)
        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        }
    })
    return Layer
})

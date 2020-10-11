
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
            {name: '坐庄次数'},
            {name: '胡牌次数'},
            {name: '点炮次数'},
            {name: '摸宝次数'},
            {name: '宝中宝'},
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
        },

        updateView: function (msg) {
            for (let i = 0; i < 4; ++i) {
                this.updatePlayerInfo(i)
            }
        },

        updatePlayerInfo: function (pinfo) {
            let pCell = this.PlayerCell.clone()
            pCell.setVisible(true)
            this.midNd.addChild(pCell)
            pCell.setPosition(this._playerPos[pinfo])

            pCell.getChildByName('name').setString(pinfo)
            pCell.getChildByName('Win').setVisible(true)
            pCell.getChildByName('Head').setVisible(true)
            pCell.getChildByName('winNum').setString('总积分不知道')

            for (let i = 0; i < 5; ++i) {
                pCell.getChildByName('Item' + i).getChildByName('Name').setString(this._itemInfo[i].name)
                pCell.getChildByName('Item' + i).getChildByName('Num').setString(22)
                pCell.getChildByName('Item' + i).getChildByName('LoadingBar').setPercent(10)
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

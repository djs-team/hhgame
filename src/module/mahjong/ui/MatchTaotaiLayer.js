
/**
 * MatchTaotaiLayer
 */
load('module/mahjong/ui/MatchTaotaiLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let Layer = BaseLayer.extend({
        _className: 'MatchTaotaiLayer',
        RES_BINDING: function () {
            return {
                'pnl/HeadNd': {  },
                'pnl/HeadNd/Head': {  },
                'pnl/HeadNd/NameTxt': {  },
                'pnl/HeadNd/MatchNameTxt': {  },
                'pnl/HeadNd/RankTxt': {  },

                'pnl/ShareWxBtn': { onClicked: this.onShareWxBtnClick   },
                'pnl/ShareFriendBtn': { onClicked: this.onShareFriendBtnClick  },
                'pnl/CloseBtn': { onClicked: this.onCloseBtnClick  },

            }
        },

        onShareWxBtnClick: function () {

        },

        onShareFriendBtnClick: function () {

        },

        onCloseBtnClick: function () {

        },

        ctor: function () {
            this._super(ResConfig.View.MatchTaotaiLayer)
        },

        updateView: function (msg) {
            this.NameTxt.setString('玩家名字')
            this.MatchNameTxt.setString('比赛名字')
            this.RankTxt.setString('第一名')
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


/**
 * MatchResultLayer
 */
load('module/mahjong/ui/MatchResultLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let Layer = BaseLayer.extend({
        _className: 'MatchResultLayer',
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
            appInstance.uiManager().removeUI(this)
        },

        ctor: function () {
            this._super(ResConfig.View.MatchResultLayer)
            cc.log('==================MatchResultLayer==========')
        },

        updateView: function (msg) {
            let reward
            if (msg.mcState === 0) {
                reward = '  获奖'
            } else if (msg.mcState === 1) {
                reward = '  淘汰'
            }
            this.NameTxt.setString(msg.playerName + reward)
            this.MatchNameTxt.setString(msg.matchName)
            this.RankTxt.setString('第' + msg.ranking + '名')
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

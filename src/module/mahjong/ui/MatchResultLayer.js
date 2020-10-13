
/**
 * MatchResultLayer
 */
load('module/mahjong/ui/MatchResultLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameUtil = include('game/public/GameUtil')
    let Layer = BaseLayer.extend({
        _className: 'MatchResultLayer',
        RES_BINDING: function () {
            return {
                'LosePnl': {  },
                'LosePnl/HeadNd': {  },
                'LosePnl/HeadNd/HeadLose': {  },
                'LosePnl/HeadNd/NameTxt': {  },
                'LosePnl/HeadNd/MatchNameTxt': {  },
                'LosePnl/HeadNd/RankTxt': {  },

                'LosePnl/ShareWxBtn': { onClicked: this.onShareWxBtnClick   },
                'LosePnl/ShareFriendBtn': { onClicked: this.onShareFriendBtnClick  },
                'LosePnl/CloseBtn': { onClicked: this.onCloseBtnClick  },

                'WinPnl': {  },
                'WinPnl/WinRankTxt': {  },
                'WinPnl/WinReward': {  },
                'WinPnl/WinSureBtn': { onClicked: this.onCloseBtnClick },
                'WinPnl/WinRightBtn': { onClicked: this.onShareFriendBtnClick  },

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
            if (msg.mcState === 0) {
                this.WinPnl.setVisible(true)
                this.LosePnl.setVisible(false)
                this.WinRankTxt.setString('第' + msg.ranking + '名')
                let rewardList = msg.mcRewardList
                let rewardStr = '比赛奖励：'
                for (let i = 0; i< rewardList.length; ++i) {
                    rewardStr += cc.formatStr('道具编码(%s) 数量(%s) \n', rewardList[i].propCode.toString(), rewardList[i].propNum.toString())
                }
                this.WinReward.setString(rewardStr)
            } else if (msg.mcState === 1) {
                this.WinPnl.setVisible(false)
                this.LosePnl.setVisible(true)
                this.NameTxt.setString(msg.playerName)
                this.MatchNameTxt.setString(msg.matchName)
                this.RankTxt.setString('第' + msg.ranking + '名')
                // GameUtil.loadUrlImage(msg.pPhoto, this.HeadLose)
            }

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

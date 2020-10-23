
/**
 * MatchResultLayer
 */
load('module/mahjong/ui/MatchResultLayer', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let Layer = BaseLayer.extend({
        _className: 'MatchResultLayer',
        RES_BINDING: function () {
            return {
                'dataPnl': {  },
                'dataPnl/HeadNd': {  },
                'dataPnl/HeadNd/HeadPhoto': {  },
                'dataPnl/HeadNd/NameTxt': {  },
                'dataPnl/HeadNd/MatchNameTxt': {  },
                'dataPnl/HeadNd/RankTxt': {  },
                'dataPnl/HeadNd/rewardTxt': {  },

                'dataPnl/ShareWxBtn': { onClicked: this.onShareWxBtnClick   },
                'dataPnl/ShareFriendBtn': { onClicked: this.onShareFriendBtnClick  },
                'dataPnl/CloseBtn': { onClicked: this.onCloseBtnClick  },


            }
        },

        onShareWxBtnClick: function () {

            let size = cc.director.getWinSize();
            let fileName = "result_share.jpg";
            let fullPath = jsb.fileUtils.getWritablePath() + fileName; //拿到可写路径，将图片保存在本地，可以在ios端或者java端读取该文件
            if (jsb.fileUtils.isFileExist(fullPath)) {
                jsb.fileUtils.removeFile(fullPath);
            }
            this.onShareBegin()
            let texture = new cc.RenderTexture(size.width, size.height);
            
            texture.begin();
            
            this.visit();
            texture.end();
            texture.saveToFile(fileName, cc.IMAGE_FORMAT_JPG);

            this.onShareEnd()
            
            this.schedule(() => {
                let fileName = "result_share.jpg";
                appInstance.nativeApi().shareImage('WEIXIN', fileName)
            }, 1, 0);
        },

        onShareFriendBtnClick: function () {
            let size = cc.director.getWinSize();
            let fileName = "result_share.jpg";
            let fullPath = jsb.fileUtils.getWritablePath() + fileName; //拿到可写路径，将图片保存在本地，可以在ios端或者java端读取该文件
            if (jsb.fileUtils.isFileExist(fullPath)) {
                jsb.fileUtils.removeFile(fullPath);
            }
            
            this.onShareBegin()
            
            let texture = new cc.RenderTexture(size.width, size.height);
            texture.begin();
            this.visit();
            texture.end();
            texture.saveToFile(fileName, cc.IMAGE_FORMAT_JPG);
            
            this.onShareEnd()
            
            this.schedule(() => {
                let fileName = "result_share.jpg";
                appInstance.nativeApi().shareImage('WEIXIN_CIRCLE', fileName)
            }, 1, 0);
        },
        
        onShareBegin: function () {
            this.ShareWxBtn.setVisible(false)
            this.ShareFriendBtn.setVisible(false)
            this.CloseBtn.setVisible(false)
        },
        
        onShareEnd: function () {
            this.ShareWxBtn.setVisible(true)
            this.ShareFriendBtn.setVisible(true)
            this.CloseBtn.setVisible(true)
        },

        onCloseBtnClick: function () {
            appInstance.uiManager().removeUI(this)
        },

        ctor: function () {
            this._super(ResConfig.View.MatchResultLayer)
            cc.log('==================MatchResultLayer==========')
        },

        updateView: function (msg) {

            GameUtil.loadUrlImg(msg.pPhoto, this.HeadPhoto)
            this.RankTxt.setString('第' + msg.ranking + '名')
            this.NameTxt.setString(msg.playerName)
            this.MatchNameTxt.setString(msg.matchName)
            let rewardList = msg.mcRewardList
            if(rewardList && rewardList.length > 0){
                this.rewardTxt.setVisible(true)
                let rewardStr = '比赛奖励：'
                for (let i = 0; i< rewardList.length; ++i) {
                    let rewardData = rewardList[i]
                    let propData = {
                        propType:rewardData.propType,
                        propCode:rewardData.propCode,
                        propNum:rewardData.propNum,
                    }
                    GameUtil.getRoleData(propData,['name'],'propType','propCode')
                    // rewardStr += cc.formatStr('(%s) x (%s) \n', propData.name, propData.propNum.toString())
                    if(i > 0)
                        rewardStr += '，'
                    rewardStr += cc.formatStr('%s x %s \n', propData.name, propData.propNum.toString())

                }
                this.rewardTxt.setString(rewardStr)
            }else{
                this.rewardTxt.setVisible(false)
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

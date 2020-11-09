
/**
 * DeskPersonlLayer
 */
load('module/mahjong/ui/DeskPersonlLayer', function () {
    let GameResConfig = include('game/config/ResConfig')
    let PlayerPlay = GameResConfig.PlayerPlay
    let AniPlayer = GameResConfig.AniPlayer
    let ResConfig = include('module/mahjong/common/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let DeskPersonlLayerMdt = include('module/mahjong/ui/DeskPersonlLayerMdt')
    let TableConfig = include('module/mahjong/common/TableConfig')
    let GameUtil = include('game/public/GameUtil')
    let Layer = BaseLayer.extend({
        _className: 'DeskPersonlLayer',
        RES_BINDING: function () {
            return {
                'pnl/dataPnl/aniNd': {  },
                'pnl/dataPnl/photoImg': {  },
                'pnl/dataPnl/idTxt': {  },
                'pnl/dataPnl/allCntTxt': {  },
                'pnl/dataPnl/winCnt': {  },
                'pnl/dataPnl/namePnl/nameTxt': {  },
                'pnl/dataPnl/coinsPnl/coinsValTxt': {  },

                'pnl/magicBlockPnl': {  },
                'pnl/magicBlockPnl/magicList': {  },
                'pnl/magicBlockPnl/magicCell': {  },
                'pnl/magicBlockPnl/jinBiFreePnl': {  },
                'pnl/closeBtn': { onClicked: this.onCloseBtnClick },

            }
        },
        ctor: function (msg) {
            this._super(ResConfig.View.DeskPersonlLayer)
            this.registerMediator(new DeskPersonlLayerMdt(this,msg))
        },

        initData: function (pData) {


        },

        initView: function (pData) {
            this.magicList.setScrollBarEnabled(false)
            if(!pData) return
            this.onShowRole(pData.pRole)
            this.loadUrlImage(pData.pPhoto,this.photoImg)
            //this.idTxt.setString(pData.pid)
            this.allCntTxt.setString(pData.allCnt)
            this.winCnt.setString(pData.winCnt)
            this.nameTxt.setString(global.cropStr(pData.nickName, 5, '...'))
            this.coinsValTxt.setString(pData.pCoins)
            this.magicCell.setVisible(false)
            if(pData.isCanSend) {
                this.jinBiFreePnl.setVisible(false)
            } else {
                this.jinBiFreePnl.setVisible(true)
            }
            let magic = TableConfig.experssion['magic']
            let magicLength = Object.keys(magic).length;
            for (let i=0; i<magicLength; i++) {
                this.onMagicView(magic[i], pData.pid)
            }
        },

        onMagicView: function (data, pid) {
            let cell = this.magicCell.clone()
            cell.setVisible(true)
            this.magicList.pushBackCustomItem(cell)
            cell._sendMsg = {
                num: data.id,
                type: 3,
                toPid: pid,
            }
            cell.setName('magicNum'+data.id)
            cell.getChildByName('magicImg').loadTexture(data.res)
            cell.addClickEventListener(function (sender,dt) {
                GameUtil.delayBtn(sender)
                this.gameSendNews(sender)
            }.bind(this))
        },

        gameSendNews: function (sender) {
            let data = sender._sendMsg
            let pData = appInstance.dataManager().getPlayData().tableData.pTableID
            let msg = {
                'type': data.type,
                'num': data.num,
                'toPid': data.toPid,
                'tableId':pData
            }
            appInstance.gameAgent().tcpGame().ToSendNewsProto(msg)
        },

        updateView: function (tData) {

        },


        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },

        loadUrlImage: function (url, cell) {
            let size = cell.getContentSize();
            cell.retain()
            cc.loader.loadImg(url, null, function (err, img) {
                if ( !err && img) {
                    var logo = new cc.Sprite(img);
                    logo.setContentSize(size)
                    logo.setPosition(cc.p(size.width / 2, size.height / 2))
                    cell.addChild(logo);
                    cell.release()
                }
            }.bind(this));
        },

        onCloseBtnClick: function () {
            appInstance.uiManager().removeUI(this)
        },

        onShowRole: function (roleCode) {
            roleCode = roleCode || 1
            this.aniNd.removeAllChildren()
            let ani = appInstance.gameAgent().gameUtil().getAni(AniPlayer[roleCode])
            this.aniNd.addChild(ani)
            ani.setPosition(cc.p(0,0))
            ani.setScale(0.4)
            ani.setAnimation(0, PlayerPlay.stand, true)

        }
    })
    return Layer
})

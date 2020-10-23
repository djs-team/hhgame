
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
            if(!pData)
                return

            this.onShowRole(pData.pRole)
            this.loadUrlImage(pData.pPhoto,this.photoImg)
            //this.idTxt.setString(pData.pid)
            this.allCntTxt.setString(pData.allCnt)
            this.winCnt.setString(pData.winCnt)
            this.nameTxt.setString(global.cropStr(pData.nickName, 5, '...'))
            this.coinsValTxt.setString(pData.pCoins)
            if(pData.isCanSend)
                this.magicBlockPnl.setVisible(false)
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

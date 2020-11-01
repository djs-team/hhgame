
/**
 * 加载页面，使用场景是网络链接、网络请求
 * 网络请求：在请求前展示该界面，请求返回后隐藏该界面
 */
load('game/ui/public/LoadingLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let LoadingLayer = BaseLayer.extend({
        _className: 'LoadingLayer',
        RES_BINDING: function () {
            return {
                'pnl': {  },
                'pnl/MidNd': { }
            }
        },
        ctor: function () {
            cc.log('===========LoadingLayer================')
            this._super(ResConfig.View.LoadingLayer)
        },
        updateView: function (info) {
            cc.log('===========LoadingLayer======updateView==========')
        },

        initView: function () {
            let jiazaidonghua = appInstance.gameAgent().gameUtil().getAni(ResConfig.AniHall.jiazaidonghua)
            jiazaidonghua.setAnimation(0, 'animation', true)
            this.MidNd.addChild(jiazaidonghua)
        },

        onEnter: function () {
            this._super()
            this.initView()
        },
        onExit: function () {
            this._super()
        }
    })
    return LoadingLayer
})

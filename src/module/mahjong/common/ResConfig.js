
/*
 * 模块资源集
 */
load('module/mahjong/common/ResConfig', function () {
  let PrefixPath = {
    codePath: 'module/mahjong/',
    resPath: 'res/module/mahjong/'
  }

  let ResConfig = {
    /**
     * 模块的脚本资源
     */
    Ui: {
      MjPlayScene: PrefixPath.codePath + 'ui/MjPlayScene',
      DeskBgLayer: PrefixPath.codePath + 'ui/DeskBgLayer',
      DeskCardLayer: PrefixPath.codePath + 'ui/DeskCardLayer',
      DeskHeadLayer: PrefixPath.codePath + 'ui/DeskHeadLayer',
      MatchBigResultLayer: PrefixPath.codePath + 'ui/MatchBigResultLayer',
      MatchJinjiLayer: PrefixPath.codePath + 'ui/MatchJinjiLayer',
      MatchResultLayer: PrefixPath.codePath + 'ui/MatchResultLayer',
    },

    /**
     * 文件资源
     */
    View: {
      MjPlayScene: PrefixPath.resPath + 'MjPlayScene.json',
      DeskBgLayer: PrefixPath.resPath + 'DeskBgLayer.json',
      DeskCardLayer: PrefixPath.resPath + 'DeskCardLayer.json',
      DeskHeadLayer: PrefixPath.resPath + 'DeskHeadLayer.json',
      DeskTopLayer: PrefixPath.resPath + 'DeskTopLayer.json',
      DeskResultLayer: PrefixPath.resPath + 'DeskResultLayer.json',
      MatchBigResultLayer: PrefixPath.resPath + 'MatchBigResultLayer.json',
      MatchJinjiLayer: PrefixPath.resPath + 'MatchJinjiLayer.json',
      MatchResultLayer: PrefixPath.resPath + 'MatchResultLayer.json',


    },
    /**
     *  模块的需要的大图文件
     */
    Atlas: {

    },
    /**
     * 一些动画特效
     */
    Effect: {
      baojing: {
        exportjson: PrefixPath.resPath + 'effect/baojing/yy_baojing.ExportJson',
        plist: PrefixPath.resPath + 'effect/baojing/yy_baojing0.plist',
        png: PrefixPath.resPath + 'effect/baojing/yy_baojing0.png'
      }
    },
    /**
     * 一些声音配置
     */
    sound: {
      play: {
        fapai: PrefixPath.resPath + 'sound/process/fapai.mp3',
        naozhong: PrefixPath.resPath + 'sound/process/naozhong.mp3',
        shengyizhang: PrefixPath.resPath + 'sound/process/shengyizhang.mp3',
        buyao1: PrefixPath.resPath + 'sound/process/buyao1.mp3',
        buyao2: PrefixPath.resPath + 'sound/process/buyao2.mp3',
        buyao3: PrefixPath.resPath + 'sound/process/buyao3.mp3'
      },
      cardtype: {
        feiji: PrefixPath.resPath + 'sound/card/special/feiji.mp3',
        liandui: PrefixPath.resPath + 'sound/card/special/liandui.mp3',
        sandaiyi: PrefixPath.resPath + 'sound/card/special/sandaiyi.mp3',
        sandaiyidui: PrefixPath.resPath + 'sound/card/special/sandaiyidui.mp3',
        shunzi: PrefixPath.resPath + 'sound/card/special/shunzi.mp3',
        zhadan: PrefixPath.resPath + 'sound/card/special/zhadan.mp3'
      },
      path: {
        one: PrefixPath.resPath + 'sound/card/one/',
        two: PrefixPath.resPath + 'sound/card/two/',
        three: PrefixPath.resPath + 'sound/card/three/'
      }
    }
  }

  ResConfig.PrefixPath = PrefixPath
  return ResConfig
})

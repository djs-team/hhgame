
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
      EffectLayer: PrefixPath.codePath + 'ui/EffectLayer',
      MatchBigResultLayer: PrefixPath.codePath + 'ui/MatchBigResultLayer',
      MatchJinjiLayer: PrefixPath.codePath + 'ui/MatchJinjiLayer',
      MatchResultLayer: PrefixPath.codePath + 'ui/MatchResultLayer',
      DeskPersonlLayer: PrefixPath.codePath + 'ui/DeskPersonlLayer',
      ShareLayer: PrefixPath.codePath + 'ui/ShareLayer',
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
      EffectLayer: PrefixPath.resPath + 'EffectLayer.json',
      DeskResultLayer: PrefixPath.resPath + 'DeskResultLayer.json',
      MatchBigResultLayer: PrefixPath.resPath + 'MatchBigResultLayer.json',
      MatchJinjiLayer: PrefixPath.resPath + 'MatchJinjiLayer.json',
      MatchResultLayer: PrefixPath.resPath + 'MatchResultLayer.json',
      DeskPersonlLayer: PrefixPath.resPath + 'DeskPersonlLayer.json',
      ShareLayer: PrefixPath.resPath + 'ShareLayer.json',


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
    Sound: {
      path: PrefixPath.resPath + 'sound/',
      play: {
        begin: [
          'begingaming',
        ],
        hu: [
          'hu_wohule',
          'hu_biedong',
        ],
        huanbao: [
          'huanbao'
        ],
        hu_zimo: [
          'hu_zimo'
        ],
        hu_mobao:[
          'hu_mobao'
        ],
        hu_baozhongbao:[
          'hu_baozhongbao'
        ],
        hu_loubao:[
          'hu_loubao'
        ],
        ting:[
          'ting_kou',
          'ting_wotingle'
        ],
        lose: [
          'lose_junzibaochou',
          'lose_taibeile',
          'lose_yibuxiaoxin'
        ],
        chi: [
            'chi_1',
            'chi_2'
        ],
        gang: [
            'gang_1',
            'gang_2'
        ],
        peng: [
            'peng_1',
            'peng_2'
        ],
        match_begin: [
            'match_begin',
        ],
        match_jinji:[
            'match_jinji_1',
            'match_jinji_2',
            'match_juesai'
        ],
        match_juesai: [
            'match_juesai'
        ],
        match_taotai: [
          'match_taotai',
          'match_taotai_rank'
        ],
        match_taotai_rank: [
          'match_taotai_rank'
        ],
        match_win_1: [
          'match_win_1'
        ],
        match_win_2: [
          'match_win_2'
        ],
        match_win_3: [
          'match_win_3'
        ],
        match_win_4: [
          'match_win_4'
        ],
      },
      kuaijieyu: {
        1: '1kuaidian',
        2: '2hairang',
        3: '3aiya',
        4: '4woyou',
        5: '5hengheng',
        6: '6dajia',
        7: '7shouwan',
        8: '8ningai',
        9: '9nida',
      },
      cardtype: {
        feiji: PrefixPath.resPath + 'sound/card/special/feiji.mp3',
        liandui: PrefixPath.resPath + 'sound/card/special/liandui.mp3',
        sandaiyi: PrefixPath.resPath + 'sound/card/special/sandaiyi.mp3',
        sandaiyidui: PrefixPath.resPath + 'sound/card/special/sandaiyidui.mp3',
        shunzi: PrefixPath.resPath + 'sound/card/special/shunzi.mp3',
        zhadan: PrefixPath.resPath + 'sound/card/special/zhadan.mp3'
      }
    }
  }

  ResConfig.PrefixPath = PrefixPath
  return ResConfig
})


/**
 * 游戏层定义
 */
load('game/config/GameConfig', function () {
    let GameConfig = {}

    GameConfig.HeartBeatInterval = 10

    GameConfig.propType_currency = 1//货币
    GameConfig.propType_role = 2//角色
    GameConfig.propType_kind = 3//实物


    GameConfig.propType_currency_coin = 1//金币
    GameConfig.propType_currency_diamonds = 2//钻石
    GameConfig.propType_currency_fuKa = 3//福卡

    GameConfig.propType_role_caiShen = 1//财神
    GameConfig.propType_role_caoMaoNanHai = 2//草帽男孩
    GameConfig.propType_role_yangGeXiaoNiu = 3//秧歌小妞
    GameConfig.propType_role_faGe = 4//发哥
    GameConfig.propType_role_huangDi = 5//皇帝
    GameConfig.propType_role_huaMuLan = 6//花木兰
    GameConfig.propType_role_huXianXiaoNiu = 7//狐仙小妞
    GameConfig.propType_role_zhangZuoLin = 8//张作霖
    GameConfig.propType_role_zhiZunBao = 9//至尊宝
    GameConfig.propType_role_ziXia = 10//紫霞


    GameConfig.propType_kind_phone = 1//手机
    GameConfig.propType_kind_jdCard = 2//京东卡
    GameConfig.propType_kind_hfCard = 3//话费卡


    GameConfig.QUANTITUINTERVAL_RESULT_LESS = 'least'
    GameConfig.QUANTITUINTERVAL_RESULT_MORE = 'more'
    GameConfig.QUANTITUINTERVAL_RESULT_MOST = 'most'

    GameConfig.QUANTITUINTERVAL_NUM_START = 'start'
    GameConfig.QUANTITUINTERVAL_NUM_END = 'end'

    GameConfig.FUNCTION_NAME_TURNTABLE = 'turntable'
    GameConfig.FUNCTION_NAME_SIGN = 'sign'

    GameConfig.ICON_RESULT_BRIGHT = 'bright'
    GameConfig.ICON_RESULT_ASH = 'ash'
    GameConfig.ICON_RESULT_CURRENCY = 'currency'
    GameConfig.ICON_RESULT_LEAST = 'least'
    GameConfig.ICON_RESULT_MORE = 'more'
    GameConfig.ICON_RESULT_MOST = 'most'

    GameConfig.VIP_LEVEL_0 = '0'//不是vip
    GameConfig.VIP_LEVEL_1 = '1'//周vip
    GameConfig.VIP_LEVEL_2 = '2'//月vip
    GameConfig.VIP_LEVEL_3 = '3'//季vip
    GameConfig.VIP_LEVEL_4 = '4'//年vip













    GameConfig.channel = {
        '10047': 'zhaoyuan',
        'zhaoyuan': '10047',
    }

    GameConfig.jumping = {
        '2': 'CoinGameLayer',
        '3': 'CoinGameLayer',
        '4': 'CoinGameLayer',
        '5': 'CoinGameLayer',
        '6': 'CoinGameLayer',
        '7': 'CoinGameLayer',
        '8': 'CoinGameLayer',
        '9': 'InvitationLayer',
        '10': 'MatchLayer',
        '11': 'TurnTableLayer',
        '12': 'CashCowLayer',
        '13': 'CoinShopLayer',
        '14': 'CoinShopLayer',
        '15': 'ShopLayer',

    }

    GameConfig.mjError = {
        '10' : '游戏已经开始',
        '11' : '牌桌人数已经够了',
        '12' : '玩家正在游戏队列中',
        '13' : '玩家不在游戏队列中',
        '14' : '没有玩家信息',
        '15' : '房卡或者金币不足',
        '16' : '房间不存在',
    }


    GameConfig.memberLever = {

        '0' : '不是会员',
        '1' : '周会员',
        '2' : '月会员',
        '3' : '季会员',
        '4' : '年会员',

    }



    GameConfig.propsRes = {

        '1' : {
            'name': '货币',
            'propCode': {
                '1': {
                    'name': '金币',
                    'currency': 'res/code/props/jinbi1.png',
                    'least': 'res/code/props/jinbi1.png',
                    'more': 'res/code/props/jinbi2.png',
                    'most': 'res/code/props/jinbi3.png',
                }
                ,
                '2': {
                    'name': '钻石',
                    'currency': 'res/code/props/zuanshi1.png',
                    'least': 'res/code/props/zuanshi1.png',
                    'more': 'res/code/props/zuanshi2.png',
                    'most': 'res/code/props/zuanshi3.png',
                }
                ,
                '3': {
                    'name': '福卡',
                    'currency': 'res/code/props/fuka.png',
                }
                ,
            }
        },
        '2' : {
            'name' : '角色',
            'propCode' : {
                '1': {
                    'name': '财神',
                    'brightImg': 'res/code/props/js_cs1.png',
                    'ashImg': 'res/code/props/js_cs1_1.png',
                    'currency': 'res/code/props/canshen.png',
                }
                ,
                '2': {
                    'name': '草帽男孩',
                    'brightImg': 'res/code/props/js_nncs1.png',
                    'ashImg': 'res/code/props/js_nncs1_1.png',
                    'currency': 'res/code/props/chushi1.png',
                }

                ,
                '3': {
                    'name': '秧歌小妞',
                    'brightImg': 'res/code/props/js_ncs1.png',
                    'ashImg': 'res/code/props/js_ncs1_1.png',
                    'currency': 'res/code/props/chushi2.png',
                }

                ,
                '4': {
                    'name': '发哥',
                    'brightImg': 'res/code/props/js_fg1.png',
                    'ashImg': 'res/code/props/js_fg1_1.png',
                    'currency': 'res/code/props/fage.png',
                }

                ,
                '5': {
                    'name': '皇帝',
                    'brightImg': 'res/code/props/js_hd1.png',
                    'ashImg': 'res/code/props/js_hd1_1.png',
                    'currency': 'res/code/props/hangdi.png',
                }

                ,
                '6': {
                    'name': '花木兰',
                    'brightImg': 'res/code/props/js_hml1.png',
                    'ashImg': 'res/code/props/js_hml1_1_1.png',
                    'currency': 'res/code/props/huamulan.png',
                }

                ,
                '7': {
                    'name': '狐仙小妞',
                    'brightImg': 'res/code/props/js_hx1.png',
                    'ashImg': 'res/code/props/js_hx1_1.png',
                    'currency': 'res/code/props/huxian.png',
                }

                ,
                '8': {
                    'name': '张作霖',
                    'brightImg': 'res/code/props/js_zzl1.png',
                    'ashImg': 'res/code/props/js_zzl1_1.png',
                    'currency': 'res/code/props/zhangzuolin.png',
                }

                ,
                '9': {
                    'name': '至尊宝',
                    'brightImg': 'res/code/props/js_zzb1.png',
                    'ashImg': 'res/code/props/js_zzb1_1.png',
                    'currency': 'res/code/props/zhizunbao.png',
                }

                ,
                '10': {
                    'name': '紫霞',
                    'brightImg': 'res/code/props/js_zx1.png',
                    'ashImg': 'res/code/props/js_zx1_1.png',
                    'currency': 'res/code/props/zixia.png',
                }

                ,

            }
        },
        '3' :  {
            'name' : '实物',
            'propCode' : {

                '1': {
                    'name': '手机',
                    'currency': 'res/code/props/zphd_12.png',
                }
                ,
                '2': {
                    'name': '京东卡',
                    'currency': 'res/code/props/zphd_12.png',
                }

                ,
                '3': {
                    'name': '话费卡',
                    'currency': 'res/code/props/zphd_12.png',
                }

                ,
            }
        },


    }


    GameConfig.quantityInterval = {
        'turntable' : {
            '1' : {//大类，货币
                '1' : {//小类，金币
                    'least' : {
                        'end' : '3999'
                    },
                    'more' : {
                        'start' : '4000',
                        'end' : '6999'
                    },
                    'most' : {
                        'start' : '7000',
                    }
                },
                '2' : {//小类，钻石
                    'least' : {
                        'end' : '19'
                    },
                    'more' : {
                        'start' : '20',
                        'end' : '29'
                    },
                    'most' : {
                        'start' : '30',
                    }
                },
            },


        },
        'sign' : {

            '1' : {//大类，货币
                '1' : {//小类，金币
                    'least' : {
                        'end' : '3000'
                    },
                    'more' : {
                        'start' : '3001',
                        'end' : '5000'
                    },
                    'most' : {
                        'start' : '5001',
                    }
                },
                '2' : {//小类，钻石
                    'least' : {
                        'end' : '10'
                    },
                    'more' : {
                        'start' : '11',
                        'end' : '20'
                    },
                    'most' : {
                        'start' : '21',
                    }
                },
            },


        },

    }

    return GameConfig
})

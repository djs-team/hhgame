
/**
 * 游戏层定义
 */
load('game/config/GameConfig', function () {
    let GameConfig = {}

    GameConfig.HeartBeatInterval = 15

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
    GameConfig.propType_kind_hb = 4//现金红包


    GameConfig.QUANTITUINTERVAL_RESULT_LESS = 'least'
    GameConfig.QUANTITUINTERVAL_RESULT_MORE = 'more'
    GameConfig.QUANTITUINTERVAL_RESULT_MOST = 'most'

    GameConfig.QUANTITUINTERVAL_NUM_START = 'start'
    GameConfig.QUANTITUINTERVAL_NUM_END = 'end'

    GameConfig.FUNCTION_NAME_TURNTABLE = 'turntable'
    GameConfig.FUNCTION_NAME_SIGN = 'sign'

    GameConfig.ICON_RESULT_BRIGHT = 'brightImg'
    GameConfig.ICON_RESULT_ASH = 'ashImg'
    GameConfig.ICON_RESULT_CURRENCY = 'currency'
    GameConfig.ICON_RESULT_LEAST = 'least'
    GameConfig.ICON_RESULT_MORE = 'more'
    GameConfig.ICON_RESULT_MOST = 'most'

    GameConfig.VIP_LEVEL_0 = 0//不是vip
    GameConfig.VIP_LEVEL_1 = 1//周vip
    GameConfig.VIP_LEVEL_2 = 2//月vip
    GameConfig.VIP_LEVEL_3 = 3//季vip
    GameConfig.VIP_LEVEL_4 = 4//年vip

    GameConfig.CHANNEL = 10001 //大庆
    GameConfig.CHANNEL = 10037 //嫩江
    GameConfig.CHANNEL = 10047 //肇源













    GameConfig.channel = {
        '10047': 'zhaoyuan',
        'zhaoyuan': '10047',
        '10001': 'daqing',
        'daqing': '10001',
    }

    GameConfig.areaName = {
        'zhaoyuan': '肇源麻将',
        'daqing': '大庆麻将'
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
        '10': 'ArenaLayer',
        '11': 'TurnTableLayer',
        '12': 'CashCowLayer',
        '13': 'CoinShopLayer',
        '14': 'CoinShopLayer',
        '15': 'FukaShopLayer',

    }

    GameConfig.channel_animation = {
        '10001': 'animation5',
        '10037': 'animation',
        '10047': 'animation6',

    }

    GameConfig.mjError = {
        '10' : '游戏已经开始',
        '11' : '牌桌人数已经够了',
        '12' : '玩家正在游戏队列中',
        '13' : '玩家不在游戏队列中',
        '14' : '没有玩家信息',
        '15' : '金币不足',
        '16' : '房间不存在',
        '17' : '服务器维护中',
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
                    'ashImg': 'res/code/props/js_hml1_1.png',
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
                '4': {
                    'name': '现金红包',
                    'currency': 'res/code/props/zphd_12.png',
                }
                ,
            }
        },


    }

    GameConfig.feedbackRes = {
        '0' : {
            'id': 1,
            'question': '1.VIP特权',
            'answer': '可获得专属会员大奖赛参赛资格\n' +
                '每日可领取专属福利礼包\n' +
                '免费获取专属尊贵角色\n' +
                '领取更多倍数签到奖励\n' +
                '与众不同的专属尊贵会员标示\n' +
                '同城相亲玩法畅游',
            'num': 7
        },
        '1' : {
            'id': 2,
            'question': '2.获取权限',
            'answer': '首次登陆游戏会提示是否允许使用相关权限，请勾选同意，否则可能会造成无法获取位置，没有音效等问题哦',
            'num': 3
        },
        '2' : {
            'id': 3,
            'question': '3.如何成为代理，成为代理后是怎么收益的？',
            'answer': '您可以联系客服开通代理权限，成为代理后通过您的专属二维码邀请玩家，玩家在游戏内充值会员，观看广告视频，直播间打赏礼物都会有您的分成，另外您邀请的玩家再次邀请玩家，您同样可以获得分成',
            'num': 5
        },
        '3' : {
            'id': 4,
            'question': '4.快速盈利',
            'answer': '邀请更多玩家，通过线上及线下广告方式发布出您的专属二维码，下载人数越多，盈利越快哦，且后续无需任何管理',
            'num': 3
        },
        '4' : {
            'id': 5,
            'question': '5.比赛场',
            'answer': '普通比赛场可点击大厅内的赛事场进行报名参加，VIP专属会员大奖赛，需要您成为会员后才可进行报名参加哦，成为当月会员，当月内所有当前城市会员大奖赛都可参加！',
            'num': 4
        },
        '5' : {
            'id': 6,
            'question': '6.实物奖品的兑换',
            'answer': '您可以点击大厅内的福卡商城，选择您想要兑换的商品，使用福卡进行兑换，记得填写正确的收货信息哦',
            'num': 3
        },
        '6' : {
            'id': 7,
            'question': '7.角色获得',
            'answer': '角色可以通过累计签到，转盘抽奖，货币购买获得，另外成为vip还可免费获得vip专属角色哦！',
            'num': 3
        },
        '7' : {
            'id': 8,
            'question': '8.货币获取',
            'answer': '您可以通过签到，每日任务，各类活动以及观看广告视频获得金币钻石，可以通过比赛场获得大量福卡。另外成为vip还可每日免费领取大量奖励哦！',
            'num': 4
        },
        '8' : {
            'id': 9,
            'question': '9.同城相亲及盈利',
            'answer': '可联系客服申请成为主播，可获取收到礼物的分成，另外成为代理邀请的玩家充值购买礼物，代理也可获得分成哦',
            'num': 3
        },
        '9': {
            'id': 10,
            'question': '10.游戏闪退',
            'answer': '网络信号不佳可能会导致游戏闪退，建议在网络通畅时进行游戏，另外应用程序打开过多，缓存过多也可能会导致游戏闪退，可关闭其他后台程序，清理设备缓存后重新登陆游戏尝试。',
            'num': 5
        },
        '10': {
            'id': 11,
            'question': '11.切换城市',
            'answer': '您可以点击大厅内的更多玩法选择其他城市进行切换',
            'num': 2
        },
        '11': {
            'id': 12,
            'question': '12.无法登陆',
            'answer': '请确认是否为麻将情缘应用打开了网络，另外网络不稳定可能会导致登陆失败，请尝试更换网络链接方式后登陆（切换WIFI/4G网络）。若以上方法仍无法解决，您可以通过问题反馈或联系人工客服详细描述您遇到的问题，我们将会及时处理',
            'num': 5
        },
        '12': {
            'id': 13,
            'question': '13.游戏没有声音',
            'answer': '请先确认是否开启了设备音量，如确认开启仍没有声音，可重新登陆游戏进行尝试',
            'num': 3
        },
    }

    GameConfig.turnTableRewardsRes = {

        '1' : {
            'name': '货币',
            'propCode': {
                '1': {
                    'name': '金币',
                    'currency': 'res/turntable/zphd_10.png',
                    'least': 'res/turntable/zphd_10.png',
                    'more': 'res/turntable/zphd_17.png',
                    'most': 'res/turntable/zphd_19.png',
                }
                ,
                '2': {
                    'name': '钻石',
                    'currency': 'res/turntable/zphd_6.png',
                    'least': 'res/turntable/zphd_6.png',
                    'more': 'res/turntable/zphd_7.png',
                    'most': 'res/turntable/zphd_8.png',
                }
                ,
                '3': {
                    'name': '福卡',
                    'currency': 'res/turntable/zphd_18.png',
                }
                ,
            }
        },
        '2' : {
            'name' : '角色',
            'propCode' : {
                '1': {
                    'name': '财神',
                    'currency': 'res/turntable/zphd_26.png',
                }
                ,
                '2': {
                    'name': '草帽男孩',
                    'currency': 'res/turntable/zphd_26.png',
                }

                ,
                '3': {
                    'name': '秧歌小妞',
                    'currency': 'res/turntable/zphd_27.png',
                }

                ,
                '4': {
                    'name': '发哥',
                    'currency': 'res/turntable/zphd_22.png',
                }

                ,
                '5': {
                    'name': '皇帝',
                    'currency': 'res/turntable/zphd_23.png',
                }

                ,
                '6': {
                    'name': '花木兰',
                    'currency': 'res/turntable/zphd_24.png',
                }

                ,
                '7': {
                    'name': '狐仙小妞',
                    'currency': 'res/turntable/zphd_27.png',
                }

                ,
                '8': {
                    'name': '张作霖',
                    'currency': 'res/turntable/zphd_25.png',
                }

                ,
                '9': {
                    'name': '至尊宝',
                    'currency': 'res/turntable/zphd_29.png',
                }

                ,
                '10': {
                    'name': '紫霞',
                    'currency': 'res/turntable/zphd_28.png',
                }

                ,

            }
        },
        '3' :  {
            'name' : '实物',
            'propCode' : {

                '1': {
                    'name': '手机',
                    'currency': 'res/turntable/zphd_12.png',
                }
                ,
                '2': {
                    'name': '京东卡',
                    'currency': 'res/turntable/zpjd_1.png',
                }
                ,
                '3': {
                    'name': '话费卡',
                    'currency': 'res/turntable/zphf_1.png',
                }

                ,
            }
        },


    }


    GameConfig.signRewardsRes = {

        '1' : {
            'name': '货币',
            'propCode': {
                '1': {
                    'name': '金币',
                    'currency': 'res/sign/qd_jl_13.png',
                    'least': 'res/sign/qd_jl_13.png',
                    'more': 'res/sign/qd_jl_11.png',
                    'most': 'res/sign/qd_jl_9.png',
                }
                ,
                '2': {
                    'name': '钻石',
                    'currency': 'res/sign/qd_jl_12.png',
                    'least': 'res/sign/qd_jl_12.png',
                    'more': 'res/sign/qd_jl_10.png',
                    'most': 'res/sign/qd_jl_10.png',
                }
                ,
                '3': {
                    'name': '福卡',
                    'currency': 'res/sign/zphd_18.png',
                }
                ,
            }
        },
        '2' : {
            'name' : '角色',
            'propCode' : {
                '1': {
                    'name': '财神',
                    'currency': 'res/sign/qd_js_6.png',
                }
                ,
                '2': {
                    'name': '草帽男孩',
                    'currency': 'res/sign/qd_js_6.png',
                }

                ,
                '3': {
                    'name': '秧歌小妞',
                    'currency': 'res/sign/qd_js_4.png',
                }

                ,
                '4': {
                    'name': '发哥',
                    'currency': 'res/sign/qd_js_2.png',
                }

                ,
                '5': {
                    'name': '皇帝',
                    'currency': 'res/sign/qd_js_3.png',
                }

                ,
                '6': {
                    'name': '花木兰',
                    'currency': 'res/sign/qd_js_8.png',
                }

                ,
                '7': {
                    'name': '狐仙小妞',
                    'currency': 'res/sign/qd_js_7.png',
                }

                ,
                '8': {
                    'name': '张作霖',
                    'currency': 'res/sign/qd_js_5.png',
                }

                ,
                '9': {
                    'name': '至尊宝',
                    'currency': 'res/sign/qd_js_1.png',
                }

                ,
                '10': {
                    'name': '紫霞',
                    'currency': 'res/sign/qd_js_8.png',
                }

                ,

            }
        },
        '3' :  {
            'name' : '实物',
            'propCode' : {

                '1': {
                    'name': '手机',
                    'currency': 'res/sign/zphd_12.png',
                }
                ,
                '2': {
                    'name': '京东卡',
                    'currency': 'res/sign/zpjd_1.png',
                }
                ,
                '3': {
                    'name': '话费卡',
                    'currency': 'res/sign/zphf_1.png',
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

    GameConfig.goBtnData = [
        { name: '新手场', score: 500, cost: '2000-6万', minCost: 2000, maxCost: 60000, gameType: 'M1'},
        { name: '初级场', score: 2000, cost: '6万-24万', minCost: 60000, maxCost: 240000, gameType: 'M2'},
        { name: '高级场', score: 5000, cost: '24万-60万', minCost: 240000, maxCost: 600000, gameType: 'M3'},
        { name: '大师场', score: 20000, cost: '60万以上', minCost: 600000, gameType: 'M4'},
    ]

    return GameConfig
})

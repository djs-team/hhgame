
/**
 * 牌桌层定义
 */
load('module/mahjong/common/TableConfig', function () {
    let TableConfig = {}

    TableConfig.Event = {
        'clearTableView': 'clearTableView',
        'initTableView': 'initTableView',
        'UpdateView': 'UpdateView',
        'prePutCard': 'prePutCard',
        'InitCardProto': 'InitCardProto',
        'DrawCardProto': 'DrawCardProto',
        'updateSelfHandCard': 'updateSelfHandCard',
        'PutCardProto': 'PutCardProto',
        'TableChangeProto': 'TableChangeProto',
        'PlayerSelectProto': 'PlayerSelectProto',
        'GameResultProto': 'GameResultProto',
        'AutoPlayProto': 'AutoPlayProto',
        'TableHostingProto': 'TableHostingProto',
        'MatchJinjiGaming': 'MatchJinjiGaming',
        'MatchResultProto': 'MatchResultProto',
        'MatchResultBigProto': 'MatchResultBigProto',
        'clearTableGaming': 'clearTableGaming',
        'JiaGangTableProto': 'JiaGangTableProto',
        'MatchReadyProto': 'MatchReadyProto',
        'MatchEnterTableProto': 'MatchEnterTableProto'
    }


    TableConfig.UiSeatArray = {
        '2' : [0, 2],
        '4' : [0, 1, 2, 3]
    }

    TableConfig.CardConfig = {
        'zhaoyuan': {
            allCardNum: 112,
            deckCardLen: 14
        },
        'daqing': {
            allCardNum: 112,
            deckCardLen: 14
        }
    }

    TableConfig.TStatus = {
        'play': 1,//开始玩
        'drawCard': 2,//摸牌
        'putCard': 3,//出牌
        'select': 4,//选择
        'over': 5,//结束
    }

    TableConfig.CardColor = [
        'wan',
        'tong',
        'tiao',
        'dong',
        'nan',
        'xi',
        'bei',
        'zhong',
        'fa',
        'bai'
    ]
    // 不想展示 就配置为空字符串
    TableConfig.HuType = {
        '0': '上庄',
        '1': '门清',
        '3': '夹胡',
        '4': '自摸胡',
        '5': '对宝胡',
        '8': '七小对',
        '13': '海底捞月',
        '14': '点炮',
        '35': '摸宝胡',//摸宝胡：自摸宝牌且胡牌张!=宝牌
        '37': '黑炮',
        '43': '调胡',
        '44': '点炮胡',
        '48': '上听',
        '49': '未上听',
        '68': '刮大风',
        '81': '开牌炸',//开牌炸  刚起牌就有四张一样的
        '83': '宝中宝',
        '95': '前后坎',
    }

    //0:明杠or1:暗杠or2:加杠or3:旋风杠or4:幺腰杠or5:幺九杠6：喜儿 7:甩幺

    TableConfig.Select = {
        '10': {
            'id': 10,
            'isBtn': false,
            'desc': '必须出的一些牌'
        },
        '20': {
            'id': 20,
            'isBtn': true,
            'img': 'res/module/mahjong/code/select/hu.png',
            'desc': '胡'
        },
        '40': {
            'id': 40,
            'isBtn': true,
            'img': 'res/module/mahjong/code/select/pengting.png',
            'desc': '碰听'
        },
        '50': {
            'id': 50,
            'isBtn': true,
            'img': 'res/module/mahjong/code/select/chiting.png',
            'desc': '吃听',
        },
        '70': {
            'id': 70,
            'isBtn': true,
            'img': 'res/module/mahjong/code/select/ting.png',
            'desc': '听'
        },
        '80': {
            'id': 80,
            'isBtn': true,
            'showType': 3,
            'img': 'res/module/mahjong/code/select/gang.png',
            'desc': '暗杠'
        },
        '90': {
            'id': 90,
            'isBtn': true,
            'showType': 3,
            'img': 'res/module/mahjong/code/select/gang.png',
            'desc': '加杠'
        },
        '100': {
            'id': 100,
            'isBtn': true,
            'showType': 3,
            'img': 'res/module/mahjong/code/select/gang.png',
            'desc': '明杠'
        },
        '110': {
            'id': 110,
            'isBtn': true,
            'img': 'res/module/mahjong/code/select/peng.png',
            'showType': 2,
            'desc': '碰'
        },
        '120': {
            'id': 120,
            'isBtn': true,
            'img': 'res/module/mahjong/code/select/chi.png',
            'showType': 1,
            'desc': '吃'
        },
        '130': {
            'id': 0,
            'isBtn': true,
            'img': 'res/module/mahjong/code/select/guo.png',
            'desc': '过'
        }
    }

    TableConfig.experssion = {
        'express':{
            '0':{
                'id':1,
                'res':'res/module/mahjong/desk/z_1.png',
            },
            '1':{
                'id':2,
                'res':'res/module/mahjong/desk/z_1.png',
            },
            '2':{
                'id':3,
                'res':'res/module/mahjong/desk/z_1.png',
            },
            '3':{
                'id':4,
                'res':'res/module/mahjong/desk/z_1.png',
            },
            '4':{
                'id':5,
                'res':'res/module/mahjong/desk/z_1.png',
            },
            '5':{
                'id':6,
                'res':'res/module/mahjong/desk/z_1.png',
            },
            '6':{
                'id':7,
                'res':'res/module/mahjong/desk/z_1.png',
            },
            '7':{
                'id':8,
                'res':'res/module/mahjong/desk/z_1.png',
            }
        },
        'say':{
            'puSay':{
                '0':{
                    'id':1,
                    'text':'快点啊，我等到花都谢了'
                },
                '1':{
                    'id':2,
                    'text':'还让不让我摸牌了'
                },
                '2':{
                    'id':3,
                    'text':'哎呀一不小心就胡了'
                },
                '3':{
                    'id':4,
                    'text':'我有一百种胡法而你却无可奈何'
                },
                '4':{
                    'id':5,
                    'text':'哼哼，这牌打得行啊，一口都吃不上'
                },
                '5':{
                    'id':6,
                    'text':'等待资源'
                },
                '6':{
                    'id':7,
                    'text':'收完秋，人有样，一天两场小麻将'
                },
                '7':{
                    'id':8,
                    'text':'宁挨千刀万剐，不胡第一把'
                },
                '8':{
                    'id':9,
                    'text':'你的牌打得也太好了'
                }
            },
            'dongSay':{
                '0':{
                    'id':1,
                    'text':'干啥呢，傻楞的啊'
                },
                '1':{
                    'id':2,
                    'text':'还让不让我摸牌了'
                },
                '2':{
                    'id':3,
                    'text':'哎呀一不小心就胡了'
                },
                '3':{
                    'id':4,
                    'text':'我有一百种胡法而你却无可奈何'
                },
                '4':{
                    'id':5,
                    'text':'哼哼，这牌打得行啊，一口都吃不上'
                },
                '5':{
                    'id':6,
                    'text':'等待资源'
                },
                '6':{
                    'id':7,
                    'text':'收完秋，人有样，一天两场小麻将'
                },
                '7':{
                    'id':8,
                    'text':'宁挨千刀万剐，不胡第一把'
                },
                '8':{
                    'id':9,
                    'text':'你的牌打得也忒好了'
                },
            }
        },
    }

    return TableConfig
})


var MatchWrongCode = {
    kCode_MatchFull:601,                //比赛人满
    kCode_MatchFeeShort:602,            //比赛费用不足
    kCode_NotInMatch: 603,              //玩家不在比赛中
    kCode_MatchExist: 604,              //比赛已存在
    kCode_MatchStopReg: 605,            //比赛停止报名
    kCode_MatchNotConfig: 606,          //比赛未配置
    kCode_AlreadyInMatch: 607,          //玩家已经在比赛中
    kCode_MatchNotExist: 608,           //比赛不存在
    kCode_MatchCreateLock: 609,         //比赛创建锁
    kCode_MatchCreateRedisLock: 610,    //比赛创建锁
    kCode_AlreadyInRoom: 38,            //已在好友房中
    kCode_OverMaxCount:613,             //达到最大场次
};

var CoinRoomType = {
    ERMJ :1,
    SRMJ :2,
    DDA  :3,
};

var BlockLayer = cc.Layer.extend({
    sprite: null,
    jsBind: {
        block: {
            _layout: [[1, 1], [0.5, 0.5], [0, 0], true, false, true]
        },
        Panel_loading: {
            _layout: [[1, 1], [0.5, 0.5], [0, 0], true, false, true],
            _visible: function () {
                return false;
            },
            Armloading: {
            }
        },
        gameCenter:{
            _layout: [[124 / 1920, 0], [0.95, 0.9], [0, 0]],
            _run:function(){
                if(typeof ppgamecenter !== "undefined"){
                    this.visible = true;
                }else{
                    this.visible = false;
                }
            },
            _click : function() {
                //游戏大厅
                if (typeof ppgamecenter != "undefined") { //退出登陆, 适配游戏中心
                    try {
                        ppgamecenter.setGameState({type: 'xynmmj', state:-1});
                        ppgamecenter.changeGameByUser("PPGame");
                        return;
                    } catch (error) {

                    }
                }
            }
        }
    },
    ctor: function () {
        this._super();
        var blockui = ccs.load(res.Block_json);
        ConnectUI2Logic(blockui.node, this.jsBind);
        this.addChild(blockui.node);
        jsclient.blockui = this;
        jsclient.native.ShowLogOnJava("------------------------------111-0");
        return true;
    },
    onEnter: function () {
        this._super();
        jsclient.block = function () {

            if(jsclient.blockui.zIndex == 1000){
                return;
            }

            //cc.log("--------------- 调用 block   ----------------------------");


            jsclient.blockui.jsBind.Panel_loading._node.visible = false;
            jsclient.blockui.zIndex = 1000;
            jsclient.blockui.visible = true;

            jsclient.blockui.runAction(cc.sequence(
                cc.delayTime(1),
                cc.callFunc(function(){
                    //cc.log("--------------- 开始转圈 ----------------------------");
                    jsclient.blockui.jsBind.Panel_loading._node.setVisible(true);
                })
            ))
        }

        jsclient.unblock = function () {

            if(jsclient.blockui.zIndex == -1000){
                return;
            }

            //cc.log("--------------- 解除 block   ----------------------------");


            jsclient.blockui.stopAllActions();
            jsclient.blockui.zIndex = -1000;
            jsclient.blockui.visible = false;
        }
        jsclient.unblock();
    }
});

jsclient.loadWxHead = function (uid, url) {
    if (!url) {
        url = "res/aress/gameInner/common_default.png";
    }
    if (uid && url) {
        cc.loader.loadImg(url, {isCrossOrigin: true}, function (err, texture) {
            if (!err && texture) {
                sendEvent("QueueNetMsg", ["loadWxHead", {uid: uid, img: texture}]);
            }
        });
    }
}

jsclient.hasFormattedAddress= function()
{
    return true;
}

function onceOpenByDay() {
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日
    var startTime0 = ""+year+month+day;

    var playTime = sys.localStorage.getItem("openAdTime");
    if (playTime == null || "" == playTime || playTime != startTime0) {
        sys.localStorage.setItem("openAdTime",startTime0);
        return true;
    }
    return false;
}

jsclient.hasBaiDuLocation = function()
{
    return jsclient.hasBaiDuLoc;
}
jsclient.hasAliDunSDK = function()
{
    //C++底层支持
    var isCSupport = ("undefined" != typeof (jsb.fileUtils.getXXSecretData));
    var isPlatform = cc.sys.isMobile;
    return isCSupport && isPlatform;
};

jsclient.getRealPlayerCount = function () {
    var sData = jsclient.data.sData;
    if(typeof (sData) =="undefined")    return;
    var count = 0;
    for(var i=0;i<sData.tData.uids.length;i++){
        if(sData.tData.uids[i]!=0){
            count++;
        }
    }
    return count;
};

function checkNNPlayerNum() {
    var sData = jsclient.data.sData;
    var tData = sData.tData;
    var trueLength = 0;
    for (var i = 0; i < tData.uids.length; i++) {
        if (tData.uids[i] == 0) {
            continue;
        }
        else {
            trueLength++;
        }
    }
    return trueLength;
}

function playEffectNN(sd) {
    mylog("f --- playEffectNN --- sd:" + sd);
    return cc.audioEngine.playEffect(sd, false);
}
//牛牛新增  start
function playNewEffect(sd){
    // if (cc.sys.OS_WINDOWS != cc.sys.os) {
    return cc.audioEngine.playEffect("res/sound/" + sd + ".mp3", false);
    // }
}

function getPlayerByIDX(idx) {
    var sData = jsclient.data.sData;
    return sData.players[sData.tData.uids[idx]];
}
//牛牛新增  end
function playMusicNN(sd) {
    function play() {
        cc.audioEngine.stopMusic();
        cc.audioEngine.playMusic(sd, true);
    }

    play();
}

jsclient.loadClipWxHead = function(node, url, posx, posy, scalex, scaley, zindex, name, tag)
{
    console.log("app.js---loadClipWxHead------")
    if(node == null)
        return;

    if(scalex == null)
        scalex = 100;

    if(scaley == null)
        scaley = 100;

    if(zindex == null)
        zindex = 0;

    if(name == null)
        name = "";

    if(tag == null)
        tag = 0;

    var isDefaultImg = false;
    if(!url){
        url = "res/aress/gameInner/common_default.png";
        isDefaultImg = true;
    }

    ClipHead(node, url, posx, posy, scalex, scaley, zindex, name, tag, "",isDefaultImg);
};


jsclient.loadClipWxHead_DDA = function(node, url, posx, posy, scalex, scaley, zindex, name, tag, _typeurl,isDefaultImg)
{
    console.log("app.js---loadClipWxHead_DDA------")
    if(node == null)
        return;

    if(scalex == null)
        scalex = 128;

    if(scaley == null)
        scaley = 124;

    if(zindex == null)
        zindex = 0;

    if(name == null)
        name = "";

    if(tag == null)
        tag = 0;

    var isDefaultReal = false;
    if(!isValid(url))
    {
        url = "res/aress/gameInner/common_default.png";
        isDefaultReal = true;
    }

    ClipHead(node, url, posx, posy, scalex, scaley, zindex, name, tag, _typeurl,isDefaultImg||isDefaultReal);
};


// //加载默认头像
// function loadDefaultHead(node, posx, posy, scale, zindex, name, tag)
// {
//     url = "res/common/common_default.png";
//     var sprite = new cc.Sprite(url);
//     sprite.x = posx;
//     sprite.y = posy;
//     sprite.scale = scale;
//     sprite.zindex = zindex;
//     sprite.name = name;
//     sprite.tag = tag;

//     node.addChild(sprite);
//     log("加载默认头像...");
// }

function showRadarAnimation(ipData)
{
    var createRoomAnim = playAnimByJson("leida","leida");
    var tempY = cc.winSize.height/480
    createRoomAnim.setPosition(cc.p(cc.winSize.width/2,cc.winSize.height/2+tempY*100));
    jsclient.Scene.addChild(createRoomAnim);

    createRoomAnim.scaleX =  jsclient.size.width/720*0.5;
    createRoomAnim.scaleY =  createRoomAnim.scaleX;


    var label1 = new cc.Label();
    label1.setString("未发现");
    label1.setColor(cc.color(65,215,1,84));
    label1.setPosition(cc.p(135,30))
    label1.setSystemFontSize(28);
    createRoomAnim.addChild(label1,99);
    label1.setVisible(false);

    var label2 = new cc.Label(); 
    label2.setString("正常");
    label2.setColor(cc.color(65,215,1,84));
    label2.setPosition(cc.p(35,-30))
    label2.setSystemFontSize(28);
    createRoomAnim.addChild(label2,99);
    label2.setVisible(false);
    label2.setAnchorPoint(0,0.5);
    if(ipData.length>1)
    {
        label2.setString("有"+ipData.length +"个IP相同");
        label2.setColor(cc.color(255,0,0,255));
    }

    label1.runAction(
        cc.sequence(
            cc.delayTime(1),
            cc.callFunc(
                function () {
                    label1.setVisible(true);
                }),
            cc.delayTime(1),
            cc.callFunc(
                function () {
                    label2.setVisible(true);
                }),
            cc.delayTime(2),
            cc.callFunc(
                function () {
                    label1.stopAllActions();
                    label2.stopAllActions();
                    createRoomAnim.removeFromParent();
                })))

    //高德定位检测
    _CalculateGodDataFun();
}

//错误提示准换文字
function handleMsgCode(code)
{
    var errorDes = "";
    switch (code)
    {
        case 2:
        {
            errorDes = "请求数据过程中出现异常";
            break;
        }
        case 3:
        case 4:
        {
            errorDes = "操作过程中出现异常";
            break;
        }
        case 11:
        {
            errorDes = "链接服务器失败,请重新连接";
            break;
        }
        case 15:
        {
            errorDes = "读取配置错误";
            break;
        }
        case 16:
        {
            errorDes = "登录链接已断开,请重新连接";
            break;
        }
        case 17:
        {
            errorDes = "游戏数据更新失败,请再次更新";
            break;
        }
        default:
        {
            errorDes = "网络连接断开,请检查网络设置，重新连接";
        }
    }
    jsclient.showMsg(errorDes,function()
    {
        if(code == 11 || code == 17)
            jsclient.removeUpdateRes();
        else
            jsclient.restartGame();
    },function () {},"1")
}

//玩家是否为游戏玩家
jsclient.playerIsPlayer = function (uid) {
    if (!jsclient.data.sData)
        return false;
    var players = jsclient.data.sData.players;
    if (players && players[uid])
        return true;
    else
        return false;
};
//玩家是否为观战玩家
jsclient.playerIsObserver = function (uid) {
    var observers = jsclient.data.sData.observers;
    if (observers && observers[uid])
        return true;
    else
        return false;
};
//自己为游戏玩家
jsclient.selfIsPlayer = function () {
    return jsclient.playerIsPlayer(SelfUid());
};
//自己为观战玩家
jsclient.selfIsObserver = function () {
    return jsclient.playerIsObserver(SelfUid());
};

//一键修复客户端
jsclient.removeUpdateRes = function() {
    var basePath = jsb.fileUtils.getWritablePath() + "update/";

    if(typeof ppgamecenter != "undefined"){ //一键清理, 适配游戏中心
        jsclient.showMsg("删除内蒙麻将返回游戏",function () {
            ppgamecenter.update333 = true;
            try {
                ppgamecenter.changeGameByUser("PPGame");
            }catch (error){
    
            }
        },function () {

        },"2")
    }else {

        var basePath = jsb.fileUtils.getWritablePath();
        var time = new Date();
        jsb.fileUtils.renameFile(basePath+"update/",basePath+"update_" + time.getTime() + "/");
        jsclient.restartGame();
    }

}




//-------------------------------------------休闲场--------------------------------------------

var CoinData = {
    type: 0, //类型
    cost: 1, //获得每局消耗
    win: 2, //获得底分
    min: 3, //获得进入下限
    max: 4 //获得进入上限
};
jsclient.showcoinRoomChoose = function () {
    if (jsclient.chooseCoinRoomui) return;
    jsclient.Scene.addChild(new ChooseCoinRoomLayer());
}
jsclient.showcoinRoomLayer = function () {
    if (jsclient.coinRoomLayerUI) return;
    if (typeof(coinRoomLayer) != "undefined") {
        jsclient.Scene.addChild(new coinRoomLayer());
    }
}
/*获得休闲场的一些数据
 index = 1  获得底分
 index = 2  获得进入下限
 index = 3  获得进入上限
 index = 4  获得每局消耗
 */
jsclient.getCoinConfig = function (coinRoomCreate, iCoinData) {

    if (!jsclient.coinCfg) {
        var iPath = jsb.fileUtils.getWritablePath() + "coinRoomCfg";
        if (jsb.fileUtils.isFileExist(iPath)) {
            cc.loader.loadTxt(iPath, function (er, txt) {
                if (txt && txt.length > 0) {
                    jsclient.coinCfg = JSON.parse(txt);
                } else {
                    return 1;
                }
            });
        }
        else {
            return 3;
        }
    }

    if (coinRoomCreate < 0 || coinRoomCreate > jsclient.coinCfg.length) {
        return 2;
    }

    //cc.log("wcx9_iCoinPara coinRoomCreate=" + coinRoomCreate + " iCoinData=" + iCoinData);

    // var iCoinPara = JSON.parse(jsclient.coinCfg[0]);
    var iCoinPara;
    try {
        iCoinPara = JSON.parse(jsclient.coinCfg[coinRoomCreate - 1]);
    } catch (error) {
        // jsclient.coinCfg = cc.sys.localStorage.getItem("CoinRoomCfg");
        iCoinPara = JSON.parse(jsclient.coinCfg[0]);
    }

    //cc.log("iCoinPara------------> "+JSON.stringify(iCoinPara));

    // var iCoinPara = JSON.parse(jsclient.coinCfg[coinRoomCreate - 1]); //休闲场分等级进入 add by zhengwei
    var iRet = iCoinPara.type;

    if (iCoinData == CoinData.type) {
        iRet = iCoinPara.type;
    } else if (iCoinData == CoinData.cost) {
        iRet = iCoinPara.cost;
    } else if (iCoinData == CoinData.win) {
        iRet = iCoinPara.win;
    } else if (iCoinData == CoinData.min) {
        iRet = iCoinPara.min;
    } else if (iCoinData == CoinData.max) {
        iRet = iCoinPara.max;
    }
    // cc.log("zhengwei_coin_iCoinPara=" + JSON.stringify(iCoinPara) + " iRet=" + iRet);
    return iRet;
}


jsclient.getRightCoinRoomLevel = function () {
    var pcoin = jsclient.data.pinfo.coin;
    var mLmax1 = jsclient.getCoinConfig(1, CoinData.max);
    var mLmin1 = jsclient.getCoinConfig(1, CoinData.min);
    var mLmax2 = jsclient.getCoinConfig(2, CoinData.max);
    var mLmin2 = jsclient.getCoinConfig(2, CoinData.min);
    var mLmax3 = jsclient.getCoinConfig(3, CoinData.max);
    var mLmin3 = jsclient.getCoinConfig(3, CoinData.min);
    var mLmax4 = jsclient.getCoinConfig(4, CoinData.max);
    var mLmin4 = jsclient.getCoinConfig(4, CoinData.min);
    var mLmax5 = jsclient.getCoinConfig(5, CoinData.max);
    var mLmin5 = jsclient.getCoinConfig(5, CoinData.min);
    var mLmin6 = jsclient.getCoinConfig(6, CoinData.min);
    if (pcoin > mLmin6) {
        return 6;
    } else if (pcoin > mLmin5 && ((pcoin < mLmax5 && mLmax5 != -1) || mLmax5 == -1)) {
        return 5;
    } else if (pcoin > mLmin4 && ((pcoin < mLmax4 && mLmax4 != -1) || mLmax4 == -1)) {
        return 4;
    } else if (pcoin > mLmin3 && ((pcoin < mLmax3 && mLmax3 != -1) || mLmax3 == -1)) {
        return 3;
    } else if (pcoin > mLmin2 && ((pcoin < mLmax2 && mLmax2 != -1) || mLmax2 == -1)) {
        return 2;
    } else if (pcoin >= mLmin1 && ((pcoin < mLmax1 && mLmax1 != -1) || mLmax1 == -1)) {
        return 1;
    }
    return 1;
}


/*休闲场创建房间
 //coinType 1初级场 2中级场 3高级场 4专家场 5大师场 6宗师场
 */
jsclient.createCoinRoom = function (aCoinType) {
    if (jsclient.data.sData) {
        jsclient.showMsg("房间已经创建,请点击返回游戏");
    } else {
        if (jsclient.remoteCfg.coinRoom) {
            jsclient.isCoinRoom = true;
            jsclient.coinRoomCreate = aCoinType; //1初级场2中级场3高级场4专家场5大师场6宗师场
            var min = jsclient.getCoinConfig(jsclient.coinRoomCreate, CoinData.min);
            var max = jsclient.getCoinConfig(jsclient.coinRoomCreate, CoinData.max); //-1 代表不限制上限制

            mylog("=====休闲场创建房间数据=========aCoinType：" + JSON.stringify(aCoinType));
            // 休闲场分等级进入判断，add by zhengwei
            var coinCheck = true;
            if (jsclient.data.pinfo.coin <= min) {
                coinCheck = false;
            } else {
                if (jsclient.data.pinfo.coin > max && max != -1) {
                    coinCheck = false;
                } else {
                    coinCheck = true;
                }
            } 

            var PlayType = {
                gangHua :false,
                withHun :false,
                gangPao :false,
            };


            var CoinRoomPlay=1;
            var _playerNum = 3;
            if(jsclient.CoinRoomType == CoinRoomType.ERMJ){
                _playerNum = 2;
                CoinRoomPlay = 2;
                PlayType.gangHua = true;
                PlayType.gangPao = true;
                PlayType.withHun = true;
            }

            // cc.log("zhengwei_coinpp_max:  " + max + "  zhengwei_coinpp_min:  " + min + "zhengwei_coinpp_coinRoomCreate:  " + jsclient.coinRoomCreate);
            // cc.log("zhengwei_coin_min = " + min + " max=" + max);
            // cc.log("zhengwei_coin: " + jsclient.data.pinfo.coin);
            var isTrust = 10; //休闲场默认10秒托管
            var isVIPTable = 0;
//             JS: {"aaMoney":false,"createForOther":false,"gameid":"xynmmj","roomid":"symj1","
// round":"erren_round4","mjType":4,"withWind":false,"mustTingHu":null,"canEatHu":t
// rue,"duopao":null,"pldValue":null,"playerNum":2,"maxFen":null,"difen":null,"peng
// GangNum":null,"isPiaoJiaHu":null,"gangShanghuaBeishu":null,"dianpaoBeishu":null,
// "gangTing":false}

            if (coinCheck) { //用户最少要有 2000 金豆
                jsclient.createRoomCoin(
                    false,//createForOther
                    false,  //aaMoney
                    "round999",//round
                    MjType.YKX,//mjType
                    false,//withWind
                    null,//mustTingHu
                    true,//canEatHu
                    null,//duopao
                    null,//pldValue
                    _playerNum,//playerNum
                    null,//maxFen
                    null,//difen
                    null,//pengGangNum
                    null,//isPiaoJiaHu
                    null,//gangShanghuaBeishu
                    null,//dianpaoBeishu
                    false,//gangTing
                    true, //isCoinRoom
                    aCoinType, //coinType,
                    CoinRoomPlay,           //1是三人一口香 2是二人一口香
                    isVIPTable
                );


            } else {
                var pinfo = jsclient.data.pinfo;
                // cc.log("zhengwei_coin_begin:  ");
                if (jsclient.data.pinfo.coin <= jsclient.getCoinConfig(1, CoinData.min)) {
                    jsclient.gamenet.request("pkplayer.handler.GetCoin", pinfo.uid,
                     function (rtn) {
                         if (rtn.result == 0) {
                             jsclient.showMsg("补偿金豆" + rtn.coin);
                             // if (rtn.remain == 0) {
                             //  jsclient.showMsg("当天第" + 2 + "次补偿金豆" + rtn.coin);
                             // } else if (rtn.remain == 1) {
                             //  jsclient.showMsg("当天第" + 1 + "次补偿金豆" + rtn.coin);
                             // }
                             // cc.log("zhengwei_coin:  " + rtn.coin);
                             // cc.log("zhengwei_remain:  " + rtn.remain);
                    
                         } else {
                             jsclient.showMsg("您金豆不足，请联系客服");
                         }
                    
                     });
                    // jsclient.showMsg("您金豆不足，请联系客服");
                } else {
                    if (jsclient.data.pinfo.coin <= min) {
                        jsclient.showMsg("您金豆不足，请换到更低级的场次");
                    } else {
                        if (jsclient.data.pinfo.coin > max && max != -1) {
                            jsclient.showMsg("请换到更高级的场次匹配吧");
                        }
                    }
                }
                // jsclient.showMsg("您金豆不足，请联系客服");
            }
        } else jsclient.showMsg("暂未开放,敬请期待");
    }
}




//创建休闲场
jsclient.createRoomCoin = function (createForOther, aaMoney, round, mjType, withWind, mustTingHu,
    canEatHu, duopao,pldValue,playerNum,maxFen,difen,pengGangNum,isPiaoJiaHu,gangShanghuaBeishu,dianpaoBeishu, gangTing,
    isCoinRoom, coinType,playType, isVIPTable) {
    jsclient.block();
    var createPara = {
        createForOther: createForOther,
        gameid: "xynmmj",
        roomid: "symj1",
        uid: SelfUid(),
        aaMoney:false,
        round: round,
        mjType:mjType,
        withWind:withWind,
        mustTingHu:mustTingHu,
        canEatHu:canEatHu,
        duopao:duopao,
        pldValue:pldValue,
        playerNum:playerNum,
        maxFen:maxFen,
        difen:difen,
        pengGangNum:pengGangNum,
        isPiaoJiaHu:isPiaoJiaHu,
        gangShanghuaBeishu:gangShanghuaBeishu,
        dianpaoBeishu:dianpaoBeishu,
        gangTing:gangTing,

        coinRoomCreate: isCoinRoom,
        coinType: coinType, //1初级场 2中级场 3高级场 4专家场 5大师场 6宗师场  注：不确定普通房间是否也需要、
        playType: playType, // //1血流成河 2血战到底 3倒倒胡 4内江麻将 5三人两房 6三人三房 7二人两房 8二人三房 9万州麻将 10德阳麻将(根据游戏类型与服务器同学商定既可)
        isVIPTable: isVIPTable
    };

    jsclient.gamenet.request(
        "pkplayer.handler.CreateVipTable",
        createPara,
        function (rtn) {
            jsclient.unblock();
            if (rtn.result == 0) {
                if (createForOther) {
                    jsclient.showMsg("代开房间成功！点击“已开房间”按钮查看开房信息！");
                    return;
                }
                jsclient.data.vipTable = rtn.vipTable;
                jsclient.joinGame(rtn.vipTable);
            }
            else if (rtn.result == 1) {
                //钻石不足
            }
            else if (rtn.result == ZJHCode.moneyOverflow) {
                //预扣所需钻石不足
                var wxh = "";
                if (jsclient.updateCfg)
                    wxh = jsclient.updateCfg.weixinBuy + "";

                jsclient.showMsg("由于代开房预扣钻石导致余额不足，无法创建房间，如有问题请联系客服" + wxh + "。");
            }
            else if (rtn.result == 42) {
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("您的游戏版本过低，请点击确定进行更新！",
                        function () {
                            jsclient.removeUpdateRes(null, null);
                        },
                        function () {
                        },
                        "1"
                    );
                }
            }
        }
    );

};






/*休闲场创建房间
 //coinType 1初级场 2中级场 3高级场 4专家场 5大师场 6宗师场
 */
jsclient.createCoinRoom_dda = function (aCoinType) {
    if (jsclient.data.sData) {
        jsclient.showMsg("房间已经创建,请点击返回游戏");
    } else {
        if (jsclient.remoteCfg.coinRoom) {
            jsclient.isCoinRoom = true;
            jsclient.coinRoomCreate = aCoinType; //1初级场2中级场3高级场4专家场5大师场6宗师场
            var min = jsclient.getCoinConfig(jsclient.coinRoomCreate, CoinData.min);
            var max = jsclient.getCoinConfig(jsclient.coinRoomCreate, CoinData.max); //-1 代表不限制上限制

            mylog("=====休闲场创建dda房间数据=========aCoinType：" + JSON.stringify(aCoinType));
            // 休闲场分等级进入判断，add by zhengwei
            var coinCheck = true;
            if (jsclient.data.pinfo.coin <= min) {
                coinCheck = false;
            } else {
                if (jsclient.data.pinfo.coin > max && max != -1) {
                    coinCheck = false;
                } else {
                    coinCheck = true;
                }
            }

            var CoinRoomPlay=3;

            var isTrust = 10; //休闲场默认10秒托管
            var isVIPTable = 0;


            //             {"round":"DDA_round2","gameid":"dda","isHongtao":true,"xuezhandaodi":false,"
            // cardNumIsVisible":true,"createForOther":false,"roomid":"symj1","playerNum":5,"ba
            // seScore":1,"jichufen":1,"a2beishu":1,"mingApingfen":true}

            if (coinCheck) { //用户最少要有 2000 金豆
                 jsclient.createRoomCoin_dda(
                    false,//createForOther
                    false,  //aaMoney
                    "round999",//round
                    "dda",//mjType
                    true,//isHongtao
                    false,//xuezhandaodi
                    true,//cardNumIsVisible
                    5,//playerNum
                    1,//baseScore
                    1,//jichufen
                    1,//a2beishu
                    true,//mingApingfen
                    true, //isCoinRoom
                    aCoinType, //coinType,
                    CoinRoomPlay,           //1是三人一口香 2是二人一口香 3是打大A
                    isVIPTable
                );



            } else {
                var pinfo = jsclient.data.pinfo;
                // cc.log("zhengwei_coin_begin:  ");
                if (jsclient.data.pinfo.coin <= jsclient.getCoinConfig(1, CoinData.min)) {
                    jsclient.gamenet.request("pkplayer.handler.GetCoin", pinfo.uid,
                     function (rtn) {
                         if (rtn.result == 0) {
                             jsclient.showMsg("补偿金豆" + rtn.coin);

                         } else {
                             jsclient.showMsg("您金豆不足，请联系客服");
                         }
                    
                     });
                } else {
                    if (jsclient.data.pinfo.coin <= min) {
                        jsclient.showMsg("您金豆不足，请换到更低级的场次");
                    } else {
                        if (jsclient.data.pinfo.coin > max && max != -1) {
                            jsclient.showMsg("请换到更高级的场次匹配吧");
                        }
                    }
                }
            }
        } else jsclient.showMsg("暂未开放,敬请期待");
    }
}



//创建休闲场
jsclient.createRoomCoin_dda = function (createForOther, aaMoney, round, mjType, isHongtao, xuezhandaodi,
    cardNumIsVisible, playerNum,baseScore,jichufen,a2beishu,mingApingfen,
    isCoinRoom, coinType,playType, isVIPTable) {
    jsclient.block();
    var createPara = {
        createForOther: createForOther,
        gameid: "dda",
        roomid: "symj1",
        uid: SelfUid(),
        aaMoney:false,
        round: round,
        mjType:mjType,
        isHongtao:isHongtao,
        xuezhandaodi:xuezhandaodi,
        cardNumIsVisible:cardNumIsVisible,
        playerNum:playerNum,
        baseScore:baseScore,
        jichufen:jichufen,
        a2beishu:a2beishu,
        mingApingfen:mingApingfen,

        coinRoomCreate: isCoinRoom,
        coinType: coinType, //1初级场 2中级场 3高级场 4专家场 5大师场 6宗师场  注：不确定普通房间是否也需要、
        playType: playType, // //1血流成河 2血战到底 3倒倒胡 4内江麻将 5三人两房 6三人三房 7二人两房 8二人三房 9万州麻将 10德阳麻将(根据游戏类型与服务器同学商定既可)
        isVIPTable: isVIPTable
    };

    jsclient.gamenet.request(
        "pkplayer.handler.CreateVipTable",
        createPara,
        function (rtn) {
            jsclient.unblock();
            if (rtn.result == 0) {
                if (createForOther) {
                    jsclient.showMsg("代开房间成功！点击“已开房间”按钮查看开房信息！");
                    return;
                }
                jsclient.data.vipTable = rtn.vipTable;
                jsclient.joinGame(rtn.vipTable);
            }
            else if (rtn.result == 1) {
                //钻石不足
            }
            else if (rtn.result == ZJHCode.moneyOverflow) {
                //预扣所需钻石不足
                var wxh = "";
                if (jsclient.updateCfg)
                    wxh = jsclient.updateCfg.weixinBuy + "";

                jsclient.showMsg("由于代开房预扣钻石导致余额不足，无法创建房间，如有问题请联系客服" + wxh + "。");
            }
            else if (rtn.result == 42) {
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("您的游戏版本过低，请点击确定进行更新！",
                        function () {
                            jsclient.removeUpdateRes(null, null);
                        },
                        function () {
                        },
                        "1"
                    );
                }
            }
        }
    );

};








jsclient.requestCoinCgfOnly = function () {
    if ("undefined" == typeof(jsclient.requestCoinCfgCount)) {
        jsclient.requestCoinCfgCount = 0;
    } else {
        jsclient.requestCoinCfgCount = ++jsclient.requestCoinCfgCount % 10;
    }
    if (jsclient.requestCoinCfgCount == 0) {
        jsclient.block();
        var pinfo = jsclient.data.pinfo;
        jsclient.gamenet.request("pkplayer.handler.GetRoomCfg", {uid: pinfo.uid, version: 100},
            function (rtn) {
                mylog("===requestCoinCgfOnly====呀呀呀呀呀=============================rtn.length：" + rtn.length);
                if (rtn.length > 0) {
                    jsclient.coinCfg = rtn;
                    var iPath = jsb.fileUtils.getWritablePath() + "coinRoomCfg";
                    jsb.fileUtils.writeStringToFile(JSON.stringify(jsclient.coinCfg), iPath);

                    sendEvent("updateCoinCfg");
                }
                jsclient.unblock();
            });
    }

}
jsclient.requestCoinCgf = function () {
    // jsclient.block();
    // var pinfo = jsclient.data.pinfo;
    // jsclient.gamenet.request("pkplayer.handler.GetRoomCfg", { uid: pinfo.uid, version: 100 },
    //     function(rtn) {
    //         if (rtn.length > 0) {
    //             jsclient.coinCfg = null;
    //             jsclient.coinCfg = rtn;
    //             jsclient.isCoinRoom = true; //add by wcx 20170330 是休闲场
    // jsclient.createCoinRoom(jsclient.coinLeverType, jsclient.coinPlayType); // 休闲场分场 add by zhengwei
    if(jsclient.CoinRoomType == CoinRoomType.DDA)
    {
        jsclient.createCoinRoom_dda(jsclient.coinLeverType,jsclient.CoinRoomType);
    }else
        jsclient.createCoinRoom(jsclient.coinLeverType,jsclient.CoinRoomType);
    // jsclient.createCoinRoom(1, 1);
    //     }
    //     jsclient.unblock();
    // });
}
/*
 检查当前用户的金豆是否满足创建已经选择 VIP房间的条件
 */
jsclient.checkUserCoin = function (aCoinType) {
    //1初级场2中级场3高级场4专家场5大师场6宗师场
    var min = jsclient.getCoinConfig(aCoinType + 10, CoinData.min);
    var max = jsclient.getCoinConfig(aCoinType + 10, CoinData.max); //-1 代表不限制上限制

    // 休闲场分等级进入判断
    var coinCheck = true;
    if (jsclient.data.pinfo.coin < min) {
        coinCheck = false;
    } else {
        if (jsclient.data.pinfo.coin > max && max != -1) {
            coinCheck = false;
        }
        else {
            coinCheck = true;
        }
    }

    if (!coinCheck) {
        if (min < 5) {
            jsclient.ShowToast("暂未开启~~");
        } else {
            jsclient.ShowToast("金豆不足! 最少携带" + min / 10000 + "万金豆");
        }
    }
    return coinCheck;
}

//________________________________________________________________休闲场 end____________


 /**
 * 获取授权码
 * pkplayer.handler.GetAuthCode
 * @param {Object}
 *
 */
jsclient.GetAuthCode = function() {
    jsclient.gamenet.request("pkplayer.handler.GetAuthCode", { },
        function(rtn) {
            if(rtn.result == 0){
                var content = "xynmmj星悦内蒙麻将\n {" + rtn.authcode + "}授权码";
                jsclient.native.writeToClipboard(content);
                jsclient.ShowToast("复制授权码成功了！");
            }else {
                jsclient.ShowToast("获取授权码失败了 " + rtn.result);
            }

        }
    );
}

jsclient.ShowToast = function(txt, time) {
    if (!jsclient.Scene) return;
    time = time || 1.5;
    var imgName = "res/aress/gameInner/tip_bg.png";
    var visibleSize = jsclient.size;
    var text = new ccui.Text(txt, "Arial", 35);
    text.setColor({ r: 255, g: 255, b: 255 });

    var layout = new ccui.ImageView();
    layout.loadTexture(imgName, ccui.Widget.LOCAL_TEXTURE);
    layout.setAnchorPoint(0.5, 0.5);
    layout.setCapInsets(cc.rect(11, 11, 9, 9));
    layout.setScale9Enabled(true);
    var MIN_WIDTH = 400;
    var textSize = text.getContentSize();
    if (textSize.width < MIN_WIDTH) {
        layout.setContentSize(cc.size(MIN_WIDTH, textSize.height + 30));
    } else {
        layout.setContentSize(cc.size(text.getContentSize().width + 30, textSize.height + 30));
    }
    text.setPosition(cc.p(layout.getContentSize().width / 2, layout.getContentSize().height / 2));
    layout.setPosition(visibleSize.width / 2, 0);
    layout.addChild(text);


    var moveTo1 = cc.EaseSineInOut.create(cc.moveTo(0.2, cc.p(visibleSize.width / 2, visibleSize.height / 2.5)));
    var moveTo2 = cc.moveTo(0.1, cc.p(visibleSize.width / 2, visibleSize.height / 2.6));

    var delay = cc.delayTime(time);
    var fadeout = cc.fadeOut(0.2);
    var callback = cc.callFunc(function() {
        layout.removeFromParent(true);
    })
    layout.runAction(cc.sequence(moveTo1, moveTo2, delay, fadeout, callback));
    jsclient.Scene.addChild(layout, 2000);
}

//裁剪头像
function ClipHead(node, url, posx, posy, scalex, scaley,zindex, name, tag, _typeurl,isDefaultImg)
{
    //var typeurl = "res/common/common_head_kuang.png";
    // let realSize = node.getContentSize();
    // console.log("ClipHead----w========999999999999999999999==============:"+realSize.width);
    // console.log("ClipHead----h========999999999999999999999==============:"+realSize.height)
    var typeurl = "res/aress/gameInner/common_head_lixian.png";

    console.log("ClipHead----w========999999999999999999999============typeurl==:::"+typeurl);


    console.log("ClipHead----w========999999999999999999999==========url==:::"+url);


    cc.loader.loadImg(url, {isCrossOrigin: true}, function (err, texture)
    {
        if (!err && texture)
        {
            var stencil = new cc.Sprite(typeurl);   //可以是精灵，也可以DrawNode画的各种图形

            //1.创建裁剪节点
            stencil.setScale(1,1);
            var clipper = new cc.ClippingNode(stencil);   //创建裁剪节点ClippingNode对象  带模板
            clipper.setInverted(false);         //显示被模板裁剪下来的底板内容。默认为false 显示被剪掉部分。
            //alpha阀值：表示像素的透明度值。
            //只有模板（stencil）中像素的alpha值大于alpha阈值时，内容才会被绘制。
            //alpha阈值（alphaThreshold）：取值范围[0,1]。
            //默认为1，表示alpha测试默认关闭，即全部绘制。
            //若不是1，表示只绘制模板中，alpha像素大于alphaThreshold的内容。
            clipper.setAlphaThreshold(0);    //设置绘制底板的Alpha值为0
            clipper.setPosition(posx,posy);
            clipper.name = name;

            //3.创建底板
            var sprite = new cc.Sprite(texture);
            if (sprite.getContentSize().width > 132) {
                sprite.setContentSize(cc.size(132, 132))    //设为固定值 避免图片太大 显示不全
            }
            
            if(isDefaultImg)
            {
                sprite.setScale(0.9, 0.9);
            }
            else
            sprite.setScale(0.6, 0.6);


            clipper.addChild(sprite);
            clipper.setScale(1, 1);
            cc.log("node  name----------> "+node.tag);

            node.addChild(clipper);
        }
    });
}

jsclient.isVIPEmojValid =function(emojIndex){
    return true;
    var pinfo = jsclient.data.pinfo;
    if(!pinfo) return false;
    var emoj = pinfo["emoj"];
    if(!emoj ) return false;

    var vipInfo ;
    if(emojIndex == 1)
    {
        vipInfo = emoj.VIP1;
    }else if(emojIndex == 2){
        vipInfo = emoj.VIP2;
    }else{
        vipInfo = emoj.VIP3;
    }

    if(!vipInfo ) return false;
    if(typeof(vipInfo.expired_ts) == 'undefined')  return false;

    var nowData = new Date();
    var nowTime = Math.floor(nowData.getTime()/1000);
    return  (nowTime < vipInfo.expired_ts);
}

jsclient.getRestDay = function(emojIndex){
    return "免费";
    var pinfo = jsclient.data.pinfo;
    if(!pinfo) return false;
    var emoj = pinfo["emoj"];
    if(!emoj ) return false;

    var vipInfo ;
    if(emojIndex == 1)
    {
        vipInfo = emoj.VIP1;
    }else if(emojIndex == 2){
        vipInfo = emoj.VIP2;
    }else{
        vipInfo = emoj.VIP3;
    }
 

    if(!vipInfo ) return false;
    if(typeof(vipInfo.expired_ts) == 'undefined')  return false;
    var viptime = vipInfo.expired_ts;
    var nowData = new Date();
    var vipData = new Date(viptime*1000); 

    var nTime = vipData.getTime() - nowData.getTime();
    var day = Math.floor(nTime/86400);    
    var hour = Math.floor(nTime%86400/3600);    


    var timeType =1;
    var diffType = "day";
    switch (diffType) {
        case"second":
            timeType =1000;
        break;
        case"minute":
            timeType =1000*60;
        break;
        case"hour":
            timeType =1000*3600;
        break;
        case"day":
            timeType =1000*3600*24;
        break;
        default:
        break;
    }
    var tempDay = parseInt((nTime) / parseInt(timeType));
    
    return tempDay;
  
}

jsclient.getCurrentTimeStr = function() {
    var now = new Date();
    var year = now.getFullYear(); //年
    var month = now.getMonth() + 1; //月
    var day = now.getDate(); //日
    var hh = now.getHours(); //时
    var mm = now.getMinutes(); //分
    var str = "";
    str = year + "/" + month + "/" + day + " " + hh;
    if (mm < 10) {
        str += ":0" + mm;
    }
    else {
        str += ":" + mm;
    }
    return str;
}

jsclient.getCurrentTime = function () {
    var now = new Date();
    var year = now.getFullYear();       //年
    var month = now.getMonth() + 1;     //月
    var day = now.getDate();            //日

    var hh = now.getHours();            //时
    var mm = now.getMinutes();          //分
    var ss = now.getSeconds();          //秒

    return [year, month, day, hh, mm, ss];
};

function getNowFormatDate() {
    var date = new Date();
    var seperator1 = ".";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    var minute = date.getMinutes();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    if(minute < 10) {
        minute = "0" + minute;
	}
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + minute;
    return currentdate;
}

jsclient.dateInRectDate = function (myTime, startTime, endTime) {
    if(!myTime || !startTime || !endTime) return;
    var makeNum = function (array) {
        var num = 0;
        for(var i = 0; i<array.length; i++){
            num += array[i] * Math.pow(10, (10 - i*2));
        }
        return num;
    };

    var myTime_Num = makeNum(myTime);
    var startTime_Num = makeNum(startTime);
    var endTime_Num = makeNum(endTime);
    return (myTime_Num >= startTime_Num && myTime_Num <= endTime_Num);
};

jsclient.deepClone = function (sObj) {
    if(!sObj || typeof(sObj) !== "object"){
        return sObj;
    }

    var s = {};
    if(sObj.constructor == Array){
        s = [];
    }

    for(var i in sObj){
        s[i] = jsclient.deepClone(sObj[i]);//Object.clone(sObj[i]);
    }
    return s;
};
    


function stopEffect(id) {
    cc.audioEngine.stopEffect(id);
}

function playEffect(sd) {
    var filePath = "res/sound/" + sd + ".mp3";
    if (!_settingData.LanguageFY) {
        filePath = filePath.replace(/fynan/,"nv");
        filePath = filePath.replace(/fydda/,"dda");
    }else{
        filePath = filePath.replace(/nv/,"fynan");
        filePath = filePath.replace(/dda/,"fydda");
    }
    return cc.audioEngine.playEffect(filePath, false);
}

function playEffectDda(sd,msg) {
    if (!msg)
        return

    if (!msg.uid)
        return

    var sData = jsclient.data.sData
    if (!sData)
        return

    var pl = sData.players[msg.uid + ""];
    var sex = pl.info.sex

    var filePath = "res/sound/" + sd + ".mp3";
    if (!_settingData.LanguageFY) {
        if (sex == 1) {
            filePath = filePath.replace(/dda/,"dda");
        } else {
            filePath = filePath.replace(/dda/,"dda/nv");
        }
    }else{
        if (sex == 1) {
            filePath = filePath.replace(/dda/,"fydda");
        } else {
            filePath = filePath.replace(/dda/,"fydda/nv");
        }
    }

    return cc.audioEngine.playEffect(filePath, false);
}
function playMusic(sd) {
    cc.audioEngine.stopMusic();
    cc.audioEngine.playMusic("res/sound/" + sd + ".mp3", true);
}

jsclient.showPlayerInfo_play = function (info) {
    if(jsclient.remoteCfg.guestLogin){
        return;
    }
    if(jsclient.replayui){
        return;
    }
    jsclient.uiPara = info;
    jsclient.Scene.addChild(new UserInfoLayer());
}

jsclient.showPlayerInfo = function (info) {
    // ShowNewInfo_play("测试地理位置信息显示", info);
    ShowNewInfoLayer(info);
    return;
}

jsclient.CDTimeNum = 0;

//这里 应该 清理计时器 和启动计时器，避免浪费
jsclient.clearCD = function()
{
    jsclient.CDTimeNum = 0;
}

jsclient.startCD = function(cdCoolTime)
{
    if (!cdCoolTime) {
        cdCoolTime = 2
    }
    if(cc.sys.OS_WINDOWS == cc.sys.os){
        jsclient.CDTimeNum = cdCoolTime;
    }else{
        jsclient.CDTimeNum = cdCoolTime;
    }
}

jsclient.createCDTimer = function (info) {

    function cdTimerCallBack() {
        if (jsclient.CDTimeNum <= 0) {
            jsclient.CDTimeNum = 0;
            return;
        }

        jsclient.CDTimeNum = jsclient.CDTimeNum - 0.1;
        if (jsclient.CDTimeNum <= 0) {
            jsclient.CDTimeNum = 0;
        }
        sendEvent("cdChange");
    }

    var node = cc.Node.create();
    node.setTag(CDTIMERTAG);
    jsclient.Scene.addChild(node);
    node.schedule(cdTimerCallBack, 0.1);
}

jsclient.restartGame = function (data) {

//游戏大厅
    if (typeof ppgamecenter != "undefined") { //退出登陆, 适配游戏中心
        try {
            if (jsclient.isRestarting) return;
            jsclient.isRestarting = true;

            var doPreRestartGame = function () {
                // 为了防止重启时下载等相关的schedule回调导致的崩溃
                cc.director.getScheduler().unscheduleAllWithMinPriority(cc.Scheduler.PRIORITY_SYSTEM + 1);
                cc.director.purgeCachedData(); //清理 1:animationCache 2:spriteFrameCache 3:textureCache
                if (jsclient.gamenet) {
                    jsclient.gamenet.disconnect();
                }
            }

            /**
             * FIXME: ugly code!!! cocos3.11.1
             * 因为上面的unscheduleAllWithMinPriority会把jsb.AudioEngine中的schedule停掉，
             * 重启后播放音频完成后不会从缓存列表中移除，导致很快达到了音频播放数量上限而不再播放任何音频。
             * 所以先停止了所有音频播放，好让jsb.AudioEngine可以再次启动schedule。
             * 为了防止崩溃，做了延时重启。
             */
            jsb.AudioEngine.stopAll();
            var delay = 100; // 需要给AudioEngine留出时间来处理update
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                // Android需要延迟大于500ms，详见AudioEngine-inl.cpp的DELAY_TIME_TO_REMOVE
                delay += 500;
            }
            setTimeout(function () {
                doPreRestartGame();
                if (cc.sys.OS_IOS === cc.sys.os || cc.sys.OS_OSX === cc.sys.os) {
                    // android调用会崩溃，由于ios即使调用stopAll也无法清空_audioPlayers，需要end后重新初始化AudioEngine来启动schedule
                    jsb.AudioEngine.end();
                }
                setTimeout(function () {
                    if (data === 'update') {
                        ppgamecenter.setGameState({ type: 'xynmmj', state: 2 }) // 热更态
                        ppgamecenter.changeGameByUser('PPGame')
                    } else {
                        ppgamecenter.changeGameByUser('PPGame')
                    }
                    //sendEvent("restartGame");
                }, 10)
            }, delay);

            return;
        } catch (error) {

        }

        return;
    }
    cc.director.getScheduler().unscheduleAllWithMinPriority(cc.Scheduler.PRIORITY_SYSTEM + 1);
    cc.director.purgeCachedData();


    if (jsclient.gamenet)jsclient.gamenet.disconnect();
    sendEvent("restartGame");
}
jsclient.changeIdLayer = function () {
    if (!ChangeIDLayer) {
        return;
    }
    if (!jsclient.changeidui) {
        jsclient.Scene.addChild(new ChangeIDLayer());
    }
}
jsclient.exportDataLayer = function () {
    if (!ExportDataLayer) {
        return;
    }
    if (!jsclient.exportdataui) {
        jsclient.Scene.addChild(new ExportDataLayer());
    }
}

//前端报错收集上传
jsclient.reportErrorCode=function (code){
    var tempCode = code;
    var loginData=sys.localStorage.getItem("loginData");
    if(loginData)
    {
        loginData=JSON.parse(loginData);
        tempCode+=",uid:"+loginData.mail;
    }
    var xhr = cc.loader.getXMLHttpRequest();//网络断开原因，上传日志
    xhr.open("POST", "http://139.129.206.54:3000/xynmmj?content=" + base64encode(tempCode));
    xhr.onreadystatechange = function () {
    };
    xhr.send();
}
jsclient.leaveGame = function () {
    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.LeaveGame", {}, function (rtn) {
        if (rtn.result == 0) {
            delete jsclient.data.sData;
            sendEvent("LeaveGame");

            if (jsclient.ready2Coin) { //add by wcx 20170224
                jsclient.ready2Coin = null;
                jsclient.requestCoinCgf();
            }
        }
        jsclient.unblock();
        //所有的结束后检测活动的条件是不已经满足的抽奖次数
        //获取可以抽奖的次数

        var action = "bindGrowth";
        var method = "user/prize/count";
        var data = {prefix:"xynmmj","type":12};
        var dataStr = JSON.stringify(data);
        jsclient.block();
        var para =  {"action":action,"method":method,"data":dataStr};
        var leftNum = 0;
        jsclient.gamenet.request("pkcon.handler.ProxyAccess", para, function (rtn) {
            jsclient.unblock();
            cc.log("所有的结束后检测活动的条件  抽奖次数  rtn " +JSON.stringify(rtn));
            if (rtn.result == 0) {
                leftNum = rtn.data.count;
            }
            if(leftNum>0 && getNewZhuanPanVis())
            {
                showZhuanPan();
            }
        });


    });
}


jsclient.getPlayLogOne = function (item) {
    var playUrl = "";
    // cc.log("item == " + JSON.stringify(item));

    if(item.url)
    {
        jsclient.block();
        if(jsclient.remoteCfg.playBackServer)
        {
            //playUrl="http://"+jsclient.remoteCfg.playBackServer+"/"+item.url+"/playlog/"+item.now.substr(0,10)+"/"+item.owner+"_"+item.tableid+".json";

            var logid   = item.owner + "_" + item.tableid;
            var timeStr = item.now.substr(0, 10);

            if (item.logid) {
                logid = item.logid;
                var tableid = logid.split("_")[0];
                if (tableid != item.tableid) {
                    logid = item.owner + "_" + item.tableid;
                }
                else {
                    var time = new Date(logid.split("_")[1] * 1);
                    var y = time.getFullYear();
                    var m = time.getMonth() + 1;
                    var d = time.getDate();
                    m = (m < 10) ? ("0" + m) : m;
                    d = (d < 10) ? ("0" + d) : d;
                    timeStr = y + "-" + m + "-" + d;
                }
            }

            playUrl = "http://" + jsclient.remoteCfg.playBackServer + "/" + item.url + "/playlog/" + timeStr + "/" + logid + ".json";


            var xhr = cc.loader.getXMLHttpRequest();
            xhr.open("GET", playUrl);
            xhr.onreadystatechange = function () {
                jsclient.unblock();
                if (xhr.readyState == 4 && xhr.status == 200) {
                    sendEvent("playLogOne",JSON.parse(xhr.responseText));
                }
            }
            xhr.onerror = function (event) {jsclient.unblock(); }
            xhr.send();
        }
    }
    else
    {
        var now=item.now; var logid=item.logid;
        jsclient.block();
        jsclient.gamenet.request("pkplayer.handler.getSymjLog",{now:now,logid:logid},function(rtn){
            jsclient.unblock();
            if(rtn.result==0)
            {
                sendEvent("playLogOne",rtn.data["mjlog"]);
            }
        });
    }
}
jsclient.getPlayLog = function () {
    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.getSymjLog", {uid: SelfUid()}, function (rtn) {
        jsclient.unblock();
        if (rtn.result == 0) {
            jsclient.data.playLog = rtn.playLog;
            sendEvent("playLog");
        }

    });

}
jsclient.logout = function () {


    jsclient.block();
    jsclient.gamenet.request("pkcon.handler.logout", {}, function () {
        sys.localStorage.removeItem("WX_USER_LOGIN");
        sys.localStorage.removeItem("XL_USER_LOGIN");
        sys.localStorage.removeItem("LB_USER_LOGIN");
        sys.localStorage.removeItem("DL_USER_LOGIN");
        sys.localStorage.removeItem("LoginType");
        sys.localStorage.removeItem("loginData");
        sys.localStorage.removeItem("PHONE_USER_LOGIN");
        sendEvent("logout");
        jsclient.unblock();
    });
}

jsclient.isVIPTable = function () {
    var sData = jsclient.data.sData;
    if (!sData) {
        return false;
    }

    if (sData.tData.isVIPTable > 0) {
        return true;
    }
    return false;
}


//获取邀请数据
jsclient.getInviteData = function () {

    for(var index in jsclient.actionCfg)
    {
        var data = jsclient.actionCfg[index];
        if(data.actType == ActivityType.invite)
            return data
    }
}

//获取新手礼包数据
jsclient.getNewPlayerData = function () {

    for(var index in jsclient.actionCfg)
    {
        var data = jsclient.actionCfg[index];
        if(data.actType == ActivityType.newPlayer)
            return data
    }
}



//获取邀请奖励
jsclient.getInviteReward = function (rewIndex) {
    jsclient.block();
    var data = jsclient.getInviteData();
    jsclient.gamenet.request("pkplayer.handler.doActivity", {actId : data._id, actType : data.actType, rewIndex : rewIndex}, function (rtn) {
        jsclient.unblock();
        if (rtn.result == 0) {
            // jsclient.data.playLog = rtn.playLog;
            sendEvent("actData", {index:rewIndex, num:rtn.reward[1]});

        }
        // cc.log(".....   rtn.reward[1]"  + rtn.reward[1])
        // cc.log(JSON.stringify(rtn));

    });
}
//获取新手奖励
jsclient.getNewPlayerReward = function ( data ) {
     jsclient.block();
     jsclient.gamenet.request("pkplayer.handler.doActivity", data, function (rtn) {
         jsclient.unblock();
         if (rtn.result == 0) {
             var diamondNum  = rtn.reward;
             jsclient.Scene.addChild( new awardPrompt( diamondNum[1] ) );
             if( newPlayerAwardBtn  ){
                 newPlayerAwardBtn.removeFromParent( true );
             }
             if( jsclient.activationCodeLayer ){
                 jsclient.activationCodeLayer.removeFromParent( true );
             }
         }else{
             jsclient.Scene.addChild( new erroPrompt("领取失败") );
         }
     });
}

jsclient.joinGame = function (tableid ,gameid ,callback ) {
    jsclient.block();

    var joinPara = {roomid: "symj1"};
    if (tableid) joinPara.tableid = tableid;
    else  joinPara.roomid = "symj2";

    // joinPara.gameid = gameid;
    if( parseInt( tableid ) == 0 ){
        jsclient.showMsg("房间不存在",function () {

        },function () {

        },"1");
        jsclient.unblock();
        return;
    }
    joinPara.uid = SelfUid();
    var askUser4CoinRoom = function () {
        jsclient.isCoinRoom = false;
        var iFullMsg = "休闲场匹配失败了! 请重新匹配。";
        if (cc.sys.OS_WINDOWS == cc.sys.os) {
            iFullMsg = iFullMsg + "房间已满 tbID=" + joinPara.tableid;
        }
        jsclient.showMsg(iFullMsg, function () {
            jsclient.requestCoinCgf();
        }, function () {

        });
    };

    var askUser4CoinRoom2 = function () { //add by wcx  休闲场
        jsclient.isCoinRoom = false;
        var iFullMsg = "休闲场匹配失败了!!请重新匹配。";
        if (cc.sys.OS_WINDOWS == cc.sys.os) {
            iFullMsg = iFullMsg + "房间不存在 tbID=" + joinPara.tableid;
        }
        jsclient.showMsg(iFullMsg, function () {
            jsclient.requestCoinCgf();
        }, function () {

        });
    };
    if(jsclient.hasBaiDuLoc)joinPara.geogData = jsclient.getGeogData();
    //cc.log("joinGame-------------> joinPara : "+JSON.stringify(joinPara));

    jsclient.gamenet.request("pkplayer.handler.JoinGame", joinPara,
        function (rtn) {
            if(callback) {
                callback(rtn);
			}
            if (rtn.result != 0) {
                jsclient.unblock();

                if (rtn.result == ZJHCode.roomFull){
                    if (jsclient.isCoinRoom && !jsclient.isVIPTable()) {
                            askUser4CoinRoom();
                    }
                    else {
                        if (jsclient.sdk_control && jsclient.sdk_controFl && jsclient.sdk_controFl.newRooms) { //允许代开房和观战
                            jsclient.showMsg("房间玩家已经满！是否加入房间观战？",
                                function () {
                                    jsclient.observeGame(tableid);
                                },
                                function () {
                                },
                                "1"
                            );
                        }
                    }
                }

                if (rtn.result == ZJHCode.roomNotFound || rtn.result == ZJHCode.Fail)
                {
                    if (jsclient.isCoinRoom && !jsclient.isVIPTable()) {
                        askUser4CoinRoom2();
                        return;
                    }
                }

                switch (rtn.result) {
                    case ZJHCode.roomFull:
                        jsclient.showMsg("房间玩家已经满");
                        break;
                    case ZJHCode.roomNotFound:
                        jsclient.showMsg("房间不存在,请确认输入正确房间号");
                        break;
                    case ZJHCode.zipVersionLow:
                        if(typeof ppgamecenter === "undefined") {
                            jsclient.showMsg("游戏版本过低");
                        }
                        break;
                    case ZJHCode.GuildTableNot:
                        jsclient.showMsg("不在一个牌友群无法加入");
                        break;
                    case ZJHCode.minVersion:
                        if(typeof ppgamecenter === "undefined") {
                            jsclient.showMsg("游戏版本过低");
                        }
                        break;
                    case ZJHCode.Fail:
                        jsclient.showMsg("服务器异常");
                        break;
                    case ZJHCode.playerNotFound:
                        jsclient.showMsg("登录状态异常");
                        break;
                    case ZJHCode.paraError:
                        jsclient.showMsg("参数错误");
                        break;
                    case ZJHCode.GuildAlreadyInGame:
                        jsclient.showMsg("已经在游戏中，请在大厅点击返回游戏");
                        break;
                    case ZJHCode.GuildNotIn:
                        jsclient.showMsg("不在该牌友群中");
                        break;
                    case ZJHCode.GuildMoneyOverflow:
                        jsclient.showMsg("群主元宝不足，无法创建房间，请通知群主");
                        break;
                    case ZJHCode.GuildConfigErr:
                        jsclient.showMsg("请联系牌友群群主或者管理员检查玩法设置");
                        break;
                    case ZJHCode.GuildNotOpen:
                        showGuildShieldTip(guildId);
                        break;
                    case ZJHCode.GuildExclusion:
                        jsclient.showMsg("房间内有玩家与您是“排斥”关系（群主设定），您不能加入");
                        break;
                    case ZJHCode.GuildBlock:
                        showGuildBlockTip();
                        break;
                    default:
                        jsclient.showMsg(getGuildNetErrorDesc(rtn.result));
                        break;
                }

            }else
            {

            }
        });
}




//分享url
jsclient.autoJoinGame = function(){

    //jsclient.native.ShowLogOnJava("--------------jsclient.autoJoinGame--------------");
    //jsclient.native.ShowLogOnJava("--------------jsclient.autoJoinGame-------------- jsclient.data.vipTable = " + jsclient.data.vipTable);
    if(jsclient.data.vipTable <= 0){
        var roomData = jsclient.native.getRoomData();
        // mylog("--------------jsclient.autoJoinGame-------------- roomData = " + roomData);
        if(roomData && roomData != ""){
            var roomDataArr = roomData.split("_");
            var tableid = roomDataArr[1];
            var gameid = roomDataArr[0];
            // mylog("--------------jsclient.autoJoinGame-------------- tableid = " + tableid);
            // mylog("--------------jsclient.autoJoinGame-------------- tableid = " + gameid);
            //jsclient.native.ShowLogOnJava("--------------jsclient.autoJoinGame-------------- tableid = " + tableid);
            if((typeof(tableid) != "undefined") ? tableid.toString().length >= 6 : false) {
                jsclient.joinGame(tableid, gameid);
            }
            return true;
        }else{
            return jsclient.autoJoin();
        }
    }

    return false;
}

//剪切板
jsclient.autoJoin = function(){
    if(!jsclient.openClipboard) return false;

    var msg =  jsclient.native.getFromClipboard();
    if(!msg) return false;
    var reg = /_(\d*)_(\w*)_/;
    var rt = msg.match(reg);
    if(/*!jsclient.data.sData*/jsclient.data.vipTable <= 0 && rt){
        if(rt[1] && rt[2]){
            jsclient.joinGame(rt[1],rt[2]);
            jsclient.native.writeToClipboard("");
            return true;
        }
    }
    return false;
}


//扎金花创建房间
jsclient.zjhCreateRoom = function (isDaiKai, playtype, roundNum, playerNum, xiqian, baozi, baoziAAA, xianshou, difen, jinHuaOrShunZi, biXiao,happymoney) {
    jsclient.gameModel = "zjh";
    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.CreateVipTable", {
            createForOther: isDaiKai,
            roomid: "symj1",

            gameid: "zjh",
            playtype: playtype,
            round: roundNum,
            playernum: playerNum,
            xiqian: xiqian,
            baozi: baozi,
            baoziAAA: baoziAAA,
            xianshou: xianshou,
            difen:difen,
            jinHuaOrShunZi:jinHuaOrShunZi,
            biXiao:biXiao,
            happymoney:happymoney
        },
        function (rtn) {
            jsclient.unblock();
            if (rtn.result == 0) {
                if (isDaiKai) {
                    jsclient.showMsg("代开房间成功！点击“已开房间”按钮查看开房信息！");
                    return;
                }
                jsclient.data.vipTable = rtn.vipTable;
                jsclient.joinGame(rtn.vipTable, "zjh");
                // jsclient.joinGame(rtn.vipTable);
            }
            else if (rtn.result == ZJHCode.moneyOverflow) {
                //预扣所需钻石不足
                var wxh = "";
                if (jsclient.updateCfg)
                    wxh = jsclient.updateCfg.weixinBuy + "";

                jsclient.showMsg("由于代开房预扣钻石导致余额不足，无法创建房间，如有问题请联系客服" + wxh + "。");
            }
            else if (rtn.result == 42) {
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("您的游戏版本过低，请点击确定进行更新！",
                        function () {
                            jsclient.removeUpdateRes(null, null);
                        },
                        function () {
                        },
                        "1"
                    );
                }
            }
        }
    );
};

//创建麻将
jsclient.createRoom = function (littleMj, isTrust, createForOther, aaMoney, round, mjType, withWind, 
                                mustTingHu, canEatHu,duopao,pldValue,playerNum,
                                maxFen,difen,pengGangNum,isPiaoJiaHu,gangShanghuaBeishu,
                                dianpaoBeishu, gangTing, xamFen,canLiangxi,canQingyise,
                                canYitiaolong,canBaojiao,canPaofen,dianGang2Bei,diFenJianBan,
                                canEat,kouPai,moDaoDi,shifoudaizhuang, btmjWanfa, xiazhuangkoufen) {
    jsclient.block();

    jsclient.gamenet.request("pkplayer.handler.CreateVipTable", {
            littleMj: littleMj,
            isTrust: isTrust,
            aaMoney: aaMoney,
            createForOther: createForOther,
            gameid: "xynmmj",
            roomid: "symj1",
            round: round,
            mjType: mjType,
            withWind: withWind,
            mustTingHu: mustTingHu,
            canEatHu: canEatHu,
            duopao: duopao,
            pldValue: pldValue,
            playerNum: playerNum,
            maxFen: maxFen,
            difen: difen,
            pengGangNum: pengGangNum,
            isPiaoJiaHu: isPiaoJiaHu,
            gangShanghuaBeishu: gangShanghuaBeishu,
            dianpaoBeishu: dianpaoBeishu,
            gangTing: gangTing,
            xamFen: xamFen,
            canLiangxi: canLiangxi,
            canQingyise: canQingyise,
            canYitiaolong: canYitiaolong,
            canBaojiao: canBaojiao,
            canPaofen: canPaofen,
            dianGang2Bei: dianGang2Bei,
            diFenJianBan: diFenJianBan,
            canEat: canEat,
            kouPai: kouPai,
            moDaoDi: moDaoDi,
            shifoudaizhuang: shifoudaizhuang,
            btmjWanfa: btmjWanfa,
            xiazhuangkoufen: xiazhuangkoufen,
            openGodMap: majiangCFG.openGodMap
        },
        function (rtn) {

            cc.log("rtn.result--------------> "+rtn.result);

            jsclient.unblock();
            if (rtn.result == 0) {
                if(createForOther){
                    jsclient.showMsg("代开房间成功！点击我的房间查看开房信息！",function () {

                    },function () {

                    },"1");
                    return;
                }
                jsclient.data.vipTable = rtn.vipTable;
                jsclient.joinGame(rtn.vipTable, "xynmmj");
            }else if( rtn.result==ZJHCode.zipVersionLow ){
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("游戏版本过低,请下载最新游戏版本.");
                }
            }else if(rtn.result == 212){
                jsclient.showMsg("钻石不足");
            }else if(rtn.result == ZJHCode.Fail){
                jsclient.showMsg("创建房间失败，请尝试重启游戏！");
            }
        });
}

//打大A创建房间
jsclient.ddaCreateRoom = function(pokerType,round,isHongtao,xuezhandaodi,cardNumIsVisible,createForOther,roomid,playerNum,baseScore,jichufen,a2beishu,mingApingfen) {

    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.CreateVipTable", {
            round: round,
            gameid:pokerType,
            isHongtao:isHongtao,
            xuezhandaodi:xuezhandaodi,
            cardNumIsVisible:cardNumIsVisible,
            createForOther:createForOther,
            roomid:roomid,
            playerNum:playerNum,
            baseScore:baseScore,
            jichufen:jichufen,
            a2beishu:a2beishu,
            mingApingfen:mingApingfen,
            openGodMap: majiangCFG.openGodMap
        },
        function(rtn) {
            jsclient.unblock();

            if (rtn.result == 0) {
                jsclient.data.vipTable = rtn.vipTable;
                if (createForOther) {
                    jsclient.showMsg("成功代开房!您可在已开房间中查看代开信息，您的房间将在30分钟后自动解散，请尽快开始。",
                                    function () {
                                    },function () {
                                    },"1"
                                    );
                }
                else{
                    jsclient.joinGame(rtn.vipTable,"dda");
                }
            }else if( rtn.result==ZJHCode.zipVersionLow ){
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("游戏版本过低,请下载最新游戏版本.");
                }
            }
            else if(rtn.result == 212){
                jsclient.showMsg("钻石不足");
            }
        });
};


//跑的快创建房间
jsclient.pdkCreateRoom = function(isDaiKai,defaultPdkCreate){
    jsclient.block();

    jsclient.gamenet.request("pkplayer.handler.CreateVipTable", {
            createForOther: isDaiKai,
            roomid: "symj1",
            round: defaultPdkCreate.round + "_p" + defaultPdkCreate.playNumberLimitType,
            gameid: "pdk",
            tableType: defaultPdkCreate.tableType,
            forcePut: defaultPdkCreate.forcePut,
            less45Cards: defaultPdkCreate.less45Cards,
            wildCardsFlag: defaultPdkCreate.wildCardsFlag,
            showCardsNum: defaultPdkCreate.showCardsNum,
            firstPutType: defaultPdkCreate.firstPutType,
            firstMust403: defaultPdkCreate.firstMust403,
            noWinFirstPut_403: defaultPdkCreate.noWinFirstPut_403,         //几个人玩
            endGameReward: defaultPdkCreate.endGameReward,
            playerNum: defaultPdkCreate.playNumberLimitType,         //几个人玩

            canEatHu: 1,
            withWind: 1,
            canEat: 1,
            noBigWin: 1,
            openGodMap: majiangCFG.openGodMap
        },
        function (rtn) {
            jsclient.unblock();
            if (rtn.result == 0) {
                jsclient.data.vipTable = rtn.vipTable;
                jsclient.joinGame(rtn.vipTable,"pdk");
            }
            else if( rtn.result==ZJHCode.zipVersionLow ){
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("游戏版本过低,请下载最新游戏版本.");
                }
            }
            else if(rtn.result == 212){
                jsclient.showMsg("钻石不足");
            }
        });
}

 function isStartPower() {
    var sData = jsclient.data.sData;
    if(typeof (sData) =="undefined")    return;
    if (sData) {
        if(sData.tData.playType=="psz"){
            return sData.tData.owner == SelfUid();
        }else if(sData.tData.playType=="nn"){
            return sData.tData.startPower == SelfUid();
        }else if(sData.tData.playType=="zjn"){
            return sData.tData.owner == SelfUid();
        }else if(sData.tData.playType=="sg"){
            return sData.tData.owner == SelfUid();
        }
    }
    return false;
}


//牛牛创建房间
jsclient.nnCreateRoom = function (isDaiKai, playtype, mode, roundNum, playerNum, zhadan,
                                  xiaoniu5, niu7Double, kouPai, zhuang, difen, iscuopai, tuizhu,
                                  jinzhijiaru,Tuoguan,lianxuTuizhu,zuozhuangfenshu) {
    // return;
    jsclient.gameModel = "nn";
    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.CreateVipTable", {
            createForOther: isDaiKai,
            roomid: "symj1",

            gameid: "nn",
            playtype: playtype,
            mode: mode,
            round: roundNum,
            playernum: playerNum,
            zhadan: zhadan,
            xiaoniu5: xiaoniu5,
            niu7Double: niu7Double,
            kouPai: kouPai,
            zhuangType: zhuang,
            difen: difen,
            iscuopai: iscuopai,
            tuizhu: tuizhu,
            jinzhijiaru:jinzhijiaru,//游戏是不是禁止中途加入(bool 禁止中途加入 true，可以中途加入 false)
            Tuoguan:Tuoguan,//是不是全程托管(bool 是全程托管 true，不是 false)
            lianxuTuizhu:lianxuTuizhu,//是不是可以连续推注(bool 可连续推注 true，不可连续推注 false )
            zuozhuangfenshu:zuozhuangfenshu,//三轮庄（int 100：失去100分下庄， 200：失去100分下庄 ）；
        },
        function (rtn) {
            jsclient.unblock();
            if (rtn.result == 0) {
                if (isDaiKai) {
                    jsclient.showMsg("代开房间成功！点击“已开房间”按钮查看开房信息！");
                    return;
                }
                jsclient.data.vipTable = rtn.vipTable;
                //jsclient.joinGame(rtn.vipTable, "nn");
                jsclient.joinGame(rtn.vipTable);
            }
            else if (rtn.result == ZJHCode.moneyOverflow) {
                //预扣所需钻石不足
                var wxh = "";
                if (jsclient.updateCfg)
                    wxh = jsclient.updateCfg.weixinBuy + "";

                jsclient.showMsg("由于代开房预扣钻石导致余额不足，无法创建房间，如有问题请联系客服" + wxh + "。");
            }
            else if (rtn.result == 42) {
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("您的游戏版本过低，请点击确定进行更新！",
                        function () {
                            jsclient.removeUpdateRes(null, null);
                        },
                        function () {
                        },
                        "1"
                    );
                }
            }
        }
    );
};



//斗地主创建房间
//类型、局数、炸弹数量、玩家人数、叫分类型

jsclient.ddzCreateRoom = function(ddzType,round,bomblime,playerNum,gameType,createForOther,roomid) {

    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.CreateVipTable", {
            round: round,
            bomblime:bomblime,
            gameid:"ddz",
            playtype:ddzType,
            playernum:playerNum,
            gametype:gameType,
            createForOther:createForOther,
            roomid:roomid,
            openGodMap: majiangCFG.openGodMap

        },
        function(rtn) {
            jsclient.unblock();

            if (rtn.result == 0) {
                jsclient.data.vipTable = rtn.vipTable;
                if (createForOther) {
                    jsclient.showMsg("成功代开房!您可在已开房间中查看代开信息，您的房间将在30分钟后自动解散，请尽快开始。",
                                    function () {
                                    },function () {
                                    },"1"
                                    );
                }
                else{
                    jsclient.joinGame(rtn.vipTable,"ddz");
                }
            }else if( rtn.result==ZJHCode.zipVersionLow ){
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("游戏版本过低,请下载最新游戏版本.");
                }
            }
            else if(rtn.result == 212){
                jsclient.showMsg("钻石不足");
            }
            else {
                jsclient.showMsg("异常:" + rtn.result);
            }
        });
};


jsclient.createRoomTBZ = function (createForOther,round,rob,noWhite,betOne,betBaoZi,betRed,betPeace,betGang) {
    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.CreateVipTable", {
        createForOther  : createForOther,
        gameid          : "tbz", 
        roomid          : "symj1",
        round           : round, 
        rob             : rob,
        playerNum       : 8 ,
        noWhite         :noWhite,
        betOne          :betOne,
        baoziDouble     :betBaoZi,
        betRed          :betRed,
        betDraw         :betPeace,
        betBar          :betGang
    },
        function (rtn) {
            jsclient.unblock();
            if (rtn.result == 0) {
                if(createForOther){
                    jsclient.showMsg("代开房间成功！点击我的房间查看开房信息！",function () {

                    },function () {

                    },"1");
                    return;
                }
                jsclient.data.vipTable = rtn.vipTable;
                jsclient.joinGame(rtn.vipTable,"tbz");
            }else if( rtn.result==ZJHCode.zipVersionLow ){
                if(typeof ppgamecenter === "undefined") {
                    jsclient.showMsg("游戏版本过低,请下载最新游戏版本.");
                }
            }else if(rtn.result == 212){
                jsclient.showMsg("钻石不足");
            }
        });
}

jsclient.AddShareTimes = function () {
    var action = "bindGrowth";
    var method = "user/add/share";
    var data = {prefix: "xynmmj"};
    var dataStr = JSON.stringify(data);
    var para = {"action": action, "method": method, "data": dataStr};

    //testtime = new Date();
    jsclient.gamenet.request("pkcon.handler.ProxyAccess", para, function (rtn) {
        if(rtn && rtn.errno > 0 && rtn.errno < 3)
        {
            jsclient.showMsg(rtn.errmsg);
        }
        if (rtn && rtn.result == 0 && rtn.errno == 0) {
        	if(typeof jsclient.fudaiMissionLayerUI != "undefined" && jsclient.fudaiMissionLayerUI != null)
			{
                jsclient.fudaiMissionLayerUI.getTaskList();
			}
        }
    })
}

//获取当前时间相对1970.1.1的天数,和服务端保持一致
jsclient.getCurTimeDays = function () {
	var day = new Date();
	var timeZone = 28800000;
	var nDay = Math.floor((day.getTime() + timeZone - 60000) / (1000 * 3600 * 24));
	return nDay;
}

function getLBagCount() {
    var lbag = jsclient.data.pinfo.lbag;
	if(typeof lbag == "undefined")
	{
		return 0;
	}

	return lbag;
}

function showMyRoom () {
    jsclient.gamenet.request("pkplayer.handler.getOwnTableList", {}, function (rtn) {
        if (rtn.result == 0) {
            jsclient.myRoomsInfo = rtn.tableList;
            jsclient.Scene.addChild(new MyRoomLayer());
        }
        // 可能错误代码
        if (rtn.result == ZJHCode.Fail) {
            jsclient.showMsg("获取房间信息失败！");
            jsclient.unblock();
        }
        if (rtn.result == ZJHCode.roomNotFound) {
            jsclient.showMsg("房间信息异常！");
            jsclient.unblock();
        }
    });
}


//查询自己是否在游戏中
jsclient.selfIsInGame=function () {
    if(jsclient.data.sData)
        return true;
    return false;
};


//查询 存储第一次登陆的登录方式（微信、闲聊）
jsclient.operateFirstLoginType = function (type) {
    mylog("operateFirstLoginType");
    if(!type){
        var loginType = sys.localStorage.getItem("LoginType");
        if (loginType) {
            return loginType;
        }
    }
    else {
        if (type) {
            sys.localStorage.setItem("LoginType", type);
            return type;
        }
        else
            return null;
    }
};

jsclient.hasWx = function ()
{
    return true;
};

//收到游戏邀请信息后的操作
jsclient.doInviteGameInfo = function (para) {
    mylog("获取到闲聊游戏邀请信息...");
    mylog("信息内容para:" + para);
    mylog("信息内容para:" + JSON.stringify(para));

    if (!para) {
        mylog("没有游戏邀请信息...");
        return;
    }
    //玩家还未登陆
    if (jsclient.loginui) {
        jsclient.showMsgDelay(0.2, "请先登陆游戏后再次点击游戏约局信息！");
    }
    //玩家已经登录-未在房间内
    else if (!jsclient.data.sData) {
        mylog("gameType:" + para.roomToken.gameType);
        mylog("roomid:" + para.roomId);
        jsclient.joinGame(para.roomId, para.roomToken.gameType);
    }
    //玩家已经登录-在房间内
    else if (jsclient.data.sData) {
        var sData = jsclient.data.sData;
        var tData = sData.tData;

        var xl_type = para.roomToken.gameType;
        var xl_roomid = para.roomId;
        mylog("xl_type:" + xl_type + "  xl_roomid:" + xl_roomid);

        var pp_type = sData.gameid;
        var pp_roomid = tData.tableid;
        mylog("pp_type:" + pp_type + "  pp_roomid:" + pp_roomid);
        if (xl_type != pp_type || xl_roomid != pp_roomid) {
            jsclient.showMsgDelay(0.2, "您现在已经在游戏中，请先退出当前房间后再次点击游戏约局信息！");
        }
    }
};


// 聊呗分享
jsclient.lbCheckAndShare = function () {
    mylog("开始聊呗分享...");
    //检查是否进行过聊呗授权（此处为第二处可以关联微信、闲聊、皮皮账号的地方）,如果没有就进行聊呗授权登录
    var LB_USER_LOGIN = sys.localStorage.getItem("LB_USER_LOGIN");
    mylog("LB_USER_LOGIN:" + LB_USER_LOGIN);
    if (!LB_USER_LOGIN) {
        mylog("没有进行过聊呗登录，将调起聊呗...");
        if (jsclient.native)
            jsclient.native.lbLogin();
        return;
    }
    //进行函数调用
    if (jsclient.xianLiaoPara) {
        mylog("jsclient.xianLiaoPara合理，开始进行分享...");
        var para = jsclient.xianLiaoPara;
        if (para.type == XianLiaoType.text) {                       //文本
            mylog("文本分享...");
            var text = para.text;
            jsclient.native.lbShareText(text);
        }
        else if (para.type == XianLiaoType.image) {             //图片
            mylog("图片分享...");
            jsclient.native.lbShareImage();
        }
        else {                                                  //游戏邀请
            mylog("游戏邀请分享...");
            var roomId = para.roomId;
            var title = para.title;
            var url = para.url;
            var description = para.description;
            mylog("url:" + url  + "  title:" + title + "  description:" + description);
            jsclient.native.lbShareUrl(url, title, description);
            // jsclient.native.xlInviteGame(roomId, roomToken, title, description);
        }
    }
};


//闲聊分享
jsclient.xlCheckAndShare = function () {
    mylog("开始闲聊分享...");
    //检查是否进行过闲聊授权（此处为第二处可以关联微信、闲聊、皮皮账号的地方）,如果没有就进行闲聊授权登录
    var XL_USER_LOGIN = sys.localStorage.getItem("XL_USER_LOGIN");
    mylog("XL_USER_LOGIN:" + XL_USER_LOGIN);
    if (!XL_USER_LOGIN) {
        mylog("没有进行过闲聊登录，将调起闲聊...");
        if (jsclient.native)
            jsclient.native.xlLogin();
        return;
    }

    //进行函数调用
    if (jsclient.xianLiaoPara) {
        mylog("jsclient.xianLiaoPara合理，开始进行分享...");
        var para = jsclient.xianLiaoPara;
        if (para.type == XianLiaoType.text) {                       //文本
            mylog("文本分享...");
            var text = para.text;
            jsclient.native.xlShareText(text);
        }
        else if (para.type == XianLiaoType.image) {             //图片
            mylog("图片分享...");
            jsclient.native.xlShareImage();
        }
        else {                                                  //游戏邀请
            mylog("游戏邀请分享...");
            var roomId = para.roomId;
            var roomToken = para.roomToken;
            var title = para.title;
            var description = para.description;
            mylog("roomId:" + roomId + "  roomToken" + roomToken + "  title:" + title + "  description:" + description);
            jsclient.native.xlInviteGame(roomId, roomToken, title, description);
        }
    }
};

//多聊分享
jsclient.dlCheckAndShare = function () {
    mylog("开始多聊分享...");
    //检查是否进行过闲聊授权（此处为第二处可以关联微信、闲聊、皮皮账号的地方）,如果没有就进行闲聊授权登录
    /*var DL_USER_LOGIN = sys.localStorage.getItem("DL_USER_LOGIN");
    mylog("DL_USER_LOGIN:" + DL_USER_LOGIN);
    if (!DL_USER_LOGIN) {
        mylog("没有进行过多聊登录，将调起多聊...");
        if (jsclient.native)
            jsclient.native.dlLogin();
        return;
    }*/

    //进行函数调用
    if (jsclient.xianLiaoPara) {
        mylog("jsclient.xianLiaoPara合理，开始进行分享...");
        var para = jsclient.xianLiaoPara;
        if (para.type == XianLiaoType.text) {                       //文本
            mylog("文本分享...");
            var text = para.text;
            jsclient.native.dlShareText(text);
        }
        else if (para.type == XianLiaoType.image) {             //图片
            mylog("图片分享...");
            jsclient.native.dlShareImage();
        }
        else {                                                  //游戏邀请
            mylog("游戏邀请分享...");
            var roomId = para.roomId;
            var roomToken = para.roomToken;
            var title = para.title;
            var url = para.url;
            var description = para.description;
            mylog("roomId:" + roomId + "  roomToken" + roomToken + "  title:" + title + "  description:" + description);
            jsclient.native.dlShareUrl(url, title, description);
        }
    }
};

// 延时显示showMsg界面，因为当游戏在后台时，调用界面会缺失纹理，
// 暂时没有找到别的解决办法，只能用这种办法处理，哪位同志学如果有好的方法请指教指教，多谢！
jsclient.showMsgDelay = function (delay, msg, yesfunc, nofunc, style) {
    if (jsclient.Scene) {
        jsclient.Scene.scheduleOnce(
            function () {
                jsclient.showMsg(msg, yesfunc, nofunc, style);
            },
            delay
        );
    }
};

//游戏中送道具
jsclient.playPropAnimation=function(propType,fromNode,toNode,offX,offY,fromIndex,toIndex,playUIType)
{
    var newPosTo = toNode.getParent().convertToWorldSpace(toNode.getPosition());
    var newPosFrom = fromNode.getParent().convertToWorldSpace(fromNode.getPosition());
    jsclient.ShowAni(jsclient.playui,propType,newPosFrom,newPosTo,fromIndex,toIndex,playUIType);
}


//获取微信unionid
jsclient.getWXunionid = function () {
    mylog("获取微信unionid");
    // var unionid = "";
    // var WX_USER_LOGIN = sys.localStorage.getItem("WX_USER_LOGIN");
    // if (WX_USER_LOGIN) {
    //     WX_USER_LOGIN = JSON.parse(WX_USER_LOGIN);
    //     unionid = WX_USER_LOGIN.unionid;
    // }
    // mylog("获取微信unionid:" + unionid);
    // return unionid;

    if(jsclient.data.pinfo.unionid)
	{
        mylog("获取微信unionid == " + jsclient.data.pinfo.unionid);
		return jsclient.data.pinfo.unionid;
	}
	else
	{
        mylog("获取微信unionid ==  无" );
		return ""
	}
};

//当前版本是否支持高德SDK
function isSurportGDSDK() {
    return ("undefined" != typeof (jsb.fileUtils.getXXSecretData));
}

jsclient.tickGame = function (tickType) {
    return;
    if (jsclient.lastMJTick > 0) {
        if (jsclient.lastMJTick < Date.now() - 15000) {
            jsclient.lastMJTick = -1;
            jsclient.showMsg("网络连接断开(" + 20 + ")，请检查网络设置，重新连接", function () {
                jsclient.restartGame();
            },function () {

            },"1")
        }
        else {
            jsclient.gamenet.request("pkroom.handler.tableMsg", {cmd: "MJTick", tickType: tickType});
        }
    }
}
jsclient.tickServer = function () {
    if (jsclient.gamenet)
        jsclient.gamenet.request("pkcon.handler.tickServer", {}, function (rtn) {
            mylog("tick");
        });
}
jsclient.openWeb = function (para) {
    var msg  = para.help ? "openWeb" : "openWeb_readMe";
    sendEvent(msg, para);
}
jsclient.showMsg = function (msg, yesfunc, nofunc, style, msgKeepLeft) {
    sendEvent("popUpMsg", {msg: msg, yes: yesfunc, no: nofunc, style: (style || "1"), msgKeepLeft: (msgKeepLeft || false)});
}
function IsRoomOwner() {
    var sData = jsclient.data.sData;
    if(!sData){
        return false;
    }
    return sData.tData.owner ==SelfUid() ;
}
jsclient.delRoom = function (yes) {
    var sData = jsclient.data.sData;
    if(!sData){
        return;
    }
    var state_temp = TableState.waitJoin;
    if(jsclient.gameid=="zjh"||jsclient.gameid=="nn")
        state_temp = -1;//牛牛和炸金花的等待状态是-1
    if (sData.tData.tState == state_temp && sData.tData.owner != SelfUid())
    {
        jsclient.leaveGame();
    }
    else
    {
        jsclient.gamenet.request("pkroom.handler.tableMsg", {cmd: "DelRoom", yes: yes});
    }
}

// 推饼子游戏开始
jsclient.startTBZgame = function(){
    jsclient.block();
    jsclient.gamenet.request("pkroom.handler.tableMsg", 
        {cmd: "Tbz_Start"},
        function (rtn) {
        jsclient.unblock();
    }

);}

//抢庄协议
jsclient.tbzRob = function(isRob){
    jsclient.gamenet.request("pkroom.handler.tableMsg", {
        cmd     : "Tbz_Rob",
        isRob   : isRob
    });
    //回复消息 TbzRobnum   注：tData.robNum == tData.robNum = {uid:1,uid:2,uid:3}; //客户端更新tData.robNum里的数据
    /*例如
        Tbz_Rob_num: function(bl) {
            var sData = jsclient.data.sData;
            var tData = sData.tData;
            tData.robNum = bl;

        },*/
}

// 推饼子下注
jsclient.tbzBet = function(locate , num){
    //下注协议
    // if(jsclient.data.sData.tData.zhuang ==jsclient.data.pinfo.uid ) 
    // {
    //     jsclient.showMsg("庄家不能下注",function () {
            
    //                             },function () {
            
    //                             },"1");
    // }
    var tdata = jsclient.data.sData.tData;
    if(tdata.betOneSt[jsclient.data.pinfo.uid+""]&&(locate<=3))
    {
        // 
        if(tdata.betOneMsg[jsclient.data.pinfo.uid+""][locate])
        {

        }
        else
        {
            jsclient.showMsg("当前只能压一门",function () {
                
                                    },function () {
                
                                    },"1");
            return;
        }
    }
    
    
    if(jsclient.data.sData.tData.betMsg[jsclient.data.pinfo.uid+""])
    {
        var totalScore = jsclient.data.sData.tData.betMsg[jsclient.data.pinfo.uid+""][locate] + num;
        if(totalScore>5)
        {
            jsclient.showMsg("每门下注分数不能大于5分",function () {
                
                                    },function () {
                
                                    },"1");
            return;
        }
    }

    jsclient.gamenet.request("pkroom.handler.tableMsg", {
        cmd      : "Tbz_Bet", 
        locate   : locate ,
        num      : num 
    });
    
}


/**
 * 拉取在线图片
 * url：图片url
 * para：其它参数（图片拉取回来后直接将para放到消息体内即可）
 */
jsclient.loadImage = function (url, para) {
    if (url) cc.loader.loadImg(url, { isCrossOrigin: true }, function(err, texture) {
        if (!err && texture) {
            sendEvent("QueueNetMsg", ["LoadImageSuccess", { para: para, img: texture }]);
        }
    });
}

/**
 * 获取比赛列表
 * */
jsclient.GetMatchList = function () {
    jsclient.gamenet.request("pkplayer.handler.GetMatchList", {},
        function (rtn) {
            if (rtn) {
                if (rtn.result == 0) {
                    jsclient.gMatchList = eval(rtn);
                    Log("app.js jsclient.GetMatchList() jsclient.gMatchList:" + JSON.stringify(jsclient.gMatchList));
                }
                else {
                    jsclient.ShowToast("获取比赛列表失败，请重试！" + rtn.result);
                }
                //返回数据，通知比赛列表页刷新UI
                sendEvent("matchList");
            }
            else {
                jsclient.ShowToast("获取比赛列表失败，请重试!!");
            }
        });
}



/**
 * 注册比赛&报名
 * */
jsclient.RegistMatch = function (index) {
    Log("app.js jsclient.RegisterMatch() index:" + index);
    //客户端自己判断下金豆是否足够报名费，不够直接提示跳商城购买
    if (jsclient.gMatchList && jsclient.gMatchList.list && index < jsclient.gMatchList.list.length) {
        var matchData = jsclient.gMatchList.list[index];
        Log("app.js jsclient.RegisterMatch() matchData:" + JSON.stringify(matchData));
        var isEnableRsigter = true;
        var tipStr;
        var tab;
        if (matchData.regist_fee_type == "diamond") {//钻石报名
            if (jsclient.data.pinfo.money < matchData.regist_fee) {
                isEnableRsigter = false;
                tipStr = "钻石不足，请先购买再报名";
                tab = 1;
            }
        } else {//金豆报名
            if (jsclient.data.pinfo.coin < matchData.regist_fee) {
                isEnableRsigter = false;
                tipStr = "金豆不足，请先购买再报名";
                tab = 0;
            }
        }
        if (false == isEnableRsigter) {
            jsclient.ShowToast(tipStr);
            // jsclient.showShopCoinLayer();
            // jsclient.switchShopTab(tab);
            return;
        }
    } else {
        jsclient.ShowToast("数据异常，请刷新后重试!");
        return;
    }
    Log("-------------------------------RegistMatch");

    sys.localStorage.setItem("matchGameID", matchData.createPara.gameid);
    var para = {
        "matchId": jsclient.gMatchList.list[index]._id,
        "gameid": matchData.createPara.gameid,
        "roomid": "symj1",
        "__route__": "pkplayer.handler.RegistMatch"
    };
    Log("RegistMatch para =" + JSON.stringify(para));
    jsclient.gamenet.request("pkplayer.handler.RegistMatch", para,
        function (rtn) {
            if (rtn) {
                Log("pkplayer.handler.RegistMatch back rtn = " + JSON.stringify(rtn));
                //{"result":0,"wait_time":59996,"joinCnt":1,"matchId":4149645,"realMatchId":3669496,"max_wait_time":60000}
                if (rtn.result == 0) {
                    jsclient.ShowToast("比赛场报名成功!");
                    jsclient.signedMatchData = jsclient.gMatchList.list[index];
                    sendEvent("matchRegistSuccess", {matchID: para.matchId, cTime: rtn.ctime});
                    
                    jsclient.showMatchWaitStartLayer(jsclient.gMatchList.list[index]);
                    jsclient.matchQueueUI.updateMatchWaitTime(rtn.wait_time);
                    var signedCount = rtn.joinCnt, gapCount = 0;
                    if (jsclient.signedMatchData && jsclient.signedMatchData.player_num_max)
                        gapCount = jsclient.signedMatchData.player_num_max - signedCount;
                    jsclient.matchQueueUI.refreshSignUpUI(signedCount, gapCount);
                    
                    //存储一下realmatchid
                    jsclient.realMatchId = rtn.realMatchId;
                    jsclient.showMatchDebugInfo({rID: rtn.realMatchId});
                    sys.localStorage.removeItem(jsclient.gMatchList.list[index]._id);
                } else {
                    if (rtn.result == MatchWrongCode.kCode_MatchCreateLock || rtn.result == MatchWrongCode.kCode_MatchCreateRedisLock) {
                        //创建房间加锁后自动报名3次，如果仍然报名失败则停止自动报名，提示用户手动重试
                        if (sys.localStorage.getItem(jsclient.gMatchList.list[index]._id)) {
                            var count = sys.localStorage.getItem(jsclient.gMatchList.list[index]._id) / 1;
                            if (count < 3) {
                                sys.localStorage.setItem(jsclient.gMatchList.list[index]._id, (count + 1));
                                jsclient.RegistMatch(index);
                            } else {
                                sys.localStorage.removeItem(jsclient.gMatchList.list[index]._id);
                                jsclient.ShowToast("比赛场报名失败，请稍后重试!");
                            }
                        } else {
                            sys.localStorage.setItem(jsclient.gMatchList.list[index]._id, 1);
                            jsclient.RegistMatch(index);
                        }
                    } else if (rtn.result == MatchWrongCode.kCode_AlreadyInRoom) {
                        jsclient.ShowToast("您已在好友房中，比赛场报名失败!");
                    } else if (rtn.result == MatchWrongCode.kCode_AlreadyInMatch) {
                        jsclient.ShowToast("您已在比赛中，不能同时报名两场比赛!");
                        jsclient.GetMatchRecover();
                    }else if(rtn.result == MatchWrongCode.kCode_OverMaxCount){
                        if(jsclient.remoteCfg && jsclient.remoteCfg.matchStartMaxPrompt){
                            jsclient.showMsg(jsclient.remoteCfg.matchStartMaxPrompt);
                        }else{
                            jsclient.showMsg("今日比赛已结束，欢迎明日报名参加！");
                        }
                    }else {
                        jsclient.ShowToast("报名失败！");
                    }
                }
            } else {
                jsclient.ShowToast("报名失败，请重试！");
            }
        });
}



/**
 * 取消注册比赛/取消报名（在开赛之前）
 * */
jsclient.UnRegistMatch = function () {

    var matchGameID = sys.localStorage.getItem("matchGameID");
    var ipara = {"gameid": matchGameID, "roomid": "symj1", "__route__": "pkplayer.handler.UnRegistMatch"};
    jsclient.gamenet.request("pkplayer.handler.UnRegistMatch", ipara,
        function (rtn) {
            if (rtn) {
                Log("pkplayer.handler.UnRegistMatch rtn = " + JSON.stringify(rtn));
                if (rtn.result == 0) {
                    jsclient.ShowToast("取消报名成功");
                    sendEvent("matchUnregistSuccess");
                    jsclient.realMatchId = null;

                    jsclient.hideMatchDebugInfo();
                    
                    // jsclient.signedMatchData = null;
                } else {
                    sendEvent("matchUnregistFail", {code: rtn.result});
                    jsclient.ShowToast("取消报名失败! " + rtn.result);
                }
            }
            else {
                sendEvent("matchUnregistFail");
                jsclient.ShowToast("取消报名失败，请重试！");
            }
        });
}

jsclient.GetMatchRewardRecord = function (pIndex) {
    //判断是否还有数据，如果有则请求，没有则提示
    if (jsclient.gMatchRewardRecords && jsclient.gMatchRewardRecords.rows && pIndex >= jsclient.gMatchRewardRecords.rows.length) {
        return;
    }
    jsclient.block();
    var ipara = {"start": pIndex, "limit": 10};
    jsclient.gamenet.request("pkplayer.handler.GetRewardRecord", ipara,
        function (rtn) {
            jsclient.unblock();
            if (rtn && rtn.result == 0) {
                try {
                    Log("match reward record=" + JSON.stringify(rtn));
                    if (!jsclient.gMatchRewardRecords)
                        jsclient.gMatchRewardRecords = eval(rtn);
                    else {
                        var newData = eval(rtn);
                        if (newData.rows && newData.rows.length)
                            jsclient.gMatchRewardRecords.row.concat(newData.rows);
                    }
                }
                catch (e) {
                }
                sendEvent("matchRewardRecord");
            } else {
            }
        });
}

jsclient.GetMatchRecover = function () {
    Log("-------------------------------GetMatchRecover");
    var para = {roomid: "symj1"};
    Log("-------para:" + JSON.stringify(para));
    jsclient.block();
    jsclient.gamenet.request("pkplayer.handler.RecoverMatch", para,
        function (rtn) {
            mylog("recover接口返回");
            jsclient.unblock();
            if (rtn && rtn.result == 0) {
                try {
                    Log("match recover=" + JSON.stringify(rtn));
                    jsclient.gMatchRecover = eval(rtn);
                    // jsclient.ShowToast("recover消息：" + JSON.stringify(rtn) );
                    if (jsclient.gMatchRecover.match_config) {
                        if (!jsclient.gMatchList) {
                            var matchArray = new Array();
                            matchArray.push(jsclient.gMatchRecover.match_config);
                            jsclient.gMatchList = {"list": matchArray};
                        } else {
                            //判断返回的比赛是否在jsclient.gMatchList中存在，不存在则添加进去
                            if (!jsclient.gMatchList.list) {
                                jsclient.gMatchList.list = new Array();
                            }
                            var isExist = false;
                            for (var i = 0; i < jsclient.gMatchList.list.length; i++) {
                                var item = jsclient.gMatchList.list[i];
                                if (item && item._id == jsclient.gMatchRecover.match_config._id) {
                                    isExist = true;
                                    break;
                                }
                            }
                            if (false == isExist)
                                jsclient.gMatchList.list.push(jsclient.gMatchRecover.match_config);
                        }
                    }
                } catch (e) {
                    jsclient.ShowToast("恢复场景数据异常！");
                }
                jsclient.signedMatchData = jsclient.gMatchRecover.match_config;
                
                sendEvent("matchRecover");
                //如果有排名则显示排名页面（晋级页面），否则显示等待开始页面
                if (jsclient.gMatchRecover && jsclient.gMatchRecover.match_config && !jsclient.playui) {
                    if (jsclient.gMatchRecover.round > 0) {
                        if (!jsclient.gMatchRecover.in_table || jsclient.gMatchRecover.in_table == 0) {//游戏不在打
                            Log("app.js showMatchPromotLayer() before 3");
                            jsclient.showMatchPromotLayer();
                            jsclient.matchPromotUI.refreshUIOnUpdate({
                                matchId: jsclient.gMatchRecover.match_config._id,
                                round: jsclient.gMatchRecover.round,
                                rank: jsclient.gMatchRecover.rank
                            });
                        }
                    } else {
                        jsclient.showMatchWaitStartLayer(jsclient.gMatchRecover.match_config);
                        jsclient.matchQueueUI.refreshSignUpUI(jsclient.gMatchRecover.regist_num, jsclient.gMatchRecover.match_config.player_num_max - jsclient.gMatchRecover.regist_num);
                        jsclient.matchQueueUI.updateMatchWaitTime(jsclient.gMatchRecover.wait_time);
                    }
                }
            } else {
                jsclient.ShowToast("恢复场景数据失败！");
            }
        });
}


//显示比赛场的一些调试信息，pData对象中存储参数
jsclient.showMatchDebugInfo = function (pData) {
    return;
    if (cc.sys.isMobile) return;
    if (!jsclient.debugContainer) {
        jsclient.debugContainer = new cc.Node();
        jsclient.debugContainer.setPosition(cc.p(cc.director.getWinSize().width - 150, cc.director.getWinSize().height - 40));
        jsclient.Scene.addChild(jsclient.debugContainer, 1000);
    }
    jsclient.debugContainer.removeAllChildren(true);
    var tipStr = JSON.stringify(pData);
    var tipsLabel = new cc.LabelTTF(tipStr, "", 20);
    if (jsclient.playui) {
        tipsLabel.setFontFillColor(cc.color.BLACK);
    } else {
        tipsLabel.setFontFillColor(cc.color.WHITE);
    }
    
    jsclient.debugContainer.addChild(tipsLabel);
}



jsclient.hideMatchDebugInfo = function () {
    return;
    if (cc.sys.isMobile) return;
    if (jsclient.debugContainer) {
        jsclient.debugContainer.removeFromParent(true);
        jsclient.debugContainer = null;
    }
}



//切换tab消息，pTab：0代表金豆，1代表钻石
jsclient.switchShopTab = function(pTab, delay){
    if(jsclient.WebShop){
        if(!delay){
            delay = 0.05;
        }
        jsclient.WebShop.scheduleOnce(function () {
            sendEvent("switchToTab", pTab);
        },delay);
    }
}
//显示内嵌webview
jsclient.showWebview = function (url) {
    if (jsclient.matchWebviewUI) {
        jsclient.matchWebviewUI.removeFromParent(true);
        jsclient.matchWebviewUI = null;
    }
    jsclient.Scene.addChild(new MatchWebview(url));
}
jsclient.showMathShareJD = function(shareLayerDesc, url, title, desc){
    if (jsclient.matchShareJDUI) {
        jsclient.matchShareJDUI.removeFromParent(true);
        jsclient.matchShareJDUI = null;
    }
    jsclient.Scene.addChild(new matchShareJD(shareLayerDesc, url, title, desc));
}
//显示比赛活动公告
jsclient.showMatchNotice = function () {
    if (jsclient.homeTip) {
        jsclient.homeTip.removeFromParent(true);
        jsclient.homeTip = null;
    }
    jsclient.Scene.addChild(new HomeTip());
    jsclient.homeTip.visible = true;
    sendEvent("loadMatchNoticeImg");
}
//显示比赛场列表页面
jsclient.showMatchListLayer = function () {
    // if(jsclient.remoteCfg && ("undefined" == jsclient.remoteCfg.showMatchRoom || !jsclient.remoteCfg.showMatchRoom)){
    //     jsclient.showMsg("敬请期待");
    // }else
    {
        if (jsclient.matchListUI) {
            jsclient.matchListUI.removeFromParent(true);
            jsclient.matchListUI = null;
        }
        jsclient.Scene.addChild(new MatchListLayer());
    }
}
//显示比赛场的规则说明
jsclient.showMatchRuleLayer = function (pData) {
    if (jsclient.matchRuleUI) {
        jsclient.matchRuleUI.removeFromParent(true);
        jsclient.matchRuleUI = null;
    }
    jsclient.Scene.addChild(new MatchRuleLayer(pData));
}
//比赛结束后返回
jsclient.matchEndQuit = function () {
    jsclient.hideMatchDebugInfo();
    if (jsclient.playui){
        jsclient.playui.removeFromParent(true);
        delete jsclient.playui;
        jsclient.playui = null;
        delete jsclient.data.sData;
        if (jsclient.ready2Coin) { //add by wcx 20170224
            jsclient.ready2Coin = null;
            jsclient.requestCoinCgf();
        }
    }
    if (jsclient.signedMatchData)
        jsclient.signedMatchData = null;
}
//显示比赛场入口页面
jsclient.showMatchEnterLayer = function (pData) {
    if (jsclient.matchEnterUI) return;
    jsclient.Scene.addChild(new MatchEnterLayer(pData));
}
//显示比赛场的我的奖励
jsclient.showMatchAwardsLayer = function () {
    if (jsclient.matchAwardsUI) return;
    jsclient.Scene.addChild(new MatchAwardsLayer());
}
//显示比赛场晋级页面
jsclient.showMatchPromotLayer = function (pData) {
    if (jsclient.matchPromotUI) return;
    jsclient.Scene.addChild(new MatchPromotLayer(pData));
}
//显示比赛结束页面
jsclient.showMatchEndLayer = function (pData) {
    if (jsclient.matchEndUI) return;
    jsclient.Scene.addChild(new MatchEndLayer(pData));
}
//显示等待比赛开始界面
jsclient.showMatchWaitStartLayer = function (pData) {
    if (jsclient.matchQueueUI) return;
    jsclient.Scene.addChild(new MatchQueueLayer(pData));
}
//判断比赛是否是京东场比赛
jsclient.judgeMatchJD = function (pData) {
    var isJDMatch = false;
    if (pData){
        if (pData.type && pData.type == "jd")
            isJDMatch = true;
    }

    return isJDMatch;
}
jsclient.callDelRoom = function (yes) {
    function IsMatchRoom() {
        var sData = jsclient.data.sData;
        if (sData) {
            var tData = sData.tData;
            if (tData && (tData.xynmmjMatch || tData.matchDV2)) {
                return true;
            }
        }
        return false;
    }
    
    var sData = jsclient.data.sData;
    if (!sData) {
        return;
    }
    Log("request DelRoom");
    //多点的情况下如果报了比赛场会弹出解散房间的问题,所以比赛场不允许解散
    if (IsMatchRoom() && jsclient.signedMatchData)return;
    jsclient.gamenet.request("pkroom.handler.tableMsg", {cmd: "DelRoom", yes: yes});
}



 // time1, time2 的差值(单位秒),使用的是格林威治标准
function getTimeOff ( time1, time2, maxOff ) {
    var d1  = Date.parse(time1)/ 1000;
    var d2  = Date.parse(time2)/ 1000;
    var off = d2 - d1;
    return off ;
}

//检查七天登录
function checkIs7Day() {
	jsclient.gamenet.request("pkplayer.handler.GetAward", {type: "login"}, function (rtn) {
		if (rtn.result == 0) {
			//jsclient.showMsgDelay(0.2, "获得7天回归钻石奖励！");
			//创建领取成功界面
			jsclient.Scene.addChild(new SevenDayTipLayer());
		}
		else if (rtn.result == 126) {
			//jsclient.showMsgDelay(0.2, "完成1局游戏后，可获得7天回归钻石奖励");
		}
		else if (rtn.result == 68) {
            //jsclient.showMsgDelay(0.2, "领取成功！");
		}
		else {
			//jsclient.showMsgDelay(0.2, "领取失败！");
		}

	});
}


jsclient.ShowToast_Ep = function (txt) {
    if (!jsclient.Scene) return;
    var time = 1.5;
    var imgName = "res/aress/gameInner/oe_jy_bjt1.png";
    var visibleSize = jsclient.size;
    var text = new ccui.Text(txt, "Arial", 50);
    text.setAnchorPoint(0, 0.5);
    var layout = new ccui.ImageView();
    layout.loadTexture(imgName, ccui.Widget.LOCAL_TEXTURE);
    layout.setAnchorPoint(0.5, 0.5);
    layout.setCapInsets(cc.rect(11, 11, 9, 9));
    layout.setScale9Enabled(true);
    text.setPosition(cc.p(layout.getContentSize().width / 2 +25 , layout.getContentSize().height / 2));
    text.setColor({r: 208, g: 242, b: 208});
    layout.setPosition(visibleSize.width / 2, visibleSize.height / 4);
    layout.addChild(text);
    layout.visible = false;

    var moveTo1 = cc.EaseSineInOut.create(cc.moveTo(0.7, cc.p(visibleSize.width / 2, visibleSize.height / 2)));
    var moveTo2 = cc.moveTo(0.1, cc.p(visibleSize.width / 2, visibleSize.height / 2.1));
    var delay0 = cc.delayTime(0.8);
    var delay = cc.delayTime(time);
    var fadeout = cc.fadeOut(0.2);
    var callback = cc.callFunc(function () {
        layout.removeFromParent(true);
    })
    var callback0 = cc.callFunc(function () {
        layout.visible = true;
    })
    layout.runAction(cc.sequence( delay0,callback0,moveTo1,moveTo2, delay, fadeout, callback));
    jsclient.Scene.addChild(layout, 2000);
}


jsclient.netCallBack = {
    onKick: [0, function (d) {
        jsclient.iskicked = true;
        jsclient.showMsg('账号在其他设备上登录，请确认是否自己操作，及时更改密码...',function () {
           jsclient.restartGame();
        },function () {},"1");
    }],
    hongbao: [0, function (info) {
    }],
    GainExperience:[0, function (d) { //玩家获取经验
        if(d.ep){
            jsclient.ShowToast_Ep("+"+d.ep);

            if(d.self_oldep >= 0){
                var self_newep = d.self_oldep + d.ep;
                d.lv = checkPlayerUpGrade(d.self_oldep,self_newep);
            }
        }

        //头像框功能
        checkHeadFrameOpen(d);
    }],
    loadWxHead: [0.01, function (d) {

        //存储房间头像缓存信息
        if(jsclient.headImgObj && d.uid && d.img){
            jsclient.headImgObj[d.uid] = d.img;
        }


    }]
    ,liangA: [0.01, function (d) {

        var sData = jsclient.data.sData;
        if(d) {
            sData.tData = d;
            //jsclient.poker_dda.setA( d.AValue );
        }else
        {
        }
    }]
    ,fanpaiMsg: [1.5, function (d) {

    }]
    ,getNewCard: [0.01, function (d) {

        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        var tData = sData.tData;
        var uids = tData.uids;

        if (uids[tData.curPlayer] == SelfUid()) {

            var lastPut = d.tData.lastPutCard;
            // cc.log(" __ getNewCard __  == " + JSON.stringify(d));
            var sData = jsclient.data.sData;
            var pl = sData.players[SelfUid()];
            for (var i = 0; i < lastPut.length; i++)
            {
                pl.mjhand.push(lastPut[i]);
            }
        }
    }]
    ,UpdateCard: [0.01, function (d) {
        var sData = jsclient.data.sData;
        var pl = sData.players[SelfUid() + ""];
        pl.mjhand = d.mjhand;

        if (d.AValue)
            sData.tData.AValue = d.AValue

    }]
    ,MJLiangXi: [0.01, function (d) {
        for(var key in d.retData){

            var pl=jsclient.data.sData.players[key];
            pl.mjliangxi = d.retData[key];
        }        

    }]
    ,mjStateChange: [0, function (d) {

        var sData = jsclient.data.sData;
        if(d.tData) {
            sData.tData = d.tData;
        }else
        {
        }

        sData.players[SelfUid()].mjState = d.mjState;
    }]
    , UpdateTData: [0, function (d) {
        // if (jsclient.gameType == jsclient.GAME_TYPE.BH_POKER) {
            var sData = jsclient.data.sData;
            if(d.tData){
                sData.tData = d.tData;
            }
            else
            {
            }
            sData.players[d.uid].mjState = d.mjState;
        // }
    }]
    //斗地主
    ,MJRob:[0, function(d) {

        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        if(d.rob)
          sData.players[d.uid].robState = d.rob;

      if (jsclient.gameid == "nn") {
            if (d.player) {
                sData.players[d.player.uid].baseWin = d.player.baseWin;
                sData.players[d.player.uid].lastRob = d.player.lastRob;
                sData.players[d.player.uid].mjState = d.player.mjState;
            }
        }
    }]
    //经典叫分
    ,DDZJDJFRob:[0, function(d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
    }]
    //欢乐叫分
    ,DDZHLRob:[0, function(d) {

        var sData = jsclient.data.sData;
        sData.tData = d.tData;

       if(d.rob)
         sData.players[d.uid].robState = d.rob;
    }]
    //欢乐显示？
    ,DDZHLShow:[0, function(d) {

        var sData = jsclient.data.sData;
        sData.tData.showPlayer.push(d);
    }]
    //赖子叫分
    ,DDZLZRob:[0, function(d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        if(d.rob)
        sData.players[d.uid].robState = d.rob;
    }]
    //二人叫分
    ,DDZERRob:[0, function(d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        //sData.players[d.uid].robState = d.rob;
    }]
    //二人pass
    ,DDZERPass:[0,function(d){
        var sData=jsclient.data.sData;
        var pl=sData.players[SelfUid()];
        pl.mjState=d.mjState;
    }]
    //赖子出牌方法
    ,DDZLZPut:[0.8,function(d) {
        var sData = jsclient.data.sData;
        var tData = sData.tData;

        tData.lastPut = d.card;
        tData.lastPutID = d.id;
        tData.tState = TableState.waitEat;
        var pl = sData.players[d.uid + ""];
        if (!pl.mjput) {
            pl.mjput = [];
        }
        pl.mjput = d.card;
        pl.mjputID = d.id;
        playEffect("nv/" + d.card);
    }]
    //二人斗地主出牌方法
    ,DDZERPut:[0.8,function(d) {
        var sData = jsclient.data.sData;
        var tData = sData.tData;

        tData.lastPut = d.card;
        tData.lastPutID = d.id;
        var pl = sData.players[d.uid + ""];
        if (!pl.mjput) {
            pl.mjput = [];
        }
        pl.mjput = d.card;
        pl.mjputID = d.id;
        playEffect("nv/" + d.card);
    }]
    , MJChat: [0, function (d) {
    }]
    , downAndPlayVoice: [0, function (d) {
    }]
    ,UseProp: [0, function(d) {

        countChengJiuData("UseProp",d);
    }]
    , allPlayersFlower: [0, function (d) {

        //数据格式{uid:[141,131,121],uid:[]}
        for(var key in d){

            var pl=jsclient.data.sData.players[key];
            pl.mjflower = d[key];
        }
        sendEvent("initFlower");
    }]
    , ConfirmPLD: [0, function (d) {

        for(var key in d){

            var pl=jsclient.data.sData.players[key];
            pl.ConfirmPLD = d[key];
        }
        
        sendEvent("ConfirmPLD");
    }]
    , ConfirmKouPai: [0, function (d) {


        var sData = jsclient.data.sData;
        var pl = sData.players[SelfUid()];
        pl.liNum = d.liNum;
        pl.mjhand = d.mjhand;
        pl.mjli = d.mjli;
        pl.mjState = d.mjState;


    }]
    , KouPaiEnd: [0, function (d) {

        jsclient.data.sData.tData.tState = d.tState;
        // jsclient.data.sData = d;
        
        for(var key in d.retData){

            var pl=jsclient.data.sData.players[key];
            pl.mjState = d.retData[key].mjState;
            pl.liNum = d.retData[key].liNum;
        }


        //商都麻将扣牌玩法，开局动画的特殊处理
        sendEvent("showKaiPaiAction");

    }]
    , ConfirmPaoFen: [0, function (d) {

        for(var key in d){

            var pl=jsclient.data.sData.players[key];
            pl.ConfirmPaoFen = d[key];
        }
        
        // sendEvent("ConfirmPaoFen");
    }]
    , fenzhang: [0, function (d) {

    }]
    , initSceneData: [0.1, function (d) {

        jsclient.endRoomMsg = null;

        //象棋接入逻辑
        if (d.gameid == "xiangqi") {
            jsclient.gameid = d.gameid;
            jsclient.initSceneDataMsgByXQ(d)
            return;
        }


        //单局结算界面断线重连后,若游戏已经开始,点击结算的准备按钮清空了手牌的问题
        if(jsclient.endoneui){
            jsclient.endoneui.removeFromParent(true);
            jsclient.endoneui = null;
        }

        if (d.tData.roundNum <= -2) {
            jsclient.leaveGame();
            return -1;
        }
        else {

            //解决在比赛场和休闲场结束后该字段还存在的问题 导致聊天按钮点击无效
            if(jsclient.data.sData && jsclient.data.sData.tData) {
                jsclient.data.sData.tData.coinRoomCreate = null;
                jsclient.data.sData.tData.xynmmjMatch = null;
                jsclient.data.sData.tData.matchDV2 = null;
            }

            jsclient.data.sData = d;
            d.serverNow -= Date.now();

            jsclient.gameid = d.gameid;


            if (!jsclient.playui) {

                //jsclient.gameid = d.gameid;

                if(d.gameid == "ddz"){
                    if(d.tData.playType == "erddz" || d.tData.playerNum == 2){//二人斗地主
                        jsclient.Scene.addChild(new CardGameLayer_DDZ_ER());//二人斗地主
                    }else if(d.tData.playType == "lzddz"){ //赖子
                        jsclient.Scene.addChild(new CardGameLayer_DDZ_LZ());
                        // jsclient.Scene.addChild(new EndAllLayer_DDZ());
                    }else if(d.tData.playType == "scddz"){ //四人斗地主
                           if(d.tData.playerNum == 4) //四人
                           {
                               jsclient.Scene.addChild(new CardGameLayer_SCDDZ_FOUR());
                           }
                           else
                           {
                               jsclient.Scene.addChild(new CardGameLayer_SCDDZ());
                           }
                    }else if(d.tData.playType == "hlddz") //欢乐
                       {
                           jsclient.Scene.addChild(new CardGameLayer_DDZ_HL());
                       }
                       else{
                        //经典
                        jsclient.Scene.addChild(new CardGameLayer());
                    }

                }else if(d.gameid == "dda"){
                    jsclient.Scene.addChild(new PlayPokerDDALayer());
                }
                else if(d.gameid == "tbz"){
                    jsclient.Scene.addChild(new TBZGameLayer());
                }
                else if (jsclient.gameid == "zjh") {
                    jsclient.Scene.addChild(new CardGameLayer_ZJH());
                }
                else if (jsclient.gameid == "nn") {
                    jsclient.Scene.addChild(new CardGameLayer_NN());

                }else if(jsclient.gameid == "pdk"){
                    if (jsclient.data.sData.tData.playerNum == 3) {
                        jsclient.Scene.addChild(new CardGameLayer_pdk());
                    } else if (jsclient.data.sData.tData.playerNum == 2) {
                        jsclient.Scene.addChild(new P2CardGameLayer());
                    }
                }
                else{ //麻将

                    //重连判断是否选择过拉跑蹲
                    var isHideCard = false;
                    var isShowPLDLayer = false;
                    var isShowPaoFenLayer = false;
                    var pldValue = d.tData.pldValue;
                    if(pldValue != 0 && Object.keys(d.players).length == d.tData.playerNum )
                    {
                        var players = d.players;
                        
                        for(var key in players){

                            var pl=players[key];
                            var isSelect = false;
                            if (!pl.ConfirmPLD)
                            {
                                pl.ConfirmPLD = {};
                                pl.ConfirmPLD.isSelect = false;
                            }
                            isSelect = pl.ConfirmPLD.isSelect;

                            if(key == SelfUid() && !isSelect)
                            {
                                isShowPLDLayer = true;
                            }
                            if (!isSelect)
                            {
                                isHideCard = true;
                            }
                        }
                    }

                    var canPaofen =  d.tData.canPaofen;
                    if(canPaofen && Object.keys(d.players).length == d.tData.playerNum)
                    {
                        var players = d.players;
                        
                        for(var key in players){

                            var pl=players[key];
                            var isSelect = false;

                            cc.log("跑分----------------》 uid: "+key+"  ConfirmPaoFen: "+JSON.stringify(pl.ConfirmPaoFen)+"   daipaofenRoom = "+d.tData.daipaofenRoom);


                            if (!pl.ConfirmPaoFen)
                            {
                                pl.ConfirmPaoFen = {};
                                pl.ConfirmPaoFen.isSelect = false;
                            }
                            isSelect = pl.ConfirmPaoFen.isSelect;

                            if(key == SelfUid() && !isSelect)
                            {
                                isShowPaoFenLayer = true;
                            }
                            if (!isSelect)
                            {
                                isHideCard = true;
                            }
                        }
                    }

                    jsclient.Scene.addChild(new PlayLayer(isHideCard,isShowPLDLayer,isShowPaoFenLayer));//创建游戏打牌内界面-------------
                }
            }
                
            sendEvent("clearCardUI");
            jsclient.native.xlGetInviteGameInfo();

            if(isGuildTable()) {
                //牌友群本地记录下此次玩的时间，用于牌友群排序
				var guildId = jsclient.data.sData.tData.guildid;
				var plUid = SelfUid();
				var nowDate = new Date();
				var nowTime = nowDate.getTime();
                cc.sys.localStorage.setItem(plUid + "_" + guildId, nowTime);
			}
        }
    }],
    reinitSceneData: [0, function (d) {
        jsclient.data.sData = d;

        //判断是否在回放中
        jsclient.data.sData.isReplay = true;

        d.serverNow -= Date.now();
        if (!jsclient.replayui)
        {
            if(jsclient.replayLogModel == "ddz")
            {
                jsclient.gameid = "ddz";
                if(jsclient.data.sData.tData.playType == "erddz")
                {
                    jsclient.Scene.addChild(new ReplayLayer_ERDDZ());//二人斗地主
                }else if(jsclient.data.sData.tData.playType == "lzddz"){
                    jsclient.Scene.addChild(new ReplayLayer_LZDDZ());//二人斗地主
                }
                else
                {
                    jsclient.Scene.addChild(new ReplayLayer_DDZ());//欢乐，经典，三人闷抓
                }
            }else if(jsclient.replayLogModel == "dda")
            {
                jsclient.gameid = "dda";
                jsclient.Scene.addChild(new ReplayLayerDDA());//打大A

            }else if(jsclient.replayLogModel == "pdk"){
                jsclient.gameid = "pdk";
                if (!d.tData.cardNumbers) {
                    return;
                }
                d.tData.less45Cards = d.tData.less45Cards || 0;
                d.tData.forcePut = d.tData.forcePut || 1;
                d.tData.showCardsNum = d.tData.showCardsNum || 1;
                jsclient.data.sData = d;
                mylog("=回放信息=d:"+JSON.stringify(d))

                if (jsclient.data.sData.tData.tableType == 2) {//两副牌
                    jsclient.Scene.addChild(new DoubleCardGameLayer(true));
                    jsclient.Scene.addChild(new ReplayControllayer(), 1);
                }
                else {
                    if (jsclient.data.sData.tData.playerNum == 3) {
                        jsclient.Scene.addChild(new CardGameLayer_pdk(true));
                        jsclient.Scene.addChild(new ReplayControllayer(), 1);
                    } else if (jsclient.data.sData.tData.playerNum == 2) {
                        jsclient.Scene.addChild(new P2CardGameLayer(true));
                        jsclient.Scene.addChild(new ReplayControllayer(), 1);
                    }
                }
            }
            else
            {
                jsclient.gameid = "xynmmj";
                jsclient.Scene.addChild(newReplayLayer());
            }
        }  
        sendEvent("clearCardUI");
    }],
    removePlayer: [0, function (d) {
        var sData = jsclient.data.sData;
        if(sData){
            delete sData.players[d.uid];
            sData.tData = d.tData;
        }
    }],
    // addPlayer: [0, function (d) {
    //     var sData = jsclient.data.sData;
    //     if(!sData) return -1;
    //     sData.players[d.player.info.uid] = d.player;
    //     sData.tData = d.tData;
    // }],
      addPlayer: [0.1, function (d) {
          var sData = jsclient.data.sData;
          if (sData) {
              sData.players[d.player.info.uid] = d.player;
              sData.tData = d.tData;
              if(d.tData.isApplyingStarGame && d.tData.firstDel != -1){
                  if(jsclient.applyStartGameLayer){
                      jsclient.applyStartGameLayer.removeFromParent();
                      delete jsclient.applyStartGameLayer;
                      jsclient.applyStartGameLayer = null;
                  }
                  jsclient.showMsg("玩家人数改变，申请失败");
              }
          } else {
              return -1;
          }
      }],
    updateInfo: [0, function (info) {

        //群管理员变动通知
        guildManagerChangeNoticeFunc(info);

        var pinfo = jsclient.data.pinfo;

        //console.log("pinfo : "+JSON.stringify(pinfo));

        for (var pty in info) {
            pinfo[pty] = info[pty];
        }
        
        if(info["emoji"]){
            sendEvent("UpdateEmoji");
        }

        if(typeof (info.guildNeedAgree) != 'undefined') {
            sendEvent("updateGuildNeedAgree");
        }
    }],
    oncemore: [0, function (d) {
		//再来一局的邀请
        var sData = jsclient.data.sData;
        if(!sData || !sData.tData || (sData.tData && sData.tData.roundNum <= 0)||sData.tData.playType=="zjh") {
            //房间数据不存在或者局数为0时 表示玩家当前不在游戏过程中
            jsclient.showOnceMorePopup(d);
        }
    }],
    FreezeNotify: [0, function (d) {
		//预扣钻变化通知
		//JS: FreezeNotify d = {"uid":100875,"freeze":{"money":2,"gold":0}}
        var pinfo = jsclient.data.pinfo;
        pinfo.freeze = d.freeze;
        sendEvent("updateFreezeInfo");
    }],
    moveHead: [1, function () {
    }],
    mjhand: [0, function (d) {

        console.log("app.js-----mjhand============88888888888888888=开始游戏")

        sendEvent("clearEndUI");//移除结算界面
        sendEvent("clearCardUI");

        var sData = jsclient.data.sData;
        if (!d || !(d.tData) || !sData) { //容错
            return;
        }


        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        for (var uid in sData.players) {
            var pl = sData.players[uid];
            pl.mjpeng = [];
            pl.mjgang0 = [];
            pl.mjgang1 = [];
            pl.mjchi = [];
            pl.mjother = [];
            pl.mjput = [];
            pl.mjli = [];
            pl.mjflower = [];
            pl.ConfirmPLD = {};
            pl.ConfirmPLD.pao = false;
            pl.ConfirmPLD.la = false;
            pl.ConfirmPLD.dun = false;

            pl.liNum = sData.tData.lipaiNum;
            pl.skipHu = false; //每局开始重置过胡状态
            pl.waitTing = false;
            pl.cannotHuCard = [];
            pl.skipPeng     = [];
            pl.mjliangxi = 0;
            pl.cardIndex = 0;
            pl.baoting = false;
            /*
            if(sData.tData.mjType==MjType.TYMJ){
                pl.liNum = 4;
            }else{
                pl.liNum = 0;
            }
            */

            //兴安盟麻将带跑分，并选择一跑到底了，就不必清空数据，所有局数跑分相同
            if(!sData.tData.daipaofenRoom){
                pl.ConfirmPaoFen = {};
                pl.ConfirmPaoFen.paofen = -1;
            }


            pl.isTing = false;
            // delete pl.mjli;
            delete pl.mjhand;
            delete pl.mjting;

            if(sData.tData.kouPai)
                pl.mjState = TableState.kouPai;
            else
                pl.mjState = TableState.waitPut;
            
            if (uid == SelfUid()) {
                pl.mjhand = d.mjhand;
                if (d.mjli != null) pl.mjli = d.mjli;
                pl.mjting = d.mjting ? d.mjting : [];
                pl.mjpeng4 = [];
            }

        }

        //象棋逻辑
        if(jsclient.gameid == "xiangqi"){
            jsclient.mjhandByXQ(d);
            return 0;
        }

        if(jsclient.gameid != "dda")
            playEffect("shuffle");

        clickTing = false;

        //鄂市麻将 跑拉蹲
        var pldValue = sData.tData.pldValue;
        if(pldValue != 0) {
            sendEvent("showPLDLayer");
        }

        //兴安盟跑分选择界面
        if(sData && sData.tData.canPaofen) {
            if(sData.tData.roundAll == sData.tData.roundNum || !sData.tData.daipaofenRoom){
                sendEvent("showPaoFenLayer");
            }else{
                //sendEvent("ConfirmPaoFen");
            }
        }

    }]
    , MJPass: [0, function (d) {
        var sData = jsclient.data.sData;
        if(!sData) return;
        var tData = sData.tData;
        var pl = sData.players[SelfUid()];
        pl.mjState = d.mjState;
        mylog("MJPass");
    }]
    ,MJAuto: [0, function (d) {
        var sData = jsclient.data.sData;
        if (sData) {
            var tData = sData.tData;
            if (d.uid) {
                sData.players[d.uid].autoState = d.autoState;
            }
        }
    }]
    , MJPut: [0.05, function (d) {

        sendEvent("removeOutAction");

        if(jsclient.gameid == "ddz"){

            var sData = jsclient.data.sData;
            var tData = sData.tData;
            tData.lastPut = d.card;
            tData.tState = TableState.waitEat;
            tData.putType = d.putType;
            var pl = sData.players[d.uid + ""];
            if (!pl.mjput) {
                pl.mjput = [];
            }
            pl.mjput.push(d.card);
            if (d.uid == SelfUid()) {
                pl.mjState = TableState.waitPut;
                pl.skipHu = false;
            } else {
                sData.players[SelfUid() + ""].mjState = TableState.waitEat;
            }
            return;
        }

        if(jsclient.gameid == "dda"){

            var sData = jsclient.data.sData;
            var tData = sData.tData;

            tData.lastPutCard = d.card;

            var pl = sData.players[d.uid + ""];
            if (!pl.mjput) {
                pl.mjput = [];
            }
            pl.mjput.push(d.card);

            if (d.changeCardFlag)
                return

            var ddatype = jsclient.poker_dda.getCardsType(d.card);
            if(ddatype.cardsType == 1)
            {
                // ddatype.level == BaseType.danzhang 
                switch(ddatype.level){
                    case  1: //单
                        var cardNum = d.card%100;
                        playEffectDda("dda/cardtype_dan_"+cardNum, d);
                    break;
                    case  2: //对子
                        var cardNum = d.card[1]%100;
                        playEffectDda("dda/cardtype_dui_"+cardNum, d);
                    break;
                    case  3: //顺子
                        playEffectDda("dda/cardtype_dl", d);
                    break;
                    case  4: //连对
                        var cardNum = d.card[1]%100;
                        playEffectDda("dda/cardtype_sl", d);
                    break;
                    case  5: //王
                        playEffectDda("dda/cardtype_dan_"+d.card, d);
                    break;
                    case  6: //大A
                        if (d.uid == tData.zhuang.zhu) {
                            playEffectDda("dda/dda_zhufangA", d);
                        } else if (d.uid == tData.zhuang.ci) {
                            playEffectDda("dda/dda_cifangA", d);
                        }
                    break;
                }
            }
            else if(ddatype.cardsType == 2)
            {
                switch(ddatype.level){
                    case  1: //三张相同
                        var cardNum = d.card[1]%100;
                        playEffectDda("dda/cardtype_san_"+cardNum, d);
                    break;
                    case  2: //4张相同
                        var cardNum = d.card[1]%100;
                        playEffectDda("dda/cardtype_si_"+cardNum, d);
                    break;
                    case  3: //5张相同
                        playEffectDda("dda/cardtype_ddz", d);
                    break;
                    case  4: //6张相同
                        playEffectDda("dda/cardtype_ddz", d);
                    break;
                    case  5: //双王
                        playEffectDda("dda/cardtype_2king", d);
                    break;
                    case  6: //连珠炸弹
                        playEffectDda("dda/cardtype_sld", d);
                    break;   
                    case  7: //假八
                        playEffectDda("dda/cardtype_jb", d);
                    break; 
                    case  8: //真七
                        playEffectDda("dda/cardtype_zq", d);
                    break; 
                    case  9: //真八
                        playEffectDda("dda/cardtype_zb", d);
                    break;   
                    case  10: //三张王
                        playEffectDda("dda/cardtype_3king", d);
                    break; 
                    case  11: //四张王
                        playEffectDda("dda/cardtype_4king", d);
                    break; 
                    case  12: //双A
                        playEffectDda("dda/cardtype_doubleA", d);
                    break;                  
                }
            }

            return;
        }

        if(jsclient.gameid == "pdk"){
            jsclient.mjputPDK(d);
            return;
        }

        if(jsclient.gameid == "xiangqi"){
            jsclient.MJPutByXQ(d);
            return;
        }

        if(isFlower(d.card))
        {
            var sData = jsclient.data.sData;
            var pl = sData.players[d.uid];
            if (d.uid == SelfUid()) {
                var idx = pl.mjhand.indexOf(d.card);
                if (idx >= 0) {
                    pl.mjhand.splice(idx, 1);
                }
            }

            pl.mjflower.push(d.card);

            return;
        }


        var sData = jsclient.data.sData;
        var tData = sData.tData;
        tData.lastPut = d.card;
        tData.tState = TableState.waitEat;
        tData.putType = d.putType;
        var pl = sData.players[d.uid];

        if(d.isTing){
            pl.liNum-=1;
            pl.isTing = d.isTing;
            pl.baoting = tData.canBaojiao;
            if( !d.isTingPutCardCanEat )
                 pl.mjput.push(999);
            else{
                pl.mjput.push(d.card);
                playEffect("give");
                playEffect("nv/" + d.card);
            }
            
        }else{
            pl.mjput.push(d.card);
            playEffect("give");
            playEffect("nv/" + d.card);
        }
        if (d.uid == SelfUid()) {
            pl.mjhand.splice(pl.mjhand.indexOf(d.card), 1);
            pl.mjState = TableState.waitPut;

            //只有门清才能继续过胡 才会重置过胡状态。否则永远不能被点炮
            if(pl.mjgang0.length + pl.mjpeng.length + parseInt(pl.mjchi.length/3) == 0)
            {
                pl.skipHu = false;  
            }
            pl.skipPeng = [];
            if(d.isTing){
                pl.mjli.splice(pl.mjli.indexOf(d.card), 1);
                pl.mjting.push(d.card);
            }
        }
        else {
            sData.players[SelfUid() + ""].mjState = TableState.waitEat;
        }
    }],
    newCard: [0, function (d) {
        var sData = jsclient.data.sData;
        var pl = sData.players[SelfUid() + ""];
        var hands = pl.mjhand;

        var tData = sData.tData;
        var uids = tData.uids;

        //setCardTouchEnable( false );
        //hands.push(d);

        if(jsclient.replayui && uids[tData.curPlayer] != SelfUid())
        {
            return;
        }


        //更新打牌时，同样牌筛选提示功能信息
        refreshSelectMjObjFun(d);

        //对新发的那张牌，进行桌面上同样牌的检索
        sendSelectMjEvent(true);


        if (hands.length % 3 == 1) {
            pl.isNew = true;
            pl.cardIndex = pl.cardIndex + 1;
            var isCanTouch = false;
            if (_settingData.CheckBoxFreeMoveMj && !pl.isTing && pl.autoState != AutoState.autoYes){
                isCanTouch = true;
            }  
            setCardTouchEnable(isCanTouch, 8);
            hands.push(d);

            //是否正在补花
            if (pl.autoState != AutoState.autoYes && findOneFlow(pl, tData)) {
                _isPickingFlowerCard = true;
            }else{
                _isPickingFlowerCard = false;
            }


        } else {
            if (!(jsclient.replayui && jsclient.replayui.visible)) {
                return -1;
            }
        }

    }],

    cardNextNotice:[0,function(d){
        var sData = jsclient.data.sData;
        if(sData.tData){
            sData.tData.cardsNum = d.cardsNum;
            sData.tData.cardNext = d.cardNext;
            cc.log("收到消息： cardsNum = "+d.cardsNum+" cardNext: "+d.cardNext);
        }
    }],
    waitPut: [0, function (d) {
        
        if(jsclient.gameid == "xiangqi"){
            jsclient.waitPutByXQ(d);
            return;
        }
        
        var sData = jsclient.data.sData;
        
        sData.tData.tState = d.tState;
        sData.tData.curPlayer = d.curPlayer;
        sData.tData.lastPutPlayer = d.lastPutPlayer;

        sData.players[SelfUid() + ""].mjState = TableState.waitPut;
        
        
        var pl = sData.players[SelfUid() + ""];
        var isCanTouch = false;
        if(_settingData.CheckBoxFreeMoveMj && !pl.isTing && pl.autoState!=AutoState.autoYes)  {
            isCanTouch = true;
        }
        setCardTouchEnable( isCanTouch , 9);
    }],

    MJPlayReadyOK: [0, function (d) {
        var sData = jsclient.data.sData;
        if (sData) {
            var tData = sData.tData;
            if (d.uid) {
                sData.players[d.uid].mjState = TableState.isReady;
            }
        }
    }],
    MJkickoutPlayerOk: [0, function (d) {
        // var i = 0;
    }],
    MJkickoutPlayer: [0, function (d) {
        // var i = 0;
    }],
    MJCoinRoomCost: [0, function (d) {
        if (d.coin) {
            var sData = jsclient.data.sData;
            var players = sData.players;
            for (var i in players) {
                sData.players[i].info.coin -= parseInt(d.coin);
                if (sData.players[i].info.coin < 0) {
                    sData.players[i].info.coin = 0;
                }
            }
        }
    }],
    MJVIPRoomUpdate: [0, function (d) {
        if (!d) {
            return;
        }

        if(jsclient.gameid == "xiangqi"){
            jsclient.MJVIPRoomUpdateByXQ(d);
            return;
        }

        var sData = jsclient.data.sData;
        var players = sData.players;
        for (var uid in d) {
            var iplayer = sData.players[uid];
            if (iplayer) {
                iplayer.info.coin += parseInt(d[uid]);
                if (iplayer.info.coin < 0) {
                    iplayer.info.coin = 0;
                }
            }
        }
    }],
    
    MJChi: [0, function (d) {

        sendEvent("removeOutAction");

        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        var tData = sData.tData;
        var uids = tData.uids;
        var cds = d.mjchi;
        cds.sort(function (a, b) {
            return a - b
        });

        //mylog("MJChi "+d.mjchi+" "+d.from+" "+tData.curPlayer);
        playEffect("nv/chi");
        var pl = sData.players[uids[tData.curPlayer]];
        var lp = sData.players[uids[d.from]];
        for (var i = 0; i < cds.length; i++) {
            pl.mjchi.push(cds[i]);
            pl.isNew = false;
            if (i == d.pos) {
                var mjput = lp.mjput;
                if (mjput.length > 0 && mjput[mjput.length - 1] == cds[i]) {
                    mjput.length = mjput.length - 1;
                }
                else  mylog("eat error from");
            }
            else if (uids[tData.curPlayer] == SelfUid()) {
                pl.mjState = TableState.waitPut;
                var mjhand = pl.mjhand;
                var idx = mjhand.indexOf(cds[i]);
                if (idx >= 0) {
                    mjhand.splice(idx, 1);
                }
                else {

                    mylog("eat error to");
                }
            }
        }
        if( pl.mjgang0.length + pl.mjpeng.length + parseInt(pl.mjchi.length/3) - 1 == 0 ){
            pl.skipHu = false;
        }
    }],

    cannotHuCard : [ 0 , function (data) {
        var sData = jsclient.data.sData;
        var pl = sData.players[SelfUid()];
        pl.cannotHuCard.push( data.card );

    } ],
    MJPeng: [0, function (d) {

        sendEvent("removeOutAction");

        var sData = jsclient.data.sData;

        // cc.log("准备比较差异");
        // var oldData = deepCloneByJSON(sData.tData);
        // getDataDlt("mjpeng", oldData, d.tData);


        sData.tData = d.tData;
        var tData = sData.tData;
        var uids = tData.uids;
        var cd = tData.lastPut;




        //mylog("MJPeng "+cd+" "+d.from+" "+tData.curPlayer);

        playEffect("nv/peng");
        var pl = sData.players[uids[tData.curPlayer]];
        var lp = sData.players[uids[d.from]];
        pl.mjpeng.push(cd);
        var mjput = lp.mjput;
        if (mjput.length > 0 && mjput[mjput.length - 1] == cd) {
            mjput.length = mjput.length - 1;
        }
        else  mylog("peng error from");

        if (d.pengLiInfo != null)
        {
            pl.liNum = d.pengLiInfo.liNum;//处理立牌数据
        }
        
        if (uids[tData.curPlayer] == SelfUid()) {
            pl.mjState = TableState.waitPut;
            pl.isNew = false;
            var mjhand = pl.mjhand;
            var idx = mjhand.indexOf(cd);
            if (idx >= 0) {
                mjhand.splice(idx, 1);
            }
            else mylog("eat error to");
            idx = mjhand.indexOf(cd);
            if (idx >= 0) {
                mjhand.splice(idx, 1);
                if( pl.mjgang0.length + pl.mjpeng.length + parseInt(pl.mjchi.length/3) - 1 == 0 ){
                    pl.skipHu = false;
                }
            }
            else mylog("eat error to");

            //处理立牌数据
            var mjli = pl.mjli;
            if (mjli != null) {

                idx = mjli.indexOf(cd);
                if (idx >= 0&&mjli.length>1) {
                    mjli.splice(idx, 1);
                }
                idx = mjli.indexOf(cd);
                if (idx >= 0&&mjli.length>1) {
                    mjli.splice(idx, 1);
                }
            }
            
            if (mjhand.indexOf(cd) >= 0)  pl.mjpeng4.push(cd);
        }
    }]
    , MJGang: [0, function (d) {

        sendEvent("removeOutAction");
        //mylog("MJGang "+d.card+" "+d.gang+" "+d.from);
        playEffect("nv/gang");
        var sData = jsclient.data.sData;
        var tData = sData.tData;
        var uids = tData.uids;
        var cd = d.card;
        var pl = sData.players[d.uid];
        pl.liNum = d.liNum;
        if (d.gang == 1) {
            pl.mjgang0.push(cd);
            if (d.uid == SelfUid()) {
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
                //处理立四牌
                if(pl.mjli.indexOf(cd)>=0){
                    pl.mjli.splice(pl.mjli.indexOf(cd), 1);
                }
                if(pl.mjli.indexOf(cd)>=0){
                    pl.mjli.splice(pl.mjli.indexOf(cd), 1);
                }
                if(pl.mjli.indexOf(cd)>=0){
                    pl.mjli.splice(pl.mjli.indexOf(cd), 1);
                }
                if( pl.mjgang0.length + pl.mjpeng.length + parseInt(pl.mjchi.length/3) - 1 == 0 ){
                    pl.skipHu = false;
                }
            }

            var lp = sData.players[uids[d.from]];
            var mjput = lp.mjput;
            if (mjput.length > 0 && mjput[mjput.length - 1] == cd) {
                mjput.length = mjput.length - 1;
            }
            else  mylog("gang error from");
            pl.isNew = false;
        }
        else if (d.gang == 2) {
            pl.mjgang0.push(cd);
            pl.mjpeng.splice(pl.mjpeng.indexOf(cd), 1);
            if (d.uid == SelfUid()) {
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
            }
        }
        else if (d.gang == 3) {
            pl.mjgang1.push(cd);
            if (d.uid == SelfUid()) {
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
                pl.mjhand.splice(pl.mjhand.indexOf(cd), 1);
                //处理立四牌
                if(pl.mjli.indexOf(cd)>=0){
                    pl.mjli.splice(pl.mjli.indexOf(cd), 1);
                }
                if(pl.mjli.indexOf(cd)>=0){
                    pl.mjli.splice(pl.mjli.indexOf(cd), 1);
                }
                if(pl.mjli.indexOf(cd)>=0){
                    pl.mjli.splice(pl.mjli.indexOf(cd), 1);
                }
            }
        }
        tData.curPlayer = tData.uids.indexOf(d.uid);
        tData.lastPut = cd;
        //tData.leaveCardNum++; //FIX BUG 呼市麻将，线上反馈不能碰，由此导致。
        if (!tData.noBigWin || (d.gang == 2 && tData.canEatHu)) tData.putType = d.gang;

        tData.tState = TableState.waitEat;

        if (d.uid == SelfUid()) {
            pl.mjState = TableState.waitCard;
        }
        else {
            sData.players[SelfUid() + ""].mjState = TableState.waitEat;
        }


    }],

    //牛牛和炸金花消息---------------------begin
    DZReady: [0, function (d) {

        var sData = jsclient.data.sData;
        sData.players[d.PlayerId].CallState = d.state;
        sData.players[d.PlayerId].afk = d.afk;
        sData.tData = d.tData;

    }],
    ChangeStartPower:[0, function (d) {
        var sData = jsclient.data.sData;
        if(typeof (sData) =="undefined")    return;
        for (var i = 0; i < d.players.length; i++) {
            sData.players[d.players[i]].CallState = d.state_arr[i];
        }
        if(typeof (sData) =="undefined")    return;
        sData.tData = d.tData;
    }],
    DZSetCapital: [0, function (d) {
        //设置完小盲注
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        /**
         players:players,
         Thesurplus:Thesurplus,
         state_arr:state_arr,
         looked_arr:looked_arr,
         turnNum_arr:turnNum_arr,
         bitNum_arr:bitNum_arr,
         winone_arr:winone_arr,
         tData:tData});
         */
        if (d.tData.playType == "zjh") {
            for (var i = 0; i < d.players.length; i++) {
                sData.players[d.players[i]].surplus = d.surplus_arr[i];
                sData.players[d.players[i]].capital = d.Thesurplus[i];
                sData.players[d.players[i]].CallState = d.state_arr[i];
                sData.players[d.players[i]].looked = d.looked_arr[i];
                sData.players[d.players[i]].turnNum = d.turnNum_arr[i];
                sData.players[d.players[i]].bitNum = d.bitNum_arr[i];
                sData.players[d.players[i]].winone = d.winone_arr[i];
            }
        } else if (d.tData.playType == "nn") {
            for (var i = 0; i < d.players.length; i++) {
                sData.players[d.players[i]].surplus = d.Thesurplus[i];
                sData.players[d.players[i]].capital = d.Thesurplus[i];
                sData.players[d.players[i]].CallState = d.state_arr[i];
                sData.players[d.players[i]].multiple = d.multiple_arr[i];
                sData.players[d.players[i]].winone = d.winone_arr[i];
                sData.players[d.players[i]].PushCoinCount = d.PushCoinCount_arr[i];
                sData.players[d.players[i]].isLastPushCoin = d.isLastPushCoin_arr[i];
                sData.players[d.players[i]].side_player = d.side_player_arr[i];
            }
            sendEvent("clearCardUI");
            sendEvent("initPlayerCardEff");
        } else if (jsclient.gameModel == "sg") {
            for (var i = 0; i < d.players.length; i++) {
                sData.players[d.players[i]].surplus = d.Thesurplus[i];
                sData.players[d.players[i]].capital = d.Thesurplus[i];
                sData.players[d.players[i]].CallState = d.state_arr[i];
                sData.players[d.players[i]].multiple = d.multiple_arr[i];
                sData.players[d.players[i]].winone = d.winone_arr[i];
            }
            sendEvent("clearCardUI");
        }
    }],
    changeTState2Play: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData.tState = d.tState;
        for (var key in sData.players) {
            sData.players[key].CallState = d.CallState;
        }
    }],
    DZNext: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        sData.players[d.uid].CallState = d.CallState;
        sData.players[d.uid].curRollChip = d.curRollChip;
        sData.players[d.uid].surplus = d.surplus;
        sData.players[d.uid].bitNum = d.bitNum;
    }],
    compareRet: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        sData.players[d.ids[0]].CallState = d.result == 0 ? PSZ_PlayerState.P_STATE_RET_WIN : PSZ_PlayerState.P_STATE_RET_LOSE;
        sData.players[d.ids[1]].CallState = d.result == 0 ? PSZ_PlayerState.P_STATE_RET_LOSE : PSZ_PlayerState.P_STATE_RET_WIN;
    }],
    showCompareRetSelf: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
    }],
    showCompareRetOpo: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
    }],
    DZSettlement: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;


        for (var i = 0; i < d.PlayerIds.length; i++) {
            var t_arr = [];
            sData.players[d.PlayerIds[i]].winone = d.playerWinOne[i];
            sData.players[d.PlayerIds[i]].surplus = d.surplus[i];
            sData.players[d.PlayerIds[i]].curRollChip = d.curRollChipArr[i];
            sData.players[d.PlayerIds[i]].winTotal = d.winTotal[i];

            var cards_statistic_length = d.statistic_arr.shift();
            for (var j = 0; j < cards_statistic_length; j++) {
                t_arr.push(d.statistic_arr.shift());
            }


            sData.players[d.PlayerIds[i]].cards_statistic = t_arr;

        }
    }],
    DZWin: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
    }],
    AutoPlay: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.players[d.uid].autoPlay = d.autoPlay;
    }],
    AgreeLess: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        for (var i = 0; i < d.players.length; i++) {
            sData.players[d.players[i]].agreeless = d.agreeless_arr[i];
        }
    }],
    DontAgreeLess: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        sData.players[d.uid].agreeless = -1;
    }],
    AgreeLessTimeOut: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
    }],

    InvokeMultiple: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
    }],
    qiang: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        var pl = sData.players[d.uid];
        pl.CallState = d.CallState;
    }],
    qiangZhuang: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        var pl = sData.players[d.uid];
        pl.CallState = d.CallState;
        pl.multiple = d.multiple;
    }],
    tStateToCALL: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        if (d.tData.playType == "sg") {
            for (var key in sData.players) {
                sData.players[key].CallState = d.CallState;
            }
        } else if (d.tData.playType == "nn" || d.tData.playType == "dgn") {
            for (var i = 0; i < d.uids.length; i++) {
                if(d.uids[i]==0){
                    continue;
                }
                sData.players[d.uids[i]].multiple = 0;
            }
        }
    }],
    callMultiple: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        var pl = sData.players[d.uid];
        pl.multiple = d.multiple;
        pl.CallState = d.CallState;
    }],
    callMultipleAll: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        for (var uid in sData.players) {
            sData.players[uid].multiple = sData.tData.difen;
            sData.players[uid].CallState = d.CallState;
        }

    }],
    showSelfCard: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        var pl = sData.players[d.uid];
        pl.mjhand = d.mjhand;
        pl.CallState = d.CallState;

    }],
    calculateFinish: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        var pl = sData.players[d.uid];
        pl.mjhand = d.mjhand;
        pl.CallState = d.CallState;
    }],
    showCardToAll: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        var pl = sData.players[d.uid];
        pl.mjhand = d.mjhand;
        pl.CallState = d.CallState;

    }],
    HandsCard: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        for (var i = 0; i < d.uids.length; i++) {
            if(d.uids[i]==0){
                continue;
            }
            var t_arr = [];
            for (var j = 0; j < 5; j++) {
                t_arr.push(d.handsCard[0]);
                d.handsCard.splice(0, 1);
            }
            sData.players[d.uids[i]].mjhand = t_arr;
        }
    }],
    ComparCardAllNN: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        for (var i = 0; i < d.uid_arr.length; i++) {
            sData.players[d.uid_arr[i]].CallState = d.CallState_arr[i];
            sData.players[d.uid_arr[i]].cardType = d.cardType_arr[i];
            sData.players[d.uid_arr[i]].winone = d.winone_arr[i];

        }
    }],
    LookCard: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;
        sData.players[d.uid].looked = d.looked;
        sendEvent("LookCardEvent");
    }],
    openBiXiao: [0, function (d) {
    }],
    biXiaoMsg: [0, function (d) {
    }],
    AllPlayersHandsCard: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        for (var i = 0; i < d.uids.length; i++) {
            if(d.uids[i]==0){
                continue;
            }
            var t_arr = [];
            for (var j = 0; j < 3; j++) {
                t_arr.push(d.handsCard[0]);
                d.handsCard.splice(0, 1);
            }
            sData.players[d.uids[i]].mjhand = t_arr;
        }
        sData.players[SelfUid()].comp_ids = d.comp_ids;
    }],
    DZCapital: [0, function (d) {
        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        for (var i = 0; i < d.PlayerIds.length; i++) {
            sData.players[d.PlayerIds[i]].capital = d.capital[i];
        }
    }]
    //牛牛和炸金花消息---------------------end


    ,roundEnd: [0, function (d)//数据
    {

        sendEvent("removeOutAction");

        //移除地图界面
        _removeMapLayerFun();

        if(jsclient.gameid == "xiangqi"){
            jsclient.roundEndByXQ(d);
            return 0;
        }

        var sData = jsclient.data.sData;
        sData.tData = d.tData;

        for (var uid in d.players) {
            var pl = d.players[uid];
            pl.isTing = false;
            pl.mjflower = [];
            var plLocal = sData.players[uid];
            for (var pty in pl) {
                plLocal[pty] = pl[pty];
            }
        }

        //成就信息统计
        countChengJiuData("roundEnd");

        if (jsclient.gameid != "ddz" && sData.tData.winner && sData.tData.winner.length > 0) {
                playEffect("nv/hu");
        }

        if (d.playInfo && jsclient.data.playLog) {
            jsclient.data.playLog.logs.push(d.playInfo);
        }
    }]
    ,endRoom: [0, function (d) {


        if(jsclient.gameid == "xiangqi"){
            jsclient.endRoomByXQ(d);
            return;
        }

        jsclient.endRoomMsg = d;
        if (d.playInfo && jsclient.data.playLog) {
            jsclient.data.playLog.logs.push(d.playInfo);
        }

    }]
    ,
    onlinePlayer: [0, function (d) {

        if(jsclient.gameid == "xiangqi"){
          return jsclient.onlinePlayerByXQ(d);
            // return;
        }

        var sData = jsclient.data.sData;
        if (sData) {
            sData.players[d.uid].onLine = d.onLine;
            sData.players[d.uid].mjState = d.mjState;
        }else{
            return -1;
        }
    }]
    , MJTick: [0, function (msg) {
        var sData = jsclient.data.sData;
        jsclient.lastMJTick = Date.now();
        if (sData) {
            var tickStr = "";
            for (var uid in msg.players) {
                var pl = msg.players[uid];
                tickStr += pl.tickType + "|";
                var PL = sData.players[uid];
                if (PL) {

                    if (pl.tickType < 0 || pl.mjTickAt + 10000 < msg.serverNow) {
                        PL.onLine = false;
                    }
                    else {
                        PL.onLine = true;
                    }
                }
            }
            mylog("mjtick " + tickStr);
        }
    }]
    ,iosiapFinish:[0, function (d) {

    }]
    , DelRoom: [0, function (dr) {

        if(jsclient.gameid == "xiangqi"){
            jsclient.DelRoomByXQ(dr);
            return;
        }

        var sData = jsclient.data.sData;
        sData.tData = dr.tData;
        for (var uid in dr.players) {
            var pl = dr.players[uid];
            sData.players[uid].delRoom = pl.delRoom;

        }
        if (dr.nouid.length >= 1) {
            jsclient.showMsg("玩家 " + GetUidNames(dr.nouid) + " 不同意解散房间"
            ,function () {
                },function () {

                },"1");
        }
    }],
    delRoomInfo:[0, function (d) {
        mylog("-=-=-=-=-=-delRoomInfo");//处理跑得快，回放消息兼容问题
    }],

    DelRoom_HF: [0, function (dr) {

        var yesUid = dr.yesUid;
        let isReadyUid = [];
        if(dr.isReadyUid){
            isReadyUid = dr.isReadyUid;
        }
        var str = "";
        /*if(yesUid.length == 0)
        {
            str = "超时,默认其他玩家自动同意" + "\n" + "解散成功!!";
        }else{

            for (var i = 0; i < yesUid.length; i++) {
                if (yesUid[i] != dr.firstUid)
                    str += "玩家 " + GetUidNames([yesUid[i]]) + (((yesUid.length) >= 3) ? " ," : " ");
            }

            str += "同意" + "\n";

            if(dr.isReadyUid && dr.isReadyUid.length > 0){
                let isReadyUid = dr.isReadyUid;
                for (let i = 0; i < isReadyUid.length; i++) {
                    str += "玩家 " + GetUidNames([isReadyUid[i]]) + (((isReadyUid.length) >= 3) ? " ," : " ");
                }
                str += "已准备" + "\n";
            }

            if(dr.reason == 1) //超时成功
            {
                str += "超时!, 默认其他玩家自动同意, " + "解散成功!!";
            }else if(dr.reason == 2){ 

                str += "房间解散成功!!" ;
            }
            
        }*/

        if(!jsclient.data || !jsclient.data.sData || !jsclient.data.sData.tData){
            return;
        }
        let uids = jsclient.data.sData.tData.uids;

        if(dr.reason != 4 && dr.reason != 5){
            for (let i = 0; i < uids.length; i++) {
                str += "玩家 " + GetUidNames([uids[i]]);
                if(yesUid.indexOf(uids[i]) >= 0 || uids[i] == dr.firstUid){
                    str += "已同意";
                }else {
                    str += "未操作";
                }
                if(isReadyUid.length > 0){
                    if(isReadyUid.indexOf(uids[i]) >= 0){
                        str += "(已准备)";
                    }else {
                        str += "(未准备)";
                    }
                }
                str += "\n";
            }
            if(yesUid.length == 0 || dr.reason == 1){
                str += "超时! 默认其他玩家自动同意," + "解散成功!!";
            }else if(dr.reason == 2){
                str += "房间解散成功!!" ;
            }

            str = "玩家 " + GetUidNames([dr.firstUid]) + " 发起解散房间" + "\n" + str;
        }else {
            if(isReadyUid.length > 0){
                for (let i = 0; i < uids.length; i++) {
                    str += "玩家 " + GetUidNames([uids[i]]);
                    if(isReadyUid.indexOf(uids[i]) >= 0){
                        str += "已准备";
                    }else {
                        str += "未准备";
                    }
                    str += "\n";
                }
            }
            if(dr.reason == 4) {//群主或管理员解散房间
                str += "牌友群群主或管理员解散房间," + "解散成功!!";
            }else if(dr.reason == 5){ //修改规则解散房间

                str += "牌友群群主或管理员修改玩法解散房间," + "解散成功!!";
            }
        }
        jsclient.showMsg(str, function () {}, function () {}, "1", true);
    }]
    ,Tbz_Rob:[//推饼子抢庄消息返回
        0,function(d){
            jsclient.data.sData.tData = d.tData;
            jsclient.data.sData.uid = d.uid;
    }]
    ,TbzStartBet:[//推饼子开始下注
        0,function(d){
    }]
    ,TbzSendCard:[//推饼子发牌
        0,function(d){
        // var sData = jsclient.data.sData;
        // var tData = sData.tData;
        jsclient.data.sData.tData = d.tData;
        for (var uid in d.players) {
            for (var key in d.players[uid]) {
                jsclient.data.sData.players[uid][key] = d.players[uid][key];
            }
        }
    }]
    ,Tbz_Reconnect:[//重连
        0,function(d){
            jsclient.data.TBZReconnect = true;
    }]
    ,TbzEndOne:[//推饼子结束
        0,function(d){
    }]
    ,Tbz_Bet:[//推饼子下注返回
        0,function(d){
            jsclient.data.sData.tData = d.tData;
            jsclient.data.sData.tbzbetMsg = d.msg; 
    }]
    ,Tbz_stateChange:[//
        0,function(d){
            jsclient.data.sData.tData = d;
    }]
    ,Tbz_roundEnd:[
        0,function(d){
            jsclient.data.sData.tData = d.tData;
            for (var uid in d.players) {
                for (var key in d.players[uid]) {
                    jsclient.data.sData.players[uid][key] = d.players[uid][key];
                }
            }
            // cc.log(JSON.stringify(d));
    }]
    ,Tbz_endRoom:[
        0,function(d){
    }],
    Tbz_RobResults:[
        0,function(d){
            jsclient.data.sData.tData = d;
    }],
    //比赛场消息
    fallout: [0, function (dr) {
        mylog("比赛场 收到 淘汰 的数据");
        Log("app.js netCallback fallout=" + JSON.stringify(dr));
        jsclient.realMatchId = null;

        if(jsclient.isFanJiZhong){
            if(!jsclient.waitFanjiMessage){
                jsclient.waitFanjiMessage = [];
            }
            jsclient.waitFanjiMessage.push(function(){
                jsclient.showMatchEndLayer(dr);
            });
        }else{
            jsclient.waitFanjiMessage = null;
            jsclient.showMatchEndLayer(dr);
        }
        if (jsclient.matchPromotUI) {
            Log("app.js netCallback fallout 2");
            jsclient.matchPromotUI.removeFromParent(true);
            jsclient.matchPromotUI = null;
        }

    }],
    win_match: [0, function (dr) {
        mylog("比赛场 收到 获胜 的数据");
        Log("app.js netCallback win_match=" + JSON.stringify(dr));
        jsclient.realMatchId = null;

        if(jsclient.isFanJiZhong){
            if(!jsclient.waitFanjiMessage){
                jsclient.waitFanjiMessage = [];
            }
            jsclient.waitFanjiMessage.push(function(){
                jsclient.showMatchEndLayer(dr);
            });
        }else{
            jsclient.waitFanjiMessage = null;
            jsclient.showMatchEndLayer(dr);
        }

        if (jsclient.matchPromotUI) {
            jsclient.matchPromotUI.removeFromParent(true);
            jsclient.matchPromotUI = null;
        };

    }],
    match_cancel: [0, function (dr) {
        mylog("比赛场 收到 取消比赛 的数据");
        Log("app.js netCallback match_cancel=" + JSON.stringify(dr));
        
        jsclient.ShowToast("报名人数不足，比赛被取消");
        jsclient.realMatchId = null;

        jsclient.hideMatchDebugInfo();
        if (jsclient.signedMatchData)
            jsclient.signedMatchData = null;
    }],
    kickout: [0, function (dr) {
        mylog("比赛场 收到 踢出比赛 的数据");
        Log("app.js netCallback kickout=" + JSON.stringify(dr));
        
            jsclient.ShowToast("人数不足，为您自动报名下一场比赛", 3);
        
        //收到被踢出消息时自动报名下一场比赛
        if (dr && dr.uid == SelfUid()) {
            var index = -1;
            if (jsclient.gMatchList && jsclient.gMatchList.list) {
                for (var i = 0; i < jsclient.gMatchList.list.length; i++) {
                    var item = jsclient.gMatchList.list[i];
                    if (item._id == dr.matchId) {
                        index = i;
                        break;
                    }
                }
            }
            if (index >= 0) {
                jsclient.RegistMatch(index);
            }
            jsclient.realMatchId = null;
            if (jsclient.signedMatchData)
                jsclient.signedMatchData = null;
        }
        
        jsclient.hideMatchDebugInfo();
    }],
    promote: [0, function (dr) {
        mylog("比赛场 收到 晋级下一轮 的数据");
        Log("app.js netCallback promote=" + JSON.stringify(dr));

        if(jsclient.isFanJiZhong){
            if(!jsclient.waitFanjiMessage){
                jsclient.waitFanjiMessage = [];
            }
            jsclient.waitFanjiMessage.push(function(){
                jsclient.showMatchPromotLayer();
                jsclient.matchPromotUI.refreshUIOnPromot(dr);
            });
        }else{
            jsclient.waitFanjiMessage = null;
            jsclient.showMatchPromotLayer();
            jsclient.matchPromotUI.refreshUIOnPromot(dr);
        }
    }],
    match_force_end: [0, function (dr) {
        
        jsclient.ShowToast("比赛异常结束，请重新报名");
        
        jsclient.hideMatchDebugInfo();
        
        if (dr.uid == SelfUid()) {
            if (jsclient.playui) {
                jsclient.playui.removeFromParent(true);
                delete jsclient.playui;
                jsclient.playui = null;
                delete jsclient.data.sData;
                if (jsclient.ready2Coin) { //add by wcx 20170224
                    jsclient.ready2Coin = null;
                    jsclient.requestCoinCgf();
                }
            }
            
            if (jsclient.matchPromotUI) {
                jsclient.matchPromotUI.removeFromParent(true);
                jsclient.matchPromotUI = null;
            }
            if (jsclient.matchQueueUI) {
                jsclient.matchQueueUI.removeFromParent(true);
                jsclient.matchQueueUI = null;
            }
            if (jsclient.matchEndUI) {
                jsclient.matchEndUI.removeFromParent(true);
                jsclient.matchEndUI = null;
            }

            jsclient.realMatchId = null;

            if (jsclient.signedMatchData)
                jsclient.signedMatchData = null;
        }
    }],
    match_info_update: [0, function (dr) {
        mylog("比赛场 收到 比赛人数更新 的数据");
        Log("app.js netCallback match_info_update=" + JSON.stringify(dr));
    }],
    startGameEvent: [0, function (dr) {
        mylog("收到 游戏开始 event");
        Log("app.js netCallback startGameEvent=" + JSON.stringify(dr));
    }],
    match_rank_update: [0, function (dr) {
        mylog("收到 排名更新 event");
        Log("app.js netCallback match_rank_update=" + JSON.stringify(dr));
        
        //游戏中才弹，游戏外要判断玩家是否报名了该比赛，如果没有则不弹，
        if (jsclient.signedMatchData && jsclient.signedMatchData._id == dr.matchId) {
            Log("app.js netCallback match_rank_update 1");
            if (!jsclient.data.sData || (jsclient.playui && jsclient.playui.isMatchRoundEnd())) {
                Log("app.js netCallback match_rank_update 2");
                //对rank中的信息进行筛选，如果没有自己则不显示排名页面
                var isExistSelf = false;
                var ranks = dr.rank;
                if (ranks) {
                    Log("app.js netCallback match_rank_update 3");
                    for (var key in ranks) {
                        if (key == SelfUid()) {
                            isExistSelf = true;
                            break;
                        }
                    }
                }
                Log("app.js netCallback match_rank_update 4 isExistSelf:" + isExistSelf);
                if (true == isExistSelf) {
                    if (jsclient.playui && jsclient.playui.isMatchRoundEnd() == true) {
                        Log("app.js netCallback match_rank_update 5");
                        Log("app.js showMatchPromotLayer() before 1");
                        jsclient.showMatchPromotLayer();
                        jsclient.matchPromotUI.refreshUIOnUpdate(dr);
                    }
                    else if (jsclient.matchPromotUI){
                        Log("app.js netCallback match_rank_update 6");
                        jsclient.matchPromotUI.refreshUIOnUpdate(dr);
                    }
                    sendEvent("removeChatLayer");
                } else {
                    // jsclient.ShowToast("排名信息异常");
                    Log("app.js netCallback match_rank_update 7");
                }
                // jsclient.showMatchPromotLayer();
                // jsclient.matchPromotUI.refreshUIOnUpdate(dr);
            }
        }
    }],
    wait_table: [0, function (dr) {
        mylog("收到 等待他人结束 event");
        Log("app.js netCallback wait_table=" + JSON.stringify(dr));
        
        if (jsclient.signedMatchData && jsclient.signedMatchData._id == dr.matchId) {
            Log("app.js showMatchPromotLayer() before 2");
            if(jsclient.isFanJiZhong){
                if(!jsclient.waitFanjiMessage){
                    jsclient.waitFanjiMessage = [];
                }
                jsclient.waitFanjiMessage.push(function(){
                    jsclient.showMatchPromotLayer();
                    jsclient.matchPromotUI.refreshUIOnPromot(dr);
                });
            }else{
                jsclient.waitFanjiMessage = null;
                jsclient.showMatchPromotLayer();
                jsclient.matchPromotUI.refreshUIOnPromot(dr);
            }

        }
    }],
    round_timeout: [0, function (dr) {
        jsclient.ShowToast("打牌超时，牌桌解散");
        if (dr.uid == SelfUid()) {
            if (jsclient.playui) {
                jsclient.playui.removeFromParent(true);
                delete jsclient.playui;
                jsclient.playui = null;
                delete jsclient.data.sData;
                if (jsclient.ready2Coin) { //add by wcx 20170224
                    jsclient.ready2Coin = null;
                    jsclient.requestCoinCgf();
                }
            }
            
            if (jsclient.matchPromotUI) {
                jsclient.matchPromotUI.removeFromParent(true);
                jsclient.matchPromotUI = null;
            }

            jsclient.realMatchId = null;

            if (jsclient.signedMatchData)
                jsclient.signedMatchData = null;
        }
    }],

    updateGuildInfo: [0, function (d) {
        var guildId = d.guildid;
        var update = d.update;

        //update 1 代表加入  2代表删除
        if (update == 1) {
            //toastMsg("您已经加入群" + guildId);
            sendEvent("updateGuildInfo");

        } else if (update == 2) {
            toastMsg("您已被移出群" + guildId);
            sendEvent("updateGuildInfo");

            //离开群时，取消对于此群的监听--不在实时推送此群消息
            //jsclient.leavingTimingGuildRequest(guildId);

        }
    }],

    GuildTableInvite: [0, function (d) {
        receiveNewInviteMsg(d);
    }],
    UserNotify: [0, function (d) {

        if (d.time) { //玩家在线游戏时长达到3小时 或者5小时 会收到通知  实时更新客户端玩家游戏时长
            jsclient.data.pinfo.otime = d.time;
            if (jsclient.playtime_tip) {
                jsclient.playtime_tip.removeFromParent(true);
                jsclient.playtime_tip = null;
            }
            var playTime_Tip0 = new playTime_Tip();
            jsclient.Scene.addChild(playTime_Tip0);
        }

        var type = d.type;
        if (type == "GuildApply") {
            var guildId = d.guildid;
            addNewApplyGuildState(guildId);
        }
        else if (type == "QuitGuild") {
            addNewQuitGuildState();
            //复用删除群成员的消息用来刷新群成员界面
            sendRefreshGuildEvent(refreshGuildType.DelMember, {
                guildId: d.guildid,
                memberUid: d.uid
            });
        }
        else if (type == "GuildAddApply") {
            //邀请加入牌友群
            receiveNewInviteGuild(d);
        }
        else if (type == "CooperGuildReq") {
            //有申请与我合作群的消息
            addNewReqGuildState(d.guildid);
        }
        else if (type == "UserRelationNotify") {
            //有成员未分配
            addNewUserRelationState(d.guildid);
        }
        else if(type == "ForceLogout") {
            cc.sys.localStorage.removeItem("WX_USER_LOGIN");
            cc.sys.localStorage.removeItem("XL_USER_LOGIN");
            cc.sys.localStorage.removeItem("LB_USER_LOGIN");
            cc.sys.localStorage.removeItem("DL_USER_LOGIN");
            cc.sys.localStorage.removeItem("LoginType");
            cc.sys.localStorage.removeItem("loginData");
            cc.sys.localStorage.removeItem("PHONE_USER_LOGIN");
            logoutResetGuildData();
            sendEvent("logout");
        }
    }],

    /** 象棋逻辑 xiangqi **/
    myTurn: [0, function (d) {
        if (jsclient.gameid == "xiangqi") {
            jsclient.myTurnByXQ(d);
        }
    }],
    QiuHeToPlayer: [0, function (d) {//对面要求和
        if (jsclient.gameid == "xiangqi") {
            console.log(d.uid + " 对面要求和");
        }
    }],
    HuiQiToPlayer: [0, function (d) {
        if (jsclient.gameid == "xiangqi") {
            console.log(d.uid + " 要求悔棋");
        }
    }],
    refuseQiuHe: [0, function (d) {//求和拒绝
        if (jsclient.gameid == "xiangqi") {
            console.log(d.uid + " 拒绝求和");
        }
    }],
    refuseHuiQi: [0, function () {
        if (jsclient.gameid == "xiangqi") {
            console.log(" refuseHuiQi");
        }
    }],

    //实时牌友群

    // 推送单个桌子变动 (没有就加，有就更新)
    OnGuildTableUpdate:[0, function (d) {
        //console.log("=======// 推送单个桌子变动 (没有就加，有就更新)====" +JSON.stringify(d));
        sendEvent("GuildTableUpdate",d);
    }],
    //// 推送删除单个桌子
    OnGuildTableDelete:[0, function (d) {
        //console.log("===========// 推送删除单个桌子" +JSON.stringify(d));
        sendEvent("GuildTableDelete",d);
    }],
    // 推送删除多张桌子 --- 此接口已废弃
    OnGuildTableMultiDelete:[0, function (d) {
        //console.log("=======// 推送删除多张桌子====" +JSON.stringify(d));
        sendEvent("GuildTableMultiDelete",d);
    }],
    guAPassHint:[0, function (d) {}],
    //人未满时，快速开局
    ApplyStartGame: [0, function (dr) {
        let sData = jsclient.data.sData
        sData.tData = dr.tData
        let tongYiNum = 0
        let names = "经玩家"
        for (let uid in dr.players) {
            let pl = dr.players[uid]
            sData.players[uid].agreeStart = pl.agreeStart
            if (pl.agreeStart > 0) {
                tongYiNum++
            }
        }

        for (let i = 0; i < dr.yesuid.length; i++) {
            let uid = dr.yesuid[i]
            let pl = jsclient.data.sData.players[dr.tData.uids[uid] + ""]
            if (typeof pl == "undefined") {
                pl = jsclient.data.sData.players[uid]
            }
            names += "【" + getfixedLenStr(unescape(pl.info.nickname || pl.info.name), 8) + "】 "
        }
        names += "同意，房间切换成功"

        if (dr.nouid.length >= 1) {
            if(jsclient.applyStartGameLayer ) {
                jsclient.applyStartGameLayer.removeFromParent(true)
                delete jsclient.applyStartGameLayer
                jsclient.applyStartGameLayer = null
            }
            let nameStr = ""
            for (let j = 0; j < dr.nouid.length; ++j) {
                let pl = sData.players[dr.nouid[j]]
                nameStr += "【" + getfixedLenStr(unescape(pl.info.nickname || pl.info.name), 8) + "】 "
            }
            jsclient.showMsg("玩家 " + nameStr + " 拒绝开局")

        } else {
            if(jsclient.applyStartGameLayer == null && !dr.start) {
                console.log("申请开局的窗口已经打开了！！")
                jsclient.Scene.addChild(new ApplyStartGameLayer(),10000000)
            } else {
                if (tongYiNum >= jsclient.getRealPlayerCount() || dr.start) {
                    jsclient.Scene.scheduleOnce(function () {
                        jsclient.showMsg(names)
                    }, 0.01)
                    if(jsclient.applyStartGameLayer ) {
                        jsclient.applyStartGameLayer.removeFromParent(true)
                        delete jsclient.applyStartGameLayer
                        jsclient.applyStartGameLayer = null
                    }
                }
            }
        }
    }],
    quickStartGame:[0, function (d) {
        let sData = jsclient.data.sData
        sData.tData = d.tData
    }],
}

function isFlower(card) {
    if(card == 111||card == 121||card == 131||card == 141||card == 151||card == 161||card == 171||card == 181)
    {
        return true;
    }
    return false;
}

function filterEmoji(source) {
    var strArr = source.split(""),
        result = "",
        totalLen = 0;

    for (var idx = 0; idx < strArr.length; idx++) {
        // 超出长度,退出程序
        if (totalLen >= 16) break;
        var val = strArr[idx];
        // 英文,增加长度1
        if (/[a-zA-Z]/.test(val)) {
            totalLen = 1 + (+totalLen);
            result += val;
        }
        // 中文,增加长度2
        else if (/[\u4e00-\u9fa5]/.test(val)) {
            totalLen = 2 + (+totalLen);
            result += val;
        }
        // 遇到代理字符,将其转换为 "口", 不增加长度
        else if (/[\ud800-\udfff]/.test(val)) {
            // 代理对长度为2,
            if (/[\ud800-\udfff]/.test(strArr[idx + 1])) {
                // 跳过下一个
                idx++;
            }
            // 将代理对替换为 "口"
            result += "口";
        }
    };
    return result;
}

function GetUidNames(uids) {
    var sData = jsclient.data.sData;
    var rtn = [];
    for (var i = 0; i < uids.length; i++) {
        var pl = sData.players[uids[i]];
        if (pl) rtn.push(unescape(pl.info.nickname || pl.info.name));
    }
    return getfixedLenStr((rtn + ""),8, "...");//截取8个字符
}

function getServersByRandForWeights(servers) {
    var serversSelect = jsclient.remoteCfg.serversSelect;
    if (serversSelect && serversSelect.length == servers.length) {
        var rand = Math.round(Math.random() * 1000);
        var sum = 0;
        for (var i = 0; i < serversSelect.length; i++) {
            sum += serversSelect[i];
            if (rand <= sum) {
                return i;
            }
        }
        return 0;
    }
    else {
        return Math.floor(Math.random() * servers.length);
    }
}

function getServerByRandForPort(parts) {
    if (parts.length > 3) {
        return parseInt(parts[1 + Math.floor(Math.random() * (parts.length - 1))]);
    }
    else {
        var min = parseInt(parts[1]);
        var max = parseInt(parts[2]);
        var offset = max - min + 1;
        var part = Math.floor(Math.random() * offset) + min;
        return parseInt(part);
    }
}

function getRoomOwner(sData) {
	var owner = null;
    if (sData && sData.tData) {
        owner = sData.tData.owner;
        if(!owner && sData.tData.uids && sData.tData.uids.length) {
            owner = sData.tData.uids[0];
        }
    }
    return owner;
}

function getYearMonthDayString(toDay,hasHour,hasMinutes,hasSecond) {
    var toDay = toDay || new Date();
    var year = toDay.getFullYear()+"";
    var month = toDay.getMonth()+1;
    month = month + "";
    var day = toDay.getDate()+"";
    var hour = toDay.getHours() +"";
    var minutes = toDay.getMinutes() + "";
    var second = toDay.getSeconds() + "";
    var toDayString = year + (month<10?("0"+month):month) + (day<10?("0"+day):day);
    if(hasHour){
        toDayString = toDayString + hour;
        if(hasMinutes){
            toDayString = toDayString + minutes;
            if(hasSecond){
                toDayString = toDayString + second;
            }
        }
    }
    return toDayString;
}


/** 拼接艺术字
 * @para  node 父节点
 * @para  number 需要转换的数字
 * @para  width 艺术字资源的宽
 * @para  offx 中心位置偏移
 * */
function setAtlasText(node, number, width, offx) {
    if (!node) {
        return;
    }

    var centerX = node.width / 2;
    var centerY = node.height / 2;

    if (offx) {
        centerX += offx;
    }

    number = number.toString();

    var baseUrl = "res/aress/nums/endNumber/";
    var addImgUrl = baseUrl + "numImg_add.png";
    var subImgUrl = baseUrl + "numImg_sub.png";
    var addNumUrl = "numImg_add_";
    var subNumUrl = "numImg_sub_";

    //加减号的纹理
    var getSymbolImg = function (tag) {
        var tmpImg = new ccui.ImageView();
        var imgUrl = tag > 0 ? addImgUrl : subImgUrl;
        tmpImg.loadTexture(imgUrl);
        tmpImg.setAnchorPoint(0.5, 0.5);
        return tmpImg;
    };

    //数字的纹理
    var getNumImg = function (num,tag) {
        if (num < 0 || num > 9 || typeof (num) == "undefined") {
            return 0;
        };

        var tmpImg = new ccui.ImageView();
        var numUrl = tag > 0 ? (baseUrl + addNumUrl + num + ".png") : (baseUrl + subNumUrl + num + ".png");
        tmpImg.loadTexture(numUrl);
        tmpImg.setAnchorPoint(0.5, 0.5);
        return tmpImg;
    };


    var startx;//数字图片开始位置

    if (parseInt(number) == 0) {
        startx = centerX;
        var numimg = getNumImg(0,-1);
        numimg.setPosition(startx, centerY);
        node.addChild(numimg);
    }
    else {
        startx = centerX - number.length / 2 * width * 0.5;
        var symImg = getSymbolImg(number);
        symImg.setPosition(startx, centerY);
        node.addChild(symImg);

        var tmpTag = number;

        if(tmpTag < 0){
            number = number.substring(1);
        }

        for (var nIndex = 0; nIndex < number.length; nIndex++) {
            var numstr = number.substring(nIndex, nIndex + 1);
            var num = parseInt(numstr);

            var tmpNumImg = getNumImg(num,tmpTag);
            tmpNumImg.setAnchorPoint(0.5, 0.5);
            tmpNumImg.setPosition((nIndex + 1) * width * 0.6 + startx, centerY);
            node.addChild(tmpNumImg);
        }
    }
}

function genGrowthSign(objInput, uri) {
    var arrInput = Object.keys(objInput);
    arrInput.sort();
    var strInput = "";
    for (var i = 0; i< arrInput.length; i++) {
        strInput += '&' + arrInput[i] + "=" + objInput[arrInput[i]];
    }
    strInput = strInput.replace(/(\&*$)/g,"").replace(/(^&*)/g,"");
    strInput = uri + "&" + strInput;
    strInput = encodeURIComponent(strInput);

    var access_key = "LsFWib3HLx0Ry71qMGne";
    var genSign = hex_hmac_sha1(access_key,strInput);
    return genSign;
}


//新增后台获取分享两节URL
function getDownload() {
    var gameid = "xynmmj";//（对应jsclient.remoteCfg.wxShareUrl里边的proName）
    var para = {
        "appid": gameid,
        "access_id": "download20181121"
    }
    var url = "https://download.jxlwgame.com/api/getDownloadUrl";
    var signs = genGrowthSign(para, "/api/getDownloadUrl");
    var data = {"appid": gameid, "sign":signs};
    var xhr = cc.loader.getXMLHttpRequest();
    cc.log("=============data:"+signs);
    xhr.open("POST", url);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if(xhr.responseText){
            var msg = JSON.parse(xhr.responseText).msg;
            if(msg && msg.downloadurl){
                jsclient.remoteCfg.wxShareUrl = msg.downloadurl;
            }
        }
        cc.log("===============jsclient.remoteCfg.wxShareUrl:"+jsclient.remoteCfg.wxShareUrl);
    }
    xhr.onerror = function (event) {
        cc.log("获取失败");
    };
    xhr.send(JSON.stringify(data));
}

jsclient.NetMsgQueue = [];
var JSScene = cc.Scene.extend({
    jsBind: {
        _event: {
            removeWKWebView:function()
            {
                //    赖子oc 的调用
                playMusic('bgMain');
            },
            showExitGamePanel:function (msg) {
                if(isValid(jsclient.h5InnerWebView) && cc.sys.isNative)
                {
                    // jsclient.h5InnerWebView.goBack();
                    jsclient.h5InnerWebView.cleanup();

                    // jsclient.h5InnerWebView.onExit();
                    // jsclient.h5InnerWebView.destroy();
                    jsclient.h5InnerWebView.removeFromParent(true);
                    jsclient.h5InnerWebView = null;
                    // cc.audioEngine.pauseMusic();
                    // cc.audioEngine.resumeMusic();
                    playMusic("bgMain");
                    return;
                }

                //显示调用退出游戏弹框
                jsclient.showMsg("亲,真的不玩了！要残忍退出吗？",function()
                {
                    jsclient.native.customExitGame();
                },function(){
                    return;
                },2)
            },
            phoneList:function(data)
            {
                phoneListReturnData(data);
            },
            hongbao:function (eD) {
                mylog("add hongbao view");
                if(eD){
                    mylog("hongbao eD = " + eD);
                }
                if(jsclient._wHongBaoLayer){
                    jsclient._wHongBaoLayer.removeFromParent(true);
                    delete jsclient._wHongBaoLayer;
                }
                mylog("开始打开红包界面");
                var layer = new wHongBaoOpenLayer(eD);
                jsclient.Scene.addChild(layer,100);
            },
            WX_SHARE_RESULT:function(para){

                //微信分享成功
                if(((para && para.errCode == 0 ) || cc.sys.OS_IOS == cc.sys.os )&& jsclient.wxShareByReword){
                    jsclient.gamenet.request("pkplayer.handler.GetAward", {type:"share1"}, function(res){
                        jsclient.native.ShowLogOnJava("pkplayer.handler.GetAward res = " + JSON.stringify(res));
                        if(res && res.result == 0){
                            jsclient.showMsg("分享成功获得金豆奖励",function () {
                            },function () {
                            },"1");
                        }
                    });
                }

                //TODO 新转盘统计分享
                jsclient.native.ShowLogOnJava("分享回调： jsclient.wxShareByTurnTable = "+jsclient.wxShareByTurnTable+" para: "+JSON.stringify(para));

                if(((para && para.errCode == 0 ) || cc.sys.OS_IOS == cc.sys.os )&& jsclient.wxShareByTurnTable){
                    var action = "bindGrowth";
                    var method = "user/add/share";
                    var data = {prefix:"xynmmj",start:0,limit:100};
                    var dataStr = JSON.stringify(data);
                    var para =  {"action":action,"method":method,"data":dataStr};

                    jsclient.gamenet.request("pkcon.handler.ProxyAccess", para, function (rtn) {
                        if (rtn.result == 0) {
                            jsclient.native.ShowLogOnJava("分享成功");

                            jsclient.showMsg("分享成功",function () {
                            },function () {
                            },"1");
                        }else{
                            jsclient.native.ShowLogOnJava("分享失败");
                        }
                    });
                }

                //传奇分享成功后，弹出提示框，可以复制礼包码
                if(jsclient.shareInChuanQi && !jsclient.TipsLayer){
                    jsclient.Scene.addChild(_getTipsLayer());
                }


                jsclient.shareInChuanQi = false;
                jsclient.wxShareByReword = false;
                jsclient.wxShareByTurnTable = false;

            },
            WX_SHARE_SUCCESS: function (para) {    //微信分享回调
                if(typeof para == "string"){
                    para = JSON.parse(para);
                }

                if(para && para.errCode == 0)
                {
                    // if(curShareType == shareType.fudaiShare)
                    // {
                        jsclient.AddShareTimes();
                    // }
                    // if(curShareType == shareType.musicdayShare)
                    // {
                    //     sendEvent("WX_SHARE_SUCCESS_MDay")
                    // }
                }


                // jsclient.ShowToast("分享的回调！！！！！！！！！")
            },
            cfgUpdate:function (changeValue) {

                if (jsclient.webView) {
                    jsclient.webView.removeFromParent(true);
                    jsclient.webView=null;
                };
            },
            openWeb: function (para) {
                jsclient.uiPara = para;
                this.addChild(new WebViewHelpLayer());
            },
            openWeb_readMe: function (para) {
                jsclient.uiPara = para;
                this.addChild(new WebViewLayer());
            }
            , popUpMsg: function (pmsg) {
                this.addChild(NewPopMsgLayer(pmsg));
            }
            , updateFinish: function () {

                // if (cc.sys.OS_IOS == cc.sys.os) {
                    if (jsclient.remoteCfg.coinmount) {
                        jsclient.coinarry = jsclient.remoteCfg.coinmount.split(',');
                        jsclient.moneyarray = jsclient.remoteCfg.moneymount.split(',');
                        jsclient.iaparray = jsclient.remoteCfg.iaparry.split(',');
                    }
                // }

                if (!jsclient.gamenet)jsclient.gamenet = new GameNet();
                priorityConnectServer();
                
               //  var servers = jsclient.remoteCfg.servers.split(',');
               //  var server = servers[getServersByRandForWeights(servers)];
               //  var parts = server.split(':');
               //  var host = parts[0];
               //  if(jsclient.remoteIpHost) host=jsclient.remoteIpHost;

               // // host = 15022;

               //  var port = getServerByRandForPort(parts);
               //  jsclient.gamenet.disconnect();
               //  mylog("ip:11111111111111111111111");
               //  mylog("ip:"+host);
               //  mylog("port:"+port);
               //  jsclient.gamenet.connect(host, port, function () {
               //      sendEvent("connect");
               //  }, function () {
               //      sendEvent("disconnect", 1);
               //      mylog("ip:22222222222222222222");
               //  });
            },
            connect: function () {
                  jsclient.unblock();
                  jsclient.setDisconnectCode(false);

                // jsclient.game_on_show = false;
                  
                if (!jsclient.homeui) {
                    mylog("loginui");
                    var isCanAutoLogIn = false ;


                    // if(isCanAutoLogIn) {//游戏中心态--不显示 登陆界面--适配游戏中心
                    //     jsclient.autoLogin();
                    // }else
                    {
                        this.addChild(new LoginLayer());
                        jsclient.unblock();
                    }
                }
                else {
                    mylog("auto login");
                    jsclient.autoLogin();
                }
                //连接上把重连提醒ui关闭
                if (jsclient.reconnectPopUI) {
                    jsclient.reconnectPopUI.removeFromParent(true);
                    jsclient.reconnectPopUI = null;
                }
            },
            game_on_hide: function () {
                jsclient.game_on_show = false;
            },
            game_on_show: function () {
                jsclient.game_on_show = true;
                if(jsclient.homeui/* && !jsclient.loginui && !jsclient.playui*/){
                    if (cc.sys.OS_ANDROID == cc.sys.os) {
                        jsclient.autoJoinGame();
                    }
                    else if (cc.sys.OS_IOS == cc.sys.os) {
                        this.scheduleOnce(function(){jsclient.autoJoinGame();}, 0.2);
                    }

                }


                //H5传奇游戏接入
                if(jsclient.h5gameui){
                    setTimeout(function () {
                        cc.audioEngine.pauseMusic();
                        cc.audioEngine.pauseAllEffects();
                    },0.1);
                }

            },

            guildAct_loginGame:function (rtn) {
                if(rtn && rtn.result == 0 && rtn.errno == 0){
                    cc.sys.localStorage.setItem(guildActLoginTaskKey, 1);
                }
            },
            // disconnect: function (code) {

                
            //     // //阿里盾IP有值但是无效的处理
            //     // if(code == 1 && jsclient.remoteIpHost && cc.sys.isMobile)
            //     // {
            //     //     jsclient.remoteIpHost=null;
            //     //     jsclient.Scene.runAction(cc.sequence(cc.delayTime(0.1),cc.callFunc(
            //     //         function(){
            //     //             jsclient.AskForAliDunIp();
            //     //         }
            //     //     )));
            //     //     return;
            //     // }
            //     if(typeof ppgamecenter != "undefined"){//踢人----适配游戏中心
            //             try {
            //                 jsclient.showMsg("网络连接断开!", function() {
            //                     ppgamecenter.changeGameByUser("PPGame");
            //                 })

            //                 return;
            //             } catch (error) {

            //         }
            //     }
            //     if (!jsclient.remoteCfg || (cc.sys.os !=  cc.sys.OS_WINDOWS) && ( code != 6 || jsclient.game_on_show))
            //     {
            //         jsclient.unblock();
            //         jsclient.showMsg("网络连接断开(" + code + ")，请检查网络设置，重新连接", function ()
            //         {
            //             jsclient.restartGame();
            //         })
            //     }
            //     else
            //     {
            //         jsclient.block();
            //         jsclient.game_on_show = true;
            //         mylog("reconnect");
            //         if(isAllPriorityServerListNull())
            //         {
            //             jsclient.unblock();
            //             jsclient.showMsg("连接服务器出错,请重新连接", function ()
            //             {
            //                 jsclient.restartGame();
            //             })
            //         }
            //         else
            //         {
            //             jsclient.Scene.runAction(cc.sequence(cc.delayTime(0.1), cc.callFunc(
            //                 function ()
            //                 {
            //                     sendEvent("updateFinish");
            //                 }
            //             )));
            //         }
            //     }
            // },
            disconnect: function (code) {

                jsclient.native.ShowLogOnJava("---- disconnect code = " + code);


                // 如果是被踢的，不要自动断线重连
                if (jsclient.iskicked) {
                    return;
                }
                /*
                1 所有服务器都重试完无法连接
                2 发送请求异常
                3/4 接收服务器数据异常
                5 获取配置文件失败
                6 网络断开
                7 3s后网络连接重试失败
                10+ 热更新异常
                ["ERROR_NO_LOCAL_MANIFEST,", "ERROR_DOWNLOAD_MANIFEST", "ERROR_PARSE_MANIFEST",
                "NEW_VERSION_FOUND", "ALREADY_UP_TO_DATE",
                "UPDATE_PROGRESSION", "ASSET_UPDATED", "ERROR_UPDATING", "UPDATE_FINISHED", "UPDATE_FAILED",
                "ERROR_DECOMPRESS"]
                */
                if (code == 1 || code == 6 || code == 7) {

                    //网络断开,直接重连，失败后发送code=1，尝试3s后重新连接
                    if (code == 6) {
                        jsclient.block();
                        jsclient.Scene.runAction(cc.sequence(cc.delayTime(0.1), cc.callFunc(
                            function () {
                                priorityConnectServer();
                            }
                        )));
                    }

                    //所有服务器立即重试无法连接，等3s重试，还失败发送code=7
                    if (code == 1) {
                        jsclient.Scene.runAction(cc.sequence(cc.delayTime(3), cc.callFunc(
                            function () {
                                jsclient.setDisconnectCode(true);
                                jsclient.reconnectServer();
                            }
                        )));
                    }

                    //3s后网络连接重试失败，弹框等待玩家点击确定重连
                    if (code == 7) {
                        if (!jsclient.reconnectPopUI) {
                            var reconnectPara = {
                                msg: "连接服务器出错,请检查网络设置,点击确定重试",
                                yes: function () {
                                    jsclient.block();
                                    jsclient.reconnectServer();
                                    //popmsg里有remove，这里不需要remove，直接重置
                                    jsclient.reconnectPopUI = null;
                                }
                            };
                            jsclient.unblock();
                            jsclient.reconnectPopUI = NewPopMsgLayer(reconnectPara);
                            jsclient.Scene.addChild(jsclient.reconnectPopUI);
                        }
                    }
                }
                //else if(code >= 10)
                //{
                //    jsclient.unblock();
                //    jsclient.showMsg("网络连接断开(" + GetErrorCode(code) + ")，请检查网络设置，重新连接", function ()
                //    {
                //        //ERROR_DOWNLOAD_MANIFEST 下载manifest出错的时候容错，让玩家能进到登录页面
                //        if(code == 11)
                //        {
                //            jsclient.GetRemoteCfg();
                //        }
                //        else
                //        {
                //            jsclient.restartGame();
                //        }
                //    });
                //}
                else {
                    jsclient.unblock();
                    jsclient.showMsg("网络连接断开(" + GetErrorCode(code) + ")，请检查网络设置，重新连接", function () {
                        jsclient.restartGame();
                    });
                }
            },
            loginOK: function (rtn) {
                jsclient.saveLocalLastLoginType()
                jsclient.data = rtn;
                jsclient.data.pinfo.accessToken=rtn.accessToken;
                jsclient.NetMsgQueue = [];

                jsclient.Scene.stopAllActions();

                //手动开启自动更新 configuration.json  更新跑马灯，更新公告等信息
                jsclient.startUpdateCfgFunc(true);

                jsclient.native.ShowLogOnJava("----- loginOK"+ JSON.stringify(jsclient.data));

                if(jsclient.data.pinfo.uid)
                {
                    sys.localStorage.setItem("uid", "xynmmj_"+jsclient.data.pinfo.uid);
                    if(jsclient.hasTalkingData){
                        jsclient.talkingDataSetAccountJS('uid_' + jsclient.data.pinfo.uid);

                    }
                }
                if(jsclient.data.pinfo.playNum)
                {
                    jsclient.native.ShowLogOnJava("-- playNum="+jsclient.data.pinfo.playNum);
                    sys.localStorage.setItem("playNum", jsclient.data.pinfo.playNum);
                }
                else
                {
                    sys.localStorage.setItem("playNum", 0);
                }

                if(!sys.localStorage.getItem("guild_hall_bg")) {
                    sys.localStorage.setItem("guild_hall_bg",jsclient.curHallBG);
                }
                if(!sys.localStorage.getItem("guild_hall_tableBG")) {
                    sys.localStorage.setItem("guild_hall_tableBG",jsclient.curTAbleBg);
                }

                for (var netEvt in jsclient.netCallBack) {
                    jsclient.gamenet.QueueNetMsgCallback(netEvt);
                }
                jsclient.gamenet.SetCallBack("disconnect", function () {
                    sendEvent("disconnect", 6);
                });
                if (!jsclient.homeui) {
                    jsclient.native.ShowLogOnJava("----- loginOK1");
                    this.addChild(new HomeLayer());
                    jsclient.native.ShowLogOnJava("----- loginOK1-1");
                    if(!jsclient.createui){
                        this.addChild(new CreateLayer());
                    }
                    jsclient.createui.visible = false;
                    //JGPushPlugin.getInstance().setName("xynmmj" + SelfUid());
                    //JGPushPlugin.getInstance().setGroups("xynmmj" );

                    // getNoticeSrcFromRemote();
                    jsclient.native.ShowLogOnJava("----- loginOK2");
                    if (cc.sys.os != cc.sys.OS_WINDOWS) {   //Windows端屏蔽公告弹窗的显示
                        checkGameNotice();
                    }
                    jsclient.native.ShowLogOnJava("----- loginOK3");
                }
                if (rtn.vipTable > 0) {
                    jsclient.joinGame(rtn.vipTable,rtn.ingame.gameid);
                }
                else if(!jsclient.autoJoinGame()){
                    sendEvent("LeaveGame");
                    jsclient.native.xlGetInviteGameInfo();
                }

                if(rtn.realMatchId && rtn.realMatchId > 0){
                    jsclient.realMatchId = rtn.realMatchId;
                    //比赛场未开始（自己已报名）,请求GetMatchRecover
                    jsclient.GetMatchRecover();
                    mylog("调用recover接口");
                    //自己报名比赛未开始，显示等待开始页面
                    // jsclient.showMatchWaitStartLayer();
                    jsclient.showMatchDebugInfo({rID:rtn.realMatchId});
                }
                jsclient.native.ShowLogOnJava("----- loginOK4");
                 jsclient.native.initGaoDeMap();
                jsclient.native.ShowLogOnJava("----- loginOK5");
                //  if (NOTICE_TEXTURES.length > 0 && !jsclient.noticeui)
                //  {
                //     jsclient.Scene.addChild(new NoticeLayer(),999);
                //  }
                checkRedDot();
                checkIs7Day();

                initMatchV2();

                jsclient.native.ShowLogOnJava("----- loginOK6");
                //定时比赛场恢复比赛场景
                if("undefined" != typeof rtn.realMatchIdV2 && rtn.realMatchIdV2){
                    jsclient.GetMatchRecoverV2();
                }

                //邮件系统获取邮件列表
                loginGetEmailBoxList();

                //获取后台游戏下载链接配置
                getDownload();
                jsclient.native.ShowLogOnJava("----- loginOK7");
                var noticeData = cc.sys.localStorage.getItem(guildActLoginTaskKey);
                if(!noticeData) {
                    guildAct.actNet.loginGame();	//牌友群转盘活动登录回调
                }

                //临时弹窗 && jsclient.remoteCfg.openTips
                var rangeDataForCQ = [2019082910,2019091210];
                var cQTimeStr = "";
                var timeData = jsclient.data.time;
                var canopenTips = false;
                if(timeData){
                    var timeDataStr = timeData + "";
                    if(timeDataStr.length == 13)
                        cQTimeStr = getYearMonthDayString(new Date(timeData),true)
                    else if(timeDataStr.length == 10)
                        cQTimeStr = getYearMonthDayString(new Date(timeData*1000),true)
                }
                else
                    cQTimeStr = getYearMonthDayString(null,true);

                if((cQTimeStr>=rangeDataForCQ[0] && cQTimeStr<rangeDataForCQ[1])){
                    canopenTips =true
                }
                // if(jsclient.remoteCfg && canopenTips && jsclient.Scene && !jsclient.TmpTipsLayer){
                //     if(canShowTips()){
                //         setShowTime();
                var rexuebayeTimes = cc.sys.localStorage.getItem("rexuebayeTimes") || 0
                if (canopenTips && jsclient.Scene && !jsclient.TmpTipsLayer && rexuebayeTimes < 3 && onceOpenByDay() ) {
                    rexuebayeTimes++
                    cc.sys.localStorage.setItem("rexuebayeTimes", rexuebayeTimes)
                    jsclient.Scene.addChild(_getTmpTipsLayer(),999);
                }
                jsclient.native.ShowLogOnJava("----- loginOK8");
                //解锁头像框小红点提醒专用
                jsclient.headframe_remind = cc.sys.localStorage.getItem("headframe_remind");



                if(jsclient.hasGetApps){//版本控制
                    jsclient.Scene.runAction(cc.sequence(cc.delayTime(5.0), cc.callFunc(function(){
                        setTimeToLoadApps("xynmmj");//暂时屏蔽此功能
                    }))); //获取手机其他App 和通讯录
                }

                if(jsclient.hasBaiDuLoc)jsclient.startLocation();

            },
            logout: function () {
                this.addChild(new LoginLayer());
            },
            XL_InviteGameInfo: function (para) {
                mylog("JS-XL_InviteGameInfo");
                jsclient.doInviteGameInfo(para);
            },
            createRoom: function () {
                if(jsclient.createui){
                    jsclient.createui.setVisible(true); 
                }else{
                    this.addChild(new CreateLayer());
                }
            },
            joinRoom: function () {
                this.addChild(new EnterLayer());
            },
            bindPhoneNumber: function () {
                this.addChild(new PhoneBindLayer());
            },
            replacePhoneNumber: function () {
                this.addChild(new PhoneReplaceLayer());
            },
            bindIdCardNumber: function () {
                this.addChild(new IdCardBindLayer());
            },
            initSceneData: function (data) {
                jsclient.unblock();
            },
            QueueNetMsg: function (ed) {
                var oldLen = jsclient.NetMsgQueue.length;
                if (ed[0] == "mjhand" && ed.length == 2) {
                    jsclient.NetMsgQueue.push(["moveHead", {}]);
                }
                jsclient.NetMsgQueue.push(ed);
                if (oldLen == 0)    this.startQueueNetMsg();
            },
            UpdateCfgSuc: function(){	//每2分钟请求一次定位信息 百度地图新添加
                console.log("地图显示-111")
                if(jsclient.hasBaiDuLoc) {
                    console.log("地图显示-112")
                    jsclient.startLocation();
                    this.runAction(cc.repeatForever(cc.sequence(cc.delayTime(60 * 2),//60 * 2
                        cc.callFunc(function () {
                            console.log("地图显示-114")
                            jsclient.startLocation();
                        }))));
                }
            }
        },
        _keyboard: {
            onKeyPressed: function (key, event) {
            },
            onKeyReleased: function (key, event) {
                if (key == 82) //R键
                {
                    jsclient.restartGame();
                    /*jsclient.showMsg("亲,真的不玩了！要残忍退出吗？",function()
                    {
                        jsclient.native.customExitGame();
                    },function(){
                        return;
                    },2)*/
                }
                if (key == 73 && jsclient.homeui) {
                    jsclient.changeIdLayer();
                }
                if (key == 67 && jsclient.homeui) { // C键

                    var effectName = "boom";
                    //boom
                    //liandui
                    //shunzi
                    var exportJsonPath = "res/pdk/Effects/" + effectName + "/" + effectName + ".ExportJson";
                    var plistPath = "res/pdk/Effects/" + effectName + "/" + effectName + "0.plist";
                    var pngPath = "res/pdk/Effects/" + effectName + "/" + effectName + "0.png";
                    ccs.armatureDataManager.addArmatureFileInfo(pngPath, plistPath, exportJsonPath);//png,plist,json
                    var armature = ccs.Armature.create(effectName);//从动画数据管理器里面创建名叫Cowboy的动画对象

                    // if (isLoop) {
                        armature.getAnimation().play(effectName, -1, 0);
                   /* }
                    else {
                        armature.getAnimation().setMovementEventCallFunc(function () {
                            armature.removeFromParent();
                        });
                        armature.getAnimation().play(effectName, -1, 0);
                    }*/

                    // var size = cc.winSize;
                    // armature.setScale(1.0);
                    var scale = jsclient.size.width / 1280;
                    armature.scale = scale;
                    armature.x = jsclient.size.width*0.5;
                    armature.y = jsclient.size.height*0.5;
                    jsclient.Scene.addChild(armature);

                    // jsclient.exportDataLayer();
                }
                if (key == 79) {
                    // this.addChild(new LoginLayer());
                }

                // cc.log("Key with keycode %d released", key);
                // mylog("Key : " + key);
            }
        }
    },
    startQueueNetMsg: function () {
        var sce = this;
        if (jsclient.NetMsgQueue.length > 0) {
            var ed = jsclient.NetMsgQueue[0];
            var dh = jsclient.netCallBack[ed[0]];
            var handleData = dh[1](ed[1]);
            sce.runAction(cc.sequence(
                cc.delayTime(0.0001),
                cc.callFunc(function () {
                    if (handleData != -1) sendEvent(ed[0], ed[1]);
                    
                }),
                cc.delayTime(dh[0]),
                cc.callFunc(function () {
                    jsclient.NetMsgQueue.splice(0, 1);
                    if (jsclient.NetMsgQueue.length > 0) sce.startQueueNetMsg();
                })));
        }
    }
    , onEnter: function () {
        this._super();
        setEffectsVolume(-1);
        setMusicVolume(-1);
        ConnectUI2Logic(this, this.jsBind);
        this.addChild(new BlockLayer());

        //Android 7.0 之后部分手机加载微信头像失败，采用本地缓存
        cc.loader.loadImg = myLoadImg;

        //游戏大厅加入
        if(typeof ppgamecenter != 'undefined'){
            if (cc.sys.OS_IOS === cc.sys.os) {
                jsb.reflection.callStaticMethod('AppController', 'setLoginIdx:', 4);
                jsb.reflection.callStaticMethod('AppController', 'registerLogin');
            }
        }

        try{
            if(typeof ppgamecenter != "undefined"){//不显示闪屏, 适配游戏中心
                this.addChild(new UpdateLayer());
            }else {
                this.addChild(new LogoLayer());
            }
        }catch (error){

        }

    }
});


jsclient.native = 
{
    wxShareHBImg: function () {
        try {
            var writePath = jsb.fileUtils.getWritablePath();
            var textrueName = "wxhb.png";
            var showType = "0";
            if (jsb.fileUtils.isFileExist(jsb.fileUtils.getWritablePath() + "wxshare.txt")) {
                mylog("111");
                if (cc.sys.OS_ANDROID == cc.sys.os) {
                    var aaa = jsb.reflection.callStaticMethod(
                        "org.cocos2dx.javascript.AppActivity",
                        "StartShareTextureWxSceneSession",
                        "(Ljava/lang/String;Ljava/lang/String;)V",
                        writePath + textrueName,
                        showType
                    );
                    mylog("aaa:" + JSON.stringify(a));
                }
                else if (cc.sys.OS_IOS == cc.sys.os) {
                    jsb.reflection.callStaticMethod(
                        "AppController",
                        "wxShareTexture:AndType:",
                        writePath + textrueName,
                        showType
                    );
                }
            }
            else {
                mylog("222");
                if (cc.sys.OS_ANDROID == cc.sys.os) {
                    jsb.reflection.callStaticMethod(
                        "org.cocos2dx.javascript.AppActivity",
                        "StartShareTextureWxSceneSession",
                        "(Ljava/lang/String;)V",
                        writePath + textrueName
                    );
                }
                else if (cc.sys.OS_IOS == cc.sys.os) {
                    jsb.reflection.callStaticMethod(
                        "AppController",
                        "wxShareTexture:",
                        writePath + textrueName
                    );
                }
            }
        } catch (e) {
            jsclient.native.HelloOC("wxShareHBImg throw: " + JSON.stringify(e));
        }
    },
    //获取网络状态 wifi还是4G
    getNetStauts: function () {
            mylog("getNetStauts...");
            var netStatus = null;

            if (!(jsb.fileUtils.isFileExist(jsb.fileUtils.getWritablePath() + "newVersion20181025.txt"))) {
                return "wifi";
            }

            var netStatus = null;
            try {
                if (cc.sys.OS_ANDROID == cc.sys.os) {
                    netStatus = jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getNetStauts", "()Ljava/lang/String;");
                } else if (cc.sys.OS_IOS == cc.sys.os) {
                    netStatus = jsb.reflection.callStaticMethod("AppController", "getNetStauts");
                    if (netStatus == "notReachable") {
                        netStatus = "notReachable";
                    } else if (netStatus == "wifi") {
                        netStatus = "wifi";
                    } else {
                        netStatus = "mobile";
                    }
                }
                return netStatus;
            } catch (e) {
                return "wifi";
            }
        },
    //直接打开微信
    openWXappOnly: function () {

        if (!jsclient.canOpenWeiXin) {
            jsclient.ShowToast("您需要重新下载游戏才能使用此功能！");
            return;
        }

        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "openWXappOnly", "()V");
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                var openStatus = jsb.reflection.callStaticMethod("AppController", "openWXappOnly");
                if (!openStatus) {
                    jsclient.showMsg("打开微信失败，请重试！");
                }

            }
            return;
        } catch (e) {
            return;
        }
    },
    wxLogin: function () {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                //Native发送_event:WX_USER_LOGIN  返回信息为json通过json中是否有nickName判断登录是否成功
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartWxLogin", "()V");
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                //Native发送_event:WX_USER_LOGIN  返回信息为json通过json中是否有nickName判断登录是否成功
                jsb.reflection.callStaticMethod("AppController", "sendAuthRequest");
            }
        } catch (e) { 
            jsclient.native.HelloOC("wxLogin throw: " + JSON.stringify(e));
        }

    }
    //聊呗登录
    ,lbLogin: function () {
        if (!jsclient.hasXianLiao) return;

        mylog("聊呗登录 平台选择");
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                //Native发送_event:WX_USER_LOGIN  返回信息为json通过json中是否有nickName判断登录是否成功
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartLiaoBeiLogin", "()V");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                //Native发送_event:WX_USER_LOGIN  返回信息为json通过json中是否有nickName判断登录是否成功
                jsb.reflection.callStaticMethod("AppController", "sendLBRequest");
            }
        } catch (e) {
            jsclient.native.HelloOC("xianLiaoLogin throw: " + JSON.stringify(e));
        }
    }
    //聊呗图片分享
    ,lbShareImage: function () {

        if (!jsclient.hasXianLiao) return;

        try {
            mylog("xlShareImage...");
            var writePath = jsb.fileUtils.getWritablePath();
            var textrueName = "wxcapture_screen.png";
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "org.cocos2dx.javascript.AppActivity",
                    "StartShareTextureToLB",
                    "(Ljava/lang/String;)V",
                    writePath + textrueName
                );
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                var writePath = jsb.fileUtils.getWritablePath();
                var textrueName = "wxcapture_screen.png";
                jsb.reflection.callStaticMethod(
                    "AppController",
                    "lbShareImage:",
                    writePath + textrueName
                );
            }
        } catch (e) {
            jsclient.native.HelloOC("XLShareImage throw: " + JSON.stringify(e));
        }
    }
    //聊呗文本分享
    ,lbShareText: function (text) {

        if (!jsclient.hasXianLiao) return;

        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "org.cocos2dx.javascript.AppActivity",
                    "StartShareTextToLB",
                    "(Ljava/lang/String;)V",
                    text
                );
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "AppController",
                    "lbShareText:",
                    text
                );
            }
        } catch (e) {
            jsclient.native.HelloOC("xlShareText throw: " + JSON.stringify(e));
        }
    }
    //聊呗分享URL
    , lbShareUrl: function (url, title, description) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartShareInviteGameToLB", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", url, title, description);
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "lbShareUrl:AndText:AndUrl:", title, description, url);
            }
        } catch (e) {
            jsclient.native.HelloOC("lbShareUrl throw: " + JSON.stringify(e));
        }

    }

    //闲聊登录
    ,xlLogin: function () {
        if (!jsclient.hasXianLiao) return;

        mylog("闲聊登录 平台选择");
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                //Native发送_event:WX_USER_LOGIN  返回信息为json通过json中是否有nickName判断登录是否成功
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartXianLiaoLogin", "()V");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                //Native发送_event:WX_USER_LOGIN  返回信息为json通过json中是否有nickName判断登录是否成功
                jsb.reflection.callStaticMethod("AppController", "sendXLRequest");
            }
        } catch (e) {
            jsclient.native.HelloOC("xianLiaoLogin throw: " + JSON.stringify(e));
        }
    }
    //闲聊邀请分享
    ,xlInviteGame: function (roomId, roomToken, title, description) {

        if (!jsclient.hasXianLiao) return;

        // mylog("先调用测试函数");
        // try {
        //     if (cc.sys.OS_ANDROID == cc.sys.os) {
        //         //Native发送_event:WX_USER_LOGIN  返回信息为json通过json中是否有nickName判断登录是否成功
        //         jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "testt", "()V");
        //     }
        // } catch (e) {
        //     mylog(" bug...... 测试函数报错 testt");
        //     jsclient.native.HelloOC("xianLiaoLogin throw: " + JSON.stringify(e));
        // }


        try {
            // mylog("xlInviteGame...roomId   " + typeof (roomId));
            // mylog("xlInviteGame...roomToken   " + typeof (roomToken));
            // mylog("xlInviteGame...title   " + typeof (title));
            // mylog("xlInviteGame...description   " + typeof (description));
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "org.cocos2dx.javascript.AppActivity",
                    "StartShareInviteGameToXL",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                    roomId, roomToken, title, description
                );
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "AppController",
                    "xlShareInvite:AndID:AndTitle:AndText:",
                    roomToken,
                    roomId,
                    title,
                    description
                );
            }
        } catch (e) {

            mylog("xlInviteGame... bug!!!!!!!");
            mylog("JSON.stringify(e) == " + JSON.stringify(e));
            jsclient.native.HelloOC("XLInviteGame throw: " + JSON.stringify(e));
        }
    }
    //闲聊图片分享
    ,xlShareImage: function () {

        if (!jsclient.hasXianLiao) return;

        try {
            mylog("xlShareImage...");
            var writePath = jsb.fileUtils.getWritablePath();
            var textrueName = "wxcapture_screen.png";
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "org.cocos2dx.javascript.AppActivity",
                    "StartShareTextureToXL",
                    "(Ljava/lang/String;)V",
                    writePath + textrueName
                );
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                var writePath = jsb.fileUtils.getWritablePath();
                var textrueName = "wxcapture_screen.png";
                jsb.reflection.callStaticMethod(
                    "AppController",
                    "xlShareImage:",
                    writePath + textrueName
                );
            }
        } catch (e) {
            jsclient.native.HelloOC("XLShareImage throw: " + JSON.stringify(e));
        }
    }
    //闲聊文本分享
    ,xlShareText: function (text) {

        if (!jsclient.hasXianLiao) return;

        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "org.cocos2dx.javascript.AppActivity",
                    "StartShareTextToXL",
                    "(Ljava/lang/String;)V",
                    text
                );
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod(
                    "AppController",
                    "xlShareText:AndText:",
                    "【星悦内蒙麻将】",
                    text
                );
            }
        } catch (e) {
            jsclient.native.HelloOC("xlShareText throw: " + JSON.stringify(e));
        }
    }
    //闲聊查询游戏邀请信息
    ,xlGetInviteGameInfo: function () {

        if (!jsclient.hasXianLiao) return;
        mylog("xlGetInviteGameInfo...");
        if (!(jsb.fileUtils.isFileExist(jsb.fileUtils.getWritablePath() + "xianliao.txt")
            || !jsclient.hasXianLiao)
        ) {
            return;
        }


        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "xlGetInviteGameInfo", "()V");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "xlGetInviteGameInfo");
            }
        } catch (e) {
            jsclient.native.HelloOC("xlGetInviteGameInfo throw: " + JSON.stringify(e));
        }
    }


    ,wxShareImageForDesignImage: function (timeline) {
        jsclient.shareInChuanQi = false;
        if(!jsclient.openWxShareLocalImage_android && cc.sys.OS_ANDROID == cc.sys.os){

            jsclient.showMsg("很抱歉，此项功能，需要去下载最新的客户端");
            return
        }


    //jsb.fileUtils.writeToFile("res/activity/btn_giftbag.png",jsb.fileUtils.getWritablePath() + "/btn_giftbag.png");
    try {
        timeline = timeline || false;
       
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            var num = (Math.floor(Math.random() * 3) + 1);
            var textrueName = "res/aress/gameInner/di_wxfx_" + num + ".png";
            var basePath = jsb.fileUtils.getWritablePath();

            var updatePath = basePath + "update/" + textrueName;
            if (jsb.fileUtils.isFileExist(updatePath)) {
                //jsclient.native.ShowLogOnJava("fileExist = " + updatePath);
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartShareTextureWxSceneSession", "(Ljava/lang/String;Z)V", updatePath, timeline);
                    
            } else if (jsb.fileUtils.isFileExist(textrueName)) {
                //jsclient.native.ShowLogOnJava("fileExist = " + textrueName);
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartwxShareTexture_Assets_WxSceneSession", "(Ljava/lang/String;Z)V", textrueName, timeline);
           
            } else{
                jsclient.showMsg("图片不存在！");
            }

            //Android 下调用StartURLImageWxSceneSession(),  可以读取URL图片,图片路径"http://sources1.happyplaygame.net/jxmj/HappyPlay/btn_giftbag.png";
            //    StartwxShareTexture_Assets_WxSceneSession()   读取本地图片,图片路径"res/home/hallBg.png";
        } else if (cc.sys.OS_IOS == cc.sys.os) {

            //ios先分享链接，之后再分享图片
            var des = "";
            des = "内蒙人都爱玩的麻将游戏，简单好玩，足不出户打麻将，亲们快快加入吧。";
            des += ", 放心游戏，绝无外挂！";
            if("undefined" == typeof (jsb.fileUtils.getXXSecretData))//老包
            {
                jsclient.native.wxShareUrl(jsclient.remoteCfg.wxShareUrl, "星悦内蒙麻将", des );
            }
            else{//新包
            
                if(jsclient.openClipboard){

                    var writePath = ""/*jsb.fileUtils.getWritablePath()*/;
                    var num = (Math.floor(Math.random() * 3) + 1);
                    var name = "res/aress/gameInner/di_wxfx_" + num + ".png";
                    var textrueName = jsb.fileUtils.fullPathForFilename(name);
                    // //jsb.reflection.callStaticMethod("AppController", "wxShareTexture:AndTimeLine:", writePath + textrueName, timeline);
                    jsb.reflection.callStaticMethod("AppController", "wxShareTexture:AndTimeLine:", writePath + textrueName, timeline);
                }
                else {
                    jsclient.uiPara = {
                        title : "【星悦•内蒙麻将】",
                        desc  :  des
                        //isActivity:false
                    };
                    jsclient.native.wxShareUrlTimeline(jsclient.remoteCfg.wxShareUrl,
                                uiPara.title,
                                uiPara.desc);
                }
            }



        }
    } catch (e) {
        jsclient.native.HelloOC("wxShareImage throw: " + JSON.stringify(e));
    }

    },
    wxShareUrl: function (url, title, description) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartShareWebViewWxSceneSession", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", url, title, description);
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "wxShareUrl:AndText:AndUrl:", title, description, url);
            }
        } catch (e) {
            jsclient.native.HelloOC("wxShareUrl throw: " + JSON.stringify(e));
        }

    },
    wxShareImage: function (textrueName) {
        jsclient.shareInChuanQi = false;
        try {
            var writePath = jsb.fileUtils.getWritablePath();
            if(!textrueName){
                textrueName = "wxcapture_screen.png";
            }

            if (cc.sys.OS_ANDROID == cc.sys.os) {
                //脚本通知c++截屏 event:capture_screen c++收到后返回截屏结果信息captureScreen_OK captureScreen_False成功本函数响应

                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartShareTextureWxSceneSession", "(Ljava/lang/String;)V", writePath + textrueName);

            } else if (cc.sys.OS_IOS == cc.sys.os) {
                //脚本通知c++截屏 event:capture_screen c++收到后返回截屏结果信息captureScreen_OK captureScreen_False成功本函数响应
                jsb.reflection.callStaticMethod("AppController", "wxShareTexture:", writePath + textrueName);
            }
        } catch (e) {
            jsclient.native.HelloOC("wxShareImage throw: " + JSON.stringify(e));
        }
    },
     wxShareImage_ss: function () {
         
         try {
            var writePath = jsb.fileUtils.getWritablePath();
            var textrueName = "m.png";
             if (cc.sys.OS_ANDROID == cc.sys.os) {
                 //脚本通知c++截屏 event:capture_screen c++收到后返回截屏结果信息captureScreen_OK captureScreen_False成功本函数响应
                 jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartShareTextureWxSceneSession", "(Ljava/lang/String;)V", writePath + textrueName);
             } else if (cc.sys.OS_IOS == cc.sys.os) {
                 //脚本通知c++截屏 event:capture_screen c++收到后返回截屏结果信息captureScreen_OK captureScreen_False成功本函数响应
                 jsb.reflection.callStaticMethod("AppController", "wxShareTexture:", null);
                //  writePath + textrueName
             }
         } catch (e) {
             jsclient.native.HelloOC("wxShareImage throw: " + JSON.stringify(e));
         }
     },


    MyShareImage: function () {
        jsclient.shareInChuanQi = false;
        //分享页已存在， 停止停止调用
        if (jsclient.showShareWXLayer) return;

        if(!jsclient.hasXianLiao && !jsclient.hasLiaoBei && !jsclient.hasDuoLiao)
        {
            jsclient.native.wxShareImage();
        }else
        {
            jsclient.xianLiaoPara = {};
            jsclient.xianLiaoPara.type = XianLiaoType.image;
            jsclient.Scene.addChild(new ShareWXLayer());
        }
    },

    wxShareUrlTimeline: function (url, title, description)
    {
        try
        {
            if (cc.sys.OS_ANDROID == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartShareWebViewWxTimeline",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", url, title, description);
            }
            else if (cc.sys.OS_IOS == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("AppController", "wxShareUrlTimeline:AndText:AndUrl:", title, description, url);
            }
        } catch (e)
        {
            jsclient.native.HelloOC("wxShareUrlTimeline throw: " + JSON.stringify(e));
        }
    },

    wxShareText: function (text) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "StartShareTextWxSceneSession", "(Ljava/lang/String;)V", text);

            } else if (cc.sys.OS_IOS == cc.sys.os) {

            }
        } catch (e) {
            jsclient.native.HelloOC("wxShareText throw: " + JSON.stringify(e));
        }
    },
    NativeBattery: function () {
    try {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "NativeBattery", "()V");
        } else if (cc.sys.OS_IOS == cc.sys.os) {
            jsb.reflection.callStaticMethod("AppController", "NativeBattery");
        }
    } catch (e) {
        jsclient.native.HelloOC("NativeBattery throw: " + JSON.stringify(e));
    }
}
    , NativeVibrato: function () {
        cc.log("++++ 你震动了一次 +++++");
    try {
        if (cc.sys.OS_ANDROID == cc.sys.os) {
            jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "NativeVibrato", "(Ljava/lang/String;Ljava/lang/String;)V", "100,300,100,300", "false");
        } else if (cc.sys.OS_IOS == cc.sys.os) {
            jsb.reflection.callStaticMethod("AppController", "NativeVibrato");
        }
    } catch (e) {
        jsclient.native.HelloOC("NativeVibrato throw: " + JSON.stringify(e));
    }
},
    StartRecord: function (filePath, fileName) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "startRecord", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", String(filePath), String(fileName));
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "startRecord:lajioc:", String(filePath), String(fileName));
            }
        } catch (e) {
            jsclient.native.HelloOC("StartRecord throw: " + JSON.stringify(e));
        }
    },
    EndRecord: function (eventName) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "endRecord", "(Ljava/lang/String;)V", String(eventName));
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "endRecord:", String(eventName));
            }
        } catch (e) {
            jsclient.native.HelloOC("EndRecord throw: " + JSON.stringify(e));
        }
    },
    UploadFile: function (fullFileName, url, eventName) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "uploadFile", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", String(fullFileName), String(url), String(eventName));
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "uploadFile:url:eventName:", String(fullFileName), String(url), String(eventName));
            }
        } catch (e) {
            jsclient.native.HelloOC("UploadFile throw: " + JSON.stringify(e));
        }
    },
    uploadPic: function (fullFileName, url, eventName,token) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "uploadPic", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", String(fullFileName), String(url), String(eventName), String(token));
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "uploadPic:url:eventName:token:", String(fullFileName), String(url), String(eventName), String(token));
            }
        } catch (e) {
            jsclient.native.HelloOC("UploadFile throw: " + JSON.stringify(e));
        }
    },

    DownLoadFile: function (filePath, fileName, url, eventName) {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "downLoadFile", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V", String(filePath), String(fileName), String(url), String(eventName));
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "downloadFile:fileName:url:eventName:", String(filePath), String(fileName), String(url), String(eventName));
            }
        } catch (e) {
            jsclient.native.HelloOC("DownLoadFile throw: " + JSON.stringify(e));
        }
    },
    HelloOC: function (message) {
        return; //
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                console.log(String(message));
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                console.log(String(message));
                jsb.reflection.callStaticMethod("AppController", "HelloOC:", String(message));
            }
        } catch (e) {
            console.log("虽然我挂掉了,但是我还是坚持打印了了log: " + String(message));
        }
    },
    getRealAliDunIp:function (xxSecretStr)
    {
        if(jsclient.hasAliDunSDK())
        {
            try
            {
                var playerUid = cc.sys.localStorage.getItem("uid");
                if(playerUid)
                {
                    xxSecretStr = xxSecretStr +",";
                    var extraInfo = jsb.fileUtils.enCodeXXSecretData(playerUid);
                    xxSecretStr += extraInfo;
                }
                if (cc.sys.OS_ANDROID == cc.sys.os)
                {
                    jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getRemoteIpByAliDun", "(Ljava/lang/String;)Ljava/lang/String;", xxSecretStr);
                }
                else if(cc.sys.OS_IOS == cc.sys.os)
                {
                    jsb.reflection.callStaticMethod("AppController","getRemoteIpByAliDun:", String(xxSecretStr));
                }
            }
            catch(e)
            {
            }
        }
    },

    ShowLogOnJava:function(str)
    {
        // console.log(str);
        // return;
        try{
            if (cc.sys.OS_ANDROID == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "ShowLogOnJava", "(Ljava/lang/String;)V",String(str));
            }
            else if(cc.sys.OS_IOS==cc.sys.os)
            {
                console.log(str);
            }

        }catch(e)
        {
            jsclient.native.HelloOC("ShowLogOnJava throw: " + JSON.stringify(e));
        }
    },
    CalculateLineDistance: function (lat1, lng1, lat2, lng2) {
        if (lat1 == 0 || lng1 == 0 || lat2 == 0 || lng2 == 0){
            return 0;
        }
        // 因为统一转换为百度地图的坐标，所以就用这个算法计算距离就可以了
        if (jsclient.hasBaiDuLoc) {
            lat1 = parseFloat(lat1);
            lng1 = parseFloat(lng1);
            lat2 = parseFloat(lat2);
            lng2 = parseFloat(lng2);
            var f = jsclient.getRad((lat1 + lat2) / 2);
            var g = jsclient.getRad((lat1 - lat2) / 2);
            var l = jsclient.getRad((lng1 - lng2) / 2);

            var sg = Math.sin(g);
            var sl = Math.sin(l);
            var sf = Math.sin(f);

            var s, c, w, r, d, h1, h2;
            var a = jsclient.EARTH_RADIUS;
            var fl = 1 / 298.257;

            sg = sg * sg;
            sl = sl * sl;
            sf = sf * sf;

            s = sg * (1 - sl) + (1 - sf) * sl;
            c = (1 - sg) * (1 - sl) + sf * sl;

            w = Math.atan(Math.sqrt(s / c));
            r = Math.sqrt(s * c) / w;
            d = 2 * w * a;
            h1 = (3 * r - 1) / 2 / c;
            h2 = (3 * r + 1) / 2 / s;

            var ret = d * (1 + fl * (h1 * sf * (1 - sg) - h2 * (1 - sf) * sg));
            return isNaN(ret) ? 0 : ret.toFixed(1);
        }
	else{

	        try {
	            if (cc.sys.OS_ANDROID == cc.sys.os) {
	                var dis = jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "CalculateDistance",
	                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
	                    String(latitude1), String(longitude1), String(latitude2), String(longitude2));

	                return dis * 1;
	            }
	            else if (cc.sys.OS_IOS == cc.sys.os) {
	                var dis = jsb.reflection.callStaticMethod("AppController", "calculateDistance:lon1:lat2:lon2:",
	                    String(latitude1), String(longitude1), String(latitude2), String(longitude2));

	                var disVar = parseFloat(dis).toFixed(1);

	                return disVar;
	            }
	            else {
	                return 0;
	            }

	        }
	        catch (e) {
	            jsclient.native.HelloOC("CalculateLineDistance throw: " + JSON.stringify(e));
	            return 0;
	        }
	}

    },
//获取玩家经度
    GetLongitudePos: function () {
        if (!jsclient.hasBaiDuLoc)
            return 0;

        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                return jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getLongitudePos", "()Ljava/lang/String;");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                return jsb.reflection.callStaticMethod("AppController", "getLongitudePos");
            }
            else {
                return 0;
            }
        }
        catch (e) {
            jsclient.native.HelloOC("getLongitudePos throw: " + JSON.stringify(e));
            return 0;
        }
    },
    GetFormattedAddress: function () {
        var address = "";
        if(jsclient.hasBaiDuLoc)
        {
            try {
                if (cc.sys.OS_ANDROID == cc.sys.os) {
                    address = jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getFormattedAddress", "()Ljava/lang/String;");
                }
                else if (cc.sys.OS_IOS == cc.sys.os) {
                    //address = jsb.reflection.callStaticMethod("LocationManager", "");
                    address = "";
                }
                return address;
            }
            catch (e) {
            }

        }
        else {
            if (jsclient.hasFormattedAddress()) {
                try {
                    if (cc.sys.OS_ANDROID == cc.sys.os) {
                        address = jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getFormattedAddress", "()Ljava/lang/String;");
                    }
                    else if (cc.sys.OS_IOS == cc.sys.os) {
                        address = jsb.reflection.callStaticMethod("AppController", "getFormattedAddress");
                    }
                    return address;
                }
                catch (e) {
                }
            }
        }

        return address;
    },
    getCurLocation: function () {
        if (!jsclient.hasBaiDuLoc)
            return "";

        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                return jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getCurLocation", "()Ljava/lang/String;");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                return jsb.reflection.callStaticMethod("LocationManager", "getCurLocation");
            }
            else {
                return "";
            }
        }
        catch (e) {
            jsclient.native.HelloOC("getLongitudePos throw: " + JSON.stringify(e));
            return "";
        }
    },
    //获取玩家纬度
    GetLatitudePos: function () {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                return jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getLatitudePos", "()Ljava/lang/String;");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                return String(jsb.reflection.callStaticMethod("AppController", "getLatitudePos"));
            }
            else {
                return 0;
            }
        }
        catch (e) {
            jsclient.native.HelloOC("getLatitudePos throw: " + JSON.stringify(e));
            return 0;
        }
    },

    //获取玩家经度
    GetLongitudePos: function () {
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                return jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getLongitudePos", "()Ljava/lang/String;");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                return jsb.reflection.callStaticMethod("AppController", "getLongitudePos");
            }
            else {
                return 0;
            }
        }
        catch (e) {
            jsclient.native.HelloOC("getLongitudePos throw: " + JSON.stringify(e));
            return 0;
        }
    },


    //获取玩家经度
    initGaoDeMap: function () {

        if(!jsclient.remoteCfg.guestLogin)
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {

              //  jsb.reflection.callStaticMethod("AppController", "initGaoDeMap")
            }
            else if(cc.sys.OS_IOS ==cc.sys.os)
            {
                jsb.reflection.callStaticMethod("AppController", "initGaoDeMap")
            }
        }
        catch (e) {

            return "";
        }
    },

    //语音
    JoinGameVoiceRoom:function (playerId,nickname,roomid)//加入语音房间
    {
        jsclient.native.HelloOC("JoinGameVoiceRoom 110 110 110");
        jsclient.native.ShowLogOnJava("---JoinGameVoiceRoom 1-----------");
        try
        {
            if (cc.sys.OS_ANDROID == cc.sys.os)
            {
                jsclient.native.HelloOC("JoinGameVoiceRoom 110 110 111");
                jsclient.native.ShowLogOnJava("---JoinGameVoiceRoom A1-----------");


                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "JoinGameVoiceRoom",
                    "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V",
                    String(playerId), String(nickname), String(roomid));

                jsclient.native.HelloOC("JoinGameVoiceRoom 110 110 112");
                jsclient.native.ShowLogOnJava("---JoinGameVoiceRoom A2-----------");

            }
            else if (cc.sys.OS_IOS == cc.sys.os)
            {
                try
                {
                    jsb.reflection.callStaticMethod("AppController", "setVoiceRoomID:", roomid);
                    jsb.reflection.callStaticMethod("AppController", "setVoiceUserName:", JSON.stringify(nickname));
                    jsb.reflection.callStaticMethod("AppController", "setVoiceUserId:", JSON.stringify(playerId));
                    jsb.reflection.callStaticMethod("AppController", "JoinGameVoiceRoom");
                }
                catch (e)
                {
                    cc.log("110 加入语AppController音AndText房间ios e==" + e);
                }
            }
        }
        catch(e)
        {
            cc.log("110 加入语AppController音AndText房间ios e==" + e);
        }
    },

    vioceStart:function ()//语音房间开始说话
    {
        try
        {
            if (cc.sys.OS_ANDROID == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "vioceStart", "()V");
            }
            else if (cc.sys.OS_IOS == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("AppController", "voiceStart");
            }
            return 1;
        }
        catch(e)
        {
            return 0;
        }
    },

    voiceStop:function ()//语音房间结束说话
    {
        try
        {
            if (cc.sys.OS_ANDROID == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "voiceStop", "()V");
            }
            else if(cc.sys.OS_IOS == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("AppController","voiceStop");
            }
            return 1;
        }
        catch(e)
        {
            return 0;
        }
    },

    leaveRoom:function ()
    {
        try
        {
            if (cc.sys.OS_ANDROID == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "leaveRoom", "()V");
            }
            else if(cc.sys.OS_IOS == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("AppController","leaveRoom");
            }
            return 1;
        }
        catch(e)
        {
            return 0;
        }
    },

    returnRoom:function ()
    {
        try
        {
            if (cc.sys.OS_ANDROID == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "returnRoom", "()V");
            }
            else if(cc.sys.OS_IOS == cc.sys.os)
            {
                jsb.reflection.callStaticMethod("AppController","returnRoom");
            }
            return 1;
        }
        catch(e)
        {
            return 0;
        }
    },

    getRoomData : function(){
        //jsclient.native.ShowLogOnJava("getRoomData ---------------11111");
        var roomData = "";
        if(!jsclient.openClipboard) return roomData;

        //jsclient.native.ShowLogOnJava("getRoomData ---------------22222222222");
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                roomData = jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getRoomData", "()Ljava/lang/String;");
                //jsclient.native.ShowLogOnJava("getRoomData ---------------roomData = " + roomData);
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                roomData = jsb.reflection.callStaticMethod("AppController", "getRoomData");
            }
            return roomData;
        }
        catch (e) {
            return roomData;
        }
    },
    customExitGame: function(message) {
        //sureValue   0 代表弹出系统确认窗体  1代表直接退到后台
        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "customExitGame",
                    "()V");
            } else if (cc.sys.OS_IOS == cc.sys.os) {
                jsb.reflection.callStaticMethod("AppController", "gameExit");
            }
        }
        catch (e) {
            jsclient.native.HelloOC("DownLoadFile throw: " + JSON.stringify(e));
        }
    },
    //剪切板
    writeToClipboard: function(message){

        if(!jsclient.openClipboard) return;
        try{
            if(cc.sys.OS_ANDROID === cc.sys.os ){
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity","writeToClipboard","(Ljava/lang/String;)V",String(message));
            }else if(cc.sys.OS_IOS === cc.sys.os){
                jsb.reflection.callStaticMethod("AppController","writeToClipboard:",String(message));
            }
        }catch(e){
            //mylog("writeToClipboard throw: " + JSON.stringify(e));
        }
    },
    //剪切板
    getFromClipboard: function(){
        var content = "";
        if(!jsclient.openClipboard) return content;
        try{
            if(cc.sys.OS_ANDROID === cc.sys.os){
                content = jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity","getFromClipboard","()Ljava/lang/String;");
            }else if(cc.sys.OS_IOS === cc.sys.os){
                content = jsb.reflection.callStaticMethod("AppController","getFromClipboard");
            }
            return content;
        }catch(e){
            //mylog("getFromClipboard throw: " + JSON.stringify(e));
            return content;
        }
    },
    clearCurLocation:function(){
        if (!jsclient.hasBaiDuLoc)
            return 0;

        try {
            if (cc.sys.OS_ANDROID == cc.sys.os) {
                return jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "clearCurLocation", "()V");
            }
            else if (cc.sys.OS_IOS == cc.sys.os) {
                return jsb.reflection.callStaticMethod("LocationManager", "clearCurLocation");
            }
        }
        catch (e) {
            jsclient.native.HelloOC("getLongitudePos throw: " + JSON.stringify(e));
            return 0;
        }
    },
    reInitBdMapSDK: function () {
        if (jsclient.hasBaiDuLoc) {
            console.log("地图reInitBdMapSDK117")
            try {
                if (cc.sys.OS_IOS == cc.sys.os) {
                    //if(!jsclient.isIOSExamine()) {
                    jsb.reflection.callStaticMethod("LocationManager", "startLocation");
                    //}
                }
                else if (cc.sys.OS_ANDROID == cc.sys.os) {
                    jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "reInitBdMapSDK", "()V");
                }
            }
            catch (e) {
            }

        } else {
            /*try {
                if (cc.sys.OS_IOS == cc.sys.os) {
                    //if(!jsclient.isIOSExamine()) {
                    jsb.reflection.callStaticMethod("AppController", "reInitBdMapSDK");
                    //}
                }
                else if (cc.sys.OS_ANDROID == cc.sys.os) {
                    jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "reInitBdMapSDK", "()V");
                }
            }
            catch (e) {
            }*/
        }
    },

    getPictureFromPhoneAlbum:function (url, token) {
        try {
            if(cc.sys.OS_ANDROID === cc.sys.os) {
                jsb.reflection.callStaticMethod("org.cocos2dx.javascript.AppActivity", "getPictureFromPhoneAlbum", '()V')
            }
            else if(cc.sys.OS_IOS === cc.sys.os) {
                jsb.reflection.callStaticMethod('AppController', 'imageViewIsSelector:uploadimgUrl:', token, url)
            }
        }
        catch (e) {
            jsclient.native.ShowLogOnJava("jsclient.native.getPictureFromPhoneAlbum throw: " + JSON.stringify(e))
        }
    }

}

/**
 *主动报错误 给错误收集服务器
 */
jsclient.upLoadErrorInfo = function(errorTag, errorMsg) {
    //errorTag和errorMsg不能使用中文
    if (!errorMsg || errorMsg.length <= 0)
        return;

    if (!cc.sys.isMobile) {
        errorTag += "_windows";
    } else {
        errorTag += "_mobile";
    }
    cc.log("--- Error: " + errorTag + "=" + errorMsg);
    var errorUrl = "";
    if(!jsclient.remoteCfg){
        if(cc.sys.OS_OSX == cc.sys.os || cc.sys.OS_WINDOWS == cc.sys.os) {
            errorUrl = "http://139.129.206.54:3000/xynmmj";
        }else{
            errorUrl = "http://139.129.206.54:3000/xynmmj";
        }
    }else {
        errorUrl =  jsclient.remoteCfg.errorReportServer;
    }

    var xhr = cc.loader.getXMLHttpRequest();
    if ("undefined" != typeof(errorUrl) && "" != errorUrl) {
        xhr.open("POST", errorUrl + "?content=" + base64encode(errorTag + ": " + errorMsg + "\n"));
    } else {
        xhr.open("POST", "http://139.129.206.54:3000/xynmmj" + "?content=" + base64encode(errorTag + ": " + errorMsg + "\n"));
    }
    xhr.onreadystatechange = function() {};
    xhr.send();
};
/**
 * 跳转贷款界面
 * 皮皮-首页	        https://pt.szprosfin.com/mlogin.html#PP_HM
 * 皮皮-麻将战绩分享	https://pt.szprosfin.com/mlogin.html#PP_MJ
 * 皮皮-棋牌战绩分享	https://pt.szprosfin.com/mlogin.html#PP_QP
 * 皮皮-牛牛战绩分享 https://pt.szprosfin.com/mlogin.html#PP_NN
 */
jsclient.jumpToLoanUrl = function (type) {
    var url = "https://pd.szprosfin.com/tologin.html?channelCode=";
    var TDtype = TDENMU.loan_XY_Home;
    switch (type) {
        case 1:
            url += "ppmghm";
            break;
        case 2:
            url += "ppmgsc";
            TDtype = TDENMU.loan_XY_Shop;
            break;
        case 6:
            url += "ppmgfx";
            TDtype = TDENMU.loan_XY_Notice;
            break;
        default:
            break;
    }
    cc.Application.getInstance().openURL(url);
    jsclient.upLoadErrorInfo("jumpToLoanUrl", url);
    jsclient.talkingDataOnClickJS(TDENMU.loan, TDtype);
}

//计算两个日期相差多少天
function getDays(strDateStart,strDateEnd){
    var strSeparator = "-"; //日期分隔符
    var oDate1;
    var oDate2;
    var iDays;
    oDate1= strDateStart.split(strSeparator);
    oDate2= strDateEnd.split(strSeparator);
    var strDateS = new Date(oDate1[0], oDate1[1]-1, oDate1[2]);
    var strDateE = new Date(oDate2[0], oDate2[1]-1, oDate2[2]);
    iDays = parseInt(Math.abs(strDateS - strDateE ) / 1000 / 60 / 60 /24);//把相差的毫秒数转换为天数
    return iDays ;
}

jsclient.getYMDHMS = function (date,Time) {
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var day = date.getDate();
    var hours = date.getHours();
    var min = date.getMinutes();
    var sec = date.getSeconds();

    if (month < 10) month = '0' + month;
    if (day < 10) day = '0' + day;
    if (hours < 10) hours = '0' + hours;
    if (min < 10) min = '0' + min;
    if (sec < 10) sec = '0' + sec;
    if(!Time){
        return (year+'-'+month+'-'+day+' '+hours+':'+min+':'+sec);
    }else {
        return (year+'-'+month+'-'+day);
    }
}

//人未满时，快速开局
jsclient.applyStartGame = function(yes){
    let counts = 2
    if (jsclient.data.sData && jsclient.data.sData.tData) {
        counts = jsclient.getRealPlayerCount()
    }
    console.log("jsclient.applyStartGame===counts=====" + counts)
    jsclient.gamenet.request("pkroom.handler.tableMsg", {cmd: "ApplyStartGame", yes: yes, counts:counts})
}

jsclient.CheckShowApplyStarGameLayer = function () {
    let sData = jsclient.data.sData
    if (sData.tData.isShowApplyStarGameLayer != 0 && !jsclient.applyStartGameLayer) {
        jsclient.Scene.addChild(new ApplyStartGameLayer())
    } else if (sData.tData.isShowApplyStarGameLayer == 0 && jsclient.applyStartGameLayer) {
        jsclient.applyStartGameLayer.removeFromParent(true)
        delete jsclient.applyStartGameLayer
        jsclient.applyStartGameLayer = null
    }
}

//本地存储上次的登录方式 用于下次登录的时候显示默认登录方式的图片
jsclient.saveLocalLastLoginType = function () {
    let type = jsclient.operateFirstLoginType()
    if (type) {
        sys.localStorage.setItem("lastLoginType", type)
    }
}

function ClipPhoto(node, url, posx, posy, name, photoPath) {
    let typeurl = "res/aress/userInfo/userInfo_photo_di.png"
    let loadTextureFunc = function(texture) {
        if (node.getChildByName(name)) {
            node.removeChildByName(name)
        }
        let stencil = new cc.Sprite(typeurl)   //可以是精灵，也可以DrawNode画的各种图形

        //1.创建裁剪节点
        stencil.setScale(0.94,0.94)
        let clipper = new cc.ClippingNode(stencil)   //创建裁剪节点ClippingNode对象  带模板
        clipper.setInverted(false)         //显示被模板裁剪下来的底板内容。默认为false 显示被剪掉部分。
        //alpha阀值：表示像素的透明度值。
        //只有模板（stencil）中像素的alpha值大于alpha阈值时，内容才会被绘制。
        //alpha阈值（alphaThreshold）：取值范围[0,1]。
        //默认为1，表示alpha测试默认关闭，即全部绘制。
        //若不是1，表示只绘制模板中，alpha像素大于alphaThreshold的内容。
        clipper.setAlphaThreshold(0)   //设置绘制底板的Alpha值为0
        clipper.setPosition(posx,posy)
        clipper.setName(name)

        //3.创建底板
        let sprite = new cc.Sprite(texture)
        let spScaleX = stencil.getContentSize().width / sprite.getContentSize().width
        let spScaleY = stencil.getContentSize().height / sprite.getContentSize().height
        let spScale = Math.max(spScaleX, spScaleY)
        sprite.setScale(spScale, spScale)

        clipper.addChild(sprite)
        clipper.setScale(1, 1)
        cc.log("node  name----------> "+node.tag)

        node.addChild(clipper)
    }
    if (!photoPath) {
        cc.loader.loadImg(url, {isCrossOrigin: true}, function (err, texture) {
            if (!err && texture) {
                loadTextureFunc(texture)
            }
        })
    } else {
        loadTextureFunc(photoPath)
    }

}

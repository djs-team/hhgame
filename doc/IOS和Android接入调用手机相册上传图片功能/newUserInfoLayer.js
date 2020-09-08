

function ShowNewInfoLayer(pinfo) {
    Date.prototype.Format = function(format) {
        var o = {
            "M+" : this.getMonth()+1, //month
            "d+" : this.getDate(),    //day
            "h+" : this.getHours(),   //hour
            "m+" : this.getMinutes(), //minute
            "s+" : this.getSeconds(), //second
            "q+" : Math.floor((this.getMonth()+3)/3),  //quarter
            "S" : this.getMilliseconds() //millisecond
        }
        if(/(y+)/.test(format)) format=format.replace(RegExp.$1,
            (this.getFullYear()+"").substr(4 - RegExp.$1.length));
        for(var k in o)if(new RegExp("("+ k +")").test(format))
            format = format.replace(RegExp.$1,
                RegExp.$1.length==1 ? o[k] :
                    ("00"+ o[k]).substr((""+ o[k]).length));
        return format;
    }

    let uiitem_list, emojiTen_Btn, uiAnimationItem;
    jsclient.EmojiType = {
        emoji_one: 1, // 表情一发
        emoji_ten: 10, //  表情十连发
    };
    let today = new Date().Format("yyyyMMdd")
    let totalFreeNum = 0
    let useType = {useMoney:0, useProp:1}
    let productType = { ledou:11, coin:3, diamond:1}
    let cdCoolTime = 3  //表情冷却时间
    let photoBtnId = 0
    let bigPhoto, bigPhotoBg
    let photoPath = ''
    let uploadUrl = [
        'http://download.jxlwgame.com/weixin/uploadimg',
        'http://download.jxlwgame.com/weixin/uploadimg1'
    ]

    function sendUseProp(para) {
        let sData = jsclient.data.sData;
        if(!sData){
            jsclient.showMsg("只能在游戏中使用道具哦！");
            return;
        }
        let tData = sData.tData;
        if(jsclient.CDTimeNum > 0){
            jsclient.ShowToast("表情CD中...")
            return
        }

        if (pinfo.uid == SelfUid()) {
            jsclient.showMsg("不能对自己使用道具哦！");
        }
        else {
            let roomType=0;
            if(tData.coinRoomCreate){
                roomType = tData.coinType;
            };

            //if (jsclient.data.pinfo.coin >= tData.usePropCoin[index]) {

            jsclient.gamenet.request("pkroom.handler.tableMsg", {
                cmd: "MJUseProp",
                param: para,
                cointype: 0
            });
            jsclient.startCD(cdCoolTime);
            // }
            // else {
            //     jsclient.showMsg("金豆不足！");
            // }
        }

        jsclient.NewUserInfoLayer.removeFromParent(true);
        delete jsclient.NewUserInfoLayer;
    }

    /*
	* 普通房间，好友房为0,金币场为coinType
	* */
    function GetRoomType() {
        if(isValid(jsclient.data.sData) && isValid(jsclient.data.sData.tData)) {
            let tData = jsclient.data.sData.tData;
            if (tData.coinRoomCreate) {
                return 1;
            }
            return 0;
        }
        return 0;
    }

    let BindItem = function (item, info) {
        let bind = {
            _run: function () {
                this.refresh = function () {
                    let animIcon = this.getChildByName("animIcon")//道具
                    let page_coin = this.getChildByName("page_coin")//金豆
                    let page_money = this.getChildByName("page_money")//钻石兑换
                    let page_free = this.getChildByName("page_free")//限免

                    let coinNum = page_coin.getChildByName("coinNum")//金豆数值
                    let Panel_name = this.getChildByName("Panel_name")//剩余
                    let Panel_num=this.getChildByName("panel_num")//剩余次数

                    Panel_name.ignoreContentAdaptWithSize(true)
                    Panel_num.ignoreContentAdaptWithSize(true)

                    page_coin.visible = page_money.visible = page_free.visible = false
                    Panel_name.visible = Panel_num.visible = false

                    let iconPath = GAME_PROPS_TAB.getPropsIcon(info.propId)
                    animIcon.loadTexture(iconPath)

                    // 使用方式区分 useType 0代表消耗货币 1代表消耗道具
                    if (info.hasOwnProperty("useType")) {
                        if (info.useType == useType.useProp) {
                            /*
                            * 考虑兼容，如果2.0版本有用户购买了道具
                            * 如果有项目组道具不是免费的，用户有剩余购买的道具，这个地方需要注意了，
                            * （道具收费）不能照搬下面代码，需要分 nums > 0的情况和=0的情况 （做兼容），后台配置也要考虑兼容
                            * 陕西所有道具是免费的，不再做兼容的处理
                            * */
                            let nums = getPropNums(info.propId)
                            Panel_name.setVisible(true)
                            Panel_name.setString("剩余")
                            Panel_num.setVisible(true)
                            Panel_num.setString("X"+nums)
                            if (nums == 0) {
                                Panel_name.visible = false
                                Panel_num.visible = false
                                page_money.visible = true
                            } else {
                                Panel_name.visible = true
                                Panel_num.visible = true
                                page_money.visible = false
                            }
                        } else {
                            let cost = info.price
                            if (cost === 0) { // 免费
                                page_free.setVisible(true)
                            } else {
                                let playerInfo = jsclient.data.pinfo
                                let freeTimes = getFreeTimes(info, playerInfo)

                                if (freeTimes < info.freeNum) {
                                    Panel_name.setVisible(true)
                                    Panel_name.setString("试用")
                                    Panel_num.setVisible(true)
                                    Panel_num.setString("X"+(info.freeNum-freeTimes))
                                }
                                else if (cost > 0) {
                                    if(info.productType == 11) {
                                        // 乐豆
                                        page_money.visible = true
                                        let moneyNum = page_money.getChildByName("moneyNum")
                                        // 乐豆
                                        moneyNum.setString("" + cost)
                                        moneyNum.ignoreContentAdaptWithSize(true)
                                    }
                                    else {
                                        // 金币
                                        page_coin.visible = true
                                        coinNum.setString("" + cost)
                                    }
                                }
                            }
                        }
                    }
                };
                this.refresh()
                //创建遮罩层
                let spld = cc.Sprite.create("res/aress/userInfo/userInfo_daojuFrame.png")
                spld.setColor(cc.color(77, 77, 77))
                spld.setOpacity(168)
                let progressCooling = cc.ProgressTimer.create(spld)
                progressCooling.setType(cc.ProgressTimer.TYPE_RADIAL)
                progressCooling.setPercentage((jsclient.CDTimeNum / cdCoolTime) * 100)
                progressCooling.setName("progressCooling")
                progressCooling.setPosition(cc.p(this.getContentSize().width/2,this.getContentSize().height/2))
                this.addChild(progressCooling)
            },
            _clickAndSetColor: function () {
                cc.log("click function")
                jsclient.NewUserInfoLayer.clickPlayProp(info)
            },
            total_free_image: {
                _run:function () {
                    let totalFreeNumLeft = getTotalFreeTimesLeft()
                    this.visible = totalFreeNumLeft > 0 ? true : false
                }
            },
            _event: {
                cdChange:function (eD) {
                    let progressCooling = this.getChildByName("progressCooling")
                    let jd = (jsclient.CDTimeNum / cdCoolTime) * 100
                    if(jd <= 1) jd = 0
                    progressCooling.setPercentage(jd)
                },
            }
        };
        ConnectUI2Logic(item, bind)
    }

    let generateItems = function () {
        uiitem_list.removeAllChildren();

        if(!jsclient.playui || pinfo.uid == jsclient.data.pinfo.uid) {
            uiitem_list.visible = false;
            return;
        }

        let emojiArrByType = []
        let recordType = sys.localStorage.getItem("emojiTenRecord");

        if (recordType == jsclient.EmojiType.emoji_ten) {
            emojiArrByType = jsclient.payAniInfo.filter(
                function (it) {
                    return it.remark === "十次";
                }
            )
        } else {
            emojiArrByType = jsclient.payAniInfo.filter(
                function (it) {
                    return it.remark === "单次";
                }
            )
        }

        //排序
        emojiArrByType.sort(function (animA, animB) {
            return animA.order - animB.order;
        });
        cc.log(">>>>>>>>>>>jsclient.payAniInfo[i]:" + JSON.stringify(emojiArrByType));
        let length = emojiArrByType.length;
        let hang = 7;   //一行几个item
        let index = 0;  //item的index
        let dis_row = 5; //两行之间的间距
        let dis_column = 9; //两列之间的间距
        let sizeWidth = uiitem_list.getContentSize().width;
        let sizeHeight = uiitem_list.getContentSize().height;
        let itemWidth = uiAnimationItem.getContentSize().width;
        let itemHeight = uiAnimationItem.getContentSize().height;

        let lineCount = parseInt((length - 1) / hang) + 1;
        let sizeHeightInner = itemHeight * lineCount + dis_row * (lineCount + 1);
        sizeHeightInner = sizeHeightInner > sizeHeight ? sizeHeightInner : sizeHeight;

        for (let i = 0; i < length; i++) {
            let newItem = uiAnimationItem.clone();
            newItem.setAnchorPoint(cc.p(0.5, 0.5));
            newItem.visible = true;
            uiitem_list.addChild(newItem);
            newItem.setPosition(
                //sizeWidth * (Math.round(index % hang) + 0.5) / hang,
                // sizeHeightInner - parseInt(index / hang) * (itemHeight + lineOff) - innerDownOff);
                ((itemWidth * 0.5 + dis_column) + (itemWidth + dis_column) * (index % hang)),
                sizeHeightInner - (dis_row + itemHeight * 0.5) - parseInt(index / hang) * (itemHeight + dis_row));
            BindItem(newItem, emojiArrByType[i]);
            index++;
        }
        uiitem_list.setInnerContainerSize(cc.size(sizeWidth, sizeHeightInner));
    }

    let getPropNums = function (propID) {
        let nums = 0;
        let pinfo = jsclient.data.pinfo;
        if (pinfo.hasOwnProperty("props") && pinfo.props.hasOwnProperty(propID + "")) {
            nums = jsclient.data.pinfo.props[propID];
        }
        return nums;
    };

    function getTotalFreeTimesLeft() {
        let propDayTotalFreeInfo = jsclient.data.pinfo.propDayTotalFreeInfo || {times:0, day:today}
        let timeUsed = 0
        if (propDayTotalFreeInfo.day == today) {
            timeUsed = propDayTotalFreeInfo.times
        }
        let leftTotalFreeNum = totalFreeNum - timeUsed
        if (leftTotalFreeNum <= 0) leftTotalFreeNum = 0
        return leftTotalFreeNum
    }

    function updateTotalFreeLeftTimesLabel() {
        let textNode = jsclient.NewUserInfoLayer.jsBind.back.total_free_title.times._node
        textNode.setString(getTotalFreeTimesLeft()+"次")
        textNode.ignoreContentAdaptWithSize(true)
        textNode.visible = true
    }

    function getFreeTimes(info, playerInfo) {
        let freeTimes = 0
        //freeType 0 是固定免费次数的表情 freeType 1 是每日免费次数的表情
        if (info.freeType === 0) {
            freeTimes = playerInfo.freeprops && playerInfo.freeprops[info.propId] || 0
        } else {
            freeTimes = 0
            // cc.log("----------------- 每日免费次数的表情 ----------------- ::: " + JSON.stringify(playerInfo));
            if (playerInfo.dailyfreeprops && playerInfo.dailyfreeprops[info.propId]) {
                cc.log('dailyfreeprops ++++++++++++++++++++++++ =' + playerInfo.dailyfreeprops[info.propId].times)
                if (today !== playerInfo.dailyfreeprops[info.propId].day) {

                }
                else {
                    freeTimes = playerInfo.dailyfreeprops[info.propId].times
                }
            }
        }
        return freeTimes
    }

    function getPropParaByInfo(info) {
        let para = {
            "propId": info.propId,
            "propType": info.animeNum,
            "roomType": info.roomType,
            "fromUid": SelfUid(),
            "toUid": pinfo.uid,
            "times": 1
        }
        return para
    }

    function clickChangePhoto(btnId) {
        if (!jsclient.hasOpenPhonePhoto) {
            jsclient.showMsg("当前版本不支持此功能，请下载游戏最新版本！")
            return
        }
        photoBtnId = btnId
        jsclient.native.getPictureFromPhoneAlbum(uploadUrl[photoBtnId-1], jsclient.data.pinfo.accessToken)
    }

    //点击图片放大
    function clickPhotoMagnify(btnId) {
        let imgUrl = ""
        if (btnId === 1 && jsclient.data.pinfo.payimg) {
            imgUrl = jsclient.data.pinfo.payimg
        } else if (btnId === 2 && jsclient.data.pinfo.payimg1) {
            imgUrl = jsclient.data.pinfo.payimg1
        }

        let actFunc = function () {
            let endposX = bigPhoto.getParent().getContentSize().width / 2
            let endposY = bigPhoto.getParent().getContentSize().height
            let startPosX =bigPhoto.getParent().getChildByName("photo_" + btnId).getPositionX()
            let startPosY =bigPhoto.getParent().getChildByName("photo_" + btnId).getPositionY()
            bigPhoto.setPosition(startPosX, startPosY)
            bigPhoto.setScale(0)
            bigPhoto.runAction(cc.Spawn(cc.moveTo(0.2, cc.p(endposX, endposY)), cc.scaleTo(0.2, 3)))
        }

        if (imgUrl !== "") {
            bigPhoto.visible = true
            bigPhotoBg.visible = true
            ClipPhoto(bigPhoto, imgUrl, bigPhoto.getContentSize().width / 2, bigPhoto.getContentSize().height / 2, "bigPhoto")
            // bigPhoto.setScale(3)
            actFunc()
        }
    }

    let NewUserInfoLayer = cc.Layer.extend({
        jsBind: {
            block: {
                _layout: [[1, 1], [0.5, 0.5], [0, 0], true,false,true]
            },
            back: {
                _layout: [[949 / 1280, 628 / 720], [0.5, 0.5], [0, 0]],
                _run:function(){ UiInAction(this ); },
                close: {
                    _click: function () {
                        if (jsclient.NewUserInfoLayer) {
                            jsclient.NewUserInfoLayer.removeFromParent(true)
                            delete jsclient.NewUserInfoLayer
                            jsclient.NewUserInfoLayer = null
                        }
                    }
                },
                headImg_bg: {
                    headImg: {
                        _run: function () {
                            // jsclient.loadWxHead(pinfo.uid, pinfo.headimgurl);
                            // jsclient.checkLoadWxHead(this, jsclient.data.wxHeadFileName);

                            var size = this.getContentSize();
                            jsclient.loadClipWxHead(this, pinfo.headimgurl, size.width/2, size.height/2, 100,100);
                        }
                    },
                    headframe_img:{
                        _run: function () {
                            this.visible = false;
                            if (pinfo.headframe && pinfo.headframe.id > 0 && pinfo.headframe.id < 100) {
                                this.loadTexture("res/headFrame/action/head_" + pinfo.headframe.id + ".png");
                                this.visible = true;
                            }
                            else if (jsclient.headframe_use &&jsclient.headframe_use.head_sn > 0 && jsclient.headframe_use.head_sn < 100 && !jsclient.data.sData) {
                                this.loadTexture("res/headFrame/action/head_" + jsclient.headframe_use.head_sn + ".png");
                                this.visible = true;
                            }
                        },
                        _event:{
                            headFrameChoice: function () {
                                if(jsclient.data.sData)
                                    return;
                                cc.log("========更新个人信息界面头像框为=====headimg=========jsclient.headframe_use.head_sn："+jsclient.headframe_use.head_sn);
                                this.visible = false;
                                if (jsclient.headframe_use && jsclient.headframe_use.head_sn > 0 && jsclient.headframe_use.head_sn < 100) {
                                    this.loadTexture("res/headFrame/action/head_" + jsclient.headframe_use.head_sn + ".png");
                                    this.visible = true;
                                }
                            },
                        }
                    },
                    ArmatureNode: {
                        _run:function () {
                            this.visible = false;
                            this.oldY = this.y;
                            cc.log("=================111===jsclient.headframe_use:"+JSON.stringify(jsclient.headframe_use));
                            if (pinfo.headframe && pinfo.headframe.id > 100) {
                                var Frameid = pinfo.headframe.id - 100;
                                cc.log("=================111===jsclient.headframe_use11111:")
                                this.init(Frameid + "");
                                this.getAnimation().playWithIndex(0, -1, 1);
                                this.visible = true;
                                cc.log("=================111===jsclient.headframe_use111112:")
                                setHeadFramePosition(Frameid+"",this,"home");
                                cc.log("=================111===jsclient.headframe_use111113:")
                            }
                            //测试服pinfo没有headframe   佩戴jsclient.headframe_use存储的头像框
                            else if (jsclient.headframe_use && jsclient.headframe_use.head_sn > 100 && !jsclient.data.sData) {
                                var Frameid = jsclient.headframe_use.head_sn - 100;
                                cc.log("=================111===jsclient.headframe_use111114:")
                                this.init(Frameid + "");
                                this.getAnimation().playWithIndex(0, -1, 1);
                                this.visible = true;
                                setHeadFramePosition(Frameid+"",this,"home");
                                cc.log("=================111===jsclient.headframe_use111115:")
                            }
                        },
                        _event:{
                            headFrameChoice: function () {
                                if(jsclient.data.sData)
                                    return;
                                this.visible = false;
                                cc.log("=======更新主界面头像框为==ArmatureNode=====jsclient.headframe_use.head_sn："+jsclient.headframe_use.head_sn);
                                if (jsclient.headframe_use && jsclient.headframe_use.head_sn > 100) {
                                    var Frameid = jsclient.headframe_use.head_sn - 100;
                                    this.init(Frameid + "");
                                    this.getAnimation().playWithIndex(0, -1, 1);
                                    this.visible = true;
                                    setHeadFramePosition(Frameid+"",this,"home");
                                }
                            },
                        }
                    },

                },
                setHeadbg: {
                    _visible:function(){
                        return false;
                    },
                    headframe_remind:{
                        _run: function () { //TODO  headframe_remind_tiyan
                            if(jsclient.headframe_remind == "true" || jsclient.headframe_remind_tiyan == "true"){
                                this.visible = true;
                            }
                            else {
                                this.visible = false;
                            }
                        },
                        _event: {
                            headFrameRemind: function () {
                                if(jsclient.headframe_remind == "true" || jsclient.headframe_remind_tiyan == "true"){
                                    this.visible = true;
                                }
                                else {
                                    this.visible = false;
                                }
                            },
                        }
                    },
                    _run: function () {
                        this.visible = !jsclient.data.sData;
                    },
                    _click: function () {
                        setHeadFrame();

                        if(jsclient.NewUserInfoLayer){
                            jsclient.NewUserInfoLayer.visible = false;
                        }
                    },
                },
                name: {
                    _text: function () {
                        if(!pinfo.nickname && !pinfo.name) return "";
                        return unescape(pinfo.nickname || pinfo.name);
                    }
                },
                money: {
                    _visible: function () {
                        return !jsclient.remoteCfg.hideMoney;
                    },
                    num: {
                        _run: function () {
                            this.freshFreezeMoney = function () {
                                var moneyText = pinfo.money ? pinfo.money : 0;
                                if(pinfo.uid == SelfUid() && jsclient.data.pinfo) {
                                    //自己的信息加上预扣钻显示
                                    var freeze = jsclient.data.pinfo.freeze;
                                    if(freeze && freeze.money) {
                                        moneyText += ("(-" + freeze.money + "预扣)");
                                    }
                                }
                                this.setString(moneyText);
                                this.ignoreContentAdaptWithSize(true);
                            };
                            this.freshFreezeMoney();
                        },
                        _event: {
                            updateFreezeInfo: function () {
                                this.freshFreezeMoney();
                            },
                            updateInfo: function () {
                                this.freshFreezeMoney();
                            }
                        }
                    }
                },
                coin: {
                    _visible: function () {
                        return jsclient.remoteCfg.coinRoom
                    },
                    num: {
                        _run: function () {
                            changeLabelAtals(this, pinfo.coin);
                        },
                        _event: {
                            updateInfo: function () {
                                changeLabelAtals(this, jsclient.data.pinfo.coin);
                            }
                        }
                    },
                },
                ledou: {
                    _visible: function () {
                        return !jsclient.remoteCfg.hideMoney
                    },
                    num: {
                        _run: function () {
                            changeLabelAtals(this, pinfo.ledou);
                        },
                        _event: {
                            updateInfo: function () {
                                changeLabelAtals(this, jsclient.data.pinfo.ledou);
                            }
                        }
                    },
                },
                ID: {
                    _text: function () {
                        if(!pinfo.uid) return "";
                        return unescape(pinfo.uid);
                    }
                },
                IP: {
                    _text: function () {
                        if (pinfo.remoteIP)  return pinfo.remoteIP;
                        else return "";
                    }
                },
                self_lv: {
                    _run: function () {
                        this.visible = true;

                        if (pinfo.noimg) {
                            this.setString("白板1");
                            return
                        }

                        var Player_exp = jsclient.getPlayer_Grade(pinfo.ep);
                        if (Player_exp && Player_exp.lv) {
                            this.setString(unescape("" + Player_exp.lv));
                        } else {
                            this.setString("");
                        }

                    },
                    exp_bg: {
                        _run: function () {

                            if (pinfo.uid != SelfUid()) {
                                this.visible = false;
                            }

                            var Player_exp = jsclient.getPlayer_Grade(pinfo.ep);
                            if (Player_exp && Player_exp.lv && Player_exp.ep >= 0 && Player_exp.currentLv_Ep) {
                                this.getChildByName("jingyan").setString(unescape(Player_exp.ep + " / " + Player_exp.currentLv_Ep));
                                this.getChildByName("jingyantiao").setPercent(Player_exp.ep / Player_exp.currentLv_Ep * 100);
                            } else {
                                this.visible = false;
                            }

                        },
                        jingyan: {
                            _run: function () {
                                if (pinfo.noimg) {
                                    this.visible = false;
                                    return
                                }

                                if (pinfo.uid != SelfUid()) {
                                    this.visible = false;
                                    return;
                                }


                                /*if (checkPlayerAge_18() || jsclient.data.pinfo.uid != pinfo.uid) {  //游戏中不是自己
                                    this.setColor(cc.color(127, 67, 43)); //棕色
                                    return;
                                }*/


                                /*var otime = jsclient.data.pinfo.otime;
                                if (otime >= jsclient.playtime_5) {
                                    this.setColor(cc.color(255, 0, 0)); //红色
                                } else if (otime >= jsclient.playtime_3) {
                                    this.setColor(cc.color(255, 78, 0)); //橙红
                                } else {
                                    this.setColor(cc.color(127, 67, 43));//棕色
                                }*/
                            }

                        }
                    }
                },
                ScrollList: {
                    _run: function() {
                        uiitem_list = this;
                    }
                },
                checkbox_ten_times: {
                    _run: function () {
                        emojiTen_Btn = this;
                        let recordData = sys.localStorage.getItem("emojiTenRecord");
                        if (recordData == jsclient.EmojiType.emoji_ten) {
                            emojiTen_Btn.setSelected(true);
                        }else {
                            emojiTen_Btn.setSelected(false);
                        }
                        if(!jsclient.playui || pinfo.uid == jsclient.data.pinfo.uid) {
                            this.visible = false;
                        }
                    },
                    _check: function (sender, type) {
                        switch (type) {
                            case ccui.CheckBox.EVENT_SELECTED: {
                                sys.localStorage.setItem("emojiTenRecord", jsclient.EmojiType.emoji_ten);
                                break;
                            }
                            case ccui.CheckBox.EVENT_UNSELECTED: {
                                sys.localStorage.setItem("emojiTenRecord", jsclient.EmojiType.emoji_one);
                                break;
                            }
                        }
                        generateItems();
                    },
                    text_ten_times: {
                        _clickAndSetColor: function () {
                            if (emojiTen_Btn.isSelected()) {
                                emojiTen_Btn.setSelected(false)
                                sys.localStorage.setItem("emojiTenRecord", jsclient.EmojiType.emoji_one)
                            } else {
                                emojiTen_Btn.setSelected(true)
                                sys.localStorage.setItem("emojiTenRecord", jsclient.EmojiType.emoji_ten)
                            }
                            generateItems()
                        }
                    }
                },
                animBtn: {
                    _run: function () {
                        uiAnimationItem = this;
                        this.visible = false;
                    }
                },
                total_free_title: {
                    _run: function() {
                        if (!jsclient.playui || pinfo.uid == jsclient.data.pinfo.uid) {
                            this.visible = false
                        }
                    },
                    times:{
                        _run:function () {
                            this.visible = false
                            this.refresh = function () {
                                this.setString(getTotalFreeTimesLeft()+"次")
                                this.ignoreContentAdaptWithSize(true)
                            }
                        },
                        _event: {
                            updateInfo: function() { this.refresh() }
                        }
                    }
                },
                photoPanel: {
                    _visible: false,
                    _run: function () {
                        if (!jsclient.playui) {
                            this.visible = true
                        }
                    },
                    photo_1: {
                        _run: function () {
                            jsclient.native.ShowLogOnJava(" -- getPictureFromPhone -- payimg = " + jsclient.data.pinfo.payimg)
                            let url = ""
                            if (jsclient.data.pinfo.payimg) {
                                url = jsclient.data.pinfo.payimg
                            }
                            this.refresh = function (url, photoPath) {
                                if (!url) {
                                    return
                                }
                                ClipPhoto(this, url, this.getContentSize().width/2, this.getContentSize().height/2, "photoPic", photoPath)
                            }
                            this.refresh(url)
                        },
                        _clickAndSetColor: function () {
                            clickPhotoMagnify(1)
                        },
                        _event:{
                            GET_PHOTO_UPLOADPIC: function (d) {
                                jsclient.unblock()
                                jsclient.native.ShowLogOnJava("-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- d110 = "+JSON.stringify(d))
                                cc.log("-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- d  = "+d)
                                // cc.log("-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- JSON.parse(d) = "+JSON.parse(d))
                                if (photoBtnId !== 1) {
                                    return
                                }
                                if (cc.sys.OS_ANDROID == cc.sys.os) {
                                    d = JSON.parse(d)
                                }
                                if (cc.sys.OS_IOS == cc.sys.os) {
                                    d = JSON.parse(JSON.stringify(d))
                                }
                                cc.log("-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- d.errno  = "+d.errno)
                                if (d && parseInt(d.errno) == 0) {  //成功
                                    cc.log("-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- d.errno222  = "+d.errno)
                                    jsclient.showMsg("图片上传成功！")
                                    // toastMsg("图片上传成功！")
                                    this.refresh(d.payimg, photoPath)
                                } else {    //失败
                                    cc.log("-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- d.errno111  = "+d.errno)
                                    // toastMsg("图片上传失败，请重试！")
                                    jsclient.showMsg("图片上传失败，请重试！")
                                }
                            },
                            updateInfo: function () {
                                cc.log("-- getPictureFromPhone --updateInfo -- jsclient.data.pinfo.payimg  = "+jsclient.data.pinfo.payimg)
                                if (jsclient.data.pinfo.payimg) {
                                    this.refresh(jsclient.data.pinfo.payimg)
                                }
                            }
                        },
                        changeBtn: {
                            _click: function () {
                                clickChangePhoto(1)
                            }
                        },
                    },
                    photo_2: {
                        _run: function () {
                            jsclient.native.ShowLogOnJava(" -- getPictureFromPhone -- payimg1 = " + jsclient.data.pinfo.payimg1)
                            let url = ""
                            if (jsclient.data.pinfo.payimg1) {
                                url = jsclient.data.pinfo.payimg1
                            }
                            this.refresh = function (url, photoPath) {
                                if (!url) {
                                    return
                                }
                                ClipPhoto(this, url, this.getContentSize().width/2, this.getContentSize().height/2, "photoPic", photoPath)
                            }
                            this.refresh(url)
                        },
                        _clickAndSetColor: function () {
                            clickPhotoMagnify(2)
                        },
                        _event:{
                            GET_PHOTO_UPLOADPIC: function (d) {
                                jsclient.unblock()
                                if (photoBtnId !== 2) {
                                    return
                                }
                                if (cc.sys.OS_ANDROID == cc.sys.os) {
                                    d = JSON.parse(d)
                                }
                                if (d && d.errno == 0) {  //成功
                                    jsclient.showMsg("图片上传成功！")
                                    this.refresh(d.payimg, photoPath)
                                } else {    //失败
                                    jsclient.showMsg("图片上传失败，请重试！")
                                }
                            },
                            updateInfo: function () {
                                if (jsclient.data.pinfo.payimg1) {
                                    this.refresh(jsclient.data.pinfo.payimg1)
                                }
                            }
                        },
                        changeBtn: {
                            _click: function () {
                                clickChangePhoto(2)
                            }
                        },
                    },
                    bigPhoto: {
                        _visible: false,
                        _run: function () {
                            bigPhoto = this
                        }
                    },
                    _event:{
                        GET_PHOTO_FROM_ALBUM:function(d) {
                            photoPath = d.photoPath
                            jsclient.native.ShowLogOnJava("-- getPictureFromPhone -- d = " + JSON.stringify(d))
                            jsclient.block()
                            jsclient.native.uploadPic(d.photoPath, uploadUrl[photoBtnId-1], "GET_PHOTO_UPLOADPIC", jsclient.data.pinfo.accessToken)
                        },
                        PIC_SIZE_WARNING:function(d)
                        {
                            jsclient.unblock();
                            jsclient.showMsg("图片较大！请上传1M以下图片，谢谢")
                        }
                    },
                }
            },
            bigPhotoBg: {
                _layout: [[1, 1], [0.5, 0.5], [0, 0], true,false,true],
                _visible: false,
                _run: function () {
                    bigPhotoBg = this
                },
                _clickAndSetColor: function () {
                    bigPhoto.visible = false
                    bigPhotoBg.visible = false
                }
            }
        },
        ctor: function () {
            this._super();
            if (pinfo.uid == jsclient.data.pinfo.uid) {
                pinfo = jsclient.data.pinfo;
            }
            let newuserinfolayer = ccs.load("res/NewUserInfoLayer.json");
            ConnectUI2Logic(newuserinfolayer.node, this.jsBind);
            this.addChild(newuserinfolayer.node);
            jsclient.NewUserInfoLayer = this;

            if(pinfo.uid != jsclient.data.pinfo.uid) {
                jsclient.GetPayAniList(function (rtn) {
                    Log("newUserInfoLayer -- ctor -- GetPayAniList -- rtn = " + JSON.stringify(rtn))
                    let result = rtn.info.filter(function (x) {
                        return x.roomType == GetRoomType();
                    })
                    jsclient.payAniInfo = result;
                    Log("newUserInfoLayer -- ctor -- GetPayAniList -- jsclient.payAniInfo = " + JSON.stringify(jsclient.payAniInfo))
                    totalFreeNum = rtn.AnimeTotalFreeNum || 0
                    today = rtn.day || today
                    updateTotalFreeLeftTimesLabel()
                    generateItems();
                });
            }

            return true;
        },
        onExit:function(){
            this._super();
            jsclient.Node_chengjiu = null;
        },
        clickPlayProp: function (info) {
            // 发送动画消息
            console.log("-------- user prop info " + JSON.stringify(info));
            let playerInfo = jsclient.data.pinfo;
            //本人不能给自己发表情
            if (pinfo.uid == SelfUid()) {
                toastMsg("不能给自己发送表情呦！");
                return;
            }

            if (!jsclient.data.sData) {
                toastMsg("只能在部分玩法对战时使用道具哦！");
                return;
            }

            // 优先使用每日总免
            if (getTotalFreeTimesLeft() > 0) {
                cc.log("---------------- 每日总剩余免费次数大于0 ---------------------")
                let para = getPropParaByInfo(info)
                para.bFree = true
                sendUseProp(para)
                return
            }

            let nums = getPropNums(info.propId)
            let freeTimes = getFreeTimes(info, playerInfo)
            let bHasProps = nums > 0
            let bFree = info.price == 0
            let bHasPropFreeTime = freeTimes < info.freeNum
            if (info.hasOwnProperty("useType") && info.useType == useType.useProp) {
                //判断剩余数量充足或者判断是否为免费的消耗道具的动画，直接播放动画
                cc.log("---------- 免费或者有剩余次数的动画 -----------------")
                if (bHasProps || bFree || bHasPropFreeTime) {
                    let para = getPropParaByInfo(info)
                    if(bHasProps || bFree || bHasPropFreeTime) { para.bFree = true }  // 实时消耗，区分是否免费
                    //TongJi(info, TDENMU.game_userinfo_normal_one)
                    sendUseProp(para);
                }
                else {
                    //剩余数量不足 打开选择购买界面
                    cc.log("剩余数量不足 打开选择购买界面");
                    jsclient.NewUserInfoLayer.addChild(new buyPropLayer(info));
                }
            }
            else {
                if(info.hasOwnProperty("productType")) {
                    if (info.productType == productType.diamond) {
                        cc.log("暂时没有直接消耗钻石播放动画的需求...");
                    } else if(info.productType == productType.ledou) {
                        let hasLedouCnt = (jsclient.data.pinfo.ledou || 0);
                        if (bFree || bHasPropFreeTime || info.price <= hasLedouCnt) {
                            let para = getPropParaByInfo(info)
                            if (bFree || bHasPropFreeTime) { para.bFree = true }
                            sendUseProp(para)
                        } else {
                            if (info.price > hasLedouCnt) {
                                jsclient.showMsg(
                                    "金币不足，无法发送表情，请及时充值！",
                                    function () { jsclient.requestForWebShop(OPTIONTYPE.LEDOU) },
                                    function () {})
                            }
                        }
                    } else if(info.productType == productType.coin) {
                        if(bFree || bHasPropFreeTime || playerInfo.coin >= info.price) {
                            // TongJi(info, TDENMU.game_userinfo_coin_one)
                            let para = getPropParaByInfo(info)
                            if(bFree || bHasPropFreeTime) { para.bFree = true }
                            sendUseProp(para)
                        } else {
                            jsclient.showMsg(
                                "金豆不足，无法发送表情，请及时充值！",
                                function () { jsclient.requestForWebShop(OPTIONTYPE.COIN) },
                                function () {}
                            );
                        }
                    }
                    else {
                        jsclient.showMsg("货币类型错误,表情播放不支持当前配置类型货币")
                    }
                }
                else {
                    jsclient.showMsg("道具缺失货币类型字段,请尝试重新打开界面")
                }
            }
        },
    });
    if (jsclient.NewUserInfoLayer) {
        jsclient.NewUserInfoLayer.removeFromParent(true);
        delete jsclient.NewUserInfoLayer;
    }
    jsclient.Scene.addChild(new NewUserInfoLayer());
}

//=============================头像框=============================

//头像框背包
function setHeadFrame() {

    
    var sData = jsclient.data.sData;
    if(sData){
        jsclient.showMsg("游戏中不能更换头像框！");
        return;
    }
    var head_list = []; //头像框总列表
    var headfree_list = []; //免费头像框列表
    var headpay_list = [];  //收费头像框列表
    var headtiyan_list = [];  //体验头像框列表
    var headframefree_use;  //当前使用的免费的
    var headframepay_use;   //当前使用的收费的
    var headframetiyan_use;   //当前使用的体验的
    var CheckBox_free,CheckBox_pay,CheckBox_tiyan,ScrollView_free,ScrollView_pay,ScrollView_tiyan,Detail_free,Detail_pay,Detail_tiyan,headframe_item;

    var headFrame_count = {};  //买点统计
    var countMax = 1;          //单个头像框可以统计的最大点击数

    initSetHeadFrameLayer();
    function initSetHeadFrameLayer(whenSelectHead_sn) {

        var whenSelect;//当前选中的头像框 用于刷新后还是选中状态
        var data = {  //拉取头像框列表数据
            prefix:"xynmmj",
            method:'list',
            type:-1
        };
        jsclient.block();
        jsclient.gamenet.request("pkcon.handler.ProxyAccess",
            {
                action: "bindGrowth",
                method: "head/sculpture/dispatch",
                data: JSON.stringify(data)
            },
            function (rtn) {
                if(rtn.result == 0){
                    if(rtn.errno == 1){
                        jsclient.ShowToast("暂无头像框！");
                    }
                    else if(rtn.errno == 0){
                        var head_sn_default; //空头相框
                        head_list = rtn.data.head_list;//总头像框列表
                        headfree_list = []; //免费头像框列表
                        headpay_list = [];//收费头像框列表
                        headtiyan_list = [];//体验头像框列表
                        headframefree_use = {}; //当前使用的免费头像框  没有为空
                        headframepay_use = {}	;	//当前使用的收费头像框  没有为空
                        headframetiyan_use = {} ;  //当前使用的体验头像框  没有为空
                        jsclient.headframe_use = null;
                        for(var i=0;i<head_list.length;i++){
                            if(Object.keys(headFrame_count).length != head_list.length -1){
                                if(head_list[i].head_sn != -1){
                                    headFrame_count[head_list[i].head_sn] = 0;
                                }
                            }
                            if(head_list[i].use){
                                jsclient.headframe_use = head_list[i]; //当前使用的头像框
                            }
                            if(head_list[i].head_sn == -1){
                                head_sn_default = head_list[i];  //空头像框
                            }

                            //筛选
                            if(head_list[i].head_sn > 100 && head_list[i].head_sn < 200){
                                headpay_list.push(head_list[i]);     //付费头像框
                            }
                            else if(head_list[i].head_sn < 100){
                                headfree_list.push(head_list[i]);  //免费头像框
                            }
                            // else if(head_list[i].head_sn > 200 && head_list[i].open && head_list[i].own){
                            //     headtiyan_list.push(head_list[i]);  //体验头像框
                            // }
                            //筛选
                            // if(head_list[i].use && head_list[i].head_sn > 200){   //当前使用的是体验的
                            //     headframetiyan_use = head_list[i];
                            // }
                            // else

                            if(head_list[i].use && head_list[i].head_sn > 100){  //当前使用的是收费的
                                headframepay_use = head_list[i];
                            }
                            else if(head_list[i].use && !head_list[i].cost){   //当前使用的是免费的
                                headframefree_use = head_list[i];
                            }

                            if(whenSelectHead_sn && whenSelectHead_sn == head_list[i].head_sn){ //记录购买成功的头像框，刷新后默认选择此头像框
                                whenSelect = head_list[i];
                            }
                            if(head_list[i].head_sn > 210){
                                jsclient.showMsg("头像框配置有误！");
                            }
                            if(head_list[i].head_sn > 110 && head_list[i].head_sn <= 200){
                                jsclient.showMsg("头像框配置有误！");
                            }
                            if(head_list[i].head_sn > 10 && head_list[i].head_sn <= 100){
                                jsclient.showMsg("头像框配置有误！");
                            }
                            if(head_list[i].head_sn != -1 && head_list[i].head_sn < 0){
                                jsclient.showMsg("头像框配置有误！");
                            }
                        }

                        if(!jsclient.headframe_use && head_sn_default && head_sn_default.head_sn == -1) {
                            if(!head_sn_default.open){
                                jsclient.showMsg("请重新登录！");
                                return;
                            }
                            //当前没有头像框，后台配置有空头像框 则头像框佩戴空头像框
                            var data = {
                                prefix: "xynmmj",
                                method: 'choice',
                                head_sn: head_sn_default.head_sn
                            };
                            jsclient.gamenet.request("pkcon.handler.ProxyAccess",
                                {
                                    action: "bindGrowth",
                                    method: "head/sculpture/dispatch",
                                    data: JSON.stringify(data)
                                },
                                function (rtn) {
                                    if (rtn.result == 0 && rtn.errno == 0) {
                                        cc.log("====佩戴空头像框成功=====2======");
                                        jsclient.headframe_use = head_sn_default;
                                        sendEvent("headFrameChoice");
                                        initSetHeadFrameLayer();
                                    }
                                });
                            return;
                        }

                        function compare(index,own,open){
                            return function(a,b){
                                var value1 = a[index];
                                var value2 = b[index];
                                a[own] ? (value1 -= 100) : value1;
                                b[own] ? (value2 -= 100) : value2;
                                a[open] ? (value1 -= 10) : value1;
                                b[open] ? (value2 -= 10) : value2;
                                return value1 - value2;
                            }
                        }
                        headpay_list.sort(compare('index','own','open')); //排序 有使用权的 > 解锁的 > 没解锁的

                        if(Object.keys(headframefree_use).length == 0){
                            headframefree_use = null;
                        }
                        if(Object.keys(headframepay_use).length == 0){
                            headframepay_use = null;
                        }
                        // if(Object.keys(headframetiyan_use).length == 0){
                        //     headframetiyan_use = null;
                        // }

                        if(!jsclient.SetHeadFrameLayer){  //没有去创建
                            jsclient.Scene.addChild(new SetHeadFrameLayer());
                        }
                        else {//有的话去刷新
                            var headframelist_default = checkheadFrameType(whenSelect);
                            cc.log("===============headframelist_default:"+JSON.stringify(headframelist_default));
                            //各个列表刷新后的默认选择项
                            var headfree_list_default = headframelist_default.headfree_list_default;
                            var headpay_list_default = headframelist_default.headpay_list_default;
                            //var headtiyan_list_default = headframelist_default.headtiyan_list_default;

                            initHeadFrameScrollView(ScrollView_free,headfree_list,headfree_list_default);
                            initHeadFrameScrollView(ScrollView_pay,headpay_list,headpay_list_default);
                            //initHeadFrameScrollView(ScrollView_tiyan,headtiyan_list,headtiyan_list_default);

                            initDetailHeadFrame(headfree_list_default);
                            initDetailHeadFrame(headpay_list_default);
                            //initDetailHeadFrame(headtiyan_list_default);
                        }
                    }
                    else {
                        jsclient.showMsg("获取列表失败，请重新尝试！");
                    }
                }
                else {
                    jsclient.showMsg("获取列表失败，请重新尝试！");
                }

                if(jsclient.NewUserInfoLayer){
                    jsclient.NewUserInfoLayer.visible = true;
                }

                jsclient.unblock();
            });
    }
    //购买时whenSelect（当前购买的）才有 默认选择whenSelect  其他情况有headframefree_use（当前使用的）默认选择  否则默认选择列表里边第一个
    function checkheadFrameType(whenSelect) {
        var headfreelist_default = headframefree_use ? headframefree_use : (headfree_list[0] ? headfree_list[0] : "headfree_list");
        var headpaylist_default = headframepay_use ? headframepay_use : (headpay_list[0] ? headpay_list[0] : "headpay_list");
        var headtiyanlist_default = headframetiyan_use ? headframetiyan_use : (headtiyan_list[0] ? headtiyan_list[0] : "headtiyan_list");
        if(whenSelect && whenSelect.head_sn){
            if(whenSelect.head_sn > 200){
                headtiyanlist_default = whenSelect;
            }
            else if(whenSelect.head_sn > 100){
                headpaylist_default = whenSelect;
            }
            else {
                headfreelist_default = whenSelect;
            }
        }
        var headframelist_default = {
            headfree_list_default:headfreelist_default,
            headpay_list_default:headpaylist_default,
            headtiyan_list_default:headtiyanlist_default
        }
        return headframelist_default;
    }
    //加载头像框
    function BindHeadFrameItem(item,headFrameOne,whenSelect) {
        item.setTouchEnabled(true);
        var bind = {
            _run:function () {
                if(headFrameOne.head_sn > 100){
                    var size = item.getContentSize();
                    var wolfAnimation_name = headFrameOne.head_sn % 100;
                    var wolfAnimation = new ccs.Armature(wolfAnimation_name+"");
                    wolfAnimation.setScale(1.0);
                    wolfAnimation.setPosition(size.width/2, size.height/2);
                    wolfAnimation.getAnimation().playWithIndex(0, -1, 1);
                    this.addChild(wolfAnimation);
                }
                this.head_sn = headFrameOne.head_sn;
            },
            headframe_img:{
                _run:function () {
                    if(!headFrameOne.cost && headFrameOne.head_sn != -1 && headFrameOne.head_sn < 100){
                        this.visible = true;
                        this.loadTexture("res/aress/home/HeadFrameAction/head_" + headFrameOne.head_sn + ".png");
                    }
                    else {
                        this.visible = false;
                    }
                }
            },
            Image_unlock: {
                _run: function () {
                    //已经解锁 并且是收费的
                    if (headFrameOne.open && headFrameOne.head_sn > 100) {
                        if(headFrameOne.own){ //如果没有购买 显示解锁标识 表明已经解锁但是未拥有
                            this.visible = false;
                        }
                        else {
                            this.visible = true;
                        }
                    }
                    else {
                        this.visible = false;
                    }
                }
            },
            tiyan:{
                _run: function () {
                    //已经解锁 并且是收费的
                    if (headFrameOne.own && headFrameOne.head_sn > 200 && headFrameOne.head_sn < 300 && headFrameOne.forever != 1) {
                        this.visible = true;
                        this.zIndex = 500;
                    }
                    else {
                        this.visible = false;
                    }
                }
            },
            now_user_kuang:{
                _run:function () {
                    if(headFrameOne.use){
                        this.visible = true;
                    }
                    else {
                        this.visible = false;
                    }
                }
            },
            select_img:{
                _run:function () {
                    this.visible = false;
                    this.zIndex = 600;
                }
            },
            now_user_bg:{
                _run:function () {
                    if(headFrameOne.use){
                        this.visible = true;
                    }
                    else {
                        this.visible = false;
                    }
                    this.zIndex = 1000;
                }
            },
            Image_lock:{
                _run:function () {
                    if(headFrameOne.open){
                        this.visible = false;
                    }
                    else {
                        this.visible = true;
                    }
                    this.zIndex = 1000;
                }
            },
            _click: function () {
                for(var key in headFrame_count){ //买点统计
                    if(key == item.head_sn && headFrame_count[key] < countMax){
                        headFrame_count[key] ++;
                    }
                }
                var Childs = item.getParent().getChildren();
                initDetailHeadFrame(headFrameOne);
                for(var i=0;i<Childs.length;i++){
                    if(Childs[i].head_sn == headFrameOne.head_sn){
                        Childs[i].getChildByName("select_img").visible = true;
                    }
                    else {
                        Childs[i].getChildByName("select_img").visible = false;
                    }
                }
            }
        };
        ConnectUI2Logic(item, bind);
    }
    function getHeadFrame_name(head_sn,getGrade) {  //获得对应的等级或头像框名称
        var head_sn0 = head_sn;
        if(head_sn > 200){
            head_sn0 -= 100;
        }
        if(getGrade && head_sn > 100){  //获得对应的等级
            head_sn0 -= 100;
        }
        switch (head_sn0) {
            case -1:
                return "无";
                break;
            case 1:
                return "白板3";
                break;
            case 2:
                return "一万1";
                break;
            case 3:
                return "二万1";
                break;
            case 4:
                return "四万1";
                break;
            case 5:
                return "五万1";
                break;
            case 6:
                return "六万1";
                break;
            case 7:
                return "七万1";
                break;
            case 8:
                return "八万1";
                break;
            case 9:
                return "九万1";
                break;
            case 10:
                return "雀神";
                break;
            case 101:
                return "金龙圣冠";
                break;
            case 102:
                return "财神元宝";
                break;
            case 103:
                return "暮光星灵";
                break;
            case 104:
                return "招财喵";
                break;
            case 105:
                return "冰涛巨龙";
                break;
            case 106:
                return "初晨之炎";
                break;
            case 107:
                return "金币辉煌";
                break;
            case 108:
                return "恋心";
                break;
            case 109:
                return "至尊罗刹";
                break;
            case 110:
                return "金雀展翅";
                break;
        }
    }
    function initDetailHeadFrame(headFrameOne) {


        if(!headFrameOne)
            return;
        // if(headFrameOne.head_sn > 200 || headFrameOne == "headtiyan_list"){
        //     var Childrens = Detail_tiyan.getChildren();
        // }
        // else
        if(headFrameOne.head_sn > 100 || headFrameOne == "headpay_list"){
            var Childrens = Detail_pay.getChildren();
        }
        else {
            var Childrens = Detail_free.getChildren();
        }
        if(!headFrameOne.head_sn){
            for(var i=0;i<Childrens.length;i++){
                Childrens[i].visible = false;
            }
            return;
        }
        for(var i=0;i<Childrens.length;i++){
            switch (Childrens[i].name) {
                case "head_bg":
                    Childrens[i].visible = true;
                    if(headFrameOne.head_sn > 100) {
                        var headFrameOne_name = headFrameOne.head_sn % 100;
                        console.log("headFrameOne_name==========="+headFrameOne_name);
                        Childrens[i].getChildren()[0].visible = true;
                        //这里可能崩溃要校对下 TODO:::
                        Childrens[i].getChildren()[0].init(headFrameOne_name + "");
                        Childrens[i].getChildren()[0].getAnimation().playWithIndex(0, -1, 1);
                    }
                    else if(headFrameOne.head_sn > 0  && !headFrameOne.cost){
                        Childrens[i].getChildByName("headframe_img").visible = true;
                        Childrens[i].getChildByName("headframe_img").loadTexture("res/aress/home/HeadFrameAction/head_" + headFrameOne.head_sn + ".png");
                    }
                    else {
                        Childrens[i].getChildByName("headframe_img").visible = false;
                    }
                    break;
                case "head_name":
                    Childrens[i].visible = true;
                    Childrens[i].setString(headFrameOne.desc+"头像框");
                    break;
                case "money_bg":
                    Childrens[i].visible = false;
                    if(headFrameOne.cost && !headFrameOne.own){
                        Childrens[i].getChildByName("text").setString(headFrameOne.cost);
                        Childrens[i].visible = true;
                    }
                    break;
                case "text_term":
                    Childrens[i].visible = true;
                    if (headFrameOne.expiry_time && headFrameOne.expiry_time > 0) {
                        var timeStr = headFrameOne.expiry_time/(60 * 60 * 24);//天  头像框期限倒计时  以秒为单位
                        var timeStr_int = parseInt(timeStr);
                    }
                    // cc.log("==========================timeStr:"+timeStr);
                    // cc.log("==========================headFrameOne.expiry_time:"+headFrameOne.expiry_time);
                    if(headFrameOne.forever == 1){
                        Childrens[i].setString("使用期限：永久");
                    }
                    else {
                        if(!headFrameOne.own && headFrameOne.expiry_data){
                            Childrens[i].setString("使用期限："+headFrameOne.expiry_data + "天");
                        }
                        else if(timeStr){
                            if(timeStr_int == 0){
                                Childrens[i].setString("剩余不足1天");
                                //Childrens[i].setString(headFrameOne.expiry_time+"秒"); //测试专用
                            }
                            else {
                                Childrens[i].setString("剩余"+timeStr_int + "天");
                            }
                        }
                        else {
                            Childrens[i].setString("永久免费");
                        }
                    }
                    break;
                case "text_addition":
                    if(headFrameOne.gain){
                        Childrens[i].setString("经验加成："+headFrameOne.gain + "%");
                        Childrens[i].visible = true;
                    }
                    else {
                        Childrens[i].visible = false;
                    }
                    break;
                case "text_unlock":
                    if(!headFrameOne.open){
                        Childrens[i].setString("等级达到"+getHeadFrame_name(headFrameOne.head_sn,true) + "\n" + "可解锁此头像框");
                        Childrens[i].visible = true;
                    }
                    else {
                        Childrens[i].visible = false;
                    }
                    break;
                case "Button_buy":
                    if(headFrameOne.cost && !headFrameOne.own && headFrameOne.open){
                        Childrens[i].visible = true;
                        Childrens[i].addTouchEventListener(
                            function (btn, eT) {
                                if (eT == 2) {
                                    jsclient.block();
                                    headFrame_buy(headFrameOne.head_sn);
                                }
                            },
                            Childrens[i]
                        );
                    }
                    else {
                        Childrens[i].visible = false;
                    }
                    break;
                case "Button_use":
                    if(headFrameOne.own && !headFrameOne.use){
                        Childrens[i].visible = true;
                        Childrens[i].addTouchEventListener(
                            function (btn, eT) {
                                if (eT == 2) {
                                    jsclient.block();
                                    headFrame_use(headFrameOne);
                                }
                            },
                            Childrens[i]
                        );
                    }
                    else {
                        Childrens[i].visible = false;
                    }
                    break;
            }
        }
    }
    function headFrame_buy(head_sn) {
        cc.log("=========headFrame_buy===========head_sn:"+head_sn);
        var data = {
            prefix: "xynmmj",
            method: 'buy',
            head_sn: head_sn
        };
        jsclient.gamenet.request("pkcon.handler.ProxyAccess",
            {
                action: "bindGrowth",
                method: "head/sculpture/dispatch",
                data: JSON.stringify(data)
            },
            function (rtn) {
                mylog("========buy==========rtn:" + JSON.stringify(rtn));
                if (rtn.result == 0 && rtn.errno == 0) {
                    jsclient.ShowToast("购买成功");
                    initSetHeadFrameLayer(head_sn);
                    //TODO 成就 begin
                    var obj = {};
                    obj["headerimg_count"] = 1;
                    inPutChengJiuInfo(obj);
                    //TODO 成就 end
                }
                else if(rtn.errno == 2){
                    jsclient.showMsg("钻石不足，请联系客服购买钻石");
                }
                else{
                    jsclient.showMsg("购买失败，请重新尝试");
                }
                jsclient.unblock();
            });
    }
    function headFrame_use(headFrameOne) {
        var data = {
            prefix:"xynmmj",
            method:'choice',
            head_sn:headFrameOne.head_sn
        };
        jsclient.gamenet.request("pkcon.handler.ProxyAccess",
            {
                action: "bindGrowth",
                method: "head/sculpture/dispatch",
                data: JSON.stringify(data)
            },
            function (rtn) {
                cc.log("======_click===========headFrameOne.head_sn:"+headFrameOne.head_sn);
                cc.log("==================rtn:" + JSON.stringify(rtn));
                if (rtn.result == 0 && rtn.errno == 0) {
                    jsclient.ShowToast("使用成功");
                    jsclient.headframe_use = headFrameOne;
                    initSetHeadFrameLayer(headFrameOne.head_sn);
                    sendEvent("headFrameChoice");
                }
                else if(rtn.errno == 3){
                    jsclient.ShowToast("头像框已过期，请佩戴其他头像框");
                    initSetHeadFrameLayer();
                }
                else {
                    jsclient.ShowToast("使用失败，请重新尝试");
                }
                jsclient.unblock();
            });
    }
    //重置滚动层
    function initHeadFrameScrollView(ScrollViewNode,headframelist,whenSelect) {
        var count = headframelist.length;
        ScrollViewNode.removeAllChildren();
        //因为select_img的尺寸比headframe_item的大  所以用select_img的尺寸计算位置
        var select_img =  headframe_item.getChildByName("select_img");
        var itemScale = headframe_item.originScale * 1.0 / ScrollViewNode.getScale();
        cc.log("================itemScale:"+itemScale);
        headframe_item.setScale(itemScale);
        var innerContainer = ScrollViewNode.getInnerContainer();
        var innerSize = innerContainer.getContentSize();
        var itemSize = cc.size(select_img.getContentSize().width * itemScale, select_img.getContentSize().height * itemScale);
        headframe_item.setAnchorPoint(0, 1);  //修改锚点方便排位置
        var rowMargin = 11;                         //行间距
        var colMargin = 5;                          //列间距
        var rowNum = 4;                             //一行放几个
        var colNum = Math.ceil(count / rowNum);     //有多少行

        var totalHeight = itemSize.height * colNum + colMargin * (colNum - 1);
        var totalWidth = innerSize.width ;
        if(totalHeight < innerSize.height) {
            totalHeight = innerSize.height;
        }
        innerContainer.setContentSize(totalWidth, totalHeight);

        // cc.log("tableDataList == " + JSON.stringify(tableDataList));
        for(var index=0;index < headframelist.length;index++){
            var item = headframe_item.clone();

            item.visible = true;
            ScrollViewNode.addChild(item);

            BindHeadFrameItem(item,headframelist[index],whenSelect);
            var rowIndex = Math.floor(index / rowNum);
            var colIndex = index % rowNum;

            var posX =  (itemSize.width + rowMargin) * colIndex + rowMargin;

            var posY = totalHeight - (itemSize.height + colMargin) * rowIndex - colMargin;
            item.setPosition(posX, posY);
        }
        if(whenSelect && whenSelect.head_sn){
            var Childs = ScrollViewNode.getChildren();
            for(var i=0;i<Childs.length;i++){
                if(Childs[i].head_sn == whenSelect.head_sn){
                    Childs[i].getChildByName("select_img").visible = true;
                }
                else {
                    Childs[i].getChildByName("select_img").visible = false;
                }
            }
        }
        ScrollViewNode.scrollToTop(0, false);
    }
    function setHeadFrameCheckBox(node,selected) {
        cc.log("======setHeadFrameCheckBox========");
        switch (node.getName()) {
            case "CheckBox_pay":
                CheckBox_pay.setSelected(selected);
                CheckBox_free.setSelected(!selected);
                //CheckBox_tiyan.setSelected(!selected);
                ScrollView_pay.visible = selected;
                ScrollView_free.visible = !selected;
                //ScrollView_tiyan.visible = !selected;
                Detail_pay.visible = selected;
                Detail_free.visible = !selected;
                //Detail_tiyan.visible = !selected;
                break;
            case "CheckBox_free":
                CheckBox_pay.setSelected(!selected);
                //CheckBox_tiyan.setSelected(!selected);
                CheckBox_free.setSelected(selected);
                ScrollView_pay.visible = !selected;
                //ScrollView_tiyan.visible = !selected;
                ScrollView_free.visible = selected;
                Detail_pay.visible = !selected;
                //Detail_tiyan.visible = !selected;
                Detail_free.visible = selected;
                break;
            case "CheckBox_tiyan":
                CheckBox_pay.setSelected(!selected);
                //CheckBox_tiyan.setSelected(selected);
                CheckBox_free.setSelected(!selected);
                ScrollView_pay.visible = !selected;
                //ScrollView_tiyan.visible = selected;
                ScrollView_free.visible = !selected;
                Detail_pay.visible = !selected;
                //Detail_tiyan.visible = selected;
                Detail_free.visible = !selected;
                break;
        }
    }

    var SetHeadFrameLayer = cc.Layer.extend({
        jsBind: {
            block: {
                _layout: [[1, 1], [0.5, 0.5], [0, 0], true,false,true]
            },
            back: {
                _layout: [[1205/1280, 682/720], [0.5, 0.5], [0, 0]],
                _run:function(){ UiInAction(this ); },
                close: {
                    _click: function () {
                        jsclient.SetHeadFrameLayer.removeFromParent(true);
                        delete jsclient.SetHeadFrameLayer;

                        if(jsclient.NewUserInfoLayer){
                            jsclient.NewUserInfoLayer.visible = true;
                        }
                    }
                },
                headframe_item:{
                    _visible: false,
                    _run: function() {
                        headframe_item = this;
                        this.setTouchEnabled(false);
                        this.originScale = this.getScale();
                    },
                    now_user_kuang:{

                    },
                    select_img:{

                    },
                    now_user_bg:{

                    },
                    Image_lock:{

                    }
                },
                other_bg:{
                    CheckBox_free:{
                        headframe_remind:{
                            _run: function () {
                                if(jsclient.headframe_remind && jsclient.headframe_remind == "true" && !headframefree_use){
                                    this.visible = true;
                                }
                                else {
                                    this.visible = false;
                                }
                            },
                            _event: {
                                headFrameRemind: function () {
                                    if(jsclient.headframe_remind && jsclient.headframe_remind == "true"){
                                        this.visible = true;
                                    }
                                    else {
                                        this.visible = false;
                                    }
                                },
                            }
                        },
                        _run: function () {
                            CheckBox_free = this;
                        },
                        _check: function (sender, type) {
                            switch (type) {
                                case ccui.CheckBox.EVENT_SELECTED:
                                    if(CheckBox_free.getChildByName("headframe_remind").visible){
                                        jsclient.headframe_remind = "false";
                                        sys.localStorage.setItem("headframe_remind", jsclient.headframe_remind);
                                        sendEvent("headFrameRemind");
                                    }
                                    setHeadFrameCheckBox(CheckBox_free,true);
                                    break;
                                case ccui.CheckBox.EVENT_UNSELECTED:
                                    setHeadFrameCheckBox(CheckBox_free,true);
                                    break;
                            }
                        }
                    },
                    CheckBox_pay:{
                        headframe_remind:{
                            _run: function () {
                                if(jsclient.headframe_remind && jsclient.headframe_remind == "true" && !headframepay_use){
                                    this.visible = true;
                                }
                                else {
                                    this.visible = false;
                                }
                            },
                            _event: {
                                headFrameRemind: function () {
                                    if(jsclient.headframe_remind && jsclient.headframe_remind == "true"){
                                        this.visible = true;
                                    }
                                    else {
                                        this.visible = false;
                                    }
                                },
                            }
                        },
                        _run: function () {
                            CheckBox_pay = this;
                        },
                        _check: function (sender, type) {
                            switch (type) {
                                case ccui.CheckBox.EVENT_SELECTED:
                                    if(CheckBox_pay.getChildByName("headframe_remind").visible){
                                        jsclient.headframe_remind = "false";
                                        sys.localStorage.setItem("headframe_remind", jsclient.headframe_remind);
                                        sendEvent("headFrameRemind");
                                    }
                                    setHeadFrameCheckBox(CheckBox_pay,true);
                                    break;
                                case ccui.CheckBox.EVENT_UNSELECTED:
                                    setHeadFrameCheckBox(CheckBox_pay,true);
                                    break;
                            }
                        }
                    },

                    ScrollView_free:{
                        _run: function () {
                            ScrollView_free = this;
                        },
                    },
                    ScrollView_pay:{
                        _run: function () {
                            ScrollView_pay = this;
                        },
                    },
                    /*ScrollView_tiyan:{
                        _run: function () {
                            ScrollView_tiyan = this;
                        },
                    }*/
                },
                Detail_free:{
                    _run: function () {
                        Detail_free = this;
                    },
                    head_bg:{
                        _run: function() {
                            this.name = "head_bg";
                        },
                        headframe_img:{
                            _run: function() {
                            },
                        }
                    },
                    head_name:{
                        _run: function() {
                            this.name = "head_name";
                        },
                    },
                    text_term:{
                        _run: function() {
                            this.name = "text_term";
                        },
                    },
                    text_unlock:{
                        _run: function() {
                            this.name = "text_unlock";
                        },
                    },
                    Button_use:{
                        _run: function() {
                            this.name = "Button_use";
                        },
                    }
                },
                Detail_pay:{
                    _run: function () {
                        Detail_pay = this;
                    },
                    head_bg:{
                        _run: function() {
                            this.name = "head_bg";

                        },
                    },
                    head_name:{
                        _run: function() {
                            this.name = "head_name";
                        },
                    },
                    money_bg:{
                        _run: function() {
                            this.name = "money_bg";
                        }
                    },
                    text_term:{
                        _run: function() {
                            this.name = "text_term";
                        },
                    },
                    text_addition:{
                        _run: function() {
                            this.name = "text_addition";
                        },
                    },
                    text_unlock:{
                        _run: function() {
                            this.name = "text_unlock";
                        },
                    },
                    Button_buy:{
                        _run: function() {
                            this.name = "Button_buy";
                        },
                    },
                    Button_use:{
                        _run: function() {
                            this.name = "Button_use";
                        },
                    }
                },
                /*Detail_tiyan:{
                    _run: function () {
                        Detail_tiyan = this;
                    },
                    head_bg:{
                        _run: function() {
                            this.name = "head_bg";
                        },
                        ArmatureNode:{

                        }
                    },
                    head_name:{
                        _run: function() {
                            this.name = "head_name";
                        },
                    },
                    text_term:{
                        _run: function() {
                            this.name = "text_term";
                        },
                    },
                    text_addition:{
                        _run: function() {
                            this.name = "text_addition";
                        },
                    },
                    Button_use:{
                        _run: function() {
                            this.name = "Button_use";
                        },
                    }
                }*/
            }
        },
        ctor: function () {
            //等级头像框
            this._super();
            var setheadframelayer = ccs.load("res/SetHeadFrameLayer.json");
            ConnectUI2Logic(setheadframelayer.node, this.jsBind);
            this.addChild(setheadframelayer.node);
            jsclient.SetHeadFrameLayer = this;

            //买点统计  30秒多次点击算一次  countMax为当前时间可以点击的最大次数
            var limitTime = 0;
            var toSetMaxCount = function () {
                if(limitTime >= 30*countMax){
                    countMax ++;
                }
                limitTime ++;
            };
            jsclient.SetHeadFrameLayer.schedule(toSetMaxCount, 1);

            var headframelist_default = checkheadFrameType();
            //各个列表刷新后的默认选择项
            var headfree_list_default = headframelist_default.headfree_list_default;
            var headpay_list_default = headframelist_default.headpay_list_default;
            var headtiyan_list_default = headframelist_default.headtiyan_list_default;

            console.log("111111111111111111111111111111111121")
            initDetailHeadFrame(headfree_list_default);	//初始化各个标签下边的默认选项
            console.log("111111111111111111111111111111111131")
            initDetailHeadFrame(headpay_list_default);
            console.log("111111111111111111111111111111111141")
            //initDetailHeadFrame(headtiyan_list_default);

            initHeadFrameScrollView(ScrollView_free,headfree_list,headfree_list_default);  //初始化ScrollView
            console.log("111111111111111111111111111111111151")
            initHeadFrameScrollView(ScrollView_pay,headpay_list,headpay_list_default);
            console.log("111111111111111111111111111111111161")
            //initHeadFrameScrollView(ScrollView_tiyan,headtiyan_list,headtiyan_list_default);

            if(headframepay_use){
                console.log("111111111111111111111111111111111171")
                setHeadFrameCheckBox(CheckBox_pay,true); //初始化标签
                console.log("111111111111111111111111111111111181")
            }
            else if(headframefree_use){
                console.log("111111111111111111111111111111111191")
                setHeadFrameCheckBox(CheckBox_free,true);//初始化标签
                console.log("1111111111111111111111111111111111911")
            }
            /*else if(headframetiyan_use){
                setHeadFrameCheckBox(CheckBox_tiyan,true);//初始化标签
            }*/
            return true;
        }
    });
}
//升级和解锁头像框动画
var showUpGradeLayer = cc.Layer.extend({
    jsBind: {
        block: {
            _layout: [[1, 1], [0.5, 0.5], [0, 0], true]
        },
        back1: {
            _layout: [[1, 1], [0.5, 0.5], [0, 0]],
            _run:function () {
                this.visible = false;
                if(jsclient.showUpGradeLayer.upGrade && !jsclient.showUpGradeLayer.upGrade.head_sn_free){
                    this.visible = true;
                    this.getChildByName("text").setString(jsclient.showUpGradeLayer.upGrade.lv);
                    var back1 = this;
                    var delay = cc.delayTime(1);
                    var callback = cc.callFunc(function () {
                        back1.addTouchEventListener(
                            function (btn, eT) {
                                if (eT == 2) {
                                    jsclient.showUpGradeLayer.removeFromParent(true);
                                    delete jsclient.showUpGradeLayer;
                                }
                            },
                            back1
                        );
                    }, this);
                    this.runAction(cc.sequence(delay, callback));
                }
            },
            ArmatureNode:{

            }
        },
        back2: {
            _layout: [[1, 1], [0.5, 0.5], [0, 0]],
            _run:function () {
                var back2 = this;
                this.visible = false;
                if(jsclient.showUpGradeLayer.upGrade && jsclient.showUpGradeLayer.upGrade.head_sn_free) {
                    this.visible = true;
                    this.getChildByName("text").setString(jsclient.showUpGradeLayer.upGrade.lv);
                    this.getChildByName("headframe_img").loadTexture("res/aress/home/HeadFrameAction/head_" + jsclient.showUpGradeLayer.upGrade.head_sn_free[0] + ".png");
                    var delay = cc.delayTime(1);
                    var callback = cc.callFunc(function () {
                        back2.addTouchEventListener(
                            function (btn, eT) {
                                if (eT == 2) {
                                    jsclient.showUpGradeLayer.removeFromParent(true);
                                    delete jsclient.showUpGradeLayer;
                                }
                            },
                            back2
                        );
                    }, this);
                    this.runAction(cc.sequence(delay, callback));
                }
            },
            headframe_Armature:{
                _run:function () {
                    if(jsclient.showUpGradeLayer.upGrade && jsclient.showUpGradeLayer.upGrade.head_sn_pay) {
                        var name = jsclient.showUpGradeLayer.upGrade.head_sn_pay[0] - 100;
                        this.init(name);
                        this.getAnimation().playWithIndex(0, -1, 1);

                    }
                }
            },
            ArmatureNode:{
            }
        }
    },
    ctor: function (upGrade) {
        //等级头像框
        this._super();
        jsclient.showUpGradeLayer = this;
        jsclient.showUpGradeLayer.upGrade = upGrade;
        var showUpGradeLayer = ccs.load("res/UpGradeLayer.json");
        ConnectUI2Logic(showUpGradeLayer.node, this.jsBind);
        this.addChild(showUpGradeLayer.node);

    }
});

//检测头像框是否解锁并解锁、检测头像框是否过期和展示解锁动画
function checkHeadFrameOpen(d) { //有参数d表示是结算加经验时更新头像框列表   只在重连进入home界面或者加完经验后去调用此方法

    if(jsclient.ingame && !d){ //jsclient.ingame是玩家重连时检测玩家是否在游戏中的标志  游戏中不检测玩家头像框是否过期 游戏结束加完经验后去检测
        return;
    }
    var pinfo = jsclient.data.pinfo;
    // //获取列表
    var data = {
        prefix:"xynmmj",
        method:'list',
        type:-1
    };
    jsclient.gamenet.request("pkcon.handler.ProxyAccess",
        {
            action: "bindGrowth",
            method: "head/sculpture/dispatch",
            data: JSON.stringify(data)
        },
        function (rtn) {
            if(rtn.result == 0 && rtn.errno == 0) {
                var waitOpen = [];    //待解锁头像框
                var UnlockArray = []; //已经解锁头像框
                var head_list = rtn.data.head_list;  //头像框列表
                var head_sn_default; //空头相框
                jsclient.headframe_use = null;//当前佩戴的头像框  因为pl里边佩戴的头像框内网获取不到  所以在头像框列表里边查找到佩戴头像框存在jsclient.headframe_use里边

                for (var i = 0; i < head_list.length; i++) {
                    if (pinfo.ep >= head_list[i].grade && !head_list[i].open && head_list[i].head_sn < 200) {//是否有新解锁头像框（正常在玩家升级播放解锁动画的时候解锁，防止玩家升级时不在线造成没有解锁的情况）  解锁头像框和解锁动画同步
                        //体验头像框不需要解锁
                        waitOpen.push(head_list[i].head_sn);
                    }
                    if (head_list[i].use) {
                        jsclient.headframe_use = head_list[i]; //当前使用的头像框
                    }
                    if (head_list[i].head_sn == -1) {
                        head_sn_default = head_list[i];
                    }
                    if(head_list[i].open){
                        UnlockArray.push(head_list[i]);
                    }
                }
                cc.log("========当前使用的头像框===========jsclient.headframe_use:" + jsclient.headframe_use);

                if (waitOpen.length >= 1) {
                    cc.log("需要解锁的头像框id：" + waitOpen);
                    var index_num = 0;//确保最后一个头像框解锁完成才去弹出解锁头像框动画
                    for (var index = 0; index < waitOpen.length; index++) {
                        cc.log("需要解锁的头像框index：" + index + "  head_sn:" + waitOpen[index]);
                        var data = {
                            prefix: "xynmmj",
                            method: 'open',
                            head_sn: waitOpen[index]
                        };
                        jsclient.gamenet.request("pkcon.handler.ProxyAccess",
                            {
                                action: "bindGrowth",
                                method: "head/sculpture/dispatch",
                                data: JSON.stringify(data)
                            },
                            function (rtn) {
                                if (rtn.result == 0 && rtn.errno == 0) {
                                    index_num ++;
                                    cc.log("解锁成功：");
                                    //解锁头像框小红点提醒专用
                                    if ((!jsclient.headframe_remind || jsclient.headframe_remind == "false") && waitOpen.length > 1) {
                                        jsclient.headframe_remind = "true";
                                        sys.localStorage.setItem("headframe_remind", jsclient.headframe_remind);
                                        sendEvent("headFrameRemind");
                                    }
                                    // cc.log("==================waitOpen.length:"+waitOpen.length);
                                    // cc.log("==================waitOpen.length:"+waitOpen);
                                    // cc.log("==================index_num:"+index_num);
                                    if(waitOpen.length >= 2 && waitOpen.length == index_num){ //校验是否要开启解锁头像框动画
                                        var upGrade_object = {
                                            lv: jsclient.getPlayer_Grade(jsclient.data.pinfo.ep).lv,
                                            head_sn_free:[],
                                            head_sn_pay:[]
                                        };
                                        for(var j=0;j < waitOpen.length;j++){
                                            if(waitOpen[j] > 100){
                                                upGrade_object.head_sn_pay.push(waitOpen[j]);
                                            }
                                            else if(waitOpen[j] > 0) {
                                                upGrade_object.head_sn_free.push(waitOpen[j]);
                                            }
                                        }
                                        cc.log("=========================upGrade_object:"+JSON.stringify(upGrade_object));
                                        //开启解锁头像框动画
                                        if(upGrade_object.head_sn_pay.length == 1 && upGrade_object.head_sn_free.length == 1){
                                            if(!jsclient.showUpGradeLayer){
                                                jsclient.Scene.addChild(new showUpGradeLayer(upGrade_object));
                                            }
                                        }
                                    }
                                }
                            });
                    }
                }
                else {
                    if(d && d.lv){ //添加经验的时候 如果玩家升级了显示升级动画
                        var upGrade_object = {
                            lv: jsclient.getPlayer_Grade(jsclient.data.pinfo.ep).lv
                        }
                        if(!jsclient.showUpGradeLayer){
                            jsclient.Scene.addChild(new showUpGradeLayer(upGrade_object));
                        }
                    }
                }
                if (jsclient.headframe_use) {
                    if(!jsclient.data.pinfo.headFrame) { //测试服没有pinfo.headframe 在这里用headframe_use佩戴
                        sendEvent("headFrameChoice");
                    }
                }
                else if (head_sn_default && head_sn_default.head_sn == -1) {
                    //当前没有头像框（新用户或者佩戴头像框过期），后台配置有空头像框 则头像框佩戴空头像框
                    if (!head_sn_default.open) {  //检查空头像框是否已解锁
                        var data = {
                            prefix: "xynmmj",
                            method: 'open',
                            head_sn: head_sn_default.head_sn
                        };
                        jsclient.gamenet.request("pkcon.handler.ProxyAccess",
                            {
                                action: "bindGrowth",
                                method: "head/sculpture/dispatch",
                                data: JSON.stringify(data)
                            },
                            function (rtn) {
                                if (rtn.result == 0 && rtn.errno == 0) {
                                    cc.log("解锁成功：尝试佩戴空头像框");
                                    var data = {
                                        prefix: "xynmmj",
                                        method: 'choice',
                                        head_sn: head_sn_default.head_sn
                                    };
                                    jsclient.gamenet.request("pkcon.handler.ProxyAccess",
                                        {
                                            action: "bindGrowth",
                                            method: "head/sculpture/dispatch",
                                            data: JSON.stringify(data)
                                        },
                                        function (rtn) {
                                            if (rtn.result == 0 && rtn.errno == 0) {
                                                jsclient.headframe_use = head_sn_default;
                                                sendEvent("headFrameChoice");
                                                cc.log("====佩戴空头像框成功=1==========");
                                            }
                                        });
                                }
                            });
                    }
                    else {
                        var data = {
                            prefix: "xynmmj",
                            method: 'choice',
                            head_sn: head_sn_default.head_sn
                        };
                        jsclient.gamenet.request("pkcon.handler.ProxyAccess",
                            {
                                action: "bindGrowth",
                                method: "head/sculpture/dispatch",
                                data: JSON.stringify(data)
                            },
                            function (rtn) {
                                if (rtn.result == 0 && rtn.errno == 0) {
                                    jsclient.headframe_use = -1;
                                    sendEvent("headFrameChoice");
                                    cc.log("====佩戴空头像框成功=====2======");
                                }
                            });
                    }

                    if(UnlockArray.length >= 1){
                        jsclient.ShowToast("您当前佩戴的头像框已过期！");
                    }

                }
            }
            else if(rtn.result == 0 && rtn.errno == 1 && rtn.errmsg == "暂无头像框"){
                jsclient.ShowToast("暂无头像框");
            }
            else {
                // jsclient.ShowToast("请稍后再试");
            }
        });
}
//=============================end=============================
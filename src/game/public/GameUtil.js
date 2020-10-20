
load('game/public/GameUtil',function () {
    let GameUtil = {}
    let GameConfig = include('game/config/GameConfig')
    let LocalSave = include('game/public/LocalSave')
    let ResConfig = include('game/config/ResConfig')
    GameUtil.getAni = function (aniInfo) {
            let ani = sp.SkeletonAnimation.createWithJsonFile(aniInfo.json, aniInfo.atlas)
            ani.update(0)
            if (aniInfo.autoRemove) {
                ani.setCompleteListener(function(){
                    ani.scheduleOnce(function(){
                        ani.removeFromParent();
                        cc.log( "[EffectMgr] " +  effect_name + " remove complete !!!" );
                    },0);
                });
            }
            return ani
        }

    GameUtil.getAniExport = function (aniInfo) {
        if (typeof aniInfo.png === 'string') {
            ccs.armatureDataManager.addArmatureFileInfo(aniInfo.png, aniInfo.plist, aniInfo.exportjson)
        } else {
            for (let i = 0; i < aniInfo.png.length; ++i) {
                ccs.armatureDataManager.addArmatureFileInfo(aniInfo.png[i], aniInfo.plist[i], aniInfo.exportjson)
            }
        }
        let armature = new ccs.Armature(aniInfo.newname)
        armature.getAnimation().play(aniInfo.playname)
        armature.setAnchorPoint(cc.p(0.5, 0.5))
        armature.getAnimation().setMovementEventCallFunc(function (btn, et) {
            if (et === 1 || et === ccs.MovementEventType.loopComplete) {
                btn.removeFromParent()
                btn = null
            }
        })
        return armature
    }

    GameUtil.loadUrlImg = function (url, parent) {
        if (!url || !parent) {
            return
        }
        cc.loader.loadImg(url, null, function (err, img) {
            if (!err && img) {
                let size = parent.getContentSize()
                let sp = new cc.Sprite(img);
                sp.setContentSize(size)
                sp.setPosition(cc.p(size.width / 2, size.height / 2))
                parent.addChild(sp);
            }
        });
    }

    GameUtil.getLocalLanguage = function () {
        return global.localStorage.getStringForKey(LocalSave.LocalLanguage) || 'putong'
    }

    GameUtil.autoPlaySound = function (sound) {
        if (!sound) {
            return
        }
        let effectType = appInstance.gameAgent().gameUtil().getLocalLanguage()
        let SEX = [
            'man',
            'woman'
        ]
        let sexStr = SEX[appInstance.dataManager().getUserData().sex]
        let soundPath = ResConfig.Sound.path + effectType + '/' + sexStr + '/'

        if (typeof sound === 'string') {
            soundPath += sound
        } else {
            let index = Math.floor(Math.random() * (sound.length))
            soundPath += sound[index]
            soundPath += '.mp3'
        }
        cc.log('=========autoPlaySound=============' + soundPath)
        appInstance.audioManager().playEffect(soundPath)
    }



    GameUtil.DATATYPE_1 = '1'
    GameUtil.DATATYPE_2 = '2'
    GameUtil.CURRENCYTYPE_1 = '1'
    GameUtil.CURRENCYTYPE_2 = '2'
    GameUtil.UNITLOCATION_NULL = '0'
    GameUtil.UNITLOCATION_BEFORE = '1'
    GameUtil.CURRENCYTYPE_AFTER = '2'


    /**
     * 转换道具列表格式
     * @param functionName 功能名称，同layer名称
     * @param dataList 原数据结构
     * @param propList 转换后的数据结构
     * @param PropertyName 需要添加的属性名称
     * @param dataType 数据类型，如果是需要在propList内再加一个数组的话，propList本身传成{}，dataType传成GameUtil.DATATYPE_2，否则
     * propList本身传成[]，dataType传成GameUtil.DATATYPE_1
     * @param currencyType 货币图片有多个，按照现在的业务，如果是转盘以及签到会根据货币的数量显示多种图片，但是其他的（例如
     * 领取奖励，以及任务等不分数量，一种货币只有一种资源），不需要按照数量获取的传GameUtil.CURRENCYTYPE_1，否则传GameUtil.CURRENCYTYPE_2
     * @param unitLocation 数量单位位置，0没有，1在前，2在后
     * @param unitProperty  数量单位
     *
     *
     *
     */
    GameUtil.getPropsData = function (dataList,propList,propertyName,dataType,currencyType,unitLocation,unitProperty,functionName) {

        propList = propList || (GameUtil.DATATYPE_2 ? {} : [])

        if(dataType == GameUtil.DATATYPE_2 && !propList.hasOwnProperty(propertyName))
            propList[propertyName] = []

        for(let i = 0; i < dataList.length; i++){

            let data = dataList[i]
            let prop = {}

            GameUtil.getPropData(data,prop,currencyType,unitLocation,unitProperty,functionName)


            if(dataType == GameUtil.DATATYPE_1)
                propList.push(prop)
            else
                propList[propertyName].push(prop)
        }

    }


    /**
     * 转换道具列表格式
     * @param functionName 功能名称，同layer名称
     * @param data 原数据结构
     * @param prop 转换后的数据结构
     * @param currencyType 货币图片有多个，按照现在的业务，如果是转盘以及签到会根据货币的数量显示多种图片，但是其他的（例如
     * 领取奖励，以及任务等不分数量，一种货币只有一种资源），不需要按照数量获取的传GameUtil.CURRENCYTYPE_1，否则传GameUtil.CURRENCYTYPE_2
     * @param unitLocation 数量单位位置，0不需要 1在前，2在后
     * @param unitProperty  数量单位
     *
     *
     *
     */
    GameUtil.getPropData = function (data,prop,currencyType,unitLocation,unitProperty,functionName) {

        let propType = prop.propType = data.propType
        let propCode = prop.propCode = data.propCode
        let propNum = prop.propNum = data.propNum

        let propsResName = 'propsRes'
        if(functionName == GameConfig.FUNCTION_NAME_TURNTABLE)
            propsResName = 'turnTableRewardsRes'
        else if(functionName == GameConfig.FUNCTION_NAME_SIGN)
            propsResName = 'signRewardsRes'



        if(currencyType == GameUtil.CURRENCYTYPE_1){
            prop.res = GameConfig[propsResName][propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
        }else{
            if(global.isUndefined(functionName)){
                prop.res = GameConfig[propsResName][propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
            }else{
                if(!GameUtil.haveMulityCurrency(functionName,propType,propCode)){
                    prop.res = GameConfig[propsResName][propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
                }else{
                    let propsQuantityInterval = GameUtil.getPropsQuantityInterval(functionName,propType,propCode,propNum)
                    if(global.isUndefined(propsQuantityInterval))
                        prop.res = GameConfig[propsResName][propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
                    else
                        prop.res = GameConfig[propsResName][propType]['propCode'][propCode][propsQuantityInterval]
                }
            }
        }

        if(unitLocation == GameUtil.UNITLOCATION_BEFORE){
            prop.num = unitProperty + propNum
        }else if(unitLocation == GameUtil.CURRENCYTYPE_AFTER){
            prop.num = propNum + unitProperty
        }

    }



    GameUtil.getPropsQuantityInterval = function (functionName,propType,propCode,propNum) {

        let result = 'undefined'
        if(GameUtil.haveMulityCurrency(functionName,propType,propCode)){

            let least = GameConfig.quantityInterval[functionName][propType][propCode][GameConfig.ICON_RESULT_LEAST][GameConfig.QUANTITUINTERVAL_NUM_END]
            let most = GameConfig.quantityInterval[functionName][propType][propCode][GameConfig.ICON_RESULT_MOST][GameConfig.QUANTITUINTERVAL_NUM_START]

            if(propNum <= least){
                result = GameConfig.ICON_RESULT_LEAST
            }else if(propNum >= most){
                result = GameConfig.ICON_RESULT_MOST
            }else{
                result = GameConfig.QUANTITUINTERVAL_RESULT_MORE
            }

        }

        return result

    }

    GameUtil.haveMulityCurrency = function (functionName,propType,propCode) {

        let flag = true



        if(global.isUndefined(functionName) || global.isUndefined(propType) || global.isUndefined(propCode)){
            return false
        }

        switch (functionName) {
            case GameConfig.FUNCTION_NAME_TURNTABLE:
            case GameConfig.FUNCTION_NAME_SIGN:

                break
            default:
                flag = false
                break
        }

        if(!flag)
            return flag

        switch (propType) {
            case GameConfig.propType_currency:
                break
            default:
                flag = false
                break
        }


        if(!flag)
            return flag

        switch (propCode) {
            case GameConfig.propType_currency_coin:
            case GameConfig.propType_currency_diamonds:

                break
            default:
                flag = false
                break

        }

        return flag

    }

    GameUtil.getRolesData = function (rolesData,needKeysArrayName,propTypeName,propCodeName) {

        for(let i = 0; i < rolesData.length; i++){

            let roleData = rolesData[i]
            GameUtil.getRoleData(roleData,needKeysArrayName,propTypeName,propCodeName)

        }
    }


    GameUtil.getRoleData = function (roleData,needKeysArrayName,propTypeName,propCodeName) {

        let propCode = roleData[propCodeName]
        let propType = roleData[propTypeName]
        for(let i = 0; i < needKeysArrayName.length; i++){

            let needKeyName = needKeysArrayName[i]
            roleData[needKeyName] = GameConfig.propsRes[propType]['propCode'][propCode][needKeyName]

        }

    }

    GameUtil.getStringRule = function (num) {
        let length = num.toString().length;
        if (length>=9) {
            num = num/100000000;
            num = num.toFixed(1);
            num = num+'亿';
        } else if (length>4 && length<9) {
            num = num/10000;
            num = num.toFixed(1);
            num = num+'万';
        }
        return num;
    }

    GameUtil.delayBtn = function (btn,delayTime) {
        btn.setTouchEnabled(false)
        delayTime = delayTime || 2
        btn.runAction(cc.Sequence(cc.DelayTime(delayTime),cc.CallFunc(function () {
            btn.setTouchEnabled(true)
        })))
    }

    return GameUtil
})
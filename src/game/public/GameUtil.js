
load('game/public/GameUtil',function () {
    let GameUtil = {}
    let GameConfig = include('game/config/GameConfig')


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
     * @param dataType 数据类型，意思是需要转换的数据是个二维数组，还是一维数组，二维数组的含义是，转换后的格式存储到
     * props数组里，然后props再放到传来的propList内，一维数组的含义是转换后的道具信息直接存放到propList内，参数值为GameUtil.DATATYPE_1,GameUtil.DATATYPE_2
     * 分别对应一维/二维数组
     * @param currencyType 货币图片有多个，按照现在的业务，如果是转盘以及签到会根据货币的数量显示多种图片，但是其他的（例如
     * 领取奖励，以及任务等不分数量，一种货币只有一种资源），不需要按照数量获取的传GameUtil.CURRENCYTYPE_1，否则传GameUtil.CURRENCYTYPE_2
     * @param unitLocation 数量单位位置，0没有，1在前，2在后
     * @param unitProperty  数量单位
     *
     *
     *
     */
    GameUtil.getPropsData = function (dataList,propList,propertyName,dataType,currencyType,unitLocation,unitProperty,functionName) {

        propList = propList || []
        if(dataType == GameUtil.DATATYPE_2 && propList.indexOf(propertyName) == -1)
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


        if(currencyType == GameUtil.CURRENCYTYPE_1){
            prop.res = GameConfig.propsRes[propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
        }else{
            if(global.isUndefined(functionName)){
                prop.res = GameConfig.propsRes[propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
            }else{

                if(!GameUtil.haveMulityCurrency(functionName))
                    prop.res = GameConfig.propsRes[propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
                else{
                    let propsQuantityInterval = GameUtil.getPropsQuantityInterval(functionName,propType,propCode,propNum)
                    if(global.isUndefined(propsQuantityInterval))
                        prop.res = GameConfig.propsRes[propType]['propCode'][propCode][GameConfig.ICON_RESULT_CURRENCY]
                    else
                        prop.res = GameConfig.propsRes[propType]['propCode'][propCode][propsQuantityInterval]
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

            let least = GameConfig.quantityInterval[functionName][propType][propCode][GameConfig.ICON_RESULT_LEAST]
            let most = GameConfig.quantityInterval[functionName][propType][propCode][GameConfig.ICON_RESULT_MOST]

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

        let flag = false
        if(global.isUndefined(functionName) || global.isUndefined(propType) || global.isUndefined(propCode)){
            return flag
        }

        switch (functionName) {
            case GameConfig.FUNCTION_NAME_TURNTABLE:
            case GameConfig.FUNCTION_NAME_SIGN:

                flag = true
                break
            default:
                break
        }

        switch (propType) {
            case GameConfig.propType_currency:
                flag = true
                break
            default:
                break

        }

        switch (propCode) {
            case GameConfig.propType_currency_coin:
            case GameConfig.propType_currency_diamonds:
                flag = true
                break
            default:
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
        for(let i = 0; i < roleData[needKeysArrayName].length; i++){

            let needKeyName = roleData[needKeysArrayName][i]
            roleData[needKeyName] = GameConfig.propsRes[propType]['propCode'][propCode][needKeyName]

        }

    }


    return GameUtil
})
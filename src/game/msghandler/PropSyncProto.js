
/**
 * 出牌消息结构体
 */
load('game/msghandler/PropSyncProto', function () {
    let baseProto = include('public/network/BaseProto')
    let GameEvent = include('game/config/GameEvent')
    let proto = baseProto.extend({
        _name: 'PropSyncProto',
        _offMsgId: 2,
        ctor: function () {
            this._super()
        },

        handleMsg: function (msg) {
            this._super(msg)

            let _saveKey = [

            ]
            let _saveData = {}
            for(let i = 0; i < msg.changes.length; i++){

                let _propInfoType = msg.changes[i].propCode
                switch (_propInfoType) {

                    case 1:
                        _saveKey.push('coin')
                        _saveData['coin'] = msg.changes[i].propNum
                        break
                    case 2:
                        _saveKey.push('diamonds')
                        _saveData['diamonds'] = msg.changes[i].propNum
                        break
                    case 3:


                        
                        _saveKey.push('fuKa')
                        _saveData['fuKa'] = msg.changes.propNum
                        break
                    default:
                        break

                }




            }

            appInstance.dataManager().getUserData().saveMsg(_saveData, _saveKey)

            appInstance.sendNotification(GameEvent.UPDATE_PROPSYNC, _saveData)
        },

        initData: function () {
            this._seMsgId = appInstance.msgTool().msgId_BASIC(this._offMsgId)
            this._reMsgId = appInstance.msgTool().msgId_BASIC_Re(this._offMsgId)
            this._seData = [

            ]

            let _propInfoArray = [
                { key: 'propCode', type: this._byteType.Int},//道具小类，默认只用于货币 1 金币，2 钻石 3福卡
                { key: 'propNum', type: this._byteType.UTF8},//货币最新值
            ]

            this._reData = [
                { key: 'reason', type: this._byteType.Int},//产生变化的原因，暂时没用
                { key: 'changes', type: this._byteType.Barray, proto: _propInfoArray},//道具变化列表
            ]
        }
    })
    return proto
})
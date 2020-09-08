
/**
 * 消息包处理
 */
load('public/network/Packet', function () {
    let Packet = cc.Class.extend({
        _bodyLen: 0,
        _msgId: 0,
        _versionId: 1,
        _extend: 1,

        ctor: function (protoObj) {
            this.initData(protoObj)
        },

        initData: function (protoObj) {
            if (!protoObj) {
                return
            }
            this._headProto = appInstance.msgTool().getHeadProto()
            this._msgId = protoObj.getSeMsgId()
            this._name = protoObj.getProtoName()
            this._bodyProto = protoObj.getSeData()
            this._bodyData = {}
            this._headData = {}

            this.loadCheckKey()
        },
        loadCheckKey: function () {
            this._headKey = []
            this._bodyKey = []
            for (let i = 0; i < this._headProto.length; ++i) {
                this._headKey.push(this._headProto[i].key)
            }
            for (let i = 0; i < this._bodyProto.length; ++i ) {
                this._bodyKey.push(this._bodyProto[i].key)
            }
        },

        setValue: function (tag, value) {
            if (typeof tag === 'string') {
                if (this._headKey.indexOf(tag) !== -1) {
                    this._headData[tag] = value
                } else if (this._bodyKey.indexOf(tag) !== -1) {
                    this._bodyData[tag] = value
                } else {
                    throw ' there is not key for this proto, proto name is ===' + this._name
                }
            } else {
                if (!global.isEmptyObject(tag)) {
                    cc.log('sendMsg setValue :::' + JSON.stringify(tag))
                }
                for ( let key in tag) {
                    this.setValue(key, tag[key])
                }
            }
        },

        getBodyData: function () {
            return this._bodyData
        },

        getHeadData: function () {
            return this._headData
        },

        LockPacket: function () {
            this._headData['msgId'] = this._msgId
            this._headData['bodyLen'] = this.getBodyLen(true)
            this._headData['versionId'] = this._versionId
            this._headData['extend'] = this._extend
            this._headData['pid'] = appInstance.msgTool().getHeadPid()
            this._headData['serverId'] = appInstance.msgTool().getHeadServerId()
        },

        getBodyProto: function () {
            return this._bodyProto
        },

        getBodyLen: function (isPacketLen) {
            let len = 0
            for ( let i = 0; i < this._bodyProto.length; ++i) {
                len += appInstance.msgTool().getByteLen(this._bodyProto[i].type, this._bodyData[this._bodyProto[i].key], isPacketLen, this._bodyProto[i].proto)
            }
            return len
        },

        getPacketLen: function () {
            return this.getBodyLen(true) + appInstance.msgTool().getHeadLen()
        },

        getData: function () {
            let result = this._headData
            for (let key in this._bodyData) {
                result[key] = this._bodyData[key]
            }
            return result
        }
    })
    return Packet
})
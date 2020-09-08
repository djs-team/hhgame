
/**
 * 消息包处理
 */
load('public/network/ParserRe', function () {
    let ParserRe = cc.Class.extend({
        _arrayBuffer: null,
        _dataView: null,
        _offPos: 0,
        _headProto: [],
        _headData: {},
        _bodyProto: [],
        _reProtoObj: null,
        _bodyData: {},
        _bodyLen: 0,
        _msgId: 0,
        _versionId: 0,
        _extend: 0,
        _pid: 0,
        _serverId: 0,
        ctor: function (arrayBuffer) {
            this.initData(arrayBuffer)
        },

        copyBuffer: function (buffer) {
            let bytes = new Uint8Array(buffer)
            let outBuffer = new ArrayBuffer(buffer.byteLength)
            let outputBytes = new Uint8Array((outBuffer))
            for (let i = 0; i < bytes.length; ++i) {
                outputBytes[i] = bytes[i]
            }
            return outBuffer
        },
        initData: function (arrayBuffer) {
            this._bodyData = {}
            this._headLen = appInstance.msgTool().getHeadLen()
            this._headProto = appInstance.msgTool().getHeadProto()

            if (this._arrayBuffer) {
                delete this._arrayBuffer
            }
            if (this._dataView) {
                delete this._dataView
            }

            this._arrayBuffer = this.copyBuffer(arrayBuffer)
            this._dataView = new DataView(this._arrayBuffer)
            this._offPos = 0
            this.initHeadProto()
            this.initBodyProto()
        },

        initHeadProto: function () {
            this._bodyLen = this._dataView.getInt32(this._offPos)
            this._offPos += 4
            this._msgId = this._dataView.getInt32(this._offPos)
            this._offPos += 4
            this._versionId = this._dataView.getUint8(this._offPos)
            this._offPos += 1
            this._extend = this._dataView.getUint8(this._offPos)
            this._offPos += 1
            this._pid = this._dataView.getInt32(this._offPos)
            this._offPos += 4
            this._serverId = this._dataView.getInt32(this._offPos)
            this._offPos += 4
        },

        getMsgData: function () {
            this._bodyData.msgId = this._msgId
            this._bodyData.msgName = this._msgName
            return this._bodyData
        },

        initBodyProto: function () {
            this._reProtoObj = appInstance.gameNet().getProtoReObj(this._msgId)
            if (!this._reProtoObj) {
                cc.log('error happen msgID:' + this._msgId)
                return
            }
            this._bodyProto = this._reProtoObj.getReData()
            this._msgName = this._reProtoObj.getProtoName()
            for (let i = 0; i < this._bodyProto.length; ++i) {
                this.readData(this._bodyProto[i], this._bodyData)
            }
        },

        readUTF8: function () {
            let strLen = this._dataView.getInt16(this._offPos)
            this._offPos += 2
            let strArray = []
            for (let i = 0; i < strLen; ++i) {
                strArray.push(this._dataView.getUint8(this._offPos))
                this._offPos += 1
            }
            return appInstance.msgTool().utf8Decode(strArray)
        },

        readBarray: function (proto) {
            let arrayLen = this._dataView.getInt16(this._offPos)
            this._offPos += 2
            let Bary = []
            for (let i = 0; i < arrayLen; ++i) {
                let tb = {}
                for (let j = 0; j < proto.length; ++j) {
                    this.readData(proto[j], tb)
                }
                Bary.push(tb)
            }
            return Bary
        },

        readData: function (proto, tb) {
            let key = proto.key
            let type = proto.type
            let byteType = appInstance.msgTool().getByteType()
            switch (type) {
                case byteType.Byte:
                    tb[key] = this._dataView.getUint8(this._offPos)
                    this._offPos += 1
                    break;
                case byteType.Short:
                    tb[key] = this._dataView.getInt16(this._offPos)
                    this._offPos += 2
                    break;
                case byteType.Int:
                    tb[key] = this._dataView.getInt32(this._offPos)
                    this._offPos += 4
                    break;
                case byteType.Float:
                    tb[key] = this._dataView.getFloat32(this._offPos)
                    this._offPos += 4
                    break;
                case byteType.Double:
                    tb[key] = this._dataView.getFloat64(this._offPos)
                    this._offPos += 4
                    break;
                case byteType.UTF8:
                    tb[key] = this.readUTF8()
                    break;
                case byteType.Barray:
                    tb[key] = this.readBarray(proto.proto)
                    break
            }
        }
    })
    return ParserRe
})
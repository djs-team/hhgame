
/**
 * 消息包处理
 */
load('public/network/ParserSe', function () {
    let ParserSe = cc.Class.extend({
        _arrayBuffer: null,
        _dataView: null,
        _offPos: 0,
        ctor: function (packet) {
            this.initData(packet)
        },
        initData: function (packet) {
            this._packet = packet
            this._headLen = appInstance.msgTool().getHeadLen()
            this._headProto = appInstance.msgTool().getHeadProto()
            this._bodyLen = packet.getBodyLen()
            this._packetLen = packet.getPacketLen()
            this._bodyProto = packet.getBodyProto()
            this._headData = packet.getHeadData()
            this._bodyData = packet.getBodyData()
            this._arrayBuffer = new ArrayBuffer(this._packetLen)
            this._dataView = new DataView(this._arrayBuffer)
            this._offPos = 0
            this.initHeadProto()
            this.initBodyProto()
        },

        getArrayBuffer: function () {
            return this._arrayBuffer
        },

        initHeadProto: function () {
            for (let i = 0; i < this._headProto.length; ++i) {
                this.writeData(this._headProto[i].type, this._headData[this._headProto[i].key] )
            }
        },

        initBodyProto: function () {
            for (let i = 0; i < this._bodyProto.length; ++i) {
                this.writeData(this._bodyProto[i].type, this._bodyData[this._bodyProto[i].key], this._bodyProto[i].proto)
            }
        },

        writeUTF8: function (str) {
            let strArray = appInstance.msgTool().utf8Encode(str)
            let strLen = strArray.length
            this._dataView.setInt16(this._offPos,strLen)
            this._offPos += 2
            for (let i = 0; i < strLen; ++i) {
                this._dataView.setUint8(this._offPos, strArray[i])
                this._offPos += 1
            }
        },

        writeArray: function (data, proto) {
            let dataLen = data.length
            this._dataView.setInt16(this._offPos,dataLen)
            this._offPos += 2
            for (let i = 0; i < dataLen; ++i) {
                for (let j = 0; j < proto.length; ++j) {
                    this.writeData(proto[j].type, data[i][proto[j].key], proto[j].proto)
                }
            }
        },

        writeData: function (type, value, proto) {
            let byteType = appInstance.msgTool().getByteType()
            switch (type) {
                case byteType.Byte:
                    this._dataView.setUint8(this._offPos, value)
                    this._offPos += 1
                    break;
                case byteType.Short:
                    this._dataView.setInt16(this._offPos, value)
                    this._offPos += 2
                    break;
                case byteType.Int:
                    this._dataView.setInt32(this._offPos, parseInt(value))
                    this._offPos += 4
                    break;
                case byteType.Float:
                    this._dataView.setFloat32(this._offPos, value)
                    this._offPos += 4
                    break;
                case byteType.Double:
                    this._dataView.setFloat64(this._offPos, value)
                    this._offPos += 4
                    break;
                case byteType.UTF8:
                    this.writeUTF8(value)
                    break;
                case byteType.Barray:
                    this.writeArray(value, proto)
                    break;
            }
        },

        defaultFunction: function () {

        }
    })
    return ParserSe
})

/**
 * 网络管理层
 */
load('public/network/MsgTool', function () {
    let UTF8Class = cc.Class.extend({
        EOF_byte: -1,
        EOF_code_point: -1,
        ctor: function () {

        },
        encoderError: function(code_point) {
            console.error("UTF8 encoderError", code_point)
        },
        decoderError: function(fatal, opt_code_point) {
            if (fatal) {
                console.error("UTF8 decoderError", opt_code_point)
            }
            return opt_code_point || 0xFFFD;
        },
        inRange: function (a, min, max) {
            return min <= a && a <= max
        },
        div: function (n, d) {
            return Math.floor(n / d)
        },
        stringToCodePoints: function(str) {
            /** @type {Array.<number>} */
            let cps = [];
            // Based on http://www.w3.org/TR/WebIDL/#idl-DOMString
            let i = 0, n = str.length;
            while (i < str.length) {
                let c = str.charCodeAt(i);
                if (!this.inRange(c, 0xD800, 0xDFFF)) {
                    cps.push(c);
                } else if (this.inRange(c, 0xDC00, 0xDFFF)) {
                    cps.push(0xFFFD);
                } else { // (inRange(c, 0xD800, 0xDBFF))
                    if (i == n - 1) {
                        cps.push(0xFFFD);
                    } else {
                        let d = str.charCodeAt(i + 1);
                        if (this.inRange(d, 0xDC00, 0xDFFF)) {
                            let a = c & 0x3FF;
                            let b = d & 0x3FF;
                            i += 1;
                            cps.push(0x10000 + (a << 10) + b);
                        } else {
                            cps.push(0xFFFD);
                        }
                    }
                }
                i += 1;
            }
            return cps;
        },

        encode: function (str) {
            let pos = 0;
            let codePoints = this.stringToCodePoints(str);
            let outputBytes = [];

            while (codePoints.length > pos) {
                let code_point = codePoints[pos++];

                if (this.inRange(code_point, 0xD800, 0xDFFF)) {
                    this.encoderError(code_point);
                }
                else if (this.inRange(code_point, 0x0000, 0x007f)) {
                    outputBytes.push(code_point);
                } else {
                    let count = 0, offset = 0;
                    if (this.inRange(code_point, 0x0080, 0x07FF)) {
                        count = 1;
                        offset = 0xC0;
                    } else if (this.inRange(code_point, 0x0800, 0xFFFF)) {
                        count = 2;
                        offset = 0xE0;
                    } else if (this.inRange(code_point, 0x10000, 0x10FFFF)) {
                        count = 3;
                        offset = 0xF0;
                    }

                    outputBytes.push(this.div(code_point, Math.pow(64, count)) + offset);

                    while (count > 0) {
                        let temp = this.div(code_point, Math.pow(64, count - 1));
                        outputBytes.push(0x80 + (temp % 64));
                        count -= 1;
                    }
                }
            }
            return new Uint8Array(outputBytes);
        },
        decode: function(data) {
            let fatal = false;
            let pos = 0;
            let result = "";
            let code_point;
            let utf8_code_point = 0;
            let utf8_bytes_needed = 0;
            let utf8_bytes_seen = 0;
            let utf8_lower_boundary = 0;

            while (data.length > pos) {
                let _byte = data[pos++];

                if (_byte == this.EOF_byte) {
                    if (utf8_bytes_needed != 0) {
                        code_point = this.decoderError(fatal);
                    } else {
                        code_point = this.EOF_code_point;
                    }
                } else {
                    if (utf8_bytes_needed == 0) {
                        if (this.inRange(_byte, 0x00, 0x7F)) {
                            code_point = _byte;
                        } else {
                            if (this.inRange(_byte, 0xC2, 0xDF)) {
                                utf8_bytes_needed = 1;
                                utf8_lower_boundary = 0x80;
                                utf8_code_point = _byte - 0xC0;
                            } else if (this.inRange(_byte, 0xE0, 0xEF)) {
                                utf8_bytes_needed = 2;
                                utf8_lower_boundary = 0x800;
                                utf8_code_point = _byte - 0xE0;
                            } else if (this.inRange(_byte, 0xF0, 0xF4)) {
                                utf8_bytes_needed = 3;
                                utf8_lower_boundary = 0x10000;
                                utf8_code_point = _byte - 0xF0;
                            } else {
                                this.decoderError(fatal);
                            }
                            utf8_code_point = utf8_code_point * Math.pow(64, utf8_bytes_needed);
                            code_point = null;
                        }
                    } else if (!this.inRange(_byte, 0x80, 0xBF)) {
                        utf8_code_point = 0;
                        utf8_bytes_needed = 0;
                        utf8_bytes_seen = 0;
                        utf8_lower_boundary = 0;
                        pos--;
                        code_point = this.decoderError(fatal, _byte);
                    } else {
                        utf8_bytes_seen += 1;
                        utf8_code_point = utf8_code_point + (_byte - 0x80) * Math.pow(64, utf8_bytes_needed - utf8_bytes_seen);

                        if (utf8_bytes_seen !== utf8_bytes_needed) {
                            code_point = null;
                        } else {
                            let cp = utf8_code_point;
                            let lower_boundary = utf8_lower_boundary;
                            utf8_code_point = 0;
                            utf8_bytes_needed = 0;
                            utf8_bytes_seen = 0;
                            utf8_lower_boundary = 0;
                            if (this.inRange(cp, lower_boundary, 0x10FFFF) && !this.inRange(cp, 0xD800, 0xDFFF)) {
                                code_point = cp;
                            } else {
                                code_point = this.decoderError(fatal, _byte);
                            }
                        }

                    }
                }
                //Decode string
                if (code_point !== null && code_point !== this.EOF_code_point) {
                    if (code_point <= 0xFFFF) {
                        if (code_point > 0) result += String.fromCharCode(code_point);
                    } else {
                        code_point -= 0x10000;
                        result += String.fromCharCode(0xD800 + ((code_point >> 10) & 0x3ff));
                        result += String.fromCharCode(0xDC00 + (code_point & 0x3ff));
                    }
                }
            }
            return result;
        }
    })
    let byteC = 0, shortC = 1,intC = 2, floatC = 3, doubleC = 4, utf8C = 5, barrayC = 6
    let MsgTool = cc.Class.extend({
        _flag: 0x80000000,
        _flag2: 0xFFFFFFF,
        HALL_ID: 0x00130000, // 大厅消息模块，基于HTTP发送
        BASIC_ID: 0x00020000, // 基础服务消息模块
        GM_MESSAGE_ID: 0x00050000, // 游戏消息模块
        LANDLORDS_ROOM_ID: 0x00030000, // 斗地主房间模块
        LANDLORDS_GAME_ID: 0x00100000, // 斗地主游戏模块
        LANDLORDS_ROOM_ID_2: 0x00090000, // 斗地主游戏模块2 给赛事用的
        LANDLORDS_MAHJONG_ROOM_MATCHGAME_ID: 0x00110000, // 麻将房间ID
        MJ_GAME_MATCHGAME_ID: 0x00120000, // 游戏服id
        G_MANAGER_ID: 0x00050000, // 后台管理消息
        MJ_ROOM_MATCHGAME_ID: 0x00110000, // 麻将房间ID
        MODULE_CLIENT: 0x00000000,

        _byteType:{
            Byte: byteC,
            Short: shortC,
            Int: intC,
            Float: floatC,
            Double: doubleC,
            UTF8: utf8C,
            Barray: barrayC
        },

        BYTE: {
            type: byteC,
            len: 1,
            enfunc: 'setUint8',
            defunc: 'getUint8'
        },

        SHORT: {
            type: shortC,
            len: 2,
            enfunc: 'setInt16',
            defunc: 'getInt16'
        },
        INT: {
            type: intC,
            len: 4,
            enfunc: 'setInt32',
            defunc: 'getInt32'
        },
        FLOAT: {
            type: floatC,
            len: 4,
            enfunc: 'setFloat32',
            defunc: 'getFloat32'
        },
        DOUBLE: {
            type: doubleC,
            len: 4,
            enfunc: 'setFloat64',
            defunc: 'getFloat64'
        },

        UTF8: {
            type: utf8C,
            len: 0,
            enfunc: 'UTF8',
            defunc: 'UTF8'
        },

        BARRAY: {
            type: barrayC,
            len: 0,
            enfunc: 'BARRAY',
            defunc: 'BARRAY'
        },

        _headPid: 0,
        _headServerId: 0,

        ctor: function () {
            this._utf8Func = new UTF8Class()
            this.initData()
        },

        utf8Encode: function (str) {
            return this._utf8Func.encode(str)
        },

        utf8Decode: function (data) {
            return this._utf8Func.decode(data)
        },

        GET_HTTP_RETID: function (ID) {
            return this._flag2 & ID
        },

        GET_PACKET_RETID: function (ID) {
            return this._flag ^ ID
        },
        mergeArrayBuffer: function (arrays) {
            let totalLen = 0
            for (let i = 0; i < arrays.length; ++i) {
                arrays[i] = new Uint8Array(arrays[i])
                totalLen += arrays[i].length
            }
            let res = new Uint8Array(totalLen)
            let offset = 0
            for (let i = 0; i < arrays.length; ++i) {
                res.set(arrays[i], offset)
                offset += arrays[i].length
            }
            return res.buffer
        },

        setHeadData: function (msg) {
            for (let key in msg) {
                // this._headProto
            }
        },

        initData: function () {
            this._headProto = [
                { key: 'bodyLen', type: this.INT.type},
                { key: 'msgId', type: this.INT.type},
                { key: 'versionId', type: this.BYTE.type},
                { key: 'extend', type:  this.BYTE.type},
                { key: 'pid', type: this.INT.type},
                { key: 'serverId', type: this.INT.type}
            ]

            this._msgId = {}
        },

        msgId_BASIC: function (num) {
            return this.BASIC_ID + num
        },

        msgId_BASIC_Re: function (num) {
            return this.GET_PACKET_RETID(this.BASIC_ID + num)
        },

        msgId_MODULE_CLIENT: function (num) {
            return this.MODULE_CLIENT + num
        },

        msgId_MODULE_CLIENT_Re: function (num) {
            return this.GET_PACKET_RETID(this.MODULE_CLIENT + num)
        },

        msgId_GAME_MATCHGAME: function (num) {
            return this.MJ_GAME_MATCHGAME_ID + num
        },

        msgId_GAME_MATCHGAME_Re: function (num) {
            return this.GET_PACKET_RETID(this.MJ_GAME_MATCHGAME_ID + num)
        },

        msgId_ROOM_MATCHGAME: function (num) {
            return this.MJ_ROOM_MATCHGAME_ID + num
        },

        msgId_ROOM_MATCHGAME_Re: function (num) {
            return this.GET_PACKET_RETID(this.MJ_ROOM_MATCHGAME_ID + num)
        },

        addMsgId: function (tb) {
            if (global.isArray(tb)) {
                for (let i = 0; i < tb.length; ++i) {
                    for (let key in tb[i]) {
                        this._msgId[key] = tb[i][key]
                    }
                }
            } else {
                for (let key in tb) {
                    this._msgId[key] = tb[key]
                }
            }
        },

        getMsgId: function (key) {
            return this._msgId[key]
        },

        getUTF8ByteLen: function(value, isPacketLen) {
            let offLen = isPacketLen ? 2 : 0
            return this._utf8Func.encode(value).length + offLen
        },

        getBarrayLen: function(value, proto, tmplen) {
            tmplen += 2
            for (let i = 0; i < value.length; ++i) {
                for (let pj = 0; pj < proto.length; ++pj) {
                    tmplen += this.getByteLen(proto[pj].type, value[i][proto[pj].key], false, proto[pj].proto)
                }
            }
            return tmplen
        },

        getByteLen: function (type, value, isPacketLen, proto, tmpLen) {
            tmpLen = tmpLen ? tmpLen : 0
            switch (type) {
                case this.BYTE.type:
                    return this.BYTE.len
                case this.SHORT.type:
                    return this.SHORT.len
                case this.INT.type:
                    return this.INT.len
                case this.FLOAT.type:
                    return this.FLOAT.len
                case this.DOUBLE.type:
                    return this.DOUBLE.len
                case this.UTF8.type:
                    return this.getUTF8ByteLen(value, isPacketLen)
                case this.BARRAY.type:
                    return this.getBarrayLen(value, proto, tmpLen)
            }
        },

        getHeadLen: function () {
            let len = 0
            for ( let i = 0; i < this._headProto.length; ++i) {
                len += this.getByteLen(this._headProto[i].type )
            }
            return len
        },

        setHeadPid: function (pid) {
            this._headPid = pid
        },

        setHeadServerId: function (serverId) {
            this._headServerId = serverId
        },

        getHeadPid: function () {
            return this._headPid
        },

        getHeadServerId: function () {
            return this._headServerId
        },

        getByte: function () {
            return this.BYTE
        },
        getShort: function () {
            return this.SHORT
        },
        getInt: function () {
            return this.INT
        },
        getFloat: function () {
            return this.FLOAT
        },
        getDouble: function () {
            return this.DOUBLE
        },
        getUTF8: function () {
            return this.UTF8
        },
        getBarray: function () {
            return this.BARRAY
        },
        getByteType: function () {
            return this._byteType
        },
        getHeadProto: function () {
            return this._headProto
        },
    })

    return MsgTool
})

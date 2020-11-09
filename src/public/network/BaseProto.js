
/**
 * proto结构基类
 */
load('public/network/BaseProto', function () {
    let proto = cc.Class.extend({
        _name: 'BaseProto',
        _offMsgId: 0,
        _seMsgId: null,
        _reMsgId: null,
        _seData: [],
        _reData: [],
        ctor: function () {
            this._byteType = appInstance.msgTool().getByteType()
            this.initData()
        },
        initData: function () {

        },
        handleMsg: function (msg) {
            if (this._name !== 'HeartBeatProto' ) {
                cc.log('----------->>>>>>>>>>>>>>>>>>>>>>>>>>>>>---netName = ' + this._name + ' ==== <<<     ' + JSON.stringify(msg) + '     >>>')
            }
        },

        getProtoName: function () {
            return this._name
        },
        getSeData: function () {
            return this._seData
        },
        getSeMsgId: function () {
            return this._seMsgId
        },
        getReData: function () {
            return this._reData
        },
        getReMsgId: function () {
            return this._reMsgId
        }
    })
    return proto
})
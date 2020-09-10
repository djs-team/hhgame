
load('public/network/ProtoArrays', function () {
    let protoArrays  = cc.Class.extend({
        ctor: function (msgTool) {
            this._byteType = msgTool.getByteType()
        },
        getTbCardsInfo: function () {
            let proto = [
                { key: 'nCardColor', type: this._byteType.Int},
                { key: 'nCardNumber', type: this._byteType.Int}
            ]
            return proto
        },

        getTbSelectAction: function () {
            let proto = [
                { key: 'pActionID', type: this._byteType.Int},
                { key: 'pCards', type: this._byteType.Barray, proto: this.getTbCardsInfo()},
                { key: 'pNoCards', type: this._byteType.Barray, proto: this.getTbCardsInfo()}
            ]
            return proto
        },

        getTbChiInfo: function () {
            let proto = [
                { key: 'pChiCardColor', type: this._byteType.Int},
                { key: 'pBeginIndex', type: this._byteType.Int},
                { key: 'pEndIndex', type: this._byteType.Int},//chi
            ]
            return proto
        },

        getTbGangInfo: function () {
            let proto = [
                { key: 'pGangCardColor', type: this._byteType.Int},
                { key: 'pGangCardNumber', type: this._byteType.Int},//gang
                { key: 'pGangType', type: this._byteType.Int},//0:明杠or1:暗杠or2:加杠or3:旋风杠or4:幺腰杠or5:幺九杠6：喜儿 7:甩幺
                { key: 'pGangCards', type: this._byteType.Barray, proto: this.getTbCardsInfo()},// gang
            ]
            return proto
        },
    })
    return protoArrays
})
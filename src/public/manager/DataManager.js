/**
 * Data Manager, Singleton
 **/
load('public/manager/DataManager', function () {
    let DataManager = cc.Class.extend({
        _gameData: null, // 游戏基础数据
        _userData: null, // 登录用户数据
        _playDataModel: null, // 标记牌桌数据类型
        _playData: null, //牌桌数据
        ctor: function () {},

        registerGameData: function (gameData) {
            this._gameData = gameData
        },

        registerUserData: function (userData) {
            this._userData = userData
        },

        registerPlayDataModel: function (playDataModel) {
            this._playDataModel = playDataModel
        },

        setLoginData: function (data) {
            this._gameData.setData(data)
            this._userData.setData(data)
        },

        resetLoginData: function () {
            this._gameData.resetData()
            this._userData.resetData()
        },

        setPlayData: function (data) {
            if (!this._playDataModel) {
                cc.log('_playDataModel======================================= error!')
                return
            }
            if (this._playData) {
                delete this._playData
            }
            this._playData = new this._playDataModel()
            this._playData.setData(data)
        },

        delTableData: function () {
            if (this._playData) {
                delete this._playData
            }
        },

        getPlayData: function () {
            return this._playData
        },

        getTableData: function () {
            return this._playData ? this._playData.tableData : {}
        },

        getUserData: function () {
            return this._userData
        },

        getGameData: function () {
            return this._gameData
        },

        selfUid: function () {
            if (this._playData && this._playData.tableData && this._playData.tableData.isReplay && this._playData.tableData.logPlayerUid) {
                return this._playData.tableData.logPlayerUid
            }
            return this._userData.uid
        },

        selfInfo: function () {
            return this.getPlayerByUid(this.selfUid())
        },
        getTableOwnerName: function () {
            let uid = this.getTableOwnerUid()
            let pl = this.getPlayerByUid(uid)
            return pl.info.nickname || pl.info.name
        },
        getTableOwnerUid: function () {
            let tData = this._playData.tableData
            let uids = tData.uids
            return uids[0]
        },
        isInTable: function () {
            let gameData = this._gameData
            if (gameData && gameData.vipTable) {
                return true
            }
            return false
        },
        getTableDeleter: function () {
            let tData = this._playData.tableData
            return tData.ownerDel
        },
        getCurrentUIOff: function () {
            let tData = this._playData.tableData
            let uids = tData.uids
            let selfIndex = uids.indexOf(this.selfUid())
            return (tData.curPlayer - selfIndex + tData.maxPlayers) % tData.maxPlayers
        },
        getCurrentUid: function () {
            let tData = this._playData.tableData
            let uids = tData.uids
            return uids[tData.curPlayer]
        },
        getCurrentPlayer: function () {
            let tData = this._playData.tableData
            let uid = tData.uids[tData.curPlayer]
            return this._playData.players[uid]
        },
        isTableOwner: function () {
            return this.selfUid() === this.getTableOwnerUid()
        },
        isPlaying: function () {
            if (this.getPlayData() &&
                this.getPlayData().tableData &&
                this.getPlayData().tableData.roundNum > 0) {
                return true
            }
            return false
        },
        isInGame: function () {
            if (!this._playData) return false
            let tData = this._playData.tableData
            if (tData && tData.tableId > 0 && this._playData.players.hasOwnProperty(this.selfUid() + '')) {
                return true
            }
            return false
        },
        getPlayerUIOff: function (uid) {
            let tData = this._playData.tableData
            let uids = tData.uids
            let selfIndex = uids.indexOf(this.selfUid())
            let targetIndex = uids.indexOf(uid)
            return (targetIndex - selfIndex + tData.maxPlayers) % tData.maxPlayers
        },
        getPlayerByUid: function (uid) {
            return this._playData.players[uid]
        },
        getPlayerNames: function (uidArray) {
            let rtn = []
            for (let i = 0; i < uidArray.length; i++) {
                let pl = this.getPlayerByUid(uidArray[i])
                if (pl) rtn.push(unescape(pl.info.nickname || pl.info.name))
            }
            return rtn + ''
        },
        getPlayers: function () {
            if (this._playData) {
                return this._playData.players
            }
            return null
        },
        getPlayerByOffset: function (off) {
            let tData = this._playData.tableData
            let uids = tData.uids
            let selfIndex = uids.indexOf(this.selfUid())
            if (off >= tData.maxPlayers) {
                off = tData.maxPlayers - 1
            }
            selfIndex = (selfIndex + off) % tData.maxPlayers
            if (selfIndex < uids.length) {
                return this._playData.players[uids[selfIndex]]
            } else {
                return null
            }
        }
    })

    return DataManager
})


load('module/mahjong/data/PlayData', function () {
  let BasePlayerData = include('game/data/BasePlayData')
  let TableData = include('module/mahjong/data/TableData')
  let PlayerInfo = include('module/mahjong/data/PlayerInfo')
  let TableConfig = include('module/mahjong/common/TableConfig')
  let TStatus = TableConfig.TStatus
  let PlayData = BasePlayerData.extend({
    ctor: function () { },
    setData: function (data) {
      this._super(data)

      let pPlayer = data.pPlayer
      for (let i = 0; i < pPlayer.length; ++i) {
        let playerInfo = new PlayerInfo()
        playerInfo.setDataMsg(pPlayer[i])
        this.players[pPlayer[i].pSeatID] = playerInfo
      }
      delete data.pPlayer

      this.tableData = new TableData()
      this.tableData.setDataMsg(data)

      this.pMySeatID = parseInt(data.pMySeatID)
      this.pPlayerNum = parseInt(data.pPlayerNum)
      this.pDongSeatID = parseInt(data.pDongSeatID)
      this.pMode = parseInt(data.pMode)
      this.uiSeatArray = TableConfig.UiSeatArray[this.pPlayerNum]
    },

    isPutCardStatus: function () {
      return TStatus.putCard === this.tableData.pTStatus
    },

    isDrawCardStatus: function () {
      return TStatus.drawCard === this.tableData.pTStatus
    },

    isSelectStatus: function () {
      return TStatus.select === this.tableData.pTStatus
    },

    getPlayerByUiseat: function (uiSeat) {
      if (typeof uiSeat === 'number') {
        let seatId = this.UI2SeatId(uiSeat)
        return this.players[seatId]
      }
      return this.players
    },

    saveTableData: function (msg,saveKey) {
      this.tableData.saveData(msg, saveKey)
    },

    getSelfInfo: function () {
      return this.getPlayer(this.pMySeatID)
    },

    getPlayer: function (seatId) {
      if (typeof seatId === 'number') {
        return this.players[seatId]
      }
      return this.players
    },

    isMatch: function () {
      if (this.pMode === 1 || this.pMode === 2) {
        return true
      } else {
        return false
      }
    },

    seatId2UI: function (seatId) {
      if (this.pPlayerNum === 2) {
        return parseInt(seatId) === this.pMySeatID ? 0 : 2
      } else if (this.pPlayerNum === 4) {
        let seatOffMy = {
          '0': 0,
          '1': 1,
          '2': 2,
          '3': 3,
          '-1': 3,
          '-2': 2,
          '-3': 1
        }
        return seatOffMy[parseInt(seatId) - this.pMySeatID]
      }
    },

    UI2SeatId: function (uiSeat) {
      if (this.pPlayerNum === 2) {
        switch (uiSeat) {
          case 0:
            return this.pMySeatID
          case 2:
            return 1 - this.pMySeatID
          default:
            return 0
        }
      } else if (this.pPlayerNum === 4) {
        let seatOffMy = []
        seatOffMy[0] = {
          '0': 0,
          '1': 1,
          '2': 2,
          '3': 3
        }
        seatOffMy[1] = {
          '-1': 1,
          '0': 2,
          '1': 3,
          '2': 0
        }
        seatOffMy[2] = {
          '-2': 2,
          '-1': 3,
          '0': 0,
          '1': 1
        }
        seatOffMy[3] = {
          '-3': 3,
          '-2': 0,
          '-1': 1,
          '0': 2
        }
        return seatOffMy[this.pMySeatID][parseInt(uiSeat) - this.pMySeatID]
      }
    }
  })
  return PlayData
})

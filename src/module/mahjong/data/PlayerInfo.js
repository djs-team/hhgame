
load('module/mahjong/data/PlayerInfo', function () {
  let BasePlayerInfo = include('game/data/BasePlayerInfo')
  let PlayerInfo = BasePlayerInfo.extend({
    ctor: function () {
      this.initData()
    },
    initData: function () {
      this.handCards = []
      this.putCards = []
      this.showCards = []
      this.handCardCount = 13
      this.pHosting = false
    }
  })

  return PlayerInfo
})

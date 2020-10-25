
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
      this.pChiList = []
      this.pPengList = []
      this.pGangList = []
      this.pJiangList = []
      this.handCardCount = 13
      this.pHosting = false
      this.pIsTing = false
    }
  })

  return PlayerInfo
})

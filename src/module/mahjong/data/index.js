
load('module/mahjong/data/index', function () {
  let PlayData = include('module/mahjong/data/PlayData')
  let index = {}
  index.run = function () {
    appInstance.dataManager().registerPlayDataModel(PlayData)
  }
  return index
})

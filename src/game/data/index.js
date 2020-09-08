
load('game/data/index', function () {
    let GameData = include('game/data/GameData')
    let UserData = include('game/data/UserData')
    let index = {}
    index.run = function () {
        appInstance.dataManager().registerGameData(new GameData())
        appInstance.dataManager().registerUserData(new UserData())
    }
    return index
})

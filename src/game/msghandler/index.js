
/**
 * 消息接收注册入口
 */
load('game/msghandler/index', function () {
    let DrawCardProto = include('game/msghandler/DrawCardProto')
    let EnterTableCancelProto = include('game/msghandler/EnterTableCancelProto')
    let EnterTableProto = include('game/msghandler/EnterTableProto')
    let GamingProto = include('game/msghandler/GamingProto')
    let InitCardProto = include('game/msghandler/InitCardProto')
    let JiaGangProto = include('game/msghandler/JiaGangProto')
    let LoginProto = include('game/msghandler/LoginProto')
    let MagicIsReProto = include('game/msghandler/MagicIsReProto')
    let PlayerOnlineProto = include('game/msghandler/PlayerOnlineProto')
    let PlayerSelectProto = include('game/msghandler/PlayerSelectProto')
    let PutCardProto = include('game/msghandler/PutCardProto')
    let TableChangeProto = include('game/msghandler/TableChangeProto')
    let TableHostingProto = include('game/msghandler/TableHostingProto')
    let HeartBeatProto = include('game/msghandler/HeartBeatProto')
    let PropSyncProto = include('game/msghandler/PropSyncProto')
    let JiaGangTableProto = include('game/msghandler/JiaGangTableProto')
    let PlayerTrustProto = include('game/msghandler/PlayerTrustProto')
    let GameResultProto = include('game/msghandler/GameResultProto')
    let AutoPlayProto = include('game/msghandler/AutoPlayProto')
    let GetArenaProto = include('game/msghandler/GetArenaProto')


    let objArray = [
        new DrawCardProto(),
        new EnterTableCancelProto(),
        new EnterTableProto(),
        new GamingProto(),
        new InitCardProto(),
        new JiaGangProto(),
        new LoginProto(),
        new MagicIsReProto(),
        new PlayerOnlineProto(),
        new PlayerSelectProto(),
        new PutCardProto(),
        new TableChangeProto(),
        new TableHostingProto(),
        new HeartBeatProto(),
        new PropSyncProto(),
        new JiaGangTableProto(),
        new PlayerTrustProto(),
        new GameResultProto(),
        new AutoPlayProto(),
        new GetArenaProto(),
    ]

    let index = {}
    index.register = function () {
        for (let i = 0; i < objArray.length; ++i) {
            appInstance.gameNet().registerMsgHandler(objArray[i].getReMsgId(), objArray[i])
        }
    }

    index.unRegister = function () {
        for (let i = 0; i < objArray.length; ++i) {
            appInstance.gameNet().registerMsgHandler(objArray[i].getReMsgId())
        }
    }

    return index
})

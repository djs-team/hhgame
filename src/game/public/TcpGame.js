
load('game/public/TcpGame', function () {
    let GameConfig = include('game/config/GameConfig')
    let Packet = include('public/network/Packet')
    let LoginProto = include('game/msghandler/LoginProto')
    let EnterTableProto = include('game/msghandler/EnterTableProto')
    let EnterTableCancelProto = include('game/msghandler/EnterTableCancelProto')
    let MatchEnterTableCancelProto = include('game/msghandler/MatchEnterTableCancelProto')
    let HeartBeatProto = include('game/msghandler/HeartBeatProto')
    let PutCardProto = include('game/msghandler/PutCardProto')
    let PlayerSelectProto = include('game/msghandler/PlayerSelectProto')
    let TableHostingProto = include('game/msghandler/TableHostingProto')
    let GetArenaProto = include('game/msghandler/GetArenaProto')
    let TcpGame  = cc.Class.extend({
        ctor: function () {

        },
        heartBeat: function (msg) {
            msg = msg || {}
            let packetProto = new Packet(new HeartBeatProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },

        tcpLogin: function (msg) {
            msg = msg || {}
            msg.gid = 5
            msg.key = appInstance.dataManager().getUserData().key
            msg.channel = appInstance.dataManager().getUserData().channel
            let packetProto = new Packet(new LoginProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },

        enterTable: function(msg) {
            msg = msg || {}
            msg.gid = 5
            msg.mjChannel = appInstance.dataManager().getUserData().getMjChannel()
            msg.ruleName = 'zhaoyuan'
            let packetProto = new Packet(new EnterTableProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },
        cancelEnterTable: function(msg) {
            msg = msg || {}
            let packetProto = new Packet(new EnterTableCancelProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },

        cancelEnterTableMatch: function(msg) {
            msg = msg || {}
            let packetProto = new Packet(new MatchEnterTableCancelProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },

        putCardProto: function(msg) {
            msg = msg || {}
            msg.pTableID = appInstance.dataManager().getPlayData().tableData.pTableID
            let packetProto = new Packet(new PutCardProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },
        PlayerSelectProto: function(msg) {
            msg = msg || {}
            msg.pTableID = appInstance.dataManager().getPlayData().tableData.pTableID
            let packetProto = new Packet(new PlayerSelectProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },

        TableHostingProto: function (msg) {
            msg = msg || {}
            msg.pTableID = appInstance.dataManager().getPlayData().tableData.pTableID
            msg.pHostingExt = ''
            let packetProto = new Packet(new TableHostingProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        },

        GetArenaMessageProto: function (msg) {
            msg = msg || {}
            msg.channel = appInstance.dataManager().getUserData().getMjChannel()
            let packetProto = new Packet(new GetArenaProto())
            packetProto.setValue(msg)
            appInstance.gameNet().send(packetProto)
        }

    })
    return TcpGame
})
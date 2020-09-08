/**
 * 用于定义事件的类型
 */
load('public/config/EventType', function () {
    let EventType = {}

    /**
     * Pomelo 层级的事件信息
     * @type {}
     */
    EventType.POMELO_EVENT = {
        POMELO_EVENT_IOERROR: 'io-error',
        POMELO_EVENT_ONKICK: 'onKick',
        POMELO_EVENT_CONNECT: 'connect',
        POMELO_EVENT_NETERROR: 'netError', // 网络错误
        POMELO_EVENT_NETCLOSE: 'netClose', // 关闭连接
        POMELO_EVENT_DISCONNECT: 'disconnect', // 断开连接
        POMELO_EVENT_RECONNECT: 'reconnect', // 重新连接
        POMELO_EVENT_HEARTBEATTIMEOUT: 'heartbeatTimeout',
        POMELO_EVENT_ROUNDCONNECT: 'roundConnect'
    }

    /**
     * NET 层级的事件
     * @type {}
     */
    EventType.NET_EVENT = {
        NET_EVENT_CONNECT: 'connect', // 网络连接成功或者失败时回调
        NET_EVENT_NOTIFYMSG: 'notifyMsg', // gameNet向外派发一条网络下行数据
        NET_EVENT_PINGPONG: 'pingPong' // pingPong值发生变化时触发的事件
    }

    // 断线重连的类型
    EventType.disconnectType = {}

    /**
     * 用户级自定义事件
     */
    EventType.USER_EVENT = {
        USER_EVENT_CAPTURE_SCREEN: 'capture_screen', // 发送截屏事件
        USER_EVENT_CAPTURE_SCREEN_OK: 'captureScreen_OK',  // 截屏成功
        USER_EVENT_RUN_START_RECORD: 'runStartRecord', // 开始录音
        USER_EVENT_RUN_STOP_RECORD: 'runStopRecord', // 停止录音
        USER_EVENT_RUNTO_CANCEL_RECORD: 'runToCancelRecord', // 录音到取消
        USER_EVENT_RUN_CANCEL_RECORD: 'runCancelRecord', // 取消录音
        USER_EVENT_UPLOAD_RECORD: 'uploadRecord', // 上传录音
        USER_EVENT_SEND_VOICE: 'sendVoice', // 发送录音
        USER_EVENT_PLAY_VOICE: 'playVoice', // 播放录音
        USER_EVENT_ONGLONOTIFY: 'OnGlobalNotify', // 全局推送的事件
        USER_EVENT_ONCE_MORE_GAME: 'onceMoreGame', // 再来一局
        USER_EVENT_UPDATE_PLAYER_LOCATION: 'UPDATE_PLAYER_LOCATION', // 刷新地理位置，底层已经这样命名了
        USER_EVENT_AY_NOT_INSTALLED: 'AY_NOT_INSTALLED', // 爱友未安装
        USER_EVENT_DL_NOT_INSTALLED: 'DL_NOT_INSTALLED', // 多聊未安装
        USER_EVENT_MW_NOT_INSTALLED: 'MW_NOT_INSTALLED' // 默往未安装
    }

    return EventType
})

/**
 *   Game Manager For every subGames
 *   这个类可以作为主模块管理所有子模块相关配置的类
 **/
load('public/manager/GameManager', function () {
    let GameManager = cc.Class.extend({
        _gameEnterCfg: null,
        _lastGameID: null,
        _curGameID: null,
        _guildInterface: null,
        ctor: function () {
        },
        /**
         * 注册牌友群的接口对象
         * （方便解耦）
         * @param guildInterface
         */
        registeCuildInterface: function (guildInterface) {
            this._guildInterface = guildInterface
        },
        /**
         * 获得牌友群的接口对象
         * @returns {null}
         */
        getGuildInterface: function () {
            return this._guildInterface
        },
        /**
         * 初始化子模块游戏的入口
         * @param config
         */
        setGameEnterConfig: function (config) {
            this._gameEnterCfg = config
        },
        /**
         * 根据gameID 获取子模块游戏的入口
         * @param gameID
         */
        getGameEnterByID: function (gameID) {
            return this._gameEnterCfg[gameID]
        },
        /**
         * 根据gameID 进入相关的游戏
         * @param gameID
         */
        enterGame: function (gameID) {
            if (!this._gameEnterCfg) {
                cc.error('[GameManager ] this._gameEnterCfg is null!!!')
                return
            }
            let gameIndex = includeSubModule(this.getGameEnterByID(gameID))
            if (gameIndex && gameIndex.enter) {
                gameIndex.enter()
                this._curGameID = gameID
            }
        },
        /**
         * 退出相GameID 的游戏时会主动调用此接口
         */
        leavGame: function () {
            if (!this._curGameID) {
                return
            }
            let gameIndex = includeSubModule(this.getGameEnterByID(this._curGameID))
            if (gameIndex && gameIndex.leave) {
                gameIndex.leave()
            }
        },
        /**
         * 通用的创建房间的接口
         * @param route
         * @param param
         * @param callback
         */
        createRoom: function (param, callback, route) {
            let createRoute = route || 'pkplayer.handler.CreateVipTableV2'
            let skipUpdate = (appInstance.enableHotUpdate() ? (!cc.sys.isMobile) : true)
            appInstance.moduleManager().enterSubModule(param.gameid, function (param) {
                global.View.block()
                appInstance.gameNet().request(createRoute, param, function (rtn) {
                    global.View.unblock()
                    if (callback) {
                        callback(rtn)
                    }
                })
            }, param, skipUpdate)
        },
        /**
         * 通用的加入房间的接口
         * @param route
         * @param param
         * @param callBack
         * @param noMoreCoin 多货币区别（非多货币不能调用getCreatePara）
         */
        joinRoom: function (param, callBack, route, noMoreCoin) {
            let joinRoute = route || 'pkplayer.handler.JoinGame'
            if (noMoreCoin) {
                this.joinGame(joinRoute, param.gameid, param, callBack)
            } else {
                appInstance.gameNet().request('pkplayer.handler.GetCreatePara', param, function (rtn) {
                    if (rtn.result === 0) {
                        this.joinGame(joinRoute, rtn.info, param, callBack)
                    } else {
                        if (callBack) {
                            callBack(rtn)
                        }
                    }
                }.bind(this))
            }
        },

        /**
         * 通用的加入游戏的接口
         * @param joinRoute
         * @param gameid
         * @param param
         * @param callBack
         */
        joinGame: function (joinRoute, gameid, param, callBack) {
            let skipUpdate = (appInstance.enableHotUpdate() ? (!cc.sys.isMobile) : true)
            appInstance.moduleManager().enterSubModule(gameid, function (joinPara) {
                appInstance.gameNet().request(joinRoute, joinPara, function (rtn) {
                    if (callBack) {
                        callBack(rtn)
                    }
                })
            }, param, skipUpdate)
        },

        /**
         * 发送离开房间的协议
         * @param route
         * @param param
         * @param callBack
         */
        requestLeaveRoom: function (callBack, route) {
            let leaveRoute = route || 'pkplayer.handler.LeaveGame'
            appInstance.gameNet().request(leaveRoute, {}, function (rtn) {
                if (rtn.result === 0) {
                    this.leavGame()
                }
                if (callBack) {
                    callBack(rtn)
                }
            }.bind(this))
        },
        /**
         * 再来一局
         * @param createParam
         * @param isGuild
         * @param callBack
         */
        reCreateRoom: function (createParam, isGuild, callBack) {
            if (appInstance.dataManager().isInTable()) {
                this.requestLeaveRoom(function (rtn) {
                    if (rtn.result === 0) {
                        if (isGuild) {
                            this._guildInterface.JoinGuildTable(createParam, function (rtn) {
                                if (callBack) {
                                    callBack(rtn)
                                }
                            })
                        } else {
                            this.createRoom(createParam, function (rtn) {
                                if (callBack) {
                                    callBack(rtn)
                                }
                            })
                        }
                    }
                }.bind(this))
            }
        }
    })
    return GameManager
})

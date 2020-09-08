/**
 *  游戏的资源管理类
 */
load('public/manager/ResManager', function () {
    let resManager = cc.Class.extend({
        _retainSpriteArray: [],
        _moduleRelationMap: {}, // 用于存储
        ctor: function () {
        },
        /**
         * 获得纹理
         * @param key
         */
        getTextureForKey: function (key) {
            return cc.textureCache.getTextureForKey(key)
        },
        /**
         * 判断是否已经加载纹理如果没有添加则添加
         * @param config
         * {plist: 'xxxxxxx.plist', png:'xxxxxxxx.png|.pvr.ccz',retain: true,gameId:xxxx},
         */
        judgeAndLoadTextureForKey: function (config) {
            if (!this.getTextureForKey(config.png)) {
                this.loadSpriteFrame(config)
            }
        },
        /**
         * 异步加载资源
         */
        loadSpriteFrameAsync: function (pathConfig, callBack, target) {
            if (pathConfig.png && pathConfig.plist) {
                let imgPath = this._findImagePath(pathConfig.png)
                cc.textureCache.addImageAsync(imgPath, function (tex) {
                    if (tex && !cc.spriteFrameCache.isSpriteFramesWithFileLoaded(pathConfig.plist)) {
                        this.addSpriteFramesCanRetain(pathConfig.plist, tex, pathConfig.retain)
                        if (pathConfig.gameId) {
                            if (!this._moduleRelationMap[pathConfig.gameId]) {
                                this._moduleRelationMap[pathConfig.gameId] = {}
                            }
                            this._moduleRelationMap[pathConfig.gameId][pathConfig.plist] = pathConfig
                        }
                    }
                    if (callBack) {
                        if (target) {
                            callBack.call(target, pathConfig)
                        } else {
                            callBack(pathConfig)
                        }
                    }
                }.bind(this))
            }
        },
        /**
         * 回去找对应的.png 与.pvr.ccz
         * @private
         */
        _findImagePath: function (path) {
            let resPath = path
            if (!jsb.fileUtils.isFileExist(resPath)) {
                if (cc.path.extname(resPath) === '.png') {
                    resPath = cc.path.changeExtname(resPath, '.pvr.ccz')
                } else if (cc.path.extname(resPath) === '.ccz') {
                    resPath = cc.path.changeExtname(cc.path.changeExtname(resPath, ''), '.png')
                }
            }
            return resPath
        },
        /**
         *  添加精灵帧到缓存
         * @param plist
         * @param texture
         * @param retain   用于给引用计数加1 防止，内存警告的时候被释放
         * @param gameId   用于跟gameID绑定
         */
        addSpriteFramesCanRetain: function (plist, texture, retain) {
            cc.spriteFrameCache.addSpriteFrames(plist, texture)
            if (retain) {
                this._retainSpriteArray.push(plist)
                this._retainOrReleaseSpriteFrame(plist, retain)
            }
        },
        /**
         * 用于清除纹理缓存的时候先释放主动retain 的精灵帧
         */
        releaseAllRetainSprite: function () {
            if (this._retainSpriteArray && this._retainSpriteArray.length > 0) {
                for (let i = 0; i < this._retainSpriteArray.length; i++) {
                    this._retainOrReleaseSpriteFrame(this._retainSpriteArray[i], false)
                }
            }
            this._retainSpriteArray = []
        },
        /**
         * 同步加载资源
         * 支持pvr.ccz
         *
         */
        loadSpriteFrame: function (pathConfig) {
            if (pathConfig.png && pathConfig.plist) {
                let imgPath = this._findImagePath(pathConfig.png)
                let tex = cc.textureCache.addImage(imgPath)
                if (tex && !cc.spriteFrameCache.isSpriteFramesWithFileLoaded(pathConfig.plist)) {

                    this.addSpriteFramesCanRetain(pathConfig.plist, tex, pathConfig.retain)
                    if (pathConfig.gameId) {
                        if (!this._moduleRelationMap[pathConfig.gameId]) {
                            this._moduleRelationMap[pathConfig.gameId] = {}
                        }
                        this._moduleRelationMap[pathConfig.gameId][pathConfig.plist] = pathConfig
                    }
                }
            }
        },
        /**
         * 防止内存警告下释放无用的精灵帧时会被释放掉
         * @param plist
         * @private
         */
        _retainOrReleaseSpriteFrame: function (plist, isRetain) {
            cc.loader.loadTxt(plist, function (err, txt) {
                if (!err) {
                    if (!txt) return
                    let data = cc.plistParser.parse(txt)
                    if (!data) return
                    let frames = data['frames']
                    for (let key in frames) {
                        let frame = cc.spriteFrameCache.getSpriteFrame(key)
                        if (frame) {
                            if (isRetain) {
                                frame.retain()
                            } else {
                                frame.release()
                            }
                        }
                    }
                }
            })
        },
        /**
         * 根据配置去加载资源
         * callBack 存在的时候会使用异步加载的方式，不传就会是同步加载
         * eg: config:[
         *  {plist: 'xxxxxxx.plist', png:'xxxxxxxx.png|.pvr.ccz',retain: true},
         *  {plist: 'xxxxxxx.plist', png:'xxxxxxxx.png|.pvr.ccz',retain: true},
         *  {plist: 'xxxxxxx.plist', png:'xxxxxxxx.png|.pvr.ccz',retain: true}
         * ]
         */
        addResConfig: function (config, callBack) {
            if (!config) {
                return
            }
            if (config instanceof Array) {
                for (let key in config) {
                    let value = config[key]
                    if (callBack) {
                        this.loadSpriteFrameAsync(value, callBack)
                    } else {
                        this.loadSpriteFrame(value)
                    }
                }
            } else {
                if (callBack) {
                    this.loadSpriteFrameAsync(config, callBack)
                } else {
                    this.loadSpriteFrame(config)
                }
            }
        },
        /**
         * 根据name 获取frame
         */
        getSpriteFrame: function (name) {
            return cc.spriteFrameCache.getSpriteFrame(name)
        },
        /**
         * 卸载纹理资源
         * @param config 可以是数组也可以是单个配置
         */
        removeLoadedResByKey: function (config) {
            if (config instanceof Array) {
                for (let i = 0; i < config.length; i++) {
                    let value = config[i]
                    this.removeCachedByName(value)
                }
            } else {
                this.removeCachedByName(config)
            }
        },
        /**
         *  移除单个cach 中的纹理
         */
        removeCachedByName: function (value) {
            if (value.png && value.plist) {
                if (cc.spriteFrameCache.isSpriteFramesWithFileLoaded(value.plist)) {
                    if (value.retain === true) {
                        let index = this._retainSpriteArray.indexOf(value.plist)
                        if (index !== -1) {
                            this._retainSpriteArray.splice(index, 1)
                        }
                        this._retainOrReleaseSpriteFrame(value.plist, false)
                    }
                    cc.spriteFrameCache.removeSpriteFramesFromFile(value.plist)
                    if (value.gameId) {
                        if (this._moduleRelationMap[value.gameId] && this._moduleRelationMap[value.gameId][value.plist]) {
                            delete this._moduleRelationMap[value.gameId][value.plist]
                        }
                    }
                }
                let imgPath = this._findImagePath(value.png)
                if (cc.textureCache.getTextureForKey(imgPath)) {
                    cc.textureCache.removeTextureForKey(imgPath)
                }
            }
        },
        /**
         * 根据gameId 来清除模块相关的纹理
         * @param gameId
         */
        removeCachedByGameID: function (gameId) {
            if (!this._moduleRelationMap[gameId]) {
                return
            }
            for (let key in this._moduleRelationMap[gameId]) {
                let value = this._moduleRelationMap[gameId][key]
                if (value) {
                    this.removeLoadedResByKey(value)
                }
            }
        },
        /**
         * 用于重启时重置数据
         */
        reset: function () {
            this.releaseAllRetainSprite()
            this._moduleRelationMap = []
        }

    })

    return resManager
})

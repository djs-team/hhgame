
/**
 * SubModuleManager
 * 用于操作管理子模块
 *
 **/
load('public/manager/ModuleManager', function () {
    let HotUpdate = include('public/suport/HotUpdate')
    let MODULEKEYS = 'subModuleKeys'
    let ModuleManager = cc.Class.extend({
        _subModuleRemoteInfos: null, // 远端的子模块配置
        _subModulesLocalVer: {}, // 用于存储读取的本地子模块的
        _uiAgent: null,
        _prefixName: 'subModuleVer_', // 用于本地存储的key的前缀
        _mainLocalVer: '', // 用于存储主包的版本号
        _updaterMap: {}, // 用于存储下载器
        _moduleKeys: [],
        _subModuleCache: {}, // 用于缓存子模块中load 到内存中代码
        _curSubModuleName: null, // 用于记录当前模块的键值
        ctor: function () {
            this._moduleKeys = global.localStorage.getDataForKey(MODULEKEYS) || []
            this.initLocalMainVer()
        },
        /**
         * 用于缓存加载到内存中的模块路径到对应 的子模块表中
         * @param modulePath
         */
        loadSubModule: function (modulePath) {
            if (this._curSubModuleName) {
                if (!this._subModuleCache[this._curSubModuleName]) {
                    this._subModuleCache[this._curSubModuleName] = []
                }
                this._subModuleCache[this._curSubModuleName].push(modulePath)
            }
        },
        /**
         * 根据加载的子模块id 来释放已经加载到内存中的模块
         * (注意: 此函数调用只能在文件下载成功后调用)
         * @param moduleName
         */
        removeSubModuleCache: function (moduleName) {
            let curGameEnter = appInstance.gameManager().getGameEnterByID(moduleName)
            let moduleIndex = includeSubModule(curGameEnter)
            if (moduleIndex && moduleIndex.unloaded) {
                moduleIndex.unloaded()
            }

            appInstance.resManager().removeCachedByGameID(moduleName)

            if (this._subModuleCache[moduleName]) {
                for (let i = 0; i < this._subModuleCache[moduleName].length; i++) {
                    let key = this._subModuleCache[moduleName][i]
                    removeModule(key)
                }
                delete this._subModuleCache[moduleName]
            }
        },
        /**
         * 设置主模块的版本号
         * @param ver
         */
        setMainLocalVer: function (ver) {
            this._mainLocalVer = ver
        },
        /**
         * 获得主模块的版本号
         * @returns {string}
         */
        getMainLocalVer: function () {
            return this._mainLocalVer
        },
        /**
         * 设置UI的界面展示的代理类
         * @param uiAgent
         */
        setUIAgent: function (uiAgent) {
            this._uiAgent = uiAgent
        },
        /**
         *  UI开始展示
         */
        uiAgentStart: function () {
            if (this._uiAgent && this._uiAgent.start && typeof (this._uiAgent.start) === 'function') {
                this._uiAgent.start()
            }
        },
        /**
         *  UI 展示结束
         */
        uiAgentEnd: function () {
            if (this._uiAgent && this._uiAgent.end && typeof (this._uiAgent.end) === 'function') {
                this._uiAgent.end()
            }
        },
        /**
         * UI 进度的更新
         * @param percent
         * @param totalSize
         * @param downLoadedSize
         */
        uiAgentUpdate: function (percent, totalSize, downLoadedSize) {
            if (this._uiAgent && this._uiAgent.updateProgress && typeof (this._uiAgent.updateProgress) === 'function') {
                this._uiAgent.updateProgress(percent, totalSize, downLoadedSize)
            }
        },
        /**
         * 更新游戏子模块的配置文件
         */
        updateSubModuleInfos: function (infos, needLoadLocalVer) {
            this._subModuleRemoteInfos = infos
            if (!needLoadLocalVer) {
                return
            }
            for (let moduleName in this._subModuleRemoteInfos) {
                let key = this._prefixName + moduleName
                let version = global.localStorage.getStringForKey(key)
                if (version) {
                    this._subModulesLocalVer[moduleName] = version
                }
            }
        },
        /**
         * 更对应新子模块的本地版本号
         */
        updateSubModuleLocalVer: function (moduleName, version) {
            this._subModulesLocalVer[moduleName] = version
            let key = this._prefixName + moduleName
            global.localStorage.setStringForKey(key, version)
            if (this._moduleKeys.indexOf(key) === -1) {
                this._moduleKeys.push(key)
                global.localStorage.setDataForKey(MODULEKEYS, this._moduleKeys)
            }
        },
        /**
         * 在进入子模块之前把子模块需要预先加载的内容加载进来
         */
        preloadSubModule: function (moduleName) {
            let curGameEnter = appInstance.gameManager().getGameEnterByID(moduleName)
            let moduleIndex = includeSubModule(curGameEnter)
            if (moduleIndex && moduleIndex.preload) {
                moduleIndex.preload()
            }
        },
        /**
         * 用于检测释放需要更新子模块如果需要更新则返回对应的配置
         * @param key
         * @returns {*}
         */
        checkSubModuleUpdate: function (moduleName) {
            let isUpdate = false
            if (this._subModuleRemoteInfos && this._subModuleRemoteInfos[moduleName]) {
                if (!this._subModulesLocalVer[moduleName]) {
                    isUpdate = true
                } else if (this._subModulesLocalVer[moduleName] !== this._subModuleRemoteInfos[moduleName].version) {
                    isUpdate = true
                }
            } else {
                return null
            }
            return { isUpdate: isUpdate, cfg: this._subModuleRemoteInfos[moduleName] }
        },
        startUpdate: function (moduleName, cfg, cb, param) {
            if (this._updaterMap && this._updaterMap[moduleName]) {
                return
            }
            this.uiAgentStart()
            let updater = new HotUpdate(cfg.localManifest, cfg.storagePath, cfg.remoteManifest)
            this._updaterMap[moduleName] = updater
            updater.setProgressCallback(function (percent, totalSize, downLoadedSize) {
                this.uiAgentUpdate(percent, totalSize, downLoadedSize)
            }.bind(this))

            updater.setSuccessCallback(function (isUpdated, curVersion) {
                if (isUpdated) {
                    this.removeSubModuleCache(moduleName)   // 更新完之后强制清一遍对应模块的缓存
                }
                this.uiAgentEnd()
                this.updateSubModuleLocalVer(moduleName, curVersion)
                delete this._updaterMap[moduleName]
                this.preloadSubModule(moduleName)
                if (cb) {
                    cb(param)
                }
            }.bind(this))

            updater.setFailureCallback(function () {
                this.uiAgentEnd()
                delete this._updaterMap[moduleName]
                global.alertMsg('下载失败，请重试。', function () {
                    this.startUpdate(moduleName, cfg, cb, param)
                }.bind(this), function () {
                })
            }.bind(this))
            updater.update()
        },
        /**
         *  检查并进入子模块
         *  moduleName 模块名字
         *  cb 更新完成的回调
         *  param 回调里面的参数
         *  skipCheck 是否跳过更新直接进游戏
         */
        enterSubModule: function (moduleName, cb, param, skipCheck) {
            this._curSubModuleName = moduleName
            if (!skipCheck) {
                let updateCfg = this.checkSubModuleUpdate(moduleName)
                if (updateCfg) {
                    let cfg = updateCfg.cfg
                    if (updateCfg.isUpdate && cfg) {
                        // 增加子模块更新对主模块更新的依赖判断
                        if (cfg.depMainVersion && global.compareVersion(this.getMainLocalVer(), cfg.depMainVersion) < 0) {
                            // 此时需要卡住子模块更新重启开始主模块更新
                            global.alertMsg('发现更新，需要更新后进入。', function () {
                                appInstance.restartGame()
                            })
                            return
                        }

                        this.startUpdate(moduleName, cfg, cb, param)
                    } else {
                        this.preloadSubModule(moduleName)
                        if (cb) {
                            cb(param)
                        }
                    }
                }
            } else {
                this.preloadSubModule(moduleName)
                if (cb) {
                    cb(param)
                }
            }
        },
        /**
         * 获得子模块的本地版本号
         */
        getSubModuleLocalVersion: function (moduleName) {
            return this._subModulesLocalVer[moduleName]
        },
        /**
         * 重置清空模块的环境信息
         */
        clearModuleEnv: function () {
            if (this._subModuleRemoteInfos) {
                this._subModuleRemoteInfos = null
            }
            if (this._moduleKeys) {
                for (let i = 0; i < this._moduleKeys.length; i++) {
                    let key = this._moduleKeys[i]
                    global.localStorage.removeItem(key)
                }
                this._moduleKeys = []
            }
            if (this._subModulesLocalVer) {
                this._subModulesLocalVer = {}
            }
            this._mainLocalVer = ''
        },
        /**
         * 初始化本地主模快版本号
         */
        initLocalMainVer: function() {
            let am = new jsb.AssetsManager('res/project.manifest',  'update')
            this.setMainLocalVer(am.getLocalManifest().getVersion() || '1.0.0')
        }

    })

    return ModuleManager
})
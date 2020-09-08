/**
 * 模块化加载的封装
 */

(function () {
    let Module = {}
    let ModuleStates = {}
    let ModuleProxies = {}

    let loadState = {
        Loading: 1,
        Loaded: 2
    }

    /**
     * 增加这个区分是为了尽可能少的设置搜索路径，通过类型来自动补齐全路径
     * @type {{MainModule: number, SubModule: number}}
     */
    window._LoadType = {
        MainModule: 1,
        SubModule: 2
    }

    /**
     * 根据类型自动补齐
     * @param name
     * @param moduleType
     * @returns {string}
     */
    let autoPath = function (name, loadType = window._LoadType.MainModule) {
        if (loadType === window._LoadType.MainModule) {
            return 'src/' + name
        } else if (loadType === window._LoadType.SubModule) {
            return 'modules/' + name
        }
        return name
    }


    /**
     * 把所有Module的信息全打印出来
     */
    window.dumpModuleInfo = function () {
        cc.log('******************************[Module]*********************************')
        for (let key in Module) {
            let info = cc.formatStr('[%s]', key)
            if (ModuleStates[key] === loadState.Loading) {
                info += ' is loading'
            }
            cc.log(info)
        }
        cc.log('***********************************************************************')
    }

    /**
     * 删除子模块的对像, 不是清理资源, js/png资源等依然存在在内存中!
     * @param name
     */
    window.removeModule = function (name) {
        if (Module[name] && ModuleProxies[name] && ModuleStates[name]) {
            let path = name + '.js'
            delete (Module[name])
            Module[name] = null

            delete (ModuleProxies[name])
            ModuleProxies[name] = null

            delete (ModuleStates[name])
            ModuleStates[name] = null
            __cleanScript(path)
            cc.log('[Module] The module [%s] is removed', name)
        }
    }
    /**
     * 子模块的对像加载
     * @param name
     * @param func
     */
    window.load = function (name, func, loadType) {
        try {
            name = autoPath(name, loadType)
            if (loadType && loadType === window._LoadType.SubModule) {
                if (appInstance && appInstance.moduleManager()) {
                    appInstance.moduleManager().loadSubModule(name)
                }
            }
            ModuleStates[name] = loadState.Loading
            let module = func()
            Module[name] = module
            let proxy = ModuleProxies[name]
            proxy.module = module
            proxy.prototype = module.prototype
            // eslint-disable-next-line no-proto
            // proxy.__proto__ = module
            Object.setPrototypeOf(proxy, module)
        } catch (e) {
            let errorInfo = cc.formatStr('[load module: %s] %s.', name, e.toString())
            throw new Error(errorInfo)
        }
    }

    // load一个子模块类
    window.loadSubModule = function (name, func) {
        window.load(name, func, window._LoadType.SubModule)
    }
    /**
     * 需要哪个js文件的时候,传入路径再require
     * @param name
     * @returns {*|Function|*}
     */
    window.include = function (name, loadType) {
        try {
            name = autoPath(name, loadType)
            let state = ModuleStates[name]
            if (state === loadState.Loaded) {
                // the module is loaded, return the proxy
                // cc.log('[Module] The module [%s] is Loaded', name)
                return ModuleProxies[name]
            } else {
                // an interface function (use this function as a constructor of cc.Class or derived class)
                let proxy = ModuleProxies[name] || function () {
                    // eslint-disable-next-line no-caller
                    if (typeof (arguments.callee.module) === 'function') {
                        // eslint-disable-next-line no-caller
                        return arguments.callee.module.apply(this, arguments)
                    } else {
                        cc.warn('[Module] The module [%s] is not valid.', name)
                    }
                }
                ModuleProxies[name] = proxy
                if (ModuleStates[name] === loadState.Loading) {
                    // the module is loading, return the proxy function
                    return proxy
                } else { // Module is not loading or loaded
                    let path = name + '.js'
                    ModuleStates[name] = loadState.Loading
                    require(path)
                    ModuleStates[name] = loadState.Loaded
                    proxy.module = Module[name]
                    proxy.prototype = Module[name].prototype
                    // eslint-disable-next-line no-proto
                    Object.setPrototypeOf(proxy, Module[name])
                    return proxy
                }
            }
        } catch (e) {
            let errorInfo = cc.formatStr('[include module: %s] %s [line: %d].\nstack: %s ', name, e.toString(), e.lineNumber, e.stack)
            throw new Error(errorInfo)
        }
    }

    // 加载一个子模块类
    window.includeSubModule = function (name) {
        return window.include(name, window._LoadType.SubModule)
    }
})()

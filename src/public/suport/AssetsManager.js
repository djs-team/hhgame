load('public/suport/AssetsManager', function () {
    let AssetsManager = cc.Class.extend({
        _am : null,
        _progress : null,
        _percent : 0,
        _percentByFile : 0,
        _loadingBar : null,
        _fileLoadingBar : null,
        _callback : null,
        cb: function(event) {
            switch (event.getEventCode())
            {
                case jsb.EventAssetsManager.ERROR_NO_LOCAL_MANIFEST:
                    cc.log("No local manifest file found, skip assets update.");
                    break;
                case jsb.EventAssetsManager.UPDATE_PROGRESSION:
                    this._percent = event.getPercent();
                    this._percentByFile = event.getPercentByFile();

                    var msg = event.getMessage();
                    if (msg) {
                        cc.log(msg);
                    }
                    cc.log(this._percent + "%");
                    break;
                case jsb.EventAssetsManager.ERROR_DOWNLOAD_MANIFEST:
                case jsb.EventAssetsManager.ERROR_PARSE_MANIFEST:
                    cc.log("Fail to download manifest file, update skipped.");
                    break;
                case jsb.EventAssetsManager.ALREADY_UP_TO_DATE:
                case jsb.EventAssetsManager.UPDATE_FINISHED:
                    cc.log("Update finished. " + event.getMessage());
                    cc.log('============event.getEventCode()================' + event.getEventCode())
                    // Restart the game to update scripts in scene 3
                    if (currentScene == 2) {

                        // Register the manifest's search path
                        var searchPaths = this._am.getLocalManifest().getSearchPaths();
                        // This value will be retrieved and appended to the default search path during game startup,
                        // please refer to samples/js-tests/main.js for detailed usage.
                        // !!! Re-add the search paths in main.js is very important, otherwise, new scripts won't take effect.
                        cc.sys.localStorage.setItem("Scene3SearchPaths", JSON.stringify(searchPaths));
                        // Restart the game to make all scripts take effect.
                        cc.game.restart();
                    }
                    else {
                        cc.log('============更新完成后  切换场景')
                    }
                    break;
                case jsb.EventAssetsManager.UPDATE_FAILED:
                    cc.log("Update failed. " + event.getMessage());

                    __failCount ++;
                    if (__failCount < 5)
                    {
                        this._am.downloadFailedAssets();
                    }
                    else
                    {
                        cc.log("Reach maximum fail count, exit update process");
                        __failCount = 0;
                        cc.log('============更新失败后  切换场景')
                    }
                    break;
                case jsb.EventAssetsManager.ERROR_UPDATING:
                    cc.log("Asset update error: " + event.getAssetId() + ", " + event.getMessage());
                    break;
                case jsb.EventAssetsManager.ERROR_DECOMPRESS:
                    cc.log(event.getMessage());
                    break;
                default:
                    break;
            }
        },

        checkUpdate : function () {
            var manifestPath = sceneManifests[currentScene];
            var storagePath = ((jsb.fileUtils ? jsb.fileUtils.getWritablePath() : "/") + 'update');
            cc.log("Storage path for this test : " + storagePath);

            this._am = new jsb.AssetsManager(manifestPath, storagePath);
            this._am.retain();

            if (!this._am.getLocalManifest().isLoaded())
            {
                cc.log("Fail to update assets, step skipped.");
            }
            else
            {
                this._callback = this.cb.bind(this);
                var listener = new jsb.EventListenerAssetsManager(this._am, this._callback);

                cc.eventManager.addListener(listener, 1);

                this._am.update();
                cc.log('========beginUpdate======')
            }

        }
    })
    return AssetsManager
})
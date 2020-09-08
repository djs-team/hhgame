
if (typeof Object.create !== 'function') {
    Object.create = function (o) {
        function F () {
        }

        F.prototype = o
        return new F()
    }
}

require('src/public/init/Global.js')
require('src/public/init/LocalStorage.js')
require('src/public/init/GameFile.js')
require('src/public/init/View.js')
require('src/public/init/Node.js')
require('src/public/init/Load.js')

require('src/public/network/index.js')

let app = include('public/App')
window.appInstance = window.appInstance || new app()
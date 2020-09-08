
load('game/ui/layer/choosecity/ChooseCityLayer', function () {
    let ResConfig = include('game/config/ResConfig')
    let BaseLayer = include('public/ui/BaseLayer')
    let Layer = BaseLayer.extend({
        _className: 'ChooseCityLayer',
        RES_BINDING: function () {
            return {
                'pnl/closeBtn': { onClicked: this.onCloseClick },
                'pnl/searchBtn': { onClicked: this.onsearchBtnClick },
                'pnl/kefuBtn': { onClicked: this.onkefuBtnClick },
                'pnl/copyBtn': { onClicked: this.oncopyBtnClick },
                'pnl/leftListView': { },
                'pnl/cityListView': { },
                'pnl/cityCell': { },
                'pnl/cityListCell': { },
                'pnl/leftCell': { }
            }
        },
        _city: [
            { name: '黑龙江省', cell: [
                    { name: '肇源麻将', _sendMsg: {channel: 10047}},
                    { name: '大庆麻将', _sendMsg: {channel: 10047}}
                ] }
        ],
        ctor: function () {
            this._super(ResConfig.View.ChooseCityLayer)
            this.initData()
            this.initView()
        },

        initData: function () {
            this._leftIndex = 0
            this._leftCell = []
        },

        initView: function () {

            this.leftListView.setItemModel(this.leftCell)
            this.cityListView.setItemModel(this.cityListCell)

            this.leftCell.setVisible(false)
            this.cityCell.setVisible(false)
            this.cityListCell.setVisible(false)

            this.initLeftList()
            this.updateLeftList()
            this.updateCellList()
        },

        initLeftList: function () {
            for (let index = 0; index < this._city.length; ++index) {
                this.initLeftListCell(index)
            }
        },

        initLeftListCell: function (index) {
            let cell = this._leftCell[index] = this.leftCell.clone()
            cell.setVisible(true)
            this.leftListView.pushBackCustomItem(cell)
            cell._leftIndex = index

            let data = this._city[index]
            let nameNd = cell.getChildByName('name')
            nameNd.setString(data.name)

            cell.addClickEventListener(function(sender, et) {
                this.onLeftBtnClick(sender)
            }.bind(this))
        },

        onLeftBtnClick: function (sender) {
            this._leftIndex = sender._leftIndex
            this.updateLeftList()
            this.updateCellList()
        },

        setLeftCellStatus: function (leftCell, isSelect) {
            let common = leftCell.getChildByName('common')
            let select = leftCell.getChildByName('select')
            if (isSelect) {
                common.setVisible(false)
                select.setVisible(true)
            } else {
                common.setVisible(true)
                select.setVisible(false)
            }
        },

        updateLeftList: function () {
            for (let i = 0; i < this._leftCell.length; ++i) {
                this.setLeftCellStatus(this._leftCell[i], i === this._leftIndex)
            }
        },

        updateCellList: function () {
            this.cityListView.removeAllChildren()
            let data = this._city[this._leftIndex].cell

            let listNum = Math.ceil(data.length / 4)
            for (let i = 0; i < listNum; ++i) {
                let listPnl = this.cityListCell.clone()
                listPnl.setVisible(true)
                this.cityListView.pushBackCustomItem(listPnl)
                for (let cellIndex = 0; cellIndex < 4; ++cellIndex) {
                    let dataIndex = 4 * i + cellIndex
                    let cellData = data[dataIndex]
                    if (cellData) {
                        this.initCityCell(listPnl, cellIndex, data[dataIndex])
                    }
                }
            }
        },

        initCityCell: function (listPnl, cellIndex, cellData) {
            let cell = this.cityCell.clone()
            cell.setVisible(true)
            listPnl.addChild(cell)
            cell.setPositionY(0)
            cell.setPositionX(160 * cellIndex)

            let nameNd = cell.getChildByName('name')
            nameNd.setString(cellData.name)
            cc.log('======cellData==' + cellData.name)

            cell._sendMsg = cellData._sendMsg

            cell.addClickEventListener(function(sender, et) {
                this.onCityCellClick(sender)
            }.bind(this))
        },

        onCityCellClick: function (sender) {
            appInstance.gameAgent().httpGame().chooseCity(sender._sendMsg)
        },

        onsearchBtnClick: function () {

        },

        onkefuBtnClick: function () {

        },

        oncopyBtnClick: function () {

        },

        onEnter: function () {
            this._super()
        },
        onExit: function () {
            this._super()
        },
        onCloseClick: function () {
            appInstance.uiManager().removeUI(this)
        }
    })
    return Layer
})

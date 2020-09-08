
load('module/mahjong/data/TableData', function () {
  let BaseTableData = include('game/data/BaseTableData')
  let TableData = BaseTableData.extend({
    ctor: function () {
      this.pMustOutCard = []
    }
  })
  return TableData
})

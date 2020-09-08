
/**
 *  TaskMdt Mediator
 *
 */
load('game/ui/layer/role/RoleMdt', function () {
    let Mediator = include('public/components/Mediator')
    let GameEvent = include('game/config/GameEvent')
    let GameUtil = include('game/public/GameUtil')
    let GameConfig = include('game/config/GameConfig')
    let mdt = Mediator.extend({
        mediatorName: 'RoleMdt',
        ctor: function (view) {
            this._super(this.mediatorName,view)
        },
        getNotificationList: function () {
            return [

                GameEvent.ROLES_GET,
                GameEvent.ROLES_BUY,
                GameEvent.ROLES_SELECT,
                GameEvent.UPDATE_PROPSYNC,

            ]
        },
        handleNotification: function (notification) {
            let name = notification.getName()
            let body = notification.getBody()
            switch (name) {
                case GameEvent.ROLES_GET:
                    this.onInitRolesData(body)
                    break
                case GameEvent.ROLES_BUY:
                    this.onInitRolesData(body)
                    break
                case GameEvent.ROLES_SELECT:
                    this.onReceiveRoleSelectedResule(body)
                    break
                case GameEvent.UPDATE_PROPSYNC:
                    this.view.onUpdatePropsData(body)
                    break

                default:
                    break

            }
        },


        onRegister: function () {

            this.initView()
        },

        onRemove: function () {
        },

        initView: function () {

            let msg = {}
            appInstance.gameAgent().httpGame().GETROLESReq(msg)

            let data = {
                'coin' : appInstance.dataManager().getUserData().coin,
                'diamonds' : appInstance.dataManager().getUserData().diamonds
            }

            this.view.onUpdatePropsData(data)

        },


        onInitRolesData: function (body) {

            let data = {}
            data.myRoles = []
            data.allRoles = []
            if(body.indexOf('showRoleCode') == -1)
                body.showRoleCode = 1

            data.showRoleCode = body.showRoleCode
            for(let i = 0; i < body.allRole.length; i++){

                let role = body.allRole[i]
                let roleGetInfo = role[rgInfoList][0]
                let roleData = {}

                roleData.getId = role.getId
                roleData.status = role.status
                roleData.roleCode = role.roleCode
                roleData.roleName = role.roleName
                roleData.currency = role.currency
                roleData.get_type = role.get_type
                roleData.code = role.code
                roleData.num = role.num
                roleData.dueReminderText = '剩余时间：无限制'


                roleData.propType = GameConfig.propType_role
                let needKeysArrayName = [
                    GameConfig.ICON_RESULT_BRIGHT,
                    GameConfig.ICON_RESULT_ASH,
                    GameConfig.ICON_RESULT_CURRENCY,
                ]
                GameUtil.getRoleData(roleData,needKeysArrayName,'propType','roleCode')


                if(roleGetInfo.get_type != 1){
                    this.formatDate(roleData,role.surplusTime)
                    let vipLvel

                    switch (role.code) {
                        case GameConfig.VIP_LEVEL_1:
                            vipLvel = '周'
                            break
                        case GameConfig.VIP_LEVEL_2:
                            vipLvel = '月'
                            break
                        case GameConfig.VIP_LEVEL_3:
                            vipLvel = '季'
                            break
                        case GameConfig.VIP_LEVEL_4:
                            vipLvel = '年'
                            break
                        default:
                            break


                    }
                    roleData.levelName = vipLvel + 'VIP专属角色'
                }

                data.allRoles.push(roleData)
                if(roleData.status == 1){
                    data.myRoles.push(roleData)
                }

            }

            this.view.onInitRolesData(data)

        },




        formatDate: function (data,timeStamp) {

            let days = parseInt(timeStamp/24)
            let hours = parseInt(timeStamp%24)
            data.dueReminderText = '剩余时间：' + days + '天' + hours + '小时'

        },


    })
    return mdt
})
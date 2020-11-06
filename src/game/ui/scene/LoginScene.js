/**
 * LoginScene
 */
load('game/ui/scene/LoginScene', function () {
    let BaseScene = include('public/ui/BaseScene')
    let ResConfig = include('game/config/ResConfig')
    let LoginMdt = include('game/ui/scene/LoginMdt')
    let LocalSave = include('game/public/LocalSave')
    let GameUtil = include('game/public/GameUtil')
    let LoginScene = BaseScene.extend({
        _className: 'LoginScene',
        _canLogin: true,
        _isLookAgree: false,
        _userAgreeContent: [
            {'title':'1.重要须知','content':'1.1 根据《网络游戏管理暂行规定》（文化部令第49号），文化部制定《网络游戏服务格式化协议必备条款》规定特制定本协议。本协议中甲方为《麻将情缘》，乙方为网络游戏用户。请用户仔细认真阅读、充分理解本《协议》中的各个条款。特别涉及免除或者限制《麻将情缘》责任的免责条款，对用户的权利限制的条款，法律适用、争议解决方式的条款。换行' +
                    '1.2 请您审慎阅读并选择同意或不同意本《协议》，除非您接受本《协议》所有条款，否则您无权下载、安装、升级、登陆、显示、运行、截屏等方式使用本软件及其相关服务。您的下载、安装、显示、帐号获取和登录、截屏等行为表明您自愿接受本协议的全部内容并受其约束，不得以任何理由包括但不限于未能认真阅读本协议等作为纠纷抗辩理由。换行' +
                    '1.3 本《协议》可由《麻将情缘》随时更新，更新后的协议条款一旦公布即代替原来的协议条款，不再另行个别通知。您可重新下载安装本软件或网站查阅最新版协议条款。在《麻将情缘》修改《协议》条款后，如果您不接受修改后的条款，请立即停止使用《麻将情缘》提供的软件和服务，您继续使用《麻将情缘》提供的软件和服务将被视为已接受了修改后的协议。' +
                    '1.4 本《协议》内容包括但不限于本协议以下内容，针对某些具体服务所约定的管理办法、公告、重要提示、指引、说明等均为本协议的补充内容，为本协议不可分割之组成部分，具有本协议同等法律效力，接受本协议即视为您自愿接受以上管理办法、公告、重要提示、指引、说明等并受其约束;否则请您立即停止使用《麻将情缘》提供的软件和服务。', 'num': 4},
            {'title':'2. 特殊规定','content':'2.1 未满十八周岁的未成年人应经其监护人陪同阅读本服务条款并表示同意，方可接受本服务条款。监护人应加强对未成年人的监督和保护，因其未谨慎履行监护责任而损害未成年人利益或者影响《麻将情缘》利益的，应由监护人承担责任。换行' +
                    '2.2 青少年用户应遵守全国青少年网络文明公约∶要善于网上学习，不浏览不良信息;要诚实友好交流，不侮辱欺诈他人;要增强自护意识，不随意约会网友;要维护网络安全，不破坏网络秩序;要有益身心健康，不沉溺虚拟时空。', 'num': 3},
            {'title':'3. 权利声明','content':'3.1 《麻将情缘》拥有向最终用户提供内容服务的、网址中包含hehegames.cn的互联网网站、以及《麻将情缘》网络游戏平台，《麻将情缘》手机游戏及其服务器端、最终客户端程序、文档的（包括上述内容的升级、改进版本）的所有权和一切知识产权，包括但不限于∶换行' +
                    '3.1.1 《麻将情缘》软件及其他物品的著作权、版权、名称权、商标权以及由其派生的各项权利;换行' +
                    '3.1.2 《麻将情缘》中的电子文档、文字、数据库、图片、图标、图示、照片、程序、音乐、色彩、版面设计、界面设计等可从作品中单独使用的作品元素的著作权、版权、名称权、商标权以及由其派生的各项权利;换行' +
                    '3.1.3 《麻将情缘》向用户提供服务过程中所产生并存储于《麻将情缘》系统中的任何数据（包括但不限于账号、游戏币、游戏道具等游戏数据）的所有权。换行' +
                    '3.1.4 用户在使用《麻将情缘》游戏过程中产生的电子文档、文字、图片、照片、色彩、游戏界面等可以单独使用的游戏元素，以及由其形成的截屏、录像、录音等衍生品的各项权利。' +
                    '3.2 上述权利《麻将情缘》书面授权用户以非商业、不损害《麻将情缘》利益的目的临时的、有限的、不可转让的使用权。用户不得为商业运营目的安装、使用、运行《麻将情缘》，不可以对该软件或者该软件运行过程中释放到任何计算机终端内存中的数据及该软件运行过程中客户端与服务器端的交互数据进行复制、更改、修改、挂接运行或创作任何衍生作品，形式包括但不限于使用截屏、插件、换行' +
                    '外挂或非经授权的第三方工具/服务接入本"软件"和相关系统。换行' +
                    '3.3 未经《麻将情缘》书面同意，用户以任何营利性、非营利性或损害《麻将情缘》利益的目的实施以下几种行为的，《麻将情缘》保留追究上述未经许可行为一切法律责任的权利，给《麻将情缘》造成经济或名誉上损失的，《麻将情缘》有权根据相关法律法规另行要求赔偿，情节严重涉嫌犯罪的，《麻将情缘》将提交司法机关追究刑事责任∶换行' +
                    '3.3.1 进行编译、反编译等方式破解该软件作品的行为;换行' +
                    '3.3.2 利用技术手段破坏软件系统或者服务器的行为;换行' +
                    '3.3.3 利用游戏外挂、作弊软件、系统漏洞侵犯《麻将情缘》利益的行为;换行' +
                    '3.3.4 对游戏进行截屏、录像或利用游戏中数据、图片、截屏进行发表的行为;换行' +
                    '3.3.5 制作游戏线下衍生品的行为;换行' +
                    '3.3.6 其他严重侵犯《麻将情缘》知识产权的行为。', 'num': 14},
            {'title':'4. 用户基本权利和责任','content':'4.1用户享有由《麻将情缘》根据实际情况提供的各种服务，包括但不限于线上游戏、网上论坛、举办活动等。在某些情况下，《麻将情缘》许可用户以其账号登录或使用《麻将情缘》合作方运营的产品或服务。换行' +
                    '4.2 用户可以通过购买游戏道具的方式获得《麻将情缘》的服务。用户购买道具至固定账号后，未经《麻将情缘》书面同意不得将道具再转至其他账号。换行' +
                    '4.3 用户认为自己在游戏中的权益受到侵害，有权根据《麻将情缘》相关规定进行投诉申诉。换行' +
                    '4.4 用户有权对《麻将情缘》的管理和服务提出批评、意见、建议，有权就客户服务相关工作向客服提出咨询。换行' +
                    '4.5 用户在《麻将情缘》的游戏活动应遵守中华人民共和国法律、法规，遵守《麻将情缘》的相关管理规定（包括但' +
                    '不限于管理办法、禁止性和限制性行为等），并自行承担因游戏活动直接或间接引起的一切法律责任。换行' +
                    '4.6 用户有权自主选择依照游戏设定的方式和规则进行竞赛或游戏，对其游戏活动承担相应责任和由此产生的损失，包括经济损失和精神损害。《麻将情缘》就用户的游戏的行为、活动、交易或利用《麻将情缘》进行的非法活动不承担任何责任。换行' +
                    '4.7 用户需遵守网络道德，注意网络礼仪，做到文明上网。换行' +
                    '4.8 《麻将情缘》仅提供相关的网络服务，除此之外与相关网络服务有关的上网设备（如电脑、调制解调器及其他互联网接入装置）及所需的费用（如为接入互联网而支付的电话费、上网费）均应由用户自行负责，，亦不能通过任何第三方进行兑换，游戏输赢以当局积分为准，但该积分或输赢在网络平台运营商或第三方处均不能兑换货币或实物或其他服务.换行' +
                    '4.9 用户必须严格中华人民共和国共和国境内一切法律规定，严禁利用本游戏从事包括赌博、诈骗在内的违法犯罪活动，如经发现查证属实，用户利用"《麻将情缘》从事赌博、诈骗等违法犯罪活动的，情节较轻的注销ID，禁止进入游戏，情节严重的，移送司法机关处理。玩家之间利用本游戏从事违反犯罪行为，由其自行承担法律责任，《麻将情缘》运营商无关。本游戏反对进行赌博，禁止利用游戏输赢进行线上线下的赌资结算。', 'num': 10},
            {'title':'5. 账号注册','content':'5.1 用户承诺按照《麻将情缘》的要求提供个人注册信息。换行' +
                    '社会道德风尚和信息真实性等七条底线;不得有明确或者隐含非法、骚扰性、侮辱性、恐吓性、伤害性、破坏性、挑衅性、庸俗性、淫秽色情性等内容的文字以及其他侵犯他人合法利益和人格尊严的文字或图样;不得冒用、关联机构或社会名人注册账号名称。换行' +
                    '5.2 《麻将情缘》认为必要时，有权要求用户提供国家相关机关的书面证明文件或公证机关的公证书以证明其身份证件的真实性，由此所产生之通讯费、邮寄费、交通费等一切费用均由用户自行承担。换行' +
                    '5.3 个人资料发生变更，如用户迟延更新个人注册信息导致影响《麻将情缘》对该用户提供服务，则由此产生的全部责任均由用户承担换行' +
                    '5.4 账号或昵称，以及头像、个人简介等注册信息须遵守公共秩序和善良风俗，遵守法律法规、社会主义制度、国家利益、公民合法权益、公共秩序、社会道德风尚和信息真实性等七条底线;不得有明确或者隐含非法、骚扰性、侮辱性、恐吓性、伤害性、破坏性、挑衅性、庸俗性、淫秽色情性等内容的文字以及其他侵犯他人合法利益和人格尊严的文字或图样;不得冒用、关联机构或社会名人注册账号名称。换行' +
                    '5.5 注册及使用账号，不得有下列情形∶换行' +
                    '5.5.1 违反宪法或法律法规规定的;换行' +
                    '5.5.2 危害国家安全，泄露国家秘密，颠覆国家政权，破坏国家统一的;换行' +
                    '5.5.3 损害国家荣誉和利益的，损害公共利益的;换行' +
                    '5.5.4 煽动民族仇恨、民族歧视，破坏民族团结的;换行' +
                    '5.5.5 破坏国家宗教政策，宣扬邪教和封建迷信的;换行' +
                    '5.5.6 散布谣言，扰乱社会秩序，破坏社会稳定的;换行' +
                    '5.5.7 散布淫秽、色情、赌博、暴力、凶杀、恐怖或者教唆犯罪的;换行' +
                    '5.5.8 侮辱或者诽谤他人，侵害他人合法权益的;换行' +
                    '5.5.9 含有法律、行政法规禁止的其他内容的。换行' +
                    '5.6 违反第5.5条的，《麻将情缘》有权酌情采取通知限期改正、暂停使用、注销登记等措施;若情节严重的，《麻将情缘》将会向互联网信息内容主管部门报告，由此产生的全部后果均由用户承担。换行' +
                    '5.7 异常注册、异常登录（如利用非法的软件、外挂进行注册与登录）的账号会被列入《麻将情缘》黑名单中，由此产生的全部后果换行' +
                    '均由用户承担。换行' +
                    '5.8 账户所有权归属于《麻将情缘》，用户享有该账户在游戏运营期间的使用权。', 'num': 18},
            {'title':'6. 用广账号固用与保管','content':'6.1《麻将情缘》有权审查用户注册所提供的身份信息是否真实、有效，并应积极地采取技术与管理等合理措施保障用户账号的安全、有效;用户有义务妥善保管其账号及密码，并根据《麻将情缘》的要求正确、安全地使用其账号及密码。因黑客行为或用户保管疏忽等非《麻将情缘》过错导致帐号、密码遭他人非法使用，《麻将情缘》不承担任何责任。换行' +
                    '6.2 用户对登录后所持账号产生的行为依法享有权利和承担责任。换行' +
                    '6.3 用户发现其账号或密码被他人非法使用或有使用异常的情况的，应及时根据《麻将情缘》公布的账号申述规则通知《麻将情缘》，并有权通知《麻将情缘》采取措施暂停该账号的登录和使用。换行' +
                    '6.4 《麻将情缘》根据用户的通知采取措施暂停用户账号的登录和使用的，《麻将情缘》有权要求用户提供并核实与其注册身份信息相一致的个人有效身份信息。用户没有提供其个人有效身份证件或者用户提供的个人有效身份证件与所注册的身份信息不一致的，《麻将情缘》公司有权拒绝用户上述请求。', 'num': 5},
            {'title':'7. 用户信息保护','content':'7.1《麻将情缘》将保护用户提供的有效个人信息数据的安全。不对外向任何第三方提供、公开或共享用户注册资料中的姓名、个人有效身份证件号码、联系方式、家庭住址等个人身份信息，但下列情况除外∶换行' +
                    '7.1.1 用户或用户监护人授权披露的;换行' +
                    '7.1.2 有关法律要求披露的;换行' +
                    '7.1.3 司法机关或行政机关基于法定程序要求提供的;换行' +
                    '7.1.4 《麻将情缘》为了维护自己合法权益而披露;换行' +
                    '7.1.5 应用户监护人的合法要求而提供用户个人身份信息时。换行' +
                    '7.2 《麻将情缘》要求用户提供与其个人身份有关的信息资料时，已事先以明确而易见的方式向用户公开其隐私保护政策和个人信息利用政策，并采取必要措施保护用户的个人信息资料的安全。', 'num': 8},
            {'title':'8. 服务的中上与终止','content':'8.1 用户实施或有重大可能实施以下行为的，《麻将情缘》有权中止对其部分或全部服务，中止提供服务的方式包括但不限于暂停对该账号的登录和使用、暂时禁止使用充值服务、暂时禁止兑换相应奖品、降低或者清除账号中的积分、游戏道具等、暂时禁止使用论坛服务∶换行' +
                    '8.1.1 私下进行买卖游戏道具的行为;换行' +
                    '8.1.2 提供虚假注册身份信息的行为;换行' +
                    '8.1.3 游戏中合伙作弊，尚未对其他用户利益造成严重影响的行为;换行' +
                    '8.1.4 发布不道德信息、广告、言论、辱骂骚扰他人，扰乱正常的网络秩序和游戏秩序的行为;换行' +
                    '8.1.5 实施违反本协议和相关规定、管理办法、公告、重要提示，对《麻将情缘》和其他用户利益造成损害的其他行为。换行' +
                    '8.2 用户实施或有重大可能实施以下不正当行为的，《麻将情缘》有权终止对用户提供服务，终止提供服务的方式包括但不限于永久性的删除该账号、发表的帖子、留言、将非法所得的积分和荣誉道具清零∶换行' +
                    '8.2.1 发布违法信息、严重违背社会公德、以及其他违反法律禁止性规定的行为;换行' +
                    '8.2.2 利用《麻将情缘》进行赌博活动的行为;换行' +
                    '8.2.3 涉嫌买卖偷盗的虚拟财产、游戏道具的行为;换行' +
                    '8.2.4 游戏中合伙作弊对其他用户利益造成严重影响的行为;换行' +
                    '8.2.5 用非法手段盗取其他用户账号和虚拟财产、游戏道具的行为;换行' +
                    '8.2.6 论坛、游戏中传播非法讯息、木马病毒、外挂软件等的行为;换行' +
                    '8.2.7 利用游戏作弊工具或者外挂、游戏bug获取非法利益，严重侵犯《麻将情缘》利益的行为;换行' +
                    '8.2.8 发布不道德信息、广告、言论、辱骂骚扰他人，严重扰乱正常的网络秩序和游戏秩序的行为;换行' +
                    '8.2.9 实施违反本协议和相关规定、管理办法、公告、重要提示，对《麻将情缘》和其他用户利益造成严重损害的其他行为。换行' +
                    '8.3 本协议中未涉及到的禁止或限制性行为及处罚规则，由《麻将情缘》针对具体服务制定相关规定、管理办法、公告、重要提示、指引、说明等，视为本协议之补充协议，为本协议不可分割之组成部分，具有本协议同等法律效力，接受本协议即视为您自愿接受相关规定、管理办法、公告、重要提示、指引、说明等并受其约束。', 'num': 19},
            {'title':'9. 免责条款','content':'9.1 用户之间因线上游戏行为所发生或可能发生的任何心理、生理上的伤害和经济上的损失，《麻将情缘》不承担任何责任。换行' +
                    '9.2 用户因其个人原因造成账号资料保管不妥而导致个人信息数据被他人泄露或账号中虚拟财产、游戏道具被盗或损失的，《麻将情缘》不承担任何责任。换行' +
                    '9.3 用户因除了按游戏规则进行游戏的行为外的其他行为触犯了中华人民共和国法律法规的，责任自负，《麻将情缘》不承担任何责任。换行' +
                    '9.4 用户账号长期不使用的，《麻将情缘》有权进行回收，因此带来的用户个人信息数据丢失、账户内虚拟财产和游戏道具清零等一切损失由用户个人承担，《麻将情缘》不承担任何责任。换行' +
                    '9.5 用户因违反本协议8.1、8.2条款而被《麻将情缘》采取处罚措施所产生的一切损失包括但不限于虚拟货币、积分、荣誉被清零、道具失效或其他损失，均由用户个人承担，《麻将情缘》不承担任何责任。换行' +
                    '9.6 基于网络环境的复杂性，《麻将情缘》不担保服务一定能满足用户的要求，也不保证各项服务不会中断，对服务的及时性、安全性也不作担保。因网络安全、网络故障问题和其他用户的非法行为给用户造成的损失，《麻将情缘》不承担任何责任。换行' +
                    '9.7 基于网络环境的特殊性，《麻将情缘》不担保对用户限制性行为和禁止性行为的判断的准确性，用户因此产生的任何损失《麻将情缘》公司不承担任何责任，用户可按《麻将情缘》相关规定进行申诉解决。换行' +
                    '9.8 《麻将情缘》不保证您从第三方获得的《麻将情缘》虚拟货币、游戏道具（金币、房卡）等游戏物品能正常使用，也不保证该等物品不被索回，因私下购买虚拟货币、游戏道具（金币、房卡）等游戏物品所产生的一切损失均由用户承担，《麻将情缘》不承担任何责任。', 'num': 9},
            {'title':'10. 法律适用和争议解','content':'10.1本协议的订立、效力、解释、履行和争议的解决均适用中华人民共和国法律。因本协议所产生的以及因履行本协议而产生的任何争议，双方均应本着友好协商的原则加以解决。协商解决未果，任何一方有权向人民法院提请审理。', 'num': 2},
            {'title':'11. 其他','content':'11.1 不弃权原则。除非得到双方签字盖章的书面形式证明，否则，不得对本协议任何条款进行修改、修订或放弃。任何一方未能按照本协议规定行使权力或进行补救或延误进行，不得视为该方放弃行使该种权力，除非本协议另有明文规定。换行' +
                    '11.2 可分割性。如果本协议的任何条款违法，该条款将被修改和解释，以在法律允许的最大限度内，最好地实现原条款的目标，同时本协议的其余条款将继续保留其全部效力。', 'num': 3},
        ],
        RES_BINDING: function () {
            return {
                'block': {},
                'topPnl': {},
                'pnl': {},
                'pnl/phoneLogin': {onClicked: this.onphoneLoginClick},
                'pnl/wxLogin': {onClicked: this.onwxLoginClick},
                'pnl/agreeBtn': {onClicked: this.onagreeBtnClick},
                'pnl/userAgreeBtn': {onClicked: this.onUserAgreeClick},
                'userAgreeTopPnl':{},
                'userAgreeBmPnl':{},
                'userAgreeBmPnl/userAgreeList':{},
                'userAgreeBmPnl/cellPnl':{},
                'userAgreeBmPnl/cellPnl/titleCell':{},
                'userAgreeBmPnl/cellPnl/contentCell':{},
                'userAgreeTopPnl/returnBtn':{onClicked: this.onCloseUserAgreeClick},
            }
        },

        ctor: function (data) {
            this._super(ResConfig.View.LoginScene)
            this.registerMediator(new LoginMdt(this))

            this._viewData = data

            this.registerEventListener('GET_PHOTO_FROM_ALBUM', this.onPhotoSuccess)
            this.registerEventListener('GET_PHOTO_UPLOADPIC', this.onUpLoadPhotoSuccess)
            this.registerEventListener('PIC_SIZE_WARNING', this.onSizeWarning)
            this.registerEventListener('THIRD_LOGIN_RESULT', this.onThirdLogin)
            this.registerEventListener('installParam', this.onInstallParam)

            this.uploadUrl = [
                'http://download.jxlwgame.com/weixin/uploadimg',
                'http://download.jxlwgame.com/weixin/uploadimg1'
            ]
        },

        initView: function () {
            this.userAgreeList.setScrollBarEnabled(false)

            this.block.setVisible(true)
            this.topPnl.setVisible(true)
            this.pnl.setVisible(true)

            this.userAgreeTopPnl.setVisible(false)
            this.userAgreeBmPnl.setVisible(false)

            this.cellPnl.setVisible(false)

            this.contentCell.setVisible(false)
            if (this._viewData) {
                if (this._viewData.sayTxt) {
                    appInstance.gameAgent().Tips(this._viewData.sayTxt)
                }
            }


            // this.goTest()
        },

        onCloseUserAgreeClick: function () {
            this.block.setVisible(true)
            this.topPnl.setVisible(true)
            this.pnl.setVisible(true)
            this.userAgreeTopPnl.setVisible(false)
            this.userAgreeBmPnl.setVisible(false)
        },

        onUserAgreeClick: function (sender) {
            GameUtil.delayBtn(sender)
            this.topPnl.setVisible(false)
            this.userAgreeTopPnl.setVisible(true)
            this.userAgreeBmPnl.setVisible(true)
            if(this.userAgreeList.getChildrenCount() == 0)
                this.onInitUserAgreePnl()


        },

        onInitUserAgreePnl: function () {
            for (let i = 0; i < this._userAgreeContent.length; i++) {
                this.onAgreeCellView(i);
            }
        },

        onAgreeCellView: function (i) {
            let content = this._userAgreeContent[i]
            if(!content)
                return
            let titleCell = this.titleCell.clone()
            titleCell.setVisible(true)
            titleCell.getChildByName('titleImg').getChildByName('titleText').setString(content.title)
            this.userAgreeList.pushBackCustomItem(titleCell)


            let forMatLength = 42
            let txtSize = 26
            let contentText = content.content

            let returnMsg = this.onForMatTxt(contentText,forMatLength)
            let forMatTxt = returnMsg.forMatTxt
            let line = returnMsg.lineNum

            let contentCell = this.contentCell.clone()
            contentCell.setVisible(true)
            let size = contentCell.getContentSize()
            size.height = txtSize * line
            contentCell.setContentSize(size)

            let contentTxtSize = contentCell.getChildByName('content').getContentSize()
            contentTxtSize.height = txtSize * line
            contentCell.getChildByName('content').setContentSize(contentTxtSize)
            contentCell.getChildByName('content').setString(forMatTxt)
            contentCell.getChildByName('content').setPositionY(contentTxtSize.height/2)
            this.userAgreeList.pushBackCustomItem(contentCell)

        },

        onForMatTxt: function (txt,forMatLength) {

            let forMatTxt = ''
            let cnt = 0
            let lineNum = 0
            for(let i = 0; i < txt.length; i++){
                let addTxt = ''
                if(i !== 0 && cnt === forMatLength){
                    addTxt = txt[i] + '\n'
                    cnt = 0
                    lineNum += 1
                }else{
                    if((i + 1 < txt.length) && txt[i] + txt[i + 1] == '换行'){
                        i += 1
                        addTxt = '\n'
                        cnt = 0
                        lineNum += 1
                    }else{
                        addTxt = txt[i]
                        cnt++
                        if(i == txt.length - 1)
                            lineNum += 1
                    }

                }
                forMatTxt += addTxt
            }

            let returnMsg = {
                forMatTxt: forMatTxt,
                lineNum: lineNum + 1
            }
            return returnMsg
        },

        /**
         * 一键登录
         */
        onphoneLoginClick: function () {
            GameUtil.delayBtns(this._delayBtns,5)
            if(!this.onCheckeCanLogin()) return

            //判断当前系统
            if (cc.sys.OS_WINDOWS === cc.sys.os) {
                this.debugLogin()
            } else {
                appInstance.nativeApi().oneClickLogin()
            }
        },

        onCheckeCanLogin: function () {

            let flag = true
            if(!this._canLogin){
                appInstance.gameAgent().Tips('请同意《用户协议》！')
                flag = false
            }
            return flag
        },

        debugLogin: function () {
            let msg = {}
            let imeiStr = global.localStorage.getStringForKey(LocalSave.LocalImei)
            if (!imeiStr) {
                imeiStr = 'windows imei random' + Math.floor(Math.random() * 1000000)
                global.localStorage.setStringForKey(LocalSave.LocalImei, imeiStr)
            }
            msg.imei = imeiStr
            appInstance.gameAgent().httpGame().httpLogin(msg)
        },

        onwxLoginClick: function () {
            GameUtil.delayBtns(this._delayBtns,5)
            if(!this.onCheckeCanLogin())
                return

            if (cc.sys.OS_WINDOWS === cc.sys.os) {
                this.debugLogin()
            } else {
                appInstance.nativeApi().wxLogin()
            }

        },

        doPhoto: function () {
            appInstance.nativeApi().getPictureFromPhoneAlbum('www.baidu.com', 'abc')
        },

        goTest: function () {
            let MjPlayScene = include('module/mahjong/ui/MjPlayScene')
            appInstance.sceneManager().replaceScene(new MjPlayScene())
            // let TurnTableLayer = include('game/ui/layer/turntable/TurnTableLayer')
            // let TurnTableLayerUI = appInstance.uiManager().createPopUI(TurnTableLayer)
            // appInstance.sceneManager().getCurScene().addChild(TurnTableLayerUI)
        },

        onagreeBtnClick: function () {

            if(this._canLogin)
                this._canLogin = false
            else
                this._canLogin = true
        },


        goChooseCity: function () {
            appInstance.gameAgent().addPopUI(ResConfig.Ui.ChooseCityLayer)
        },


        onBtnSendPhoto: function () {
            let pInfo = appInstance.dataManager().getUserData()
            if (pInfo && pInfo.accessToken) {
                appInstance.nativeApi().getPictureFromPhoneAlbum(this.uploadUrl[type], pInfo.accessToken)
            }
        },

        onPhotoSuccess: function (msg) {
            this.photoPath = msg.photoPath
            let pInfo = appInstance.dataManager().getUserData()
            cc.log('=========1111msg.photoPath' + msg.photoPath)
            cc.log('========1111this.uploadUrl[this.sIndex]' + this.uploadUrl[0])
            cc.log('=======111pInfo.accessToken' + pInfo.accessToken)
            appInstance.nativeApi().uploadPic(msg.photoPath, this.uploadUrl[0], 'GET_PHOTO_UPLOADPIC', 'abc')
        },

        onUpLoadPhotoSuccess: function (msg) {
            // utils.View.unblock()
            cc.log('-- getPictureFromPhone -- GET_PHOTO_UPLOADPIC -- d  = ' + JSON.stringify(msg))
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                msg = JSON.parse(msg)
            }
            if (cc.sys.OS_IOS === cc.sys.os) {
                msg = JSON.parse(JSON.stringify(msg))
            }
            if (msg && parseInt(msg.errno) === 0) {
                // utils.showMsg('图片上传成功！')
                this.refreshPhoto(this.sIndex, msg.payimg, this.photoPath)
            } else {
                // utils.showMsg('图片上传失败，请重试！')
            }
        },
        onThirdLogin: function (msg) {
            cc.log('======onThirdLogin=======' + JSON.stringify(msg))
            //收到登录返回事件，接触锁定
            for(let i = 0; i < this._delayBtns.length; i++){
                this._delayBtns[i].setTouchEnabled(true)
            }
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                msg.imei = appInstance.nativeApi().getImei()
                appInstance.gameAgent().httpGame().httpLogin(msg)
            }
            if (cc.sys.OS_IOS === cc.sys.os) {
                msg = JSON.parse(msg)
                if (msg.code === 1) {
                    cc.log('======授权成功=======' + msg.code)
                    msg.imei = appInstance.nativeApi().getImei()
                    appInstance.gameAgent().httpGame().httpLogin(msg)
                } else {
                    cc.log('======授权失败=======' + msg.code)
                }
            }
        },
        onInstallParam: function (msg) {
            cc.log('======onInstallParam=======' + JSON.stringify(msg))
            if (cc.sys.OS_ANDROID === cc.sys.os) {
                cc.sys.localStorage.setItem("installParam", msg);
                let myParam = cc.sys.localStorage.getItem("installParam");
                cc.log('======onInstallParam=======' + JSON.stringify(myParam))


            }
            if (cc.sys.OS_IOS === cc.sys.os) {

            }
        },

        onSizeWarning: function (msg) {
            appInstance.gameAgent().Tips('图片较大！请上传1M以下图片，谢谢')
        },

        onEnter: function () {
            this._super()
            this.initData()
            this.initView()
            this.showView()
        },

        onExit: function () {
            this._super()
        },

        initData: function () {
            this._delayBtns = []
            appInstance.nativeApi().getInstallParam()
            appInstance.audioManager().playMusic(ResConfig.Sound.bg1, true)
            this._delayBtns.push(this.phoneLogin)
            this._delayBtns.push(this.wxLogin)

        },

        showView: function () {
        }
    })

    return LoginScene
})

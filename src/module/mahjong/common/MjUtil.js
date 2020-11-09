
load('module/mahjong/common/MjUtil', function () {
    let ResConfig = include('module/mahjong/common/ResConfig')
    let MjUtil  = cc.Class.extend({
        ctor: function () {
        },

        sortCard: function (cardArray) {
            cardArray.sort(function (a, b) {
                if ( a.nCardColor === b.nCardColor ) {
                    return a.nCardNumber < b.nCardNumber
                } else {
                    return a.nCardColor < b.nCardColor
                }
            })
            return cardArray
        },
        removeCard: function (cardArray, card) {
            // for (let i = 0; i < cardArray.length; ++i) {
            for (let i = cardArray.length - 1; i > -1 ; --i) {
                if (cardArray[i].nCardNumber === card.nCardNumber && cardArray[i].nCardColor === card.nCardColor) {
                    cardArray.splice(i, 1)
                    return
                }
            }
        },

        putCardSound: function (card) {
            let effectType = appInstance.gameAgent().gameUtil().getLocalLanguage()
            let SEX = [
                'man',
                'woman'
            ]
            let sexStr = SEX[appInstance.dataManager().getPlayData().getSelfInfo().sex]
            let soundPath = ResConfig.Sound.path + effectType + '/' + sexStr + '/card/'
            let COLOR = [
                'wan',
                'tong',
                'tiao',
                'feng'
            ]
            let cardColor = card.nCardColor
            let cardNumber = card.nCardNumber

            if (cardColor > 2) {
                cardNumber = cardColor - 2
                cardColor = 3
            }
            soundPath += COLOR[cardColor]
            soundPath += '_'
            soundPath += cardNumber
            soundPath += '.mp3'
            appInstance.audioManager().playEffect(soundPath)
        },

        playGamingSound: function ( soundArray ) {
            if (!soundArray || !soundArray.length) {
                return
            }
            let effectType = appInstance.gameAgent().gameUtil().getLocalLanguage()
            let SEX = [
                'man',
                'woman'
            ]
            let sexStr = SEX[appInstance.dataManager().getPlayData().getSelfInfo().sex]
            let soundPath = ResConfig.Sound.path + effectType + '/' + sexStr + '/gaming/'

            let index = Math.floor(Math.random() * (soundArray.length))
            soundPath += soundArray[index]
            soundPath += '.mp3'
            appInstance.audioManager().playEffect(soundPath)
        },

        isCardInArray: function (cardArray, card) {
            for (let i = 0; i < cardArray.length; ++i) {
                if (cardArray[i].nCardColor === card.nCardColor && cardArray[i].nCardNumber === card.nCardNumber) {
                    return true
                }
            }
            return false
        },
        getCardValueImg: function (uiSeat, cardType, cards) {
            let img = 'res/module/mahjong/card/value/'
            img += cardType
            img += '/'

            let CARDTYPE = {
                'selfhand': '_hand_'
            }

            let COLOR = [
                'wan',
                'tong',
                'tiao',
                'feng'
            ]
            let cardColor = cards.nCardColor
            let cardNumber = cards.nCardNumber

            if (cardColor > 2) {
                cardNumber = cardColor - 2
                cardColor = 3
            }

            switch (uiSeat) {
                case 0:
                    img += COLOR[cardColor]
                    img += CARDTYPE[cardType]
                    img += cardNumber
                    img += '.png'
                    break
                case 1:
                    break
                case 2:
                    break
                case 3:
                    break
            }
            return img
        }
    })
    return MjUtil
})

load('module/mahjong/common/MjUtil', function () {
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
        },
        removeCard: function (cardArray, card) {
            for (let i = 0; i < cardArray.length; ++i) {
                if (cardArray[i].nCardNumber === card.nCardNumber && cardArray[i].nCardColor === card.nCardColor) {
                    cardArray.splice(i, 1)
                    return
                }
            }
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
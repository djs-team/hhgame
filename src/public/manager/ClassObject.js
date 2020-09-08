/**
 *  es5 下的不定参数的变相解析，为了方便封装
 *  (暂时只支持8个参数)
 */
load('public/manager/ClassObject', function () {
    let classObject = {
        MAX_ARG_NUMBERS: 8,
        createObjByArgs: function (args) {
            if (args.length > 0) {
                let ObjProto = args.shift()
                let func = this['createObjByArgs_' + args.length]
                if (func) {
                    return func(ObjProto, args)
                }
            }
        },
        createObjByArgs_0: function (ObjProto) {
            return new ObjProto()
        },
        createObjByArgs_1: function (ObjProto, args) {
            return new ObjProto(args[0])
        },
        createObjByArgs_2: function (ObjProto, args) {
            return new ObjProto(args[0], args[1])
        },
        createObjByArgs_3: function (ObjProto, args) {
            return new ObjProto(args[0], args[1], args[2])
        },
        createObjByArgs_4: function (ObjProto, args) {
            return new ObjProto(args[0], args[1], args[2], args[3])
        },
        createObjByArgs_5: function (ObjProto, args) {
            return new ObjProto(args[0], args[1], args[2], args[3], args[4])
        },
        createObjByArgs_6: function (ObjProto, args) {
            return new ObjProto(args[0], args[1], args[2], args[3], args[4], args[5])
        },
        createObjByArgs_7: function (ObjProto, args) {
            return new ObjProto(args[0], args[1], args[2], args[3], args[4], args[5], args[6])
        },
        createObjByArgs_8: function (ObjProto, args) {
            return new ObjProto(args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7])
        }
    }

    return classObject
})

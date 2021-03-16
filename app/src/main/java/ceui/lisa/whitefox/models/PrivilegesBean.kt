package ceui.lisa.whitefox.models

class PrivilegesBean {
    /**
     * id : 1443634040
     * fee : 0
     * payed : 0
     * realPayed : 0
     * st : 0
     * pl : 320000
     * dl : 999000
     * sp : 7
     * cp : 1
     * subp : 1
     * cs : false
     * maxbr : 999000
     * fl : 320000
     * pc : null
     * toast : false
     * flag : 128
     * paidBigBang : false
     * preSell : false
     * playMaxbr : 999000
     * downloadMaxbr : 999000
     * freeTrialPrivilege : {"resConsumable":false,"userConsumable":false}
     * chargeInfoList : [{"rate":128000,"chargeUrl":null,"chargeMessage":null,"chargeType":0},{"rate":192000,"chargeUrl":null,"chargeMessage":null,"chargeType":0},{"rate":320000,"chargeUrl":null,"chargeMessage":null,"chargeType":0},{"rate":999000,"chargeUrl":null,"chargeMessage":null,"chargeType":1}]
     */
    var id: Long? = null
    var fee: Int? = null
    var payed: Int? = null
    var realPayed: Int? = null
    var st: Int? = null
    var pl: Int? = null
    var dl: Int? = null
    var sp: Int? = null
    var cp: Int? = null
    var subp: Int? = null
    var cs: Boolean? = null
    var maxbr: Int? = null
    var fl: Int? = null
    var pc: Any? = null
    var toast: Boolean? = null
    var flag: Int? = null
    var paidBigBang: Boolean? = null
    var preSell: Boolean? = null
    var playMaxbr: Int? = null
    var downloadMaxbr: Int? = null
    var freeTrialPrivilege: FreeTrialPrivilegeBean? = null
    var chargeInfoList: List<ChargeInfoListBean>? = null

    class FreeTrialPrivilegeBean {
        /**
         * resConsumable : false
         * userConsumable : false
         */
        var resConsumable: Boolean? = null
        var userConsumable: Boolean? = null
    }

    class ChargeInfoListBean {
        /**
         * rate : 128000
         * chargeUrl : null
         * chargeMessage : null
         * chargeType : 0
         */
        var rate: Int? = null
        var chargeUrl: Any? = null
        var chargeMessage: Any? = null
        var chargeType: Int? = null
    }
}
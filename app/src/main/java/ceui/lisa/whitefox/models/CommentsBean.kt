package ceui.lisa.whitefox.models

class CommentsBean {
    /**
     * user : {"locationInfo":null,"liveInfo":null,"anonym":0,"vipType":10,"nickname":"Aimertia-iko","avatarUrl":"https://p4.music.126.net/jGj0vkahAvHu2gqrHcdvkA==/18526770929949905.jpg","remarkName":null,"experts":null,"authStatus":0,"vipRights":{"associator":null,"musicPackage":{"vipCode":220,"rights":true},"redVipAnnualCount":-1,"redVipLevel":1},"expertTags":null,"avatarDetail":null,"userType":0,"userId":481495105}
     * beReplied : [{"user":{"locationInfo":null,"liveInfo":null,"anonym":0,"vipType":0,"nickname":"叶小麟-","avatarUrl":"https://p4.music.126.net/yjx_ok4bPCvcee3ZjYxKUg==/109951163562438859.jpg","remarkName":null,"experts":null,"authStatus":0,"vipRights":null,"expertTags":null,"avatarDetail":null,"userType":0,"userId":302767858},"beRepliedCommentId":1622833642,"content":"能推到这首歌的，歌单里歌手日向文   cinnamons  cannapeco 真夜中  ハンバート ハンバート 肯定有其中一个\u2026喜欢这种风格[奸笑]","status":0,"expressionUrl":null}]
     * pendantData : null
     * showFloorComment : null
     * status : 0
     * commentId : 5269090783
     * content : 4个~~
     * time : 1616416992335
     * likedCount : 0
     * expressionUrl : null
     * commentLocationType : 0
     * parentCommentId : 1622833642
     * decoration : {}
     * repliedMark : null
     * liked : false
     */
    var user: UserBean? = null
    var beReplied: List<BeRepliedBean>? = null
    var pendantData: Any? = null
    var showFloorComment: Any? = null
    var status: Int? = null
    var commentId: Long? = null
    var content: String? = null
    var time: Long? = null
    var likedCount: Int? = null
    var expressionUrl: Any? = null
    var commentLocationType: Int? = null
    var parentCommentId: Int? = null
    var decoration: DecorationBean? = null
    var repliedMark: Any? = null
    var liked: Boolean? = null

    class UserBean {
        class VipRightsBean {
            class MusicPackageBean
        }
    }

    class DecorationBean
}
package ceui.lisa.whitefox.models

class OriginSongSimpleDataBean {
    /**
     * songId : 1439814454
     * name : 我的悲伤是水做的
     * artists : [{"id":34477557,"name":"ChiliChill"},{"id":906118,"name":"洛天依"}]
     * albumMeta : {"id":87893694,"name":"我的悲伤是水做的"}
     */
    var songId: Int? = null
    var name: String? = null
    var artists: List<ArBean>? = null
    var albumMeta: AlbumMetaBean? = null
}
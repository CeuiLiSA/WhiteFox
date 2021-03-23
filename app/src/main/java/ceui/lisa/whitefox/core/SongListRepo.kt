package ceui.lisa.whitefox.core

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.models.ListPlayListResponse
import ceui.lisa.whitefox.models.ListSongResponse
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class SongListRepo : RemoteData<Song>() {

    var id = 0L

    override fun initApi(): Observable<out ListShow<Song>> {
        return RxHttp.get("http://192.243.123.124:3000/playlist/detail?id=$id")
            .add("uid", App.user.account!!.id)
            .asClass(ListSongResponse::class.java)
    }
}
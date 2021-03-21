package ceui.lisa.whitefox.core

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.models.ListPlayListResponse
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class PlayListRepo : RemoteData<PlaylistBean>() {

    override fun initApi(): Observable<out ListShow<PlaylistBean>> {
        return RxHttp.get("http://192.243.123.124:3000/user/playlist")
            .add("uid", App.user.account!!.id)
            .asClass(ListPlayListResponse::class.java)
    }
}
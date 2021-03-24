package ceui.lisa.whitefox.ui.recmd

import ceui.lisa.whitefox.core.RemoteData
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.models.RecmdPlayListResponse
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import rxhttp.RxHttp

class RecmdPlaylistsRepository : RemoteData<PlaylistBean>() {

    override fun initApi(): Observable<out ListShow<PlaylistBean>> {
        return RxHttp.get("http://192.243.123.124:3000/recommend/resource")
                .asClass(RecmdPlayListResponse::class.java)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
package ceui.lisa.whitefox.ui.recmd

import ceui.lisa.whitefox.core.RemoteData
import ceui.lisa.whitefox.models.DailySongList
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import rxhttp.RxHttp

class RecmdSongsRepository : RemoteData<Song>() {

    override fun initApi(): Observable<out ListShow<Song>> {
        return RxHttp.get("http://192.243.123.124:3000/recommend/songs")
                .asClass(DailySongList::class.java)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
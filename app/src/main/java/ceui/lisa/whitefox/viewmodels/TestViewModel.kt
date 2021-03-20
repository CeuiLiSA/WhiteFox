package ceui.lisa.whitefox.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.models.ListPlayListResponse
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.models.Song
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import rxhttp.RxHttp

class TestViewModel: ViewModel() {

    var isloaded = false
    var list1: MutableList<PlaylistBean> = ArrayList()
    var list2: MutableList<PlaylistBean> = ArrayList()
    val liveData1: MutableLiveData<MutableList<PlaylistBean>> = MutableLiveData()
    val liveData2: MutableLiveData<MutableList<PlaylistBean>> = MutableLiveData()

    init {
        liveData1.value = list1
        liveData2.value = list2
    }

    fun load() {
        if (isloaded) {
            return
        }

        RxHttp.get("http://192.243.123.124:3000/user/playlist")
            .add("uid", App.user.account!!.id)
            .asClass(ListPlayListResponse::class.java)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isloaded = true
                list1.addAll(it.playlist!!)
                liveData1.value = list1
            }

        RxHttp.get("http://192.243.123.124:3000/user/playlist")
            .add("uid", App.user.account!!.id)
            .asClass(ListPlayListResponse::class.java)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                isloaded = true
                list2.addAll(it.playlist!!)
                liveData2.value = list2
            }
    }
}
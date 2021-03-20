package ceui.lisa.whitefox.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.collections.ArrayList

abstract class ListViewModel<Bean> : ViewModel() {
    // TODO: Implement the ViewModel

    var playList: MutableList<Bean> = ArrayList()
    val liveData: MutableLiveData<MutableList<Bean>> = MutableLiveData()
    var loadResult: MutableLiveData<Int> = MutableLiveData()

    init {
        liveData.value = playList
        loadResult.value = -1
    }

    fun loadFirst() {
        Log.d("trace ", "loadFirst")
        initApi().subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                it.getListData()?.let { list ->
                    if (list.isNotEmpty()) {
                        playList.addAll(list)
                        liveData.value = playList
                    }
                }
                loadResult.value = 1
            }, {
                loadResult.value = 2
                it.printStackTrace()
            })
    }

    fun loadNext() {

    }

    abstract fun initApi(): Observable<out ListShow<Bean>>
}
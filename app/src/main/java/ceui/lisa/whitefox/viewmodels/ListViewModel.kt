package ceui.lisa.whitefox.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ceui.lisa.whitefox.core.Repository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.collections.ArrayList

abstract class ListViewModel<Bean> : ViewModel() {

    val playList: MutableList<Bean> = ArrayList()
    val liveData: MutableLiveData<MutableList<Bean>> = MutableLiveData()
    val loadResult: MutableLiveData<Int> = MutableLiveData()
    val hasMore: MutableLiveData<Boolean> = MutableLiveData()
    var isLoaded = false
    val repository = loadRepo()


    fun loadData(isRefresh: Boolean) {
        Log.d("traceFragmentList", "loadFirst")

        if (isRefresh) {
            repository.pageNo = 1
            repository.initApi().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        isLoaded = true
                        repository.pageNo++
                        repository.onResponse(it)
                        it.getListData()?.let { list ->
                            if (list.isNotEmpty()) {
                                playList.addAll(list)
                                liveData.value = playList
                            }
                        }
                        loadResult.value = 1
                        hasMore.value = it.hasMore()
                    }, {
                        loadResult.value = 2
                        it.printStackTrace()
                    })
        } else {
            repository.initApi().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        repository.onResponse(it)
                        repository.pageNo++
                        it.getListData()?.let { list ->
                            if (list.isNotEmpty()) {
                                playList.addAll(list)
                                liveData.value = playList
                            }
                        }
                        loadResult.value = 3
                        hasMore.value = it.hasMore()
                    }, {
                        loadResult.value = 4
                        it.printStackTrace()
                    })
        }
    }

    abstract fun loadRepo(): Repository<Bean>
}
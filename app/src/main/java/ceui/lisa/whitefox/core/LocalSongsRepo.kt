package ceui.lisa.whitefox.core

import android.util.Log
import ceui.lisa.whitefox.cache.LocalFile.getParentFile
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import java.util.*

class LocalSongsRepo : LocalData<String>() {

    override fun initApi(): Observable<out ListShow<String>> {
        return Observable.create<ListShow<String>>(ObservableOnSubscribe<ListShow<String>> { emitter ->
            Log.d("LocalData", "LocalSongsRepo")
            val parent = getParentFile()
            val result: MutableList<String> = ArrayList()
            for (file in parent.listFiles()) {
                result.add(file.name)
            }
            val listShow: ListShow<String> = object : ListShow<String> {
                override fun getListData(): List<String>? {
                    return result
                }
            }
            emitter.onNext(listShow)
        })
    }
}
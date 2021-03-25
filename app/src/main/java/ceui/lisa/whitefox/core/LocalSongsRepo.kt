package ceui.lisa.whitefox.core

import android.util.Log
import ceui.lisa.whitefox.cache.LocalFile.getParentFile
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import java.io.File
import java.util.*

class LocalSongsRepo : LocalData<File>() {

    override fun initApi(): Observable<out ListShow<File>> {
        return Observable.create { emitter ->
            Log.d("LocalData", "LocalSongsRepo")
            val parent = getParentFile()
            val result: MutableList<File> = ArrayList()
            val tempList = parent.listFiles()
            if (tempList != null) {
                for (listFile in tempList) {
                    result.add(listFile)
                }
                result.reverse()
            }
            val listShow: ListShow<File> = object : ListShow<File> {
                override fun getListData(): List<File> {
                    return result
                }

                override fun hasMore(): Boolean {
                    return false
                }
            }
            emitter.onNext(listShow)
        }
    }
}
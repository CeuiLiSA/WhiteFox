package ceui.lisa.whitefox.core

import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable

abstract class Repository<T> {

    abstract fun initApi(): Observable<out ListShow<T>>

    var pageNo = 1
    var pageSize = 20

    open fun <Response : ListShow<T>> onResponse(response: Response) {

    }
}
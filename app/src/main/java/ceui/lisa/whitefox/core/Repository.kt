package ceui.lisa.whitefox.core

import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable

abstract class Repository<T> {

    abstract fun initApi(): Observable<out ListShow<T>>
}
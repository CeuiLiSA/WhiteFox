package ceui.lisa.whitefox.ui.comment

import ceui.lisa.whitefox.core.RemoteData
import ceui.lisa.whitefox.models.*
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class CommentListRepo : RemoteData<CommentsBean>() {

    var id = 0L
    var sortType = 1

    override fun initApi(): Observable<out ListShow<CommentsBean>> {
        return RxHttp.get("http://192.243.123.124:3000/comment/new?type=0&sortType=$sortType&id=$id")
            .asClass(CommentResponse::class.java)
    }
}
package ceui.lisa.whitefox.core

import ceui.lisa.whitefox.App
import ceui.lisa.whitefox.models.*
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class CommentListRepo : RemoteData<CommentsBean>() {

    var id = 0L

    override fun initApi(): Observable<out ListShow<CommentsBean>> {
        return RxHttp.get("http://192.243.123.124:3000/comment/music?id=$id")
            .asClass(CommentResponse::class.java)
    }
}
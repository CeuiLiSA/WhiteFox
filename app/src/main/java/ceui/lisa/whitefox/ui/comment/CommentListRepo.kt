package ceui.lisa.whitefox.ui.comment

import ceui.lisa.whitefox.core.RemoteData
import ceui.lisa.whitefox.models.CommentResponse
import ceui.lisa.whitefox.models.CommentsBean
import ceui.lisa.whitefox.test.ListShow
import io.reactivex.rxjava3.core.Observable
import rxhttp.RxHttp

class CommentListRepo : RemoteData<CommentsBean>() {

    var id = 0L
    var sortType = 1
    var cursor = 0L
    var type = 0

    override fun initApi(): Observable<out ListShow<CommentsBean>> {
        if (sortType == 3) {
            return RxHttp.get(
                    "http://192.243.123.124:3000/comment/new")
                    .add("type", type)
                    .add("pageNo", pageNo)
                    .add("pageSize", pageSize)
                    .add("sortType", sortType)
                    .add("id", id)
                    .add("cursor", cursor)
                    .asClass(CommentResponse::class.java)
        } else {
            return RxHttp.get(
                    "http://192.243.123.124:3000/comment/new")
                    .add("type", type)
                    .add("pageNo", pageNo)
                    .add("pageSize", pageSize)
                    .add("sortType", sortType)
                    .add("id", id)
                    .asClass(CommentResponse::class.java)
        }
    }

    override fun <Response : ListShow<CommentsBean>> onResponse(response: Response) {
        super.onResponse(response)
        if (response is CommentResponse) {
            cursor = (response as CommentResponse).data!!.cursor!!
        }
    }
}
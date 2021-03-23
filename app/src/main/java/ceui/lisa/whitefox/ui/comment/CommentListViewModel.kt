package ceui.lisa.whitefox.ui.comment

import ceui.lisa.whitefox.core.Repository
import ceui.lisa.whitefox.models.CommentsBean
import ceui.lisa.whitefox.viewmodels.ListViewModel

class CommentListViewModel: ListViewModel<CommentsBean>() {

    override fun loadRepo(): Repository<CommentsBean> {
        return CommentListRepo()
    }

    fun setID(id: Long) {
        (repository as CommentListRepo).id = id
    }

    fun setSortType(type: Int) {
        (repository as CommentListRepo).sortType = type
    }

    fun getSortType(): Int {
        return (repository as CommentListRepo).sortType
    }
}
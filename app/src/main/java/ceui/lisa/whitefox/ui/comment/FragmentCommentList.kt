package ceui.lisa.whitefox.ui.comment

import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.CommentListAdapter
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.CommentsBean
import ceui.lisa.whitefox.test.FragmentList
import ceui.lisa.whitefox.viewmodels.ListViewModel

class FragmentCommentList : FragmentList<CommentsBean>() {

    override fun modelClass(): Class<out ListViewModel<CommentsBean>> {
        return CommentListViewModel::class.java
    }

    override fun onViewModelCreated() {
        val id = requireActivity().intent.getLongExtra("songID", 0L)
        (mViewModel as CommentListViewModel).setID(id)
    }

    override fun initAdapter(): BaseAdapter<CommentsBean, RecySongItemBinding> {
        return CommentListAdapter(mViewModel.playList)
    }
}
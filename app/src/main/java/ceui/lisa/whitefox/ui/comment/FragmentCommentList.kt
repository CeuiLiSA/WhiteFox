package ceui.lisa.whitefox.ui.comment

import android.os.Bundle
import android.view.View
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.CommentListAdapter
import ceui.lisa.whitefox.core.OnCheckChangeListener
import ceui.lisa.whitefox.databinding.FragmentCommentListBinding
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.CommentsBean
import ceui.lisa.whitefox.ui.base.BindFragmentList
import ceui.lisa.whitefox.viewmodels.ListViewModel

class FragmentCommentList : BindFragmentList<FragmentCommentListBinding, CommentsBean>() {

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

    override fun layout(): Int {
        return R.layout.fragment_comment_list
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        baseBind.glareLayout.check((mViewModel as CommentListViewModel).getSortType() - 1)
        baseBind.glareLayout.listener = object : OnCheckChangeListener {
            override fun onSelect(index: Int, view: View?) {
                (mViewModel as CommentListViewModel).setSortType(index + 1)
                baseBind.smartRefreshLayout.autoRefresh()
            }

            override fun onReselect(index: Int, view: View?) {
            }
        }
    }
}
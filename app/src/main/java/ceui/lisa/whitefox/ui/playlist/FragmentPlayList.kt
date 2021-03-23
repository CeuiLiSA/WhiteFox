package ceui.lisa.whitefox.ui.playlist

import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.PlayListAdapter
import ceui.lisa.whitefox.databinding.RecyPlaylistItemBinding
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.ui.base.FragmentList
import ceui.lisa.whitefox.viewmodels.ListViewModel

class FragmentPlayList: FragmentList<PlaylistBean>() {

    override fun modelClass(): Class<out ListViewModel<PlaylistBean>> {
        return PlayListViewModel::class.java
    }

    override fun initAdapter(): BaseAdapter<PlaylistBean, RecyPlaylistItemBinding> {
        return PlayListAdapter(mViewModel.playList)
    }
}
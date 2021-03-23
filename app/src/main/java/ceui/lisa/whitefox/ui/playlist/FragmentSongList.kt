package ceui.lisa.whitefox.ui.playlist

import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.SongListAdapter
import ceui.lisa.whitefox.databinding.RecySongItemBinding
import ceui.lisa.whitefox.models.Song
import ceui.lisa.whitefox.test.FragmentList
import ceui.lisa.whitefox.viewmodels.ListViewModel
import ceui.lisa.whitefox.viewmodels.SongListViewModel

class FragmentSongList : FragmentList<Song>() {

    override fun modelClass(): Class<out ListViewModel<Song>> {
        return SongListViewModel::class.java
    }

    override fun onViewModelCreated() {
        val id = requireActivity().intent.getLongExtra("playlistID", 0L)
        (mViewModel as SongListViewModel).setID(id)
    }

    override fun initAdapter(): BaseAdapter<Song, RecySongItemBinding> {
        return SongListAdapter(mViewModel.playList)
    }
}
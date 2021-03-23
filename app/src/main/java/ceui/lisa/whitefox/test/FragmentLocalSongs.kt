package ceui.lisa.whitefox.test

import androidx.databinding.ViewDataBinding
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.adapters.LocalSongsAdapter
import ceui.lisa.whitefox.viewmodels.ListViewModel
import ceui.lisa.whitefox.viewmodels.LocalSongsViewModel

class FragmentLocalSongs : FragmentList<String>() {

    override fun modelClass(): Class<out ListViewModel<String>> {
        return LocalSongsViewModel::class.java
    }

    override fun initAdapter(): BaseAdapter<String, out ViewDataBinding> {
        return LocalSongsAdapter(mViewModel.playList)
    }
}
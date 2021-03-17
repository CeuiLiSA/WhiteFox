package ceui.lisa.whitefox.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.adapters.BaseAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

abstract class FragmentList<Bean> : Fragment() {

    lateinit var mAdapter: BaseAdapter<Bean, out ViewDataBinding>
    lateinit var mViewModel: ListViewModel<Bean>

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    abstract fun modelClass(): Class<out ListViewModel<Bean>>
    abstract fun initApi(): Observable<out ListShow<Bean>>?

    abstract fun initAdapter(): BaseAdapter<Bean, out ViewDataBinding>

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(modelClass())

        val recylist = view?.findViewById<RecyclerView>(R.id.list)
        recylist?.layoutManager = LinearLayoutManager(requireContext())
        mAdapter = initAdapter()
        recylist?.adapter = mAdapter


        if (!mViewModel.isLoaded) {
            val api = initApi()
            if (api != null) {
                api.subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        mViewModel.isLoaded = true
                        it.getListData()?.let { list ->
                            if (list.isNotEmpty()) {
                                mViewModel.playList.addAll(list)
                                mAdapter.notifyDataSetChanged()
                            }
                        }
                    }
            } else {
                loadFromLocal()
            }
        }
    }

    open fun loadFromLocal() {

    }
}
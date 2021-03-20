package ceui.lisa.whitefox.test

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.viewmodels.ListViewModel
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator

abstract class FragmentList<Bean> : Fragment() {

    lateinit var mAdapter: BaseAdapter<Bean, out ViewDataBinding>
    lateinit var mViewModel: ListViewModel<Bean>
    var recyclerView: RecyclerView? = null
    var refreshLayout: SmartRefreshLayout? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_item_list, container, false)
    }

    abstract fun modelClass(): Class<out ListViewModel<Bean>>

    abstract fun initAdapter(): BaseAdapter<Bean, out ViewDataBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list)
        refreshLayout = view.findViewById(R.id.smart_refresh_layout)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(modelClass())
        onViewModelCreated()
        initView()
        mAdapter = initAdapter()
        onAdapterCreated()
        mViewModel.liveData.observe(this, object :Observer<MutableList<Bean>>{
            override fun onChanged(t: MutableList<Bean>) {
                Log.d("liveData list", "onChanged $t")
                if (mAdapter.itemCount < t.size) {
                    mAdapter.notifyItemRangeInserted(mAdapter.itemCount, t.size)
                } else {
                    mAdapter.notifyItemRangeRemoved(0, t.size)
                }
            }
        })
        mViewModel.loadResult.observe(this, object :Observer<Int>{
            override fun onChanged(t: Int?) {
                Log.d("liveData refresh", "onChanged $t")
                t?.let {
                    if (t == -1) {

                    } else if (t == 1) {
                        refreshLayout?.finishRefresh()
                    } else if (t == 2) {
                        refreshLayout?.finishRefresh(false)
                        ToastUtils.show("加载失败")
                    } else {

                    }
                }
            }
        })
        refreshLayout?.autoRefresh()
        Log.d("trace ", "autoRefresh")
    }

    open fun loadFromLocal() {

    }

    open fun onViewModelCreated() {
    }

    open fun initView() {
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        val baseItemAnimator = LandingAnimator()
        baseItemAnimator.addDuration = 400L
        baseItemAnimator.changeDuration = 400L
        baseItemAnimator.moveDuration = 400L
        baseItemAnimator.removeDuration = 400L
        recyclerView?.itemAnimator = baseItemAnimator
        refreshLayout?.setRefreshHeader(MaterialHeader(requireContext()))
        refreshLayout?.setOnRefreshListener {
            Log.d("trace ", "OnRefreshListener")
            mAdapter.clear()
            mViewModel.loadFirst()
        }
    }

    open fun onAdapterCreated() {
        recyclerView?.adapter = mAdapter
    }
}
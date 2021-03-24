package ceui.lisa.whitefox.ui.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ceui.lisa.whitefox.R
import ceui.lisa.whitefox.adapters.BaseAdapter
import ceui.lisa.whitefox.databinding.FragmentItemListBinding
import ceui.lisa.whitefox.viewmodels.ListViewModel
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.FalsifyFooter
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import jp.wasabeef.recyclerview.animators.LandingAnimator

abstract class FragmentList<Bean> : BaseFragment<FragmentItemListBinding>() {

    lateinit var mAdapter: BaseAdapter<Bean, out ViewDataBinding>
    lateinit var mViewModel: ListViewModel<Bean>
    var recyclerView: RecyclerView? = null
    var refreshLayout: SmartRefreshLayout? = null

    abstract fun modelClass(): Class<out ListViewModel<Bean>>

    abstract fun initAdapter(): BaseAdapter<Bean, out ViewDataBinding>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.list)
        refreshLayout = view.findViewById(R.id.smart_refresh_layout)
    }

    override fun initModel() {
        mViewModel = ViewModelProvider(requireActivity()).get(modelClass())
    }

    override fun onViewModelCreated() {
        mViewModel.liveData.observe(this, object :Observer<MutableList<Bean>>{
            override fun onChanged(t: MutableList<Bean>) {
                Log.d("liveData list", "onChanged ")
                mAdapter.notifyItemRangeInserted(mAdapter.itemCount, t.size)
            }
        })
        mViewModel.hasMore.observe(this, object : Observer<Boolean> {
            override fun onChanged(t: Boolean) {
                if (!t) {
                    refreshLayout?.setRefreshFooter(FalsifyFooter(requireContext()))
                }
            }
        })
        mViewModel.loadResult.observe(this, object :Observer<Int>{
            override fun onChanged(t: Int?) {
                Log.d("liveData refresh", "onChanged ")
                t?.let {
                    if (t == -1) {

                    } else if (t == 1) {
                        refreshLayout?.finishRefresh()
                    } else if (t == 2) {
                        refreshLayout?.finishRefresh(false)
                        ToastUtils.show("加载失败")
                    } else if (t == 3) {
                        refreshLayout?.finishLoadMore()
                    } else if (t == 4) {
                        refreshLayout?.finishLoadMore(false)
                        ToastUtils.show("加载失败")
                    } else {

                    }
                }
            }
        })

        mAdapter = initAdapter()
        recyclerView?.adapter = mAdapter
    }


    override fun initView() {
        recyclerView?.layoutManager = LinearLayoutManager(requireContext())
        val baseItemAnimator = LandingAnimator()
        baseItemAnimator.addDuration = 400L
        baseItemAnimator.changeDuration = 400L
        baseItemAnimator.moveDuration = 400L
        baseItemAnimator.removeDuration = 400L
        recyclerView?.itemAnimator = baseItemAnimator
        refreshLayout?.setRefreshHeader(MaterialHeader(requireContext()))
        refreshLayout?.setRefreshFooter(ClassicsFooter(requireContext()))
        refreshLayout?.setOnRefreshListener {
            Log.d("traceFragmentList", "OnRefreshListener")
            mAdapter.clear()
            mViewModel.loadData(true)
        }
        refreshLayout?.setOnLoadMoreListener {
            Log.d("trace ", "OnLoadMoreListener")
            mViewModel.loadData(false)
        }
    }

    override fun initData() {
        if (!mViewModel.isLoaded) {
            refreshLayout?.autoRefresh()
            Log.d("traceFragmentList", "autoRefresh")
        }
    }

    override fun layout(): Int {
        return R.layout.fragment_item_list
    }
}
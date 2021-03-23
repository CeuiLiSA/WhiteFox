package ceui.lisa.whitefox.test

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
import ceui.lisa.whitefox.adapters.PlayListAdapter
import ceui.lisa.whitefox.models.PlaylistBean
import ceui.lisa.whitefox.viewmodels.ListViewModel
import ceui.lisa.whitefox.viewmodels.TestViewModel
import com.hjq.toast.ToastUtils
import com.scwang.smart.refresh.header.MaterialHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import jp.wasabeef.recyclerview.animators.LandingAnimator

class FragmentTest : Fragment() {

    var list1: RecyclerView? = null
    var list2: RecyclerView? = null
    var adapter1: PlayListAdapter? = null
    var adapter2: PlayListAdapter? = null
    lateinit var mViewModel: TestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list1 = view.findViewById(R.id.list1)
        list1?.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        list2 = view.findViewById(R.id.list2)
        list2?.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(TestViewModel::class.java)
        adapter1 = PlayListAdapter(mViewModel.list1)
        list1?.adapter = adapter1
        adapter2 = PlayListAdapter(mViewModel.list2)
        list2?.adapter = adapter2
        mViewModel.liveData1.observe(this, Observer<List<PlaylistBean>> { list ->
            run {
                adapter1?.notifyDataSetChanged()
            }
        })
        mViewModel.liveData2.observe(this, Observer<List<PlaylistBean>> { list ->
            run {
                adapter2?.notifyDataSetChanged()
            }
        })
        mViewModel.load()
    }
}